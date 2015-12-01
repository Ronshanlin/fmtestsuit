package com.shanlin.demo.bean;

import java.util.List;

public class CompileBo {
    private String svnBranch;
    private String svnUser;
    private String svnPassword;
    private List<String> javaPaths;
    private String varsPath;
    private String propertiesPath;
    private String springPath;
    private String systemCode;
    private boolean refresh = false;
    
    public List<String> getJavaPaths() {
        return javaPaths;
    }
    public void setJavaPaths(List<String> javaPaths) {
        this.javaPaths = javaPaths;
    }
    public String getVarsPath() {
        return varsPath;
    }
    public void setVarsPath(String varsPath) {
        this.varsPath = varsPath;
    }
    public String getPropertiesPath() {
        return propertiesPath;
    }
    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }
    public String getSvnBranch() {
        return svnBranch;
    }
    public void setSvnBranch(String svnBranch) {
        this.svnBranch = svnBranch;
    }
    public String getSvnUser() {
        return svnUser;
    }
    public void setSvnUser(String svnUser) {
        this.svnUser = svnUser;
    }
    public String getSvnPassword() {
        return svnPassword;
    }
    public void setSvnPassword(String svnPassword) {
        this.svnPassword = svnPassword;
    }
    public String getSystemCode() {
        return systemCode;
    }
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
    public String getSpringPath() {
        return springPath;
    }
    public void setSpringPath(String springPath) {
        this.springPath = springPath;
    }
    public boolean getRefresh() {
        return refresh;
    }
    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
