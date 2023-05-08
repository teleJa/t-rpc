package com.tele.rpc.remoting.transport.codec;

import com.tele.rpc.remoting.transport.request.Response;
import com.tele.rpc.remoting.transport.serialize.SerializeSupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author tele
 * @Description
 * @create 2020-09-20
 */
public class ResponseDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
        throws Exception
    {
        if (in.readableBytes() > 0)
        {
            byte[] bytes = new byte[in.readableBytes()];
            in.readBytes(bytes);
            // 还原为response
            Response response = SerializeSupport.deserialize(bytes, Response.class);
            out.add(response);
        }
    }
}
