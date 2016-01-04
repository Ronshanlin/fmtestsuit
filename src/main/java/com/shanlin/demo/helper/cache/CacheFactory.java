package com.shanlin.demo.helper.cache;

public class CacheFactory {
    private static final CacheType defaultCacheType = CacheType.MAP;
    
    private CacheFactory(){
    }
    
    public static CacheInterface getCacheBean(){
        CacheInterface cache = null; // TODO other cache
        
        if (defaultCacheType.equals(CacheType.MAP)) {
            cache = new MapCache();
        }
        
        return cache;
    }
}
