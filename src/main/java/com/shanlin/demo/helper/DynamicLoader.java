/*
 * Copyright (C), 2002-2015, 苏宁易购电子商务有限公司
 * FileName: DynamicLoader.java
 * Author:   13073457
 * Date:     2015-11-8 下午3:07:40
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.shanlin.demo.helper;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 动态加载<br>
 * 
 * @author shazl
 */
public class DynamicLoader {

    /**
     * 功能描述: 动态加载jar文件<br>
     * 
     * @param libPath
     */
    public void loadJar(String libPath) throws Exception {
        File libDir = new File(libPath);

        // 获取jar列表
        File[] jars = libDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") || name.endsWith(".zip");
            }
        });

        // 没有jar文件
        if (jars == null || jars.length == 0) {
            return;
        }

        // 加载jar文件
        for (File jar : jars) {
            this.load(jar.toURI().toURL());
        }
    }

    /**
     * 功能描述: 加载类路径所有类(含子路径)<br>
     *
     * @param classPath
     * @throws Exception
     */
    public void loadClass(String classPath) throws Exception {
        File clazzPath = new File(classPath);

        if (!clazzPath.exists() || !clazzPath.isDirectory()) {
            return;
        }

        Stack<File> stack = new Stack<File>();
        stack.push(clazzPath);

        while (!stack.isEmpty()) {
            File path = stack.pop();

            File[] files = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || pathname.getName().endsWith(".class");
                }
            });

            for (File sub : files) {
                if (sub.isDirectory()) {
                    stack.push(sub);
                    continue;
                }
                
                // 添加类到加载器中
                this.load(clazzPath.toURI().toURL());
            }
        }
    }
    
    public List<String> getClassesName(String classPath){
        File clazzPath = new File(classPath);

        if (!clazzPath.exists() || !clazzPath.isDirectory()) {
            return new ArrayList<String>();
        }

        Stack<File> stack = new Stack<File>();
        stack.push(clazzPath);

        List<String> classesName = new ArrayList<String>();
        while (!stack.isEmpty()) {
            File path = stack.pop();

            File[] files = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || pathname.getName().endsWith(".class");
                }
            });

            for (File sub : files) {
                if (sub.isDirectory()) {
                    stack.push(sub);
                    continue;
                }
                // 取文件名
                String className = sub.getAbsolutePath();
                className = className.substring(clazzPath.getAbsolutePath().length()+1, className.length()-6)
                        .replace(File.separatorChar, '.');
                
                classesName.add(className);
            }
        }
        
        return classesName;
    }
    
    /**
     * 功能描述: 获取类实例<br>
     *
     * @param className 全名
     * @return
     */
    public <T> T getClassInstance(String className){
        try {
            Class<?> clazz = Class.forName(className);
            
            if (!clazz.isInterface()) {
                return (T) clazz.newInstance();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void load(URL url) throws Exception{
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        boolean accessible = method.isAccessible();
        
        try {
            if (accessible == false) {
                method.setAccessible(true);
            }
            // 设置类加载器
            URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
            
            if (classLoader.findResource(url.getPath())==null) {
                // 将当前类路径加入到类加载器中
                method.invoke(classLoader, url);
            }
        } finally {
            method.setAccessible(accessible);
        }
    }
}
