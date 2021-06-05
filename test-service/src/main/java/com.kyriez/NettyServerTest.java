package com.kyriez;

import com.kyriez.Registry.DefaultServiceProvider;
import com.kyriez.provider.ServiceProvider;
import com.kyriez.Server.Netty.NettyServer;

public class NettyServerTest {
    public static void main(String[] args) {
        HelloService service = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999);
        server.publishService(service, HelloService.class);
        server.start();
    }
}
