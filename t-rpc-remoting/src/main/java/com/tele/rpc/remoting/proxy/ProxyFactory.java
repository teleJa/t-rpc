package com.tele.rpc.remoting.proxy;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;

/**
 * @author tele
 * @Description
 * @since 2023-05-06
 */
@UtilityClass
public class ProxyFactory
{
    public static <T> T getProxy(Class<T> interfaces)
    {
        return (T)Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces}, new ProxyHandler());
    }
}
