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

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                String mock = System.getProperty("mock");
                if(mock != null && mock.startsWith("return:")){
                    String result = mock.replace("return" , "");
                    return result;
                }

                Invocation invocation = new Invocation(interfaceClass.getName() , method.getName() ,
                        method.getParameterTypes() , args);

                HttpClient httpClient = new HttpClient();

                //服务发现
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
                        result = httpClient.send(url.getHostname() , url.getPort(), invocation);
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
