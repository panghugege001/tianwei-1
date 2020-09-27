package com.nnti.common.model.vo;

import java.io.Serializable;

/**
 * Created by wander on 2017/2/7.
 */
public class Dictionary implements Serializable {

    private static final long serialVersionUID = -7986516055165267863L;

    private Long id;
    private String dictType;// '字典大项',
    private String dictName;// '字典项名称',
    private String dictValue;// '字典项值',
    private String dictShow;// '显示项',
    private String dictDesc;// '字典项描述',
    private Integer orderBy;// '排序',
    private Integer useable;// '是否可用 0默认为不可用，1可用'

    public Dictionary() {
    }

    public Dictionary(String dictType) {
        this.dictType = dictType;
    }

    public Dictionary(String dictType, Integer useable) {
        this.dictType = dictType;
        this.useable = useable;
    }

    public Dictionary(String dictType, String dictName, Integer useable) {
        this.dictType = dictType;
        this.dictName = dictName;
        this.useable = useable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictDesc() {
        return dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getUseable() {
        return useable;
    }

    public void setUseable(Integer useable) {
        this.useable = useable;
    }

    public String getDictShow() {
        return dictShow;
    }

    public void setDictShow(String dictShow) {
        this.dictShow = dictShow;
    }
}
