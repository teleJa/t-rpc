package com.tele.rpc.remoting.transport;

import com.tele.rpc.registry.model.ServiceInfo;
import com.tele.rpc.remoting.transport.client.NettyClient;
import com.tele.rpc.remoting.transport.request.Request;
import com.tele.rpc.remoting.transport.request.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Slf4j
public class RpcClient
{
    private static final Map<Integer, CompletableFuture<Response>> requestMap = new ConcurrentHashMap<>();

    public static Response sendRequest(Request request, ServiceInfo serviceInfo)
        throws ExecutionException, InterruptedException, TimeoutException
    {

        HostInfo hostInfo = new HostInfo(serviceInfo.getIp(), serviceInfo.getPort());
        NettyClient nettyClient = NettyClientStore.getChannel(hostInfo.getHost());
        if (nettyClient == null)
        {
            nettyClient = new NettyClient(hostInfo);
            nettyClient.start();
        }
        nettyClient.sendRequest(request);
        CompletableFuture<Response> future = new CompletableFuture<>();
        requestMap.put(request.getRequestId(), future);
        return future.get(30, TimeUnit.SECONDS);
    }

    public static void receiveResponse(Response response)
    {
       requestMap.get(response.getId()).complete(response);
    }

}
