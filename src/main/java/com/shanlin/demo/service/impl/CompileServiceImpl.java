package com.shanlin.demo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.util.StringUtils;

import com.shanlin.demo.bean.CompileBo;
import com.shanlin.demo.bean.Response;
import com.shanlin.demo.bean.SvnNode;
import com.shanlin.demo.dao.SystemConfigDao;
import com.shanlin.demo.entity.SysConfEntity;
import com.shanlin.demo.helper.DynamicLoader;
import com.shanlin.demo.helper.JavaCompilerUtil;
import com.shanlin.demo.helper.SpringXmlParser;
import com.shanlin.demo.helper.Svnkit;
import com.shanlin.demo.helper.cache.CacheFactory;
import com.shanlin.demo.helper.cache.CacheInterface;
import com.shanlin.demo.service.CompileService;
import com.shanlin.demo.utils.Constants;
import com.shanlin.demo.utils.PropertiesUtil;
import com.shanlin.demo.utils.VariableReplacement;

import freemarker.template.Configuration;

@Service
public class CompileServiceImpl implements CompileService {

    private CacheInterface cache = CacheFactory.getCacheBean();
    
    @Autowired
    private SystemConfigDao configDao;
    
    public Response<SvnNode> getSvnTree(String userNo, String sysCode){
        Response<SvnNode> response = new Response<SvnNode>();
        
        String key = MessageFormat.format(Constants.CACHE_USER_PWD, userNo);
        String pwd = (String) cache.get(key);
        // svn地址
        String httpUrl = configDao.getSysConfs(sysCode, SysConfType.SVNBRANCH).get(0);
        if (StringUtils.isEmpty(httpUrl)) {
            response.setSuccess(false);
            response.setMsg("没有查询到svn分支地址");
            
            return response;
        }
        
        boolean hasAuth = Svnkit.testAuth(httpUrl, userNo, pwd);
        if (!hasAuth) {
            response.setSuccess(false);
            response.setMsg("没有权限");
            
            return response;
        }
        
        // 获取节点
        SvnNode node = Svnkit.getSvnRepository(httpUrl, userNo, pwd,"");
        response.setObj(node);
        
        return response;
    }
    
    @Override
    @Transactional
    public void load(CompileBo compileBo) throws Exception {
        // 保存配置数据
        this.saveSysConf(compileBo);
        // 加载类
        this.loadClass(compileBo);
        // 获取环境变量
        Properties varsProp = this.loadVars(compileBo);
        // 获取properties文件
        Properties settings = this.loadSettings(compileBo, varsProp);
        // 加载freemarker config
        this.loadSpring(compileBo, varsProp, settings);
    }
    
    public void reLoad(String sysCode, String userNo) throws Exception{
        CompileBo compileBo = new CompileBo();
        compileBo.setJavaPaths(configDao.getSysConfs(sysCode, SysConfType.JAVA));
        compileBo.setSvnBranch(configDao.getSysConfs(sysCode, SysConfType.SVNBRANCH).get(0));
        compileBo.setPropertiesPath(configDao.getSysConfs(sysCode, SysConfType.PROPERTIES).get(0));
        compileBo.setSpringPath(configDao.getSysConfs(sysCode, SysConfType.VARS_PROPS).get(0));
        compileBo.setVarsPath(configDao.getSysConfs(sysCode, SysConfType.XML).get(0));
        compileBo.setSvnUser(userNo);
        
        String key = MessageFormat.format(Constants.CACHE_USER_PWD, userNo);
        String pwd = (String) cache.get(key);
        compileBo.setSvnPassword(pwd);
        compileBo.setSystemCode(sysCode);
        
        // 加载类
        this.loadClass(compileBo);
        // 获取环境变量
        Properties varsProp = this.loadVars(compileBo);
        // 获取properties文件
        Properties settings = this.loadSettings(compileBo, varsProp);
        // 加载freemarker config
        this.loadSpring(compileBo, varsProp, settings);
    };
    
    /**
     * 功能描述: 保存系统配置<br>
     *
     * @param compileBo
     */
    public void saveSysConf(CompileBo compileBo){
        String system = compileBo.getSystemCode();
        
        List<SysConfEntity> sysConfEntities = new ArrayList<SysConfEntity>();
        SysConfEntity propsEntity = new SysConfEntity();
        propsEntity.setSysCode(system);
        propsEntity.setSysConfCode(SysConfType.PROPERTIES.getValue());
        propsEntity.setSysConfName(compileBo.getPropertiesPath());
        
        SysConfEntity svnEntity = new SysConfEntity();
        svnEntity.setSysCode(system);
        svnEntity.setSysConfCode(SysConfType.SVNBRANCH.getValue());
        svnEntity.setSysConfName(compileBo.getSvnBranch());
        
        SysConfEntity xmlEntity = new SysConfEntity();
        xmlEntity.setSysCode(system);
        xmlEntity.setSysConfCode(SysConfType.XML.getValue());
        xmlEntity.setSysConfName(compileBo.getSpringPath());
        
        SysConfEntity varsEntity = new SysConfEntity();
        varsEntity.setSysCode(system);
        varsEntity.setSysConfCode(SysConfType.VARS_PROPS.getValue());
        varsEntity.setSysConfName(compileBo.getVarsPath());
        
        sysConfEntities.add(propsEntity);
        sysConfEntities.add(xmlEntity);
        sysConfEntities.add(varsEntity);
        
        SysConfEntity javaEntity = null;
        for (String path : compileBo.getJavaPaths()) {
            javaEntity = new SysConfEntity();
            javaEntity.setSysCode(system);
            javaEntity.setSysConfCode(SysConfType.JAVA.getValue());
            javaEntity.setSysConfName(path);
            
            sysConfEntities.add(javaEntity);
        }
        
        configDao.batchSaveSystemConf(sysConfEntities);
    }
    
