package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/3/10.
 */
public class DbPayVo {

    /*** 商户 ID */
    private String parter;

    /*** 银行类型*/
    private String type;

    /*** 金额 单位元（人民币） ，2 位小数，最小支付金额为 1.00,微信支付宝至少 2 元 */
    private String value;
    /*** 商户系统订单号 */
    private String orderid;
    /*** 下行异步通知地址 */
    private String callbackurl;
    /*** 下行同步 通知地址*/
    private String hrefbackurl;

    /*** 获取二维码地址*/
    private String onlyqr;
    /*** 备注消息 */
    private String attach;
    /*** MD5 签名 32 位小写 MD5 签名值，GB2312 编码*/
    private String sign;

    /*** 订单结果 0：支付成功*/
    private String opstate;

    /*** 订单实际支付金额，单位元 */
    private String ovalue;

    /*** 第一次通知时的时间戳，年/月/日 时：分：秒，如 2010/04/05 21:50:58 */
    private String systime;

    /*** 多宝接口系统内的订单 Id*/
    private String sysorderid;

    /*** 多宝接口系统内的订单结束时间。格式为年/月/日 时：分：秒，如 2010/04/05 21:50:58 */
    private String completiontime;

    /*** 订单结果说明 */
    private String msg;

    /*** 页面属性，1.跳转，2抠图 */
    private String pageType;
    
    /*** pc****/
    private String pc;
    
    /*** web****/
    private String webH;
    
    


    public void setWxZfb(String merchantCode, String orderId, String amount, String attach, String notifyUrl, String shopUrl, String type) {
        this.parter = merchantCode;
        this.orderid = orderId;
        this.value = amount;
        this.attach = attach;
        this.callbackurl = notifyUrl;
        this.hrefbackurl = shopUrl;
        this.type = type;
    }

    public String getParter() {
        return parter;
    }

    public void setParter(String parter) {
        this.parter = parter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getHrefbackurl() {
        return hrefbackurl;
    }

    public void setHrefbackurl(String hrefbackurl) {
        this.hrefbackurl = hrefbackurl;
    }

    public String getOnlyqr() {
        return onlyqr;
    }

    public void setOnlyqr(String onlyqr) {
        this.onlyqr = onlyqr;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOpstate() {
        return opstate;
    }

    public void setOpstate(String opstate) {
        this.opstate = opstate;
    }

    public String getOvalue() {
        return ovalue;
    }

    public void setOvalue(String ovalue) {
        this.ovalue = ovalue;
    }

    public String getSystime() {
        return systime;
    }

    public void setSystime(String systime) {
        this.systime = systime;
    }

    public String getSysorderid() {
        return sysorderid;
    }

    public void setSysorderid(String sysorderid) {
        this.sysorderid = sysorderid;
    }

    public String getCompletiontime() {
        return completiontime;
    }

    public void setCompletiontime(String completiontime) {
        this.completiontime = completiontime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

	public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}

	public String getWebH() {
		return webH;
	}

	public void setWebH(String webH) {
		this.webH = webH;
	}
    
    
}
