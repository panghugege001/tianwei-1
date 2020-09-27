package com.nnti.pay.controller.vo;

//直付支付
public class ZPPayVo {
	private String key;//	String(20)	Y	支付平台分配的应用ID
	private String method;//	String(20)	Y	固定值：easypay.trade.pay
	private String trade_no;//	String(20)	Y	商户订单号，确保应用内唯一
	private String title;//	String(40)	Y	商品名称
	private String memo;//	String(40)	N	订单备注
	private String money;//	String(10)	Y	订单金额，单位为分
	private String platform;//	String(10)	Y	支付渠道，取值如下：alipay 支付宝 wxpay 微信支付(暂不支持)
	private String mobile;//	String(1)	Y	移动端标记，取值如下：Y 移动端 N PC端
	private String timestamp;//	String(20)	Y	发送请求的时间，格式：yyyyMMddHHmmss
	private String notify;//	String(128)	Y	异步通知地址，用于接收支付结果回调相关规则详见1.2 异步回调通知机制
	private String redirect;//	String(128)	Y	支付返回地址，支付成功时跳转到此地址相关规则详见1.2 异步回调通知机制
	private String sign;//	String(32)	Y	数据签名,详见1.1签名算法

	private String sn;//	String(20)	Y	支付平台流水号
	private String sysid;//	String(40)	N	支付宝交易号
	private String finish;//	String(20)	Y	支付完成时间,UNIX时间戳
	private String time;//	String(20)	Y	返回数据的时间，格式：yyyyMMddHHmmss
	
	public void setOnline_pay(String key,String method,String money,String notify,String redirect,String timestamp,
			String title,String trade_no,String mobile,String memo){
		this.key = key;
		this.method = method;
		this.money = money;
		this.notify = notify;
		this.redirect = redirect;
		this.timestamp = timestamp;
		this.title = title;
		this.trade_no = trade_no;
		this.mobile = mobile;
		this.memo = memo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
