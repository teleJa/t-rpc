package com.tele.rpc.remoting.transport.codec;

import com.tele.rpc.remoting.transport.request.Response;
import com.tele.rpc.remoting.transport.serialize.SerializeSupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author tele
 * @Description
 * @create 2020-09-20
 */
public class ResponseEncoder extends MessageToByteEncoder<Response>
{

    @Override
    protected void encode(ChannelHandlerContext ctx, Response msg, ByteBuf out)
        throws Exception
    {
        out.writeBytes(SerializeSupport.serialize(msg));
    }
}
