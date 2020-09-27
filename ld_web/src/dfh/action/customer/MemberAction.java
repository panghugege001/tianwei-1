package dfh.action.customer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import dfh.model.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.opensymphony.xwork2.Action;

import dfh.action.SubActionSupport;
import dfh.action.SynchronizedUtil;
import dfh.action.vo.DtPotVO;
import dfh.action.vo.DtVO;
import dfh.action.vo.QueryDataEuroVO;
import dfh.filter.UrlPatternFilter;
import dfh.listener.MySessionContext;
import dfh.model.bean.Bet;
import dfh.model.enums.IssuingBankNumber;
import dfh.model.enums.NTErrorCode;
import dfh.model.enums.ProposalType;
import dfh.model.enums.PtGameCode;
import dfh.model.enums.UserRole;
import dfh.model.notdb.FirstlyCashin;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.NTwoCheckClientResponseBean;
import dfh.security.EncryptionUtil;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ILogin;
import dfh.service.interfaces.IMemberSignrecord;
import dfh.service.interfaces.IUserbankinfoService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.TransferService;
import dfh.skydragon.webservice.model.LoginInfo;
import dfh.skydragon.webservice.model.PtTotal;
import dfh.skydragon.webservice.model.WinPointInfo;
import dfh.utils.AESUtil;
import dfh.utils.AxisSecurityEncryptUtil;
import dfh.utils.AxisUtil;
import dfh.utils.ClientInfo;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.FTPUtil;
import dfh.utils.GsonUtil;
import dfh.utils.HttpClientHelper;
import dfh.utils.IPSeeker;
import dfh.utils.Live800Encode;
import dfh.utils.MatchDateUtil;
import dfh.utils.NumericUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.SSLMailSender;
import dfh.utils.SkyUtils;
import dfh.utils.StringUtil;
import dfh.utils.Utils;

public class MemberAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(MemberAction.class);
	private CustomerService cs;
	private IMemberSignrecord memberService;
	private IUserbankinfoService userbankinfoService;
//	private GuestBookService guestBookService;

	public SSLMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(SSLMailSender mailSender) {
		this.mailSender = mailSender;
	}

	private SSLMailSender mailSender;
	private Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);

	public IUserbankinfoService getUserbankinfoService() {
		return userbankinfoService;
	}

	public void setUserbankinfoService(IUserbankinfoService userbankinfoService) {
		this.userbankinfoService = userbankinfoService;
	}

	public IMemberSignrecord getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberSignrecord memberService) {
		this.memberService = memberService;
	}

	private ILogin login;

	public ILogin getLogin() {
		return login;
	}

	public void setLogin(ILogin login) {
		this.login = login;
	}

	private ProposalService proposalService;
	private TransferService transferService;
	private SeqService seqService;
	private String password;
	private String new_password;
	private String confirm_password;
	private String aliasName;
	private String email;
	private String phone;
	private String bank;
	private String qq;
	private String sms;
	private Integer bankinfoid;


	private String saveway;
	private String bankname;
	private String username;
	private String msflag;
	private String intro;
	private String mailaddress;
	private String birthday;
	private String couponType;
	private Double couponRemit;
	private String couponCode;
	private String imageCode;
	private String partner;
	private String microchannel ;
	
	private String youHuiType;
	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();
	
	//private final String website_key = "9d8d3eb9-c4a7-4921-9720-11c0e90a1ebe";
	//private final String private_key = "6f854ede-17ee-47e6-871b-28d8195392f0";
	private String check_address ;
	private String check_key ;
	private String ioBB ;

	private String type;
	private String tkType ;
	private Integer ptBigBangId;
	
	private Integer questionid;
	private String answer;
	
	private String goddess;
	private String querytype;
	
	private String ubankname;//玩家存款银行
	private String uaccountname;//玩家银行卡持卡人姓名
	private String ubankno;//玩家银行卡号
	private String depositId;//附言
	private String banktype;//存款类型
	private boolean force;
	private int[] userId;
	 private Double slotmachine;
	    private Double liveall;
		
		  
		public Double getSlotmachine() {
			return slotmachine;
		}

		public void setSlotmachine(Double slotmachine) {
			this.slotmachine = slotmachine;
		}

		public Double getLiveall() {
			return liveall;
		}

		public void setLiveall(Double liveall) {
			this.liveall = liveall;
		}
		

	public int[] getUserId() {
		return userId;
	}

	public void setUserId(int[] userId) {
		this.userId = userId;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public String getUbankname() {
		return ubankname;
	}

	public void setUbankname(String ubankname) {
		this.ubankname = ubankname;
	}

	public String getUaccountname() {
		return uaccountname;
	}

	public void setUaccountname(String uaccountname) {
		this.uaccountname = uaccountname;
	}

	public String getUbankno() {
		return ubankno;
	}

	public void setUbankno(String ubankno) {
		this.ubankno = ubankno;
	}

	public String getDepositId() {
		return depositId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}

	public String getBanktype() {
		return banktype;
	}

	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}

	public String getGoddess() {
		return goddess;
	}

	public void setGoddess(String goddess) {
		this.goddess = goddess;
	}

	public String getQuerytype() {
		return querytype;
	}

	public void setQuerytype(String querytype) {
		this.querytype = querytype;
	}

	public String getCheck_address() {
		return check_address;
	}

	public void setCheck_address(String check_address) {
		this.check_address = check_address;
	}

	public String getCheck_key() {
		return check_key;
	}

	public void setCheck_key(String check_key) {
		this.check_key = check_key;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	private String referWebsite;
	private String accountName;
	private String accountType;
	private String accountCity;
	public Integer getGuildId() {
		return guildId;
	}

	public void setGuildId(Integer guildId) {
		this.guildId = guildId;
	}

	private Integer guildId;
	private String bankAddress;
	private String accountNo;
	private String validateCode;
	private Double amount;
	private String remark;
	private Double firstCash;
	private Double tryCredit;
	private String loginname;
	private String new_content;
	private String errormsg;
	private Double balance;

	public String getNew_content() {
		return new_content;
	}

	public void setNew_content(String new_content) {
		this.new_content = new_content;
	}

	private String service;
	private String notifyNote;// 是否短信通知
	private String notifyPhone;// 是否电话通知
	private String platform;
	private String entrance;

	public String getEntrance() {
		return entrance;
	}

	public void setEntrance(String entrance) {
		this.entrance = entrance;
	}

	private Date start;// 起始时间
	private Date end;// 结束时间
	private String starttime;
	private String endtime;
	private int totalpageno = 0;
	private int totalrowsno = 0;
	private int maxpageno = 20;
	private int pageno = 1;
	private List<Proposal> list = new ArrayList<Proposal>();
	private Double totalamount;
	private String cashintime;// 前台存入资金时间
	private Integer proposalFlag;// 状态
	private Integer proposalType;// 类型
	//代理中心会员账务的存款|提款|返水|优惠的js函数后缀切换
	private String tail;
	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}

	private Integer pageIndex;// 当前页
	private Integer size;// 每页显示的行数

	private Integer year;
	private Integer month;

	private Integer betTotal;
	private String order;
	private String by;
	private String ptby;
	private String ptorder;

	private String jobPno;// 订单号

	private Double remit;
	private String gameType ; 
	
	private String gameCode ;
	private String depositCode ;
	
	private Integer giftID;        //礼品ID
	private String address;       //地址
	private String addressee;     //收件人
	private String cellphoneNo;   //手机号
	
	private Integer sid;
	private String content;
	private String gameList;
	
	private String thirdOrder; //订单号
	private String nickname;	//昵称
	private String pictureFileName;	//图片名称
	private String pictureContentType; //图片格式
	private File picture;	//图片文件
	private Date depositDate;	//存款日期
	private String depositTime;	//存款时间
	private Integer cardtype;	//点卡类型
	private String cardno;	//点卡账号

	
	public String getThirdOrder() {
		return thirdOrder;
	}

	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPictureFileName() {
		return pictureFileName;
	}

	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}

	public String getPictureContentType() {
		return pictureContentType;
	}

	public void setPictureContentType(String pictureContentType) {
		this.pictureContentType = pictureContentType;
	}

	public File getPicture() {
		return picture;
	}

	public void setPicture(File picture) {
		this.picture = picture;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public String getDepositTime() {
		return depositTime;
	}

	public void setDepositTime(String depositTime) {
		this.depositTime = depositTime;
	}

	public Integer getCardtype() {
		return cardtype;
	}

	public void setCardtype(Integer cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getGameList() {
		return gameList;
	}

	public void setGameList(String gameList) {
		this.gameList = gameList;
	}

	public Double getRemit() {
		return remit;
	}

	public void setRemit(Double remit) {
		this.remit = remit;
	}

	private static final long serialVersionUID = 1L;

	public MemberAction() {
	}

	public String loginFind() {
		String view = "reLogin";
		if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null)
			return INPUT;

		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(rand)) {
			setErrormsg("验证码错误");
			return "reLogin";
		}

		IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
		String remoteIp = getIp();
		String city = "";
		String temp = remoteIp != null ? seeker.getAddress(remoteIp) : "";
		if (null != temp && !"CZ88.NET".equals(temp)) {
			city = temp;
		}
		// 保证执行一次，事务完整
		LoginInfo info = null;

		try {
			info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "login", new Object[] { loginname, password, remoteIp, city }, LoginInfo.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrormsg("登录异常，请重新登录！");
			return "reLogin";
		}

		if (null != info && null != info.getUser() && 1 == info.getSucFlag()) {// 登录成功
			Users olduser = info.getUser();
			setErrormsg(info.getMsg());
			if (null != olduser && null != olduser.getLastLoginIp() && !"".equals(olduser.getLastLoginIp())) {
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
			if (olduser != null && null != olduser.getTempLastLoginTime()) {
				getHttpSession().setAttribute("time", olduser.getTempLastLoginTime());
			} else {
				getHttpSession().setAttribute("time", "");
			}
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, olduser);
			getHttpSession().removeAttribute("loginNum");
			return INPUT;
		} else {// 登录失败
			if (null != info && null == info.getUser() && null == info.getSucFlag()) {
				setErrormsg(info.getMsg());
			} else {
				setErrormsg("登录异常，请重新登录！");
			}
		}
		return view;
	}
	
	// 新增代理
	public String addAgent() {
		try {
			if (loginname == null || loginname.equals("")) {
				GsonUtil.GsonObject("登入账号不可为空");
				return null;
			}
			if (!loginname.startsWith("a_")) {
				GsonUtil.GsonObject("登入账号必须以a_开头");
				return null;
			}
			if (referWebsite == null || referWebsite.equals("")) {
				GsonUtil.GsonObject("代理网址不可为空");
				return null;
			}
			if (referWebsite.trim().equals("www")) {
				GsonUtil.GsonObject("代理网址已被注册");
				return null;
			}
			if (accountName == null || accountName.equals("")) {
				GsonUtil.GsonObject("姓名不可为空");
				return null;
			}
			if (phone == null || phone.equals("")) {
				GsonUtil.GsonObject("电话号码不可为空");
				return null;
			}
			if (!isMobileNO(phone)) {
				GsonUtil.GsonObject("输入的手机号码有误！只支持13、14、15、16、17、18、19开头的电话号码！");
				return ERROR;
			}
			if (email == null || email.equals("")) {
				GsonUtil.GsonObject("电子邮箱不可为空");
				return null;
			}
			if (!p_email.matcher(email).matches()) {
				GsonUtil.GsonObject("电子邮箱格式错误");
				return null;
			}
			if (this.password == null || this.password.trim().equals("")) {
				GsonUtil.GsonObject("登录密码不可为空");
				return null;
			}
			if (!StringUtils.isBlank(this.microchannel)) {
				if (this.microchannel.length() > 20) {
					GsonUtil.GsonObject("微信长度错误");
					return null;
				}
			} else {
				GsonUtil.GsonObject("微信号码不能为空");
				return null;
			}
			if (qq == null || qq.equals("")) {
				GsonUtil.GsonObject("QQ不可为空");
				return null;
			}
			String code = (String) getHttpSession().getAttribute(Constants.SESSION_AG_TRY_RANDID);
			// 查看验证码是否为空
			if (code == null || code.equals("") || code.length() <= 0) {
				GsonUtil.GsonObject("验证码错误！");
				return null;
			}
			if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(code)) {
				GsonUtil.GsonObject("验证码错误");
				return null;
			}
			// 重新生成验证码 防止重复提交
			getHttpSession().setAttribute(Constants.SESSION_AG_TRY_RANDID, null);
			// 得到代理域名： 得到如何知道本站点(titile.jsp 配合):
			String howToKnow = (String) this.getRequest().getSession().getAttribute("referURL");
			IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
			String city = "";
			String remoteIp = getIp();
			String temp = remoteIp != null && !"".equals(remoteIp) ? seeker.getAddress(remoteIp) : "";
			if (null != temp && !"CZ88.NET".equals(temp)) {
				city = temp;
			}
			
			email = AESUtil.aesEncrypt(email,AESUtil.KEY);
			phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);

			String msgInfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addAgent", new Object[] { howToKnow, StringUtils.trim(loginname), password, StringUtils.trim(accountName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), StringUtils.trim(referWebsite), remoteIp, city, StringUtil.trim(microchannel) ,partner }, String.class);
			if (msgInfo == null) {
				Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { loginname }, Users.class);
//				try {
//					String html = EmailTemplateHelp.toHTML(Constants.EMAIL_REGISTER_BODY_HTML, new Object[] { loginname, DateUtil.formatDateForStandard(user.getCreatetime()) });
//					mailSender.sendmail(html, user.getEmail(), "恭喜您成为龙都娱乐城代理");
//				} catch (Exception e) {
//					log.error(e.getMessage(), e);
//				}
				if (user.getFlag() != 1) {
					getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				} else {
					GsonUtil.GsonObject("帐号已禁用 :联系市场部门！");
					return null;
				}
				GsonUtil.GsonObject("SUCCESS");
				return null;
			} else {
				GsonUtil.GsonObject("抱歉注册失败 :" + msgInfo);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("抱歉注册失败 :" + e.toString());
			return null;
		}
	}
	/**
	 * 获取二维码列表
	 */
	public void queryQRcode() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("请先登录");
				return;
			}
			String loginname = customer.getLoginname();

			List<Qrcode> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryQRcode", new Object[] {loginname }, Qrcode.class);

			Gson gson = new Gson();
			String str = gson.toJson(list);
			log.info("客服二维码返回结果:"+str);
			writeJson(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void queryBirthdayRecords() {
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("请先登录");
				return;
			}
			String loginname = customer.getLoginname();

			List<Activity> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryBirthdayRecords", new Object[] {loginname }, Activity.class);

			Gson gson = new Gson();
			String str = gson.toJson(list);
			writeJson(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 领取红包
	 */
	public void doHB(){
		try {
			Users customer = customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
				return;
			}

			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,
					"doHB", new Object[] {customer.getLoginname(),type, sid },String.class);
			if (!StringUtil.isEmpty(info)) {
				GsonUtil.GsonObject(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void chrismesCount(){
		try {
			Users customer = customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
				return;
			}
			Date date = new Date();
			String startTime="2017-12-18 00:00:00";
			String endTime="2017-12-25 00:00:00";
			Date start = DateUtil.parseDateForStandard(startTime);
			Date end = DateUtil.parseDateForStandard(endTime);
            if(date.before(start)||date.after(end)){
				GsonUtil.GsonObject("活动未开始");
				return;
			}
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {customer.getLoginname(),startTime,endTime}, String.class);
			Double value = Double.valueOf(result);
			if(value<1000.0){
				GsonUtil.GsonObject("您好，需要存款达到1000方可报名参加");
				return;
			}
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,
					"chrismesCount", new Object[] {customer.getLoginname() },String.class);
			if (!StringUtil.isEmpty(info)) {
				GsonUtil.GsonObject(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void queryChrismesCount(){
		try {

			Integer result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryChrismesCount", new Object[] {}, Integer.class);

				GsonUtil.GsonObject(result);
				return;

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(0);
			return;
		}
	}




	/**
	 * 查询所有银行卡
	 */
	public void queryBankAll(){
		try {
			Users customer = customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
				return ;
			}

			List<Userbankinfo> userbankinfoList=null;
			userbankinfoList = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getUserbankinfoList2", new Object[] {
					customer.getLoginname()}, Userbankinfo.class);
			GsonUtil.GsonObject(userbankinfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}


	/**
	 * 查询最后一次登录时间
	 */
	public void queryLastLoginDate(){
		try {
			Users customer = customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
				return ;
			}

			String date = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryLastLoginDate", new Object[] {
					customer.getLoginname()}, String.class);
			GsonUtil.GsonObject(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}



	/**
	 * 查询红包奖金余额
	 */
	public void getHBMoney() {
		try {
			Users customer = null;
			customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
			}
			//giftID=是否为手机端 0 存款金额  1：提款金额
			String info = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,"queryHBbonus",new Object[] { customer.getLoginname(), type}, String.class);
			if (!StringUtil.isEmpty(info)) {
				GsonUtil.GsonObject(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询玩家可领红包
	 */
	public String queryHBSelect() {
		List list = new ArrayList();
		try {
			Users customer = customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
			} else {
				list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,"queryHBSelect", new Object[] { customer.getLevel(),type },
						HBConfig.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		getRequest().setAttribute("list", list);
		return INPUT;
	}

	/**
	 * 红包账户转入游戏账户
	 */
	public void submitHBRemit(){
		String info= SynchronizedUtil.getInstance().transferInforHB(type, Double.parseDouble(transferGameIn),0);
		GsonUtil.GsonObject(info);
	}
	
	public String queryAgentAddress(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");
			return "index";
		}else {
			try {
				List<String> addrList = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "queryAgentAddress", new Object[] {customer.getLoginname()}, String.class);
				String addr = "http://www.longdu188.com/?agentcode="+customer.getId();
				addrList.add(addr);
				ArrayList<String> strings = new ArrayList<>();
				for(String url:addrList){
					if(url.contains("http:")){
						String replace = url.replace("http", "https");
						strings.add(replace);
					}else {
						strings.add(url);
					}
				}
				getRequest().setAttribute("addrList", strings);
			} catch (AxisFault e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	public String queryAgentAddress2(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");
			return "index";
		}else {
			try {
				List<String> addrList = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "queryAgentAddress", new Object[] {customer.getLoginname()}, String.class);
				String addr = "http://www.longdu188.com/?agentcode="+customer.getId();
				addrList.add(addr);
				GsonUtil.GsonObject(addrList);
			} catch (AxisFault e) {
				e.printStackTrace();
				GsonUtil.GsonObject("系统繁忙，请稍候重试！");
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("系统繁忙，请稍候重试");
			}
		}
		return null;
	}


//创建工会
	public String createUnion(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("程序异常");
			return null;
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}else {
			try {
				if(customer.getLevel()<1){
					GsonUtil.GsonObject("您尚未达到忠实等级!");
					return null;
				}
				String b = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
						AxisUtil.NAMESPACE, "createUnion", new Object[] { customer.getLoginname(),customer.getLevel()}, String.class);
				GsonUtil.GsonObject(b);
			} catch (AxisFault e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			}
		}
		return null;
	}

	//工会首页
	public String unionList(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("程序异常");
			return null;
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}else {
			try {
				Page page = null;
				try {
					Boolean b=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "isPresident", new Object[] { customer.getLoginname()}, Boolean.class);
					if(b){
					List<UnionStaffInfos> list=AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
								AxisUtil.NAMESPACE, "getMembers", new Object[] { customer.getLoginname()}, UnionStaffInfos.class);
						getRequest().setAttribute("page", list);
						return null;
					}
					Boolean boo=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "isApply", new Object[] { customer.getLoginname()}, Boolean.class);
					if(boo){
						List<UnionStaff> unionStaffs=AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService",false),
								AxisUtil.NAMESPACE, "getApplyLists", new Object[] { customer.getLoginname()}, UnionStaff.class);
						getRequest().setAttribute("page", list);
						return null;
					}
					page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "unionList", new Object[] {  pageIndex, size }, Page.class, Union.class);
					getRequest().setAttribute("page", page);
				} catch (AxisFault e) {
					e.printStackTrace();
					GsonUtil.GsonObject("网络异常");
				}
			}  catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			}
		}
		return null;
	}
	//加入工会
	public String joinUnion(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("程序异常");
			return null;
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}else {
			try {
				if(customer.getLevel()<1) {
					Boolean kjzfflag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkAllDeposit", new Object[]{customer.getLoginname(), 300.0}, Boolean.class);
					if (!kjzfflag) {
						GsonUtil.GsonObject("存款需大于300元！");
						return null;
					}
				}
				String b = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
						AxisUtil.NAMESPACE, "joinUnion", new Object[] { customer.getLoginname(),customer.getLevel(),Integer.valueOf(id)}, String.class);
				GsonUtil.GsonObject(b);
			}  catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			}
		}
		return null;
	}

	//T出工会
	public String Kpeople(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("程序异常");
			return null;
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}else {
			try {
				Boolean b=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
						AxisUtil.NAMESPACE, "isPresident", new Object[] { customer.getLoginname()}, Boolean.class);
				if(b){
					Boolean t=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "Kpeople", new Object[] { userId}, Boolean.class);
					if(t){
						GsonUtil.GsonObject("踢除成功");
						return null;
					}

				}
				GsonUtil.GsonObject("你没有权限操作");
			}  catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			}
		}
		return null;
	}

	//退出工会
	public String exitUnion(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("程序异常");
			return null;
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}else {
			try {
				Boolean b=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
						AxisUtil.NAMESPACE, "isPresident", new Object[] { customer.getLoginname()}, Boolean.class);
				if(b){
					Boolean t=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "exitUnion", new Object[] { customer.getLoginname()}, Boolean.class);
					if(t){
						GsonUtil.GsonObject("踢除成功");
						return null;
					}

				}
				else {
					Boolean u=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "exitUnion", new Object[] { customer.getLoginname()}, Boolean.class);
				}
				GsonUtil.GsonObject(b);
			}  catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			}
		}
		return null;
	}

	//工会管理
	public String unionManage(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("程序异常");
			return null;
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}else {
			try {
				Boolean b=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
						AxisUtil.NAMESPACE, "isPresident", new Object[] { customer.getLoginname()}, Boolean.class);
				if(b){
				List<UnionStaff> unionStaffs=AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService",false),
							AxisUtil.NAMESPACE, "getApplyLists", new Object[] { customer.getLoginname()}, UnionStaff.class);
					getRequest().setAttribute("page", list);
					return null;
				}
				GsonUtil.GsonObject("你没有权限操作");
			}  catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			}
		}
		return null;
	}

	//工会成员同意/拒绝
	public String passOrRefuse(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("程序异常");
			return null;
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}else {
			try {
				Boolean b=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
						AxisUtil.NAMESPACE, "isPresident", new Object[] { customer.getLoginname()}, Boolean.class);
				if(b){
					String o=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
							AxisUtil.NAMESPACE, "passOrRefuse", new Object[] { sid,type}, String.class);
					GsonUtil.GsonObject(o);
				}
				GsonUtil.GsonObject("你没有权限操作");
			}  catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("网络异常");
			}
		}
		return null;
	}




	
	/**
	 * 手机号码验证
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public String login() {
		if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null) {
			GsonUtil.GsonObject("SUCCESS");
			return null;
		}
		if (loginname == null || loginname.equals("") || loginname.length() <= 0) {
			GsonUtil.GsonObject("账号不能为空！");
			return null;
		}
		if (password == null || password.equals("") || password.length() <= 0) {
			GsonUtil.GsonObject("密码不能为空！");
			return null;
		}
		/*if (imageCode == null || imageCode.equals("") || imageCode.length() <= 0) {
			GsonUtil.GsonObject("验证码不能为空！");
			return null;
		}
		String code = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (code == null || code.equals("") || code.length() <= 0) {
			GsonUtil.GsonObject("验证码错误！");
			return null;
		}
		if (!code.equals(imageCode)) {
			GsonUtil.GsonObject("验证码错误！");
			return null;
		}
		// 重新生成验证码 防止重复提交
		getHttpSession().setAttribute(Constants.SESSION_RANDID, null);*/
		IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
