package com.tele.rpc.registry.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author tele
 * @Description 接口级
 * @date 2023-05-08
 */
@Getter
@Setter
@ToString
public class ServiceInfo
{

    private String ip;

    private int port;

    private String name;

    private String version = "1.0";

    public ServiceInfo()
    {
    }


}
