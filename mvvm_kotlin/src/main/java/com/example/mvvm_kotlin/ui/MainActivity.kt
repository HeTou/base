package com.example.mvvm_kotlin.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.baselib.common.utils.ToastUtils
import com.example.mvvm_kotlin.Job_scheduler.JobSchedulerActivity
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.basekotlin.KotlinActivity
import com.example.mvvm_kotlin.basekotlin.KotlinAdapter
import com.example.mvvm_kotlin.common.bean.UserModel
import com.example.mvvm_kotlin.common.db.MRoomDatabase
import com.example.mvvm_kotlin.coroutines.CoroutinesActivity
import com.example.mvvm_kotlin.databinding.ActivityMainBinding
import com.example.mvvm_kotlin.viewmodel.MainViewModel
import com.example.mvvm_kotlin.work_manager.WorkManagerActivity

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .apply {

                }
        binding.name = "安静的咖啡了解了撒"
//        contentView.tv.setText(Constants.httpUrl)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MainViewModel::class.java)

        mainViewModel!!.users?.observe(this, Observer {
            println("发生变化了${it}")
            binding.name = "请求到数据了，兄弟e"
        })

        mainViewModel.text.observe(this, Observer {
            binding.name = it
        })
    }

    fun get(view: View) {
        when (view.id) {
            R.id.btn -> {
                ToastUtils.showLongMsg(this, binding.tv.text.toString())

            }
            R.id.database -> {
                mainViewModel.dao()

                mainViewModel.requestNet()
            }
            R.id.lifecycle -> {
                val users = mainViewModel.users
            }
            R.id.job_schduler -> {
                val intent = Intent(this, JobSchedulerActivity::class.java)
                startActivity(intent)
            }
            R.id.work_manager -> {
                val intent = Intent(this, WorkManagerActivity::class.java)
                startActivity(intent)
            }
            R.id.coroutines -> {
                val intent = Intent(this, CoroutinesActivity::class.java)
                startActivity(intent)
            }

            R.id.base_kotlin -> {
                val intent = Intent(this, KotlinActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }
}