package com.gsmc.png.quart.fetch;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.gsmc.png.model.PlatformData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.quart.thread.TTGThread;
import com.gsmc.png.service.interfaces.IGetdateService;
import com.gsmc.png.utils.DateUtil;

public class FetchTTGJobService {
	
	private static Logger log = Logger.getLogger(FetchTTGJobService.class);
	
	private IGetdateService getdateService;

	public static void main(String[] args) {
		FetchTTGJobService fetchTTGJobService=new FetchTTGJobService();
		Calendar instance = Calendar.getInstance();
		instance.setTime(new DateTime().withTimeAtStartOfDay().plusDays(0).minusSeconds(1).toDate());
		try {
			System.out.println(DateUtil.fmtyyyyMMddHHmmss(instance.getTime()));
			fetchTTGJobService.processStatusData(instance);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	public void execute(){
		try {
			log.info("计时器开始获取ttg输赢数据...");
			Calendar searchDate = Calendar.getInstance();//获取现在时间的TTG输赢数据
			processStatusData(searchDate);
			Checker checker = new Checker();
			if (checker.needsCoverUpMissPlatformDataYesterday(GamePlatform.TTG)) {
				searchDate.setTime(checker.getYesterdayDate(GamePlatform.TTG));
				log.info(String.format("获取 ttg 昨日 %s 输赢数据开始：", DateUtil.fmtyyyyMMddHHmmss(searchDate.getTime())));
				processStatusData(searchDate);
				log.info("获取 ttg 昨日输赢数据完毕。");
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 给quartz用 , 已经处理 Excpetion.
	 */
	public synchronized void processStatusData(Calendar searchDate) throws ExecutionException {
		try {
			List<PlatformData> platformDataList = processData(searchDate);
			log.info(GamePlatform.TTG.name() + "本地次抓取到有游戏输赢的玩家数量为："+platformDataList.size());
			for (PlatformData platformData : platformDataList) {
				getdateService.updateTTGPlatForm(platformData);
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchNTwoJobService 失败 , message : " + e.getMessage());
		}
	}
	
	/**
	 * 给外部呼叫用. 会抛出Exceptioin.
	 * @throws ExecutionException
     */
	public synchronized List<PlatformData> processData(Calendar searchDate) throws ExecutionException{
		try {
			Future<List<PlatformData>> processFuture = getProcessFuture(searchDate);
			return processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchTTGJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(),e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchTTGJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}
	
	private Future<List<PlatformData>> getProcessFuture(Calendar searchDate) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		TTGThread thread = new TTGThread(searchDate);
		Future<List<PlatformData>> future = executorService.submit(thread);
		return future;
	}
	
	public IGetdateService getGetdateService() {
		return getdateService;
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}
