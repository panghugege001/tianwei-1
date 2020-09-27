package dfh.action.customer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import dfh.model.weCustomer.BankCardinfo;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.utils.*;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import dfh.action.SubActionSupport;
import dfh.action.SynchronizedUtil;
import dfh.model.Userbankinfo;
import dfh.model.Users;
import dfh.model.enums.QuestionEnum;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.IUserbankinfoService;

public class UserbankinfoAction extends SubActionSupport implements SessionAware,ServletRequestAware,ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6337780550839143855L;
	private Map<String, Object> session;
	private IUserbankinfoService userbankinfoService;
	private String errormsg;
	private Logger log=Logger.getLogger(UserbankinfoAction.class);
	private String bankno;
	private String bankname;
	private HttpServletRequest req;
	private HttpServletResponse res;
	private String r;
	private String password;
	private String bankaddress;
	private String bindingCode;
	private Integer questionid;
	private String answer;
	public String getBankaddress() {
		return bankaddress;
	}
	public void setBankaddress(String bankaddress) {
		this.bankaddress = bankaddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setR(String r) {
		this.r = r;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public IUserbankinfoService getUserbankinfoService() {
		return userbankinfoService;
	}
	public void setUserbankinfoService(IUserbankinfoService userbankinfoService) {
		this.userbankinfoService = userbankinfoService;
	}
	protected ObjectMapper JSON = new ObjectMapper();


	public String searchBankno(){
		
		try {
			req.setCharacterEncoding("UTF-8");
			getResponse().setCharacterEncoding("UTF-8");
			getResponse().setContentType("text/plain;charset=UTF-8");  
		} catch (UnsupportedEncodingException e1) {
			
		}
		Users user=(Users) session.get(Constants.SESSION_CUSTOMERID);
		if (user==null||this.bankname==null||this.bankname.trim().equals("")) {
			this.println("1");
			return null;
		}
		
		Userbankinfo userbankinfo=null;
		try {
			
			userbankinfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getUserbankinfo", new Object[] {
				user.getLoginname(),bankname}, Userbankinfo.class);
		} catch (AxisFault e) {
			log.error(e);
			this.println("1");
		}
		if (null!=userbankinfo && -1000!=userbankinfo.getFlag()) {
			int len=userbankinfo.getBankno().length()-3;
			String str="";
			for (int i = 0; i < len; i++) {
				str+="*";
			}
			this.bankno=str+userbankinfo.getBankno().substring(userbankinfo.getBankno().length()-3);
			this.bankaddress=userbankinfo.getBankaddress();
			try {
				getResponse().getWriter().println(bankno+"|||"+bankaddress);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			this.println("1");
		}
		
		return null;
	}
	
	private void println(String msg){
		ServletOutputStream out=null;
		try {
			out = res.getOutputStream();
			out.println(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public String getBankInfo(){
		Users user=(Users) session.get(Constants.SESSION_CUSTOMERID);
		if (user==null) {
			GsonUtil.GsonObject("请先登录后在继续操作");
			return null;
		}
		BankCardinfo bankInfo = null;
		try {
			String url = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("middleservice.url"));
			System.out.print(url);
			String result = MyWebUtils.getHttpContentByParam(url + "/bank/getBankInfo", MyWebUtils.getListNamevaluepair("identifycode", bankno));
			if(result == null || result.equals("") == true){
				GsonUtil.GsonObject("我们不支持此银行卡");
				return null;
			}
			System.out.println(result);
			bankInfo = JSON.readValue(result, BankCardinfo.class);
			if(bankInfo.getCardlength().intValue() != bankno.length()) {
				GsonUtil.GsonObject("您输入的银行卡信息错误");
				return null;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			return null;
		}
		GsonUtil.GsonObject(bankInfo);
		return null;
	}
	
	public String bandingBankno(){
		
		if (this.bankname==null||this.bankname.trim().equals("")) {
			GsonUtil.GsonObject("银行名称不能为空");
			return null;
		}
		if (this.bankno==null||this.bankno.trim().equals("")) {
			GsonUtil.GsonObject("卡/折号不能为空");
			return null;
		}
		if (this.bankno.trim().length()>80||this.bankno.trim().length()<10) {
			GsonUtil.GsonObject("卡/折号长度只能在10-80位之间，请重新输入");
			return null;
		}
//		if (bankaddress==null||bankaddress.trim().equals("")) {
//			GsonUtil.GsonObject("开户网点不能为空");
//			return null;
//		}
		
		if (this.password==null||this.password.trim().equals("")) {
			GsonUtil.GsonObject("登录密码不可为空");
			return null;
		}
		
		Users user=(Users) session.get(Constants.SESSION_CUSTOMERID);
		if (user==null) {
			GsonUtil.GsonObject("请先登录后在继续操作");
			return null;
		}
		
		if (!EncryptionUtil.encryptPassword(this.password).equalsIgnoreCase(user.getPassword())) {
			GsonUtil.GsonObject("登录密码错误，请重新输入");
			return null;
		}
		//支付宝绑定手机验证
		if(StringUtils.equals(bankname, "支付宝")){
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if(StringUtils.isBlank(sessionCode)){
				GsonUtil.GsonObject("验证码失效或为空,请重新获取");
				return null;
			}
			if(!sessionCode.equals(bindingCode)){
				GsonUtil.GsonObject("验证码不正确");
				return null;
			}
		}

		List<Userbankinfo> userbankinfoList=null;
		try {
			userbankinfoList = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getUserbankinfoList", new Object[] {
				user.getLoginname()}, Userbankinfo.class);
			if(null==userbankinfoList){
				GsonUtil.GsonObject("系统异常，请稍后再试");
				return null;
			}
		} catch (Exception e) {
			log.error(e);
			GsonUtil.GsonObject("系统异常，请稍后再试");
			return null;
		}
		if (userbankinfoList.size()<=2) {
			// 未超限制，可以继续帮定
			boolean isBanding=true;
			for (int i = 0; i < userbankinfoList.size(); i++) {
				Userbankinfo userbankinfo = userbankinfoList.get(i);
				if (userbankinfo.getBankname().equals(bankname.trim())) {
					isBanding=false;
					break;
				}
			}
			if (isBanding) {
				try {
					bankno=bankno.replaceAll(" ", "");
					String flag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE, "banding", new Object[] {
						user.getLoginname(), bankno.trim(), bankname.trim(),bankaddress.trim()}, String.class);
					if("success".equals(flag)){
						GsonUtil.GsonObject("绑定成功");
						return null;
					}else if("failed".equals(flag)){
						GsonUtil.GsonObject("系统异常，绑定失败，请稍后再试");
						return null;
					}else
					{
						GsonUtil.GsonObject(flag);
						return null;
					}
				} catch (Exception e) {
					log.error(e);
					GsonUtil.GsonObject("系统异常，绑定失败，请稍后再试");
					return null;
				}
			}else{
				GsonUtil.GsonObject("该银行已经绑定过卡/折号，请不要重复绑定\\n如须解绑，请与在线客服联系");
				return null;
			}
			
		}else{
			GsonUtil.GsonObject("您已经绑定了三个银行卡/折号，且最多只可以绑定三个\\n如须解绑，请与在线客服联系");
			return null;
		}
		
	}
	
	public String saveQuestion(){
		Users user=(Users) session.get(Constants.SESSION_CUSTOMERID);
		if (user==null) {
			GsonUtil.GsonObject("请先登录后在继续操作");
			return null;
		}
		if(StringUtils.isBlank(answer)){
			GsonUtil.GsonObject("答案不能为空");
			return null ;
		}
		if(!QuestionEnum.existKey(questionid)){
			GsonUtil.GsonObject("您所绑定的问题不存在");
			return null ;
		}
		if (!EncryptionUtil.encryptPassword(this.password).equalsIgnoreCase(user.getPassword())) {
			GsonUtil.GsonObject("登录密码错误，请重新输入");
			return null;
		}
		String returnStr=SynchronizedUtil.getInstance().saveQuestion(user.getLoginname(), questionid, answer);
		GsonUtil.GsonObject(returnStr);
		return null ;
	}
	
	public String isBildingQuestion(){
		Users user=(Users) session.get(Constants.SESSION_CUSTOMERID);
		if (user==null) {
			GsonUtil.GsonObject("请先登录后在继续操作");
			return null;
		}
		try {
			Integer number = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "questionsNumber", new Object[] {user.getLoginname()}, Integer.class);
			GsonUtil.GsonObject(number);
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject("绑定失败"+e.getMessage());
		}
		return null;
	}
	
	public void getWithDrawBankStatus() {
		String bankStatus = "ERROR";
		if(StringUtil.isBlank(bankname) || "请选择".equals(bankname)){
			return ;
		}
		try {
			bankStatus = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getWithDrawBankStatus", new Object[] { bankname }, String.class);
		} catch (AxisFault e) {
			log.error("获取提款银行状态异常",e);
			this.print("ERROR");
		}
		writeText(bankStatus);
	}
	
	private void print(String msg){
		ServletOutputStream out=null;
		try {
			out = res.getOutputStream();
			out.print(msg);
			out.flush();
		} catch (IOException e) {
			log.error("server error", e);
		}
		finally{
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("server error", e);
				}
			}
		}
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		session=arg0;
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
	public String getBindingCode() {
		return bindingCode;
	}
	public void setBindingCode(String bindingCode) {
		this.bindingCode = bindingCode;
	}
	public Integer getQuestionid() {
		return questionid;
	}
	public void setQuestionid(Integer questionid) {
		this.questionid = questionid;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
