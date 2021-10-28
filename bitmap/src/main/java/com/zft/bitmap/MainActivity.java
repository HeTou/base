package com.zft.bitmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.baselib.common.utils.BitmapUtil;
import com.base.baselib.common.utils.DeviceUtil;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions3.RxPermissions;

import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    TextView content;
    Button btn;
    private RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        content = findViewById(R.id.content);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        mRxPermissions = new RxPermissions(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Throwable {
                        DeviceUtil.systemPicture(MainActivity.this, 1);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (RESULT_OK == resultCode) {
                String s = DeviceUtil.systemPictrueForResult(this, data);

                BitmapUtil.log(s);
                Glide.with(this).asBitmap().load(s).into(img);
            }
        }
    }
}