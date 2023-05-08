package com.tele.rpc.core;

import lombok.experimental.UtilityClass;

import java.util.Properties;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@UtilityClass
public class ApplicationConfig
{
    private static Properties PROPERTIES;

    public static void init(Properties properties)
    {
        PROPERTIES = properties;
    }

    public static String get(String key)
    {
        return PROPERTIES.getProperty(key);
    }

}
