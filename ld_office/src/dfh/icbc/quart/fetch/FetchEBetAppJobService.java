package dfh.icbc.quart.fetch;

import dfh.model.PlatformData;
import dfh.service.interfaces.IGetdateService;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FetchEBetAppJobService {

	private IGetdateService getdateService;
	private static Logger log = Logger.getLogger(FetchEBetAppJobService.class);
	//預設抓昨天
	private static final int minusDayNumber = 1;

	/**
	 * 给quartz用 , 已经处理 Excpetion.
	 */
	public synchronized  void processStatusData() {
		try {
			Future processFuture = getProcessFuture();
			processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchEBetAppJobService 失败 , message : " + e.getMessage());
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchEBetAppJobService 失败 , message : " + e.getMessage());
		}
	}

	/**
	 * 给外部呼叫用. 会抛出Exceptioin.
	 * @throws ExecutionException
     */
	public synchronized List<PlatformData> processData() throws ExecutionException {
		try {
			Future<List<PlatformData>> processFuture = getProcessFuture();
			return processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchEBetAppJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(),e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchEBetAppJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}

	private Future<List<PlatformData>> getProcessFuture(){
		/**
		 * 调用EBetApp获取有投注信息
		 */
		log.info("process EBetApp Data ....");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		EBetAppDataThread thread = new EBetAppDataThread(getdateService,getQueryDate());
		return executorService.submit(thread);
	}

	public static Date getQueryDate() {
		return getDate(minusDayNumber);
	}

	/**
	 * 保留用来处理区间范围日其资料
	 * e.g.
	 * dailyProfit = EBetAppUtil.getDailyProfit(queryDate);
	 * dailyProfit.addAll(EBetAppUtil.getDailyProfit(getQueryDate(1)));
	 * dailyProfit.addAll(EBetAppUtil.getDailyProfit(getQueryDate(2)));
	 *
	 * @param minusDay
	 * @return
	 */
	public static Date getQueryDate(int minusDay) {
		return getDate(minusDay);
	}

	public static Date getDate(int minusDay) {
		Date date = new Date();
		Calendar cals = Calendar.getInstance();
		cals.setTime(date);
		cals.add(Calendar.DAY_OF_MONTH, -minusDay);
		return cals.getTime();
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}
