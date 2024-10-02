package com.wl.protocol;


import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;



public class HttpServer {

    public void start(String hostname , int port) {
        //读取用户的配置 server.name=tomcat

//        创建Tomcat实例，并获取其Server和服务（Service）对象。
//        配置连接器（Connector），设置端口号。
//        初始化引擎（Engine）、主机（Host）和上下文（Context），设置相关属性。
//        将上下文和主机添加到引擎，服务设置容器为引擎并添加连接器。
//        添加分发Servlet映射。
//        启动Tomcat服务器并等待
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(port);

        Engine engine = new StandardEngine();
        engine.setDefaultHost(hostname);

        Host host = new StandardHost();
        host.setName(hostname);

        String contextPath = "";
        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet());
        context.addServletMappingDecoded("/*", "dispatcher");

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
