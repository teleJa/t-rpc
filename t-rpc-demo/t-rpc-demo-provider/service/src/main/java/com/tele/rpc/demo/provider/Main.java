package com.tele.rpc.demo.provider;

import com.tele.rpc.core.ApplicationConfig;
import com.tele.rpc.core.ServiceBean;
import com.tele.rpc.core.constants.RpcConstants;
import com.tele.rpc.demo.provider.demo.DemoService;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
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

        Properties properties = new Properties();
        properties.setProperty(RpcConstants.BIND_IP,"192.168.1.103");
        properties.setProperty(RpcConstants.BIND_PORT,"8080");

        ApplicationConfig.init(properties);

        ServiceBean<DemoService> serviceBean = new ServiceBean<>();
        serviceBean.setServiceName("com.tele.rpc.demo.provider.demo.DemoService");
        serviceBean.setVersion("1.0");
        serviceBean.setClazz(DemoService.class);
        serviceBean.setRef(new DemoServiceImpl());
        serviceBean.setRegisterAddress("127.0.0.1:32772");
        serviceBean.export();

        new CountDownLatch(1).await();

    }

}