//		String remoteIp = getIp();
		String remoteIp = getIpAddr();
		String city = "";
		String temp = remoteIp != null ? seeker.getAddress(remoteIp) : "";
		if (null != temp && !"CZ88.NET".equals(temp)) {
			city = temp;
		}
		// 保证执行一次，事务完整
		LoginInfo info = null;

		try {
			loginname = loginname.toLowerCase().trim();
			String str="";//(String) getHttpSession().getAttribute(Constants.CPUID);//cpuid iobb
			//String ourdeviceID = (String) getHttpSession().getAttribute(Constants.OURDEVICE);
			info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
					AxisUtil.NAMESPACE, "login", new Object[] { loginname, password, remoteIp, city, ClientInfo.getOSName(getRequest().getHeader("user-agent")), str}, LoginInfo.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			log.error("call web service error", e);
			// setErrormsg("登录异常，请重新登录！");
			GsonUtil.GsonObject("登录异常，请重新登录！");
			return null;
		}

		if (null != info && null != info.getUser() && 1 == info.getSucFlag()) {// 登录成功
			String trycode = EncryptionUtil.encryptPassword(loginname+"e68"+"ruofgergiowqnr8342047tujtgvasw2q0e38");
			getHttpSession().setAttribute("trycode",trycode);
			
			Users olduser = info.getUser();
			setErrormsg(info.getMsg());
			if (null != olduser && null != olduser.getLastLoginIp() && !"".equals(olduser.getLastLoginIp())) {
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
			if (olduser != null && null != olduser.getTempLastLoginTime()) {
				getHttpSession().setAttribute("time", olduser.getTempLastLoginTime());
			} else {
				getHttpSession().setAttribute("time", "");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String birthday="";
			if(olduser.getBirthday()!=null){
				birthday=format.format(olduser.getBirthday());
			}
			getHttpSession().setAttribute(Constants.PT_SESSION_USER, password);
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, olduser);
			getHttpSession().removeAttribute("loginNum");
			getHttpSession().setAttribute("birthday",birthday);
			try {
				long timestamp=System.currentTimeMillis();
				String hashCode = Live800Encode.getMD5Encode(URLEncoder.encode(olduser.getLoginname() + olduser.getLoginname() + "" + timestamp + Constants.LIVE800KEY, "UTF-8"));
				String info4Live800 = URLEncoder.encode("userId=" + olduser.getLoginname() + "&name=" + olduser.getLoginname() + "&memo=&hashCode=" + hashCode + "&timestamp=" + timestamp, "UTF-8");
				log.info("###################初始化live800信任信息参数:" + info4Live800);
				getHttpSession().setAttribute(Constants.LIVE800INFOVALUE, info4Live800);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			if(info.getUser().getRole().equals(UserRole.AGENT.getCode())){
				try {
					String agentvip = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
							AxisUtil.NAMESPACE, "getAgentVip", new Object[] { loginname}, String.class);
					getHttpSession().setAttribute(Constants.AGENTVIP, agentvip);
				} catch (AxisFault e) {
					log.error(e.getMessage());
				}
				//查询代理的老虎机佣金额度
				try {
					Userstatus userstatus = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getAgentSlot", new Object[] { loginname }, Userstatus.class);
					if(null == userstatus || userstatus.getSlotaccount() == null){
						userstatus.setSlotaccount(0.0);
					}
					this.getHttpSession().setAttribute("slotAccount", userstatus.getSlotaccount());
				} catch (AxisFault e) {
					e.printStackTrace();
				}
			}
			
			if (StringUtils.equals(loginname, password)) {
				log.info("账号密码一样，提醒玩家修改密码");
				getHttpSession().setAttribute(Constants.NEEDMODIFY, "1");
			} else {
				getHttpSession().setAttribute(Constants.NEEDMODIFY, "0");
			}
			Question question = null;
			try {
				question = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "queryQuestionForApp", new Object[] {olduser.getLoginname() }, Question.class);
			} catch (AxisFault e) {
				e.printStackTrace();
			}
			if(question != null && question.getQuestionid().intValue() == 7 ){

				getHttpSession().removeAttribute(Constants.SESSION_PAYPASSWORD);
				getHttpSession().setAttribute(Constants.SESSION_PAYPASSWORD, question.getContent());
			}
			GsonUtil.GsonObject("SUCCESS");
		} else {// 登录失败
			if (null != info && null == info.getUser() && null == info.getSucFlag()) {
				String msg = info.getMsg();
				if (msg != null && !msg.equals("")) {
					GsonUtil.GsonObject(info.getMsg());
				} else {
					GsonUtil.GsonObject("用户不存在！");
				}
			} else {
				GsonUtil.GsonObject("登录异常，请重新登录！");
			}
		}
		return null;
	}

	/****
	 * 检查活动信息
	 * @return
	 */
	public String checkActivityInfo(){
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"活动开关" }, Const.class);
			if (constPay == null) {
				GsonUtil.GsonObject("该活动不存在");
				return null ;
			}
			// 判断在线支付是否在维护
			if (constPay.getValue().equals("0")) {
				GsonUtil.GsonObject("该活动暂未开启");
				return null ;
			}
			Map<String,Object> returnData = new HashMap<String,Object>();
			Users user = getCustomerFromSession();
			ActivityConfig s = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkActivityInfo", new Object[]{titleId,user.getLevel()}, ActivityConfig.class);
			if(null==s){
				return null;
			}

			returnData.put("amount",s.getAmount());
			if(s.getMultiplestatus()==1){
				returnData.put("platform",s.getPlatform());
				returnData.put("multiple",s.getMultiple()==null?0:s.getMultiple());
			}else {
				returnData.put("multiple",0);
			}

			GsonUtil.GsonObject(returnData);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("领取失败");
			return null ;
		}
		return null ;
	}



	/****
	 * 领取礼品
	 * @return
	 */
	public String applyActivity(){
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] {"活动开关" }, Const.class);
			if (constPay == null) {
				GsonUtil.GsonObject("该活动不存在");
				return null ;
			}
			// 判断在线支付是否在维护
			if (constPay.getValue().equals("0")) {
				GsonUtil.GsonObject("该活动暂未开启");
				return null ;
			}
			String s = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkStatus", new Object[]{titleId,loginname,platform,entrance}, String.class);
			GsonUtil.GsonObject(s);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("领取失败");
			return null ;
		}
		return null ;
	}


	/**
	 * ea 客户端 登录页面
	 * 
	 * @return
	 */
	public String loginTransfer() {
		String view = "input";
		if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null)
			return SUCCESS;

		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(rand)) {
			setErrormsg("验证码错误");
			return INPUT;
		}

		IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
		String remoteIp = getIp();
		String city = "";
		String temp = remoteIp != null ? seeker.getAddress(remoteIp) : "";
		if (null != temp && !"CZ88.NET".equals(temp)) {
			city = temp;
		}
		// 保证执行一次，事务完整
		LoginInfo info = null;

		try {
			info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "login", new Object[] { loginname, password, remoteIp, city }, LoginInfo.class);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrormsg("登录异常，请重新登录！");
			return INPUT;
		}

		if (null != info && null != info.getUser() && 1 == info.getSucFlag()) {// 登录成功
			Users olduser = info.getUser();
			setErrormsg(info.getMsg());
			if (null != olduser && null != olduser.getLastLoginIp() && !"".equals(olduser.getLastLoginIp())) {
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
			if (olduser != null && null != olduser.getTempLastLoginTime()) {
				getHttpSession().setAttribute("time", olduser.getTempLastLoginTime());
			} else {
				getHttpSession().setAttribute("time", "");
			}
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, olduser);
			getHttpSession().removeAttribute("loginNum");
			return SUCCESS;
		} else {// 登录失败
			if (null != info && null == info.getUser() && null == info.getSucFlag()) {
				setErrormsg(info.getMsg());
			} else {
				setErrormsg("登录异常，请重新登录！");
			}
		}
		return view;
	}
	private String transferGameIn;
	public String getTransferGameIn() {
		return transferGameIn;
	}

	public void setTransferGameIn(String transferGameIn) {
		this.transferGameIn = transferGameIn;
	}
	/**
	 * 获取闯关报名情况
	 */
	public void getEmigratedApply(){
		try {
			Users customer = null;
			customer = getCustomerFromSession();
		if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
			GsonUtil.GsonObject("请登录后再操作");
		   }
		
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getEmigratedApply", new Object[]{customer.getLoginname()}, String.class);
			if(!StringUtil.isEmpty(info)){
			GsonUtil.GsonObject(info);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 闯关奖励转入游戏
	 */
	public void submitEmigratedRemit(){
		String info= SynchronizedUtil.getInstance().transferInforEmigrated(type, Double.parseDouble(transferGameIn));
		GsonUtil.GsonObject(info);
	}
	/**
	 * 领取闯关奖励
	 */
	public void doEmigrated(){
		try {
			Users customer = null;
			customer = getCustomerFromSession();
		if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
			GsonUtil.GsonObject("请登录后再操作");
		   }
			String starttime = DateUtil.fmtyyyy_MM_d(new Date())+" 10:00:00";
			Date begindate=DateUtil.parseDateForStandard(starttime);
			if((new Date()).getTime()<begindate.getTime()){
				GsonUtil.GsonObject("请于10点以后再来领取奖励");
				return ;
			}
			
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "doEmigrated", new Object[]{customer.getLoginname()}, String.class);
			if(!StringUtil.isEmpty(info)){
			GsonUtil.GsonObject(info);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 报名闯关活动
	 */
	public void EmigratedApply(){
		try {
			Users customer = null;
			customer = getCustomerFromSession(); 
		if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
			GsonUtil.GsonObject("请登录后再操作");
			return;
		   }
			if(StringUtil.isEmpty(type)){
				GsonUtil.GsonObject("请选择报名等级");
				return;
			}
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "doEmigratedApply", new Object[]{customer.getLoginname(),type}, String.class);
			if(!StringUtil.isEmpty(info)){
			GsonUtil.GsonObject(info);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查询闯关奖金余额
	 */
	public void getEmigratedMoney(){
		try {
			Users customer = null;
			customer = getCustomerFromSession();
		if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
			GsonUtil.GsonObject("请登录后再操作");
		   }
		
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryEmigratedbonus", new Object[]{customer.getLoginname()}, String.class);
			if(!StringUtil.isEmpty(info)){
			GsonUtil.GsonObject(info);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 修改会员个人基本信息
	public String modifyCusinfo() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null)
			setErrormsg("请您从首页登录");
		else
			try {
				if (null != aliasName && !"".equals(aliasName.trim()) && aliasName.trim().length() > 10) {
					this.setErrormsg("昵称格式错误！请填写10个以内的汉字、英文字母或数字");
					return INPUT;
				}
				/*if (UrlPatternFilter.sql_inj(mailaddress.trim())) {
					this.setErrormsg("邮寄地址不允许有特殊字符");
					return INPUT;
				}
				if (null != mailaddress && !"".equals(mailaddress.trim()) && mailaddress.trim().length() > 50) {
					this.setErrormsg("邮寄地址最大长度50个字符");
					return INPUT;
				}*/
				if(StringUtils.isNotEmpty(accountName)) {
					if (!Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$").matcher(accountName).matches()) {
						this.setErrormsg("姓名必须为中文");
						return INPUT;
					}
				}
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "modifyCustomerInfo", new Object[] { sms == null ? 1 : 0, customer.getLoginname(), StringUtils.trim(aliasName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), getIp(), StringUtils.trim(mailaddress),StringUtils.trim(microchannel),StringUtils.trim(birthday),StringUtils.trim(accountName) }, String.class);

				if (null != msg && "success".equals(msg)) {
					refreshUserInSession();
					setErrormsg("修改成功");
					if (null != aliasName && !aliasName.trim().equals(customer.getAliasName())) {
						// 同步bbs 会员昵称
						String bbs_modifyNickName_url = Configuration.getInstance().getValue("bbs_modifyNickName_url");
						int status = new HttpClientHelper().modifyNickName(bbs_modifyNickName_url, StringUtil.convertByteArrayToHexStr(customer.getLoginname().getBytes("gbk")), StringUtil.convertByteArrayToHexStr(aliasName.trim().getBytes("gbk")));
						if (status != 200) {
							this.setErrormsg(this.getErrormsg() + "，同步bbs昵称失败；[" + status + "]");
						}
					}
				} else {
					setErrormsg(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统异常");
			}
		return INPUT;
	}

	
	// 修改会员个人基本信息
	public String modifyCusinfoAjax() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			e.printStackTrace();
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null)
			setErrormsg("请您从首页登录");
		else
			try {
				if (null != aliasName && !"".equals(aliasName.trim()) && aliasName.trim().length() > 10) {
					this.setErrormsg("昵称格式错误！请填写10个以内的汉字、英文字母或数字");
					return INPUT;
				}
				
				if(StringUtils.isNotBlank(mailaddress)){
					if (null != mailaddress && !"".equals(mailaddress.trim()) && mailaddress.trim().length() > 50) {
						this.setErrormsg("邮寄地址最大长度50个字符");
						return INPUT;
					}
					
					if (UrlPatternFilter.sql_inj(mailaddress.trim())) {
						this.setErrormsg("邮寄地址不允许有特殊字符");
						return INPUT;
					}
				}
				if(StringUtils.isNotBlank(email)){
					if (!p_email.matcher(email).matches()) {
						this.setErrormsg("[提示]电子邮箱格式错误！");
						return INPUT;
					}
				}
				if(StringUtils.isNotBlank(accountName)&&!Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$").matcher(accountName).matches()){
					this.setErrormsg("[提示]姓名必须为中文！");
					return INPUT;
				}

				
				
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "modifyCustomerInfo", new Object[] { sms == null ? 1 : 0, customer.getLoginname(), StringUtils.trim(aliasName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), getIp(), StringUtils.trim(mailaddress),StringUtils.trim(microchannel) ,StringUtils.trim(birthday),StringUtils.trim(accountName)}, String.class);

				if (null != msg && "success".equals(msg)) {
					refreshUserInSession();
					setErrormsg("修改成功");
					if(customer.getBirthday()==null){
						getHttpSession().setAttribute("birthday",birthday);
					}
					if (null != aliasName && !aliasName.trim().equals(customer.getAliasName())) {
						// 同步bbs 会员昵称
						String bbs_modifyNickName_url = Configuration.getInstance().getValue("bbs_modifyNickName_url");
						int status = new HttpClientHelper().modifyNickName(bbs_modifyNickName_url, StringUtil.convertByteArrayToHexStr(customer.getLoginname().getBytes("gbk")), StringUtil.convertByteArrayToHexStr(aliasName.trim().getBytes("gbk")));
						if (status != 200) {
							this.setErrormsg(this.getErrormsg() + "，同步bbs昵称失败；[" + status + "]");
						}
					}
				} else {
					setErrormsg(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统异常");
			}
		GsonUtil.GsonObject(errormsg);
		return null;
	}

	// 修改密码
	public String modifyPassword() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请登录后在进行操作");
			return "index" ;
		}
		else
			try {
				if(StringUtils.equals(customer.getLoginname(), new_password) || !StringUtil.regPwd(new_password)){
				   setErrormsg("密码为6-16位数字或英文字母，英文字母开头且不能和账号相同");
				   return INPUT;
				}

				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "modifyPassword", new Object[] { customer.getLoginname(), password, new_password, getIp() }, String.class);

				setErrormsg(null != msg && "success".equals(msg) ? "修改成功" : msg);
				
				if("success".equals(msg)){
					refreshUserInSession();
					getHttpSession().setAttribute(Constants.NEEDMODIFY, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统异常");
			}
		return INPUT;
	}

	// 修改qq
	public String updateQQ() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("请先登录");
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}
		else
			try {

				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "updateQQ", new Object[] { customer.getLoginname(), qq }, String.class);


				if("success".equals(msg)){
					refreshUserInSession();
					getHttpSession().setAttribute(Constants.NEEDMODIFY, null);
					GsonUtil.GsonObject("修改成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("修改失败");
			}
		return null;
	}
	// 修改微信
	public String updateWeiXin() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("请先登录");
		}
		if (customer == null){
			GsonUtil.GsonObject("请先登录");
			return null;
		}
		else
			try {

				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "updateWeiXin", new Object[] { customer.getLoginname(), microchannel }, String.class);

				setErrormsg(null != msg && "success".equals(msg) ? "修改成功" : msg);

				if("success".equals(msg)){
					refreshUserInSession();
					getHttpSession().setAttribute(Constants.NEEDMODIFY, null);
					GsonUtil.GsonObject("修改成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("修改失败");
			}
		return null;
	}
	
	// 修改密码
	public String modifyPasswordAjax() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			log.error("server error", e);
			this.setErrormsg(e.getMessage().toString());
			GsonUtil.GsonObject(errormsg);
			return null;
		}
		if(StringUtils.equals(customer.getLoginname(), new_password) || !StringUtil.regPwd(new_password)){
			setErrormsg("密码为6-16位数字或英文字母，英文字母开头且不能和账号相同");
			GsonUtil.GsonObject(errormsg);
			return null ;
		}
		if (customer == null){
			setErrormsg("请登录后在进行操作");
			GsonUtil.GsonObject(errormsg);
			return null ;
		}
		else
			try {
				if(StringUtils.equals(customer.getLoginname(), new_password) || !StringUtil.regPwd(new_password)){
				   setErrormsg("密码为6-16位数字或英文字母，英文字母开头且不能和账号相同");
				   GsonUtil.GsonObject(errormsg);
				   return null ;
				}
				
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "modifyPassword", new Object[] { customer.getLoginname(), password, new_password, getIp() }, String.class);
				setErrormsg(null != msg && "success".equals(msg) ? "修改成功" : msg);
				
				if("success".equals(msg)){
					getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
					getHttpSession().setAttribute(Constants.NEEDMODIFY, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统异常");
			}
		GsonUtil.GsonObject(errormsg);
		return null;
	}

	// 定制服务
	public String chooseservice() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			GsonUtil.GsonObject(e.getMessage().toString());
			return null;
		}
		if (customer == null)
			GsonUtil.GsonObject("请登录后在进行操作");
		else
			try {
				String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "chooseService", new Object[] {
					customer.getLoginname(),service, getIp()}, String.class);
				
				GsonUtil.GsonObject(null!=msg && "success".equals(msg) ? "定制成功":msg);
				refreshUserInSession();
			} catch (Exception e) {
				e.printStackTrace();
				GsonUtil.GsonObject("系统异常");
			}
		return null;
	}

	// 检查用户是否登陆 sun
	public boolean checkLogin() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			setErrormsg("请您从首页登录");
			return false;
		}
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return false;
		}
		return true;
	}

	public String logout() {

		Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user != null) {
			this.getHttpSession().removeAttribute(Constants.SESSION_CUSTOMERID);
			this.getHttpSession().removeAttribute(Constants.NEEDMODIFY);
			this.getHttpSession().removeAttribute(Constants.SESSION_PAYPASSWORD);
			try {
				AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "logout", new Object[] { user.getLoginname() }, String.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e);
				return SUCCESS;
			}
		}

		return SUCCESS;
	}

	// 开户
	public String register() {
		getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, null);
		if (loginname == null || loginname.equals("")) {
			GsonUtil.GsonObject("登入账号不可为空");
			return null;
		}
		if (loginname!=null && !"".equals(loginname) && !(loginname.trim().length()>5 && loginname.trim().length()<15)) {
			GsonUtil.GsonObject("登入账号长度必须由6-14个数字或小写英文字母组成");
			return null;
		}
		if (phone == null || phone.equals("")) {
			GsonUtil.GsonObject("电话号码不可为空");
			return null;
		}
		/*if (email == null || email.equals("")) {
			GsonUtil.GsonObject("电子邮箱不可为空");
			return null;
		}
		if (!p_email.matcher(email).matches()) {
			GsonUtil.GsonObject("电子邮箱格式错误");
			return null;
		}*/
		if (this.password == null || this.password.trim().equals("")) {
			GsonUtil.GsonObject("登录密码不可为空");
			return null;
		}
		if (this.intro != null && !"".equals(this.intro.trim()) && this.intro.trim().length() > 40) {
			GsonUtil.GsonObject("邀请码长度不允许");
			return null;
		}
		if(StringUtils.equals(loginname, password) || !StringUtil.regPwd(password)){
			setErrormsg("密码为6-16位数字或英文字母，英文字母开头且不能和账号相同");
			GsonUtil.GsonObject(errormsg);
			return null ;
		}
		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(rand)) {
			GsonUtil.GsonObject("验证码错误");
			return null;
		}
		//触点
		/*Boolean result = Touclick.check(website_key, private_key, check_key, check_address, null, null, null);
		if(!result){
			setErrormsg("验证码错误");
			return INPUT;
		}*/
		if (aliasName != null && !aliasName.trim().equals("") && aliasName.trim().length() > 10) {
			GsonUtil.GsonObject("昵称格式错误！请填写10个以内的汉字、英文字母或数字");
			return null;
		}
		// 得到代理域名：
		this.referWebsite = this.getRequest().getScheme() + "://" + this.getRequest().getServerName();
		// 得到如何知道本站点(titile.jsp 配合):
		String howToKnow = (String) this.getRequest().getSession().getAttribute("referURL");

		String remoteIp = getIp();
		if(remoteIp.trim().equals("39.168.17.91") || remoteIp.trim().equals("39.168.18.146") || remoteIp.trim().equals("39.168.20.85")){
			return null;
		}
		IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
		String city = "";
		String temp = remoteIp != null && !"".equals(remoteIp) ? seeker.getAddress(remoteIp) : "";
		if (null != temp && !"CZ88.NET".equals(temp)) {
			city = temp;
		}
		
        //注册用户
		LoginInfo info = null;
		try {
			Proposal introInfo = null;
			if (this.intro != null && !"".equals(this.intro.trim())) {
				introInfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "registerYqm", new Object[] { intro }, Proposal.class);
				if (introInfo == null) {
					GsonUtil.GsonObject("邀请码错误！");
					return null;
				}
			}
			ioBB="";//(String)getHttpSession().getAttribute(Constants.CPUID);
			//String ourDeviceID = (String) getHttpSession().getAttribute(Constants.OURDEVICE);
			email = AESUtil.aesEncrypt(email,AESUtil.KEY);
			phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);
			String agentcode = (String) getHttpSession().getAttribute(Constants.AGENG_CODE);
			//测试
			
			String friendcode = (String) getHttpSession().getAttribute("friendcode");
			if(!StringUtil.isEmpty(friendcode)){
				Users users=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "getUserById", new Object[] {friendcode}, Users.class);
				if (null != users) {
					/*if (accountName.equalsIgnoreCase(users.getAccountName()) || (remoteIp != null && users.getRegisterIp() != null && remoteIp.equals(users.getRegisterIp()))) {
						friendcode = "";
					} else {
						friendcode = users.getLoginname();
						referWebsite = "";
						agentcode = "";
					}*/
                    friendcode = users.getLoginname();
                    referWebsite = "";
                    agentcode = "";
				} else {
					friendcode = "";
				}
			}
			info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "register", new Object[] { StringUtils.trim(howToKnow), sms == null ? 1 : 0, StringUtils.trim(loginname), password, StringUtils.trim(accountName), StringUtils.trim(aliasName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), StringUtils.trim(referWebsite), remoteIp, city, birthday, intro, introInfo ,ioBB , agentcode,friendcode,StringUtils.trim(microchannel)}, LoginInfo.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			GsonUtil.GsonObject("注册异常，请重新注册!");
			return null;
		}
		if (null != info && null != info.getUser() && null != info.getView() && "success".equals(info.getView())) {
			// 同步到论坛
			//SynBbsMemberInfo bbsMemberInfo=new SynBbsMemberInfo(loginname, password);
			//bbsMemberInfo.start();
			// 注册成功
			Users user = info.getUser();
			//屏蔽邮件
			/*
			try {
				String html = EmailTemplateHelp.toHTML(Constants.EMAIL_REGISTER_BODY_HTML, new Object[] { loginname, DateUtil.formatDateForStandard(user.getCreatetime()) });
				mailSender.sendmail(html, user.getEmail(), "恭喜您成为龙都国际娱乐城会员");
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e.getMessage(), e);
			}*/
			// 根据需求，在注册的的时候，违反约束依然可以注册成功，但是账户的状态时被禁用的。 如：注册成功，并且在账号被未禁用的前提下，该用户信息，才会被加入session.
			if (user.getFlag() != 1) {
//				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				//setErrormsg("注册成功，请登录");
			}else{
				GsonUtil.GsonObject("帐号被禁用 :请联系客服!");
				return null;
			}
			getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
			GsonUtil.GsonObject("SUCCESS");
			return null;
		} else {
			if (null != info.getMsg() && !"".equals(info.getMsg())) {
				GsonUtil.GsonObject("抱歉注册失败 :" + info.getMsg());
			} else {
				GsonUtil.GsonObject("注册异常，请重新注册!!");
			}
			return null;
		}
	}
	
	// 代理提款自动平账需求
	public String agentWithdrawpz() {
      	Users customerTwo=null;  
		try {
			 refreshUserInSession();
			 customerTwo = getCustomerFromSession();
			  
			if(customerTwo==null){
				GsonUtil.GsonObject("登陆游戏失效！请重新登陆!");
				return null;
			}
			if(customerTwo.getFlag()==1){
				GsonUtil.GsonObject("账号已冻结，请联系客服!");
				return null;
			}

			if(tkType.equals("slotmachine") && slotmachine >0.0 && liveall <0.0 ){
				slotmachine = slotmachine+liveall;
				if(slotmachine>100){
					GsonUtil.GsonObject("老虎机佣金综合最高可提款金额："+NumericUtil.formatDouble(slotmachine));
				}
				else {
					GsonUtil.GsonObject("老虎机佣金综合余额不足100无法提款");
				}
				return null;
			}
			else if (tkType.equals("liveall") && liveall >0.0 && slotmachine <0.0){
				liveall = liveall+slotmachine;
				if(liveall>100){
					GsonUtil.GsonObject("其它类佣金综合最高可提款金额："+NumericUtil.formatDouble(liveall));	
				}
				else {
					GsonUtil.GsonObject("其它类佣金综合余额不足100无法提款");
				}
				
				return null;
			}
			
			
		} catch (Exception e) {
		    e.printStackTrace();
			GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			return null;   
		}
		return null;   
	}
			

	// 申请提款
