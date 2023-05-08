package com.tele.rpc.remoting.transport.serialize;

import lombok.experimental.UtilityClass;

import java.util.ServiceLoader;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@UtilityClass
public class SerializeSupport
{
    private static Serializer SERIALIZER;

    static
    {
        ServiceLoader<Serializer> load = ServiceLoader.load(Serializer.class);
        for (Serializer serializer : load)
        {
            SERIALIZER = serializer;
        }
    }

    public static <T> byte[] serialize(T t)
    {
        return SERIALIZER.serialize(t);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz)
    {
        return SERIALIZER.deserialize(bytes,clazz);
    }

}
