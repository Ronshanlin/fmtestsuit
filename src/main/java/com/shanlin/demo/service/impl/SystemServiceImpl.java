package com.shanlin.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shanlin.demo.bean.Response;
import com.shanlin.demo.dao.SystemDao;
import com.shanlin.demo.entity.SystemEntity;
import com.shanlin.demo.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService{
    
    @Autowired
    private SystemDao systemDao;

    @Override
    public List<SystemEntity> getSystems() {
        return systemDao.querySystems();
    }

    @Override
    public void setSysUsrRel(String userNo, String sysCode) {
        boolean isExist = systemDao.isExistSysUserRel(userNo, sysCode);
        
        // 如果不存在，则添加关系
        if (!isExist) {
            systemDao.setSysUsrRel(userNo, sysCode);
        }
    }

    @Override
    public Response<String> saveSystem(String sysCode, String sysName) {
        Response<String> response = new Response<String>();
        
        if (StringUtils.isEmpty(sysCode) || StringUtils.isEmpty(sysName)) {
            response.setSuccess(false);
            response.setMsg("系统编码或系统名称为空！");
            return response;
        }
        
        String code = sysCode.toUpperCase();
        
        SystemEntity entity = systemDao.querySystem(sysCode);
        if (entity!=null) {
            response.setSuccess(false);
            response.setMsg("该系统已存在！");
        }
        
        systemDao.saveSystem(code, sysName);
        
        return response;
    }
}
