package com.shanlin.demo.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shanlin.demo.utils.Constants;

public class LoginFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse sresponse,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)sresponse;
        
        String contextPath = request.getContextPath();
        String url = request.getRequestURI().replace(contextPath, "");
        if(url.startsWith(Constants.LOGIN_URL) || url.equals(Constants.INDEX_URL)){
            chain.doFilter(servletRequest, sresponse);
            return;
        }
        
        String userNo=(String) request.getSession().getAttribute("userNo");
        if (userNo==null) {
            String targetUrl = "http://"+request.getServerName()+":"+request.getServerPort()+contextPath+url;
            targetUrl = URLEncoder.encode(targetUrl,"utf-8");
            response.sendRedirect(contextPath+"/login?targetUrl="+targetUrl);
            
            return;
        }
        
        chain.doFilter(servletRequest, sresponse);
    }

    @Override
    public void destroy() {
        // nothing to do
    }

}
