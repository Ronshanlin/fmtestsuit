package com.shanlin.demo.helper.cache;

public class CacheFactory {
    private CacheFactory(){
    }
    
    public static CacheInterface getCacheBean(CacheType cacheType){
        CacheInterface cache = null;
        
        if (cacheType.equals(CacheType.MAP)) {
            cache = new MapCache();
        }
        
        return cache;
    }
}
