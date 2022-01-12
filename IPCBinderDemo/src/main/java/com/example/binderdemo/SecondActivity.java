package com.example.binderdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipcbinder.servicemanager.core.FBinder;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//      获取服务
        FBinder.getDefault().open(this);
    }

    public void onClick(View view) {
        IFtService instance = FBinder.getDefault().getInstance(IFtService.class);
        String user = instance.getUser();
        Toast.makeText(this, user, Toast.LENGTH_LONG).show();
    }
}
