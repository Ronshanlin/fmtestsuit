package com.shanlin.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.shanlin.demo.utils.JsonUtil;

public class BaseController {
    
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception e){
        ModelAndView mav = new ModelAndView("error.ftl");
        mav.addObject("msg", "出错了！囧");
        e.printStackTrace();
        
        return mav;
    }
    
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
    
    /**
     * 功能描述: 设置session<br>
     *
     * @param request
     * @param key
     * @param value
     */
    protected void setSession(HttpServletRequest request, String key, String value){
        request.getSession().setAttribute(key, value);
        request.getSession().setMaxInactiveInterval(7200);
    }
    
    protected String getUserNo(HttpServletRequest request){
        return (String) request.getSession().getAttribute("userNo");
    }
}
