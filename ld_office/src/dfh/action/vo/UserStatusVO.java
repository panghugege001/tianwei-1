package dfh.action.vo;

import java.sql.Timestamp;

public class UserStatusVO {

	private String loginname;
	
	private Integer mailflag;
	
	private String phone;
	
	private String email;
	
	private Timestamp createtime;
	
	private Integer touzhuflag;
	
	private String remark; 
	
	public UserStatusVO(){
		
	}

	public UserStatusVO(String loginname, Integer mailflag, String phone,
			String email, Timestamp createtime,String remark) {
		super();
		this.loginname = loginname;
		this.mailflag = mailflag;
		this.phone = phone;
		this.email = email;
		this.createtime = createtime;
		this.remark = remark;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Integer getMailflag() {
		return mailflag;
	}

	public void setMailflag(Integer mailflag) {
		this.mailflag = mailflag;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Integer getTouzhuflag() {
		return touzhuflag;
	}

	public void setTouzhuflag(Integer touzhuflag) {
		this.touzhuflag = touzhuflag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
