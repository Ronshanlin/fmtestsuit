package com.shanlin.demo.service;

import com.shanlin.demo.bean.CompileBo;

public interface CompileService {
    
    public void load(CompileBo compileBo) throws Exception;
    
    public void reLoad(String sysCode, String userNo) throws Exception;
    
    public static enum SysConfType{
        SVNBRANCH("svnBranch"),
        PROPERTIES("properties"),
        VARS_PROPS("vars_props"),
        JAVA("java"),
        XML("xml");
        
        private String value;
        SysConfType(String value){
            this.value = value;
        }
        
        public String getValue(){
            return this.value;
        }
    } 
}
