package com.tele.rpc.core.proxy;

import com.tele.rpc.core.constants.RpcConstants;
import com.tele.rpc.registry.LocalServiceHolder;
import com.tele.rpc.registry.model.ServiceInfo;
import com.tele.rpc.registry.zk.ServiceRegistry;
import com.tele.rpc.remoting.transport.RpcClient;
import com.tele.rpc.remoting.transport.request.Request;
import com.tele.rpc.remoting.transport.request.Response;
import com.tele.rpc.remoting.utils.IdGenerator;
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
        // find service
        String serviceName = params.get(RpcConstants.SERVICE_NAME);
        ServiceInfo service = registry.findService(serviceName);
        log.debug("find service:{}", service);

        // in jvm
        Object targetService = LocalServiceHolder.getService(serviceName);
        if (targetService != null)
        {
            log.debug("local invoke");
            return method.invoke(targetService, args);
        }

        // rpc
        Request request = new Request();
        request.setRequestId(IdGenerator.getId());
        request.setArgs(args);
        request.setServiceName(serviceName);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());

        Response response = RpcClient.sendRequest(request, service);
        if (!response.isSuccess())
        {
            throw new RuntimeException("service invoke failed");
        }
        Object result = response.getResult(method.getReturnType());
        log.debug("invoke result:{}",result);
        return result;
    }

    private void initRegistry()
    {
        registry = new ServiceRegistry(params.get(RpcConstants.REGISTER_ADDRESS));
    }

}
