package com.gsmc.png.quart.fetch;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import com.gsmc.png.model.PgData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.quart.thread.PGThread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.utils.DateUtil;

public class FetchPgDataJobService {

	private static Logger log = Logger.getLogger(FetchPgDataJobService.class);

	final CommonService commonService;

	public FetchPgDataJobService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void execute() {
		try {
			log.info("定时器开始获取pg输赢数据...");
			long delta1 = new Date().getTime();
			long delta2 = delta1 - 8 * 60 * 1000L;
			//8分钟前
			String startTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss.SSS", delta2);// 8分钟之前
			String endTime = DateUtil.transferLongToDate("yyyy-MM-dd HH:mm:ss.SSS", delta1);
			processStatusData(startTime, endTime);
			log.info("获取 pg" + startTime + "至" + endTime + "输赢数据完毕。");
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
			List<PgData> pgDataList = processData(startTime, endTime);
			if (org.springframework.util.CollectionUtils.isEmpty(pgDataList)) {
				log.info(GamePlatform.PG.name() + "本地次抓取到有游戏输赢的玩家数量为0");
			} else {
				log.info(GamePlatform.PG.name() + "本地次抓取到有游戏输赢的玩家数量为：" + pgDataList.size());
				for (PgData pgData : pgDataList) {
					commonService.saveOrUpdate(pgData);
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
	public synchronized List<PgData> processData(String startTime, String endTime) throws ExecutionException {
		try {
			Future<List<PgData>> processFuture = getProcessFuture(startTime, endTime);
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

	private Future<List<PgData>> getProcessFuture(String startTime, String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
	    PGThread thread = new PGThread(startTime, endTime);
		Future<List<PgData>> future = executorService.submit(thread);
		return future;
	}

}
