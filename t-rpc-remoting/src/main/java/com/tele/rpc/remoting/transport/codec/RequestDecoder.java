package com.tele.rpc.remoting.transport.codec;

import com.tele.rpc.remoting.transport.request.Request;
import com.tele.rpc.remoting.transport.serialize.SerializeSupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author tele
 * @Description
 * @create 2020-06-17
 */
public class RequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() > 0) {

            byte[] bytes = new byte[in.readableBytes()];
            in.readBytes(bytes);

            // 还原为requestMessage
            Request request = SerializeSupport.deserialize(bytes,Request.class);
            out.add(request);
        }

    }
}
