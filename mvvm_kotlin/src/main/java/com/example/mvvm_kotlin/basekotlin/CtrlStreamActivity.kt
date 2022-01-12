package com.example.mvvm_kotlin.basekotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.databinding.ActivityCtrlstreamBinding

class CtrlStreamActivity : AppCompatActivity() {

    private val TAG = this::class.java.name
    lateinit var databing: ActivityCtrlstreamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databing = DataBindingUtil.setContentView(this, R.layout.activity_ctrlstream)


    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_if -> {
//             在kotln里 if-else函数可以返回值
                var a = 2
                var b = 3;
                var c = if (a > b) {
                    println("a>b=${a > b}")
                    4
                } else {
                    println("a>b=${a > b}")
                    5
                }
                Log.d(TAG, "if函数的值 ${c}")
            }
            R.id.btn_when -> {

            }
            R.id.btn_for -> {

            }
            R.id.btn_while -> {

            }
            else -> {
            }
        }
    }
}