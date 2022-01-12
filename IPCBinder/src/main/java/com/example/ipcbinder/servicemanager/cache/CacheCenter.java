package com.example.ipcbinder.servicemanager.cache;

import com.example.ipcbinder.servicemanager.request.RequestBean;
import com.example.ipcbinder.servicemanager.request.RequestParamter;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CacheCenter {
    private static final CacheCenter ourInstance = new CacheCenter();

    public static CacheCenter getInstance() {

        return ourInstance;
    }

    private final ConcurrentHashMap<String, Object> mInstanceObjMap;


    /***
     * hashMap在处理并发情况容易出问题。
     * ConcurrentHashMap并发容器
     */
    private final ConcurrentHashMap<String, Class> mClassMap;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Method>> mAllMethodMap;

    public void putObject(String className, Object object) {

        mInstanceObjMap.put(className, object);
    }

    public CacheCenter() {
        mClassMap = new ConcurrentHashMap<>();
        mAllMethodMap = new ConcurrentHashMap<>();
        mInstanceObjMap = new ConcurrentHashMap<>();
    }


    /***
     * 注册类
     * @param clazz
     */
    public void register(Class clazz) {
        registerClass(clazz);
        registerMethod(clazz);
    }

    /***
     * 缓存服务类的方法
     * @param clazz
     */
    private void registerMethod(Class clazz) {

        Method[] methods = null;
        methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            ConcurrentHashMap<String, Method> map = mAllMethodMap.get(clazz.getName());
            if (map == null) {
                map = new ConcurrentHashMap<>();
                mAllMethodMap.put(clazz.getName(), map);
            }
            String key = getMethodParameters(method);
            map.put(key, method);
        }
    }

    private void registerClass(Class clazz) {
        String className = clazz.getName();
        mClassMap.put(className, clazz);
    }

    public Method getMethod(RequestBean requestBean) {

        ConcurrentHashMap<String, Method> map = mAllMethodMap.get(requestBean.getClassName());
        if (map != null) {
            String key = getMethodParameters(requestBean);
            return map.get(key);
        }
        return null;
    }

    /***
     * 获取方法签名
     * @param method
     * @return
     */
    private String getMethodParameters(Method method) {

//      方法签名：方法名-参数类型
//      getMethodParameters-Method
        StringBuilder result = new StringBuilder();
        result.append(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        int length = parameterTypes.length;
        if (length == 0) {
            return result.toString();
        }
        for (int i = 0; i < length; i++) {
            result.append("-").append(parameterTypes[i].getName());
        }
        return result.toString();
    }

    /***
     * 获取方法签名
     * @return
     */
    private String getMethodParameters(RequestBean requestBean) {

//      方法签名：方法名-参数类型
//      getMethodParameters-Method
        StringBuilder result = new StringBuilder();
        result.append(requestBean.getMethodName());

        RequestParamter[] requestParamters = requestBean.getRequestParamters();

        if (requestParamters == null || requestParamters.length == 0) {
            return result.toString();
        }
        int length = requestBean.getRequestParamters().length;
        if (length == 0) {
            return result.toString();
        }
        for (int i = 0; i < length; i++) {
            result.append("-").append(requestParamters[i].getParameterClassName());
        }
        return result.toString();
    }

    public Class getClassType(String parameterClassName) {
        try {
            Class<?> clazz = Class.forName(parameterClassName);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject(String className) {
        return mInstanceObjMap.get(className);
    }
}

