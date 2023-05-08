package com.tele.rpc.registry;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@UtilityClass
public class LocalServiceHolder
{
    private static final Map<String, Object> serviceInfoMap = new ConcurrentHashMap<>();

    public static void addService(String serviceName, Object service)
    {
        serviceInfoMap.putIfAbsent(serviceName, service);
    }

    public static Object getService(String serviceName)
    {
        return serviceInfoMap.get(serviceName);
    }

}
