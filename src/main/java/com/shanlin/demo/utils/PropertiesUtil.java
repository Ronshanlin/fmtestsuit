/*
 * Copyright (C), 2013-2020, shanlin demo
 * FileName: PropertiesUtil.java
 * Author:   shanlin
 * Date:     2015-11-16 上午11:56:16
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.shanlin.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * properties工具<br> 
 *
 * @author shanlin
 */
public class PropertiesUtil {
    private PropertiesUtil(){}
    
    public static Properties getProps(InputStream in){
        Properties properties = new Properties();
        
        Reader reader = new InputStreamReader(in);
        try {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return properties;
    }
    
    public static String getPropertiesVal(String key){
        if (key==null) {
            return null;
        }
        
        Properties properties = new Properties();
        
        Resource resource = new ClassPathResource("main-setting-web.properties");
        
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return properties.getProperty(key);
    }
}
