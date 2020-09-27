package dfh.action.vo;

import java.io.Serializable;
import java.util.List;

public class FirstCashinUsresVoOfTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8728177003130470049L;
	
	private int personCount;
	private double firstAmount;
	private double concessionsAmount;
	private List userList;
	
	
	public List getUserList() {
		return userList;
	}
	public void setUserList(List userList) {
		this.userList = userList;
	}
	public int getPersonCount() {
		return personCount;
	}
	public void setPersonCount(int personCount) {
		this.personCount += personCount;
	}
	public double getFirstAmount() {
		return firstAmount;
	}
	public void setFirstAmount(double firstAmount) {
		this.firstAmount += firstAmount;
	}
	public double getConcessionsAmount() {
		return concessionsAmount;
	}
	public void setConcessionsAmount(double concessionsAmount) {
		this.concessionsAmount += concessionsAmount;
	}
	
	

}
