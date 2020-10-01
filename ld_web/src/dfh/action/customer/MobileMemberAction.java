package dfh.action.customer;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import dfh.model.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dfh.action.SubActionSupport;
import dfh.action.SynchronizedUtil;
import dfh.action.tpp.ThirdPartyPayment;
import dfh.action.tpp.ThirdPartyPaymentManage;
import dfh.action.tpp.dcard.DCardPaymentUtil;
import dfh.action.vo.AnnouncementVO;
import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoXimaReturnVo;
import dfh.action.vo.DtPotVO;
import dfh.action.vo.DtVO;
import dfh.action.vo.EbetH5VO;
import dfh.action.vo.MessageVo;
import dfh.action.vo.Messages;
import dfh.captcha_dc.model.Status;
import dfh.captcha_dc.service.TouClick;
import dfh.filter.UrlPatternFilter;
import dfh.model.bean.Bet;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.NTErrorCode;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.QuestionEnum;
import dfh.remote.DocumentParser;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.KenoResponseBean;
import dfh.security.DESEncrypt;
import dfh.security.EncryptionUtil;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.service.interfaces.GuestBookService;
import dfh.skydragon.webservice.model.LoginInfo;
import dfh.skydragon.webservice.model.RecordInfo;
import dfh.utils.AESUtil;
import dfh.utils.APInUtils;
import dfh.utils.AxisSecurityEncryptUtil;
import dfh.utils.AxisUtil;
import dfh.utils.ClientInfo;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
import dfh.utils.IPSeeker;
import dfh.utils.Live800Encode;
import dfh.utils.Page;
import dfh.utils.PatternUtils;
import dfh.utils.SendPhoneMsgUtil;
import dfh.utils.SportBookUtils;
import dfh.utils.StringUtil;
import dfh.utils.TelCrmUtil;
import edu.emory.mathcs.backport.java.util.Collections;

