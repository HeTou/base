package com.example.proxy2;

public class Hello implements HelloInterface {
    @Override
    public void sayHello() {
        System.out.println("Hello world");
    }
}
