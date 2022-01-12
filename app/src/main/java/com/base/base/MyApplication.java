package com.base.base;

import android.app.Application;
import android.content.Context;

import com.base.base.carsh.CarshHandler;

public class MyApplication extends Application {
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();

//      方法耗时统计
//        Debug.startMethodTracing("launcher");
//        context = this;
//        sleep(1000);
//        Debug.stopMethodTracing();

        CarshHandler.getCarshHandler().init(this);

    }

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
