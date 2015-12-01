/*
 * Copyright (C), 2013-2020, shanlin demo
 * FileName: ProjectInitialize.java
 * Author:   shanlin
 * Date:     2015-11-12 下午5:07:09
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.shanlin.demo.helper;

import java.io.File;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.shanlin.demo.utils.Constants;

/**
 * 项目初始化<br> 
 *
 * @author shanlin
 * @see [相关类/方法]（可选）
 */
@Component
public class ProjectInitialize implements InitializingBean{

    @Override
    public void afterPropertiesSet() throws Exception {
        // lib
        makeDir(Constants.LIB_PATH);
        // scr
        makeDir(Constants.SRC_PATH);
        // classes
        makeDir(Constants.CLASSES_PATH);
    }
    
    private void makeDir(String filePath){
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
