package dfh.action.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import dfh.utils.AESUtil;

public class TelvisitVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -877158963970388168L;
	private int id,taskid,islock,execstatus,intervaltime;
	private String loginname,locker,aliasname,accountname,email,phone,intro,qq;
	private Date createtime,lastlogintime;
	
	
	// methods:
	
	public TelvisitVO(int id, int taskid, int islock, int execstatus,
			int intervaltime, String loginname, String locker,
			String aliasname, String accountname, String email, String phone,
			Date createtime, Date lastlogintime,String intro,String qq) {
		super();
		this.id = id;
		this.taskid = taskid;
		this.islock = islock;
		this.execstatus = execstatus;
		this.intervaltime = intervaltime;
		this.loginname = loginname;
		this.locker = locker;
		this.aliasname = aliasname;
		this.accountname = accountname;
		this.email = email;
		this.phone = phone;
		this.createtime = createtime;
		this.lastlogintime = lastlogintime;
		this.intro = intro;
		this.qq = qq;
	}
	
	
	// getter and setter:
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public int getIslock() {
		return islock;
	}
	public void setIslock(int islock) {
		this.islock = islock;
	}
	public int getExecstatus() {
		return execstatus;
	}
	public void setExecstatus(int execstatus) {
		this.execstatus = execstatus;
	}
	public int getIntervaltime() {
		return intervaltime;
	}
	public void setIntervaltime(int intervaltime) {
		this.intervaltime = intervaltime;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLocker() {
		return locker;
	}
	public void setLocker(String locker) {
		this.locker = locker;
	}
	public String getAliasname() {
		return aliasname;
	}
	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getEmail() {
		if(StringUtils.isNotBlank(email) && !email.contains("@")){
			try {
				return AESUtil.aesDecrypt(email, AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	        return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		if(StringUtils.isNotBlank(phone) && !dfh.utils.StringUtil.isNumeric(phone)){
			try {
				return AESUtil.aesDecrypt(phone, AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getLastlogintime() {
		return lastlogintime;
	}
	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}


	public String getIntro() {
		return intro;
	}


	public void setIntro(String intro) {
		this.intro = intro;
	}


	public String getQq() {
		return qq;
	}


	public void setQq(String qq) {
		this.qq = qq;
	}
	

}
