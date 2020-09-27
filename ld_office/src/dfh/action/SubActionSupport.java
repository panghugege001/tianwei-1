package dfh.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.nnti.office.model.auth.Operator;
import com.opensymphony.xwork2.ActionSupport;

import dfh.model.Users;
import dfh.utils.Constants;

public class SubActionSupport extends ActionSupport {

	public String getCustomerLoginname() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user != null)
			return user.getLoginname();
		else
			return null;
	}

	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}

	public String getIp() {
		String forwaredFor = getRequest().getHeader("X-Forwarded-For");
		String remoteAddr = getRequest().getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor)) {
			String[] ipArray = forwaredFor.split(",");
			return ipArray[0];
		} else
			return remoteAddr;
	}

	public String getOperatorLoginname() {
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		String operatename =  getHttpSession().getAttribute(Constants.SESSION_OPERATORNAME) == null ? null : getHttpSession().getAttribute(Constants.SESSION_OPERATORNAME).toString();
		if (operator != null)
			return operator.getUsername();
		else if(StringUtils.isNotEmpty(operatename)){
			return operatename ;
		}else
			return null;
	}

	public String getPartnerLoginname() {
		Users partner = (Users) getHttpSession().getAttribute(Constants.SESSION_PARTNERID);
		if (partner != null)
			return partner.getLoginname();
		else
			return null;
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public Users getCustomerFromSession() {
		return (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
	}
	
	public void writeText(String text){
        try {
        	getResponse().setContentType("text/plain;charset=UTF-8");
        	getResponse().setHeader("Charset", "UTF-8");
        	getResponse().setHeader("Cache-Control", "no-cache");
			PrintWriter out = getResponse().getWriter();
			out.write(text);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void writeObjectJson(Object obj){
        try {
        	getResponse().setContentType("text/plain;charset=UTF-8");
        	getResponse().setHeader("Charset", "UTF-8");
        	getResponse().setHeader("Cache-Control", "no-cache");
			PrintWriter out = getResponse().getWriter();
			out.write(JSONObject.fromObject(obj).toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
