import com.kyriez.Client.Netty.NettyClient;
import com.kyriez.Client.RpcClientProxy;
import com.kyriez.HelloObject;
import com.kyriez.HelloService;

public class NettyClientTest {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService service = proxy.getProxy(HelloService.class);
        HelloObject obj = new HelloObject(12, "KYRIEZ");
        String res = service.hello(obj);
        System.out.println(res);
    }
}
