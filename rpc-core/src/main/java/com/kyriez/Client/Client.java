package com.kyriez.Client;

import com.kyriez.entity.RpcRequest;

public interface Client {

    Object SendRequest(RpcRequest rpcRequest, String host, int port);
}
