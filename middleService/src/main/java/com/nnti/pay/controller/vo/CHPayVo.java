package com.nnti.pay.controller.vo;

//畅汇支付
public class CHPayVo {

    private String p0_Cmd;//固定值 buy
    private String p1_MerId;
    private String p2_Order;
    private String p3_Cur;// 固定值“CNY”
    private String p4_Amt; //单位:元，精确到分.
    private String p5_Pid;
    private String p6_Pcat;
    private String p7_Pdesc;
    private String p8_Url; 
    private String p9_MP; 
    private String pa_FrpId; 
    private String pg_BankCode; //网银支付时必传
    private String ph_Ip; 
    private String pi_Url; 
    private String hmac; 
    /*** pc****/
    private String pc;
    /*** wap****/
    private String wap;
    
    //回调
    private String r0_Cmd;
    private String r1_Code;//固定值 “1”, 代表支付成功，9999:失败
    private String r2_TrxId;
    private String r3_Amt;
    private String r4_Cur;
    private String r5_Pid;
    private String r6_Order;
    private String r8_MP;
    private String r9_BType;
    private String ro_BankOrderId;
    private String rp_PayDate;
    
    public void setOnline_pay(String p0_Cmd,String p1_MerId, String p2_Order, String p3_Cur,String p4_Amt, String p5_Pid,String p6_Pcat,String p7_Pdesc,
    		String p8_Url,String p9_MP,String pa_FrpId,String pg_BankCode,String ph_Ip,String pi_Url) {
    	this.p0_Cmd = p0_Cmd;
    	this.p1_MerId = p1_MerId;
    	this.p2_Order = p2_Order;
    	this.p3_Cur = p3_Cur;
    	this.p4_Amt = p4_Amt;
    	this.p5_Pid = p5_Pid;
    	this.p6_Pcat = p6_Pcat;
    	this.p7_Pdesc = p7_Pdesc;
    	this.p8_Url = p8_Url;
    	this.p9_MP = p9_MP;
    	this.pa_FrpId = pa_FrpId;
    	this.pg_BankCode = pg_BankCode;
    	this.ph_Ip = ph_Ip;
    	this.pi_Url = pi_Url;
	}

	public String getP0_Cmd() {
		return p0_Cmd;
	}

	public void setP0_Cmd(String p0_Cmd) {
		this.p0_Cmd = p0_Cmd;
	}

	public String getP1_MerId() {
		return p1_MerId;
	}

	public void setP1_MerId(String p1_MerId) {
		this.p1_MerId = p1_MerId;
	}

	public String getP2_Order() {
		return p2_Order;
	}

	public void setP2_Order(String p2_Order) {
		this.p2_Order = p2_Order;
	}

	public String getP3_Cur() {
		return p3_Cur;
	}

	public void setP3_Cur(String p3_Cur) {
		this.p3_Cur = p3_Cur;
	}

	public String getP4_Amt() {
		return p4_Amt;
	}

	public void setP4_Amt(String p4_Amt) {
		this.p4_Amt = p4_Amt;
	}

	public String getP5_Pid() {
		return p5_Pid;
	}

	public void setP5_Pid(String p5_Pid) {
		this.p5_Pid = p5_Pid;
	}

	public String getP6_Pcat() {
		return p6_Pcat;
	}

	public void setP6_Pcat(String p6_Pcat) {
		this.p6_Pcat = p6_Pcat;
	}

	public String getP7_Pdesc() {
		return p7_Pdesc;
	}

	public void setP7_Pdesc(String p7_Pdesc) {
		this.p7_Pdesc = p7_Pdesc;
	}

	public String getP8_Url() {
		return p8_Url;
	}

	public void setP8_Url(String p8_Url) {
		this.p8_Url = p8_Url;
	}

	public String getP9_MP() {
		return p9_MP;
	}

	public void setP9_MP(String p9_MP) {
		this.p9_MP = p9_MP;
	}

	public String getPa_FrpId() {
		return pa_FrpId;
	}

	public void setPa_FrpId(String pa_FrpId) {
		this.pa_FrpId = pa_FrpId;
	}

	public String getPg_BankCode() {
		return pg_BankCode;
	}

	public void setPg_BankCode(String pg_BankCode) {
		this.pg_BankCode = pg_BankCode;
	}

	public String getPh_Ip() {
		return ph_Ip;
	}

	public void setPh_Ip(String ph_Ip) {
		this.ph_Ip = ph_Ip;
	}

	public String getPi_Url() {
		return pi_Url;
	}

	public void setPi_Url(String pi_Url) {
		this.pi_Url = pi_Url;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
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

	public String getR0_Cmd() {
		return r0_Cmd;
	}

	public void setR0_Cmd(String r0_Cmd) {
		this.r0_Cmd = r0_Cmd;
	}

	public String getR1_Code() {
		return r1_Code;
	}

	public void setR1_Code(String r1_Code) {
		this.r1_Code = r1_Code;
	}

	public String getR2_TrxId() {
		return r2_TrxId;
	}

	public void setR2_TrxId(String r2_TrxId) {
		this.r2_TrxId = r2_TrxId;
	}

	public String getR3_Amt() {
		return r3_Amt;
	}

	public void setR3_Amt(String r3_Amt) {
		this.r3_Amt = r3_Amt;
	}

	public String getR4_Cur() {
		return r4_Cur;
	}

	public void setR4_Cur(String r4_Cur) {
		this.r4_Cur = r4_Cur;
	}

	public String getR5_Pid() {
		return r5_Pid;
	}

	public void setR5_Pid(String r5_Pid) {
		this.r5_Pid = r5_Pid;
	}

	public String getR6_Order() {
		return r6_Order;
	}

	public void setR6_Order(String r6_Order) {
		this.r6_Order = r6_Order;
	}

	public String getR8_MP() {
		return r8_MP;
	}

	public void setR8_MP(String r8_MP) {
		this.r8_MP = r8_MP;
	}

	public String getR9_BType() {
		return r9_BType;
	}

	public void setR9_BType(String r9_BType) {
		this.r9_BType = r9_BType;
	}

	public String getRo_BankOrderId() {
		return ro_BankOrderId;
	}

	public void setRo_BankOrderId(String ro_BankOrderId) {
		this.ro_BankOrderId = ro_BankOrderId;
	}

	public String getRp_PayDate() {
		return rp_PayDate;
	}

	public void setRp_PayDate(String rp_PayDate) {
		this.rp_PayDate = rp_PayDate;
	}
    
}
