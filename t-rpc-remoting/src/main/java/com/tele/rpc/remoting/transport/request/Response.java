package com.tele.rpc.remoting.transport.request;

import com.tele.rpc.remoting.transport.serialize.SerializeSupport;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author tele
 * @Description
 * @create 2020-09-18
 */
@ToString
public class Response extends BaseResponse
{

    private final int requestId;

    /**
     * 消息体
     */
    @NonNull
    @Getter
    private byte[] body;

    public Response(Integer requestId, @NonNull byte[] body) {
        this.requestId = requestId;
        this.body = body;
    }

    public int getId() {
        return requestId;
    }

    public <T> T getResult(Class<T> clazz) {
        return SerializeSupport.deserialize(body, clazz);
    }

}
