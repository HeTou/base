package com.example.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("TAG", "handleMessage() called with: msg = [" + msg + "]");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void send(View view) {
//        for (int i = 0; i < 70; i++) {
//            Message obtain = Message.obtain();
//            obtain.what = i;
//            mainHandler.sendMessageDelayed(obtain, 2000);
//        }
        test();
    }


    public void test() {
        ThreadLocal<String> threadLocal = new ThreadLocal<String>() {

            @Nullable
            @Override
            protected String initialValue() {
                return "涛哥好";
            }
        };


        String mainThread = threadLocal.get();

        Log.d("TAG", "主线程：" + mainThread);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String runThread = threadLocal.get();
                Log.d("TAG", "子线程：" + runThread);
                threadLocal.set("我是子线程修改了值的：哈哈哈");

                String s = threadLocal.get();
                Log.d("TAG", "set() method : " + s);
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "子线程执行完毕：再次调用Thread.get():" + threadLocal.get());
            }
        }, 2000);
    }
}