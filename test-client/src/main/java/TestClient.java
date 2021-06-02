import com.kyriez.Client.RpcClient;
import com.kyriez.Client.RpcClientProxy;
import com.kyriez.HelloObject;
import com.kyriez.HelloService;

import java.lang.reflect.Proxy;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 8000);
        HelloService service = (HelloService) proxy.getProxy(HelloService.class);
        for (int i = 0; i < 10; i++) {
            HelloObject hello = new HelloObject(i, "message"+i);
            System.out.println(service.hello(hello));;
        }

    }

}
