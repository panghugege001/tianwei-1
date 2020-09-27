package com.nnti.pay.model.vo;

import java.io.Serializable;
import java.util.Date;


public class MerchantPay implements Serializable {

    private static final long serialVersionUID = 1521761251577382524L;

    //id
    private Long id;
    //商户号
    private String merchantCode;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //操作人
    private String updateBy;
    //商户名称
    private String payName;
    /*** 显示名字*/
    private String showName;
    /*** 别名 */
    private String asName;
    //支付类型 标识码
    private int type;
    //支付key值
    private String signKey;
    //回调url
    private String notifyUrl;
    //商城url
    private String shopUrl;
    //自身扣除费率 微信0.992 网银 支付宝0.991
    private Double fee;
    //支付开关 1.开 2.关
    private Integer paySwitch;
    /*** 是否禁用 1.启用 2禁用 默认1 */
    private Integer useable;
    /*** 支付金额限制 0.无限制 1.小于1000 2.大于1000 */
    private Integer amountCut;
    /*** 使用类型 1wab 2pc 3all */
    private Integer usetype;
    //支付名称简写
    private String payPlatform;
    //支付平台手机费率
    private Double phoneFee;
    //支付平台pc费率
    private Double pcFee;
    //最小支付金额
    private Double minPay;
    //最大支付金额
    private Double maxPay;
    //允许支付等级
    private String levels;
    /*** api接口 */
    private String apiUrl;
    /*** 系统代号*/
    private String systemCode;
    /*** 调用第三方app支付  0.否 1.是*/
    private Integer appPay;
    /*** 调用支付接口 */
    private String payUrl;
    /*** 支付方式，1.支付宝 2.微信 3.在线支付 4.快捷支付 5.点卡支付 13.银联*/
    private Integer payWay;
    /*** 支付中心接口*/
    private String payCenterUrl;
    /*** 接口留余属性值 */
    private String remain;

    /*** 备注 */
    private String remark;

    private Double amount;

    public MerchantPay() {
    }

    /*** 已用*/
    public MerchantPay(Integer paySwitch, Integer useable,Integer payWay) {
        this.paySwitch = paySwitch;
        this.useable = useable;
        this.payWay = payWay;
    }

    public MerchantPay(String platform) {
        this.payPlatform = platform;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getPaySwitch() {
        return paySwitch;
    }

    public void setPaySwitch(Integer paySwitch) {
        this.paySwitch = paySwitch;
    }

    public Integer getAmountCut() {
        return amountCut;
    }

    public void setAmountCut(Integer amountCut) {
        this.amountCut = amountCut;
    }

    public String getPayPlatform() {
        return payPlatform;
    }

    public void setPayPlatform(String payPlatform) {
        this.payPlatform = payPlatform;
    }

    public Double getPhoneFee() {
        return phoneFee;
    }

    public void setPhoneFee(Double phoneFee) {
        this.phoneFee = phoneFee;
    }

    public Double getPcFee() {
        return pcFee;
    }

    public void setPcFee(Double pcFee) {
        this.pcFee = pcFee;
    }

    public Double getMinPay() {
        return minPay;
    }

    public void setMinPay(Double minPay) {
        this.minPay = minPay;
    }

    public Double getMaxPay() {
        return maxPay;
    }

    public void setMaxPay(Double maxPay) {
        this.maxPay = maxPay;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Integer getAppPay() {
        return appPay;
    }

    public void setAppPay(Integer appPay) {
        this.appPay = appPay;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getPayCenterUrl() {
        return payCenterUrl;
    }

    public void setPayCenterUrl(String payCenterUrl) {
        this.payCenterUrl = payCenterUrl;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public Integer getUseable() {
        return useable;
    }

    public void setUseable(Integer useable) {
        this.useable = useable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }

    public Integer getUsetype() {
        return usetype;
    }

    public void setUsetype(Integer usetype) {
        this.usetype = usetype;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    /**
     * 记录日志有使用到
     */
    @Override
    public String toString() {
        return "{" +
                "merchantCode='" + merchantCode + '\'' +
                ", payName='" + payName + '\'' +
                ", showName='" + showName + '\'' +
                ", asName='" + asName + '\'' +
                ", 费率=" + fee +
                ", 是否开启=" + convertFlag(paySwitch) +
                ", 是否禁用=" + convertFlag(useable) +
                ", amountCut=" + amountCut +
                ", 手机费率=" + phoneFee +
                ", PC费率=" + pcFee +
                ", 使用类型 =" + usetype +
                ", 最小充值=" + minPay +
                ", 最大充值=" + maxPay +
                ", levels='" + levels + '\'' +
                ", 备注='" + remark + '\'' +
                '}';
    }

    private String convertFlag(Integer var) {
        return var == null ? "" : var == 1 ? "是" : "否";
    }
}