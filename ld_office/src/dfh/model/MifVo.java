package dfh.model;

/**
 * Created by Addis on 2017/10/12.
 */
public class MifVo {

    /*** 卡号*/
    private String cardNumber;

    /*** 余额 */
    private String balance;
    
    /*** 时间 */
    private String lastUpdateBalanceTime;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLastUpdateBalanceTime() {
		return lastUpdateBalanceTime;
	}

	public void setLastUpdateBalanceTime(String lastUpdateBalanceTime) {
		this.lastUpdateBalanceTime = lastUpdateBalanceTime;
	}

    
    
}
