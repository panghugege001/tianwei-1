package app.model.po;

import app.model.po.UserSidName;

public class UserSidNameKey implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2357280029285102011L;

	private String loginname;
	
	private String sid;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	
    @Override  
    public boolean equals(Object o) {  
        if(o instanceof UserSidNameKey){  
        	UserSidName key = (UserSidName)o ;  
            if(this.sid == key.getSid() && this.loginname.equals(key.getLoginname())){  
                return true ;  
            }  
        }  
        return false ;  
    }  
      
    @Override  
    public int hashCode() {  
        return this.loginname.hashCode();  
    }  
}
