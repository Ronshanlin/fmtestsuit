package com.shanlin.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.shanlin.demo.dao.UserDao;
import com.shanlin.demo.helper.cache.CacheFactory;
import com.shanlin.demo.helper.cache.CacheInterface;
import com.shanlin.demo.helper.cache.CacheType;
import com.shanlin.demo.service.UserService;

public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao;
    
    private CacheInterface cache = CacheFactory.getCacheBean(CacheType.MAP);
    
    @Override
    public void saveUser(String userNo, String pwd, long expire) {
        userDao.saveUser(userNo, pwd, expire);
        
        cache.put(userNo, pwd, expire);
    }

}
