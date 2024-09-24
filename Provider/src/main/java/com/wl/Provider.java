package com.wl;

import com.wl.common.URL;
import com.wl.protocol.HttpServer;
import com.wl.register.LocalRegister;
import com.wl.register.MapRemoteRegister;



public class Provider {

    public static void main(String[] args) {

        //本地注册
        LocalRegister.regist(HelloService.class.getName() , "1.0", HelloServiceImpl.class);

        //注册中心实现 , 服务注册
        URL url = new URL("localhost" , 6666);
        MapRemoteRegister.regist(HelloService.class.getName() , url);
        //Netty , Tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname() , url.getPort());

    }
}
