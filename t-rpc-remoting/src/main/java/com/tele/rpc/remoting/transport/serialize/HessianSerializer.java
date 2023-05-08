package com.tele.rpc.remoting.transport.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author tele
 * @Description
 * @date 2023-05-08
 */
@Slf4j
public class HessianSerializer implements Serializer
{

    @Override
    public <T> byte[] serialize(T t)
    {
        Hessian2Output hessian2Output = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            hessian2Output = new Hessian2Output(byteArrayOutputStream);
            hessian2Output.writeObject(t);
            hessian2Output.flush();
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e)
        {
           log.error("fail to serialize object:", e);
        }
        finally
        {
            if (hessian2Output != null)
            {
                try
                {
                    hessian2Output.close();
                }
                catch (IOException e)
                {
                    log.error("fail to close hessian2Output:", e);
                }
            }
        }
        throw new RuntimeException("fail to serialize object");
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz)
    {
        Hessian2Input hessian2Input = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes))
        {
            hessian2Input = new Hessian2Input(bis);
            return (T)hessian2Input.readObject();
        }
        catch (IOException e)
        {
            log.error("fail to deserialize object:", e);
        }
        finally
        {
            if (hessian2Input != null)
            {
                try
                {
                    hessian2Input.close();
                }
                catch (IOException e)
                {
                    log.error("fail to close hessian2Input:", e);
                }
            }
        }
       throw new RuntimeException("fail to deserialize object");
    }
}
