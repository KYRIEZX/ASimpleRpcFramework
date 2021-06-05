package com.kyriez.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcError {
    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("无法连接注册中心"),
    FAILED_TO_REGISTER("注册失败"),
    CLIENT_FAIL_TO_CONNECT("服务器连接失败"),
    RESPONSE_NOT_MATCH("服务不匹配");

    private final String message;
}
