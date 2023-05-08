package com.tele.rpc.registry.zk;

import com.tele.rpc.registry.model.ServiceInfo;
import com.tele.rpc.registry.zk.constants.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Slf4j
public class ServiceRegistry
{
    private final ServiceDiscovery<ServiceInfo> serviceDiscovery;

    private final ConcurrentHashMap<String,ServiceProvider<ServiceInfo>> providers = new ConcurrentHashMap<>();

    public ServiceRegistry(String address) {
        this(address,true);
    }

    // TODO 实例构建
    public ServiceRegistry(String address,boolean start)
    {
        CuratorFramework client = CuratorFrameworkFactory.builder().retryPolicy(new ExponentialBackoffRetry(100,2)).connectString(address).build();
        if(start) {
            client.start();
            try
            {
                client.blockUntilConnected(10, TimeUnit.SECONDS);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
            .serializer(new JsonInstanceSerializer<>(ServiceInfo.class))
            .client(client)
            .basePath(Paths.basePath)
            .watchInstances(true)
            .build();
    }

    public boolean register(ServiceInfo serviceInfo) {
        try
        {
            ServiceInstance<ServiceInfo> instance = ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getName())
                .address(serviceInfo.getIp())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();
            serviceDiscovery.registerService(instance);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error("register service error", e);
        }
        return true;
    }

    public ServiceInfo findService(String serviceName)
    {
        ServiceProvider<ServiceInfo> serviceProvider =
            providers.computeIfAbsent(serviceName, this::buildProvider);
        try
        {
            serviceProvider.start();
            return serviceProvider.getInstance().getPayload();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException("no service available");
    }

    private ServiceProvider<ServiceInfo> buildProvider(String serviceName)
    {
        return serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).providerStrategy(new RandomStrategy<>()).build();
    }

}
