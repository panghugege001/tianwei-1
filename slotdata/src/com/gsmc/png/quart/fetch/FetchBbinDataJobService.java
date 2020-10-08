package com.gsmc.png.quart.fetch;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import com.gsmc.png.model.BbinData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.quart.thread.BBINThread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.utils.DateUtil;

public class FetchBbinDataJobService {
	
	private static Logger log = Logger.getLogger(FetchBbinDataJobService.class);

	final CommonService commonService;

	public FetchBbinDataJobService(CommonService commonService) {
		this.commonService = commonService;
	}


	public void execute() {
		try {
			log.info("定时器开始获取bbin输赢数据...");
			long delta1 = new Date().getTime();
			long delta2 = delta1 - 8 * 60 * 1000L;
			//8分钟前
			String startTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss.SSS", delta2);// 8分钟之前
			String endTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss.SSS", delta1);
			processStatusData(startTime, endTime);
			log.info("获取 bbin" + startTime + "至" + endTime + "输赢数据完毕。");
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 给quartz用 , 已经处理 Excpetion.
	 */
	public synchronized void processStatusData(String startTime, String endTime) throws ExecutionException {
		try {
			List<BbinData> bbinDataList = processData(startTime, endTime);
			if (org.springframework.util.CollectionUtils.isEmpty(bbinDataList)) {
				log.info(GamePlatform.BBIN.name() + "本地次抓取到有游戏输赢的玩家数量为0");
			} else {
				log.info(GamePlatform.BBIN.name() + "本地次抓取到有游戏输赢的玩家数量为：" + bbinDataList.size());
				for (BbinData bbinData : bbinDataList) {
					commonService.saveOrUpdate(bbinData);
				}
			}
			List<BbinData> bbinByDataList = byProcessData(startTime, endTime);
			if (org.springframework.util.CollectionUtils.isEmpty(bbinByDataList)) {
				log.info(GamePlatform.BBIN.name() + "本地次抓取到捕鱼游戏输赢的玩家数量为0");
			} else {
				log.info(GamePlatform.BBIN.name() + "本地次抓取到捕鱼游戏输赢的玩家数量为：" + bbinDataList.size());
				for (BbinData bgData : bbinByDataList) {
					commonService.saveOrUpdate(bgData);
				}
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchCq9DataJobService 失败 , message : " + e.getMessage());
		}
	}

	/**
	 * 给外部呼叫用. 会抛出Exceptioin.
	 * 
	 * @throws ExecutionException
	 */
	public synchronized List<BbinData> processData(String startTime, String endTime) throws ExecutionException {
		try {
			Future<List<BbinData>> processFuture = getProcessFuture(startTime, endTime);
			return processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchPgDataJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(), e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchPgDataJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}

	private Future<List<BbinData>> getProcessFuture(String startTime, String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		BBINThread thread = new BBINThread(startTime, endTime);
		Future<List<BbinData>> future = executorService.submit(thread);
		return future;
	}
	
	/**
	 * 给外部呼叫用. 会抛出Exceptioin.
	 * 
	 * @throws ExecutionException
	 */
	public synchronized List<BbinData> byProcessData(String startTime, String endTime) throws ExecutionException {
		try {
			Future<List<BbinData>> processFuture = getByProcessFuture(startTime, endTime);
			return processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchPgDataJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(), e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchPgDataJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}

	private Future<List<BbinData>> getByProcessFuture(String startTime, String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		BBINThread thread = new BBINThread(startTime, endTime);
		Future<List<BbinData>> future = executorService.submit(thread);
		return future;
	}
}
