package dfh.action.vo;

import java.io.Serializable;

/**
 * Created by wander on 2017/2/1.
 */
public class PayWayVo implements Serializable {

    private static final long serialVersionUID = -1850555625951434688L;

    //id
    private Long id;
    //商户号
    private String merchantCode;
    //商户名称
    private String payName;
    //支付类型 标识码
    private int type;
    //自身扣除费率 微信0.992 网银 支付宝0.991
    private Double fee;
    //支付开关 1.开 2.关
    private Integer paySwitch;
    /*** 是否禁用 1.启用 2禁用 默认1 */
    private Integer useable;
    /*** 支付金额限制 0.无限制 1.小于1000 2.大于1000 */
    private Integer amountCut;
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
    /*** 调用第三方app支付  0.否 1.是*/
    private Integer appPay;
    /*** 支付方式，1.支付宝 2.微信 3.在线支付 4.快捷支付 5.点卡支付 */
    private Integer payWay;

    private String payCenterUrl;

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

    public Integer getAppPay() {
        return appPay;
    }

    public void setAppPay(Integer appPay) {
        this.appPay = appPay;
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

    public Integer getUseable() {
        return useable;
    }

    public void setUseable(Integer useable) {
        this.useable = useable;
    }
}
