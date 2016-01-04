package com.shanlin.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shanlin.demo.bean.Response;
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
    
    /**
     * 功能描述: 添加系统，逻辑如下：<br>
     *  1. 
     *
     * @param request
     * @param response
     * @param sysCode
     * @param sysName
     */
    @RequestMapping("/add/do")
    public void add(HttpServletRequest request, HttpServletResponse response, String sysCode, String sysName){
        
        try {
            // 保存系统
            Response<String> serviceResponse = systemService.saveSystem(sysCode, sysName);
            if (!serviceResponse.isSuccess()) {
                ajaxJson(response, serviceResponse.getMsg());
            }
            
            String usr = (String) request.getSession().getAttribute("userNo");
            // set sys_usr_rel
            systemService.setSysUsrRel(usr, sysCode);
            
            String sys = (String) request.getSession().getAttribute("sysCode");
            if (sys==null) {
                super.setSession(request, "sysCode", sysCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson(response, "exption"+e.getMessage());
        }
        
        ajaxJson(response, "success");
    }
    
    @RequestMapping(value="/select", method=RequestMethod.POST)
    public void select(HttpServletRequest request,HttpServletResponse response,
            String sysCode){
        
        String usr = (String) request.getSession().getAttribute("userNo");
        // set sys_usr_rel
        systemService.setSysUsrRel(usr, sysCode);
        
        String sys = (String) request.getSession().getAttribute("sysCode");
        if (sys==null) {
            super.setSession(request, "sysCode", sysCode);
        }
        
        ajaxJson(response, "success");
    }
    
    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String update(HttpServletRequest request,Model model,String sysCode){
        // 删除当前系统
        request.getSession().removeAttribute("sysCode");
        
        // 返回到系统列表
        return this.list(model);
    }
}
