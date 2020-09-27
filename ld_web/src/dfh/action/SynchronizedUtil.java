package dfh.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dfh.utils.*;
import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoXimaReturnVo;
import dfh.action.vo.AutoYouHuiVo;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.Proposal;
import dfh.model.QuestionStatus;
import dfh.model.Userbankinfo;
import dfh.model.Users;
import dfh.model.enums.UserRole;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.skydragon.webservice.model.LoginInfo;

/**
 * 处理同步工具类
 * 每处理一个同步功能，需要一个Obj对象，一个带同步块的方法，同步块获取obj对象的锁
 *  
 */
public class SynchronizedUtil {
	
	private static Logger log = Logger.getLogger(SynchronizedUtil.class);

	private static final SynchronizedUtil instance = new SynchronizedUtil();

	private static Map<String,String> youhuiTypeMap;
	static{
		youhuiTypeMap = new HashMap<String,String>();
		youhuiTypeMap.put("590","PT首存优惠");
		youhuiTypeMap.put("591","PT次存优惠");
		youhuiTypeMap.put("592","EA次存优惠");
		youhuiTypeMap.put("593","AG存送优惠");
		youhuiTypeMap.put("594","AGIN存送优惠");
		youhuiTypeMap.put("595","BBIN存送优惠");
		youhuiTypeMap.put("596","EBET首存优惠");
		youhuiTypeMap.put("597","EBET次存优惠");
		youhuiTypeMap.put("598","TTG首存优惠");
		youhuiTypeMap.put("599","TTG次存优惠");
		youhuiTypeMap.put("702","GPI首存优惠");
		youhuiTypeMap.put("703","GPI次存优惠");
		youhuiTypeMap.put("704","GPI限时优惠");
		youhuiTypeMap.put("705","PT限时优惠");
		youhuiTypeMap.put("706","TTG限时优惠");
		youhuiTypeMap.put("707","NT首存优惠");
		youhuiTypeMap.put("708","NT次存优惠");
		youhuiTypeMap.put("709","NT限时优惠");
		youhuiTypeMap.put("710","QT首存优惠");
		youhuiTypeMap.put("711","QT次存优惠");
		youhuiTypeMap.put("712","QT限时优惠");
		youhuiTypeMap.put("730","MG首存优惠");
		youhuiTypeMap.put("731","MG次存优惠");
		youhuiTypeMap.put("732","MG限时优惠");
		youhuiTypeMap.put("733","DT首存优惠");
		youhuiTypeMap.put("734","DT次存优惠");
		youhuiTypeMap.put("735","DT限时优惠");
	}
	
	/**
	 * 防止外部实例化
	 */
	private SynchronizedUtil(){};
	
	/**
	 * 单例
	 * @return
	 */
	public static SynchronizedUtil getInstance(){
		return instance;
	}
	
