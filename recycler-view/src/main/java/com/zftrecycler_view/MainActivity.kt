package com.zftrecycler_view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.baselib.RxBaseActivity

class MainActivity : RxBaseActivity() {

    private lateinit var rlv: RecyclerView

    override fun contentLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        super.initUI()
        rlv = findViewById<RecyclerView>(R.id.rlv)
        rlv.layoutManager = LinearLayoutManager(this)
        rlv.addItemDecoration(CertProgressItemDecoration())
        rlv.adapter = MainAdapter(this)
    }
}