import com.kyriez.Client.Client;
import com.kyriez.Client.Netty.NettyClient;
import com.kyriez.Client.RpcClient;
import com.kyriez.Client.RpcClientProxy;
import com.kyriez.HelloObject;
import com.kyriez.HelloService;

public class NettyClientTest {
    public static void main(String[] args) {
        NettyClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService service = proxy.getProxy(HelloService.class);
        HelloObject obj = new HelloObject(21, " KYRIEZ");

        System.out.println(service.hello(obj));
    }
}
