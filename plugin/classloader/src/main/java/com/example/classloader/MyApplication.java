package com.example.classloader;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoadUtil.loadClass(this);

        HookUtil.hookAMS();
        HookUtil.hookHandler();
    }
}
