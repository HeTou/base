package com.example.mvvm_kotlin.viewmodel

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.lifecycle.*
import com.example.mvvm_kotlin.common.bean.BaseResp
import com.example.mvvm_kotlin.common.bean.LoginRespData
import com.example.mvvm_kotlin.common.bean.UserModel
import com.example.mvvm_kotlin.common.db.MRoomDatabase
import com.example.mvvm_kotlin.common.http.AppService
import com.example.mvvm_kotlin.common.http.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var users: MutableLiveData<List<UserModel>>? = null

    var text: MutableLiveData<String> = MutableLiveData()

    fun getUsers(): LiveData<List<UserModel>>? {
        if (users == null) {
            users = MutableLiveData()
            loadUsers()
        }
        return users
    }

    constructor(application: Application, a: Int) : this(application) {

    }

    private fun loadUsers() {
        Handler().postDelayed({
            val lists: MutableList<UserModel> = ArrayList()
            for (i in 0..9) {
                val userModel = UserModel()
                userModel.name = i.toString() + ""
                lists.add(userModel)
            }
            users!!.value = lists
        }, 2000)
        // Do an asynchronous operation to fetch users.
    }

    //  操作数据库
    fun dao() {
        suspend fun a1(): String {
            val userDao = MRoomDatabase.getInstance(getApplication()).userDao()
            val queryAll = userDao.queryAll();
            println("queryAll:${queryAll}")

            val userModel = UserModel();
            userModel.age = 11
            userModel.faceId = "111";
            userModel.name = "我是你爸爸"
            val insertUser = userDao.insertUser(userModel)
            println("insertUser:${insertUser} ${Thread.currentThread().name}")


            val queryAll2 = userDao.queryAll();
            println("queryAll2:${queryAll2}")
            val dogDao = MRoomDatabase.getInstance(getApplication()).dogDao();
            return "我刚刚已经操作了一遍数据库了操作了哦，具体请看日志"
        }

//      协程
        GlobalScope.launch(Dispatchers.Main) {
            val await = async(Dispatchers.IO) {
                return@async a1()
            }.await()

            text?.value = await

        }
    }

    /***
     * 请求接口
     */
    fun requestNet() {
        GlobalScope.launch(Dispatchers.Main) {
            val await = async(Dispatchers.IO) {

                val service = RetrofitClient.service(AppService::class.java);

                var login: BaseResp<LoginRespData>? = null
                runCatching {
                    service.login("11111", "1111111", "1111")
                }.onSuccess {
                    login = it
                }.onFailure {
                    it.printStackTrace()
                }
//                val login = "jlfas;jfalfj"
                return@async login
            }.await()
            text.value = Gson().toJson(await)
        }
    }


}