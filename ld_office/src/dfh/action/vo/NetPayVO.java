package dfh.action.vo;

import java.io.Serializable;

public class NetPayVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1035293939624652397L;
	private String username;
	private String password;
	private String responseCode;			// 响应编码
	private String inner_serialNo;			// 内部订单编号
	private String payPlatform_serialNo;	// 第三方支付平台流水号
	private String payPlatform_Name;		// 第三方支付平台名称
	private String bankOrderNo;				// 银行单号
	private String amount;					// 存款、退款金额
	private String payDate;					// 支付成功时间
	private String informDate;				// 第三方平台通知本平台时间
	private String thirdpartyID;			// 第三方平台会员ID
	private String payType;					// 支付类型；如存款、退款；
	private String payResult;				// 支付结果
	private String mac;
	private String bankName;				// 银行编号
	
	
	public NetPayVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public NetPayVO(String username, String innerSerialNo,
			String payPlatformSerialNo, String payPlatformName,
			String bankOrderNo, String amount, String payDate, String informDate,
			String thirdpartyID, String payType, String payResult,String mac) {
		super();
		this.username = username;
		inner_serialNo = innerSerialNo;
		payPlatform_serialNo = payPlatformSerialNo;
		payPlatform_Name = payPlatformName;
		this.bankOrderNo = bankOrderNo;
		this.amount = amount;
		this.payDate = payDate;
		this.informDate = informDate;
		this.thirdpartyID = thirdpartyID;
		this.payType = payType;
		this.payResult = payResult;
		this.mac=mac;
	}


	public NetPayVO(String username,String password,String mac) {
		// TODO Auto-generated constructor stub
		super();
		this.username=username;
		this.password=password;
		this.mac=mac;
	}
	

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getInner_serialNo() {
		return inner_serialNo;
	}
	public void setInner_serialNo(String innerSerialNo) {
		inner_serialNo = innerSerialNo;
	}
	public String getPayPlatform_serialNo() {
		return payPlatform_serialNo;
	}
	public void setPayPlatform_serialNo(String payPlatformSerialNo) {
		payPlatform_serialNo = payPlatformSerialNo;
	}
	public String getPayPlatform_Name() {
		return payPlatform_Name;
	}
	public void setPayPlatform_Name(String payPlatformName) {
		payPlatform_Name = payPlatformName;
	}
	public String getBankOrderNo() {
		return bankOrderNo;
	}
	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getInformDate() {
		return informDate;
	}
	public void setInformDate(String informDate) {
		this.informDate = informDate;
	}
	public String getThirdpartyID() {
		return thirdpartyID;
	}
	public void setThirdpartyID(String thirdpartyID) {
		this.thirdpartyID = thirdpartyID;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayResult() {
		return payResult;
	}
	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}
	

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	

}
