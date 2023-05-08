package com.tele.rpc.demo.provider;

import com.tele.rpc.registry.model.ServiceInfo;
import com.tele.rpc.registry.zk.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Slf4j
public class Main
{
    public static void main(String[] args)
        throws InterruptedException
    {

        ServiceRegistry serviceRegistry = new ServiceRegistry("127.0.0.1:32772");

        String ip = "127.0.0.1";
        ServiceInfo demoService = new ServiceInfo();
        demoService.setName("demoService");
        demoService.setIp(ip);
        demoService.setPort(8080);
        serviceRegistry.register(demoService);

        new CountDownLatch(1).await();

    }

}
