package com.zhangli.apihttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlHttp {
    public static final  String baseUrl="http://www.warmtel.com:8080/";
    private static UrlHttp NETUTIL=null;
    public static UrlHttp getInstance(){
        if(NETUTIL==null){
            NETUTIL=new UrlHttp();
        }
        return NETUTIL;
    }
    private  UrlHttp() {

    }

    public String getDataByConnectNet(String httpUrl) throws IOException {
        String myBaseUrl=baseUrl+httpUrl;
        URL url = new URL(myBaseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(8000);
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            return readStrFromInputStream(inputStream);
        } else {
            return "";
        }
    }

    /**
     * 从输入流读数据
     * @param is
     * @return
     * @throws IOException
     */
    public String readStrFromInputStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        is.close();
        return sb.toString();
    }
}
