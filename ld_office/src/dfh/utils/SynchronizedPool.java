package dfh.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步对象池
 */
public class SynchronizedPool {

    private static Logger log = Logger.getLogger(SynchronizedPool.class);

    private static final LoadingCache<String, ReentrantLock> LockMap = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, ReentrantLock>() {
                        @Override
                        public ReentrantLock load(String key) throws Exception {
                            return new ReentrantLock();
                        }
                    });

    public static synchronized ReentrantLock getLockByEvent(LockEvent event) {
        try {
        	ReentrantLock reentrantLock = LockMap.get(event.name());
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>用户对象池:" + LockMap.size() + "*******锁定事件:" + event + ",同步物件:"+reentrantLock);
            return reentrantLock;
        } catch (ExecutionException e) {
            log.error("取得Lock 失败 *******锁定事件:" + event);
            e.printStackTrace();
            return null;
        }
    }


    public enum LockEvent{
        XIMA_PROPOSAL_UPDATE,
        PT_DATA_ACQUISITION,//PT数据采集方法


    }


}
