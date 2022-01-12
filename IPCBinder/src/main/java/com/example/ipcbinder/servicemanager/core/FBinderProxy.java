package com.example.ipcbinder.servicemanager.core;

import android.text.TextUtils;

import com.example.ipcbinder.servicemanager.FServiceManager;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FBinderProxy implements InvocationHandler {

    private Class clazz;
    private static final Gson GSON = new Gson();

    public FBinderProxy(Class clazz) {

        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String data = FBinder.getDefault().sendRequest(clazz, method, args, FServiceManager.TYPE_INVOKE);

        if (!TextUtils.isEmpty(data)) {
            Object o = GSON.fromJson(data, method.getReturnType());

            return o;
        } else {
            return null;
        }
    }
}
