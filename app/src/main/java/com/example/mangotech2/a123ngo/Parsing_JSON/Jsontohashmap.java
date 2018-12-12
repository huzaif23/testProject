package com.example.mangotech2.a123ngo.Parsing_JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Jsontohashmap {

 public static    Map<String, Object> jsonmapvalue= new HashMap<String, Object>();
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                listtoobj((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            jsonmapvalue.put(key,value);
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();

        for(int i = 0; i < array.length(); i++) {


           List<Object> value = (List<Object>) array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = (List<Object>) toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


    public static void listtoobj(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();

        for(int i = 0; i < array.length(); i++) {
String jsonstr=  array.get(i).toString();
            JSONObject object= new JSONObject(jsonstr);

            if(object != JSONObject.NULL) {
            Map<String,Object>   mapp = toMap(object);
            }
        /*    List<Object> value = (List<Object>) array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = (List<Object>) toMap((JSONObject) value);
            }
            list.add(value);*/
        }
    }
}
