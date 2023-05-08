package com.tele.rpc.demo.provider;

import com.tele.rpc.demo.provider.demo.DemoService;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
public class DemoServiceImpl implements DemoService
{
    @Override
    public String sayHello(String name)
    {
        return "hello rpc";
    }
}
