package com.shanlin.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.shanlin.demo.utils.JsonUtil;

public class BaseController {
    
    
    protected void ajaxJson(HttpServletResponse response, Object obj){
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.getWriter().write(JsonUtil.toJson(obj));
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
