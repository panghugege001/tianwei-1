package com.nnti.pay.model;

import java.util.Date;

/**
 *  支付商户参数
 * @author addis
 *
 */
public class Zfparm {
	
	/**
	 * 用户来源平台 千亿:qy,龙八:yl,乐虎:e68,亚虎:777,优发:ufa,优乐:ule,梦之城:mzc,齐乐:qle,武松:ws
	 */
	private String source;
	
	/**
	 * 商户号 
	 */
	private String merNo;
	
	/**
	 * 玩家名称
	 * @return
	 */
	private String loginname;
	
	/**
	 * 支付金额
	 * @return
	 */
	private String amount;
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getMerNo() {
		return merNo;
	}
	
	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}
	
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
