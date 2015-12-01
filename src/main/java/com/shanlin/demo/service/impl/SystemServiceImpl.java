package com.shanlin.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        
        if (isExist) {
            systemDao.setSysUsrRel(userNo, sysCode);
        }
    }

    @Override
    public void saveSystem(String sysCode, String sysName) {
        if (StringUtils.isEmpty(sysCode) || StringUtils.isEmpty(sysName)) {
            return;
        }
        
        String code = sysCode.toUpperCase();
        
        systemDao.saveSystem(code, sysName);
    }
}