    private Configuration loadSpring(CompileBo compileBo, Properties varsProp, Properties settings)
            throws IOException {
        String key = compileBo.getSystemCode();
        String field = "freemarkerConfig";
        
        if (compileBo.getRefresh() && cache.hexist(key, field)) {
            return (Configuration)cache.hget(key, field);
        }
        
        // 获取spring xml路径
        String confPath = this.getConfPath(compileBo.getSystemCode(), compileBo.getSpringPath());
        // 读取
        ByteArrayOutputStream out = Svnkit.getFile(compileBo.getSvnBranch(), compileBo.getSvnUser(),
              compileBo.getSvnPassword(), compileBo.getSpringPath());
        // 替换变量
//        Writer writer = VariableReplacement.replaceByProps(settings, new InputStreamReader(
//                new ByteArrayInputStream(out.toByteArray())));
//        
//        Writer writer2 = VariableReplacement.replaceByProps(varsProp, new InputStreamReader(
//                new ByteArrayInputStream(writer.toString().getBytes())));
//        
//        // 配置写入磁盘
//        FileWriter fileWriter=new FileWriter(new File(confPath));
//        fileWriter.write(writer2.toString());
//        fileWriter.close();
        
        // 解析xml,获取bean
        SpringXmlParser builder = SpringXmlParser.getInstance(confPath);
        Configuration fmConfig = builder.getBean(FreeMarkerConfigurationFactoryBean.class).getObject();
        
        cache.hset(key, field, fmConfig);
        
        if (out!=null) {
            out.close();
        }
        
        return fmConfig;
    }
    
    
    private Properties loadSettings(CompileBo compileBo, Properties varsProp) throws IOException{
        String key = compileBo.getSystemCode();
        String field = compileBo.getPropertiesPath();
        
        if (compileBo.getRefresh() && cache.hexist(key, field)) {
            return (Properties)cache.hget(key, field);
        }
        
        // 替换变量
        ByteArrayOutputStream out = Svnkit.getFile(compileBo.getSvnBranch(), compileBo.getSvnUser(),
                compileBo.getSvnPassword(), compileBo.getPropertiesPath());
        
        Writer writer = VariableReplacement.replaceByProps(varsProp, new InputStreamReader(
                new ByteArrayInputStream(out.toByteArray())));
        
        // 转为properties文件
        Properties mainSetting = PropertiesUtil.getProps(new ByteArrayInputStream(writer.toString().getBytes()));
        
        cache.hset(key, field, mainSetting);
        
        return mainSetting;
    }
    /**
     * 功能描述: 获取环境变量<br>
     *
     * @param compileBo
     * @return
     */
    private Properties loadVars(CompileBo compileBo){
        String key = compileBo.getSystemCode();
        if (compileBo.getRefresh() && cache.hexist(key, compileBo.getVarsPath())) {
            return (Properties)cache.hget(key, compileBo.getVarsPath());
        }
        
        
        // 获取vars
        ByteArrayOutputStream out = Svnkit.getFile(compileBo.getSvnBranch(), compileBo.getSvnUser(),
                compileBo.getSvnPassword(), compileBo.getVarsPath());
        
        if (out==null) {
            return null;
        }
        
        Properties varsProp = PropertiesUtil.getProps(new ByteArrayInputStream(out.toByteArray()));
        cache.hset(compileBo.getSystemCode(), compileBo.getVarsPath(),varsProp);
        
        if (out!=null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return varsProp;
    } 
    
    /**
     * 功能描述: 加载类<br>
     *
     * @param compileBo
     * @throws Exception
     */
    private void loadClass(CompileBo compileBo) throws Exception{
        String system = compileBo.getSystemCode();
        
        ByteArrayOutputStream out = null;
        for (String path : compileBo.getJavaPaths()) {
            out = Svnkit.getFile(compileBo.getSvnBranch(), compileBo.getSvnUser(),
                    compileBo.getSvnPassword(), path);
            // 写入到指定文件
            String javaPath = this.getJavaPath(system, path);

            new FileOutputStream(new File(javaPath)).write(out.toByteArray());
        }
        
        // 编译
        String classesPath = JavaCompilerUtil.compile(Constants.SRC_PATH+File.separator+system,system);
        if (classesPath == null) {
            return;
        }
        
        // 加载类
        DynamicLoader loader = new DynamicLoader();
        loader.loadClass(classesPath);
    }
    
    /**
     * 功能描述: 获取java存储路径<br>
     *
     * @param system
     * @param path
     * @return str
     */
    private String getJavaPath(String system, String path){
        String prefix = "src/main/java";
        int pos = path.indexOf(prefix);
        int endPos = path.lastIndexOf("/");
        
        if (pos<0) {
            return null;
        }
        
        // 包路径是否存在，如果不存在，则创建 
        String dir = path.substring(pos+prefix.length(), endPos);
        File file = new File(Constants.SRC_PATH+File.separator+system+dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        
        String java = path.substring(pos+prefix.length(), path.length());
        return Constants.SRC_PATH+File.separator+system+java;
    }
    
    private String getConfPath(String system, String path){
        String prefix = "src/main/resources";
        int pos = path.indexOf(prefix);
        int endPos = path.lastIndexOf("/");
        
        if (pos<0) {
            return null;
        }
        
        // 包路径是否存在，如果不存在，则创建 
        String dir = path.substring(pos+prefix.length(), endPos);
        File file = new File(Constants.CONF_PATH+File.separator+system+dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        
        String java = path.substring(pos+prefix.length(), path.length());
        return Constants.CONF_PATH+File.separator+system+java;
    }
}
