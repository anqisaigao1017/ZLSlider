package com.jude.rollviewpagerdome.module.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by garry on 17/5/1.
 */

public class JsonParser {
    public static Map<String, Object> parseObject(JSONObject jsonObject) {
        Map<String, Object> valueMap = null;
        if (jsonObject != null) {
            valueMap = new HashMap<>();
            Iterator<String> itr = jsonObject.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                if (key != null && !"".equals(key)) {
                    try {
                        Object value = jsonObject.get(key);
                        if (value instanceof JSONObject) {
                            valueMap.putAll(parseObject((JSONObject) value));
                        }else if(value instanceof JSONArray){
                            valueMap.put(key,parseArray((JSONArray)value));
                        }else{
                            valueMap.put(key,value);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return valueMap;
    }

    public static List<Object> parseArray(JSONArray jsonArray){
        List<Object> valueList = null;
        if(jsonArray!=null){
            valueList = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                try {
                    Object value = jsonArray.get(i);
                    if(value instanceof JSONObject){
                        valueList.add(parseObject((JSONObject) value));
                    }else if(value instanceof JSONArray){
                        valueList.add(parseArray((JSONArray)value));
                    }else{
                        valueList.add(value);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return valueList;
    };
}
