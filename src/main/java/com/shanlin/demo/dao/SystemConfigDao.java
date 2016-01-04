package com.shanlin.demo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.shanlin.demo.entity.SysConfEntity;
import com.shanlin.demo.entity.SysConfValsEntity;
import com.shanlin.demo.service.CompileService.SysConfType;

@Repository
public class SystemConfigDao extends BaseDao{
    
    public void saveSystemConf(String sysCode, String sysConfCode, String sysConfName){
        String sql = "insert into system_conf" +
        		"(sys_code,sys_conf_code,sys_conf_name)" +
        		"values" +
        		"(:sysCode,:sysConfCode,:sysConfName)";
        
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("sysCode", sysCode);
        paramMap.put("sysConfCode", sysConfCode);
        paramMap.put("sysConfName", sysConfName);
        
        template.update(sql, paramMap);
    }
    
    /**
     * 功能描述: 批量保存系统配置<br>
     *
     * @param entities
     */
    @SuppressWarnings("unchecked")
    public void batchSaveSystemConf(List<SysConfEntity> entities){
        String sql = "insert into system_conf" +
                "(sys_code,sys_conf_code,sys_conf_name)" +
                "values" +
                "(:sysCode,:sysConfCode,:sysConfName)";
        
        Map<String, String>[] batchValues = new HashMap[entities.size()];
        Map<String, String> paramMap = null;
        int i = 0;
        for (SysConfEntity entity : entities) {
            paramMap = new HashMap<String, String>();
            paramMap.put("sysCode", entity.getSysCode());
            paramMap.put("sysConfCode", entity.getSysConfCode());
            paramMap.put("sysConfName", entity.getSysConfName());
            
            batchValues[i++] = paramMap;
        }
        
        template.batchUpdate(sql, batchValues);
    }
    
    @SuppressWarnings("unchecked")
    public void saveSystemConfVals(List<SysConfValsEntity> vals){
        String sql = "insert into system_conf_vals" +
                "(sys_conf_code,sys_conf_key,sys_conf_val)" +
                "values" +
                "(:sysConfCode,:sysConfKey,:sysConfVal)";
        
        Map<String, String>[] batchValues = new HashMap[vals.size()];
        Map<String, String> paramMap = null;
        int i = 0;
        for (SysConfValsEntity entity : vals) {
            paramMap = new HashMap<String, String>();
            paramMap.put("sysConfCode", entity.getSysConfCode());
            paramMap.put("sysConfKey", entity.getSysConfKey());
            paramMap.put("sysConfVal", entity.getSysConfVal());
            
            batchValues[i++] = paramMap;
        }
        
        template.batchUpdate(sql, batchValues);
    }
    
    /**
     * 功能描述: 根据系统，查询系统配置<br>
     *
     * @param sysCode
     * @param sysConfCode
     * @return
     */
    public List<String> getSysConfs(String sysCode, SysConfType sysConfType){
        String sql = "sys_conf_name" +
        		"from system_conf" +
        		"where sys_code:sysCode";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("sysCode", sysCode);
        paramMap.put("sysConfCode", sysConfType.getValue());
        
        return super.queryForList(sql, paramMap, String.class);
    }
}
