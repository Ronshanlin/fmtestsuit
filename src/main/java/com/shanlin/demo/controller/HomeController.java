package com.shanlin.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    
    @RequestMapping("/index")
    public String indexx(){
        return "index.ftl";
    }
    
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("");
        
        
        
        return null;
    }
}
