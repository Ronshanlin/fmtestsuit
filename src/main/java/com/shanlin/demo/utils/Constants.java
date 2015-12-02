/*
 * Copyright (C), 2013-2020, shanlin demo
 * FileName: Contants.java
 * Author:   shanlin
 * Date:     2015-11-4 下午3:47:42
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.shanlin.demo.utils;

/**
 * 常量类<br> 
 *
 * @author shanlin
 */
public abstract class Constants {
    public static final String USER_DIR = System.getProperty("user.dir")+"\\dynamic";
    public static final String LIB_PATH = USER_DIR+"\\lib";
    public static final String CLASSES_PATH = USER_DIR+"\\classes";
    public static final String SRC_PATH = USER_DIR+"\\src";
    public static final String CONF_PATH = USER_DIR+"\\resources";
    
    public static final long EXPIRE_TIME=5*24*60*60L;
    
    public static final String CACHE_USER_PWD="user:pwd:{0}";
    
    public static final String LOGIN_URL="/login";
}
