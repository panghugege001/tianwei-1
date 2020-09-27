package dfh.model.notdb;

import dfh.utils.Constants;
import dfh.utils.StringUtil;

public class ReturnBean {

	private Integer success=0;
	private String errormsg;
	private Object obj;
	private String msg1;
//	private Object obj1;
//	private Object obj2;
	
	
	public ReturnBean() {}
	
	public ReturnBean(String msg,Object obj) {
		
		if (StringUtil.isNotEmpty(msg)) {
			this.success=Constants.FLAG_FALSE;
			this.errormsg=msg;
		}else {
			this.obj=obj;
		}
	
	}
	
  public ReturnBean(String msg,Integer success,String msg1) {
			this.errormsg=msg;
			this.success=success;
			this.msg1=msg1;
	}
	
     public ReturnBean(String msg) {
		
		if (StringUtil.isNotEmpty(msg)) {
			this.success=Constants.FLAG_FALSE;
			this.errormsg=msg;
		}else
			this.errormsg=msg;
	}

	

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}


	public String getMsg1() {
		return msg1;
	}

	public void setMsg1(String msg1) {
		this.msg1 = msg1;
	}

//	public Object getObj1() {
//		return obj1;
//	}
//
//	public void setObj1(Object obj1) {
//		this.obj1 = obj1;
//	}
//
//	public Object getObj2() {
//		return obj2;
//	}
//
//	public void setObj2(Object obj2) {
//		this.obj2 = obj2;
//	}

	
	
}
