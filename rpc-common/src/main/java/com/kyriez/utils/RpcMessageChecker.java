package com.kyriez.utils;

import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import com.kyriez.enumeration.ResponseCode;
import com.kyriez.enumeration.RpcError;
import com.kyriez.exeption.RpcExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);
    private RpcMessageChecker() {
    }

    public static void check(RpcRequest request, RpcResponse response){
        if(response == null){
            logger.error("调用服务失败,serviceName:{}", request.getInterfaceName());
            throw new RpcExeption(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfaceName());
        }

        if(!response.getRequestId().equals(request.getRequestId())){
            throw new RpcExeption(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + request.getInterfaceName());
        }

        if (response.getStatusCode() == null || !response.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            logger.error("调用服务失败,serviceName:{},RpcResponse:{}", request.getInterfaceName(), response);
            throw new RpcExeption(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfaceName());
        }
    }
}
