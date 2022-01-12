package com.example.proxy2;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

public class MyClass {
    public static void main(String[] args) throws Exception {

        /**** 这里输出相当于 proxy.newinstance 所生成的class文件 */
        byte[] fentaoHelloImpls = ProxyGenerator.generateProxyClass("FentaoHelloImpl", new Class[]{HelloInterface.class});

        File file = new File("E:\\Android\\zft\\base\\proxy2\\src\\main\\java\\com\\example\\proxy2\\FentaoHelloImpl.class");

        FileOutputStream ios = new FileOutputStream(file);
        ios.write(fentaoHelloImpls);
        ios.flush();
        ios.close();
    }

    public static void main1(String[] args) {
        Hello hello = new Hello();
        HelloInterface helloInterface = (HelloInterface) Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), new H());
        helloInterface.sayHello();
    }

    public static class H implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}