/*	public synchronized String withdraw() {
		Users customerTwo=null;
		try {
			 refreshUserInSession();
			 customerTwo = getCustomerFromSession();
			if(customerTwo==null){
				GsonUtil.GsonObject("登陆游戏失效！请重新登陆!");
				return null;
			}
		} catch (Exception e) {
			GsonUtil.GsonObject(e.getMessage().toString());
			return null;
		}
		SynchronizedUtil.getInstance().withdraw(msflag, amount, bank, accountNo, bankAddress, tkType, password, email , questionid , answer);
		try {
			refreshUserInSession();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
		
	// 申请提款
	public String withdraw() {
      	Users customerTwo=null;  
		try {
			 refreshUserInSession();
			 customerTwo = getCustomerFromSession();
			  
			if(customerTwo==null){
				GsonUtil.GsonObject("登陆游戏失效！请重新登陆!");
				return null;
			}
			if(customerTwo.getFlag()==1){
				GsonUtil.GsonObject("账号已冻结，请联系客服!");
				return null;
			}
			log.info(customerTwo.getLoginname()+"PC端秒付宝提款，提款类型"+tkType);
		} catch (Exception e) {
		    e.printStackTrace();
			GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			return null;   
		}
		
		try {
//			if(questionid !=null && questionid.intValue() == 7) { 和APP统一在middleService加密
//				// questionid如果是7，answer就是支付密码，并且md5加密
//				answer = EncryptionUtil.encryptPassword(answer);
//			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("product", Constants.PRODUCT_NAME);
			paramMap.put("loginName", customerTwo.getLoginname());
			paramMap.put("ip", getIpAddr());
			paramMap.put("password", password);
			paramMap.put("money", amount);
			paramMap.put("bankName", bank);
			paramMap.put("accountNo", accountNo);
			paramMap.put("tkType", tkType);
			paramMap.put("questionid", questionid);
			paramMap.put("answer", answer);
			
			String str = mapper.writeValueAsString(paramMap);
			
			String requestJson = app.util.AESUtil.getInstance().encrypt(str);
			
			String url = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("middleservice.url"));
			
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
				
	    		GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
			} else {
				
				byte[] responseBody = method.getResponseBody();
		    	String responseString = new String(responseBody);
		    	
		    	Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
		    	responseString = resultMap.get("responseData");
		    	responseString = app.util.AESUtil.getInstance().decrypt(responseString);
	    		
	    		resultMap = mapper.readValue(responseString, Map.class);
	    		
	    		GsonUtil.GsonObject(resultMap.get("message"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
		}

		try {
			refreshUserInSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	    	
		
		
		

	
	public String getRemarkAgain(){
		
		String sessionCode = (String) getHttpSession().getAttribute(Constants.USER_DEPOSIT);
 		if(!sessionCode.equals(depositCode)){
			GsonUtil.GsonObject("验证码不正确..");
			return null;
		}
		
		String [] banks = {"工商银行","招商银行","中国银行","广发银行","交通银行","建设银行","支付宝","农业银行"};
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(banks) ;
		if(!list.contains(bankname)){
			GsonUtil.GsonObject("不存在该种银行。");
			return null;
		}
		if(StringUtils.isBlank(username)){
			GsonUtil.GsonObject(bankname+"未开启。");
			return null;
		}
		String msg = null ;
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "getRemarkAgain", new Object[] {
				loginname, bankname , username}, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("网络繁忙..");
			return null ;
		}
		if(null == msg){
			GsonUtil.GsonObject("网络繁忙Q..");
		}else{
			GsonUtil.GsonObject(msg);
		}
		return null ;
	}
	

	

	
	/**
	 * 申请存款
	 * 
	 * @return
	 */
	public String addCashinProposal() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null) {
			setErrormsg("请登录后在进行操作");
			return INPUT;
		}

		try {
			if (username == null || username.equals("")) {
				setErrormsg("请填写收款人姓名");
				return INPUT;
			}
			if (saveway.equals("")) {
				setErrormsg("请选择存款方式");
				return INPUT;
			}
			if (bankname == null || bankname.equals("")) {
				setErrormsg("请填写收款人对应的收款银行");
				return INPUT;
			}
			String sql = "select * from bankinfo where type=1 and username=? and bankname=? ";
			// List ls=
			// proposalService.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql).list();
			List ls = userbankinfoService.getBankinfo(sql, username, bankname);
			if (ls == null || ls.size() == 0) {
				setErrormsg("没有与之对应的银行账户，请仔细核对");
				return INPUT;
			}
			String msg = proposalService.addCashin("", StringUtil.trim(customer.getLoginname()), StringUtil.trim(accountName), "", Constants.FROM_FRONT, amount, StringUtil.trim(bankname), StringUtil.trim(remark), IssuingBankNumber.getText(bankname), username, saveway, cashintime);
			if (msg == null)
				setErrormsg("提交成功");
			else
				setErrormsg("提交失败:" + msg);

		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}
