package com.tele.rpc.registry.zk;

import com.tele.rpc.registry.model.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.discovery.ServiceProvider;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Slf4j
public class ServiceProviderWrapper
{

    private final ServiceProvider<ServiceInfo> serviceProvider;

    public ServiceProviderWrapper(ServiceProvider<ServiceInfo> serviceProvider)
    {
        this.serviceProvider = serviceProvider;
        try
        {
            this.serviceProvider.start();
        }
        catch (Exception e)
        {
            log.error("service provider start error", e);
        }
    }

    public ServiceProvider<ServiceInfo> provider()
    {
        return serviceProvider;
    }

}
