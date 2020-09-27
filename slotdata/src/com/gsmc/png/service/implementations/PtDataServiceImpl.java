package com.gsmc.png.service.implementations;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.gsmc.png.service.interfaces.PtDataService;


public class PtDataServiceImpl implements PtDataService {

    //用于返水返水时更新老虎机和累计奖池老虎机，因为需要判断是否执行完，调用shutdown，会导致定时器扫描PT数据，无法加入线程池中执行，因此单独用一个线程池
    ExecutorService customerExecutorService = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    @Override
    public void execute(Thread ptBetUpdateThread) {
        Future<?> future = customerExecutorService.submit(ptBetUpdateThread);
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage().replaceAll("java.lang.RuntimeException:",""));
        } catch (ExecutionException e) {
            throw new RuntimeException("PT更新数据失败 , 请稍后再试");
        }
    }

    @Override
    public Future<?> executeOtherTask(Runnable runnable) {
        Future<?> future = customerExecutorService.submit(runnable);
        return future;
    }

    void shutdown() {
        if (!customerExecutorService.isShutdown()) {
            customerExecutorService.shutdown();
        }
    }

}
