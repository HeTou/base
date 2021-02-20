package com.base.baselib.common.http;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 定义请求结果处理接口
 *
 * @param <T>
 */
public interface ISubscriber<T> {
    /**
     * doSubscribe 回调
     *
     * @param d Disposable
     */
    void doSubscribe(Disposable d);

    /**
     * 成功回调
     *
     * @param t 泛型
     */
    void doSuccess(T t);

    /**
     * 错误回调
     *
     * @param errorMsg 错误信息
     */
    void doFail(@NonNull Throwable errorMsg);


    /**
     * 请求完成回调
     */
    void doCompleted();
}
