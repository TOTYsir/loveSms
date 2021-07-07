package com.yui.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Util {

    /**
     * 读取网址数据
     *
     * @return
     */
    private static String getString(String url) {
        //1.创建一个网站的 抽象表示（对象）
        try {
            URL u = new URL(url);
            //2.打开链接
            URLConnection conn = u.openConnection();
            //3.获取传输数据的通道（字节输入流）
            InputStream is = conn.getInputStream();
            //4.将字节输入流，装饰为能一次读取一行文字的缓冲字符输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            //5.读取一行行的数据，并汇总
            StringBuffer sb = new StringBuffer();
            String text = null;
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            //6.将读取的数据返回给调用者
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查询天气
     *
     * @param city 要查询的城市
     * @return 天气结果
     */

    private static String getWeather(String city) {

        try {
            String json = getString("https://itdage.cn/hw/weather?city=" + URLEncoder.encode(city, "utf-8"));
            return json;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static String sendSms(String name, String phoneNumber, String s1, String s2, String s3) {
        try {
            name = URLEncoder.encode(name, "utf-8");
            s1 = URLEncoder.encode(s1, "utf-8");
            s2 = URLEncoder.encode(s2, "utf-8");
            s3 = URLEncoder.encode(s3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String json = getString("https://itdage.cn/hw/hwSms?name=" + name + "&phoneNumber=" + phoneNumber + "&s1=" + s1 + "&s2=" + s2 + "&s3=" + s3);
        return json;
    }


    public static String send(String phoneNumber, String name, String city) {
        //1.根据城市查天气
        String json = getWeather(city);
        //2.根据天气的JSON信息，解析出天气、温度区间、温馨提示
        ParseJSON obj = new ParseJSON(json);
        //天气信息
        String s1 = obj.getWeather();
        //获取并构建温度区间
        HashMap<String, String> temp = obj.getTemp();
        String lowTemp = temp.get("low");
        String highTemp = temp.get("high");
        String s2 = lowTemp + "-" + highTemp + "°";
        //获取指数
        Map<String, String> tips = obj.getTips();
        String s3 = tips.values().toArray()[0].toString();

        //3.发短信
        return sendSms(name, phoneNumber, s1, s2, s3);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //需要填充手机号码，昵称，城市
        String message = send("", "", "");
        System.out.println(message);
    }


}
