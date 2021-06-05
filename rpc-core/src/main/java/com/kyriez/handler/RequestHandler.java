package com.kyriez.handler;

import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import com.kyriez.enumeration.ResponseCode;
import com.kyriez.provider.ServiceProvider;
import com.kyriez.provider.ServiceProviderImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static{
        serviceProvider = new ServiceProviderImp();
    }

    public Object handle(RpcRequest request){
        Object result = null;
        Object service = serviceProvider.getServiceProvider(request.getInterfaceName());
        try{
            result = invokeTargetMethod(request, service);
            logger.info("服务:{} 成功调用方法:{}", request.getInterfaceName(), request.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
        return result;
    }

    public Object invokeTargetMethod(RpcRequest request, Object service) throws InvocationTargetException, IllegalAccessException {
        Method method = null;
        try {
            method = service.getClass().getMethod(
                    request.getMethodName(),
                    request.getParamType()
            );
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD, request.getRequestId());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return method.invoke(service, request.getParams());


    }

}
