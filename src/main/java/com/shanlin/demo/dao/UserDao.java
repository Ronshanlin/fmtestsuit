package com.shanlin.demo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.shanlin.demo.entity.UserEntity;

@Repository
public class UserDao extends BaseDao{
    
    /**
     * 功能描述: 保存用户<br>
     *
     * @param userNo
     * @param pwd
     * @param expire 过期时间，单位秒
     */
    public void saveUser(String userNo, String pwd, long expire){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String sql = "insert into user " +
        		"(user_no,user_password, create_time, pwd_expire)" +
        		"values" +
        		"(:userNo,:pwd, :creatTime, :expire)";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userNo", userNo);
        paramMap.put("pwd", pwd);
        paramMap.put("expire", expire);
        paramMap.put("creatTime", format.format(new Date()));
        
        template.update(sql, paramMap);
    }
    
    public UserEntity queryUser(String userNo){
        String sql = "select user_no, user_password as pwd, pwd_expire as expire " +
        		"from user where user_no=:userNo";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userNo", userNo);
        
        return super.queryForObject(sql, paramMap, UserEntity.class);
    }
    
    public void updatePwd(String userNo, String pwd, long expire){
        String sql = "update user " +
                "set " +
                "user_password=:pwd,pwd_expire=:expire " +
                "where " +
                "user_no=:userNo";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userNo", userNo);
        paramMap.put("pwd", pwd);
        paramMap.put("expire", expire);
        
        template.update(sql, paramMap);
    }
}
