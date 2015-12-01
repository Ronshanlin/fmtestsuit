package com.shanlin.demo.helper.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapCache implements CacheInterface{
    private final static Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
    
    public synchronized void put(String key, String field, Object value){
        cache.put(getKey(key, field), value);
    }
    
    public Object get(String key, String field){
        return cache.get(getKey(key, field));
    }
    
    public boolean exist(String key, String field){
        return cache.containsKey(getKey(key, field));
    }
    
    private static String getKey(String key, String field){
        return key+"_"+field;
    }
}
