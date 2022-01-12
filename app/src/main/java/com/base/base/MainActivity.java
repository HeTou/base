package com.base.base;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BuildConfig
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_carsh:
                int i = 1 / 0;
                break;
        }
    }
}
