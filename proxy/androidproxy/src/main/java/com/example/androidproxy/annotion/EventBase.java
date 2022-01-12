package com.example.androidproxy.annotion;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface EventBase {
    String listenerSet();

    /***
     * 事件监听的类型
     * @return
     */
    Class<?> listenerType();

    /***
     * 事件被触发之后，执行的回调方法的名称
     * @return
     */
    String callbackMethod();
}
