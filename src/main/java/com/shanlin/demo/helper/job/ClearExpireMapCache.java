package com.shanlin.demo.helper.job;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ClearExpireMapCache {
    private Map<String, Long> expireCache;
    private long period;
    private Map<String, Object> cache;
    
    public ClearExpireMapCache(long period, Map<String, Long> expireCache, Map<String, Object> cache){
        this.cache = cache;
        this.period = period;
        this.expireCache = expireCache;
    }
    
    public void clear(){
        Timer timer = new Timer("ClearExpireLoginUserJob", true);
        
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                long expire = 0;
                long current = System.currentTimeMillis();
                
                for (String key : expireCache.keySet()) {
                    // 获取过期时间
                    expire = expireCache.get(key);
                    
                    if (expire < current) {
                        cache.remove(key);
                    }
                }   
            }
        }, new Date(), this.period);
    }
}
