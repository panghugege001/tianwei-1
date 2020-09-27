package dfh.action.vo;

import java.io.Serializable;

public class QueryDataEuroVO implements Serializable {
	private static final long serialVersionUID = -2592252906084278627L;
	
	
	public QueryDataEuroVO() {
		// TODO Auto-generated constructor stub
	}

	public QueryDataEuroVO(String loginname,String createtime) {
		this.loginname=loginname;
		this.createtime=createtime;
	}
	
	
	
	private String loginname;
	private String createtime;


	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
	
}
