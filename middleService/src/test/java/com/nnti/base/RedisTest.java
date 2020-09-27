package com.nnti.base;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by wander on 2017/2/7.
 */
public class RedisTest {

    @Test
    public void test() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server sucessfully");
//        //查看服务是否运行
//        System.out.println("Server is running: " + jedis.ping());
//        //设置 redis 字符串数据
//        jedis.set("runoobkey", "你好");
//        // 获取存储的数据并输出
//        System.out.println("Stored string in redis:: " + jedis.get("runoobkey"));
//
//        //存储数据到列表中
//        jedis.lpush("tutorial-list", "Redis");
//        jedis.lpush("tutorial-list", "Mongodb");
//        jedis.lpush("tutorial-list", "Mysql");
//        // 获取存储的数据并输出
//        List<String> list = jedis.lrange("tutorial-list", 0, 5);
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println("Stored string in redis:: " + list.get(i));
//        }

        // 获取数据并输出
        Set<String> list2 = jedis.keys("*");
        Iterator<String> iterator = list2.iterator();
        while (iterator.hasNext()) {
            System.out.println("List of stored keys:: " + iterator.next());
        }
    }
}
