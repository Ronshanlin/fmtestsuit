package com.shanlin.demo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.shanlin.demo.bean.CompileBo;
import com.shanlin.demo.helper.DynamicLoader;
import com.shanlin.demo.helper.JavaCompilerUtil;
import com.shanlin.demo.helper.SpringXmlParser;
import com.shanlin.demo.helper.Svnkit;
import com.shanlin.demo.helper.cache.CacheFactory;
import com.shanlin.demo.helper.cache.CacheInterface;
import com.shanlin.demo.helper.cache.CacheType;
import com.shanlin.demo.service.CompileService;
import com.shanlin.demo.utils.Constants;
import com.shanlin.demo.utils.PropertiesUtil;
import com.shanlin.demo.utils.VariableReplacement;

import freemarker.template.Configuration;

@Service
public class CompileServiceImpl implements CompileService {

    private CacheInterface cache = CacheFactory.getCacheBean(CacheType.MAP);
    
    @Override
    public void load(CompileBo compileBo) throws Exception {
        // 加载类
        this.loadClass(compileBo);
        // 获取环境变量
        Properties varsProp = this.loadVars(compileBo);
        // 获取properties文件
        Properties settings = this.loadSettings(compileBo, varsProp);
        // 获取freemarker config
        Configuration configuration = this.loadSpring(compileBo, varsProp, settings);
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
