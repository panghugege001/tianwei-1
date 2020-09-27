package dfh.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 红包雨
 *
 */
@Entity
@Table(name = "red_rain_wallet", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class RedRainWallet implements Serializable {
	private String loginname;
	private Double amout;
	private String remark;

	@Id
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "amout")
	public Double getAmout() {
		return amout;
	}

	public void setAmout(Double amout) {
		this.amout = amout;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
