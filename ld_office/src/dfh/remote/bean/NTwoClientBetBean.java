package dfh.remote.bean;

import java.math.BigDecimal;

import org.dom4j.Element;

public class NTwoClientBetBean {
	
	private String loginName;
	private BigDecimal betAmount;
	private BigDecimal payoutAmount;
	private BigDecimal handle;
	private BigDecimal hold;
	
	public NTwoClientBetBean() {
		
	}
	
	public NTwoClientBetBean(String loginName, BigDecimal betAmount, BigDecimal payoutAmount, BigDecimal handle,
			BigDecimal hold) {
		super();
		this.loginName = loginName;
		this.betAmount = betAmount;
		this.payoutAmount = payoutAmount;
		this.handle = handle;
		this.hold = hold;
	}

	public NTwoClientBetBean(Element element) {
		super();
		this.loginName = element.attributeValue("login");
		this.betAmount = new BigDecimal(element.attributeValue("bet_amount"));
		this.payoutAmount = new BigDecimal(element.attributeValue("payout_amount"));
		this.handle = new BigDecimal(element.attributeValue("handle"));
		this.hold = new BigDecimal(element.attributeValue("hold"));
	}
	
	public void calculateBeanValue(Element element) {
		setBetAmount(getBetAmount().add(new BigDecimal(element.attributeValue("bet_amount"))));
		setPayoutAmount(getPayoutAmount().add(new BigDecimal(element.attributeValue("payout_amount"))));
		setHandle(getHandle().add(new BigDecimal(element.attributeValue("handle"))));
		setHold(getHold().add(new BigDecimal(element.attributeValue("hold"))));
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public BigDecimal getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}
	public BigDecimal getPayoutAmount() {
		return payoutAmount;
	}
	public void setPayoutAmount(BigDecimal payoutAmount) {
		this.payoutAmount = payoutAmount;
	}
	public BigDecimal getHandle() {
		return handle;
	}
	public void setHandle(BigDecimal handle) {
		this.handle = handle;
	}
	public BigDecimal getHold() {
		return hold;
	}
	public void setHold(BigDecimal hold) {
		this.hold = hold;
	}
	
	@Override
	public String toString() {
		return "NTwoClientBetBean [loginName=" + loginName + ", betAmount=" + betAmount + ", payoutAmount="
				+ payoutAmount + ", handle=" + handle + ", hold=" + hold + "]";
	}
}
