package dfh.action.customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.utils.AxisUtil;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.HttpUtils;

public class JiuanAction extends SubActionSupport implements SessionAware,ServletRequestAware,ServletResponseAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5102759520815146147L;

	private static Logger log = Logger.getLogger(JiuanAction.class);
	
	private HttpServletResponse res;
	private Map<String, Object> session;
	private HttpServletRequest req;
	
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
	
	private String userName;//产品同步用户名
    private String loginId;//产品登录账号
    private String merchantUserid;//产品在钱包的商户user_id。
    private Integer level;//产品用户信用等级
    private BigDecimal creditScore;//产品信用分数
    private BigDecimal loseAmount;//产品输赢值
    
	public HttpServletResponse getRes() {
		return res;
	}

	public void setRes(HttpServletResponse res) {
		this.res = res;
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getMerchantUserid() {
		return merchantUserid;
	}

	public void setMerchantUserid(String merchantUserid) {
		this.merchantUserid = merchantUserid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public BigDecimal getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(BigDecimal creditScore) {
		this.creditScore = creditScore;
	}

	public BigDecimal getLoseAmount() {
		return loseAmount;
	}

	public void setLoseAmount(BigDecimal loseAmount) {
		this.loseAmount = loseAmount;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 绑定久安账户
	 */
	public void bindAccount(){
		
		Map<String ,Object> map = new HashMap<String ,Object>();
		
		try {
			
			refreshUserInSession();

			Users customer = getCustomerFromSession();

			if (customer == null) {
				map.put("code", 10001);
				map.put("msg", "尚未登录！请重新登录！");
				GsonUtil.GsonObject(map);
				return;
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", "ld");
			paramMap.put("loginName", customer.getLoginname());
			paramMap.put("loginId", loginId);
//			paramMap.put("merchantUserid", merchantUserid);
			paramMap.put("level", customer.getLevel());
//			paramMap.put("creditScore", creditScore);
//			paramMap.put("loseAmount", loseAmount);

			String str = mapper.writeValueAsString(paramMap);

			String requestJson = app.util.AESUtil.getInstance().encrypt(str);

			String url = Configuration.getInstance().getValue("middleservice.url");
			url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/jiuan/account/bind");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				map.put("code", 10001);
				map.put("msg", "系统繁忙，请稍后重试1！");
				GsonUtil.GsonObject(map);
				return;
			} else {

				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, Object> resultMap = mapper.readValue(responseString, Map.class);
				responseString = (String) resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);
				map.put("code", resultMap.get("code"));
				map.put("msg",  resultMap.get("message"));
				GsonUtil.GsonObject(map);
				return;
			}
		} catch (Exception e) {

			e.printStackTrace();
			map.put("code", 10001);
			map.put("msg", "系统繁忙，请稍后重试2！");
			GsonUtil.GsonObject(map);
			return;
		}
		
	}

	/**
	 * 同步久安账户
	 */
	public void syncAccount(){
		
		Map<String ,Object> map = new HashMap<String ,Object>();
		
		try {
			
			refreshUserInSession();

			Users customer = getCustomerFromSession();

			if (customer == null) {
				map.put("code", 10001);
				map.put("msg", "尚未登录！请重新登录！");
				GsonUtil.GsonObject(map);
				return;
			}

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", "ld");
			paramMap.put("loginName", customer.getLoginname());
//			paramMap.put("loginId", loginId);
//			paramMap.put("merchantUserid", merchantUserid);
			paramMap.put("level", customer.getLevel());
//			paramMap.put("creditScore", creditScore);
//			paramMap.put("loseAmount", loseAmount);

			String str = mapper.writeValueAsString(paramMap);

			String requestJson = app.util.AESUtil.getInstance().encrypt(str);

			String url = Configuration.getInstance().getValue("middleservice.url");
			url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/jiuan/account/sync");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				map.put("code", 10001);
				map.put("msg", "系统繁忙，请稍后重试1！");
				GsonUtil.GsonObject(map);
				return;
			} else {

				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, Object> resultMap = mapper.readValue(responseString, Map.class);
				responseString = (String) resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);
				map.put("code", resultMap.get("code"));
				map.put("msg",  resultMap.get("message"));
				map.put("data",  resultMap.get("data"));
				GsonUtil.GsonObject(map);
				return;
			}
		} catch (Exception e) {

			e.printStackTrace();
			map.put("code", 10001);
			map.put("msg", "系统繁忙，请稍后重试2！");
			GsonUtil.GsonObject(map);
			return;
		}
		
	}
	
	public void refreshUserInSession()throws Exception  {
		Users customer = getCustomerFromSession();
		Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {
			 customer.getLoginname()}, Users.class);
		 getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
		 if(customer.getPostcode()==null || user.getPostcode()==null){
			 getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		 }
		 if(!customer.getPostcode().equals(user.getPostcode())){
			 getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		 }
	}
}
