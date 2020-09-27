package com.nnti.pay.controller.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wander on 2017/3/11.
 */
public class ReturnVo implements Serializable {

    private static final long serialVersionUID = 7399806313178377132L;

    /*** 1 页面跳转post方式 2.二维码code 3.图片链接 4.成功页面7微信附言8页面跳转get方式 */
    private String type;
    private String url;
    private String data;
    private Map params = new HashMap();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

	public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
    
}
