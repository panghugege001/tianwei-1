package dfh.service.interfaces;

import dfh.model.Users;

public interface IMemberSignrecord {

	public void login(String username)throws Exception;
	
	public void logout(String username)throws Exception;
	
	public boolean isLogined(String username)throws Exception;
	
	public void update(Users user);
	
	public void updateUserCreditSqlVip(Users user);
	
	/**
	 * 用户在线支付订单数量
	 * @param user
	 * @return
	 */
	public Integer getPayOrderCountByUser(Users user);

	/**
	 * 秒存订单数量
	 * @param user
	 * @return
	 */
	public Integer getDepositProposalCountByUser(Users user);
}
