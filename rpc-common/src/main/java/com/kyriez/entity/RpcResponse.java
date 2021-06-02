package com.kyriez.entity;

import com.kyriez.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse<T> implements Serializable {
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<T>();

        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);

        return response;
    }
    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<T>();
        response.setStatusCode(ResponseCode.FAIL.getCode());
        response.setMessage(ResponseCode.FAIL.getMessage());
        return response;
    }
}
