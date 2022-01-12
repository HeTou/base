package com.example.androidproxy.annotion;


import android.view.View;

@EventBase(listenerSet = "setOnClickListener", listenerType = View.OnClickListener.class, callbackMethod = "onClick")
public @interface OnLongClick {
    int[] value();
}
