package com.nnti.common.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

/**
 * Created by wander on 2017/2/9.
 */
public class RedisCacheManager extends AbstractCacheManager {

    private Collection<? extends RedisCache> caches;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

    public Collection<? extends RedisCache> getCaches() {
        return caches;
    }

    public void setCaches(Collection<? extends RedisCache> caches) {
        this.caches = caches;
    }
}
