package dfh.model.pay;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by wander on 2017/2/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayWayVo implements Serializable {

    private static final long serialVersionUID = -1850555625951434688L;

    //id
    private Long id;
    /*** 显示名称 */
    private String showName;
    //自身扣除费率
    private Double fee;
    //支付平台手机费率
    private Double phoneFee;
    //支付平台pc费率
    private Double pcFee;
    //最小支付金额
    private Double minPay;
    //最大支付金额
    private Double maxPay;
    /*** 调用第三方app支付  0.否 1.是*/
    private Integer appPay;
    /*** 支付方式，1.支付宝 2.微信 3.在线支付 4.快捷支付 5.点卡支付 */
    private Integer payWay;

    private String payCenterUrl;
    
    /*** 简称 */
    private String payPlatform;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
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

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
	}
    
    
}
