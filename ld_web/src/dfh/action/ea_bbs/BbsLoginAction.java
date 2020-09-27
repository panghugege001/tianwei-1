package dfh.action.ea_bbs;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import dfh.model.Users;
import dfh.service.interfaces.IMemberSignrecord;
import dfh.utils.AxisUtil;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.StringUtil;
import dfh.utils.Utils;

public class BbsLoginAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware,SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2744344133975917767L;
	private HttpServletRequest req;
	private HttpServletResponse res;
	private IMemberSignrecord memberService;
	private Map<String, Object> session;
	private String e68_bbs_index;
	private String e68_bbs_login;
	

	public IMemberSignrecord getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberSignrecord memberService) {
		this.memberService = memberService;
	}

	private Logger log=Logger.getLogger(BbsLoginAction.class);
	
	public String bbsUrlforword(){
		e68_bbs_index=Configuration.getInstance().getValue("e68_bbs_index");
		e68_bbs_login=Configuration.getInstance().getValue("e68_bbs_login");
		return SUCCESS;
	}

	
	// 用于论坛的退出动作
	public String logout(){
		try {
			if (this.validateIP()) {
				String username=req.getParameter("username");
				if (username==null||username.trim().equals("")) {
					this.println("1"); 
				}else{
					String crcString=req.getParameter("rd");
					if (crcString!=null&&crcString.equals(Utils.string2crc(username))) {
						username=new String(StringUtil.convertHexStrToByteArray(username),"gbk");
						//System.out.println("logout:"+username);
						memberService.logout(username);
						//session.remove(Constants.SESSION_CUSTOMERID);
						//ServletActionContext.getRequest().getSession().removeAttribute(Constants.SESSION_CUSTOMERID);
						this.println("0"); // 成功
					}else{
						//System.out.println("logout:验证为通过："+crcString);
						this.println("1");
					}
					
					
				}
			}else{
				this.println("1"); 
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			this.println("1"); // 失败
		}
		return null;
	}

	// 用于论坛的登录回调验证
	public String checkLogin(){
		try {
			if (this.validateIP()) {
				String username=req.getParameter("username");
				if (username==null||username.trim().equals("")) {
					this.println("1"); 
				}else{
					String crcString=req.getParameter("rd");
					if (crcString!=null&&crcString.equals(Utils.string2crc(username))) {
						username=new String(StringUtil.convertHexStrToByteArray(username),"gbk");
						//System.out.println("checklogin:"+username);
						if (memberService.isLogined(username)) {
							this.println("0"); // 成功
						}else{
							this.println("1"); // 失败
						}
					}else{
						//System.out.println("checklogin:验证为通过："+crcString);
						this.println("1");
					}
					
				}
			}else{
				this.println("1"); 
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
			this.println("1"); // 失败
		}
		return null;
	}
	
	private boolean validateIP(){
		String remoteIP = req.getRemoteAddr();
		log.info("请求IP地址："+remoteIP+"，请求数据："+req.getParameter("username"));
		List<String> ipList = Arrays.asList(Configuration.getInstance().getValue("valid_bbs_ip").split("\\,"));
		return ipList.contains(remoteIP);
	}
	
	private void println(String msg){
		log.info("响应IP地址："+req.getRemoteAddr()+";响应数据："+msg);
		PrintWriter out=null;
		try {
			out = res.getWriter();
			out.println(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if (out!=null) {
				out.close();
			}
		}
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		req=arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		res=arg0;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session=arg0;
	}

	public String getE68_bbs_index() {
		return e68_bbs_index;
	}

	public void setE68_bbs_index(String e68_bbs_index) {
		this.e68_bbs_index = e68_bbs_index;
	}

	public String getE68_bbs_login() {
		return e68_bbs_login;
	}

	public void setE68_bbs_login(String e68_bbs_login) {
		this.e68_bbs_login = e68_bbs_login;
	}

}
