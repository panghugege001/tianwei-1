package app.model.vo;

import org.apache.commons.lang3.StringUtils;

public class AgentCenterVO {

	// 其它佣金余额
	private String credit;
	// 老虎机佣金余额
	private String slotaccount;
	// 本月总输赢
	private String profitall;
	// 本月总返水
	private String ximafee;
	// 本月总优惠
	private String couponfee;
	// 会员总人数
	private String reg;
	// 本月注册量
	private String monthReg;
	// 本月投注额
	private String betall;
	
	public String getCredit() {
		return credit;
	}
	
	public void setCredit(String credit) {
		this.credit = credit;
	}
	
	public String getSlotaccount() {
		return slotaccount;
	}
	
	public void setSlotaccount(String slotaccount) {
		this.slotaccount = slotaccount;
	}

	public String getProfitall() {
		return profitall;
	}

	public void setProfitall(String profitall) {
		this.profitall = profitall;
	}

	public String getXimafee() {
		return ximafee;
	}

	public void setXimafee(String ximafee) {
		this.ximafee = ximafee;
	}

	public String getCouponfee() {
		return couponfee;
	}

	public void setCouponfee(String couponfee) {
		this.couponfee = couponfee;
	}

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		
		if (StringUtils.isBlank(reg) || "null".equals(reg)) {
		
			reg = "0";
		}
		
		this.reg = reg;
	}

	public String getMonthReg() {
		return monthReg;
	}

	public void setMonthReg(String monthReg) {
		
		if (StringUtils.isBlank(monthReg) || "null".equals(monthReg)) {
			
			monthReg = "0";
		}
		
		this.monthReg = monthReg;
	}

	public String getBetall() {
		return betall;
	}

	public void setBetall(String betall) {
		this.betall = betall;
	}
}