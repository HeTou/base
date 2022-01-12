package com.example.mvvm_kotlin.common.http

import com.base.baselib.common.http.FLogInterceptor
import com.example.mvvm_kotlin.common.constant.AppConst
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {

    lateinit var retrofit: Retrofit

    init {
        val okHttpClient = OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.MINUTES)
                .addInterceptor(FLogInterceptor())
                .build()
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConst.httpUrl).build()
    }

    companion object {
        //双重校验线程锁
        val instance: RetrofitClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitClient()
        }

        fun <T> service(cls: Class<T>): T {
            return instance.retrofit.create(cls)
        }
    }


}