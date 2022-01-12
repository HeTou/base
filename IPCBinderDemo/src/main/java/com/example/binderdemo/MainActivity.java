package com.example.binderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ipcbinder.servicemanager.core.FBinder;

public class MainActivity extends AppCompatActivity {

    public static String aaa = "aahahah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aaa = "bbbbbbbbb";
//        FBinder.getDefault().open(this);


//      注册服务
        FBinder.getDefault().register(FtService.class);

        FtService.getInstance().setUser("我是你八八八");
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}