package com.kyriez.Client;

import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


@Data
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                                            new Class<?>[]{clazz},
                                        this);
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .InterfaceName(method.getDeclaringClass().getName())
                .MethodName(method.getName())
                .params(args)
                .paramType(method.getParameterTypes())
                .build();

        RpcClient client = new RpcClient();
        return ((RpcResponse) client.SendRequest(rpcRequest, host, port)).getData();


    }
}
