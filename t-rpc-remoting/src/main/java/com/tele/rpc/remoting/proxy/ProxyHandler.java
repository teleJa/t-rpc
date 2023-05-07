package com.tele.rpc.remoting.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author tele
 * @Description
 * @since 2023-05-06
 */
@Slf4j
public class ProxyHandler implements InvocationHandler
{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        log.debug("ready to invoke remoting service");
        // 寻找服务

        // rpc调用

        return null;
    }


}
