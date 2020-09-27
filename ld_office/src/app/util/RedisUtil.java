package app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis操作接口
 */
public class RedisUtil {

	private static Logger log = Logger.getLogger(RedisUtil.class);
	private static ObjectMapper mapper = new ObjectMapper();
	private static String SC_10000 = "10000";
	private static String SC_10001 = "10001";

//	    private static String MIDDLESERVICE_URL = "http://big.redisservice.com";
	private static String MIDDLESERVICE_URL = "http://redisservice.com/";
	//			String url = Configuration.getInstance().getValue("middleservice.url");

	/**
	 * 普通缓存放入
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, String value) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("key", key);
		paramMap.put("value", value);
		String url = MIDDLESERVICE_URL+"/common/setData";
		try {
			Map<String, String> resultMap = HttpClientUtil.postByAES(url, paramMap);
			String code = resultMap.get("code");
			if (RedisUtil.SC_10000.equals(code)) {
				flag = true;
				log.info("RedisUtil set 缓存成功！");
			} else {
				log.info("RedisUtil set 缓存失败！");
			}
			return flag;
		} catch (Exception e) {
			log.info("setData error" + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 增加失效时间放入
	 *
	 * @param key
	 * @param value
	 * @param time
	 * @return
	 */
	public static boolean setDataTime(String key, String value, long time) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("key", key);
		paramMap.put("value", value);
		paramMap.put("time", time);
		String url =  MIDDLESERVICE_URL+"/common/setDataTime";
		try {
			Map<String, String> resultMap = HttpClientUtil.postByAES(url, paramMap);
			String code = resultMap.get("code");
			if (RedisUtil.SC_10000.equals(code)) {
				flag = true;
				log.info("RedisUtil setDataTime 缓存成功！");
			} else {
				log.info("RedisUtil setDataTime 缓存失败！");
			}
			return flag;
		} catch (Exception e) {
			log.info("setDataTime error" + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 获取缓存数据
	 *
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String data = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("key", key);
		String url =  MIDDLESERVICE_URL+"/common/getData";
		try {
			Map<String, String> resultMap = HttpClientUtil.postByAES(url, paramMap);
			String code = resultMap.get("code");
			if (RedisUtil.SC_10000.equals(code)) {
				log.info("RedisUtil getData 缓存成功！");
				data = resultMap.get("data");
			} else {
				log.info("RedisUtil getData 缓存失败！");
			}
			return data;
		} catch (Exception e) {
			log.info("getData error" + e.getMessage());
			e.printStackTrace();
		}
		return data;
	}

	public static Boolean reloadCache(Map<String, Object> paramMap) {
		Boolean flag = false;
		paramMap.put("product", "ld");
		String url =  MIDDLESERVICE_URL+"/ipCache/reloadCache";
		try {
			Map<String, String> resultMap = HttpClientUtil.postByAES(url, paramMap);
			String code = resultMap.get("code");
			if (RedisUtil.SC_10000.equals(code)) {
				flag = true;
				log.info("RedisUtil reloadCache 缓存成功！");
			} else {
				log.info("RedisUtil reloadCache 缓存失败！");
			}
			return flag;
		} catch (Exception e) {
			log.info("reloadCache error" + e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 删除数据
	 *
	 * @param key
	 * @return
	 */
	public static String delete(String key) {
		String data = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("key", key);
		String url = MIDDLESERVICE_URL+"/common/deleteData";
		try {
			Map<String, String> resultMap = HttpClientUtil.postByAES(url, paramMap);
			String code = resultMap.get("code");
			if (RedisUtil.SC_10000.equals(code)) {
				log.info("RedisUtil deleteData 缓存成功！");
				data = resultMap.get("data");
			} else {
				log.info("RedisUtil deleteData 缓存失败！");
			}
			return data;
		} catch (Exception e) {
			log.info("deleteData error" + e.getMessage());
			e.printStackTrace();
		}
		return data;
	}

	public static void main(String[] args) {

	}
}