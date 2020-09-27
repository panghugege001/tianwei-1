package app.util;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 同步用户对象池
 *
 */
public class SynchronizedUserPool {

	private static Logger log = Logger.getLogger(SynchronizedUserPool.class);
	
	private static Map<String, SynchronizedUser> userMap = new LinkedHashMap<String, SynchronizedUser>();
	
	public static synchronized SynchronizedUser getSynchronizedByUser(String loginName) {

		log.info(">>>>>>>>>>同步用户对象池>>>>>>>>>>" + userMap.size() + ">>>>>>>>>>账号:" + loginName);
		
		if (userMap.containsKey(loginName)) {
			
			return userMap.get(loginName);
		} else {
			
			userMap.put(loginName, new SynchronizedUser());
			return userMap.get(loginName);
		}
	}
}