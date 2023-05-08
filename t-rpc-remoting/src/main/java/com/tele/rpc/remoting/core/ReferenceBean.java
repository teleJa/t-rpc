package com.tele.rpc.remoting.core;

import com.tele.rpc.remoting.proxy.ProxyFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tele.rpc.remoting.constants.ReferenceConstants.REGISTER_ADDRESS;
import static com.tele.rpc.remoting.constants.ReferenceConstants.SERVICE_NAME;

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
            stub = ProxyFactory.getProxy(interfaces, getParameters());
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

    public Map<String,String> getParameters()
    {
        Map<String,String> params = new HashMap<>();
        params.put(REGISTER_ADDRESS,registryAddress);
        params.put(SERVICE_NAME,interfaces.getName());
        return params;
    }

}
