package com.tele.rpc.remoting.transport.serialize;

/**
 * @author tele
 * @Description
 * @create 2020-05-29
 */
public interface Serializer {


    /**
     * 序列化
     * @param t
     * @return
     */
    <T> byte[] serialize(T t);


    /**
     * 反序列化
     * @param bytes
     * @return
     */
    <T> T deserialize(byte[] bytes,Class<T> clazz);


}
