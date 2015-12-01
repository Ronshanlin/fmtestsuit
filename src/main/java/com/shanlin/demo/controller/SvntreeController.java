package com.shanlin.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shanlin.demo.bean.CompileBo;
import com.shanlin.demo.bean.SvnNode;
import com.shanlin.demo.helper.Svnkit;
import com.shanlin.demo.service.CompileService;
import com.shanlin.demo.utils.ClassPathUtil;
import com.shanlin.demo.utils.Constants;

import freemarker.template.Template;

@Controller
@RequestMapping("svn")
public class SvntreeController {
    
    @Autowired
    CompileService compileService;
    
    @RequestMapping("tree")
    public String showTree(Model model,String httpUrl,String username, String passpord){
        httpUrl = "";
        username="";
        passpord = "";
        SvnNode node = Svnkit.getSvnRepository(httpUrl, username, passpord,"");
        
        model.addAttribute("svnNode", node);
        
        System.out.println(ClassPathUtil.getClassPath());
        System.out.println(Constants.USER_DIR);
        
        return "svntools/svn_tree.ftl";
    }
    
    @RequestMapping("compile")
    public void compile(Model model, HttpServletResponse response, String httpUrl,
            String username, String passpord) {
        httpUrl = "";
        username = "";
        passpord = "";
        
        CompileBo compileBo = new CompileBo();
        compileBo.setSvnBranch(httpUrl);
        compileBo.setSvnUser(username);
        compileBo.setSvnPassword(passpord);
        compileBo.setSystemCode("geep");
        List<String> paths = new ArrayList<String>();
        paths.add("");
        compileBo.setJavaPaths(paths);
        compileBo.setVarsPath("");
        compileBo.setPropertiesPath("");
        compileBo.setSpringPath("");
        
        try {
            compileService.load(compileBo);
            response.getWriter().write("success");
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping("/genhtml_{system}_{htmlPath}")
    public void genHtml(){
        Writer out = new StringWriter();
        
        ByteArrayOutputStream bo = Svnkit.getFile("", "",
                "", "");
        
        Template template;
        try {
            template = new Template("index.ftl", new InputStreamReader(
                    new ByteArrayInputStream(bo.toByteArray())), null);
            template.process(null, out);
            System.out.println(out.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
