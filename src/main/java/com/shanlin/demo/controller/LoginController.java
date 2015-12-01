package com.shanlin.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController extends BaseController {

    @RequestMapping("/login")
    public String login(Model model, String targetUrl) {
        model.addAttribute("targetUrl", targetUrl);
        return "login.ftl";
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
        // 添加session
        request.getSession().setAttribute("userNo", userNo);
        request.getSession().setMaxInactiveInterval(7200);
        
        jsonMap.put("code", "0");
        ajaxJson(response, jsonMap);
    }
}