package com.tele.rpc.core;

import com.tele.rpc.core.constants.RpcConstants;
import com.tele.rpc.core.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tele
 * @Description
 * @since 2023-05-06
 */
@Slf4j
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
            log.error("latch await error", e);
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
        params.put(RpcConstants.REGISTER_ADDRESS,registryAddress);
        params.put(RpcConstants.SERVICE_NAME,interfaces.getName());
        return params;
    }

    public Class<T> getInterfaces()
    {
        return interfaces;
    }

    public void setInterfaces(Class<T> interfaces)
    {
        this.interfaces = interfaces;
    }

    public String getRegistryAddress()
    {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress)
    {
        this.registryAddress = registryAddress;
    }
}
