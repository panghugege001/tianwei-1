package dfh.model.notdb;

import java.sql.Timestamp;

public class FirstlyCashin {
	private Timestamp createTime;
	private String loginname;
	private Double money;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "FirstlyCashin [createTime=" + createTime + ", loginname=" + loginname + ", money=" + money + "]";
	}
}
