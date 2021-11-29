package com.zft.vr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button mVrImg;
    Button mVrVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVrImg = findViewById(R.id.btn_vrimg);
        mVrVideo = findViewById(R.id.btn_vrvideo);
        mVrImg.setOnClickListener(this);
        mVrVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vrimg: {
                Intent intent = new Intent(this, PanoWidgetActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_vrvideo: {
                Intent intent = new Intent(this, VrVideoActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}
