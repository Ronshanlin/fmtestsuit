package com.shanlin.demo.helper.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.shanlin.demo.helper.job.ClearExpireMapCache;

public class MapCache implements CacheInterface{
    private static final Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
    private static final Map<String, Long> expireCache = new ConcurrentHashMap<String, Long>();
    
    static{
        new ClearExpireMapCache(10000L, expireCache, cache).clear();
    }
    
    
    public synchronized void hset(String key, String field, Object value){
        cache.put(getKey(key, field), value);
    }
    
    public Object hget(String key, String field){
        return cache.get(getKey(key, field));
    }
    
    public boolean hexist(String key, String field){
        return cache.containsKey(getKey(key, field));
    }
    
    private static String getKey(String key, String field){
        return key+"_"+field;
    }

    @Override
    public void set(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public boolean exist(String key) {
        return cache.containsKey(key);
    }

    @Override
    public List<Object> getVals(String regex) {
        Pattern pattern = Pattern.compile(regex);
        
        List<Object> vals = new ArrayList<Object>();
        
        for (String key : cache.keySet()) {
            if (pattern.matcher(key).matches()) {
                vals.add(cache.get(key));
            }
        }
        
        return vals;
    }

    @Override
    public void del(String key) {
        cache.remove(key);
    }

    @Override
    public void setx(String key, Object value, long expire) {
        cache.put(key, value);
        expireCache.put(key, expire*1000);
    }
}