/**
 * 所有的转入操作
 * @return
 */
	public String transferIn() {

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
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferIn", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("ag")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("agin")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("keno")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
			}else if(gameType.equals("keno2")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforkeno2", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
			}else if(gameType.equals("sba")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforSbaTy", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("pt")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferNewPtIn", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("bbin")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("sixlottery")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInSixLottery", new Object[] { getCustomerLoginname(), remit ,getIp() }, String.class);
			}else if(gameType.equals("ebet")){
				Integer ebetRemit=0;
				try {
					ebetRemit = remit.intValue();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.toString());
					GsonUtil.GsonObject("提交失败:金额格式不正确！");
					return null;
				}
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "IN"}, String.class);
			}else if(gameType.equals("ttg")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
						AxisUtil.NAMESPACE, "transferTtIn", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("jc")){
				//TODO 时时彩下架, 限制转入
				msg = "尊敬的玩家您好, E68因发展需要即将下架时时彩, 请您尽快转出时时彩金额!!";
				//msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferToJc", new Object[] {getCustomerLoginname(), remit}, String.class);
			}else if(gameType.equals("gpi")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforGPI", new Object[] { getCustomerLoginname(), remit }, String.class);
			}
			
			refreshUserInSession();
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		if (msg == null) {
			GsonUtil.GsonObject("转账成功");
		} else {
			GsonUtil.GsonObject("提交失败:" + msg);
		}
		return null;
	}

	public String transferInEa() {
		
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer==null) {
				GsonUtil.GsonObject("请登录后在进行操作");
				return null;
			}
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "transferInEa", 
					new Object[] {getCustomerLoginname()}, String.class);
			
			refreshUserInSession();
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		if (msg == null) {
			GsonUtil.GsonObject("SUCCESS");
		} else {
			GsonUtil.GsonObject("提交失败:" + msg);
		}
		return null;
	}
	
	
	/**
	 * 获取游戏金额
	 * @return
	 */
	public String getGameMoney(){
		try {
			Users customer = getCustomerFromSession();
			String password=(String) getHttpSession().getAttribute(Constants.PT_SESSION_USER);
			if(customer==null){
				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if(gameCode==null || gameCode.equals("")){
				GsonUtil.GsonObject("请选择账户!");
				return null;
			}
			if(gameCode.equals("self")){
				GsonUtil.GsonObject(StringUtil.getMoneyfromStr(customer.getCredit().toString()));
				return null;
			}
			if(gameCode.equals("ea")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getEaGameMoney(customer.getLoginname()) + "元");
				return null;
			}
			if(gameCode.equals("ag")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getAgGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("agin")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getAginGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if (gameCode.equals("qd")) {
				String amout = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "querySignAmount", new Object[]{customer.getLoginname()}, String.class);
				GsonUtil.GsonObject(amout+" 元");
				return null;
			}
			if(gameCode.equals("keno")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getKenoGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("keno2")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getKeno2GameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("sba")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getSbaGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("mwg")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getMwgGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("newpt")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getPtPlayerMoney", new Object[] { customer.getLoginname(),password }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("sixlottery")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getSixLotteryGameMoney", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("ebet")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getEbetBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("ttg")){
				/*Object result = (customer != null ? PtUtil1.getPlayerAccount(customer.getLoginname()) : "");
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;*/
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getTtgBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("jc")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getJcBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("fanya")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
						AxisUtil.NAMESPACE, "getFanyaBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("gpi")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getGPIBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("qt")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getQtBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("dt")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getDtBalance", new Object[] { customer.getLoginname(),password }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("mg")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getMGBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("cq9")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getCQ9Balance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("pg")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getPGBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("nt")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getNTBalance", new Object[] { customer.getLoginname() }, String.class);
				JSONObject rj = JSONObject.fromObject(result);
				GsonUtil.GsonObject(rj.getBoolean("result")?rj.getDouble("balance")+"元":NTErrorCode.compare(rj.getString("error")));
				return null;
			}
			if(gameCode.equals("fish")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false),
						AxisUtil.NAMESPACE, "getFishBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("slot")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
						AxisUtil.NAMESPACE, "getSlotBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			
			if (gameCode.equals("n2live")) {
				NTwoCheckClientResponseBean bean = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getNTwoCheckClientResponseBean", new Object[] {customer.getLoginname()}, NTwoCheckClientResponseBean.class);
				GsonUtil.GsonObject(bean==null?"系统繁忙":String.valueOf(bean.getBalance().setScale(2, RoundingMode.HALF_UP))+" 元");
				return null;
			}
			if(gameCode.equals("ebetapp")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),AxisUtil.NAMESPACE, "getEbetAppBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元"   );
				return null;
			}
			if(gameCode.equals("png")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getPNGBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("chess")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
						AxisUtil.NAMESPACE, "getChessBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("bbin")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
						AxisUtil.NAMESPACE, "getBbinBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("pb")){
				Double result = getPBBalance();
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("bit")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
						AxisUtil.NAMESPACE, "getBitGameBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}if(gameCode.equals("kyqp")||gameCode.equals("vr")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
						AxisUtil.NAMESPACE, "getKyqpBalance", new Object[] { customer.getLoginname(),gameCode,getIp()}, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("redrain")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getRedWalletBalance(customer.getLoginname())+" 元");
				return null;
			}
			return null;
		}  catch (Exception e) {
			GsonUtil.GsonObject("网络繁忙！请稍后再试！");
			return null;
		}
	}
	/**
	 * 平博用户登录
	 *
	 * @return
	 */
	public String PBUserLogin() {
		log.info("平博用户登录开始");
		PostMethod method = null;
		try {
			refreshUserInSession();
			Users customer = getCustomerFromSession();
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", "ld");
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

			method = new PostMethod(url + "/gameCenter/userLogin/PBUserLogin");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.info("系统繁忙，请稍后重试,statusCode:"+statusCode);
				GsonUtil.GsonObject("系统繁忙，请稍后重试！");
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
			GsonUtil.GsonObject("系统繁忙，请稍后重试！");
			log.error(e);
		} finally {
			// 释放
			if (null != method) {
				method.releaseConnection();
			}
		}
		log.info("平博用户登录结束");
		return null;
	}
	/**
	 * 获取平博用户余额
	 *
	 * @return
	 */
	public Double getPBBalance() {
		log.info("平博用户获取余额开始");
		PostMethod method = null;
		try {
			refreshUserInSession();
			Users customer = getCustomerFromSession();
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", "ld");
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
				log.info("系统繁忙，请稍后重试,statusCode:"+statusCode);
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
		log.info("平博用户获取余额结束");
		return null;
	}
	/**
	 * 签到奖金转入游戏平台
	 */
	public void transferSign(String  transferGameMoney,String transferGameOut) {
		try {
			if(Integer.valueOf(transferGameMoney)<5){
				GsonUtil.GsonObject("签到金额需大于或等于5元");
				return ;
			}
			Users customer = null;
			customer = getCustomerFromSession();
			if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
				GsonUtil.GsonObject("请登录后再操作");
				return;
			}
			if(!(transferGameMoney).matches("^\\d*[1-9]\\d*$")){
				GsonUtil.GsonObject("转账金额必须为整数！");
			}
			if(transferGameIn.equals("qd")){
				GsonUtil.GsonObject("不能转账，任何平台游戏都不能转至签到余额");
				return;
			}
			if(transferGameOut.equals("qd")&&transferGameIn.equals("self")){
				GsonUtil.GsonObject("不能转账，签到余额不能转到主账户");
				return;
			}
			Date now = new Date();
			String msg = SynchronizedUtil.getInstance().chekDepositOrderRecord(customer,DateUtil.getDateBegin(now),DateUtil.getDateEnd(now));//检查签到转账的存款记录
			if(!"success".equals(msg)){
				GsonUtil.GsonObject(msg);
				return;
			}
		/*boolean boo= checkWaterAmout(customer,10);//检查签到转账的流水是否达到要求 （10倍）   注意：还有一个条件为 目标的钱不能大于5元   在后面判断的
		if(!boo){
			return;
		}*/
			SynchronizedUtil.getInstance().transferInforSign(transferGameIn,Integer.parseInt(transferGameMoney));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//获取生日礼金
	public void getBirthdayMoney() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
				return;
			}
			Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {
					customer.getLoginname()}, Users.class);
			Integer level = user.getLevel();
			Date birthday = customer.getBirthday();
			if(birthday == null){
				GsonUtil.GsonObject("请先设置出生年月");
				return;
			}
			Map<String,Object> returnData = new HashMap<String,Object>();
			if(level == 0){
				returnData.put("amount","0");
				returnData.put("birthday",DateUtil.fmtyyyy_MM_d(birthday));
				returnData.put("level",user.getLevel().toString());
				returnData.put("isDraw",false);
				returnData.put("state",false);
				GsonUtil.GsonObject(returnData);
				return;
			}

			if(birthday == null){
				GsonUtil.GsonObject("请先完善您的个人信息后再进行领取噢");
				return;
			}
			//按照会员等级对应系统派发彩金;存款量
			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,
					"getBirthdayMoney",
					new Object[] { customer.getLoginname() }, String.class);
			System.out.println("============"+result);
			if(StringUtils.isNotBlank(result) && !StringUtil.equals(result,"未配置生日礼金活动奖励")){
				Date currDate = new Date();
				String year = DateUtil.getYear(currDate);
				String month = DateUtil.getMonth(birthday);
				String day = DateUtil.getDay(birthday);
				Date newDay = DateUtil.parseDateForStandard(year+"-"+month+"-"+day+" 00:00:00");
				Date beforeDays = DateUtil.getMontToDate(newDay, -3);
				Date afterDays = DateUtil.getMontToDate(newDay, 4);
				if(currDate.after(afterDays) || currDate.before(beforeDays)){
					returnData.put("state",false);
				}else {
					returnData.put("state",true);
				}
				String[] tt=result.split("元");
				String s = tt[2];
				returnData.put("amount",tt[0]);
				returnData.put("birthday",DateUtil.fmtyyyy_MM_d(birthday));
				returnData.put("level",user.getLevel().toString());
				returnData.put("id",tt[1]);
				returnData.put("isDraw",s.equals("0")?true:false);
				GsonUtil.GsonObject(returnData);
			}else{
				GsonUtil.GsonObject("未配置生日礼金活动奖励");

			}


		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统异常");
		}
	}

	//领取生日礼金金额，
	public void drawBirthdayMoney() {
		try {
			Map<String,Object> returnData = new HashMap<String,Object>();
			Users customer = getCustomerFromSession();
			if (null == customer || StringUtil.isEmpty(customer.getLoginname())) {
				GsonUtil.GsonObject("请登录后再操作");
				return;
			}


			Date birthday = customer.getBirthday();
			if(birthday == null){
				returnData.put("message","请先完善您的个人信息后再进行领取噢");
				returnData.put("state",false);
				GsonUtil.GsonObject(returnData);
				return;
			}
			Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] {
					customer.getLoginname()}, Users.class);
			Integer level = user.getLevel();
			if(level == 0){
				returnData.put("message","您的VIP等级不在奖励范围之内");
				returnData.put("state",false);
				GsonUtil.GsonObject(returnData);
				return;
			}
			Date currDate = new Date();
			String year = DateUtil.getYear(currDate);
			String month = DateUtil.getMonth(birthday);
			String day = DateUtil.getDay(birthday);
			Date newDay = DateUtil.parseDateForStandard(year+"-"+month+"-"+day+" 00:00:00");
			Date beforeDays = DateUtil.getMontToDate(newDay, -3);
			Date afterDays = DateUtil.getMontToDate(newDay, 4);
			if(currDate.after(afterDays) || currDate.before(beforeDays)){
				returnData.put("message","请在您填写的生日前后三天内领取");
				returnData.put("state",false);
				GsonUtil.GsonObject(returnData);
				return;
			}
			if(id==""){
				returnData.put("message","未配置生日礼金活动奖励");
				returnData.put("state",false);
				GsonUtil.GsonObject(returnData);
				return;
			}
			String result = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false),
					AxisUtil.NAMESPACE, "drawBirthdayMoney", new Object[] {user.getLoginname(),Integer.parseInt(id)},String.class);
			returnData.put("message",result);
			if(result.contains("成功领取")){
				returnData.put("state",true);
			}else {
				returnData.put("state", false);
			}
			GsonUtil.GsonObject(returnData);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}






	/**
	 * 转账到游戏
	 * @return
	 */
	/*public synchronized String updateGameMoney() {
		try {
			String transferGameOut = getRequest().getParameter("transferGameOut");
			String transferGameMoney = getRequest().getParameter("transferGameMoney");
			if("qd".equals(transferGameOut)){
				transferSign(transferGameMoney,transferGameOut);
				refreshUserInSession();
			}
			refreshUserInSession();
			SynchronizedUtil.getInstance().updateGameMoney(remit);
			refreshUserInSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	public String updateGameMoney() {

		try {

			String transferGameOut = getRequest().getParameter("transferGameOut");
			String transferGameIn = getRequest().getParameter("transferGameIn");
			String transferGameMoney = getRequest().getParameter("transferGameMoney");

			refreshUserInSession();

			Users customer = getCustomerFromSession();

			if (customer == null) {

				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if(transferGameIn.equals("redrain")){
				if(Double.valueOf(transferGameMoney)<10.0){
					GsonUtil.GsonObject("转账金额不能少于10元");
					return null;
				}
				String msg = RedRainAction.transferSelfToRedRain(customer.getLoginname(), transferGameMoney);
				GsonUtil.GsonObject(msg);
				return null;
			}

			if ("qd".equals(transferGameOut)) {

				transferSign(transferGameMoney, transferGameOut);

				refreshUserInSession();

				return null;
			}

			
			if ("n2live".equals(transferGameOut) || "n2live".equals(transferGameIn)) {

				String returnStr=SynchronizedUtil.getInstance().updateGameMoney(remit);
				refreshUserInSession();
				GsonUtil.GsonObject(returnStr);

				refreshUserInSession();

				return null;
			}
			
			String msg = transfer(customer.getLoginname(), transferGameOut, transferGameIn, transferGameMoney);

			refreshUserInSession();

			GsonUtil.GsonObject(msg);
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后重试！");
		}

		return null;
	}


	/**
	 * 查询玩家各平台的输赢(柱状图)
	 * @return
	 */
	public String queryProfit() {
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			PlatformVo plateformVo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryagprofit", new Object[] {loginname}, PlatformVo.class);
			JSONObject fromObject = net.sf.json.JSONObject.fromObject(plateformVo);
			writeJson(fromObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void checkAgentUrl() {

		try {

			this.referWebsite = this.getRequest().getScheme() + "://" + this.getRequest().getServerName();

			Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getAgentByUrl", new Object[] { referWebsite }, Users.class);

			if (user != null) {

				GsonUtil.GsonObject(true);
				return;

			}

			GsonUtil.GsonObject(false);
		} catch (Exception e) {

			log.error("根据url：" + referWebsite + "获取代理账号失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject(false);
		}
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

			if (statusCode != HttpStatus.SC_OK) {

				return "系统繁忙，请稍后重试！";
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
			return "系统繁忙，请稍后重试！";
		}
	}

	/**
	 * DT登录或者创建用户
	 * @return
	 */
	public String loginDT() {
		try {
			if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null) {
				Users user =(Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
				String password=(String) getHttpSession().getAttribute(Constants.PT_SESSION_USER);
				if(null==user||StringUtil.isEmpty(user.getLoginname())){
					return "error";
				}
				loginname = user.getLoginname();
				DtVO info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "loginDT", new Object[]{loginname,password}, DtVO.class);
				if(null!=info&&null!=info.getSlotKey()){
					getHttpSession().setAttribute(Constants.SESSION_DT_SLOTKEY, info.getSlotKey());
					getHttpSession().setAttribute(Constants.SESSION_DT_GAMEURL, info.getGameurl());
					getHttpSession().setAttribute("referWebsite", this.getRequest().getScheme()+"://"+this.getRequest().getServerName());
					return "success";
				}else {
					getHttpSession().removeAttribute(Constants.SESSION_DT_SLOTKEY);
					getHttpSession().removeAttribute(Constants.SESSION_DT_GAMEURL);
					return "error";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			getHttpSession().removeAttribute("TTplayerhandle");
			return "error";
		}
		getHttpSession().removeAttribute("TTplayerhandle");
		return "error";
	}
	
	/**
	 * mwgpc登录
	 * @return
	 */
	public void mwgLogin() {
		Users customer = null;
		ReturnInfo ri = new ReturnInfo();
		try {
			customer = getCustomerFromSession();
			if( customer == null){
				ri.setCode("-1");
				ri.setMsg("请登录后，在进行操作");
				GsonUtil.GsonObject(ri);
				return;
			}
			String loginUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"mwgLogin", new Object[]{customer.getLoginname(),gameCode,0}, String.class);
			//System.out.println(loginUrl);
			if(loginUrl != null){
				ri.setCode("0");
				ri.setMsg("");
				ri.setData(loginUrl);
				GsonUtil.GsonObject(ri);
			}else{
				ri.setCode("-3");
				ri.setMsg("游戏地址错误");
				GsonUtil.GsonObject(ri);
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			ri.setCode("-2");
			ri.setMsg("系统异常请稍后再试！");
		}
		GsonUtil.GsonObject(ri);
		return ;
	}
	
	/**
	 * tt登录或者创建用户
	 * @return
	 */
	public String ttLogin() {
		try {
		if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null) {
			Users user =(Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if(null==user||StringUtil.isEmpty(user.getLoginname())){
				return "error";
			}
			String gameName = getRequest().getParameter("gameName");
			String gameId = getRequest().getParameter("gameId");
			String gameType = getRequest().getParameter("gameType");
			if (StringUtils.isEmpty(gameName) || StringUtils.isEmpty(gameId)){
//				setErrorMsg("进入TTG游戏失败，请您从TTG页面进入吧");
				return ERROR;
			}
			getRequest().setAttribute("ttg_gameName", gameName);
			getRequest().setAttribute("ttg_gameId", gameId);
			getRequest().setAttribute("ttg_gameType", gameType);
			if (getHttpSession().getAttribute("TTplayerhandle") != null){
				return SUCCESS;
			}
			
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "loginttg", new Object[]{user.getLoginname()}, String.class);
			if(!StringUtil.isEmpty(info)){
				getHttpSession().setAttribute("TTplayerhandle", info);
				return "success";
			}else {
				getHttpSession().removeAttribute("TTplayerhandle");
				return "error";
			}
		}
				} catch (Exception e) {
			e.printStackTrace();
			getHttpSession().removeAttribute("TTplayerhandle");
			return "error";
		}
		getHttpSession().removeAttribute("TTplayerhandle");
		return "error";
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
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@SuppressWarnings({ "unchecked" })
	public String getSelfYouHuiObject() {
		
		String loginName = null;
		String postCode = null;
		
		try {
			
			refreshUserInSession();
			
			Users customer = getCustomerFromSession();
			loginName = customer.getLoginname();
			postCode = customer.getPostcode();
		} catch (Exception e) {
			
			e.printStackTrace();
			GsonUtil.GsonObject("[提示]登录已过期，请重新登录！");
			return null;
		}
		
		try {
			if(StringUtils.isNotEmpty(type)) {
				String b = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkLuActivity", new Object[]{getCustomerLoginname()}, String.class);
				if (StringUtils.isNotEmpty(b)) {
					GsonUtil.GsonObject(b);
					return null;
				}
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("product", "ld");
			paramMap.put("loginName", loginName);
			paramMap.put("platform", platformId);
			paramMap.put("id", id);
			paramMap.put("type", titleId);
			paramMap.put("amount", remit);
			paramMap.put("sid", postCode);
			paramMap.put("channel", "官网");
			
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
				if(StringUtils.isNotEmpty(type)) {
					if (resultMap.get("code").equals("10000")) {
						String s = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "luActivity", new Object[]{getCustomerLoginname(), remit, platformId, type}, String.class);
					}
				}
	    		
	    		GsonUtil.GsonObject(resultMap.get("message"));
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			GsonUtil.GsonObject("[提示]系统繁忙，请稍后再试！");
		}
		
		return null;
	}
	

	public String transferInForCaiwu() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferIn", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");
			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			setErrormsg("提交失败:" + msg);
		}
		return INPUT;
	}
	/**
	 * 所有的转出
	 * @return
	 */
	public String transferOut() {
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
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOut", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("ag")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("agin")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("keno")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
			}else if(gameType.equals("keno2")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforkeno2", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);
			}else if(gameType.equals("bbin")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("sba")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforSbaTy", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("pt")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferNewPtOut", new Object[] { getCustomerLoginname(), remit }, String.class);
			}else if(gameType.equals("sixlottery")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutSixLottery", new Object[] { getCustomerLoginname(), remit ,getIp() }, String.class);
			}else if(gameType.equals("ebet")){
				Integer ebetRemit=0;
				try {
					ebetRemit = remit.intValue();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.toString());
					GsonUtil.GsonObject("提交失败:金额格式不正确！");
					return null;
				}
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "ebetTransfer", new Object[] {getCustomerLoginname(), ebetRemit, "OUT"}, String.class);

			}else if(gameType.equals("ttg")){
			    msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
						AxisUtil.NAMESPACE, "transferTtOut", new Object[] { getCustomerLoginname(), remit }, String.class);

			}else if(gameType.equals("jc")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferFromJc", new Object[] {getCustomerLoginname(), remit}, String.class);
			}else if(gameType.equals("gpi")){
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOut4GPI", new Object[] { getCustomerLoginname(), remit }, String.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
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

	public String transferOutforDsp() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");
			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferOutforAginDsp() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");
			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferInforDsp() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforDsp", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferInforAginDsp() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforAginDsp", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferOutforkeno() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");
			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}
	public void checkAgentURLogin(){

		try {
			this.referWebsite=this.getRequest().getScheme()+"://"+this.getRequest().getServerName();
			Users user=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "getAgentByUrl", new Object[] {referWebsite}, Users.class);
			if(user != null && "AGENT".equalsIgnoreCase(user.getRole())){
				Users customer = getCustomerFromSession();
				if (customer == null) {
					GsonUtil.GsonObject(false);
					return;
				}
			}
			GsonUtil.GsonObject(true);
		} catch (Exception e) {
			log.error("根据URL获取代理账号失败：", e);
			GsonUtil.GsonObject(true);
		}
	}
	public String transferInforkeno() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforkeno", new Object[] { getCustomerLoginname(), remit, getIp() }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	// 存送优惠券提交
	public void transferInforCoupon() {

		String msg = "";

		try {

			Users customer = getCustomerFromSession();

			if (null == customer) {

				GsonUtil.GsonObject("请登录后在进行操作！");
				return;
			}

			msg = SynchronizedUtil.getInstance().coupon(customer.getLoginname(), couponCode, couponType, couponRemit, "/self/depositCoupon/submit");
		} catch (Exception e) {

			e.printStackTrace();
			msg = "系统繁忙，请稍后重试！";
		}

		GsonUtil.GsonObject(msg);
	}

	/*public synchronized String transferInforCoupon() {
		SynchronizedUtil.getInstance().transferInforCoupon(couponRemit, couponType, remit, couponCode);
		return null;
	}*/

	public String transferInforCouponSb() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("登录已过期，请重新登录!");
				return INPUT;
			}
			remit = 0.0;
			if (couponRemit != null) {
				remit = couponRemit;
				if (remit <= 0) {
					this.setErrormsg("存款金额必须大于0！");
					return INPUT;
				}
			}
			if (couponCode == null || couponCode.equals("")) {
				this.setErrormsg("优惠代码不能为空!");
				return INPUT;
			}
			String ip = getIp();
			if (ip == null || ip.equals("")) {
				ip = "127.0.0.1";
			}
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforCouponSb", new Object[] { getCustomerLoginname(), remit, couponCode, ip }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferOutforBbin() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferInforBbin() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforBbin", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferOutforSky() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforSky", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferAjaxOutforSky() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				GsonUtil.GsonObject(getErrormsg());
				return null;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				GsonUtil.GsonObject(getErrormsg());
				return null;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				GsonUtil.GsonObject(getErrormsg());
				return null;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforSky", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			GsonUtil.GsonObject(msg);
			return null;
		}

		if (msg == null) {
			setErrormsg("转账成功");
			List<String> list = new ArrayList();
			try {
				refreshUserInSession();
				Users customer = getCustomerFromSession();
				String e68credit = String.valueOf(customer.getCredit());
				String ptcredit = RemoteCaller.queryPtCredit(customer.getId());
				list.add(getErrormsg());
				list.add(e68credit);
				list.add(ptcredit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GsonUtil.GsonList(list);
			return null;
		} else {
			setErrormsg("提交失败:" + msg);
			GsonUtil.GsonObject(getErrormsg());
			return null;
		}
	}


	//查询工会
	public String queryGuild() {

		try {


			Users customer = getCustomerFromSession();

			if (customer == null) {

				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}

			List<Guild> result = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"queryGuild", new Object[]{customer.getLoginname()}, Guild.class);

			Guild self = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"selfGuild", new Object[]{customer.getLoginname()}, Guild.class);
			GuildData guildData= new GuildData();
			guildData.setGuildList(result);
			if(null!=self){
				guildData.setSelfGuild(self.getName());
			}

			refreshUserInSession();

			GsonUtil.GsonObject(guildData);
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后重试！");
		}

		return null;
	}


	public String queryUserLuActivity() {

		try {
			Users customer = getCustomerFromSession();

			if (customer == null) {

				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}

            List<Activity> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
                    + "UserWebService", false), AxisUtil.NAMESPACE, "LuActivityHistory", new Object[] {customer.getLoginname()}, Activity.class);
			GsonUtil.GsonObject(list);
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后重试！");
		}

		return null;
	}


	/***
	 * 查询工会数据
	 * @return
	 */
	public String queryGuildDate(){
		try {
			Users user=(Users) getCustomerFromSession();
			if (user==null) {
				GsonUtil.GsonObject("请登录后在进行操作");
				return null;
			}
			if(null==pageIndex){
				pageIndex=1;
			}
			if(null==size){
				size=10;
			}


			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryGuildDate", new Object[] {guildId,pageIndex, size}, Page.class,GuildStaff.class);
			getRequest().setAttribute("page", page);
			GsonUtil.GsonObject(page);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 查询工会数据
	 * @return
	 */
	public String queryGuildRanking(){
		try {
			Users user=(Users) getCustomerFromSession();
			if (user==null) {
				GsonUtil.GsonObject("请登录后在进行操作");
				return null;
			}
			if(null==pageIndex){
				pageIndex=1;
			}
			if(null==size){
				size=10;
			}


			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryGuildRanking", new Object[] {pageIndex, size}, Page.class,GuildRanking.class);
			getRequest().setAttribute("page", page);
			GsonUtil.GsonObject(page);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public String queryGuildCountEach(){
		try {
			Users user=(Users) getCustomerFromSession();
			if (user==null) {
				GsonUtil.GsonObject("请登录后在进行操作");
				return null;
			}

			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "queryGuildCountEach", new Object[] {}, Page.class,GuildCountEach.class);
			getRequest().setAttribute("page", page);
			GsonUtil.GsonObject(page);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	//加入工会
	public String joinGuild() {

		try {


			Users customer = getCustomerFromSession();

			if (customer == null) {

				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if (guildId == null) {

				GsonUtil.GsonObject("请先选择工会！");
				return null;
			}

			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"joinGuild", new Object[]{customer.getLoginname(),guildId}, String.class);

			refreshUserInSession();

			GsonUtil.GsonObject(result);
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后重试！");
		}

		return null;
	}

	public String transferInforSky() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforSky", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferAjaxInforSky() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				GsonUtil.GsonObject(getErrormsg());
				return null;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				GsonUtil.GsonObject(getErrormsg());
				return null;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				GsonUtil.GsonObject(getErrormsg());
				return null;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforSky", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");
			List<String> list = new ArrayList();
			try {
				refreshUserInSession();
				Users customer = getCustomerFromSession();
				String e68credit = String.valueOf(customer.getCredit());
				String ptcredit = RemoteCaller.queryPtCredit(customer.getId());
				list.add(getErrormsg());
				list.add(e68credit);
				list.add(ptcredit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GsonUtil.GsonList(list);
			return null;
		} else {
			setErrormsg("提交失败:" + msg);
			GsonUtil.GsonObject(getErrormsg());
			return null;
		}
	}

	/**
	 * 得到PT额度
	 */
	public String transferAjaxGetPtE68Monery() {
		/**
		 * session失效跳转首页
		 */
		Calendar c = Calendar.getInstance();
		Date dnow = c.getTime();
		c.set(Calendar.HOUR_OF_DAY, 11);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 0);
		Date sc = c.getTime();
		c.set(Calendar.HOUR_OF_DAY, 13);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date ec = c.getTime();
		if (dnow.after(sc) && dnow.before(ec)) {
			try {
				SkyUtils.removieUser(getCustomerFromSession().getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "maintain";
		}

		String resultAmout = "";
		try {
			List<String> list = new ArrayList<String>();
			Users skyCustomer = getCustomerFromSession();
			if (skyCustomer != null) {
				String loginString = SkyUtils.getSkyMonery(skyCustomer.getId());
				JSONObject jsonObj = JSONObject.fromObject(loginString);
				try {
					if (jsonObj.containsKey("error")) {
						Integer error = jsonObj.getInt("error");
						if (error == 1) {
							resultAmout = "参数错误";
						} else if (error == 2) {
							resultAmout = "货币错误";
						} else if (error == 3) {
							resultAmout = "token或secret key 有误";
						} else if (error == 4) {
							resultAmout = "jackpot group id错误";
						} else if (error == 5) {
							resultAmout = "会员id错误";
						} else if (error == 6) {
							resultAmout = "用户名不能进入游戏";
						} else if (error == 7) {
							resultAmout = "资金不足";
						} else if (error == 8) {
							resultAmout = "日期错误";
						}
					}
					if (jsonObj.containsKey("balance")) {
						Double balance = jsonObj.getDouble("balance");
						if (balance != null) {
							resultAmout = String.valueOf(balance / 100);
							list.add(String.valueOf(skyCustomer.getCredit()));
							list.add(resultAmout);
							GsonUtil.GsonList(list);
							return null;
						} else {
							resultAmout = "获取游戏金额失败！";
						}
					}
				} catch (Exception e) {
				}
			} else {
				resultAmout = "登录失效！请重新登录!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultAmout = e.getMessage();
		}
		GsonUtil.GsonObject(resultAmout);
		return null;
	}


	// 支付密码
	public String modifyPasswordPayAjax() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			GsonUtil.GsonObject(errormsg);
			return null;
		}
		if( !StringUtil.regPayPwd(new_content)){
			setErrormsg("支付密码为6位纯数字");
			GsonUtil.GsonObject(errormsg);
			return null ;
		}
		String msg = null;
		if( StringUtil.isEmpty(content) ){
			// 原登录密码
			if( StringUtils.isBlank(password)){
				setErrormsg("登录密码不能为空");
				GsonUtil.GsonObject(errormsg);
				return null ;
			}
//			setErrormsg("原支付密码不能为空");
//			GsonUtil.GsonObject(errormsg);
//			return null ;

			try {
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "modifyPayPassword", new Object[] { customer.getLoginname(), password, new_content, 0 }, String.class);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("修改支付密码失败");
				GsonUtil.GsonObject(errormsg);
				return null;
			}

			setErrormsg(null != msg && "success".equals(msg) ? "修改成功" : msg);
			if("success".equals(msg) == true) {
				getHttpSession().setAttribute(Constants.SESSION_PAYPASSWORD, EncryptionUtil.encryptPassword(new_content));
			}
		}
		if( StringUtil.isEmpty(password)){
			//原支付密码为6位纯数字
			if( !StringUtil.regPayPwd(content)){
				setErrormsg("支付密码为6位纯数字");
				GsonUtil.GsonObject(errormsg);
				return null ;
			}
			try {
				msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "modifyPayPassword", new Object[] { customer.getLoginname(), content, new_content,1 }, String.class);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("修改支付密码失败");
				GsonUtil.GsonObject(errormsg);
				return null;
			}

			setErrormsg(null != msg && "success".equals(msg) ? "修改成功" : msg);
			if("success".equals(msg) == true) {
				getHttpSession().setAttribute(Constants.SESSION_PAYPASSWORD, EncryptionUtil.encryptPassword(new_content));
			}
		}
		GsonUtil.GsonObject(errormsg);
		return null;
	}

	public String transferAjaxInforSkyGetE68Monery() {
		String resultAmout = "";
		try {
			Users skyCustomer = getCustomerFromSession();
			if (skyCustomer != null) {
				resultAmout = String.valueOf(skyCustomer.getCredit());
			} else {
				resultAmout = "登录失效！请重新登录!";
			}
			GsonUtil.GsonObject(resultAmout);
		} catch (Exception e) {
			e.printStackTrace();
			resultAmout = e.getMessage();
			GsonUtil.GsonObject(resultAmout);
		}
		return null;
	}

	public String transferInforNewPt() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferNewPtIn", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferOutforNewPt() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferNewPtOut", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	/**
	 * PT投注记录
	 * 
	 * @return
	 */
	public String transferAjaxGetTurnoverAllAndPtList() {
		try {
			Users ptUser = getCustomerFromSession();
			if (ptUser == null) {
				GsonUtil.GsonObject("");
				return null;
			}
			// 获取今天时间
			start = new Date();
			SimpleDateFormat dfHH = new SimpleDateFormat("HH");
			Integer hh = Integer.parseInt(dfHH.format(start));
			Date startTime = null;
			Date endTime = null;
			if (hh < 12) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(start);
				cal.add(Calendar.DAY_OF_MONTH, -1); // 减1天
				cal.set(Calendar.HOUR_OF_DAY, 12);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.add(Calendar.HOUR_OF_DAY, -7);
				startTime = cal.getTime();

				cal.add(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 12);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.add(Calendar.HOUR_OF_DAY, -7);
				endTime = cal.getTime();
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(start);
				cal.set(Calendar.HOUR_OF_DAY, 12);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.add(Calendar.HOUR_OF_DAY, -7);
				startTime = cal.getTime();

				cal.add(Calendar.DAY_OF_MONTH, 1); // 加1天
				cal.set(Calendar.HOUR_OF_DAY, 12);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.add(Calendar.HOUR_OF_DAY, -7);
				endTime = cal.getTime();
			}
			List<PtStatistical> listPtStatistical = new ArrayList<PtStatistical>();
			// 获取数据
			String loginString = SkyUtils.getEffectiveBets(ptUser.getId(), startTime, endTime);
			JSONObject jsonObj = JSONObject.fromObject(loginString);
			// 判断是否有投注额 如果大于了1个小时没有投注 自动下线
			if (!jsonObj.containsKey("stat")) {
				return null;
			}
			PtTurnover ptTurnover = new PtTurnover();
			ptTurnover.setBetCredit(0.0);
			ptTurnover.setPayOut(0.0);
			ptTurnover.setAmount(0.0);
			for (Object object : jsonObj.getJSONArray("stat")) {
				List listObject = (List) object;
				if (Integer.parseInt((String) listObject.get(7)) == 0) {
					ptTurnover.setBetCredit(ptTurnover.getBetCredit() + (Double.parseDouble((String) listObject.get(2)) * Double.parseDouble((String) listObject.get(3)) * Double.parseDouble((String) listObject.get(4))) / 100);
				}
				ptTurnover.setPayOut(ptTurnover.getPayOut() + Double.parseDouble((String) listObject.get(6)) / 100);

				PtStatistical ptSt = new PtStatistical();
				ptSt.setUserId(ptUser.getId());

				Calendar c = Calendar.getInstance();
				c.setTime(DateUtil.parseDateForStandard((String) listObject.get(0)));
				c.add(Calendar.HOUR_OF_DAY, 7);
				ptSt.setPlaytime(c.getTime());

				ptSt.setAmount(Double.parseDouble((String) listObject.get(1)) / 100);
				ptSt.setType(Integer.parseInt((String) listObject.get(7)));
				if (ptSt.getType() == 0) {
					ptSt.setMultiplier(Double.parseDouble((String) listObject.get(2)));
					ptSt.setLine(Double.parseDouble((String) listObject.get(3)));
					ptSt.setBet(Double.parseDouble((String) listObject.get(4)));
				} else {
					ptSt.setMultiplier(0.0);
					ptSt.setLine(0.0);
					ptSt.setBet(0.0);
				}
				ptSt.setGameCode(Integer.parseInt((String) listObject.get(5)));
				ptSt.setPayOut(Double.parseDouble((String) listObject.get(6)) / 100);
				ptSt.setCreatetime(new Timestamp(new Date().getTime()));
				ptSt.setLoginname(ptUser.getLoginname());
				listPtStatistical.add(ptSt);
			}

			Collections.sort(listPtStatistical, new Comparator<PtStatistical>() {
				@Override
				public int compare(PtStatistical o1, PtStatistical o2) {
					Date d1 = o1.getPlaytime();
					Date d2 = o2.getPlaytime();
					return d2.compareTo(d1);
				}
			});

			StringBuffer buffer = new StringBuffer();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			for (PtStatistical ptStatistical : listPtStatistical) {
				// <p><span>43:13:90</span><span>钢铁侠2</span><span>23%</span><span>2341</span><span>13456</span></p>
				buffer.append("<p><span>" + formatter.format(ptStatistical.getPlaytime()) + "</span><span title='" + PtGameCode.getText(ptStatistical.getGameCode()) + "'>" + PtGameCode.getText(ptStatistical.getGameCode()) + "</span><span>" + ptStatistical.getPayOut() + "</span><span>" + ((ptStatistical.getBet() * ptStatistical.getLine() * ptStatistical.getMultiplier()) / 100) + "</span><span>" + ptStatistical.getAmount() + "</span></p>");
			}
			ptTurnover.setPtInfoStr(buffer.toString());

			DecimalFormat dfFF = new DecimalFormat("#0.00");
			ptTurnover.setBetCredit(Double.parseDouble(dfFF.format(ptTurnover.getBetCredit())));
			ptTurnover.setAmount(Double.parseDouble(dfFF.format((-1) * (ptTurnover.getBetCredit() - ptTurnover.getPayOut()))));
			GsonUtil.GsonObject(ptTurnover);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject("");
			return null;
		}
	}

	/**
	 * 历史投注记录 仅一个礼拜数据
	 * 
	 * @return
	 */
	public String getPtHistoryTurnoverList() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			List<PtProfit> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getPtHistoryTurnoverList", new Object[] { customer.getLoginname() }, PtProfit.class);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
			StringBuffer sbf = new StringBuffer();
			for (PtProfit ptp : list) {
				ptp.setAmount(-1 * ptp.getAmount());
				sbf.append("<p><span>" + sdf.format(ptp.getStarttime()) + "</span><span>" + ptp.getBetCredit() + "</span><span>" + ptp.getAmount() + "</span></p>");
			}
			GsonUtil.GsonObject(sbf.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrormsg("系统繁忙，请稍候重试！");
		}
		return null;
	}

	/**
	 * pt赢点排名 前10个
	 * 
	 * @return
	 */
	public String getPtWinPointList() {
		try {
			List<PtTotal> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getPtWinPointList", new Object[] { ptby, ptorder }, PtTotal.class);
			StringBuffer sbf = new StringBuffer();
			DecimalFormat dfFF = new DecimalFormat("#0.00");
			for (int i = 0; i < list.size(); i++) {
				PtTotal pt = list.get(i);
				sbf.append("<p><code>" + pt.getLoginname().substring(0, pt.getLoginname().length() - 2) + "***</code><code>" + dfFF.format(pt.getTotalProfit()) + "</code><code>" + dfFF.format(pt.getTotalBet()) + "</code><code>" + dfFF.format(pt.getWinPointAll() * 100) + "%</code></p>");
			}
			GsonUtil.GsonObject(sbf.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrormsg("系统繁忙，请稍候重试！");
		}
		return null;
	}

	public String transferOutforSB() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOutforSB", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferInforSB() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferInforSB", new Object[] { getCustomerLoginname(), remit }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferInforBok() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = transferService.transferInforBok(seqService.generateTransferID(), getCustomerLoginname(), remit);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferOutforBok() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				this.setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}

			msg = transferService.transferOutforBok(seqService.generateTransferID(), getCustomerLoginname(), remit);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");

			try {
				refreshUserInSession();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return INPUT;
		} else {
			setErrormsg("提交失败:" + msg);

		}
		return INPUT;
	}

	public String transferOutForCaiwu() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				setErrormsg("请登录后在进行操作");
				return INPUT;
			}
			if (remit == null) {
				setErrormsg("转账金额不可为空");
				return INPUT;
			}
			if (remit <= 0) {
				setErrormsg("请重新输入转账金额");
				return INPUT;
			}
			if (!(remit.toString().matches("^\\d*[1-9]\\d*$") || remit.toString().endsWith(".0"))) {
				this.setErrormsg("转账金额必须为整数！");
				return INPUT;
			}
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "transferOut", new Object[] { getCustomerLoginname(), remit }, String.class);
			refreshUserInSession();
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		if (msg == null) {
			setErrormsg("转账成功");
		} else {
			setErrormsg("提交失败:" + msg);
		}
		return INPUT;
	}

	public String addConcessions() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null)
			setErrormsg("请您从首页登录");
		else
			try {
				String msg = proposalService.addUserConcession(customer.getLoginname(), customer.getLoginname(), customer.getRole(), Constants.FROM_FRONT, firstCash, tryCredit, "在线支付", remark, getIp());
				if (msg == null) {
					return SUCCESS;
				} else {
					setErrormsg("提交失败:" + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍后尝试");
			}
		return INPUT;
	}

	// 检查用户是否存在
	public String checkUserExsit() {
		PrintWriter pw = null;
		try {
			Boolean b = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkUserIsExit", new Object[] { loginname }, Boolean.class);
			pw = getResponse().getWriter();
			if (null == b) {
				setErrormsg("系统异常");
			} else if (b) {
				pw.write("true");
			} else {
				pw.write("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("系统异常");
		} finally {
			pw.close();
		}
		return null;
	}
	
	
	private String sjType;
	private String sjValue;
	
	
	public String getSjType() {
		return sjType;
	}

	public void setSjType(String sjType) {
		this.sjType = sjType;
	}

	public String getSjValue() {
		return sjValue;
	}

	public void setSjValue(String sjValue) {
		this.sjValue = sjValue;
	}
	
	/**
	 * 数据验证  手机号 邮箱 用户名
	 * @return
	 */
	public String sjyz(){
		try {
			if(!sjType.equals("yhm")){
				sjValue = AESUtil.aesEncrypt(sjValue,AESUtil.KEY);
			}
			if(sjType.equals("yhm")){
				if(StringUtils.isEmpty(sjValue)){
					GsonUtil.GsonObject("用户名不允许为空");	
					return null;
				}
			}
			if(sjType.equals("sjh")){
				if(StringUtils.isEmpty(sjValue)){
					GsonUtil.GsonObject("手机号不允许为空");	
					return null;
				}
			}
			if(sjType.equals("yx")){
				if(StringUtils.isEmpty(sjValue)){
					GsonUtil.GsonObject("邮箱不允许为空");	
					return null;
				}
			}
			String str=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "sjyz", new Object[] { sjType,sjValue }, String.class);
			GsonUtil.GsonObject(str);
		} catch (AxisFault e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String queryDepositReccords() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return "index";
		} else {
			Page page = null;
			try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "selectDepositRecord", new Object[] { customer.getLoginname(), pageIndex, size }, Page.class, Payorder.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}

	public String querywinpoint() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar g = new GregorianCalendar();
		if (start == null) {
			g.add(GregorianCalendar.DAY_OF_MONTH, -1);
			g.set(GregorianCalendar.HOUR_OF_DAY, 0);
			g.set(GregorianCalendar.MINUTE, 0);
			g.set(GregorianCalendar.SECOND, 0);
			starttime = sdf.format(g.getTime());
		} else {
			starttime = sdf.format(start);
		}
		if (end == null) {
			g.add(GregorianCalendar.DAY_OF_MONTH, 1);
			endtime = sdf.format(g.getTime());
		} else {
			endtime = sdf.format(end);
		}
		if (betTotal == null) {
			betTotal = 0;
		} else if (betTotal < 0 || betTotal > 3) {
			setErrormsg("系统繁忙，请稍候重试！");
		}
		if (by == null) {
			by = "amountTotal";
		}
		if (order == null) {
			order = "desc";
		}
		Page page = null;
		try {
			page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "selectWinPoint", new Object[] { starttime, endtime, betTotal, by, order, pageIndex, size }, Page.class, WinPointInfo.class);
			getRequest().setAttribute("page", page);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrormsg("系统繁忙，请稍候重试！");
			return INPUT;
		}
		return INPUT;
	}

	public String queryCashinRecords() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return "index";
		} else {

			Page page = null;
			try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "selectCashinRecords", new Object[] { customer.getLoginname(), pageIndex, size }, Page.class, Proposal.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	public String depositOrderRecord() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");
			return "index";
		}else {
			
			Page page = null;
			 try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
							+ "UserWebService", false), AxisUtil.NAMESPACE, "depositOrderRecord", new Object[] {
					customer.getLoginname(),pageIndex, size}, Page.class,DepositOrder.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	

	


	public String queryWithdrawReccords() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return "index";
		} else {
			Page page = null;
			try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "selectWithdrawReccords", new Object[] { customer.getLoginname(), pageIndex, size }, Page.class, Proposal.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	/**
	 * 查询每天签到
	 * @param loginName
	 */
	public void findSignrecord() {
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
			}
			List<SignRecord> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "findSignrecord", new Object[] {loginname}, SignRecord.class);
			for (SignRecord signRecord : list) {
				signRecord.setTimeStr(DateUtil.fmtyyyy_MM_d(signRecord.getCreatetime()));
			}
			Gson gson = new Gson();
			String str = gson.toJson(list);
			writeJson(str);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void getdepositAmout() {
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
			}
			Date date = new Date();

			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "checkSignRecord", new Object[] {loginname,DateUtil.fmtDateForBetRecods(DateUtil.getDateBegin(date)),DateUtil.fmtDateForBetRecods(DateUtil.getDateEnd(date))}, String.class);
			Double value = Double.valueOf(result);
			GsonUtil.GsonObject(value);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("查询失败");
		}

	}



	public String queryTransferReccords() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return "index";
		} else {
			Page page = null;
			try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "selectTransferReccords", new Object[] { customer.getLoginname(), pageIndex, size }, Page.class, Transfer.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}

	public String queryCouponRecords() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return "index";
		} else {
			Page page = null;
			try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "selectCouponRecords", new Object[] { customer.getLoginname(), pageIndex, size }, Page.class, Proposal.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}

	public String queryConcessionReccords() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return "index";
		} else {
			Page page = null;
			try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "selectConcessionReccords", new Object[] { customer.getLoginname(), pageIndex, size }, Page.class, Proposal.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	/**
	 * 
	* @methods searchAgentBetProfit 
	* @description <p>查询代理下玩家的实时投注记录</p> 
	* @author erick
	* @date 2015年1月2日 下午2:02:46
	* @return 参数说明
	* @return String 返回结果的说明
	 */
	public String searchAgentBetProfit(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");return "index";
		}else {
			Page page = null;
			 try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
							+ "UserWebService", false), AxisUtil.NAMESPACE, "selectBetProfit", new Object[] {
					customer.getLoginname() , starttime , endtime , platform , loginname ,pageIndex, size }, Page.class,PlatformData.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	/**
	 * 查询老虎机周周回馈记录
	 * @return
	 */
	public String queryWeekSentReccords() { 
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");return "index";
		}else {
			Page page = null;
			 try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
							+ "UserWebService", false), AxisUtil.NAMESPACE, "weekSentReccords", new Object[] {
					customer.getLoginname(), pageIndex, size}, Page.class, WeekSent.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}


	
	/**
	 * 处理周周回馈
	 * @return
	 */
	public String optWeekSent(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");return "index";
		}else {
			try {
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
						"optWeekSent", new Object[]{jobPno, proposalFlag, getIp(), platform}, String.class);
				if(result == null){
					GsonUtil.GsonObject("操作成功！");
				}else{
					GsonUtil.GsonObject("操作失败：" + result);
				}
				
			} catch (AxisFault e) {
				e.printStackTrace();
				GsonUtil.GsonObject("系统繁忙，请稍候重试！");
				return null;
			}
		}
		return null;
	}

	/**
	 * 查询pt老虎机负盈利反赠记录
	 * @return
	 */
	public String queryPTLosePromoReccords() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			//setErrormsg("请您从首页登录");return "index";
			GsonUtil.GsonObject("-1");
			return null;
		}else {
			Page page = null;
			 try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
							+ "UserWebService", false), AxisUtil.NAMESPACE, "losePromoReccords", new Object[] {
					customer.getLoginname(),pageIndex, size}, Page.class,LosePromo.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	/**
	 * 处理负盈利反赠
	 * @return
	 */
	public String optLosePromo(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");return "index";
		}else {
			try {
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE,
						"optLosePromo", new Object[]{jobPno, proposalFlag, getIp(), platform}, String.class);
				if(result == null){
					GsonUtil.GsonObject("操作成功！");
				}else{
					GsonUtil.GsonObject("操作失败：" + result);
				}
				
			} catch (AxisFault e) {
				e.printStackTrace();
				GsonUtil.GsonObject("系统繁忙，请稍候重试！");
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 查询本月游戏平台投注额
	 * @return
	 */
	public String queryBetOfPlatform(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");return "index";
		}else {
			try {
				List<Bet> betslist = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
						+ "UserWebService", false), AxisUtil.NAMESPACE, "queryBets", new Object[] {customer.getLoginname(), "month"}, Bet.class);
				if(betslist==null || betslist.isEmpty()){
					throw new Exception();
				}
				getRequest().setAttribute("bets", betslist);
			} catch (AxisFault e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}




	/**
	 * 查询本周游戏平台投注额
	 * @return
	 */
	public String queryBetOfPlatformWeek(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");return "index";
		}else {
			try {
				List<Bet> betslist = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
						+ "UserWebService", false), AxisUtil.NAMESPACE, "queryBets", new Object[] {customer.getLoginname(), "week"}, Bet.class);
				if(betslist==null || betslist.isEmpty()){
					throw new Exception();
				}
				getRequest().setAttribute("bets", betslist);
			} catch (AxisFault e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	/**
	 * 处理升级
	 */
	public void checkUpgrade(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
		}
		if (customer == null){
			//setErrormsg("请您从首页登录");return "index";
			GsonUtil.GsonObject("-1");
		}else {
			try {
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"checkUpgrade", new Object[]{customer.getLoginname(), type}, String.class);
				if(result == null){
					GsonUtil.GsonObject("升级成功");
				}else{
					GsonUtil.GsonObject(result);
				}
				
			} catch (AxisFault e) {
				e.printStackTrace();
				GsonUtil.GsonObject("系统繁忙，请稍候重试！");
			}
		}
	}
	
	//查询PT大爆炸礼金
	public String queryPTBigBang(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");return "index";
		}else {
			try {
				/*PTBigBang ptBigBang = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "ptBigBangBonus", new Object[] {customer.getLoginname()}, PTBigBang.class);
				getRequest().setAttribute("ptBigBang", ptBigBang);*/
				
				List<PTBigBang> bonusList = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "ptBigBangBonus", new Object[] {customer.getLoginname()}, PTBigBang.class);
				getRequest().setAttribute("bonusList", bonusList);
			} catch (AxisFault e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	/**
	 * 领取PT大爆炸礼金
	 */
	public void getPTBigBangBonus(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			GsonUtil.GsonObject("系统繁忙，请稍候重试！");
		}
		if (customer == null){
			GsonUtil.GsonObject("系统繁忙，请稍候重试！");
		}else {
			try {
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"getPTBigBangBonus", new Object[]{ptBigBangId, getIp()}, String.class);
				if(result == null){
					GsonUtil.GsonObject("操作成功！");
				}else{
					GsonUtil.GsonObject("操作失败：" + result);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				GsonUtil.GsonObject("系统繁忙，请稍候重试！");
			}
		}
	}
	
	/**
	 * 判断是否存在待领取的礼品
	 */
	public void isExistGift(){
		try {
			Users customer = getCustomerFromSession();
			if (customer == null){
				return;
			}
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"isExistGift", new Object[]{customer.getLoginname()}, String.class);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 申请礼品
	 */
	public void applyGift(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			Utils.write("系统繁忙，请稍候重试！");
		}
		if (customer == null){
			Utils.write("请重新登录网站后再申请礼品！");
		}else {
			try {
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
						"applyGift", new Object[]{customer.getLoginname(), giftID, address, addressee, cellphoneNo}, String.class);
				if(result == null){
					Utils.write("申请已提交");
				}else{
					Utils.write("申请失败：" + result);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				Utils.write("系统繁忙，请稍候重试！");
			}
		}
	}
	
	public String getAccountCity() {
		return accountCity;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public String getAliasName() {
		return aliasName;
	}

	public Double getAmount() {
		return amount;
	}

	public Double getBalance() {
		return balance;
	}

	public String getBank() {
		return bank;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public String getConfirm_password() {
		return confirm_password;
	}

	public CustomerService getCs() {
		return cs;
	}

	public String getEmail() {
		return email;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public Double getFirstCash() {
		return firstCash;
	}

	public String getLoginname() {
		return loginname;
	}

	public String getNew_password() {
		return new_password;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public String getRemark() {
		return remark;
	}

	public SeqService getSeqService() {
		return seqService;
	}

	public TransferService getTransferService() {
		return transferService;
	}

	public Double getTryCredit() {
		return tryCredit;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getReferWebsite() {
		return referWebsite;
	}

	public void setReferWebsite(String referWebsite) {
		this.referWebsite = referWebsite;
	}

	// 得到用户余额
	public String queryRemoteCredit() {
		String msg = "";
		// Users user = getCustomerFromSession();
		if (loginname == null) {
			msg = "您还未登录";
		} else {
			try {
				msg = NumericUtil.formatDouble(RemoteCaller.queryCredit(loginname));
			} catch (Exception e) {
				e.printStackTrace();
				msg = "系统繁忙，请稍后尝试";
			}
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	// 提款记录
	public String queryCreditoutlogs() {
		if (checkLogin()) {
			String msg = "";
			// String loginname = getCustomerLoginname();
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				if (proposalType != null)
					dc.add(Restrictions.eq("type", proposalType));
				else
					dc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode()));
				if (proposalFlag != null)
					dc = dc.add(Restrictions.eq("flag", proposalFlag));
				if (start != null)
					dc = dc.add(Restrictions.ge("createTime", start));
				if (end != null)
					dc = dc.add(Restrictions.lt("createTime", end));
				dc = dc.add(Restrictions.eq("loginname", getCustomerLoginname()));
				dc = dc.addOrder(Order.desc("createTime"));

				Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, new Integer(10));
				getRequest().setAttribute("pagecashoutlogs", page);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("返回消息:" + e.getMessage());
			}
		}
		return INPUT;
	}

	// 取消提款
	public String cancleProposal() {
		if (checkLogin()) {
			try {
				String msg = proposalService.clientCancle(jobPno, getCustomerLoginname(), getIp(), "自注取消");
				if (msg == null)
					setErrormsg("返回消息:取消成功");
				else
					setErrormsg("返回消息:" + msg);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("返回消息:" + e.getMessage());
			}
		}
		return SUCCESS;
	}

	public String querySubUsers() {
		try {
			if (StringUtils.isNotEmpty(getCustomerLoginname())) {

				Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "querySubUsers", new Object[] { getCustomerLoginname(), pageIndex, size }, Page.class, Users.class);
				getRequest().setAttribute("page", page);
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
		}
		return INPUT;
	}

	public String searchagprofit() {
		try {
			if (StringUtils.isNotEmpty(getCustomerLoginname())) {

				Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "searchagprofit", new Object[] { getCustomerLoginname(), loginname, platform, starttime, endtime, pageIndex, size }, Page.class, AgProfit.class);

				getRequest().setAttribute("page", page);
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
		}
		return INPUT;
	}

	public String searchsubuserProposal() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				this.errormsg = "请登录后在进行操作";
				return "index";
			}
			if (proposalType == null) {
				proposalType = 502;
			}
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "searchsubuserProposal", new Object[] { this.starttime, this.endtime, getCustomerLoginname(), proposalType, loginname, pageIndex, size }, Page.class, proposalType == 1000 ? Payorder.class : Proposal.class);
			getRequest().setAttribute("page", page);
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
	}
	
	public String queryAgentSubUserInfo() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject("请登录后在进行操作");
				return null;
			}

			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "queryInitSubUsers", new Object[] { getCustomerLoginname(), DateUtil.fmtDateForBetRecods(start), DateUtil.fmtDateForBetRecods(end), pageIndex, size }, Page.class, Users.class);

			getRequest().setAttribute("page", page);

			getRequest().setAttribute("start", start != null ? DateUtil.fmtDateForBetRecods(start) : "");
			getRequest().setAttribute("end", end != null ? DateUtil.fmtDateForBetRecods(end) : "");

			List<Integer> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getAgentSubUserInfo", new Object[] { getCustomerLoginname(), DateUtil.fmtDateForBetRecods(start), DateUtil.fmtDateForBetRecods(end) }, Integer.class);
			if (null != list && !list.isEmpty()) {
				if (null != list.get(1)) {
					getRequest().setAttribute("activeUsers", list.get(1));
				}
				if (null != list.get(0)) {
					getRequest().setAttribute("subUsers", list.get(0));
				}
			}
		} catch (Exception e) {
			GsonUtil.GsonObject(e.getMessage().toString());
			return null;
		}
		return INPUT;
	}

	public String queryCommissionrecords() {
		try {
			Users user = (Users) getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject("请登录后在进行操作");
				return null;
			}
			
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "queryCommissionrecords", new Object[] { getCustomerLoginname(), year, month, pageIndex, size }, Page.class, Commissionrecords.class);

			Commissions cmm = (Commissions) AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getCommissions", new Object[] { getCustomerLoginname(), year, month }, Commissions.class);

			getRequest().setAttribute("page", page);
			getRequest().setAttribute("cmm", cmm);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}
	
	//查询老虎机日结佣金记录 
		public String searchPtCommissions(){
			Users customer = null;
			try {
				customer = getCustomerFromSession();
			} catch (Exception e) {
				this.setErrormsg(e.getMessage().toString());
				return Action.INPUT;
			}
			if (customer == null){
				setErrormsg("请您从首页登录");
				return "index";
			}else {
				Page page = null;
				 try {
					page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
								+ "UserWebService", false), AxisUtil.NAMESPACE, "searchPtCommissions", new Object[] {
						customer.getLoginname() , starttime , endtime  ,pageIndex, size }, Page.class,PtCommissions.class);
					getRequest().setAttribute("page", page);
				} catch (AxisFault e) {
					e.printStackTrace();
					setErrormsg("系统繁忙，请稍候重试！");
					return INPUT;
				}
			}
			return INPUT;
		}

	/**
	 * 接受从后台来的会话刷新请求，保持额度更改及时显示
	 * 
	 * @return
	 */
	public String forceToRefreshSession() {
		String msg = Constants.FLAG_FALSE + "";
		if (StringUtils.isNotEmpty(loginname)) {
			Iterator<HttpSession> it = MySessionContext.iterator();
			while (it.hasNext()) {
				HttpSession session = it.next();
				Users customer = (Users) session.getAttribute(Constants.SESSION_CUSTOMERID);
				if (customer != null && StringUtils.equals(loginname, customer.getLoginname())) {
					// 刷新会话
					if (StringUtils.isNotEmpty(loginname)) {

						try {
							Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { loginname }, Users.class);
							session.setAttribute(Constants.SESSION_CUSTOMERID, user);
							msg = Constants.FLAG_TRUE + "";
						} catch (AxisFault e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}
		}
		try {
			getResponse().getWriter().println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询玩家投诉建议
	 * @throws IOException 
	 */
	public void qrySuggestion() throws IOException{
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			return;
		}
		String result;
		try {
			result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getSuggetionsByUser", new Object[] {pageIndex, size, user.getLoginname()}, String.class);
		} catch (AxisFault e) {
			return;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(result);
	}
	
	/**
	 * 查看投诉建议办理进度
	 * @throws IOException 
	 */
	public void viewSugguestionProgress() throws IOException{
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			return;
		}
		String result;
		try {
			result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), 
					AxisUtil.NAMESPACE, "getSuggetionByID", new Object[] {sid, user.getLoginname()}, String.class);
		} catch (AxisFault e) {
			return;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(result);
	}
	
	/**
	 * 提交投诉建议
	 * @throws IOException 
	 */
	public void submitSuggestion() throws IOException{
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		// 检测用户是否登录
		if (user == null) {
			return;
		}
		String result;
		try {
			result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), 
					AxisUtil.NAMESPACE, "createOAWorkFlow", new Object[] {user.getLoginname(), type, content}, String.class);
		} catch (AxisFault e) {
			return;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write(result);
	}

	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}

	public void setCs(CustomerService cs) {
		this.cs = cs;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public void setFirstCash(Double firstCash) {
		this.firstCash = firstCash;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSeqService(SeqService seqService) {
		this.seqService = seqService;
	}

	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}

	public void setTryCredit(Double tryCredit) {
		this.tryCredit = tryCredit;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getProposalFlag() {
		return proposalFlag;
	}

	public void setProposalFlag(Integer proposalFlag) {
		this.proposalFlag = proposalFlag;
	}

	public Integer getProposalType() {
		return proposalType;
	}

	public void setProposalType(Integer proposalType) {
		this.proposalType = proposalType;
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

	public String getJobPno() {
		return jobPno;
	}

	public void setJobPno(String jobPno) {
		this.jobPno = jobPno;
	}

	public void setNotifyNote(String notifyNote) {
		this.notifyNote = notifyNote;
	}

	public void setNotifyPhone(String notifyPhone) {
		this.notifyPhone = notifyPhone;
	}

	public String getNotifyNote() {
		return notifyNote;
	}

	public String getNotifyPhone() {
		return notifyPhone;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	private void refreshAccountinfoInSession() throws Exception {
		Users customer = getCustomerFromSession();
		getHttpSession().setAttribute("accountinfo", cs.get(Accountinfo.class, customer.getLoginname()));
	}
	
	
	private String itemNo;
	
	private String typeNo;
	
	
	
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	
	/**
	 * 查询系统配置表的内容
	 * @return
	 */
	public void checkConfigSystem() {
		try {
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "checkSystemConfig", new Object[]{typeNo,itemNo,"否"}, String.class);
			if(!StringUtil.isEmpty(info)){
			GsonUtil.GsonObject(info);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MemberAction ma =new MemberAction();
		ma.setType("aaa");
		 ma.querybetRank();
	}
	
	/**
	 * 十月老虎机流水比赛 ttg gpi pttiger
	 * @return
	 */
	public String querybetRank() {
			Page page = null;
			Date date = new Date();
			long date1 =date.getTime();
            long date2=DateUtil.parseDateForStandard("2015-10-12 00:00:00").getTime();
            long date3=DateUtil.parseDateForStandard("2015-10-19 00:00:00").getTime();
            long date4=DateUtil.parseDateForStandard("2015-10-26 00:00:00").getTime();
            type="PT/TTG/GPI";
			/*if(type.equals("ttg")){
				starttime="2015-10-05 00:00:00";
				endtime="2015-10-11 23:59:59";
			}*/
			if(date1>date2){
				starttime="2015-10-12 00:00:00";
				endtime="2015-10-18 23:59:59";
			}
			if(date1>date3){
				starttime="2015-10-19 00:00:00";
				endtime="2015-10-25 23:59:59";
			}
			if(date1>date4){
				starttime="2015-10-26 00:00:00";
				endtime="2015-11-01 23:59:59";
			}
			if(null==pageIndex){
				pageIndex=1;
			}
			if(null==size){
				size=10;
			}
			try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "querybetRank", new Object[] {starttime,endtime,type,pageIndex, size }, Page.class, BetRankModel.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		return INPUT;
	}
	
	/**
	 * 老虎机体验金周比赛排名查询
	 * @return
	 */
	public String querySMWeekly(){
		try {
			//String platform = getRequest().getParameter("platform");
			Integer week_ = Integer.valueOf(getRequest().getParameter("week_").toString());
			String startTime=null,endTime=null;
			startTime = MatchDateUtil.getWeekStart(new Date());
			endTime = MatchDateUtil.getWeekEnd(new Date());
			if (week_ < 0){
				for (int i = 0; i > week_; i--) {
					startTime = MatchDateUtil.getBeforeWeekStart(MatchDateUtil.parseDate(startTime));
					endTime = MatchDateUtil.getWeekEnd(MatchDateUtil.parseDate(startTime));
				}
			} else if (week_ > 0){
				for (int i = 0; i < week_; i++) {
					startTime = MatchDateUtil.getNextWeekStart(MatchDateUtil.parseDate(endTime));
					endTime = MatchDateUtil.getWeekEnd(MatchDateUtil.parseDate(startTime));
				}
			}
			if(null==pageIndex){
				pageIndex=1;
			}
			if(null==size){
				size=10;
			}
			startTime+=" 00:00:00";
			endTime+=" 23:59:59";
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
					+ "UserWebService", false), AxisUtil.NAMESPACE, "querySlotMatchWeekly", new Object[]{startTime,endTime,pageIndex,size},
					Page.class,SlotsMatchWeekly.class);
			if (page == null){ //webservice返回为null时表示本周比赛关闭
				GsonUtil.GsonObject(platform.toUpperCase()+"老虎机本周竞赛已关闭..");
				return null;
			}
			getRequest().setAttribute("week_", week_);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
			return "index";
		}
		return INPUT;
	}
	
	/**
	 * 查询NT游戏记录
	 * @return
	 */
	public String queryNTGameRecord(){
		String res=ERROR;
		try {
			String sdate = this.getRequest().getParameter("s_date");
			String edate = this.getRequest().getParameter("e_date");
			if (StringUtils.isEmpty(sdate) && StringUtils.isEmpty(edate)){ //为空表示用户第一次进入该页面
				return INPUT;
			}
			
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryNTGameRecord", new Object[]{sdate,edate,pageIndex,size},
					Page.class,SlotsMatchWeekly.class);
		} catch (Exception e) {
			log.error("queryNTGameRecord error:",e);
		}
		return res;
	}
	
	/**
	 * 签到操作
	 */
	public void doSignRecord() {
		try {
			SynchronizedUtil.getInstance().dosign();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkSignRecord() {
		try {
			SynchronizedUtil.getInstance().checkSignRecord();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void generateVerificationCode() {

		Integer wordLength = Integer.valueOf(4);
		String sRand = "";
		Random random = new Random(System.currentTimeMillis());

		for (int i = 0; i < wordLength.intValue(); i++) {

			String rand = String.valueOf(random.nextInt(10));
			sRand = new StringBuilder(sRand).append(rand).toString();
		}

		getHttpSession().setAttribute(Constants.SESSION_RANDID, sRand);

		GsonUtil.GsonObject(sRand);
	}

	
	/**
	 * 查询签到总金额
	 */
	public void querySignAmount() {
		try {
			Users customer = null;
			customer = getCustomerFromSession();
		if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
			GsonUtil.GsonObject("请登录后再操作");
			return;
		   }
			Double info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "querySignAmount", new Object[]{customer.getLoginname()}, Double.class);
				GsonUtil.GsonObject(info);
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 刷新玩家余额
	 * @throws Exception
	 */
	public void refreshUserBalance() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=utf-8");
		refreshUserInSession();
		Users customer = getCustomerFromSession();
		if(null==customer){
			response.getWriter().write("");
		}else{
			response.getWriter().write(customer.getCredit().toString());
		}
	}
	
	private String signType;
	
	private String signRemit;
	
	public String getSignRemit() {
		return signRemit;
	}

	public void setSignRemit(String signRemit) {
		this.signRemit = signRemit;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}


	
	public Integer getBankinfoid() {
		return bankinfoid;
	}

	public void setBankinfoid(Integer bankinfoid) {
		this.bankinfoid = bankinfoid;
	}

	public String getSaveway() {
		return saveway;
	}

	public void setSaveway(String saveway) {
		this.saveway = saveway;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCashintime() {
		return cashintime;
	}

	public void setCashintime(String cashintime) {
		this.cashintime = cashintime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getTotalpageno() {
		return totalpageno;
	}

	public void setTotalpageno(int totalpageno) {
		this.totalpageno = totalpageno;
	}

	public int getTotalrowsno() {
		return totalrowsno;
	}

	public void setTotalrowsno(int totalrowsno) {
		this.totalrowsno = totalrowsno;
	}

	public int getMaxpageno() {
		return maxpageno;
	}

	public void setMaxpageno(int maxpageno) {
		this.maxpageno = maxpageno;
	}

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public List<Proposal> getList() {
		return list;
	}

	public void setList(List<Proposal> list) {
		this.list = list;
	}

	public Double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMsflag() {
		return msflag;
	}

	public void setMsflag(String msflag) {
		this.msflag = msflag;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		MemberAction.log = log;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public Double getCouponRemit() {
		return couponRemit;
	}

	public void setCouponRemit(Double couponRemit) {
		this.couponRemit = couponRemit;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Integer getBetTotal() {
		return betTotal;
	}

	public void setBetTotal(Integer betTotal) {
		this.betTotal = betTotal;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getPtby() {
		return ptby;
	}

	public void setPtby(String ptby) {
		this.ptby = ptby;
	}

	public String getPtorder() {
		return ptorder;
	}

	public void setPtorder(String ptorder) {
		this.ptorder = ptorder;
	}

/*	public GuestBookService getGuestBookService() {
		return guestBookService;
	}

	public void setGuestBookService(GuestBookService guestBookService) {
		this.guestBookService = guestBookService;
	}*/

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public Pattern getP_email() {
		return p_email;
	}

	public void setP_email(Pattern p_email) {
		this.p_email = p_email;
	}

	public String getMicrochannel() {
		return microchannel;
	}

	public void setMicrochannel(String microchannel) {
		this.microchannel = microchannel;
	}

	public String getGameCode() {
		return gameCode;
	}

	public String getYouHuiType() {
		return youHuiType;
	}

	public void setYouHuiType(String youHuiType) {
		this.youHuiType = youHuiType;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getDepositCode() {
		return depositCode;
	}

	public void setDepositCode(String depositCode) {
		this.depositCode = depositCode;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getIoBB() {
		return ioBB;
	}

	public void setIoBB(String ioBB) {
		this.ioBB = ioBB;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPtBigBangId() {
		return ptBigBangId;
	}

	public void setPtBigBangId(Integer ptBigBangId) {
		this.ptBigBangId = ptBigBangId;
	}

	public String getTkType() {
		return tkType;
	}

	public void setTkType(String tkType) {
		this.tkType = tkType;
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

	public Integer getGiftID() {
		return giftID;
	}

	public void setGiftID(Integer giftID) {
		this.giftID = giftID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public String getCellphoneNo() {
		return cellphoneNo;
	}

	public void setCellphoneNo(String cellphoneNo) {
		this.cellphoneNo = cellphoneNo;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	/**
	 * 好友推荐奖励奖金
	 * @return
	 */
	public String transferInforFriend() {
		return SynchronizedUtil.getInstance().transferInforFriend(signType, Double.parseDouble(signRemit));
	}
	
	/**
	 * 查询推荐好友奖金余额
	 * @return
	 */
	public void queryfriendBonue(){
		try {
			Users customer = null;
			customer = getCustomerFromSession();
		if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
			GsonUtil.GsonObject("请登录后再操作");
		   }
		
			String friendurl = customer.getId()+"";
			String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryFriendbonus", new Object[]{customer.getLoginname()}, String.class);
			if(!StringUtil.isEmpty(info)){
			GsonUtil.GsonObject(info+"#"+friendurl);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String friendtype;
	
	public String getFriendtype() {
		return friendtype;
	}

	public void setFriendtype(String friendtype) {
		this.friendtype = friendtype;
	}

	/**
	 * 好友推荐查询
	 * @return
	 */
	public String queryfriendRecord() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");
			return "index";
		}else {
			
			Page page = null;
			 try { 
				if(null==friendtype||friendtype.equals("0")){
					page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE, "friendintroduce", new Object[] {
					customer.getLoginname(),pageIndex, size}, Page.class,Friendintroduce.class);	
				}else{
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE, "friendbonusrecord", new Object[] {
					customer.getLoginname(),friendtype,pageIndex, size}, Page.class,Friendbonusrecord.class);
				}
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	/**
	 * 查询积分总余额
	 */
	public void queryPoints() {
		try {
			Users customer = null;
			customer = getCustomerFromSession();
		if(null==customer||StringUtil.isEmpty(customer.getLoginname())){
			GsonUtil.GsonObject("请登录后再操作");
			return;
		   }
		TotalPoint info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "queryPoints", new Object[]{customer.getLoginname()}, TotalPoint.class);
			if(null!=info){
				Double mul=500.0;//默认新会元
				if(customer.getLevel()==1){
					mul=400.0;	
				}else if(customer.getLevel()==2){
					mul=325.0;	
				}else if(customer.getLevel()==3){
					mul=280.0;	
				}else if(customer.getLevel()==4){
					mul=245.0;	
				}else if(customer.getLevel()==5){
					mul=220.0;	
				}else if(customer.getLevel()==6){
					mul=100.0;	
				}
			int totalmoney=(int) (info.getTotalpoints()/mul);
			GsonUtil.GsonObject(info.getTotalpoints()+"#"+info.getOldtotalpoints()+"#"+totalmoney);
			}else{
				GsonUtil.GsonObject(0+"#"+0+"#"+0);	
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 积分兑换成奖金
	 * @return
	 */
	public String transferInforPoint() {
		if(!(signRemit).matches("^\\d*[1-9]\\d*$")){ 
			GsonUtil.GsonObject("转账金额必须为整数！");
		}
		return SynchronizedUtil.getInstance().transferInforPoint(Double.parseDouble(signRemit));
	}
	
	
	
	/**
	 * 积分查询
	 * @return
	 */
	public String querypointRecord() {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			return Action.INPUT;
		}
		if (customer == null){
			setErrormsg("请您从首页登录");
			return "index";
		}else {
			
			Page page = null;
			 try {
				page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE, "querypointRecord", new Object[] {
					customer.getLoginname(),pageIndex, size}, Page.class,DetailPoint.class);
				getRequest().setAttribute("page", page);
			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrormsg("系统繁忙，请稍候重试！");
				return INPUT;
			}
		}
		return INPUT;
	}
	
	
	//红包优惠
	public String transferInforRedCoupon() {
		SynchronizedUtil.getInstance().transferInforRedCoupon(couponType, couponCode);
		return null;
	}
	
	/**
	 * 守护女神报名
	 */
	public void goddessApply(){
		
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("请先登录");
				return;
			}
			if(StringUtils.isBlank(this.goddess)){
				GsonUtil.GsonObject("未接收到守护女神名称");
				return;
			}
			
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "doGoddessApply", new Object[] {
				customer.getLoginname(),this.goddess}, String.class);
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("网络繁忙");
		}
	}
	
	/**
	 * 守护女神报名
	 */
	public void getGoddessApply(){
		
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("请先登录");
				return;
			}
			
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getGoddessApply", new Object[] {
				customer.getLoginname()}, String.class);
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("网络繁忙");
		}
	}
	
	/**
	 * 查看鲜花排行榜
	 */
	public String queryFlowerRanking() {

			
		 try {
			 
			Users customer = null;
			try {
				customer = getCustomerFromSession();
			} catch (Exception e) {
				this.setErrormsg(e.getMessage().toString());
				return Action.INPUT;
			}
			if(customer != null){
				List<Goddessrecord> single = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getRecordByLoginname", new Object[] {
				 		customer.getLoginname()}, Goddessrecord.class);//System.out.println(single.get(0).getGoddessname());
				getRequest().setAttribute("single", single);
			}
			
			List<Goddessrecord> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getGoddesses", new Object[] {}, Goddessrecord.class);
			
			Iterator<Goddessrecord> it = list.iterator();
			while(it.hasNext()){
				Goddessrecord g = it.next();
				if("白石茉莉奈".equals(g.getGoddessname())){
					getRequest().setAttribute("baishi", g.getPeonum());
				} else if("冲田杏梨".equals(g.getGoddessname())){
					getRequest().setAttribute("chongtian", g.getPeonum());
				} else if("明日花綺羅".equals(g.getGoddessname())){
					getRequest().setAttribute("mingri", g.getPeonum());
				} else if("神咲诗织".equals(g.getGoddessname())){
					getRequest().setAttribute("shenxiao", g.getPeonum());
				} else if("柴小圣".equals(g.getGoddessname())){
					getRequest().setAttribute("chaixiao", g.getPeonum());
				}
			}
			
				
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getFlowerAllRecord", new Object[] {
						pageIndex, size}, Page.class,Goddessrecord.class);
			getRequest().setAttribute("page", page);
			 
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setErrormsg("系统繁忙，请稍候重试！");
			return INPUT;
		}
		return INPUT;
	}
	
	/**
	 * 单身狗查看
	 */
	public String queryNineDaysBets(){
		
		try {
			
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getNineDaysBets", new Object[] {
					pageIndex, size}, Page.class,SingleParty.class);
			getRequest().setAttribute("page", page);
			
			Users customer = getCustomerFromSession();
			if(customer != null){
				List<SingleParty> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "getSelfSingleParty", new Object[] {
			 		customer.getLoginname()}, SingleParty.class);
				getRequest().setAttribute("list", list);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("系统繁忙，请稍候重试！");
			return INPUT;
		}
		return INPUT;
	}
	
	/**
	 * 获取游戏状态
	 */
	public void queryGameStatus(){
		
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("");
				return;
			}
			
			GameStatus gameStatus = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getGameStatus", new Object[] {
				customer.getLoginname()}, GameStatus.class);
			if(gameStatus != null){
				GsonUtil.GsonObject(gameStatus);
			} else {
				GsonUtil.GsonObject("");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("");
		}
	}
	
	/**
	 * 更新游戏状态
	 */
	public void saveOrUpdateGameStatus(){
		
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("请先登录");
				return;
			}
			
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "saveOrUpdateGameStatus", new Object[] {
				customer.getLoginname(), this.gameList}, String.class);
			GsonUtil.GsonObject(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("网络繁忙");
		}
	}
	
	//催账 支付宝订单号校验
	public void checkthirdOrder(){
		try {
			Users customer = getCustomerFromSession();
			if (customer == null){
				GsonUtil.GsonObject("请您从首页登录！");
				return;
			}
			String tempDepositDate = DateUtil.fmtyyyy_MM_d(depositDate);
			if(StringUtils.isNotEmpty(tempDepositDate) && StringUtils.isNotEmpty(thirdOrder)){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
						"checkthirdOrder", new Object[]{tempDepositDate,thirdOrder}, String.class);
				GsonUtil.GsonObject(result);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}
	//催账提交
	public String submitUrgeOrder(){
		Users customer = null;
		StringBuffer errMsg = new StringBuffer(); 
		try {
			customer = getCustomerFromSession();
			
			if( customer == null){
				setErrormsg("请登录后，在进行操作");
				return "index";
			}
			
			if ("".equals(type)) {
				setErrormsg("请选择存款类型!");
				return INPUT;
			}
			
			String resultMsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkUrgeOrderCount", new Object[]{customer.getLoginname()}, String.class);
			if(!"".equals(resultMsg)){
				setErrormsg(resultMsg);
				return INPUT;
			}
			
			String dDate = (depositDate==null ? null : DateUtil.fmtyyyy_MM_d(depositDate));
			if(("2".equals(type) || "3".equals(type)) && StringUtils.isNotEmpty(dDate) && StringUtils.isNotEmpty(thirdOrder)){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
						"checkthirdOrder", new Object[]{dDate,thirdOrder}, String.class);
				if(!"".equals(result)){
					setErrormsg(result);
					return INPUT;
				}
			}
		} catch (Exception e) {
			setErrormsg("系统繁忙，请稍候重试！");
			return INPUT;
		}
		loginname = customer.getLoginname();
		
		if(!("5".equals(type) || "9".equals(type)) && StringUtil.isEmpty(accountName)){
			errMsg.append("存款人姓名不能为空！\\r\\n");
		}
		if(depositTime == null){
			errMsg.append("存款时间不能为空！\\r\\n");
		}
		if(amount == null){
			errMsg.append("金额不能为空！\\r\\n");
		}else{
			String s = "([1-9]+[0-9]*|0)(\\.[\\d]+)?";
			String am = amount+"";
			if(!am.matches(s)){
				errMsg.append("金额不能为非数字！\\r\\n");
			}
		}
		
		if ("2".equals(type)) {
			if(StringUtil.isEmpty(thirdOrder)){
				errMsg.append("存款类型为【支付宝扫描】时支付宝单号称不能为空！\\r\\n");
			}
		}
		if ("3".equals(type)) {
			if(StringUtil.isEmpty(nickname)){
				errMsg.append("存款类型为【支付宝附言】支付宝单号和支付宝昵称不能为空！\\r\\n");
			}
		}
		
		String imagepath = "";
		if (null == pictureFileName || pictureFileName.equals("")) {
			if("6".equals(type)){
				errMsg.append("请先上传图片!\\r\\n");
			}
		}else{
			List<String> imgList = new ArrayList<String>();
			imgList.add("image/jpeg");
			imgList.add("image/pjpeg");
			imgList.add("image/png");
			imgList.add("image/x-png");
			imgList.add("image/bmp");
			imgList.add("image/gif");
			imgList.add("image/tiff");
			pictureContentType = pictureContentType.toLowerCase();
			if (!imgList.contains(pictureContentType)) {
				errMsg.append("图片格式错误！\\r\\n");
			}else{
				try {
					imagepath = "/e68/"+loginname+"_"+DateUtil.fmtyyyyMMddHHmmss(new Date())+pictureFileName;
					FTPUtil.uploadImgToRemote(imagepath, picture);
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("系统繁忙，请稍候重试！\\r\\n");
					return INPUT;
				}
			}
		}
	
		if ("9".equals(type)) {
			if(cardtype==null){
				errMsg.append("存款类型为【点卡支付】时点卡类型不能为空！\\r\\n");
			}
			if(StringUtil.isEmpty(cardno)){
				errMsg.append("存款类型为【点卡支付】时点卡卡号不能为空！\\r\\n");
			}
		}
		
		if(errMsg.length()>0){
			setErrormsg(errMsg.toString());
			return INPUT;
		}
		
		UrgeOrder urgeOrder = new UrgeOrder();
		urgeOrder.setLoginname(loginname);
		urgeOrder.setAccountName(accountName);
		urgeOrder.setThirdOrder(thirdOrder);
		urgeOrder.setNickname(nickname);
		urgeOrder.setAmount(amount);
		String tempDepositTime = DateUtil.fmtyyyy_MM_d(depositDate)+" "+depositTime;
		urgeOrder.setTempDepositTime(tempDepositTime);
		urgeOrder.setRemark(remark);
		urgeOrder.setStatus(0);//
		urgeOrder.setType(type);
		urgeOrder.setPicture(imagepath);
		urgeOrder.setCardtype(cardtype);
		urgeOrder.setCardno(cardno);
		try {
			String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"submitUrgeOrder", new Object[]{urgeOrder}, String.class);
			setErrormsg(result+"\\r\\n");
		} catch (Exception e) {
			log.error(e.getMessage());
			setErrormsg("系统繁忙，请稍候重试！\\r\\n");
		}
		return INPUT;
	}
	//催账信息查询
	public String queryUrgeOrderPage(){
		Users customer = null;
		try {
			customer = getCustomerFromSession();
			if (customer == null){
				setErrormsg("请您从首页登录");
				return "index";
			}else {
				if(null==pageIndex){
					pageIndex=1;
				}
				if(null==size){
					size=5;
				}
				loginname = customer.getLoginname();
				Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2
						+ "UserWebService", false), AxisUtil.NAMESPACE, "queryUrgeOrderPage", new Object[] {
				loginname,pageIndex, size}, Page.class,UrgeOrder.class);
				getRequest().setAttribute("page", page);
			}
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
		}
		return INPUT;
	}
	
	public String queryGiftData() {
		
		Users customer = null;
		
		try {
			
			customer = getCustomerFromSession();
			
			if (null == customer) {
				customer = new Users();
			}
			
			if (null == pageIndex) {
				pageIndex = 1;
			}
			
			if (null == size) {
				size = 10;
			}
			
			org.apache.axis2.rpc.client.RPCServiceClient client = AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false);
			
			Page page = AxisUtil.getPageInList(client, AxisUtil.NAMESPACE, "queryGiftData", new Object[] { customer.getLoginname(), pageIndex, size }, Page.class, QueryDataEuroVO.class);
			
			getRequest().setAttribute("page", page);
			
		} catch (Exception e) {
			
			this.setErrormsg(e.getMessage());
		}
		
		return INPUT;
	}
	
	/**
	 * @author ck 1-9-2016 陈奕迅演唱会活动 判断是否报名
	 */
	public void isExistConcertRecord() {
		try {
			String result = "";
			Integer type = 5;   //活动期间

			Users customer = getCustomerFromSession();
			if (customer == null) {
				writeJson("请登入后在报名!!!");
				return;
			}
			 Date date=new Date();
//			Date date = DateUtil.fmtStandard("2016-11-8 12:22:00"); // 模拟当前时间只用于测试
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new DateUtil().fmtyyyy_MM_d("2018-04-02"));
			Date start = calendar.getTime();
			calendar.set(calendar.DATE, 8);
			calendar.set(calendar.MINUTE, 59);
			calendar.set(calendar.SECOND, 59);
			calendar.set(calendar.HOUR, 23);
			Date end = calendar.getTime();

			if (date.getTime() < start.getTime())
				writeJson("活动还没开始");
			else if (end.getTime() < date.getTime())
				writeJson("活动已经结束");
			else {

				result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false),AxisUtil.NAMESPACE,"isExistConcertRecord",new Object[] { customer.getLoginname(),
								DateUtil.formatDateForStandard(date),type },String.class);

				if (StringUtil.isEmpty(result))
					log.info(customer.getLoginname() + ":用户已成功报名抢票活动 !!! "
							+ date);

			}

			writeJson(result);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * @author ck 
	 * @return
	 */
	public String queryConcertBet() {
		try {

			String searchname = getRequest().getParameter("searchname") == null ? "": getRequest().getParameter("searchname").toString();
			if (null == pageIndex)
				pageIndex = 1;
			if (null == size)
				size = 10; 
			// 获取流水数据
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,"getConcertPageBet", new Object[] { searchname, pageIndex,size, 1,1 }, Page.class, Concert.class);
			getRequest().setAttribute("searchname", searchname);
			getRequest().setAttribute("page", page);

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
			return "index";
		}
		return INPUT;
	}
	
	/**
	 * @author ck 
	 * @return
	 */
	public void queryConcertBetNew() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Users customer = getCustomerFromSession();
			if(customer != null){
				Concert self = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getConcertSelfBet", new Object[] {
						customer.getLoginname(), this.type}, Concert.class);
				result.put("self", self);
			}
			Page page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2+ "UserWebService", false), AxisUtil.NAMESPACE,"getConcertPageBetNew", new Object[] { pageIndex, size, this.type }, Page.class, Concert.class);
			result.put("success", true);
			result.put("page", page);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "系统繁忙，请稍后再试！");
		}
		GsonUtil.GsonObject(result);
	}
	
	/**
	 * 11月抢金子活动
	 * @author ck 
	 * @return
	 */
	public String queryGoldBet() {
		try {

			String searchname = getRequest().getParameter("searchname") == null ? "": getRequest().getParameter("searchname").toString();
			if (null == pageIndex)
				pageIndex = 1;
			if (null == size)
				size = 10;
			if (null == sid) {
				String strSid = AxisUtil.getObjectOne( AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,"getConcertShow", new Object[] {}, String.class);
				sid = Integer.valueOf(strSid).intValue();
			}
			
			if (getCustomerFromSession()!=null) {
				Integer rankNow = AxisUtil.getObjectOne( AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,"getConcertPageRank", new Object[] {getCustomerLoginname(),2,sid}, Integer.class);
				getRequest().setAttribute("rankNow", rankNow);
			}
			
			// 获取流水数据
			Page page = AxisUtil.getPageInList(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,"getConcertPageBet", new Object[] { searchname, pageIndex,size, sid ,2}, Page.class, Concert.class);
			getRequest().setAttribute("searchname", searchname);
			getRequest().setAttribute("page", page);

		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
			return "index";
		}
		return INPUT;
	}
	
	public void getAgentCode(){
		
		try {
			this.referWebsite=this.getRequest().getScheme()+"://"+this.getRequest().getServerName();
			Users user=AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "getAgentByUrl", new Object[] {referWebsite}, Users.class);
			if(user != null && "AGENT".equalsIgnoreCase(user.getRole())){
				GsonUtil.GsonObject(user.getId());
			} else {
				GsonUtil.GsonObject(null);
			}
		} catch (AxisFault e) {
			log.error("根据URL获取代理账号失败：", e);
			GsonUtil.GsonObject(null);
		}
	}

	public void queryGoldBetByJson() {

		try {
			List<FirstlyCashin> list = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE,"getConcertPageBetByJson", new Object[] { sid, 2 },FirstlyCashin.class);
			JSONArray jsonArray = JSONArray.fromObject(list);			
		    writeJson(jsonArray.toString());
		    
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
		}
	}
	
	/***
	 * 新秒存
	 */
	public String getNewdeposit(){
		
		
		Bankinfo bankinfo = null;
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if(!"MONEY_CUSTOMER".equals(user.getRole())){
				bankinfo = new Bankinfo();
				bankinfo.setMassage("代理不能存款！");
				GsonUtil.GsonObject(bankinfo);
				return null ;
			}
			String loginname = user.getLoginname();
			if(StringUtils.isBlank(loginname)){
				bankinfo = new Bankinfo();
				bankinfo.setMassage("请登录后再试！");
				GsonUtil.GsonObject(bankinfo);
				return null ;
			}
			if(user.getFlag()==1){
				bankinfo = new Bankinfo();
				bankinfo.setMassage("账号已经禁用,请联系客服！");
				GsonUtil.GsonObject(bankinfo);
				return null ;
			}
			DecimalFormat currentNumberFormat = new DecimalFormat("#0");
			bankinfo = SynchronizedUtil.getInstance().getNewdeposit(loginname, banktype,uaccountname,ubankname,ubankno,amount,force,depositId);
			
			System.out.println("bankinfo:"+bankinfo.toString());
			BankinfoResponseVo bankinfoResponseVo = new BankinfoResponseVo();
			BeanUtils.copyProperties(bankinfoResponseVo, bankinfo);
			bankinfoResponseVo.setAmount(String.valueOf(bankinfo.getQuota()));
			GsonUtil.GsonObject(bankinfoResponseVo);
		} catch (Exception e) {
			e.printStackTrace();
			bankinfo = new Bankinfo();
			bankinfo.setMassage("系统异常，请您稍后再试！");
			GsonUtil.GsonObject(bankinfo);
			return null ;
		}
		return null ;
	}
	
	
	

	/***
	 * 获取微信转账额度
	 */
	public String getWxZzQuota(){
		Bankinfo bankinfo = null;
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				bankinfo = new Bankinfo();
				bankinfo.setMassage("[提示]代理不能使用秒存！");
				GsonUtil.GsonObject(bankinfo);
				return null ;
			}
			DecimalFormat df = new DecimalFormat("######0.00");
			Double doubleAmount = SynchronizedUtil.getInstance().getWxZzQuota(loginname,amount);
			String amounts = df.format(doubleAmount);
			GsonUtil.GsonObject(amounts);
	
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(bankinfo);
			return null ;
		}
		return null ;
	}
	
	
	
	
	
	/**
	 * 玩家废除秒存订单。
	 * */
	public void cancelNewdeposit(){
		
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试！");
				return;
			}
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "cancelNewdeposit", new Object[] {loginname, this.depositId}, String.class);
			GsonUtil.GsonObject(msg);
		} catch(Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统异常，请您稍后再试！");
		}
	}
	
	/***
	 * 新秒存 查询历史银行记录接口
	 */
	public String queryDepositBank(){
		Page page;
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			page = AxisUtil.getPageInList(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2+ "UserWebService", false), AxisUtil.NAMESPACE, "queryDepositBank", new Object[] {loginname,pageIndex, size}, Page.class,DepositOrder.class);
			GsonUtil.GsonObject(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * 新秒存 删除历史银行记录接口
	 */
	public String updateDepositBank(){
		Integer msg = null;
		try {
			msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "updateDepositBank", new Object[] {loginname,ubankno,depositId}, Integer.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject("删除失败");
		}
		GsonUtil.GsonObject("删除成功");
		return null;
	}
	
	
	/***
	 * 新秒存 选中历史银行记录接口
	 */
	public String getDepositBank(){
		DepositOrder depositOrder = null;
		try {
			depositOrder = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "getDepositBank", new Object[] {loginname,ubankno}, DepositOrder.class);
		} catch (AxisFault e) {
			e.printStackTrace();
			GsonUtil.GsonObject("选中失败");
		}
		GsonUtil.GsonObject(depositOrder);
		return null;
	}
	
	
	/***
	 * 新秒存 保存订单
	 */
	public String saveOrder(){
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "UserWebService", false), AxisUtil.NAMESPACE, "saveOrder", new Object[] {loginname,bankname,username,amount,depositId,banktype,uaccountname}, String.class);
			if(msg == null){
				GsonUtil.GsonObject("保存订单失败，请不要重复提交");
			}
			else{
				GsonUtil.GsonObject("保存订单成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("保存订单失败，请不要重复提交");
		}
		return null;
	}
	
	/****
	 * 微信秒存
	 * @return
	 */
	public String getWeiXinDeposit(){
		Bankinfo bankinfo = null;
		try {
			String loginname = getCustomerLoginname();
			if(StringUtils.isBlank(loginname)){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			
			// 判断订单金额
		    if (amount == null || amount.equals("")) {
				bankinfo = new Bankinfo();
				bankinfo.setMassage("存款金额不能为空");
				GsonUtil.GsonObject(bankinfo);
				return null ;
			}
			
		
			bankinfo = SynchronizedUtil.getInstance().createWeiXindeposit(loginname,amount);
			GsonUtil.GsonObject(bankinfo);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(bankinfo);
			return null ;
		}
		return null ;
	}
	
	
	/***
	 * 微信秒存 作废订单
	 */
	public void discardOrder(){
		log.info("废弃微信秒存存款订单" + id);
		Boolean flag =true;
		try {
		    flag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "discardOrder", new Object[]{id}, Boolean.class);
			if(flag){
				GsonUtil.GsonObject("1");
			}
		} catch (AxisFault e) {
			GsonUtil.GsonObject("-2");
		}
	}
	
	
	/**
	 * 一键转账到主账户
	 * 
	 * @return
	 */
	public String oneKeyGameMoney() {
		PostMethod method = null;
		try {
			
			Users customer = getCustomerFromSession();
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("product", "ld");
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

			method = new PostMethod(url + "/self/transfer/oneKeyGameMoney");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				GsonUtil.GsonObject("系统繁忙，请稍后重试！");
			} else {
				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				responseString = resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);
				GsonUtil.GsonObject(resultMap.get("message"));
				refreshUserInSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后重试！");
		} finally {
			// 释放
			if (null != method) {
				method.releaseConnection();
			}
		}
		return null;
	}
	
	/**
	 * 获取 Dt 奖池
	 * */
	public void dtJackpot() {
		DtPotVO result = new DtPotVO();

		try {
			result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "dtJackpot", null, DtPotVO.class);
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


	//返回生日礼金信息，以及用户获取等级信息
	public String checkBirthday(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if(user == null){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			ActivityConfig s = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "checkActivityInfo", new Object[]{"birthdaycash",user.getLevel().toString()}, ActivityConfig.class);
			if(null==s){
				GsonUtil.GsonObject("对不起您不在此活动范围内");
			}else{
				Map<String, String> map = new HashMap<String, String>();
				map.put("birthday", user.getBirthday() == null?"":DateUtil.fmtyyyy_MM_d(user.getBirthday()));
				map.put("level", user.getLevel().toString());
				map.put("amount", s.getAmount() + "");
				map.put("id", s.getId()+"");
				GsonUtil.GsonObject(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//领取生日礼金
	public String birthdayActivityInfo(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			if(user == null){
				GsonUtil.GsonObject("请登录后再试。");
				return null ;
			}
			String s = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "birthdayActivityInfo", new Object[]{user.getLoginname(),Integer.parseInt(id)}, String.class);
			GsonUtil.GsonObject(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}