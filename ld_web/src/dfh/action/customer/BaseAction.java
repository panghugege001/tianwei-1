package dfh.action.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dfh.action.vo.ActivityCalendarVO;
import dfh.utils.*;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dfh.action.SubActionSupport;
import dfh.action.vo.EbetH5VO;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.NTErrorCode;
import dfh.remote.bean.EBetResp;

public class BaseAction extends SubActionSupport {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(SubActionSupport.class);
	
	private String kenoInfo;
	private String sportInfo;
	private String agUrl;
	private String agResult;
	private String ptUrl;
	private String gameCode;
	private Integer ptLogin;
	private ActivityCalendarVO actVo;
	
	private String sixLotteryUrl ;
	private String errorMsg ;
	
	private String payType;
	
	private String ebetLoginUrl;
	
	private String gpiLoginUrl;
	private int isfun;   //gpi是否试玩, 1 试玩模式   0：真实游戏

	private String demoMode; //是否试玩，true试玩，false真钱
	private String gameUrl;
	private String mobileKind;
	
	//#####live800信任信息
	private String userId;
	private String timestamp;
	private String hashCode;
	//#####live800信任信息
	private String type;
	private String practice;//png 0表示正式，1表示试玩
	private String game;//NTgameCode
	
	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}
	private String fromApp;
	
	public String getFromApp() {
		return fromApp;
	}

	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}
	
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public String getPractice() {
		return practice;
	}

	public void setPractice(String practice) {
		this.practice = practice;
	}

	/**
	 * ebet
	 * 
	 * @return
	 */
	public String gameEbetLogin() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null || user.getRole().equals("AGENT")) {
				return ERROR;
			}
			EBetResp ebetResp = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "ebetLogin", new Object[] { user.getLoginname(), getRequest().getServerName() }, EBetResp.class);
			// GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
			if (ebetResp != null) {
				if(ebetResp.getCode()==10000){
					ebetLoginUrl=ebetResp.getMsg();
					return SUCCESS;
				}
				System.out.println("登录EBET失败" + user.getLoginname()+"**********"+ebetResp.getMsg());
				return ERROR;
			} else {
				System.out.println("登录EBET失败" + user.getLoginname());
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String gameSixLottery() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏
			if (user == null) {
				return ERROR;
			}
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			String password = (String) getHttpSession().getAttribute(Constants.PT_SESSION_USER); //玩家密码
			sixLotteryUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "sixLotteryLogin", new Object[] { user.getLoginname() , password}, String.class);
			if(sixLotteryUrl.contains("http")){
				return SUCCESS;
			}else{
				setErrorMsg(sixLotteryUrl);
				return ERROR;
			}
		} catch (Exception e) {
			return ERROR;
		}
	}

	public String getActivityCalendarDetail() {
		String id = getRequest().getParameter("id");
		Integer idI = Integer.parseInt(id);
		List<ActivityCalendarVO> headnewsList = AxisSecurityEncryptUtil.queryTopActivity(Integer.MAX_VALUE);
		for (ActivityCalendarVO activityCalendarVO : headnewsList) {
			if (activityCalendarVO.getId().equals(idI)) {
				actVo = activityCalendarVO;
				return INPUT;
			}
		}
		return INPUT;
	}

	/**
	 * ebet
	 * 
	 * @return
	 */
	public String gameEbetLoginNew() {
		try {
			
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null || user.getRole().equals("AGENT")) {
				return ERROR;
			}
			
			EbetH5VO ebetH5VO = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getEBetVO", new Object[] {user.getLoginname() }, EbetH5VO.class);
			if(ebetH5VO == null){
				return ERROR;
			}
			this.getHttpSession().setAttribute("channelId", ebetH5VO.getChannelId());
			this.getHttpSession().setAttribute("accessToken", ebetH5VO.getAccessToken());
			log.info("loginname:" + user.getLoginname() + ",gameEbetLoginNew success , channelId" + ebetH5VO.getAccessToken() + ", accessToken:" + ebetH5VO.getChannelId());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * 访问代理管理页面
	 * 
	 * @return
	 */
	public String agentManage() {
		Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			return ERROR;
		}
		// 用户不能登录
		if (user.getRole().equals("MONEY_CUSTOMER")) {
			return ERROR;
		}
		//查询代理的老虎机佣金额度
		try {
			Userstatus userstatus = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getAgentSlot", new Object[] { user.getLoginname() }, Userstatus.class);
			if(userstatus.getSlotaccount() == null){
				userstatus.setSlotaccount(0.0);
			}
			this.getHttpSession().setAttribute("slotAccount", userstatus.getSlotaccount());
			
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
					AxisUtil.NAMESPACE, "agentMonthlyReport", new Object[] {user.getLoginname()}, String.class);
			Gson gson = new Gson();
			HashMap<String, String> valmap = gson.fromJson(result, new TypeToken<HashMap<String, String>>(){}.getType());
			getRequest().setAttribute("report", valmap);
			
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}


	/**
	 * 登录体育游戏
	 * 
	 * @return
	 */
	public String gameSport() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏 ---   改为未登录也可以进入游戏
			if (user == null) {
				sportInfo = "http://sb.e68ph.net/Sportsbook/Launch?t=&l=&g=CHS&tz=GMT%2B08%3A00&mid=F5623D1F57F64952iofWkynFF%2BDRdxFz78p8Vw%3D%3D" ;
				return SUCCESS;
			}
			// 代理不能登录邮箱
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			// 获取游戏链接地址
			sportInfo = SportBookUtils.getSportBookUrl(user.getPostcode(), user.getLoginname());
			if (sportInfo == null || sportInfo.equals("")) {
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}

	/**
	 * 登录游戏验证
	 * 
	 * @return
	 */
	public String gameLogin() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏
			if (user == null) {
				return ERROR;
			}
			// 代理不能登录
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			System.out.println("==================================================");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public String gamePtPlay() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏
			if (user == null) {
				return ERROR;
			}
			// 代理不能登录游戏
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			if (user == null || gameCode == null || "".equals(gameCode)) {
				return ERROR;
			} 
			//判断pt是否在线
			Boolean ptOnline= AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getPlayerOnlineInfo", new Object[] { user.getLoginname()}, Boolean.class);
			//Boolean ptOnline = PtUtil.getPlayerOnlineInfo(user.getLoginname());
			if (ptOnline) {
				Integer ptId=(Integer)this.getHttpSession().getAttribute(Constants.PT_SESSION_ID);
				if(ptId!=null){
					this.getHttpSession().removeAttribute(Constants.PT_SESSION_ID);
				    this.getHttpSession().setAttribute(Constants.PT_SESSION_ID, 1);
				}
			} else {
				this.getHttpSession().removeAttribute(Constants.PT_SESSION_ID);
				this.getHttpSession().setAttribute(Constants.PT_SESSION_ID, 0);
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	public String loginGamePt(){
		if(ptLogin!=null){
			this.getHttpSession().removeAttribute(Constants.PT_SESSION_ID);
			this.getHttpSession().setAttribute(Constants.PT_SESSION_ID, ptLogin);
			GsonUtil.GsonObject("SUCCESS");
		}else{
			GsonUtil.GsonObject("ERROR");
		}
		return null;
	}
	
	public String gameQT() {
		try {
			String domain = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "");
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null && isfun != 1) {
				return ERROR;
			}
			if (user != null && user.getRole().equals("AGENT")) {
				return ERROR;
			}
			if(StringUtils.isEmpty(gameCode)){
				return ERROR;
			}
			// 获取游戏链接地址
			//String isflash = "1";	//1为flash，否则html5 1108 modify by jalen 改成前端传入的type
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "qtGameUrl", new Object[] { isfun==1?"DEMOPLAY":user.getLoginname(), gameCode, isfun, type, domain}, String.class);
			if (gameUrl == null || "".equals(gameUrl) || "FAIL".equals(gameUrl)) {
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	
	
	public String gameQTForTp() {
		try {
			String domain = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "");
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null && isfun != 1) {
				return ERROR;
			}
			if (user != null && user.getRole().equals("AGENT")) {
				return ERROR;
			}
			if(StringUtils.isEmpty(gameCode)){
				return ERROR;
			}
			
			// 获取游戏链接地址
			String isflash = "1";	//1为flash，否则html5
			
			
			if(StringUtils.isNotBlank(this.getFromApp())){
				
				isflash = "0";
			}
			
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "qtGameUrlForTp", new Object[] { isfun==1?"DEMOPLAY":user.getLoginname(), gameCode, isfun, isflash, domain}, String.class);
			if (gameUrl == null || "".equals(gameUrl) || "FAIL".equals(gameUrl)) {
				return ERROR;
			}
			
			if(StringUtils.isNotBlank(this.getFromApp())){
				
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("url", gameUrl);
				GsonUtil.GsonObject(toResultJson("",data,true));
				return null;
			}
			
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}	
	
	
	/**
	 * 登入GPI
	 * @return
	 */
	public String gameGPI() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				return ERROR;
			}
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			if(StringUtils.isEmpty(gameCode)){
				return ERROR;
			}
			// 获取游戏链接地址
			gpiLoginUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "gpiLogin", new Object[] { user.getLoginname(), gameCode, isfun}, String.class);
			if (gpiLoginUrl == null || gpiLoginUrl.equals("")) {
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	/**
	 * GPI PNG、BS、CTXM登入
	 * @return
	 */
	public String gamePNGOfGPI(){
		Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			return ERROR;
		}
		if (user.getRole().equals("AGENT")) {
			return ERROR;
		}
		if(StringUtils.isEmpty(gameCode)){
			return ERROR;
		}
		try {
			// 获取游戏链接地址
			gpiLoginUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "gpiOtherLogin", new Object[] { user.getLoginname(), gameCode, isfun, type}, String.class);
			if (gpiLoginUrl == null || gpiLoginUrl.equals("")) {
				return ERROR;
			}
			if(type.equalsIgnoreCase("png")){
				return "PNG";
			}else{
				return SUCCESS;
			}
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	/**
	 * 登入GPI Mobile
	 * @return
	 */
	public String gameGPIMobile() {
		String domain = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "");
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				return ERROR;
			}
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			if(StringUtils.isEmpty(gameCode)){
				return ERROR;
			}
			// 获取游戏链接地址
			gpiLoginUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "gpiMobileLogin", new Object[] { user.getLoginname(), gameCode, isfun, type, domain}, String.class);
			if (gpiLoginUrl == null || gpiLoginUrl.equals("")) {
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	public String loginGameJc(){
		try {
			//TODO 禁止用户登录JC, 直接跳回首页
			return ERROR;
			/*Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
			}
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			String uuid = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "loginJc", new Object[] { user.getLoginname(), user.getPassword() }, String.class);
			if (StringUtils.isNotEmpty(uuid)){
				this.getHttpSession().setAttribute(Constants.JC_LOGINID, uuid);
				return SUCCESS;
			}
			return ERROR;*/
		} catch (Exception e) {
			return ERROR;
		}
	}
	

	/**
	 * 登录NT游戏
	 * @return
	 */
	public String loginGameNT(){
		String res = ERROR;
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				return ERROR;
			}
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			String sion = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "loginGWNT", new Object[] { user.getLoginname(), this.game }, String.class);
			if (StringUtils.isNotEmpty(sion)){
				JSONObject json = JSONObject.fromObject(sion); //返回值sion为json字符串,方便做判断与提示信息
				if (json.getBoolean("result")){
					String key = json.getString("key");
					getRequest().getSession().setAttribute(Constants.NT_SESSION, key);
					res = SUCCESS;
				} else {
					String msg = NTErrorCode.compare(json.getString("error"));
					log.error("loginGameNT错误, 错误消息: "+msg);
				}
				return res;
			}
		} catch (Exception e) {
			log.error("loginGameNT error:",e);
		}
		return res;
	}
	
	/**
	 * 获取玩家一段时间内的NT游戏投注记录
	 * @return
	 */
	public String gameNTRecord(){
		String res=ERROR;
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (null==user){
				return ERROR;
			}
			String fws=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "loginNT", new Object[] { user.getId() }, String.class);
			JSONObject record = JSONObject.fromObject(fws);
			if (null!=record && record.getBoolean("result")){
				
				res=SUCCESS;
			} else {
				res=ERROR;
			}
		} catch (Exception e) {
			log.error("gameNTRecord error:",e);
		}
		return res;
	}

	
	@SuppressWarnings("unchecked")
	public String playerInfo4Live800(){
		log.info("########################live800 获取信任信息");
		if(StringUtils.isEmpty(userId)){
			return "error";
		}
		try {
			String tmpHashCode = Live800Encode.getMD5Encode(userId + timestamp + Constants.LIVE800KEY);
			if(!tmpHashCode.equals(hashCode)){
				log.error("live信任信息hashcode检查未通过");
				return "error";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return "error";
		}
		String result;
		try {
			result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getUserInfo4Live800", new Object[] {userId}, String.class);
		} catch (AxisFault e) {
			log.error(e.getMessage());
			return "error";
		}
		JSONObject jsonObj = JSONObject.fromObject(result);
		//账号信息
		String playerStr = jsonObj.getString("playerInfo");
		Users player = (Users) JSONObject.toBean(JSONObject.fromObject(playerStr), Users.class);
		getRequest().setAttribute("playerInfo", player);
		List recordList = new ArrayList();
		//在线支付
		String payOrderStr = jsonObj.getString("payOrder");
		List payOrderList = (List) JSONSerializer.toJava(JSONSerializer.toJSON(payOrderStr));
		for (Object object : payOrderList) {
			 JSONObject jsonObject = JSONObject.fromObject(object);  
			 recordList.add((Payorder) JSONObject.toBean(jsonObject, Payorder.class));
		}
		getRequest().setAttribute("payOrder", recordList);
		
		//网银存款
		recordList = new ArrayList();
		String depositStr = jsonObj.getString("deposit");
		List depositList = (List) JSONSerializer.toJava(JSONSerializer.toJSON(depositStr));
		for (Object object : depositList) {
			 JSONObject jsonObject = JSONObject.fromObject(object);  
			 recordList.add((Proposal) JSONObject.toBean(jsonObject, Proposal.class));
		}
		getRequest().setAttribute("deposit", recordList);
		
		//提款
		recordList = new ArrayList();
		String withdrawalStr = jsonObj.getString("withdrawal");
		List withdrawalList = (List) JSONSerializer.toJava(JSONSerializer.toJSON(withdrawalStr));
		for (Object object : withdrawalList) {
			 JSONObject jsonObject = JSONObject.fromObject(object);  
			 recordList.add((Proposal) JSONObject.toBean(jsonObject, Proposal.class));
		}
		getRequest().setAttribute("withdrawal", recordList);
		
		//优惠
		recordList = new ArrayList();
		String couponStr = jsonObj.getString("coupon");
		List couponList = (List) JSONSerializer.toJava(JSONSerializer.toJSON(couponStr));
		for (Object object : couponList) {
			 JSONObject jsonObject = JSONObject.fromObject(object);  
			 recordList.add((Proposal) JSONObject.toBean(jsonObject, Proposal.class));
		}
		getRequest().setAttribute("coupon", recordList);
		
		//转账记录
		recordList = new ArrayList();
		String transferStr = jsonObj.getString("transfer");
		List transferList = (List) JSONSerializer.toJava(JSONSerializer.toJSON(transferStr));
		for (Object object : transferList) {
			 JSONObject jsonObject = JSONObject.fromObject(object);  
			 recordList.add((Transfer) JSONObject.toBean(jsonObject, Transfer.class));
		}
		getRequest().setAttribute("transfer", recordList);
		return SUCCESS;
	}
	
	/**
	 * 查询代理每月汇总数据(已移至上方agentManage中处理)
	 * @return
	 */
	public String agentMonthlyReport(){
		Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null || user.getRole().equals("AGENT")) {
			return ERROR;
		}
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getAgentMonthlyReport", new Object[] {user.getLoginname()}, String.class);
			Gson gson = new Gson();
			HashMap<String, String> valmap = gson.fromJson(result, new TypeToken<HashMap<String, String>>(){}.getType());
			getRequest().setAttribute("report", valmap);
		} catch (Exception e) {
			log.error("agentMonthlyReport : ", e);
		} finally{
			
		}
		return SUCCESS;
	}
	
	/*
	public String gamePtPlay() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏
			if (user == null) {
				return ERROR;
			}
			// 代理不能登录邮箱
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			if (user != null && gameCode != null && !"".equals(gameCode)) {
				String loginString = SkyUtils.loginSkyGame(user.getId());
				JSONObject jsonObj = JSONObject.fromObject(loginString);
				try {
					if (jsonObj.containsKey("error")) {
						Integer error = jsonObj.getInt("error");
						if (error == 1) {
							agResult = "参数错误";
						} else if (error == 2) {
							agResult = "货币错误";
						} else if (error == 3) {
							agResult = "token或secret key 有误";
						} else if (error == 4) {
							agResult = "jackpot group id错误";
						} else if (error == 5) {
							agResult = "会员id错误";
						} else if (error == 6) {
							agResult = "用户名不能进入游戏";
						} else if (error == 7) {
							agResult = "资金不足";
						} else if (error == 8) {
							agResult = "日期错误";
						}
					} else if (jsonObj.containsKey("key")) {
						String key = jsonObj.getString("key");
						if (key != null && !"".equals(key)) {
							ptUrl = "http://115.28.78.88/zh-cn1/client/?game=" + gameCode + "&key=" + key;
						} else {
							agResult = "获取登录游戏key失败！";
						}
					}
				} catch (Exception e) {
					agResult = "网络繁忙！请稍后再试！";
				}
			} else {
				agResult = "请选择游戏！";
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}*/
	
	/**
	 * 登录游戏验证
	 * 
	 * @return
	 */
	public String updatePassword() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏
			if (user == null) {
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	public String gameNTwoAppRedirect() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			String loginName = "";
			if (user != null && !user.getRole().equals("AGENT")) {
				loginName = user.getLoginname();
			}
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "nTwoAppLoginUrl", new Object[] {loginName}, String.class);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String gameNTwoRedirect() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			String loginName = "";
			if (user != null && !user.getRole().equals("AGENT")) {
				loginName = user.getLoginname();
			}
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "nTwoSingleLoginUrl", new Object[] {loginName}, String.class);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * mg 登入
	 * @return
	 */
	public String gameMGS() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				return "MG";
			}
			if (user.getRole().equals("AGENT")) {
				return "MG";
			}
			if(StringUtils.isEmpty(gameCode)){
				return "MG";
			}
			String lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "") + "/slotGame.jsp?showtype=MGS";
			// 获取游戏链接地址
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "mgsLogin", new Object[] { user.getLoginname(),user.getPassword(), gameCode,demoMode,Utils.getIpAddr(),this.getHttpSession()}, String.class);
			if (gameUrl == null || gameUrl.equals("")) {
				return "MG";
			}
			return SUCCESS;
		} catch (Exception e) {
			return "MG";
		}
	}
	
	/**
	 * mg 桌面H5游戏登入
	 * @return
	 */
	public String gameMGS4H5Desktop() {
		String lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "");
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				return "MG";
			}
			if (user.getRole().equals("AGENT")) {
				return "MG";
			}
			if(StringUtils.isEmpty(gameCode)){
				return "MG";
			}
			// 获取游戏链接地址
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "mgsH5Login", new Object[] { user.getLoginname(),user.getPassword(), gameCode,demoMode,Utils.getIpAddr(),this.getHttpSession()}, String.class);
			if (gameUrl == null || gameUrl.equals("")) {
				return "MG";
			}
			getResponse().sendRedirect(gameUrl);
			return null;
		} catch (Exception e) {
			return "MG";
		}
	}
	
	/**
	 * 手机PNG登入
	 * @return
	 */
	public String gamePNGRedirect() {
		try {
			Users user = getCustomerFromSession();
			if (user == null||user.getRole().equals("AGENT")||StringUtils.isEmpty(this.gameCode)) {
				return ERROR;
			}
			int port = this.getRequest().getServerPort();
			String reloadUrl = this.getRequest().getScheme()+"://"+this.getRequest().getServerName() + ((port == 0 || port == 80 || port == 443)? "":":" + port);
			// 获取游戏链接地址
			this.gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getPNGMobileUrl", new Object[] { user.getLoginname(), this.gameCode, reloadUrl}, String.class);
			
			if (StringUtils.isNotBlank(this.gameUrl)) {
				return SUCCESS;
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String gamePNGFlash() {
		try {
			if(StringUtils.isEmpty(this.gameCode)){
				return ERROR;
			}
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user != null && "AGENT".equals(user.getRole())) {
				return ERROR;
			}
			
			if("0".equals(this.practice)){//正式
				if(user == null){
					return ERROR;
				}
				// 获取游戏链接地址
				this.gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getPNGFlashUrl", new Object[] {user.getLoginname(), gameCode}, String.class);
				if (StringUtils.isBlank(gameUrl)) {
					return ERROR;
				}
			} else {
				//试玩
				this.gameUrl = "https://bsicw.playngonetwork.com/Casino/js?div=pngCasinoGame&practice=1&lang=zh_CN&pid=292&gid=" + this.gameCode + "&width=100%&height=100%";
			}
			
			return SUCCESS;
		} catch (Exception e) {
			log.error("gamePNGFlash ERROR：", e);
			return ERROR;
		}
	}
	
	
public String gamePNGFlashForTp() {
		
		String rootUrl = "https://bsicw.playngonetwork.com";
		//String rootUrl = "https://bsistage.playngonetwork.com";
		
		try {
			if(StringUtils.isEmpty(this.gameCode)){
				return ERROR;
			}
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user != null && "AGENT".equals(user.getRole())) {
				return ERROR;
			}
			
			if("0".equals(this.practice)){//正式
				if(user == null){
					return ERROR;
				}
				// 获取游戏链接地址
				String result =  AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getPNGLoadingTicket", new Object[] {user.getLoginname(), gameCode}, String.class);
				
				if(StringUtils.isEmpty(result)){
					//GsonUtil.GsonObject(toResultJson("PNG游戏维护中",null,false));
					log.error("png获取ticket失败");
					return ERROR;
				}
				
				JSONObject resultObj = JSONObject.fromObject(result);
				
				if(!StringUtils.equals(resultObj.getString("code"),"10000")){
					
					log.error("png获取ticket异常");
					return ERROR;
				}
				
				
				Map<String, String> dataMap = (Map<String, String>) resultObj.get("data");
				
				
				
				if(StringUtils.isNotBlank(this.getFromApp())){
					
					int port = this.getRequest().getServerPort();
					String reloadUrl = this.getRequest().getScheme()+"://"+this.getRequest().getServerName() + ((port == 0 || port == 80 || port == 443)? "":":" + port);
					
					this.gameUrl = rootUrl + "/casino/PlayMobile" + "?pid=365" + "&gid=" + this.gameCode + "&lang=" + "zh_CN" + 
								"&practice=0&user=" + dataMap.get("ticket") + "&width=100%&height=100%" + "lobby=" + reloadUrl;
					Map<String,Object> data = new HashMap<String,Object>();
					data.put("url", this.gameUrl);
					GsonUtil.GsonObject(toResultJson("",data,true));
					return null;
				}
				
				this.gameUrl = rootUrl + "/Casino/js" + "?div=pngCasinoGame&pid=365" + "&gid=" + this.gameCode + "&lang=" + "zh_CN" + "&practice=0&username=" + dataMap.get("ticket") + "&width=100%&height=100%";
				
			} else {
				//试玩
				
				if(StringUtils.isNotBlank(this.getFromApp())){
					
					int port = this.getRequest().getServerPort();
					String reloadUrl = this.getRequest().getScheme()+"://"+this.getRequest().getServerName() + ((port == 0 || port == 80 || port == 443)? "":":" + port);
					
					this.gameUrl = rootUrl + "/casino/PlayMobile" + "?pid=365" + "&gid=" + this.gameCode + "&lang=" + "zh_CN" + 
								   "&practice=0&&width=100%&height=100%" + "lobby=" + reloadUrl;
					Map<String,Object> data = new HashMap<String,Object>();
					data.put("url", this.gameUrl);
					GsonUtil.GsonObject(toResultJson("",data,true));
					return null;
				}
				
			
				this.gameUrl = rootUrl + "/Casino/js?div=pngCasinoGame&practice=1&lang=zh_CN&pid=365&gid=" + this.gameCode + "&width=100%&height=100%";
			}
			
			return SUCCESS;
		} catch (Exception e) {
			log.error("gamePNGFlash ERROR：", e);
			return ERROR;
		}
	}	

	/**
	 * 
	 * @param message 訊息
	 * @param success 成功/失敗
	 * @return map
	 */
	private Object toResultJson(Object message,Object data,boolean success){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("success", success);
		result.put("message", message);
		result.put("data", data);
		return result;
	}
	
	public String indexGame(){
		return SUCCESS;
	}

	public String getPtUrl() {
		return ptUrl;
	}

	public void setPtUrl(String ptUrl) {
		this.ptUrl = ptUrl;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getKenoInfo() {
		return kenoInfo;
	}

	public void setKenoInfo(String kenoInfo) {
		this.kenoInfo = kenoInfo;
	}

	public String getSportInfo() {
		return sportInfo;
	}

	public void setSportInfo(String sportInfo) {
		this.sportInfo = sportInfo;
	}

	public String getAgUrl() {
		return agUrl;
	}

	public void setAgUrl(String agUrl) {
		this.agUrl = agUrl;
	}

	public String getAgResult() {
		return agResult;
	}

	public void setAgResult(String agResult) {
		this.agResult = agResult;
	}

	public Integer getPtLogin() {
		return ptLogin;
	}

	public void setPtLogin(Integer ptLogin) {
		this.ptLogin = ptLogin;
	}

	public String getSixLotteryUrl() {
		return sixLotteryUrl;
	}

	public void setSixLotteryUrl(String sixLotteryUrl) {
		this.sixLotteryUrl = sixLotteryUrl;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getEbetLoginUrl() {
		return ebetLoginUrl;
	}

	public void setEbetLoginUrl(String ebetLoginUrl) {
		this.ebetLoginUrl = ebetLoginUrl;
	}

	public String getGpiLoginUrl() {
		return gpiLoginUrl;
	}

	public void setGpiLoginUrl(String gpiLoginUrl) {
		this.gpiLoginUrl = gpiLoginUrl;
	}

	public int getIsfun() {
		return isfun;
	}

	public void setIsfun(int isfun) {
		this.isfun = isfun;
	}

	public String getGameUrl() {
		return gameUrl;
	}

	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getMobileKind() {
		return mobileKind;
	}

	public void setMobileKind(String mobileKind) {
		this.mobileKind = mobileKind;
	}

	public ActivityCalendarVO getActVo() {
		return actVo;
	}

	public void setActVo(ActivityCalendarVO actVo) {
		this.actVo = actVo;
	}
	
	public String getDemoMode() {
		return demoMode;
	}

	public void setDemoMode(String demoMode) {
		this.demoMode = demoMode;
	}
}
