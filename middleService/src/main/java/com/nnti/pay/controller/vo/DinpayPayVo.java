package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/2/1.
 */
public class DinpayPayVo {

    private String url;
    private String apiUrl;
    /*** 签名（RSA-S）*/
    private String sign;  
    /*** 商户号(必填)*/
    private String merchant_code;
    /*** 支付银行(必填) */
    private String bank_code;
    /*** 商家定单号(必填)*/
    private String order_no;
    /*** 定单金额（必填）*/
    private String order_amount;
    /*** 业务类型(必填)*/
    private String service_type;
    private String pay_type;
    /*** 参数编码字符集(必选)*/
    private String input_charset;
    /*** 后台通知地址(必填)*/
    private String notify_url;
    /*** 接口版本(必选)固定值:V3.0*/
    private String interface_version;
    private String sign_type;
    /*** 商家定单时间(必填)*/
    private String order_time;
    /*** 商品名称（必填）*/
    private String product_name;
    private String client_ip;
    /*** 公用业务扩展参数（选填）*/
    private String extend_param;
    /*** 公用业务回传参数（选填）*/
    private String extra_return_param;
    /*** 是否允许重复订单（选填）*/
    private String redo_flag;
    /*** 商品编号(选填)*/
    private String product_code;
    /*** 商品描述（选填）*/
    private String product_desc;
    /*** 商品数量(选填)*/
    private String product_num;
    /*** 页面跳转同步通知地址(选填)*/
    private String return_url;
    /*** 商品展示地址(选填)*/
    private String show_url;
    /*** 银行交易流水号*/
    private String bank_seq_no;
    /*** 交易状态 SUCCESS 成功 FAILED 失败*/
    private String trade_status;
    /*** 智付交易时间*/
    private String trade_time;
    /*** 智付交易定单号*/
    private String trade_no;
    /*** 通知校验ID*/
    private String notify_id;
    /*** 通知类型*/
    private String notify_type;
    /*** 回调验签公钥 */
    private String zfPublicKey;
    /*** 点卡 类型 */
    private String card_code;
    /*** 点卡充值金额 */
    private String card_amount;
    /*** */
    private String encrypt_info;
    /*** 支付时间*/
    private String pay_date;
    /*** 回调点卡卡号*/
    private String card_no;
    /*** 真实充值金额 */
    private String card_actual_amount;
    /*** 公钥*/
    private String RSA_S_PublicKey;
    /*** 私钥*/
    private String RSA_S_PrivateKey;
    /*** 回调公钥 */
    private String zhifu_PublicKey;

    public DinpayPayVo() {
    }

    public void setZfbWx(String apiUrl, String loginName, String merchantCode, String orderId, String orderAmount, String notifyUrl, String order_time, String product_name, String product_code, String product_desc, String product_num,String customerIp) {
        this.apiUrl = apiUrl;
        this.extra_return_param = loginName;
        this.merchant_code = merchantCode;
        this.notify_url = notifyUrl;
        this.order_no = orderId;
        this.order_amount = orderAmount;
        this.order_time = order_time;
        this.product_name = product_name;
        this.product_code = product_code;
        this.product_desc = product_desc;
        this.product_num = product_num;
        this.client_ip = customerIp;
    }

    /*** 在线支付接口属性值*/
    public void setOnline(String url, String merchantCode, String bankCode, String orderId, String orderAmount, String notifyUrl, String order_time, String productName, String client_ip, String extend_param, String loginName, String product_code, String product_desc, String product_num, String shopUrl, String show_url) {
        this.url = url;
        this.apiUrl = url;
        this.merchant_code = merchantCode;
        this.bank_code = bankCode;
        this.order_no = orderId;
        this.order_amount = orderAmount;
        this.notify_url = notifyUrl;
        this.product_name = productName;
        this.order_time = order_time;
        this.client_ip = client_ip;
        this.extend_param = extend_param;
        this.extra_return_param = loginName;
        this.product_code = product_code;
        this.product_desc = product_desc;
        this.product_num = product_num;
        this.return_url = shopUrl;
        this.show_url = show_url;
    }

    public void setPointCard(String order_no, String orderAmount, String order_time, String card_code, String merchantCode, String notifyUrl, String encrypt_info, String client_ip) {
        this.order_no = order_no;
        this.card_amount = orderAmount;
        this.order_time = order_time;
        this.card_code = card_code;
        this.merchant_code = merchantCode;
        this.notify_url = notifyUrl;
        this.encrypt_info = encrypt_info;
        this.client_ip = client_ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getInput_charset() {
        return input_charset;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getInterface_version() {
        return interface_version;
    }

    public void setInterface_version(String interface_version) {
        this.interface_version = interface_version;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }

    public String getExtra_return_param() {
        return extra_return_param;
    }

    public void setExtra_return_param(String extra_return_param) {
        this.extra_return_param = extra_return_param;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getShow_url() {
        return show_url;
    }

    public void setShow_url(String show_url) {
        this.show_url = show_url;
    }

    public String getBank_seq_no() {
        return bank_seq_no;
    }

    public void setBank_seq_no(String bank_seq_no) {
        this.bank_seq_no = bank_seq_no;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getZfPublicKey() {
        return zfPublicKey;
    }

    public void setZfPublicKey(String zfPublicKey) {
        this.zfPublicKey = zfPublicKey;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public String getCard_amount() {
        return card_amount;
    }

    public void setCard_amount(String card_amount) {
        this.card_amount = card_amount;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_actual_amount() {
        return card_actual_amount;
    }

    public void setCard_actual_amount(String card_actual_amount) {
        this.card_actual_amount = card_actual_amount;
    }

    public String getEncrypt_info() {
        return encrypt_info;
    }

    public void setEncrypt_info(String encrypt_info) {
        this.encrypt_info = encrypt_info;
    }

    public String getRSA_S_PublicKey() {
        return RSA_S_PublicKey;
    }

    public void setRSA_S_PublicKey(String RSA_S_PublicKey) {
        this.RSA_S_PublicKey = RSA_S_PublicKey;
    }

    public String getRSA_S_PrivateKey() {
        return RSA_S_PrivateKey;
    }

    public void setRSA_S_PrivateKey(String RSA_S_PrivateKey) {
        this.RSA_S_PrivateKey = RSA_S_PrivateKey;
    }

    public String getZhifu_PublicKey() {
        return zhifu_PublicKey;
    }

    public void setZhifu_PublicKey(String zhifu_PublicKey) {
        this.zhifu_PublicKey = zhifu_PublicKey;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

	public String getRedo_flag() {
		return redo_flag;
	}

	public void setRedo_flag(String redo_flag) {
		this.redo_flag = redo_flag;
	}
    
}
