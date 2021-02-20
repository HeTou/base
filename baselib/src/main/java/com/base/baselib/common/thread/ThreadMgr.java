package com.base.baselib.common.thread;


import com.base.baselib.common.log.FLog;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作者:zft
 * 日期:2018/3/1 0001.
 */

public class ThreadMgr {
    //    核心线程大小
    /**
     * 获取处理器数量
     */
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors();
    //    private static final int corePoolSize = 2; //获取处理器数量
    /**
     * 最大线程大小
     */
    private static final int maximumPoolSize = corePoolSize * 2;
    /**
     * 线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
     */
    private static final int keepAliveTime = 1;
    /**
     * keepAliveTime时间单位
     */
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;


    //    新建线程工厂
    /**
     * 当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理
     */
    public ThreadPoolExecutor threadPoolExecutor;

    private static ThreadMgr instance;

    private ThreadMgr() {
        //    阻塞任务队列
        FLog.d("核心线程大小：" + corePoolSize + "-最大线程大小：" + maximumPoolSize);
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, taskQueue, new BackgroundThreadFactory(), new DefaultRejectedExecutionHandler());
    }

    public static ThreadMgr getInstance() {
        if (instance == null) {
            instance = new ThreadMgr();
        }
        return instance;
    }

    public void execute(Runnable r) {
        threadPoolExecutor.execute(r);
    }

    public void cancel() {
        BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
        for (Runnable runnable : queue) {
            threadPoolExecutor.remove(runnable);
        }
    }
}
