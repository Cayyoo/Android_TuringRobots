package com.imooc.mooo.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.imooc.mooo.bean.ChatMessage;
import com.imooc.mooo.bean.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 接收、发送消息的工具类
*/
public class HttpUtils {

    private static final String URL="http://www.tuling123.com/openapi/api";//API地址
    private static final String API_KEY="d077adddd2ad4dc191943612f642513d";

    /**
     * 发送一个消息，得到返回的消息数据
     * @param msg
     * @return
     */
    public static ChatMessage sendMessage(String msg){
        ChatMessage chatMessage=new ChatMessage();

        String jsonRes=doHttpRequest(msg);
        Gson gson=new Gson();
        Result result=null;

        try{
            result=gson.fromJson(jsonRes,Result.class);

            if (result!=null)
                chatMessage.setMsg(result.getText());
            else
                Log.e("TAG","数据源为空"+jsonRes);
        }catch (JsonSyntaxException e){
            chatMessage.setMsg("服务器繁忙，请稍后再试");
        }

        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);

        return chatMessage;
    }

    /**
     * 网络方式访问，新版已建议使用HTTP POST方式访问，且加密操作必须采用POST
     * @param msg
     * @return
     */
    public static String doHttpRequest(String msg){
        String result="";//返回结果
        String url=setParams(msg);//url链接
        ByteArrayOutputStream baos=null;
        InputStream is=null;

        try {
            java.net.URL urlNet=new URL(url);
            HttpURLConnection conn= (HttpURLConnection) urlNet.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");//设置访问方式
            //conn.setRequestMethod("POST");//使用POST方式后，需要添加加密方法。可使用的加密方法请参考官方接入指南

            is=conn.getInputStream();
            int len=-1;
            byte[] buf=new byte[128];
            baos=new ByteArrayOutputStream();

            while ((len=is.read(buf))!=-1){//没到结尾
                baos.write(buf,0,len);
            }

            baos.flush();
            result=new String(baos.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
                try {
                    if (baos!=null)
                    baos.close();

                    if (is!=null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return result;
    }

    /**
     * 设置参数，获取完整的URL，注意编码格式是UTF-8
     * @param msg
     * @return
     */
    private static String setParams(String msg){
        String url= "";
        try {
            url = URL+"?key="+API_KEY+"&info="+ URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return url;
    }

}
