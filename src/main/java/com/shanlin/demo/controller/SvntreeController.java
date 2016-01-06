package com.shanlin.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shanlin.demo.bean.CompileBo;
import com.shanlin.demo.bean.Response;
import com.shanlin.demo.bean.SvnNode;
import com.shanlin.demo.helper.Svnkit;
import com.shanlin.demo.service.CompileService;
import com.shanlin.demo.service.SystemService;

import freemarker.template.Template;

@Controller
@RequestMapping("svn")
public class SvntreeController extends BaseController{
    
    @Autowired
    private CompileService compileService;
    
    @Autowired
    private SystemService systemService;
    
    
    @RequestMapping("/add/show/{sysCode}")
    public ModelAndView addSvnBranch(HttpServletRequest request,
            @PathVariable("sysCode") String sysCode){
        ModelAndView mav = new ModelAndView("system/sys-svn-add-show.ftl");
        
        mav.getModel().put("sysCode", sysCode);
        return mav;
    }
    
    /**
     * 功能描述: 添加svn分支<br>
     *
     * @param request
     * @param response
     * @param sysCode
     * @param branch
     */
    @RequestMapping("/add/do")
    public void add(HttpServletRequest request, HttpServletResponse response, String sysCode, String branch){
        
        try {
            // 保存系统
            Response<String> serviceResponse = systemService.updateSvnBranch(sysCode, branch);
            if (!serviceResponse.isSuccess()) {
                ajaxJson(response, serviceResponse.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson(response, "exption:"+e.getMessage());
        }
        
        ajaxJson(response, "success");
    }
    
    @RequestMapping("tree")
    public String showTree(Model model, HttpServletRequest request, String httpUrl, 
            String sysCode) {

        String userNo=super.getUserNo(request);
        
        Response<SvnNode> resp = compileService.getSvnTree(userNo, sysCode);
        
        if (!resp.isSuccess()) {
            model.addAttribute("msg", resp.getMsg());
            return "error.ftl";
        }
        
        model.addAttribute("svnNode", resp.getObj());
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
        compileBo.setSystemCode("");
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
