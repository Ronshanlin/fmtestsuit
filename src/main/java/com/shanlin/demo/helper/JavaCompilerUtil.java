/*
 * Copyright (C), 2002-2015, 苏宁易购电子商务有限公司
 * FileName: JavaCompiler.java
 * Author:   13073457
 * Date:     2015-11-4 下午2:14:30
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.shanlin.demo.helper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.shanlin.demo.utils.ClassPathUtil;
import com.shanlin.demo.utils.Constants;

/**
 * 编译器工具<br>
 * 
 * @author 13073457
 */
public class JavaCompilerUtil {
    
    /**
     * 功能描述: <br>
     *
     * @param classPath 类路径
     * @param systemCode 系统编码
     * @return 编译成功，返回当前系统的类途径，否则返回null;
     */
    public static String compile(String classPath, String systemCode) {
        System.out.println(classPath);

        File clazzPath = new File(classPath);
        Stack<File> stack = new Stack<File>();
        stack.push(clazzPath);

        // 遍历所有类
        List<File> fileList = new ArrayList<File>();
        while (!stack.isEmpty()) {
            File path = stack.pop();

            File[] files = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || pathname.getName().endsWith(".java");
                }
            });

            for (File sub : files) {
                if (sub.isDirectory()) {
                    stack.push(sub);
                    continue;
                }

                fileList.add(sub);
            }
        }

        // 编译
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        String classessPath = Constants.CLASSES_PATH+File.separator+systemCode;
        try {
            Iterable<? extends JavaFileObject> compilationUnits1 = fileManager
                    .getJavaFileObjectsFromFiles(fileList);
            File file = new File(classessPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            List<String> options = Arrays.asList("-Djava.ext.dirs="+ClassPathUtil.getClassPath()+"\\WEB-INF\\lib", 
                    "-d", classessPath);
            
            Writer out = new StringWriter();
            boolean isSuccess = compiler.getTask(out, fileManager, null, options, null,
                    compilationUnits1).call();
            // 编译错误
            System.err.println(out.toString());
            if (!isSuccess) {
                return null;
            }
        } finally {
            try {
                fileManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return classessPath;
    }
}
