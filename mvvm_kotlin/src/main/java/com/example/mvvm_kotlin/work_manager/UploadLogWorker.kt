package com.example.mvvm_kotlin.work_manager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.base.baselib.common.utils.ToastUtils

class UploadLogWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG: String = UploadLogWorker::class.java.simpleName


    override fun doWork(): Result {
//        执行成功返回Result.success()
//        执行失败返回Result.failure()
//        需要重新执行返回Result.retry()
        Log.d(TAG, "doWork() called $id")

        //接收外面传递进来的数据
        //接收外面传递进来的数据
        val inputData = inputData.getString("input_data")


        // 任务执行完成后返回数据

        // 任务执行完成后返回数据
        val outputData: Data = Data.Builder().putString("output_data", "Task Success!").build()
        Handler(Looper.getMainLooper()).post(Runnable {
            ToastUtils.showLongMsg(applicationContext, "hahahh ")
        })

        if ("retry".equals(inputData)) {
            Log.d(TAG, "doWork() called ${this.hashCode()} -- $inputData")
            return Result.retry()
        } else if ("fail".equals(inputData)) {
            Log.d(TAG, "doWork() called ${this.hashCode()} --$inputData")
            return Result.failure()
        } else {
            Log.d(TAG, "doWork() called ${this.hashCode()} --$inputData")
            return Result.success(outputData)
        }
//        return Result.success()
    }
}