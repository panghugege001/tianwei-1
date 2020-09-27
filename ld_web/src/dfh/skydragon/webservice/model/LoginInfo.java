package dfh.skydragon.webservice.model;

import java.io.Serializable;

import dfh.model.Users;

public class LoginInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3132726349008147359L;
	private Users user;
	//登录信息
	private String msg;
	//返回视图
	private String view;
	//登录成功标识
	private Integer sucFlag;
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public Integer getSucFlag() {
		return sucFlag;
	}
	public void setSucFlag(Integer sucFlag) {
		this.sucFlag = sucFlag;
	}
}
