package com.example.mvvm.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import com.example.mvvm.R;
import com.example.mvvm.databinding.ActivityMainBinding;
import com.example.mvvm.viewmodel.ExpressViewModel;

/***
 * mvvm开发模式主要是利用 databinding 进行双向绑定 从而进一步的解耦
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ExpressViewModel expressViewModel = new ExpressViewModel(this, binding);
        binding.setHandlers(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_text:
                        expressViewModel.request();
                        break;
                    case R.id.btn1:
                        expressViewModel.getText();
                        Log.d("TAG", expressViewModel.expressInfo.toString());
                        break;
                }
            }
        });

        expressViewModel.expressInfoObservableField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.d("TAG", "onPropertyChanged() called with: sender = [" + sender + "], propertyId = [" + propertyId + "]");
            }
        });
    }
}