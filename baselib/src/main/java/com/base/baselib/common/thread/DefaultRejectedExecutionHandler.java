package com.base.baselib.common.thread;


import com.base.baselib.common.log.FLog;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/***
 * 线程池拒绝策略
 *
 */
public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        FLog.json("超过最大线程数");
        if (!executor.isShutdown()) {
            r.run();
        }
    }
}
