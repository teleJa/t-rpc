package com.tele.rpc.remoting.transport.request;

import lombok.NonNull;

import java.io.Serializable;

/**
 * @author tele
 * @Description
 * @create 2020-07-15
 */
public class BaseResponse implements Serializable
{

    /**
     * 状态码
     */
    @NonNull
    protected int code;

    /**
     * msg
     */
    @NonNull
    protected String msg;

    public boolean isSuccess()
    {
        return code == 0;
    }

}
