package dfh.utils;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

public class SessionCacheUtil {
	
	public static void saveCache(HttpServletRequest request, Object[] params, String cacheName){
		Integer beforeWeek;
		String slots;
		Object[] match_cache = (Object[]) request.getSession().getAttribute(cacheName);
		match_cache = match_cache==null?new Object[3]:match_cache;
		beforeWeek = Integer.valueOf(params[0].toString());
		slots = params[1].toString();
		if (match_cache != null && (match_cache[0] != beforeWeek || !match_cache[1].toString().equals(slots))){ //当周与平台变化时,缓存领取过8元体验金的玩家
			match_cache[2] = new ArrayList();
		}
		match_cache[0] = params[0];
		match_cache[1] = params[1];
		request.getSession().setAttribute("match_cache", match_cache);
	}
	
	public static Object getCacheByNameIndex(HttpServletRequest request, String cacheName, Integer index) {
		Object[] match_cache = (Object[]) request.getSession().getAttribute(cacheName);
		match_cache = match_cache==null?new Object[3]:match_cache;
		return match_cache[index];
	}
	
	public static void saveCacheByNameIndex(HttpServletRequest request, String cacheName, Integer index, Object param) {
		Object[] match_cache = (Object[]) request.getSession().getAttribute(cacheName);
		match_cache = match_cache==null?new Object[3]:match_cache;
		match_cache[index] = param;
		request.getSession().setAttribute(cacheName, match_cache);
	}
}
