package com.tele.rpc.core.proxy;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author tele
 * @Description
 * @since 2023-05-06
 */
@UtilityClass
public class ProxyFactory
{
    public static <T> T getProxy(Class<T> interfaces, Map<String,String> params)
    {
        return (T)Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces}, new ProxyHandler(params));
    }
}
