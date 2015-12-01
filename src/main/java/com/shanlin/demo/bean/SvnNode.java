package com.shanlin.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class SvnNode {
    private String name;
    private String parentPath;
    private int kind;
    
    private List<SvnNode> nodes = new ArrayList<SvnNode>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public List<SvnNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<SvnNode> nodes) {
        this.nodes = nodes;
    }
}
