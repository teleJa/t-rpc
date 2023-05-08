package com.tele.rpc.remoting.transport.request;

import com.sun.jndi.toolkit.url.Uri;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author tele
 * @Description
 * @create 2020-06-17
 */
@ToString
@Getter
@Setter
public class Request implements Serializable
{

    private int requestId;

    @NonNull
    private String serviceName;

    @NonNull
    private String methodName;

    @NonNull
    private Class<?>[] parameterTypes;

    @NonNull
    private Object[] args;

    /**
     * 服务uri
     */
    private Uri uri;

    public int getId()
    {
        return requestId;
    }
}
