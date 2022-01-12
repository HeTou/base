package com.example.androidproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ListenerInvocationHandler implements InvocationHandler {
    private Object obj;
    private Method activityMethod;

    public ListenerInvocationHandler(Object obj, Method method) {
        this.obj = obj;
        this.activityMethod = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return activityMethod.invoke(obj,args);
    }
}
