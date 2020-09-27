package com.nnti.common.controller;

import com.nnti.common.cache.RedisCache;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by wander on 2017/2/10.
 */
@Controller
@RequestMapping("/redis")
public class RedisController extends BaseController {

    @Autowired
    private RedisCache redisCache;

    private static final String brandPrefix = ConfigUtil.getValue("brand.prefix");

    @RequestMapping("main")
    public String main() {
        return "/base/redis";
    }

    /*** 清除所有缓存 */
    @ResponseBody
    @RequestMapping("clear")
    public String clear() {
        redisCache.clear();
        return SUCCESS;
    }

    /*** 清除某key的缓存 */
    @ResponseBody
    @RequestMapping("clearByKey")
    public String clearByKey(String key) throws BusinessException {
        Assert.notEmpty(key);
        Set<String> set = redisCache.keys(brandPrefix + "_" + key + "*");
        Iterator cache = set.iterator();
        while (cache.hasNext()) {
            redisCache.evict(cache.next());
        }
        return SUCCESS;
    }
}
