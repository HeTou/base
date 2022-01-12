package com.example.mvvm_kotlin.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_kotlin.basekotlin.CtrlStreamActivity
import com.example.mvvm_kotlin.basekotlin.InlineActivity
import com.example.mvvm_kotlin.basekotlin.KotlinAdapter
import com.example.mvvm_kotlin.basekotlin.PropertiesDelegateActivity
import com.example.mvvm_kotlin.common.bean.Item

class KotlinViewModel(application: Application) : AndroidViewModel(application) {


    var index = 0
    var adapter: KotlinAdapter
    var itemList: MutableList<Item> = ArrayList()
    var listData: MutableLiveData<MutableList<Item>> = MutableLiveData(itemList)

    init {
        itemList.apply {
            itemList.add(Item("ctrl_stream", "控制流", CtrlStreamActivity::class.java))
            itemList.add(Item("return_break", "返回与跳转", CtrlStreamActivity::class.java))
            itemList.add(Item("properties_delegate", "属性委托", PropertiesDelegateActivity::class.java))
            itemList.add(Item("properties_delegate", "内联", InlineActivity::class.java))
        }

        adapter = KotlinAdapter(application, itemList)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem() {
        for (i in 1..3) {
            index++

            itemList.add(Item(index.toString(), index.toString(), null))
        }
        listData.value = itemList

        adapter.notifyDataSetChanged()
    }
}
