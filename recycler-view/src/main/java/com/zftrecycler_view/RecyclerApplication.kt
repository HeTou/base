package com.zftrecycler_view

import android.app.Application
import com.base.baselib.common.utils.Utils

class RecyclerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}