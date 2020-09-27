package dfh.icbc.quart.fetch;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import dfh.model.PlatformData;

public class FetchNTwoJobService {
	
	private static Logger log = Logger.getLogger(FetchNTwoJobService.class);
	//預設抓昨天
	private static final int minusDayNumber = 1;
	
	/**
	 * 给quartz用 , 已经处理 Excpetion.
	 */
	public synchronized void processStatusData() throws ExecutionException {
		try {
			Future processFuture = getProcessFuture();
			processFuture.get();
		} catch (InterruptedException e) {
			log.error("执行FetchNTwoJobService 失败 , message : " + e.getMessage());
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchNTwoJobService 失败 , message : " + e.getMessage());
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
			log.error("执行FetchNTwoJobService 失败 , message : " + e.getMessage());
			throw new ExecutionException(e.getMessage(),e);
		} catch (ExecutionException e) {
			e.printStackTrace();
			log.error("执行FetchNTwoJobService 失败 , message : " + e.getMessage());
			throw e;
		}
	}
	
	private Future<List<PlatformData>> getProcessFuture(){
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		NTwoDataThread thread = new NTwoDataThread(getSearchDate(minusDayNumber));
		Future<List<PlatformData>> future = executorService.submit(thread);
		return future;
	}
	
	public static Calendar getSearchDate(int minusDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -minusDay);
		return calendar;
	}
}
