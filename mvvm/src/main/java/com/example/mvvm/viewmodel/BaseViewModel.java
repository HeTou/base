package com.example.mvvm.viewmodel;


import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.android.ActivityEvent;

/**
 * ViewModel基类
 * Created by yangle on 2017/7/26.
 */

public class BaseViewModel {

    public BaseViewModel(LifecycleProvider<ActivityEvent> provider) {
        this.provider = provider;
    }

    private LifecycleProvider<ActivityEvent> provider;

    public LifecycleProvider<ActivityEvent> getProvider() {
        return provider;
    }
}
