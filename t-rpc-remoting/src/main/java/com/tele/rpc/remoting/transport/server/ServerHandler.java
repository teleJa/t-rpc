package com.tele.rpc.remoting.transport.server;

import com.tele.rpc.registry.LocalServiceHolder;
import com.tele.rpc.remoting.transport.HostInfo;
import com.tele.rpc.remoting.transport.request.Request;
import com.tele.rpc.remoting.transport.request.Response;
import com.tele.rpc.remoting.transport.serialize.SerializeSupport;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author tele
 * @Description
 * @create 2020-06-17
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter
{

    private final HostInfo host;

    public ServerHandler(HostInfo host)
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
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        Request request = (Request)msg;
        log.debug("receive request:{}",request);
        Object service = LocalServiceHolder.getService(request.getServiceName());
        Method method = service.getClass().getMethod(request.getMethodName(), request.getParameterTypes());

        Object result = method.invoke(service, request.getArgs());
        log.debug("invoke result:{}", result);

        Response responseMessage = new Response(request.getId(), SerializeSupport.serialize(result));
        ctx.writeAndFlush(responseMessage);
        log.debug("send response:{}",responseMessage);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        cause.printStackTrace();
    }
}
