package com.tele.rpc.remoting.transport.client;

import com.tele.rpc.remoting.transport.HostInfo;
import com.tele.rpc.remoting.transport.RpcClient;
import com.tele.rpc.remoting.transport.request.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tele
 * @Description
 * @create 2020-06-17
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Response>
{

    private final HostInfo host;

    public ClientHandler(HostInfo host)
    {
        this.host = host;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        log.info("channel active,host:{}",host.getHost());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg)
        throws Exception
    {
        RpcClient.receiveResponse(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        log.error("client caught exception:",cause);
    }
}
