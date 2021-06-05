package com.kyriez.Client;

import com.kyriez.Client.Netty.NettyClient;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;


@Data
public class RpcClientProxy implements InvocationHandler {
    private final Client client;
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    public RpcClientProxy(NettyClient client) {
        this.client = client;
    }



    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this);
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        return client.SendRequest(rpcRequest);

    }
}
