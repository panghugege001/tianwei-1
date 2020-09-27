package dfh.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 同步用户对象池
 *
 */
public class UserSynchronizedPool {
	
	private static Logger log = Logger.getLogger(UserSynchronizedPool.class);

	private static Map<String, Synchronized4User> userSynchMap = new LinkedHashMap<String, Synchronized4User>();
	
	
	public static synchronized Synchronized4User getSynchUtilByUser(String loginname){
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>同步用户对象池" + userSynchMap.size()+"******账号:"+loginname);
		if(userSynchMap.containsKey(loginname)){
			return userSynchMap.get(loginname);
		}else{
			userSynchMap.put(loginname, new Synchronized4User());
			return userSynchMap.get(loginname); 
		}
	}
}
