package com.nnti.office.action.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.nnti.office.model.auth.Operator;
import com.nnti.office.model.auth.Role;
import com.nnti.office.model.common.ResponseData;
import com.nnti.office.model.common.ResponseEnum;
import com.nnti.office.service.auth.OperatorService;
import com.nnti.office.service.auth.PermissionService;
import com.nnti.office.service.auth.RoleService;
import com.nnti.office.service.constant.ContantService;
import com.nnti.office.util.IPUtil;
import com.nnti.office.util.JSONUtil;
import com.nnti.office.util.StringUtil;
import com.nnti.office.util.StrutsPrintUtil;
import com.opensymphony.xwork2.ActionSupport;

import dfh.model.ReCaptchaConfig;
import dfh.utils.CheckInOutUtil;
import dfh.utils.Constants;
import dfh.utils.HttpUtils;

@Namespace("/operator")
@ResultPath(value="/")
public class OperatorAction extends ActionSupport implements SessionAware,ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 4705050111782885763L;
	private Logger logger = Logger.getLogger(OperatorAction.class);
	
	private HttpServletResponse response;
	
	@Autowired
	ContantService contantService;
	
	@Autowired
	OperatorService operatorService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PermissionService permissionService;
	
	private Map<String,Object> session;
	private HttpServletRequest request;
	private String validateCode;
	private String mailCode;
	private String loginname;
	private String password;
	private String smsValidPwd;//短信验证密码
	private String errormsg;
	private String username;
	
	private Operator operator;
	private String deviceID;
	
	
	
	public String getDeviceID() {
		return deviceID;
	}
	
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	
	private String checkRecaptcha(String secret){
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod("https://www.google.com/recaptcha/api/siteverify");
		try {
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("secret", secret);
			//String gRecaptchaResponse = getRequest().getParameter ("g-recaptcha-response");
			method.setParameter("response", deviceID);
			method.setParameter("remoteip", getIp());
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			JSONObject jobj = JSONObject.fromObject(result);
			Boolean res = jobj.getBoolean("success");
			if(res){
				return null;
			}else{
				String errMsg = jobj.getString("error-codes");
				return "人机验证异常！";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return "人机验证异常！";
	}
	
	/**
     * 根据URL获取domain
     * @param url
     * @return
     */
	private String getDomainForUrl(String url) {
		String domainUrl = null;
		if (url == null) {
			return null;
		} else {
			Pattern p = Pattern.compile(
					"(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(url);
			while (matcher.find()) {
				domainUrl = matcher.group();
			}
			return domainUrl;
		}
	}	
	
	@Action(value="officeLogin", results={
			@Result(name="success",location="/html/home/home.html"),
			@Result(name="input",location="/office/index.jsp"),
			@Result(name="changePwd",location="/office/functions/modifyOwnPwdTwo.jsp"),
	})
	public String login() {
		if (session.get(Constants.SESSION_OPERATORID) != null) {
			return SUCCESS;
		}

		String referWebsite=this.getRequest().getServerName();
		String domain = referWebsite;		
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "1");
		map.put("domain", domain);
		
		List<ReCaptchaConfig> resultList = operatorService.getReCaptchaConfig(map);
		if(resultList !=null && resultList.size()>0 && resultList.get(0)!=null){
			ReCaptchaConfig rc = (ReCaptchaConfig) resultList.get(0);
			String resultMsg = checkRecaptcha(rc.getSecret_key());
			if(resultMsg !=null){
				addFieldError("wwctrl", resultMsg);
				return INPUT;
			}
		}
		
		//String rand = (String) session.get(Constants.SESSION_RANDID);
		String erand = (String) session.get(Constants.SESSION_EMAIL_RANDID);
		/*if (!validateCode.equalsIgnoreCase(rand)) {
			addFieldError("wwctrl", "验证码错误");
			session.remove(Constants.SESSION_RANDID);
			return INPUT;
		}*/

		if(contantService.isOpenEmail()){
			if (erand==null||"".equals(erand) || !mailCode.equalsIgnoreCase(erand)) {
				addFieldError("wwctrl", "邮箱验证码错误");
				return INPUT;
			}
		}
		
		String result =  operatorService.judgeWetherToChangePwd(loginname) ;
		if(null == result){
			addFieldError("wwctrl", "用户不存在");
			return INPUT ;
		}
		if(result.equals("changePwd")){
			session.put(Constants.SESSION_OPERATORNAME, loginname);
			return result ;
		}
		
		Operator operator = operatorService.getOperatorByUsername(loginname);
		if(operator == null){
			addFieldError("wwctrl", "用户不存在");
			return INPUT;
		}
		if(operator.getValidType() == null){
			addFieldError("wwctrl", "您的账号未指定验证类型，请先修改指定验证类型");
			return INPUT;
		}
		if(operator.getValidType() == 1){//1,短信验证，2，打卡验证，3无需验证
			String msmValidMsg = operatorService.validateSms(operator, this.smsValidPwd);
			if(msmValidMsg != null){
				addFieldError("wwctrl", msmValidMsg);
				return INPUT;
			}
		} else if(operator.getValidType() == 2) {
			if(StringUtils.isEmpty(operator.getEmployeeNo())){
				addFieldError("wwctrl", "根据您的安全验证类型，员工编码为空，不能登录系统！");
			return INPUT;
			}
			String rsMsg = CheckInOutUtil.checkData(operator.getEmployeeNo());
			if(!"".equals(rsMsg)){
				addFieldError("wwctrl", rsMsg);
				return INPUT;
			}
		} else if(operator.getValidType() != 3) {
			addFieldError("wwctrl", "您的账号指定验证类型不存在，请修改指定验证类型");
			return INPUT;
		}
		
		String msg = operatorService.login(operator, password, IPUtil.getIp(request));
		if (msg != null) {
			addFieldError("wwctrl", msg);
		}else {
			session.put(Constants.SESSION_OPERATORID, operator);
			//add authorization data
			List<Role> roleList = roleService.getRoleByUsername(operator.getUsername());
			session.put(Constants.SESSION_AUTH_ROLE, roleList);
			List<String> permissionUrlList = permissionService.getUserPermissionUrl(operator.getUsername());
			session.put(Constants.SESSION_AUTH_PERMISSION, permissionUrlList);
			List<String> permissionCodeList = permissionService.getUserPermissionCode(operator.getUsername());
			session.put(Constants.SESSION_AUTH_CODE, permissionCodeList);
			String empNo = operator.getEmployeeNo();
			String phoneno = operator.getPhoneno();
			if (StringUtils.isEmpty(empNo) && StringUtils.isEmpty(phoneno)) {
				setErrormsg("登陆后请先在左下角绑定员工编号，谢谢"); 
			}
			return SUCCESS;
		}
		return INPUT;
	}
	
	@Action(value="officeLogout")
	public void logout() {
		ResponseData responseData = new ResponseData();
		try {
			session.remove(Constants.SESSION_OPERATORID);
			session.remove(Constants.SESSION_OPERATORNAME);
			session.remove(Constants.SESSION_RANDID);
			session.remove(Constants.SESSION_EMAIL_RANDID);
		}catch(Exception e) {
			logger.error("登出异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getAllOperator")
	public void getAllOperator() {
		ResponseData responseData = new ResponseData();
		List<Operator> operatorList = null;
		try {
			operatorList = operatorService.getAllOperator();
		}catch(Exception e) {
			logger.error("获取所有角色列表异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(operatorList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="searchOperator" )
	public void searchOperator() {
		ResponseData responseData = new ResponseData();
		List<Operator> operatorList = null;
		//validation
		if(operator == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			operatorList = operatorService.searchOperator(operator);
		}catch(Exception e) {
			logger.error("搜索用户异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(operatorList);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="updateOperator" )
	public void updateOperator() {
		ResponseData responseData = new ResponseData();
		//validation
		if(operator == null || StringUtil.isEmpty(operator.getUsername()) || operator.getEnabled() == null || operator.getValidType() == null ) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			operatorService.updateOperator(operator);
		}catch(Exception e) {
			logger.error("修改角色异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getOperatorByUsername")
	public void getOperatorByUsername() {
		ResponseData responseData = new ResponseData();
		Operator operator = null;
		try {
			operator = operatorService.getOperatorByUsername(username);
		}catch(Exception e) {
			logger.error("按照用户名获取用户异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		responseData.setData(operator);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="deleteOperator" )
	public void deleteOperator() {
		ResponseData responseData = new ResponseData();
		//validation
		if(username == null) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		try {
			operatorService.deleteOperator(username);
		}catch(Exception e) {
			logger.error("删除用户异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="addOperator" )
	public void addOperator() {
		ResponseData responseData = new ResponseData();
		//validation
		if(operator == null || StringUtil.isEmpty(operator.getUsername()) || operator.getEnabled() == null || operator.getValidType() == null || StringUtil.isEmpty(operator.getAuthority())) {
			responseData.setResponseEnum(ResponseEnum.PARAMETER_MISSING);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		System.out.println(JSONUtil.toJSONString(operator));
		try {
			operatorService.insertOperator(operator);
		}catch(Exception e) {
			logger.error("创建用户异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	@Action(value="getSubRole")
	public void getSubRole() {
		ResponseData responseData = new ResponseData();
		Operator operatorInSession = (Operator)session.get(Constants.SESSION_OPERATORID);
		List<Role> roleList = null;
		try {
			roleList = roleService.getRoleListByNames(operatorInSession.getSubRole());
		}catch(Exception e) {
			logger.error("获取下级角色列表异常", e);
			responseData.setResponseEnum(ResponseEnum.SERVER_ERROR);
			StrutsPrintUtil.printJsonResponse(response,responseData);
			return;
		}
		responseData.setData(roleList);
		responseData.setResponseEnum(ResponseEnum.SUCCESS);
		StrutsPrintUtil.printJsonResponse(response,responseData);
	}
	
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public String getMailCode() {
		return mailCode;
	}
	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getSmsValidPwd() {
		return smsValidPwd;
	}
	public void setSmsValidPwd(String smsValidPwd) {
		this.smsValidPwd = smsValidPwd;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
}
