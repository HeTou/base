package com.example.mvvm_kotlin.common.http

import com.example.mvvm_kotlin.common.bean.BaseResp
import com.example.mvvm_kotlin.common.bean.LoginRespData
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {


    /***
     * 登录
     * @param account 账号
     * @param password 密码
     * @return
     */
    @POST("/client/login")
    suspend fun login(@Query("account") account: String?, @Query("password") password: String?, @Query("regid") regId: String?): BaseResp<LoginRespData>

}
