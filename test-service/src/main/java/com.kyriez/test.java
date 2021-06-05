package com.kyriez;

public class test {
    public static void main(String[] args) {
        HelloService service = new HelloServiceImpl();
        System.out.println(service.getClass().getInterfaces());
        System.out.println(HelloService.class.getCanonicalName());
    }
}
