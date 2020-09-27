package com.gsmc.png.model.shaba;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SBA_DATA_QLE")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SBADataQLE extends SBAData4OracleVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public String trans_id;// 沙巴体育上的下注ID
	public String vendor_member_id;//供应商的成员编号
	public String operator_id;//操作符号
	public String match_datetime;//比赛开球时间
	public String stake;//投注额
	public String transaction_time;//下注的交易时间
	public String ticket_status;//票据状态（Half WON/Half LOSE/WON/LOSE/VOID/running/DRAW/Reject/Refund/Waiting）Half WON/Half LOSE/WON/LOSE/DRAW/Refund状态为已结算
	public String winlost_amount;//最终输赢金额
	public String after_amount;//结算后的余额
	public String currency;//货币种类id
	public String winlost_datetime;//最终下注输赢时间
	public String odds_type;//赔率类型
	public String version_key;//版本号
	
	@Id
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public String getVendor_member_id() {
		return vendor_member_id;
	}
	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}
	public String getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}
	public String getMatch_datetime() {
		return match_datetime;
	}
	public void setMatch_datetime(String match_datetime) {
		this.match_datetime = match_datetime;
	}
	public String getStake() {
		return stake;
	}
	public void setStake(String stake) {
		this.stake = stake;
	}
	public String getTransaction_time() {
		return transaction_time;
	}
	public void setTransaction_time(String transaction_time) {
		this.transaction_time = transaction_time;
	}
	public String getTicket_status() {
		return ticket_status;
	}
	public void setTicket_status(String ticket_status) {
		this.ticket_status = ticket_status;
	}
	public String getWinlost_amount() {
		return winlost_amount;
	}
	public void setWinlost_amount(String winlost_amount) {
		this.winlost_amount = winlost_amount;
	}
	public String getAfter_amount() {
		return after_amount;
	}
	public void setAfter_amount(String after_amount) {
		this.after_amount = after_amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getWinlost_datetime() {
		return winlost_datetime;
	}
	public void setWinlost_datetime(String winlost_datetime) {
		this.winlost_datetime = winlost_datetime;
	}
	public String getOdds_type() {
		return odds_type;
	}
	public void setOdds_type(String odds_type) {
		this.odds_type = odds_type;
	}
	public String getVersion_key() {
		return version_key;
	}
	public void setVersion_key(String version_key) {
		this.version_key = version_key;
	}

}