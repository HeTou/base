package com.example.ipcbinder.servicemanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.example.ipcbinder.servicemanager.cache.CacheCenter;
import com.example.ipcbinder.servicemanager.request.RequestBean;
import com.example.ipcbinder.servicemanager.request.RequestParamter;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FServiceManager extends Service {

    /***得到单例*/
    public static final int TYPE_GET = 1;
    /***调用方法*/
    public static final int TYPE_INVOKE = 2;


    Gson gson = new Gson();


    CacheCenter cacheCenter = CacheCenter.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    FBinderInterface.Stub mBinder = new FBinderInterface.Stub() {

        @Override
        public String request(String requst) throws RemoteException {

            RequestBean requestBean = gson.fromJson(requst, RequestBean.class);

            int type = requestBean.getType();
            switch (type) {
                case TYPE_GET:
                    Method method = cacheCenter.getMethod(requestBean);
                    try {
                        Object[] objects = makeParameterObject(requestBean);
//                        单例获取
                        Object invoke = method.invoke(null, objects);
                        cacheCenter.putObject(requestBean.getClassName(), invoke);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case TYPE_INVOKE:
                    Object object = cacheCenter.getObject(requestBean.getClassName());

                    Method tempMethod = cacheCenter.getMethod(requestBean);

                    Object[] parameters = makeParameterObject(requestBean);

                    try {
                        Object invoke = tempMethod.invoke(object, parameters);

                        return gson.toJson(invoke);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return null;
        }
    };


    private Object[] makeParameterObject(RequestBean requestBean) {
        Object[] mParameters = null;
        RequestParamter[] requestParamters = requestBean.getRequestParamters();

        if (requestParamters != null && requestParamters.length > 0) {
            mParameters = new Object[requestParamters.length];
            for (int i = 0; i < requestParamters.length; i++) {
                RequestParamter requestParamter = requestParamters[i];
                Class clazz = cacheCenter.getClassType(requestParamter.getParameterClassName());

                mParameters[i] = gson.fromJson(requestParamter.getParameterValue(), clazz);
            }
        } else {
            mParameters = new Object[0];
        }
        return mParameters;
    }
}
