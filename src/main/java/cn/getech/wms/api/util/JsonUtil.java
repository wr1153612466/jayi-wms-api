package cn.getech.wms.api.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;


public class JsonUtil {


    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static String beautyJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

        /**
         * json字符转换成对象
         * @param json
         * @param beanClass
         * @return
         */
    public static <T> T JSONToObject(String json, Class<T> beanClass){
        Gson gson = new Gson();
        T res = gson.fromJson(json, beanClass);
        return res;
    }


    public static <T> List<T> toList(String jsonStr, Class<T> beanClass){
        return JSONUtil.toList(new JSONArray(jsonStr),beanClass);
    }

    public static <T> List<T> toList(com.alibaba.fastjson.JSONArray json, Class<T> beanClass){
        return toList(json.toString(),beanClass);
    }

}
