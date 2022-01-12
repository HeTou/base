package com.example.androidproxy;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproxy.annotion.ContentView;
import com.example.androidproxy.annotion.OnClick;
import com.example.androidproxy.annotion.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        tv.setText("我已经通过注解反射 赋值了");
    }

    @OnClick({R.id.tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv:
                Toast.makeText(this, "动态代理陈公公了", Toast.LENGTH_LONG).show();
                break;
        }
    }
}