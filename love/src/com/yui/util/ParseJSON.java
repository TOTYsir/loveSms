package com.yui.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ParseJSON {
    private JSONObject data;

    public ParseJSON(String json) {
        this.data = JSONObject.parseObject(json);
        this.data = this.data.getJSONObject("resp");
    }

    /**
     * 获取当天温度区间
     *
     * @return low, high 两个key
     */
    public HashMap<String, String> getTemp() {
        HashMap<String, String> map = new HashMap<>();
        JSONObject yesterday = data.getJSONObject("yesterday");
        String low = yesterday.getString("low_1");
        low = low.substring(3, low.length() - 1);
        String high = yesterday.getString("high_1");
        high = high.substring(3, high.length() - 1);
        map.put("low", low);
        map.put("high", high);
        return map;
    }

    /**
     * 获取用于Message的风力等级集合
     *
     * @return
     */
    public HashMap<String, ArrayList<String>> getWindpowers() {
        JSONArray array = data.getJSONObject(("forest")).getJSONArray("weather");
        ArrayList low = new ArrayList();
        ArrayList high = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String low1 = obj.getJSONObject("day").getString("fengli");
            String high1 = obj.getJSONObject("night").getString("fengli");
            low1 = low1.substring(0, low1.length() - 1);
            high1 = high1.substring(0, high1.length() - 1);
            low.add(low1);
            high.add(high1);
        }
        HashMap map = new HashMap();
        map.put("low", low);
        map.put("high", high);
        return map;
    }

    /**
     * 获取用于Message的未来五天温度变化
     *
     * @return
     */
    public HashMap<String, ArrayList<String>> getTemps() {
        JSONArray array = data.getJSONObject(("forest")).getJSONArray("weather");
        ArrayList low = new ArrayList();
        ArrayList high = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String low1 = obj.getJSONObject("day").getString("fengli");
            String high1 = obj.getJSONObject("night").getString("fengli");
            low1 = low1.substring(3, low1.length() - 1);
            high1 = high1.substring(3, high1.length() - 1);
            low.add(low1);
            high.add(high1);
        }
        HashMap map = new HashMap();
        map.put("low", low);
        map.put("high", high);
        return map;
    }

    /**
     * 获取当天风力等级
     *
     * @return day, night两个key
     */
    public Map<String, String> getWindpower() {
        HashMap<String, String> map = new HashMap<>();
        JSONObject yesterday = data.getJSONObject("yesterday");
        String day = yesterday.getJSONObject("day_1").getString("fl_1");
        day = day.substring(0, day.length() - 1);
        String night = yesterday.getJSONObject("night_1").getString("fl_1");
        night = night.substring(0, night.length() - 1);
        map.put("day", day);
        map.put("night", night);
        return map;
    }

    /**
     * 获取当天风向
     *
     * @return
     */
    public String getWinddirect() {
        return data.getString("fengxiang");
    }

    /**
     * 获取当天白天天气信息
     *
     * @return
     */
    public String getWeather() {
        JSONObject yesterday = data.getJSONObject("yesterday");
        String day = yesterday.getJSONObject("day_1").getString("type_1");
        return day;
    }

    /**
     * 获取随机的温馨提示
     *
     * @return
     */
    public Map<String, String> getTips() {
        String[] texts = {
                "宝，我今天暴走了，什么暴走，想把你抱走。",
                "宝，我今天成人了，成什么人，你的心上人。",
                "宝，今天我去输液 输什么液，想你的夜。",
                "宝，我去种棉花了，种什么棉，我对你情意绵绵。"
        };
        Random r = new Random();
        String tip = texts[r.nextInt(texts.length)];
        ArrayList<String> names = new ArrayList<>();
        names.add("情话");
        names.add("紫外线强度");
        names.add("穿衣指数");
        names.add("护肤指数");
        names.add("感冒指数");
        names.add("户外指数");
        names.add("污染指数");
        int index = r.nextInt(texts.length);
        if (index == 0) {
            HashMap<String, String> map = new HashMap<>();
            map.put("情话", tip);
            return map;
        }
        String name = names.get(index);

        JSONArray array = data.getJSONObject("zhishus").getJSONArray("zhishu");
        for (int i = 0; i < array.size(); i++) {
            if (name.equals(array.getJSONObject(i).getString("name"))) {
                HashMap<String, String> map = new HashMap<>();
                map.put(name, array.getJSONObject(i).getString("detail"));
                return map;
            }
        }
        return null;

    }
}
