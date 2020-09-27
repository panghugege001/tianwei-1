package com.gsmc.png.quart.fetch;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.gsmc.png.model.SwData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.quart.thread.SWThread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.utils.DateUtil;

public class FetchSwDataJobService {
	
	private static Logger log = Logger.getLogger(FetchSwDataJobService.class);

	final CommonService commonService;

	public FetchSwDataJobService(CommonService commonService) {
		this.commonService = commonService;
	}


	public void execute() {
		try {
			log.info("定时器开始获取sw输赢数据...");
			Calendar beforeTime = Calendar.getInstance();
			beforeTime.add(Calendar.MINUTE, -8);// 8分钟之前的时间
			Date beforeD = beforeTime.getTime();
			String startTime = DateUtil.fmtDate("yyyy-MM-dd HH:mm", beforeD) + ":00";
			String endTime = DateUtil.fmtDate("yyyy-MM-dd", new Date()) + " 23:59:59";
			processStatusData(startTime, endTime);
			log.info("获取 sw" + startTime + "至" + endTime + "输赢数据完毕。");
			
			Checker checker = new Checker();
			if (checker.needsCoverUpMissPlatformDataYesterday(GamePlatform.SW)) {
				log.info("获取 sw 昨日输赢数据开始");
				String date = DateUtil.getchangedDate(-1);
				processStatusData(date+" 00:00:00",date+" 23:59:59");
				log.info("获取 sw 昨日输赢数据完毕。");
			}
			
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	
	/**
	 * 给quartz用 , 已经处理 Excpetion.
	 */
	public synchronized void processStatusData(String startTime,String endTime) throws ExecutionException {
		try {
			List<SwData> swDataList = processData(startTime,endTime);
			if(swDataList != null){
				log.info(GamePlatform.SW.name() + "本次抓取到数据条数："+swDataList.size());
				for (SwData swData : swDataList) {
					commonService.saveOrUpdate(swData);
				}
			}else{
				log.info(GamePlatform.SW.name() + "本次抓取到数据条数：0");
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchSwDataJobService 失败 , message : " + e.getMessage());
		}
	}
	
	/**
	 * 给外部呼叫用. 会抛出Exceptioin.
	 * @throws ExecutionException
     */
	public synchronized List<SwData> processData(String startTime,String endTime) throws ExecutionException{
		try {
			Future<List<SwData>> processFuture = getProcessFuture(startTime,endTime);
			return processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchSwDataJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(),e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchSwDataJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}
	
	private Future<List<SwData>> getProcessFuture(String startTime,String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		SWThread thread = new SWThread(startTime,endTime);
		Future<List<SwData>> future = executorService.submit(thread);
		return future;
	}
	
	public static void main(String[] args) {
		FetchSwDataJobService ss = new FetchSwDataJobService(null);
		ss.execute();
	}

}
