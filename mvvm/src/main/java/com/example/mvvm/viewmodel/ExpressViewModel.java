package com.example.mvvm.viewmodel;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.mvvm.bean.ExpressInfo;
import com.example.mvvm.databinding.ActivityMainBinding;
import com.trello.rxlifecycle4.LifecycleProvider;
import com.trello.rxlifecycle4.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

public class ExpressViewModel extends BaseViewModel {
    public ExpressInfo expressInfo;

    // 是否显示Loading
    public final ObservableBoolean isShowLoading = new ObservableBoolean();
    // 错误信息
    public final ObservableField<String> errorMessage = new ObservableField<>();

    public final ObservableField<ExpressInfo> expressInfoObservableField = new ObservableField<>();

    ActivityMainBinding binding;

    public ExpressViewModel(LifecycleProvider<ActivityEvent> provider, ActivityMainBinding binding) {
        super(provider);
        expressInfo = new ExpressInfo();
        binding.setUser(expressInfo);
        this.binding = binding;
    }


    public void request() {
        ExpressInfo expressInfo = new ExpressInfo();
        expressInfo.setCom("com");
        expressInfo.setMessage("修改了对象");
        List<ExpressInfo.DataBean> list = new ArrayList<>();
        list.add(new ExpressInfo.DataBean());
        expressInfo.setData(list);
        Log.d("TAG", "修改了对象");
        expressInfoObservableField.set(expressInfo);
        binding.setUser(expressInfo);

    }

    public void getText() {
        Log.d("TAG", "修改了文本");
        errorMessage.set("我修改了文本");
        expressInfo.setMessage("我点击了按钮");
    }
}
