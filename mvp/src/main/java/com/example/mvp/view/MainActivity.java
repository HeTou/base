package com.example.mvp.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mvp.R;
import com.example.mvp.contract.MainContract;
import com.example.mvp.presenter.MainPresenter;
import com.example.mvp.test_dagger2.Cloth;
import com.example.mvp.test_dagger2.Cloth2;
import com.example.mvp.test_dagger2.DaggerMainComponent;
import com.example.mvp.test_dagger2.MainComponent;
import com.example.mvp.test_dagger2.MainModule;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @Inject
    @Named("red")
    Cloth colorCloth;

    @Inject
    @Named("blue")
    Cloth colorCloth2;

    @Inject
    Cloth2 colorCloth3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainPresenter mainPresenter = new MainPresenter();

        MainComponent build = DaggerMainComponent.builder().mainModule(new MainModule()).build();
        build.injectMain(this);

        Log.d("TAG", "适配的值1：" + colorCloth);
        Log.d("TAG", "适配的值2：" + colorCloth2);
        Log.d("TAG", "适配的值3：" + colorCloth3);
    }
}