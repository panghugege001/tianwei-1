package dfh.action.office;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import dfh.service.interfaces.IAutoDeposit;
import dfh.utils.Configuration;
import java.util.Arrays;

public class AutoDepositAction extends ActionSupport implements SessionAware,
		ServletRequestAware, ServletResponseAware {
	
	private Map<String,Object> session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String reqxml;
	private IAutoDeposit autoDeposit;
	private Logger log=Logger.getLogger(AutoDepositAction.class);
	
	
	
	public String login(){
		if(validation()){
			// 合法用户
			if (reqxml!=null&&!reqxml.trim().equals("")) {
				String msg=autoDeposit.Login(reqxml,request.getRemoteAddr());
				this.write(msg);
			}
		}
		return null;
	}
	
	public String callBack(){
		if (validation()) {
			if (reqxml!=null&&!reqxml.trim().equals("")) {
				String msg = autoDeposit.CallBack(reqxml,request.getRemoteAddr());
				this.write(msg);
			}
		}
		return null;
	}
	
	
	public String validationAmount(){
		if (validation()) {
			if (reqxml!=null&&!reqxml.trim().equals("")) {
				String msg = autoDeposit.validationAmount(reqxml, request.getRemoteAddr());
				this.write(msg);
			}
		}
		return null;
	}
	
	
	private void write(String msg){
		if (msg!=null&&!msg.equals("")) {
			PrintWriter out=null;
			try {
				log.info("响应数据:"+msg);
				out = response.getWriter();
				out.println(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("callBack:构建写xml数据回支付平台的数据流时，发生异常", e);
			}
			finally{
				if (out!=null) {
					out.close();
				}
			}
		}
	}
	
	
	private boolean validation(){
		log.info("请求IP地址："+request.getRemoteAddr()+"，请求数据："+reqxml);
		String ips=Configuration.getInstance().getValue("netpay_services_ip");
		List ipList = Arrays.asList(ips.split("\\,"));
		return ipList.contains(request.getRemoteAddr());
	}


	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session=arg0;

	}

	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		request=arg0;
	}

	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		response=arg0;
	}
	
	public IAutoDeposit getAutoDeposit() {
		return autoDeposit;
	}

	public void setAutoDeposit(IAutoDeposit autoDeposit) {
		this.autoDeposit = autoDeposit;
	}
	
	public String getReqxml() {
		return reqxml;
	}

	public void setReqxml(String reqxml) {
		this.reqxml = reqxml;
	}


}
