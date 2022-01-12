package com.example.classloader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/***
 *
 * todo 注意
 * todo 注意
 * todo 注意
 * todo 注意
 *
 *  这里的hook修改是适合低版本的 26以下，高版本修改了就无效了
 *
 */
public class HookUtil {

    public static String TARGET_INTENT = "target_intent";

//    hook AMS流程 --startActivity方法

    /***
     * 插件化hook原理：intent.setclassName("包名","插件中的全类名")
     * 1、hook AMS的startActivity方法
     * 将intent改为intent.setClassName("包名","宿主中的全类名（代理类名）")
     * 2、当AMS过程执行结束，在将intent还原
     * 将intent改为intent.setClassName("包名","将插件中的全类名")
     */

    public static void hookAMS() {
//        1、反射
        try {
            Object singleton;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Class<?> aClass = Class.forName("android.app.ActivityManager");
                Field singletonField = aClass.getDeclaredField("IActivityManagerSingleton");
                singletonField.setAccessible(true);
                singleton = singletonField.get(null);
            } else {
                Class<?> aClass = Class.forName("android.app.ActivityManagerNative");
                Field singletonField = aClass.getDeclaredField("gDefault");
                singletonField.setAccessible(true);
                singleton = singletonField.get(null);
            }

            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

//            这里获取ActivityManagerService即 AMS
            Object mInstance = mInstanceField.get(singleton);

//            动态代理
            Class<?> IActivityMangerClass = Class.forName("android.app.IActivityManager");

            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IActivityMangerClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                    这里去该startActivity方法
                    if (method.getName().equals("startActivity")) {

                        int index = 0;
                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof Intent) {
                                index = i;
                                break;
                            }
                        }
                        //这里获取的是ams中的intent
                        Intent intent = (Intent) args[index];
//                        新建intent来欺骗 ams
                        Intent proxyIntent = new Intent();
                        proxyIntent.setClassName("com.example.classloader", ProxyNewActivity.class.getName());
//                        在ams流程结束的时候，后期还需要还原

                        proxyIntent.putExtra(TARGET_INTENT, intent);
                        args[index] = proxyIntent;
                    }
                    return method.invoke(mInstance, args);
                }
            });

            mInstanceField.set(singleton, proxyInstance);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    public static void hookHandler() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object activityThread = sCurrentActivityThreadField.get(null);
//          获取mH的hanglder对象
            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Object mH = mHField.get(activityThread);

//          获取handler.callback对象
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            mCallbackField.set(mH, new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {

                    switch (msg.what) {
                        case 100:
//                          26版本

                            try {
                                /***
                                 * 去给我们的msg重新赋值
                                 */
                                Class<?> aClass = msg.obj.getClass();

                                Field intentField = aClass.getDeclaredField("intent");
                                intentField.setAccessible(true);

//                              代理的intent
                                Intent proxyIntent = (Intent) intentField.get(msg.obj);
//                              插件的intent
                                Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
//                              插件的intent 替换代理的intent

                                if (intent == null) {
                                    intent = new Intent();
                                    intent.setClassName("com.example.classloader", "com.example.classloader.ProxyNewActivity");
                                }
//                                intent.setComponent(proxyIntent.getComponent());
                                intentField.set(msg.obj, intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 159:
//                            26版本以上

                            try {
//                                1、找到hook点，26以后，根据EXECUTE_TRANSACTION消息指令，找到了ClientTransaction里面的
//                                 mActivityCallbacks的成员变量里面的元素ClientTransationItem的子类LaunchActivityItem中
//                                有一个intent，即为反射的intent，我们只需要改变成我们自己的intent就可以了
                                Class<?> ClientTransactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                                Field mActivityCallbacksField = ClientTransactionClass.getDeclaredField("mActivityCallbacks");
                                mActivityCallbacksField.setAccessible(true);

                                List activityCallbacks = (List) mActivityCallbacksField.get(msg.obj);
                                for (int i = 0; i < activityCallbacks.size(); i++) {
                                    if (activityCallbacks.get(i).getClass().getName().equals("android.app.servertransaction.LaunchActivityItem")) {
                                        Object launchActivityItem = activityCallbacks.get(i);
                                        Field mIntentField = launchActivityItem.getClass().getDeclaredField("mIntent");
                                        mIntentField.setAccessible(true);
//                                      代理的intent
                                        Intent proxyIntent = (Intent) mIntentField.get(launchActivityItem);
//                                      插件的intent
                                        Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                                        if (intent != null) {
                                            mIntentField.set(launchActivityItem, intent);
                                        }
                                        break;
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    return false;
                }
            });

            Object callback = mCallbackField.get(mH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
