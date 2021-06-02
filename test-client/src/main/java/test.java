import com.kyriez.HelloObject;
import com.kyriez.HelloService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class test {
    public static void main(String[] args) {
        HelloService proxy = (HelloService) Proxy.newProxyInstance(
                HelloService.class.getClassLoader(),
                new Class<?>[]{HelloService.class},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println(method.getName());

                        return null;
                    }
                }
        );
        System.out.println(proxy);
        System.out.println(proxy.hello(new HelloObject(1, "message")));;
    }
}
