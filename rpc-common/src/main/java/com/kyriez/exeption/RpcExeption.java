package com.kyriez.exeption;

import com.kyriez.enumeration.RpcError;

public class RpcExeption extends RuntimeException{

    public RpcExeption(RpcError error) {
        super(error.getMessage());
    }

    public RpcExeption(RpcError error, String detail) {
        super(error.getMessage()+": "+detail);
    }

    public RpcExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
