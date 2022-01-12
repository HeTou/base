package com.example.mvvm_kotlin.Job_scheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.google.gson.Gson

class MyJobService : JobService() {

    private val TAG = MyJobService::class.java.simpleName

    /***
     *定义：Job开始的时候的回调，实现实际的工作逻辑。
     *@return 注意：如果返回false的话，系统会自动结束本job；即后续不会调用onStopJob
     */
    override fun onStartJob(params: JobParameters?): Boolean {

        Log.d(TAG, "onStartJob() called with: params = ${Gson().toJson(params)}")

//        Job的任务执行完毕后，APP端自己调用，用以通知JobScheduler已经完成了任务。
//        注意:该方法执行完后不会回调onStopJob(),但是会回调onDestroy()
//        当wantsReschedule参数设置为true时，表示任务需要另外规划时间进行执行。
//        而这个执行的时间受限与JobInfo的退避规则。
        jobFinished(params, false)
        return true
    }

    /***
     *定义：停止该Job。当JobScheduler发觉该Job条件不满足的时候，或者job被抢占被取消的时候的强制回调。
     *@return 注意:如果想在这种意外的情况下让Job重新开始，返回值应该设置为true。
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob() called with: params = ${Gson().toJson(params)}")
        return true
    }


}