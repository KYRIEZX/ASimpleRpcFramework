package com.kyriez.Client;

import com.kyriez.entity.RpcRequest;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


public class RpcClient implements Client{
    Logger logger = LoggerFactory.getLogger(RpcClient.class);


    @Override
    public Object SendRequest(RpcRequest rpcRequest) {
//        try(Socket socket = new Socket(host, port)) {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//
//            logger.info("正在连接");
//            objectOutputStream.writeObject(rpcRequest);
//            System.out.println("Request is "+rpcRequest);
//            logger.info("写入");
//            objectOutputStream.flush();
//            logger.info("FLUSH....");
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//
//            return objectInputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            logger.error("调用时有错误发生：", e);
//        }
        return null;
    }
}
