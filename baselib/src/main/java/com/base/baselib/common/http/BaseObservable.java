package com.base.baselib.common.http;


import java.net.ConnectException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/***
 *
 * @param <T>
 */
public abstract class BaseObservable<T> implements Observer<T>, ISubscriber<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        doSubscribe(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        doSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        Throwable error = e;
        if (e instanceof ConnectException) {
            error = new NetException("网络异常，请检查网络后重试");
        }
        doFail(error);
    }

    @Override
    public void onComplete() {
        doCompleted();
    }
}
