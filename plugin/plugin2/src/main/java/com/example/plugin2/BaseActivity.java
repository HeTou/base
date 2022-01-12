package com.example.plugin2;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = new ContextThemeWrapper(getBaseContext(), 0);
        Class<? extends Context> aClass = mContext.getClass();
        try {
            Field mResoureceField = aClass.getDeclaredField("mResourece");
            mResoureceField.setAccessible(true);
            mResoureceField.set(mContext, LoadUtil.getResources(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
