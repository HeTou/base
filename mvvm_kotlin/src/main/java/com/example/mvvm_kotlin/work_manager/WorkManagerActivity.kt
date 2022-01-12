package com.example.mvvm_kotlin.work_manager

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.databinding.ActivityWorkManagerBinding
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequest


class WorkManagerActivity : AppCompatActivity() {

    lateinit var dataBinding: ActivityWorkManagerBinding

    var index = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_manager)
    }

    fun send(view: View) {
        when (view.id) {
            R.id.btn_send -> {

                val constraints: Constraints = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Constraints.Builder()
                            //是否在充电状态下执行任务。
                            .setRequiresCharging(true)
                            //指明设备是否为空闲时是否启动任务。
//                            .setRequiresDeviceIdle(true)
                            //设置存储容量不足是否执行。
//                            .setRequiresStorageNotLow(true)
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            //设置电量低是否执行
//                            .setRequiresBatteryNotLow(true)

                            .build()
                } else {
                    Constraints.Builder()
                            //设置充电时是否执行。
                            .setRequiresCharging(true)
                            //设置存储容量不足是否执行。
                            .setRequiresStorageNotLow(true)
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            //设置电量低是否执行
                            .setRequiresBatteryNotLow(true)
                            .build()
                }

//                Data只能用于传递一些小的基本类型数据，且数据最大不能超过10kb。

                val dataBuilder = Data.Builder();
                dataBuilder.putString("input_data", "Hello World!")


                val builder = OneTimeWorkRequest.Builder(UploadLogWorker::class.java);


                val builder2 = PeriodicWorkRequest.Builder(UploadLogWorker::class.java, 15, TimeUnit.MINUTES)




                builder.setConstraints(constraints) //设置触发条件
                var i = index++ % 5 + 1

                Log.d("UploadLogWorker", "send() called with: view = $i")
                when (i) {
                    1 -> {
                        //符合触发条件后，延迟10秒执行
                        builder.setInitialDelay(2, TimeUnit.SECONDS)
                    }
                    2 -> {
                        //设置指数退避算法
//                        Worker的doWork()方法中返回Result.retry()，系统会有默认的指数退避策略来帮你重试任务，
//                        你也可以通过setBackoffCriteria()方法，自定义指数退避策略。
//                        builder.setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        dataBuilder.putString("input_data", "retry")
                    }
                    3 -> {
                        dataBuilder.putString("input_data", "fail")
                    }
                    4 -> {
                    }
                    5 -> {
                    }
                }

                builder.setInputData(dataBuilder.build())
                builder2.setInputData(dataBuilder.build())

                if (index in 1..3) {
                    val uploadWorkRequest = builder

                            .addTag("uploadTag")  //设置标签便于追踪
                            .build()
                    WorkManager.getInstance(this).enqueue(uploadWorkRequest);
                    WorkManager.getInstance(this).getWorkInfoByIdLiveData(uploadWorkRequest.id).observe(this, Observer<WorkInfo?>() {
                        fun onChanged(workInfo: WorkInfo) {
                            Log.d("onChanged()->", "workInfo:$workInfo")
                        }
                    })
                } else {
                    val uploadWorkRequest = PeriodicWorkRequest.Builder(UploadLogWorker::class.java, 15, TimeUnit.MINUTES)
//                            .setConstraints(constraints)
                            .addTag("uploadTag2")
                            .build()
                    WorkManager.getInstance(this).enqueue(uploadWorkRequest);
                    WorkManager.getInstance(this).getWorkInfoByIdLiveData(uploadWorkRequest.id).observe(this, Observer<WorkInfo?>() {
                        fun onChanged(workInfo: WorkInfo) {
                            Log.d("onChanged()->", "workInfo:$workInfo")
                        }
                    })
                }
            }
            else -> {
            }
        }
    }
}