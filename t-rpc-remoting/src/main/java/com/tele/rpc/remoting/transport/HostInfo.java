package com.tele.rpc.remoting.transport;

import lombok.Getter;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Getter
public class HostInfo
{
    final String ip;

    final int port;

    public HostInfo(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public String getHost()
    {
        return ip + ":" + port;
    }
}
