package com.shanlin.demo.service.impl;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shanlin.demo.dao.UserDao;
import com.shanlin.demo.entity.UserEntity;
import com.shanlin.demo.helper.cache.CacheFactory;
import com.shanlin.demo.helper.cache.CacheInterface;
import com.shanlin.demo.helper.cache.CacheType;
import com.shanlin.demo.service.UserService;
import com.shanlin.demo.utils.Constants;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao;
    
    private CacheInterface cache = CacheFactory.getCacheBean(CacheType.MAP);
    
    @Override
    public void saveUser(String userNo, String pwd, long expire) {
        String key = MessageFormat.format(Constants.CACHE_USER_PWD, userNo);
        
        if (expire<1) {
            expire = Constants.EXPIRE_TIME; // 默认时间
        }
        long saveExpire = expire+System.currentTimeMillis()/1000;
        
        if (cache.exist(key)) {
            // 如果密码相同，则认为保存成功
            String c_pwd = (String) cache.get(key);
            if (c_pwd.equals(pwd)) {
                return;
            }
            
            //如果密码不同，则更新密码
            cache.setx(key, pwd, saveExpire);
            return;
        }
        
        UserEntity user = userDao.queryUser(userNo);
        if (user==null) {
            userDao.saveUser(userNo, pwd, saveExpire);
            // 加入缓存
            cache.setx(key, pwd, saveExpire);
            return;
        }
        
        //如果密码相同
        if (user.getPwd().equals(pwd)) {
            return;
        }
        
        //如果密码不同，则更新密码 
        userDao.updatePwd(userNo, pwd, saveExpire);
        // 加入缓存
        cache.setx(key, pwd, saveExpire);
    }

}
