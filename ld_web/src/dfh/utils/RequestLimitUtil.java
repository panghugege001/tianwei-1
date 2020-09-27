package dfh.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestLimitUtil {

	private static final Map<String, RequestRecord> requestMap = new HashMap<>();
	private static final RequestLimitUtil instance = new RequestLimitUtil();
	private static final int maxCount = 10;
	private static final int perMin = 5;
	private static final int maxCountTime = perMin * 60 * 1000;
	private RequestLimitUtil(){}
	public static RequestLimitUtil getInstance(){
		return instance;
	}
	
	/**
	 * 检测key请求次数。perMin分钟内最多maxCount次。
	 * @param key，建议自定义常量+ip，或方法名+ip
	 * @return
	 * */
	public boolean checkRequestLimit(String key){
		
		synchronized (key) {
			Date now = new Date();
			RequestRecord requestRecord = null;
			if(requestMap.size() > 2000) {
				requestMap.clear();
				return true;
			}
			if(requestMap.containsKey(key)){
				
				requestRecord = requestMap.get(key);
				Date lastTime = requestRecord.getLastTime();
				Date minReqDate = new Date(lastTime.getTime() + maxCountTime);
				if(minReqDate.after(now)){
					
					if(requestRecord.getCount() > maxCount) return false;
					requestRecord.setCount(requestRecord.getCount() + 1);
					return true;
				}
			}
			if(requestRecord == null){
				requestRecord = new RequestRecord();
				requestMap.put(key, requestRecord);
			}
			requestRecord.setCount(1);
			requestRecord.setLastTime(now);
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		
	}
	
	class RequestRecord {
		
		private Date lastTime;
		private Integer count;
		
		public Date getLastTime() {
			return lastTime;
		}
		public void setLastTime(Date lastTime) {
			this.lastTime = lastTime;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
	}
}
