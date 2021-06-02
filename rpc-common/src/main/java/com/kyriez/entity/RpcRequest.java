package com.kyriez.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 调用接口名称
     */
    private String InterfaceName;
    /**
     * 调用方法名
     */
    private String MethodName;
    /**
     * 调用参数
     */
    private Object[] params;
    /**
     * 调用参数类型
     */
    private Class<?>[] paramType;
}
