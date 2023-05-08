package com.tele.rpc.remoting.transport.codec;

import com.tele.rpc.remoting.transport.request.Request;
import com.tele.rpc.remoting.transport.serialize.SerializeSupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author tele
 * @Description
 * @create 2020-06-17
 */
public class RequestEncoder extends MessageToByteEncoder<Request>
{

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out)
        throws Exception
    {
        out.writeBytes(SerializeSupport.serialize(msg));
    }
}
