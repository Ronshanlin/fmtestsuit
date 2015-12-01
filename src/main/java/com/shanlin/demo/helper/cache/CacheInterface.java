package com.shanlin.demo.helper.cache;

public interface CacheInterface {
    public void put(String key, String field, Object value);
    
    public Object get(String key, String field);
    
    public boolean exist(String key, String field);
}
