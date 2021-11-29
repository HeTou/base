package com.base.baselib.devmode.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationHandler implements InvocationHandler {
    Object object;

    public ProxyInvocationHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("invoke() called with: proxy = ["  + "], method = [" + method + "], args = [" + args + "]");
        Object invoke = method.invoke(object, args);
        return invoke;
    }
}
    