package com.base.baselib;

import static org.junit.Assert.assertEquals;

import com.base.baselib.devmode.proxy.ProxyInvocationHandler;
import com.base.baselib.devmode.proxy.ProxyInterface;
import com.base.baselib.devmode.proxy.ProxyObject;

import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void proxy() {
        ProxyObject proxyObject = new ProxyObject();
        ProxyInterface o = (ProxyInterface) Proxy.newProxyInstance(proxyObject.getClass().getClassLoader(), proxyObject.getClass().getInterfaces(), new ProxyInvocationHandler(proxyObject));
        o.goHome();
        o.shopping("hahha");
    }
}