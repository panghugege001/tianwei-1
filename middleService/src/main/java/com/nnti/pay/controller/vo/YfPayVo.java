package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/2/10.
 */
public class YfPayVo {

    private String BANK_CODE;
    private String CUSTOMER_IP;
    private String CUSTOMER_PHONE;
    private String INPUT_CHARSET;
    private String MER_NO;
    private String NOTIFY_URL;
    private String ORDER_AMOUNT;
    private String ORDER_NO;
    private String PRODUCT_NAME;
    private String PRODUCT_NUM;
    private String RECEIVE_ADDRESS;
    private String REFERER;
    private String RETURN_PARAMS;
    private String RETURN_URL;
    private String VERSION;
    private String KEY;
    private String SIGN;
    private String apiUrl;

    /**************** 回调 ******/
    /*** 商户号*/
    private String mer_no;
    /*** 订单金额,小数点后3位*/
    private String order_amount;
    /*** 优付订单号*/
    private String order_no;
    /*** 商户订单号*/
    private String trade_no;
    /*** 返回参数*/
    private String trade_params;
    /*** 订单状态,success才成功,其余状态不返回*/
    private String trade_status;
    /*** 订单日期.时间戳*/
    private String trade_time;
    /*** 服务器返回的签名*/
    private String sign;

    public void setWxZfb(String bank_code, String customer_ip, String customer_phone, String input_charset, String mer_no, String notifyUrl, String orderAmount, String orderId, String systemCode, String product_num, String receive_address, String referer, String loginName, String shopUrl, String version, String signKey) {
        this.BANK_CODE = bank_code;
        this.CUSTOMER_IP = customer_ip;
        this.CUSTOMER_PHONE = customer_phone;
        this.INPUT_CHARSET = input_charset;
        this.MER_NO = mer_no;
        this.NOTIFY_URL = notifyUrl;
        this.ORDER_AMOUNT = orderAmount;
        this.ORDER_NO = orderId;
        this.PRODUCT_NAME = systemCode;
        this.PRODUCT_NUM = product_num;
        this.RECEIVE_ADDRESS = receive_address;
        this.REFERER = referer;
        this.RETURN_PARAMS = loginName;
        this.RETURN_URL = shopUrl;
        this.VERSION = version;
        this.KEY = signKey;
    }


    public String getBANK_CODE() {
        return BANK_CODE;
    }

    public void setBANK_CODE(String BANK_CODE) {
        this.BANK_CODE = BANK_CODE;
    }

    public String getCUSTOMER_IP() {
        return CUSTOMER_IP;
    }

    public void setCUSTOMER_IP(String CUSTOMER_IP) {
        this.CUSTOMER_IP = CUSTOMER_IP;
    }

    public String getCUSTOMER_PHONE() {
        return CUSTOMER_PHONE;
    }

    public void setCUSTOMER_PHONE(String CUSTOMER_PHONE) {
        this.CUSTOMER_PHONE = CUSTOMER_PHONE;
    }

    public String getINPUT_CHARSET() {
        return INPUT_CHARSET;
    }

    public void setINPUT_CHARSET(String INPUT_CHARSET) {
        this.INPUT_CHARSET = INPUT_CHARSET;
    }

    public String getMER_NO() {
        return MER_NO;
    }

    public void setMER_NO(String MER_NO) {
        this.MER_NO = MER_NO;
    }

    public String getNOTIFY_URL() {
        return NOTIFY_URL;
    }

    public void setNOTIFY_URL(String NOTIFY_URL) {
        this.NOTIFY_URL = NOTIFY_URL;
    }

    public String getORDER_AMOUNT() {
        return ORDER_AMOUNT;
    }

    public void setORDER_AMOUNT(String ORDER_AMOUNT) {
        this.ORDER_AMOUNT = ORDER_AMOUNT;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getPRODUCT_NUM() {
        return PRODUCT_NUM;
    }

    public void setPRODUCT_NUM(String PRODUCT_NUM) {
        this.PRODUCT_NUM = PRODUCT_NUM;
    }

    public String getRECEIVE_ADDRESS() {
        return RECEIVE_ADDRESS;
    }

    public void setRECEIVE_ADDRESS(String RECEIVE_ADDRESS) {
        this.RECEIVE_ADDRESS = RECEIVE_ADDRESS;
    }

    public String getREFERER() {
        return REFERER;
    }

    public void setREFERER(String REFERER) {
        this.REFERER = REFERER;
    }

    public String getRETURN_PARAMS() {
        return RETURN_PARAMS;
    }

    public void setRETURN_PARAMS(String RETURN_PARAMS) {
        this.RETURN_PARAMS = RETURN_PARAMS;
    }

    public String getRETURN_URL() {
        return RETURN_URL;
    }

    public void setRETURN_URL(String RETURN_URL) {
        this.RETURN_URL = RETURN_URL;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }

    public String getMer_no() {
        return mer_no;
    }

    public void setMer_no(String mer_no) {
        this.mer_no = mer_no;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_params() {
        return trade_params;
    }

    public void setTrade_params(String trade_params) {
        this.trade_params = trade_params;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
