package com.shanlin.demo.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DBClient {
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    public void save(){
        String sql = "";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        
        template.execute(sql, paramMap, new PreparedStatementCallback<Integer>() {

            @Override
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException,
                    DataAccessException {
                return null;
            }
        });
    }
}
