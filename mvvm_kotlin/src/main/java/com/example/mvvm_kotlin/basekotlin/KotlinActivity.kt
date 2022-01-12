package com.example.mvvm_kotlin.basekotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.databinding.ActivityKotlinBinding
import com.example.mvvm_kotlin.viewmodel.KotlinViewModel

/***
 * 基础操作
 */
class KotlinActivity : AppCompatActivity() {

    val TAG = this::class.java.name

    lateinit var databing: ActivityKotlinBinding
    lateinit var viewmodel: KotlinViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(KotlinViewModel::class.java)
        viewmodel.adapter.onItemClickListener = object : KotlinAdapter.OnItemClickListener {
            override fun onItemClicked(view: View, position: Int) {
                val item = viewmodel.itemList.get(position)
                if (item.clazz != null) {
                    val intent = Intent(this@KotlinActivity, item.clazz)
                    startActivity(intent)
                }
            }
        }
        viewmodel.listData.observe(this, Observer {
            Log.d(TAG, "onCreate() called ${it}")
        })
        databing = DataBindingUtil.setContentView(this, R.layout.activity_kotlin)
        databing.rlv.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = viewmodel.adapter

        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn -> {
                viewmodel.addItem()
            }
            else -> {
            }
        }
    }
}