public class MobileMemberAction extends SubActionSupport {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MobileMemberAction.class);
	private Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
	private Pattern p_phone = Pattern
			.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
	private Pattern p_account = Pattern.compile("^[a-zA-Z0-9]{6,10}$");
	private Pattern p_agentaccount = Pattern.compile("^a_[a-zA-Z0-9]{1,13}$");

	// private GuestBookService guestBookService;

	private final String websiteKey = "68aca137-f3c5-457b-87a4-8a46880b1e66";
	private final String privateKey = "60ccd51f-5df3-4f49-bdeb-5c36eed2329c";
	

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String name;
	private String email;
	private String phone;
	private String intro;
	private String aliasName;
	private String birthDate;
	private String qq;
	private String sms;
	private String url;
	private String wechat;
	private String partner;
	private String check1;
	private String check2;

	private String password;
	private String newPassword;
	private String confirmPassword;
	private String imageCode;
	private String account;
	private Integer pageIndex;// 当前页
	private Integer size;// 每页显示的行数

	private String money;
	private String gameCode;
	private String historyType;

	private String bankName;
	private String payId;

	private String withdrawlType;
	private String withdrawlWay;
	private String addr;
	private String cardNo;

	private String transferGameOut;
	private String transferGameIn;

	private String checkAddress;
	private String checkKey;
	private String sid;

	private String gameId;
	private String startDate;
	private String endDate;

	private String ptBigBangId;

	private String pno;
	private String flag;

	private Integer proposalType;
	private String yearmonth;
	private String address;

	private String answer;
	private Integer questionId;

	private String isfun;
	private String gameType;

	private String bindingCode;

	private String helpType;
	private String couponCode;
	private Double couponRemit;

	private Integer emailId;

	private String youhuiType;

	private String card_no;
	private String card_code;
	private String card_password;

	private String emigratedeLevel;

	private String origin;
	private String microchannel;
	private String typeNo;
	private String itemNo;
	private String itemName;
	private Pattern ymPattern2 = Pattern.compile("^\\d{4}-(0[1-9]{1}||1[012]{1})$");

	private Pattern integerP = Pattern.compile("^\\d+(\\.[0]+)*$");
	private String accountName;

	private Integer itemId;
	private Integer appId;
	private Integer demo;

	public Integer getDemo() {
		return demo;
	}

	public void setDemo(Integer demo) {
		this.demo = demo;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getMicrochannel() {
		return microchannel;
	}

	public void setMicrochannel(String microchannel) {
		this.microchannel = microchannel;
	}

	public String getAccountName() {

		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	private String ticket;
	private int agFish;
	private int bgType;
	static NumberFormat decimalFormat = new DecimalFormat("###,##0.00");
	private String birthday;

	public String getBirthday() {
		return birthday;
	}



	public int getBgType() {
		return bgType;
	}

	public void setBgType(int bgType) {
		this.bgType = bgType;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public static void main(String[] args) {
	}

	/**
	 * 登入
	 * 
	 * @return
	 */
	public String mobileLogin() {

		// 保证执行一次，事务完整
		try {
			if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null) {
				GsonUtil.GsonObject(toResultJson("登录成功！", true));
				return null;
			}
			if (StringUtil.isBlank(account)) {
				GsonUtil.GsonObject(toResultJson("[提示]账号不能为空！", false));
				return null;
			}
			if (StringUtil.isBlank(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]密码不能为空！", false));
				return null;
			}
			/*
			 * if (StringUtil.isBlank(imageCode)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]验证码不能为空！",false)); return
			 * null; }
			 * 
			 * String code = (String)
			 * getHttpSession().getAttribute(Constants.SESSION_RANDID); if
			 * (StringUtil.isBlank(code)||!code.equals(imageCode)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]验证码错误！",false)); return
			 * null; }
			 */

			// 验证装置ID
			// String ioBB =
			// (String)getHttpSession().getAttribute(Constants.CPUID);
			String ioBB = "ioBB";

			// 重新生成验证码 防止重复提交
			// getHttpSession().setAttribute(Constants.SESSION_RANDID, null);

			IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
			String remoteIp = getIpAddr();
			String city = "";
			String temp = remoteIp != null ? seeker.getAddress(remoteIp) : "";
			if (null != temp && !"CZ88.NET".equals(temp)) {
				city = temp;
			}

			account = account.toLowerCase().trim(); // 用户名都改成小写
			LoginInfo info = AxisUtil
					.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "login",
							new Object[] { account, password, remoteIp, city,
									ClientInfo.getOSName(getRequest().getHeader("user-agent")), ioBB },
							LoginInfo.class);
			if (info == null || info.getUser() == null || info.getSucFlag() != 1) {
				if (null != info && null == info.getUser() && null == info.getSucFlag()) {
					String msg = info.getMsg();
					if (msg != null && !msg.equals("")) {
						GsonUtil.GsonObject(toResultJson(info.getMsg(), false));
					} else {
						GsonUtil.GsonObject(toResultJson("用户不存在！", false));
					}
				} else {
					GsonUtil.GsonObject(toResultJson("登录异常，请重新登录！", false));
				}
				return null;
			}

			String trycode = EncryptionUtil.encryptPassword(account + "e68" + "ruofgergiowqnr8342047tujtgvasw2q0e38");
			getHttpSession().setAttribute("trycode", trycode);
			Users olduser = info.getUser();
			if (!StringUtil.isBlank(olduser.getLastLoginIp())) {
				getHttpSession().setAttribute("ip", olduser.getLastLoginIp());
				String lastcity = seeker.getAddress(olduser.getLastLoginIp());
				if (null != lastcity && !"CZ88.NET".equals(lastcity)) {
					getHttpSession().setAttribute("city", lastcity);
				} else {
					getHttpSession().setAttribute("city", "");
				}
			} else {
				getHttpSession().setAttribute("ip", "");
				getHttpSession().setAttribute("city", "");
			}

			if (StringUtil.isBlank(olduser.getTempLastLoginTime())) {
				getHttpSession().setAttribute("time", olduser.getTempLastLoginTime());
			} else {
				getHttpSession().setAttribute("time", "");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String birthday = "";
			if (olduser.getBirthday() != null) {
				birthday = format.format(olduser.getBirthday());
			}
			getHttpSession().setAttribute(Constants.PT_SESSION_USER, password);
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, olduser);
			getHttpSession().removeAttribute("loginNum");
			getHttpSession().setAttribute("birthday", birthday);
			try {
				long timestamp = System.currentTimeMillis();
				String hashCode = Live800Encode.getMD5Encode(URLEncoder.encode(
						olduser.getLoginname() + olduser.getLoginname() + "" + timestamp + Constants.LIVE800KEY,
						"UTF-8"));
				String info4Live800 = URLEncoder.encode("userId=" + olduser.getLoginname() + "&name="
						+ olduser.getLoginname() + "&memo=&hashCode=" + hashCode + "&timestamp=" + timestamp, "UTF-8");
				log.info("###################初始化live800信任信息参数:" + info4Live800);
				getHttpSession().setAttribute(Constants.LIVE800INFOVALUE, info4Live800);
			} catch (Exception e) {
				log.error(e.getMessage());
			}

			if (StringUtils.equals(account, password)) {
				log.info("账号密码一样，提醒玩家修改密码");
				getHttpSession().setAttribute(Constants.NEEDMODIFY, "1");
			} else {
				getHttpSession().setAttribute(Constants.NEEDMODIFY, "0");
			}
			Question question = null;
			try {
				question = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
						"queryQuestionForApp", new Object[] { olduser.getLoginname() }, Question.class);
			} catch (AxisFault e) {
				e.printStackTrace();
			}
			if (question != null && question.getQuestionid().intValue() == 7) {

				getHttpSession().removeAttribute(Constants.SESSION_PAYPASSWORD);
				getHttpSession().setAttribute(Constants.SESSION_PAYPASSWORD, question.getContent());
			}
			// getHttpSession().setAttribute(Constants.NEEDMODIFY, "1");
			GsonUtil.GsonObject(toResultJson("登录成功！", true));

		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("登录异常，请重新登录！", false));
		}
		return null;
	}

	/**
	 * 手機版登出
	 * 
	 * @return
	 */
	public String mobileLogout() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			getHttpSession().invalidate();
			if (user != null) {
				String msg = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"logout", new Object[] { user.getLoginname() }, String.class);

				GsonUtil.GsonObject(toResultJson(msg, true));
			} else {
				GsonUtil.GsonObject(toResultJson("登出成功", true));
			}
		} catch (Exception e) {
			GsonUtil.GsonObject(toResultJson("网络繁忙", false));
		}
		return null;
	}

	/**
	 * 手機版註冊
	 * 
	 * @return
	 */
	public String mobileRegister() {
		try {
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
			if (StringUtil.isBlank(account)) {
				GsonUtil.GsonObject(toResultJson("[提示]登入账号不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]登录密码不可为空！", false));
				return null;
			}
			/*
			 * if(!password.equals(confirmPassword)){
			 * GsonUtil.GsonObject(toResultJson("[提示]两次输入的密码不一致，请核对后重新输入！",false
			 * )); return null; }
			 */
			/*
			 * if (StringUtil.isBlank(name)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]姓名不可为空！",false)); return
			 * null; }
			 */
			if (StringUtil.isBlank(phone)) {
				GsonUtil.GsonObject(toResultJson("[提示]电话号码不可为空！", false));
				return null;
			}
			if (!p_phone.matcher(phone).matches()) {
				GsonUtil.GsonObject(toResultJson("[提示]电话号码格式错误！", false));
				return null;
			}
			/*
			 * if (StringUtil.isBlank(email)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]电子邮箱不可为空！",false)); return
			 * null; } if (!p_email.matcher(email).matches()) {
			 * GsonUtil.GsonObject(toResultJson("[提示]电子邮箱格式错误！",false)); return
			 * null; }
			 */
			if (!StringUtil.isBlank(intro) && this.intro.trim().length() > 40) {
				GsonUtil.GsonObject(toResultJson("[提示]邀请码长度不允许！", false));
				return null;
			}
			if (!StringUtils.isBlank(aliasName) && aliasName.trim().length() > 10) {
				GsonUtil.GsonObject(toResultJson("[提示]昵称格式错误！请填写10个以内的汉字、英文字母或数字", false));
				return null;
			}
			if (StringUtils.equals(account, password) || !StringUtil.regPwd(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]密码为6-16位数字或英文字母，英文字母开头且不能和账号相同！", false));
				return null;
			}
			if (!StringUtil.isBlank(name) && name.length() > 10) {
				GsonUtil.GsonObject(toResultJson("[提示]请填写10个以内的汉字、英文字母或数字！", false));
				return null;
			}
			if (!p_account.matcher(account).matches()) {
				GsonUtil.GsonObject(toResultJson("[提示]由6-10个数字或英文字母组成！", false));
				return null;
			}
			String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
			if (StringUtils.isBlank(imageCode) || !imageCode.equalsIgnoreCase(rand)) {
				GsonUtil.GsonObject(toResultJson("[提示]验证码错误！", false));
				return null;
			}

			Proposal introInfo = null;
			// 驗證邀請碼
			if (!StringUtil.isBlank(intro)) {
				introInfo = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"registerYqm", new Object[] { intro }, Proposal.class);
				if (introInfo == null) {
					GsonUtil.GsonObject(toResultJson("[提示]邀请码错误！", false));
					return null;
				}
			}

			String agentflag = (String) getHttpSession().getAttribute("agentflag");
			// agentFlag为true时，代理APP不准注册。
			if (StringUtils.isNotBlank(agentflag) && "true".equals(agentflag)) {
				GsonUtil.GsonObject("您好,请您先从手机网页版进行注册!");
				return null;
			}

			// 得到代理域名：
			String referWebsite = getRequest().getScheme() + "://" + getRequest().getServerName();
			// 得到如何知道本站点(titile.jsp 配合):
			String howToKnow = (String) this.getRequest().getSession().getAttribute("referURL");

			String remoteIp = getIp();
			IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
			String city = "";
			String temp = remoteIp != null && !"".equals(remoteIp) ? seeker.getAddress(remoteIp) : "";
			if (null != temp && !"CZ88.NET".equals(temp)) {
				city = temp;
			}

			/*
			 * String ioBB =
			 * (String)getHttpSession().getAttribute(Constants.mobileDeviceID);
			 * //String ioBB = "ioBB"; if(StringUtil.isBlank(ioBB)){
			 * GsonUtil.GsonObject(toResultJson("[提示]该装置或环境无法进行注册！",false));
			 * return null; }
			 */

			String ioBB = "";// (String)getHttpSession().getAttribute(Constants.CPUID);
			// 注册用户
			email = AESUtil.aesEncrypt(email, AESUtil.KEY);
			phone = AESUtil.aesEncrypt(phone, AESUtil.KEY);
			String agentcode = (String) getHttpSession().getAttribute(Constants.AGENG_CODE);

			String friendcode = (String) getHttpSession().getAttribute("friendcode");
			if (!StringUtil.isEmpty(friendcode)) {
				Users users = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getUserById", new Object[] { friendcode }, Users.class);
				if (null != users) {
					/*
					 * if (accountName.equalsIgnoreCase(users.getAccountName())
					 * || (remoteIp != null && users.getRegisterIp() != null &&
					 * remoteIp.equals(users.getRegisterIp()))) { friendcode =
					 * ""; } else { friendcode = users.getLoginname();
					 * referWebsite = ""; agentcode = ""; }
					 */
					friendcode = users.getLoginname();
					referWebsite = "";
					agentcode = "";
				} else {
					friendcode = "";
				}
			}

			// String platform = (String)
			// getHttpSession().getAttribute(Constants.mobilePlatform);
			LoginInfo info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"register",
					new Object[] { StringUtils.trim(howToKnow), sms == null ? 1 : 0, StringUtils.trim(account),
							password, StringUtils.trim(name), StringUtils.trim(aliasName), StringUtils.trim(phone),
							StringUtils.trim(email), StringUtils.trim(qq), StringUtils.trim(referWebsite), remoteIp,
							city, birthDate, intro, introInfo, ioBB, agentcode, friendcode, wechat },
					LoginInfo.class);

			if (!"success".equals(info.getView()) || null == info.getUser()) {
				GsonUtil.GsonObject(toResultJson("注册异常，请重新注册！" + info.getMsg(), false));
				return null;
			}
			// SynBbsMemberInfo bbsMemberInfo = new SynBbsMemberInfo(account,
			// password);
			// bbsMemberInfo.start();
			// 注册成功
			Users user = info.getUser();
			if (user.getFlag() == 1) {
				GsonUtil.GsonObject(toResultJson("注册成功,违反约束,被冻结,请联系在线客服！", false));
				return null;
			}
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
			GsonUtil.GsonObject(toResultJson("注册成功，请登录！", true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("注册异常，请重新注册！", false));
		}
		return null;
	}

	/**
	 * 手机版代理注册
	 * 
	 * @return
	 */
	public String mobileRegisterAgent() {
		try {
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
			if (StringUtil.isBlank(account)) {
				GsonUtil.GsonObject(toResultJson("[提示]登入账号不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]登录密码不可为空！", false));
				return null;
			}
			/*
			 * if (StringUtil.isBlank(confirmPassword)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]确认密码不可为空！",false)); return
			 * null; }
			 */
			if (StringUtil.isBlank(name)) {
				GsonUtil.GsonObject(toResultJson("[提示]姓名不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(url)) {
				GsonUtil.GsonObject(toResultJson("[提示]代理网址不可为空！", false));
				return null;
			} else {
				if (!PatternUtils.matchAgentAddress(url)) {
					GsonUtil.GsonObject(toResultJson("[提示]代理网址格式不对！", false));
					return null;
				}
			}

			if (StringUtil.isBlank(phone)) {
				GsonUtil.GsonObject(toResultJson("[提示]电话号码不可为空！", false));
				return null;
			}
			if (!p_phone.matcher(phone).matches()) {
				GsonUtil.GsonObject(toResultJson("[提示]手机号码格式错误！", false));
				return null;
			}
			if (StringUtil.isBlank(email)) {
				GsonUtil.GsonObject(toResultJson("[提示]电子邮箱不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(qq)) {
				GsonUtil.GsonObject(toResultJson("[提示]QQ不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(wechat)) {
				GsonUtil.GsonObject(toResultJson("[提示]微信不可为空！", false));
				return null;
			}
			if (!p_email.matcher(email).matches()) {
				GsonUtil.GsonObject(toResultJson("[提示]电子邮箱格式错误！", false));
				return null;
			}
			if (StringUtils.equals(account, password) || !StringUtil.regPwd(password)) {
				GsonUtil.GsonObject(toResultJson("密码为6-16位数字或英文字母，英文字母开头且不能和账号相同", false));
				return null;
			}
			if (!password.equals(confirmPassword)) {
				GsonUtil.GsonObject(toResultJson("[提示]两次输入的密码不一致，请核对后重新输入！", false));
				return null;
			}
			if (account.equals(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]登入账号与登录密码不可为相同！", false));
				return null;
			}
			if (!StringUtil.isBlank(name) && name.length() > 10) {
				GsonUtil.GsonObject(toResultJson("[提示]请填写10个以内的汉字、英文字母或数字！", false));
				return null;
			}

			if (!StringUtil.isBlank(name) && name.length() > 10) {
				GsonUtil.GsonObject(toResultJson("[提示]请填写10个以内的汉字、英文字母或数字！", false));
				return null;
			}
			if (!StringUtil.isBlank(wechat) && wechat.length() > 20) {
				GsonUtil.GsonObject(toResultJson("[提示]微信长度错误！", false));
				return null;
			}
			if (StringUtils.equals(account, password) || !StringUtil.regPwd(password)) {
				GsonUtil.GsonObject(toResultJson("密码为6-16位数字或英文字母，英文字母开头且不能和账号相同", false));
				return null;
			}
			if (!p_agentaccount.matcher(account).matches()) {
				GsonUtil.GsonObject(toResultJson("[提示]账号以a_开头（a后面是下划线_）由3-15个数字或字母组成！", false));
				return null;
			}

			String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
			if (StringUtils.isBlank(imageCode) || !imageCode.equalsIgnoreCase(rand)) {
				GsonUtil.GsonObject(toResultJson("[提示]验证码错误！", false));
				return null;
			}

			// 取得代理域：
			// 得到如何知道本站点(titile.jsp 配合):
			String howToKnow = (String) this.getRequest().getSession().getAttribute("referURL");

			String remoteIp = getIp();
			IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
			String city = "";
			String temp = remoteIp != null && !"".equals(remoteIp) ? seeker.getAddress(remoteIp) : "";
			if (null != temp && !"CZ88.NET".equals(temp)) {
				city = temp;
			}

			// 注册用户
			email = AESUtil.aesEncrypt(email, AESUtil.KEY);
			phone = AESUtil.aesEncrypt(phone, AESUtil.KEY);
			String message = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"addAgent",
					new Object[] { howToKnow, StringUtils.trim(account), password, StringUtils.trim(name),
							StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq),
							StringUtils.trim(url), remoteIp, city, StringUtil.trim(wechat), partner },
					String.class);
			if (!StringUtils.isBlank(message)) {
				GsonUtil.GsonObject(toResultJson("注册失败 :" + message, false));
				return null;
			}
			Users user = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getUser", new Object[] { account }, Users.class);
			try {
				// String html =
				// EmailTemplateHelp.toHTML(Constants.EMAIL_REGISTER_BODY_HTML,
				// new Object[] { account ,
				// DateUtil.formatDateForStandard(user.getCreatetime()) });
				// mailSender.sendmail(html, user.getEmail(), "恭喜您成为龙都娱乐城代理");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			if (user.getFlag() == 1) {
				GsonUtil.GsonObject(toResultJson("恭喜您，代理帐号注册成功，请联系市场部门激活帐号，谢谢！", true));
				return null;
			}
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);

			GsonUtil.GsonObject(toResultJson("恭喜您成为龙都娱乐城代理！", true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("注册异常，请重新注册！", false));
		}
		return null;
	}

	/**
	 * 修改密碼
	 * 
	 * @return
	 */
	public String mobileModifyPassword() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("[提示]请登录后在进行操作！", false));
				return null;
			}
			if (StringUtils.equals(customer.getLoginname(), newPassword) || !StringUtil.regPwd(newPassword)) {
				GsonUtil.GsonObject(toResultJson("密码为6-16位数字或英文字母，英文字母开头且不能和账号相同", false));
				return null;
			}

			if (StringUtil.isBlank(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]用户旧密码不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(newPassword)) {
				GsonUtil.GsonObject(toResultJson("[提示]用户新密码不可为空！", false));
				return null;
			}
			if (customer.getLoginname().equals(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]登入账号与登录密码不可为相同！", false));
				return null;
			}
			if (StringUtil.isBlank(confirmPassword)) {
				GsonUtil.GsonObject(toResultJson("[提示]用户确认新密码不可为空！", false));
				return null;
			}
			if (!newPassword.equals(confirmPassword)) {
				GsonUtil.GsonObject(toResultJson("[提示]两次输入的密码不一致，请核对后重新输入！", false));
				return null;
			}

			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
					"modifyPassword", new Object[] { customer.getLoginname(), password, newPassword, getIp() },
					String.class);
			if ("success".equals(msg)) {
				// customer = refreshUserInSession();
				// getHttpSession().setAttribute(Constants.NEEDMODIFY, null);
				getHttpSession().invalidate();

				GsonUtil.GsonObject(toResultJson("密码修改成功，请重新登入！", true));
			} else {
				GsonUtil.GsonObject(toResultJson(msg, false));
			}

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙", false));
		}
		return null;
	}

	/**
	 * 修改会员个人基本信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String mobileModifyInfo() throws Exception {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("[提示]请登录后在进行操作！", false));
				return null;
			}

			if (StringUtil.isNotBlank(aliasName) && aliasName.trim().length() > 10) {
				GsonUtil.GsonObject(toResultJson("[提示]昵称格式错误！请填写10个以内的汉字、英文字母或数字！", false));
				return null;
			}
			if (UrlPatternFilter.sql_inj(address.trim())) {
				GsonUtil.GsonObject(toResultJson("[提示]邮寄地址不允许有特殊字符！", false));
				return null;
			}
			if (StringUtil.isNotBlank(address) && !"".equals(address.trim()) && address.trim().length() > 50) {
				GsonUtil.GsonObject(toResultJson("[提示]邮寄地址最大长度50个字符！", false));
				return null;
			}
			if (!p_email.matcher(email).matches()) {
				GsonUtil.GsonObject(toResultJson("[提示]电子邮箱格式错误！", false));
				return null;
			}
			if (!Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$").matcher(accountName).matches()) {
				GsonUtil.GsonObject(toResultJson("姓名必须为中文", false));
				return null;
			}

			if (StringUtil.isBlank(qq) && StringUtil.isNotBlank(customer.getQq())) {
				qq = customer.getQq();
			}
			if (StringUtil.isBlank(address) && StringUtil.isNotBlank(customer.getMailaddress())) {
				address = customer.getMailaddress();
			}
			if (StringUtil.isBlank(aliasName) && StringUtil.isNotBlank(customer.getAliasName())) {
				aliasName = customer.getAliasName();
			}

			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"modifyCustomerInfo",
					new Object[] { sms == null ? 1 : 0, customer.getLoginname(), StringUtils.trim(aliasName),
							StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), getIp(),
							StringUtils.trim(address), StringUtils.trim(microchannel), StringUtils.trim(birthday),
							StringUtils.trim(accountName) },
					String.class);
			if (null != msg && "success".equals(msg)) {
				if (customer.getBirthday() == null) {
					getHttpSession().setAttribute("birthday", birthday);
				}
				customer = refreshUserInSession2();
				/*
				 * if (null!=aliasName &&
				 * !aliasName.trim().equals(customer.getAliasName())){ // 同步bbs
				 * 会员昵称 String
				 * bbs_modifyNickName_url=Configuration.getInstance().getValue(
				 * "bbs_modifyNickName_url"); int status = new
				 * HttpClientHelper().modifyNickName(bbs_modifyNickName_url,
				 * StringUtil.convertByteArrayToHexStr(customer.getLoginname().
				 * getBytes("gbk")),
				 * StringUtil.convertByteArrayToHexStr(aliasName.trim().getBytes
				 * ("gbk"))); if (status!=200) {
				 * GsonUtil.GsonObject(this.getErrormsg()+"，同步bbs昵称失败；["+status+
				 * "]"); } }
				 */
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("aliasName", StringUtil.formatStar(customer.getAliasName(), 0.7, 0));
				data.put("QQ", StringUtil.formatStar(customer.getQq(), 0.7, 0));
				data.put("address", StringUtil.formatStar(customer.getMailaddress(), 0.5, 1));
				refreshUserInSession();
				GsonUtil.GsonObject(toResultJson("修改成功！", data, true));
			} else {
				GsonUtil.GsonObject(toResultJson(msg, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * 透過email找回密碼
	 * 
	 * @return
	 */
	public String modifyFindPasswordByEmail() {
		if (StringUtil.isBlank(account)) {
			GsonUtil.GsonObject(toResultJson("[提示]账号不可为空！", false));
			return null;
		}
		if (StringUtil.isBlank(email)) {
			GsonUtil.GsonObject(toResultJson("[提示]电子邮箱不可为空！", false));
			return null;
		}
		if (!p_email.matcher(email).matches()) {
			GsonUtil.GsonObject(toResultJson("[提示]电子邮箱格式错误！", false));
			return null;
		}
		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (StringUtils.isBlank(imageCode) || !imageCode.equalsIgnoreCase(rand)) {
			GsonUtil.GsonObject(toResultJson("[提示]验证码错误！", false));
			return null;
		}
		try {
			Object result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
					"getPwdByYx", new Object[] { account, email }, Object.class);
			OMTextImpl b = (OMTextImpl) result;
			GsonUtil.GsonObject(toResultJson(b.getText(), true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("邮件发送失败，请重试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * 透過手機找回密碼
	 * 
	 * @return
	 */
	public String modifyFindPasswordByPhone() {
		try {
			if (StringUtil.isBlank(account)) {
				GsonUtil.GsonObject(toResultJson("[提示]账号不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(phone)) {
				GsonUtil.GsonObject(toResultJson("[提示]手机号码不可为空！", false));
				return null;
			}
			if (!p_phone.matcher(phone).matches()) {
				GsonUtil.GsonObject(toResultJson("[提示]手机号码格式错误！", false));
				return null;
			}

			// 触点
			/*
			 * Status status = new TouClick().check(
			 * checkAddress,sid,checkKey,websiteKey, privateKey); if(status ==
			 * null || status.getCode()!=0){
			 * GsonUtil.GsonObject(toResultJson("[提示]点触验证码错误！",false)); return
			 * null; }
			 */

			String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
			if (StringUtils.isBlank(imageCode) || !imageCode.equalsIgnoreCase(rand)) {
				GsonUtil.GsonObject(toResultJson("[提示]验证码错误！", false));
				return null;
			}

			String validCode = getCharAndNumr(8);
			// 验证用户和手机号是否匹配 如果匹配则修改为随机密码 如果不匹配则返回 不匹配 0 :匹配 1：不匹配
			Object obj = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
					"getPwdByDx", new Object[] { account, phone, validCode }, Object.class);
			OMTextImpl b = (OMTextImpl) obj;
			if (b.getText().equals("2")) {
				GsonUtil.GsonObject(toResultJson("[提示]该账号已冻结!不能使用找回密码！", false));
				return null;
			} else if (!b.getText().equals("0")) {
				GsonUtil.GsonObject(toResultJson("[提示]用户信息错误，请核对后再次尝试！", false));
				return null;
			}

			String msg = SendPhoneMsgUtil.callfour(phone, "您的新密码为：" + validCode);
			if (msg.equals("发送成功")) {
				GsonUtil.GsonObject(toResultJson("发送成功，请注意查收短信", true));
				return null;
			}
			msg = SendPhoneMsgUtil.sendSms(phone, validCode);
			if (msg.equals("发送成功")) {
				GsonUtil.GsonObject(toResultJson("发送成功，请注意查收短信", true));
				return null;
			}
			msg = SendPhoneMsgUtil.callMine(phone, validCode);
			if (msg.equals("发送成功")) {
				GsonUtil.GsonObject(toResultJson("发送成功，请注意查收短信", true));
				return null;
			} else {
				GsonUtil.GsonObject(toResultJson(msg, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * 获取随机数字和字母
	 * 
	 * @param length
	 * @return
	 */
	private String getCharAndNumr(int length) {
		String val = "";
		String codes = "0123456789";
		char[] c = codes.toCharArray();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val += c[random.nextInt(codes.length())];
		}
		return val;
	}

	/**
	 *
	 * @return
	 */
	public String checkLogin() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				return "false";
			}
		} catch (Exception e) {
			return "false";
		}
		return "true";
	}

	/**
	 * 申请提款
	 * 
	 * @return
	 */
	/*
	 * public synchronized String mobileWithdraw() { Users customerTwo=null; try
	 * { refreshUserInSession2(); customerTwo = getCustomerFromSession();
	 * if(customerTwo==null){
	 * GsonUtil.GsonObject(toResultJson("请登录后在进行操作！",false)); return null; }
	 * Object obj=SynchronizedUtil.getInstance().addCashoutM(password, bankName,
	 * cardNo, addr, money, withdrawlWay,withdrawlType,questionId,answer);
	 * GsonUtil.GsonObject(obj); refreshUserInSession2(); } catch (Exception e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } return null;
	 * }
	 */

	/**
	 * 申请提款 (中间件)
	 * 
	 * @return
	 */
	public void mobileWithdraw() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请登录后在进行操作！", false));
			}
			if (customer.getFlag() == 1) {
				GsonUtil.GsonObject(toResultJson("账号已冻结，请联系客服!", false));
			}
			log.info(customer.getLoginname() + "手机端秒付宝提款，提款类型" + withdrawlType);
		} catch (Exception e) {
			GsonUtil.GsonObject(toResultJson(e.getMessage().toString(), false));
		}
		try {
			String password = getHttpSession().getAttribute(Constants.PT_SESSION_USER).toString();
			// if(questionId !=null && questionId.intValue() == 7) {
			// 和APP统一在middleService加密
			// // questionid如果是7，answer就是支付密码，并且md5加密
			// answer = EncryptionUtil.encryptPassword(answer);
			// }
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("product", Constants.PRODUCT_NAME);
			paramMap.put("loginName", customer.getLoginname());
			paramMap.put("ip", getIpAddr());
			paramMap.put("password", password);
			paramMap.put("money", money);
			paramMap.put("bankName", bankName);
			paramMap.put("accountNo", cardNo);
			paramMap.put("tkType", withdrawlType);
			paramMap.put("questionid", questionId);
			paramMap.put("answer", answer);

			String str = mapper.writeValueAsString(paramMap);

			String requestJson = app.util.AESUtil.getInstance().encrypt(str);

			String url = SpecialEnvironmentStringPBEConfig
					.getPlainText(Configuration.getInstance().getValue("middleservice.url"));
			log.info("------->url:" + url);
			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/withdraw/submit");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				GsonUtil.GsonObject(toResultJson("[提示]系统繁忙，请稍后再试！", false));
			} else {

				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				responseString = resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);
				log.info("resultMap" + resultMap.get("message"));

				GsonUtil.GsonObject(toResultJson(resultMap.get("message"), false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("[提示]系统繁忙，请稍后再试！", false));
		}

		try {
			refreshUserInSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String mobileTransfer() {

		Date now = new Date();
		String msg = "";

		try {

			refreshUserInSession2();

			Users customer = getCustomerFromSession();

			if (customer == null) {

				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			// 签到
			if (transferGameOut.equals("qd")) {

				msg = SynchronizedUtil.getInstance().chekDepositOrderRecord(customer, DateUtil.getDateBegin(now),
						DateUtil.getDateEnd(now));

				if (!"success".equals(msg)) {

					GsonUtil.GsonObject(toResultJson("提交失败:" + msg, false));
					return null;
				}

				msg = SynchronizedUtil.getInstance().transferInforSignM(transferGameIn, Integer.parseInt(money));

				GsonUtil.GsonObject(msg);

				return null;
			}

			msg = transfer(customer.getLoginname(), transferGameOut, transferGameIn, money);

			refreshUserInSession2();

			GsonUtil.GsonObject(toResultJson(msg, true));
		} catch (Exception e) {

			e.printStackTrace();
			log.info("转账异常信息：" + e.getMessage());
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍后重试！", false));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public String transfer(String loginName, String source, String target, String amount) {

		try {

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", "tw");
			paramMap.put("loginName", loginName);
			paramMap.put("source", source);
			paramMap.put("target", target);
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

			PostMethod method = new PostMethod(url + "/self/transfer/submit");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);
			log.info("转账请求地址:" + url + "状态码:" + statusCode + requestJson);

			byte[] responseBody = method.getResponseBody();
			String responseString = new String(responseBody);

			Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
			responseString = resultMap.get("responseData");
			responseString = app.util.AESUtil.getInstance().decrypt(responseString);

			resultMap = mapper.readValue(responseString, Map.class);

			return resultMap.get("message");
		} catch (Exception e) {
			log.info("转账异常：" + e.getMessage());
			return "系统繁忙，请稍后重试！";
		}
	}

	/**
	 * 查詢交易明細
	 * 
	 * @return
	 */
	public String mobileQueryHistory() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
				return null;
			}
			if (historyType == null) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择查询交易明细类型", false));
				return null;
			}

			String historyAction = "";
			Class cls = null;
			if ("deposit".equals(historyType)) {
				historyAction = "selectDepositRecord";
				cls = Payorder.class;
			} else if ("cashin".equals(historyType)) {
				historyAction = "selectCashinRecords";
				cls = Proposal.class;
			} else if ("withdraw".equals(historyType)) {
				historyAction = "selectWithdrawReccords";
				cls = Proposal.class;
			} else if ("transfer".equals(historyType)) {
				historyAction = "selectTransferReccords";
				cls = Transfer.class;
			} else if ("depositOrderRecord".equals(historyType)) {
				historyAction = "depositOrderRecord";
				cls = DepositOrder.class;
			} else if ("concessionReccords".equals(historyType)) {
				historyAction = "selectConcessionReccords";
				cls = Proposal.class;

			} else if ("couponRecords".equals(historyType)) {
				historyAction = "selectCouponRecords";
				cls = Proposal.class;
			} else if ("ximaDetail".equals(historyType)) {

			} else if ("point".equals(historyType)) {
				historyAction = "querypointRecord";
				cls = DetailPoint.class;
			} else if ("friend".equals(historyType)) {

			} else if ("lottery".equals(historyType)) {
				historyAction = "queryUserLotteryRecordPage";
				cls = UserLotteryRecord.class;
			} else {
				GsonUtil.GsonObject(toResultJson("[提示]查询类型不存在！", false));
				return null;
			}

			Page page = null;
			Map<String, Object> data = new HashMap<String, Object>();
			if ("ximaDetail".equals(historyType)) {
				String start = "2013-01-01 00:00:01";
				String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				AutoXima ximaTotalRecord = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
						AxisUtil.NAMESPACE, "getTotalCount", new Object[] { customer.getLoginname(), start, end },
						AutoXima.class);

				// if(ximaTotalRecord.getTotalCount()==0){
				if (ximaTotalRecord == null) {
					data.put("records", null);
					data.put("pageIndex", pageIndex);
					data.put("total", 0);
					data.put("size", size);
				} else {
					List<AutoXima> ximaList = AxisUtil.getListObjectOne(
							AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
							AxisUtil.NAMESPACE, "searchXimaDetail",
							new Object[] { customer.getLoginname(), start, end, pageIndex, size }, AutoXima.class);
					data.put("records", getList(ximaList));
					data.put("pageIndex", pageIndex);
					data.put("total", ximaTotalRecord.getTotalCount());
					data.put("size", size);
				}
				GsonUtil.GsonObject(toResultJson("", data, true));
				return null;
			} else if ("friend".equals(historyType)) {
				if ("0".equals(flag)) {
					page = AxisUtil.getPageInList(
							AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
							AxisUtil.NAMESPACE, "friendintroduce",
							new Object[] { customer.getLoginname(), pageIndex, size }, Page.class,
							Friendintroduce.class);

				} else {
					page = AxisUtil.getPageInList(
							AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
							AxisUtil.NAMESPACE, "friendbonusrecord",
							new Object[] { customer.getLoginname(), flag, pageIndex, size }, Page.class,
							Friendbonusrecord.class);
				}
			} else {
				page = AxisUtil.getPageInList(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
						AxisUtil.NAMESPACE, historyAction,
						new Object[] { customer.getLoginname(), pageIndex, size, "", "" }, Page.class, cls);
			}

			if (page != null) {
				data.put("records", getList(page.getPageContents()));
				data.put("pageIndex", pageIndex);
				data.put("total", page.getTotalRecords());
				data.put("size", page.getSize());
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else {
				GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
			}
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("服务异常", false));
		}
		return null;
	}

	/**
	 * 只取前端需要的欄位資料
	 * 
	 * @param datas
	 * @return
	 */
	private List getList(List datas) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> m;
		if ("deposit".equals(historyType)) {
			for (Payorder p : (List<Payorder>) datas) {
				m = new HashMap<String, Object>();
				m.put("billno", p.getBillno());
				m.put("money", decimalFormat(p.getMoney()));
				m.put("tempCreateTime", p.getTempCreateTime());
				m.put("loginname", p.getLoginname());
				list.add(m);
			}
			return list;
		} else if ("cashin".equals(historyType)) {
			for (Proposal p : (List<Proposal>) datas) {
				m = new HashMap<String, Object>();
				m.put("pno", p.getPno());
				m.put("amount", decimalFormat(p.getAmount()));
				m.put("tempCreateTime", p.getTempCreateTime());
				m.put("remark", p.getRemark());
				m.put("loginname", p.getLoginname());
				m.put("flag", ProposalFlagType.getText(p.getFlag()));
				list.add(m);
			}
			return list;
		} else if ("withdraw".equals(historyType)) {
			for (Proposal p : (List<Proposal>) datas) {
				m = new HashMap<String, Object>();
				m.put("pno", p.getPno());
				m.put("amount", decimalFormat(p.getAmount()));
				m.put("tempCreateTime", p.getTempCreateTime());
				m.put("loginname", p.getLoginname());
				m.put("flag", ProposalFlagType.getText(p.getFlag()));
				m.put("unknowflag", p.getUnknowflag());
				list.add(m);
			}
			return list;
		} else if ("transfer".equals(historyType)) {
			for (Transfer p : (List<Transfer>) datas) {
				m = new HashMap<String, Object>();
				m.put("id", p.getId());
				m.put("remit", decimalFormat(p.getRemit()));
				m.put("tempCreateTime", p.getTempCreateTime());
				m.put("remark", p.getRemark());
				m.put("loginname", p.getLoginname());
				m.put("source", p.getSource());
				m.put("target", p.getTarget());
				list.add(m);
			}
			return list;
		} else if ("depositOrderRecord".equals(historyType)) {
			for (DepositOrder p : (List<DepositOrder>) datas) {
				m = new HashMap<String, Object>();
				m.put("depositId", p.getDepositId());
				m.put("bankname", p.getBankname());
				m.put("status", p.getStatus());
				m.put("tempCreateTime", p.getTempCreateTime());
				m.put("accountname", p.getAccountname());
				m.put("remark", p.getRemark());
				list.add(m);
			}
			return list;
		} else if ("concessionReccords".equals(historyType)) {
			for (Proposal p : (List<Proposal>) datas) {
				m = new HashMap<String, Object>();
				m.put("pno", p.getPno());
				m.put("type", ProposalType.getText(p.getType()));
				m.put("amount", decimalFormat(p.getAmount()));
				m.put("tempCreateTime", p.getTempCreateTime());
				m.put("loginname", p.getLoginname());
				m.put("remark", p.getRemark());
				list.add(m);
			}
			return list;
		} else if ("couponRecords".equals(historyType)) {
			for (Proposal p : (List<Proposal>) datas) {
				m = new HashMap<String, Object>();
				m.put("pno", p.getPno());
				m.put("type", ProposalType.getText(p.getType()));
				m.put("tempCreateTime", p.getTempCreateTime());
				m.put("loginname", p.getLoginname());
				m.put("remark", p.getRemark());
				m.put("amount", decimalFormat(p.getAmount()));
				m.put("gifTamount", decimalFormat(p.getGifTamount()));
				m.put("shippingCode", p.getShippingCode());
				m.put("betMultiples", p.getBetMultiples());
				list.add(m);
			}

			return list;
		} else if ("ximaDetail".equals(historyType)) {
			for (AutoXima p : (List<AutoXima>) datas) {
				m = new HashMap<String, Object>();
				m.put("pno", p.getPno());
				m.put("statisticsTimeRange", p.getStatisticsTimeRange());
				m.put("validAmount", decimalFormat(p.getValidAmount()));
				m.put("ximaAmount", decimalFormat(p.getXimaAmount()));
				m.put("ximaType", p.getXimaType());
				m.put("rate", p.getRate());
				m.put("ximaStatus", p.getXimaStatus());
				list.add(m);
			}
			return list;
		} else if ("lottery".equals(historyType)) {
			for (UserLotteryRecord p : (List<UserLotteryRecord>) datas) {
				System.out.println(p.toString());
				m = new HashMap<String, Object>();
				m.put("itemName", p.getItemName());
				m.put("winningDate", p.getWinningDate());
				m.put("isReceive", p.getIsReceive());
				list.add(m);
			}
			return list;
		} else {
			return datas;
		}
	}

	/**
	 * 取得當前用戶餘額
	 * 
	 * @return
	 */
	public String mobileCredit() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			customer = refreshUserInSession2();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			GsonUtil.GsonObject(toResultJson(customer.getCredit(), true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍后尝试！", false));
		}

		return null;
	}

	/**
	 * 获取游戏金额
	 * 
	 * @return
	 */
	public String mobileGameAmount() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtil.isBlank(gameCode)) {
				GsonUtil.GsonObject(toResultJson("请选择账户！", false));
				return null;
			}
			Object money = null;
			if ("self".equals(gameCode)) {
				money = customer.getCredit();
			} else if ("ea".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getEaGameMoney(customer.getLoginname());
			} else if ("ag".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getAgGameMoney(customer.getLoginname());
			} else if ("agin".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getAginGameMoney(customer.getLoginname());
			} else if ("bbin".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getBbinGameMoney(customer.getLoginname());
			} else if ("keno".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getKenoGameMoney(customer.getLoginname());
			} else if ("keno2".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getKeno2GameMoney(customer.getLoginname());
			} else if ("sba".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getSbaGameMoney(customer.getLoginname());
			} else if ("mwg".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getMwgGameMoney(customer.getLoginname());
			} else if ("newpt".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getPtGameMoney(customer.getLoginname());
			} else if ("sixlottery".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getSixLotteryGameMoney(customer.getLoginname());
			} else if ("ebet".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getEbetGameMoney(customer.getLoginname());
			} else if ("gpi".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getGPIBalance(customer.getLoginname());
			} else if ("ttg".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getTTGBalance(customer.getLoginname());
			} else if ("qt".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getQTBalance(customer.getLoginname());
			} else if ("jc".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getJCBalance2(customer.getLoginname());
			} else if ("nt".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getNTBalance(customer.getLoginname());
			} else if ("fish".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getFishBalance(customer.getLoginname());
			} else if ("slot".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getSlotBalance(customer.getLoginname());
			} else if ("n2live".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getNTwoBalance(customer.getLoginname());
			} else if ("ebetapp".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getEbetAppBalance(customer.getLoginname());
			} else if ("dt".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getDtAppBalance(customer.getLoginname());
			} else if ("mg".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getMgAppBalance(customer.getLoginname(), gameCode);
			} else if ("cq9".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getCq9AppBalance(customer.getLoginname(), gameCode);
			}  else if ("pg".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getPgAppBalance(customer.getLoginname(), gameCode);
			}else if ("bg".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getBgAppBalance(customer.getLoginname(), gameCode);
			}else if ("png".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getPngBalance(customer.getLoginname());
			} else if ("qd".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getQdBalance(customer.getLoginname());
			} else if ("chess".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getChessBalance(customer.getLoginname());
			} else if ("fanya".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getFanyaBalance(customer.getLoginname());
			} else if (gameCode.equals("pb")) {
				// 平博查询余额调用middleservice
				money = getPBBalance();
				log.info("H5获取平博游戏金额成功money:" + money);
			} else if ("bit".equals(gameCode)) {
				money = AxisSecurityEncryptUtil.getBitGameBalance(customer.getLoginname());
			} else if (gameCode.equals("kyqp") || gameCode.equals("vr")) {
				money = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getKyqpBalance", new Object[] { customer.getLoginname(), gameCode, getIp() }, Double.class);
			}
			/*
			 * else if ("bbin".equals(gameCode)){ money =
			 * AxisSecurityEncryptUtil.getBbinBalance(customer.getLoginname());
			 * }
			 */else {
				GsonUtil.GsonObject(toResultJson("帐户不存在！", false));
				return null;
			}
			if (money != null && NumberUtils.isNumber(String.valueOf(money))) {
				GsonUtil.GsonObject(toResultJson(money, true));
			} else {
				GsonUtil.GsonObject(toResultJson("系统繁忙！", false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙！请稍后再试！", false));
		}
		return null;
	}

	/**
	 * 获取平博用户余额
	 *
	 * @return
	 */
	public Double getPBBalance() {
		log.info("H5平博用户获取余额开始");
		PostMethod method = null;
		try {
			refreshUserInSession();
			Users customer = getCustomerFromSession();
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", "ql");
			paramMap.put("loginName", customer.getLoginname());

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

			method = new PostMethod(url + "/gameCenter/balance/getPBBalance");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.info("系统繁忙，请稍后重试,statusCode:" + statusCode);
				GsonUtil.GsonObject("系统繁忙，请稍后重试！");
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				responseString = resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);
				return new Double(resultMap.get("message").toString());
			}
		} catch (Exception e) {
			GsonUtil.GsonObject("系统繁忙，请稍后重试！");
			log.info(e);
		} finally {
			// 释放
			if (null != method) {
				method.releaseConnection();
			}
		}
		log.info("H5平博用户获取余额结束");
		return null;
	}

	/**
	 * 依銀行名稱查詢卡號跟開戶分行
	 * 
	 * @return
	 */
	public String mobileBankData() {
		try {
			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("number", "");
			data.put("addr", "");
			if (StringUtil.isBlank(bankName)) {
				GsonUtil.GsonObject(toResultJson("", data, true));
				return null;
			}

			Userbankinfo userbankinfo = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getUserbankinfo", new Object[] { user.getLoginname(), bankName }, Userbankinfo.class);

			if (null == userbankinfo || -1000 == userbankinfo.getFlag()
					|| StringUtil.isBlank(userbankinfo.getBankno())) {
				GsonUtil.GsonObject(toResultJson("", data, true));
				return null;
			}
			String no = userbankinfo.getBankno();

			data.put("number", StringUtil.formatStar(no, 0.7, 1));
			data.put("addr", userbankinfo.getBankaddress());
			GsonUtil.GsonObject(toResultJson("", data, true));

		} catch (Exception e) {
			log.error(e);
			GsonUtil.GsonObject(toResultJson("网络繁忙", false));
		}
		return null;
	}

	public String isBindBankNo() {
		try {
			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("请登录后重试", false));
				return null;
			}
			Integer numbers = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getCardNums", new Object[] { user.getLoginname() }, Integer.class);
			Map<String, Integer> data = new HashMap<String, Integer>();
			data.put("count", numbers);
			if (0 == numbers) {
				GsonUtil.GsonObject(toResultJson("[提示]请绑定银行卡", false));
			} else {
				GsonUtil.GsonObject(toResultJson("已有绑定银行卡", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍后尝试！", false));
		}
		return null;
	}

	/**
	 * 綁定卡號
	 * 
	 * @return
	 */
	public String mobileBindBankNo() {
		try {
			if (StringUtil.isBlank(bankName)) {
				GsonUtil.GsonObject(toResultJson("[提示]银行名称不能为空！", false));
				return null;
			}
			if (StringUtil.isBlank(cardNo)) {
				GsonUtil.GsonObject(toResultJson("[提示]卡/折号不能为空！", false));
				return null;
			}
			if (cardNo.trim().length() > 30 || cardNo.trim().length() < 10) {
				GsonUtil.GsonObject(toResultJson("[提示]卡/折号长度只能在10-30位之间，请重新输入！", false));
				return null;
			}
			if (StringUtil.isBlank(addr)) {
				GsonUtil.GsonObject(toResultJson("[提示]开户网点不能为空！", false));
				return null;
			}

			if (StringUtil.isBlank(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]登录密码不可为空！", false));
				return null;
			}

			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("[提示]请先登录后在继续操作！", false));
				return null;
			}

			if (!EncryptionUtil.encryptPassword(password).equalsIgnoreCase(user.getPassword())) {
				GsonUtil.GsonObject(toResultJson("[提示]登录密码错误，请重新输入！", false));
				return null;
			}
			List<Userbankinfo> userbankinfoList = AxisUtil.getListObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getUserbankinfoList", new Object[] { user.getLoginname() }, Userbankinfo.class);
			if (null == userbankinfoList) {
				GsonUtil.GsonObject(toResultJson("系统异常，请稍后再试！", false));
				return null;
			}

			if (userbankinfoList.size() >= 3) {
				GsonUtil.GsonObject(toResultJson("您已经绑定了三个银行卡/折号，且最多只可以绑定三个；如须解绑，请与在线客服联系！", false));
				return null;
			}

			for (int i = 0; i < userbankinfoList.size(); i++) {
				Userbankinfo userbankinfo = userbankinfoList.get(i);
				if (userbankinfo.getBankname().equals(bankName.trim())) {
					GsonUtil.GsonObject(toResultJson("该银行已经绑定过卡/折号，请不要重复绑定；如须解绑，请与在线客服联系！", false));
					return null;
				}
			}

			String flag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"banding", new Object[] { user.getLoginname(), cardNo.trim(), bankName.trim(), addr.trim() },
					String.class);
			if ("success".equals(flag)) {
				GsonUtil.GsonObject(toResultJson("绑定成功！", true));
			} else {
				GsonUtil.GsonObject(toResultJson("系统异常，绑定失败，请稍后再试！", false));
			}
		} catch (Exception e) {
			log.error(e);
			GsonUtil.GsonObject(toResultJson("系统异常，请稍后再试！", false));
		}
		return null;
	}

	// 获取银行账号
	public String mobileGetFastBankInfo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return null;
			}

			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			if (user.getLevel() != null) {
				Map<String, Object> m = null;
				// 获取银行账号
				List<Bankinfo> bankInfos = AxisUtil.getListObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getBankinfoEmail", new Object[] { String.valueOf(user.getLevel()) }, Bankinfo.class);
				for (Bankinfo bankInfo : bankInfos) {
					m = new HashMap<String, Object>();
					m.put("loginName", user.getLoginname());
					m.put("bankName", bankInfo.getBankname());
					m.put("accountNo", bankInfo.getAccountno());
					m.put("userName", bankInfo.getUsername());
					map.put(bankInfo.getBankname(), m);
				}
				Bankinfo zfbBank = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getZfbBankRedirectInfo", new Object[] { user.getLoginname(), false }, Bankinfo.class);

				if (StringUtils.isBlank(zfbBank.getMassage()) && StringUtils.isNotBlank(zfbBank.getVpnpassword())) {
					m = new HashMap<String, Object>();
					m.put("loginName", user.getLoginname());
					m.put("bankName", zfbBank.getBankname());
					m.put("accountNo", zfbBank.getAccountno());
					m.put("userName", zfbBank.getUsername());
					m.put("vpnpassword", zfbBank.getVpnpassword());
					map.put(zfbBank.getBankname(), m);
				}
			}

			GsonUtil.GsonObject(toResultJson("", map, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
		}
		return null;
	}

	/**
	 * 获取银行账号 支付宝
	 * 
	 * @return
	 */
	public String mobileGetZFBBankInfo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return null;
			}

			Map<String, Object> data = new HashMap<String, Object>();

			// 获取银行账号
			Bankinfo zfbBank = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getZfbBankRedirectInfo", new Object[] { user.getLoginname(), false }, Bankinfo.class);

			if (StringUtils.isBlank(zfbBank.getMassage()) && StringUtils.isNotBlank(zfbBank.getVpnpassword())) {
				data.put("loginName", user.getLoginname());
				data.put("bankName", zfbBank.getBankname());
				data.put("accountNo", zfbBank.getAccountno());
				data.put("userName", zfbBank.getUsername());
				data.put("vpnpassword", zfbBank.getVpnpassword());
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else {
				GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
		}
		return null;
	}

	// 取得附言
	public String mobileGetRemark() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return null;
			}
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
			if (!sessionCode.equals(imageCode)) {
				GsonUtil.GsonObject(toResultJson("验证码不正确！", false));
				return null;
			}

			String[] banks = { "工商银行", "招商银行", "中国银行", "广发银行", "交通银行", "建设银行", "支付宝", "农业银行" };
			List<String> list = new ArrayList<String>();
			list = Arrays.asList(banks);
			if (!list.contains(bankName)) {
				GsonUtil.GsonObject(toResultJson("不存在该种银行！", false));
				return null;
			}

			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getRemarkAgain", new Object[] { user.getLoginname(), bankName, name }, String.class);

			if (null == msg) {
				GsonUtil.GsonObject(toResultJson("网络繁忙", false));
			} else {
				GsonUtil.GsonObject(toResultJson(msg, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙", false));
		}
		return null;
	}

	/**
	 * 取得已开启的第三方支付
	 * 
	 * @return
	 */
	public void mobileGetAllPayments() {

		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return;
			}

			List<Const> consts = AxisUtil.getListObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDepositSwitch", new Object[] { 1, 200 }, Const.class);
			List<PayMerchant> payMers = AxisUtil.getListObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectPayMerchant", new Object[] { "1", "" }, PayMerchant.class);

			List<String> payments = new ArrayList<String>();
			for (Const constPay : consts) {
				if ("1".equals(constPay.getValue())) {
					payments.add(constPay.getId());
				}
			}

			for (PayMerchant payMerchant : payMers) {
				payments.add(payMerchant.getId());
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("payments", payments);
			GsonUtil.GsonObject(toResultJson("", payments, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
		}
	}

	/**
	 * 取得可使用的支付宝在线支付
	 * 
	 * @return
	 */
	public String mobileGetWorkZfbqr2() {

		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return null;
			}
			int count = 1;
			Set<String> showSet = new HashSet<String>(
					Arrays.asList(new String[] { "口袋支付", "优付支付宝", "新贝支付宝", "银宝支付宝", "千网支付宝", "口袋支付宝2", "迅联宝支付宝" }));
			List<Map<String, String>> tpps = ThirdPartyPaymentManage.getInstance().getWorkThirdPartyPayments(user);
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			for (Map<String, String> map : tpps) {
				if (showSet.contains(map.get("value"))) {
					map.put("name", map.get("name") + count++);
					result.add(map);
				}
			}
			GsonUtil.GsonObject(toResultJson("", result, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
		}
		return null;
	}

	/**
	 * 取得可使用的第三方支付
	 * 
	 * @return
	 */
	public String mobileGetWorkThirdPartyPayments() {

		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return null;
			}

			// 过滤掉微信支付的类型
			Set<String> hideSet = new HashSet<String>(Arrays.asList(
					new String[] { "口袋微信支付", "新贝微信", "乐富微信", "乐富微信1", "智付微信", "智付微信1", "口袋支付", "汇潮", "迅联宝", "汇付宝微信",
							"优付支付宝", "新贝支付宝", "银宝支付宝", "优付微信", "千网支付宝", "千网微信", "口袋微信支付2", "口袋支付宝2", "迅联宝支付宝" }));
			List<Map<String, String>> tpps = ThirdPartyPaymentManage.getInstance().getWorkThirdPartyPayments(user);
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			for (Map<String, String> map : tpps) {
				if (!hideSet.contains(map.get("value"))) {
					result.add(map);
				}
			}
			GsonUtil.GsonObject(toResultJson("", result, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
		}
		return null;
	}

	/**
	 * 取得可使用的第三方支付
	 * 
	 * @return
	 */
	public String mobileGetWorkWeixin() {

		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return null;
			}
			int count = 1;
			Set<String> showSet = new HashSet<String>(Arrays.asList(new String[] { "口袋微信支付", "口袋微信支付2", "口袋微信支付3",
					"新贝微信", "乐富微信1", "乐富微信", "迅联宝", "汇付宝微信", "优付微信", "千网微信" }));
			List<Map<String, String>> tpps = ThirdPartyPaymentManage.getInstance().getWorkThirdPartyPayments(user);
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			for (Map<String, String> map : tpps) {
				if (showSet.contains(map.get("value"))) {
					map.put("name", map.get("name") + count++);
					result.add(map);
				}
			}
			// 智付微信有特殊的处理方式 不列入一般的在线支付处理
			Const constPay = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDeposit", new Object[] { "智付微信", "智付微信1" }, Const.class);
			if (constPay != null && "1".equals(constPay.getValue())) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", constPay.getId().replace("智付微信", "zfwx"));
				map.put("name", "微信支付" + count++);
				result.add(map);
			}
			GsonUtil.GsonObject(toResultJson("", result, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
		}
		return null;
	}

	/**
	 * 第三方支付
	 * 
	 * @return
	 */
	public String mobileThirdPartyPayment() {

		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("你的登录已过期，请从首页重新登录！", false));
				return null;
			}
			if (user.getFlag() == 1) {
				GsonUtil.GsonObject(toResultJson("[提示]该玩家已经冻结！", false));
				return null;
			}

			if (StringUtil.isBlank(payId)) {
				GsonUtil.GsonObject(toResultJson("[提示]支付方式不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(bankName)) {
				GsonUtil.GsonObject(toResultJson("[提示]銀行不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(money)) {
				GsonUtil.GsonObject(toResultJson("[提示]金额不可为空！", false));
				return null;
			}
			if (!NumberUtils.isNumber(money)) {
				GsonUtil.GsonObject(toResultJson("[提示]金额必須为数字！", false));
				return null;
			}
			boolean isXBZFB = "新贝支付宝".equals(payId);
			if (!isXBZFB) {
				if (!integerP.matcher(money).find()) {
					GsonUtil.GsonObject(toResultJson("[提示]金额必須为整数！", false));
					return null;
				}
			}

			ThirdPartyPayment tpp = ThirdPartyPaymentManage.getInstance().getThirdPartyPayment(user, payId);
			if (tpp == null) {
				GsonUtil.GsonObject(toResultJson("[提示]该支付方式不存在！", tpp, false));
				return null;
			}

			//
			if (tpp.isValid(user, bankName, Double.valueOf(money))) {
				GsonUtil.GsonObject(toResultJson(tpp.getMessage(), tpp, true));
			} else {
				GsonUtil.GsonObject(toResultJson(tpp.getMessage(), false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("网络繁忙，请稍后再试！", false));
		}
		return null;
	}

	/**
	 * 额度验证存款
	 * 
	 * @return
	 */
	public String mobileVerifyDeposit() {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			// 代理不能使用在线支付
			if (!user.getRole().equals("MONEY_CUSTOMER")) {
				GsonUtil.GsonObject(toResultJson("[提示]代理不能使用在线支付！", false));
				return null;
			}
			if (StringUtil.isBlank(money)) {
				GsonUtil.GsonObject(toResultJson("[提示]存款金额不可为空！", false));
				return null;
			}
			if (!NumberUtils.isNumber(money)) {
				GsonUtil.GsonObject(toResultJson("[提示]存款金额必須為數字！", false));
				return null;
			}
			if (Double.valueOf(money) < 100) {
				GsonUtil.GsonObject(toResultJson("[提示]最低存款100元！", data, false));
				return null;
			}
			Bankinfo bankinfo = null;
			if (user.getLevel() != null) {
				// 获取银行账号
				bankinfo = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getValidateDepositBankInfo", new Object[] { String.valueOf(user.getLevel()) }, Bankinfo.class);
			}
			if (bankinfo == null) {
				GsonUtil.GsonObject(toResultJson("当前系统无法处理额度验证存款，对此给您带来的不便我们深表歉意。", false));
				return null;
			}

			data.put("userName", bankinfo.getUsername());
			data.put("accountNo", bankinfo.getAccountno());
			data.put("bankName", bankinfo.getBankname());
			data.put("QQ", "800134482");

			PayOrderValidation param = new PayOrderValidation();
			param.setOriginalAmount(Double.valueOf(money));
			param.setUserName(user.getLoginname());
			// param.setCreateTime(new Date()); 在webservice
			// 端设置时间，此处设置时间，传输到webservice端时间精度丢失，只有年月日
			PayOrderValidation payOrder = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"createValidatedPayOrder", new Object[] { param }, PayOrderValidation.class);
			String flag = payOrder.getCode();
			if (flag.equals("0")) {
				GsonUtil.GsonObject(toResultJson("24小时内存在三笔或以上的未支付额度验证存款订单，不能创建新订单！", data, false));
			} else if (flag.equals("-1")) {
				GsonUtil.GsonObject(toResultJson("由于当前存款人数太多，无法处理您 " + money + " 元的存款，请尝试其他金额！", data, false));
			} else {
				data.put("amount", payOrder.getAmount());
				GsonUtil.GsonObject(toResultJson("存款订单创建成功！", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统异常，存款下单失败！", data, false));
		}
		return null;
	}

	/**
	 * 查询密保问题
	 * 
	 * @return
	 */
	public String mobileGetQuestion() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
				return null;
			}
			Question question = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"queryQuestion", new Object[] { customer.getLoginname() }, Question.class);
			if (question != null) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("questionId", question.getQuestionid());
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else {
				GsonUtil.GsonObject(toResultJson("", false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * 设定密保问题
	 * 
	 * @return
	 */
	public String mobileSaveQuestion() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
				return null;
			}
			if (StringUtils.isBlank(password)) {
				GsonUtil.GsonObject(toResultJson("[提示]密码不可为空！", false));
				return null;
			}
			if (StringUtils.isBlank(answer)) {
				GsonUtil.GsonObject(toResultJson("[提示]请填写你的回答！", false));
				return null;
			}
			if (questionId == null) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择密保问题！", false));
				return null;
			}
			if (!QuestionEnum.existKey(questionId)) {
				GsonUtil.GsonObject(toResultJson("您所绑定的问题不存在！", false));
				return null;
			}
			if (!EncryptionUtil.encryptPassword(password).equalsIgnoreCase(customer.getPassword())) {
				GsonUtil.GsonObject(toResultJson("登录密码错误，请重新输入！", false));
				return null;
			}

			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"saveQuetion", new Object[] { customer.getLoginname(), questionId, answer }, String.class);

			if (StringUtils.equals(msg, "绑定成功")) {
				GsonUtil.GsonObject(toResultJson("密保问题设定成功！", true));
			} else {
				GsonUtil.GsonObject(toResultJson(msg, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * 登入GPI Mobile
	 * 
	 * @return
	 */
	public String mobileGetGPIGame() {
		String domain = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "");

		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
				return null;
			}
			if (customer.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
				return null;
			}
			if (StringUtils.isEmpty(gameCode)) {
				GsonUtil.GsonObject(toResultJson("请选择游戏", false));
				return null;
			}
			// 获取游戏链接地址
			String gpiLoginUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"gpiMobileLogin", new Object[] { customer.getLoginname(), gameCode, isfun, gameType, domain },
					String.class);

			if (gpiLoginUrl == null || gpiLoginUrl.equals("")) {
				GsonUtil.GsonObject(toResultJson("请选择游戏", false));
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", gpiLoginUrl);
				GsonUtil.GsonObject(toResultJson("", data, true));
			}

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * 登入QT Mobile
	 * 
	 * @return
	 */
	public String mobileGetQTGame() {
		try {
			String domain = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "");
			if (!StringUtil.isBlank(origin)) {
				domain = origin;
			}
			Users customer = getCustomerFromSession();
			if (customer == null && !"1".equals(isfun)) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
				return null;
			}
			if (customer != null && customer.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
				return null;
			}
			if (StringUtils.isEmpty(gameCode)) {
				GsonUtil.GsonObject(toResultJson("请选择游戏", false));
				return null;
			}
			// 获取游戏链接地址
			String gameUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
					AxisUtil.NAMESPACE, "qtGameUrl", new Object[] {
							"1".equals(isfun) ? "DEMOPLAY" : customer.getLoginname(), gameCode, isfun, "0", domain },
					String.class);

			if (gameUrl == null || gameUrl.equals("") || "FAIL".equals(gameUrl)) {
				GsonUtil.GsonObject(toResultJson("进入游戏失败", false));
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", gameUrl);
				GsonUtil.GsonObject(toResultJson("", data, true));
			}

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}
	
	public String mobileGetBgGame() {
		Users customer = null;
		ReturnInfo ri = new ReturnInfo();
		try {
			customer = getCustomerFromSession();
			if (customer == null) {
				ri.setCode("-1");
				ri.setMsg("请登录后，在进行操作");
				return	GsonUtil.GsonObject(ri);
			}
			String loginUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getBgFishGameUrl", new Object[] { customer.getLoginname(), String.valueOf(bgType)}, String.class);// 手机登录
			ri.setCode("0");
			ri.setMsg("");
			ri.setData(loginUrl);
		} catch (Exception e) {
			e.printStackTrace();
			ri.setCode("-2");
			ri.setMsg("系统异常请稍后再试！");
		}
		return GsonUtil.GsonObject(ri);
	}

	public String mobileGetAginGame() {
		final String encrypt_key = "njvs90z1";
		final String md5encrypt_key = "sdf7&^#gfas";
		final String cagent = "B20_AGIN";
		final String dspurl = "http://gci.sunrise88.net:81";
		final String prefix = "ki_";

		try {
			boolean canRedirect = false;

			Users customer = getCustomerFromSession();
			if (customer == null) {
				throw new IllegalStateException("请先登入");
			}
			if (customer != null && customer.getRole().equals("AGENT")) {
				throw new IllegalStateException("代理玩家不可游玩");
			}

			String loginName = customer.getLoginname();
			DspResponseBean dspResponseBean = DocumentParser
					.parseDspResponseRequest(AxisSecurityEncryptUtil.aginIsCustomerExist(loginName));
			if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
				// 检测是否有帐号，如果帐号不存在，则创建DSP帐号,0表示帐号不存在
				DspResponseBean createaccount = DocumentParser.parseDspResponseRequest(
						AxisSecurityEncryptUtil.aginCheckOrCreateGameAccount(loginName, loginName));
				if (createaccount != null && createaccount.getInfo().equals("0")) { // 表示创建成功
					// 处理字符串,开始登录游戏
					canRedirect = true;
				} else {
					throw new IllegalStateException(createaccount.getInfo());
				}
			} else if (dspResponseBean != null && dspResponseBean.getInfo().equals("1")) {// 表示已经存在改帐号
				canRedirect = true;
			} else {
				throw new IllegalStateException(dspResponseBean.getInfo());
			}

			if (canRedirect) {
				String loginid = AxisSecurityEncryptUtil.generateLoginID(loginName);
				if ("".equals(loginid)) {
					throw new IllegalStateException("系统繁忙,请稍后再试，或者直接与客服联系！");
				}
				StringBuffer params = new StringBuffer("cagent=" + cagent + "/\\\\/loginname=" + prefix + loginName
						+ "/\\\\/dm=" + "152.101.114.206/" + "/\\\\/actype=" + APInUtils.getActype(loginName)
						+ "/\\\\/password=" + prefix + loginName + "/\\\\/sid=" + cagent + loginid + "/\\\\/oddtype=A");
				// String
				// params="cagent="+cagent+"/\\\\/loginname="+prefix+loginName+"/\\\\/dm="+"152.101.114.206/"+"/\\\\/actype="+
				// APInUtils.getActype(loginName)+"/\\\\/password="+prefix+loginName+"/\\\\/sid="+cagent+loginid;
				if (agFish == 1) {
					params.append("/\\\\/mh5=Y" + "/\\\\/gameType=6");
				}
				DESEncrypt des = new DESEncrypt(encrypt_key);
				String targetParams = des.encrypt(params.toString());
				String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
				String tempUrl = dspurl + "/forwardGame.do?params=" + targetParams + "&key=" + key;
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", tempUrl);
				GsonUtil.GsonObject(toResultJson("成功", data, true));
				return null;
			} else {
				throw new IllegalStateException("系统繁忙,请稍后再试，或者直接与客服联系！");
			}
		} catch (IllegalStateException ise) {
			GsonUtil.GsonObject(toResultJson(ise.getMessage(), false));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * mg Html5 登入
	 * 
	 * @return
	 */
	public void gameH5MGS() {
		String lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "")
				+ "/mobile/mgs.jsp";

		String fromApp = (String) getRequest().getParameter("fromApp");

		if (StringUtils.equals("app", fromApp)) {

			lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "")
					+ "/mobile/app/slotGame.jsp";// 原生app游戏大厅
		}
		try {
			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
			}
			if (user.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
			}
			if (StringUtils.isEmpty(itemId.toString()) || StringUtils.isEmpty(appId.toString())) {
				GsonUtil.GsonObject(toResultJson("请选择游戏", false));
			}
			// 获取游戏链接地址
			String gameUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"mgsH5Login", new Object[] { user.getLoginname(), user.getPassword(), itemId.toString(),
							appId.toString(), lobbyUrl, demo.toString() },
					String.class);
			if (gameUrl == null || gameUrl.equals("")) {
				GsonUtil.GsonObject(toResultJson("进入游戏失败", false));
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", gameUrl);
				GsonUtil.GsonObject(toResultJson("", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
	}

	/**
	 * CQ9 Html5 登入
	 * 
	 * @return
	 */
	public void gameH5CQ9() {
		String lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "")
				+ "/mobile/cq9.jsp";

		String fromApp = (String) getRequest().getParameter("fromApp");

		if (StringUtils.equals("app", fromApp)) {

			lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "")
					+ "/mobile/app/slotGame.jsp";// 原生app游戏大厅
		}
		try {
			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
			}
			if (user.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
			}
			if (StringUtils.isEmpty(gameCode)) {
				GsonUtil.GsonObject(toResultJson("请选择游戏", false));
			}
			// 获取游戏链接地址
			String gameUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"cq9H5Login", new Object[] { user.getLoginname(), user.getPassword(), gameCode, demo.toString() },
					String.class);
			if (gameUrl == null || gameUrl.equals("")) {
				GsonUtil.GsonObject(toResultJson("进入游戏失败", false));
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", gameUrl);
				GsonUtil.GsonObject(toResultJson("", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
	}
	
	/**
	 * pg Html5 登入
	 * 
	 * @return
	 */
	public void gameH5PG() {
		String lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "")
				+ "/mobile/cq9.jsp";

		String fromApp = (String) getRequest().getParameter("fromApp");

		if (StringUtils.equals("app", fromApp)) {

			lobbyUrl = getRequest().getRequestURL().toString().replace(getRequest().getServletPath(), "")
					+ "/mobile/app/slotGame.jsp";// 原生app游戏大厅
		}
		try {
			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
			}
			if (user.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
			}
			if (StringUtils.isEmpty(gameCode)) {
				GsonUtil.GsonObject(toResultJson("请选择游戏", false));
			}
			// 获取游戏链接地址
			String gameUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"pgH5Login", new Object[] { user.getLoginname(), user.getPassword(), gameCode, demo.toString() },
					String.class);
			if (gameUrl == null || gameUrl.equals("")) {
				GsonUtil.GsonObject(toResultJson("进入游戏失败", false));
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", gameUrl);
				GsonUtil.GsonObject(toResultJson("", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
	}


	/**
	 * 登入NT Mobile
	 *
	 * @return
	 */
	public String mobileGetNTGame() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null && !"1".equals(isfun)) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
				return null;
			}
			if (customer != null && customer.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
				return null;
			}
			// NT_SESSION为空才进行登入
			// if (getRequest().getSession().getAttribute(Constants.NT_SESSION)
			// == null) {
			String jsonText = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"loginGWNT", new Object[] { customer.getLoginname(), this.gameCode }, String.class);
			if (StringUtils.isNotEmpty(jsonText)) {
				JSONObject json = JSONObject.fromObject(jsonText); // 返回值sion为json字符串,方便做判断与提示信息
				if (json.getBoolean("result")) {
					String key = json.getString("key");
					getRequest().getSession().setAttribute(Constants.NT_SESSION, key);
					GsonUtil.GsonObject(toResultJson(key, true));
				} else {
					String msg = NTErrorCode.compare(json.getString("error"));
					log.error("loginGameNT错误, 错误消息: " + msg);
					GsonUtil.GsonObject(toResultJson("loginGameNT错误, 错误消息: " + msg, false));
				}
			}
			/*
			 * }else{ //已经登入NT不再做登入 GsonUtil.GsonObject(toResultJson("", "",
			 * true)); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * 登入BBIN Mobile
	 * 
	 * @return
	 */
	public String mobileGetBBINGame() {

		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
				return null;
			}
			if (customer.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
				return null;
			}
			DspResponseBean createaccount = DocumentParser.parseBBinDspResponseRequest(
					AxisSecurityEncryptUtil.bbinCheckOrCreateGameAccount(customer.getLoginname()));

			Map<String, Object> data = new HashMap<String, Object>();
			if (createaccount != null && createaccount.getInfo().equals("21100")) { // 表示创建成功
				// 处理字符串,开始登录游戏
				url = AxisSecurityEncryptUtil.bbinForwardGame(customer.getLoginname());
				data.put("url", url);
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else if (createaccount != null && createaccount.getInfo().equals("21001")) {// 表示已经存在该帐号
				url = AxisSecurityEncryptUtil.bbinForwardGame(customer.getLoginname());
				data.put("url", url);
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else {
				if (createaccount.getInfo() != null) {
					GsonUtil.GsonObject(toResultJson("登录游戏过程中出现问题**" + createaccount.getInfo() + "**,请联系在线客服", false));
				} else {
					GsonUtil.GsonObject(toResultJson("登录游戏过程中出现问题,请联系在线客服", false));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
		return null;
	}

	/**
	 * ebet
	 *
	 * @return
	 */
	public void mobileGetEbetToken() {
		try {

			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}

			if (user.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
				return;
			}
			EbetH5VO ebetH5VO = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getEBetVO", new Object[] { user.getLoginname() }, EbetH5VO.class);
			GsonUtil.GsonObject(toResultJson("success", ebetH5VO, true));

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("登录异常，请重新登录！", false));
		}
	}

	/**
	 * 取得洗码资料
	 * 
	 * @return
	 */
	public String mobileGetXimaData() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			String gameFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { gameId }, String.class);
			if (!gameFlag.equals("1")) {
				GsonUtil.GsonObject(toResultJson("游戏维护中...", false));
				return null;
			}

			String startDate = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getXimaEndTime", new Object[] { user.getLoginname(), gameId }, String.class);
			String endDate = DateUtil.formatDateForStandard(new Date());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			if (sdf.parse(endDate).getTime() < sdf.parse(startDate).getTime()) {
				GsonUtil.GsonObject(toResultJson("截止时间必须大于起始时间！", false));
				return null;
			}

			AutoXima autoXima = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getAutoXimaObject", new Object[] { endDate, startDate, user.getLoginname(), gameId },
					AutoXima.class);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("startDate", startDate);
			data.put("endDate", endDate);

			if (StringUtils.isBlank(autoXima.getMessage())) {
				data.put("validAmount", autoXima.getValidAmount());
				data.put("ximaAmount", autoXima.getXimaAmount());
				data.put("rate", autoXima.getRate());
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else {
				GsonUtil.GsonObject(toResultJson(autoXima.getMessage(), data, false));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("日期格式有误！", false));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("取得洗马资料异常！", false));
		}
		return null;

	}

	public String mobileGetDepositRedEnvelopeData() {
		try {
			Map<String, Object> returnData = new HashMap<String, Object>();
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("[提示]你的登录已过期，请从首页重新登录", false));
				return null;
			}
			// 查询玩家可领红包
			List<HBConfig> hBConfigs = AxisUtil.getListObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryHBSelect", new Object[] { user.getLevel(), 0 }, HBConfig.class);
			// 查询红包奖金余额
			String balance = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryHBbonus", new Object[] { user.getLoginname(), 0 }, String.class);

			if (StringUtil.isNotEmpty(balance) && hBConfigs != null) {
				List<Object> types = new ArrayList<Object>();
				for (int i = 0; i < hBConfigs.size(); i++) {
					Map<String, Object> returnTypes = new HashMap<String, Object>();
					returnTypes.put("value", hBConfigs.get(i).getId());
					returnTypes.put("name", hBConfigs.get(i).getTitle());
					types.add(returnTypes);
				}
				returnData.put("balance", balance);
				returnData.put("types", types);
				GsonUtil.GsonObject(toResultJson("", returnData, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 自助洗码
	 * 
	 * @return
	 */
	public String mobileDoXima() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			if (StringUtils.isBlank(gameId)) {
				GsonUtil.GsonObject(toResultJson("请选择平台！", false));
				return null;
			}
			if (StringUtils.isBlank(endDate)) {
				GsonUtil.GsonObject(toResultJson("截止时间有误！", false));
				return null;
			}
			if (StringUtils.isBlank(startDate)) {
				GsonUtil.GsonObject(toResultJson("起始时间有误！", false));
				return null;
			}
			String gameFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { gameId }, String.class);
			if (!gameFlag.equals("1")) {
				GsonUtil.GsonObject(toResultJson("该游戏平台维护中...", false));
				return null;
			}

			Date nowDate = new Date();
			// 检测是否为每天的12点至15点之间，这段时间为系统洗码时间，不能执行自助洗码功能。
			Calendar c = Calendar.getInstance();
			c.setTime(nowDate);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			Set<String> slotSet = new HashSet<String>(
					Arrays.asList(new String[] { "ttg", "gpi", "pttiger", "nt", "qt", "mg", "dt" }));
			if (!slotSet.contains(gameId)) {
				if (hour >= 12 && hour < 15) {
					GsonUtil.GsonObject(toResultJson("抱歉，暂不能执行自助洗码操作，每天的12点至15点是系统洗码时间", false));
					return null;
				}
			}
			if (hour >= 0 && hour < 3) {
				GsonUtil.GsonObject(toResultJson("抱歉，暂不能执行自助洗码操作，每天的0点至3点是自助洗码系统维护时间", false));
				return null;
			}

			Date tempDate = yyyy_MM_d.parse(startDate);
			// 两种情况：防止页面串改起始时间或者防止提交了自助洗码后，先停留在页面，等自助反水执行后，再重复提交
			String date = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getXimaEndTime", new Object[] { user.getLoginname(), gameId }, String.class);

			if (StringUtils.isBlank(date)) {
				GsonUtil.GsonObject(toResultJson("本次洗码异常，请重试！", false));
				return null;
			}

			Date start = yyyy_MM_d.parse(date);
			Date end = yyyy_MM_d.parse(endDate);
			if (start.compareTo(tempDate) != 0) {
				GsonUtil.GsonObject(toResultJson("起始时间不相符！", false));
				return null;
			}

			if (end.getTime() > nowDate.getTime()) {
				end = nowDate;
			}

			AutoXimaReturnVo autoXimaReturnVo = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkSubmitXima", new Object[] { user.getLoginname(), gameId }, AutoXimaReturnVo.class);

			if (null != autoXimaReturnVo && !autoXimaReturnVo.getB() && null == autoXimaReturnVo.getMsg()) {
				String endDate = yyyy_MM_d.format(end);
				String startDate = yyyy_MM_d.format(start);
				String msg = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
						AxisUtil.NAMESPACE, "execXima", new Object[] { user, endDate, startDate, gameId },
						String.class);

				if (null != msg && !"".equals(msg)) {
					GsonUtil.GsonObject(toResultJson(msg, true));
					refreshUserInSession2();
				} else {
					GsonUtil.GsonObject(toResultJson("本次洗码异常，请重试！", false));
				}
			} else if (null != autoXimaReturnVo && autoXimaReturnVo.getB() && null != autoXimaReturnVo.getMsg()
					&& !"".equals(autoXimaReturnVo.getMsg())) {
				GsonUtil.GsonObject(toResultJson(autoXimaReturnVo.getMsg(), false));
			} else {
				GsonUtil.GsonObject(toResultJson("本次洗码异常，请重试！", false));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("日期格式错误，请重新填写！", false));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统异常，请稍候再试或与现在客服取得联系！", false));
		}
		return null;
	}

	/**
	 * 检查是否为安全用户
	 * 
	 * @return
	 */
	public String mobileCheckSecurityUser() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
					AxisUtil.NAMESPACE, "getUser", new Object[] { user.getLoginname() }, Users.class);

			if (user.getWarnflag().equals(2)) {
				GsonUtil.GsonObject(toResultJson("非常抱歉！您注册信息有重复,暂无法领取体验金,您可申请其它优惠。", false));
				log.info("您的安全等级不够!");
				return null;
			}

			String youHuiFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { "PT8元自助优惠" }, String.class);
			if (!youHuiFlag.equals("1")) {
				GsonUtil.GsonObject(toResultJson("PT8元自助优惠维护中...", false));
				log.info("PT8元自助优惠维护中...");
				return null;
			}

			// 判断是否有重复的信息(真实姓名、手机号、ip、email、cpu、银卡关联卡 )
			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"haveSameInfo", new Object[] { user.getLoginname() }, String.class);
			if (null == result) {
				GsonUtil.GsonObject(toResultJson("success", true));
			} else {
				GsonUtil.GsonObject(toResultJson(result, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统异常，请稍候再试或与现在客服取得联系！", false));
		}
		return null;
	}

	/**
	 * 短信验证
	 * 
	 * @return
	 */
	public String mobileSendSmsCode() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			// 触点
			Status status = new TouClick().check(checkAddress, sid, checkKey, websiteKey, privateKey);
			if (status == null || status.getCode() != 0) {
				GsonUtil.GsonObject(toResultJson("[提示]点触验证码错误！", false));
				return null;
			}

			String code = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if (StringUtils.isBlank(code)) {
				code = RandomStringUtils.randomNumeric(4);
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
			}

			sendSmsToPhone(user.getPhone(), code);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙", false));
		}
		return null;
	}

	/**
	 * 微信额度验证取得付款讯息 或 账单记录
	 */
	public String mobileWXValidateDepositInfo() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		Map<String, Object> data = new HashMap<String, Object>();
		// 检测用户是否登录
		if (user == null) {
			GsonUtil.GsonObject(toResultJson("[提示]你的登录已过期，请从首页重新登录", false));
			return null;
		}
		try {
			// 判断是否存在支付订单
			PayOrderValidation payOrderValidation = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getPayOrderValidation", new Object[] { user.getLoginname() }, PayOrderValidation.class);
			if (payOrderValidation != null) {
				data.put("wxValidaTeAmout", payOrderValidation.getAmount());
				data.put("wxValidaId", payOrderValidation.getId());
			}
			Bankinfo wxBank = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getWxBankRedirectInfo", new Object[] { user.getLoginname() }, Bankinfo.class);
			if (StringUtils.isNotBlank(wxBank.getMassage())) {
				GsonUtil.GsonObject(toResultJson(wxBank.getMassage(), false)); // 等级不足
				return null;
			}
			data.put("wxBank", wxBank);
		} catch (AxisFault e) {
			e.printStackTrace();
			return null;
		}
		GsonUtil.GsonObject(toResultJson("取得成功！", data, true));
		return null;
	}

	/**
	 * 语音验证
	 * 
	 * @return
	 */
	public String mobileSendVoiceCode() {

		try {

			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			// 触点
			Status status = new TouClick().check(checkAddress, sid, checkKey, websiteKey, privateKey);
			if (status == null || status.getCode() != 0) {
				GsonUtil.GsonObject(toResultJson("[提示]点触验证码错误！", false));
				return null;
			}

			String code = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if (StringUtils.isBlank(code)) {
				code = RandomStringUtils.randomNumeric(4);
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
			}

			sendVoiceToPhone(user.getLoginname(), user.getPhone(), code);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙", false));
		}
		return null;
	}

	/**
	 * 查询pt老虎机负盈利反赠记录
	 * 
	 * @return
	 */
	public String mobileQueryPTLosePromo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			Page page = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
					"losePromoReccords", new Object[] { user.getLoginname(), pageIndex, size }, Page.class,
					LosePromo.class);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("records", page.getPageContents());
			data.put("pageIndex", pageIndex);
			data.put("total", page.getTotalRecords());
			data.put("size", page.getSize());
			GsonUtil.GsonObject(toResultJson("", data, true));

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	/**
	 * 处理负盈利反赠
	 * 
	 * @return
	 */
	public String mobileOptLosePromo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			if (StringUtils.isBlank(pno)) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择救援金！", false));
				return null;
			}
			if (StringUtils.isBlank(platform)) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择老虎机平台！", false));
				return null;
			}
			if (StringUtils.isBlank(flag)) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择操作类型！", false));
				return null;
			}
			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
					"optLosePromo", new Object[] { pno, flag, getIp(), platform }, String.class);

			if (result == null) {
				GsonUtil.GsonObject(toResultJson("操作成功！", true));
			} else {
				GsonUtil.GsonObject(toResultJson("操作失败：" + result, false));
			}

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	/**
	 * 查询周周回馈
	 * 
	 * @return
	 */
	public String mobileQueryWeekSentReccords() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			Page page = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"weekSentReccords", new Object[] { user.getLoginname(), pageIndex, size }, Page.class,
					WeekSent.class);

			List<Map<String, Object>> reacords = new ArrayList<Map<String, Object>>();
			Map<String, Object> data = null;
			WeekSent week = null;
			for (int i = 0; i < page.getPageContents().size(); i++) {
				data = new HashMap<String, Object>();
				week = (WeekSent) page.getPageContents().get(i);
				data.put("pno", week.getPno());
				data.put("promo", week.getPromo());
				data.put("tempCreateTime", week.getTempCreateTime());
				data.put("remark", week.getRemark());
				data.put("status", week.getStatus());
				reacords.add(data);
			}

			data = new HashMap<String, Object>();
			data.put("records", reacords);
			data.put("pageIndex", pageIndex);
			data.put("total", page.getTotalRecords());
			data.put("size", page.getSize());

			GsonUtil.GsonObject(toResultJson("", data, true));

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	/**
	 * 处理周周回馈
	 * 
	 * @return
	 */
	public String mobileOptWeekSent() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			if (StringUtils.isBlank(pno)) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择一笔回馈！", false));
				return null;
			}
			if (!"-1".equals(flag) && StringUtils.isBlank(platform)) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择老虎机平台！", false));
				return null;
			}
			if (StringUtils.isBlank(flag)) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择操作类型！", false));
				return null;
			}
			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"optWeekSent", new Object[] { pno, flag, getIp(), platform }, String.class);

			if (result == null) {
				GsonUtil.GsonObject(toResultJson("操作成功！", true));
			} else {
				GsonUtil.GsonObject(toResultJson("操作失败：" + result, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}

		return null;
	}

	/**
	 * 查询本月游戏平台投注额
	 * 
	 * @return
	 */
	public String mobileQueryBetOfPlatform() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			List<Bet> betslist = AxisUtil.getListObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryBets", new Object[] { user.getLoginname(), "month" }, Bet.class);
			if (betslist == null || betslist.isEmpty() || betslist.get(0) == null) {
				betslist = new ArrayList<Bet>();
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("records", betslist);
			data.put("pageIndex", 1);
			data.put("total", betslist.size());
			data.put("size", betslist.size());
			GsonUtil.GsonObject(toResultJson("", data, true));

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	/**
	 * 查询本月游戏平台投注额
	 * 
	 * @return
	 */
	public String mobileQueryBetOfPlatformWeek() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			List<Bet> betslist = AxisUtil.getListObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryBets", new Object[] { user.getLoginname(), "week" }, Bet.class);
			if (betslist == null || betslist.isEmpty() || betslist.get(0) == null) {
				betslist = new ArrayList<Bet>();
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("records", betslist);
			data.put("pageIndex", 1);
			data.put("total", betslist.size());
			data.put("size", betslist.size());
			GsonUtil.GsonObject(toResultJson("", data, true));

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	/**
	 * 处理升级
	 */
	public String mobileCheckUpgrade() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtils.isBlank(helpType)) {
				GsonUtil.GsonObject(toResultJson("请选择检测类型！", false));
				return null;
			}
			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkUpgrade", new Object[] { user.getLoginname(), helpType }, String.class);
			if (result == null) {
				GsonUtil.GsonObject(toResultJson("升级成功！", true));
			} else {
				GsonUtil.GsonObject(toResultJson(result, false));
			}

		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	/**
	 * 查询PT大爆炸礼金
	 * 
	 * @param action
	 * @param interval
	 * @return
	 */
	public String mobileQueryPTBigBang() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			List<PTBigBang> bonusList = AxisUtil.getListObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"ptBigBangBonus", new Object[] { user.getLoginname() }, PTBigBang.class);

			Map<String, Object> data = new HashMap<String, Object>();
			if (bonusList != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Map<String, Object> record = null;
				for (PTBigBang ptBigBang : bonusList) {
					record = new HashMap<String, Object>();
					record.put("id", ptBigBang.getId());
					record.put("netWinOrLose", ptBigBang.getNetWinOrLose());
					record.put("bonus", ptBigBang.getBonus() == null ? 0 : ptBigBang.getBonus());
					record.put("tempDistributeTime", ptBigBang.getTempDistributeTime());
					record.put("giftMoney", ptBigBang.getGiftMoney());
					record.put("status", ptBigBang.getStatus());
					list.add(record);
				}
				data.put("records", list);
				data.put("pageIndex", 1);
				data.put("total", list.size());
				data.put("size", list.size());
			} else {
				data.put("records", null);
				data.put("pageIndex", 1);
				data.put("total", 0);
				data.put("size", 1);
			}
			GsonUtil.GsonObject(toResultJson("", data, true));

		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	/**
	 * 领取PT大爆炸礼金
	 */
	public String mobileGetPTBigBangBonus() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			if (StringUtils.isBlank(ptBigBangId)) {
				GsonUtil.GsonObject(toResultJson("[提示]请选择领取礼金！", false));
				return null;
			}

			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getPTBigBangBonus", new Object[] { ptBigBangId, getIp() }, String.class);
			if (result == null) {
				GsonUtil.GsonObject(toResultJson("领取成功！", true));
			} else {
				GsonUtil.GsonObject(toResultJson("操作失败：" + result, false));
			}

		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍候重试！", false));
		}
		return null;
	}

	private String sortname;
	private String sortorder;
	private String query;
	private String qtype;
	private String platform;

	// 额度记录
	public String mobileQueryCreditlogs() {

		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtil.isBlank(startDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]起始时间不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(endDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]结束时间不可为空！", false));
				return null;
			}

			try {
				sdf.parse(startDate);
				sdf.parse(endDate);
			} catch (Exception e) {
				GsonUtil.GsonObject(toResultJson("[提示]日期格式错误，请重新填写！", false));
				return null;
			}

			RecordInfo recordInfo = AxisUtil.getRecordInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getCreditLogList", new Object[] { startDate, endDate, user.getLoginname(), pageIndex, size },
					RecordInfo.class, Creditlogs.class);
			// RecordInfo recordInfo =
			// AxisUtil.getRecordInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+
			// "UserWebService", false), AxisUtil.NAMESPACE, "getCreditLogList",
			// new Object[] {
			// startDate, endDate, "wwwe68ph", page, rp},
			// RecordInfo.class,Creditlogs.class);
			//
			if (null == recordInfo) {
				GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("pageIndex", pageIndex);
				data.put("total", recordInfo.getLength());
				data.put("records", recordInfo.getDataList());

				String type = null;
				for (int i = 0, len = recordInfo.getDataList().size(); i < len; i++) {
					type = CreditChangeType.getText(recordInfo.getDataList().get(i).getType());
					if (StringUtils.isNotBlank(type)) {
						recordInfo.getDataList().get(i).setType(type);
					}
				}
				GsonUtil.GsonObject(toResultJson("查询成功！", data, true));

			}
		} catch (Exception e) {
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 提案类型
	public String mobileGetProposalType() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> data = new HashMap<String, Object>();

			for (ProposalType pt : ProposalType.values()) {
				data = new HashMap<String, Object>();
				data.put("value", pt.getCode());
				data.put("name", pt.getText());
				list.add(data);
			}

			GsonUtil.GsonObject(toResultJson("查询成功！", list, true));
		} catch (Exception e) {
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 下线提案
	public String mobileQuerySubProposal() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtil.isBlank(startDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]起始时间不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(endDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]结束时间不可为空！", false));
				return null;
			}
			if (proposalType == null) {
				proposalType = 502;
			}
			Page pageObj = AxisUtil
					.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
							AxisUtil.NAMESPACE,
							"searchsubuserProposal", new Object[] { startDate, endDate, user.getLoginname(),
									proposalType, account, pageIndex, size },
							Page.class, proposalType == 1000 ? Payorder.class : Proposal.class);
			// Page pageObj =
			// AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+
			// "UserWebService", false), AxisUtil.NAMESPACE,
			// "searchsubuserProposal", new Object[] {
			// startDate, endDate,
			// "wwwe68ph",proposalType,account, page, rp },
			// Page.class,proposalType==1000?Payorder.class:Proposal.class);

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			Map<String, Object> data = null;

			Payorder p = null;
			Proposal p2 = null;
			for (int i = 0, len = pageObj.getPageContents().size(); i < len; i++) {
				data = new HashMap<String, Object>();
				if (proposalType == 1000) {
					p = (Payorder) pageObj.getPageContents().get(i);
					data.put("loginname", p.getLoginname());
					data.put("tempCreateTime", p.getTempCreateTime());
					data.put("money", decimalFormat(p.getMoney()));
					data.put("gifTamount", decimalFormat("0"));
					data.put("type", "在线支付");
				} else {
					p2 = (Proposal) pageObj.getPageContents().get(i);
					data.put("loginname", p2.getLoginname());
					data.put("tempCreateTime", p2.getTempCreateTime());
					data.put("money", decimalFormat(p2.getAmount()));
					data.put("gifTamount", decimalFormat(p2.getGifTamount()));
					data.put("type", ProposalType.getText(p2.getType()));
				}
				list.add(data);
			}
			data = new HashMap<String, Object>();
			data.put("pageIndex", pageIndex);
			data.put("total", pageObj.getTotalRecords());
			data.put("records", list);

			data.put("tMoney", decimalFormat(pageObj.getStatics1()));
			data.put("tGifTamount", decimalFormat(pageObj.getStatics2()));

			GsonUtil.GsonObject(toResultJson("查询成功！", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 平台输赢
	public String mobileQueryPlatformDetails() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtil.isBlank(startDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]起始时间不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(endDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]结束时间不可为空！", false));
				return null;
			}
			Page pageObj = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"searchagprofit",
					new Object[] { user.getLoginname(), name, platform, startDate, endDate, pageIndex, size },
					Page.class, AgProfit.class);
			// Page pageObj =
			// AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
			// + "UserWebService", false), AxisUtil.NAMESPACE, "searchagprofit",
			// new Object[] { "wwwe68ph", name, platform, startDate, endDate,
			// page, rp }, Page.class, AgProfit.class);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("pageIndex", pageIndex);
			data.put("total", pageObj.getTotalRecords());
			data.put("records", pageObj.getPageContents());

			data.put("tAmount", decimalFormat(pageObj.getStatics1()));
			data.put("tBettotal", decimalFormat(pageObj.getStatics2()));

			GsonUtil.GsonObject(toResultJson("查询成功！", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 实时输赢
	public String mobileQueryAgentBetProfit() {
		try {

			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtil.isBlank(startDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]起始时间不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(endDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]结束时间不可为空！", false));
				return null;
			}

			Page pageObj = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectBetProfit",
					new Object[] { user.getLoginname(), startDate, endDate, platform, account, pageIndex, size },
					Page.class, PlatformData.class);
			// Page pageObj =
			// AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+
			// "UserWebService", false), AxisUtil.NAMESPACE, "selectBetProfit",
			// new Object[] {
			// "a_longfa123", startDate , endDate , platform , account ,page, rp
			// }, Page.class,PlatformData.class);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("pageIndex", pageIndex);
			data.put("total", pageObj.getTotalRecords());
			data.put("records", pageObj.getPageContents());

			data.put("tbet", decimalFormat(pageObj.getStatics1()));
			data.put("tProfit", decimalFormat(pageObj.getStatics2()));

			GsonUtil.GsonObject(toResultJson("查询成功！", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 下线会员
	public String mobileQueryAgentSubUserInfo() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtil.isBlank(startDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]起始时间不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(endDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]结束时间不可为空！", false));
				return null;
			}

			Page pageObj = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryInitSubUsers", new Object[] { user.getLoginname(), startDate, endDate, pageIndex, size },
					Page.class, Users.class);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> data;

			// Page pageObj =
			// AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
			// + "UserWebService", false), AxisUtil.NAMESPACE,
			// "queryInitSubUsers", new Object[] { "wwwe68ph",startDate,
			// endDate, page, rp }, Page.class, Users.class);
			// List<Map<String,Object>> list = new
			// ArrayList<Map<String,Object>>();
			// Map<String,Object> data;

			Users u = null;
			for (int i = 0, len = pageObj.getPageContents().size(); i < len; i++) {
				u = (Users) pageObj.getPageContents().get(i);
				data = new HashMap<String, Object>();
				data.put("loginname", u.getLoginname());
				data.put("flag", Constants.ENABLE.equals(u.getFlag()) ? "启用" : "禁用");
				data.put("credit", decimalFormat(u.getCredit()));
				data.put("tempCreateTime", u.getTempCreateTime());
				data.put("howToKnow", u.getHowToKnow());
				list.add(data);
			}
			data = new HashMap<String, Object>();
			data.put("pageIndex", pageIndex);
			data.put("total", pageObj.getTotalRecords());
			data.put("records", list);

			GsonUtil.GsonObject(toResultJson("查询成功！", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 佣金明细
	public String mobileQueryAgentCommission() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtils.isBlank(yearmonth)) {
				GsonUtil.GsonObject(toResultJson("[提示]年月不可为空！", false));
				return null;
			}
			if (!ymPattern2.matcher(yearmonth).find()) {
				GsonUtil.GsonObject(toResultJson("[提示]年月格式错误！", false));
				return null;
			}
			String[] yearmonths = yearmonth.split("-");

			Page pageObj = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryCommissionrecords",
					new Object[] { user.getLoginname(), yearmonths[0], yearmonths[1], pageIndex, size }, Page.class,
					Commissionrecords.class);
			// Page pageObj =
			// AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
			// + "UserWebService", false), AxisUtil.NAMESPACE,
			// "queryCommissionrecords", new Object[] {
			// "a_longfa123",yearmonths[0],yearmonths[1],page, rp},
			// Page.class,Commissionrecords.class);

			// Commissions
			// cmm=(Commissions)AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
			// + "UserWebService", false), AxisUtil.NAMESPACE, "getCommissions",
			// new Object[] {
			// user.getLoginname(),yearmonths[0],yearmonths[1]},
			// Commissions.class);

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> data;

			Commissionrecords c = null;
			for (int i = 0, len = pageObj.getPageContents().size(); i < len; i++) {
				c = (Commissionrecords) pageObj.getPageContents().get(i);
				data = new HashMap<String, Object>();
				data.put("yearmonth", c.getId().getYear() + "-" + c.getId().getMonth());
				data.put("loginname", c.getId().getLoginname());
				data.put("ximaAmount", decimalFormat(c.getXimaAmount()));
				data.put("firstDepositAmount", decimalFormat(c.getFirstDepositAmount()));
				data.put("agAmount", decimalFormat(c.getAgAmount()));
				data.put("otherAmount", decimalFormat(c.getOtherAmount()));
				data.put("remark", c.getRemark());
				list.add(data);
			}
			data = new HashMap<String, Object>();
			data.put("pageIndex", pageIndex);
			data.put("total", pageObj.getTotalRecords());
			data.put("records", list);
			// data.put("comm", cmm);
			data.put("tXima", pageObj.getStatics1());
			data.put("tAg", decimalFormat(pageObj.getStatics2()));
			data.put("tOther", decimalFormat(pageObj.getStatics3()));

			GsonUtil.GsonObject(toResultJson("查询成功！", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 查询佣金列表
	public String mobileQueryPtCommission() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtil.isBlank(startDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]起始时间不可为空！", false));
				return null;
			}
			if (StringUtil.isBlank(endDate)) {
				GsonUtil.GsonObject(toResultJson("[提示]结束时间不可为空！", false));
				return null;
			}

			Page pageObj = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"searchPtCommissions", new Object[] { user.getLoginname(), startDate, endDate, pageIndex, size },
					Page.class, PtCommissions.class);
			// Page pageObj =
			// AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
			// + "UserWebService", false), AxisUtil.NAMESPACE,
			// "searchPtCommissions", new Object[] {"wwwe68ph" , startDate ,
			// endDate ,page, rp }, Page.class,PtCommissions.class);

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> data;
			PtCommissions p = null;

			for (int i = 0, len = pageObj.getPageContents().size(); i < len; i++) {
				p = (PtCommissions) pageObj.getPageContents().get(i);
				data = new HashMap<String, Object>();
				data.put("platform", p.getId().getPlatform());
				data.put("createdate", p.getId().getCreatedate());
				data.put("profitall", decimalFormat(p.getProfitall()));
				data.put("couponfee", decimalFormat(p.getCouponfee()));
				data.put("ximafee", decimalFormat(p.getXimafee()));
				data.put("amount", decimalFormat(p.getAmount()));
				data.put("flag", p.getFlag());
				data.put("tempExcuteTime", p.getTempExcuteTime());
				list.add(data);

			}
			data = new HashMap<String, Object>();
			data.put("pageIndex", pageIndex);
			data.put("total", pageObj.getTotalRecords());
			data.put("records", list);
			data.put("tXimafee", decimalFormat(pageObj.getStatics1()));

			GsonUtil.GsonObject(toResultJson("查询成功！", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 查询新闻动态
	 * 
	 * @return
	 */
	public String mobileGetAllNews() {

		try {
			List<AnnouncementVO> list1 = new ArrayList<AnnouncementVO>();
			try {
				int end = (Integer) AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"totalNewsCount", new Object[] {}, Integer.class);
				// 新闻动态
				list1 = AxisUtil.getListObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"querybyPage", new Object[] { 0, end }, AnnouncementVO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			Map<String, String> data = null;

			for (AnnouncementVO announcementVO : list1) {
				data = new HashMap<String, String>();
				data.put("id", String.valueOf(announcementVO.getId()));
				data.put("title", announcementVO.getTitle());
				data.put("content", announcementVO.getContent());
				data.put("time", announcementVO.getCreatetime());
				data.put("type", "news");
				result.add(data);
			}

			Collections.sort(result, new Comparator<Map<String, String>>() {
				@Override
				public int compare(Map<String, String> map1, Map<String, String> map2) {
					try {
						return Integer.valueOf(map2.get("time").replaceAll("-", ""))
								- Integer.valueOf(map1.get("time").replaceAll("-", ""));
					} catch (Exception e) {
						e.printStackTrace();
						return -1;
					}
				}
			});

			GsonUtil.GsonObject(toResultJson("查询成功！", result, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 支付宝语音验证
	 * 
	 * @return
	 */
	public String mobileSendAlipayPhoneVoiceCode() {

		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			if (StringUtils.isBlank(bankName)) {
				GsonUtil.GsonObject(toResultJson("请选择要绑定的支付机构！", false));
				return null;
			}
			if (StringUtils.isNotBlank(bankName) && !StringUtils.equals(bankName, "支付宝")) {
				GsonUtil.GsonObject(toResultJson("只是支付宝绑定需要验证！", false));
				return null;
			}

			// 判断是否有绑定的支付宝账号
			Userbankinfo userbankinfo = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getUserbankinfo", new Object[] { user.getLoginname(), "支付宝" }, Userbankinfo.class);

			if (null != userbankinfo && -1000 != userbankinfo.getFlag()) {
				GsonUtil.GsonObject(toResultJson("你已经绑定支付宝账号！", false));
				return null;
			}

			// 触点
			Status status = new TouClick().check(checkAddress, sid, checkKey, websiteKey, privateKey);
			if (status == null || status.getCode() != 0) {
				GsonUtil.GsonObject(toResultJson("[提示]点触验证码错误！", false));
				return null;
			}

			String code = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if (StringUtils.isBlank(code)) {
				code = RandomStringUtils.randomNumeric(4);
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
			}

			sendVoiceToPhone(user.getLoginname(), user.getPhone(), code);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 支付宝短信验证
	 * 
	 * @return
	 */
	public String mobileSendAlipayPhoneSmsCode() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			if (StringUtils.isBlank(bankName)) {
				GsonUtil.GsonObject(toResultJson("请选择要绑定的支付机构！", false));
				return null;
			}
			if (StringUtils.isNotBlank(bankName) && !StringUtils.equals(bankName, "支付宝")) {
				GsonUtil.GsonObject(toResultJson("只是支付宝绑定需要验证！", false));
				return null;
			}

			// 判断是否有绑定的支付宝账号
			Userbankinfo userbankinfo = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getUserbankinfo", new Object[] { user.getLoginname(), "支付宝" }, Userbankinfo.class);

			if (null != userbankinfo && -1000 != userbankinfo.getFlag()) {
				GsonUtil.GsonObject(toResultJson("你已经绑定支付宝账号！", false));
				return null;
			}

			// 触点
			Status status = new TouClick().check(checkAddress, sid, checkKey, websiteKey, privateKey);
			if (status == null || status.getCode() != 0) {
				GsonUtil.GsonObject(toResultJson("[提示]点触验证码错误！", false));
				return null;
			}

			String code = RandomStringUtils.randomNumeric(4);
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if (StringUtils.isNotBlank(sessionCode)) {
				code = sessionCode;
			} else {
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
			}

			sendSmsToPhone(user.getPhone(), code);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 电话回播
	 * 
	 * @return
	 */
	public String mobileMakeCall() {

		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"makeCall", new Object[] { user.getLoginname(), phone }, String.class);

			GsonUtil.GsonObject(toResultJson(msg, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 取得体育投注网址
	 * 
	 * @return
	 */
	public String mobileGetSportGame() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏 --- 改为未登录也可以进入游戏
			if (user == null) {

				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url",
						"http://sb.e68ph.net/Sportsbook/Launch?t=&l=&g=CHS&tz=GMT%2B08%3A00&mid=F5623D1F57F64952iofWkynFF%2BDRdxFz78p8Vw%3D%3D");
				GsonUtil.GsonObject(toResultJson("尚未登录！", data, true));
				return null;
			}
			// 代理不能登录邮箱
			if (user.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩！", false));
				return null;
			}
			// 获取游戏链接地址
			String sportUrl = SportBookUtils.getSportBookUrl(user.getPostcode(), user.getLoginname());
			if (StringUtils.isNotBlank(sportUrl)) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", sportUrl);
				GsonUtil.GsonObject(toResultJson("已登录，可进行投注！", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 取得KG快乐彩的URL
	 * 
	 * @return
	 */
	public String mobileGetKENO2() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("请先登录，再登录游戏！", false));
				return null;
			}

			String loginid = AxisSecurityEncryptUtil.generateLoginKenoID(user.getLoginname());
			String xml = AxisSecurityEncryptUtil.keno2CheckOrCreateGameAccount(user.getLoginname(), user.getAliasName(),
					getRequest().getRemoteAddr(), loginid, "");
			KenoResponseBean bean = DocumentParser.parseKenologinResponseRequest(xml);
			if (bean == null || StringUtils.isBlank(bean.getName())) {
				GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
				return null;
			}
			if (bean.getName().equals("Link")) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", bean.getValue());
				GsonUtil.GsonObject(toResultJson("成功！", data, true));
			} else {
				GsonUtil.GsonObject(toResultJson(bean.getValue(), false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;

	}

	/**
	 * 访问代理管理页面
	 *
	 * @return
	 */
	public String mobileAgentReport() {

		// 查询代理的老虎机佣金额度
		try {

			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("请先登录，再登录游戏！", false));
				return null;
			}
			// 用户不能登录
			if (user.getRole().equals("MONEY_CUSTOMER")) {
				GsonUtil.GsonObject(toResultJson("一般用户无法访问！", false));
				return null;
			}
			Userstatus userstatus = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getAgentSlot", new Object[] { user.getLoginname() }, Userstatus.class);
			if (userstatus.getSlotaccount() == null) {
				userstatus.setSlotaccount(0.0);
			}

			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"agentMonthlyReport", new Object[] { user.getLoginname() }, String.class);

			Map<String, String> report = new Gson().fromJson(result, new TypeToken<Map<String, String>>() {
			}.getType());
			report.put("slotAccount", String.valueOf(userstatus.getSlotaccount()));

			GsonUtil.GsonObject(toResultJson("成功！", report, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	// 存送优惠券提交
	public void mobileTransferInforCoupon() {

		try {

			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);

			if (user == null) {

				GsonUtil.GsonObject(toResultJson("请登录后在进行操作！", false));
				return;
			}

			String msg = SynchronizedUtil.getInstance().coupon(user.getLoginname(), couponCode, platform, couponRemit,
					"/self/depositCoupon/submit");

			GsonUtil.GsonObject(toResultJson(msg, true));
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍后重试！", false));
		}
	}

	// 红包优惠券提交
	public void mobileTransferInforRedCoupon() {

		try {

			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);

			if (user == null) {

				GsonUtil.GsonObject(toResultJson("请登录后在进行操作！", false));
				return;
			}

			String msg = SynchronizedUtil.getInstance().coupon(user.getLoginname(), couponCode, platform, null,
					"/self/redEnvelope/submit");

			GsonUtil.GsonObject(toResultJson(msg, true));
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙，请稍后重试！", false));
		}
	}

	/**
	 * 领取优惠卷
	 * 
	 * @return
	 *//*
		 * public String mobileTransferInforCoupon(){
		 * SynchronizedUtil.getInstance().mobileTransferInforCoupon(couponRemit,
		 * platform,couponCode); return null; }
		 */

	/**
	 * 领取红包优惠卷
	 * 
	 * @return
	 *//*
		 * public void mobileTransferInforRedCoupon() {
		 * SynchronizedUtil.getInstance().transferInforRedCouponM(platform,
		 * couponCode); }
		 */

	/**
	 * 查询签到余额
	 */
	public String mobileQuerySignMoney() {
		try {
			Users customer = (Users) getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			String info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"querySignAmount", new Object[] { customer.getLoginname() }, String.class);
			if (!StringUtil.isEmpty(info)) {
				GsonUtil.GsonObject(toResultJson(info, true));
			} else {
				GsonUtil.GsonObject(toResultJson("操作失败！", false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 签到
	 */
	public String mobileDoSign() {
		SynchronizedUtil.getInstance().dosignM();
		return null;
	}

	/**
	 * 签到状态
	 */
	public String mobileSignStatus() {
		try {
			Users customer = (Users) getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			Boolean isTodaySign = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"isTodaySign", new Object[] { customer.getLoginname() }, Boolean.class);
			Boolean isYesterdaySign = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"isYesterdaySign", new Object[] { customer.getLoginname() }, Boolean.class);
			// String signMoney =
			// AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2+
			// "UserWebService", false), AxisUtil.NAMESPACE, "querySignAmount",
			// new Object[]{customer.getLoginname()}, String.class);
			SignAmount signAmount = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"querySignAmountObj", new Object[] { customer.getLoginname() }, SignAmount.class);
			Map<String, Object> data = new HashMap<String, Object>();

			// 判断签到是否中断
			if (isYesterdaySign || isTodaySign || signAmount.getContinuesigncount() == 0) {
				data.put("signContinue", "true");
			} else {
				data.put("signContinue", "false");
			}

			data.put("status", String.valueOf(isTodaySign));
			data.put("money", money);
			data.put("signMoney", signAmount.getAmountbalane());
			data.put("continueSignCount", signAmount.getContinuesigncount());

			GsonUtil.GsonObject(toResultJson("", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 签到奖金转入游戏平台
	 */
	public String mobileTransferInforSign() {
		try {

			if (!(money).matches("^\\d*[1-9]\\d*$")) {
				GsonUtil.GsonObject(toResultJson("转账金额必须为整数！", false));
			}
			SynchronizedUtil.getInstance().transferInforSignM(platform, Integer.parseInt(money));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 获取站内信
	 */
	// public String mobileQueryEmail(){
	// try {
	// Users user = (Users) getCustomerFromSession();
	// if (user == null) {
	// GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
	// return null;
	// }
	//
	// Integer agent =
	// user.getRole().equalsIgnoreCase("AGENT")?8:user.getLevel();
	// Messages msg = new Messages();
	// msg.setPageNo(pageIndex);
	// msg.setPageSize(size);
	// guestBookService.getMessagesByUser(user.getLoginname(), agent, msg);
	// Integer unreadCount =
	// guestBookService.getUnReadMessageCount(user.getLoginname(),agent);
	//
	// Map<String,Object> data = new HashMap<String,Object>();
	// data.put("records", msg.getMsgList());
	// data.put("pageIndex", msg.getPageNo());
	// data.put("total", msg.getCount());
	// data.put("size", msg.getPageSize());
	// data.put("unreadCount", unreadCount);
	//
	// GsonUtil.GsonObject(toResultJson("成功",data,true));
	// } catch (Exception e) {
	// e.printStackTrace();
	// GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
	// }
	// return null ;
	// }

	/**
	 * 阅读站内信 并回传未读信件数量
	 *
	 */
	// public String mobileReadEmail(){
	// try {
	// Users user = (Users) getCustomerFromSession();
	// if (user == null) {
	// GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！",false));
	// return null;
	// }
	//
	// Guestbook msg = (Guestbook) guestBookService.get(Guestbook.class,
	// emailId);
	// if(msg == null){
	// GsonUtil.GsonObject(toResultJson("[提示]信件不存在！",false));
	// return null;
	// }
	//
	// if(msg.getUserstatus().intValue()==0 && msg.getUsername() != null &&
	// msg.getUsername().equals(user.getLoginname())){
	// //个人未读站内信，标记为已读
	// msg.setUserstatus(1);
	// guestBookService.update(msg);
	// }else if(msg.getUsername() == null){
	// //群发站内信，记录已读
	// if(!guestBookService.isRead4PublicMsg(emailId, user.getLoginname())){
	// ReadedMsg readedMsg = new ReadedMsg(emailId, user.getLoginname(), new
	// Date());
	// guestBookService.save(readedMsg);
	// }
	// }
	// //查询未读邮件
	// Integer agent =
	// user.getRole().equalsIgnoreCase("AGENT")?8:user.getLevel();
	// Integer unreadCount =
	// guestBookService.getUnReadMessageCount(user.getLoginname(),agent);
	//
	// Map<String,Object> data = new HashMap<String,Object>();
	// data.put("unreadCount", unreadCount);
	// data.put("content", msg.getContent());
	// GsonUtil.GsonObject(toResultJson("已阅读！",data,true));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！",false));
	// }
	// return null;
	// }

	/**
	 * 未读信件数量
	 */
	public String mobileUnreadEmailCount() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			// 查询未读邮件
			Integer agent = user.getRole().equalsIgnoreCase("AGENT") ? 8 : user.getLevel();
			Integer unreadCount = 0;// guestBookService.getUnReadMessageCount(user.getLoginname(),agent);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("unreadCount", unreadCount);
			GsonUtil.GsonObject(toResultJson("", data, true));

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 取得支付宝扫码帐户
	 * 
	 * @return
	 */
	public String mobileGetAlipayAccount() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			AlipayAccount alipayAccount = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"getAlipayAccount", new Object[] { user.getLoginname(), 0 }, AlipayAccount.class);
			Map<String, Object> data = new HashMap<String, Object>();
			if (alipayAccount == null) {
				data.put("bind", false);
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else {
				Bankinfo zfbBank = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getZfbBankRedirectInfo", new Object[] { user.getLoginname(), true }, Bankinfo.class);
				if (StringUtils.isNotBlank(zfbBank.getMassage())) {
					data.put("auth", false);
				} else {
					data.put("auth", true);
				}

				data.put("bind", true);
				data.put("account", StringUtil.formatStar(alipayAccount.getAlipayAccount(), 0.7, 0));
				data.put("image", zfbBank.getZfbImgCode());

				GsonUtil.GsonObject(toResultJson(zfbBank.getMassage(), data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 绑定支付宝扫码帐户
	 * 
	 * @return
	 */
	public String mobileSaveAlipayAccount() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			if (StringUtils.isBlank(password)) {
				GsonUtil.GsonObject(toResultJson("密码不能为空！", false));
				return null;
			}
			if (StringUtils.isBlank(account)) {
				GsonUtil.GsonObject(toResultJson("支付宝存款账号不能为空！", false));
				return null;
			}

			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"saveAlipayAccount", new Object[] { user.getLoginname(), password, account }, String.class);

			GsonUtil.GsonObject(toResultJson(msg, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("更新账号失败" + e.getMessage(), false));
		}
		return null;
	}

	/**
	 * 查询积分总余额
	 */
	public String mobileQueryPoints() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			TotalPoint info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryPoints", new Object[] { user.getLoginname() }, TotalPoint.class);

			Double ratio = 500.0;// 默认新会员
			if (user.getLevel() == 1) {
				ratio = 400.0;
			} else if (user.getLevel() == 2) {
				ratio = 325.0;
			} else if (user.getLevel() == 3) {
				ratio = 280.0;
			} else if (user.getLevel() == 4) {
				ratio = 245.0;
			} else if (user.getLevel() == 5) {
				ratio = 220.0;
			} else if (user.getLevel() == 6) {
				ratio = 100.0;
			}
			Map<String, Object> data = new HashMap<String, Object>();

			if (info != null) {
				data.put("oldPoint", info.getOldtotalpoints());
				data.put("nowPoint", info.getTotalpoints());
				data.put("ratio", ratio);

				GsonUtil.GsonObject(toResultJson("获取成功", data, true));
			} else {
				data.put("oldPoint", 0);
				data.put("nowPoint", 0);
				data.put("ratio", ratio);
				GsonUtil.GsonObject(toResultJson("获取成功", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 积分总转账
	 */
	public String mobileTransferPoints() {
		try {
			if (!(money).matches("^\\d*[1-9]\\d*$")) {
				GsonUtil.GsonObject(toResultJson("转账金额必须为整数！", false));
			}
			SynchronizedUtil.getInstance().transferInforPointM(Double.parseDouble(money));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	private static ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String mobileGetYouhuiData() {

		String loginName = null;
		Integer level = null;

		try {

			Users customer = getCustomerFromSession();
			loginName = customer.getLoginname();
			level = customer.getLevel();
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("[提示]登录已过期，请重新登录！");
			return null;
		}

		try {

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("loginName", loginName);
			paramMap.put("conditionType", "2");

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

				Map<String, String> resultMap = gson.fromJson(responseString, new TypeToken<Map<String, String>>() {
				}.getType());
				String responseData = resultMap.get("responseData");

				responseData = app.util.AESUtil.getInstance().decrypt(responseData);

				resultMap = mapper.readValue(responseData, Map.class);
				JSONArray jsonArray = JSONArray.fromObject(resultMap.get("data"));
				String jsonData = jsonArray.toString();

				List<PreferentialConfig> list = gson.fromJson(jsonData, new TypeToken<List<PreferentialConfig>>() {
				}.getType());

				List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
				Map<String, Object> data;

				if (null != list && !list.isEmpty()) {

					for (PreferentialConfig youhui : list) {

						data = new HashMap<String, Object>();

						data.put("id", youhui.getId());
						data.put("platformId", youhui.getPlatformId());
						data.put("titleId", youhui.getTitleId());
						data.put("titleName", youhui.getTitleName());
						data.put("name", youhui.getAliasTitle());
						data.put("percent", youhui.getPercent());
						data.put("limitMoney", youhui.getLimitMoney());
						data.put("betMultiples", youhui.getBetMultiples());

						mapList.add(data);
					}
				}

				GsonUtil.GsonObject(toResultJson("", mapList, true));
			}
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
		}

		return null;
	}

	private String id;
	private String platformId;
	private String titleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	/**
	 * 获取自助优惠Object
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public String mobileDoYouhui() {

		String loginName = null;
		String postCode = null;

		try {

			Users customer = getCustomerFromSession();
			loginName = customer.getLoginname();
			postCode = customer.getPostcode();
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("[提示]登录已过期，请重新登录！", false));
			return null;
		}

		try {

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("product", "ld");
			paramMap.put("loginName", loginName);
			paramMap.put("platform", platformId);
			paramMap.put("id", id);
			paramMap.put("type", titleId);
			paramMap.put("amount", money);
			paramMap.put("sid", postCode);
			paramMap.put("channel", "手机网页");

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

			PostMethod method = new PostMethod(url + "/self/deposit/submit");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			} else {

				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				responseString = resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);

				GsonUtil.GsonObject(toResultJson(resultMap.get("message"), true));
			}
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("[提示]系统繁忙，请稍后重试！", false));
		}

		return null;
	}

	/**
	 * 查询推荐好友奖金余额
	 * 
	 * @return
	 */
	public String mobileQueryFriendBonue() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}

			String friendurl = user.getId() + "";
			String info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryFriendbonus", new Object[] { user.getLoginname() }, String.class);

			if (StringUtil.isNotBlank(info)) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("money", info);
				data.put("url", friendurl);
				GsonUtil.GsonObject(toResultJson("", data, true));
			} else {
				GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 推存奖金转换
	 * 
	 * @return
	 */
	public String mobileTransferFriend() {
		return SynchronizedUtil.getInstance().transferInforFriendM(platform, Double.parseDouble(money));
	}

	/**
	 * 检查微信支付是否启用
	 * 
	 * @return
	 */
	public String mobileIsOpenWeixin() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			Const constPay = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDeposit", new Object[] { "乐富微信" }, Const.class);
			if (constPay == null) {
				GsonUtil.GsonObject(toResultJson("该支付方式不存在！", false));
				return null;
			}
			// 判断在线支付是否在维护
			if (constPay.getValue().equals("0")) {
				GsonUtil.GsonObject(toResultJson("微信支付正在维护！", false));
				return null;
			}
			GsonUtil.GsonObject(toResultJson("微信支付服务正常！", true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 查询系统配置表的内容
	 * 
	 * @return
	 */
	public String mobileSystemConfig() {
		try {
			String info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkSystemConfig", new Object[] { typeNo, itemNo, "否" }, String.class);
			if (StringUtil.isNotEmpty(info)) {
				GsonUtil.GsonObject(toResultJson(info, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 点卡支付
	 */
	public void mobileGetDCardPayWork() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			Const constPay = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDeposit", new Object[] { "智付点卡1" }, Const.class);

			if (constPay == null) {
				GsonUtil.GsonObject(toResultJson("点卡支付正在维护！", false));
				return;
			}
			if ("0".equals(constPay.getValue())) {
				GsonUtil.GsonObject(toResultJson("点卡支付正在维护！", false));
				return;
			}
			// GsonUtil.GsonObject(toResultJson("点卡支付正在维护！",false));
			GsonUtil.GsonObject(toResultJson("正常服务中！", true));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 点卡支付
	 */
	public void mobileDCardPay() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			DCardPaymentUtil.getInstance().pay(user.getLoginname(), card_code, card_no, card_password, money);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 获取闯关报名情况&查询金额
	 */
	public void mobileGetEmigratedStatus() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}

			String apply = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getEmigratedApply", new Object[] { user.getLoginname() }, String.class);

			String money = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryEmigratedbonus", new Object[] { user.getLoginname() }, String.class);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("money", money);
			if (StringUtils.isNotEmpty(apply)) {
				data.put("isApply", true);
				data.put("message", apply);
			} else {
				data.put("isApply", false);
				data.put("message", "今日尚未报名闯关");
			}

			GsonUtil.GsonObject(toResultJson("", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 报名闯关活动
	 */
	public void mobileApplyEmigrated() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}

			String info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"doEmigratedApply", new Object[] { user.getLoginname(), emigratedeLevel }, String.class);
			GsonUtil.GsonObject(toResultJson(info, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 领取闯关奖励
	 */
	public void mobileGetEmigratedAward() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
				GsonUtil.GsonObject(toResultJson("请于10点以后再来领取奖励！", false));
				return;
			}

			String info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"doEmigrated", new Object[] { user.getLoginname() }, String.class);
			String money = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryEmigratedbonus", new Object[] { user.getLoginname() }, String.class);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("money", money);

			GsonUtil.GsonObject(toResultJson(info, data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 闯关奖励转账
	 */
	public void mobileTransferEmigrated() {
		SynchronizedUtil.getInstance().transferInforEmigratedM(platform, Double.parseDouble(money));
	}

	/**
	 * 体验金 检查使用者资讯是否重复
	 */
	public void mobileCheckSameInfo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
					AxisUtil.NAMESPACE, "getUser", new Object[] { user.getLoginname() }, Users.class);

			// 取消玩家是否危险的限制，根据同IP或者同姓名下的三个月内是否领过
			/*
			 * if (user.getWarnflag().equals(2)) {
			 * GsonUtil.GsonObject(toResultJson(
			 * "非常抱歉！您注册信息有重复,暂无法领取体验金,您可申请其它优惠。",false)); return; }
			 */

			String youHuiFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { "PT8元自助优惠" }, String.class);
			if (!youHuiFlag.equals("1")) {
				GsonUtil.GsonObject(toResultJson("PT8元自助优惠维护中...", false));
				return;
			}

			// 判断是否有重复的信息(真实姓名、手机号、ip、email、cpu、银卡关联卡 )
			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"haveSameInfo", new Object[] { user.getLoginname() }, String.class);
			if (null == result) {

				Integer numbers = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getCardNums", new Object[] { user.getLoginname() }, Integer.class);
				if (0 == numbers) {
					GsonUtil.GsonObject(toResultJson("请绑定银行卡！", false));
					return;
				}

				Const smsFlag = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"selectDeposit", new Object[] { "短信反转验证" }, Const.class);
				if (null != smsFlag && smsFlag.getValue().equals("1")) {
					GsonUtil.GsonObject(toResultJson("successSms", true));
				} else {
					GsonUtil.GsonObject(toResultJson("success", true));
				}
			} else {
				GsonUtil.GsonObject(toResultJson(result, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 体验金取得验证码
	 */
	public void mobileGetPhoneAndCode() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"getPhoneAndValidateCode", new Object[] { user.getLoginname() }, String.class);
			GsonUtil.GsonObject(toResultJson("", msg, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 体验金验证码验证
	 */
	public void mobileCheckValidCode() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			if (StringUtils.isBlank(imageCode)) { // 常规短信验证
				GsonUtil.GsonObject(toResultJson("请输入手机验证码！", false));
				return;
			}
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if (StringUtils.isBlank(sessionCode)) {
				GsonUtil.GsonObject(toResultJson("验证码失效,重新获取！", false));
				return;
			}
			if (sessionCode.equals(imageCode)) {
				GsonUtil.GsonObject(toResultJson("验证码正确！", true));
			} else {
				GsonUtil.GsonObject("验证码不正确");
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 体验金验证短信回传
	 */
	public void mobileCheckPhoneCode() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}

			// 短信反转验证
			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"checkValidateCode", new Object[] { user.getLoginname() }, String.class);
			if (msg.contains("success")) {
				getHttpSession().setAttribute(Constants.SMSVALIDATE, "success");
				GsonUtil.GsonObject(toResultJson("验证码正确！", true));
			} else {
				GsonUtil.GsonObject(toResultJson(msg, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 判断是否有重复的银行卡
	 */
	public void mobileCheckRepeatBankCards() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			Boolean flag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"haveRepeatCards", new Object[] { user.getLoginname() }, Boolean.class);
			if (flag) {
				GsonUtil.GsonObject(toResultJson("重复银行卡信息！", false));
				return;
			}
			Integer numbers = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getCardNums", new Object[] { user.getLoginname() }, Integer.class);
			if (0 == numbers) {
				GsonUtil.GsonObject(toResultJson("请绑定银行卡！", false));
			}

			GsonUtil.GsonObject(toResultJson("已绑定银行卡！", true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
	}

	/**
	 * 体验金 发送验证码给用户手机
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void mobileExperienceSendSmsCode() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}

			// 触点
			Status status = new TouClick().check(checkAddress, sid, checkKey, websiteKey, privateKey);
			if (status == null || status.getCode() != 0) {
				GsonUtil.GsonObject(toResultJson("[提示]点触验证码错误！", false));
				return;
			}

			String code = RandomStringUtils.randomNumeric(4);
			String sessionCode = (String) getHttpSession().getAttribute(Constants.SESSION_PHONECODE);
			if (StringUtils.isNotBlank(sessionCode)) {
				code = sessionCode;
			} else {
				getHttpSession().setAttribute(Constants.SESSION_PHONECODE, code);
			}
			// 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDeposit", new Object[] { "体验金短信" }, Const.class);
			if (constPay == null || constPay.getValue().equals("0")) {
				GsonUtil.GsonObject(toResultJson("短信暂不可使用！", false));
				return;
			}
			sendSmsToPhone(user.getPhone(), code);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙", false));
		}
		return;
	}

	/**
	 * 提交PT8元自助
	 */
	public void mobileGetExperience() {
		SynchronizedUtil.getInstance().commitPT8SelfM(imageCode, platform);
	}

	/**
	 * NTwo财务中心自动登入,利用account跟ticket验证LoginInfo
	 * 
	 * @return
	 */
	public String mobileTicketLogin() {
		// 保证执行一次，事务完整
		try {
			Users sessionUser = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if (sessionUser != null && sessionUser.getLoginname().equals(account)) {
				GsonUtil.GsonObject(toResultJson("登录成功！", true));
				return null;
			}
			if (StringUtil.isBlank(account)) {
				GsonUtil.GsonObject(toResultJson("[提示]账号不能为空！", false));
				return null;
			}
			/*
			 * if (StringUtil.isBlank(password)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]密码不能为空！",false)); return
			 * null; }
			 * 
			 * if (StringUtil.isBlank(imageCode)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]验证码不能为空！",false)); return
			 * null; }
			 * 
			 * String code = (String)
			 * getHttpSession().getAttribute(Constants.SESSION_RANDID); if
			 * (StringUtil.isBlank(code)||!code.equals(imageCode)) {
			 * GsonUtil.GsonObject(toResultJson("[提示]验证码错误！",false)); return
			 * null; }
			 */
			// 验证装置ID
			// String ioBB = "ioBB";

			// 重新生成验证码 防止重复提交
			getHttpSession().setAttribute(Constants.SESSION_RANDID, null);

			IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
			String remoteIp = getIpAddr();
			String city = "";
			String temp = remoteIp != null ? seeker.getAddress(remoteIp) : "";
			if (null != temp && !"CZ88.NET".equals(temp)) {
				city = temp;
			}

			account = account.toLowerCase().trim(); // 用户名都改成小写
			// LoginInfo info =
			// AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
			// + "UserWebService", false), AxisUtil.NAMESPACE, "login", new
			// Object[] { account, password, remoteIp, city,
			// ClientInfo.getOSName(getRequest().getHeader("user-agent")),
			// ioBB}, LoginInfo.class);
			LoginInfo info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"nTwoTicketlogin", new Object[] { account, ticket, remoteIp, city,
							ClientInfo.getOSName(getRequest().getHeader("user-agent")) },
					LoginInfo.class);
			if (info == null || info.getUser() == null || info.getSucFlag() != 1) {
				if (null != info && null == info.getUser() && null == info.getSucFlag()) {
					String msg = info.getMsg();
					if (msg != null && !msg.equals("")) {
						GsonUtil.GsonObject(toResultJson(info.getMsg(), false));
					} else {
						GsonUtil.GsonObject(toResultJson("用户不存在！", false));
					}
				} else {
					GsonUtil.GsonObject(toResultJson("登录异常，请重新登录！", false));
				}
				return null;
			}

			String trycode = EncryptionUtil.encryptPassword(account + "e68" + "ruofgergiowqnr8342047tujtgvasw2q0e38");
			getHttpSession().setAttribute("trycode", trycode);
			Users olduser = info.getUser();
			if (StringUtil.isBlank(olduser.getLastLoginIp())) {
				getHttpSession().setAttribute("ip", olduser.getLastLoginIp());
				String lastcity = seeker.getAddress(olduser.getLastLoginIp());
				if (null != lastcity && !"CZ88.NET".equals(lastcity)) {
					getHttpSession().setAttribute("city", lastcity);
				} else {
					getHttpSession().setAttribute("city", "");
				}
			} else {
				getHttpSession().setAttribute("ip", "");
				getHttpSession().setAttribute("city", "");
			}

			if (StringUtil.isBlank(olduser.getTempLastLoginTime())) {
				getHttpSession().setAttribute("time", olduser.getTempLastLoginTime());
			} else {
				getHttpSession().setAttribute("time", "");
			}
			// getHttpSession().setAttribute(Constants.PT_SESSION_USER,
			// password);
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, olduser);
			getHttpSession().removeAttribute("loginNum");

			try {
				long timestamp = System.currentTimeMillis();
				String hashCode = Live800Encode.getMD5Encode(URLEncoder.encode(
						olduser.getLoginname() + olduser.getLoginname() + "" + timestamp + Constants.LIVE800KEY,
						"UTF-8"));
				String info4Live800 = URLEncoder.encode("userId=" + olduser.getLoginname() + "&name="
						+ olduser.getLoginname() + "&memo=&hashCode=" + hashCode + "&timestamp=" + timestamp, "UTF-8");
				log.info("###################初始化live800信任信息参数:" + info4Live800);
				getHttpSession().setAttribute(Constants.LIVE800INFOVALUE, info4Live800);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			/*
			 * if(StringUtils.equals(account, password)){
			 * log.info("账号密码一样，提醒玩家修改密码");
			 * getHttpSession().setAttribute(Constants.NEEDMODIFY, "1"); }else{
			 * getHttpSession().setAttribute(Constants.NEEDMODIFY, "0"); }
			 */
			GsonUtil.GsonObject(toResultJson("登录成功！", true));

		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("登录异常，请重新登录！", false));
		}
		return null;
	}

	/**
	 * mwg手机登录
	 * 
	 * @return
	 */
	public void mwgLogin() {
		Users customer = null;
		ReturnInfo ri = new ReturnInfo();
		try {
			customer = getCustomerFromSession();
			if (customer == null) {
				ri.setCode("-1");
				ri.setMsg("请登录后，在进行操作");
				GsonUtil.GsonObject(ri);
				return;
			}
			String loginUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"mwgLogin", new Object[] { customer.getLoginname(), "", 2 }, String.class);// 手机登录
			ri.setCode("0");
			ri.setMsg("");
			ri.setData(loginUrl);
			GsonUtil.GsonObject(ri);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			ri.setCode("-2");
			ri.setMsg("系统异常请稍后再试！");
		}
		GsonUtil.GsonObject(ri);
		return;
	}

	/**
	 * 登录TTG或者创建帐号
	 */
	public void mobileLoginTTG() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}
			String info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"loginttg", new Object[] { user.getLoginname() }, String.class);
			if (!StringUtil.isEmpty(info)) {
				getHttpSession().setAttribute("TTplayerhandle", info);
				GsonUtil.GsonObject(toResultJson(info, true));
			} else {
				GsonUtil.GsonObject(toResultJson("进入游戏失败", false));
				getHttpSession().removeAttribute("TTplayerhandle");
			}

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙", false));
			getHttpSession().removeAttribute("TTplayerhandle");
		}
	}

	/**
	 * 登录dt或者创建帐号
	 */
	public void mobileloginDT() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);

			// 检测用户是否登录
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return;
			}

			String password = (String) getHttpSession().getAttribute(Constants.PT_SESSION_USER);
			log.info(password);
			if (StringUtil.isBlank(password)) {
				password = getPassword();
			}

			log.info(password);
			DtVO info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"loginDT", new Object[] { user.getLoginname(), password }, DtVO.class);

			log.info(info);
			if (null != info && null != info.getSlotKey()) {
				getHttpSession().setAttribute(Constants.SESSION_DT_SLOTKEY, info.getSlotKey());
				getHttpSession().setAttribute(Constants.SESSION_DT_GAMEURL, info.getGameurl());
				getHttpSession().setAttribute("referWebsite",
						this.getRequest().getScheme() + "://" + this.getRequest().getServerName());
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("key", info.getSlotKey());
				data.put("url", info.getGameurl());
				data.put("referWebsite", this.getRequest().getScheme() + "://" + this.getRequest().getServerName());
				GsonUtil.GsonObject(toResultJson("success", data, true));
			} else {
				getHttpSession().removeAttribute(Constants.SESSION_DT_SLOTKEY);
				getHttpSession().removeAttribute(Constants.SESSION_DT_GAMEURL);
				GsonUtil.GsonObject(toResultJson("系统繁忙", false));
			}

		} catch (Exception e) {
			e.printStackTrace();
			getHttpSession().removeAttribute(Constants.SESSION_DT_SLOTKEY);
			getHttpSession().removeAttribute(Constants.SESSION_DT_GAMEURL);
			GsonUtil.GsonObject(toResultJson("系统繁忙", false));
		}
	}

	/**
	 * 发送验证码给用户手机
	 * 
	 * @param user
	 * @throws Exception
	 */
	private void sendVoiceToPhone(String name, String phone, String code) throws Exception {
		if (StringUtil.isBlank(name)) {
			GsonUtil.GsonObject(toResultJson("无用户名称！", true));
			return;
		}
		if (StringUtil.isBlank(phone)) {
			GsonUtil.GsonObject(toResultJson("无手机号码！", true));
			return;
		}
		if (StringUtil.isBlank(code)) {
			GsonUtil.GsonObject(toResultJson("无验证码！", true));
			return;
		}

		System.out.println("sessionCode " + code);
		String flag = TelCrmUtil.call(name, phone, code);
		if (flag.trim().equals("486")) {
			GsonUtil.GsonObject(toResultJson("前面有验证,稍后再尝试！", false));
			return;
		}

		String result = "";
		for (int i = 0; i < 15; i++) {
			result = TelCrmUtil.getResult(name);
			if (result.equals("1")) { // 发送成功
				log.info(name + "：手机验证码-->" + code);
				GsonUtil.GsonObject(toResultJson("请注意查收语音验证码！", true));
				return;
			}
			Thread.sleep(2000);
		}

		String message = "";
		if (result.equals("0")) {
			message = "正在排队等待呼叫";
		} else if (result.equals("2")) {
			message = "呼叫失败";
		} else if (result.equals("3")) {
			message = "空号";
		} else if (result.equals("4")) {
			message = "正在呼叫中";
		} else if (result.equals("5")) {
			message = "对方忙";
		} else if (result.equals("6")) {
			message = "关机，不在服务区";
		} else {
			message = "系统繁忙";
		}
		GsonUtil.GsonObject(toResultJson(message, false));
	}

	/**
	 * 发送验证码给用户手机
	 * 
	 * @param phone
	 * @param code
	 * @throws Exception
	 */
	private void sendSmsToPhone(String phone, String code) throws Exception {
		if (StringUtil.isBlank(phone)) {
			GsonUtil.GsonObject(toResultJson("无手机号码！", true));
			return;
		}
		if (StringUtil.isBlank(code)) {
			GsonUtil.GsonObject(toResultJson("无验证码！", true));
			return;
		}

		System.out.println("sessionCode " + code);
		// 接口1
		String msg = SendPhoneMsgUtil.callfour(phone, "您的验证码为：" + code);
		if (msg.equals("发送成功")) {
			GsonUtil.GsonObject(toResultJson("请注意查收短信验证码", true));
			log.info("接口1发送短信成功：" + code);
			return;
		} else {
			msg = "发送失败！";
			log.info("接口1发送短信失败:" + msg);
		}

		// 接口2
		msg = SendPhoneMsgUtil.sendSms(phone, code);
		if (msg.equals("发送成功")) {
			GsonUtil.GsonObject(toResultJson("请注意查收短信验证码", true));
			log.info("接口2发送短信成功：" + code);
			return;
		} else {
			log.info("接口2发送短信失败:" + msg);
		}

		// 接口3
		msg = SendPhoneMsgUtil.callMine(phone, code);
		if (msg.equals("发送成功")) {
			GsonUtil.GsonObject(toResultJson("请注意查收短信验证码", true));
			log.info("接口3发送短信成功：" + code);
			return;
		} else {
			log.info("接口3发送短信失败:" + msg);
		}
		GsonUtil.GsonObject(toResultJson(msg, false));
	}

	/**
	 * 千分位+小数后两位
	 * 
	 * @param obj
	 * @return
	 */
	private String decimalFormat(Object obj) {
		if (obj == null)
			return decimalFormat.format(0);
		if (obj instanceof String) {
			return decimalFormat.format(Double.valueOf(obj.toString()));
		}
		return decimalFormat.format(obj);
	}

	/**
	 * 刷新Session users
	 * 
	 * @return
	 * @throws Exception
	 */
	private Users refreshUserInSession2() {
		try {
			Users customer = getCustomerFromSession();
			Users user = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getUser", new Object[] { customer.getLoginname() }, Users.class);
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
			if (customer.getPostcode() == null || user.getPostcode() == null) {
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
				return null;
			}
			if (!customer.getPostcode().equals(user.getPostcode())) {
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
				return null;
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 领取APP下载彩金
	 */
	public void mobileGetAppPreferential() {
		SynchronizedUtil.getInstance().getAppPreferentialM(imageCode, platform);
	}

	/**
	 * app下载彩金 玩家资格验证 1.优惠是否开启2.ip、姓名 、是否申请过 3.是否使用短信反向验证
	 * 
	 * @throws AxisFault
	 */
	public String checkForAppPreferential() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		Integer statusCode = new Integer(0);
		Map<String, String> data = new HashMap<String, String>();
		// 检测用户是否登录
		if (user == null) {
			GsonUtil.GsonObject(toResultJson("[提示]你的登录已过期，请从首页重新登录", false));
			return null;
		}

		try {
			// [1.app下载彩金是否开启 const]
			String youHuiFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { "app下载彩金" }, String.class);
			if (!youHuiFlag.equals("1")) {
				GsonUtil.GsonObject(toResultJson("维护中...", false));
				return null;
			}
			// [2玩家资格验证 ip、姓名 、是否申请过]
			String accountName = user.getAccountName();
			String registIp = user.getRegisterIp();
			String loginName = user.getLoginname();
			statusCode = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkForAppPreferential", new Object[] { accountName, registIp, loginName }, Integer.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统忙碌中", false));
			return null;
		}
		String message = getMessage(statusCode);
		boolean success = statusCode == 0;
		// System.out.println("玩家验证是否通过=="+success);
		// [3.如果玩家资料验证ok，判断step2是否使用短信反向验证]
		if (success == true && isAntiSmsOpened()) {
			data.put("isAntiSms", "true");
		}

		GsonUtil.GsonObject(toResultJson(message, data, success));
		return null;
	}

	// 反向短信验证是否开启
	private boolean isAntiSmsOpened() {
		String isAntiSms = "";
		try {
			Const sms = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDeposit", new Object[] { "短信反转验证" }, Const.class);
			if (sms != null) {
				isAntiSms = sms.getValue();
			}
		} catch (AxisFault e) {
			e.printStackTrace();
		}
		System.out.println("短信反向验证是否开启=> " + ("1".equals(isAntiSms)));
		return "1".equals(isAntiSms);
	}

	// 判断讯息类别
	private String getMessage(int status) {
		String message = "";
		switch (status) {
		case 1:
			message = "[提示]重复的玩家注册ip地址或姓名";
			break;
		case 2:
			message = "[提示]已申请过app下载彩金";
			break;
		default:
			message = "[提示]请验证短信!";
			break;
		}
		return message;
	}

	/**
	 * 抽取7天手机连续签到奖品
	 */
	public void mobileSignLottery() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
			return;
		}
		try {
			String returnString = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"mobileSignLottery", new Object[] { user.getLoginname(), itemName }, String.class);
			System.out.println(returnString);
			String[] returnArray = returnString.split(",");
			String returnStatus = returnArray[0];
			String returnMsg = returnArray[1];
			if ("success".equals(returnStatus)) {
				GsonUtil.GsonObject(toResultJson(returnMsg, true));
			} else {
				GsonUtil.GsonObject(toResultJson(returnMsg, false));
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param message
	 *            訊息
	 * @param success
	 *            成功/失敗
	 * @return map
	 */
	private Object toResultJson(Object message, boolean success) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", success);
		result.put("message", message);
		return result;
	}

	/**
	 *
	 * @param message
	 *            訊息
	 * @param success
	 *            成功/失敗
	 * @return map
	 */
	private Object toResultJson(Object message, Object data, boolean success) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", success);
		result.put("message", message);
		result.put("data", data);
		return result;
	}

	/**
	 * 手机PNG登入
	 * 
	 * @return
	 */
	public void gamePNGMobile() {
		try {
			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("请您从首页登录", false));
			}
			if (user.getRole().equals("AGENT")) {
				GsonUtil.GsonObject(toResultJson("代理玩家不可游玩", false));
			}
			if (StringUtils.isEmpty(this.gameCode)) {
				GsonUtil.GsonObject(toResultJson("请选择游戏", false));
			}
			int port = this.getRequest().getServerPort();
			String reloadUrl = this.getRequest().getScheme() + "://" + this.getRequest().getServerName()
					+ ((port == 0 || port == 80 || port == 443) ? "" : ":" + port);
			// 获取游戏链接地址
			String gameUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getPNGMobileUrl", new Object[] { user.getLoginname(), this.gameCode, reloadUrl }, String.class);

			if (gameUrl == null || gameUrl.equals("")) {
				GsonUtil.GsonObject(toResultJson("进入游戏失败", false));
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("url", gameUrl);
				GsonUtil.GsonObject(toResultJson("", data, true));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统繁忙,请稍后再试，或者直接与客服联系！", false));
		}
	}

	/****
	 * 微信秒存
	 * 
	 * @return
	 */
	public String mobileGetWxDeposti() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		Map<String, Object> data = new HashMap<String, Object>();
		// 检测用户是否登录
		if (user == null) {
			GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
			return null;
		}

		// 代理不能使用在线支付
		if (!user.getRole().equals("MONEY_CUSTOMER")) {
			GsonUtil.GsonObject(toResultJson("[提示]代理不能使用秒存！", false));
			return null;
		}

		try {
			// 判断是否存在支付订单
			DepositOrder depositOrder = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getDepositPayorder", new Object[] { user.getLoginname() }, DepositOrder.class);
			if (depositOrder != null) {
				data.put("wxValidaTeAmout", depositOrder.getAmount());
				data.put("wxValidaId", depositOrder.getDepositId());
			}
			Bankinfo wxBank = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getWxDepositPayorder", new Object[] { user.getLoginname() }, Bankinfo.class);
			if (StringUtils.isNotBlank(wxBank.getMassage())) {
				GsonUtil.GsonObject(toResultJson(wxBank.getMassage(), false));
				return null;
			}
			data.put("wxBank", wxBank);
		} catch (AxisFault e) {
			e.printStackTrace();
			return null;
		}
		GsonUtil.GsonObject(toResultJson("取得成功！", data, true));
		return null;
	}

	/****
	 * 通用支付
	 * 
	 * @return
	 */
	public String mobileGetGatherDeposti() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		String bankname = getRequest().getParameter("platformID").equals("tlzf") ? "通联转账"
				: getRequest().getParameter("platformID");
		Map<String, Object> data = new HashMap<String, Object>();
		// 检测用户是否登录
		if (user == null) {
			GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
			return null;
		}

		// 代理不能使用在线支付
		if (!user.getRole().equals("MONEY_CUSTOMER")) {
			GsonUtil.GsonObject(toResultJson("[提示]代理不能使用秒存！", false));
			return null;
		}

		try {
			Bankinfo bank = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"getGatherPayorder", new Object[] { user.getLoginname(), bankname }, Bankinfo.class);
			if (StringUtils.isNotBlank(bank.getMassage())) {
				GsonUtil.GsonObject(toResultJson(bank.getMassage(), false));
				return null;
			}
			data.put("bank", bank);
		} catch (AxisFault e) {
			e.printStackTrace();
			return null;
		}
		GsonUtil.GsonObject(toResultJson("取得成功！", data, true));
		return null;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getHistoryType() {
		return historyType;
	}

	public void setHistoryType(String historyType) {
		this.historyType = historyType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getWithdrawlType() {
		return withdrawlType;
	}

	public void setWithdrawlType(String withdrawlType) {
		this.withdrawlType = withdrawlType;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTransferGameOut() {
		return transferGameOut;
	}

	public void setTransferGameOut(String transferGameOut) {
		this.transferGameOut = transferGameOut;
	}

	public String getTransferGameIn() {
		return transferGameIn;
	}

	public void setTransferGameIn(String transferGameIn) {
		this.transferGameIn = transferGameIn;
	}

	public Pattern getP_email() {
		return p_email;
	}

	public void setP_email(Pattern p_email) {
		this.p_email = p_email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getCheckAddress() {
		return checkAddress;
	}

	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}

	public String getCheckKey() {
		return checkKey;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getWithdrawlWay() {
		return withdrawlWay;
	}

	public void setWithdrawlWay(String withdrawlWay) {
		this.withdrawlWay = withdrawlWay;
	}

	public String getIsfun() {
		return isfun;
	}

	public void setIsfun(String isfun) {
		this.isfun = isfun;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getCheck1() {
		return check1;
	}

	public void setCheck1(String check1) {
		this.check1 = check1;
	}

	public String getCheck2() {
		return check2;
	}

	public void setCheck2(String check2) {
		this.check2 = check2;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPtBigBangId() {
		return ptBigBangId;
	}

	public void setPtBigBangId(String ptBigBangId) {
		this.ptBigBangId = ptBigBangId;
	}

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getProposalType() {
		return proposalType;
	}

	public void setProposalType(Integer proposalType) {
		this.proposalType = proposalType;
	}

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBindingCode() {
		return bindingCode;
	}

	public void setBindingCode(String bindingCode) {
		this.bindingCode = bindingCode;
	}

	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQtype() {
		return qtype;
	}

	public void setQtype(String qtype) {
		this.qtype = qtype;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getHelpType() {
		return helpType;
	}

	public void setHelpType(String helpType) {
		this.helpType = helpType;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Double getCouponRemit() {
		return couponRemit;
	}

	public void setCouponRemit(Double couponRemit) {
		this.couponRemit = couponRemit;
	}

	public Integer getEmailId() {
		return emailId;
	}

	public void setEmailId(Integer emailId) {
		this.emailId = emailId;
	}

	public String getYouhuiType() {
		return youhuiType;
	}

	public void setYouhuiType(String youhuiType) {
		this.youhuiType = youhuiType;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_code() {
		return card_code;
	}

	public void setCard_code(String card_code) {
		this.card_code = card_code;
	}

	public String getCard_password() {
		return card_password;
	}

	public void setCard_password(String card_password) {
		this.card_password = card_password;
	}

	public String getEmigratedeLevel() {
		return emigratedeLevel;
	}

	public void setEmigratedeLevel(String emigratedeLevel) {
		this.emigratedeLevel = emigratedeLevel;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getAgFish() {
		return agFish;
	}

	public void setAgFish(int agFish) {
		this.agFish = agFish;
	}

	/**
	 * 阅读站内信 并回传未读信件数量
	 *
	 */
	public String mobileReadEmail() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("topicId", emailId);
			paramMap.put("loginName", user.getLoginname());
			// paramMap.put("pageNum", page);
			// paramMap.put("pageSize", count);

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

			PostMethod method = new PostMethod(url + "/topic/queryTopicById");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				String responseData = resultMap.get("responseData");
				responseData = app.util.AESUtil.getInstance().decrypt(responseData);

				resultMap = mapper.readValue(responseData, Map.class);

				JSONObject jsonObject = JSONObject.fromObject(resultMap.get("data"));
				// msg.setCount(
				// Integer.parseInt(null == jsonObject.get("total") ? "0" :
				// jsonObject.get("total").toString()));
				JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("dataList"));

				List<TopicInfo> list = (List) JSONArray.toCollection(jsonArray, TopicInfo.class);
				/*
				 * for (TopicInfo topicInfo : list) { MessageVo msgVo = new
				 * MessageVo(); msgVo.setId(emailId);
				 * msgVo.setTitle(topicInfo.getTitle());
				 * msgVo.setContent(topicInfo.getContent());
				 * msgVo.setCreateDate(topicInfo.getCreateTimeStr());
				 * msg.getMsgList().add(msgVo); }
				 */
				TopicInfo topicInfo = list.get(0);
				// 查询未读邮件
				Integer agent = user.getRole().equalsIgnoreCase("AGENT") ? Constants.TOPIC_SEND_TYPE_ALL_AGENT
						: user.getLevel();
				String unreadCount = getUnReadMessageCount(user.getLoginname(), agent);

				Map<String, Object> data = new HashMap<String, Object>();
				data.put("unreadCount", unreadCount);
				data.put("content", topicInfo.getContent());
				GsonUtil.GsonObject(toResultJson("已阅读！", data, true));

			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	public String getUnReadMessageCount(String loginname, Integer agent) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginName", loginname);
		paramMap.put("userNameType", agent);

		String str = mapper.writeValueAsString(paramMap);

		String requestJson = app.util.AESUtil.getInstance().encrypt(str);

		String url = Configuration.getInstance().getValue("middleservice.url");
		url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

		HttpClient httpClient = new HttpClient();

		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 500000);

		httpClient.setParams(params);

		PostMethod method = new PostMethod(url + "/topic/queryUnReadNum");
		method.setRequestHeader("Connection", "close");
		method.setParameter("requestData", requestJson);

		int statusCode = httpClient.executeMethod(method);

		if (statusCode != HttpStatus.SC_OK) {

			return "0";
		} else {
			byte[] responseBody = method.getResponseBody();
			String responseString = new String(responseBody);

			Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
			String responseData = resultMap.get("responseData");
			responseData = app.util.AESUtil.getInstance().decrypt(responseData);

			resultMap = mapper.readValue(responseData, Map.class);

			String countBook = resultMap.get("data");
			if (countBook != null) {
				return countBook;
			} else {
				return "0";
			}
		}
	}

	/**
	 * 获取站内信
	 */
	public String mobileQueryEmail() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject(toResultJson("尚未登录！请重新登录！", false));
				return null;
			}
			Messages msg = new Messages();
			Integer agent = user.getRole().equalsIgnoreCase("AGENT") ? Constants.TOPIC_SEND_TYPE_ALL_AGENT
					: user.getLevel();
			// 获取站内信，并且添加未读的站内信消息。
			String unreadCount = getUnReadMessageCount(user.getLoginname(), agent);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("loginName", user.getLoginname());
			paramMap.put("pageNum", pageIndex);
			paramMap.put("pageSize", size);

			String str = mapper.writeValueAsString(paramMap);

			String requestJson = app.util.AESUtil.getInstance().encrypt(str);

			String url = Configuration.getInstance().getValue("middleservice.url");
			url = SpecialEnvironmentStringPBEConfig.getPlainText(url);

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 500000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/topic/queryList");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				String responseData = resultMap.get("responseData");
				responseData = app.util.AESUtil.getInstance().decrypt(responseData);

				resultMap = mapper.readValue(responseData, Map.class);

				JSONObject jsonObject = JSONObject.fromObject(resultMap.get("data"));
				msg.setCount(
						Integer.parseInt(null == jsonObject.get("total") ? "0" : jsonObject.get("total").toString()));
				JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("dataList"));

				List<TopicInfo> list = (List) JSONArray.toCollection(jsonArray, TopicInfo.class);
				for (TopicInfo topicInfo : list) {
					MessageVo msgVo = new MessageVo();
					msgVo.setId(topicInfo.getStatusId());
					msgVo.setTitle(topicInfo.getTitle());
					msgVo.setCreateDate(topicInfo.getCreateTimeStr());
					msgVo.setPrivate(topicInfo.getTopicStatus().toString().equals("0") ? true : false);
					msgVo.setRead(topicInfo.getIsUserRead().toString().equals("0") ? false : true);
					msg.getMsgList().add(msgVo);
				}
			}

			// msg.setPageNo(pageIndex);
			// msg.setPageSize(size);
			// guestBookService.getMessagesByUser(user.getLoginname(), agent,
			// msg);
			// Integer unreadCount =
			// guestBookService.getUnReadMessageCount(user.getLoginname(),agent);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("records", msg.getMsgList());
			data.put("pageIndex", pageIndex);
			data.put("total", msg.getCount());
			data.put("size", size);

			data.put("unreadCount", unreadCount);
			//
			GsonUtil.GsonObject(toResultJson("成功", data, true));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("系统缓忙，请稍后重试！", false));
		}
		return null;
	}

	/**
	 * 获取 Dt 奖池
	 */
	public void mobileDtJackpot() {
		DtPotVO result = new DtPotVO();

		try {
			result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
					AxisUtil.NAMESPACE, "dtJackpot", null, DtPotVO.class);
			GsonUtil.GsonObject(result);
			return;
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			log.error("异常：" + axisFault);
		}
		result.setCode("000028");
		GsonUtil.GsonObject(result);
		return;
	}

}
