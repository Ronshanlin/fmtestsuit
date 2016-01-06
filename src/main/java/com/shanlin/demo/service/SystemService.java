package com.shanlin.demo.service;

import java.util.List;

import com.shanlin.demo.bean.Response;
import com.shanlin.demo.entity.SystemEntity;

public interface SystemService {
    public List<SystemEntity> getSystems();
    
    public Response<String> saveSystem(String sysCode, String sysName);
    
    public void setSysUsrRel(String userNo, String sysCode);
    
    public Response<String> updateSvnBranch(String sysCode, String branch);
    public Response<String> getSvnBranch(String sysCode);
}
