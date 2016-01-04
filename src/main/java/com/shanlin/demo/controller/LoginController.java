package com.shanlin.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.shanlin.demo.service.UserService;

@Controller
public class LoginController extends BaseController {
    
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ModelAndView login(Model model, HttpServletRequest request, String targetUrl) {
        ModelAndView mav = new ModelAndView("login.ftl");
        
        String usr = (String)request.getSession().getAttribute("userNo");
        if (usr == null) {
            model.addAttribute("targetUrl", targetUrl);
            return mav;
        }
        
        if (StringUtils.isEmpty(targetUrl)) {
            mav.setView(new RedirectView("index", true));
        }else {
            mav.setView(new RedirectView(targetUrl));
        }
        
        return mav;
    }

    @RequestMapping("/login/confirm")
    public void confirmLogin(HttpServletRequest request, HttpServletResponse response,
            String userNo, String pwd) {

        Map<String, String> jsonMap = new HashMap<String, String>();
        if (StringUtils.isEmpty(userNo) || StringUtils.isEmpty(pwd)) {
            jsonMap.put("code", "1");
            jsonMap.put("msg", "用户名或密码不可为空");
            ajaxJson(response, jsonMap);
            return;
        }
        // 保存用户
        try {
            userService.saveUser(userNo, pwd, 0);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "1");
            jsonMap.put("msg", "登录失败！");
            ajaxJson(response, jsonMap);
            return;
        }
        // 添加session
        request.getSession().setAttribute("userNo", userNo);
        request.getSession().setMaxInactiveInterval(7200);
        
        jsonMap.put("code", "0");
        ajaxJson(response, jsonMap);
    }
    
    @RequestMapping("/login/check")
    public void isLogin(HttpServletRequest request, HttpServletResponse response){
        String usr = (String)request.getSession().getAttribute("userNo");
        if (usr == null) {
            ajaxJson(response, "false");
            return;
        }
        
        ajaxJson(response, usr);
    }
}
