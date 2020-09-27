package com.nnti.common.utils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.log4j.Logger;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DynamicReflectionUtil {

	private static Logger log = Logger.getLogger(DynamicReflectionUtil.class);

	public static Map<String, Class> clazzMap = new HashMap<String, Class>();
	public static Map<String, Object> objMap = new HashMap<String, Object>();
	public static Map<String, Method> metMap = new HashMap<String, Method>();

	/**
	 * 通过反射+缓存高效的调用某个类里面的某个方法
	 */
	public static Object cacheExecute(String nodeName, String clazz, String method, Object[] os, Class[] cs) {

		Object result = null;
		int size = 0;

		try {

			if (cs != null) {

				size = cs.length;
			}

			// 用于区分重载的方法
			Method m = metMap.get(clazz + "_" + method + "_" + size);
			Object obj = objMap.get(clazz);

			if (m == null || obj == null) {

				Class cl = clazzMap.get(clazz);

				// 缓存class对象
				if (cl == null) {

					cl = Class.forName(clazz);
					clazzMap.put(clazz, cl);
				}

				// 缓存对象的实例
				if (obj == null) {

					obj = cl.newInstance();
					objMap.put(clazz, obj);
				}

				// 缓存Method对象
				if (m == null) {

					m = cl.getMethod(method, cs);
					metMap.put(clazz + "_" + method + "_" + size, m);
				}
			}

			String time = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
			Boolean lockFlag = false;

			final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, nodeName);

			try {

				lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
			} catch (Exception e) {

				e.printStackTrace();
				log.error("获取锁发生异常，异常信息：" + e.getMessage());
				lockFlag = true;
			}

			try {

				if (lockFlag) {

					log.info("正在处理请求，执行时间：" + time);

					// 动态调用某个对象中的public声明的方法
					result = m.invoke(obj, os);

					log.info("处理完成请求，执行时间：" + time + "，结束时间：" + DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date()));
				} else {

					log.error("未能获取锁，无法执行请求对应的方法....");

					result = "系统繁忙，请稍后重试！";
				}
			} catch (Exception e) {

				e.printStackTrace();
				log.error("执行请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
				result = "系统繁忙，请稍后重试！";
				lockFlag = true;
			} finally {

				if (lockFlag) {

					try {

						lock.release();
					} catch (Exception e) {

						e.printStackTrace();
						log.error("释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
						result = "系统繁忙，请稍后重试！";
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.error("执行DynamicReflectionUtil类的cacheExecute方法发生异常，异常信息：" + e.getMessage());
			result = "系统繁忙，请稍后重试！";
		}

		return result;
	}
}