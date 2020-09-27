package dfh.action.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.util.AESUtil;
import dfh.action.SubActionSupport;
import dfh.action.SynchronizedUtil;
import dfh.captcha_dc.model.Status;
import dfh.captcha_dc.service.TouClick;
import dfh.model.AlipayAccount;
import dfh.model.Const;
import dfh.model.PreferentialConfig;
import dfh.model.Userbankinfo;
import dfh.model.Users;
import dfh.model.bean.ServiceStatus;
import dfh.model.notdb.ServiceStatusDTO;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.utils.AxisUtil;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.SendPhoneMsgUtil;
import dfh.utils.TelCrmUtil;
import dfh.utils.Touclick;

/**
 * 自助活动
 * 
 * @author Administrator
 *
 */
public class SelfHelpAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(SelfHelpAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 4305882572176561227L;

	private String type; // 自助类型
	private String code ;
	private String bdbank ;

	private final String website_key = "68aca137-f3c5-457b-87a4-8a46880b1e66";
	private final String private_key = "60ccd51f-5df3-4f49-bdeb-5c36eed2329c";
//	private final String website_key = "9d8d3eb9-c4a7-4921-9720-11c0e90a1ebe";
//	private final String private_key = "6f854ede-17ee-47e6-871b-28d8195392f0";
	private String check_key ;
	private String check_address;
	
	private String ioBB ;
	private String phonenum ;
	private String platform ;
	private String password ;
	private String account ;//支付宝账号
	private String checkCode = "123";
	private String sid;
	/**
	 * 是否为安全账户
	 * 
	 * @return
	 */
	public String haveSameInfo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			} else {
				user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,"getUser", new Object[] { user.getLoginname() }, Users.class);
			}
			//取消玩家是否危险的限制，根据同IP或者同姓名下的三个月内是否领过
			/*if (user.getWarnflag().equals(2)) {
				GsonUtil.GsonObject("您的安全等级不够!");
				log.info("您的安全等级不够!");
				return null;
			}*/
			
			String youHuiFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,"checkGameIsProtect", new Object[] { "PT8元自助优惠" }, String.class);
			if (!youHuiFlag.equals("1")) {
				GsonUtil.GsonObject( "PT8元自助优惠维护中...");
				log.info("PT8元自助优惠维护中...");
				return null;
			}
			
			// 判断是否有重复的信息(真实姓名、手机号、ip、email、cpu、银卡关联卡 )
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,"haveSameInfo", new Object[] { user.getLoginname() }, String.class);
			if (null == result) {
				Const smsFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "短信反转验证" }, Const.class);
				if(null != smsFlag && smsFlag.getValue().equals("1")){
					GsonUtil.GsonObject("successSms");
				}else{
					GsonUtil.GsonObject("success");
				}
				return null;
			} else {
				log.info(result);
				GsonUtil.GsonObject(result);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GsonUtil.GsonObject("error");
		return null;
	}
	
	public String isBlindingCard(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			Integer numbers = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getCardNums", new Object[] { user.getLoginname() }, Integer.class);
			if(0 == numbers){
				GsonUtil.GsonObject("请绑定银行卡");
				return null;
			}else{
				GsonUtil.GsonObject("pass");
				return null;
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	public void checkWebServerStatus(){
		
		List<ServiceStatus> total = new ArrayList<>();
		
		Long redisStart = System.currentTimeMillis();
		String exceptionMsg = null;
		Integer redisStatusCode = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("product", "ld");
		String url = "http://redisservice.com/ipCache/checkRedisStatus";
		PostMethod method = new PostMethod(url);
		String message = null;
		try {
			String str = mapper.writeValueAsString(paramMap);

			String requestJson = AESUtil.getInstance().encrypt(str);

			HttpClient httpClient = new HttpClient();

			HttpClientParams paramsClient = new HttpClientParams();
			paramsClient.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			paramsClient.setParameter("http.protocol.content-charset", "UTF-8");
			paramsClient.setParameter("http.socket.timeout", 10000);

			httpClient.setParams(paramsClient);

			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				redisStatusCode = 1;
				exceptionMsg = "请求redis服务器异常,状态码：" + statusCode;
				log.info("web AreaRestrictFilter：连接不到http://redisservice.com");
				
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);
				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				String responseData = resultMap.get("responseData");
				responseData = app.util.AESUtil.getInstance().decrypt(responseData);

				resultMap = mapper.readValue(responseData, Map.class);
				message = resultMap.get("message");
				// 拒绝
				if ("false".equals(message)) {
					redisStatusCode = 2;
					exceptionMsg = "redis服务器自检异常";
				}
			}
		} catch (Exception e) {
			log.error("请求redis集群异常：", e);
			exceptionMsg = e.getMessage();
			redisStatusCode = 3;
		} finally {
			method.releaseConnection();
		}
		
		ServiceStatus rs = new ServiceStatus();
		rs.setName("WebRedisStatus");
		rs.setExecMs(System.currentTimeMillis() - redisStart);
		rs.setStatus(redisStatusCode);
		rs.setExceptionMsg(exceptionMsg);
		total.add(rs);
		
		Long wsStart = System.currentTimeMillis();
		try {
			List<ServiceStatus> wsStatus = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkServiceStatus",
					new Object[] { }, ServiceStatus.class);
			total.addAll(wsStatus);
		} catch (Exception e) {
			ServiceStatus ws = new ServiceStatus();
			ws.setName("WebServiceStatus");
			ws.setExecMs(System.currentTimeMillis() - wsStart);
			ws.setStatus(1);
			ws.setExceptionMsg(e.getMessage());
			total.add(ws);
		}
		
		GsonUtil.GsonObject(total);
	}
	
	public void checkMidServerStatus(){
		
		List<ServiceStatus> total = new ArrayList<>();
		Long wsStart = System.currentTimeMillis();
		try {
			String url = Configuration.getInstance().getValue("middleservice.url");
			url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/checkServiceStatus");
			method.setRequestHeader("Connection", "close");

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				ServiceStatus ws = new ServiceStatus();
				ws.setName("MidServiceStatus");
				ws.setExecMs(System.currentTimeMillis() - wsStart);
				ws.setStatus(2);
				ws.setExceptionMsg("Response Exception, statusCode:" + statusCode);
				total.add(ws);
			} else {

				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				ServiceStatusDTO mid = mapper.readValue(responseString, ServiceStatusDTO.class);
				total.addAll(mid.getStatus());

			}
		} catch (Exception e) {
			ServiceStatus ws = new ServiceStatus();
			ws.setName("MidServiceStatus");
			ws.setExecMs(System.currentTimeMillis() - wsStart);
			ws.setStatus(1);
			ws.setExceptionMsg(e.getMessage());
			total.add(ws);
		}
		
		GsonUtil.GsonObject(total);
	}
	
	//判断是否有重复的银行卡
	public String repeatBankCards(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			Boolean flag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"haveRepeatCards", new Object[] { user.getLoginname() }, Boolean.class);
			if(flag){
				GsonUtil.GsonObject("重复银行卡信息");
				return null;
			}
			Integer numbers = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getCardNums", new Object[] { user.getLoginname() }, Integer.class);
			if(0 == numbers){
				GsonUtil.GsonObject("请绑定银行卡");
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GsonUtil.GsonObject("success");
		return null ;
	}
	
	/**
	 * 语音验证手机号
	 * @return
	 */
	private static Map<String  , Date> map = new HashMap<String, Date>();
	
	
	public String sendPhoneVoiceCode(){
		
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			String code = RandomStringUtils.randomNumeric(4);
			if(map.containsKey(user.getLoginname())){
				Date date = map.get(user.getLoginname());
				if((new Date().getTime() - date.getTime()) < 1000*20){
					GsonUtil.GsonObject("有正在处理的请求。");
					return null;
				}
			}
			map.put(user.getLoginname(), new Date());
			
			/*//触点
			Boolean touFlag = Touclick.check(website_key, private_key, check_key, check_address, null, null, null);
			if(!touFlag){
				GsonUtil.GsonObject("点触验证码错误");
				return null;
			}*/
			
			TouClick touclick = new TouClick();
//			Status status = touclick.check(checkCode, check_address, check_key, website_key, private_key);
			Status status = touclick.check(check_address,sid, check_key, website_key, private_key);
			System.out.println("code :"+status.getCode() + ",message:" + status.getMessage());
			if(status == null || status.getCode()!=0){
				GsonUtil.GsonObject("点触验证码错误");
				return null;
			}
			
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if(StringUtils.isNotBlank(sessionCode)){
				code = sessionCode ;
			}else{
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
			}
			String flag = TelCrmUtil.call(user.getLoginname(), user.getPhone(), code);
			if(flag.trim().equals("486")){
				GsonUtil.GsonObject("前面有验证,稍后再尝试");
				return null;
			}
			GsonUtil.GsonObject("请注意查收语音验证码！");
			return null;
			/*Boolean sendFlag = false ;
			String result = "";
			for(int i=0 ; i<15 ;i++){
				if(i == 0){
					Thread.sleep(2000);
				}
				result = TelCrmUtil.getResult(user.getLoginname());
				if(result.equals("1")){ //发送成功
					log.info(user.getLoginname()+"：手机验证码-->"+code);
					//保存code
					sendFlag = true ;
					break;
				}else{
					Thread.sleep(5000);
				}
			}
			if(sendFlag){
				GsonUtil.GsonObject("请注意查收语音验证码！");
				return null;
			}else{
				if(result.equals("0")){
					GsonUtil.GsonObject("正在排队等待呼叫");
				}else if(result.equals("2")){
					GsonUtil.GsonObject("呼叫失败");
				}else if(result.equals("3")){
					GsonUtil.GsonObject("空号");
				}else if(result.equals("4")){
					GsonUtil.GsonObject("正在呼叫中");
				}else if(result.equals("5")){
					GsonUtil.GsonObject("对方忙");
				}else if(result.equals("6")){
					GsonUtil.GsonObject("关机，不在服务区");
				}else{
					GsonUtil.GsonObject("系统繁忙");
				}
				return null;
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	public String sendPhoneSmsCode(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			
			/*//触点
			Boolean result = Touclick.check(website_key, private_key, check_key, check_address, null, null, null);
			if(!result){
				GsonUtil.GsonObject("点触验证码错误");
				return null;
			}*/
			
			TouClick touclick = new TouClick();
//			Status status = touclick.check(checkCode, check_address, check_key, website_key, private_key);
			Status status = touclick.check(check_address,sid, check_key, website_key, private_key);
			System.out.println("code :"+status.getCode() + ",message:" + status.getMessage());
			if(status == null || status.getCode()!=0){
				GsonUtil.GsonObject("点触验证码错误");
				return null;
			};
			
			String code = RandomStringUtils.randomNumeric(4);
			if(map.containsKey(user.getLoginname())){
				Date date = map.get(user.getLoginname());
				if((new Date().getTime() - date.getTime()) < 1000*120){
					GsonUtil.GsonObject("有正在处理的请求。");
					return null;
				}
			}
			map.put(user.getLoginname(), new Date());
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "体验金短信" }, Const.class);
			if (constPay == null || constPay.getValue().equals("0")) {
				GsonUtil.GsonObject("短信暂不可使用");
				return null;
			}
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if(StringUtils.isNotBlank(sessionCode)){
				code = sessionCode ;
			}
			log.info("短信验证码："+code);
			String msg = null ;
			/*String smsFlag = StringUtil.getConfigValue("SMS");
			if(smsFlag.equals("1")){
				msg = SendPhoneMsgUtil.sendSms(user.getPhone(), code);
			}else if(smsFlag.equals("2")){
				msg = SendPhoneMsgUtil.callMine(user.getPhone(), code);
			}else{
				return "errorXX";
			}*/
			 
			msg=SendPhoneMsgUtil.callfour(user.getPhone(), "您的验证码为：" + code);
			if(!msg.equals("发送成功")){  
				log.info("接口1发送短信失败:"+msg);
				msg="发送失败！";
			}else {
				log.info("接口1发送短信成功："+code);
			}
			
			if(!msg.equals("发送成功")){
			msg = SendPhoneMsgUtil.sendSms(user.getPhone(), code);
			log.info(msg);
			}
			if(!msg.equals("发送成功")){
				msg = SendPhoneMsgUtil.callMine(user.getPhone(), code);
			}
			
			if(msg.equals("发送成功")){
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
				GsonUtil.GsonObject("请注意查收短信验证码");
				return null;
			}else{
				GsonUtil.GsonObject(msg);
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		GsonUtil.GsonObject("系统繁忙");
		return null ;
	}
	
	
	public String sendPhoneSmsTwoCode(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			String code = RandomStringUtils.randomNumeric(4);
			if(map.containsKey(user.getLoginname())){
				Date date = map.get(user.getLoginname());
				if((new Date().getTime() - date.getTime()) < 1000*120){
					GsonUtil.GsonObject("有正在处理的请求。");
					return null;
				}
			}
			map.put(user.getLoginname(), new Date());
			
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if(StringUtils.isNotBlank(sessionCode)){
				code = sessionCode ;
			}
			
//			String msg = SendPhoneMsgUtil.callTwo(user.getPhone(), "请注意查收您的短信验证码。【"+code+"】");
//			String msg = SendPhoneMsgUtil.callThree(user.getPhone(), "您的验证码为："+code);
			String msg = "改短信通道维护中...";
			if(msg.equals("发送成功")){
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
				GsonUtil.GsonObject("请注意查收短信验证码");
				return null;
			}else{
				GsonUtil.GsonObject(msg);
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		GsonUtil.GsonObject("系统繁忙");
		return null ;
	}	
	
	public String checkPhoneCode(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			Const smsFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
					new Object[] { "短信反转验证" }, Const.class);
			
			if(null != smsFlag && smsFlag.getValue().equals("1")){ //短信反转验证
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "checkValidateCode", new Object[] {user.getLoginname()}, String.class);
				if(msg.contains("success")){
					getHttpSession().setAttribute(Constants.SMSVALIDATE, "success");
					GsonUtil.GsonObject("right");
				}else{
					GsonUtil.GsonObject(msg);
				}
				return null ;
			}else{
				if(StringUtils.isBlank(code)){ //常规短信验证
					GsonUtil.GsonObject("请输入手机验证码");
					return null;
				}
				String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
				if(StringUtils.isBlank(sessionCode)){
					GsonUtil.GsonObject("验证码失效,重新获取");
					return null;
				}
				if(sessionCode.equals(code)){
					GsonUtil.GsonObject("right");
					return null;
				}else{
					GsonUtil.GsonObject("验证码不正确");
					return null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("手机验证码未知错误");
		}
		return null;
	}
	
	/**
	 * 提交PT8元自助
	 * @return
	 */
	public synchronized String commitPT8Self(){
		SynchronizedUtil.getInstance().commitPT8Self(type, code ,platform);
		return null;
	}
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getYouHuiConfig() {
		String loginName = null;
		Integer level = null;
		try {
			Users customer = getCustomerFromSession();
			loginName = customer.getLoginname();
			level = customer.getLevel();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("loginName", loginName);
			paramMap.put("conditionType", "1");
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
			PostMethod method = new PostMethod(url + "/self/deposit/list");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);
			int statusCode = httpClient.executeMethod(method);
	    	if (statusCode != HttpStatus.SC_OK) {
	    		GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			} else {
				byte[] responseBody = method.getResponseBody();
		    	String responseString = new String(responseBody);
		    	
		    	Gson gson = new Gson();
		    	
		    	Map<String, String> resultMap = gson.fromJson(responseString, new TypeToken<Map<String, String>>() {}.getType());
		    	String responseData = resultMap.get("responseData");
		    		    		
		    	responseData = app.util.AESUtil.getInstance().decrypt(responseData);
		    		    		
		    	resultMap = mapper.readValue(responseData, Map.class);
		    	JSONArray jsonArray = JSONArray.fromObject(resultMap.get("data"));
		    	String jsonData = jsonArray.toString();
		    		    		
		    	List<PreferentialConfig> list = gson.fromJson(jsonData, new TypeToken<List<PreferentialConfig>>() {}.getType());
		    	
	    		GsonUtil.GsonObject(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
		}
		return null;
	}
	
	private static Map<String  , Date> callMap = new HashMap<String, Date>();
	//前端打电话
	public String makeCall(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		if(callMap.containsKey(user.getLoginname())){
			Date date = callMap.get(user.getLoginname());
			if((new Date().getTime() - date.getTime()) < 1000*60){
				GsonUtil.GsonObject("有正在处理的请求。");
				return null;
			}
		}
		callMap.put(user.getLoginname(), new Date());
		
		try {
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "makeCall", new Object[] {user.getLoginname() , phonenum}, String.class);
			GsonUtil.GsonObject(msg) ;
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	//支付宝语音验证
	public String sendAlipayPhoneVoiceCode(){
		
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			String code = RandomStringUtils.randomNumeric(4);
			if(map.containsKey(user.getLoginname())){
				Date date = map.get(user.getLoginname());
				if((new Date().getTime() - date.getTime()) < 1000*20){
					GsonUtil.GsonObject("有正在处理的请求。");
					return null;
				}
			}
			map.put(user.getLoginname(), new Date());
			if(StringUtils.isNotBlank(bdbank) && !StringUtils.equals(bdbank, "支付宝")){
				GsonUtil.GsonObject("只是支付宝绑定需要验证");
				return null;
			}
			//判断是否有绑定的支付宝账号
			Userbankinfo userbankinfo=null;
			try {
				userbankinfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getUserbankinfo", new Object[] {
					user.getLoginname(),"支付宝"}, Userbankinfo.class);
			} catch (AxisFault e) {
				log.error(e);
			}
			if(null!=userbankinfo && -1000!=userbankinfo.getFlag()){
				GsonUtil.GsonObject("您已经绑定支付宝账号。");
				return null;
			}
			//触点
			Boolean touFlag = Touclick.check(website_key, private_key, check_key, check_address, null, null, null);
			if(!touFlag){
				GsonUtil.GsonObject("点触验证码错误");
				return null;
			}
			
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if(StringUtils.isNotBlank(sessionCode)){
				code = sessionCode ;
			}else{
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
			}
			String flag = TelCrmUtil.call(user.getLoginname(), user.getPhone(), code);
			if(flag.trim().equals("486")){
				GsonUtil.GsonObject("前面有验证,稍后再尝试");
				return null;
			}
			
			Boolean sendFlag = false ;
			String result = "";
			for(int i=0 ; i<15 ;i++){
				if(i == 0){
					Thread.sleep(2000);
				}
				result = TelCrmUtil.getResult(user.getLoginname());
				if(result.equals("1")){ //发送成功
					log.info(user.getLoginname()+"：手机验证码-->"+code);
					//保存code
					sendFlag = true ;
					break;
				}else{
					Thread.sleep(5000);
				}
			}
			if(sendFlag){
				GsonUtil.GsonObject("请注意查收语音验证码！");
				return null;
			}else{
				if(result.equals("0")){
					GsonUtil.GsonObject("正在排队等待呼叫");
				}else if(result.equals("2")){
					GsonUtil.GsonObject("呼叫失败");
				}else if(result.equals("3")){
					GsonUtil.GsonObject("空号");
				}else if(result.equals("4")){
					GsonUtil.GsonObject("正在呼叫中");
				}else if(result.equals("5")){
					GsonUtil.GsonObject("对方忙");
				}else if(result.equals("6")){
					GsonUtil.GsonObject("关机，不在服务区");
				}else{
					GsonUtil.GsonObject("系统繁忙");
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	//支付宝短信验证
	public String sendAlipayPhoneSmsCode(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject("请登录后重试");
				return null;
			}
			if(StringUtils.isNotBlank(bdbank) && !StringUtils.equals(bdbank, "支付宝")){
				GsonUtil.GsonObject("只是支付宝绑定需要验证");
				return null;
			}
			//判断是否有绑定的支付宝账号
			Userbankinfo userbankinfo=null;
			try {
				userbankinfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getUserbankinfo", new Object[] {
					user.getLoginname(),"支付宝"}, Userbankinfo.class);
			} catch (AxisFault e) {
				log.error(e);
			}
			if(null!=userbankinfo && -1000!=userbankinfo.getFlag()){
				GsonUtil.GsonObject("你已经绑定支付宝账号。");
				return null;
			}
			//触点
			Boolean result = Touclick.check(website_key, private_key, check_key, check_address, null, null, null);
			if(!result){
				GsonUtil.GsonObject("点触验证码错误");
				return null;
			}
			
			String code = RandomStringUtils.randomNumeric(4);
			if(map.containsKey(user.getLoginname())){
				Date date = map.get(user.getLoginname());
				if((new Date().getTime() - date.getTime()) < 1000*120){
					GsonUtil.GsonObject("有正在处理的请求。");
					return null;
				}
			}
			map.put(user.getLoginname(), new Date());
			
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if(StringUtils.isNotBlank(sessionCode)){
				code = sessionCode ;
			}
			String msg = null ;
			msg=SendPhoneMsgUtil.callfour(user.getPhone(), "您的验证码为：" + code);
			if(!msg.equals("发送成功")){  
				log.info("接口1发送短信失败:"+msg);
				msg="发送失败！";
			}else {
				log.info("接口1发送短信成功："+code);
			}
			if(!msg.equals("发送成功")){
			msg = SendPhoneMsgUtil.sendSms(user.getPhone(), code);
			log.info(msg);
			}
			if(!msg.equals("发送成功")){
				msg = SendPhoneMsgUtil.callMine(user.getPhone(), code);
			}
			
			if(msg.equals("发送成功")){
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
				log.info(user.getLoginname()+"短信验证码-->"+code);
				GsonUtil.GsonObject("请注意查收短信验证码");
				return null;
			}else{
				GsonUtil.GsonObject(msg);
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		GsonUtil.GsonObject("系统繁忙");
		return null ;
	}
	
	public String getPhoneAndCode(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		try {
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "getPhoneAndValidateCode", new Object[] {user.getLoginname()}, String.class);
			GsonUtil.GsonObject(JSONObject.fromObject(msg)) ;
			return null ;
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		GsonUtil.GsonObject("getPhoneAndCode error") ;
		return null ;
	}
	
	public String phoneSmsCodeValidate(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		try {
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "checkValidateCode", new Object[] {user.getLoginname()}, String.class);
			GsonUtil.GsonObject(msg) ;
			return null ;
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		GsonUtil.GsonObject("phoneSmsCodeValidate error") ;
		return null ;
	}
	
	public String getAlipayAccount(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		try {
			AlipayAccount alipayAccount = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "getAlipayAccount", new Object[] {user.getLoginname() , 0 }, AlipayAccount.class);
			GsonUtil.GsonObject(alipayAccount==null?"empty":alipayAccount) ;
			return null ;
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject("getAlipayAccount error"+e.getMessage()) ;
			return null;
		}
	}
	public String saveAlipayAccount(){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		if(StringUtils.isBlank(password)){
			GsonUtil.GsonObject("密码不能为空");
			return null;
		}
		if(StringUtils.isBlank(account)){
			GsonUtil.GsonObject("支付宝存款账号不能为空");
			return null;
		}
		try {
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "saveAlipayAccount", new Object[] {user.getLoginname() , password , account}, String.class);
			GsonUtil.GsonObject(msg) ;
			return null ;
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject("更新账号失败"+e.getMessage()) ;
			return null;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCheck_key() {
		return check_key;
	}

	public void setCheck_key(String check_key) {
		this.check_key = check_key;
	}

	public String getCheck_address() {
		return check_address;
	}

	public void setCheck_address(String check_address) {
		this.check_address = check_address;
	}

	public String getIoBB() {
		return ioBB;
	}

	public void setIoBB(String ioBB) {
		this.ioBB = ioBB;
	}
	
	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getBdbank() {
		return bdbank;
	}

	public void setBdbank(String bdbank) {
		this.bdbank = bdbank;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
 
}
