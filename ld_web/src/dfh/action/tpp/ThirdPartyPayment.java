package dfh.action.tpp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AxisUtil;

public abstract class ThirdPartyPayment implements Cloneable{
	protected String id;
	protected String name;
	protected String url;
	protected String message;
	protected String qrcode;
	protected boolean work = false;
	protected Map<Enum,Object> formData;
	
	ThirdPartyPayment(){
		
	}
	
	public boolean isValid(Users user,String bankId,double money){
		clear();
		//代理不能使用在线支付
		if(!user.getRole().equals("MONEY_CUSTOMER")){
			message = "[提示]代理不能使用在线支付！";
			return false;
		}
		// 判断在线支付是否存在
		try {
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
					new Object[] { id}, Const.class);
			if (constPay == null||"0".equals(constPay.getValue())) {
				message = "目前该支付平台维护中！";
				work = false;
				return false;
			}else{
				work = true;
			}
		
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return verification(user, bankId, money);
	}
	
	protected void clear(){
		message = null;
		formData = null;
	}
	
	@Override
	protected ThirdPartyPayment clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (ThirdPartyPayment)super.clone();
	}

	protected abstract boolean verification(Users user,String bankId,double money);
	

	protected String getIp(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
		
		return ip;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isWork() {
		return work;
	}

	public void setWork(boolean work) {
		this.work = work;
	}
	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

}
