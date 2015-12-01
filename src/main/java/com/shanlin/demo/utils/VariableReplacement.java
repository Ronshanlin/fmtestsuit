package com.shanlin.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

public class VariableReplacement {

    public static Writer replaceByProps(Properties props, Reader reader) throws IOException {
        if (props == null || props.isEmpty()) {
            return null;
        }

        BufferedReader br = new BufferedReader(reader);
        String line = null;
        Writer writer = new StringWriter();
        while ((line = br.readLine()) != null) {
            // 替换变量
            String lineAfterReplace = replaceString(line, props);
            writer.append(lineAfterReplace);
            writer.append("\n");
        }

        if (br!=null) {
            br.close();
        }
        
        return writer;
    }

    private static String replaceString(String line, Properties props){
        if (line==null || line=="") {
            return line;
        }
        
        char c = 0;
        int len = line.length();
        
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while(i<len){
            c = line.charAt(i);
            
            switch (c) {
            case '$':
                int next = i+1;
                // 
                if (line.charAt(next) == '{') {
                    int end = line.indexOf('}', next);
                    if (end<0) {
                        break;
                    }
                    
                    String val = props.getProperty(line.substring(next+1, end));
                    buf.append(val);
                    i = next+end;
                    break;
                }
                
                if (line.charAt(next) == '[') {
                    int end = line.indexOf(']', next);
                    if (end<0) {
                        break;
                    }
                    
                    String val = props.getProperty(line.substring(next, end));
                    buf.append(val);
                    i = next+end;
                    
                    break;
                }
                
                i++;
                break;
            default:
                buf.append(c);
                i++;
                break;
            }
        }
        
        return buf.toString();
    }
}
