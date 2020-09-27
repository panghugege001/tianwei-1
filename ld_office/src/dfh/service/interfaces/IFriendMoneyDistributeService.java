package dfh.service.interfaces;


public interface IFriendMoneyDistributeService {
	
	public String calculateFriendMoneyByDate(String distributeDate)throws Exception;

	public String calculateFlower()throws Exception;

	public String calculateRankingAndCoupon(String proposer) throws Exception;

	public String updateBettotal(String loginname, Double newbet);

/*	public void updateSingleParty(Date agStart, Date agEnd, Date ptStart,
			Date ptEnd, String rankdate);*/


}
