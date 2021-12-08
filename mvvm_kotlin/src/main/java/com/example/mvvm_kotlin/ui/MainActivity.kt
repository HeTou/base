package com.example.mvvm_kotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var contentView = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .apply {

                }
        contentView.tv.setText("您好")
    }
}