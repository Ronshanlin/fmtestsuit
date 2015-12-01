package com.shanlin.demo.utils;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;

public class ClassPathUtil{
    
    public static InputStream getResourceAsStream(String path){
        ServletContext sc = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        
        return sc.getResourceAsStream(path);
    }
    
    public static String getClassPath(){
        ServletContext sc = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        
        return sc.getRealPath("/");
    }
}
