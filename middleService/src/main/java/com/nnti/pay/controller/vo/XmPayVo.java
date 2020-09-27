package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * Created by addis on 2017/7/26.
 */
public class XmPayVo implements Serializable {

	private static final long serialVersionUID = 3484505667279585305L;

	private String messageid;
	private String out_trade_no;
	private String branch_id;
	private String pay_type;
	private String total_fee;
	private String prod_name;
	private String prod_desc;
	private String back_notify_url;
	private String nonce_str;
	private String attach_content;
	private String sign;
	private String nonceStr;

	/****** 回调参数 **/
	private String resultCode;
	private String resultDesc;
	private String resCode;
	private String resDesc;
	private String branchId;
	private String createTime;
	private String orderAmt;
	private String orderNo;
	private String outTradeNo;
	private String productDesc;
	private String productName;
	private String attachContent;
	private String status;
	private String payType;

	public void setZfbWx(String branch_id, String out_trade_no, String total_fee, String back_notify_url,
			String prod_name, String prod_desc, String attach_content,String nonce_str) {
		this.branch_id = branch_id;
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.back_notify_url = back_notify_url;
		this.prod_name = prod_name;
		this.prod_desc = prod_desc;
		this.attach_content = attach_content;
		this.nonce_str = nonce_str;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public String getProd_desc() {
		return prod_desc;
	}

	public void setProd_desc(String prod_desc) {
		this.prod_desc = prod_desc;
	}

	public String getBack_notify_url() {
		return back_notify_url;
	}

	public void setBack_notify_url(String back_notify_url) {
		this.back_notify_url = back_notify_url;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getAttach_content() {
		return attach_content;
	}

	public void setAttach_content(String attach_content) {
		this.attach_content = attach_content;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAttachContent() {
		return attachContent;
	}

	public void setAttachContent(String attachContent) {
		this.attachContent = attachContent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Override
	public String toString() {
	    return "[XmPayVo] resultCode = " + this.resultCode + ", resultDesc = " + this.resultDesc + ", status = " + this.status + ", outTradeNo = " + this.outTradeNo + ", branchId = " + this.branchId;
	}
	

}
