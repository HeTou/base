package com.example.poet_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//正对类有效
@Target(ElementType.TYPE)
//生命周期，针对编译时期
@Retention(RetentionPolicy.CLASS)
public @interface Route {
    String value();
}
