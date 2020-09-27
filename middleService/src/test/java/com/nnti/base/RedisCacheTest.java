//package com.nnti.base;
//
//import com.nnti.common.cache.RedisCache;
//import com.nnti.common.model.vo.Dictionary;
//import com.nnti.common.service.interfaces.IDictionaryService;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
///**
// * Created by wander on 2017/2/8.
// */
//public class RedisCacheTest extends BaseTest {
//
//    @Autowired
//    private IDictionaryService dictionaryService;
//    @Autowired
//    private RedisCache redisCache;
//
//    @Test
//    public void clear() {
//        log.info("---------clear---------------");
//        redisCache.clear();
//    }
//
//    @Test
//    public void initDictionary() {
//        log.info("......................................");
//        List<Dictionary> dictionaries = dictionaryService.findByCondition(null);
//        log.info("size=" + dictionaries.size());
//        for (Dictionary d : dictionaries) {
//            redisCache.put(d.getDictType() + "|" + d.getDictName(), d);
//        }
//    }
//
//    @Test
//    public void keys() {
//        Set<String> vw = redisCache.keys("*");
//        log.info("-------------");
//        for (Iterator d = vw.iterator(); d.hasNext(); ) {
//            log.info("--" + d.next());
//        }
//    }
//
//    @Test
//    public void exists() {
//        boolean b = redisCache.exists("point_card_type|SFYKT");
//        System.out.println("bbbbb=" + b);
//    }
//
//    @Test
//    public void dbSize() {
//        Long size = redisCache.dbSize();
//        System.out.println("sizesize=" + size);
//    }
//
//    @Test
//    public void listT() {
//        Dictionary dictionarie = redisCache.get("point_card_type|YDSZX", Dictionary.class);
//
//        log.info("-------------");
//        log.info(dictionarie.getDictName());
//    }
//}
