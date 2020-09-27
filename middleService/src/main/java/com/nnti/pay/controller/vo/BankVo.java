package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * Created by wander on 2017/3/11.
 */
public class BankVo implements Serializable {

    private static final long serialVersionUID = 2011358628268863289L;

    private String dictName;
    private String dictValue;
    private String dictShow;// '显示项',
    private String dictDesc;// '字典项描述',

    public BankVo() {
    }

    public BankVo(String dictName, String dictValue) {
        this.dictName = dictName;
        this.dictValue = dictValue;
    }

    public BankVo(String dictName, String dictValue, String dictDesc, String dictShow) {
        this.dictName = dictName;
        this.dictValue = dictValue;
        this.dictShow = dictShow;
        this.dictDesc = dictDesc;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictShow() {
        return dictShow;
    }

    public void setDictShow(String dictShow) {
        this.dictShow = dictShow;
    }

    public String getDictDesc() {
        return dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }
}
