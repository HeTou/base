package com.example.ipcbinder.servicemanager.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.example.ipcbinder.servicemanager.FBinderInterface;
import com.example.ipcbinder.servicemanager.FServiceManager;
import com.example.ipcbinder.servicemanager.annotion.ClassId;
import com.example.ipcbinder.servicemanager.cache.CacheCenter;
import com.example.ipcbinder.servicemanager.request.RequestBean;
import com.example.ipcbinder.servicemanager.request.RequestParamter;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class FBinder {

    public static FBinder getDefault() {
        return ourInstance;
    }

    private static final Gson GSON = new Gson();

    private static final FBinder ourInstance = new FBinder();
    //  进行进程通信
    FBinderInterface fBinderInterface = null;
    CacheCenter cacheCenter = CacheCenter.getInstance();
    Context sContext;

    private void init(Context context) {
        sContext = context.getApplicationContext();
    }

    /***
     * 注册服务的方法
     * @param clazz
     */
    public void register(Class clazz) {
        cacheCenter.register(clazz);
    }


    public <T> T getInstance(Class<T> clazz, Object... parameters) {
//        发邮件
        sendRequest(clazz, null, parameters, FServiceManager.TYPE_GET);
        return getProxy(clazz);
    }

    private <T> T getProxy(Class<T> clazz) {
        T instance = (T) Proxy.newProxyInstance(sContext.getClassLoader(), new Class[]{clazz}, new FBinderProxy(clazz));
        return instance;
    }

    public <T> String sendRequest(Class<T> clazz, Method method, Object[] parameters, int type) {
//        发送邮件
        RequestParamter[] requestParamters = null;
        if (parameters != null && parameters.length > 0) {
            requestParamters = new RequestParamter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = GSON.toJson(parameter);

                RequestParamter requestParamter = new RequestParamter(parameterClassName, parameterValue);
                requestParamters[i] = requestParamter;

            }
        }

        String className = clazz.getAnnotation(ClassId.class).value();
        String methodName = method == null ? "getInstance" : method.getName();
        RequestBean requestBean = new RequestBean(type, className, methodName, requestParamters);
        String req = GSON.toJson(requestBean);
        try {
            String response = fBinderInterface.request(req);
            return response;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    访问进程的方法    多个进程
    public void open(Context context) {
        open(context, context.getPackageName());
    }

    public void open(Context context, String packageName) {
        init(context);
//      打开驱动
        bind(sContext, packageName, FServiceManager.class);
    }

    private void bind(Context context, String packageName, Class<FServiceManager> service) {

        Intent intent;

        FServiceConnection fServiceConnection = new FServiceConnection();

        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent();
        } else {
            ComponentName component = new ComponentName(packageName, service.getName());
            intent = new Intent();
            intent.setComponent(component);
            intent.setAction(service.getName());
        }

        context.bindService(intent, fServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public class FServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            fBinderInterface = FBinderInterface.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


//    open

}
