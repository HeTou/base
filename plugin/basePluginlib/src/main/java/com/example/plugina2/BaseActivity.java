package com.example.plugina2;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends Activity implements PayInterface {
    protected Activity that;

    @Override
    public void attach(Activity proxyActivity) {
        this.that = proxyActivity;
    }

    public void setContentView(int resId) {
        that.setContentView(resId);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
//        super.onCreate(saveInstanceState);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }
}
