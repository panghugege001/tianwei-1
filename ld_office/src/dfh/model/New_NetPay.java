package dfh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "new_netpay", catalog = "tianwei")
public class New_NetPay implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6429878567442011149L;
	private int id;
	private String userID;
	private String inner_serialNo;			// 内部订单编号
	private String payPlatform_serialNo;	// 第三方支付平台流水号
	private String payPlatform_Name;		// 第三方支付平台名称
	private String bankOrderNo;				// 银行单号
	private double amount;					// 存款、退款金额
	private Date payDate;				// 支付成功时间
	private Date informDate;			// 第三方平台通知本平台时间
	private String thirdpartyID;			// 第三方平台会员ID
	private int payType;					// 支付类型；如存款、退款；
	private int payResult;					// 支付结果
	private String bankName;				// 银行编号
	
	
	public New_NetPay() {
		// TODO Auto-generated constructor stub
	}
	
	public New_NetPay(String userID, String innerSerialNo,
			String payPlatformSerialNo, String payPlatformName,
			String bankOrderNo, double amount, Date payDate,
			Date informDate, String thirdpartyID, int payType,
			int payResult,String bankName) {
		super();
		this.userID = userID;
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
		this.bankName=bankName;
	}
	
	
	@Id
	@javax.persistence.Column(name = "id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@javax.persistence.Column(name = "userID")
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	@javax.persistence.Column(name = "inner_serialNo")
	public String getInner_serialNo() {
		return inner_serialNo;
	}
	public void setInner_serialNo(String innerSerialNo) {
		inner_serialNo = innerSerialNo;
	}
	
	@javax.persistence.Column(name = "payPlatform_serialNo")
	public String getPayPlatform_serialNo() {
		return payPlatform_serialNo;
	}
	public void setPayPlatform_serialNo(String payPlatformSerialNo) {
		payPlatform_serialNo = payPlatformSerialNo;
	}
	
	@javax.persistence.Column(name = "payPlatform_Name")
	public String getPayPlatform_Name() {
		return payPlatform_Name;
	}
	public void setPayPlatform_Name(String payPlatformName) {
		payPlatform_Name = payPlatformName;
	}
	
	@javax.persistence.Column(name = "bankOrderNo")
	public String getBankOrderNo() {
		return bankOrderNo;
	}
	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	
	@javax.persistence.Column(name = "amount")
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@javax.persistence.Column(name = "payDate")
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	@javax.persistence.Column(name = "informDate")
	public Date getInformDate() {
		return informDate;
	}
	public void setInformDate(Date informDate) {
		this.informDate = informDate;
	}
	
	@javax.persistence.Column(name = "thirdpartyID")
	public String getThirdpartyID() {
		return thirdpartyID;
	}
	public void setThirdpartyID(String thirdpartyID) {
		this.thirdpartyID = thirdpartyID;
	}
	
	@javax.persistence.Column(name = "payType")
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	
	@javax.persistence.Column(name = "payResult")
	public int getPayResult() {
		return payResult;
	}
	public void setPayResult(int payResult) {
		this.payResult = payResult;
	}
	
	@javax.persistence.Column(name = "bankName")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	

}
