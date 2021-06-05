package com.kyriez.Server;

import com.kyriez.provider.ServiceProvider;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import com.kyriez.handler.RequestHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
public class RequestHandlerThread {

//    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);
//    private RequestHandler requestHandler;
//    private ServiceProvider serviceProvider;
//    private Socket socket;
//
//
//    @Override
//    public void run() {
//        try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
//            RpcRequest request = (RpcRequest) ois.readObject();
//            String interfaceName = request.getInterfaceName();
//            Object service = serviceProvider.getService(interfaceName);
//            Object result = requestHandler.handle(request, service);
//            oos.writeObject(RpcResponse.success(result));
//            oos.flush();
//        } catch (IOException | ClassNotFoundException e) {
//            logger.error("调用或发送时有错误", e);
//        }
//
//    }
}
