package com.base.baselib.common.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * retrofit 配置管理
 */
public class RetrofitMgr {

    private static RetrofitMgr instance;
    private Retrofit retrofit;
    private OkHttpClient client;
    private String basicUrl;

    private List<Interceptor> interceptors = new ArrayList<>();


    private RetrofitMgr() {
    }

    public static RetrofitMgr getInstance() {
        if (instance == null) {
            instance = new RetrofitMgr();
        }
        return instance;
    }

    public void init() {
//        HttpLoggingInterceptor log = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                try {
//                    String text = URLDecoder.decode(message, "utf-8");
//                    Log.i("OKHttp-----", text);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("OKHttp-----", message);
//                }
//            }
//        });
        if (client == null) {
//            log.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS)//连接超时设置
                    .writeTimeout(30, TimeUnit.SECONDS)//写入超时设置，
                    .readTimeout(30, TimeUnit.SECONDS);//读取超时设置
//                .addInterceptor(log)
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
            builder.addInterceptor(new FLogInterceptor());
//            builder.addInterceptor(log);
            client = builder.build();
        }
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(basicUrl)
                /***处理请求对象*/
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                /***  转换成对象 */
                .addConverterFactory(GsonConverterFactory.create(createGson())).build();
    }


    private Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(int.class, new IntegerDefaultAdapter());
        return builder.create();
    }


    public void changeBaseUrl(String baseUrl) {
        this.basicUrl = baseUrl;
        Retrofit.Builder builder = retrofit.newBuilder();
        builder.baseUrl(baseUrl);
        Retrofit build = builder.build();
        retrofit = build;
    }

    public String getBasicUrl() {
        return basicUrl;
    }

    public void baseUrl(String baseUrl) {
        basicUrl = baseUrl;
    }

    public void client(OkHttpClient client) {
        this.client = client;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public <T> T create(Class<T> t) {
        return retrofit.create(t);
    }


    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public static <T> T creates(Class<T> t) {
        return RetrofitMgr.getInstance().create(t);
    }

}
