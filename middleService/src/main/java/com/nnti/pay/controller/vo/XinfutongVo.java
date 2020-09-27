package com.nnti.pay.controller.vo;

/**
 * Created by Addis on 2017/5/16.
 */
public class XinfutongVo {

	//商品的描述
    private String body;
    
    //参数编码字符集
    private String charset;
    
    //网银代码
    private String defaultbank;
    
    //app方式
    private String isApp;
    
    //商户号
    private String merchantId;
    
    //异步通知地址
    private String notifyUrl;
    
    //商户订单号
    private String orderNo;
    
    //支付类型
    private String paymentType;
    
    //支付方式
    private String paymethod;
    
    //同步通知地址
    private String returnUrl;
    
    //固定值
    private String service;
    
    //商品名称
    private String title;
    
    //订单金额 单位：元
    private String totalFee;
    
    //签名方式
    private String signType;
    
    //签名值
    private String sign;
    
    
    /**** 回调通知参数 ********/
    
    //买家email
    private String buyer_email;
    
    //买家ID
    private String buyer_id;
    
    //折扣
    private String discount;
    
    //扩展字段
    private String ext_param1;
    
    //扩展字段2
    private String ext_param2;
    
    //交易创建时间
    private String gmt_create;
    
    //订单更改时间
    private String gmt_logistics_modify;
    
    //交易付款时间
    private String gmt_payment;
    
    //通讯状态
    private String is_success;
    
    //总费用
    private String is_total_fee_adjust;
    
    //通知ID
    private String  notify_id;
    
    //通知时间
    private String notify_time;
    
    //通知类型
    private String notify_type;
    
    //外部交易号
    private String order_no;
    
    //支付类型
    private String payment_type;
    
    //单价
    private String price;
    
    //数量
    private String quantity;
    
    //卖家
    private String seller_actions;
    
    //卖家邮箱
    private String seller_email;
    
    //卖家email
    private String  seller_id;
    
    //交易金额
    private String total_fee;
    
    //我司交易号
    private String trade_no;
    
    //交易状态
    private String trade_status;
    
    //优惠券
    private String use_coupon;
    
    

    public void setOnline(String body, String defaultbank, String isApp, String merchantId, String notifyUrl, String orderNo, String returnUrl, String title, String totalFee) {
          this.body = body;
          this.defaultbank = defaultbank;
          this.isApp  = isApp;
          this.merchantId = merchantId;
          this.notifyUrl = notifyUrl;
          this.orderNo = orderNo;
          this.returnUrl = returnUrl;
          this.title = title;
          this.totalFee = totalFee;
    }

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getDefaultbank() {
		return defaultbank;
	}

	public void setDefaultbank(String defaultbank) {
		this.defaultbank = defaultbank;
	}

	public String getIsApp() {
		return isApp;
	}

	public void setIsApp(String isApp) {
		this.isApp = isApp;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getExt_param1() {
		return ext_param1;
	}

	public void setExt_param1(String ext_param1) {
		this.ext_param1 = ext_param1;
	}

	public String getExt_param2() {
		return ext_param2;
	}

	public void setExt_param2(String ext_param2) {
		this.ext_param2 = ext_param2;
	}

	public String getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	public String getGmt_logistics_modify() {
		return gmt_logistics_modify;
	}

	public void setGmt_logistics_modify(String gmt_logistics_modify) {
		this.gmt_logistics_modify = gmt_logistics_modify;
	}

	public String getGmt_payment() {
		return gmt_payment;
	}

	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	public String getIs_total_fee_adjust() {
		return is_total_fee_adjust;
	}

	public void setIs_total_fee_adjust(String is_total_fee_adjust) {
		this.is_total_fee_adjust = is_total_fee_adjust;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getNotify_time() {
		return notify_time;
	}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}

	public String getNotify_type() {
		return notify_type;
	}

	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getSeller_actions() {
		return seller_actions;
	}

	public void setSeller_actions(String seller_actions) {
		this.seller_actions = seller_actions;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getUse_coupon() {
		return use_coupon;
	}

	public void setUse_coupon(String use_coupon) {
		this.use_coupon = use_coupon;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}
    
}

