package com.example.mvp.test_dagger2;

import com.example.mvp.view.MainActivity;

import dagger.Component;

@Component(modules = {MainModule.class})
public interface MainComponent {
    void injectMain(MainActivity mainActivity);

}