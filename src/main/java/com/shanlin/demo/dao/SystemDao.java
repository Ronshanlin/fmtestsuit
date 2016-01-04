package com.shanlin.demo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shanlin.demo.entity.SystemEntity;

@Repository
public class SystemDao extends BaseDao{
    
    public void saveSystem(String sysCode, String sysName){
        String sql = "insert into system(sys_code,sys_name)values(:sysCode,:sysName)";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("sysCode", sysCode);
        paramMap.put("sysName", sysName);
        
        template.update(sql, paramMap);
    }
    
    public void setSysUsrRel(String userNo, String sysCode){
        String sql = "insert into system_user_rel(sys_code,user_no)values(:sysCode,:userNo)";
        
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("sysCode", sysCode);
        paramMap.put("userNo", userNo);
        
        template.update(sql, paramMap);
    }
    
    public boolean isExistSysUserRel(String userNo, String sysCode){
        String sql = "select count(1) from system_user_rel where sys_code=:sysCode and user_no=:userNo";
        
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("sysCode", sysCode);
        paramMap.put("userNo", userNo);
        
        int count = template.queryForObject(sql, paramMap, Integer.class);
        
        return count>0;
    }
    
    public SystemEntity querySystem(String sysCode){
        String sql = "select id, sys_code, sys_name from system where sys_code=:sysCode";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sysCode", sysCode);
        
        return super.queryForObject(sql, paramMap, SystemEntity.class);
    }
    
    public List<SystemEntity> querySystems(){
        
        String sql = "select id, sys_code, sys_name from system";
        
        List<SystemEntity> systems = new ArrayList<SystemEntity>();
        try {
            systems = super.queryForList(sql, null, SystemEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return systems;
    }
}
