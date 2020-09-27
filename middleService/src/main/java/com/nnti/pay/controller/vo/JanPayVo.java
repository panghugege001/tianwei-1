package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/4/13.
 */
public class JanPayVo {

    private String sign;
    private String amount;
    private String assetCode;
    private String merchantAddress;
    private String merchantCallbackurl;
    private String merchantId;
    private String merchantOrderid;
    private String bindUserid;
    private String bindName;
    private String bindPhone;
    private String bindAreacode;

    private String jiuanOrderid;
    private String rate;
    private String bindUserLevel;

    private String publicKey;

    public void setCoin(String amount, String assetCode, String merchantAddress, String merchantCallbackurl, String merchantId, String merchantOrderid,
                        String bindUserid, String bindName, String bindPhone, String bindAreacode, String bindUserLevel) {
        this.amount = amount;
        this.assetCode = assetCode;
        this.merchantAddress = merchantAddress;
        this.merchantCallbackurl = merchantCallbackurl;
        this.merchantId = merchantId;
        this.merchantOrderid = merchantOrderid;
        this.bindUserid = bindUserid;
        this.bindName = bindName;
        this.bindPhone = bindPhone;
        this.bindAreacode = bindAreacode;
        this.bindUserLevel = bindUserLevel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantCallbackurl() {
        return merchantCallbackurl;
    }

    public void setMerchantCallbackurl(String merchantCallbackurl) {
        this.merchantCallbackurl = merchantCallbackurl;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantOrderid() {
        return merchantOrderid;
    }

    public void setMerchantOrderid(String merchantOrderid) {
        this.merchantOrderid = merchantOrderid;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getBindUserid() {
        return bindUserid;
    }

    public void setBindUserid(String bindUserid) {
        this.bindUserid = bindUserid;
    }

    public String getBindName() {
        return bindName;
    }

    public void setBindName(String bindName) {
        this.bindName = bindName;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getBindAreacode() {
        return bindAreacode;
    }

    public void setBindAreacode(String bindAreacode) {
        this.bindAreacode = bindAreacode;
    }

    public String getJiuanOrderid() {
        return jiuanOrderid;
    }

    public void setJiuanOrderid(String jiuanOrderid) {
        this.jiuanOrderid = jiuanOrderid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBindUserLevel() {
        return bindUserLevel;
    }

    public void setBindUserLevel(String bindUserLevel) {
        this.bindUserLevel = bindUserLevel;
    }
}
