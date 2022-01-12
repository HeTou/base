package com.base.base;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.base.baselib.common.utils.BarUtil;

import java.io.File;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtil.immersionFull(this, false);
        setContentView(R.layout.activity_splash);
    }

    public void onclick(View view) {
        String str = Environment.getExternalStorageDirectory() + "/client.pdf";

        FileOpenUtils.openFile(this, new File(str));
    }
}
