package com.shanlin.demo.helper.cache;

import java.util.List;

public interface CacheInterface {
    public void hset(String key, String field, Object value);
    
    public Object hget(String key, String field);
    
    public boolean hexist(String key, String field);
    
    public void set(String key,Object value);
    
    /**
     * 功能描述: <br>
     *
     * @param key
     * @param value
     * @param expire 单位秒，过期时间
     */
    public void setx(String key,Object value, long expire);
    
    public void del(String key);
    
    public Object get(String key);
    
    public boolean exist(String key);
    
    public List<Object> getVals(String keyRegex);
}
