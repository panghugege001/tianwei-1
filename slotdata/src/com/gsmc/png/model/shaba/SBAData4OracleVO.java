package com.gsmc.png.model.shaba;


public class SBAData4OracleVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 *  		"trans_id": 102076884460, 
                "vendor_member_id": "q_devtest999", 
                "operator_id": "qy", 
                "league_id": 49661, 
                "match_id": 22329674, 
                "home_id": 176948, 
                "away_id": 254952, 
                "match_datetime": "2017-05-16T14:59:00", 
                "sport_type": 1, 
                "bet_type": 3, 
                "parlay_ref_no": 0, 
                "odds": 0.91, 
                "stake": 3, 
                "transaction_time": "2017-05-16T04:23:21.277", 
                "ticket_status": "LOSE", 
                "winlost_amount": -3, 
                "after_amount": 9988, 
                "currency": 20, 
                "winlost_datetime": "2017-05-16T00:00:00", 
                "odds_type": 2, 
                "bet_team": "h", 
                "isLucky": "False", 
                "home_hdp": 3.5, 
                "away_hdp": 0, 
                "hdp": 3.5, 
                "betfrom": "d", 
                "islive": "0", 
                "home_score": null, 
                "away_score": null, 
                "customInfo1": "", 
                "customInfo2": "", 
                "customInfo3": "", 
                "customInfo4": "", 
                "customInfo5": "", 
                "ba_status": "0", 
                "version_key": 2125, 
                "ParlayData": null
	 * 
	 */
	public String trans_id;// 沙巴体育上的下注ID
	public String vendor_member_id;//供应商的成员编号
	public String operator_id;//操作符号
	//public String league_id;//联赛ID
	//public String match_id;//匹配ID
	//public String home_id;//主队ID
	//public String away_id;//客队ID
	public String match_datetime;//比赛开球时间
	//public String sport_type;//运动型
	//public String bet_type;//投注类型（参考BetType表）
	//public String parlay_ref_no;//对于parlay票证，请使用此选项来查看parlay_info中的parlay ID
	//public String odds;//赔率
	public String stake;//投注额
	public String transaction_time;//下注的交易时间
	public String ticket_status;//票据状态（Half WON/Half LOSE/WON/LOSE/VOID/running/DRAW/Reject/Refund/Waiting）Half WON/Half LOSE/WON/LOSE/DRAW/Refund状态为已结算
	public String winlost_amount;//最终输赢金额
	public String after_amount;//结算后的余额
	public String currency;//货币种类id
	public String winlost_datetime;//最终下注输赢时间
	public String odds_type;//赔率类型
	//public String bet_team;//客户投注的队伍
	//public String exculding;//排除排除其他得分的列表
	//public String home_hdp;//主队让球
	//public String away_hdp;//客队让球
	//public String hdp;//盘口
	//public String betfrom;//赌注来自的平台
	//public String islive;//是指票是在滾球的時候下注還是deadball
	//public String home_score;//主队的最后得分
	//public String away_score;//客队的最后得分
	//public String customInfo1;//注册期间客户的自定义信息
	//public String customInfo2;//注册期间客户的自定义信息
	//public String customInfo3;//注册期间客户的自定义信息
	//public String customInfo4;//注册期间客户的自定义信息
	//public String customInfo5;//注册期间客户的自定义信息
	//public String ba_status;//投注聚合器的标志，表示这个会员的输赢由另一个公司来承担
	public String version_key;//版本号
	//public String ParlayData;// Parlay数据
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