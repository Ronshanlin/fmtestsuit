package com.shanlin.demo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shanlin.demo.entity.SystemEntity;
import com.shanlin.demo.utils.Constants;

@Repository
public class UserDao {
    
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    @Autowired
    private BaseDao baseDao;
    
    /**
     * 功能描述: 保存用户<br>
     *
     * @param userNo
     * @param pwd
     * @param expire 过期时间，单位秒
     */
    public void saveUser(String userNo, String pwd, long expire){
        if (expire<1) {
            expire = Constants.EXPIRE_TIME;
        }
        
        String sql = "insert into user " +
        		"(user_no,user_password, create_time, pwd_expire)" +
        		"values" +
        		"(:userNo,:pwd, curDate(), :expire)";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userNo", userNo);
        paramMap.put("pwd", pwd);
        paramMap.put("expire", expire);
        
        template.update(sql, paramMap);
    }
}
