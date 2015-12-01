package com.shanlin.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shanlin.demo.service.SystemService;

@Controller
@RequestMapping("/sys")
public class SystemController extends BaseController{
    
    @Autowired
    private SystemService systemService;
    
    @RequestMapping("/list")
    public String list(Model model){
        
        model.addAttribute("sysList", systemService.getSystems());
        
        return "system/sys-select.ftl";
    }
    
    @RequestMapping("/add/show")
    public String register(Model model){
        return "system/sys-add-show.ftl";
    }
    
    @RequestMapping("/add/do")
    public void add(HttpServletResponse response, String sysCode, String sysName){
        
        systemService.saveSystem(sysCode, sysName);
        
        ajaxJson(response, "success");
    }
    
    @RequestMapping(value="/select", method=RequestMethod.POST)
    public void select(HttpServletRequest request,HttpServletResponse response,
            String sysCode){
        
        String usr = (String) request.getSession().getAttribute("userNo");
        
        //TODO set sys_usr_rel
        
        String sys = (String) request.getSession().getAttribute("sysCode");
        if (sys==null) {
            request.getSession().setAttribute("sysCode", sysCode);
        }
        
        ajaxJson(response, "success");
        
    }
    
    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String update(HttpServletRequest request,Model model,String sysCode){
        // 删除当前系统
        request.getSession().removeAttribute("sysCode");
        
        return this.list(model);
    }
}
