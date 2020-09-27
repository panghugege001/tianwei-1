package com.gsmc.png.quart.fetch;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import com.gsmc.png.model.CqData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.quart.thread.Cq9Thread;
import com.gsmc.png.service.interfaces.CommonService;
import com.gsmc.png.utils.DateUtil;

public class FetchCq9DataJobService {

	private static Logger log = Logger.getLogger(FetchCq9DataJobService.class);

	final CommonService commonService;

	public FetchCq9DataJobService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void execute() {
		try {
			log.info("定时器开始获取cq9输赢数据...");
			long delta1 = new Date().getTime();
			long delta2 = delta1 - 8 * 60 * 1000L;
			//8分钟前
			String startTime = DateUtil.getDateTime(delta2);
			String endTime = DateUtil.getDateTime(delta1);
			processStatusData(startTime, endTime);
			log.info("获取 cq9" + startTime + "至" + endTime + "输赢数据完毕。");
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
			List<CqData> CqDataList = processData(startTime, endTime);
			if (org.springframework.util.CollectionUtils.isEmpty(CqDataList)) {
				log.info(GamePlatform.CQ9.name() + "本地次抓取到有游戏输赢的玩家数量为0");
			} else {
				log.info(GamePlatform.CQ9.name() + "本地次抓取到有游戏输赢的玩家数量为：" + CqDataList.size());
				for (CqData Cq9Data : CqDataList) {
					commonService.saveOrUpdate(Cq9Data);
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
	public synchronized List<CqData> processData(String startTime, String endTime) throws ExecutionException {
		try {
			Future<List<CqData>> processFuture = getProcessFuture(startTime, endTime);
			return processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchCq9DataJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(), e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchCq9DataJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}

	private Future<List<CqData>> getProcessFuture(String startTime, String endTime) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Cq9Thread thread = new Cq9Thread(startTime, endTime);
		Future<List<CqData>> future = executorService.submit(thread);
		return future;
	}

}
