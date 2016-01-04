package com.shanlin.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shanlin.demo.service.PreViewService;

@Controller
@RequestMapping("/preview")
public class PreviewController {
    
    @Autowired
    private PreViewService preViewService;
    
    @RequestMapping("/show")
    public String show(Model model, HttpServletRequest request, 
            String requestPath, String data) {
        String sysCode = (String) request.getSession().getAttribute("sysCode");
        String userNo = (String) request.getSession().getAttribute("userNo");
        
        String html = preViewService.doPreView(sysCode, requestPath, userNo, data);
        
        model.addAttribute("html", html);
        
        return "preview/preview_show.ftl";
    }
}
