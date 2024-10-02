package com.wl.protocol;

import com.wl.common.Invocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient {

    public String send(String hostname , Integer port , Invocation invocation) throws IOException {
        //用户的配置

//        创建URL对象用于指定目标服务器地址。
//        获取HTTP连接并设置为POST请求。
//        允许输出以发送数据。
//        获取连接输出流。
//        创建对象输出流以序列化对象。
//        将Invocation对象写入输出流。
//        刷新并关闭输出流。
//        从连接获取输入流读取服务器响应。
//        将输入流转换为字符串作为结果返回。
//        捕获URL异常但未处理。
//        捕获并重新抛出IO异常。

        try {

            URL url = new URL("http", hostname, port, "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            // 配置
            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);

            oos.writeObject(invocation);
            oos.flush();
            oos.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            String result = IOUtils.toString(inputStream);
            return result;
        } catch (MalformedURLException e) {

        } catch (IOException e) {
            throw e;

        }

        return null;
    }
}
