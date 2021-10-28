package com.zft.bluetooth;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.application = this;
    }

    public static Context getInstance() {
        return MyApplication.application;
    }
}
