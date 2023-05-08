package com.tele.rpc.demo.consumer;

import com.tele.rpc.core.ReferenceBean;
import com.tele.rpc.demo.provider.demo.DemoService;
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
        String address = "127.0.0.1:32772";
        ReferenceBean<DemoService> referenceBean = new ReferenceBean<>();
        referenceBean.setInterfaces(DemoService.class);
        referenceBean.setRegistryAddress(address);

        DemoService service = referenceBean.getService();

        for (int i = 0; i < 3; i++)
        {
            service.sayHello("hello");
        }

        new CountDownLatch(1).await();
    }
}
