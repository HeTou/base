package com.example.https;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.base.baselib.common.http.CommonBaseObservable;
import com.base.baselib.common.http.RetrofitMgr;

import io.reactivex.rxjava3.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void request(View view) {
        RetrofitMgr.getInstance().create(AppService.class).page(1, 1, 1, "").subscribe(new CommonBaseObservable<Object>() {
            @Override
            public void doSuccess(Object o) {

            }

            @Override
            public void doFail(@NonNull Throwable errorMsg) {

            }
        });
    }
}