package com.nnti.pay.controller.vo;

import java.io.Serializable;

public class WMPayVo implements Serializable {

	private static final long serialVersionUID = 3484505667279585305L;
	public String pay_memberid;
	public String pay_orderid;
	public String pay_applydate;
	public String pay_bankcode;
	public String pay_notifyurl;
	public String pay_callbackurl;
	public String pay_amount;
	public String pay_md5sign;
	public String pay_attach;
	public String pay_productname;
	public String pay_productnum;
	public String pay_productdesc;
	public String pay_producturl;

	/*** pc ****/
	private String pc;

	/*** wap ****/
	private String wap;
	
	public String memberid;
	public String orderid;
	public String amount;
	public String transaction_id;
	public String datetime;
	public String returncode;
	public String attach;
	public String sign;

	public void setOnline_pay(String pay_memberid, String pay_orderid, String pay_applydate, String pay_bankcode,
			String pay_notifyurl, String pay_callbackurl, String pay_amount, String pay_md5sign,String pay_attach,String pay_productname,String pay_productnum,
			String pay_productdesc,String pay_producturl) {
		this.pay_memberid = pay_memberid;
		this.pay_orderid = pay_orderid;
		this.pay_applydate = pay_applydate;
		this.pay_bankcode = pay_bankcode;
		this.pay_notifyurl = pay_notifyurl;
		this.pay_callbackurl = pay_callbackurl;
		this.pay_amount = pay_amount;
		this.pay_md5sign = pay_md5sign;
		this.pay_attach = pay_attach;
		this.pay_productname = pay_productname;
		this.pay_productnum = pay_productnum;
		this.pay_productdesc = pay_productdesc;
		this.pay_producturl = pay_producturl;
	}

	public String getPay_memberid() {
		return pay_memberid;
	}

	public void setPay_memberid(String pay_memberid) {
		this.pay_memberid = pay_memberid;
	}

	public String getPay_orderid() {
		return pay_orderid;
	}

	public void setPay_orderid(String pay_orderid) {
		this.pay_orderid = pay_orderid;
	}

	public String getPay_applydate() {
		return pay_applydate;
	}

	public void setPay_applydate(String pay_applydate) {
		this.pay_applydate = pay_applydate;
	}

	public String getPay_bankcode() {
		return pay_bankcode;
	}

	public void setPay_bankcode(String pay_bankcode) {
		this.pay_bankcode = pay_bankcode;
	}

	public String getPay_notifyurl() {
		return pay_notifyurl;
	}

	public void setPay_notifyurl(String pay_notifyurl) {
		this.pay_notifyurl = pay_notifyurl;
	}

	public String getPay_callbackurl() {
		return pay_callbackurl;
	}

	public void setPay_callbackurl(String pay_callbackurl) {
		this.pay_callbackurl = pay_callbackurl;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getPay_md5sign() {
		return pay_md5sign;
	}

	public void setPay_md5sign(String pay_md5sign) {
		this.pay_md5sign = pay_md5sign;
	}

	public String getPay_attach() {
		return pay_attach;
	}

	public void setPay_attach(String pay_attach) {
		this.pay_attach = pay_attach;
	}

	public String getPay_productname() {
		return pay_productname;
	}

	public void setPay_productname(String pay_productname) {
		this.pay_productname = pay_productname;
	}

	public String getPay_productnum() {
		return pay_productnum;
	}

	public void setPay_productnum(String pay_productnum) {
		this.pay_productnum = pay_productnum;
	}

	public String getPay_productdesc() {
		return pay_productdesc;
	}

	public void setPay_productdesc(String pay_productdesc) {
		this.pay_productdesc = pay_productdesc;
	}

	public String getPay_producturl() {
		return pay_producturl;
	}

	public void setPay_producturl(String pay_producturl) {
		this.pay_producturl = pay_producturl;
	}

	public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}

	public String getWap() {
		return wap;
	}

	public void setWap(String wap) {
		this.wap = wap;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
