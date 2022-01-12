package com.example.androidproxy;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidproxy.annotion.ContentView;
import com.example.androidproxy.annotion.EventBase;
import com.example.androidproxy.annotion.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtil {

    private static final String TAG = "Inject";

    /***
     * 注入方法
     * @param obj
     */
    public static void inject(Object obj) {

//        setContentView()
        injectLayout(obj);
        injectView(obj);
        injectClick(obj);
    }

    private static void injectClick(Object obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();

            if (annotations.length == 0) {
                continue;
            }

            for (Annotation annotation : annotations) {

                Class<? extends Annotation> aClass = annotation.annotationType();
                EventBase annotationEventBase = aClass.getAnnotation(EventBase.class);
                if (annotationEventBase == null) {
                    continue;
                }
                String listenerSet = annotationEventBase.listenerSet();
                Class<?> listenerType = annotationEventBase.listenerType();
                String callbackMethod = annotationEventBase.callbackMethod();


                try {
                    Method valueMethod = aClass.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    for (int viewId : viewIds) {
//                        有多少个按钮的id 就反射多少次
                        Method findViewByIdMethod = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewByIdMethod.invoke(obj, viewId);

                        Method setLinstenerMethod = view.getClass().getMethod(listenerSet, listenerType);

                        Object instance = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, new ListenerInvocationHandler(obj, method));

                        setLinstenerMethod.invoke(view, instance);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static void injectView(@NonNull Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ViewInject annotation = field.getAnnotation(ViewInject.class);
            if (annotation == null) {
                continue;
            }
            int value = annotation.value();
            if (obj instanceof Activity) {
                Activity activity = (Activity) obj;
                View viewById = activity.findViewById(value);
                try {
                    field.set(obj, viewById);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (obj instanceof Fragment) {
                Fragment fragment = (Fragment) obj;
                View view = fragment.getView();
                View viewById = view.findViewById(value);
                try {
                    field.set(obj, viewById);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectLayout(Object obj) {
        ContentView annotation = obj.getClass().getAnnotation(ContentView.class);

        if (annotation != null) {
            int value = annotation.value();
            if (obj instanceof Activity) {
                Activity activity = (Activity) obj;
                activity.setContentView(value);
            }
        }
    }
}
