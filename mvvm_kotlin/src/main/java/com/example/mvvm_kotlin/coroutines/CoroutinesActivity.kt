package com.example.mvvm_kotlin.coroutines

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.*
import okhttp3.internal.wait
import java.lang.Runnable
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

/***
 * 线程
 */
class CoroutinesActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {


    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onDestroy() {
        super.onDestroy()
        // 当 Activity 销毁的时候取消该 Scope 管理的 job。
        // 这样在该 Scope 内创建的子 Coroutine 都会被自动的取消。
        job.cancel()
    }


    private val TAG: String = this::class.java.simpleName
    lateinit var dataBinding: ActivityCoroutinesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_coroutines)
        dataBinding.onClick = this

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_send -> {

//                test4()
//                test5()
//                test6()
                test7()

                Log.d("AA", "我在协程后面==================")
            }
            else -> {

            }
        }
    }


    private fun test() {

        Thread(Runnable {
//            runBlocking 挂起协程 ，阻塞当前线程 直到协程执行完毕
            runBlocking {
                repeat(8) {
                    Log.e(TAG, "协程执行$it 线程id：${Thread.currentThread().id}")
                    delay(1000)
                }
            }
            Log.d(TAG, "test() called")
        }).start()
    }

    private fun test2() {
        Thread(Runnable {
            repeat(8) {
                Log.e(TAG, "协程执行2|||$it 线程id：${Thread.currentThread().id}")
                Thread.sleep(1000)
            }
        }).start()

        Log.d(TAG, "test2() called")
    }

    private fun test3() {
        launch {
            doSomethingUsefulOne()
            doSomethingUsefulTwo()
            Log.d(TAG, "test3() called ${Thread.currentThread().name}")
        }
    }


    private fun test4() {
        Log.d("AA", "协程初始化开始，时间: " + System.currentTimeMillis())

        GlobalScope.launch(Dispatchers.Unconfined) {
            Log.d("AA", "协程初始化完成，时间: " + System.currentTimeMillis())
            for (i in 1..3) {
                Log.d("AA", "协程任务1打印第$i 次，时间: " + System.currentTimeMillis())
            }
            delay(500)
            for (i in 1..3) {
                Log.d("AA", "协程任务2打印第$i 次，时间: " + System.currentTimeMillis())
            }
        }

        Log.d("AA", "主线程 sleep ，时间: " + System.currentTimeMillis())
        Thread.sleep(1000)
        Log.d("AA", "主线程运行，时间: " + System.currentTimeMillis())

        for (i in 1..3) {
            Log.d("AA", "主线程打印第$i 次，时间: " + System.currentTimeMillis())
        }
    }

    private fun test5() {
        GlobalScope.launch(Dispatchers.Unconfined) {
            val deferred = GlobalScope.async {
                delay(1000L)
                Log.d("AA", "This is async ")
                return@async "taonce"
            }

            Log.d("AA", "协程 other start")
            val result = deferred.await()
            Log.d("AA", "async result is $result")
            Log.d("AA", "协程 other end ")
        }

        Log.d("AA", "主线程位于协程之后的代码执行，时间:  ${System.currentTimeMillis()}")
    }

    private fun test6() {
        suspend fun getToken(): String {
            delay(3000)
            Log.d("AA", "getToken 开始执行，时间:  ${System.currentTimeMillis()} ${Thread.currentThread().name}")
            return "ask"
        }

        suspend fun getResponse(token: String): String {
            delay(1000)
            Log.d("AA", "getResponse 开始执行，时间:  ${System.currentTimeMillis()}")
            return "response"
        }

        fun setText(response: String) {
            Log.d("AA", "setText 执行，时间:  ${System.currentTimeMillis()}")
        }

// 运行代码
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("AA", "协程 开始执行，时间:  ${System.currentTimeMillis()}")
            val token = getToken()
            val response = getResponse(token)
            setText(response)
        }
    }


    fun test7() {
        suspend fun getToken(): String {
            delay(3000)
            Log.d("AA", "getToken 开始执行，时间:  ${System.currentTimeMillis()} ${Thread.currentThread().name}")
            return "ask"
        }

        suspend fun getResponse(token: String): String {
            delay(1000)
            Log.d("AA", "getResponse 开始执行，时间:  ${System.currentTimeMillis()} ${Thread.currentThread().name}")
            return "response"
        }

        fun setText(response: String) {
            Log.d("AA", "setText 执行，时间:  ${System.currentTimeMillis()} ${Thread.currentThread().name}")
        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.d("AA", "协程测试 开始执行，线程：${Thread.currentThread().name} ${Thread.currentThread().name}")

            var token = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getToken()
            }.await()

            var response = GlobalScope.async(Dispatchers.Unconfined) {
                return@async getResponse(token)
            }.await()

            setText(response)
        }

        Log.d("AA", "主线程协程后面代码执行，线程：${Thread.currentThread().name} ${Thread.currentThread().name}")
    }

    suspend fun doSomethingUsefulOne(): Int {
        Log.d(TAG, "doSomethingUsefulOne() called ${Thread.currentThread().name}")
        delay(1000L) // 假设我们在这里做了一些有用的事
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        Log.d(TAG, "doSomethingUsefulTwo() called ${Thread.currentThread().name}")

        delay(1000L) // 假设我们在这里也做了一些有用的事
        return 29
    }

}