package com.gsmc.png.quart.fetch;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.gsmc.png.model.AgData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.quart.thread.AGThread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.utils.DateUtil;

public class FetchAgDataJobService {
	
	private static Logger log = Logger.getLogger(FetchAgDataJobService.class);

	final CommonService commonService;

	public FetchAgDataJobService(CommonService commonService) {
		this.commonService = commonService;
	}


	public void execute() {
		try {
			log.info("定时器开始获取ag输赢数据...");
			long delta1 = new Date().getTime()-12*3600*1000L;//12小时时差
			long delta2 = delta1-8*60*1000L;
			String startTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss", delta2);//8分钟之前
			String endTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss", delta1);
			processStatusData(startTime, endTime);
			log.info("获取ag" + startTime + "至" + endTime + "输赢数据完毕。");
			
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
			List<AgData> AgDataList = processData(startTime,endTime);
			if(AgDataList != null){
				log.info(GamePlatform.AGIN.name() + "本次抓取到数据条数："+AgDataList.size());
				for (AgData AgData : AgDataList) {
					commonService.saveOrUpdate(AgData);
				}
			}else{
				log.info(GamePlatform.AGIN.name() + "本次抓取到数据条数：0");
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchAgDataJobService 失败 , message : " + e.getMessage());
		}
	}
	
	/**
	 * 给外部呼叫用. 会抛出Exceptioin.
	 * @throws ExecutionException
     */
	public synchronized List<AgData> processData(String startTime,String endTime) throws ExecutionException{
		try {
			Future<List<AgData>> processFuture = getProcessFuture(startTime,endTime);
			return processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchAgDataJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(),e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchAgDataJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}
	
	private Future<List<AgData>> getProcessFuture(String startTime,String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		AGThread thread = new AGThread(startTime,endTime);
		Future<List<AgData>> future = executorService.submit(thread);
		return future;
	}
	
	public static void main(String[] args) {
		FetchAgDataJobService ss = new FetchAgDataJobService(null);
		ss.execute();
	}

}
