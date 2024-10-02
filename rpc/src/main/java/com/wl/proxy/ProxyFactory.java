package com.wl.proxy;

import com.wl.common.Invocation;
import com.wl.common.URL;
import com.wl.loadbalance.Loadbalance;
import com.wl.protocol.HttpClient;
import com.wl.register.MapRemoteRegister;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass){
        //用户配置

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                proxy：代表被代理的对象。
//                method：表示正在调用的方法的元信息。
//                args：传递给方法的实际参数。

//                开启mock，则出现waitProvider ， 服务端写好，关闭
//                String mock = System.getProperty("mock");
//                if(mock != null && mock.startsWith("return:")){
//                    String result = mock.replace("return" , "");
//                    return result;
//                }

                //往服务端发送的信息，要调用的接口名，方法名，方法参数类型，方法参数
                Invocation invocation = new Invocation(interfaceClass.getName() , method.getName() ,
                        method.getParameterTypes() , args);

                //客户端对象
                HttpClient httpClient = new HttpClient();

                //服务发现，从远程注册中心发现服务列表
                List<URL> list = MapRemoteRegister.get(interfaceClass.getName());



                //服务调用
                String result = null;
                List<URL> invokedUrls =  new ArrayList<>();

                int max = 3;

                while (max > 0) {
                    //负载均衡
                    list.remove(invokedUrls);
                    URL url = Loadbalance.random(list);
                    invokedUrls.add(url);
                    try {
                        //向服务端发送请求
                        result = httpClient.send(url.getHostname() , url.getPort(), invocation);
                        break;

                    } catch (Exception e) {
                        if(max-- != 0)continue;
                        return "服务报错";
                    }
                }

                return result;
            }
        });

        return (T) proxyInstance;
    }
}
