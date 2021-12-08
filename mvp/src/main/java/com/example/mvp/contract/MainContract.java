package com.example.mvp.contract;

import com.example.mvp.view.MainActivity;
import com.example.mvp.presenter.IPresenter;
import com.example.mvp.presenter.MainPresenter;
import com.example.mvp.view.IView;

public interface MainContract {
    interface View extends IView<MainPresenter> {

    }

    interface Presenter extends IPresenter<MainActivity> {

    }
}
