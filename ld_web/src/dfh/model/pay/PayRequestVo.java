package dfh.model.pay;

import java.io.Serializable;

/**
 * Created by wander on 2017/1/25.
 * 官网请求支付中心参数
 */
public class PayRequestVo implements Serializable{

    private static final long serialVersionUID = -1727790835804499143L;

    /*** 用户登陆名*/
    private String loginName;
    /*** 支付平台ID */
    private Long platformId;
    /*** 使用平台 1.pc 2.web */
    private Integer usetype;
    /*** 充值金额*/
    private String orderAmount;
    /*** 银行编码*/
    private String bankCode;
    /*** 客户端IP*/
    private String customerIp;
    /*** 域名 */
    private String domain;
    /*** 点卡类型*/
    private String cardCode;
    /*** 点卡号码*/
    private String cardNo;
    /*** 点卡密码*/
    private String cardPassword;
	/*** 银行卡号 */
	private String bankcard;
	/*** 银行卡户名 */
	private String bankname;
	/*** 手机号码 */
	private String phoneNumber;

    private String payUrl;

    
    public void setParam(Long platformId, String loginName, String orderAmount, Integer usetype,String bankCode, String cardNo, String cardCode, String cardPassword,String payUrl,String bankcard,String bankname,String phoneNumber) {
        this.platformId = platformId;
        this.loginName = loginName;
        this.orderAmount = orderAmount;
        this.usetype = usetype;
        this.bankCode = bankCode;
        this.cardNo = cardNo;
        this.cardCode = cardCode;
        this.cardPassword = cardPassword;
        this.payUrl = payUrl;
        this.bankcard = bankcard;
        this.bankname = bankname;
        this.phoneNumber = phoneNumber;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardPassword() {
        return cardPassword;
    }

    public void setCardPassword(String cardPassword) {
        this.cardPassword = cardPassword;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    @Override
    public String toString() {
        return "PayRequestVo{" +
                "loginName='" + loginName + '\'' +
                ", platformId=" + platformId +
                ", orderAmount='" + orderAmount + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", customerIp='" + customerIp + '\'' +
                ", cardCode='" + cardCode + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardPassword='" + cardPassword + '\'' +
                ", payUrl='" + payUrl + '\'' +
                '}';
    }

    public Integer getUsetype() {
        return usetype;
    }

    public void setUsetype(Integer usetype) {
        this.usetype = usetype;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
    
    

}