	//绑定密保
	private Object saveQuestionO = new Object();
	public String saveQuestion(String loginname , Integer questionid , String answer){
		synchronized (saveQuestionO) {
			try {
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "saveQuetion", new Object[] {loginname, questionid , answer }, String.class);
				return msg ;
			} catch (AxisFault e) {
				e.printStackTrace();
				return "绑定失败"+e.getMessage();
			}
		}
	}
	/**
	 * 检查签到转账的存款记录
	 * @param customer
	 * @throws AxisFault
	 */
	public String chekDepositOrderRecord(Users customer,Date start,Date end) throws AxisFault {
        String st=DateUtil.fmtDateForBetRecods(start);
		String en=DateUtil.fmtDateForBetRecods(end);
		String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {customer.getLoginname(),st,en}, String.class);
		double records=Double.valueOf(result);
		log.info("签到转账的存款记录:"+records);

		if("0".equals(customer.getLevel()+"")&&(records<50.00)){
			return "您当日存款还差"+(50.00-records)+"元可进行转账";
		}
		if("1".equals(customer.getLevel()+"")&&(records<100.00)){
			return "您当日存款还差"+(100.00-records)+"元可进行转账";
		}
		if("2".equals(customer.getLevel()+"")&&(records<300.00)){
			return "您当日存款还差"+(300.00-records)+"元可进行转账";
		}
		if("3".equals(customer.getLevel()+"")&&(records<500.00)){
			return "您当日存款还差"+(500.00-records)+"元可进行转账";
		}
		if("4".equals(customer.getLevel()+"")&&(records<700.00)){
			return "您当日存款还差"+(700.00-records)+"元可进行转账";
		}
		return "success";
	}
	//自助红包转入游戏
	private Object transferInforHBO = new Object();
	public String transferInforHB(String type, Double remit,Integer deposit) {
		synchronized (transferInforHBO) {
			if (remit < 10) {
				GsonUtil.GsonObject("转入金额不能小于10元！");
				return null;
			}
			try {
				Users customer = getCustomerFromSession();
				// 判断是否登录
				if (customer == null) {
					GsonUtil.GsonObject("尚未登录！请重新登录！");
					return null;
				}
				if (customer.getRole().equals(UserRole.AGENT.getCode())) {
					GsonUtil.GsonObject("[提示]代理不能转账");
					return null;
				}
				String msg = null;

				msg = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1
								+ "UserWebService", false), AxisUtil.NAMESPACE,
						"transferInforHB", new Object[] {
								getCustomerLoginname(), remit, type ,deposit},
						String.class);
				if (msg == null) {
					GsonUtil.GsonObject("转账成功");
					return null;
				} else {
					GsonUtil.GsonObject("提交失败:" + msg);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("提交失败:" + e.toString());
				return null;
			}
		}
	}
	/**
	 * 检查签到转账的存款记录
	 * @param customer
	 * @throws AxisFault
	 *//*
	public Object chekDepositOrderRecord(Users customer,Date start,Date end) throws AxisFault {

		String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {customer.getLoginname(),start,end}, String.class);
		double records=Double.valueOf(result);
		log.info("签到转账的存款记录:"+records);

		if("0".equals(customer.getLevel()+"")&&(records<50.00)){
			GsonUtil.GsonObject("您当日存款还差"+(50.00-records)+"元可进行转账");
			return false;
		}
		if("1".equals(customer.getLevel()+"")&&(records<100.00)){
			GsonUtil.GsonObject("您当日存款还差"+(100.00-records)+"元可进行转账");
			return false;
		}
		if("2".equals(customer.getLevel()+"")&&(records<300.00)){
			GsonUtil.GsonObject("您当日存款还差"+(300.00-records)+"元可进行转账");
			return false;
		}
		if("3".equals(customer.getLevel()+"")&&(records<500.00)){
			GsonUtil.GsonObject("您当日存款还差"+(500.00-records)+"元可进行转账");
			return false;
		}
		if("4".equals(customer.getLevel()+"")&&(records<700.00)){
			GsonUtil.GsonObject("您当日存款还差"+(700.00-records)+"元可进行转账");
			return false;
		}
		return true;
	}*/
	/**
	 * 注册
	 */
	private Object registerO = new Object();
	public LoginInfo register(String howToKnow, Integer sms, String loginname, String pwd, String accountName, 
			String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress,
			String city, String birthday, String intro, Proposal proposal,String ioBB,String agentcode
			,String friendcode) {
		synchronized (registerO) {
			LoginInfo info = null;
			try {
				info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
						AxisUtil.NAMESPACE, "register", new Object[] { 
					howToKnow, sms, loginname, pwd,
					accountName, aliasName, phone,
					email, qq, referWebsite, 
					ipaddress, city, birthday, intro, proposal ,ioBB , agentcode,friendcode}, LoginInfo.class);	
			} catch (AxisFault e) {
				e.printStackTrace();
			}
			
			return info;
		}
	}
	
	/**
	 * 手机端转账
	 */
	//private Object mobileTransferO = new Object();
	public String mobileTransfer(String transferGameOut,String transferGameIn,String money){
		// 判断转出账户
		if (transferGameOut == null || transferGameOut.equals("") || transferGameOut.length() <= 0) {
			GsonUtil.GsonObject(toResultJson("[提示]来源账号不能为空！",false));
			return null;
		}
		// 判断转入账户
		if (transferGameIn == null || transferGameIn.equals("") || transferGameIn.length() <= 0) {
			GsonUtil.GsonObject(toResultJson("[提示]目标账号不能为空！",false));
			return null;
		}
		if(transferGameOut.equals(transferGameIn)){
			GsonUtil.GsonObject(toResultJson("[提示]相同帳戶不能互相轉帳！",false));
			return null;
		}
		if(!transferGameOut.equals("ld")&&!transferGameIn.equals("ld")&&!transferGameOut.equals("qd")){
			GsonUtil.GsonObject(toResultJson("[提示]游戏之间不能对转！",false));
			return null;
		}
		if (StringUtil.isBlank(money)){
			GsonUtil.GsonObject(toResultJson("[提示]转账金额不可为空！",false));
		    return null;
		}
		if (!NumberUtils.isNumber(money)) {
			GsonUtil.GsonObject(toResultJson("[提示]转账金额必須為數字！",false));
		    return null;
		}
		// 判断游戏是否为数字
		if (Double.parseDouble(money)<= 0) {
			GsonUtil.GsonObject(toResultJson("[提示]转账金额不能小于等于0元！",false));
			return null;
		}
		
		if(!money.matches("^\\d*[1-9]\\d*$")){ 
			GsonUtil.GsonObject(toResultJson("[提示]转账金额必须为整数！",false));
			return null ;
		}
		if(transferGameOut.equals("qd")&&transferGameIn.equals("ld")){
			GsonUtil.GsonObject(toResultJson("[提示]不能转账，签到余额不能转到主账户！",false));
			return null;
		}
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			// 判断是否登录
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
				return null;
			}
			if (customer.getRole().equals(UserRole.AGENT.getCode())) {
				GsonUtil.GsonObject(toResultJson("[提示]代理不能转账",false));
				return null;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			GsonUtil.GsonObject(toResultJson("转账失败",false));
			return null;
		}

		synchronized (customer.getId()) {
			try {
				String msg = null;
				// e68账号转游戏账号
				if (transferGameOut.equals("ld")) {
					if (transferGameIn.equals("ea")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferIn", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameIn.equals("ag")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforDsp", new Object[] { getCustomerLoginname(),money }, String.class);
					} else if (transferGameIn.equals("agin")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforAginDsp", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameIn.equals("bbin")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforBbin", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameIn.equals("keno")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforkeno", new Object[] { getCustomerLoginname(), money, getIp() }, String.class);
					} else if (transferGameIn.equals("keno2")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforkeno2", new Object[] { getCustomerLoginname(), money, getIp() }, String.class);
					} else if (transferGameIn.equals("sba")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforSbaTy", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameIn.equals("newpt")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferNewPtIn", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameIn.equals("sixlottery")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInSixLottery", new Object[] { getCustomerLoginname(), money ,getIp() }, String.class);
					} else if (transferGameIn.equals("ebet")){
						Integer ebetRemit=0;
						try {
							ebetRemit = Double.valueOf(money).intValue();
						} catch (Exception e) {
							e.printStackTrace();
							GsonUtil.GsonObject(toResultJson("提交失败:金额格式不正确！",false));
							return null;
						}
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "IN"}, String.class);
					} else if (transferGameIn.equals("ttg")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferTtIn", new Object[] { getCustomerLoginname(), money ,getIp() }, String.class);
					} else if (transferGameIn.equals("jc")) {
						//TODO 时时彩下架, 限制转入
						msg = "尊敬的玩家您好, E68因发展需要即将下架时时彩, 请您尽快转出时时彩金额!!";
					} else if(transferGameIn.equals("qt")){
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferQtIn", new Object[] {getCustomerLoginname(), money}, String.class);
					} else if (transferGameIn.equals("gpi")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforGPI", new Object[] { getCustomerLoginname(), money ,getIp() }, String.class);
					} else if (transferGameIn.equals("nt")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferToNT", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameIn.equals("n2live")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInNTwo", new Object[] { getCustomerLoginname(), money }, String.class); 
					} else if (transferGameIn.equals("ebetapp")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferToEBetApp", new Object[]{getCustomerLoginname(), money , "IN"}, String.class);
					} else if (transferGameIn.equals("dt")) {    
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferDtIn", new Object[]{getCustomerLoginname(), money}, String.class);
					} else if (transferGameIn.equals("mg")) {    
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferToMG", new Object[]{getCustomerLoginname(), money}, String.class);
					} else if (transferGameIn.equals("png")) {   
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferToPNG", new Object[]{getCustomerLoginname(), money}, String.class);
					} else {
						msg = "无该平台！";
					}
				}else if(transferGameOut.equals("qd")){//签到
					Date now = new Date();
					String msg_c = chekDepositOrderRecord(customer, DateUtil.getDateBegin(now),DateUtil.getDateEnd(now));//检查签到转账的存款记录
					/*if(!b.toString().equals("true")){
						GsonUtil.GsonObject(toResultJson(b,false));
					}*/
					if(!"success".equals(msg_c)){
						GsonUtil.GsonObject(toResultJson("提交失败:" + msg_c,false));
						return null;
					}
					msg=SynchronizedUtil.getInstance().transferInforSignM(transferGameIn,Integer.parseInt(money));


				}
				else {
					if (transferGameOut.equals("ea")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOut", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameOut.equals("ag")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOutforDsp", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameOut.equals("agin")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOutforAginDsp", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameOut.equals("bbin")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOutforBbin", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameOut.equals("keno")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOutforkeno", new Object[] { getCustomerLoginname(), money, getIp() }, String.class);
					} else if (transferGameOut.equals("keno2")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOutforkeno2", new Object[] { getCustomerLoginname(), money, getIp() }, String.class);
					} else if (transferGameOut.equals("sba")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOutforSbaTy", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameOut.equals("newpt")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferNewPtOut", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if(transferGameOut.equals("sixlottery")){
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferOutSixLottery", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if(transferGameOut.equals("ebet")){
						Integer ebetRemit=0;
						try {
							ebetRemit = Double.valueOf(money).intValue();
						} catch (Exception e) {
							e.printStackTrace();
							GsonUtil.GsonObject(toResultJson("提交失败:金额格式不正确！",false));
							return null;
						}
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), 
								AxisUtil.NAMESPACE, "ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "OUT"}, String.class);
					} else if (transferGameOut.equals("ttg")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferTtOut", new Object[] { getCustomerLoginname(), money ,getIp() }, String.class);
					} else if (transferGameOut.equals("jc")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"transferFromJc", new Object[] { getCustomerLoginname(), money ,getIp() }, String.class);
					} else if(transferGameOut.equals("qt")){
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferQtOut", new Object[] {getCustomerLoginname(), money}, String.class);
					} else if (transferGameOut.equals("gpi")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOut4GPI", new Object[] { getCustomerLoginname(), money ,getIp() }, String.class);
					} else if (transferGameOut.equals("nt")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromNT", new Object[] { getCustomerLoginname(), money }, String.class);
					} else if (transferGameOut.equals("n2live")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutNTwo", new Object[] {getCustomerLoginname(), money}, String.class);
					} else if (transferGameOut.equals("ebetapp")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromEBetApp", new Object[]{getCustomerLoginname(), money}, String.class);
					}else if  (transferGameOut.equals("dt")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferDtOut", new Object[]{getCustomerLoginname(), money}, String.class);
					} else if  (transferGameOut.equals("mg")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromMG", new Object[]{getCustomerLoginname(), money}, String.class);
					} else if (transferGameOut.equals("png")) {
						msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromPNG", new Object[] { getCustomerLoginname(), money }, String.class);
					} else {
						msg = "无该平台！";
					}
				}
				if (msg == null || msg.contains("成功")) {
					refreshUserInSession();
					GsonUtil.GsonObject(toResultJson("转账成功",true));
				} else {
					GsonUtil.GsonObject(toResultJson("提交失败:"+msg,false));
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject(toResultJson("转账失败",false));
			}
			return null;
		}
	}
	
	
	/**
	 * 网页端转账 转入
	 */

	private Object transferInO = new Object();
	public String transferIn(Double remit,String gameType){
		synchronized (transferInO) {
			String msg = null;
			try {
				Users customer = getCustomerFromSession();
				if (customer == null) {
					GsonUtil.GsonObject("请登录后在进行操作");
					return null ;
				}
				if (remit == null) {
					GsonUtil.GsonObject("转账金额不可为空");
					return null ;
				}
				if (remit <= 0) {
					GsonUtil.GsonObject("请重新输入转账金额");
					return null ;
				}
				if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
					GsonUtil.GsonObject("转账金额必须为整数！");
					return null ;
				}
				if(gameType.equals("ea")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, 
							"transferIn", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("ag")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferInforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("agin")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, 
							"transferInforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("keno")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferInforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
				}else if(gameType.equals("keno2")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferInforkeno2", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
				}else if(gameType.equals("sb")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferInforSB", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("pt")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferNewPtIn", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("bbin")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferInforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("sixlottery")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferInSixLottery", new Object[] { getCustomerLoginname(), remit ,getIp() }, String.class);
				}else if(gameType.equals("ebet")){
					Integer ebetRemit=0;
					try {
						ebetRemit = remit.intValue();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
//						System.out.println(e.toString());
						GsonUtil.GsonObject("提交失败:金额格式不正确！");
						return null;
					}
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "IN"}, String.class);
				}else if(gameType.equals("ttg")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), 
							AxisUtil.NAMESPACE, "transferTtIn", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("jc")){
					//TODO 时时彩下架, 限制转入
					msg = "尊敬的玩家您好, E68因发展需要即将下架时时彩, 请您尽快转出时时彩金额!!";
					/*msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferToJc", new Object[] {getCustomerLoginname(), remit}, String.class);*/
				}else if(gameType.equals("gpi")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferInforGPI", new Object[] { getCustomerLoginname(), remit }, String.class);
				}
				refreshUserInSession();
			} catch (Exception e) {
				e.printStackTrace();
				msg = "服务异常";
			}
			if (msg == null) {
				GsonUtil.GsonObject("转账成功");
			} else {
				GsonUtil.GsonObject("提交失败:" + msg);
			}
			return null;
		
		}
	
	}
	
	
	
	/**
	 * 转出 转账--
	 */
	private Object transferOutO = new Object();
	public String transferOut(Double remit,String gameType){
		synchronized (transferOutO) {
			String msg = null;
			try {
				Users customer = getCustomerFromSession();
				if (customer == null) {
					GsonUtil.GsonObject("请登录后在进行操作");
					return null ;
				}
				if (remit == null) {
					GsonUtil.GsonObject("转账金额不可为空");
					return null ;
				}
				if (remit <= 0) {
					GsonUtil.GsonObject("请重新输入转账金额");
					return null ;
				}
				if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
					GsonUtil.GsonObject("转账金额必须为整数！");
					return null ;
				}
				if(gameType.equals("ea")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOut", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("ag")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOutforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("agin")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOutforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("keno")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOutforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
				}else if(gameType.equals("keno2")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOutforkeno2", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
				}else if(gameType.equals("bbin")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOutforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("sb")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOutforSB", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("pt")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferNewPtOut", new Object[] { getCustomerLoginname(), remit }, String.class);
				}else if(gameType.equals("sixlottery")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOutSixLottery", new Object[] { getCustomerLoginname(), remit ,getIp() }, String.class);
				}else if(gameType.equals("ebet")){
					Integer ebetRemit=0;
					try {
						ebetRemit = remit.intValue();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
//						System.out.println(e.toString());
						GsonUtil.GsonObject("提交失败:金额格式不正确！");
						return null;
					}
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "OUT"}, String.class);

				}else if(gameType.equals("ttg")){
				    msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), 
							AxisUtil.NAMESPACE, "transferTtOut", new Object[] { getCustomerLoginname(), remit }, String.class);

				}else if(gameType.equals("jc")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferFromJc", new Object[] {getCustomerLoginname(), remit}, String.class);
				}else if(gameType.equals("gpi")){
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"transferOut4GPI", new Object[] { getCustomerLoginname(), remit }, String.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "服务异常";
			}

			if (msg == null) {
				GsonUtil.GsonObject("转账成功");
				try {
					refreshUserInSession();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				GsonUtil.GsonObject("提交失败:" + msg);
			}
			return null;
		
		}
	
	}
	
	
	
	
	/**
	 * 户内转账
	 */
	//private Object updateGameMoneyO = new Object();
	public String updateGameMoney(Double remit){
		String transferGameOut = getRequest().getParameter("transferGameOut");//来源账户
		String transferGameIn = getRequest().getParameter("transferGameIn");//目标账户
		String transferGameMoney = getRequest().getParameter("transferGameMoney");
		// 判断转出账户
		if (transferGameOut == null || transferGameOut.equals("") || transferGameOut.length() <= 0) {
			GsonUtil.GsonObject("来源账号不能为空！");
			return null;
		}
		// 判断转入账户
		if (transferGameIn == null || transferGameIn.equals("") || transferGameIn.length() <= 0) {
			GsonUtil.GsonObject("目标账号不能为空！");
			return null;
		}
		// 判断游戏金额
		if (transferGameMoney == null || transferGameMoney.equals("") || transferGameMoney.length() <= 0) {
			GsonUtil.GsonObject("转账金额不能为空！");
			return null;
		}
		if(!transferGameMoney.matches("^\\d*[1-9]\\d*$")){ 
			GsonUtil.GsonObject("转账金额必须为整数！");
			return null ;
		}
		// 判断游戏是否为数字
		remit = Double.parseDouble(transferGameMoney);
		if (remit <= 0.0) {
			GsonUtil.GsonObject("转账金额不能小于等于0元！");
			return null;
		}
		if (transferGameOut.equals("self") && transferGameIn.equals("self")) {
			GsonUtil.GsonObject("龙都账户不能转账到龙都账户！");
			return null;
		}
		
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			// 判断是否登录
			if (customer == null) {
				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if (customer.getRole().equals(UserRole.AGENT.getCode())) {
				GsonUtil.GsonObject("代理不能转账");
				return null;
			}
		} catch (Exception e1) {
			GsonUtil.GsonObject("服务异常");
			return null;
		}

		synchronized (customer.getId()) {
			try {
				if (transferGameOut.equals("self") || transferGameIn.equals("self")) {
					// 龙都账号转游戏账号
					if (transferGameOut.equals("self")) {
						// 龙都转ea
						if (transferGameIn.equals("ea")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferIn", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("ag")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("agin")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("bbin")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("keno")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("keno2")) {
							String 	msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferInforkeno2", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						}else if (transferGameIn.equals("sba")) {
							String  msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferInforSbaTy", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("dt")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferDtIn", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("newpt")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferNewPtIn", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						}else if (transferGameIn.equals("sixlottery")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInSixLottery", new Object[] { getCustomerLoginname(), remit ,getIp() }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						}else if(transferGameIn.equals("ebet")){
							Integer ebetRemit=0;
							try {
								ebetRemit = remit.intValue();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
//								System.out.println(e.toString());
								GsonUtil.GsonObject("提交失败:金额格式不正确！");
								return null;
							}
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "IN"}, String.class);
							refreshUserInSession();
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;

						}else if (transferGameIn.equals("ttg")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferTtIn", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if(transferGameIn.equals("jc")){
							//TODO 暂时关闭时时彩,不允许转入
							refreshUserInSession();
							GsonUtil.GsonObject("尊敬的玩家您好, E68因发展需要即将下架时时彩, 请您尽快转出时时彩金额!!");
							/*String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), 
									AxisUtil.NAMESPACE, "transferToJc", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}*/
							return null;
						} else if (transferGameIn.equals("gpi")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforGPI", new Object[] { getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("qt")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferQtIn", new Object[] { getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.endsWith("nt")){ //nt老虎机转账
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferToNT", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if(transferGameIn.equals("mg")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferToMG", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("n2live")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInNTwo", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameIn.equals("ebetapp")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferToEBetApp", new Object[]{getCustomerLoginname(), remit , "IN"}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if(transferGameIn.equals("png")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferToPNG", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						}else {
							GsonUtil.GsonObject("无该平台！");
							return null;
						}
					} else {
						if (transferGameOut.equals("ea")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOut", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("ag")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("agin")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("bbin")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("keno")) {
							String 	msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferOutforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("keno2")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferOutforkeno2", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						}else if (transferGameOut.equals("sba")) {
							String 	msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferOutforSbaTy", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("newpt")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferNewPtOut", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						}else if(transferGameOut.equals("sixlottery")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutSixLottery", new Object[] { getCustomerLoginname(), remit ,getIp() }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						
						}else if(transferGameOut.equals("ebet")){
							Integer ebetRemit=0;
							try {
								ebetRemit = remit.intValue();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								GsonUtil.GsonObject("提交失败:金额格式不正确！");
								return null;
							}
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "OUT"}, String.class);
							refreshUserInSession();
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						}else if (transferGameOut.equals("ttg")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferTtOut", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;

						} else if(transferGameOut.equals("jc")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromJc", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if(transferGameOut.equals("gpi")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOut4GPI", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("qt")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferQtOut", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("dt")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferDtOut", new Object[] { getCustomerLoginname(), remit }, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if(transferGameOut.equals("mg")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromMG", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.endsWith("nt")){ //nt老虎机转账
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromNT", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("n2live")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutNTwo", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if (transferGameOut.equals("ebetapp")) {
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferFromEBetApp", new Object[]{getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else if(transferGameOut.equals("png")){
							String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromPNG", new Object[] {getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								refreshUserInSession();
								GsonUtil.GsonObject("转账成功");
							} else {
								GsonUtil.GsonObject("提交失败:" + msg);
							}
							return null;
						} else {
							GsonUtil.GsonObject("无该平台！");
							return null;
						}
					}
				} else {
					GsonUtil.GsonObject("游戏之间不能对转！");
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("服务异常");
				return null;
			}
		}
	
	}

	/**
	 * 
	 * @param message 訊息
	 * @param data 资料
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
	
	
	//全民闯关转入游戏
	private Object transferInforEmigratedO = new Object();
	public String transferInforEmigrated(String type , Double remit){
		synchronized (transferInforEmigratedO) {
			if(remit<10){
				GsonUtil.GsonObject("存款金额不能小于10元！");
				return null;
			}
			try {
				Users customer = getCustomerFromSession();
				// 判断是否登录
				if (customer == null) {
					GsonUtil.GsonObject("尚未登录！请重新登录！");
					return null ;
				}
				if (customer.getRole().equals(UserRole.AGENT.getCode())) {
					GsonUtil.GsonObject("[提示]代理不能转账");
					return null ;
				}
				String msg = null;
		
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
						AxisUtil.NAMESPACE, "transferInforEmigrated", new Object[] { getCustomerLoginname(), remit,type}, String.class);
				if (msg == null) {
					GsonUtil.GsonObject("转账成功");
					return null ;
				} else {
					GsonUtil.GsonObject("提交失败:"+msg);
					return null ;
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("提交失败:"+e.toString());
				return null ;
			}
		}
		}
	

	
	/**
	 * 手机
	 * 全民闯关转入游戏
	 */
	private Object transferInforEmigratedMO = new Object();
	public void transferInforEmigratedM(String type , Double remit){
		try {
			Users user = getCustomerFromSession();
			// 判断是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
				return ;
			}
			if(remit<10){
				GsonUtil.GsonObject(toResultJson("存款金额不能小于10元！",false));
				return;
			}
			if (user.getRole().equals(UserRole.AGENT.getCode())) {
				GsonUtil.GsonObject(toResultJson("[提示]代理不能转账！",false));
				return ;
			}
	
			synchronized (transferInforEmigratedMO) {
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
					AxisUtil.NAMESPACE, "transferInforEmigrated", new Object[] { getCustomerLoginname(), remit,type}, String.class);
				
				
				if (msg == null) {
					String money = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryEmigratedbonus", new Object[]{user.getLoginname()}, String.class);
					Map<String,Object> data = new HashMap<String,Object>();
					data.put("money",money);
					GsonUtil.GsonObject(toResultJson("转账成功！",data,true));
				} else {
					GsonUtil.GsonObject(toResultJson("提交失败:"+msg,false));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
		}
	}

	
	
	/**
	 * 提款
	 */
	private Object withdrawO = new Object();
	public String withdraw(String msflag,Double amount,String bank,String accountNo,String bankAddress,String tkType,String password,String email , Integer questionid , String answer){
		synchronized (withdrawO) {
			String errormsg=null;

			Users customer = null;
			try {
				Users customerTwo = getCustomerFromSession();
				customer = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { customerTwo.getLoginname() }, Users.class);
			} catch (Exception e) {
				GsonUtil.GsonObject(e.getMessage().toString());
				return null;
			}
			if (customer == null)
				GsonUtil.GsonObject("请登录后在进行操作");
			else {
				try {

					if (accountNo == null || accountNo.trim().equals("")) {
						errormsg = "卡/折号不能为空，请重新输入";
						GsonUtil.GsonObject(errormsg);
						return null;
					}
					if("AGENT".equals(customer.getRole())){
						String [] typeArgs = {"liveall","slotmachine"};
						if(StringUtils.isBlank(tkType) || !StringUtil.isContain(tkType, typeArgs)){
							GsonUtil.GsonObject("代理提款类型不能为空");
							return null;
						}
					}
						// 配合页面的账号隐藏功能
						Userbankinfo userbankinfo = null;
						try {
							userbankinfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getUserbankinfo", new Object[] { customer.getLoginname(), bank }, Userbankinfo.class);
						} catch (AxisFault e) {
							// TODO Auto-generated catch block
							log.error(e);
						}

						if (userbankinfo == null) {
							errormsg = "您输入的卡/折号有误，请重新输入";
							GsonUtil.GsonObject(errormsg);
							return null;
						} else if (userbankinfo != null && -1000 == userbankinfo.getFlag()) {
							errormsg = "系统繁忙，请重新输入";
							GsonUtil.GsonObject(errormsg);
							return null;
						}
						   accountNo = userbankinfo.getBankno();
				if(UserRole.MONEY_CUSTOMER.getCode().equals(customer.getRole())){
					//验证提款密保是否正确
//					if(bank.equals("支付宝")){
						   if(StringUtils.isBlank(answer)){
								return "请输入您的回答";
							}
						String validatemsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE, "questionValidate", new Object[] {customer.getLoginname(), questionid , answer }, String.class);
						if(!validatemsg.equals("SUCCESS")){
							//判断有几次输错
							if(validatemsg.equals("bindingPlease")){
//								errormsg = validatemsg;
								errormsg = "请您先绑定密保！";
								GsonUtil.GsonObject(errormsg);
								return null;
							}
							QuestionStatus status = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE, "queryQuesStatus", new Object[] {customer.getLoginname()}, QuestionStatus.class);
							if(status.getErrortimes()>=5){
								this.getHttpSession().removeAttribute(Constants.SESSION_CUSTOMERID);
								errormsg = "密保答案输入5次错误，请联系在线客服处理";
								GsonUtil.GsonObject(errormsg);
								return null ;
							}
							errormsg = validatemsg;
							GsonUtil.GsonObject(errormsg);
							return null ;
						}
//					}
				}	
					String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), 
							AxisUtil.NAMESPACE, "addCashout", new Object[] { customer.getLoginname(), customer.getLoginname(), password, 
						customer.getRole(), Constants.FROM_FRONT, amount, customer.getAccountName(), StringUtils.trim(accountNo), 
						Constants.DEFAULT_ACCOUNTTYPE, StringUtils.trim(bank), StringUtils.trim(bankAddress), StringUtils.trim(bankAddress), 
						StringUtils.trim(email), customer.getPhone(), getIp(), null, msflag ,tkType}, String.class);
					if (msg == null) {
						refreshUserInSession();
						GsonUtil.GsonObject("提交成功");
						return null;
					} else {
						GsonUtil.GsonObject(msg);
						return null;
						// setErrormsg("提交失败:" + msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return e.getMessage();
				}
			}
			GsonUtil.GsonObject(errormsg);
			return null;
		
		
		}
	
	}
	
	
	/**
	 * 提款Moblile
	 */
	private Object addCashoutMO = new Object();
	public Object addCashoutM(String password,String bankName,String cardNo,String addr,String money,String withdrawlWay,String withdrawlType,Integer questionId,String answer) {
		synchronized (addCashoutMO) {
			try {
				Users customer = getCustomerFromSession();
				if (customer == null){
					return toResultJson("请登录后在进行操作！",false);
				}
				
				customer = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { customer.getLoginname() }, Users.class);
				if (customer == null){
					return toResultJson("请登录后在进行操作！",false);
				}
				
				if (StringUtil.isBlank(password)){
					return toResultJson("[提示]密码不可为空！",false);
				}
				if(StringUtil.isBlank(bankName)){
					return toResultJson("[提示]请选择卡折种类！",false);
				}
				if (StringUtil.isBlank(cardNo)){
					return toResultJson("[提示]卡折号不可为空！",false);
				}
				if (cardNo.length()>20){
					return toResultJson("[提示]卡折号长度不能大于20！",false);
				}
				if (StringUtil.isBlank(addr)){
					return toResultJson("[提示]开户网点不可为空！",false);
				}
				if (StringUtil.isBlank(money)){
					return toResultJson("[提示]提款金额不可为空！",false);
				}
				if(!NumberUtils.isNumber(money)){
					return toResultJson("[提示]提款金额必須為數字！",false);
				}
				if (Double.valueOf(money)<1){
					return toResultJson("[提示]单次提款金额最低1！",false);
			    }

				// 配合页面的账号隐藏功能
				Userbankinfo userbankinfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getUserbankinfo", new Object[] { customer.getLoginname(), bankName }, Userbankinfo.class);
				
				if (userbankinfo == null) {
					return toResultJson("[提示]您输入的卡/折号有误，请重新输入！",false);
				}
				if (userbankinfo != null && -1000 == userbankinfo.getFlag()) {
					return toResultJson("系统繁忙，请重新输入！",false);
				}
				
				if(UserRole.MONEY_CUSTOMER.getCode().equals(customer.getRole())){
					//验证提款密保是否正确
					

					if(StringUtils.isBlank(answer)){
						return toResultJson("[提示]您的回答不可为空！",false);
					}
					
					String validatemsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE, "questionValidate", new Object[] {customer.getLoginname(), questionId , answer }, String.class);
					if(!validatemsg.equals("SUCCESS")){
						//判断有几次输错
						if(validatemsg.equals("bindingPlease")){
							return toResultJson("请您先绑定密保！",false);
						}
						QuestionStatus status = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE, "queryQuesStatus", new Object[] {customer.getLoginname()}, QuestionStatus.class);
						if(status.getErrortimes()>=5){
							this.getHttpSession().removeAttribute(Constants.SESSION_CUSTOMERID);
							return toResultJson("密保答案输入5次错误！",false);
						}
						return toResultJson(validatemsg,false);
					}
				}
				
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, 
						"addCashout", new Object[] { customer.getLoginname(), customer.getLoginname(), password, customer.getRole(), 
					Constants.FROM_FRONT, money, customer.getAccountName(), userbankinfo.getBankno(), Constants.DEFAULT_ACCOUNTTYPE,
					StringUtils.trim(bankName), StringUtils.trim(addr), StringUtils.trim(addr), null, customer.getPhone(), getIp(), null, 
					withdrawlWay,withdrawlType }, String.class);
				

				if (msg == null) {
					return toResultJson("提交成功！",true);
				} else {
					return toResultJson(msg,false);
				}
			} catch (Exception e) {
				return toResultJson("提交失败",false);
			}
			
		}

	}
	
	/**
	 * pt8元
	 */
	private Object commitPT8SelfO = new Object();
	public String commitPT8Self(String type ,String code , String platform){
		synchronized (commitPT8SelfO) {
			try {
				Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
				if (user == null) {
					GsonUtil.GsonObject("请登录后重试");
					return null;
				}else {
					user = AxisUtil.getObjectOne(
							AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
							"getUser", new Object[] { user.getLoginname() }, Users.class);
				}

				if (user.getWarnflag().equals(2)) {
					GsonUtil.GsonObject("您的安全等级不够!");
					log.info("您的安全等级不够!");
					return null;
				}
				
				String youHuiFlag = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
						"checkGameIsProtect", new Object[] { "PT8元自助优惠" }, String.class);
				if (!youHuiFlag.equals("1")) {
					GsonUtil.GsonObject(type + "维护中...");
					log.info(type + "维护中...");
					return null;
				}
				
				Const smsFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
						new Object[] { "短信反转验证" }, Const.class);
				
				if(null != smsFlag && smsFlag.getValue().equals("1")){ //短信反转验证
					String smsValidateFlag = (String) getHttpSession().getAttribute(Constants.SMSVALIDATE);
					if(StringUtils.isBlank(smsValidateFlag) || !smsValidateFlag.equals("success")){
						return "短信反转验证失败"+smsValidateFlag ;
					}else {
						getHttpSession().removeAttribute(Constants.SMSVALIDATE);
					}
				}else{
					String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
					if(StringUtils.isBlank(code) || StringUtils.isBlank(sessionCode) ||!sessionCode.equals(code)){
						return "验证码不正确,请重新获取" ;
					}else {
						getHttpSession().removeAttribute(Constants.SESSION_PHONECODE);
					}
				}
				
				// 判断是否有重复的信息(真实姓名、手机号、ip、email、cpu、银卡关联卡 )
				String ip = getIpAddr() ;
				String ioBB="";//(String)getHttpSession().getAttribute(Constants.CPUID);
				String result = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
						"commitPT8Self", new Object[] { user.getLoginname() , ioBB ,ip ,platform}, String.class);
				if (null != result) {
					GsonUtil.GsonObject(result);
					log.info(result);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			GsonUtil.GsonObject("系统繁忙");
			return null ;
		
		}
	
	}


	/*
	 * pt8元
	 */
	private Object commitPT8SelfMO = new Object();
	public void commitPT8SelfM(String code ,String platform) {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
				return;
			}else {
				user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
						"getUser", new Object[] { user.getLoginname() }, Users.class);
			}

			if (user.getWarnflag().equals(2)) {
				GsonUtil.GsonObject(toResultJson("您的安全等级不够!",false));
				return;
			}
			
			String youHuiFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { "PT8元自助优惠" }, String.class);
			if (!youHuiFlag.equals("1")) {
				GsonUtil.GsonObject(toResultJson("维护中...",false));
				return;
			}
			
			Const smsFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
					new Object[] { "短信反转验证" }, Const.class);
			
			if(null != smsFlag && smsFlag.getValue().equals("1")){ //短信反转验证
				String smsValidateFlag = (String) getHttpSession().getAttribute(Constants.SMSVALIDATE);
				if(StringUtils.isBlank(smsValidateFlag) || !smsValidateFlag.equals("success")){
					GsonUtil.GsonObject(toResultJson("短信反转验证失败"+smsValidateFlag ,false));
					return;
				}
			}else{
				String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
				if(StringUtils.isBlank(code) || StringUtils.isBlank(sessionCode) ||!sessionCode.equals(code)){
					GsonUtil.GsonObject(toResultJson("验证码不正确,请重新获取" ,false));
					return;
				}
			}
			
			// 判断是否有重复的信息(真实姓名、手机号、ip、email、cpu、银卡关联卡 )
			String ip = getIpAddr() ;
			String ioBB="";//(String)getHttpSession().getAttribute(Constants.CPUID);

			synchronized (commitPT8SelfMO) {
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"commitPT8Self", new Object[] { user.getLoginname() , ioBB ,ip , platform}, String.class);
				if (null != result) {
					GsonUtil.GsonObject(toResultJson(result,true));
				}else{
					GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
		}

	}

	
	/**
	 * 红包优惠
	 * @param couponType
	 * @param couponCode
	 * @return
	 */
	private Object transferInforRedCouponMO = new Object();
	public String transferInforRedCouponM(String couponType,String couponCode) {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请登录后在进行操作！", false));
				return null;
			}
			if (StringUtils.isBlank(couponType)) {
				GsonUtil.GsonObject(toResultJson("请选择平台账户！", false));
				return null;
			}
			if (StringUtils.isBlank(couponCode)) {
				GsonUtil.GsonObject(toResultJson("优惠代码不能为空！", false));
				return null;
			}

			if (couponType.equalsIgnoreCase("pt8")) {
				if (couponCode == null || couponCode.equals("")) {
					GsonUtil.GsonObject(toResultJson("pt8优惠劵不正确！", false));
					return null;
				}
			}
			String ip = getIp();
			if (ip == null || ip.equals("")) {
				ip = "127.0.0.1";
			}

			synchronized (transferInforRedCouponMO) {
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforRedCoupon", new Object[] {
						getCustomerLoginname(), couponType,couponCode, ip }, String.class);
				if (msg == null) {
					GsonUtil.GsonObject(toResultJson("转账成功", true));
					refreshUserInSession();
				} else {
					GsonUtil.GsonObject(toResultJson("提交失败:"+msg, false));
				}
				return null;
			}
		} catch (Exception e) {
			GsonUtil.GsonObject(toResultJson("提交失败:", false));
			return null;
		}
	}
	
	/**
	 * 优惠券
	 */
	private Object transferInforCouponO = new Object();
	public String transferInforCoupon(Double couponRemit,String couponType,Double remit,String couponCode){
		synchronized (transferInforCouponO) {
			String msg = null;
			try {
				Users customer = getCustomerFromSession();
				if (customer == null) {
					GsonUtil.GsonObject("登录已过期，请重新登录!");
					return null;
				}
				if (couponType == null && couponType.equals("")) {
					GsonUtil.GsonObject("请选择平台！");
					return null;
				}
				remit = 0.0;
				if (couponRemit != null) {
					remit = couponRemit;
					if (remit <50) {
						GsonUtil.GsonObject("存款金额不得小于50！");
						return null;
					}
				}
				if (couponCode == null || couponCode.equals("")) {
					GsonUtil.GsonObject("优惠代码不能为空!");
					return null;
				}
				String ip = getIp();
				if (ip == null || ip.equals("")) {
					ip = "127.0.0.1";
				}
				if (couponType.equalsIgnoreCase("pt8")) {
					if (couponCode == null || couponCode.equals("")) {
						GsonUtil.GsonObject("pt8元红包优惠劵不正确");
						return null;
					}
				}
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, 
						"transferInforCoupon", new Object[] { getCustomerLoginname(), couponType, remit, couponCode, ip }, String.class);

			} catch (Exception e) {
				e.printStackTrace();
				msg = "服务异常";
			}

			if (msg == null) {
				GsonUtil.GsonObject("转账成功");
				try {
					refreshUserInSession();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			} else {
				GsonUtil.GsonObject("提交失败:" + msg);
			}
			return null;
		
			
		}
	
	}
	
	/**
	 * 手机优惠券
	 */
	private Object mobileTransferInforCouponO = new Object();
	public String mobileTransferInforCoupon(Double couponRemit,String couponType,String couponCode){
		synchronized (mobileTransferInforCouponO) {
			String msg = null;
			try {
				Users customer = getCustomerFromSession();
				if (customer == null) {
					GsonUtil.GsonObject(toResultJson("登录已过期，请重新登录!", false));
					return null;
				}
				if (StringUtils.isBlank(couponType)) {
					GsonUtil.GsonObject(toResultJson("请选择平台！", false));
					return null;
				}
				if (StringUtils.isBlank(couponCode)) {
					GsonUtil.GsonObject(toResultJson("优惠代码不能为空!", false));
					return null;
				}
				if (couponRemit == null) {
					GsonUtil.GsonObject(toResultJson("存款金额不能为空!", false));
					return null;
				}
				Double remit = couponRemit;
				if (remit <50) {
					GsonUtil.GsonObject(toResultJson("存款金额不得小于50！", false));
					return null;
				}
				
				String ip = getIp();
				if (ip == null || ip.equals("")) {
					ip = "127.0.0.1";
				}
				if (couponType.equalsIgnoreCase("pt8")) {
					if (couponCode == null || couponCode.equals("")) {
						GsonUtil.GsonObject(toResultJson("pt8元红包优惠劵不正确", false));
						return null;
					}
				}
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, 
						"transferInforCoupon", new Object[] { getCustomerLoginname(), couponType, remit, couponCode, ip }, String.class);

			} catch (Exception e) {
				e.printStackTrace();
				msg = "服务异常";
			}

			if (msg == null) {
				GsonUtil.GsonObject(toResultJson("转账成功", true));
				try {
					refreshUserInSession();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			} else {
				GsonUtil.GsonObject(toResultJson("提交失败:" + msg, true));
			}
			return null;
		
			
		}
	
	}
	
	
	/**
	 * 自助反水
	 */
	private Object execXimaO = new Object();
	public String execXima(Users user, String endTime, String startTime , String platform, Double validAmount,Double ximaAmount,Double rate){
		synchronized (execXimaO) {
			String returnStr = null;
				try {
					String gameFlag = AxisUtil.getObjectOne(
							AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
									+ "UserWebService", false), AxisUtil.NAMESPACE,
							"checkGameIsProtect", new Object[] { platform },
							String.class);
					if (!gameFlag.equals("1")) {
						return platform + "维护中..." ;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 检测是否为每天的12点至15点之间，这段时间为系统洗码时间，不能执行自助洗码功能。
				Calendar c=Calendar.getInstance();
				c.setTime(new Date());
				int hour=c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				
				if (!platform.equals("ttg") && !platform.equals("gpi")
						&& !platform.equals("pttiger") && !platform.equals("nt") && !platform.equals("qt")&& !platform.equals("mg")&& !platform.equals("dt")) {
					if ((hour >= 12 && hour < 15) || (hour == 11 && minute >= 50 )) {
						returnStr = "抱歉，暂不能执行自助洗码操作\\n每天的11点50分至15点是系统洗码时间";
						return returnStr;
					}
				}
				if ((hour >= 0 && hour < 3) || (hour == 23 && minute >=50)) {
					returnStr = "抱歉，暂不能执行自助洗码操作\\n每天的23点50分至3点是自助洗码系统维护时间";
					return returnStr;
				}
				if (rate==null||startTime==null||endTime==null||ximaAmount==null||validAmount==null) {
					return "所有栏目，都为必填项！" ;
				}
				SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date start=null;
				Date end=null;
				try {
					Date tempDate=yyyy_MM_d.parse(startTime);
					//两种情况：防止页面串改起始时间或者防止提交了自助洗码后，先停留在页面，等自助反水执行后，再重复提交
					String date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE, "getXimaEndTime", new Object[] {
						user.getLoginname() , platform }, String.class);
					if(null!=date && !"".equals(date)){
						start=yyyy_MM_d.parse(date);
						if(start.compareTo(tempDate)!=0){
							return "不允许的时间间隔" ;
						}
						end=yyyy_MM_d.parse(endTime);
					}else{
						return "本次洗码异常，请重试！T" ;
					}
				} catch (Exception e) {
					// TODO: handle exception
					return "日期格式错误，请重新填写" ;
				}
				
				if (end.getTime()>new Date().getTime()) {
					end=new Date();
				}
//				if (validAmount<=35000) {
//					this.errormsg="有效投注金额小于35000元，无法进行结算";
//					return "input";
//				}
				try {
					AutoXimaReturnVo autoXimaReturnVo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE, "checkSubmitXima", new Object[] {
						user.getLoginname() , platform }, AutoXimaReturnVo.class);
					if(null!=autoXimaReturnVo && !autoXimaReturnVo.getB() && null==autoXimaReturnVo.getMsg()){
						 endTime = yyyy_MM_d.format(end);
						 startTime = yyyy_MM_d.format(start);
						String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1
								+ "UserWebService", false), AxisUtil.NAMESPACE, "execXima", new Object[] {
							user,endTime,startTime , platform }, String.class);
						if(null!=msg && !"".equals(msg)){
							returnStr=msg;
						}else{
							returnStr="本次洗码异常，请重试！C";
						}
					}else if(null!=autoXimaReturnVo && autoXimaReturnVo.getB() && null!=autoXimaReturnVo.getMsg() 
							&& !"".equals(autoXimaReturnVo.getMsg())){
						returnStr=autoXimaReturnVo.getMsg();
					}else{
						returnStr="本次洗码异常，请重试！M";
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					log.error("会员执行自助洗码时，出现异常",e);
					returnStr="系统异常，请稍候再试或与现在客服取得联系";
				}
			return returnStr;
		}
	
	}
	
	public AutoXima execSlotXima(Users user, String platform) {
		AutoXima autoXima= null;

		try {
			AutoXimaReturnVo autoXimaReturnVo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkSubmitXima",
					new Object[] { user.getLoginname(), platform }, AutoXimaReturnVo.class);
			if (null != autoXimaReturnVo && !autoXimaReturnVo.getB() && null == autoXimaReturnVo.getMsg()) {
			
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "execXimaSlot", new Object[] { user, platform }, String.class);
				if (null != msg && !"".equals(msg)) {
					autoXima=new AutoXima(msg);
				} else {
					autoXima=new AutoXima("本次洗码异常，请重试！");
				}
			} else if (null != autoXimaReturnVo && autoXimaReturnVo.getB() && null != autoXimaReturnVo.getMsg() && !"".equals(autoXimaReturnVo.getMsg())) {
				autoXima=new AutoXima(autoXimaReturnVo.getMsg());
			} else {
				autoXima=new AutoXima("本次洗码异常，请重试！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("会员执行自助洗码时，出现异常", e);
			autoXima=new AutoXima("系统异常，请稍候再试或与现在客服取得联系"); 
		}
		return autoXima;
	}
	
	
	
	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();
	/**
	 * 自助存送
	 */
	private Object getSelfYouHuiObjectO = new Object();
	public AutoYouHuiVo getSelfYouHuiObject(String youHuiType,Double remit){
		Users customer = null ;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 判断是否登录
		if (customer == null) {
			GsonUtil.GsonObject("尚未登录！请重新登录！");
			return null;
		}
		synchronized (customer.getId()) {

			try {
				Pattern pat = Pattern.compile("^[1-9][0-9]*([0-9]|.0)$");
				if(!pat.matcher(remit+"").matches()){
					GsonUtil.GsonObject("抱歉，金额只能输入整数哦。");
					return null;
				}
				String [] youhuiArgs = {"590","592","591","593","594","595","596","597","598","599","702","703","704","705","706","707","708","709","710","711","712","730","731","732","733","734","735"};
				List<String> list = new ArrayList<String>();
				list = Arrays.asList(youhuiArgs) ;
				if(!list.contains(youHuiType)){
					GsonUtil.GsonObject("不存在该种优惠。");
					return null;
				}
				//每天凌晨0点到3点不允许自助优惠
				Calendar c=Calendar.getInstance();
				c.setTime(new Date());
				int hour=c.get(Calendar.HOUR_OF_DAY);
				if (hour>=0&&hour<1) {
					GsonUtil.GsonObject("每天的0点至1点是自助优惠系统维护时间");
					return null;
				}
				
				String str = "" ;
					if(youHuiType.equals("590")){
						str = "PT首存优惠" ;
					}else if(youHuiType.equals("591")){
						str = "PT次存优惠";
					}else if(youHuiType.equals("592")){
						str = "EA次存优惠";
					}else if(youHuiType.equals("593")){
						str = "AG存送优惠";
					}else if(youHuiType.equals("594")){
						str = "AGIN存送优惠";
					}else if(youHuiType.equals("595")){
						str = "BBIN存送优惠";
					}else if(youHuiType.equals("596")){
						str = "EBET首存优惠";
					}else if(youHuiType.equals("597")){
						str = "EBET次存优惠";
					}else if(youHuiType.equals("598")){
						str = "TTG首存优惠";
					}else if(youHuiType.equals("599")){
						str = "TTG次存优惠";
					}else if(youHuiType.equals("702")){
						str = "GPI首存优惠";
					}else if(youHuiType.equals("703")){
						str = "GPI次存优惠";
					}else if(youHuiType.equals("704")){
						str = "GPI限时优惠";
					}else if(youHuiType.equals("705")){
						str = "PT限时优惠";
					}else if(youHuiType.equals("706")){
						str = "TTG限时优惠";
					}else if(youHuiType.equals("707")){
						str = "NT首存优惠";
					}else if(youHuiType.equals("708")){
						str = "NT次存优惠";
					}else if(youHuiType.equals("709")){
						str = "NT限时优惠";
					}else if(youHuiType.equals("710")){
						str = "QT首存优惠";
					}else if(youHuiType.equals("711")){
						str = "QT次存优惠";
					}else if(youHuiType.equals("712")){
						str = "QT限时优惠";
					}else if(youHuiType.equals("730")){
						str = "MG首存优惠";
					}else if(youHuiType.equals("731")){
						str = "MG次存优惠";
					}else if(youHuiType.equals("732")){
						str = "MG限时优惠";
					}else if(youHuiType.equals("733")){
						str = "DT首存优惠";
					}else if(youHuiType.equals("734")){
						str = "DT次存优惠";
					}else if(youHuiType.equals("735")){
						str = "DT限时优惠";
					}
					String youHuiFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,"checkGameIsProtect", new Object[] { str },String.class);
					if (!youHuiFlag.equals("1")) {
						GsonUtil.GsonObject(str + "维护中...");
						return null;
					}
				
					Users user = null ;
					user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser",new Object[] {customer.getLoginname()}, Users.class); 
					///***************************begin
					String str11 = "getSelfYouHuiObject"+user.getLoginname()+youHuiType ;
					log.info(str11);
					Long nowTiem = System.currentTimeMillis();
				    Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry<String, Long> entry = it.next();
				        String keyStr = entry.getKey();
				        Long longTime = cacheMap.get(keyStr);
				        if (nowTiem - longTime > 1000 * 60 *3) {
				            it.remove();
				        }
				    }
//				    if (cacheMap.get(str11) != null) {
//				    	log.info(str11);
//				        GsonUtil.GsonObject("180秒内不能重复提交同一优惠。");
//				        return null;
//				    }
				    cacheMap.put(str11, nowTiem);
				  
					///***************************end
					
				
				if(user.getCredit()<remit){
					GsonUtil.GsonObject("主账户金额不足。");
					return null;
				}
				/* 判断是否老虎机存送,并限制最低10元 2015.1.5*/
				if (Arrays.asList(new String[]{"590","591","598","599","702","703","704","705","706","707","708","709","710","711","712","730","731","732","733","734","735"}).contains(youHuiType)){
					if(remit<10){
						GsonUtil.GsonObject("转账金额不能少于10元。");
						return null;
					}
				} else {
					if(remit<100){
						GsonUtil.GsonObject("转账金额不能少于100元。");
						return null; 
					}
				}
				String remoteIp = getIpAddr();
				String ioBB="";//(String)getHttpSession().getAttribute(Constants.CPUID);
				AutoYouHuiVo youHuiVo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getSelfYouHuiObject",new Object[] {user.getLoginname() , Integer.parseInt(youHuiType) , remit , ioBB ,remoteIp}, AutoYouHuiVo.class);
				GsonUtil.GsonObject(youHuiVo.getMessage());
			} catch (Exception e) {
				GsonUtil.GsonObject("网络繁忙");
				e.printStackTrace();
			}
			return null ;
		}
	}

	/**
	 * 自助优惠
	 */
	private Object getSelfYouHuiObjectMO = new Object();
	private Set<String> youhuiMoenySet = new HashSet<String>(Arrays.asList(new String[]{"590","591","598","599","702","703","704","705","706","707","708","709","710","711","712","730","731","732","733","734","735"}));
	public String getSelfYouHuiObjectM(String youhuiType,String money,Users customer){
		try {
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
				return null;
			}
			Calendar c=Calendar.getInstance();
			c.setTime(new Date());
			int hour=c.get(Calendar.HOUR_OF_DAY);
			if (hour>=0&&hour<1) {
				GsonUtil.GsonObject(toResultJson("每天的0点至1点是自助优惠系统维护时间。",false));
				return null;
			}
			if(StringUtils.isBlank(money)){
				GsonUtil.GsonObject(toResultJson("请输入转帐金额！",false));
				return null;
			}
			if(!(money).matches("^\\d*[1-9]\\d*$")){ 
				GsonUtil.GsonObject(toResultJson("转账金额必须为整数！",false));
				return null;
			}
			
			String youhuiStr = youhuiTypeMap.get(youhuiType);
			if(StringUtils.isBlank(youhuiStr)){
				GsonUtil.GsonObject(toResultJson("不存在该种优惠！",false));
				return null;
			}
			
			Double remit = Double.valueOf(money);
			
			if (youhuiMoenySet.contains(youhuiType)){
				if(remit<10){
					GsonUtil.GsonObject(toResultJson("转账金额不能少于10元！",false));
					return null;
				}
			} else if(remit<100) {
				GsonUtil.GsonObject(toResultJson("转账金额不能少于100元！",false));
				return null;
			}
			
			String youHuiFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "UserWebService", false), AxisUtil.NAMESPACE,"checkGameIsProtect", new Object[] { youhuiStr },String.class);
			if (!youHuiFlag.equals("1")) {
				GsonUtil.GsonObject(toResultJson(youhuiStr + "维护中...",false));
				return null;
			}
			synchronized (getSelfYouHuiObjectMO) {
				customer = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "UserWebService", false), AxisUtil.NAMESPACE, "getUser",new Object[] {customer.getLoginname()}, Users.class); 
				
				if(customer.getCredit()<remit){
					GsonUtil.GsonObject(toResultJson("主账户金额不足。",false));
					return null;
				}
				
				AutoYouHuiVo youHuiVo = null ;
				String remoteIp = getIpAddr();
				String ioBB="";//(String)getHttpSession().getAttribute(Constants.CPUID);
					youHuiVo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1
							+ "UserWebService", false), AxisUtil.NAMESPACE, "getSelfYouHuiObject",
							new Object[] {customer.getLoginname() , Integer.parseInt(youhuiType) , remit , ioBB ,remoteIp}, AutoYouHuiVo.class);
					
					GsonUtil.GsonObject(toResultJson(youHuiVo.getMessage(),true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
		}
		return null;
	}
	
	public Users getCustomerFromSession() throws Exception{
		try {
			return (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后再进行操作");
		}
	}
	
	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 
	 * @param message 訊息
	 * @param success 成功/失敗
	 * @return map
	 */
	private Object toResultJson(Object message,boolean success){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("success", success);
		result.put("message", message);
		return result;
	}
	
	
	public void refreshUserInSession() throws Exception {
		Users customer = getCustomerFromSession();
		Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { customer.getLoginname() }, Users.class);
		getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
		if(customer.getPostcode()==null || user.getPostcode()==null){
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		}
		if(!customer.getPostcode().equals(user.getPostcode())){
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		}
	}
	
	public String getCustomerLoginname()throws Exception {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user != null)
				return user.getLoginname();
			else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后，在进行操作");
		}
	}
	
	public Integer getCustomerId()throws Exception {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user != null)
				return user.getId();
			else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取会话失败", e);
			throw new Exception("请登录后，在进行操作");
		}
	}
	
	public String getIp() {
		String forwaredFor = getRequest().getHeader("X-Forwarded-For");
		String remoteAddr = getRequest().getHeader("X-Real-IP");
		String forwaredFor2 = getRequest().getHeader("HTTP_X_FORWARDED_FOR");
		System.out.println("forwaredFor2:   "+forwaredFor2);
		System.out.println("forwaredFor:   "+forwaredFor);
		System.out.println("remoteAddr:   "+remoteAddr);
		if (StringUtils.isNotEmpty(forwaredFor)) {
			String[] ipArray = forwaredFor.split(",");
			return ipArray[0];
		} else
			return remoteAddr;
	}
	
	
	public String getIpAddr() {  
		HttpServletRequest request = getRequest() ;
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
        log.debug("getIpAddr-->"+ip);
        if (StringUtils.isNotEmpty(ip)) {
			String[] ipArray = ip.split(",");
			return ipArray[0];
		} else
			return ip;  
    }  
	
	
	 //赠送金额转入游戏
	private Object transferInforSignO = new Object();
	public String transferInforSign(String signType , int remit){
		synchronized (transferInforSignO) {
			/*if(remit<10){
				GsonUtil.GsonObject("签到转入金额不能小于10元！");
				return null;
			}*/
			try {
				Users customer = getCustomerFromSession();
				// 判断是否登录
				if (customer == null) {
					GsonUtil.GsonObject("尚未登录！请重新登录！");
					return null ;
				}
				if (customer.getRole().equals(UserRole.AGENT.getCode())) {
					GsonUtil.GsonObject("[提示]代理不能转账");
					return null ;
				}
				String msg = null;
		
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
						AxisUtil.NAMESPACE, "transferSign", new Object[] { getCustomerLoginname(), remit,signType}, String.class);
				if (msg == null) {
					GsonUtil.GsonObject("转账成功");
					return null ;
				} else {
					GsonUtil.GsonObject("提交失败:"+msg);
					return null ;
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("提交失败:"+e.toString());
				return null ;
			}
		}
		}
	
	private Object transferInforSignMO = new Object();
	/**
	 * 签到金额转入游戏
	 * @param {String} signType 平台类型
	 * @param {int} remit 金额
	 */
	public String transferInforSignM(String signType , int remit){
		synchronized (transferInforSignMO) {
			try {
				Users customer = (Users) getCustomerFromSession();
				if (customer == null) {
					GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
					return null;
				}
				if (customer.getRole().equals(UserRole.AGENT.getCode())) {
					GsonUtil.GsonObject(toResultJson("代理不能转账！",false));
					return null ;
				}

				if(StringUtils.isBlank(signType)){
					GsonUtil.GsonObject(toResultJson("请选择要转入的帐户！",false));
				}
				/*if(remit<10){
					GsonUtil.GsonObject(toResultJson("签到转入金额最低不可低于10元",false));
					return null;
				}*/
		
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),AxisUtil.NAMESPACE, "transferSign", new Object[] { getCustomerLoginname(), remit,signType}, String.class);
				if (msg == null) {
					GsonUtil.GsonObject(toResultJson("转账成功",true));
				} else {
					GsonUtil.GsonObject(toResultJson("提交失败:"+msg,false));
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("提交失败:"+e.toString());
			}
			return null ;
		}
	}
	//签到操作
	private Object dosignO = new Object();
	public void dosign(){
		synchronized (dosignO) {
			try {
				Users customer = null;
				try {
					customer = getCustomerFromSession();
					GsonUtil.GsonObject("签到功能维护中");
					return ;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
				GsonUtil.GsonObject("请登录后再操作");
				return ;
			   }
				String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1
						+ "UserWebService", false), AxisUtil.NAMESPACE, "doSignRecord", new Object[]{customer.getLoginname(),null,customer.getLevel()}, String.class);
				if(!StringUtil.isEmpty(info)){
				GsonUtil.GsonObject(info);
				}
				} catch (AxisFault e) {
				GsonUtil.GsonObject("系统异常");
				e.printStackTrace();
			}
		}
	}
	private Object checkRecord = new Object();
	public void checkSignRecord(){
		synchronized (checkRecord) {
			try {
				Users customer = null;
				try {
					customer = getCustomerFromSession();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
					GsonUtil.GsonObject("请登录后再操作");
					return ;
				}
				Boolean info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1
						+ "UserWebService", false), AxisUtil.NAMESPACE, "checkRecord", new Object[]{customer.getLoginname(),null,customer.getLevel()}, Boolean.class);
					GsonUtil.GsonObject(info);
			} catch (AxisFault e) {
				GsonUtil.GsonObject("系统异常");
				e.printStackTrace();
			}
		}
	}
	
	//签到操作
	private Object dosignMO = new Object();
	public void dosignM(){
		synchronized (dosignMO) {
			try {
				Users customer = (Users) getCustomerFromSession();
				if (customer == null) {
					GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
					return;
				}
				
				String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "doSignRecord", new Object[]{customer.getLoginname()}, String.class);
				
				if(!StringUtil.isEmpty(info)){
					GsonUtil.GsonObject(toResultJson(info,true));
				}else{
					GsonUtil.GsonObject(toResultJson("操作失败！",false));
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
			}
		}
	}
	
	
	
	 //好友推荐奖金转入游戏
		private Object transferInforFriendO = new Object();
		public String transferInforFriend(String signType , Double remit){
			synchronized (transferInforFriendO) {
				if(remit<5){
					GsonUtil.GsonObject("转账金额不能小于5元！");
					return null;
				}
				if(remit%1 != 0){
					GsonUtil.GsonObject("转账金额只能是整数！");
					return null;
				}
				try {
					Users customer = getCustomerFromSession();
					// 判断是否登录
					if (customer == null) {
						GsonUtil.GsonObject("尚未登录！请重新登录！");
						return null ;
					}
					if (customer.getRole().equals(UserRole.AGENT.getCode())) {
						GsonUtil.GsonObject("[提示]代理不能转账");
						return null ;
					}
					String msg = null;
			
					msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "transferFriend", new Object[] { getCustomerLoginname(), remit,signType}, String.class);
					if (msg == null) {
						GsonUtil.GsonObject("转账成功");
						return null ;
					} else {
						GsonUtil.GsonObject("提交失败:"+msg);
						return null ;
					}
				} catch (Exception e) {
					e.printStackTrace();
					GsonUtil.GsonObject("提交失败:"+e.toString());
					return null ;
				}
			}
			}
		
		//好友推荐奖金转入游戏
		private Object transferInforFriendMO = new Object();
		public String transferInforFriendM(String signType , Double remit){
			try {
				Users customer = getCustomerFromSession();
				// 判断是否登录
				if (customer == null) {
					GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
					return null ;
				}
				if (customer.getRole().equals(UserRole.AGENT.getCode())) {
					GsonUtil.GsonObject(toResultJson("[提示]代理不能转账！",false));
					return null ;
				}
				if(remit<5){
					GsonUtil.GsonObject(toResultJson("[提示]存款金额不能小于5元！",false));
					return null;
				}
				
				synchronized (transferInforFriendMO) {
					String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
							AxisUtil.NAMESPACE, "transferFriend", new Object[] { getCustomerLoginname(), remit,signType}, String.class);
					if (msg == null) {
						GsonUtil.GsonObject(toResultJson("转账成功",true));
					} else {
						GsonUtil.GsonObject(toResultJson("提交失败:"+msg,false));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
			}
			return null ;
		}
		
		//积分兑换奖金
		private Object transferInforPointO = new Object();
		public String transferInforPoint(Double remit){//兑换的奖金
					synchronized (transferInforPointO) {
						try {
							Users customer = getCustomerFromSession();
							// 判断是否登录
							if (customer == null) {
								GsonUtil.GsonObject("尚未登录！请重新登录！");
								return null ;
							}
							if (customer.getRole().equals(UserRole.AGENT.getCode())) {
								GsonUtil.GsonObject("[提示]代理不能转账");
								return null ;
							}
							if(remit<1){
								GsonUtil.GsonObject("转账金额不得小于1元！");
								return null ;
							}
							String msg = null;
					
									msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
											AxisUtil.NAMESPACE, "transferPoint", new Object[] { getCustomerLoginname(), remit}, String.class);
							if (msg == null) {
								GsonUtil.GsonObject("转账成功");
								return null ;
							} else {
								GsonUtil.GsonObject("提交失败:"+msg);
								return null ;
							}
						} catch (Exception e) {
							e.printStackTrace();
							GsonUtil.GsonObject("提交失败");
							return null ;
						}
					}
					}

		

		//积分兑换奖金
		private Object transferInforPointMO = new Object();
		public String transferInforPointM(Double remit){//兑换的奖金
			try {
				Users customer = getCustomerFromSession();
				// 判断是否登录
				if (customer == null) {
					GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
					return null ;
				}
				if (customer.getRole().equals(UserRole.AGENT.getCode())) {
					GsonUtil.GsonObject(toResultJson("[提示]代理不能转账！",false));
					return null ;
				}
				if(remit<1){
					GsonUtil.GsonObject(toResultJson("[提示]转账金额不得小于1元！",false));
					return null ;
				}
				synchronized (transferInforPointMO) {
					String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
									AxisUtil.NAMESPACE, "transferPoint", new Object[] { getCustomerLoginname(), remit}, String.class);
					if (msg == null) {
						GsonUtil.GsonObject(toResultJson("转账成功",true));
					} else {
						GsonUtil.GsonObject(toResultJson("提交失败:"+msg,false));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
			}
			return null ;
		}
	

		// 红包优惠
			private Object transferInforRedCouponO = new Object();
			public String transferInforRedCoupon(String couponType,String couponCode) {
				synchronized (transferInforRedCouponO) {
					try {
						Users customer = getCustomerFromSession();
						if (customer == null) {
							GsonUtil.GsonObject("请登录后在进行操作!");
							return null;
						}
						if (couponType == null && couponType.equals("")) {
							GsonUtil.GsonObject("请选择平台账户！");
							return null;
						}
						if (StringUtils.isBlank(couponCode)) {
							GsonUtil.GsonObject("优惠代码不能为空!");
							return null;
						}
						String ip = getIp();
						if (ip == null || ip.equals("")) {
							ip = "127.0.0.1";
						}

						if (couponType.equalsIgnoreCase("pt8")) {
							if (couponCode == null || couponCode.equals("")) {
								GsonUtil.GsonObject("pt8优惠劵不正确");
								return null;
							}
						}

						String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforRedCoupon", new Object[] {
								getCustomerLoginname(), couponType,couponCode, ip }, String.class);
						if (msg == null) {
							GsonUtil.GsonObject("转账成功");
							refreshUserInSession();
						} else {
							GsonUtil.GsonObject("提交失败:" + msg);
						}

						return null;
					} catch (Exception e) {
						GsonUtil.GsonObject("提交失败");
						return null;
					}
				}
			}
		
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Object agentUserBindGameUser = new Object();
	/**
	 * 代理账号绑定游戏账号
	 * @param loginnameGame
	 * @param password
	 * @return
	 */
	public void agentUserBindGameUser(String loginnameGame, String password){
		try {
			
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请登录后在进行操作!",false));
				return;
			}
			if(!customer.getRole().equals(UserRole.AGENT.getCode())){
				GsonUtil.GsonObject(toResultJson("您的账号不是代理账号，不能进行绑定操作!",false));
				return;
			}
			if(StringUtils.isBlank(loginnameGame)){
				GsonUtil.GsonObject(toResultJson("游戏账号名不能为空!",false));
				return;
			}
			if(StringUtils.isBlank(password)){
				GsonUtil.GsonObject(toResultJson("清输入代理账号密码!",false));
				return;
			}
			synchronized (agentUserBindGameUser) {
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "agentUserBindGameUser", new Object[] {
					customer.getLoginname(), loginnameGame, password}, String.class);
				if(msg != null && msg.contains("与游戏账号绑定成功")){
					GsonUtil.GsonObject(toResultJson(msg,true));
				} else {
					GsonUtil.GsonObject(toResultJson(msg,false));
				}
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
		}
	}
	
	private Object transferToGameUserFromAgentUser = new Object();
	/**
	 * 代理账号绑定游戏账号
	 * @param loginnameGame
	 * @param password
	 * @return
	 */
	public void transferToGameUserFromAgentUser(Double remit, String password){
		try {
			
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请登录后在进行操作!",false));
				return;
			}
			if(!customer.getRole().equals(UserRole.AGENT.getCode())){
				GsonUtil.GsonObject(toResultJson("您的账号不是代理账号，不能进行该操作!",false));
				return;
			}
			if(remit == null || remit < 10){
				GsonUtil.GsonObject(toResultJson("转入游戏账号金额不能小于10元!",false));
				return;
			}
			if(password == null){
				GsonUtil.GsonObject(toResultJson("请输入代理账号密码!",false));
				return;
			}

			synchronized (transferToGameUserFromAgentUser) {
				
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "transferInGameUserFromAgentTiger", new Object[] {
					customer.getLoginname(), remit, password}, String.class);
				if(msg == null){
					GsonUtil.GsonObject(toResultJson("转账成功！",true));
				} else {
					GsonUtil.GsonObject(toResultJson(msg,false));
				}
				
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
		}
	}
	
	/* 
	 * app下载彩金   
	 */
	private Object getAppPreferentialMO = new Object();
	public void getAppPreferentialM(String code ,String platform) {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
				return;
			}else {
				user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
						"getUser", new Object[] { user.getLoginname() }, Users.class);
			}

			//[再次验证手机验证]?[SMSVALIDATE在哪里出现]
			Const smsFlag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", 
					new Object[] { "短信反转验证" }, Const.class);
			
			if(null != smsFlag && smsFlag.getValue().equals("1")){ //短信反转验证
				String smsValidateFlag = (String) getHttpSession().getAttribute(Constants.SMSVALIDATE);
				if(StringUtils.isBlank(smsValidateFlag) || !smsValidateFlag.equals("success")){
					GsonUtil.GsonObject(toResultJson("短信反转验证失败"+smsValidateFlag ,false));
					return;
				}
			}else{
				String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
				if(StringUtils.isBlank(code) || StringUtils.isBlank(sessionCode) ||!sessionCode.equals(code)){
					GsonUtil.GsonObject(toResultJson("验证码不正确,请重新获取" ,false));
					return;
				}
			}
			
			synchronized (getAppPreferentialMO) {
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,"getAppPreferentialM", new Object[] { user.getLoginname(), platform}, String.class);
				if (null != result) {
					GsonUtil.GsonObject(toResultJson(result,true));
				}else{
					GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
		}
	}
	
	/**
	 * 秒存
	 * 
	 * */
	private Object getNewdepositO = new Object();
	public Bankinfo getNewdeposit(String loginname, String banktype,String uaccountname,String ubankname,String ubankno,Double amount, boolean force,String depositId){
		synchronized (getNewdepositO) {
			try {
				return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "UserWebService", false), AxisUtil.NAMESPACE, "getNewdeposit", new Object[] {loginname, banktype,uaccountname,ubankname,ubankno,amount, force,depositId}, Bankinfo.class);
			} catch (AxisFault e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	//获取微信转账小数额度
	public Double getWxZzQuota(String loginname,Double amount) throws Exception{
		return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "UserWebService", false), AxisUtil.NAMESPACE, "getWxZzQuota", new Object[] {loginname,amount}, Double.class);
	}
	
	
	
	/**
	 * 微信秒存
	 * 
	 * */
	private Object createWeiXindeposit = new Object();
	public Bankinfo createWeiXindeposit(String loginname,Double amount){
		synchronized (createWeiXindeposit) {
			try {
				return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "UserWebService", false), AxisUtil.NAMESPACE, "createWeiXindeposit", new Object[] {loginname,amount}, Bankinfo.class);
			} catch (AxisFault e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * 一键转账
	 */
	public String oneKeyGameMoney(String loginname, String password, Users customer) {
		String msg = null;
		synchronized (customer.getId()) {
			try {
				msg = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
						AxisUtil.NAMESPACE, "oneKeyGameMoney", new Object[] { loginname, getIp(), password },
						String.class);
			} catch (AxisFault e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public String coupon(String loginName, String couponCode, String platform, Double amount, String requestUrl) {

		try {

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", Constants.PRODUCT_NAME);
			paramMap.put("loginName", loginName);
			paramMap.put("couponCode", couponCode);
			paramMap.put("platform", platform);
			paramMap.put("amount", amount);

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

			PostMethod method = new PostMethod(url + requestUrl);
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				return "系统繁忙，请稍后再试！";
			} else {

				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				responseString = resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);

				return resultMap.get("message");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return "系统繁忙，请稍后再试！";
		}
	}
}