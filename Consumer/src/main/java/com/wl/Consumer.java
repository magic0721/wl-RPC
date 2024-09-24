package com.wl;

import com.wl.common.Invocation;
import com.wl.protocol.HttpClient;
import com.wl.proxy.ProxyFactory;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) throws IOException {
        HelloService service = ProxyFactory.getProxy(HelloService.class);;
        String result = service.sayHello("wl");
        System.out.println(result);

//        Invocation invocation = new Invocation(HelloService.class.getName() , "sayHello" ,
//                new Class[]{String.class}, new Object[]{"wl"});
//
//        HttpClient httpClient = new HttpClient();
//        String result = httpClient.send("localhost", 6666, invocation);
//        System.out.println(result);
    }
}
