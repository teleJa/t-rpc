package com.tele.rpc.remoting.transport;

import com.tele.rpc.remoting.transport.client.NettyClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
public class NettyClientStore
{

    // key-host
    private static final Map<String, NettyClient> CLIENT_MAP = new ConcurrentHashMap<>();

    public static void addChannel(String host, NettyClient nettyClient)
    {
        CLIENT_MAP.putIfAbsent(host, nettyClient);
    }

    public static void removeChannel(String host)
    {
        CLIENT_MAP.remove(host);
    }

    public static NettyClient getChannel(String host)
    {
        return CLIENT_MAP.get(host);
    }

}
