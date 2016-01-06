package com.shanlin.demo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shanlin.demo.dao.SystemConfigDao;
import com.shanlin.demo.helper.FreemarkerParser;
import com.shanlin.demo.helper.Svnkit;
import com.shanlin.demo.helper.cache.CacheFactory;
import com.shanlin.demo.helper.cache.CacheInterface;
import com.shanlin.demo.service.CompileService.SysConfType;
import com.shanlin.demo.service.PreViewService;
import com.shanlin.demo.utils.Constants;

import freemarker.template.Configuration;

@Service
public class PreViewServiceImpl implements PreViewService {
    private CacheInterface cache = CacheFactory.getCacheBean();
    private Gson gson = new GsonBuilder().create();
    
    @Autowired
    private SystemConfigDao configDao;
    
    @Override
    public String doPreView(String sysCode, String ftlPath, String userNo, String jsonData) {
        Configuration configuration = (Configuration)cache.hget(sysCode, "freemarkerConfig");
        
        String branch = configDao.getSysConfs(sysCode, SysConfType.SVNBRANCH).get(0);
        
        String key = MessageFormat.format(Constants.CACHE_USER_PWD, userNo);
        String pwd = (String) cache.get(key);
        
        // 读取svn 分支内容
        ByteArrayOutputStream out = Svnkit.getFile(branch, userNo, pwd, ftlPath);
        
        Map<String, Object> model = gson.fromJson(jsonData, new TypeToken<Map<String, Object>>() {
                 }.getType());
        
        String html = FreemarkerParser.parse(new ByteArrayInputStream(out.toByteArray()), model, configuration);
        
        return html;
    }

}
