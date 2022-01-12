package com.example.mvvm_kotlin.Job_scheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.databinding.ActivityJobSchedulerBinding

class JobSchedulerActivity : AppCompatActivity() {

    var index = 0
    lateinit var dataBinding: ActivityJobSchedulerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_job_scheduler)
        dataBinding.onClick = View.OnClickListener {
            when (it.id) {
                R.id.btn -> {
                    //获取系统服务
                    val mJobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                    val nextInt = index++ % 8 + 1

                    val mBuilder = JobInfo.Builder(nextInt, ComponentName(this, MyJobService::class.java))//Builder第一个参数是Job的id，我们可以通过这个id取消任务，第二个参数可以配置要执行任务逻辑的Service
                    Log.d("MyJob", "onCreate() called $nextInt")
                    when (nextInt) {
                        1 -> {
                            // 任务最少延迟时间
                            mBuilder.setMinimumLatency(5000)

                        }
                        2 -> {
                            // 任务deadline，当到期没达到指定条件也会开始执行 //设置任务的截止时间,五秒内一定会执行一次
                            mBuilder.setOverrideDeadline(5000)

                        }
                        3 -> {
                            // 网络条件，默认值NETWORK_TYPE_NONE
//                             必须有网络连接的时候才能执行::NETWORK_TYPE_ANY
//                             无论是否有网络都可以执行::NETWORK_TYPE_NONE
//                             只有wifi情况下才能执行::NETWORK_TYPE_UNMETERED
                            mBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        }
                        4 -> {
                            // 设置充电的状态下才能执行
                            //如果我们执行任务时，手机没有充电则不会执行，当我们插上电源的时候任务会自动开始执行
//                         mBuilder   .setRequiresDeviceIdle(false)// 设备是否空闲
//                         mBuilder   .setPersisted(true) //设备重启后是否继续执行
//                         mBuilder   .setBackoffCriteria(3000,JobInfo.BACKOFF_POLICY_LINEAR) //设置退避/重试策略
                            mBuilder.setRequiresCharging(true)
                        }
                        5 -> {
                            mBuilder.setRequiresDeviceIdle(false)// 设备是否空闲
                        }
                        6 -> {
                            mBuilder.setPersisted(true) //设备重启后是否继续执行

                        }
                        7 -> {
                            //你无法控制任务的执行时间，系统只保证在此时间间隔内，任务最多执行一次。
                            mBuilder.setPeriodic(4000)
                            mBuilder.setPersisted(true)
                        }
                        8 -> {
                            mBuilder.setPersisted(true) //设备重启后是否继续执行 android.permission.RECEIVE_BOOT_COMPLETED权限，否则将会抛出异常。这也很好理解，毕竟持久化任务需要设备重启后，依然能够执行，因此我们需要申明可以接收设备启动广播。
                        }
                        else -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                mBuilder.setImportantWhileForeground(true)
                            }
                        }
                    }

//                    mBuilder.setMinimumLatency(5000)// 任务最少延迟时间
//                    mBuilder.setOverrideDeadline(5000)// 任务deadline，当到期没达到指定条件也会开始执行 //设置任务的截止时间,五秒内一定会执行一次
//                    mBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)// 网络条件，默认值NETWORK_TYPE_NONE
////                                      必须有网络连接的时候才能执行::NETWORK_TYPE_ANY
////                                      无论是否有网络都可以执行::NETWORK_TYPE_NONE
////                                      只有wifi情况下才能执行::NETWORK_TYPE_UNMETERED
//                    mBuilder.setRequiresCharging(true)// 设置充电的状态下才能执行
//                    //如果我们执行任务时，手机没有充电则不会执行，当我们插上电源的时候任务会自动开始执行
//                                                mBuilder.setRequiresDeviceIdle(false)// 设备是否空闲
////                         mBuilder   .setPersisted(true) //设备重启后是否继续执行 android.permission.RECEIVE_BOOT_COMPLETED权限，否则将会抛出异常。这也很好理解，毕竟持久化任务需要设备重启后，依然能够执行，因此我们需要申明可以接收设备启动广播。
////                         mBuilder   .setBackoffCriteria(3000,JobInfo.BACKOFF_POLICY_LINEAR) //设置退避/重试策略
                    var mJobInfo = mBuilder.build()
                    mJobScheduler.schedule(mJobInfo)//开始调度任务执行
                }
                else -> {
                }
            }
        }

    }
}