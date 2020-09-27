package dfh.model.notdb;

import java.io.Serializable;

public class MGPlaycheckVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginname;
	private Double bet = 0.0;
	private Double refund = 0.0;
	private Double win = 0.0;
	private Double progressivewin = 0.0;
	private Double net = 0.0;
	
	public MGPlaycheckVo(){}
	
	public MGPlaycheckVo(String loginname, Double bet, Double refund, Double win, Double progressivewin, Double net){
		this.loginname = loginname;
		this.bet = bet;
		this.refund = refund;
		this.win = win;
		this.progressivewin = progressivewin;
		this.net = net;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getBet() {
		return bet;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	public Double getRefund() {
		return refund;
	}

	public void setRefund(Double refund) {
		this.refund = refund;
	}

	public Double getWin() {
		return win;
	}

	public void setWin(Double win) {
		this.win = win;
	}

	public Double getProgressivewin() {
		return progressivewin;
	}

	public void setProgressivewin(Double progressivewin) {
		this.progressivewin = progressivewin;
	}

	public Double getNet() {
		return net;
	}

	public void setNet(Double net) {
		this.net = net;
	}

}
