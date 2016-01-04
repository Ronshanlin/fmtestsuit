package com.shanlin.demo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao {
    
    @Autowired
    protected NamedParameterJdbcTemplate template;
    
    protected <T> List<T> queryForList(String sql, Map<String, Object> paramMap, Class<T> requiredType){
        if (paramMap==null) {
            paramMap = new HashMap<String, Object>();
        }
        return template.query(sql, paramMap, new RowMapperFactory<T>(requiredType).getRowMapper());
    }
    
    protected <T> T queryForObject(String sql, Map<String, Object> paramMap, Class<T> requiredType){
        if (paramMap==null) {
            paramMap = new HashMap<String, Object>();
        }
        List<T> results = template.query(sql, paramMap, new RowMapperFactory<T>(requiredType).getRowMapper());
        
        return this.singleResult(results);
    }
    
    private <T> T singleResult(List<T> results){
        if (results == null || results.isEmpty()) {
            return null;
        }
        
        return results.iterator().next();
    }
}
