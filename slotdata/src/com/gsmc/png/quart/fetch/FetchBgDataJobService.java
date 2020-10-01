package com.gsmc.png.quart.fetch;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

import com.gsmc.png.model.BgData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.quart.thread.BgLiveThread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.utils.DateUtil;

public class FetchBgDataJobService {

	private static Logger log = Logger.getLogger(FetchBgDataJobService.class);

	final CommonService commonService;

	public FetchBgDataJobService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void execute() {
		try {
			log.info("定时器开始获取bg输赢数据...");
			long delta1 = new Date().getTime();
			long delta2 = delta1 - 8 * 60 * 1000L;
			//8分钟前
			String startTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss.SSS", delta2);// 8分钟之前
			String endTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss.SSS", delta1);
			processStatusData(startTime, endTime);
			log.info("获取 bg" + startTime + "至" + endTime + "输赢数据完毕。");
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
			List<BgData> bgDataList = processData(startTime, endTime);
			if (org.springframework.util.CollectionUtils.isEmpty(bgDataList)) {
				log.info(GamePlatform.PG.name() + "本地次抓取到有游戏输赢的玩家数量为0");
			} else {
				log.info(GamePlatform.PG.name() + "本地次抓取到有游戏输赢的玩家数量为：" + bgDataList.size());
				for (BgData bgData : bgDataList) {
					commonService.saveOrUpdate(bgData);
				}
			}
			List<BgData> bgByDataList = byProcessData(startTime, endTime);
			if (org.springframework.util.CollectionUtils.isEmpty(bgByDataList)) {
				log.info(GamePlatform.PG.name() + "本地次抓取到捕鱼游戏输赢的玩家数量为0");
			} else {
				log.info(GamePlatform.PG.name() + "本地次抓取到捕鱼游戏输赢的玩家数量为：" + bgDataList.size());
				for (BgData bgData : bgByDataList) {
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
	public synchronized List<BgData> processData(String startTime, String endTime) throws ExecutionException {
		try {
			Future<List<BgData>> processFuture = getProcessFuture(startTime, endTime);
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

	private Future<List<BgData>> getProcessFuture(String startTime, String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
	    BgLiveThread thread = new BgLiveThread(startTime, endTime);
		Future<List<BgData>> future = executorService.submit(thread);
		return future;
	}
	
	/**
	 * 给外部呼叫用. 会抛出Exceptioin.
	 * 
	 * @throws ExecutionException
	 */
	public synchronized List<BgData> byProcessData(String startTime, String endTime) throws ExecutionException {
		try {
			Future<List<BgData>> processFuture = getByProcessFuture(startTime, endTime);
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

	private Future<List<BgData>> getByProcessFuture(String startTime, String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
	    BgLiveThread thread = new BgLiveThread(startTime, endTime);
		Future<List<BgData>> future = executorService.submit(thread);
		return future;
	}

}
