package com.example.classloader;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DexClassLoader()   //我们外部加载器（非系统代码加载器）
//        PathClassLoader()  //系统的加载器
    }
}