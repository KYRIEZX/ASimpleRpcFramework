package com.kyriez.Serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.kyriez.entity.RpcRequest;
import com.kyriez.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements CommonSerializer {
    private final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Output output = new Output(baos);
            Kryo kryo = kryoThreadLocal.get();
            /**
             *  Object -> byte
             */
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            System.out.println("序列化成功");
            return output.toBytes();
        } catch (Exception e) {
            logger.error("序列化失败", e);
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz){
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Input input = new Input(bais);
            Kryo kryo = kryoThreadLocal.get();
            System.out.println(clazz);
            Object obj = kryo.readObject(input, clazz);

            kryoThreadLocal.remove();
            return obj;
        } catch (Exception e) {
            logger.error("反序列化失败", e);
            return null;
        }

    }


    @Override
    public int getCode() {
        return SerializerCode.valueOf("KYRO").getCode();
    }
}
