package app.model.vo;

/**
 * 将请求的json转bean
 * @author lin
 *
 */
public  class OnlinePayRecordVO extends PageParamterVO {
//查询条件字段1
//查询条件字段2
//查询条件字段3
	
	
//好友推荐记录查询条件  0:推荐注册成功玩家  1:推荐奖金收入  2:推荐奖金支出
private String friendtype;

public String getFriendtype() {
	return friendtype;
}

public void setFriendtype(String friendtype) {
	this.friendtype = friendtype;
}	
}
