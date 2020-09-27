package dfh.icbc.quart.fetch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-4
 * Time: 上午11:33
 * To change this template use File | Settings | File Templates.
 */
public class AlipayThreadPoolUtil {

    private static Log log = LogFactory.getLog(AlipayThreadPoolUtil.class);
    private static AlipayThreadPoolUtil pool = null;
    private static ThreadPoolExecutor threadPool = null;
    private int corePoolSize = 1;
    private int maximumPoolSize = 2;
    private int keepAliveTime = 1;

    private AlipayThreadPoolUtil() {
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static synchronized AlipayThreadPoolUtil getInstance() {
        try {
            if (pool == null) {
                pool = new AlipayThreadPoolUtil();
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return pool;
    }

    public void add(Runnable run) {
        try {
            threadPool.execute(run);
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
