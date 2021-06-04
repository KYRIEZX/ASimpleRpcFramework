package com.kyriez.Server;

import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import com.kyriez.enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Object handle(RpcRequest request, Object service){
        Object result = null;
        try {
            result = invokeTargetMethod(request, service);
            logger.info("使用服务{} 成功调用了方法{}", request.getInterfaceName(), request.getMethodName());
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error("调用出现错误", e);
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
            RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return method.invoke(service, request.getParams());


    }

}
