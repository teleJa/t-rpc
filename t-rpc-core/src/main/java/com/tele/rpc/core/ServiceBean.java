package com.tele.rpc.core;

import com.tele.rpc.registry.LocalServiceHolder;
import com.tele.rpc.registry.model.ServiceInfo;
import com.tele.rpc.registry.zk.ServiceRegistry;
import com.tele.rpc.remoting.transport.server.NettyServer;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.tele.rpc.core.constants.RpcConstants.BIND_IP;
import static com.tele.rpc.core.constants.RpcConstants.BIND_PORT;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Getter
@Setter
public class ServiceBean<T>
{
    private String serviceName;

    private String registerAddress;

    private Class<T> clazz;

    private T ref;

    private String version;

    private final AtomicBoolean exported = new AtomicBoolean(false);

    public void export()
    {
        if(exported.compareAndSet(false,true)) {

            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setName(serviceName);
            String ip = ApplicationConfig.get(BIND_IP);
            int port = Integer.parseInt(ApplicationConfig.get(BIND_PORT));
            serviceInfo.setIp(ip);
            serviceInfo.setPort(port);
            serviceInfo.setVersion(version);
            new ServiceRegistry(registerAddress).register(serviceInfo);
            LocalServiceHolder.addService(serviceName,ref);
            new NettyServer(ip,port).start();
        }
    }

}
