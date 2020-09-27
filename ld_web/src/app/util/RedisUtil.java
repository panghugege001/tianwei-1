package app.util;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis操作接口
 */
public class RedisUtil {

	private static Logger log = Logger.getLogger(RedisUtil.class);

	private static JedisPool jedisPool = null;
	private static String ADDR = "";
	private static int PORT = 0000;
	private static String AUTH = "";

	/**
	 * 初始化Redis连接池
	 */
	static {
		
		try {
			
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("oscache.properties"));
			
			ADDR = properties.getProperty("redis.server.ip");
			PORT = Integer.parseInt(properties.getProperty("redis.server.port"));
			AUTH = properties.getProperty("redis.server.password");
			
			log.info("redis缓存服务器ip:" + ADDR + ",端口:" + PORT + ",密码:" + AUTH);
			
			JedisPoolConfig config = new JedisPoolConfig();
			// 连接耗尽时是否阻塞,false报异常,true阻塞直到超时,默认true
			config.setBlockWhenExhausted(true);
			// 设置的逐出策略类名,默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
			// 是否启用pool的jmx管理功能,默认true
			config.setJmxEnabled(true);
			// 最大空闲连接数,默认8个,控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
			config.setMaxIdle(10);
			// 最大连接数,默认8个
			config.setMaxTotal(100);
			// 表示当borrow(引入)一个jedis实例时,最大的等待时间,如果超过等待时间,则直接抛出JedisConnectionException
			config.setMaxWaitMillis(1000 * 100);
			// 在borrow一个jedis实例时,是否提前进行validate操作,如果为true,则得到的jedis实例均是可用的
			config.setTestOnBorrow(true);
			
			if (StringUtils.isNotEmpty(AUTH)) {

				jedisPool = new JedisPool(config, ADDR, PORT, 10000, AUTH);

				log.info("redis通过密码连接成功");
			} else {

				jedisPool = new JedisPool(config, ADDR, PORT, 10000);
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.info("redis缓存服务器链接失败");
		}
	}
	
	public static Jedis getJedis() {
		
		try {
			
			if (jedisPool != null) {

				return jedisPool.getResource();
			}
			
			return null;
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public static void set(String key, String value) {

		Jedis jedis = null;

		try {

			jedis = getJedis();

			jedis.set(key, value);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			close(jedis);
		}
	}
	
	public static String get(String key) {

		Jedis jedis = null;
		String value = "";
		
		try {

			jedis = getJedis();
			value = jedis.get(key);
		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			close(jedis);
		}
		
		return value;
	}
	
	public static void delete(String key) {

		Jedis jedis = null;
		
		try {

			jedis = getJedis();
			jedis.del(key);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			close(jedis);
		}
	}
	
	public static void close(final Jedis jedis) {
		
		if (jedis != null) {
			
			jedis.close();
		}
	}
	
	public static void main(String[] args) {
		
	}
}