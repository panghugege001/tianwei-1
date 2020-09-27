package com.nnti.pay.controller.vo;

import java.util.List;

/**
 * Created by wander on 2017/3/8.
 */
public class TreeVo {

    private String id;
    private String text;
    private Boolean leaf;

    private List<TreeVo> nodes;

    public TreeVo() {
    }

    public TreeVo(String id, String text, boolean leaf) {
        this.id = id;
        this.text = text;
        this.leaf = leaf;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public List<TreeVo> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeVo> nodes) {
        this.nodes = nodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
