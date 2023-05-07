package com.tele.rpc.remoting.core;

import com.tele.rpc.remoting.proxy.ProxyFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tele
 * @Description
 * @since 2023-05-06
 */
public class ReferenceBean<T>
{
    private Class<T> interfaces;

    private String registryAddress;

    private final AtomicBoolean inited = new AtomicBoolean(false);

    private final CountDownLatch latch = new CountDownLatch(1);

    private volatile T stub;

    public T getService()
    {
        if (stub != null)
        {
            return stub;
        }
        if (inited.compareAndSet(false, true))
        {
            // 生成代理类
            stub = ProxyFactory.getProxy(interfaces);
            latch.countDown();
        }
        try
        {
            latch.await(1, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        if (stub == null)
        {
            throw new RuntimeException("rpc stub init fail");
        }
        return stub;
    }

}
