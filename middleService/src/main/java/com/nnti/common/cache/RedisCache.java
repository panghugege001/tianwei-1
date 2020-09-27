package com.nnti.common.cache;

import com.nnti.common.utils.ConfigUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.*;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by wander on 2017/2/7.
 */
@Configuration
public class RedisCache implements Cache {

    private Logger log = Logger.getLogger(RedisCache.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String name;

    private static final String brandPrefix = ConfigUtil.getValue("brand.prefix");

    @Override
    public Object getNativeCache() {
        return this.redisTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object object = getByKey(key);
        return (object != null ? new SimpleValueWrapper(object) : null);
    }

    @Override
    public <T> T get(Object key, Class<T> clazz) {
        T object = (T) getByKey(key);
        return object;
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    private Object getByKey(Object key) {
        final String keyf = brandPrefix + "_" + (String) key;
        Object object = null;
        try {
            object = redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    byte[] key = keyf.getBytes();
                    byte[] value = connection.get(key);
                    if (value == null) {
                        return null;
                    }
                    return toObject(value);
                }
            });
        } catch (Exception e) {
            log.error("redis 异常(忽略)：", e);
        }
        return object;
    }
    
    public Object testGetByKey(Object key) {
        final String keyf = brandPrefix + "_" + (String) key;
        return redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    byte[] key = keyf.getBytes();
                    byte[] value = connection.get(key);
                    if (value == null) {
                        return null;
                    }
                    return toObject(value);
                }
            });
    }

    @Override
    public void put(Object key, Object value) {
        final String keyf = brandPrefix + "_" + (String) key;
        final Object valuef = value;
        final long liveTime = 86400;

        try {
            redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyb = keyf.getBytes();
                    byte[] valueb = toByteArray(valuef);
                    connection.set(keyb, valueb);
                    if (liveTime > 0) {
                        connection.expire(keyb, liveTime);
                    }
                    return 1L;
                }
            });
        } catch (Exception e) {
            log.error("redis 异常(忽略)：", e);
        }
    }
    
    public void testPut(Object key, Object value) {
        final String keyf = brandPrefix + "_" + (String) key;
        final Object valuef = value;
        final long liveTime = 86400;

        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyb = keyf.getBytes();
                byte[] valueb = toByteArray(valuef);
                connection.set(keyb, valueb);
                if (liveTime > 0) {
                    connection.expire(keyb, liveTime);
                }
                return 1L;
            }
        });
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    @Override
    public void evict(Object key) {
        final String keyf = (String) key;
        try {
            redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.del(keyf.getBytes());
                }
            });
        } catch (Exception e) {
            log.error("redis 异常(忽略)：", e);
        }
    }

    @Override
    public void clear() {
        log.info("清除缓存....");
        try {
            redisTemplate.execute(new RedisCallback<String>() {
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.flushDb();
                    return "ok";
                }
            });
        } catch (Exception e) {
            log.error("redis 异常(忽略)：", e);
        }
    }

    public long del(final String... keys) {
        try {
            return redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    long result = 0;
                    for (int i = 0; i < keys.length; i++) {
                        result = connection.del(keys[i].getBytes());
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            log.error("redis 异常(忽略)：", e);
        }
        return 0;
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }


    public boolean exists(final String key) {
        final String keyf = (String) key;
        try {
            return redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.exists(keyf.getBytes());
                }
            });
        } catch (Exception e) {
            log.error("redis 异常(忽略)：", e);
        }
        return false;
    }

    public long dbSize() {
        try {
            return redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.dbSize();
                }
            });
        } catch (Exception e) {
            log.error("redis 异常(忽略)：", e);
        }
        return 0;
    }

    public String ping() {
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.ping();
            }
        });
    }

    /**
     * 描述 : <Object转byte[]>. <br>
     *
     * @param obj
     * @return
     */
    private byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 描述 : <byte[]转Object>. <br>
     *
     * @param bytes
     * @return
     */
    private Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
