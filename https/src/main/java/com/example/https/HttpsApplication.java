package com.example.https;

import android.app.Application;

import com.base.baselib.common.http.FLogInterceptor;
import com.base.baselib.common.http.RetrofitMgr;
import com.base.baselib.common.utils.HttpsUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class HttpsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();


//          测试结果为如果配置信任证书的话，就只会验证这个配置了证书的服务，如：我配置了FiddlerRoot.cet,就只是访问代理了，访问官方就访问不到了
            HttpsUtils.SSLParams sslSocketFactory = HttpsUtils.getSslSocketFactory(getAssets().open("client.bks"), "111");

            builder.connectTimeout(30, TimeUnit.SECONDS)//连接超时设置
                    .sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager)
                    .writeTimeout(30, TimeUnit.SECONDS)//写入超时设置，
                    .readTimeout(30, TimeUnit.SECONDS);//读取超时设置
//                .addInterceptor(log)

            builder.addInterceptor(new FLogInterceptor());

            RetrofitMgr.getInstance().client(builder.build());
            RetrofitMgr.getInstance().baseUrl("https://epm.tentcoo.com:31000");
            RetrofitMgr.getInstance().init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
