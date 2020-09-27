package dfh.model;

import dfh.utils.StringUtil;

/**
 * 载体
 */
public class FanyaLogVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	//订单编号
	private String OrderID;
	//用户名
	private String UserName;
	//游戏类型
	private String Category;
	//联赛类型
	private String League;
	//赛事名称
	private String Match;	
	//所参与的投注
	private String Bet;
	//投注内容
	private String Content;	
	//投注金额
	private String BetAmount;
	//状态
	private String Status;	
	//开奖结果
	private String Result;
	//盈亏
	private String Money;
	//状态最新更改的时间
	private String UpdateAt;
	//投注时间
	private String CreateAt;
	//比赛开始时间
	private String StartAt;
	//比赛结束时间
	private String EndAt;
	//开奖时间
	private String ResultAt;
	//派彩時間（結算時間）
	private String RewardAt;
	private String BetMoney;

	
	
	public String getBetMoney() {
		return BetMoney;
	}

	public void setBetMoney(String BetMoney) {
		this.BetMoney = BetMoney;
	}
	
	public String getResultAt() {
		return ResultAt;
	}
	public void setResultAt(String resultAt) {
		if(StringUtil.isNotBlank(resultAt)){
			String [] b = resultAt.split("-");
			String year = b[1];
			String month = b[1];
			String day = b[2];
			if(month.length()<2){
				month = "0"+ month;
			}
			if(day.length()<11){
				day = "0"+ day;
			}	
			ResultAt = year+"-"+month+"-"+day;
		}else{
			ResultAt = resultAt;
		}
		
		
	}
	public String getRewardAt() {
		return RewardAt;
	}
	public void setRewardAt(String rewardAt) {
		if(StringUtil.isNotBlank(rewardAt)){
			String [] b = rewardAt.split("-");
			String year = b[1];
			String month = b[1];
			String day = b[2];
			if(month.length()<2){
				month = "0"+ month;
			}
			if(day.length()<11){
				day = "0"+ day;
			}	
			RewardAt = year+"-"+month+"-"+day;
		} else {
			RewardAt = rewardAt;
		}
	}


	
	public String getOrderID() {
		return OrderID;
	}
	public void setOrderId(String orderID) {
		OrderID = orderID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getLeague() {
		return League;
	}
	public void setLeague(String league) {
		League = league;
	}
	public String getMatch() {
		return Match;
	}
	public void setMatch(String match) {
		Match = match;
	}
	public String getBet() {
		return Bet;
	}
	public void setBet(String bet) {
		Bet = bet;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getBetAmount() {
		return BetAmount;
	}
	public void setBetAmount(String betAmount) {
		BetAmount = betAmount;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public String getMoney() {
		return Money;
	}
	public void setMoney(String money) {
		Money = money;
	}
	public String getUpdateAt() {
		return UpdateAt;
	}
	public void setUpdateAt(String updateAt) {
		String [] b = updateAt.split("-");
		String year = b[1];
		String month = b[1];
		String day = b[2];
		if(month.length()<2){
			month = "0"+ month;
		}
		if(day.length()<11){
			day = "0"+ day;
		}	
		UpdateAt = year+"-"+month+"-"+day;
	}
	public String getCreateAt() {
		return CreateAt;
	}
	public void setCreateAt(String createAt) {
		String [] b = createAt.split("-");
		String year = b[1];
		String month = b[1];
		String day = b[2];
		if(month.length()<2){
			month = "0"+ month;
		}
		if(day.length()<11){
			day = "0"+ day;
		}	
		CreateAt = year+"-"+month+"-"+day;
	}
	public String getStartAt() {
		return StartAt;
	}
	public void setStartAt(String startAt) {
		String [] b = startAt.split("-");
		String year = b[1];
		String month = b[1];
		String day = b[2];
		if(month.length()<2){
			month = "0"+ month;
		}
		if(day.length()<11){
			day = "0"+ day;
		}	
		StartAt = year+"-"+month+"-"+day;
	}
	public String getEndAt() {
		return EndAt;
	}
	public void setEndAt(String endAt) {
		String [] b = endAt.split("-");
		String year = b[1];
		String month = b[1];
		String day = b[2];
		if(month.length()<2){
			month = "0"+ month;
		}
		if(day.length()<11){
			day = "0"+ day;
		}	
		EndAt = year+"-"+month+"-"+day;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}