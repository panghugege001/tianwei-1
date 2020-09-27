package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * 多多支付微信和支付宝支付提交网关参数
 */
public class DdzfPayVo implements Serializable {

    private static final long serialVersionUID = 7324634869954607545L;
    /*** 商户号*/
    private String MerId;
    /*** 订单号*/
    private String OrdId;
    /*** 金额 单位：元*/
    private String OrdAmt;
    /*** 类型*/
    private String PayType;
    /*** 支付币种*/
    private String CurCode;
    /*** 银行编码*/
    private String BankCode;
    /*** 物品信息*/
    private String ProductInfo;
    /*** 备注信息*/
    private String Remark;
    /*** 前端页面返回地址*/
    private String ReturnURL;
    /*** 后台异步通知*/
    private String NotifyURL;
    /*** 签名方式*/
    private String SignType;
    /*** 签名数据*/
    private String SignInfo;
    
    

    /*******************以下回调参数*******************/
    /*** 支付平台订单号 */
    private String OrdNo;

    /*** 交易状态 */
    private String ResultCode;



    public DdzfPayVo() {
    }


    /*** 设置支付宝或微信接口属性值*/
    public void setWxZfb(String MerId, String OrdId, String OrdAmt, 
    		String ProductInfo,String Remark,String ReturnURL,String NotifyURL) {
         this.MerId = MerId;
         this.OrdId = OrdId;
         this.OrdAmt = OrdAmt;
         this.ProductInfo = ProductInfo;
         this.Remark = Remark;
         this.ReturnURL = ReturnURL;
         this.NotifyURL = NotifyURL;
    }
    


	public String getMerId() {
		return MerId;
	}


	public void setMerId(String merId) {
		MerId = merId;
	}


	public String getOrdId() {
		return OrdId;
	}


	public void setOrdId(String ordId) {
		OrdId = ordId;
	}


	public String getOrdAmt() {
		return OrdAmt;
	}


	public void setOrdAmt(String ordAmt) {
		OrdAmt = ordAmt;
	}


	public String getPayType() {
		return PayType;
	}


	public void setPayType(String payType) {
		PayType = payType;
	}


	public String getCurCode() {
		return CurCode;
	}


	public void setCurCode(String curCode) {
		CurCode = curCode;
	}


	public String getBankCode() {
		return BankCode;
	}


	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}


	public String getProductInfo() {
		return ProductInfo;
	}


	public void setProductInfo(String productInfo) {
		ProductInfo = productInfo;
	}


	public String getRemark() {
		return Remark;
	}


	public void setRemark(String remark) {
		Remark = remark;
	}


	public String getReturnURL() {
		return ReturnURL;
	}


	public void setReturnURL(String returnURL) {
		ReturnURL = returnURL;
	}


	public String getNotifyURL() {
		return NotifyURL;
	}


	public void setNotifyURL(String notifyURL) {
		NotifyURL = notifyURL;
	}


	public String getSignType() {
		return SignType;
	}


	public void setSignType(String signType) {
		SignType = signType;
	}


	public String getSignInfo() {
		return SignInfo;
	}


	public void setSignInfo(String signInfo) {
		SignInfo = signInfo;
	}


	public String getOrdNo() {
		return OrdNo;
	}


	public void setOrdNo(String ordNo) {
		OrdNo = ordNo;
	}


	public String getResultCode() {
		return ResultCode;
	}


	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
    


    
   
    
    
}
