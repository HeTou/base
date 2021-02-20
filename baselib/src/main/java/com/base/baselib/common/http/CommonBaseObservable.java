package com.base.baselib.common.http;

import io.reactivex.rxjava3.disposables.Disposable;

public abstract class CommonBaseObservable<T> extends BaseObservable<T> {
    @Override
    public void doSubscribe(Disposable d) {

    }

    @Override
    public void doCompleted() {

    }
}
