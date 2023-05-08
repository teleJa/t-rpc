package com.tele.rpc.remoting.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@UtilityClass
public class IdGenerator
{
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public static int getId()
    {
        return sequence.getAndIncrement();
    }

}
