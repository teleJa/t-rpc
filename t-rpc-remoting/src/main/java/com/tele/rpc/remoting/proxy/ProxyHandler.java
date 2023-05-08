package com.tele.rpc.remoting.proxy;

import com.tele.rpc.registry.model.ServiceInfo;
import com.tele.rpc.registry.zk.ServiceRegistry;
import com.tele.rpc.remoting.constants.ReferenceConstants;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author tele
 * @Description
 * @since 2023-05-06
 */
@Slf4j
public class ProxyHandler implements InvocationHandler
{
    private final Map<String,String> params;

    private ServiceRegistry registry;

    public ProxyHandler(Map<String, String> parameters)
    {
        this.params = parameters;
        this.initRegistry();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable
    {
        log.debug("ready to invoke remoting service");
        // 寻找服务
        ServiceInfo service = registry.findService(params.get(ReferenceConstants.SERVICE_NAME));
        log.debug("find service:{}", service);

        // rpc调用

        return null;
    }

    private void initRegistry()
    {
        registry = new ServiceRegistry(params.get(ReferenceConstants.REGISTER_ADDRESS));
    }

}
