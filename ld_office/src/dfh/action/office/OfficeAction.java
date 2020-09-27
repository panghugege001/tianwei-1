package dfh.action.office;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nnti.office.model.auth.Operator;
import com.nnti.office.model.log.Operationlogs;
import com.opensymphony.xwork2.Action;

import app.model.po.PreferentialConfig;
import app.util.FTPClientUtilX;
import app.util.RedisUtil;
import au.com.bytecode.opencsv.CSVReader;
import dfh.action.SubActionSupport;
import dfh.action.bbs.Bbs;
import dfh.action.vo.PayWayVo;
import dfh.action.vo.WeekSentVO;
import dfh.dao.ConstDao;
import dfh.dao.UserDao;
import dfh.email.template.EmailTemplateHelp;
import dfh.icbc.quart.fetch.JCDataThread;
import dfh.icbc.quart.fetch.SlotsMatchWeeklyJob;
import dfh.model.*;
import dfh.model.bean.AuditDetail;
import dfh.model.bean.QtBetVo;
import dfh.model.enums.ConcertDateType;
import dfh.model.enums.Goddesses;
import dfh.model.enums.IssuingBankNumber;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.PayType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.model.notdb.MGPlaycheckVo;
import dfh.model.notdb.MGTransferLog;
import dfh.model.notdb.PtCommissionsVo;
import dfh.model.notdb.Report;
import dfh.model.notdb.ReturnBean;
import dfh.remote.RemoteCaller;
import dfh.remote.RemoteConstant;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.IAutoUpgradeService;
import dfh.service.interfaces.IFriendMoneyDistributeService;
import dfh.service.interfaces.IGetdateService;
import dfh.service.interfaces.ILosePromoService;
import dfh.service.interfaces.ISystemConfigService;
import dfh.service.interfaces.IWeekSentService;
import dfh.service.interfaces.IchangeLineUserService;
import dfh.service.interfaces.MGSService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.SlaveService;
import dfh.service.interfaces.SynRecordsService;
import dfh.service.interfaces.TransferService;
import dfh.spider.PTBetVO;
import dfh.spider.PTSpider;
import dfh.spider.PTSpider2;
import dfh.utils.AESUtil;
import dfh.utils.Arith;
import dfh.utils.AutoAgentVipUtil;
import dfh.utils.AutoUpdateLevelUtil;
import dfh.utils.BBinUtils;
import dfh.utils.CheckInOutUtil;
import dfh.utils.ChessUtil;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.DtUtil;
import dfh.utils.EBetAppUtil;
import dfh.utils.EBetUtil;
import dfh.utils.FanYaUtil;
import dfh.utils.FlashPayUtil;
import dfh.utils.GPIUtil;
import dfh.utils.GsonUtil;
import dfh.utils.HttpUtils;
import dfh.utils.JCUtil;
import dfh.utils.JLKSHA256;
import dfh.utils.JLKUtil;
import dfh.utils.KYQPUtil;
import dfh.utils.LogUtils;
import dfh.utils.MGSUtil;
import dfh.utils.MWGUtils;
import dfh.utils.MatchDateUtil;
import dfh.utils.NTUtils;
import dfh.utils.NTwoUtil;
import dfh.utils.NumericUtil;
import dfh.utils.PBUtil;
import dfh.utils.PNGUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.PagenationUtil;
import dfh.utils.PtUtil;
import dfh.utils.PtUtil1;
import dfh.utils.QtUtil;
import dfh.utils.SHAUtil;
import dfh.utils.SSLMailSender;
import dfh.utils.SendPhoneMsgUtil;
import dfh.utils.ShaBaUtils;
import dfh.utils.SixLotteryUtil;
import dfh.utils.SlotUtil;
import dfh.utils.SmsPwdUtils;
import dfh.utils.StringUtil;
import dfh.utils.SynchronizedPool;
import dfh.utils.SynchronizedUtil;
import dfh.utils.TlyDepositUtil;
import dfh.utils.bitGame.BitGameUtil;
import dfh.utils.sendemail.SendEmailWs;
import dfh.utils.sendemail.SendEmailWsByEdm;
import dfh.utils.sendemail.SendEmailWsByMyself;
import dfh.utils.sendemail.SendYiYeEmail;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class OfficeAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(OfficeAction.class);
	private static final int BUFFER_SIZE = 16 * 1024;
	private JdbcTemplate jdbcTemplate;
	private ProposalService proposalService;
	private NetpayService netpayService;
	private OperatorService operatorService;
	private CustomerService customerService;
	private TransferService transferService;
	private SynRecordsService synRecordsService;
	private SeqService seqService;
	private NotifyService notifyService;
	private UserDao userDao;
	private ConstDao constDao;
	private IGetdateService getdateService;
	private ILosePromoService losePromoService;
	private IWeekSentService weekSentService;
	private String countProxyFirst_start;
	private String countProxyFirst_end;
	private IFriendMoneyDistributeService friendMoneyDistributeService;
	private MGSService mgsService;
	private String belongto;
	private Integer transfeId;
	private String transferIn;
	private String transferOut;
	private ActivityConfig activityConfig;
	private CouponConfig couponConfig;
	private Date startcreateTime;
	
	private Date endcreateTime;
	
	private Double startDeposit;
	
	private Double endDeposit;
	
	private String codeAfter;
	
	private String codeBefore;
	
	private String userName;
	
	private Double startWinOrLose;
	
	private Double endWinOrLose;
	
	private String callDayNum; 
	
	private IchangeLineUserService changeLineUserService;

	public CouponConfig getCouponConfig() {
		return couponConfig;
	}

	public void setCouponConfig(CouponConfig couponConfig) {
		this.couponConfig = couponConfig;
	}
	
	public ActivityConfig getActivityConfig() {
		return activityConfig;
	}

	public void setActivityConfig(ActivityConfig activityConfig) {
		this.activityConfig = activityConfig;
	}


	public Integer getTransfeId() {
		return transfeId;
	}

	public void setTransfeId(Integer transfeId) {
		this.transfeId = transfeId;
	}

	public String getBelongto() {
		return belongto;
	}

	public void setBelongto(String belongto) {
		this.belongto = belongto;
	}
	
	private static ResourceBundle config = ResourceBundle.getBundle("config");
	
	public MGSService getMgsService() {
		return mgsService;
	}

	public void setMgsService(MGSService mgsService) {
		this.mgsService = mgsService;
	}

	public IFriendMoneyDistributeService getFriendMoneyDistributeService() {
		return friendMoneyDistributeService;
	}

	public void setFriendMoneyDistributeService(
			IFriendMoneyDistributeService friendMoneyDistributeService) {
		this.friendMoneyDistributeService = friendMoneyDistributeService;
	}

	public String getCountProxyFirst_start() {
		return countProxyFirst_start;
	}

	public void setCountProxyFirst_start(String countProxyFirst_start) {
		this.countProxyFirst_start = countProxyFirst_start;
	}

	public String getCountProxyFirst_end() {
		return countProxyFirst_end;
	}

	public void setCountProxyFirst_end(String countProxyFirst_end) {
		this.countProxyFirst_end = countProxyFirst_end;
	}

	public IWeekSentService getWeekSentService() {
		return weekSentService;
	}

	public void setWeekSentService(IWeekSentService weekSentService) {
		this.weekSentService = weekSentService;
	}
	private IAutoUpgradeService autoUpgradeService;
	private SlaveService slaveService ;
	
	public IGetdateService getGetdateService() {
		return getdateService;
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
	public ConstDao getConstDao() {
		return constDao;
	}

	public void setConstDao(ConstDao constDao) {
		this.constDao = constDao;
	}
	public IchangeLineUserService getChangeLineUserService() {
		return changeLineUserService;
	}
	public void setChangeLineUserService(IchangeLineUserService changeLineUserService) {
		this.changeLineUserService = changeLineUserService;
	}
	private String payBillno;
	private String payTime;
	private String payPlatform;
	private String payAmount;
	private String proposalRole;
	private String sms;
	private String billnotype;
	private SSLMailSender mailSender;
	private String shippinginfo;
	private String proposer;
	private String limit;
	private String paytype ;
	Proposal propo=new Proposal();
	private Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);

	public SSLMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(SSLMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getBillnotype() {
		return billnotype;
	}

	public void setBillnotype(String billnotype) {
		this.billnotype = billnotype;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getProposalRole() {
		return proposalRole;
	}

	public void setProposalRole(String proposalRole) {
		this.proposalRole = proposalRole;
	}

	private String loginname;
	private String password;
	private String validateCode;
	private Date start;
	private Date end;
	private String birthday;
	private Integer size;
	private Integer pageIndex;
	private String order;
	private String by;
	private String roleCode;
	private String pno;
	private String belong;
	private String qq;
	private Integer proposalFlag;
	private Integer proposalStatus;
	private String proposalType;
	private String creditLogType;
	private String bankCreditChangeType;
	private String downLmit;
	private String billno;
	private String jylx;
	private Integer examine;
	private Integer payOrderFlag;
	private Integer isTransferIn;
	private Integer transferFalg;
	private String amount;
	private String remark;
	private String title;
	private String corpBankName;
	private String corpBankAccount;
	private String payBankName;
	private String payBankAccount;
	private String accountName;
	private String accountType;
	private String accountCity;
	private String bankAddress;
	private String accountNo;
	private String bank;
	private String phone;
	private String email;
	private String aliasName;
	private String agent;
	private String payType;
	private Double firstCash;
	private Double tryCredit;
	private Double rate;
	private Double fee;
	private String type;
	private String content;
	private Integer id;
	private Long fldid;
	private String PhoneNum;
	private String Msg;
	private Boolean isEnable;
	private String jobPno;
	private String errormsg;
	private Integer isAdd;
	private String remoteUrl;
	private String bankaccount;
	private String saveway;
	private Integer overtime;
	private String bankname;
	private Integer bankinfoid;
	private String platform;
	private String acceptNameTwo;
	private String qrcode;
	private Qrcode weiXcode;

	public Qrcode getWeiXcode() {
		return weiXcode;
	}

	public void setWeiXcode(Qrcode weiXcode) {
		this.weiXcode = weiXcode;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getAcceptNameTwo() {
		return acceptNameTwo;
	}

	public void setAcceptNameTwo(String acceptNameTwo) {
		this.acceptNameTwo = acceptNameTwo;
	}
	private String oldPassword;
	private String newPassword;
	private String retypePassword;
	private String pesoRate;
	private Integer newaccount;
	private Integer betflag;
	
	private String randnum;

	private String code;// 存款确认码
	private String mailCode;
	
	private String partner;// 合作伙伴
	private String retypePartner;
	private String retypeLoginname;// 确定会员名

	private Integer stencil;// 模版

	private String urladdress;// 抄送网址 author:sun

	private Integer listLoginDay;// 最后登陆时间

	private Integer gameid;// 游戏公告编号
	private String gamecontent;// 内容

	private String gmCode;
	private String drawNo;
	private String playCode;

	private String key;
	private String value;

	private String acode;
	private String newagcode;
	private String referWebsite;
	private String howToKnow;
	private ISystemConfigService systemConfigService;

	public ISystemConfigService getSystemConfigService() {
		return systemConfigService;
	}

	public void setSystemConfigService(ISystemConfigService systemConfigService) {
		this.systemConfigService = systemConfigService;
	}
	//agTryGame
	private String agName;
	private String agPhone;
	private Integer agIsLogin;
	
	private String xml;

	private String registerIp;
	private String lastLoginIp;
	private String status;
	private String flag;
	private Integer times;
	private String isCashin;
	private Integer year;
	private Integer month;
	private Integer warnflag;
	private String warnremark;
	private String intro;
	private String intvalday;
	private String nintvalday;
	private Boolean isExecute;
	
	private String gifTamount;
	private String betMultiples;
	private String shippingCode;
	private String shippingCount;
	
	
	private String isreg;
	private String isdeposit;
	private String phonestatus;
	private String userstatus;
	private String cs;
    private Customer iphone;
    private AgentCustomer agentPhone;
    private String startYyyyMM;
    private String endYyyyMM;
    private String authority;
    
    private String activityName;
    private String percent;
    
    private Date startPt;
    private Date endPt;
    private Date startNT;
    private Date endNT;
    
    private String userType;
    private String usernameType;
    
    private Double earebate;
	private Double bbinrebate;
	private Double aginrebate;
	private Double agrebate;
	private Double kenorebate;
	private Double sbrebate;
	private Double ptrebate;
	/**
	 * 批次
	 * 2014-11-12
	 */
	private Integer batch;  
	
	private Date endDate ;
	private Date startDate ;
	private String executeTime ;
	private File file;
	private String ids ; 
	
	private String deviceID;
	
	private String acceptName ;
	
	private Boolean unfinshedYouHui ;
	private String remarkRemark;
	
	private boolean isShow ;
	private Date startTime;
	private Date createdate ;
	private String stringStartTime;
	private String stringEndTime;
    private String vip ;
	private String keyword;
	private Integer round;

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private String upgradeYyyyMm;
    
    private String distributeDate;
    
    private String activityBeginTime;
    private String activityEndTime;
    private Double depositAmount; 
    private String startTimeDeposit;
    private String endTimeDeposit;
    private Double betAmount;
    private String startTimeBet;
    private String endTimeBet;
    private Integer giftQuantity;
    
    private String address;       //地址
	private String addressee;     //收件人
	private String cellphoneNo;   //手机号
	private Integer agentType;
	private String dayNumflag;//未玩游戏玩家
	private String agentCommission;//老虎机佣金比率
	private Double liverate;//真人佣金比率
	private Double sportsrate;//体育佣金比率
	private Double lotteryrate;//彩票佣金比率
	private String agentUserName;//代理账号
	private String gameUserName;//游戏账号
	private String deleteFlag;//解绑标志
	
    //好友推荐金派发
    private String topLineUser;
    private String downLineUser;

    //守护女神
    private String goddessname;
    private String couponnum;
    private Double newbet;
    
    private String smsValidPwd;//短信验证密码
	private Integer validType;
	private String employeeNo;
	
	private String gamename;
	private String itemId;
	private String prizeId;
	private String prizePercent;
	private Integer minvalue;
	private String disable;
	private Guild guild;

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public Double getLiverate() {
		return liverate;
	}

	public void setLiverate(Double liverate) {
		this.liverate = liverate;
	}

	public Double getSportsrate() {
		return sportsrate;
	}

	public void setSportsrate(Double sportsrate) {
		this.sportsrate = sportsrate;
	}

	public Double getLotteryrate() {
		return lotteryrate;
	}

	public void setLotteryrate(Double lotteryrate) {
		this.lotteryrate = lotteryrate;
	}
	
	public String getAgentCommission() {
		return agentCommission;
	}

	public void setAgentCommission(String agentCommission) {
		this.agentCommission = agentCommission;
	}

	public Integer getMinvalue() {
		return minvalue;
	}

	public void setMinvalue(Integer minvalue) {
		this.minvalue = minvalue;
	}

	public String getDisable() {
		return disable;
	}

	public void setDisable(String disable) {
		this.disable = disable;
	}

	public String getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(String prizeId) {
		this.prizeId = prizeId;
	}

	public String getPrizePercent() {
		return prizePercent;
	}
	public void setPrizePercent(String prizePercent) {
		this.prizePercent = prizePercent;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public Integer getValidType() {
		return validType;
	}

	public void setValidType(Integer validType) {
		this.validType = validType;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getSmsValidPwd() {
		return smsValidPwd;
	}

	public void setSmsValidPwd(String smsValidPwd) {
		this.smsValidPwd = smsValidPwd;
	}
    
	public Double getNewbet() {
		return newbet;
	}

	public void setNewbet(Double newbet) {
		this.newbet = newbet;
	}

	public String getGoddessName() {
		return goddessname;
	}

	public void setGoddessName(String goddessName) {
		this.goddessname = goddessName;
	}

	public String getCouponnum() {
		return couponnum;
	}

	public void setCouponnum(String couponnum) {
		this.couponnum = couponnum;
	}

	public String getTopLineUser() {
		return topLineUser;
	}

	public void setTopLineUser(String topLineUser) {
		this.topLineUser = topLineUser;
	}

	public String getDownLineUser() {
		return downLineUser;
	}

	public void setDownLineUser(String downLineUser) {
		this.downLineUser = downLineUser;
	}

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public String getGameUserName() {
		return gameUserName;
	}

	public void setGameUserName(String gameUserName) {
		this.gameUserName = gameUserName;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDayNumflag() {
		return dayNumflag;
	}

	public void setDayNumflag(String dayNumflag) {
		this.dayNumflag = dayNumflag;
	}

	
	
	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

    public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	private String fileFileName;
	
	public Integer getBatch() {
		return batch;
	}

	public void setBatch(Integer batch) {
		this.batch = batch;
	} 
	private String invitecode ;
	public String getInvitecode() {
		return invitecode;
	}

	public void setInvitecode(String invitecode) {
		this.invitecode = invitecode;
	}
    
    Activity activity=new Activity();
    Userbankinfo userbank=new Userbankinfo();
    

	public Integer getWarnflag() {
		return warnflag;
	}

	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}

	public String getWarnremark() {
		return warnremark;
	}

	public void setWarnremark(String warnremark) {
		this.warnremark = warnremark;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsCashin() {
		return isCashin;
	}

	public void setIsCashin(String isCashin) {
		this.isCashin = isCashin;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getReferWebsite() {
		return referWebsite;
	}

	public void setReferWebsite(String referWebsite) {
		this.referWebsite = referWebsite;
	}

	public String getHowToKnow() {
		return howToKnow;
	}

	public void setHowToKnow(String howToKnow) {
		this.howToKnow = howToKnow;
	}

	public String getAcode() {
		return acode;
	}

	public void setAcode(String acode) {
		this.acode = acode;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUrladdress() {
		return urladdress;
	}

	public void setUrladdress(String urladdress) {
		this.urladdress = urladdress;
	}

	public String getRetypePassword() {
		return retypePassword;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}

	public Integer getNewaccount() {
		return newaccount;
	}

	public void setNewaccount(Integer newaccount) {
		this.newaccount = newaccount;
	}

	private String newOperator;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewOperator() {
		return newOperator;
	}

	public void setNewOperator(String newOperator) {
		this.newOperator = newOperator;
	}

	public Integer getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}

	public Integer getStencil() {
		return stencil;
	}

	public void setStencil(Integer stencil) {
		this.stencil = stencil;
	}

	public Integer getListLoginDay() {
		return listLoginDay;
	}

	public void setListLoginDay(Integer listLoginDay) {
		this.listLoginDay = listLoginDay;
	}

	public SynRecordsService getSynRecordsService() {
		return synRecordsService;
	}

	public void setSynRecordsService(SynRecordsService synRecordsService) {
		this.synRecordsService = synRecordsService;
	}

	public Integer getGameid() {
		return gameid;
	}

	public void setGameid(Integer gameid) {
		this.gameid = gameid;
	}

	public String getGamecontent() {
		return gamecontent;
	}

	public void setGamecontent(String gamecontent) {
		this.gamecontent = gamecontent;
	}

	public String getGmCode() {
		return gmCode;
	}

	public void setGmCode(String gmCode) {
		this.gmCode = gmCode;
	}

	public String getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(String drawNo) {
		this.drawNo = drawNo;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	/**
	 * queryRemoteTransfer
	 */
	private String transID;

	/**
	 * setLevel
	 */
	private Integer level;

	private static final long serialVersionUID = 1L;

	public String addCashinProposal() {
		try {
			if (bankinfoid == 0) {
				setErrormsg("请选择存款账户");
				return INPUT;
			}
			if (saveway.equals("")) {
				setErrormsg("请选择存款方式");
				return INPUT;
			}
			Bankinfo bankinfo = (Bankinfo) operatorService.get(Bankinfo.class, bankinfoid);
			if (bankinfo == null) {
				setErrormsg("此存款账户不存在");
				return INPUT;
			}
			Double d = 0.00;
			if (null != amount && !"".equals(amount.trim())) {
				try {
					d = Double.parseDouble(amount.trim());
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("额度类型不正确");
					return INPUT;
				}
			}
			String msg = proposalService.addCashin(getOperatorLoginname(), StringUtil.trim(loginname), StringUtil.trim(accountName), title, Constants.FROM_BACK, d, StringUtil.trim(bankinfo.getBankname()), StringUtil.trim(remark), IssuingBankNumber.getText(StringUtil.trim(bankinfo.getBankname())), bankinfo.getUsername(), saveway,fee);
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
	 * 查询自助红包
	 * @return
	 */
	public String getHBrecord(){
		DetachedCriteria dc=null;
		if(type.equals("1")){//获取奖金记录
			dc= DetachedCriteria.forClass(HBrecord.class);
			dc.add(Restrictions.eq("type", "1"));
		}else if(type.equals("2")){//消费奖金记录
			dc= DetachedCriteria.forClass(HBrecord.class);
			dc.add(Restrictions.eq("type", "2"));
		}else if(type.equals("3")){//奖金池
			dc= DetachedCriteria.forClass(HBbonus.class);
		}else{//查询全部
			dc= DetachedCriteria.forClass(HBrecord.class);
		}
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("username", username));
		}
		if (startTime != null){
			dc = dc.add(Restrictions.ge("updatetime", startTime));
		}
		if (endTime != null){
			dc = dc.add(Restrictions.le("updatetime", endTime));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("updatetime");
//			dc = dc.addOrder(Order.desc("updatetime"));
		}

		Page  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		Double sumMoney =0.0;

		List list = operatorService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			for (Object obj : list) {
				if ("3".equals(type)) {
					HBbonus fd = (HBbonus) obj;
					sumMoney = sumMoney + fd.getMoney();
				} else {
					HBrecord fd = (HBrecord) obj;
					sumMoney = sumMoney + fd.getMoney();
				}
			}
			page.setStatics1(sumMoney);
		}
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String addCashOutProposal() {
		Double d = 0.00;
		if (null != amount && !"".equals(amount.trim())) {
			try {
				d = Double.parseDouble(amount.trim());
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("额度类型不正确");
				return INPUT;
			}
		}
		try {
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			String msg = proposalService.addCashout(getOperatorLoginname(), customer.getLoginname(), customer.getPassword(), customer.getRole(), Constants.FROM_BACK, d, customer.getAccountName(), StringUtils.trim(accountNo), StringUtils.trim(accountType), StringUtils.trim(bank), StringUtils.trim(accountCity), StringUtil.trim(bankAddress), StringUtils.trim(email), StringUtils.trim(phone), getIp(), StringUtils.trim(remark));
			if (msg == null)
				setErrormsg("提交成功");
			else
				this.addActionError(msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String addProxyCashOutProposal(){
		try {
			if(loginname==null && loginname.equals("")){
				setErrormsg("代理账号不能为空！");
				return INPUT;
			}
			
			if(accountNo==null && accountNo.equals("")){
				setErrormsg("银行账号不能为空！");
				return INPUT;
			}
			
			if(accountName==null && accountName.equals("")){
				setErrormsg("账号姓名不能为空！");
				return INPUT;
			}
			
			if(bank==null && bank.equals("")){
				setErrormsg("账号姓名不能为空！");
				return INPUT;
			}
			
			Double d = 0.00;
			if (null != amount && !"".equals(amount.trim())) {
				try {
					d = Double.parseDouble(amount.trim());
				} catch (Exception e) {
					e.printStackTrace();
					setErrormsg("额度类型不正确");
					return INPUT;
				}
			}
			
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getRole().equals("MONEY_CUSTOMER")){
				setErrormsg("不是代理账号！");
				return INPUT;
			}
			
			String msg = proposalService.addProxyCashOutProposal(getOperatorLoginname(), customer.getLoginname(), Constants.FROM_BACK, d, StringUtils.trim(accountNo), accountName,StringUtils.trim(bank),StringUtils.trim(remark),getIp());
			if (msg == null)
				setErrormsg("提交成功");
			else
				setErrormsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}finally{
			accountName=null;
			amount=null;
			userbank=null;
			remark=null;
		}
		return INPUT;
	}
	
	public String getProxyAdvance() {
		if (loginname != null) {
			Users user = (Users) operatorService.get(Users.class, loginname);
			if (user != null) {
				if(user.getRole().equals("MONEY_CUSTOMER")){
					this.setErrormsg("不是代理账号！");
					setAccountName(null);
					userbank=null;
					return INPUT;
				}
				setAccountName(user.getAccountName());
				
				DetachedCriteria dc = DetachedCriteria.forClass(Userbankinfo.class);
				dc.add(Restrictions.eq("loginname", user.getLoginname()));
				dc.add(Restrictions.eq("flag", 0));
				List<Userbankinfo> userbankinfo = operatorService.findByCriteria(dc);
				if(userbankinfo!=null && userbankinfo.size()>0 && userbankinfo.get(0)!=null){
					userbank=userbankinfo.get(0);
				}
			}
		}
		return INPUT;
	}
	

	public String addConcessionsProposal() {
		try {
			String msg = null;
			if (flag == null || flag.equals("") || flag.equals("1")) {
				msg = proposalService.addUserConcession(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, firstCash, tryCredit, StringUtil.trim(payType), StringUtil.trim(remark), getIp());
			} else {
				if (times == null || times == 0) {
					msg = "投注倍数不可为空或等于0";
				} else {
					msg = proposalService.addUserTimesConcession(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, firstCash, tryCredit, StringUtil.trim(payType), StringUtil.trim(remark), times, getIp());
				}
			}

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
	 * 增加转账优惠提案
	 * 
	 * @return
	 */
	public String addBankTransferConsProposal() {
		try {
			String msg = proposalService.addBankTransferCons(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, firstCash, tryCredit, StringUtil.trim(payType), StringUtil.trim(remark));
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
	 * 增加幸运抽奖提案
	 * 
	 * @return
	 */
	public String addPrizeProposal() {
		try {
			String msg = proposalService.addPrize(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, tryCredit, StringUtil.trim(remark));
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
	 * 增加生日礼金提案
	 * 
	 * @return
	 */
	public String addPrizeProposalBirthdayGifts() {
		try {
			String msg = proposalService.addPrizeBirthdayGifts(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, tryCredit, StringUtil.trim(remark));
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
	 * 优惠劵提案
	 * 
	 * @return
	 */
	public String addPrizeProposalCoupon() {
		try {
			Integer typeNew=0;
			if(type==null || "".equals(type)){
				this.setErrormsg("优惠劵类型不能为空！");
				return INPUT;
			}
			try {
				typeNew=Integer.parseInt(type);
				if(typeNew==537){
					if(agent==null || "".equals(agent)){
						this.setErrormsg("代理账号不能为空！");
						return INPUT;
					}
					Users userAgent = proposalService.getUserAgent(agent);
					if(userAgent==null){
						this.setErrormsg("代理账号不存在！");
						return INPUT;
					}
				}
			} catch (Exception e) {
				this.setErrormsg("优惠类型有误！");
				return INPUT;
			}
			Double amountNew=0.00;
			if(amount==null || "".equals(amount)){
				amountNew=0.00;
			}else{
				try {
					amountNew=Double.parseDouble(amount);
				} catch (Exception e) {
					this.setErrormsg("赠送金额不能为空！");
					return INPUT;
				}
			}
			if(betMultiples==null || "".equals(betMultiples)){
				betMultiples="0";
			}
			//判断是群发还是个体发
			if(userType.equals("0")||typeNew==537||typeNew==536){
				Integer shippingCounts=1;
				if(shippingCount==null || shippingCount.equals("")){
					shippingCounts=1;
				}else{
					//判断是否是数字
					try {
						shippingCounts=Integer.parseInt(shippingCount);
					} catch (Exception e) {
						this.setErrormsg("生成数量非有效数字！请使用正整数");
						return INPUT;
					}
				}
				if(shippingCounts<=0){
					this.setErrormsg("生成数量非有效数字！请使用正整数");
					return INPUT;
				}
				for (int i = 0; i < shippingCounts; i++) {
					String msg = proposalService.addPrizeProposalCoupon(getOperatorLoginname(),Constants.FROM_BACK, typeNew, amountNew, betMultiples,StringUtil.trim(remark),agent);
					if (msg != null){
						setErrormsg("提交失败:" + msg);
						return INPUT;
					}
				}
			} else {
				String msg = proposalService.addPrizeProposalCouponTwo(getOperatorLoginname(),Constants.FROM_BACK, typeNew, amountNew, betMultiples,StringUtil.trim(remark),agent);
				if (msg != null){
					setErrormsg("提交失败:" + msg);
					return INPUT;
				}
			}
			setErrormsg("提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	
    /***********
     *  红包优惠
     * @return
     */
	public String addRedProposalCoupon() {

		try {
			if (type == null || "".equals(type)) {
				this.setErrormsg("优惠类型不能为空！");
				return INPUT;
			}
			Integer typeNew = 0;
			try {
				typeNew = Integer.parseInt(type);
			} catch (Exception e) {
				this.setErrormsg("优惠类型有误！");
				return INPUT;
			}
			Double amountNew = 0.00;
			// 如果是红包优惠劵 判断金额
			if (typeNew == 419) {
				if (amount == null || "".equals(amount)) {
					amountNew = 0.00;
				} else {
					try {
						amountNew = Double.parseDouble(amount);
					} catch (Exception e) {
						this.setErrormsg("赠送金额不能为空！");
						return INPUT;
					}
				}
			}
			if (betMultiples == null || "".equals(betMultiples)) {
				betMultiples = "0";
			}
			Integer shippingCounts = 1;
			if (shippingCount == null || shippingCount.equals("")) {
				shippingCounts = 1;
			} else {
				// 判断是否是数字
				try {
					shippingCounts = Integer.parseInt(shippingCount);
				} catch (Exception e) {
					this.setErrormsg("生成数量非有效数字！请使用正整数");
					return INPUT;
				}
			}
			if (shippingCounts <= 0) {
				this.setErrormsg("生成数量非有效数字！请使用正整数");
				return INPUT;
			}
			for (int i = 0; i < shippingCounts; i++) {
				String msg = proposalService.addRedProposalCoupon(getOperatorLoginname(), Constants.FROM_BACK, typeNew, amountNew, betMultiples, StringUtil.trim(remark), agent);
				if (msg != null) {
					setErrormsg("提交失败:" + msg);
					return INPUT;
				}
			}
			setErrormsg("提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	
	
	
	public String addPrizeProposalCouponPt(){
		try {
			if(type==null || "".equals(type)){
				this.setErrormsg("优惠劵类型不能为空！");
				return INPUT;
			}
			Integer typeNew=0;
			try {
				typeNew=Integer.parseInt(type);
			} catch (Exception e) {
				this.setErrormsg("优惠类型有误！");
				return INPUT;
			}
			Double amountNew=0.00;
			//如果是红包优惠劵 判断金额
			if(typeNew==571){
				if(amount==null || "".equals(amount)){
					amountNew=0.00;
				}else{
					try {
						amountNew=Double.parseDouble(amount);
					} catch (Exception e) {
						this.setErrormsg("赠送金额不能为空！");
						return INPUT;
					}
				}
			}
			if(betMultiples==null || "".equals(betMultiples)){
				betMultiples="0";
			}
			//判断是群发还是个体
			if(userType==null || "".equals(userType)){
				this.setErrormsg("优惠劵用户类型不能为空！");
				return INPUT;
			}
			//判断是群发还是个体发
			if(userType.equals("0")){
				Integer shippingCounts=1;
				if(shippingCount==null || shippingCount.equals("")){
					shippingCounts=1;
				}else{
					//判断是否是数字
					try {
						shippingCounts=Integer.parseInt(shippingCount);
					} catch (Exception e) {
						this.setErrormsg("生成数量非有效数字！请使用正整数");
						return INPUT;
					}
				}
				if(shippingCounts<=0){
					this.setErrormsg("生成数量非有效数字！请使用正整数");
					return INPUT;
				}
				for (int i = 0; i < shippingCounts; i++) {
					String msg = proposalService.addPrizeProposalCouponPt(getOperatorLoginname(),Constants.FROM_BACK, typeNew, amountNew, betMultiples,StringUtil.trim(remark),agent);
					if (msg != null){
						setErrormsg("提交失败:" + msg);
						return INPUT;
					}
				}
			} else {
				if (usernameType == null || "".equals(usernameType)) {
					this.setErrormsg("群发用户类型不能为空！");
					return INPUT;
				}
				String msg = proposalService.addPrizeProposalCouponPtTwo(getOperatorLoginname(),Constants.FROM_BACK, typeNew, amountNew, betMultiples,StringUtil.trim(remark),agent,usernameType);
				if (msg != null){
					setErrormsg("提交失败:" + msg);
					return INPUT;
				}
			}
			setErrormsg("提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	
	/**
	 * ttg/gpi/nt/qt优惠券
	 * @return
	 */
	public String addPrizeProposalCouponTtg() {
		try {
			if(type==null || "".equals(type)){
				this.setErrormsg("优惠劵类型不能为空！");
				return INPUT;
			}
			Integer typeNew=0;
			try {
				typeNew=Integer.parseInt(type);
			} catch (Exception e) {
				this.setErrormsg("优惠类型有误！");
				return INPUT;
			}
			Double amountNew=0.00;
			if(betMultiples==null || "".equals(betMultiples)){
				betMultiples="0";
			}
			//判断是群发还是个体
			if(userType==null || "".equals(userType)){
				this.setErrormsg("优惠劵用户类型不能为空！");
				return INPUT;
			}
			//判断是群发还是个体发
				Integer shippingCounts=1;
				if(shippingCount==null || shippingCount.equals("")){
					shippingCounts=1;
				}else{
					//判断是否是数字
					try {
						shippingCounts=Integer.parseInt(shippingCount);
					} catch (Exception e) {
						this.setErrormsg("生成数量非有效数字！请使用正整数");
						return INPUT;
					}
				}
				if(shippingCounts<=0){
					this.setErrormsg("生成数量非有效数字！请使用正整数");
					return INPUT;
				}
				for (int i = 0; i < shippingCounts; i++) {
					String msg = proposalService.addPrizeProposalCouponTtg(getOperatorLoginname(),Constants.FROM_BACK, typeNew, amountNew, betMultiples,StringUtil.trim(remark));
					if (msg != null){
						setErrormsg("提交失败:" + msg);
						return INPUT;
					}
				}
			setErrormsg("提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String addPrizeProposalCouponSb() {
		try {
			if(type==null || "".equals(type)){
				this.setErrormsg("优惠劵类型不能为空！");
				return INPUT;
			}
			Integer typeNew=0;
			try {
				typeNew=Integer.parseInt(type);
			} catch (Exception e) {
				this.setErrormsg("优惠类型有误！");
				return INPUT;
			}
			Double amountNew=0.00;
			//如果是红包优惠劵 判断金额
			if(typeNew==581||typeNew==582){
				if(amount==null || "".equals(amount)){
					amountNew=0.00;
				}else{
					try {
						amountNew=Double.parseDouble(amount);
					} catch (Exception e) {
						this.setErrormsg("赠送金额不能为空！");
						return INPUT;
					}
				}
			}
			if(betMultiples==null || "".equals(betMultiples)){
				betMultiples="0";
			}
			//判断是群发还是个体
			if(userType==null || "".equals(userType)){
				this.setErrormsg("优惠劵用户类型不能为空！");
				return INPUT;
			}
			//判断是群发还是个体发
			if(userType.equals("0")){
				Integer shippingCounts = 1;
				if (shippingCount == null || shippingCount.equals("")) {
					shippingCounts = 1;
				} else {
					try {
						shippingCounts = Integer.parseInt(shippingCount);
					} catch (Exception e) {
						this.setErrormsg("生成数量非有效数字！请使用正整数");
						return INPUT;
					}
				}
				if (shippingCounts <= 0) {
					this.setErrormsg("生成数量非有效数字！请使用正整数");
					return INPUT;
				}
				for (int i = 0; i < shippingCounts; i++) {
					String msg = proposalService.addPrizeProposalCouponSb(getOperatorLoginname(),Constants.FROM_BACK, typeNew, amountNew,betMultiples, StringUtil.trim(remark));
					if (msg != null) {
						setErrormsg("提交失败:" + msg);
						return INPUT;
					}
				}
			} else {
				if (usernameType == null || "".equals(usernameType)) {
					this.setErrormsg("群发用户类型不能为空！");
					return INPUT;
				}
				String msg = proposalService.addPrizeProposalCouponSbTwo(getOperatorLoginname(),Constants.FROM_BACK, typeNew, amountNew, betMultiples,StringUtil.trim(remark),usernameType);
				if (msg != null){
					setErrormsg("提交失败:" + msg);
					return INPUT;
				}
			}
			
			
			setErrormsg("提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String addActivity(){
		if (activityName == null || activityName.equals("")) {
			setErrormsg("活动名称不能为空！");
			return INPUT;
		}
		if (percent == null || percent .equals("")) {
			setErrormsg("返水比例不能为空！");
			return INPUT;
		}
		Double activityPercent=0.0;
		try {
			activityPercent=Double.parseDouble(percent);
			if (activityPercent <= 0.0 || activityPercent >= 1.0) {
				setErrormsg("返水比例有问题，介于0到1之间");
				return INPUT;
			}
		} catch (Exception e) {
			setErrormsg("返水比例格式不正确！");
			return INPUT;
		}
		int day=0;
		if(start!=null && end !=null){
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			day=daysxiangcha(sdf2.format(start),sdf2.format(end));
			if(day>10){
				setErrormsg("时间间隔最多10天！");
				return INPUT;
			}
		}else{
			setErrormsg("活动时间不能为空！");
			return INPUT;
		}
		String msg = proposalService.addActivity(null,activityName,activityPercent,start,end,StringUtil.trim(remark));
		if (msg != null){
			setErrormsg("提交失败:" + msg);
			return INPUT;
		}
		setErrormsg("提交成功！");
		return INPUT;
	}
	
	public String addActivityTwo(){
		if(id==null){
			setErrormsg("id不能为空！");
			return INPUT;
		}
		if (activityName == null || activityName.equals("")) {
			setErrormsg("活动名称不能为空！");
			return INPUT;
		}
		if (percent == null || percent .equals("")) {
			setErrormsg("返水比例不能为空！");
			return INPUT;
		}
		Double activityPercent=0.0;
		try {
			activityPercent=Double.parseDouble(percent);
			if (activityPercent <= 0.0 || activityPercent >= 1.0) {
				setErrormsg("返水比例有问题，介于0到1之间");
				return INPUT;
			}
		} catch (Exception e) {
			setErrormsg("返水比例格式不正确！");
			return INPUT;
		}
		int day=0;
		if(start!=null && end !=null){
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			day=daysxiangcha(sdf2.format(start),sdf2.format(end));
			if(day>10){
				setErrormsg("时间间隔最多10天！");
				return INPUT;
			}
		}else{
			setErrormsg("活动时间不能为空！");
			return INPUT;
		}
		String msg = proposalService.addActivity(id,activityName,activityPercent,start,end,StringUtil.trim(remark));
		activity = (Activity) operatorService.get(Activity.class, id);
		if (msg != null){
			setErrormsg("提交失败:" + msg);
			return INPUT;
		}
		setErrormsg("提交成功！");
		return INPUT;
	}
	
	public String getProposalPno(){
		propo = proposalService.getProposalPno(pno);
		return INPUT;
	}

	/**
	 * 增加晋级礼金提案
	 * 
	 * @return
	 */
	public String addProposalLevelPrize() {
		try {
			String msg = proposalService.addProposalLevelPrize(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, tryCredit, StringUtil.trim(remark));
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

	public String addNewAccountProposal() {
		try {
			String msg = proposalService.operatorAddNewAccount(getOperatorLoginname(), StringUtil.trim(loginname), StringUtil.trim(password), title, Constants.FROM_BACK, StringUtil.trim(aliasName), StringUtil.trim(phone), email, title, StringUtil.trim(remark), getIp());
			if (msg == null)
				setErrormsg("开户成功");
			else
				setErrormsg("开户失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("开户失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String addRebankInfoProposal() {
		try {
			String msg = proposalService.addReBankInfo(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, StringUtil.trim(accountName), StringUtil.trim(accountNo), StringUtil.trim(accountType), StringUtil.trim(bank), StringUtil.trim(accountCity), StringUtil.trim(bankAddress), getIp(), StringUtil.trim(remark));
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

	public String addXimaProposal() {
		try {
			String msg = proposalService.addXima(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, start, end, firstCash, (rate.doubleValue() / 100), StringUtil.trim(payType), StringUtil.trim(remark));
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

	public String auditProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("提案审核");
		try {
			String msg = proposalService.audit(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	

    /****
	 * 审核且秒提
	 * @Description 
	 * @author Addis
	 * @return
	 */
    public String auditSProposal() {
        PrintWriter out = null;
        String msg = null;
        try {
            out = this.getResponse().getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
			Const cons = (Const) proposalService.get(Const.class, "银行秒付(后台)");     
			if(cons!=null && cons.getValue().equals("0")){
				msg =  "银行秒付(后台)暂停";
				out.println(msg);
				out.flush();
				out.close();
				return null;
			}
			//秒付宝
			msg = FlashPayUtil.addFPay(jobPno,getOperatorLoginname(),StringUtil.trim(remark),getIp());
			out.println(msg);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    
    
    /**
	 * 更新订单
	 * @return
	 */
	public String updateFlashPayOrder(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			//秒付宝
			String msg = FlashPayUtil.updateFPayOrder(jobPno);
			out.println(msg);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 重新提交
	 * @return
	 */
	public String resubmit(){
		PrintWriter out = null;
		String msg="";
		try {
			out = this.getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			//秒付宝
			msg = FlashPayUtil.resubmit(jobPno,getOperatorLoginname(),getIp());
			out.println(msg);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	public String auditSProposal3() {
		try {
			
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("提案审核");
		try {
			out = this.getResponse().getWriter();
//			Const cons = (Const) userDao.get(Const.class, "民生银行秒付");
//			if(cons!=null && cons.getValue().equals("0")){
//				out.println("民生银行秒付暂停");
//				return null;
//			}
			Const cons = (Const) userDao.get(Const.class, "银行秒付(后台)");
			if(cons!=null && cons.getValue().equals("0")){
				out.println("银行秒付(后台)暂停");
				return null;
			}
			
			String msg = proposalService.auditS(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功，已提交秒付");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String auditSProposal2() {
		try {
			
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("提案审核");
		try {
			out = this.getResponse().getWriter();
//			Const cons = (Const) userDao.get(Const.class, "民生银行秒付");
//			if(cons!=null && cons.getValue().equals("0")){
//				out.println("民生银行秒付暂停");
//				return null;
//			}
			Const cons = (Const) userDao.get(Const.class, "银行秒付(后台)");
			if(cons!=null && cons.getValue().equals("0")){
				out.println("银行秒付(后台)暂停");
				return null;
			}
			
			String msg = proposalService.auditS(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功，已提交秒付");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String auditMsProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("提案审核");
		try {
			String msg = proposalService.auditMsProposal(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String excuteCommission() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("执行佣金记录");
		try {
			// String msg = proposalService.audit(jobPno,
			// getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			String msg = proposalService.excuteCommission(loginname, getOperatorLoginname(), year, month, remark);
			;
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("执行成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("执行失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("执行失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String auditBusinessProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("事务提案审核");
		try {
			String msg = proposalService.auditBusinessProposal(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String excuteicbctransfer() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("网上银行转账补单");
		try {
			String msg = proposalService.excuteicbctransfer(id, loginname, getOperatorLoginname());
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("补单成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("补单失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("补单失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String excutecmbtransfer() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("网上银行招行转账补单");
		try {
			String msg = proposalService.excutecmbtransfer(id, loginname, getOperatorLoginname());
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("补单成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("补单失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("补单失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	
	
	public String excuteAlipaytransfer() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("支付宝转账补单");
		try {
			String msg = proposalService.excuteAlipaytransfer(transID, loginname, getOperatorLoginname());
			out = this.getResponse().getWriter();
			if (msg == null) {
				out.println("补单成功");
			} else {
				out.println("补单失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("补单失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}


	
	
	public String excuteabctransfer() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("网上银行农行转账补单");
		if(loginname.length()!=4){
			out.println("补单失败:请输入该用户的4位随机数!");
			out.flush();
			if (out != null) {
				out.close();
			}
			return null;
		}
		try {
			String msg = proposalService.excuteabctransfer(id, loginname, getOperatorLoginname());
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("补单成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("补单失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("补单失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String supplementPayOrder() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("额度验证存款补单");
		if(!(StringUtil.isNotEmpty(loginname) && StringUtil.isNotEmpty(amount))){
			out.println("额度验证存款补单失败.参数非法");
			out.flush();
			if (out != null) {
				out.close();
			}
			return null;
		}
		try {
			String msg = proposalService.supplementPayOrder(id, loginname, Double.valueOf(amount), getOperatorLoginname());
			out = this.getResponse().getWriter();
			if (msg == null) {
				out.println("补单成功");
			} else {
				out.println("补单失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("补单失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	
	public String cancleProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			String msg = proposalService.cancle(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			out = this.getResponse().getWriter();
			if (msg == null)
				out.println("取消成功");
			else
				out.println("取消失败:" + msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("取消失败:" + e.getMessage());
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String cancleBusinessProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			String msg = proposalService.cancleBusinessProposal(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			out = this.getResponse().getWriter();
			if (msg == null)
				out.println("取消成功");
			else
				out.println("取消失败:" + msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("取消失败:" + e.getMessage());
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	/*
	 * 存款自动审核查询
	 */
	public String cashinAutoExecute() {

		DetachedCriteria dc = DetachedCriteria.forClass(CashinRecord.class);

		if (null == code || code.equals("")) {
			setErrormsg("确认码不可为空");
			return INPUT;
		}
		if (StringUtils.isNotEmpty(code)) {
			dc.add(Restrictions.eq("code", code));
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		if (page.getPageContents() == null || page.getPageContents().size() == 0) {
			setErrormsg("无匹配记录");
		}
		return INPUT;
	}

	/*
	 * 存款自动审核确认
	 */
	public String cashinExecute() {
		try {
			String msg = proposalService.cashinExcute(jobPno, getOperatorLoginname(), getIp());
			if (msg == null)
				println("执行成功");
			else
				println("执行失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			println("执行失败:" + e.getMessage());
		}
		return null;
	}

	/*
	 * 存款自动审核取消
	 */
	public String cashinCancel() {
		try {
			String msg = proposalService.cashinCancel(jobPno, getOperatorLoginname(), getIp(), remark);
			if (msg == null)
				println("执行成功");
			else
				println("执行失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			println("执行失败:" + e.getMessage());
		}
		return null;
	}

	public String enableUser() {
		if (loginname == null || loginname.equals("")) {
			println("操作错误");
			return null;
		}
		try {
			loginname = new String(loginname.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg = operatorService.EnableUser(loginname, isEnable.booleanValue(), getOperatorLoginname(), remark);
		if (msg == null){
			
			try {
				if(!isEnable){ 
					
					RedisUtil.delete("ld_" + loginname.toLowerCase());//禁用玩家，app踢下线
				}
			} catch (Exception e) {
				
				log.error("app clear token error:" + e.getMessage());
			}
			
			println("操作成功");
		}
		else{
			
			println(msg);
		}
		return null;
	}

	public String editRateUser() {
		if (loginname == null || loginname.equals("")) {
			println("操作错误");
			return null;
		}
		try {
			loginname = new String(loginname.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String msg = operatorService.editRateUser(loginname, getOperatorLoginname(), rate, getRequest().getParameter("platform"));
		if (msg == null)
			println("操作成功");
		else
			println(msg);
		return null;
	}

	public String editUser() {
		if (loginname == null || loginname.equals("")) {
			println("操作错误");
			return null;
		}
		if (intro == null || "".equals(intro)) {
			println("推荐码不允许为空！");
			return null;
		}
		try {
			loginname = new String(loginname.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg = operatorService.editUser(loginname, getOperatorLoginname(), intro);
		if (msg == null)
			println("操作成功");
		else
			println(msg);
		return null;
	}
	
	
	public String editAgentPartner() {
		if (loginname == null || loginname.equals("")) {
			println("操作错误");
			return null;
		}
		if (partner == null || "".equals(partner)) {
			println("推荐码不允许为空！");
			return null;
		}
		try {
			loginname = new String(loginname.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg = operatorService.editAgentPartner(loginname, getOperatorLoginname(), partner);
		if (msg == null)
			println("操作成功");
		else
			println(msg);
		return null;
	}


	@SuppressWarnings("unused")
	private void println(String msg) {
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			out.println(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		} finally {
			if (out != null)
				out.close();
		}

	}
	public void findUserMaintainLogList() {

		List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String sql = "select id,loginname,content,create_time,create_user,update_time,update_user,type from user_maintain_log where loginname = ? order by create_time desc";

		Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();

		Query query = session.createSQLQuery(sql).setString(0, loginname);

		List list = query.list();

		if (!list.isEmpty()) {

			for (int i = 0; i < list.size(); i++) {

				Object[] arr = (Object[]) list.get(i);

				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("id", arr[0]);
				map.put("loginname", arr[1]);
				map.put("content", arr[2]);
				Timestamp ts1 = (Timestamp) arr[3];
				String tsStr1 = sdf.format(ts1);
				map.put("createTime", tsStr1);
				map.put("createUser", arr[4]);
				Timestamp ts2 = (Timestamp) arr[5];
				String tsStr2 = sdf.format(ts2);
				map.put("updateTime", tsStr2);
				map.put("updateUser", arr[6]);
				map.put("type", arr[7]);

				resultList.add(map);
			}
		}

		if (null != session) {

			session.close();
		}

		GsonUtil.GsonObject(resultList);
	}

	public String queryJLKList() {

		DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);

		dc.add(Restrictions.like("payPlatform", "jlk", MatchMode.ANYWHERE));

		if (StringUtils.isNotEmpty(billno)) {

			dc.add(Restrictions.eq("billno", billno));

			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);

			getRequest().setAttribute("page", page);

			return INPUT;
		}

		if (payOrderFlag != null) {

			dc.add(Restrictions.eq("type", payOrderFlag));
		}

		if (StringUtils.isNotEmpty(loginname)) {

			dc.add(Restrictions.eq("loginname", loginname));
		}

		if (startTime != null) {

			dc.add(Restrictions.ge("createTime", startTime));
		}

		if (endTime != null) {

			dc.add(Restrictions.lt("createTime", endTime));
		}

		Order o = null;

		if (StringUtils.isNotEmpty(by)) {

			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "money", null, null, o);

		getRequest().setAttribute("page", page);

		return INPUT;
	}

	public void queryJLKOrder() throws Exception {

		Session session = customerService.getHibernateTemplate().getSessionFactory().openSession();

		Payorder payorder = (Payorder) session.get(Payorder.class, billno);

		String returnResult = JLKUtil.queryOrder(billno, payorder.getMd5info());

		JSONObject jsonObject = JSONObject.fromObject(returnResult);
		log.info("jsonObject的值为：" + jsonObject.toString());

		String return_code = jsonObject.getString("return_code");
		String return_data = jsonObject.getString("return_data");

		if ("success".equals(return_code)) {

			ObjectMapper mapper = new ObjectMapper();

			Map<String, Object> dataMap = mapper.readValue(return_data, Map.class);

			GsonUtil.GsonObject(dataMap);
		}

		if (null != session) {

			session.close();
		}
	}

	public void remedyJLKOrder() throws Exception {

		String out_trade_no = getRequest().getParameter("out_trade_no");
		String coin_total = getRequest().getParameter("coin_total");
		String status = getRequest().getParameter("status");
		String trade_no = getRequest().getParameter("trade_no");
		String coin_type = getRequest().getParameter("coin_type");

		String url = config.getString("middleservice.url.pay") + "/jlk/scp_cb";

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("rand", JLKUtil.getRandomString(16));
		param.put("out_trade_no", out_trade_no);
		param.put("trade_no", trade_no);
		param.put("coin_type", coin_type);
		param.put("coin_total", coin_total);
		param.put("status", status);

		String asciiStr = JLKSHA256.unionpayASCII(param) + "&mch_secret=" + JLKUtil.PRO_MCH_SECRET;
		log.info("asciiStr的值为：" + asciiStr);

		String signature = JLKSHA256.sha256_HMAC(asciiStr, JLKUtil.PRO_MCH_SECRET).toUpperCase();
		log.info("signature的值为：" + signature);

		param.put("sign", signature);

		String msg = JLKUtil.post(url, param);

		GsonUtil.GsonObject(msg);
	}

	public void addUserMaintainLog() {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String operatename = getOperatorLoginname();

		if (StringUtils.isBlank(operatename)) {

			resultMap.put("code", "20000");
			resultMap.put("message", "请登录后再进行操作！");

			GsonUtil.GsonObject(resultMap);
		} else {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());

			String sql = "insert into user_maintain_log(loginname,content,create_time,create_user,update_time,update_user,type) values(?,?,?,?,?,?,?)";

			Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createSQLQuery(sql);

			query.setString(0, loginname);
			query.setString(1, content);
			query.setString(2, time);
			query.setString(3, operatename);
			query.setString(4, time);
			query.setString(5, operatename);
			query.setInteger(6, 1);


			query.executeUpdate();

			if (null != session) {

				session.close();
			}

			resultMap.put("code", "10000");
			resultMap.put("message", "数据添加成功！");

			GsonUtil.GsonObject(resultMap);
		}
	}

	public void updateUserMaintainLog() {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String operatename = getOperatorLoginname();

		if (StringUtils.isBlank(operatename)) {

			resultMap.put("code", "20000");
			resultMap.put("message", "请登录后再进行操作！");

			GsonUtil.GsonObject(resultMap);
		} else {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());

			String sql = "update user_maintain_log set content = ?, update_time = ?, update_user = ? where id = ?";

			Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createSQLQuery(sql);

			query.setString(0, content);
			query.setString(1, time);
			query.setString(2, operatename);
			query.setInteger(3, id);

			query.executeUpdate();

			if (null != session) {

				session.close();
			}

			resultMap.put("code", "10000");
			resultMap.put("message", "数据修改成功！");

			GsonUtil.GsonObject(resultMap);
		}
	}

	public void updateUserRemark() {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String sql = "update users set user_remark = ? where loginname = ?";

		Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();

		Query query = session.createSQLQuery(sql);

		query.setString(0, remark);
		query.setString(1, loginname);

		query.executeUpdate();

		if (null != session) {

			session.close();
		}

		resultMap.put("code", "10000");
		resultMap.put("message", "数据修改成功！");

		GsonUtil.GsonObject(resultMap);
	}

	public String excuteProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			String msg = proposalService.excute(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark), StringUtil.trim(bankaccount), fee);
			out = this.getResponse().getWriter();
			if (msg == null)
				out.println("执行成功");
			else
				out.println("执行失败:" + msg);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("执行失败:" + e.getMessage());
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	/**
	 * 手工再存优惠执行完成
	 * @return
	 */
	public String confirmOfferFinished(){
		PrintWriter out = null; 
		try {
			proposalService.excuteOffer(jobPno, getOperatorLoginname(), getIp());
			out = this.getResponse().getWriter();
			out.println("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
		return null;
	}
	
	public String secondProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("提案审核");
		try {
			String msg = proposalService.secondProposal(jobPno, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String getAccountCity() {
		return accountCity;
	}

	public String getAccountInfo() {
		if (loginname != null) {
			Accountinfo accountinfo = (Accountinfo) operatorService.get(Accountinfo.class, loginname);
			if (accountinfo != null) {
				setAccountCity(accountinfo.getAccountCity());
				setAccountName(accountinfo.getAccountName());
				setAccountNo(accountinfo.getAccountNo());
				setAccountType(accountinfo.getAccountType());
				setBank(accountinfo.getBank());
				setBankAddress(accountinfo.getBankAddress());
			}
		}
		return INPUT;
	}

	public String getAccountInfoForCashout() {
		if (loginname != null) {
			Users user = (Users) operatorService.get(Users.class, loginname);
			if (user != null) {
				setPhone(user.getPhone());
				setEmail(user.getEmail());
				setAccountName(user.getAccountName());
				getRequest().setAttribute("credit", NumericUtil.formatDouble(user.getCredit()));
			}
		}
		return INPUT;
	}

	public String getAccountInfoForChangebankCredit() {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		if (loginname != null && !loginname.equals("")) {
			dc.add(Restrictions.eq("username", loginname));
			List<Bankinfo> bankinfos = operatorService.findByCriteria(dc);
			Bankinfo bankinfo = null;
			try {
				if (bankinfos.size() != 0) {
					bankinfo = bankinfos.get(0);
					getRequest().setAttribute("credit", NumericUtil.formatDouble(bankinfo.getAmount()));
					// bankinfo.setAmount(bankinfo.getAmount()+amount);
					// saveorupdate(bankinfo);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		// if (loginname != null) {
		// Users user = (Users) operatorService.get(Users.class, loginname);
		// if (user != null) {
		// setPhone(user.getPhone());
		// setEmail(user.getEmail());
		// setAccountName(user.getAccountName());
		// getRequest().setAttribute("credit",
		// NumericUtil.formatDouble(user.getCredit()));
		// }
		// }
		return INPUT;
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

	public String getAmount() {
		return amount;
	}

	public String getBank() {
		return bank;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public String getBillno() {
		return billno;
	}

	public String getBy() {
		return by;
	}

	public String getContent() {
		return content;
	}

	public String getCorpBankAccount() {
		return corpBankAccount;
	}

	public String getCorpBankName() {
		return corpBankName;
	}

	public String getCreditLogType() {
		return creditLogType;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public String getDownLmit() {
		return downLmit;
	}

	public String getEmail() {
		return email;
	}

	public Date getEnd() {
		return end;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public Double getFirstCash() {
		return firstCash;
	}

	public Integer getId() {
		return id;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public Integer getIsTransferIn() {
		return isTransferIn;
	}

	public String getJobPno() {
		return jobPno;
	}

	public Integer getLevel() {
		return level;
	}

	public String getLoginname() {
		return loginname;
	}

	public String getMsg() {
		return Msg;
	}

	public NetpayService getNetpayService() {
		return netpayService;
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public OperatorService getOperatorService() {
		return operatorService;
	}

	public String getOrder() {
		return order;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public String getPassword() {
		return password;
	}

	public String getPayBankAccount() {
		return payBankAccount;
	}

	public String getPayBankName() {
		return payBankName;
	}

	public Integer getPayOrderFlag() {
		return payOrderFlag;
	}

	public String getPayType() {
		return payType;
	}

	public String getPhone() {
		return phone;
	}

	public String getPhoneNum() {
		return PhoneNum;
	}

	public String getPno() {
		return pno;
	}

	public Integer getProposalFlag() {
		return proposalFlag;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public String getProposalType() {
		return proposalType;
	}

	public Double getRate() {
		return rate;
	}

	public String getRemark() {
		return remark;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public SeqService getSeqService() {
		return seqService;
	}

	public Integer getSize() {
		return size;
	}

	public Date getStart() {
		return start;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTitle() {
		return title;
	}

	public Integer getTransferFalg() {
		return transferFalg;
	}

	public TransferService getTransferService() {
		return transferService;
	}

	public String getTransID() {
		return transID;
	}

	public Double getTryCredit() {
		return tryCredit;
	}

	public String getType() {
		return type;
	}

	// 得以会员基本信息
	public String getUserInfo() {
		if (StringUtil.isNotBlank(loginname) && !loginname.trim().equals("admin")) {
			Users user = (Users) slaveService.get(Users.class, loginname);
			if (user != null)
//				if(user.getRole() !=null && user.getRole().equals("AGENT")){
//					int countProxyFirst = slaveService.getCountProxyFirst(loginname,countProxyFirst_start,countProxyFirst_end);
//					getRequest().setAttribute("countProxyFirst", countProxyFirst);
//				}


				getRequest().setAttribute("user", user);
			
			    if (user.getBirthday()!=null) {
			        getRequest().setAttribute("birthday",DateUtil.fmtDateForBetRecods(user.getBirthday()));
			    }
		}

		return SUCCESS;
	}
	
	//首存数
	public String getUserInfoWithSCSH() {
		if (loginname != null && !loginname.trim().equals("admin")) {
			Users user = (Users) slaveService.get(Users.class, loginname);
			if (user != null)
				/*if(user.getRole() !=null && user.getRole().equals("AGENT")){
					int countProxyFirst = slaveService.getCountProxyFirst(loginname,countProxyFirst_start,countProxyFirst_end);
					getRequest().setAttribute("countProxyFirst", countProxyFirst);
				}*/
				getRequest().setAttribute("user", user);
		}

		return SUCCESS;
	}
	
	public String getUserInfoBackup() {
		if (loginname != null && !loginname.trim().equals("admin")) {
			UsersBackup user = (UsersBackup) operatorService.get(UsersBackup.class, loginname);
			if (user != null)
				getRequest().setAttribute("user", user);
		}

		return SUCCESS;
	}
	
	//   代理基本信息
	public String getAgentInfo() {
		if (loginname != null && !loginname.trim().equals("admin")) {
			Users user = (Users) operatorService.get(Users.class, loginname);
			if (user != null && user.getRole().equals(UserRole.AGENT.getCode())){
				getRequest().setAttribute("user", user);
				InternalAgency inAgency = (InternalAgency) operatorService.get(InternalAgency.class, loginname);
				getRequest().setAttribute("inAgency", inAgency);
				
				Userstatus userstatus = (Userstatus) operatorService.get(Userstatus.class, loginname);
				
				if (null == userstatus) {
										
					userstatus = new Userstatus();
				}
										
				if (StringUtils.isBlank(userstatus.getCommission())) {
										
					userstatus.setCommission("0.3");
				}
									
				getRequest().setAttribute("userstatus", userstatus);
			}else{
				
			}
		}

		return SUCCESS;
	}
	
	// 得以会员基本信息
	public String updateRebate() {
		if (loginname == null || loginname.equals("")) {
			setErrormsg("用户不能为空！");
			return INPUT;
		}
		if (earebate == null || earebate.equals("")) {
			setErrormsg("ea优惠不能为空！");
			return INPUT;
		}
		
		if (bbinrebate == null || bbinrebate.equals("")) {
			setErrormsg("bbin优惠不能为空！");
			return INPUT;
		}
		
		if (agrebate == null || agrebate.equals("")) {
			setErrormsg("ag优惠不能为空！");
			return INPUT;
		}
		
		if (aginrebate == null || aginrebate.equals("")) {
			setErrormsg("agin优惠不能为空！");
			return INPUT;
		}
		
		if (kenorebate == null || kenorebate.equals("")) {
			setErrormsg("keno优惠不能为空！");
			return INPUT;
		}
		
		if (sbrebate == null || sbrebate.equals("")) {
			setErrormsg("sb优惠不能为空！");
			return INPUT;
		}
		
//		if (ptrebate == null || ptrebate.equals("")) {
//			setErrormsg("pt优惠不能为空！");
//			return INPUT;
//		}
		
		Users user = (Users) operatorService.get(Users.class, loginname);
		if (user != null){
			user.setEarebate(earebate);
			user.setBbinrebate(bbinrebate);
			user.setAgrebate(agrebate);
			user.setAginrebate(aginrebate);
			user.setKenorebate(kenorebate);
			user.setSbrebate(sbrebate);
			user.setPtrebate(ptrebate);
			operatorService.update(user);
			setErrormsg("更新成功！");
			getRequest().setAttribute("user", user);
		}else{
			setErrormsg("用户不存在！");
			return INPUT;
		}
		return INPUT;
	}
	

	// 得以会员基本信息
	public String getUserInfoById() {
		if (id != null && !loginname.trim().equals("admin")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			if (id != null)
				dc = dc.add(Restrictions.eq("id", id));
//			dc = dc.addOrder(Order.asc("createtime"));
			Order o = Order.asc("createtime");
			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
			if (page.getPageContents().size() > 0) {
				getRequest().setAttribute("user", (Users) page.getPageContents().get(0));
			}
		}

		return SUCCESS;
	}

	// 得以会员基本信息
	// public String getUserEmail() {
	// if (loginname!=null&&!loginname.trim().equals("admin")) {
	// Users user = (Users) operatorService.get(Users.class, loginname);
	// if (user != null){
	// List<Users> list = new ArrayList<Users>();
	// list.add(user);
	// getHttpSession().setAttribute("list", list);
	// }
	//				
	// }
	//		
	// return SUCCESS;
	// }

	// // 得到全部会员邮箱
	// public String getUsersEmail() {
	// DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
	// List<Users> list=null;
	// if(roleCode ==null||roleCode.trim().equals("")){
	// return SUCCESS;
	// }
	// if(roleCode !=null&&!roleCode.trim().equals("")){
	// dc.add(Restrictions.eq("role", roleCode));
	// }
	// if (loginname!=null&&!loginname.trim().equals("")){
	// dc.add(Restrictions.ilike("loginname", loginname, MatchMode.ANYWHERE));
	// }
	// dc.add(Restrictions.eq("flag", 0));
	// dc.add(Restrictions.isNotNull("email"));
	// //dc.add(Restrictions.isNotNull(email));
	// list = operatorService.findByCriteria(dc);
	// // Users user =new Users();
	// // String emailString="";
	// // for(int i=0;i<users.size();i++){
	// // Users users2 =users.get(i);
	// // if(users2.getEmail()!=null&&!users2.getEmail().trim().equals("")){
	// // if(i==0){
	// // emailString=users2.getEmail();
	// // }else{
	// // emailString=emailString+","+users2.getEmail();
	// // }
	// // }
	// //
	// // }
	// //user.setEmail(emailString);
	// getHttpSession().setAttribute("list",list);
	//		
	//		
	// return SUCCESS;
	// }

	public String getValidateCode() {
		return validateCode;
	}

	public void getSite_key(){
		this.referWebsite=this.getRequest().getServerName();
		//String domain = getDomainForUrl(referWebsite);
		String domain = referWebsite;
		DetachedCriteria dc = DetachedCriteria.forClass(ReCaptchaConfig.class);
		dc = dc.add(Restrictions.eq("status", "1"));
		dc = dc.add(Restrictions.eq("domain", domain));
		List resultList = proposalService.getHibernateTemplate().findByCriteria(dc);
		if(resultList == null || resultList.size()==0){
			GsonUtil.GsonObject("");
			return ;
		}
		ReCaptchaConfig  re = (ReCaptchaConfig) resultList.get(0);
		GsonUtil.GsonObject(re.getSite_key());
	}	
	
	public String officeLogin() {
		if (getHttpSession().getAttribute(Constants.SESSION_OPERATORID) != null)
			return SUCCESS;
		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		String erand = (String) getHttpSession().getAttribute(Constants.SESSION_EMAIL_RANDID);
		if (!validateCode.equalsIgnoreCase(rand)) {
			addFieldError("wwctrl", "验证码错误");
			getHttpSession().removeAttribute(Constants.SESSION_RANDID);
			return INPUT;
		}

		if(constDao.isOpenEmail()){
			if (erand==null||"".equals(erand) || !mailCode.equalsIgnoreCase(erand)) {
				addFieldError("wwctrl", "邮箱验证码错误");
				return INPUT;
			}
		}

		
//		if(DateUtil.nowIsMonday()){
			try {
				String result =  operatorService.judgeWetherToChangePwd(loginname) ;
				if(null == result){
					addFieldError("wwctrl", "用户不存在");
					return INPUT ;
				}
				if(result.equals("changePwd")){
					getHttpSession().setAttribute(Constants.SESSION_OPERATORNAME, loginname);
					return result ;
				}
				
				Operator op = (Operator) operatorService.get(Operator.class, loginname);
				if(op == null){
					addFieldError("wwctrl", "用户不存在");
					return INPUT;
				}
				if(op.getValidType() == null){
					addFieldError("wwctrl", "您的账号未指定验证类型，请先修改指定验证类型");
					return INPUT;
				}
				if(op.getValidType() == 1){//1,短信验证，2，打卡验证，3无需验证
					String msmValidMsg = this.validateSms(op, this.smsValidPwd);
					if(msmValidMsg != null){
						addFieldError("wwctrl", msmValidMsg);
						return INPUT;
					}
				} else if(op.getValidType() == 2) {
					if(StringUtils.isEmpty(op.getEmployeeNo())){
						addFieldError("wwctrl", "根据您的安全验证类型，员工编码为空，不能登录系统！");
						return INPUT;
					}
					String rsMsg = CheckInOutUtil.checkData(op.getEmployeeNo());
					if(!"".equals(rsMsg)){
						addFieldError("wwctrl", rsMsg);
						return INPUT;
					}
				} else if(op.getValidType() != 3) {
					addFieldError("wwctrl", "您的账号指定验证类型不存在，请修改指定验证类型");
					return INPUT;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
//		}
		
		String msg = operatorService.login(loginname, password, getIp());
		if (msg != null)
			addFieldError("wwctrl", msg);
		else {
			Operator operator = (Operator) operatorService.get(Operator.class, loginname);
			getHttpSession().setAttribute(Constants.SESSION_OPERATORID, operator);
			String empNo = operator.getEmployeeNo();
			String phoneno = operator.getPhoneno();
			if (StringUtils.isEmpty(empNo) && StringUtils.isEmpty(phoneno)) {
				setErrormsg("登陆后请先在左下角绑定员工编号，谢谢"); 
			}
			return SUCCESS;
		}
		return INPUT;
	}
	
	private String validateSms(Operator op, String smsValidPwd) {
		if(smsValidPwd == null){
			return "请输入短信验证码";
		}
		if(op.getSmsPwd() == null || op.getSmsUpdateTime() == null){
			return "请重新发送验证码";
		}
		Date limit = new Date(op.getSmsUpdateTime().getTime() + 10*60*1000);
		Date now = new Date();
		if(limit.compareTo(now) < 0){
			op.setSmsPwd(null);
			op.setSmsUpdateTime(null);
			this.operatorService.update(op);
			return "您短信验证码超时，有效时间为10分钟";
		}
		if(!op.getSmsPwd().equalsIgnoreCase(smsValidPwd)){
			return "您输入的短信验证码有误";
		}
		
		return null;
	}
	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();
	/**
	 * 短信验证码
	 * */
	public void sendSmsPwd(){
		
		if (getHttpSession().getAttribute(Constants.SESSION_OPERATORID) != null)
			return;
        
		String smsPwd = StringUtil.getRandomNumString(6);
		Operator op = (Operator) this.operatorService.get(Operator.class, this.loginname);
		
		if(op.getEnabled() == 1){
			GsonUtil.GsonObject("您的账号已被锁定，不能发送短信");
			return;
		}
		if(op.getValidType() == null || op.getValidType() != 1){
			GsonUtil.GsonObject("您的账号安全验证类型不需要发送短信");
			return;
		}
		if(StringUtils.isBlank(op.getPhoneno())){
			GsonUtil.GsonObject("您的手机号为空，不能发送短信");
			return;
		}
		
		Long nowTiem = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            String keyStr = entry.getKey();
            Long longTime = cacheMap.get(keyStr);
            if (nowTiem - longTime > 1000 * 60) {
                it.remove();
            }
        }
        if (cacheMap.get(this.loginname) != null) {
            GsonUtil.GsonObject("60秒内不能重复发送。");
            return;
        }
        cacheMap.put(this.loginname, nowTiem);
        
		op.setSmsPwd(smsPwd);
		op.setSmsUpdateTime(DateUtil.convertToTimestamp(new Date()));
		this.operatorService.update(op);
		
		log.info("用户：" + loginname + "，发送短信手机号：" + op.getPhoneno() + "，准备发送短信验证码：" + smsPwd);
		if(SmsPwdUtils.sendSms(op.getPhoneno(),smsPwd)){
			GsonUtil.GsonObject("发送成功，请注意查收");
			log.info("验证码发送成功");
		} else {
			GsonUtil.GsonObject("发送失败，请重新操作");
			log.info("验证码发送失败");
			cacheMap.remove(this.loginname);
		}
	}
	
	public void validSmsType(){
		
		if(this.loginname != null){
			Operator op = (Operator) this.operatorService.get(Operator.class, this.loginname);
			if(op != null && op.getValidType() !=null && op.getValidType() == 1){
				GsonUtil.GsonObject("success");
				return;
			}
		}
		
		GsonUtil.GsonObject("false");
	}
	/**
	 * 二维码
	 * @return
	 */
	private String  recommendCode;

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public String queryQrcode() {
		DetachedCriteria dc = DetachedCriteria.forClass(Qrcode.class);
		if ( StringUtils.isNotEmpty(agent) ) {
			dc = dc.add(Restrictions.eq("agent", agent));
		}
		if ( StringUtils.isNotEmpty(recommendCode) ) {
			dc = dc.add(Restrictions.eq("recommendCode", recommendCode));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size,o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public void deleteQrcode(){
		log.info("id:"+id);
		proposalService.delete(Qrcode.class, id);
		PrintWriter out=null;
		try {
			out =this.getResponse().getWriter();
			out.println("删除成功");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
	}



	public String addQrcode() {
		try {
			if ( (agent != null && "".equals(agent.trim()) == false)  &&  (recommendCode != null && "".equals(recommendCode.trim()) == false)) {
				setErrormsg("代理名称和推荐码不能同时填入");
				return INPUT;
			}
			if (StringUtils.isEmpty(address)) {
				setErrormsg("网址不能为空");
				return INPUT;
			}
			String operator = getOperatorLoginname();
			String msg =proposalService.addQrcode(agent,recommendCode,address,operator,remark,qrcode);
			if (msg == null) {
				setErrormsg("提交成功" );
				return SUCCESS;
			}
			else {
				setErrormsg("提交失败:" + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String getQrcodeConfig(){
		weiXcode = (Qrcode) operatorService.get(Qrcode.class, id) ;
		return SUCCESS ;
	}
	public String updateQrcodeConfig(){
		String operator = getOperatorLoginname();
		weiXcode = (Qrcode) operatorService.get(Qrcode.class, id) ;
		weiXcode.setRemark(remark);
		weiXcode.setQrcode(qrcode);
		weiXcode.setUpdateoperator(operator);
		weiXcode.setAgent(agent);
		weiXcode.setRecommendCode(recommendCode);
		weiXcode.setAddress(address);
		try {
			operatorService.update(weiXcode);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("修改失败:" + e.getMessage());
		}

		GsonUtil.GsonObject("修改成功");
		return null ;
	}


	public String queryActionLog() {
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(op == null){
			GsonUtil.GsonObject("会话已失效，请重新登录");
			return INPUT;
		}
		String auth = op.getAuthority();
		List authList = Arrays.asList(new String[] {"boss","admin","finance_manager","sale_manager","market_manager","vc","finance","finance_leader","qc","card"});
		if(authList.contains(auth)) {
			getRequest().setAttribute("authStatus", 1);//可以看到所有事件
		}else {
			getRequest().setAttribute("authStatus", 0);
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Actionlogs.class);
		if (StringUtils.isNotEmpty(type))
			dc = dc.add(Restrictions.eq("action", type));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		if(StringUtils.isNotBlank(deviceID)){
			dc.add(Restrictions.like("remark", deviceID, MatchMode.ANYWHERE));
		}
//		dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryActivity() {
		DetachedCriteria dc = DetachedCriteria.forClass(Activity.class);
		if (StringUtils.isNotEmpty(activityName)){
			dc.add(Restrictions.like("activityName", "%"+activityName+"%"));
		}
		if(type!=null && !type.equals("")){
			dc.add(Restrictions.eq("activityStatus", Integer.parseInt(type)));
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createDate", end));
//		dc = dc.addOrder(Order.desc("createDate"));
		Order o = Order.desc("createDate");
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String updateActivity(){
		activity = (Activity) operatorService.get(Activity.class, id);
		return INPUT;
	}
	
	public String updageActivityStatus(){
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(id==null){
				out.println("id不能为空！");
				return null;
			}
			if(type==null || type.equals("")){
				out.println("状态不能为空！");
				return null;
			}
			activity = (Activity) operatorService.get(Activity.class, id);
			activity.setActivityStatus(Integer.parseInt(type));
			String msg = proposalService.updateActivityStatus(activity);
			if (msg != null){
				out.println("提交失败:" + msg);
			}else{
				out.println("提交成功");
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("提交失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	
	public String updateActivityRole(){
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(id==null){
				out.println("id不能为空！");
				return null;
			}
			if(type==null || type.equals("")){
				out.println("状态不能为空！");
				return null;
			}
			activity = (Activity) operatorService.get(Activity.class, id);
			String userroleOld=activity.getUserrole();
			if(userroleOld==null){
				userroleOld="";
			}
			if(userroleOld.contains(type)){
				userroleOld=userroleOld.replace(type, "");
				activity.setUserrole(userroleOld);
			}else{
				activity.setUserrole(userroleOld+type+"");
			}
			String msg = proposalService.updateActivityStatus(activity);
			if (msg != null){
				out.println("提交失败:" + msg);
			}else{
				out.println("提交成功");
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("提交失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	

	public String queryBankInfo() {
		Accountinfo accountinfo = (Accountinfo) operatorService.get(Accountinfo.class, loginname);
		if (accountinfo != null)
			getRequest().setAttribute("bankinfo", accountinfo);
		else
			addFieldError("errorMessage", "找不到该用户的会员资料");
		return SUCCESS;
	}

	public String queryBetRecord() {
		DetachedCriteria dc = DetachedCriteria.forClass(Betrecords.class);
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("passport", loginname));
		if (start != null)
			dc = dc.add(Restrictions.ge("billTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("billTime", end));
		if (StringUtil.isNotEmpty(gmCode))
			dc = dc.add(Restrictions.eq("gmCode", StringUtil.trim(gmCode)));
		if (StringUtil.isNotEmpty(drawNo))
			dc = dc.add(Restrictions.eq("drawNo", StringUtil.trim(drawNo)));
		if (StringUtil.isNotEmpty(playCode))
			dc = dc.add(Restrictions.eq("playCode", StringUtil.trim(playCode)));

//		dc = dc.addOrder(Order.asc("billTime"));
		Order o = Order.asc("billTime");
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "billAmount", "result", null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryAgTryinfo() {
		DetachedCriteria dc = DetachedCriteria.forClass(AgTryGame.class);
		if (StringUtils.isNotEmpty(agPhone)){
			dc.add(Restrictions.eq("agPhone", agPhone));
		}
		if (StringUtils.isNotEmpty(agName)){
			dc.add(Restrictions.eq("agName", agName));
		}
		if(agIsLogin!=2){
			dc.add(Restrictions.eq("agIsLogin", agIsLogin));
		}
//		dc.addOrder(Order.desc("agRegDate"));
		Order o = Order.desc("agRegDate");
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	/**
	 * 实时投注记录
	 */
	public String nonceQueryBetRecord() {
		if (start == null || end == null)
			setErrormsg("请输入开始时间和结束时间");
		else {
			Page page = new Page();
			try {
				// page.setPageContents(RemoteCaller.getBetRecord(start, end,
				// loginname, null));
				getRequest().setAttribute("page", page);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg(e.getMessage());
			}
		}
		return INPUT;
	}

	/**
	 * 实时额度记录
	 */
	public String nonceBalanceRecords() {
		if (start == null || end == null)
			setErrormsg("请输入开始时间和结束时间");
		else {
			Page page = new Page();
			try {
				// page.setPageContents(RemoteCaller.getBalanceRecords(loginname,
				// start, end));
				getRequest().setAttribute("page", page);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg(e.getMessage());
			}
		}
		return INPUT;
	}

	/**
	 * 会员优惠审核，关于有效投注额 update sun
	 * 
	 * @return
	 */
	public String queryConcessionAudit() {
		// if (StringUtil.isNotEmpty(loginname) && start != null) {
		// if (end == null)
		// end = DateUtil.getOneHourAgo();
		//
		// Users user = (Users) operatorService.get(Users.class, loginname);
		// if (user == null) {
		// setErrormsg("会员不存在");
		// } else {
		// // 所有存款金额，提款金额，优惠金额(首存优惠，洗码优惠)，有效投注金额，盈利金额
		// Double totalDepositAmount = 0.0, totalCashoutAmount = 0.0,
		// totalConcessionAmount = 0.0, totalBetAmount = 0.0, totalProfitAmount
		// = 0.0;
		//
		// // 从提案表来查询
		// DetachedCriteria dcProposal =
		// DetachedCriteria.forClass(Proposal.class).add(Restrictions.ge("createTime",
		// start)).add(Restrictions.lt("createTime", end)).add(
		// Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag",
		// ProposalFlagType.EXCUTED.getCode()));
		// dcProposal.setProjection(Projections.projectionList().add(Projections.groupProperty("type")).add(Projections.sum("amount")));
		// List resultProposal =
		// proposalService.getHibernateTemplate().findByCriteria(dcProposal);
		// for (int j = 0; j < resultProposal.size(); j++) {
		// Object[] array = (Object[]) resultProposal.get(j);
		// Integer type = (Integer) array[0];
		// if (type.intValue() == ProposalType.CASHIN.getCode().intValue())
		// totalDepositAmount += (Double) array[1];
		// if (type.intValue() == ProposalType.CASHOUT.getCode().intValue())
		// totalCashoutAmount += (Double) array[1];
		// if (type.intValue() == ProposalType.CONCESSIONS.getCode().intValue())
		// totalConcessionAmount += (Double) array[1];
		// if (type.intValue() == ProposalType.XIMA.getCode().intValue())
		// totalConcessionAmount += (Double) array[1];
		// }
		//
		// // 从在线支付表来查询
		// DetachedCriteria dcPayOrder =
		// DetachedCriteria.forClass(PayOrder.class).add(Restrictions.ge("createTime",
		// start)).add(Restrictions.lt("createTime", end)).add(
		// Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag",
		// PayOrderFlagType.SUCESS.getCode()));
		// dcPayOrder.setProjection(Projections.sum("money"));
		// List resultPayOrder =
		// proposalService.getHibernateTemplate().findByCriteria(dcPayOrder);
		// Double netpayAmount = (Double) resultPayOrder.get(0);
		// if (netpayAmount != null)
		// totalDepositAmount += netpayAmount;
		//
		// // 从投注记录表来查询
		// // 有效投注额，排除没有开出结果和派彩为0的注单
		// DetachedCriteria dcBetRecord =
		// DetachedCriteria.forClass(BetRecords.class).add(Restrictions.ge("wagersDate",
		// start)).add(Restrictions.lt("wagersDate", end)).add(
		// Restrictions.eq("userName",
		// loginname)).add(Restrictions.sqlRestriction("result!=''")).add(Restrictions.ne("payoff",
		// new Double(0)));
		// dcBetRecord.setProjection(Projections.projectionList().add(Projections.sum("betAmount")).add(Projections.sum("payoff")));
		// List resultBetRecord =
		// proposalService.getHibernateTemplate().findByCriteria(dcBetRecord);
		// Object[] arrayBetRecord = (Object[]) resultBetRecord.get(0);
		// if (arrayBetRecord[0] != null)
		// totalBetAmount += (Double) arrayBetRecord[0];
		// if (arrayBetRecord[1] != null)
		// totalProfitAmount += (Double) arrayBetRecord[1];
		//
		// // 龙虎投注。龙虎游戏中，下注龙、虎而结果为和的情况，按照投注额的一半计算为有效投注额
		// dcBetRecord.add(Restrictions.eq("gameType",
		// GameType.LONGHUMEN.getCode())).add(Restrictions.sqlRestriction("
		// substr(result,1,1)=substr(result,3,1) ")).add(
		// Restrictions.sqlRestriction("payoff=betAmount/-2"));
		// List resultBetRecord2 =
		// proposalService.getHibernateTemplate().findByCriteria(dcBetRecord);
		// Object[] arrayBetRecord2 = (Object[]) resultBetRecord2.get(0);
		// Double skipBetAmount = 0.0, skipProfitAmount = 0.0;
		// skipBetAmount = (Double) (arrayBetRecord2[0] == null ? 0.0 :
		// arrayBetRecord2[0]);
		// skipProfitAmount = (Double) (arrayBetRecord2[1] == null ? 0.0 :
		// arrayBetRecord2[1]);
		// log.info("src totalBetAmount:" + totalBetAmount + ";skipBetAmount:" +
		// skipBetAmount + ";skipProfitAmount:" + skipProfitAmount);
		// totalBetAmount -= skipBetAmount / 2;
		//
		// getRequest().setAttribute("totalDepositAmount", totalDepositAmount);
		// getRequest().setAttribute("totalCashoutAmount", totalCashoutAmount);
		// getRequest().setAttribute("totalBetAmount", totalBetAmount);
		// getRequest().setAttribute("totalProfitAmount", totalProfitAmount);
		// getRequest().setAttribute("totalConcessionAmount",
		// totalConcessionAmount);
		// getRequest().setAttribute("aliasName", user.getAliasName());
		// }
		// } else {
		// setErrormsg("会员帐号,开始时间都不能为空");
		// }
		return INPUT;
	}

	public String queryCreditlogs() {
		DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
		if (StringUtils.isNotEmpty(creditLogType)) {
			dc = dc.add(Restrictions.eq("type", creditLogType));
		}
		if (null != downLmit && !"".equals(downLmit.trim())) {
			Double d = 0.00;
			try {
				d = Double.parseDouble(downLmit.trim());
			} catch (Exception e) {
				e.printStackTrace();
				setMsg("下限额度类型不正确!");
				return INPUT;
			}
			dc = dc.add(Restrictions.ge("remit", d));
		}
		if (StringUtils.isNotEmpty(loginname)) {
			dc = dc.add(Restrictions.eq("loginname", loginname));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null) {
			dc = dc.add(Restrictions.ge("createtime", start));
		}
		if (end != null) {
			dc = dc.add(Restrictions.lt("createtime", end));
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "remit", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryCreditWarnlogs() {
		DetachedCriteria dc = DetachedCriteria.forClass(CreditWarnlogs.class);
		if (StringUtils.isNotEmpty(creditLogType))
			dc = dc.add(Restrictions.eq("type", creditLogType));
		if (null != downLmit && !"".equals(downLmit.trim())) {
			Double d = 0.00;
			try {
				d = Double.parseDouble(downLmit.trim());
			} catch (Exception e) {
				e.printStackTrace();
				setMsg("下限额度类型不正确!");
				return INPUT;
			}
			dc = dc.add(Restrictions.ge("remit", d));
		}
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "remit", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryBankCreditlogs() {
		DetachedCriteria dc = DetachedCriteria.forClass(BankCreditlogs.class);
		if (StringUtils.isNotEmpty(bankCreditChangeType))
			dc = dc.add(Restrictions.eq("type", bankCreditChangeType));
		if (null != downLmit && !"".equals(downLmit.trim())) {
			Double d = 0.00;
			try {
				d = Double.parseDouble(downLmit.trim());
			} catch (Exception e) {
				e.printStackTrace();
				setMsg("下限额度类型不正确!");
				return INPUT;
			}
			dc = dc.add(Restrictions.ge("remit", d));
		}
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("bankname", loginname));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "remit", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryBankonline() {
		DetachedCriteria dc = DetachedCriteria.forClass(IcbcTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtils.isNotEmpty(accountName))
			dc = dc.add(Restrictions.eq("name", accountName));// 记录状态
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("notes", remark, MatchMode.ANYWHERE));// 附言
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if(StringUtils.isNotBlank(acceptName)){
			   dc = dc.add(Restrictions.eq("acceptName", acceptName));
			}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", "fee", null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryCmbBankonline() {
		DetachedCriteria dc = DetachedCriteria.forClass(CmbTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("notes", remark, MatchMode.ANYWHERE));// 附言
		if (StringUtil.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));//用户名
		if (StringUtil.isNotEmpty(username))
			dc = dc.add(Restrictions.eq("uaccountname", username));//姓名
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//
		if (StringUtils.isNotBlank(jylx)) {
			dc.add(Restrictions.like("jylx", jylx, MatchMode.ANYWHERE));
		}
		
		 Integer[] data = {1,2,3,4,22};
		 dc = dc.add(Restrictions.in("payType",data));


		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if(StringUtils.isNotBlank(acceptName)){
			   dc = dc.add(Restrictions.eq("acceptName", acceptName));
			}
		
		if (StringUtils.isBlank(acceptName)) {
			if (StringUtils.isNotBlank(acceptNameTwo)) {
				dc = dc.add(Restrictions.eq("acceptName", acceptNameTwo));
			}
		}
		
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	
	

	/**
	 * 查询云闪付订单记录
	 * @return
	 */
	public String queryYunPayOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));  //订单状态
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(amount)) {
			dc.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}
		dc.add(Restrictions.isNull("spare"));
		dc.add(Restrictions.eq("type", "5"));   
		
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("uaccountname", username));
		}
		
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}



    /*** 查询秒存订单 */
	public String queryCmbTransferline() {

		DetachedCriteria dc = DetachedCriteria.forClass(CmbTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("notes", remark, MatchMode.ANYWHERE));// 附言
		if (StringUtil.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));//用户名
		if (StringUtil.isNotEmpty(username))
			dc = dc.add(Restrictions.eq("uaccountname", username));//姓名
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//

		if (StringUtils.isNotBlank(uaccountno)) {
			dc.add(Restrictions.like("uaccountno", uaccountno, MatchMode.ANYWHERE));// 附言
		}
		
		dc = dc.add(Restrictions.eq("payType", 5));//云闪付
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if (StringUtils.isNotBlank(acceptName)) {
			dc = dc.add(Restrictions.eq("acceptName", acceptName));
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	
	/**
	 * 查询银行二维码订单记录
	 * @return
	 */
	public String queryQrcodePayOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));  //订单状态
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(amount)) {
			dc.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}
		dc.add(Restrictions.isNull("spare"));
		dc.add(Restrictions.eq("type", "6"));   
		
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("uaccountname", username));
		}
		
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}



    
	public String queryCmbTransferQrCodeline() {

		DetachedCriteria dc = DetachedCriteria.forClass(CmbTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("notes", remark, MatchMode.ANYWHERE));// 附言
		if (StringUtil.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));//用户名
		if (StringUtil.isNotEmpty(username))
			dc = dc.add(Restrictions.eq("uaccountname", username));//姓名
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//

		if (StringUtils.isNotBlank(uaccountno)) {
			dc.add(Restrictions.like("uaccountno", uaccountno, MatchMode.ANYWHERE));// 附言
		}
		
		dc = dc.add(Restrictions.eq("payType", 6));
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if (StringUtils.isNotBlank(acceptName)) {
			dc = dc.add(Restrictions.eq("acceptName", acceptName));
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
     
     
     public String reSendTlyOrder(){
 		
 		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
 		
 		if (operator!=null) {

 		    DepositOrder depositOrder = (DepositOrder) operatorService.get(DepositOrder.class, order);
 
 		if (depositOrder!=null) {
 		    DepositOrder newdepositOrder = new DepositOrder();
 		    newdepositOrder.setLoginname(depositOrder.getLoginname());
 		    newdepositOrder.setBankname(depositOrder.getBankname());
 		    newdepositOrder.setDepositId(depositOrder.getDepositId()+"1");
 		    newdepositOrder.setCreatetime(new Date());
 		    newdepositOrder.setStatus(0);
 		    newdepositOrder.setAccountname(depositOrder.getAccountname());
 		    newdepositOrder.setBankno(depositOrder.getBankno());
 		    newdepositOrder.setUbankname(depositOrder.getUbankname());
			newdepositOrder.setUaccountname(depositOrder.getUaccountname());
			newdepositOrder.setRealname(depositOrder.getRealname());
			newdepositOrder.setFlag(1);
			newdepositOrder.setType(depositOrder.getType());   
			newdepositOrder.setAmount(Double.valueOf(amount));

  		     
		   String tlyOrderId=newdepositOrder.getLoginname()+"_7_"+newdepositOrder.getUbankname().split("_")[0]+"_"+newdepositOrder.getDepositId();   //ubankno 臨時處理為選擇微信 還是
		   try {
			
  			   JSONObject json=TlyDepositUtil.sendOrder(tlyOrderId,newdepositOrder.getUaccountname(), newdepositOrder.getUbankname().split("_")[1], "", newdepositOrder.getBankno(), newdepositOrder.getUbankname().split("_")[0],amount, "", new Date());
			if (!json.getBoolean("success")) {
				writeText(json.getString("message"));
				return INPUT;
			}else {
				newdepositOrder.setSpare(String.valueOf(json.getInt("id")));
				newdepositOrder.setAmount(Double.valueOf(amount));
				TlyDepositUtil.revoke_order(depositOrder.getSpare());
			}
			depositOrder.setStatus(2);
 			depositOrder.setRemark("补单强制废除订单,时间:"+ DateUtil.getNow());
 			operatorService.update(depositOrder);
 			operatorService.save(newdepositOrder);
 			
 			
 			writeText("更新成功");
 			
  		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
 			return SUCCESS;
 		
 		}else
 			writeText("單號不存在");
 	   }else
 		   writeText("登入超时,登入后在操作");
 		
 		return INPUT;
 		
 	}
	//采集泛亚电竞记录
	public String fanyaLogData(){
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<1){
			GsonUtil.GsonObject("提示：请您每天的1点后提交") ;
			return SUCCESS;
		}

		try {
			getdateService.fayalogData();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			writeText("采集失败，请联系技术");
		}
		writeText("采集成功，可以上传反水");
		return null ;
	}
	

	/**
	 * 查询同略云订单记录
	 * @return
	 */
	public String queryTlyPayOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));  //订单状态
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(amount)) {
			dc.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}
		dc.add(Restrictions.eq("type", "7"));   
		
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("uaccountname", username));
		}
		
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryCmbTransferTly() {

		DetachedCriteria dc = DetachedCriteria.forClass(CmbTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("notes", remark, MatchMode.ANYWHERE));// 附言
		if (StringUtil.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));//用户名
		if (StringUtil.isNotEmpty(username))
			dc = dc.add(Restrictions.eq("uaccountname", username));//姓名
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//

		if (StringUtils.isNotBlank(uaccountno)) {
			dc.add(Restrictions.like("uaccountno", uaccountno, MatchMode.ANYWHERE));// 附言
		}
		
		dc = dc.add(Restrictions.eq("payType", 7));
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if (StringUtils.isNotBlank(acceptName)) {
			dc = dc.add(Restrictions.eq("acceptName", acceptName));
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 查询支付宝/微信收款订单记录
	 * @return
	 */
	public String querySelfPayOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));  //订单状态
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(amount)) {
			dc.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}
		dc.add(Restrictions.in("type",new String[]{"8","9"}));   
		
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("uaccountname", username));
		}
		
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	public String queryCmbTransferSelf() {

		DetachedCriteria dc = DetachedCriteria.forClass(CmbTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("notes", remark, MatchMode.ANYWHERE));// 附言
		if (StringUtil.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));//用户名
		if (StringUtil.isNotEmpty(username))
			dc = dc.add(Restrictions.eq("uaccountname", username));//姓名
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//

		if (StringUtils.isNotBlank(uaccountno)) {
			dc.add(Restrictions.like("uaccountno", uaccountno, MatchMode.ANYWHERE));// 附言
		}

		dc = dc.add(Restrictions.in("payType", new Integer[]{8,9}));
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if (StringUtils.isNotBlank(acceptName)) {
			dc = dc.add(Restrictions.eq("acceptName", acceptName));
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}





	public void makeUp() {

		HttpClient httpClient = null;
		PostMethod postMethod = null;

		try {

			JSONObject jsonObject = new JSONObject();

			jsonObject.put("method", "get_deposit");
			jsonObject.put("company_key", SHAUtil.apikey);

			JSONObject paramObject = new JSONObject();

			paramObject.put("start", activityBeginTime);
			paramObject.put("end", activityEndTime);

			String sign = SHAUtil.sign(paramObject.toString() + SHAUtil.secretKey, "SHA-256");

			jsonObject.put("data_sign", sign);
			jsonObject.put("data", "\"" + paramObject.toString() + "\"");

			httpClient = createHttpClient();

			postMethod = new PostMethod(SHAUtil.url);
			postMethod.addRequestHeader("content-type", "application/json");

			RequestEntity requestEntity = new StringRequestEntity(jsonObject.toString(), "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);

			int statusCode = httpClient.executeMethod(postMethod);
			log.info("Response statusCode：" + statusCode);

			if (statusCode == HttpStatus.SC_OK) {

				String phpHtml = postMethod.getResponseBodyAsString();
				log.info("Response phpHtml：" + phpHtml);

				if (StringUtils.isNotBlank(phpHtml)) {

					JSONObject json = JSONObject.fromObject(phpHtml);

					if (null != json.get("success") && "true".equalsIgnoreCase(String.valueOf(json.get("success")))) {

						String data = String.valueOf(json.get("data"));

						JSONArray jsonArray = JSONArray.fromObject(data);

						if (null != jsonArray && !jsonArray.isEmpty()) {

							java.util.Date date = new java.util.Date();

							for (int i = 0, len = jsonArray.size(); i < len; i++) {

								Object obj = jsonArray.get(i);
								JSONObject valueObject = JSONObject.fromObject(obj);

								CmbTransfers cmbTransfer = new CmbTransfers();

								cmbTransfer.setAmount(Double.parseDouble(String.valueOf(valueObject.get("amount"))));
								cmbTransfer.setBalance(0.00);
								cmbTransfer.setJylx(getString(valueObject, "client_transtype"));
								cmbTransfer.setNotes(getNoteString(getString(valueObject, "client_postscript")));
								cmbTransfer.setAcceptName(getUserName(getString(valueObject, "deposit_cardnumber")));
								cmbTransfer.setAcceptCardnum(getString(valueObject, "deposit_cardnumber"));
								cmbTransfer.setPayDate(DateUtil.parseDateForStandard(getString(valueObject, "deposit_time")));
								cmbTransfer.setAdminId(0);
								cmbTransfer.setStatus(0);
								cmbTransfer.setDate(date);
								cmbTransfer.setUaccountname(getString(valueObject, "client_accountname"));
								cmbTransfer.setPayType(3);
								cmbTransfer.setOrderNumber(getString(valueObject, "order_number"));

								if (!isExist(cmbTransfer.getOrderNumber())) {

									proposalService.save(cmbTransfer);
								}
							}
						}

						GsonUtil.GsonObject("补录数据成功！补录条数："+jsonArray.size());
					} else {

						GsonUtil.GsonObject(String.valueOf(json.get("error_message")));
					}
				}
			} else {

				GsonUtil.GsonObject("访问接口发生异常，请稍后再试！");
			}
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("补录数据发生异常，请稍后再试！");
		} finally {

			if (postMethod != null) {

				postMethod.releaseConnection();
			}
		}
	}

     public static HttpClient createHttpClient() {

		HttpClient httpclient = new HttpClient();

		HttpClientParams params = new HttpClientParams();

		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 240 * 1000);

		httpclient.setParams(params);

		return httpclient;
	}


 	public String getString(JSONObject jsonObject, String key) {

		String value = "";

		if (null != jsonObject.get(key)) {

			value = String.valueOf(jsonObject.get(key));
		}

		return value;
	}


	public String getNoteString(String note){
		note = note.replaceAll("[\u4e00-\u9fa5]+", "").replace(" " , "");
		return note;
	}


 	public Boolean isExist(String orderNumber) {

		String selectSql = "select count(1) from cmb_transfers where order_number = :ordernumber";
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("ordernumber", orderNumber);

		int num = proposalService.getCount(selectSql, paramsMap);

		if (num > 0) {

			return true;
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	public String getUserName(String bankCard) {

		String selectSql = "select username from bankinfo where bankcard = :bankcard";
		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("bankcard", bankCard);

		try {

			List list = proposalService.getListBySql(selectSql, paramsMap);

			if (null != list && !list.isEmpty()) {

				String str = String.valueOf(list.get(0));

				return String.valueOf(str);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}




	public String getJylx() {
		return jylx;
	}

	public void setJylx(String jylx) {
		this.jylx = jylx;
	}












	public String queryAbcBankonline() {
		DetachedCriteria dc = DetachedCriteria.forClass(AbcTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("jyzy", remark, MatchMode.ANYWHERE));// 附言
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if(StringUtils.isNotBlank(acceptName)){
			   dc = dc.add(Restrictions.eq("acceptName", acceptName));
			}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 查询额度验证存款
	 * @return
	 */
	public String queryValidateAmountDeposit() {
		DetachedCriteria dc = DetachedCriteria.forClass(ValidateAmountDeposit.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtil.isNotEmpty(amount))
			dc = dc.add(Restrictions.eq("amount", Double.parseDouble(amount)));// 额度
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payTime", end));

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", "fee", null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 查询额度验证存款订单
	 * @return
	 */
	public String queryValidatePayOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(PayOrderValidation.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", status));  //订单状态
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("userName", loginname));
		if (StringUtils.isNotEmpty(amount)) {
			dc.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createTime", end));
		// if (newaccount != null)
		// dc = dc.add(Restrictions.eq("newaccount", newaccount));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 作废额度验证存款订单
	 * @return
	 */
	public String discardDepositOrder() {
		log.info(this.getOperatorLoginname() +"废弃额度验证存款订单" + id);
		PayOrderValidation order = (PayOrderValidation) operatorService.getHibernateTemplate().get(PayOrderValidation.class, id);
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(order.getStatus().equals("0")){
				order.setStatus("2");
				order.setRemark(this.getOperatorLoginname() +"废弃额度验证存款订单。" + DateUtil.getNow());
				operatorService.update(order);
			}
			out.println("订单已废弃");
		} catch (Exception e) {
			log.error("废弃额度验证存款订单"+e.getMessage());
			out.println("作废订单失败");
		}
		return null;
	}
	
	public String queryAlipayline() {
		DetachedCriteria dc = DetachedCriteria.forClass(AlipayTransfers.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));// 记录状态
		if (StringUtils.isNotEmpty(paytype))
			dc = dc.add(Restrictions.eq("paytype", Integer.parseInt(paytype)));//
		if (StringUtils.isNotEmpty(accountName))
			dc = dc.add(Restrictions.eq("tradeType", accountName));
		if (StringUtil.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("notes", remark, MatchMode.ANYWHERE));// 附言
		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime.intValue()));//
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("payDate", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("payDate", end));
		if(StringUtils.isNotBlank(acceptName)){
			dc = dc.add(Restrictions.eq("acceptName", acceptName));
		}

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryOperationLog() {
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(op == null){
			GsonUtil.GsonObject("会话已失效，请重新登录");
			return INPUT;
		}
		String auth = op.getAuthority();
		List authList = Arrays.asList(new String[] {"boss","admin","finance_manager","sale_manager","market_manager","vc","finance","finance_leader","qc","card"});
		if(authList.contains(auth)) {
			getRequest().setAttribute("authStatus", 1);//可以看到所有事件
		}else {
			getRequest().setAttribute("authStatus", 0);
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Operationlogs.class);
		if (StringUtils.isNotEmpty(type))
			dc = dc.add(Restrictions.eq("action", type));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(remark))
			dc = dc.add(Restrictions.like("remark", remark+"%"));
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
//		dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryPayOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
		if (StringUtils.isNotEmpty(billno)) {
			dc = dc.add(Restrictions.eq("billno", billno));
			Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, null);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (payOrderFlag != null) {
			dc = dc.add(Restrictions.eq("type", payOrderFlag));
		}
		if (StringUtils.isNotEmpty(loginname)) {
			dc = dc.add(Restrictions.eq("loginname", loginname));
		}
		if (StringUtils.isNotEmpty(accountName)) {
			dc = dc.add(Restrictions.eq("aliasName", accountName));
		} else if (StringUtils.isNotEmpty(referWebsite)) {
			List users = userDao.getAgents(referWebsite);
			dc.add(Property.forName("loginname").in(users));
		}
		if (StringUtils.isNotEmpty(billnotype)) {
			dc.add(Restrictions.eq("payPlatform", billnotype));
		}
		if (StringUtil.isNotEmpty(stringStartTime)) {
			dc = dc.add(Restrictions.ge("createTime", DateUtil.parseDateForStandard(stringStartTime)));
		}
		if (StringUtil.isNotEmpty(stringEndTime)) {
			dc = dc.add(Restrictions.lt("createTime", DateUtil.parseDateForStandard(stringEndTime)));
		}
		if(StringUtils.isNotEmpty(merchants) && StringUtils.isEmpty(billnotype)){
			dc.add(Restrictions.eq("merchants", Integer.valueOf(merchants)));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex, size, "money", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryGameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(GameTransfers.class);
		if (StringUtils.isNotEmpty(billno)) {
			dc = dc.add(Restrictions.eq("billno", billno));
			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (payOrderFlag != null)
			dc = dc.add(Restrictions.eq("type", payOrderFlag));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		else if (StringUtils.isNotEmpty(referWebsite)) {
			List users = userDao.getAgents(referWebsite);
			dc.add(Property.forName("loginname").in(users));

		}
		if (StringUtils.isNotEmpty(billnotype)) {
			dc.add(Restrictions.eq("payPlatform", billnotype));
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createTime", end));
		// if (newaccount != null)
		// dc = dc.add(Restrictions.eq("newaccount", newaccount));
		if(StringUtils.isNotBlank(accountName)){
			dc.add(Restrictions.eq("aliasName", accountName));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "money", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryEbetCredit() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getLoginname()!=null && !customer.getLoginname().equals("")){
				Double afterPtRemoteAmount = EBetUtil.getBalance(customer.getLoginname(), "EBET");
				if (afterPtRemoteAmount == null) {
					out.println("系统繁忙，请稍后尝试！");
				} else {
					out.println(afterPtRemoteAmount);
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	public String queryGPICredit() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getLoginname()!=null && !customer.getLoginname().equals("")){
				Double afterPtRemoteAmount = GPIUtil.getBalance(customer.getLoginname());
				if (afterPtRemoteAmount == null) {
					out.println("系统繁忙，请稍后尝试！");
				} else {
					out.println(afterPtRemoteAmount);
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	public String queryQuotalList() {
		DetachedCriteria dc = DetachedCriteria.forClass(QuotalRevision.class);
		if (examine != null) {
			dc = dc.add(Restrictions.eq("examine", examine));
		}
		 
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		 
		 
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		// if (newaccount != null)
		// dc = dc.add(Restrictions.eq("newaccount", newaccount));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "remit", "credit", "newCredit", o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String querySb() {
		DetachedCriteria dc = DetachedCriteria.forClass(Sbcoupon.class);
		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		if (StringUtils.isNotEmpty(shippingCode)) {
			dc.add(Restrictions.eq("shippingcode", shippingCode));
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String getSbListInfo() {
		DetachedCriteria dc = DetachedCriteria.forClass(Sbbets.class);
		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "sbbets", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryProposal() {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);

		if (StringUtils.isNotEmpty(pno)) {
			dc = dc.add(Restrictions.eq("pno", pno));
			dc = dc.add(Restrictions.ne("type", 516));
			Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, null);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(proposalRole)) {
			// 当提案中的角色类型不为空：
			DetachedCriteria userDC = DetachedCriteria.forClass(Users.class);
			userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("role", proposalRole));

			// 并且 代理账号 不为空：
			if (StringUtils.isNotEmpty(agent)) {
				userDC.add(Restrictions.eq("agent", agent));
			}

			// 并且 代理网址不为空：
			if (StringUtils.isNotEmpty(referWebsite)) {
				userDC.add(Restrictions.eq("referWebsite", referWebsite));
			}

			if (StringUtils.isNotEmpty(loginname)) {
				userDC.add(Restrictions.eq("loginname", loginname));
			}

			// 得到所有代理：
			dc.add(Property.forName("loginname").in(userDC));
		} else {
			// 如果角色类型为空：

			// 并且 代理账号 不为空：
			if (StringUtils.isNotEmpty(agent)) {
				DetachedCriteria userDC = DetachedCriteria.forClass(Users.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("agent", agent));

				dc.add(Property.forName("loginname").in(userDC));
			} else if (StringUtils.isNotEmpty(referWebsite)) {
				// 或 代理网址不为空：
				DetachedCriteria userDC = DetachedCriteria.forClass(Users.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("referWebsite", referWebsite));

				dc.add(Property.forName("loginname").in(userDC));
			}

			if (StringUtils.isNotEmpty(loginname)) {
				dc.add(Restrictions.eq("loginname", loginname));
			}

		}

		if (StringUtils.isNotEmpty(proposalType))
			dc = dc.add(Restrictions.eq("type", Integer.valueOf(Integer.parseInt(proposalType))));
		else
			dc = dc.add(Restrictions.ne("type", 516));
		if (proposalFlag != null)
			dc = dc.add(Restrictions.eq("flag", proposalFlag));

		if (overtime != null)
			dc = dc.add(Restrictions.eq("overtime", overtime));

		if (saveway != null && !saveway.equals("")) {
			dc = dc.add(Restrictions.eq("saveway", saveway));
		}
		if (bankaccount != null && !bankaccount.equals("")) {
			dc = dc.add(Restrictions.eq("bankaccount", bankaccount));
		}
		if (bankname != null && !bankname.equals("")) {
			dc = dc.add(Restrictions.like("bankname", bankname, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(this.gifTamount)) {
			Double giftAmount = Double.parseDouble(this.gifTamount);
			dc = dc.add(Restrictions.eq("gifTamount", giftAmount));
		}
		if (shippingCode != null && !shippingCode.equals("")) {
			dc = dc.add(Restrictions.like("shippingCode", shippingCode, MatchMode.ANYWHERE));
		}
		if (shippinginfo != null && !shippinginfo.equals("")) {
			dc = dc.add(Restrictions.like("shippinginfo", "%"+shippinginfo+"%", MatchMode.ANYWHERE));
		}
		if (proposer != null && !proposer.equals("")) {
			dc = dc.add(Restrictions.like("proposer", "%"+proposer+"%", MatchMode.ANYWHERE));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null) {
			if (null != isExecute && isExecute) {
				dc = dc.add(Restrictions.ge("executeTime", start));
			} else {
				dc = dc.add(Restrictions.ge("createTime", start));
			}
		}
		if (end != null) {
			if (null != isExecute && isExecute) {
				dc = dc.add(Restrictions.lt("executeTime", end));
			} else {
				dc = dc.add(Restrictions.lt("createTime", end));
			}
		}
		if(null != unfinshedYouHui && unfinshedYouHui){
			dc = dc.add(Restrictions.like("remark", "BEGIN", MatchMode.START));
			dc = dc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		}
		if (StringUtils.isNotBlank(remarkRemark)) {
			dc = dc.add(Restrictions.like("remark", remarkRemark, MatchMode.ANYWHERE));
		}
		if (level!=null) {
			dc = dc.add(Restrictions.eq("quickly", level));
		}
		
		
		
		if (StringUtils.isNotBlank(title)) {
			
			DetachedCriteria pc = DetachedCriteria.forClass(PreferentialConfig.class);
			
			pc.setProjection(Property.forName("id"));
			pc.add(Restrictions.like("aliasTitle", title, MatchMode.ANYWHERE));
			
			List<Integer> idList = proposalService.findByCriteria(pc);
			
			if (null != idList && !idList.isEmpty()) {
				
				DetachedCriteria pe = DetachedCriteria.forClass(ProposalExtend.class);
				
				pe.setProjection(Property.forName("pno"));
				pe.add(Restrictions.in("preferentialId", idList.toArray()));
				
				dc.add(Property.forName("pno").in(pe));
			}
		}
		
		Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex, size, "amount", "gifTamount", null, o);

		if (null != page && null != page.getPageContents() && !page.getPageContents().isEmpty()) {
			// 处理proposal中的代理类型
			List<Proposal> list = page.getPageContents();
			for (Proposal p : list) {
				Users user = (Users) slaveService.getHibernateTemplate().get(Users.class, p.getLoginname());
				if(user!=null){
					p.setAgentType(user.getRole());
					String warnremark = user.getWarnremark();
					if(StringUtils.isNotEmpty(warnremark)){
						warnremark=warnremark.replace("hangupcause:no_answer","");
						warnremark=warnremark.replace("hangupcause:success","");
						warnremark=warnremark.replace("hangupcause:call_reject","");
						warnremark=warnremark.replace(";","");
					}
					p.setUserRemark(warnremark);
				}
			}
		}

		getRequest().setAttribute("page", page);
		return INPUT;
	}
	private String emailflag;
	
	public String getEmailflag() {
		return emailflag;
	}

	public void setEmailflag(String emailflag) {
		this.emailflag = emailflag;
	}

	/**
	 * 电服电销 群呼功能查询
	 * @return
	 */
	public String queryPhone(){

		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);

		DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);

		//电服电销维护号二选一 不能都为空
		if ( StringUtil.isEmpty(operator.getType()) && StringUtil.isEmpty(operator.getCs())) {
			return INPUT;
		}

		//电服电销维护号只能唯一
		if ( StringUtil.isNotEmpty(operator.getType()) && StringUtil.isNotEmpty(operator.getCs())) {
			return INPUT;
		}

		//boss权限并且 电服电销维护号不能为空
		if (StringUtil.isNotEmpty(operator.getCs())&&!operator.getAuthority().equals("boss"))
			dc = dc.add(Restrictions.eq("cs", operator.getCs()));
		if (StringUtil.isNotEmpty(operator.getType())&&!operator.getAuthority().equals("boss"))
			dc = dc.add(Restrictions.eq("type",operator.getType()));

		if (loginname!=null &&!loginname.equals(""))
			dc = dc.add(Restrictions.eq("name", loginname));
		if (email!=null &&!email.equals(""))
			dc = dc.add(Restrictions.eq("email", email));
		if (phone!=null &&!phone.equals(""))
			dc = dc.add(Restrictions.eq("phone", phone));
		if (isreg!=null &&!isreg.equals(""))
			dc = dc.add(Restrictions.eq("isreg", Integer.parseInt(isreg)));
		if (isdeposit!=null &&!isdeposit.equals(""))
			dc = dc.add(Restrictions.eq("isdeposit", Integer.parseInt(isdeposit)));
		if (phonestatus!=null &&!phonestatus.equals(""))
			dc = dc.add(Restrictions.eq("phonestatus", Integer.parseInt(phonestatus)));
		if (userstatus!=null &&!userstatus.equals(""))
			dc = dc.add(Restrictions.eq("userstatus", Integer.parseInt(userstatus)));
		if (emailflag!=null &&!emailflag.equals(""))
			dc = dc.add(Restrictions.eq("emailflag", emailflag));
		if (shippingCode!=null &&!shippingCode.equals(""))
			dc = dc.add(Restrictions.eq("shippingCode", shippingCode));

		if (start != null) {
			dc = dc.add(Restrictions.ge("createTime", start));
		}
		if (end != null) {
			dc = dc.add(Restrictions.lt("createTime", end));
		}
		Order o = null;
		if (by!=null &&!by.equals("")) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	public String queryBbinData(){
		if(StringUtil.isBlank(billno) && StringUtil.isBlank(loginname)) {
			GsonUtil.GsonObject("玩家帐号不能为空！");
			return INPUT;
		}
		if(null == startTime){
			startTime = DateUtil.getToday();
		}
		if(null == endTime){
			endTime = DateUtil.getTomorrow();
		}
		List listt = new ArrayList();
		List listb = new ArrayList();
		if("0".equals(type)) {//bbin转账记录
			listt = BBinUtils.TransferRecord(loginname, paytype, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime), 1);
			getRequest().setAttribute("listt", listt);
		}
		if("1".equals(type)) {//bbin游戏记录
			listb = BBinUtils.queryBbinData(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime), paytype,billno);
			getRequest().setAttribute("listb", listb);
		}
		getRequest().setAttribute("type", type);
		return INPUT;
	}
	
	public String queryMail(){
		DetachedCriteria dc = DetachedCriteria.forClass(RecordMail.class);

		if (emailflag!=null &&!emailflag.equals(""))
			dc = dc.add(Restrictions.eq("status", Integer.valueOf(emailflag).intValue()));
		if (start != null) {
			dc = dc.add(Restrictions.ge("createtime", start));
		}
		if (end != null) {
			dc = dc.add(Restrictions.lt("createtime", end));
		}

		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	
	public String getCustomerPno(){
		iphone = (Customer) operatorService.get(Customer.class, id);
		return INPUT;
	}
	
	public String updatePhone(){
		iphone = (Customer) operatorService.get(Customer.class, id);
		if (phonestatus==null || phonestatus.equals("")){
			setErrormsg("手机状态不能为空!");
		}
		if (isreg==null || isreg.equals("")){
			setErrormsg("客户状态不能为空!");
		}	
		iphone.setPhonestatus(Integer.parseInt(phonestatus));
		iphone.setIsreg(Integer.parseInt(isreg));
		iphone.setRemark(remark);
		operatorService.update(iphone);
		setErrormsg("更新成功!");
		return INPUT;
	}

	public String queryagprofit() {
		DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class);
		if (!platform.equals("0")) {
			dc = dc.add(Restrictions.eq("platform", platform));
		}
		// if (StringUtils.isNotEmpty(pno)) {
		// dc = dc.add(Restrictions.eq("pno", pno));
		// Page page =
		// PageQuery.queryForPagenation(operatorService.getHibernateTemplate(),
		// dc, pageIndex, size);
		// getRequest().setAttribute("page", page);
		// return INPUT;
		// }

		// 如果角色类型为空：

		// 并且 代理账号 不为空：
		if (StringUtils.isNotEmpty(agent)) {
			DetachedCriteria userDC = DetachedCriteria.forClass(Users.class);
			userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("agent", agent));

			dc.add(Property.forName("loginname").in(userDC));
		}
		
		if (StringUtils.isNotEmpty(partner)) {
			DetachedCriteria pDC = DetachedCriteria.forClass(Users.class);
			pDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("partner", partner));
			dc.add(Property.forName("agent").in(pDC));
		}
		
		if (null != startDate && null != endDate) {
			DetachedCriteria tDC = DetachedCriteria.forClass(Users.class);
			tDC.setProjection(Property.forName("loginname")).add(Restrictions.ge("createtime", startDate)).add(Restrictions.lt("createtime", endDate));
			dc.add(Property.forName("agent").in(tDC));
		}

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		if(StringUtils.isNotBlank(intro)){  // cs1 cs2 a b c 
			DetachedCriteria dcI = DetachedCriteria.forClass(Users.class);
			dcI.setProjection(Property.forName("loginname")).add(Restrictions.eq("intro", intro)) ;
			dc.add(Property.forName("loginname").in(dcI));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createTime", end));

		Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex, size, "amount", "bettotal", null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	public void updateEbetAgprofitRecord(){
		Double ebetBet = Double.parseDouble(getRequest().getParameter("ebetBet"));
		Double ebetNet = Double.parseDouble(getRequest().getParameter("ebetNet"));
		AgProfit ebetAgprofitRecord = (AgProfit) operatorService.get(AgProfit.class, pno);
		Timestamp today = new Timestamp(new DateTime().withTimeAtStartOfDay().toDate().getTime());
		if(ebetAgprofitRecord.getPlatform().equals("ebetapp") && ebetAgprofitRecord.getCreateTime().after(today)){
			ebetAgprofitRecord.setBettotal(ebetBet);
			ebetAgprofitRecord.setAmount(ebetNet);
			operatorService.update(ebetAgprofitRecord);
			writeText("更新成功");
		}else{
			writeText("更新失败，只允许修改前一天的EBET数据");
		}
	}
	
	public String querywinpoint() {
		if(start==null || end==null){
			setErrormsg("时间不允许为空!");
			return INPUT;
		}
		Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			StringBuffer sbf = new StringBuffer();
			sbf.append("select aloginname,level,amountTotal,betTotal,winPointTotal,agent from " +
					"	(select loginname as aloginname,-1*sum(amount) as amountTotal,sum(bettotal) as betTotal,-1*sum(amount)/sum(bettotal) as winPointTotal from agprofit " +
					"	where createTime>=? and createTime<=? ");
			if (!platform.equals("0")) {
				sbf.append(" and platform=? ");
			}
			sbf.append(" group by loginname )t " +
					"	left join users on users.loginname=t.aloginname where 1=1 ");
			
			Map<String,Object> mm = new HashMap<String, Object>();
			switch (betflag!=null?betflag:4) {
			case 0:
				sbf.append(" and betTotal>=:gtBet");
				sbf.append(" and betTotal<:ltBet");
				mm.put("gtBet",50000 );
				mm.put("ltBet",500000 );
				break;
			case 1:
				sbf.append(" and betTotal>=:gtBet");
				sbf.append(" and betTotal<:ltBet");
				mm.put("gtBet",500000 );
				mm.put("ltBet",1000000 );
				break;
			case 2:
				sbf.append(" and betTotal>=:gtBet");
				sbf.append(" and betTotal<:ltBet");
				mm.put("gtBet",1000000 );
				mm.put("ltBet",1500000 );
				break;
			case 3:
				sbf.append(" and betTotal>=:gtBet");
				mm.put("gtBet",1500000 );
				break;
			default:
				break;
			}
			if (StringUtils.isNotEmpty(loginname)) {
				sbf.append(" and aloginname =:lgname");
				mm.put("lgname",loginname );
			}
			
			if(StringUtils.isNotEmpty(agent)){
				sbf.append(" and agent =:agt");
				mm.put("agt",agent );
			}
			if(null!=level){
				sbf.append(" and level =:lel");
				mm.put("lel",level );
			}
			if(StringUtils.isNotEmpty(by)){
				sbf.append(" order by "+by+" "+order);
			}
			
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			Query query = null;
			if(!platform.equals("0")){
				query = session.createSQLQuery(sbf.toString()).setDate(0, start).setDate(1, end).setString(2, platform);
			}else{
				query = session.createSQLQuery(sbf.toString()).setDate(0, start).setDate(1, end);
			}
			
			if(!mm.isEmpty()){
				query = query.setProperties(mm);//直接将map参数传组query对像
			}
			List list = query.list();
			Double totalAmount = 0.00;
			Double totalBet = 0.00;
			for(Object obj : list){
				Object[]objarray = (Object[])obj;
				totalAmount +=(Double)objarray[2];
				totalBet +=(Double)objarray[3];
			}
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(list.size());
			int pages = PagenationUtil.computeTotalPages(list.size(), size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			page.setStatics1(totalAmount);
			page.setStatics2(totalBet);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}
	
	public String queryagtrywinpoint() {
		if(start==null || end==null){
			setErrormsg("时间不允许为空!");
			return INPUT;
		}
		Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			StringBuffer sbf = new StringBuffer();
			sbf.append("select aloginname,agphone,amountTotal,betTotal,winPointTotal,sbetnum from " +
					"	(select loginname as aloginname,-1*sum(amount) as amountTotal,sum(bettotal) as betTotal,-1*sum(amount)/sum(bettotal) as winPointTotal,sum(betnum) as sbetnum from agtryprofit " +
					"	where createTime>=? and createTime<=? ");
			sbf.append(" group by loginname )t " +
					"	left join agtrygame on agtrygame.agname=t.aloginname where 1=1 ");
			
			Map<String,Object> mm = new HashMap<String, Object>();
			
			if (StringUtils.isNotEmpty(loginname)) {
				sbf.append(" and aloginname =:lgname");
				mm.put("lgname",loginname );
			}
			if (StringUtils.isNotEmpty(phone)) {
				sbf.append(" and agphone =:lphone");
				mm.put("lphone",phone );
			}
			
			if(StringUtils.isNotEmpty(by)){
				sbf.append(" order by "+by+" "+order);
			}
			
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			Query query = session.createSQLQuery(sbf.toString()).setDate(0, start).setDate(1, end);
			
			if(!mm.isEmpty()){
				query = query.setProperties(mm);//直接将map参数传组query对像
			}
			List list = query.list();
			Double totalAmount = 0.00;
			Double totalBet = 0.00;
			Integer totalTime = 0;
			for(Object obj : list){
				Object[]objarray = (Object[])obj;
				totalAmount +=(Double)objarray[2];
				totalBet +=(Double)objarray[3];
				BigDecimal b = (BigDecimal)objarray[5];
				totalTime += b.intValue();
			}
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(list.size());
			int pages = PagenationUtil.computeTotalPages(list.size(), size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			page.setStatics1(totalAmount);
			page.setStatics2(totalBet);
			getRequest().setAttribute("betTimes",totalTime );
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}

	public String querybaobiao() {
		/** **************2013年6月开始按照新的方式结算盈利*************** */
		if (year > 2013 || (year == 2013 && month > 5)) {
			try{
				if(pesoRate==null || "".equals(pesoRate)){
					pesoRate = "6.60";
				}else{
					Double d = Double.valueOf(pesoRate);
					getRequest().setAttribute("pRate", d);
				}
			}catch(Exception e){
				setMsg("peso汇率格式不正确!");
				return INPUT;
			}
			
			Calendar calendar = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH,1);//得到当前日期所在周的第一天
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.YEAR, year);
			
			calendar2.set(Calendar.YEAR, year);
			calendar2.set(Calendar.DAY_OF_MONTH,1);//
			calendar2.set(Calendar.HOUR_OF_DAY, 0);
			calendar2.set(Calendar.SECOND, 0);
			calendar2.set(Calendar.MINUTE, 0);
			
			if(month-12==0){
				calendar.set(Calendar.MONTH,11);
				calendar2.set(Calendar.MONTH,1);
				calendar2.set(Calendar.YEAR, year+1);
			}else if(month-1==0){
				calendar.set(Calendar.MONTH,0);
			}else{
				calendar.set(Calendar.MONTH,month-1);
				calendar2.set(Calendar.MONTH,month); 
			}
			//后台总存款   计算 银行额度记录表  没有手续费

			DetachedCriteria savedc = DetachedCriteria.forClass(BankCreditlogs.class);
			DetachedCriteria withdrawdc = DetachedCriteria.forClass(BankCreditlogs.class);
			DetachedCriteria payorderdc= DetachedCriteria.forClass(BankCreditlogs.class);
			DetachedCriteria intransferdc= DetachedCriteria.forClass(Intransfer.class);
			DetachedCriteria businessproposaldc= DetachedCriteria.forClass(BusinessProposal.class);
			DetachedCriteria businessproposalpesodc= DetachedCriteria.forClass(BusinessProposal.class);
			DetachedCriteria eaprofitdc= DetachedCriteria.forClass(EaProfit.class);
			
			eaprofitdc = eaprofitdc.add(Restrictions.eq("month", month));
			eaprofitdc = eaprofitdc.add(Restrictions.eq("year", year));
			
			//银行额度总存款
			savedc.add(Restrictions.eq("type", "CASHIN"));
			savedc = savedc.add(Restrictions.ge("createtime", calendar.getTime()));
			//银行额度总提款
			withdrawdc.add(Restrictions.eq("type", "CASHOUT"));
			withdrawdc = withdrawdc.add(Restrictions.ge("createtime", calendar.getTime()));
			//代理信用预支开始时间
			Date startAgent=calendar.getTime();
			//在线支付总存款
			payorderdc.add(Restrictions.or(Restrictions.eq("type", "NETPAY"),Restrictions.eq("type", "REPAIR_PAYORDER")));
			payorderdc = payorderdc.add(Restrictions.ge("createtime", calendar.getTime()));
			//户内转账手续费
			intransferdc = intransferdc.add(Restrictions.ge("transferflag", 0));
			intransferdc = intransferdc.add(Restrictions.ge("createTime", calendar.getTime()));
			
			businessproposaldc = businessproposaldc.add(Restrictions.ge("createTime", calendar.getTime()));
			businessproposalpesodc = businessproposalpesodc.add(Restrictions.ge("createTime", calendar.getTime()));
			if(month==12){
				calendar2.set(Calendar.MONTH,0);//
				calendar.set(Calendar.MONTH,0);//
			}
			if(month==1){
				calendar2.set(Calendar.MONTH,month);//
			}
			calendar.set(Calendar.MONTH,month);
			calendar2.set(Calendar.DAY_OF_MONTH,18);
			
			savedc = savedc.add(Restrictions.le("createtime", calendar.getTime()));
			withdrawdc = withdrawdc.add(Restrictions.le("createtime", calendar.getTime()));
			//代理信用预支开始时间
			Date endAgent=calendar.getTime();
			payorderdc = payorderdc.add(Restrictions.le("createtime", calendar.getTime()));
			intransferdc = intransferdc.add(Restrictions.le("createTime", calendar.getTime()));
			
			Page infeepage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), intransferdc, pageIndex, size, "fee", null,null, null);
			getRequest().setAttribute("infeepage", infeepage);
			
			businessproposaldc = businessproposaldc.add(Restrictions.le("createTime", calendar.getTime()));
			businessproposaldc = businessproposaldc.add(Restrictions.eq("bankaccountid", 5));
			businessproposaldc = businessproposaldc.add(Restrictions.eq("flag", 2));
			businessproposalpesodc = businessproposalpesodc.add(Restrictions.le("createTime", calendar.getTime()));
			businessproposalpesodc = businessproposalpesodc.add(Restrictions.eq("bankaccountid", 6));
			businessproposalpesodc = businessproposalpesodc.add(Restrictions.eq("flag", 2));
			
			Page savePage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), savedc, pageIndex, size, "remit", null,null, null);
			getRequest().setAttribute("page", savePage);
			Page memberpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), withdrawdc, pageIndex, size, "remit", null,null, null);
			memberpage.setStatics1(-1*memberpage.getStatics1());
			getRequest().setAttribute("memberpage", memberpage);
			//代理信用预支
			getRequest().setAttribute("agentAmount", operatorService.getAgentProposal(startAgent, endAgent));
			Page payorderpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), payorderdc, pageIndex, size, "remit", null,null, null);
			getRequest().setAttribute("payorderpage", payorderpage);
			Page businessproposalpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), businessproposaldc, pageIndex, size, "actualmoney", null,null, null);
			getRequest().setAttribute("businessproposalpage", businessproposalpage);
			Page businessproposalpesopage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), businessproposalpesodc, pageIndex, size, "actualmoney", null,null, null);
			getRequest().setAttribute("businessproposalpesopage", businessproposalpesopage);
			
			Page eaprofitpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), eaprofitdc, pageIndex, size, "eaprofit", "xingzheng",null, null);
			getRequest().setAttribute("amount", eaprofitpage.getStatics1());
			getRequest().setAttribute("xingzheng", eaprofitpage.getStatics2());
			//标识新的盈利报表
			getRequest().setAttribute("_newProfit",1);
		} else {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			DetachedCriteria savedc = DetachedCriteria.forClass(Proposal.class);
			// DetachedCriteria icbctransfetdc=
			// DetachedCriteria.forClass(IcbcTransfers.class);
			DetachedCriteria payorderdc = DetachedCriteria.forClass(Payorder.class);
			DetachedCriteria businessproposaldc = DetachedCriteria.forClass(BusinessProposal.class);
			DetachedCriteria businessproposalpesodc = DetachedCriteria.forClass(BusinessProposal.class);
			DetachedCriteria userDC = DetachedCriteria.forClass(Users.class);
			DetachedCriteria user2DC = DetachedCriteria.forClass(Users.class);
			DetachedCriteria memberdc = DetachedCriteria.forClass(Proposal.class);
			DetachedCriteria agentdc = DetachedCriteria.forClass(Proposal.class);
			DetachedCriteria eaprofitdc = DetachedCriteria.forClass(EaProfit.class);

			userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("role", "MONEY_CUSTOMER"));
			user2DC.setProjection(Property.forName("loginname")).add(Restrictions.eq("role", "AGENT"));

			// 得到所有真钱会员：
			memberdc.add(Property.forName("loginname").in(userDC));
			agentdc.add(Property.forName("loginname").in(user2DC));

			Calendar calendar = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);// 得到当前日期所在周的第一天
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.YEAR, year);
			calendar2.set(Calendar.YEAR, year);

			calendar2.set(Calendar.DAY_OF_MONTH, 1);//
			calendar2.set(Calendar.HOUR_OF_DAY, 0);
			calendar2.set(Calendar.SECOND, 0);
			calendar2.set(Calendar.MINUTE, 0);
			if (month - 12 == 0) {
				calendar.set(Calendar.MONTH, 11);
				calendar2.set(Calendar.MONTH, 1);
				calendar2.set(Calendar.YEAR, year + 1);
			} else if (month - 1 == 0) {
				calendar.set(Calendar.MONTH, 0);
			} else {
				calendar.set(Calendar.MONTH, month - 1);
				calendar2.set(Calendar.MONTH, month);
			}

			eaprofitdc = eaprofitdc.add(Restrictions.eq("month", month));
			eaprofitdc = eaprofitdc.add(Restrictions.eq("year", year));
			// icbctransfetdc = icbctransfetdc.add(Restrictions.ge("payDate",
			// calendar.getTime()));
			savedc = savedc.add(Restrictions.ge("createTime", calendar.getTime()));
			dc = dc.add(Restrictions.ge("createTime", calendar.getTime()));
			payorderdc = payorderdc.add(Restrictions.ge("createTime", calendar.getTime()));
			businessproposaldc = businessproposaldc.add(Restrictions.eq("belong", month.toString()));
			businessproposalpesodc = businessproposalpesodc.add(Restrictions.eq("belong", month.toString()));
			// businessproposaldc =
			// businessproposaldc.add(Restrictions.ge("createTime",
			// calendar.getTime()));
			// businessproposalpesodc =
			// businessproposalpesodc.add(Restrictions.ge("createTime",
			// calendar.getTime()));
			memberdc = memberdc.add(Restrictions.ge("createTime", calendar.getTime()));
			if (month == 12) {
				calendar2.set(Calendar.MONTH, 0);//
				calendar.set(Calendar.MONTH, 0);//
			}

			if (month == 1) {
				calendar2.set(Calendar.MONTH, month);//
			}
			agentdc = agentdc.add(Restrictions.ge("createTime", calendar2.getTime()));

			calendar.set(Calendar.MONTH, month);
			calendar2.set(Calendar.MONTH, month);//
			calendar2.set(Calendar.DAY_OF_MONTH, 18);//

			// icbctransfetdc = icbctransfetdc.add(Restrictions.le("payDate",
			// calendar.getTime()));
			dc = dc.add(Restrictions.le("createTime", calendar.getTime()));
			dc = dc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode().intValue()));
			dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode().intValue()));

			savedc = savedc.add(Restrictions.le("createTime", calendar.getTime()));
			savedc = savedc.add(Restrictions.in("bankaccount", new String[] { "赵华刚", "肖雯", "邵小花", "袁德轮", "王志勤", "范献国" }));
			savedc = savedc.add(Restrictions.eq("type", ProposalType.CASHIN.getCode().intValue()));
			savedc = savedc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode().intValue()));

			payorderdc = payorderdc.add(Restrictions.le("createTime", calendar.getTime()));
			memberdc = memberdc.add(Restrictions.le("createTime", calendar.getTime()));
			memberdc = memberdc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode().intValue()));
			memberdc = memberdc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode().intValue()));

			agentdc = agentdc.add(Restrictions.le("createTime", calendar2.getTime()));
			agentdc = agentdc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode().intValue()));
			agentdc = agentdc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode().intValue()));

			// businessproposaldc =
			// businessproposaldc.add(Restrictions.le("createTime",
			// calendar.getTime()));
			businessproposaldc = businessproposaldc.add(Restrictions.eq("bankaccountid", 5));
			businessproposaldc = businessproposaldc.add(Restrictions.eq("flag", 2));
			// businessproposalpesodc =
			// businessproposalpesodc.add(Restrictions.le("createTime",
			// calendar.getTime()));
			businessproposalpesodc = businessproposalpesodc.add(Restrictions.eq("bankaccountid", 6));
			businessproposalpesodc = businessproposalpesodc.add(Restrictions.eq("flag", 2));

			Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), savedc, pageIndex, size, "amount", null, null, null);
			getRequest().setAttribute("page", page);

			Page proposalpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, null);
			getRequest().setAttribute("proposalpage", proposalpage);

			Page payorderpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), payorderdc, pageIndex, size, "money", null, null, null);
			getRequest().setAttribute("payorderpage", payorderpage);

			Page businessproposalpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), businessproposaldc, pageIndex, size, "actualmoney", null, null, null);
			getRequest().setAttribute("businessproposalpage", businessproposalpage);

			Page businessproposalpesopage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), businessproposalpesodc, pageIndex, size, "actualmoney", null, null, null);
			getRequest().setAttribute("businessproposalpesopage", businessproposalpesopage);

			Page memberpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), memberdc, pageIndex, size, "amount", null, null, null);
			getRequest().setAttribute("memberpage", memberpage);
			// System.out.println(agentdc.toString());
			Page agentpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), agentdc, pageIndex, size, "amount", null, null, null);
			getRequest().setAttribute("agentpage", agentpage);

			Page eaprofitpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), eaprofitdc, pageIndex, size, "eaprofit", "xingzheng", null, null);

			getRequest().setAttribute("amount", eaprofitpage.getStatics1());
			getRequest().setAttribute("xingzheng", eaprofitpage.getStatics2());
		}

		// jdbc查询行政后台 中国国内开支 and 菲律宾事务开支
		/** *********************** */
		/*
		 * Double amount1 = null; Double amount2 = null; try{ DBUtil dbutil =
		 * DBUtil.getInstanceDBUtil(); java.sql.Connection conn =
		 * dbutil.getConnectionJDBC(); ArrayList<HashMap> listMap1 =
		 * dbutil.selectPrepare(conn, "select sum(amount) as amount from
		 * businessproposal " + "where flag = ? and belong = ? and bankaccountid =
		 * ?", new Object[]{"2",month,"5"}); if(listMap1!=null &&
		 * !listMap1.isEmpty() && listMap1.size()==1){ amount1 =
		 * Double.parseDouble(((String)listMap1.get(0).get("amount"))); }
		 * 
		 * ArrayList<HashMap> listMap2 = dbutil.selectPrepare(conn, "select
		 * sum(amount) as amount from businessproposal " + "where flag = ? and
		 * belong = ? and bankaccountid = ?", new Object[]{"2",month,"6"});
		 * if(listMap2!=null && !listMap2.isEmpty() && listMap2.size()==1){
		 * amount2 = Double.parseDouble((String)listMap2.get(0).get("amount")); }
		 * }catch(Exception e){ e.printStackTrace(); }
		 */
		return INPUT;
	}

	public String getproposalforaudit() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		// 开始查询是否有待审核的提案
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.eq("flag", 0));
			dc = dc.add(Restrictions.in("type", new Integer[] { ProposalType.CASHOUT.getCode(), ProposalType.CASHIN.getCode() }));
			if (start != null)
				dc = dc.add(Restrictions.ge("createTime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createTime", end));
			List<Proposal> list = proposalService.findByCriteria(dc);

			out = this.getResponse().getWriter();
			if (list != null && list.size() > 0) {
				// setErrormsg("审核成功");
				out.println("1");// 输出1表示有待审核的提案
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("0");// 输出0表示没有有待审核的提案
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("3");// 输出3表示查询过程中出现异常
			// setErrormsg("审核失败:" + e.getMessage());
			// out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String rPayProposal() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("秒提失败，重新付款");
		try {
			String msg = proposalService.rPayProposal(jobPno, getOperatorLoginname(), getIp(),loginname);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("提交成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("提交失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("提交失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String getSubmitYhjAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("提案审核");
		try {
			String msg = proposalService.submitYhjProposal(jobPno, getOperatorLoginname(), getIp(),loginname);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	public String gongHuiMembersList(){
		DetachedCriteria dc = DetachedCriteria.forClass(GuildStaff.class);
		dc = dc.add(Restrictions.eq("guildId", id));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("username", loginname));

		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	public String queryGongHuiList(){
		DetachedCriteria dc = DetachedCriteria.forClass(GuildStaff.class);
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("username", loginname));
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}


	private GuildStaff guildStaff;

	public GuildStaff getGuildStaff() {
		return guildStaff;
	}

	public void setGuildStaff(GuildStaff guildStaff) {
		this.guildStaff = guildStaff;
	}
	public String addGongHuiFalseData(){
		try {
			GuildStaff guildStaff = this.guildStaff;
			DetachedCriteria dc = DetachedCriteria.forClass(GuildStaff.class);
			dc.add(Restrictions.eq("username",guildStaff.getUsername()));
			List<GuildStaff> list = operatorService.findByCriteria(dc);
			if(list.size()>0){
				GuildStaff staff = list.get(0);
				Guild guild=(Guild)operatorService.get(Guild.class,staff.getGuildId());
				guildStaff.setId(staff.getId());
				guildStaff.setName(guild.getName());
				guildStaff.setRemark("虚拟数据");
				operatorService.update(guildStaff);
				GsonUtil.GsonObject("虚拟数据更新成功");
				return null;
			}

			Guild guild=(Guild)operatorService.get(Guild.class,guildStaff.getGuildId());
			guildStaff.setState(10);  //10标识假数据
			guildStaff.setRemark("虚拟数据");
			guildStaff.setName(guild.getName());
			GsonUtil.GsonObject("虚拟数据添加完成!");
			operatorService.save(guildStaff);
		} catch (Exception e) {
			log.error("setSmWeeklyJob error:",e);
			GsonUtil.GsonObject("虚拟数据添加失败,系统错误!");
		}
		return null;
	}


	public String getSubmitEmailAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			//根据id获取用户信息
			Customer customer = (Customer) operatorService.get(Customer.class, Integer.parseInt(email));
			email=customer.getEmail();
			out = this.getResponse().getWriter();
			if(title==null||title.equals("")){
				out.println("邮箱标题不能空！");
				return null;
			}
			if(remark==null||remark.equals("")){
				out.println("邮箱内容不能空！");
				return null;
			}
//			String msg = proposalService.addPrizeEmail(email,getOperatorLoginname(),Constants.FROM_BACK, 537, 68.00, "15",StringUtil.trim(remark),"a_test02");
//			if (msg != null){
//				out.println("提交失败:" + msg);
//			}else{
			this.yjnr=remark;
			this.sjr=email;
			this.zt=title;
			this.sendEmails();
//			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("发送邮箱失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	public String getGongHuiInfo() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Guild.class);
			List<Guild> list = proposalService.findByCriteria(dc);
			getRequest().setAttribute("data", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
	}

	public String deleteDataOne(){
		try {
			operatorService.delete(Guild.class,id);
			GsonUtil.GsonObject("删除成功!");
		} catch (Exception e) {
			log.error("setSmWeeklyJob error:",e);
			GsonUtil.GsonObject("删除失败!");
		}
		return null;
	}
	public String getSubmitPhoneAction(){
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(phone==null||phone.equals("")){
				out.println("短信内容不能空！");
				return null;
			}
			if(remark==null||remark.equals("")){
				out.println("短信内容不能空！");
				return null;
			}
			Customer customer = (Customer) operatorService.get(Customer.class, Integer.parseInt(phone));
			phone = customer.getPhone();
//			String msg = proposalService.addPrizePhone(phone,getOperatorLoginname(),Constants.FROM_BACK, 537, 68.00, "15",StringUtil.trim(remark),"a_test02");
//			if (msg != null){
//				out.println("提交失败:" + msg);
//			}else{
				String msg = notifyService.sendSmsNew(phone.trim(), StringUtil.trim(remark));
				out.println(msg);
//			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("发送短信失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	// 国际短信电服专员短信单发
	public void sendDFSmsByPhoneList() {
		String operatorLoginname = getOperatorLoginname();
		if (StringUtils.isBlank(operatorLoginname)) {

			GsonUtil.GsonObject("登录会话已过期，请重新登录后再操作！");
			return ;
		}
		String value = querySystemConfig("type031", "001", "否");

		if (StringUtils.isBlank(value)) {

			GsonUtil.GsonObject("短信平台通道已关闭！");
			return;
		}

		List list = checkKeyWord(content);

		if (null != list && list.size() > 0) {

			StringBuffer sbf = new StringBuffer();

			for (int i = 0; i < list.size(); i++) {

				sbf = sbf.append(list.get(i) + ",");
			}

			if (sbf.length() > 0) {

				GsonUtil.GsonObject("该短信内容包含关键字：" + sbf.toString() + "，请修改后在发送。");
				return;
			}
		}

		String str = SendPhoneMsgUtil.InternationDFSms(phone, content);
		//记录发送日志
		operatorService.insertOperatorSendSMSLog(operatorLoginname,str);
		GsonUtil.GsonObject(str);
	}
	// 国际短信服务(产品电服使用注意使用通道)
	public String sendDFSmsByUsersList(){
		String operatorLoginname = getOperatorLoginname();
		if (StringUtils.isBlank(operatorLoginname)) {

			GsonUtil.GsonObject("登录会话已过期，请重新登录后再操作！");
			return null;
		}
		String value = querySystemConfig("type031", "001", "否");
		if (StringUtils.isBlank(value)) {
			GsonUtil.GsonObject("短信平台通道已关闭！");
			return null;
		}
		List list  = checkKeyWord(content);
		if(null!=list&&list.size()>0){
			StringBuffer sbf = new StringBuffer();
			for(int i=0;i<list.size();i++){
				sbf =sbf.append(list.get(i)+",");
			}
			if(sbf.length()>0){
				GsonUtil.GsonObject("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
				return null;
			}
		}
		if(StringUtils.isNotBlank(ids)){
			String[] loginnames = ids.split(",");
			if(loginnames.length<1){
				GsonUtil.GsonObject("请选中要执行的条目");
				return null;
			}
			StringBuffer phones = new StringBuffer();
			for (String loginname : loginnames) {
				Users user = (Users) operatorService.get(Users.class, loginname);
				if (StringUtil.isNotBlank(user.getPhone())) {
					phones.append(user.getPhone()+",");
				}
			}
			String str = SendPhoneMsgUtil.InternationDFSms(phones.toString(), content);

			GsonUtil.GsonObject(str);
		}
		return null;
	}
	public String queryUserSmsDF() {
		Page page = new Page();
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		loginname = StringUtil.trim(loginname);
		dc.add(Restrictions.ne("loginname", "admin"));

		agent = StringUtil.trim(agent);
		if (StringUtils.isNotEmpty(roleCode))
			dc = dc.add(Restrictions.eq("role", roleCode));
		if (warnflag != null)
			dc = dc.add(Restrictions.eq("warnflag", warnflag));// sun
		if (intro != null && !"".equals(intro)) {
			dc = dc.add(Restrictions.eq("intro", intro));
		}
		if (qq != null && !"".equals(qq)) {
			dc = dc.add(Restrictions.eq("qq", qq));
		}
		if (StringUtils.isNotBlank(partner)) {
			dc = dc.add(Restrictions.eq("partner", partner));
		}
		if (StringUtils.isNotBlank(lastLoginIp)) {
			dc = dc.add(Restrictions.eq("lastLoginIp", lastLoginIp));
		}
		if (StringUtils.isNotBlank(sms)) {
			dc = dc.add(Restrictions.eq("sms", Integer.parseInt(sms)));
		}
		if (invitecode != null && !"".equals(invitecode)) {
			dc = dc.add(Restrictions.eq("invitecode", invitecode));
		}
		if (level != null)
			dc = dc.add(Restrictions.eq("level", level));// sun
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("flag", Integer.parseInt(status)));
		if (StringUtils.isNotEmpty(isCashin)) {
			// 当提案中的角色类型不为空：
			if (Integer.parseInt(isCashin) == 0) {
				//有存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode())).add(Restrictions.eq("type", PayType.SUCESS.getCode()));
				dc.add( Restrictions.or(Property.forName("loginname").in(userDC),Restrictions.eq("isCashin", Integer.parseInt(isCashin))));
			}else{
				//未存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
				dc.add( Property.forName("loginname").notIn(userDC));
				dc = dc.add(Restrictions.eq("isCashin", Integer.parseInt(isCashin)));
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//判断7天未游戏玩家
		if(dayNumflag.equals("0")){
			cal.add(Calendar.DATE, - 7);
			Date monday = cal.getTime();
			dc = dc.add(Restrictions.le("lastLoginTime", monday));
		}
		//判断15天未游戏玩家
		if(dayNumflag.equals("1")){
			cal.add(Calendar.DATE, - 15);
			Date monday = cal.getTime();
			dc = dc.add(Restrictions.le("lastLoginTime", monday));
		}
		//判断30天未游戏玩家
		if(dayNumflag.equals("2")){
			cal.add(Calendar.DATE, - 31);
			Date monday = cal.getTime();
			dc = dc.add(Restrictions.le("lastLoginTime", monday));
		}
		//判断30天以上未游戏玩家
		if(dayNumflag.equals("3")){
			cal.add(Calendar.DATE, - 31);
			Date monday = cal.getTime();
			dc = dc.add(Restrictions.lt("lastLoginTime", monday));
		}



		if (StringUtil.isEmpty(dayNumflag) && StringUtils.isEmpty(birthday) && StringUtils.isEmpty(loginname) && StringUtils.isEmpty(randnum) && StringUtils.isEmpty(aliasName) && StringUtils.isEmpty(accountName) && id == null && StringUtils.isEmpty(registerIp) && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(accountNo) && StringUtils.isEmpty(referWebsite) && StringUtils.isEmpty(agent) && StringUtils.isEmpty(howToKnow)) {
			if (start != null)
				dc = dc.add(Restrictions.ge("createtime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createtime", end));
			Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//					dc = dc.addOrder(o);
			}

			page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(birthday))
			dc = dc.add(Restrictions.sqlRestriction("birthday like '%-"+birthday+"%'"));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (randnum!=null &&!"".equals(randnum))
			dc = dc.add(Restrictions.eq("randnum", randnum));
		if (id != null)
			dc = dc.add(Restrictions.eq("id", id));

		if (StringUtils.isNotEmpty(aliasName))
			dc = dc.add(Restrictions.ilike("aliasName", aliasName, MatchMode.ANYWHERE));
		if (StringUtils.isNotEmpty(agent)){
			dc = dc.add(Restrictions.eq("agent", agent));
			if (start != null)
				dc = dc.add(Restrictions.ge("createtime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createtime", end));
		}

		if (StringUtils.isNotEmpty(accountName))
			dc = dc.add(Restrictions.eq("accountName", accountName));
		if (StringUtils.isNotEmpty(email)){
			try {
				email = AESUtil.aesEncrypt(email,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("email", email));
		}
		if (StringUtils.isNotEmpty(phone)){
			try {
				phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("phone", phone));
		}
		if (StringUtils.isNotEmpty(registerIp))
			dc = dc.add(Restrictions.eq("registerIp", registerIp));
		if (StringUtils.isNotEmpty(howToKnow))
			dc = dc.add(Restrictions.eq("howToKnow", howToKnow));
		if (StringUtils.isNotEmpty(referWebsite)) {
			dc = dc.add(Restrictions.eq("referWebsite", referWebsite));
			if (start != null)
				dc = dc.add(Restrictions.ge("createtime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createtime", end));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//				dc = dc.addOrder(o);
		}
		page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		try {
			email = AESUtil.aesDecrypt(email,AESUtil.KEY);
			phone = AESUtil.aesDecrypt(phone,AESUtil.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return INPUT;
	}
	
	public String duanXinQunFa(){
		PrintWriter out = null ;
		try {
			out = this.getResponse().getWriter();
			this.getRequest().setCharacterEncoding("UTF-8");
			
			String[] idNu = ids.split(",");
			if(idNu.length<1){
				out.print("您未选中条目。");
			}else{
				List list1  = checkKeyWord(remark);
				if(null!=list1&&list1.size()>0){
					StringBuffer sbf = new StringBuffer();
					for(int i=0;i<list1.size();i++){
						sbf =sbf.append(list1.get(i)+",");
					}
					if(sbf.length()>0){
					out.print("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
					return null;
					}
				}
				
				DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
				dc.add(Restrictions.in("id", StringUtil.strArray2intArray(idNu))) ;
				List<Customer> list = proposalService.findByCriteria(dc) ;
				StringBuffer phones = new StringBuffer() ;
				for(int i=0 ; i<list.size();i++){
					Customer customer = list.get(i) ;
					if(i != 0 ){
						phones.append(","+customer.getPhone());
					}else {
						phones.append(customer.getPhone());
					}
				}
				/*SendPhoneThread thread = new SendPhoneThread(notifyService , remark , phones.toString()) ;
				thread.start();*/
				String str=SendPhoneMsgUtil.callfour(phones.toString(), remark);
				out.print(str);
				}
			
		} catch (Exception e) {
		}finally {
			if (out != null) {
				out.close();
			}
		}
		
		return null ;
	}
	

	
	public String getSendEmailAction(){
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String msg = proposalService.addPrizeProposalEmail(getOperatorLoginname(),Constants.FROM_BACK, 537, 68.00, "15",StringUtil.trim(remark),"a_test02");
			if (msg != null){
				out.println("提交失败:" + msg);
			}else{
				out.println("提交成功");
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("提交失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}


	public String updateGongHuiWeekData(){
		try {
			Boolean b=false;
			DateTime now = DateTime.now();
			Date date = now.plusDays(-1).toDate();
			String s = DateUtil.fmtyyyyMMdd(date);
			DetachedCriteria dc = DetachedCriteria.forClass(Guild.class);
			List<Guild> list = operatorService.findByCriteria(dc);
			for (Guild guild:list) {
				String game = guild.getGame();
				int id = guild.getId();
				DetachedCriteria dc2 = DetachedCriteria.forClass(GuildStaff.class);
				dc2.add(Restrictions.eq("guildId", id));
				dc2.add(Restrictions.ne("state", 10));
				List<GuildStaff> guildStaffList = operatorService.findByCriteria(dc2);
				for (GuildStaff guildStaff : guildStaffList) {
					if(StringUtils.isNotEmpty(guildStaff.getDay())&&guildStaff.getDay().equals(s)){
						b=true;
						break;
					}
					Date startTime =DateUtil.fmtyyMMdd00d( DateUtil.fmtyyMMdd00(date));
					Date endTime =DateUtil.fmtyyMMdd23d( DateUtil.fmtyyMMdd23(now.toDate()));

					Date startTime1 =DateUtil.fmtyyMMdd00d( DateUtil.fmtyyMMdd00(now.toDate()));
					Date endTime2 =DateUtil.fmtyyMMdd23d( DateUtil.fmtyyMMdd23(now.plusDays(1).toDate()));

					String deposit = operatorService.queryDeposit(startTime, endTime, guildStaff.getUsername(), proposalService);
					List bet = operatorService.queryBet(guildStaff.getUsername(), startTime1, endTime2, game);
					guildStaff.setDeposet(guildStaff.getDeposet() + Double.valueOf(deposit));

					try {
						guildStaff.setGameAmount(guildStaff.getGameAmount() + (Double) bet.get(0));
						guildStaff.setRangeGameAmount((Double) bet.get(0));
					} catch (NullPointerException e) {
						guildStaff.setGameAmount(guildStaff.getGameAmount() + 0.0);
						guildStaff.setRangeGameAmount(0.0);
					}
					guildStaff.setRangeDeposet(Double.valueOf(deposit));
					guildStaff.setUpdatetime(now.toDate());
					guildStaff.setDay(s);
					operatorService.update(guildStaff);
				}

			}
			if(b){
				GsonUtil.GsonObject("今天数据已更新!如有疑问,请联系技术!");
				return null;
			}
			//smWeeklyJob.generateWeeklyData(u_d);
			GsonUtil.GsonObject("数据更新成功!");
		} catch (Exception e) {
			log.error("setSmWeeklyJob error:",e);
			GsonUtil.GsonObject("更新数据失败,系统错误!");
		}
		return null;
	}
	
	public String getSubmitEmailAllAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(title==null||title.equals("")){
				out.println("邮箱标题不能空！");
				return null;
			}
			if(remark==null||remark.equals("")){
				out.println("邮箱内容不能空！");
				return null;
			}
			String msg = proposalService.addPrizeProposalEmailAll(title,remark,batch);
			if (msg != null){
				out.println("提交失败:" + msg);
			}else{
				out.println("提交成功");
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("提交失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	
	public String sendPt8CouponEmailAction(){
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String msg = proposalService.addPt8CouponProposalEmail(getOperatorLoginname());
			if (msg != null){
				out.println("提交失败:" + msg);
			}else{
				out.println("提交成功");
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("提交失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	public void insertGuild() {

		String msg = "";

		try {

			Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);

			if (null == operator) {

				msg = "请先登录！";
			} else {
				Guild g=this.guild;
				g.setCreator(operator.getUsername());
				g.setCreateTime(new Date());
				operatorService.save(g);

				msg = "添加成功";
			}
		} catch (Exception e) {

			msg = e.getMessage();
		}

		GsonUtil.GsonObject(msg);
	}
	
	public String sendPt8CouponSingleEmailAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(title==null||title.equals("")){
				out.println("邮箱标题不能空！");
				return null;
			}
			if(remark==null||remark.equals("")){
				out.println("邮箱内容不能空！");
				return null;
			}
			notifyService.sendEmail(email, title, remark);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("发送邮箱失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	
	public String queryAgentPhone(){
		DetachedCriteria dc = DetachedCriteria.forClass(AgentCustomer.class);
		if (loginname!=null &&!loginname.equals(""))
			dc = dc.add(Restrictions.eq("name", loginname));
		if (email!=null &&!email.equals(""))
			dc = dc.add(Restrictions.eq("email", email));
		if (phone!=null &&!phone.equals(""))
			dc = dc.add(Restrictions.eq("phone", phone));
		if (isreg!=null &&!isreg.equals(""))
			dc = dc.add(Restrictions.eq("isreg", Integer.parseInt(isreg)));
		if (isdeposit!=null &&!isdeposit.equals(""))
			dc = dc.add(Restrictions.eq("isdeposit", Integer.parseInt(isdeposit)));
		if (phonestatus!=null &&!phonestatus.equals(""))
			dc = dc.add(Restrictions.eq("phonestatus", Integer.parseInt(phonestatus)));
		if (userstatus!=null &&!userstatus.equals(""))
			dc = dc.add(Restrictions.eq("userstatus", Integer.parseInt(userstatus)));
		if (emailflag!=null &&!emailflag.equals(""))
			dc = dc.add(Restrictions.eq("emailflag", emailflag));
		if (cs!=null &&!cs.equals(""))
			dc = dc.add(Restrictions.eq("cs", cs));
		if (shippingCode!=null &&!shippingCode.equals(""))
			dc = dc.add(Restrictions.eq("shippingCode", shippingCode));
		if (type!=null &&!type.equals(""))
			dc = dc.add(Restrictions.eq("type", type));
		if (start != null) {
			dc = dc.add(Restrictions.ge("createTime", start));
		}
		if (end != null) {
			dc = dc.add(Restrictions.lt("createTime", end));
		}
		Order o = null;
		if (by!=null &&!by.equals("")) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String getAgentCustomerPno(){
		agentPhone = (AgentCustomer) operatorService.get(AgentCustomer.class, id);
		return INPUT;
	}
	private String username;
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	private Emigratedapply ey=null;
	public Emigratedapply getEy() {
		return ey;
	}

	public void setEy(Emigratedapply ey) {
		this.ey = ey;
	}

	/**
	 * 查询全民闯关
	 * @return
	 */
	public String getEmigratedrecord(){ 
		DetachedCriteria dc=null;
		if(type.equals("0")){//报名记录
			dc= DetachedCriteria.forClass(Emigratedapply.class);
		}else if(type.equals("1")){//获取奖金记录
			dc= DetachedCriteria.forClass(Emigratedrecord.class);
			dc.add(Restrictions.eq("type", "1"));
		}else if(type.equals("2")){//消费奖金记录
			dc= DetachedCriteria.forClass(Emigratedrecord.class);
			dc.add(Restrictions.eq("type", "2"));
		}else if(type.equals("3")){//奖金池
			dc= DetachedCriteria.forClass(Emigratedbonus.class);
		}else{//查询全部
			dc= DetachedCriteria.forClass(Emigratedrecord.class);
		}
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("username", username));
		}
		if (startTime != null){
			dc = dc.add(Restrictions.ge("updatetime", startTime));//>=
		}
		if (endTime != null){
			dc = dc.add(Restrictions.le("updatetime", endTime));//<=
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("updatetime");
//			dc = dc.addOrder(Order.desc("updatetime"));
		}
		 
		Page  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		Double sumMoney =0.0;
		 if(!"0".equals(type)){
		 List list = operatorService.findByCriteria(dc);
		 if(null!=list&&list.size()>0){
		 for(Object obj : list){
			if("3".equals(type)){
				Emigratedbonus fd =(Emigratedbonus)obj;
				 sumMoney=sumMoney+fd.getMoney();
			 }else{
				 Emigratedrecord fd =(Emigratedrecord)obj;
				 sumMoney=sumMoney+fd.getMoney();
			 }
		 }
		 page.setStatics1(sumMoney);
		 }
		 }
		 else{
			 List<Emigratedapply> listsEmigrated=page.getPageContents();
			 for(Emigratedapply ey :listsEmigrated){
				 if(ey.getType().equals("1")){
					 ey.setType("龙都-不屈白银");
				 }else  if(ey.getType().equals("2")){
					 ey.setType("龙都-荣耀黄金");
				 }else  if(ey.getType().equals("3")){
					 ey.setType("龙都-华贵铂金");
				 }else  if(ey.getType().equals("4")){
					 ey.setType("龙都-璀璨钻石");
				 }else  if(ey.getType().equals("5")){
					 ey.setType("龙都-最强王者");
				 }
			 }
		 }
		 
  		getRequest().setAttribute("page", page);
		return INPUT;
	}	
	
	public String updateAgentPhone(){
		agentPhone = (AgentCustomer) operatorService.get(AgentCustomer.class, id);
		if (phonestatus==null || phonestatus.equals("")){
			setErrormsg("手机状态不能为空!");
		}
		if (isreg==null || isreg.equals("")){
			setErrormsg("客户状态不能为空!");
		}	
		agentPhone.setPhonestatus(Integer.parseInt(phonestatus));
		agentPhone.setIsreg(Integer.parseInt(isreg));
		agentPhone.setRemark(remark);
		operatorService.update(agentPhone);
		setErrormsg("更新成功!");
		return INPUT;
	}

	
	//查询pt8元优惠劵列表
		public String queryPtCoupon(){
			DetachedCriteria dc = DetachedCriteria.forClass(PtCoupon.class);
			if (email!=null &&!email.equals(""))
				dc = dc.add(Restrictions.eq("email", email));
			if (type!=null &&!type.equals(""))
				dc = dc.add(Restrictions.eq("type", type));
			if (start != null) {
				dc = dc.add(Restrictions.ge("createtime", start));
			}
			if (end != null) {
				dc = dc.add(Restrictions.lt("createtime", end));
			}
			if (code!=null &&!code.equals(""))
				dc = dc.add(Restrictions.eq("code", code));
			Order o = null;
			if (by!=null &&!by.equals("")) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//				dc = dc.addOrder(o);
			}
			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
			getRequest().setAttribute("order", order);
			return INPUT;
		}
	
	public String getSendPhoneAction(){
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String msg = proposalService.addPrizeProposalPhone(getOperatorLoginname(),Constants.FROM_BACK, 537, 68.00, "15",StringUtil.trim(remark),"a_test02");
			if (msg != null){
				out.println("提交失败:" + msg);
			}else{
				out.println("提交成功");
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.println("提交失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}
	
	
	public String submitYhjCancelAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("取消审核");
		try {
			String msg = proposalService.submitYhjCancel(jobPno, getOperatorLoginname(), getIp(),loginname);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("取消成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("取消失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("取消失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	

	public String queryBsinessProposal() {
		DetachedCriteria dc = DetachedCriteria.forClass(BusinessProposal.class);

		dc = dc.add(Restrictions.eq("bankaccountid", 5));

		if (StringUtils.isNotEmpty(pno)) {
			dc = dc.add(Restrictions.eq("pno", pno));
			dc = dc.add(Restrictions.ne("type", 516));

			Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "actualmoney", null, null, null);
			getRequest().setAttribute("page", page);

			return INPUT;
		}

		if (StringUtils.isNotEmpty(belong)) {
			dc = dc.add(Restrictions.eq("belong", belong));
			dc = dc.add(Restrictions.ne("type", 516));

			if (proposalFlag != null) {
				dc = dc.add(Restrictions.eq("flag", proposalFlag));
			}

			Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "actualmoney", null, null, null);
			getRequest().setAttribute("page", page);

			return INPUT;
		}

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("depositname", loginname));
		}

		if (StringUtils.isNotEmpty(proposalType)) {
			dc = dc.add(Restrictions.eq("type", Integer.valueOf(Integer.parseInt(proposalType))));
		}

		if (proposalFlag != null) {
			dc = dc.add(Restrictions.eq("flag", proposalFlag));
		}

		if (bankaccount != null && !bankaccount.equals("")) {
			dc = dc.add(Restrictions.eq("bankaccount", bankaccount));
		}
		if (bankinfoid != null) {
			dc = dc.add(Restrictions.eq("bankaccountid", bankinfoid));
		}
		if (bankname != null && !bankname.equals("")) {
			dc = dc.add(Restrictions.like("depositbank", bankname, MatchMode.ANYWHERE));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null) {
			dc = dc.add(Restrictions.ge("createTime", start));
		}

		if (end != null) {
			dc = dc.add(Restrictions.lt("createTime", end));
		}

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "actualmoney", null, null, o);
		getRequest().setAttribute("page", page);

		return INPUT;
	}

	public String querypesoBsinessProposal() {
		DetachedCriteria dc = DetachedCriteria.forClass(BusinessProposal.class);

		dc = dc.add(Restrictions.eq("bankaccountid", 6));

		if (StringUtils.isNotEmpty(pno)) {
			dc = dc.add(Restrictions.eq("pno", pno));
			dc = dc.add(Restrictions.ne("type", 516));

			Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "actualmoney", null, null, null);
			getRequest().setAttribute("page", page);

			return INPUT;
		}

		if (StringUtils.isNotEmpty(belong)) {
			dc = dc.add(Restrictions.eq("belong", belong));
			dc = dc.add(Restrictions.ne("type", 516));

			if (proposalFlag != null) {
				dc = dc.add(Restrictions.eq("flag", proposalFlag));
			}

			Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "actualmoney", null, null, null);
			getRequest().setAttribute("page", page);

			return INPUT;
		}

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("depositname", loginname));
		}

		if (StringUtils.isNotEmpty(proposalType)) {
			dc = dc.add(Restrictions.eq("type", Integer.valueOf(Integer.parseInt(proposalType))));
		}

		if (proposalFlag != null) {
			dc = dc.add(Restrictions.eq("flag", proposalFlag));
		}

		if (bankaccount != null && !bankaccount.equals("")) {
			dc = dc.add(Restrictions.eq("bankaccount", bankaccount));
		}
		if (bankinfoid != null) {
			dc = dc.add(Restrictions.eq("bankaccountid", bankinfoid));
		}
		if (bankname != null && !bankname.equals("")) {
			dc = dc.add(Restrictions.like("depositbank", bankname, MatchMode.ANYWHERE));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null) {
			dc = dc.add(Restrictions.ge("createTime", start));
		}

		if (end != null) {
			dc = dc.add(Restrictions.lt("createTime", end));
		}

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "actualmoney", null, null, o);
		getRequest().setAttribute("page", page);

		return INPUT;
	}

	public String queryIntransferProposal() {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		if (StringUtils.isNotEmpty(pno)) {
			dc = dc.add(Restrictions.eq("pno", pno));
			dc = dc.add(Restrictions.eq("type", 516));
			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
			getRequest().setAttribute("page", page);
			return INPUT;
		}

		dc = dc.add(Restrictions.eq("type", 516));

		if (StringUtils.isNotEmpty(loginname)) {
			if (proposalFlag == 0) {
				dc.add(Restrictions.like("loginname", loginname,MatchMode.ANYWHERE));
			} else if (proposalFlag == 1) {
				dc.add(Restrictions.like("loginname", loginname + "->", MatchMode.START));
			} else if (proposalFlag == 2) {
				dc.add(Restrictions.like("loginname", "->" + loginname, MatchMode.END));
			}

		}
		
		
		if (StringUtils.isNotEmpty(transferOut)) {
			dc.add(Restrictions.like("generateType", transferOut + "->", MatchMode.START));
		}	
		if (StringUtils.isNotEmpty(transferIn)) {
			dc.add(Restrictions.like("generateType", "->" + transferIn, MatchMode.END));
		}
		
		if (proposalStatus != null)
			dc = dc.add(Restrictions.eq("flag", proposalStatus));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createTime", end));

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryProposalDetail() {
		Proposal p = (Proposal) operatorService.get(Proposal.class, pno);
		// System.out.println(p.getPno());
		if (p != null) {
			getRequest().setAttribute("proposal", p);
			/*if (ProposalType.NEWACCOUNT.getCode().toString().equals(proposalType)) {
				Newaccount newAccount = (Newaccount) operatorService.get(Newaccount.class, pno);
				getRequest().setAttribute("newAccount", newAccount);
			} else*/ if (ProposalType.CASHIN.getCode().toString().equals(proposalType)|| ProposalType.JANPAY.getCode().toString().equals(proposalType)) {
				Cashin cashin = (Cashin) operatorService.get(Cashin.class, pno);
				getRequest().setAttribute("cashin", cashin);

			} else if (ProposalType.CASHOUT.getCode().toString().equals(proposalType)||ProposalType.PROXYADVANCE.getCode().toString().equals(proposalType)) {
				/**
				 * 判断为民生秒付银行
				 */
				/*DetachedCriteria isSecondDc = DetachedCriteria.forClass(Bankinfo.class);
				isSecondDc.add(Restrictions.eq("type", 2));
				isSecondDc.add(Restrictions.eq("useable", 0));
				isSecondDc.add(Restrictions.eq("bankname", "民生银行"));
				isSecondDc.add(Restrictions.like("remark", "%秒付%"));
				isSecondDc.setProjection(Projections.property("id"));
				List<Integer> list = this.getProposalService().findByCriteria(isSecondDc);
				if(list.size()==1){
					getRequest().setAttribute("isMsSecondId", list.get(0).toString());
				}*/
				
				
				Cashout cashout = (Cashout) operatorService.get(Cashout.class, pno);
				getRequest().setAttribute("cashout", cashout);

				// 输出审核结果
				Map<String, AuditDetail> map = (Map<String, AuditDetail>) getRequest().getSession().getServletContext().getAttribute(Constants.AUDIT_DETAIL);
				if (map == null) {
					map = new HashMap<String, AuditDetail>();
					getRequest().getSession().getServletContext().setAttribute(Constants.AUDIT_DETAIL, map);
				}

				if (map.get(p.getPno()) == null) {
					AuditDetail auditDetail = new AuditDetail(p.getLoginname(), p.getPno(), null, null, null, null);
					log.info("查找上次成功提款的提案");
					Proposal lastCashout = proposalService.getLastSuccCashout(p.getLoginname(), p.getCreateTime()); // 小于本次提款时间的提案记录

					if (lastCashout == null || lastCashout.getAfterLocalAmount() == null || lastCashout.getAfterRemoteAmount() == null) {
						log.info("上次成功提款提案为空，确定为首次提款");
						auditDetail.setOutput("用户首次提款");
					} else if (lastCashout.getAfterLocalAmount() == -1.0 || lastCashout.getAfterRemoteAmount() == -1.0) {
						log.info("上次提款[" + lastCashout.getPno() + "]瞬时额度记录为空");
						auditDetail.setOutput("上次提款瞬时额度记录为空");
					} else if (p.getAfterLocalAmount() == -1.0 || p.getAfterRemoteAmount() == -1.0) {
						log.info("本次提款[" + p.getPno() + "]瞬时额度记录为空");
						auditDetail.setOutput("本次提款瞬时额度记录为空");
					} else {
						log.info("上次提款提案号[" + lastCashout.getPno() + "]");
						Double lastafterAgRemoteAmount = lastCashout.getAfterAgRemoteAmount();
						if(lastafterAgRemoteAmount == null || lastafterAgRemoteAmount==-1.0){
							lastCashout.setAfterAgRemoteAmount(0.0);
							lastafterAgRemoteAmount =0.0;
						}
						Double lastafterAgInRemoteAmount = lastCashout.getAfterAgInRemoteAmount();
						if (lastafterAgInRemoteAmount == null || lastafterAgInRemoteAmount == -1.0) {
							lastCashout.setAfterAgInRemoteAmount(0.0);
							lastafterAgInRemoteAmount = 0.0;
						}
						
						Double lastafterBbinRemoteAmount = lastCashout.getAfterBbinRemoteAmount();
						if(lastafterBbinRemoteAmount == null || lastafterBbinRemoteAmount==-1.0){
							lastCashout.setAfterBbinRemoteAmount(0.0);
							lastafterBbinRemoteAmount =0.0;
						}
						Double lastafterKenoRemoteAmount = lastCashout.getAfterKenoRemoteAmount();
						if(lastafterKenoRemoteAmount == null || lastafterKenoRemoteAmount==-1.0){
							lastCashout.setAfterKenoRemoteAmount(0.0);
							lastafterKenoRemoteAmount =0.0;
						}
						Double lastTotalAmount = lastCashout.getAfterLocalAmount() + lastCashout.getAfterRemoteAmount()+lastafterAgRemoteAmount+lastafterAgInRemoteAmount+lastafterBbinRemoteAmount+lastafterKenoRemoteAmount;
						
						Double currentafterAgRemoteAmount = p.getAfterAgRemoteAmount();
						if(currentafterAgRemoteAmount == null || currentafterAgRemoteAmount==-1.0){
							p.setAfterAgRemoteAmount(0.0);
							currentafterAgRemoteAmount =0.0;
						}
						Double currentafterAgInRemoteAmount = p.getAfterAgInRemoteAmount();
						if (currentafterAgInRemoteAmount == null || currentafterAgInRemoteAmount == -1.0) {
							p.setAfterAgInRemoteAmount(0.0);
							currentafterAgInRemoteAmount = 0.0;
						}
						Double currentafterBbinRemoteAmount = p.getAfterBbinRemoteAmount();
						if(currentafterBbinRemoteAmount == null || currentafterBbinRemoteAmount==-1.0){
							p.setAfterBbinRemoteAmount(0.0);
							currentafterBbinRemoteAmount =0.0;
						}
						Double currentafterKenoRemoteAmount = p.getAfterKenoRemoteAmount();
						if(currentafterKenoRemoteAmount == null || currentafterKenoRemoteAmount==-1.0){
							p.setAfterKenoRemoteAmount(0.0);
							currentafterKenoRemoteAmount =0.0;
						}
						// 本次提款前的总额度
						Double currentTotalAmount = p.getAfterLocalAmount() + p.getAfterRemoteAmount() + p.getAmount()+currentafterAgRemoteAmount+currentafterAgInRemoteAmount+currentafterBbinRemoteAmount+currentafterKenoRemoteAmount;
						log.info("查询据上次提款之间的本地转入转出额度变化");
						Double localCreditChange = operatorService.getLocalCreditChangeByPeriod(p.getLoginname(), lastCashout.getCreateTime(), p.getCreateTime());
						log.info("localCreditChange:" + localCreditChange);
						Double loseAmount = currentTotalAmount - lastTotalAmount - localCreditChange;
						log.info("计算出预计的输赢值是" + loseAmount);
						auditDetail.setLastTime(lastCashout.getCreateTime());
						auditDetail.setCurrentTime(p.getCreateTime());
						auditDetail.setLastTotalAmount(lastTotalAmount);
						auditDetail.setCurrentTotalAmount(currentTotalAmount);
						auditDetail.setLocalCreditChange(localCreditChange);
						auditDetail.setLoseAmount(loseAmount);
						/**
						 * 上次和当前(本地、所有平台瞬时额度)
						 */
						auditDetail.setLastCashout(lastCashout);
						auditDetail.setCurrentCashout(p);
					}
					map.put(p.getPno(), auditDetail);
				}
				getRequest().getSession().getServletContext().setAttribute(Constants.AUDIT_DETAIL, map);

			} /*else if (ProposalType.CONCESSIONS.getCode().toString().equals(proposalType)) {
				Concessions concessions = (Concessions) operatorService.get(Concessions.class, pno);
				getRequest().setAttribute("concessions", concessions);
			} else if (ProposalType.REBANKINFO.getCode().toString().equals(proposalType)) {
				Rebankinfo rebankinfo = (Rebankinfo) operatorService.get(Rebankinfo.class, pno);
				getRequest().setAttribute("rebankinfo", rebankinfo);
			}*/ else if (ProposalType.XIMA.getCode().toString().equals(proposalType) || ProposalType.SELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.SELFXIMAPT.getCode().toString().equals(proposalType)
					|| ProposalType.AGSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.AGINSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.BBINSELFXIMA.getCode().toString().equals(proposalType)
					|| ProposalType.KGSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.SBSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.PTTIGERSELFXIMA.getCode().toString().equals(proposalType)
					|| ProposalType.PTOTHERSELFXIMA.getCode().toString().equals(proposalType) //|| ProposalType.SIXLOTTERYSELFXIMA.getCode().toString().equals(proposalType) //|| ProposalType.KENOSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.GPISELFXIMA.getCode().toString().equals(proposalType)
					|| ProposalType.TTGSELFXIMA.getCode().toString().equals(proposalType) || ProposalType.QTSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.NTSELFXIMA.getCode().toString().equals(proposalType)
					|| ProposalType.DTSELFXIMA.getCode().toString().equals(proposalType) || ProposalType.MGSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.PNGSELFXIMA.getCode().toString().equals(proposalType)|| ProposalType.PTSKYSELFXIMA.getCode().toString().equals(proposalType)) {
				Xima xima = (Xima) operatorService.get(Xima.class, pno);
				Users user = (Users) userDao.get(Users.class, xima.getLoginname());
				getRequest().setAttribute("xima", xima);
				getRequest().setAttribute("ximaUser", user);
			} else if (ProposalType.PROFIT.getCode().toString().equals(proposalType)) {
				/*Xima xima = (Xima) operatorService.get(Xima.class, pno);
				Users user = (Users) userDao.get(Users.class, xima.getLoginname());
				getRequest().setAttribute("xima", xima);
				getRequest().setAttribute("ximaUser", user);*/
				LosePromo losePromo = (LosePromo) operatorService.get(LosePromo.class, pno);
				getRequest().setAttribute("losePromo", losePromo);
			} else if (ProposalType.WEEKSENT.getCode().toString().equals(proposalType)) {
				WeekSent weekSent = (WeekSent) operatorService.get(WeekSent.class, pno);
				getRequest().setAttribute("weekSent", weekSent);
			} /*else if (ProposalType.BANKTRANSFERCONS.getCode().toString().equals(proposalType)) {
				Banktransfercons cons = (Banktransfercons) operatorService.get(Banktransfercons.class, pno);
				getRequest().setAttribute("bankTransferCons", cons);
			}*/ else if (ProposalType.OFFER.getCode().toString().equals(proposalType) //|| ProposalType.SELFPT94.getCode().toString().equals(proposalType) || ProposalType.SELFPT93.getCode().toString().equals(proposalType) 
					||ProposalType.SELFPT91.getCode().toString().equals(proposalType)||ProposalType.SELFPT90.getCode().toString().equals(proposalType)//||ProposalType.SELFPT95.getCode().toString().equals(proposalType)
					||ProposalType.SELFPT92.getCode().toString().equals(proposalType)||ProposalType.SELFEBET98.getCode().toString().equals(proposalType)||ProposalType.SELFEBET99.getCode().toString().equals(proposalType)//||ProposalType.SELFGPIFIRST.getCode().toString().equals(proposalType) || ProposalType.SELFGPITWICE.getCode().toString().equals(proposalType)||ProposalType.SELFGPI704.getCode().toString().equals(proposalType)
					||ProposalType.SELFPT705.getCode().toString().equals(proposalType)||ProposalType.SELFTTG706.getCode().toString().equals(proposalType)
					||ProposalType.SELFQTFIRST.getCode().toString().equals(proposalType)||ProposalType.SELFQTTWICE.getCode().toString().equals(proposalType)||ProposalType.SELFQTSPEC.getCode().toString().equals(proposalType)
					||ProposalType.SELFNTFIRST.getCode().toString().equals(proposalType)||ProposalType.SELFNTTWICE.getCode().toString().equals(proposalType)||ProposalType.SELFNTSPEC.getCode().toString().equals(proposalType)
					||ProposalType.SELFDTFIRST.getCode().toString().equals(proposalType)||ProposalType.SELFDTTWICE.getCode().toString().equals(proposalType)||ProposalType.SELFDTSPEC.getCode().toString().equals(proposalType)
					//||ProposalType.SELFPNGFIRST.getCode().toString().equals(proposalType)||ProposalType.SELFPNGTWICE.getCode().toString().equals(proposalType)||ProposalType.SELFPNGSPEC.getCode().toString().equals(proposalType)
					||ProposalType.SELFAGINFIRST.getCode().toString().equals(proposalType)||ProposalType.SELFAGINTWICE.getCode().toString().equals(proposalType)||ProposalType.SELFAGINSPEC.getCode().toString().equals(proposalType)
					||ProposalType.SELFMGFIRST.getCode().toString().equals(proposalType)||ProposalType.SELFMGTWICE.getCode().toString().equals(proposalType)||ProposalType.SELFMGSPEC.getCode().toString().equals(proposalType)) {
				Offer offer = (Offer) operatorService.get(Offer.class, pno);
				getRequest().setAttribute("offer", offer);
			} else if (ProposalType.PRIZE.getCode().toString().equals(proposalType)) {
				Prize prize = (Prize) operatorService.get(Prize.class, pno);
				getRequest().setAttribute("prize", prize);
			} else if (ProposalType.BIRTHDAY.getCode().toString().equals(proposalType)) {
				Prize prize = (Prize) operatorService.get(Prize.class, pno);
				getRequest().setAttribute("prize", prize);
			} else if (ProposalType.LEVELPRIZE.getCode().toString().equals(proposalType)) {
				Prize prize = (Prize) operatorService.get(Prize.class, pno);
				getRequest().setAttribute("prize", prize);
			} else if ("516".equals(proposalType)) {
				Intransfer intransfer = (Intransfer) operatorService.get(Intransfer.class, pno);
				getRequest().setAttribute("intransfer", intransfer);
			} else if(ProposalType.PTBIGBANG.getCode().toString().equals(proposalType)){
				DetachedCriteria dc = DetachedCriteria.forClass(PTBigBang.class).add(Restrictions.eq("remark", pno));
				PTBigBang ptBigBang = (PTBigBang) operatorService.getHibernateTemplate().findByCriteria(dc).get(0);
				getRequest().setAttribute("ptBigBang", ptBigBang);
			}
		} else {
			addFieldError("errorMessage", "找不到该提案记录");
		}
		return SUCCESS;
	}

	public String queryBusinessProposalDetail() {
		BusinessProposal p = (BusinessProposal) operatorService.get(BusinessProposal.class, pno);
		// System.out.println(p.getPno());
		if (p != null) {
			getRequest().setAttribute("proposal", p);
			getHttpSession().setAttribute("businessProposal", p);
		} else {
			addFieldError("errorMessage", "找不到该提案记录");
		}
		return SUCCESS;
	}

	public String queryProposalAuditDetail() {
		Proposal p = (Proposal) operatorService.get(Proposal.class, pno);
		if (p != null) {
			getRequest().setAttribute("proposal", p);
			/*if (ProposalType.NEWACCOUNT.getCode().toString().equals(proposalType)) {
				Newaccount newAccount = (Newaccount) operatorService.get(Newaccount.class, pno);
				getRequest().setAttribute("newAccount", newAccount);
			} else */if (ProposalType.CASHIN.getCode().toString().equals(proposalType)) {
				Cashin cashin = (Cashin) operatorService.get(Cashin.class, pno);
				getRequest().setAttribute("cashin", cashin);

			} else if (ProposalType.CASHOUT.getCode().toString().equals(proposalType)||ProposalType.PROXYADVANCE.getCode().toString().equals(proposalType)) {
				Cashout cashout = (Cashout) operatorService.get(Cashout.class, pno);
				getRequest().setAttribute("cashout", cashout);

				Userstatus userstatus = (Userstatus) operatorService.get(Userstatus.class, p.getLoginname());
				if (userstatus != null) {
					if (userstatus.getTouzhuflag() == null) {
						userstatus.setTouzhuflag(0);
					}
					if (userstatus.getFirstCash() == null)
						userstatus.setFirstCash(0.0);
					if (userstatus.getAmount() == null)
						userstatus.setAmount(0.0);
					if (userstatus.getTimes() == null) {
						userstatus.setTimes(0);
					}
					getRequest().setAttribute("totalamount", userstatus.getTimes() * (userstatus.getFirstCash() + userstatus.getAmount()));
				}

				getRequest().setAttribute("userstatus", userstatus);
				if (ProposalType.CASHOUT.getCode().toString().equals(proposalType)) {

					DetachedCriteria cashindc = DetachedCriteria.forClass(Proposal.class);
					DetachedCriteria cashoutdc = DetachedCriteria.forClass(Proposal.class);
					if (StringUtils.isNotEmpty(p.getLoginname())) {
						cashindc = cashindc.add(Restrictions.eq("loginname", p.getLoginname()));
						cashoutdc = cashoutdc.add(Restrictions.eq("loginname", p.getLoginname()));
					}
					cashindc = cashindc.add(Restrictions.eq("type", 502));
					cashoutdc = cashoutdc.add(Restrictions.eq("type", 503));

					cashindc = cashindc.add(Restrictions.eq("flag", 2));
					cashoutdc = cashoutdc.add(Restrictions.eq("flag", 2));

					Page cashinpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), cashindc, pageIndex, 5000, "amount", "", "", null);
					getRequest().setAttribute("cashinpage", cashinpage);

					Page cashoutpage = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), cashoutdc, pageIndex, 5000, "amount", "", "", null);
					getRequest().setAttribute("cashoutpage", cashoutpage);
					if (cashoutpage.getStatics1() - cashinpage.getStatics1() + p.getAmount() > 100000) {// 总提减去总存
						getRequest().setAttribute("overshiwan", 1);
					} else {
						getRequest().setAttribute("overshiwan", 0);
					}
				}

				// 输出审核结果
				Map<String, AuditDetail> map = (Map<String, AuditDetail>) getRequest().getSession().getServletContext().getAttribute(Constants.AUDIT_DETAIL);
				if (map == null) {
					map = new HashMap<String, AuditDetail>();
					getRequest().getSession().getServletContext().setAttribute(Constants.AUDIT_DETAIL, map);
				}

				if (map.get(p.getPno()) == null) {
					AuditDetail auditDetail = new AuditDetail(p.getLoginname(), p.getPno(), null, null, null, null);
					log.info("查找上次成功提款的提案");
					Proposal lastCashout = proposalService.getLastSuccCashout(p.getLoginname(), p.getCreateTime()); // 小于本次提款时间的提案记录

					if (lastCashout == null || lastCashout.getAfterLocalAmount() == null || lastCashout.getAfterRemoteAmount() == null) {
						log.info("上次成功提款提案为空，确定为首次提款");
						auditDetail.setOutput("用户首次提款");
					} else if (lastCashout.getAfterLocalAmount() == -1.0 || lastCashout.getAfterRemoteAmount() == -1.0) {
						log.info("上次提款[" + lastCashout.getPno() + "]瞬时额度记录为空");
						auditDetail.setOutput("上次提款瞬时额度记录为空");
					} else if (p.getAfterLocalAmount() == -1.0 || p.getAfterRemoteAmount() == -1.0) {
						log.info("本次提款[" + p.getPno() + "]瞬时额度记录为空");
						auditDetail.setOutput("本次提款瞬时额度记录为空");
					} else {
						log.info("上次提款提案号[" + lastCashout.getPno() + "]");
						
						Double lastafterAgRemoteAmount = lastCashout.getAfterAgRemoteAmount();
						if(lastafterAgRemoteAmount == null || lastafterAgRemoteAmount==-1.0){
							lastCashout.setAfterAgRemoteAmount(0.0);
							lastafterAgRemoteAmount =0.0;
						}
						Double lastafterBbinRemoteAmount = lastCashout.getAfterBbinRemoteAmount();
						if(lastafterBbinRemoteAmount == null || lastafterBbinRemoteAmount==-1.0){
							lastCashout.setAfterBbinRemoteAmount(0.0);
							lastafterBbinRemoteAmount =0.0;
						}
						Double lastafterKenoRemoteAmount = lastCashout.getAfterBbinRemoteAmount();
						if(lastafterKenoRemoteAmount == null || lastafterKenoRemoteAmount==-1.0){
							lastCashout.setAfterKenoRemoteAmount(0.0);
							lastafterKenoRemoteAmount =0.0;
						}
						Double lastTotalAmount = lastCashout.getAfterLocalAmount() + lastCashout.getAfterRemoteAmount()+lastafterAgRemoteAmount+lastafterBbinRemoteAmount+lastafterKenoRemoteAmount;
						
						Double currentafterAgRemoteAmount = p.getAfterAgRemoteAmount();
						if(currentafterAgRemoteAmount == null || currentafterAgRemoteAmount==-1.0){
							p.setAfterAgRemoteAmount(0.0);
							currentafterAgRemoteAmount =0.0;
						}
						Double currentafterBbinRemoteAmount = p.getAfterBbinRemoteAmount();
						if(currentafterBbinRemoteAmount == null || currentafterBbinRemoteAmount==-1.0){
							p.setAfterBbinRemoteAmount(0.0);
							currentafterBbinRemoteAmount =0.0;
						}
						Double currentafterKenoRemoteAmount = p.getAfterKenoRemoteAmount();
						if(currentafterKenoRemoteAmount == null || currentafterKenoRemoteAmount==-1.0){
							p.setAfterKenoRemoteAmount(0.0);
							currentafterKenoRemoteAmount =0.0;
						}
						// 本次提款前的总额度
						Double currentTotalAmount = p.getAfterLocalAmount() + p.getAfterRemoteAmount() + p.getAmount()+currentafterAgRemoteAmount+currentafterBbinRemoteAmount+currentafterKenoRemoteAmount;
						log.info("查询据上次提款之间的本地转入转出额度变化");
						Double localCreditChange = operatorService.getLocalCreditChangeByPeriod(p.getLoginname(), lastCashout.getCreateTime(), p.getCreateTime());
						log.info("localCreditChange:" + localCreditChange);
						Double loseAmount = currentTotalAmount - lastTotalAmount - localCreditChange;
						log.info("计算出预计的输赢值是" + loseAmount);
						auditDetail.setLastTime(lastCashout.getCreateTime());
						auditDetail.setCurrentTime(p.getCreateTime());
						auditDetail.setLastTotalAmount(lastTotalAmount);
						auditDetail.setCurrentTotalAmount(currentTotalAmount);
						auditDetail.setLocalCreditChange(localCreditChange);
						auditDetail.setLoseAmount(loseAmount);
						/**
						 * 上次和当前(本地、所有平台瞬时额度)
						 */
						auditDetail.setLastCashout(lastCashout);
						auditDetail.setCurrentCashout(p);
					}
					map.put(p.getPno(), auditDetail);
				}
				getRequest().getSession().getServletContext().setAttribute(Constants.AUDIT_DETAIL, map);

			} /*else if (ProposalType.CONCESSIONS.getCode().toString().equals(proposalType)) {
				Concessions concessions = (Concessions) operatorService.get(Concessions.class, pno);
				getRequest().setAttribute("concessions", concessions);
			} else if (ProposalType.REBANKINFO.getCode().toString().equals(proposalType)) {
				Rebankinfo rebankinfo = (Rebankinfo) operatorService.get(Rebankinfo.class, pno);
				getRequest().setAttribute("rebankinfo", rebankinfo);
			}*/ else if (ProposalType.XIMA.getCode().toString().equals(proposalType) || ProposalType.SELFXIMA.getCode().toString().equals(proposalType)) {
				Xima xima = (Xima) operatorService.get(Xima.class, pno);
				Users user = (Users) userDao.get(Users.class, xima.getLoginname());
				getRequest().setAttribute("xima", xima);
				getRequest().setAttribute("ximaUser", user);
			} /*else if (ProposalType.BANKTRANSFERCONS.getCode().toString().equals(proposalType)) {
				Banktransfercons cons = (Banktransfercons) operatorService.get(Banktransfercons.class, pno);
				getRequest().setAttribute("bankTransferCons", cons);
			}*/ else if (ProposalType.OFFER.getCode().toString().equals(proposalType)) {
				Offer offer = (Offer) operatorService.get(Offer.class, pno);
				getRequest().setAttribute("offer", offer);
			} else if (ProposalType.PRIZE.getCode().toString().equals(proposalType)) {
				Prize prize = (Prize) operatorService.get(Prize.class, pno);
				getRequest().setAttribute("prize", prize);
			} else if ("516".equals(proposalType)) {
				Intransfer intransfer = (Intransfer) operatorService.get(Intransfer.class, pno);
				getRequest().setAttribute("intransfer", intransfer);
			}
		} else {
			addFieldError("errorMessage", "找不到该提案记录");
		}
		return SUCCESS;
	}

	public String queryTaskDetail() {
		Proposal p = (Proposal) operatorService.get(Proposal.class, pno);
		if (p != null) {
			DetachedCriteria dc = DetachedCriteria.forClass(Task.class).add(Restrictions.eq("pno", pno)).addOrder(Order.asc("level"));
			List<Task> list = operatorService.getHibernateTemplate().findByCriteria(dc);
			Task auditedTask = null, excutedTask = null, cancledTask = null;
			if (p.getFlag().intValue() == ProposalFlagType.AUDITED.getCode().intValue() && list.size() > 0) {
				auditedTask = list.get(0);
			} else if (p.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue() && list.size() > 1) {
				auditedTask = list.get(0);
				excutedTask = list.get(1);
			} else if (p.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue() && list.size() > 0) {
				cancledTask = list.get(0);
			}
			getRequest().setAttribute("createTime", p.getCreateTime());
			getRequest().setAttribute("pno", pno);
			getRequest().setAttribute("flag", p.getFlag());
			getRequest().setAttribute("auditedTask", auditedTask);
			getRequest().setAttribute("excutedTask", excutedTask);
			getRequest().setAttribute("cancledTask", cancledTask);
		} else {
			addFieldError("errorMessage", "找不到该提案记录");
		}
		return SUCCESS;
	}

	public String queryBusinessTaskDetail() {
		BusinessProposal p = (BusinessProposal) operatorService.get(BusinessProposal.class, pno);
		if (p != null) {
			DetachedCriteria dc = DetachedCriteria.forClass(Task.class).add(Restrictions.eq("pno", pno)).addOrder(Order.asc("level"));
			List<Task> list = operatorService.getHibernateTemplate().findByCriteria(dc);
			Task auditedTask = null, excutedTask = null, cancledTask = null;
			if (p.getFlag().intValue() == ProposalFlagType.AUDITED.getCode().intValue() && list.size() > 0) {
				auditedTask = list.get(0);
			} else if (p.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue() && list.size() > 1) {
				auditedTask = list.get(0);
				excutedTask = list.get(1);
			} else if (p.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue() && list.size() > 0) {
				cancledTask = list.get(0);
			}
			getRequest().setAttribute("createTime", p.getCreateTime());
			getRequest().setAttribute("pno", pno);
			getRequest().setAttribute("flag", p.getFlag());
			getRequest().setAttribute("auditedTask", auditedTask);
			getRequest().setAttribute("excutedTask", excutedTask);
			getRequest().setAttribute("cancledTask", cancledTask);
		} else {
			addFieldError("errorMessage", "找不到该提案记录");
		}
		return SUCCESS;
	}

	// 查询实时转账记录
	public String queryRemoteTransfer() {
		if (start == null || end == null)
			setErrormsg("请输入开始时间和结束时间");
		else {
			Page page = new Page();
			try {
				Boolean isIn = null;
				if (isTransferIn != null)
					isIn = isTransferIn == Constants.FLAG_TRUE ? true : false;
				// page.setPageContents(RemoteCaller.getTransferRecord(loginname,
				// start, end, isIn));
				getRequest().setAttribute("page", page);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg(e.getMessage());
			}
		}
		return INPUT;
	}

	public String queryTransfer() {
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		if (isTransferIn != null)
			dc = dc.add(Restrictions.eq("source", isTransferIn.intValue() != Constants.FLAG_TRUE.intValue() ? ((Object) (RemoteConstant.PAGESITE)) : ((Object) (RemoteConstant.WEBSITE))));
		if (transferFalg != null)
			dc = dc.add(Restrictions.eq("flag", transferFalg));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(by)) {
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);

		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryUser() {
		Page page = new Page();
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		loginname = StringUtil.trim(loginname);
		dc.add(Restrictions.ne("loginname", "admin"));
		warnremark = StringUtil.trim(warnremark);
		if (warnremark != null  &&  StringUtils.isNotEmpty(warnremark)   ) {
			dc = dc.add(Restrictions.ilike("warnremark", "%"+warnremark+"%"));
	    }
		agent = StringUtil.trim(agent);
		if (StringUtils.isNotEmpty(roleCode))
			dc = dc.add(Restrictions.eq("role", roleCode));
		if (warnflag != null)
			dc = dc.add(Restrictions.eq("warnflag", warnflag));// sun
		if (intro != null && !"".equals(intro)) {
			dc = dc.add(Restrictions.eq("intro", intro));
		}
		if (qq != null && !"".equals(qq)) {
			dc = dc.add(Restrictions.eq("qq", qq));
		}
		if (StringUtils.isNotBlank(partner)) {
			dc = dc.add(Restrictions.eq("partner", partner));
		}
		if (StringUtils.isNotBlank(lastLoginIp)) {
			dc = dc.add(Restrictions.eq("lastLoginIp", lastLoginIp));
		}
		if (StringUtils.isNotBlank(sms)) {
			dc = dc.add(Restrictions.eq("sms", Integer.parseInt(sms)));
		}
		if (invitecode != null && !"".equals(invitecode)) {
			dc = dc.add(Restrictions.eq("invitecode", invitecode));
		}
		if (level != null)
			dc = dc.add(Restrictions.eq("level", level));// sun
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("flag", Integer.parseInt(status)));
		if (StringUtils.isNotEmpty(isCashin)) {
			// 当提案中的角色类型不为空：
			if (Integer.parseInt(isCashin) == 0) {
				//有存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode())).add(Restrictions.eq("type", PayType.SUCESS.getCode()));
				dc.add( Restrictions.or(Property.forName("loginname").in(userDC),Restrictions.eq("isCashin", Integer.parseInt(isCashin))));
			}else{
				//未存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
				dc.add( Property.forName("loginname").notIn(userDC));
				dc = dc.add(Restrictions.eq("isCashin", Integer.parseInt(isCashin)));	
			}
		}
		
		   Calendar cal = Calendar.getInstance(); 
	        cal.set(Calendar.HOUR_OF_DAY, 0); 
	        cal.set(Calendar.SECOND, 0); 
	        cal.set(Calendar.MINUTE, 0); 
	        cal.set(Calendar.MILLISECOND, 0); 
			//判断7天未游戏玩家
			if(dayNumflag.equals("0")){
			        cal.add(Calendar.DATE, - 7);  
			        Date monday = cal.getTime();
			    	dc = dc.add(Restrictions.le("lastLoginTime", monday));
			}
			//判断15天未游戏玩家
	       if(dayNumflag.equals("1")){
	    	    cal.add(Calendar.DATE, - 15);  
		        Date monday = cal.getTime();
		    	dc = dc.add(Restrictions.le("lastLoginTime", monday));
			}
	       //判断30天未游戏玩家
	       if(dayNumflag.equals("2")){
	    	    cal.add(Calendar.DATE, - 31);  
		        Date monday = cal.getTime();
		    	dc = dc.add(Restrictions.le("lastLoginTime", monday));
			}
	       //判断30天以上未游戏玩家
	       if(dayNumflag.equals("3")){
	    	    cal.add(Calendar.DATE, - 31);  
		        Date monday = cal.getTime();
		    	dc = dc.add(Restrictions.lt("lastLoginTime", monday));
			}
	       //增加条件
	       if (StringUtil.isNotEmpty(callDayNum)) {
				DetachedCriteria dcc = DetachedCriteria.forClass(CallInfo.class);
				// 判断3天未拨打游戏玩家
				if ("0".equals(callDayNum)) {
					cal.add(Calendar.DATE, -3);
					Date monday = cal.getTime();
					dcc = dcc.add(Restrictions.ge("createtime", monday));
				}
				// 判断7天未拨打游戏玩家
				if ("1".equals(callDayNum)) {
					cal.add(Calendar.DATE, -7);
					Date monday = cal.getTime();
					dcc = dcc.add(Restrictions.ge("createtime", monday));
				}
				// 判断15天未拨打游戏玩家
				if ("2".equals(callDayNum)) {
					cal.add(Calendar.DATE, -15);
					Date monday = cal.getTime();
					dcc = dcc.add(Restrictions.ge("createtime", monday));
				}
				// 判断30天未拨打游戏玩家
				if ("3".equals(callDayNum)) {
					cal.add(Calendar.DATE, -31);
					Date monday = cal.getTime();
					dcc = dcc.add(Restrictions.ge("createtime", monday));
				}
				List<String> tempList = new ArrayList<String>();
				List<CallInfo> list = slaveService.getHibernateTemplate().findByCriteria(dcc);
				for (CallInfo call : list) {
					String phone = call.getCalled();
					try {
						String aPhone = AESUtil.aesEncrypt(phone, AESUtil.KEY);
						tempList.add(aPhone);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				log.info("打过电话的有" + tempList.size());
				if (null != tempList && tempList.size() > 0) {
					dc = dc.add(Restrictions.not(Restrictions.in("phone", tempList)));
				}
			}
		
		
		if (StringUtil.isEmpty(dayNumflag) && StringUtils.isEmpty(birthday) && StringUtils.isEmpty(loginname) && StringUtils.isEmpty(randnum) && StringUtils.isEmpty(aliasName) && StringUtils.isEmpty(accountName) && id == null && StringUtils.isEmpty(registerIp) && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(accountNo) && StringUtils.isEmpty(referWebsite) && StringUtils.isEmpty(agent) && StringUtils.isEmpty(howToKnow)) {
			if (StringUtil.isNotEmpty(stringStartTime)) {
				dc = dc.add(Restrictions.ge("createtime", DateUtil.parseDateForStandard(stringStartTime)));
			}
			if (StringUtil.isNotEmpty(stringEndTime)) {
				dc = dc.add(Restrictions.lt("createtime", DateUtil.parseDateForStandard(stringEndTime)));
			}
			Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//				dc = dc.addOrder(o);
			}

			page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(birthday))
			dc = dc.add(Restrictions.sqlRestriction("birthday like '%-"+birthday+"%'"));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (randnum!=null &&!"".equals(randnum))
			dc = dc.add(Restrictions.eq("randnum", randnum));
		if (id != null)
			dc = dc.add(Restrictions.eq("id", id));

		if (StringUtils.isNotEmpty(aliasName))
			dc = dc.add(Restrictions.ilike("aliasName", aliasName, MatchMode.ANYWHERE));
		if (StringUtils.isNotEmpty(agent)){
			dc = dc.add(Restrictions.eq("agent", agent));
			if (StringUtil.isNotEmpty(stringStartTime)) {
				dc = dc.add(Restrictions.ge("createtime", DateUtil.parseDateForStandard(stringStartTime)));
			}
			if (StringUtil.isNotEmpty(stringEndTime)) {
				dc = dc.add(Restrictions.lt("createtime", DateUtil.parseDateForStandard(stringEndTime)));
			}
		}
			
		if (StringUtils.isNotEmpty(accountName))
			dc = dc.add(Restrictions.eq("accountName", accountName));
		if (StringUtils.isNotEmpty(email)){
			try {
				email = AESUtil.aesEncrypt(email,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("email", email));
		}
		if (StringUtils.isNotEmpty(phone)){
			try {
				phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("phone", phone));
		}
		if (StringUtils.isNotEmpty(registerIp))
			dc = dc.add(Restrictions.eq("registerIp", registerIp));
		if (StringUtils.isNotEmpty(howToKnow))
			dc = dc.add(Restrictions.eq("howToKnow", howToKnow));
		if (StringUtils.isNotEmpty(referWebsite)) {
			dc = dc.add(Restrictions.eq("referWebsite", referWebsite));
			if (StringUtil.isNotEmpty(stringStartTime)) {
				dc = dc.add(Restrictions.ge("createtime", DateUtil.parseDateForStandard(stringStartTime)));
			}
			if (StringUtil.isNotEmpty(stringEndTime)) {
				dc = dc.add(Restrictions.lt("createtime", DateUtil.parseDateForStandard(stringEndTime)));
			}
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		try {
			email = AESUtil.aesDecrypt(email,AESUtil.KEY);
			phone = AESUtil.aesDecrypt(phone,AESUtil.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return INPUT;
	}

	public String activityList() {

		DetachedCriteria dc = DetachedCriteria.forClass(ActivityHistory.class);

		if (StringUtils.isNotEmpty(title)) {

			dc = dc.add(Restrictions.eq("englishtitle", title));
		}



		if (StringUtils.isNotEmpty(loginname)) {

			dc = dc.add(Restrictions.eq("username", loginname));
		}

		if (id!=null) {

			dc = dc.add(Restrictions.eq("activityid", id));
		}


		Order o = null;
		o = Order.desc("createtime");


		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);

		getRequest().setAttribute("page", page);

		return INPUT;
	}


	public String activityConfigSave() {
		try {
			/*String s = new Gson().toJson(activityConfig);*/
			/*String msg =FlashPayUtil.vipFreeSave(s);*/
			ActivityConfig config = this.activityConfig;
			config.setStatus(0);
			config.setCreatetime(new Date());
			proposalService.save(config);
			return GsonUtil.GsonObject("活动配置添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return GsonUtil.GsonObject("活动配置添加失败");
		}


	}

	public String editSaveActivity() {
		try {
			/*String s = new Gson().toJson(activityConfig);*/
			/*String msg =FlashPayUtil.vipFreeSave(s);*/
			ActivityConfig config = this.activityConfig;
			ActivityConfig activityConfig=(ActivityConfig)proposalService.get(ActivityConfig.class,config.getId());
			config.setCreatetime(activityConfig.getCreatetime());
			config.setUpdatetime(new Date());
			config.setStatus(activityConfig.getStatus());
			proposalService.update(config);
			return GsonUtil.GsonObject("修改配置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return GsonUtil.GsonObject("修改配置失败");
		}


	}



	public String changeActivityStatus() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(ActivityConfig.class);
			dc = dc.add(Restrictions.eq("id", id));
			List<ActivityConfig> list = proposalService.findByCriteria(dc);
			if(list!=null||!list.isEmpty()){
				ActivityConfig config = list.get(0);
				config.setStatus(Integer.valueOf(status));
				config.setUpdatetime(new Date());
				proposalService.update(config);
				return GsonUtil.GsonObject("切换成功");
			}
			return GsonUtil.GsonObject("没有此活动配置");
		} catch (Exception e) {
			e.printStackTrace();
			return GsonUtil.GsonObject("切换失败");
		}


	}

	public String deleteActivity() {
		try {
			proposalService.delete(ActivityConfig.class,id);
			return GsonUtil.GsonObject("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return GsonUtil.GsonObject("删除失败");
		}
	}

	public String saveTitle() {
		try {
			ActivityConfig config=(ActivityConfig)proposalService.get(ActivityConfig.class,id);
			config.setTitle(title);
			proposalService.update(config);
			return GsonUtil.GsonObject("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return GsonUtil.GsonObject("修改失败");
		}
	}



	public String editActivity() {
		try {
			ActivityConfig config=(ActivityConfig)proposalService.get(ActivityConfig.class,id);
			getRequest().setAttribute("data",config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;


	}





	public String queryActivityConfig() {
		DetachedCriteria dc = DetachedCriteria.forClass(ActivityConfig.class);
		if (StringUtils.isNotEmpty(title)) {

			dc = dc.add(Restrictions.like("title", title, MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotEmpty(activityBeginTime)) {

			dc = dc.add(Restrictions.le("activitystarttime", DateUtil.parseDateForStandard(activityBeginTime)));
		}

		if (StringUtils.isNotEmpty(activityEndTime)) {

			dc = dc.add(Restrictions.ge("activityendtime", DateUtil.parseDateForStandard(activityEndTime)));
		}

		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);

		return INPUT;
	}




	public String queryUserBackup() {
		Page page = new Page();
		DetachedCriteria dc = DetachedCriteria.forClass(UsersBackup.class);
		loginname = StringUtil.trim(loginname);
		dc.add(Restrictions.ne("loginname", "admin"));

		agent = StringUtil.trim(agent);
		if (StringUtils.isNotEmpty(roleCode))
			dc = dc.add(Restrictions.eq("role", roleCode));
		if (warnflag != null)
			dc = dc.add(Restrictions.eq("warnflag", warnflag));// sun
		if (intro != null && !"".equals(intro)) {
			dc = dc.add(Restrictions.eq("intro", intro));
		}
		if (qq != null && !"".equals(qq)) {
			dc = dc.add(Restrictions.eq("qq", qq));
		}
		if (StringUtils.isNotBlank(partner)) {
			dc = dc.add(Restrictions.eq("partner", partner));
		}
		if (StringUtils.isNotBlank(lastLoginIp)) {
			dc = dc.add(Restrictions.eq("lastLoginIp", lastLoginIp));
		}
		if (invitecode != null && !"".equals(invitecode)) {
			dc = dc.add(Restrictions.eq("invitecode", invitecode));
		}
		if (level != null)
			dc = dc.add(Restrictions.eq("level", level));// sun
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("flag", Integer.parseInt(status)));
		if (StringUtils.isNotEmpty(isCashin)) {
			// 当提案中的角色类型不为空：
			if (Integer.parseInt(isCashin) == 0) {
				//有存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode())).add(Restrictions.eq("type", PayType.SUCESS.getCode()));
				dc.add( Restrictions.or(Property.forName("loginname").in(userDC),Restrictions.eq("isCashin", Integer.parseInt(isCashin))));
			}else{
				//未存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
				dc.add( Property.forName("loginname").notIn(userDC));
				dc = dc.add(Restrictions.eq("isCashin", Integer.parseInt(isCashin)));	
			}
		}
		
		if (StringUtils.isEmpty(loginname) && StringUtils.isEmpty(randnum) && StringUtils.isEmpty(aliasName) && StringUtils.isEmpty(accountName) && id == null && StringUtils.isEmpty(registerIp) && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(accountNo) && StringUtils.isEmpty(referWebsite) && StringUtils.isEmpty(agent) && StringUtils.isEmpty(howToKnow)) {
			if (start != null)
				dc = dc.add(Restrictions.ge("createtime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createtime", end));
			Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//				dc = dc.addOrder(o);
			}

			page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (randnum!=null &&!"".equals(randnum))
			dc = dc.add(Restrictions.eq("randnum", randnum));
		if (id != null)
			dc = dc.add(Restrictions.eq("id", id));

		if (StringUtils.isNotEmpty(aliasName))
			dc = dc.add(Restrictions.ilike("aliasName", aliasName, MatchMode.ANYWHERE));
		if (StringUtils.isNotEmpty(agent))
			dc = dc.add(Restrictions.eq("agent", agent));
		if (StringUtils.isNotEmpty(accountName))
			dc = dc.add(Restrictions.eq("accountName", accountName));
		if (StringUtils.isNotEmpty(email)){
			try {
				email = AESUtil.aesEncrypt(email,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("email", email));
		}
		if (StringUtils.isNotEmpty(phone)){
			try {
				phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("phone", phone));
		}
		if (StringUtils.isNotEmpty(registerIp))
			dc = dc.add(Restrictions.eq("registerIp", registerIp));
		if (StringUtils.isNotEmpty(howToKnow))
			dc = dc.add(Restrictions.eq("howToKnow", howToKnow));
		if (StringUtils.isNotEmpty(referWebsite)) {
			dc = dc.add(Restrictions.eq("referWebsite", referWebsite));
			if (start != null)
				dc = dc.add(Restrictions.ge("createtime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createtime", end));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		try {
			email = AESUtil.aesDecrypt(email,AESUtil.KEY);
			phone = AESUtil.aesDecrypt(phone,AESUtil.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return INPUT;
	}
	

	public String queryUserSms() {
		Page page = new Page();
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		loginname = StringUtil.trim(loginname);
		dc.add(Restrictions.ne("loginname", "admin"));

		agent = StringUtil.trim(agent);
		if (StringUtils.isNotEmpty(roleCode))
			dc = dc.add(Restrictions.eq("role", roleCode));
		if (warnflag != null)
			dc = dc.add(Restrictions.eq("warnflag", warnflag));// sun
		if (intro != null && !"".equals(intro)) {
			dc = dc.add(Restrictions.eq("intro", intro));
		}
		if (qq != null && !"".equals(qq)) {
			dc = dc.add(Restrictions.eq("qq", qq));
		}
		if (StringUtils.isNotBlank(partner)) {
			dc = dc.add(Restrictions.eq("partner", partner));
		}
		if (StringUtils.isNotBlank(lastLoginIp)) {
			dc = dc.add(Restrictions.eq("lastLoginIp", lastLoginIp));
		}
		if (StringUtils.isNotBlank(sms)) {
			dc = dc.add(Restrictions.eq("sms", Integer.parseInt(sms)));
		}
		if (invitecode != null && !"".equals(invitecode)) {
			dc = dc.add(Restrictions.eq("invitecode", invitecode));
		}
		if (level != null)
			dc = dc.add(Restrictions.eq("level", level));// sun
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("flag", Integer.parseInt(status)));
		if (StringUtils.isNotEmpty(isCashin)) {
			// 当提案中的角色类型不为空：
			if (Integer.parseInt(isCashin) == 0) {
				//有存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode())).add(Restrictions.eq("type", PayType.SUCESS.getCode()));
				dc.add( Restrictions.or(Property.forName("loginname").in(userDC),Restrictions.eq("isCashin", Integer.parseInt(isCashin))));
			}else{
				//未存款
				DetachedCriteria userDC = DetachedCriteria.forClass(Payorder.class);
				userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
				dc.add( Property.forName("loginname").notIn(userDC));
				dc = dc.add(Restrictions.eq("isCashin", Integer.parseInt(isCashin)));	
			}
		}
		
		   Calendar cal = Calendar.getInstance(); 
	        cal.set(Calendar.HOUR_OF_DAY, 0); 
	        cal.set(Calendar.SECOND, 0); 
	        cal.set(Calendar.MINUTE, 0); 
	        cal.set(Calendar.MILLISECOND, 0); 
			//判断7天未游戏玩家
			if(dayNumflag.equals("0")){
			        cal.add(Calendar.DATE, - 7);  
			        Date monday = cal.getTime();
			    	dc = dc.add(Restrictions.le("lastLoginTime", monday));
			}
			//判断15天未游戏玩家
	       if(dayNumflag.equals("1")){
	    	    cal.add(Calendar.DATE, - 15);  
		        Date monday = cal.getTime();
		    	dc = dc.add(Restrictions.le("lastLoginTime", monday));
			}
	       //判断30天未游戏玩家
	       if(dayNumflag.equals("2")){
	    	    cal.add(Calendar.DATE, - 31);  
		        Date monday = cal.getTime();
		    	dc = dc.add(Restrictions.le("lastLoginTime", monday));
			}
	       //判断30天以上未游戏玩家
	       if(dayNumflag.equals("3")){
	    	    cal.add(Calendar.DATE, - 31);  
		        Date monday = cal.getTime();
		    	dc = dc.add(Restrictions.lt("lastLoginTime", monday));
			}
			
		
		
		if (StringUtil.isEmpty(dayNumflag) && StringUtils.isEmpty(birthday) && StringUtils.isEmpty(loginname) && StringUtils.isEmpty(randnum) && StringUtils.isEmpty(aliasName) && StringUtils.isEmpty(accountName) && id == null && StringUtils.isEmpty(registerIp) && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(accountNo) && StringUtils.isEmpty(referWebsite) && StringUtils.isEmpty(agent) && StringUtils.isEmpty(howToKnow)) {
			if (StringUtil.isNotEmpty(stringStartTime)) {
				dc = dc.add(Restrictions.ge("createtime", DateUtil.parseDateForStandard(stringStartTime)));
			}
			if (StringUtil.isNotEmpty(stringEndTime)) {
				dc = dc.add(Restrictions.lt("createtime", DateUtil.parseDateForStandard(stringEndTime)));
			}
			Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//				dc = dc.addOrder(o);
			}

			page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(birthday))
			dc = dc.add(Restrictions.sqlRestriction("birthday like '%-"+birthday+"%'"));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (randnum!=null &&!"".equals(randnum))
			dc = dc.add(Restrictions.eq("randnum", randnum));
		if (id != null)
			dc = dc.add(Restrictions.eq("id", id));

		if (StringUtils.isNotEmpty(aliasName))
			dc = dc.add(Restrictions.ilike("aliasName", aliasName, MatchMode.ANYWHERE));
		if (StringUtils.isNotEmpty(agent)){
			dc = dc.add(Restrictions.eq("agent", agent));
			if (StringUtil.isNotEmpty(stringStartTime)) {
				dc = dc.add(Restrictions.ge("createtime", DateUtil.parseDateForStandard(stringStartTime)));
			}
			if (StringUtil.isNotEmpty(stringEndTime)) {
				dc = dc.add(Restrictions.lt("createtime", DateUtil.parseDateForStandard(stringEndTime)));
			}
		}
			
		if (StringUtils.isNotEmpty(accountName))
			dc = dc.add(Restrictions.eq("accountName", accountName));
		if (StringUtils.isNotEmpty(email)){
			try {
				email = AESUtil.aesEncrypt(email,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("email", email));
		}
		if (StringUtils.isNotEmpty(phone)){
			try {
				phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dc = dc.add(Restrictions.eq("phone", phone));
		}
		if (StringUtils.isNotEmpty(registerIp))
			dc = dc.add(Restrictions.eq("registerIp", registerIp));
		if (StringUtils.isNotEmpty(howToKnow))
			dc = dc.add(Restrictions.eq("howToKnow", howToKnow));
		if (StringUtils.isNotEmpty(referWebsite)) {
			dc = dc.add(Restrictions.eq("referWebsite", referWebsite));
			if (StringUtil.isNotEmpty(stringStartTime)) {
				dc = dc.add(Restrictions.ge("createtime", DateUtil.parseDateForStandard(stringStartTime)));
			}
			if (StringUtil.isNotEmpty(stringEndTime)) {
				dc = dc.add(Restrictions.lt("createtime", DateUtil.parseDateForStandard(stringEndTime)));
			}
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		try {
			email = AESUtil.aesDecrypt(email,AESUtil.KEY);
			phone = AESUtil.aesDecrypt(phone,AESUtil.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return INPUT;
	}
	public String downloadConstraintAddressConfig() {
		try {
			log.info("下载约束地址配置 excel");
			//	String fileUrl = FTPProperties.getInstance().getValue("constraintAddressConfigure_excel_download_url") +  "constraintAddressConfigure.xls" ;
			FTPClientUtilX.downLoadFile("constraintAddressConfigure.xls");
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("服务器异常");
		}
		return null;
	}
	public String duanXinQunFa4Internation(){
		PrintWriter out = null ;
		try {
			out = this.getResponse().getWriter();
			this.getRequest().setCharacterEncoding("UTF-8");

			String[] idNu = ids.split(",");
			if(idNu.length<1){
				out.print("您未选中条目。");
			}else{
				List list1  = checkKeyWord(remark);
				if(null!=list1&&list1.size()>0){
					StringBuffer sbf = new StringBuffer();
					for(int i=0;i<list1.size();i++){
						sbf =sbf.append(list1.get(i)+",");
					}
					if(sbf.length()>0){
						out.print("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
						return null;
					}
				}

				DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
				dc.add(Restrictions.in("id", StringUtil.strArray2intArray(idNu))) ;
				List<Customer> list = proposalService.findByCriteria(dc) ;
				StringBuffer phones = new StringBuffer() ;
				for(int i=0 ; i<list.size();i++){
					Customer customer = list.get(i) ;
					if(i != 0 ){
						phones.append(","+customer.getPhone());
					}else {
						phones.append(customer.getPhone());
					}
				}
				/*SendPhoneThread thread = new SendPhoneThread(notifyService , remark , phones.toString()) ;
				thread.start();*/
				String str=SendPhoneMsgUtil.InternationSms(phones.toString(), remark);
				out.print(str);
			}

		} catch (Exception e) {
		}finally {
			if (out != null) {
				out.close();
			}
		}

		return null ;
	}

	public String sendSmsByUsersList(){
		String operatorLoginname = getOperatorLoginname();
		if (StringUtils.isBlank(operatorLoginname)) {

			GsonUtil.GsonObject("登录会话已过期，请重新登录后再操作！");
			return null;
		}
		String value = querySystemConfig("type030", "001", "否");

		if (StringUtils.isBlank(value)) {

			GsonUtil.GsonObject("短信平台通道已关闭！");
			return null;
		}
		List list  = checkKeyWord(content);
		if(null!=list&&list.size()>0){
			StringBuffer sbf = new StringBuffer();
			for(int i=0;i<list.size();i++){
				sbf =sbf.append(list.get(i)+",");
			}
			if(sbf.length()>0){
				GsonUtil.GsonObject("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
			return null;
			}
		}
		
		
		if(StringUtils.isNotBlank(ids)){
			String[] loginnames = ids.split(",");
			if(loginnames.length<1){
				GsonUtil.GsonObject("请选中要执行的条目");
				return null;
			}
			StringBuffer phones = new StringBuffer();
			for (String loginname : loginnames) {
				Users user = (Users) operatorService.get(Users.class, loginname);
				if (StringUtil.isNotBlank(user.getPhone())) {
					phones.append(user.getPhone()+",");
				}
			}
			
			
			String str = SendPhoneMsgUtil.InternationSms(phones.toString(), content);
			//记录发送日志
			operatorService.insertOperatorSendSMSLog(operatorLoginname,str);
			GsonUtil.GsonObject(str);
		}
		return null;
		
	}
	
	public String repairPayOrder() {
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			
			if(loginname==null || "".equals(loginname)){
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if(billno==null || "".equals(billno)){
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			if(payBillno==null || "".equals(payBillno)){
				this.setErrormsg("环迅单号不允许为空！");
				return INPUT;
			}
			if(payAmount==null || "".equals(payAmount)){
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>2000){
				this.setErrormsg("金额不能超过2000元人民币！");
				return INPUT;
			}
			/**
			 * 进行qy订单号查询，防止两平台重复补单
			 */
			List<Map<String,Object>> paylist = jdbcTemplate.queryForList(" select billno from payorder where billno=? ", new Object[]{billno.trim()});
			if(paylist!=null && !paylist.isEmpty() && paylist.size()>0){
				this.setErrormsg("在qy平台已存在该订单，不能重复提交！");
				return INPUT;
			}else{
				String msg = netpayService.repairNetpayOrder(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
			}
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String repairPayOrderZf() {
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			
			if(loginname==null || "".equals(loginname)){
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if(billno==null || "".equals(billno)){
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			try {
				Long.parseLong(billno);
			} catch (Exception e) {
				this.setErrormsg("单号有问题，请检测！");
				return INPUT;
			}
			if(payAmount==null || "".equals(payAmount)){
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>50000){
				this.setErrormsg("金额不能超过50000元人民币！");
				return INPUT;
			}
			/**
			 * 进行qy订单号查询，防止两平台重复补单
			 */
				String msg = netpayService.repairNetpayOrderZf(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String repairPayOrderZfb() {
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			
			if(loginname==null || "".equals(loginname)){
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if(billno==null || "".equals(billno)){
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			if(payAmount==null || "".equals(payAmount)){
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>5000){
				this.setErrormsg("金额不能超过5000元人民币！");
				return INPUT;
			}
			/**
			 * 进行qy订单号查询，防止两平台重复补单
			 */
				String msg = netpayService.repairNetpayOrderZfb(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String repairGameOrder() {
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			
			if(loginname==null || "".equals(loginname)){
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if(billno==null || "".equals(billno)){
				this.setErrormsg("单号不允许为空！");
				return INPUT;
			}
			try {
				Long.parseLong(billno);
			} catch (Exception e) {
				this.setErrormsg("单号有问题，请检测！");
				return INPUT;
			}
			if(payAmount==null || "".equals(payAmount)){
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)<=0){
				this.setErrormsg("最低不能超过0元");
				return INPUT;
			}
			/**
			 * 进行qy订单号查询，防止两平台重复补单
			 */
				String msg = netpayService.repairGameOrder(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	
	public String repairPayOrderHf(){
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			if (loginname == null || "".equals(loginname)) {
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if (billno == null || "".equals(billno)) {
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			if (payAmount == null || "".equals(payAmount)) {
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>5000){
				this.setErrormsg("金额不能超过5000元人民币！");
				return INPUT;
			}
				String msg = netpayService.repairNetpayOrderHf(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String getActivityHistory(){
		HashMap<String, String> map = new HashMap<>();
		map.put("sd", "圣诞敲钟活动");
		DetachedCriteria hd=null;
		String s = map.get(type);
		if(null==s||s.isEmpty()){
			return INPUT;
		}

		hd= DetachedCriteria.forClass(ActivityHistory.class);


		hd.add(Restrictions.eq("title", s));

		if (startTime != null){
			hd = hd.add(Restrictions.ge("createtime", startTime));
		}
		if(!keyword.isEmpty()){
			hd = hd.add(Restrictions.like("remark",keyword,MatchMode.ANYWHERE));
		}
		if(!username.isEmpty()){
			hd = hd.add(Restrictions.eq("username", username));
		}
		if (endTime != null){
			hd = hd.add(Restrictions.le("createtime", endTime));
		}
		Page  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), hd, pageIndex, size, Order.desc("createtime"));
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String getSubmitPayAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("取消审核");
		try {
			String msg = netpayService.submitProposal(billno, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				try {
					String smsmsg = this.notifyService.sendSMSByBillno(billno, "payorder");
					log.info(smsmsg);
				} catch (Exception e) {
					log.error("补单发送短信失败：", e);
				}
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}

	public String deletegonghuidata(){
		try {
			operatorService.delete(GuildStaff.class,id);
			GsonUtil.GsonObject("删除成功!");
		} catch (Exception e) {
			log.error("setSmWeeklyJob error:",e);
			GsonUtil.GsonObject("删除失败!");
		}
		return null;
	}
	
	public String repairPayOrderHc(){
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			if (loginname == null || "".equals(loginname)) {
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if (billno == null || "".equals(billno)) {
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			if (payAmount == null || "".equals(payAmount)) {
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>5000){
				this.setErrormsg("金额不能超过5000元人民币！");
				return INPUT;
			}
				String msg = netpayService.repairNetpayOrderHc(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String repairPayOrderBfb(){
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			if (loginname == null || "".equals(loginname)) {
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if (billno == null || "".equals(billno)) {
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			if (payAmount == null || "".equals(payAmount)) {
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>5000){
				this.setErrormsg("金额不能超过5000元人民币！");
				return INPUT;
			}
				String msg = netpayService.repairNetpayOrderBfb(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String getSubmitGameAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("取消审核");
		try {
			String msg = netpayService.submitGameProposal(billno, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String getSubmitQuotalAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("取消审核");
		try {
			String msg = netpayService.submitQuotalProposal(billno, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String getSubmitSbAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("取消审核");
		try {
			String msg = netpayService.submitSbProposal(id, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String submitPayCancelAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("取消审核");
		try {
			String msg = netpayService.submitCancel(billno, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("取消成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("取消失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("取消失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	
	public String submitGameCancelAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("取消审核");
		try {
			String msg = netpayService.submitGameCancel(billno, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("取消成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("取消失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("取消失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String submitQuotalCancelAction() {
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			// getResponse().setContentType("text/plain;charset=UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter out = null;
		log.info("审核不通过审核");
		try {
			String msg = netpayService.submitQuotalCancel(billno, getOperatorLoginname(), getIp(),remark);
			out = this.getResponse().getWriter();
			if (msg == null) {
				// setErrormsg("审核成功");
				out.println("审核不通过成功");
			} else {
				// setErrormsg("审核失败:" + msg);
				out.println("审核不通过失败:" + msg);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// setErrormsg("审核失败:" + e.getMessage());
			out.println("审核不通过失败:" + e.getMessage());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		// return INPUT;
		return null;
	}
	
	public String repairPayOrderGfb(){
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			if (loginname == null || "".equals(loginname)) {
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if (billno == null || "".equals(billno)) {
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			if (payAmount == null || "".equals(payAmount)) {
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>50000){
				this.setErrormsg("金额不能超过50000元人民币！");
				return INPUT;
			}
				String msg = netpayService.repairNetpayOrderGfb(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String resetUserPassword() {
		String msg = null;
		try {
			password = dfh.utils.StringUtil.getRandomString(8);
			
			String role = userDao.getRole(loginname);
			if(!"AGENT".equals(role)){
				Boolean b = PtUtil.updatePlayerPassword(loginname, password);
				if(!b.booleanValue()){
					String rs = PtUtil.createPlayerName(loginname, password);
					if(rs==null){
						setErrormsg("创建PT账号错误，PT API 接口出现问题！");
						return INPUT;
					}
				}
			}
			
			msg = operatorService.resetPassword(loginname, password, getOperatorLoginname());
			if (msg.equals("ERROR")) {
				setErrormsg("密码重设失败:" + msg);
			} else if (msg.equals("AGENT")) {
				setErrormsg("新密码是：" + password);
			} else {
				setErrormsg("新密码是：" + password);
				Bbs bbs=new Bbs(loginname,password);
				bbs.start();
				// String bbs_modifyPassword_url =
				// Configuration.getInstance().getValue("bbs_modifyPassword_url");
//				int status = synMemberInfo(loginname, password);
//				// System.out.println("status:"+status);
//				if (status != 200) {
//					this.setErrormsg(this.getErrormsg() + ";同步bbs会员密码状态失败，请重新操作。 code[" + status + "]");
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("密码重设失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public static int synMemberInfo(String loginname, String password) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String databasesIp = Configuration.getInstance().getValue("bbs_ip");
			String databasesPort = Configuration.getInstance().getValue("bbs_port");
			String databases = Configuration.getInstance().getValue("bbs_databases");
			String dbuser = Configuration.getInstance().getValue("bbs_user");
			String dbpassword = Configuration.getInstance().getValue("bbs_password");
			String usrl = "jdbc:mysql://" + databasesIp + ":" + databasesPort + "/" + databases + "?useUnicode=true&characterEncoding=UTF-8";
			conn = DriverManager.getConnection(usrl, dbuser, dbpassword);
			String sqlTwo = "update pre_ucenter_members set password='" + EncryptionUtil.encryptPassword(password) + "' where username='" + loginname + "'";
			stmt = conn.prepareStatement(sqlTwo);
			stmt.executeUpdate(sqlTwo);
			return 200;
		} catch (Exception e) {
			e.printStackTrace();
			return 100;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 100;
			}
		}
	}
	
	public String collectionChessData(){
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(hour<1){
			GsonUtil.GsonObject("提示：请您每天的1点后提交") ;
			return SUCCESS;
		}
		
		try {
			getdateService.collectionChessData();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			writeText("采集失败，请联系技术");
		}
		writeText("采集成功，可以上传反水");
		return null ;
	}
	
	public String getNewPtList(){
		DetachedCriteria dc = DetachedCriteria.forClass(PtData.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("playername", loginname));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("creattime");
//			dc = dc.addOrder(Order.desc("creattime"));
		}
		if (start != null){
			dc = dc.add(Restrictions.ge("starttime", start));
		}
		if (end != null){
			Calendar c=Calendar.getInstance();
			c.setTime(end);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 0);
			end = c.getTime();
			dc = dc.add(Restrictions.le("endtime",c.getTime()));
		}
		
		String agent = this.getRequest().getParameter("agent");//增加代理查询条件
		if(StringUtil.isNotEmpty(agent)){
			
			
			String getUserByAgentSql = 
					" SELECT CONCAT('K', UCASE(u.loginname))" +
					" FROM users u" +
					" WHERE u.agent IS NOT NULL AND u.agent !='' AND u.agent = '" + agent.trim() + "'";
			
			Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
			try {
				SQLQuery query = session.createSQLQuery(getUserByAgentSql);
				List userList = query.list() ;
				
				if (userList != null && !userList.isEmpty()) {
					
					dc = dc.add(Restrictions.in("playername", userList));
				}else{
					dc = dc.add(Restrictions.eq("playername", null));
				}
			} catch (Exception e) {
				log.error("server error", e);
			} finally {
				session.close();
			}
		}
		
		Page page = PageQuery.queryForPagenationWithStatistics_ptdate(operatorService.getHibernateTemplate(), dc, pageIndex, size, "houseearnings", "bets" ,"progressivebets","progressivewins", o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String queryDtCredit() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getLoginname()!=null && !customer.getLoginname().equals("")){
				String afterPtRemoteAmount = DtUtil.getamount(customer.getLoginname());
				if (afterPtRemoteAmount == null) {
					out.println("系统繁忙，请稍后尝试！");
				} else {
					out.println(afterPtRemoteAmount);
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	public String queryNewPtCredit() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getLoginname()!=null && !customer.getLoginname().equals("")){
				Double afterPtRemoteAmount = PtUtil.getPlayerMoney(customer.getLoginname());
				if (afterPtRemoteAmount == null) {
					out.println("系统繁忙，请稍后尝试！");
				} else {
					out.println(afterPtRemoteAmount);
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	private String usernames;
	
	
	
	public String getUsernames() {
		return usernames;
	}

	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}
	

	// 发送手机短信 update:sun
	public String sendSms() {
		try {
			
//			SimpleDateFormat dfHH = new SimpleDateFormat("HH");
//			Date date = new Date();
//			Integer hh = Integer.parseInt(dfHH.format(date));
//			if (hh < 8 ) {
//				setErrormsg("发送不成功 ,发送时间定为 08：00-19：00");
//				return INPUT;
//			}else if (hh >= 19) {
//				setErrormsg("发送不成功 ,发送时间定为 08：00-19：00");
//				return INPUT;
//			}	
			
			
			List list  = checkKeyWord(Msg);
			if(null!=list&&list.size()>0){
				StringBuffer sbf = new StringBuffer();
				for(int i=0;i<list.size();i++){
					sbf =sbf.append(list.get(i)+",");
				}
				if(sbf.length()>0){
				setErrormsg("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
				return INPUT;
				}
			}
			
			// loadUsersPhone();
			Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if(!StringUtil.isEmpty(usernames)){
				Map params = new HashMap<String,String>();
				usernames=usernames.replace("，", ",");
				usernames="'"+usernames+"'";
				usernames=usernames.replace(",", "','");
				String sql = "select phone from users where loginname in ("+usernames+")";
				
				Session session = userDao.getSessionFactory().openSession();
				try {
					SQLQuery query = session.createSQLQuery(sql);
					List phones = query.list() ;
					for(int i=0;i<phones.size();i++){
						PhoneNum=PhoneNum+","+AESUtil.aesDecrypt((String)phones.get(i), AESUtil.KEY);
					}
					if(PhoneNum.startsWith(",")){
						PhoneNum=PhoneNum.replaceFirst(",", "");
					}
				} catch (Exception e) {
					log.error("server error", e);
				} finally {
					session.close();
				}
			}
			/*	if(!"boss".equals(op.getAuthority())){
				if(loginname!=null && !"".equals(loginname)){
					Users user = (Users) operatorService.get(Users.class, loginname);
					PhoneNum = user.getPhone();
				}
				if (PhoneNum==null || PhoneNum.length() <= 0)
					setErrormsg("请输入手机号码:");
				else {
					String msg = notifyService.sendSmsNew(PhoneNum.trim(), StringUtil.trim(Msg));
					setErrormsg("返回消息:" + msg);
				}
			}else{*/
				if (PhoneNum!=null && PhoneNum.length() <= 0)
					setErrormsg("请输入手机号码:");
				else {
					String msg = notifyService.sendSmsNew(PhoneNum.trim(), StringUtil.trim(Msg));
					setErrormsg("返回消息:" + msg);
				}
			//}
		} catch (Exception e) {
			setErrormsg("返回消息:" + e.getMessage());
			e.printStackTrace();
		}
		return INPUT;
	}
	
	public String sendAgSms() {
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			String msg = notifyService.sendSms(PhoneNum.trim(), StringUtil.trim(Msg));
			out=this.getResponse().getWriter();
			out.println("返回消息:" + msg);
			out.flush();
		} catch (Exception e) {
			out.println("返回消息:" + e.getMessage());
			out.flush();
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}


	public String sendSmsListByLevel() {

		
//		SimpleDateFormat dfHH = new SimpleDateFormat("HH");
//		Date date = new Date();
//		Integer hh = Integer.parseInt(dfHH.format(date));
//		if (hh < 8 ) {
//			setErrormsg("发送不成功 ,发送时间定为 08：00-19：00");
//			return INPUT;
//		}else if (hh >= 19) {
//			setErrormsg("发送不成功 ,发送时间定为 08：00-19：00");
//			return INPUT;
//		}
		
		
		if (level != null && level >= 0 && level < 7) {
			List list  = checkKeyWord(Msg);
			if(null!=list&&list.size()>0){
				StringBuffer sbf = new StringBuffer();
				for(int i=0;i<list.size();i++){
					sbf =sbf.append(list.get(i)+",");
				}
				if(sbf.length()>0){
				setErrormsg("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
				return Action.INPUT;
				}
			}
			
			if (this.start != null && this.end != null) {
				if (Msg != null || !Msg.trim().equals("")) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					try {
						format.format(start);
						format.format(end);
					} catch (Exception e) {
						// TODO: handle exception
						this.setErrormsg("日期格式不正确,请重新填写或使用日期控件（yyyy-MM-dd）");
						return Action.INPUT;
					}
					long diff = end.getTime() - start.getTime();
					if (diff / (1000 * 60 * 60 * 24) > 7) {
						this.setErrormsg("时间间隔不得大于7天");
						return Action.INPUT;
					}
					// 所有的格式合法，获取指定范围内的会员电话号码，发送短信。
					String sendResult = notifyService.sendSmsListByLevel(Msg,
							level, start, end);
					if (sendResult == null) {
						this.setErrormsg("发送短信失败，请重新发送或与管理员联系");
					} else {
						this.setErrormsg(sendResult);
					}

				} else {
					this.setErrormsg("请输入要发送的短信内容");
				}

			} else {
				this.setErrormsg("请选择起始日期与结束日期");
			}

		} else {
			this.setErrormsg("会员级别不可为空");
		}
		return Action.INPUT;

	}

	public String sendSmsList() {
		
		SimpleDateFormat dfHH = new SimpleDateFormat("HH");
		Date date = new Date();
		Integer hh = Integer.parseInt(dfHH.format(date));
		if (hh < 8 ) {
			setErrormsg("发送不成功 ,发送时间定为 08：00-19：00");
			return INPUT;
		}else if (hh >= 19) {
			setErrormsg("发送不成功 ,发送时间定为 08：00-19：00");
			return INPUT;
		}	
		
		if (type != null && type.trim().length() > 0) {
			List list  = checkKeyWord(Msg);
			if(null!=list&&list.size()>0){
				StringBuffer sbf = new StringBuffer();
				for(int i=0;i<list.size();i++){
					sbf =sbf.append(list.get(i)+",");
				}
				if(sbf.length()>0){
				setErrormsg("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
				return Action.INPUT;
				}
			}
			
			if (this.start != null && this.end != null) {
				if (Msg != null || !Msg.trim().equals("")) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					try {
						format.format(start);
						format.format(end);
					} catch (Exception e) {
						// TODO: handle exception
						this.setErrormsg("日期格式不正确,请重新填写或使用日期控件（yyyy-MM-dd）");
						return Action.INPUT;
					}
					long diff = end.getTime() - start.getTime();
					if (diff / (1000 * 60 * 60 * 24) > 7) {
						this.setErrormsg("时间间隔不得大于7天");
						return Action.INPUT;
					}
					// 所有的格式合法，获取指定范围内的会员电话号码，发送短信。
					String sendResult = notifyService.sendSmsList(Msg, type,start, end);
					if (sendResult == null) {
						this.setErrormsg("发送短信失败，请重新发送或与管理员联系");
					} else {
						this.setErrormsg(sendResult);
					}

				} else {
					this.setErrormsg("请输入要发送的短信内容");
				}

			} else {
				this.setErrormsg("请选择起始日期与结束日期");
			}
		} else {
			this.setErrormsg("会员类型不可为空");
		}
		return Action.INPUT;
	}

	public void sendSmsList45() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
//			SimpleDateFormat dfHH = new SimpleDateFormat("HH");
//			Date date = new Date();
//		
//			Integer hh = Integer.parseInt(dfHH.format(date));
//			if (hh < 8 ) {
//				out.print("发送不成功 ,发送时间定为 08：00-19：00");
//				return ;
//			}else if (hh >= 19) {
//				out.print("发送不成功 ,发送时间定为 09：00-19：00");
//				return ;
//			}	
			List list  = checkKeyWord(Msg);
			if(null!=list&&list.size()>0){
				StringBuffer sbf = new StringBuffer();
				for(int i=0;i<list.size();i++){
					sbf =sbf.append(list.get(i)+",");
				}
				if(sbf.length()>0){
				out.print("该短信内容包含关键字："+sbf.toString()+",请修改后在发送。");
				return ;
				}
			}
			
		if (this.start != null && this.end != null) {
			if (Msg != null || !Msg.trim().equals("")) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					format.format(start);
					format.format(end);
				// 所有的格式合法，获取指定范围内的会员电话号码，发送短信。
				String sendResult = notifyService.sendSmsList45(Msg,
						getPnone45(), start, end);
				if (sendResult == null) {
					out.print("发送短信失败，请重新发送或与管理员联系");
				} else {
					out.print(sendResult);
				}

			} else {
				out.print("请输入要发送的短信内容");
			}

		} else {
			out.print( "请选择起始日期与结束日期");
		}
		} catch (Exception e) {
			// TODO: handle exception
			out.print("日期格式不正确,请重新填写或使用日期控件（yyyy-MM-dd）");
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
	}

	private List getPnone45() {
		if (fslb.equals("5")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("phone"));
			// dc = dc.add(Restrictions.eq("role", roleCode));
			dc = dc.add(Restrictions.isNotNull("phone"));
			dc = dc.add(Restrictions.eq("agent", agentValue));
			if (start != null) {
				dc = dc.add(Restrictions.ge("createtime", start));
			}
			if (end != null) {
				dc = dc.add(Restrictions.lt("createtime", end));
			}
			dc = dc.add(Restrictions.eq("flag", 0));
			Integer pageCount = PageQuery.queryForCount(
					operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(
					operatorService.getHibernateTemplate(), dc, 1, pageCount, null)
					.getPageContents();
			return usersList;
			// System.out.println("*******"+email);
		} else if (fslb.equals("4")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("phone"));
			dc = dc.add(Restrictions.eq("intro", introValue));
			if (start != null) {
				dc = dc.add(Restrictions.ge("createtime", start));
			}
			if (end != null) {
				dc = dc.add(Restrictions.lt("createtime", end));
			}
			dc = dc.add(Restrictions.isNotNull("phone"));
			dc = dc.add(Restrictions.eq("flag", 0));
			Integer pageCount = PageQuery.queryForCount(
					operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(
					operatorService.getHibernateTemplate(), dc, 1, pageCount, null)
					.getPageContents();
			return usersList;
		}else if (fslb.equals("6")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("phone"));
			dc = dc.add(Restrictions.isNotNull("phone"));
			if (start != null) {
				dc = dc.add(Restrictions.ge("createtime", start));
			}
			if (end != null) {
				dc = dc.add(Restrictions.lt("createtime", end));
			}
			if (isMoney.equals("有存款")) {
				dc = dc.add(Restrictions.isNotNull("credit"));
				dc = dc.add(Restrictions.not(Restrictions.eq("credit", 0.0)));
			} else {
				dc = dc.add(Restrictions.or(Restrictions.isNull("credit"),
						Restrictions.eq("credit", 0.0)));
			}
			dc = dc.add(Restrictions.eq("flag", 0));
			Integer pageCount = PageQuery.queryForCount(
					operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(
					operatorService.getHibernateTemplate(), dc, 1, pageCount, null)
					.getPageContents();
			return usersList;
		}
		return null;
	}
	public String getPtList(){
		DetachedCriteria dc = DetachedCriteria.forClass(PtProfit.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}

		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("starttime");
//			dc = dc.addOrder(Order.desc("starttime"));
		}
		if (startNT != null)
			dc = dc.add(Restrictions.ge("starttime", startNT));
		if (endNT != null)
			dc = dc.add(Restrictions.le("endtime", endNT));

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", "betCredit", "payOut", o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 指定日期更新NT游戏数据 
	 * @return
	 */
	public String singleUpdateQQ(){
		try {
			String sd=getRequest().getParameter("single_date");
			Date update=MatchDateUtil.parseDate(sd); 
			
			getdateService.processStatusData(update);
			
			GsonUtil.GsonObject("更新指定日期NT数据完成.");
		} catch(Exception e){
			GsonUtil.GsonObject("更新指定日期NT数据出错."+e.getMessage());
			log.error("单独指定日期更新NT游戏数据出错..",e);
		}
		//getRequest().setAttribute("page", page);
		return null;
	}
	
	
	//******
			public String getPtNewDatas(){
				DetachedCriteria dc = DetachedCriteria.forClass(PtDataNew.class);
				
				if (StringUtils.isNotEmpty(loginname)) {
					dc.add(Restrictions.eq("playername", loginname));
				}
				if (StringUtils.isNotEmpty(by)) {
					Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
					dc = dc.addOrder(o);
				}else{
					dc = dc.addOrder(Order.desc("creattime"));
				}
				if (startDate != null)
					dc = dc.add(Restrictions.ge("starttime", startDate));
				if (endDate != null)
					dc = dc.add(Restrictions.le("endtime", endDate));
				Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "income", "bets" ,"betsTiger","winsTiger","progressiveBet","progressiveWin");
				getRequest().setAttribute("page", page);
				return INPUT;
			}
			
			public String addPtTigerGameData(){
				if (null == file || StringUtils.isBlank(fileFileName)) {
					setErrormsg("请先提交文件");
					return INPUT;
				}
				if (!fileFileName.endsWith(".csv")) {
					setErrormsg("文件必须是csv");
					return INPUT;
				}
				String excelFileName = new Date().getTime() + fileFileName;
				try {
					File UploadFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
					if(!UploadFiles.exists()){
						UploadFiles.mkdirs();
					}
					File newfile = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);
					copy(file, newfile);
					CSVReader csvReader = new CSVReader(new FileReader(newfile), ',');
					if (csvReader != null) {
						String[] csvRow = null;
						while ((csvRow = csvReader.readNext()) != null) {
							String playername = "" ;
							Double betsTiger = 0.0 ;
							Double winsTiger = 0.0 ;
							if(csvRow[0].startsWith("Totals")){
								break ;
							}
							if(!csvRow[0].startsWith("K") ){
								continue ;
							}
							
							playername = csvRow[0];
							log.info("playername-->"+playername);
							betsTiger = new Double(csvRow[5].replace("CNY", "").replace("," , ""));
							winsTiger = new Double(csvRow[7].replace("CNY", "").replace("," , ""));
							getdateService.updateNewPtData(playername, DateUtil.getchangedDate(-1), betsTiger, winsTiger) ;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return INPUT;
			}
			
			private static void copy(File src, File dst) {
				try {
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
						out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
						byte[] buffer = new byte[BUFFER_SIZE];
						while (in.read(buffer) > 0) {
							out.write(buffer);
						}
					} finally {
						if (null != in) {
							in.close();
						}
						if (null != out) {
							out.close();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
	/**
	 * 查询JC已更新盈利数据
	 * @return
	 */
	public String getJCDailyData(){
		DetachedCriteria dc = DetachedCriteria.forClass(JCProfitData.class);
		
		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("playerName", loginname));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("creatTime");
//			dc = dc.addOrder(Order.desc("creatTime"));
		}
		if (startDate != null)
			dc = dc.add(Restrictions.ge("startTime", startDate));
		if (endDate != null)
			dc = dc.add(Restrictions.le("endTime", endDate));
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "orderSum", "actual", "bonus", "win", o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 根据日期更新JC盈利数据
	 * @return
	 */
	public String updateJCDailyProfit(){
		if(org.apache.commons.lang3.StringUtils.isBlank(executeTime)){
			writeText("时间不能为空！");
			return null ;
		}
		try {
			String profit = JCUtil.dailyProfitJC(executeTime);
			if (StringUtils.isEmpty(profit)){
				log.error("********查询每日盈利结果出现异常*******");
			} else {
				new JCDataThread(getdateService).parseJCData(profit,executeTime);
			}
			//TODO 页面展示与系统洗码内容
		} catch (Exception e) {
			log.error("updateJcDailyProfit error", e);
		}
		return null;
	}
			
	public String sendLoginEmail() {
		String msg = "";
		try {
			if(loginname==null){
				msg = "登录帐号不允许为空!";
			}else{
				String erd = StringUtil.getRandomString(5);
				Operator operator = (Operator) operatorService.get(Operator.class, loginname);
				if(operator!=null && !"".equals(operator.getEmail())){
					getHttpSession().setAttribute(Constants.SESSION_EMAIL_RANDID, erd);
					System.out.println("龙都 邮箱验证码："+erd);
					msg = notifyService.sendEmail(operator.getEmail(), "龙都邮箱验证码", "["+loginname+"] 龙都 邮箱验证码："+erd +" ;登录ip为："+getIp() +" ;登录时间为："+DateUtil.getNow());
				}else{
					msg = "帐号异常!";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	

	// 发送邮件 author:sun
	public String sendEmail() {
		try {
			String urlContext = "", msg = "";
			if (StringUtil.isNotEmpty(urladdress)) {
				urlContext = HttpUtils.get(urladdress, null);
				if (urlContext != null && !urlContext.equals("")) {
					Msg = urlContext;
				}
			}
			if (Msg == null || Msg.equals("")) {
				setErrormsg("发送失败内容不能为空！");
				return INPUT;
			}
			// 判断邮箱是否为空
			if (email == null || email.equals("")) {
				if (loginname != null && !loginname.equals("")) {
					Users user = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
					if (user != null && user.getEmail() != null && !user.getEmail().equals("") &&user.getEmail().length()>0) {
						msg = notifyService.sendEmail(user.getEmail().trim(), title, Msg);
					} else {
						setErrormsg("返回消息:未找到任何的收件人");
						return INPUT;
					}
				} else {
					setErrormsg("返回消息:未找到任何的收件人");
					return INPUT;
				}
			} else {
				if (email.startsWith(",")) {
					email = email.substring(1);
				}
				msg = notifyService.sendEmail(email.trim(), title, Msg);
			}
			setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("发送失败:" + e.getMessage());
		}
		return INPUT;
	}

	// 发送邮件 author:sun
	public String sendEmailTwo() {
		try {
			String urlContext = "", msg = "";
			if (StringUtil.isNotEmpty(urladdress)) {
				urlContext = HttpUtils.get(urladdress, null);
				if (urlContext != null && !urlContext.equals("")) {
					Msg = urlContext;
				}
			}
			if (Msg == null || Msg.equals("")) {
				setErrormsg("发送失败内容不能为空！");
				return INPUT;
			}
			if (roleCode != null && !roleCode.equals("")) {
				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc = dc.setProjection(Projections.property("email"));
				dc = dc.add(Restrictions.eq("role", roleCode));
				dc = dc.add(Restrictions.isNotNull("email"));
				dc = dc.add(Restrictions.eq("flag", 0));
				Integer pageCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
				List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, pageCount, null).getPageContents();
				int forCount = usersList.size();
				for (int i = 0; i < forCount; i++) {
					Users user = (Users) usersList.get(i);
					if(user.getEmail()!=null && !user.getEmail().equals("") && user.getEmail().length()>0){
						email = email + "," + user.getEmail();
					}
				}
				if (email.startsWith(",")) {
					email = email.substring(1);
				}
				//System.out.println("*******"+email);
				msg = notifyService.sendEmail(email.trim(), title, Msg);
			} else if (level != null && !level.equals("")) {
				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc = dc.setProjection(Projections.property("level"));
				dc = dc.add(Restrictions.eq("level", level));
				dc = dc.add(Restrictions.isNotNull("level"));
				dc = dc.add(Restrictions.eq("flag", 0));
				Integer pageCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
				List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, pageCount, null).getPageContents();
				int forCount = usersList.size();
				for (int i = 0; i < forCount; i++) {
					Users user = (Users) usersList.get(i);
					if(user.getEmail()!=null && !user.getEmail().equals("") && user.getEmail().length()>0){
						email = email + "," + user.getEmail();
					}
				}
				if (email.startsWith(",")) {
					email = email.substring(1);
				}
				//System.out.println("*******"+email);
				msg = notifyService.sendEmail(email.trim(), title, Msg);
			}
			setErrormsg("返回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("发送失败:" + e.getMessage());
		}
		return INPUT;
	}


	// 加载用户手机号码信息
	public void loadUsersPhone() {
		if (roleCode != null && !roleCode.equals("")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("phone"));
			dc = dc.add(Restrictions.eq("role", roleCode));
			dc = dc.add(Restrictions.isNotNull("phone"));
			Integer sumCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, sumCount, null).getPageContents();
			int usersCount = usersList.size();
			for (int i = 0; i < usersCount; i++) {
				Users user = (Users) usersList.get(i);
				PhoneNum = PhoneNum + "," + user.getPhone().trim();
			}
			if (PhoneNum.startsWith(",")) {
				PhoneNum = PhoneNum.substring(1);
			}
		}
	}

	// 加载用户email
	public void loadUsersEmail() {
		if (roleCode != null && !roleCode.equals("")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("email"));
			dc = dc.add(Restrictions.eq("role", roleCode));
			dc = dc.add(Restrictions.isNotNull("email"));
			dc = dc.add(Restrictions.eq("flag", 0));
			// System.out.println(dc.toString());
			Integer pageCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, pageCount, null).getPageContents();
			int forCount = usersList.size();
			for (int i = 0; i < forCount; i++) {
				Users user = (Users) usersList.get(i);
				email = email + "," + user.getEmail();
			}
			if (email.startsWith(",")) {
				email = email.substring(1);
			}
		}
		if(email==null || "".equals(email)){
			if(loginname!=null && !"".equals(loginname)){
				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc = dc.add(Restrictions.eq("loginname", loginname));
				Integer pageCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
				List<Users> usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, pageCount, null).getPageContents();
				if(usersList.size()==1){
					email =  usersList.get(0).getEmail();
				}
			}
		}
	}
	
	 private String subject;
	 
	 
		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		/**
		 * 已注册用户群发  
		 * @author ck
		 * @return
		 * 
		 * 1-7 id = 页面顺序功能
		 */
		public String sendYiYeEmailExternal(){
		
			try {
				
				DetachedCriteria dc=DetachedCriteria.forClass(Users.class);

				
				if (id==1) {
					
					  String [] userMail=email.split("\\,|;");
					   writeObjectJson(SendYiYeEmail.sendYiYeMail(userMail,subject,content,"EXTERNER",getOperatorLoginname(),"ex_"+id));
					   
					   return null;
					   
				}else if (id==2) {
					
					    dc.add(Restrictions.eq("role", roleCode));
					if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
					if (startTime != null) 
						dc = dc.add(Restrictions.ge("createtime", startTime));
					if (endTime != null) 
						dc = dc.add(Restrictions.lt("createtime", endTime));
					
				}else if (id==3) {
					
					dc.add(Restrictions.eq("level", level));
					if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
					if (startTime != null) 
						dc = dc.add(Restrictions.ge("createtime", startTime));
					if (endTime != null) 
						dc = dc.add(Restrictions.lt("createtime", endTime));
					
				}else if (id==4) {
					
					Users user = (Users) operatorService.get(Users.class,agentValue);
					if (user != null) {
						dc.add(Restrictions.eq("agent", user.getLoginname()));
						if (Integer.valueOf(isMoney) != -1) 
							dc.add(Restrictions.eq("isCashin",Integer.valueOf(isMoney)));
						if (startTime != null)
							dc = dc.add(Restrictions.ge("createtime", startTime));
						if (endTime != null)
							dc = dc.add(Restrictions.lt("createtime", endTime));
					} else{
						writeObjectJson(new ReturnBean(agentValue + ":此用户不是代理"));
					     return null;	
					}
					
				}else if (id==5) {
					
					   dc.add(Restrictions.eq("partner",partner));
		   	  		if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
		   	  		if (startTime != null) 
		   	  				dc = dc.add(Restrictions.ge("createtime", startTime));
		   	  		if (endTime != null) 
		   	  				dc = dc.add(Restrictions.lt("createtime", endTime));
		   	  		
				}else if (id==6) {
					
					dc.add(Restrictions.eq("intro",introValue));
		   	  		if (Integer.valueOf(isMoney) != -1) 
					    dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
		   	  		if (startTime != null) 
		   	  				dc = dc.add(Restrictions.ge("createtime", startTime));
		   	  		if (endTime != null) 
		   	  				dc = dc.add(Restrictions.lt("createtime", endTime));
		   	  		
				}else if (id==7) {
					
					if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
		  			if (startTime != null) 
		  				dc = dc.add(Restrictions.ge("createtime", startTime));
		  			if (endTime != null) 
		  				dc = dc.add(Restrictions.lt("createtime", endTime));
					
				}else {
					writeObjectJson(new ReturnBean("非法操作"));
					return null;
				}
				List<Users> list = operatorService.findByCriteria(dc);
				if(list==null||list.size()==0)
					writeObjectJson(new ReturnBean("查看不到相关记录"));
				else {
					String [] userMail= new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						Users users=list.get(i);
						userMail[i]=users.getEmail();
					}
				   writeObjectJson(SendYiYeEmail.sendYiYeMail(userMail,subject,content,"EXTERNER",getOperatorLoginname(),"ex_"+id));
				}
			   
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		
		public String sendYiYeEmailInternal(){
			
			try {
				
				DetachedCriteria dc=DetachedCriteria.forClass(Users.class);


				if (id==1) {
					
					  dc.add(Restrictions.eq("loginname", loginname));
					  
				}else if (id==2) {
					
					    dc.add(Restrictions.eq("role", roleCode));
					if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
					if (startTime != null) 
						dc = dc.add(Restrictions.ge("createtime", startTime));
					if (endTime != null) 
						dc = dc.add(Restrictions.lt("createtime", endTime));
					
				}else if (id==3) {
					
					dc.add(Restrictions.eq("level", level));
					if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
					if (startTime != null) 
						dc = dc.add(Restrictions.ge("createtime", startTime));
					if (endTime != null) 
						dc = dc.add(Restrictions.lt("createtime", endTime));
					
				}else if (id==4) {
					
					Users user = (Users) operatorService.get(Users.class,agentValue);
					if (user != null) {
						dc.add(Restrictions.eq("agent", user.getLoginname()));
						if (Integer.valueOf(isMoney) != -1) 
							dc.add(Restrictions.eq("isCashin",Integer.valueOf(isMoney)));
						if (startTime != null)
							dc = dc.add(Restrictions.ge("createtime", startTime));
						if (endTime != null)
							dc = dc.add(Restrictions.lt("createtime", endTime));
					} else{
						writeObjectJson(new ReturnBean(agentValue + ":此用户不是代理"));
					     return null;	
					}
					
				}else if (id==5) {
					
					   dc.add(Restrictions.eq("partner",partner));
		   	  		if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
		   	  		if (startTime != null) 
		   	  				dc = dc.add(Restrictions.ge("createtime", startTime));
		   	  		if (endTime != null) 
		   	  				dc = dc.add(Restrictions.lt("createtime", endTime));
		   	  		
				}else if (id==6) {
					
					dc.add(Restrictions.eq("intro",introValue));
		   	  		if (Integer.valueOf(isMoney) != -1) 
					    dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
		   	  		if (startTime != null) 
		   	  				dc = dc.add(Restrictions.ge("createtime", startTime));
		   	  		if (endTime != null) 
		   	  				dc = dc.add(Restrictions.lt("createtime", endTime));
		   	  		
				}else if (id==7) {
					
					if (Integer.valueOf(isMoney) != -1) 
						dc.add(Restrictions.eq("isCashin", Integer.valueOf(isMoney)));
		  			if (startTime != null) 
		  				dc = dc.add(Restrictions.ge("createtime", startTime));
		  			if (endTime != null) 
		  				dc = dc.add(Restrictions.lt("createtime", endTime));
					
				}else {
					writeObjectJson(new ReturnBean("非法操作"));
				}
				
				List<Users> list = operatorService.findByCriteria(dc);
				if(list==null||list.size()==0)
					writeObjectJson(new ReturnBean("查看不到相关记录"));
				else {
					String [] userMail= new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						Users users=list.get(i);
						userMail[i]=users.getEmail();
					}
				   writeObjectJson(SendYiYeEmail.sendYiYeMail(userMail,subject,content,"INTERNAL",getOperatorLoginname(),"IN_"+id));
				}
			   
			} catch (Exception e) {
				e.printStackTrace();
			} 

			return null;
		}
	       
	       /**
	        * 查询订阅邮件 用户
	        * @author ck
	        * @return jsonObject.toString
	        */
	       public String checkYiYeEmailSubscription(){
	      	  try {
	      	   ReturnBean rBean =SendYiYeEmail.GetContactListMail(start,end,pageIndex, size);
	      	    if (rBean.getErrormsg()==null) 
	      	    	getRequest().setAttribute("page", (Page)rBean.getObj());
	      	    else
	      	      	errormsg= rBean.getErrormsg();
	      	    
	    		
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  	     } 
	      	   return INPUT;
	      	   
	        }
	       
	       
	       
	       /**
	        * 订阅邮件 群发
	        * @author ck
	        * @return jsonObject.toString
	        */
	       public String sendYiYeEmailSubscription(){
	      	  try {

	      		String[] idNu = ids.split(",");
	    		writeObjectJson(SendYiYeEmail.sendYiYeMailSubscription(idNu,zt,remark,getOperatorLoginname(),"dinyue"));
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  	     } 
	      	   
	      	   return null;
	        }

	// 以模版的方式发送email
	public String stencilSendMail() {
		try {
			loadUsersEmail();
			if (stencil == 1) {
				Msg = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>大富豪国际娱乐城</title><style type='text/css'>body{margin:0px; width:650px;}</style></head><body><a href='http://www.88fuhao.com/' target='_blank'><img src='http://www.88fuhao.com/images/email_top.jpg' border='0' /></a><img src='http://www.88fuhao.com/images/pic_bg1.jpg' border='0' /><img src='http://www.88fuhao.com/images/pic_bg2.jpg' border='0' /><img src='http://www.88fuhao.com/images/pic_bg3.jpg' border='0' /><img src='http://www.88fuhao.com/images/pic_bg4.jpg' border='0' /><img src='http://www.88fuhao.com/images/email_buttom.jpg' border='0' /></body></html>";
			}
			if (Msg.length() <= 0)
				setErrormsg("返回消息:未选择任何模版");
			if (email.length() <= 0)
				setErrormsg("返回消息:未找到任何的收件人");
			else {
				String msg = notifyService.sendEmail(email, title, Msg);
				setErrormsg("返回消息:" + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("发送失败:" + e.getMessage());
		}
		setMsg("");
		setTitle("");
		return INPUT;
	}

	// createdate:2009-12-4 修改用户EMAIL 和电话与用户真实姓名
	public String modifyCustomerInfo() {
		if(StringUtil.isBlank(loginname))
		{
			setErrormsg("修改失败:用户名不能为空！");
			return INPUT;
		}
		String msg = null;
		try {
			// System.out.println(birthday);
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			msg = operatorService.modifyCustomerInfo(loginname, aliasName, accountName, phone, email, qq, remark, getOperatorLoginname(), DateUtil.convertToTimestamp(standardFmt.parse(birthday)), sms == null ? 1 : 0);
			if (msg == null) {
				setErrormsg("修改成功");

			} else
				setErrormsg("修改失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("修改失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String limitMethod(){
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out=this.getResponse().getWriter();
			if(loginname==null || loginname.equals("")){
				out.println("玩家账号不能为空！");
				return null;
			}
			if(limit==null || limit.equals("")){
				out.println("限额不能为空！");
				return null;
			}
			if(!limit.equals("-1")&&!limit.equals("0")&&!limit.equals("1000")&&!limit.equals("5000")){
				out.println("该额度限额不存在！");
				return null;
			}
			String msg = operatorService.limitMethod(loginname,limit,getOperatorLoginname());
			if (msg == null){
				out.println("更新成功！");
				return null;
			}
			out.println("更新失败！");
			return null;
		} catch (Exception e) {
			out.println("返回消息:" + e.getMessage());
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
		return null;
	}

	public String modifyAgentInfo() {
		String msg = null;
		try {
			// System.out.println(birthday);
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			msg = operatorService.modifyAgentInfo(loginname, referWebsite, phone, email, qq, partner,remark, acode, newagcode, getOperatorLoginname(), agentType, agentCommission,liverate,sportsrate,lotteryrate);
			if (msg == null) {
				setErrormsg("修改成功");

			} else
				setErrormsg("修改失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("修改失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String queryUserAnalyze() {
		Integer free_customer_count = 0, money_customer_count = 0, common_money_customer_count = 0, baijin_vip_count = 0, zuanshi_vip_count = 0, partner_count = 0;
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class).setProjection(Projections.projectionList().add(Projections.groupProperty("role")).add(Projections.groupProperty("level")).add(Projections.rowCount()));
		List list = operatorService.findByCriteria(dc);
		if (list != null) {
			Iterator<Object[]> it = list.listIterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				String role = obj[0].toString();
				Integer level = (Integer) obj[1];
				Integer count = (Integer) obj[2];
				if (role.equals(UserRole.MONEY_CUSTOMER.getCode())) {
					money_customer_count += count;
					if (level.intValue() == VipLevel.TIANBING.getCode().intValue())
						common_money_customer_count += count;
					if (level.intValue() == VipLevel.TIANJIANG.getCode().intValue())
						baijin_vip_count += count;
					if (level.intValue() == VipLevel.TIANWANG.getCode().intValue())
						zuanshi_vip_count += count;
				}
			}
		}
		getRequest().setAttribute("free_customer_count", free_customer_count);
		getRequest().setAttribute("money_customer_count", money_customer_count);
		getRequest().setAttribute("common_money_customer_count", common_money_customer_count);
		getRequest().setAttribute("baijin_vip_count", baijin_vip_count);
		getRequest().setAttribute("zuanshi_vip_count", zuanshi_vip_count);
		getRequest().setAttribute("partner_count", partner_count);
		return INPUT;
	}

	public String changeCreditManual() {
		String msg = null;
		Double d = 0.00;
		if (null != amount && !"".equals(amount.trim())) {
			try {
				d = Double.parseDouble(amount.trim());
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("额度类型不正确");
				return INPUT;
			}
		}
		try {
			if (isAdd.intValue() == Constants.FLAG_TRUE.intValue()) {
				msg = operatorService.changeCreditManual(loginname, Math.abs(d), remark, getOperatorLoginname());
			} else {
				msg = operatorService.changeCreditManual(loginname, Math.abs(d) * -1, remark, getOperatorLoginname());
			}
			if (msg == null)
				return SUCCESS;
			else
				setErrormsg("操作失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("操作失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	public String changeQuotal() {
		/*String loginname = getRequest().getParameter("loginname");	//玩家账户 
		String credit = getRequest().getParameter("credit");	//当前额度 
		String isAdd = getRequest().getParameter("isAdd");	//额度修改类型 
		String amount = getRequest().getParameter("amount");	//增减额度 
		String remark = getRequest().getParameter("remark");	//备注
*/		String msg = null;
		Double d = 0.0;
		if (loginname == null || "".equals(loginname)) {
			setErrormsg("玩家账户不能为空!");
			return INPUT;
		}
		
		if (amount == null || "".equals(amount)) {
			setErrormsg("额度不能为空!");
			return INPUT;
		}
		
		try {
			 d = Double.parseDouble(amount.trim());
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("额度类型不正确");
			return INPUT;
		}
		try {
			if(null == status || StringUtils.isBlank(status) || StringUtils.equals(status, "1")){
				if (isAdd.intValue() == Constants.FLAG_TRUE.intValue()) {
					msg = operatorService.changeQuotal(billno, loginname, Math.abs(d), remark, getOperatorLoginname(), getIp());
				} else {
					msg = operatorService.changeQuotal(billno, loginname, Math.abs(d) * -1, remark, getOperatorLoginname(), getIp());
				}
			}else if(null != status && StringUtils.equals(status, "2")){
				if (isAdd.intValue() == Constants.FLAG_TRUE.intValue()) {
					msg = operatorService.changeQuotalForSlot(billno, loginname, Math.abs(d), remark, getOperatorLoginname(), getIp());
				} else {
					msg = operatorService.changeQuotalForSlot(billno, loginname, Math.abs(d) * -1, remark, getOperatorLoginname(), getIp());
				}
			}
			if (msg == null) {
				return SUCCESS;
			} else {
				setErrormsg("操作失败:" + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("操作失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String changebankCreditManual() {
		String msg = null;
		Double d = 0.00;
		if (null != amount && !"".equals(amount.trim())) {
			try {
				d = Double.parseDouble(amount.trim());
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("额度类型不正确");
				return INPUT;
			}
		}
		try {
			if (isAdd.intValue() == Constants.FLAG_TRUE.intValue()) {
				msg = operatorService.changeBankCreditManual(loginname, Math.abs(d),type, remark, getOperatorLoginname());
			} else {
				msg = operatorService.changeBankCreditManual(loginname, Math.abs(d) * -1,type, remark, getOperatorLoginname());
			}
			if (msg == null)
				return SUCCESS;
			else
				setErrormsg("操作失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("操作失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String modifyOwnPwd() {
		String msg = null;
		try {
			String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
			if (!validateCode.equalsIgnoreCase(rand)) {
//				addFieldError("wwctrl", "验证码错误");
				getHttpSession().removeAttribute(Constants.SESSION_RANDID);
				msg = "验证码错误";
				return INPUT;
			}
			Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
			if(null == operator){
				msg = "请先登录";
			}else if(newPassword.contains(getOperatorLoginname())){
				msg = "密码中不能包含用户名";
			}else if(!newPassword.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,16}")){
				msg = "密码至少8位，数字开头，且包含字母和特殊字符";
			}else{
				msg = operatorService.modifyOwnPassword(operator.getUsername(), oldPassword, newPassword, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		} finally {
			if (msg == null)
				setErrormsg("修改成功");
			else
				setErrormsg("修改失败:" + msg);
		}
		return INPUT;
	}
	
	@SuppressWarnings("finally")
	public String modifyOwnPwdTwo() {
		String msg = null;
		try {
			if(StringUtil.isBlank(retypePassword) || StringUtil.isBlank(newPassword) || StringUtil.isBlank(oldPassword)){
				setErrormsg("参数不正确");
				msg= "参数不正确";
				return ERROR;
			}
			if(!retypePassword.equals(newPassword)){
				msg = "两次新密码不一样";
			}else if(newPassword.contains(getOperatorLoginname())){
				msg = "密码中不能包含用户名";
			}else if(!newPassword.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,16}")){
				msg = "密码至少8位，数字开头，且包含字母和特殊字符";
			}else{
				Operator op = (Operator) operatorService.get(Operator.class, getOperatorLoginname());
				if(op == null){
					msg = "用户不存在";
					return ERROR;
				}
				if(op.getValidType() == null){
					msg = "您的账号未指定验证类型，请先修改指定验证类型";
					return ERROR;
				}
				if(op.getValidType() == 1){//1,短信验证，2，打卡验证，3无需验证
					String msmValidMsg = this.validateSms(op, this.smsValidPwd);
					if(msmValidMsg != null){
						msg = msmValidMsg;
						return ERROR;
					}
				} else if(op.getValidType() == 2) {
					if(StringUtils.isEmpty(op.getEmployeeNo())){
						msg = "根据您的安全验证类型，员工编码为空，不能登录系统！";
						setErrormsg("根据您的安全验证类型，员工编码为空，不能登录系统！");
						return ERROR;
					}
					String rsMsg = CheckInOutUtil.checkData(op.getEmployeeNo());
					if(!"".equals(rsMsg)){
						msg = rsMsg;
						setErrormsg(rsMsg);
						return ERROR;
					}
				} else if(op.getValidType() != 3) {
					msg = "您的账号指定验证类型不存在，请修改指定验证类型";
					return ERROR;
				}
				msg = operatorService.modifyOwnPassword(getOperatorLoginname(), oldPassword, newPassword,true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "修改异常";
			setErrormsg("修改异常");
			return ERROR ;
		} finally {
			if (msg == null){
				setErrormsg("修改成功");
				Operator operator = (Operator) operatorService.get(Operator.class, getOperatorLoginname());
				String empNo = operator.getEmployeeNo();
				String phoneno = operator.getPhoneno();
				if (StringUtils.isEmpty(empNo) && StringUtils.isEmpty(phoneno)) {
					setErrormsg("修改成功,登陆后请先在左下角绑定员工编号，谢谢"); 
				}else{
					setErrormsg("修改成功");
				}
				return INPUT;
			}
			else{
				setErrormsg("修改失败:" + msg);
				return ERROR ;
			}
		}
	}
	//删除
	public void deleteUserMaintainLog() {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String operatename = getOperatorLoginname();

		if (StringUtils.isBlank(operatename)) {

			resultMap.put("code", "20000");
			resultMap.put("message", "请登录后再进行操作！");

			GsonUtil.GsonObject(resultMap);
		} else {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date());

			String sql = "DELETE FROM user_maintain_log   where id = ?";

			Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createSQLQuery(sql);

			query.setInteger(0, id);

			query.executeUpdate();

			if (null != session) {

				session.close();
			}

			resultMap.put("code", "10000");
			resultMap.put("message", "数据删除成功！");

			GsonUtil.GsonObject(resultMap);
		}
	}
	

	public String createSubOperator() {
		String msg = null;
		if(validType==1){
			if(StringUtil.isEmpty(cellphoneNo)){
				setErrormsg("短信验证手机号不能为空！");
				return INPUT;
			}
		}
		if(validType==2){
			if(StringUtil.isEmpty(employeeNo)){
				setErrormsg("员工编号不能为空！");
				return INPUT;
			}
		}
		if(StringUtil.isEmpty(password)){
			setErrormsg("密码不能为空！");
			return INPUT;
		}
		try {
			//msg = operatorService.createSubOperator(newOperator,email, password, getOperatorLoginname(),agent,validType,cellphoneNo,employeeNo);
			msg = operatorService.createSubOperatorTwo(newOperator,email, password,getOperatorLoginname(),authority,agent,validType,cellphoneNo,employeeNo);

		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		} finally {
			if (msg == null)
				setErrormsg("创建成功");
			else
				setErrormsg("创建失败:" + msg);
		}
		return INPUT;
	}
	
	public String createSubOperatorTwo() {
		String msg = null;
		if(validType==1){
			if(StringUtil.isEmpty(cellphoneNo)){
				setErrormsg("短信验证手机号不能为空！");
				return INPUT;
			}
		}
		if(validType==2){
			if(StringUtil.isEmpty(employeeNo)){
				setErrormsg("员工编号不能为空！");
				return INPUT;
			}
		}
		if(StringUtil.isEmpty(password)){
			setErrormsg("密码不能为空！");
			return INPUT;
		}
		try {
			msg = operatorService.createSubOperatorTwo(newOperator,email, password,getOperatorLoginname(),authority,agent,validType,cellphoneNo,employeeNo);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		} finally {
			if (msg == null)
				setErrormsg("创建成功");
			else
				setErrormsg("创建失败:" + msg);
		}
		return INPUT;
	}

	
	public String partnerSetlower() {
		try {
			String msg = operatorService.partnerSetlower(loginname, partner);
			if (msg != null)
				setErrormsg("设置失败" + msg);
			else
				setErrormsg("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}

	public String getAllRemoteUrl() {
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class).add(Restrictions.like("id", "URL_", MatchMode.START));
		getRequest().setAttribute("urls", operatorService.findByCriteria(dc));
		return INPUT;
	}

	// 手动同步注单
	public String synBetRecords() {
		// 开始
		log.info("start Thread synBetRecords");
		synRecordsService.synBetRecords();
		return INPUT;
	}
	
	public String updateUsersForCS(){
		customerService.updateIntroForCS();
		return null ;
	}
	
	public String updateYesterdaySixLottery(){
		try {
			Date date = new Date();
			
			Date startT = null ;
			Date endT = null ;
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.add(Calendar.DAY_OF_MONTH, -1);
				cals.set(Calendar.HOUR_OF_DAY,0 );
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				startT = cals.getTime();
				
				Calendar endcals = Calendar.getInstance();
				endcals.setTime(new Date());
				endcals.add(Calendar.DAY_OF_MONTH, -1);
				endcals.set(Calendar.HOUR_OF_DAY,23 );
				endcals.set(Calendar.MINUTE, 59);
				endcals.set(Calendar.SECOND, 59);
				endT = endcals.getTime();
			String xml = SixLotteryUtil.getValidateBet(null, DateUtil.formatDateForStandard(startT), DateUtil.formatDateForStandard(endT)) ;
			if(xml.contains("無注單")){
				return null;
			}
			List<PlatformData> list = SixLotteryUtil.parseBetXml(xml) ;
			if(null == list){
				writeText(SixLotteryUtil.compileVerifyData("<error>(.*?)</error>", xml));
				return null;
			}
			if(null != list && list.size()>0 ){
				for (PlatformData platformData : list) {
					platformData.setStarttime(startT);
					platformData.setEndtime(endT) ;
					platformData.setUuid(UUID.randomUUID().toString());
					platformData.setPlatform("sixlottery");
					getdateService.updateSixLotteryPlatForm(platformData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
		writeText("更新成功!");
		return null ;
	}
	
	public String updateYesterdayQT(){
		try {
			Date date = new Date();
			
			Date startT = null ;
			Date endT = null ;
			if(StringUtils.isNotEmpty(type)){
				//指定日期
				date = DateUtil.parseDateForYYYYmmDD(type);
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.set(Calendar.HOUR_OF_DAY, 0);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				startT = cals.getTime();
				
				Calendar endcals = Calendar.getInstance();
				endcals.setTime(date);
				endcals.add(Calendar.DAY_OF_MONTH, 1);
				endcals.set(Calendar.HOUR_OF_DAY, 0);
				endcals.set(Calendar.MINUTE, 0);
				endcals.set(Calendar.SECOND, 0);
				endT = endcals.getTime();
			}else{
				//为空默认昨天
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.add(Calendar.DAY_OF_MONTH, -1);
				cals.set(Calendar.HOUR_OF_DAY,0 );
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				startT = cals.getTime();
				
				Calendar endcals = Calendar.getInstance();
				endcals.setTime(date);
				endcals.add(Calendar.DAY_OF_MONTH, 0);
				endcals.set(Calendar.HOUR_OF_DAY,0);
				endcals.set(Calendar.MINUTE, 0);
				endcals.set(Calendar.SECOND, 0);
				endT = endcals.getTime();
			}
				List<PlatformData> list;
				//查询玩家NGR接口
				Map<String, QtBetVo> map = new HashMap<String, QtBetVo>();
				String backStr = QtUtil.getBetTotalByNGR(DateUtil.formatDateForQt(startT), DateUtil.formatDateForQt(endT), map);
				if("FAIL".equals(backStr)){
					writeText("查询玩家NGR接口失败!");
				}
				
				list = QtUtil.parseBetString(backStr);
				if(null != list && list.size()>0 ){
					for (PlatformData platformData : list) {
						platformData.setStarttime(startT);
						platformData.setEndtime(endT) ;
						platformData.setUuid(UUID.randomUUID().toString());
						platformData.setPlatform("qt");
						getdateService.updateQtPlatForm(platformData);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
		writeText("更新QT成功!");
		return null ;
	}
	
	public String relievePtLimit(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			writeText("会话已失效，请重新登录");
			return null;
		}
		String msg = operatorService.relievePtLimit(loginname,op.getUsername());
		writeText(msg);
		return null ;
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

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCorpBankAccount(String corpBankAccount) {
		this.corpBankAccount = corpBankAccount;
	}

	public void setCorpBankName(String corpBankName) {
		this.corpBankName = corpBankName;
	}

	public void setCreditLogType(String creditLogType) {
		this.creditLogType = creditLogType;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setDownLmit(String downLmit) {
		this.downLmit = downLmit;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public void setFirstCash(Double firstCash) {
		this.firstCash = firstCash;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public void setIsTransferIn(Integer isTransferIn) {
		this.isTransferIn = isTransferIn;
	}

	public void setJobPno(String jobPno) {
		this.jobPno = jobPno;
	}

	public String setLevel() {
		String msg = null;
		try {
			msg = operatorService.setLevel(loginname, level, getOperatorLoginname());
			if (msg == null)
				setErrormsg("等级设定成功");
			else
				setErrormsg("等级设定失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("等级设定失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String setWarnLevel() {
		String msg = null;
		try {
			msg = operatorService.setWarnLevel(loginname, level, getOperatorLoginname(), warnremark);
			if (msg == null)
				setErrormsg("警告等级设定成功");
			else
				setErrormsg("警告等级设定失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("警告等级设定失败:" + e.getMessage());
		}
		return INPUT;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public void setNetpayService(NetpayService netpayService) {
		this.netpayService = netpayService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPayBankAccount(String payBankAccount) {
		this.payBankAccount = payBankAccount;
	}

	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}

	public void setPayOrderFlag(Integer payOrderFlag) {
		this.payOrderFlag = payOrderFlag;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public void setProposalFlag(Integer proposalFlag) {
		this.proposalFlag = proposalFlag;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setProposalType(String proposalType) {
		this.proposalType = proposalType;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public void setSeqService(SeqService seqService) {
		this.seqService = seqService;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTransferFalg(Integer transferFalg) {
		this.transferFalg = transferFalg;
	}

	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}

	public void setTransID(String transID) {
		this.transID = transID;
	}

	public void setTryCredit(Double tryCredit) {
		this.tryCredit = tryCredit;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getRetypePartner() {
		return retypePartner;
	}

	public void setRetypePartner(String retypePartner) {
		this.retypePartner = retypePartner;
	}

	public String getRetypeLoginname() {
		return retypeLoginname;
	}

	public void setRetypeLoginname(String retypeLoginname) {
		this.retypeLoginname = retypeLoginname;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	// 修改用户提款错误 sun
	public String rehabUserCreditout() {
		if (StringUtil.isEmpty(loginname))
			setErrormsg("请输入账户!");
		else {
			try {
				Users user = (Users) customerService.get(Users.class, loginname);
				if (user == null)
					setErrormsg("不存在此用户");
				else {
					// transferService.transferIn(seqService.generateTransferID(),
					// loginname);// 转入
					// transferService.transferOut(seqService.generateTransferID(),
					// loginname);// 转出
					setErrormsg("已修复完成");
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg(e.getMessage());
			}
		}
		return INPUT;
	}

	// 修改单个的远程使用接口地址
	public String modifyUserRemoteUrl() {
		if (StringUtil.isEmpty(loginname))
			setErrormsg("请输入账户!");
		else {
			try {
				String msg = null;
				// String msg = customerService.modifyUserRemoteUrl(loginname,
				// remoteUrl);
				if (msg == null)
					setErrormsg("修改成功");
				else
					setErrormsg(msg);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg(e.getMessage());
			}
		}
		return INPUT;

	}

	// 发布游戏公告
	public String gameAddbulletin() {
		try {
			String msg = null;
			// String msg = RemoteCaller.addBulletin(gameid, gamecontent);
			if (msg == null)
				setErrormsg("发布成功");
			else
				setErrormsg("接口反回消息:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}

	// 参加擂台赛
	public String addMatch() {
		// try {
		// String msg = customerService.addMatch(loginname,
		// getOperatorLoginname());
		// if (msg == null)
		// setErrormsg("设置成功");
		// else
		// setErrormsg("反回消息:" + msg);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// setErrormsg(e.getMessage());
		// }
		return INPUT;
	}

	// 得到用户第一笔存款
	public String queryFirstCashin() {
		try {
			this.getRequest().setAttribute("page", customerService.getFirstCashin(loginname));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
	}

	// 提交再存优惠
	public String addOfferProposal() {
		try {
			String msg = null;
			if (flag == null || flag.equals("") || flag.equals("1")) {
				// System.out.println("无投注要求");
				msg = proposalService.addOffer(getOperatorLoginname(), StringUtil.trim(loginname), StringUtil.trim(title), Constants.FROM_BACK, firstCash, tryCredit, remark);
			} else {
				// System.out.println("有投注要求："+times);
				if (times == null || times == 0) {
					msg = "投注倍数不可为空或等于0";
				} else {
					msg = proposalService.addTimesOffer(getOperatorLoginname(), StringUtil.trim(loginname), StringUtil.trim(title), Constants.FROM_BACK, firstCash, tryCredit, times, remark);
				}
			}
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

	// 总报表信息
	public String queryReport() {
		try {
			getRequest().setAttribute("page", operatorService.queryReport(start, end, StringUtil.isEmpty(loginname) ? roleCode : null, loginname));
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	// 总报表信息
	public String queryCustomerInfoAnalysis() {
		try {
			Map<String,List> map = operatorService.queryCustomerInfoAnalysis(start, end, loginname,level,warnflag, agent, intvalday,nintvalday, intro, partner , startDate , endDate, order, by);
			getRequest().setAttribute("page", map.get("calist"));
			getRequest().setAttribute("pageSum", map.get("sumlist"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}
	
	// 总报表信息
	public String queryCustomerInfoAnalysisNew() {
		try {
			if(stringStartTime==null || stringEndTime==null){
				setErrormsg("时间不允许为空!");
				return INPUT;
			}
			start = DateUtil.fmtStandard(stringStartTime);
			end = DateUtil.fmtStandard(stringEndTime);
			Map<String,List> map = operatorService.queryCustomerInfoAnalysisNew(agentType ,start, end, loginname,level,warnflag, agent, intvalday,nintvalday, intro , partner,startDate , endDate,  order, by);
			getRequest().setAttribute("page", map.get("calist"));
			getRequest().setAttribute("pageSum", map.get("sumlist"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	public String queryCustomerInfoAnalysisNew2() {

		try {

			if (null == start) {

				setErrormsg("起始时间不能为空！");
				return INPUT;
			}

			if (null == end) {

				setErrormsg("结束时间不能为空！");
				return INPUT;
			}
			
			List list = operatorService.queryCustomerInfoAnalysisNew2(agentType, start, end, loginname, level, warnflag, agent, intvalday, nintvalday, intro, partner, startDate, endDate, order, by,flag);

			getRequest().setAttribute("page", list);
			getRequest().setAttribute("startdate", startYyyyMM);
			getRequest().setAttribute("enddate", endYyyyMM);
		} catch (Exception e) {

			e.printStackTrace();
			setErrormsg("查询出现异常，异常信息：" + e.getMessage());
		}

		return INPUT;
	}
	
	// 总报表信息详细
	public String queryReportInfo() {
		try {
			getRequest().setAttribute("page", operatorService.queryReport(start, end, StringUtil.isEmpty(loginname) ? roleCode : null, loginname));

			// 查询详细
			if (StringUtil.isEmpty(loginname)) {
				DetachedCriteria dcBetRecord = DetachedCriteria.forClass(Betrecords.class);
				if (StringUtil.isNotEmpty(roleCode)) {
					if (roleCode.equals(UserRole.MONEY_CUSTOMER.getCode()))
						dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
					// if (roleCode.equals(UserRole.FREE_CUSTOMER.getCode()))
					// dcBetRecord =
					// dcBetRecord.add(Restrictions.like("passport", "" +
					// Constants.PREFIX_FREE_CUSTOMER + "%"));
				}
				if (start != null)
					dcBetRecord = dcBetRecord.add(Restrictions.ge("billTime", start));
				if (end != null)
					dcBetRecord = dcBetRecord.add(Restrictions.lt("billTime", end));

				dcBetRecord = dcBetRecord.setProjection(Projections.groupProperty("passport"));
				List<String> list = operatorService.findByCriteria(dcBetRecord);

				if (list != null && list.size() > 0) {
					List reportList = new ArrayList<Report>();
					Iterator<String> it = list.listIterator();
					while (it.hasNext()) {
						String passport = it.next();
						reportList.add(operatorService.queryReport(start, end, null, passport));
					}
					getRequest().setAttribute("reportlist", reportList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	public String addNewAgent() {
		try {
			if (operatorService.isUseReferWebsite(referWebsite)) {
				if (p_email.matcher(email).matches()) {
					email = AESUtil.aesEncrypt(email,AESUtil.KEY);
				    phone = AESUtil.aesEncrypt(phone,AESUtil.KEY);
				    String msg = null;
					if(StringUtil.isEmpty(partner)){

						msg = operatorService.addAgent(StringUtils.trim(acode), StringUtils.trim(loginname), StringUtils.trim(accountName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), StringUtils.trim(referWebsite), getIp(), getOperatorLoginname() , agentType);
					}else{

						msg = operatorService.addAgentForRecommendedCode(StringUtils.trim(acode), StringUtils.trim(loginname), StringUtils.trim(accountName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), StringUtils.trim(partner),StringUtils.trim(referWebsite), getIp(), getOperatorLoginname() , agentType);
					}
					if (msg == null) {
						setErrormsg("提交成功");
						Users user = (Users) this.userDao.get(Users.class, loginname);

						try {
							String html = EmailTemplateHelp.toHTML(Constants.EMAIL_REGISTER_BODY_HTML, new Object[] { loginname, referWebsite, DateUtil.formatDateForStandard(user.getCreatetime()) });
							mailSender.sendmail(html, user.getEmail(), "恭喜您成为天威娱乐城代理");
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}

					} else
						setErrormsg("提交失败:" + msg);
				} else {
					this.setErrormsg("Email地址有误");
				}

			} else {
				this.setErrormsg("您填写的代理网址已经被占用，请更换其他的代理网址");
			}

		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("提交失败:" + e.getMessage());
		}
		return INPUT;
	}

	// 检查用户是否存在
	public String checkUserExsit() {
		try {
			if (operatorService.get(Users.class, loginname) == null)
				getResponse().getWriter().write("false");
			else
				getResponse().getWriter().write("true");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("系统异常");
		}
		return null;
	}

	// 自动结算洗码，载入excel文件,客户端自动转换成xml
	// 生成洗码列表完成查询所有待执行的洗码
	public String ximaList() {
		try {
			xml = StringUtil.trimToEmpty(xml);
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.eq("type", ProposalType.XIMA.getCode()));
			dc = dc.add(Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode()));
			dc = dc.add(Restrictions.eq("generateType", Constants.GENERATE_AUTO));
			List list = operatorService.findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				// AutoTask task =
				// operatorService.getLastAutoTask(AutoGenerateType.AUTO_XIMA);
				// operatorService.finishAutoTask(task.getId(), "已执行");
				return NONE;
			}
			getRequest().setAttribute("page", list);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}

	public String excuteXimaList() {
		return INPUT;

	}

	public String queryCommissions() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class);
			if (StringUtils.isNotEmpty(agent))
				dc = dc.add(Restrictions.eq("id.loginname", agent));
			if (StringUtils.isNotEmpty(partner)) {
				DetachedCriteria pDC = DetachedCriteria.forClass(Users.class);
				pDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("partner", partner));
				dc.add(Property.forName("id.loginname").in(pDC));
			}
			if(null != startDate && null != endDate){
				String sql = "select loginname from users where createtime >= '" + DateUtil.formatDateForStandard(startDate) + "' and createtime<= '"+DateUtil.formatDateForStandard(endDate)+"'  and role='AGENT' " ;
				Session session = userDao.getSessionFactory().openSession();
		        try {
					SQLQuery query = session.createSQLQuery(sql);
					List list = query.list() ;
					if(null != list && list.size()>0){
						dc.add(Restrictions.in("id.loginname", list.toArray())) ;
					}else{
						return INPUT;
					}
		        } catch (Exception e) {
					log.error("server error", e);
				} finally {
					session.close();
				}
			}
			if(startYyyyMM!=null){
				Date startTime=format.parse(startYyyyMM);
				Calendar calendar=Calendar.getInstance();  
				calendar.setTime(startTime);  
				Calendar lastDate = (Calendar) calendar.clone();  
				lastDate.add(Calendar.MONTH, +1);  
				standardFmt.format(lastDate.getTime());
				dc = dc.add(Restrictions.ge("createTime",standardFmt.parse(standardFmt.format(lastDate.getTime()))));
			}
            if(endYyyyMM!=null){
            	Date startTime=format.parse(endYyyyMM);
				Calendar calendar=Calendar.getInstance();  
				calendar.setTime(startTime);  
				Calendar lastDate = (Calendar) calendar.clone();  
				lastDate.add(Calendar.MONTH, +2);  
				standardFmt.format(lastDate.getTime());
				dc = dc.add(Restrictions.lt("createTime", standardFmt.parse(standardFmt.format(lastDate.getTime()))));
			}
			//dc = dc.add(Restrictions.eq("id.year", year));
			//dc = dc.add(Restrictions.eq("id.month", month));
            Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//				dc = dc.addOrder(o);
			}
			Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}

	public String queryCommissionrecords() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Commissionrecords.class);
			if (StringUtils.isNotEmpty(agent))
				dc = dc.add(Restrictions.eq("parent", agent));
			dc = dc.add(Restrictions.eq("id.year", year));
			dc = dc.add(Restrictions.eq("id.month", month));
			Order o = Order.desc("ximaAmount");
			dc = dc.addOrder(o);
			List list = operatorService.findByCriteria(dc);
			getRequest().setAttribute("result", list);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}
	
	public static int daysxiangcha(String dateStr1, String dateStr2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateStr1 = sdf.format(sdf2.parse(dateStr1));
			dateStr2 = sdf.format(sdf2.parse(dateStr2));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		int year1 = Integer.parseInt(dateStr1.substring(0, 4));
		int month1 = Integer.parseInt(dateStr1.substring(4, 6));
		int day1 = Integer.parseInt(dateStr1.substring(6, 8));
		int year2 = Integer.parseInt(dateStr2.substring(0, 4));
		int month2 = Integer.parseInt(dateStr2.substring(4, 6));
		int day2 = Integer.parseInt(dateStr2.substring(6, 8));
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, year1);
		c1.set(Calendar.MONTH, month1 - 1);
		c1.set(Calendar.DAY_OF_MONTH, day1);
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, year2);
		c2.set(Calendar.MONTH, month2 - 1);
		c2.set(Calendar.DAY_OF_MONTH, day2);
		long mills = c1.getTimeInMillis() > c2.getTimeInMillis() ? c1.getTimeInMillis() - c2.getTimeInMillis() : c2.getTimeInMillis() - c1.getTimeInMillis();
		return (int) (mills / 1000 / 3600 / 24);
	}
	public String queryPtCredit(){
		PrintWriter out = null;
		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");
			out=this.getResponse().getWriter();
			String loginString = NTUtils.getNTMoney(loginname);
			JSONObject jsonObj = JSONObject.fromObject(loginString);
			if(jsonObj.containsKey("balance")){
				Double	afterSkyRemoteAmount = jsonObj.getDouble("balance");
				if(afterSkyRemoteAmount==null){
					out.println("系统繁忙，请稍后尝试！");
				}else{
					out.println(afterSkyRemoteAmount/100);
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
			out.flush();
		}
		return null;
	}

	
	
	public String getPayBillno() {
		return payBillno;
	}

	public void setPayBillno(String payBillno) {
		this.payBillno = payBillno;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getSaveway() {
		return saveway;
	}

	public void setSaveway(String saveway) {
		this.saveway = saveway;
	}

	public Integer getBankinfoid() {
		return bankinfoid;
	}

	public void setBankinfoid(Integer bankinfoid) {
		this.bankinfoid = bankinfoid;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getOvertime() {
		return overtime;
	}

	public void setOvertime(Integer overtime) {
		this.overtime = overtime;
	}

	public String getNewagcode() {
		return newagcode;
	}

	public void setNewagcode(String newagcode) {
		this.newagcode = newagcode;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getIntvalday() {
		return intvalday;
	}

	public void setIntvalday(String intvalday) {
		this.intvalday = intvalday;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getBankCreditChangeType() {
		return bankCreditChangeType;
	}

	public void setBankCreditChangeType(String bankCreditChangeType) {
		this.bankCreditChangeType = bankCreditChangeType;
	}

	public Boolean getIsExecute() {
		return isExecute;
	}

	public void setIsExecute(Boolean isExecute) {
		this.isExecute = isExecute;
	}

	public String getPesoRate() {
		return pesoRate;
	}

	public void setPesoRate(String pesoRate) {
		this.pesoRate = pesoRate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getAgName() {
		return agName;
	}

	public void setAgName(String agName) {
		this.agName = agName;
	}

	public String getAgPhone() {
		return agPhone;
	}

	public void setAgPhone(String agPhone) {
		this.agPhone = agPhone;
	}

	public Integer getAgIsLogin() {
		return agIsLogin;
	}

	public void setAgIsLogin(Integer agIsLogin) {
		this.agIsLogin = agIsLogin;
	}

	public String getGifTamount() {
		return gifTamount;
	}

	public void setGifTamount(String gifTamount) {
		this.gifTamount = gifTamount;
	}

	public String getBetMultiples() {
		return betMultiples;
	}

	public void setBetMultiples(String betMultiples) {
		this.betMultiples = betMultiples;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getShippingCount() {
		return shippingCount;
	}

	public void setShippingCount(String shippingCount) {
		this.shippingCount = shippingCount;
	}

	public Proposal getPropo() {
		return propo;
	}

	public void setPropo(Proposal propo) {
		this.propo = propo;
	}

	public String getShippinginfo() {
		return shippinginfo;
	}

	public void setShippinginfo(String shippinginfo) {
		this.shippinginfo = shippinginfo;
	}
	
	public String getProposer() {
		return proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public Integer getBetflag() {
		return betflag;
	}

	public void setBetflag(Integer betflag) {
		this.betflag = betflag;
	}

	public String getMailCode() {
		return mailCode;
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}

	public String getIsreg() {
		return isreg;
	}

	public void setIsreg(String isreg) {
		this.isreg = isreg;
	}

	public String getIsdeposit() {
		return isdeposit;
	}

	public void setIsdeposit(String isdeposit) {
		this.isdeposit = isdeposit;
	}

	public String getPhonestatus() {
		return phonestatus;
	}

	public void setPhonestatus(String phonestatus) {
		this.phonestatus = phonestatus;
	}

	public String getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public Customer getIphone() {
		return iphone;
	}

	public void setIphone(Customer iphone) {
		this.iphone = iphone;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getStartYyyyMM() {
		return startYyyyMM;
	}

	public void setStartYyyyMM(String startYyyyMM) {
		this.startYyyyMM = startYyyyMM;
	}

	public String getEndYyyyMM() {
		return endYyyyMM;
	}

	public void setEndYyyyMM(String endYyyyMM) {
		this.endYyyyMM = endYyyyMM;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Userbankinfo getUserbank() {
		return userbank;
	}

	public void setUserbank(Userbankinfo userbank) {
		this.userbank = userbank;
	}

	public String getRandnum() {
		return randnum;
	}

	public void setRandnum(String randnum) {
		this.randnum = randnum;
	}

	public Date getStartPt() {
		return startPt;
	}

	public void setStartPt(Date startPt) {
		this.startPt = startPt;
	}

	public Date getEndPt() {
		return endPt;
	}

	public void setEndPt(Date endPt) {
		this.endPt = endPt;
	}

	public Date getStartNT() {
		return startNT;
	}

	public void setStartNT(Date startNT) {
		this.startNT = startNT;
	}

	public Date getEndNT() {
		return endNT;
	}

	public void setEndNT(Date endNT) {
		this.endNT = endNT;
	}

	public String getNintvalday() {
		return nintvalday;
	}

	public void setNintvalday(String nintvalday) {
		this.nintvalday = nintvalday;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsernameType() {
		return usernameType;
	}

	public void setUsernameType(String usernameType) {
		this.usernameType = usernameType;
	}

	public Double getEarebate() {
		return earebate;
	}

	public void setEarebate(Double earebate) {
		this.earebate = earebate;
	}

	public Double getBbinrebate() {
		return bbinrebate;
	}

	public void setBbinrebate(Double bbinrebate) {
		this.bbinrebate = bbinrebate;
	}

	public Double getAginrebate() {
		return aginrebate;
	}

	public void setAginrebate(Double aginrebate) {
		this.aginrebate = aginrebate;
	}

	public Double getAgrebate() {
		return agrebate;
	}

	public void setAgrebate(Double agrebate) {
		this.agrebate = agrebate;
	}

	public Double getKenorebate() {
		return kenorebate;
	}

	public void setKenorebate(Double kenorebate) {
		this.kenorebate = kenorebate;
	}

	public Double getSbrebate() {
		return sbrebate;
	}

	public void setSbrebate(Double sbrebate) {
		this.sbrebate = sbrebate;
	}

	public Double getPtrebate() {
		return ptrebate;
	}

	public void setPtrebate(Double ptrebate) {
		this.ptrebate = ptrebate;
	}

	public Integer getExamine() {
		return examine;
	}

	public void setExamine(Integer examine) {
		this.examine = examine;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(Integer proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getEmailList() {
		List<Yjnr> list;
		PrintWriter out = null;
		try {
			list = SendEmailWs.getAllMailList();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			if (null != list && list.size() > 0) {
				out = response.getWriter();
				for (Yjnr yjnr : list) {
					out.print("<option value='" + yjnr.getId() + "'>"
							+ yjnr.getName() + "</option>");
				}

			}
		} catch (Exception e) {
			// FIXME Auto-generated catch block
			out.print("wrong");
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return null;
	}

	private String newSletterId;

	public String getNewSletterId() {
		return newSletterId;
	}

	public void setNewSletterId(String newSletterId) {
		this.newSletterId = newSletterId;
	}

	public String getEmailDetilById() throws Exception {
		Yjnr yjnr = SendEmailWs.GetMailingListDetails(newSletterId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		out = response.getWriter();
		out.print(yjnr.getBodyHtml() + "#######" + yjnr.getSubject());
		out.flush();
		out.close();

		return null;
	}

	private String sjr;
	private String zt;
	private String yjnr;
	private String fslb;

	public String getSjr() {
		return sjr;
	}

	public void setSjr(String sjr) {
		this.sjr = sjr;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getYjnr() {
		return yjnr;
	}

	public void setYjnr(String yjnr) {
		this.yjnr = yjnr;
	}

	public String getFslb() {
		return fslb;
	}

	public void setFslb(String fslb) {
		this.fslb = fslb;
	}

	/**
	 * 發送郵件
	 * 
	 * @return
	 */
	public String sendEmails() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8"); 
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			String[] strs=null;
			if(null==fslb||fslb.equals("")){
				if(sjr.contains(";")){
					sjr=sjr.replace(";", ",");
					}
		     strs=sjr.split(",");
			}else if(fslb.equals("0")){
				strs =getEmails().split(",");
				
			}else if(fslb.equals("1")){
				strs =getEmails().split(",");
			}else if(fslb.equals("4")){
				strs =getEmails456().split(",");
			}else if(fslb.equals("5")){
				strs =getEmails456().split(",");
			}else if(fslb.equals("6")){
				strs =getEmails456().split(",");
			}
			//SendEmailWsByEdm se = new SendEmailWsByEdm();
			//String result=se.sendemail(strs, zt, yjnr);
			
			//取随机内容段落
			StringBuffer sbf=new StringBuffer("");
			DetachedCriteria dc = DetachedCriteria.forClass(Emailremark.class);
			List<Emailremark> list = proposalService.findByCriteria(dc) ;
			int z = (int)(Math.random() * (6)); 
			z=z+1;
			for(int i=0;i<z;i++){
			//取随机第N条
			Random r=new Random();
			int s=r.nextInt(list.size())-1;
			if(s<0){
				s=0;
			}
			Emailremark em =list.get(s);//
			String str=em.getNr();
			int x = (int)(Math.random() * (str.length()-100)); 
			int y=x+100;
			sbf.append(str.substring(x, y));
			}			
			String sfjy =querySystemConfig("type021","001","否");//是否禁用自建邮件 如果禁用 则使用edm
			String result="";
			if(!StringUtil.isEmpty(sfjy)){
				 result=SendEmailWsByMyself.sendemail(strs,zt,yjnr);
			}else{
				 result=SendEmailWsByEdm.sendemail(strs, zt, yjnr);
			}
			if("发送成功".equals(result)){
				out.println("发送成功");
			}else{
				out.println("发送失败："+result);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		
		return null;
		/*
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String[] strs = null;
			if (null == fslb || fslb.equals("")) {
				strs = sjr.split(",");
			} else if (fslb.equals("0")) {
				strs = getEmails().split(",");

			} else if (fslb.equals("1")) {
				strs = getEmails().split(",");
			} else if (fslb.equals("4")) {
				strs = getEmails456().split(",");
			} else if (fslb.equals("5")) {
				strs = getEmails456().split(",");
			} else if (fslb.equals("6")) {
				strs = getEmails456().split(",");
			}
			if (null == newSletterId || StringUtils.isEmpty(newSletterId)
					|| newSletterId.equals("0")) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (yjnr.contains("html") || yjnr.contains("head")) {
					yjnr = yjnr.replace("\n", "<br/>");
					yjnr = yjnr.replace(" ", "&nbsp;");
					yjnr = yjnr.replace("\t", "&nbsp;&nbsp;");
					yjnr = yjnr.replaceAll("\"", "\\\\\"");
					StringBuffer sbf = new StringBuffer(
							"<html><head><title></title></head><body>");
					sbf.append(yjnr);
					sbf.append("</body></html>");
					map = SendEmailWs.CreateSendNewsletter(sbf.toString(), "",
							zt, zt);
				} else {
					yjnr = yjnr.replace("\n", "<br/>");
					yjnr = yjnr.replace(" ", "&nbsp;");
					yjnr = yjnr.replace("\t", "&nbsp;&nbsp;");
					yjnr = yjnr.replaceAll("\"", "\\\\\"");
					StringBuffer sbf = new StringBuffer(
							"<html><head><title></title></head><body><div>");
					sbf.append("<div  style='float:right'>如果不能打开邮件，请 "
							+ "<a href='##WebVersion##'>点击这里</a></div><br/>");
					sbf.append("<div style='float:right'><a href='##OptOutAll##'>若要取消訂閱該業務通訊，請按一下此連結</a></div><br/><br/>");
					sbf.append(yjnr);
					sbf.append("<br/>");
					sbf.append("</div></body></html>");
					map = SendEmailWs.CreateSendNewsletter(sbf.toString(), "",
							zt, zt);
				}
				if (!(map.get("Code") + "").equals("1")) {
					out.println("在邮件服务器创建邮件失败，请联系管理员，或再次尝试！");
					return null;
				}
				newSletterId = map.get("Result") + "";
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				if (yjnr.contains("html") || yjnr.contains("head")) {
					yjnr = yjnr.replace("\n", "<br/>");
					yjnr = yjnr.replace(" ", "&nbsp;");
					yjnr = yjnr.replace("\t", "&nbsp;&nbsp;");
					yjnr = yjnr.replaceAll("\"", "\\\\\"");
					map = SendEmailWs.UpdateNewsletter(yjnr, "", zt, zt,
							newSletterId);
				} else {
					yjnr = yjnr.replace("\n", "<br/>");
					yjnr = yjnr.replace(" ", "&nbsp;");
					yjnr = yjnr.replace("\t", "&nbsp;&nbsp;");
					yjnr = yjnr.replaceAll("\"", "\\\\\"");
					StringBuffer sbf = new StringBuffer(
							"<html><head><title></title></head><body><div>");
					sbf.append("<div  style='float:right'>如果不能打开邮件，请 "
							+ "<a href='##WebVersion##'>点击这里</a></div><br/>");
					sbf.append("<div style='float:right'><a href='##OptOutAll##'>若要取消訂閱該業務通訊，請按一下此連結</a></div><br/><br/>");
					sbf.append(yjnr);
					sbf.append("<br/>");
					sbf.append("</div></body></html>");
					map = SendEmailWs.UpdateNewsletter(sbf.toString(), "", zt,
							zt, newSletterId);
				}
				if (!(map.get("Code") + "").equals("1")) {
					out.println("在邮件服务器修改邮件失败，请联系管理员，或再次尝试！");
					return null;
				}
			}
			SendEmailWs se = new SendEmailWs();
			Map<String, Object> mapEmail = se.SendNewsletter(strs,
					newSletterId, operatorService.getHibernateTemplate());
			if (!(mapEmail.get("Code") + "").equals("1")) {
				out.println("在邮件服务器发送邮件失败，请联系管理员，或再次尝试！");
				return null;
			}
			out.println("邮件发送成功！共发送" + strs.length + "封邮件！");
		} catch (IOException e) {
			out.println("在邮件服务器发送邮件失败，请联系管理员，或再次尝试！");
			e.printStackTrace();
		} catch (Exception e) {
			out.println("在邮件服务器发送邮件失败，请联系管理员，或再次尝试！");
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return null;
	*/}
	
	/**
	 * 电销专员电话记录(群发邮件短信)
	 * @author ck
	 * @return
	 */
	public String teleGroupSendMail() {
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8"); 
		PrintWriter out = null;

		String[] idNu = ids.split(",");
		DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
		dc.add(Restrictions.in("id", StringUtil.strArray2intArray(idNu)));
		//dc.add(Restrictions.eq(propertyName, dc));
		List<Customer> list = proposalService.findByCriteria(dc);
		StringBuffer mail = new StringBuffer("");
		for (int i = 0; i < list.size(); i++) {
			Customer customer = list.get(i);
			if (i != 0) {
				mail.append("," + customer.getEmail());
			} else {
				mail.append(customer.getEmail());
			}
		}
		this.yjnr = remark; //信息内容
		this.sjr = mail.toString();
		
		
		try {
			out = response.getWriter();
			String[] strs=null;
			if(null==fslb||fslb.equals("")){
			  if(sjr.contains(";"))
				sjr=sjr.replace(";", ",");
				
		      strs=sjr.split(",");
			}else if(fslb.equals("0"))
				strs =getEmails().split(",");
			else if(fslb.equals("1"))
				strs =getEmails().split(",");
			else if(fslb.equals("4"))
				strs =getEmails456().split(",");
			else if(fslb.equals("5"))
				strs =getEmails456().split(",");
			else if(fslb.equals("6"))
				strs =getEmails456().split(",");
			
			ReturnBean rbean=SendYiYeEmail.sendYiYeMail(strs, zt, yjnr, "EXTERNAL",getOperatorLoginname(),"tele");
			
			if(rbean.getSuccess().intValue()==200)
				out.println("发送成功");
			else
				out.println("发送失败："+rbean.getErrormsg());
			
		} catch (IOException e) {
			out.println("发送失败："+"系统错误");
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	

	public String youjianQunFa() {
		String[] idNu = ids.split(",");
		DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
		dc.add(Restrictions.in("id", StringUtil.strArray2intArray(idNu)));
		//dc.add(Restrictions.eq(propertyName, dc));
		List<Customer> list = proposalService.findByCriteria(dc);
		StringBuffer phones = new StringBuffer("");
		for (int i = 0; i < list.size(); i++) {
			Customer customer = list.get(i);
			if (i != 0) {
				phones.append("," + customer.getEmail());
			} else {
				phones.append(customer.getEmail());
			}
		}
		this.yjnr = remark;
		this.sjr = phones.toString();
		this.sendEmails();
		return null;
	}

	private String getEmails() {
		String emailsss = "";
		if (roleCode != null && !roleCode.equals("")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("email"));
			dc = dc.add(Restrictions.eq("role", roleCode));
			dc = dc.add(Restrictions.isNotNull("email"));
			dc = dc.add(Restrictions.eq("flag", 0));
			if (beginTime != null) {
				dc = dc.add(Restrictions.ge("createtime", beginTime));
			}
			if (endTime != null) {
				dc = dc.add(Restrictions.lt("createtime", endTime));
			}
			Integer pageCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, pageCount, null).getPageContents();
			int forCount = usersList.size();
			for (int i = 0; i < forCount; i++) {
				Users user = (Users) usersList.get(i);
				if (user.getEmail() != null && !user.getEmail().equals("")&& user.getEmail().length() > 0) {
					
					emailsss = emailsss + "," + user.getEmail();
					// email = email + "," +
					// SpecialEnvironmentStringPBEConfig.getEmailNormalText(user.getEmail());;
				}
			}
			if (emailsss.startsWith(",")) {
				emailsss = emailsss.substring(1);
			}
			// System.out.println("*******"+email);
		} else if (level != null && !level.equals("")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("email"));
			dc = dc.add(Restrictions.eq("level", level));
			dc = dc.add(Restrictions.isNotNull("email"));
			dc = dc.add(Restrictions.eq("flag", 0));
			if (beginTime != null) {
				dc = dc.add(Restrictions.ge("createtime", beginTime));
			}
			if (endTime != null) {
				dc = dc.add(Restrictions.lt("createtime", endTime));
			}
			Integer pageCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, pageCount, null).getPageContents();
			int forCount = usersList.size();
			for (int i = 0; i < forCount; i++) {
				Users user = (Users) usersList.get(i);
				if (user.getEmail() != null && !user.getEmail().equals("")
						&& user.getEmail().length() > 0) {
					emailsss = emailsss + "," + user.getEmail();
				}
			}
			if (emailsss.startsWith(",")) {
				emailsss = emailsss.substring(1);
			}
		}
		return emailsss;
	}

	private String agentValue;

	private String introValue;

	public String getAgentValue() {
		return agentValue;
	}

	public void setAgentValue(String agentValue) {
		this.agentValue = agentValue;
	}

	public String getIntroValue() {
		return introValue;
	}

	public void setIntroValue(String introValue) {
		this.introValue = introValue;
	}

	public Date endTime;
	public Date beginTime;
	public String isMoney;
	private String merchants;
	private Integer payWay;
	private RedEnvelopeActivity redEnvelopeActivity;
	public RedEnvelopeActivity getRedEnvelopeActivity() {
		return redEnvelopeActivity;
	}

	public void setRedEnvelopeActivity(RedEnvelopeActivity redEnvelopeActivity) {
		this.redEnvelopeActivity = redEnvelopeActivity;
	}
	
	
	private String uaccountno;
	
	
	public String getUaccountno() {
		return uaccountno;
	}

	public void setUaccountno(String uaccountno) {
		this.uaccountno = uaccountno;
	}



	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public String getMerchants() {
		return merchants;
	}

	public void setMerchants(String merchants) {
		this.merchants = merchants;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getIsMoney() {
		return isMoney;
	}

	public void setIsMoney(String isMoney) {
		this.isMoney = isMoney;
	}

	private String getEmails456() {
		String emailsss = "";
		if (fslb.equals("4")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("email"));
			// dc = dc.add(Restrictions.eq("role", roleCode));
			dc = dc.add(Restrictions.isNotNull("email"));
			dc = dc.add(Restrictions.eq("agent", agentValue));
			if (beginTime != null) {
				dc = dc.add(Restrictions.ge("createtime", beginTime));
			}
			if (endTime != null) {
				dc = dc.add(Restrictions.lt("createtime", endTime));
			}
			dc = dc.add(Restrictions.eq("flag", 0));
			Integer pageCount = PageQuery.queryForCount(
					operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(
					operatorService.getHibernateTemplate(), dc, 1, pageCount, null)
					.getPageContents();
			int forCount = usersList.size();
			for (int i = 0; i < forCount; i++) {
				Users user = (Users) usersList.get(i);
				if (user.getEmail() != null && !user.getEmail().equals("")
						&& user.getEmail().length() > 0) {
					emailsss = emailsss + "," + user.getEmail();
					// emailsss = emailsss + "," +
					// SpecialEnvironmentStringPBEConfig.getEmailNormalText(user.getEmail());;
				}
			}
			if (emailsss.startsWith(",")) {
				emailsss = emailsss.substring(1);
			}
			// System.out.println("*******"+email);
		} else if (fslb.equals("5")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("email"));
			dc = dc.add(Restrictions.eq("intro", introValue));
			if (beginTime != null) {
				dc = dc.add(Restrictions.ge("createtime", beginTime));
			}
			if (endTime != null) {
				dc = dc.add(Restrictions.lt("createtime", endTime));
			}
			dc = dc.add(Restrictions.isNotNull("email"));
			dc = dc.add(Restrictions.eq("flag", 0));
			Integer pageCount = PageQuery.queryForCount(
					operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(
					operatorService.getHibernateTemplate(), dc, 1, pageCount, null)
					.getPageContents();
			int forCount = usersList.size();
			for (int i = 0; i < forCount; i++) {
				Users user = (Users) usersList.get(i);
				if (user.getEmail() != null && !user.getEmail().equals("")
						&& user.getEmail().length() > 0) {
					// emailsss = emailsss + "," +
					// SpecialEnvironmentStringPBEConfig.getEmailNormalText(user.getEmail());;
					emailsss = emailsss + "," + user.getEmail();
				}
			}
			if (emailsss.startsWith(",")) {
				emailsss = emailsss.substring(1);
			}
		} else if (fslb.equals("6")) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.setProjection(Projections.property("email"));
			dc = dc.add(Restrictions.isNotNull("email"));
			if (beginTime != null) {
				dc = dc.add(Restrictions.ge("createtime", beginTime));
			}
			if (endTime != null) {
				dc = dc.add(Restrictions.lt("createtime", endTime));
			}
			if (isMoney.equals("有存款")) {
				dc = dc.add(Restrictions.isNotNull("credit"));
				dc = dc.add(Restrictions.not(Restrictions.eq("credit", 0.0)));
			} else {
				dc = dc.add(Restrictions.or(Restrictions.isNull("credit"),
						Restrictions.eq("credit", 0.0)));
			}
			dc = dc.add(Restrictions.eq("flag", 0));
			Integer pageCount = PageQuery.queryForCount(
					operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(
					operatorService.getHibernateTemplate(), dc, 1, pageCount, null)
					.getPageContents();
			int forCount = usersList.size();
			for (int i = 0; i < forCount; i++) {
				Users user = (Users) usersList.get(i);
				if (user.getEmail() != null && !user.getEmail().equals("")
						&& user.getEmail().length() > 0) {
					// emailsss = emailsss + "," +
					// SpecialEnvironmentStringPBEConfig.getEmailNormalText(user.getEmail());;
					emailsss = emailsss + "," + user.getEmail();
				}
			}
			if (emailsss.startsWith(",")) {
				emailsss = emailsss.substring(1);
			}
		}
		return emailsss;
	}
	
	public String getAcceptName() {
		return acceptName;
	}

	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}
	
	/**
	 *  查询PT大爆炸活动记录
	 * @return
	 */
	public String queryPTBigBangRecords(){
		Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			String countS = "select count(1), IFNULL(sum(a.giftmoney), 0) ";
			String selectS = "select a.username, a.netwin_lose, a.startTime, a.endTime, a.bonus, a.giftmoney, a.status, a.times, a.distributeTime, a.getTime, a.remark, b.intro ";
			String midS = "FROM ptbigbang a LEFT JOIN users b on a.username = b.loginname ";
			String whereS = "where 1=1 ";
			if (StringUtils.isNotEmpty(status))
				whereS += "and a.status = '"+status+"' ";  //订单状态
			if (StringUtils.isNotEmpty(loginname))
				whereS += "and a.username = '"+loginname+"' ";
			if (StringUtils.isNotEmpty(intro))
				whereS += "and b.intro = '"+intro+"' ";
			
			if (StringUtils.isNotEmpty(distributeDate)){
				/*Calendar cd = Calendar.getInstance();
				cd.setTime(DateUtil.parseDateForYYYYmmDD(distributeDate));
				whereS += "and a.distributeTime>='"+DateUtil.formatDateForStandard(cd.getTime())+"' ";
				cd.add(Calendar.DAY_OF_MONTH, 1);
				whereS += "and a.distributeTime<='"+DateUtil.formatDateForStandard(cd.getTime())+"' ";*/
				whereS += " and a.distributeDay = '"+distributeDate+"' ";
			}
			
			if(StringUtils.isNotEmpty(type)){
				if(type.equals("1")){
					whereS += "and a.netwin_lose < 0.00 ";
				}else if(type.equals("2")){
					whereS += "and a.netwin_lose >= 0.00 ";
				}
			}
			if (StringUtils.isNotEmpty(by)) {
				whereS += "ORDER BY a."+by+("desc".equals(order)?" DESC ":" ASC ");
			}
			Query query = session.createSQLQuery(countS+midS+whereS);
			Object[] countAry = (Object[])(query.list().get(0));
			Integer total = Integer.parseInt(countAry[0].toString());
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			
			query = session.createSQLQuery(selectS+midS+whereS);
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setStatics1(Double.parseDouble(countAry[1].toString()));
			page.setTotalRecords(total);
			int pages = PagenationUtil.computeTotalPages(total, size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			getRequest().setAttribute("page", page);
		}catch (Exception e) {
			log.error("server error", e);
		}finally {
			session.close();
		}
		return INPUT;
	}
	public String queryRedEnvelopeActivityList() {
		DetachedCriteria dc = DetachedCriteria.forClass(RedEnvelopeActivity.class);

		if (redEnvelopeActivity != null) {

			if (StringUtils.isNotBlank(redEnvelopeActivity.getTitle())) {

				dc.add(Restrictions.like("title", redEnvelopeActivity.getTitle(), MatchMode.ANYWHERE));
			}

			if (StringUtils.isNotBlank(redEnvelopeActivity.getPlatformId())) {

				dc.add(Restrictions.eq("platformId", redEnvelopeActivity.getPlatformId()));
			}
		}

		dc.add(Restrictions.eq("deleteFlag", "N"));

		Order o = null;

		if (StringUtils.isNotEmpty(by)) {

			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {

			o = Order.desc("createTime");
		}

		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);

		getRequest().setAttribute("page", page);

		return INPUT;
	}

	public void addRedEnvelopeActivity() {

		try {

			if (redEnvelopeActivity == null) {

				GsonUtil.GsonObject("新增失败：数据为空！");
				return;
			}

			String operator = getOperatorLoginname();
			Date currentDate = new Date();

			if (StringUtils.isBlank(operator)) {

				GsonUtil.GsonObject("新增失败：登录会话已失效，请重新登录后再进行操作！");
				return;
			}

			redEnvelopeActivity.setDeleteFlag("N");
			redEnvelopeActivity.setCreateUser(operator);
			redEnvelopeActivity.setCreateTime(currentDate);
			redEnvelopeActivity.setUpdateUser(operator);
			redEnvelopeActivity.setUpdateTime(currentDate);
			redEnvelopeActivity.setPlatformId("红包雨账户");
			proposalService.save(redEnvelopeActivity);

			GsonUtil.GsonObject("操作成功！");
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
		}
	}
	public void queryRedEnvelopeActivity() {

		if (redEnvelopeActivity == null || redEnvelopeActivity.getId() == null) {

			String result = "{ \"status\": \"404\", \"message\": \"查询失败：参数值错误！\"}";
			GsonUtil.GsonObject(result);
			return;
		}

		RedEnvelopeActivity obj = (RedEnvelopeActivity) proposalService.get(RedEnvelopeActivity.class, redEnvelopeActivity.getId());

		if (obj == null) {

			String result = "{ \"status\": \"404\", \"message\": \"查询失败：查询的记录不存在！\"}";
			GsonUtil.GsonObject(result);
			return;
		}

		String data = JSONObject.fromObject(obj).toString();
		GsonUtil.GsonObject(data);
	}
	public void updateRedEnvelopeActivity() {

		try {

			if (redEnvelopeActivity == null || redEnvelopeActivity.getId() == null) {

				GsonUtil.GsonObject("修改失败：数据为空！");
				return;
			}

			RedEnvelopeActivity obj = (RedEnvelopeActivity) proposalService.get(RedEnvelopeActivity.class, redEnvelopeActivity.getId());

			if (obj == null) {

				GsonUtil.GsonObject("修改失败：修改的记录不存在！");
				return;
			}

			String operator = getOperatorLoginname();
			Date currentDate = new Date();

			if (StringUtils.isBlank(operator)) {

				GsonUtil.GsonObject("新增失败：登录会话已失效，请重新登录后再进行操作！");
				return;
			}
			redEnvelopeActivity.setStartTime(DateUtil.fmtStandard(stringStartTime));
			redEnvelopeActivity.setEndTime(DateUtil.fmtStandard(stringEndTime));
			redEnvelopeActivity.setDeleteFlag(obj.getDeleteFlag());
			redEnvelopeActivity.setCreateUser(obj.getCreateUser());
			redEnvelopeActivity.setCreateTime(obj.getCreateTime());
			redEnvelopeActivity.setUpdateUser(operator);
			redEnvelopeActivity.setUpdateTime(currentDate);
			redEnvelopeActivity.setPlatformId("红包雨账户");
			proposalService.update(redEnvelopeActivity);

			GsonUtil.GsonObject("操作成功！");
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
		}
	}

	public String queryRedRainCredit(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			RedRainWallet redRainWallet =(RedRainWallet) proposalService.get(RedRainWallet.class, loginname);
			if(redRainWallet==null){
				out.println(0.0);
			}else {
				out.println(redRainWallet.getAmout());
			}

		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}

	public void deleteRedEnvelopeActivity() {
		if (StringUtils.isBlank(ids)) {

			GsonUtil.GsonObject("删除失败：数据为空！");
			return;
		}

		Session session = null;

		try {
			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();
			String sql="update B_RED_ENVELOPE_ACTIVITY set delete_flag='Y' where id in ("+ids+")";
			Query query = session.createSQLQuery(sql);

			query.executeUpdate();

			GsonUtil.GsonObject("删除成功！");
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
		} finally {

			if (null != session && session.isOpen()) {

				session.close();
			}
		}
	}

	public void deleteRedRainDataAll(){
		try {
			HashMap<String, Object> hashMap = new HashMap<>();
			String sql="UPDATE red_rain_wallet set amout=0.0,remark=NULL";
			proposalService.excuteSql(sql, hashMap);
			this.println("清零成功");
			return;
		}catch (Exception e){
			e.printStackTrace();
			this.println("清零失败");
			return;
		}

	}
	public void editLimitRedRain(){
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
			dc.add(Restrictions.eq("typeNo", "type987"));
			dc.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> configs = transferService.findByCriteria(dc);
			if (configs == null || configs.size() < 1) {
				this.println("请先配置限额");
				return;
			}
			SystemConfig systemConfig = configs.get(0);
			systemConfig.setValue(value);
			transferService.update(systemConfig);
			this.println("修改成功");
			return;
		}catch (Exception e){
			e.printStackTrace();
			this.println("修改失败");
			return;
		}

	}

	public void queryLimitRedRain(){
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
			dc.add(Restrictions.eq("typeNo", "type987"));
			dc.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> configs = transferService.findByCriteria(dc);
			if (configs == null || configs.size() < 1) {
				this.println("ERRO");
				return;
			}
			SystemConfig systemConfig = configs.get(0);
			this.println(systemConfig.getValue());
			return;
		}catch (Exception e){
			e.printStackTrace();
			this.println("ERRO");
			return;
		}

	}

	/**
	 * 查询活动记录
	 * @return
	 */
	public String getActivityHistoryRedRain(){
		HashMap<String, String> map = new HashMap<>();
		map.put("redrain", "红包雨活动");
		DetachedCriteria hd=null;
		String s = map.get(type);
		if(null==s||s.isEmpty()){
			return INPUT;
		}
		Double sum=0.0;
		hd= DetachedCriteria.forClass(Activity.class);


		hd.add(Restrictions.eq("activityName", s));

		if (startTime != null){
			hd = hd.add(Restrictions.ge("createDate", startTime));
		}
		if(null!=keyword){
			hd = hd.add(Restrictions.like("remark",keyword,MatchMode.ANYWHERE));
		}
		if(!ids.isEmpty()){
			hd = hd.add(Restrictions.like("remark","红包ID-"+ids,MatchMode.ANYWHERE));
		}
		if(!username.isEmpty()){
			hd = hd.add(Restrictions.eq("userrole", username));
		}
		if (startTime != null){
			hd = hd.add(Restrictions.ge("createDate", startTime));
		}
		if (endTime != null){
			hd = hd.add(Restrictions.le("createDate", endTime));
		}
		List<Activity>  page2 = proposalService.findByCriteria(hd);
		Page  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), hd, pageIndex, size, Order.desc("createDate"));
		if(null!=page2&&page2.size()>0){
			for (Activity activity:page2){
				double v = activity.getActivityPercent() == null ? 0.0 : activity.getActivityPercent();
				sum=sum+v;
			}
		}
		getRequest().setAttribute("page", page);
		getRequest().setAttribute("sum", sum.toString());
		return INPUT;
	}

	public String getActivityHistoryLu(){

		DetachedCriteria hd=null;
		Double sum=0.0;
		hd= DetachedCriteria.forClass(Activity.class);


		hd.add(Restrictions.eq("activityName", "别撸了,战起来"));

		if (startTime != null){
			hd = hd.add(Restrictions.ge("createDate", startTime));
		}
		if(!username.isEmpty()){
			hd = hd.add(Restrictions.eq("userrole", username));
		}
		if (startTime != null){
			hd = hd.add(Restrictions.ge("createDate", startTime));
		}
		if (endTime != null){
			hd = hd.add(Restrictions.le("createDate", endTime));
		}
		if (StringUtils.isNotEmpty(type)){
			hd = hd.add(Restrictions.eq("activityStatus", Integer.valueOf(type)));
		}
		List<Activity>  page2 = proposalService.findByCriteria(hd);
		Page  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), hd, pageIndex, size, Order.desc("createDate"));
		if(null!=page2&&page2.size()>0){
			for (Activity activity:page2){
				double v = activity.getActivityPercent() == null ? 0.0 : activity.getActivityPercent();
				sum=sum+v;
			}
		}
		getRequest().setAttribute("page", page);
		getRequest().setAttribute("sum", sum.toString());
		return INPUT;
	}

	public void batchUpdateRedEnvelopeActivity() {

		if (StringUtils.isBlank(ids)) {

			GsonUtil.GsonObject("修改失败：数据为空！");
			return;
		}

		String updateSql = "update B_RED_ENVELOPE_ACTIVITY set :updateField = :updateValue where id in (:ids)";
		String updateField = "";

		if ("1".equals(type)) {

			updateField = "deposit_start_time";
		} else if ("2".equals(type)) {

			updateField = "deposit_end_time";
		} else if ("3".equals(type)) {

			updateField = "bet_start_time";
		} else if ("4".equals(type)) {

			updateField = "bet_end_time";
		} else if ("5".equals(type)) {

			updateField = "start_time";
		} else if ("6".equals(type)) {

			updateField = "end_time";
		}

		updateSql = updateSql.replace(":updateField", updateField);
		updateSql = updateSql.replace(":ids", ids);

		Session session = null;

		try {

			Map<String, Object> paramsMap = new HashMap<String, Object>();

			paramsMap.put("updateValue", executeTime);

			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createSQLQuery(updateSql);
			query.setProperties(paramsMap);

			query.executeUpdate();

			GsonUtil.GsonObject("修改成功！");
		} catch (Exception e) {

			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后再试！");
		} finally {

			if (null != session && session.isOpen()) {

				session.close();
			}
		}
	}
	
	/**
	 * 计算PT 大爆炸活动礼金
	 */
	@SuppressWarnings("unchecked")
	public void calculatePTBigBangGifgMoney(){
		DetachedCriteria dc = DetachedCriteria.forClass(PTBigBang.class);
		//未派发状态
		dc = dc.add(Restrictions.eq("status", "0"));
		dc = dc.add(Restrictions.eq("distributeDay", DateUtil.fmtYYYY_MM_DD(new Date())));
		if(type.equals("1")){
			//玩家正赢反赠
			dc = dc.add(Restrictions.lt("netWinOrLose", 0.0));
		}else if(type.equals("-1")){
			//玩家负赢反赠
			dc = dc.add(Restrictions.ge("netWinOrLose", 0.0));
		}else{
			this.println("请指定类型");
			return;
		}
		List<PTBigBang> ptTigerBettingList = proposalService.findByCriteria(dc);
		for (PTBigBang item : ptTigerBettingList) {
			try {
				losePromoService.calculatePTBigBangGifgMoney(item);
			} catch (Exception e) {
				continue;
			}
		}
		this.println("PT大爆炸计算礼金执行完毕");
	}

	/**
	 * 取消PT大爆炸PT转出流水限制
	 */
	@SuppressWarnings("unchecked")
	public void removePTBigBangTransferLimit(){
		DetachedCriteria dc = DetachedCriteria.forClass(PTBigBang.class);
		dc = dc.add(Restrictions.eq("username", loginname));
		dc = dc.add(Restrictions.eq("status", "2"));  //已领取
		List<PTBigBang> bigBangList = proposalService.findByCriteria(dc);
		for (PTBigBang item : bigBangList) {
			item.setStatus("3");
			transferService.update(item);
		}
	}
	
	/**
	 * 派发周周回馈的赠送
	 */
	public void excWeekSent(){
		Map<String, Double> amountMap = new HashMap<String, Double>(); 
		//获得上周一和上周日
		Date now = new Date();
		Date monday = DateUtil.parseDateForYYYYmmDD(DateUtil.fmtYYYY_MM_DD(DateUtil.getLastWeekMonday(now)));
		Date sunday = DateUtil.parseDateForYYYYmmDD(DateUtil.fmtYYYY_MM_DD(DateUtil.getLastWeekSunday(now)));
		//检测是否重复派发
		DetachedCriteria dc = DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("promoDateStart", monday));
		dc.add(Restrictions.eq("promoDateEnd", sunday));
		dc.setProjection(Projections.rowCount());
		Integer count = (Integer)proposalService.findByCriteria(dc).get(0);
		if(count > 0){
			this.println("警告！检测到重复派发");
			return;
		}
		
		//createTime是T-1天的数据
		String sqlStr = "select loginname, SUM(bettotal) from agprofit where TO_DAYS(createTime) >= TO_DAYS(:monday)+1 and TO_DAYS(createTime) <= TO_DAYS(:sunday)+1 and platform in(:gpi, :ttg, :nt, :qt) GROUP BY loginname";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("monday", monday);
		params.put("sunday", sunday);
		params.put("gpi", "gpi");
		params.put("ttg", "ttg");
		params.put("nt", "nt");
		params.put("qt", "qt");
		try {
			List bettotalList = weekSentService.getListBySql(sqlStr, params);
			for (Object object : bettotalList) {
				Object[] arrObj = (Object[]) object;
				amountMap.put(arrObj[0].toString().toLowerCase(), Double.parseDouble(arrObj[1].toString()));
			}
		} catch (Exception e) {
			this.println("查询GPI、TTG、NT投注额异常");
			e.printStackTrace();
			return;
		}
		
		//查询PT slot投注额
		sqlStr = "select PLAYERNAME, BETS_TIGER + PROGRESSIVE_BETS from pt_data_new where TO_DAYS(STARTTIME) >= TO_DAYS(:monday) and TO_DAYS(ENDTIME) <= TO_DAYS(:sunday)";
		
		try {
			List betsTigerList = weekSentService.getListBySql(sqlStr, params);
			for (Object object : betsTigerList) {
				Object[] arrObj = (Object[]) object;
				String userName = arrObj[0].toString().substring(1).toLowerCase();
				if(!amountMap.containsKey(userName)){
					amountMap.put(userName, Double.parseDouble(arrObj[1].toString()));
				}else{
					Double oldBet = amountMap.get(userName);
					amountMap.put(userName, Arith.add(oldBet, Double.parseDouble(arrObj[1].toString())));
				}
			}
		} catch (Exception e) {
			this.println("查询PT老虎机投注额异常");
			e.printStackTrace();
			return;
		}
		
		for (String loginName : amountMap.keySet()) {
			if(amountMap.get(loginName) >= WeekSentVO.startAmount){
				weekSentService.excWeekSent(loginName, amountMap.get(loginName), monday, sunday);
			}
		}
		this.println("周周回馈执行完毕");
	}

	/**
	 *  查询周周回馈记录
	 * @return
	 */
	public String queryWeekSentRecords(){
		Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			String countS = "select count(1) ";
			String selectS = "select a.pno, a.username, b.intro, a.promodatestart, a.promodateend, a.amount, a.promo, a.status, a.platform, a.creattime, a.gettime, a.remark, a.times, a.betting ";
			String midS = "FROM weeksent a LEFT JOIN users b on a.username = b.loginname ";
			String whereS = "where 1=1 ";
			if (StringUtils.isNotEmpty(status))
				whereS += "and a.status = '"+status+"' ";  //订单状态
			if (StringUtils.isNotEmpty(loginname))
				whereS += "and a.username = '"+loginname+"' ";
			if (StringUtils.isNotEmpty(intro))
				whereS += "and b.intro = '"+intro+"' ";
			
			if (start != null)
				whereS += "and a.creattime>='"+DateUtil.formatDateForStandard(start)+"' ";
			if (end != null)
				whereS += "and a.creattime<='"+DateUtil.formatDateForStandard(end)+"' ";
			
			if (StringUtils.isNotEmpty(by)) {
				whereS += "ORDER BY "+by+("desc".equals(order)?" DESC ":" ASC ");
			}
			Query query = session.createSQLQuery(countS+midS+whereS);
			Integer total = Integer.parseInt(query.list().get(0).toString());
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			
			query = session.createSQLQuery(selectS+midS+whereS);
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List<WeekSent> contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(total);
			int pages = PagenationUtil.computeTotalPages(total, size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}
	
	/**
	 * 取消周周回馈转出流水限制
	 */
	public void removeWeekSentTransferLimit(){
		DetachedCriteria dc = DetachedCriteria.forClass(WeekSent.class);
		dc = dc.add(Restrictions.eq("pno", pno));
		dc = dc.add(Restrictions.eq("status", "1"));  //已领取
		List<WeekSent> wsList = proposalService.findByCriteria(dc);
		for (WeekSent item : wsList) {
			item.setStatus("2");
			transferService.update(item);
		}
	}
	
	/**
	 * 取消该条周周回馈
	 */
	public void cancelWeekSent(){
		DetachedCriteria dc = DetachedCriteria.forClass(WeekSent.class);
		dc = dc.add(Restrictions.eq("pno", pno));
		dc = dc.add(Restrictions.eq("status", "0"));  //未领取
		List<WeekSent> wsList = proposalService.findByCriteria(dc);
		for (WeekSent item : wsList) {
			item.setStatus("3");
			transferService.update(item);
		}
		//取消相应提案
		DetachedCriteria dcp = DetachedCriteria.forClass(Proposal.class);
		dcp = dcp.add(Restrictions.eq("pno", pno));
		dcp = dcp.add(Restrictions.eq("type", ProposalType.WEEKSENT.getCode()));
		List<Proposal> pList = proposalService.findByCriteria(dcp);
		for(Proposal proposal : pList){
			proposal.setFlag(ProposalFlagType.CANCLED.getCode());
			proposal.setRemark(proposal.getRemark() + ";取消:" + getOperatorLoginname());
			transferService.update(proposal);
		}
	}
	
	/**
	 * 派发负盈利反赠
	 */
	public void distributeLosePromo(){
		Map<String, Double> winloseMap = new HashMap<String, Double>(); 
		String sqlStr = "select loginname, SUM(amount) from agprofit where TO_DAYS(createTime) = TO_DAYS(:now) and platform in(:gpi, :ttg, :nt, :png, :qt, :mg, :dt, :sw) GROUP BY loginname";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("now", new Date());
		params.put("gpi", "gpi");
		params.put("ttg", "ttg");
		params.put("nt", "nt");
		params.put("png", "png");
		params.put("qt", "qt");
		params.put("mg", "mg");
		params.put("dt", "dt");
		params.put("sw", "sw");
		try {
			List winloseList = losePromoService.getListBySql(sqlStr, params);
			for (Object object : winloseList) {
				Object[] arrObj = (Object[]) object;
				winloseMap.put(arrObj[0].toString(), Double.parseDouble(arrObj[1].toString()));
			}
		} catch (Exception e) {
			this.println("查询GPI、TTG输赢值异常");
			e.printStackTrace();
			return;
		}
		
		//查询PT slot输赢值
		sqlStr = "select PLAYERNAME, WINS_TIGER+PROGRESSIVE_WINS from pt_data_new where STARTTIME >= :start and ENDTIME <= :end";
		Date startTime, endTime;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		startTime = calendar.getTime();
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		endTime = calendar.getTime();
		
		params.put("start", startTime);
		params.put("end", endTime);
		
		try {
			List winloseList = losePromoService.getListBySql(sqlStr, params);
			for (Object object : winloseList) {
				Object[] arrObj = (Object[]) object;
				String userName = arrObj[0].toString().substring(1).toLowerCase();
				if(!winloseMap.containsKey(userName)){
					winloseMap.put(userName, Double.parseDouble(arrObj[1].toString()));
				}else{
					Double oldWinLose = winloseMap.get(userName);
					winloseMap.put(userName, Arith.add(oldWinLose, Double.parseDouble(arrObj[1].toString())));
				}
			}
		} catch (Exception e) {
			this.println("查询PT老虎机输赢值异常");
			e.printStackTrace();
			return;
		}
		
		for (String key : winloseMap.keySet()) {
			if(winloseMap.get(key) >= 50){
				try {
					losePromoService.distributeLosePromo(key, winloseMap.get(key), startTime, endTime);
				} catch (Exception e) {
					continue;
				}
			}
		}
		this.println("负盈利反赠执行完毕");
	}

	/**
	 *  查询负盈利反赠记录
	 * @return
	 */
	public String queryLosePromoRecords(){
		Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			String countS = "select count(1) ";
			String selectS = "select a.username, a.amount, a.deduct, a.promo, a.status, a.platform, a.creattime, a.gettime, a.remark, b.intro ";
			String midS = "FROM losepromo a LEFT JOIN users b on a.username = b.loginname ";
			String whereS = "where 1=1 ";
			if (StringUtils.isNotEmpty(status))
				whereS += "and a.status = '"+status+"' ";  //订单状态
			if (StringUtils.isNotEmpty(loginname))
				whereS += "and a.username = '"+loginname+"' ";
			if (StringUtils.isNotEmpty(intro))
				whereS += "and b.intro = '"+intro+"' ";
			
			if (start != null)
				whereS += "and a.creattime>='"+DateUtil.formatDateForStandard(start)+"' ";
			if (end != null)
				whereS += "and a.creattime<='"+DateUtil.formatDateForStandard(end)+"' ";
			
			if (StringUtils.isNotEmpty(by)) {
				whereS += "ORDER BY "+by+("desc".equals(order)?" DESC ":" ASC ");
			}
			Query query = session.createSQLQuery(countS+midS+whereS);
			Integer total = Integer.parseInt(query.list().get(0).toString());
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			
			query = session.createSQLQuery(selectS+midS+whereS);
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(total);
			int pages = PagenationUtil.computeTotalPages(total, size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}
	
	/**
	 * 取消负盈利反赠PT转出流水限制
	 */
	public void removeLosePromoTransferLimit(){
		DetachedCriteria dc = DetachedCriteria.forClass(LosePromo.class);
		dc = dc.add(Restrictions.eq("username", loginname));
		dc = dc.add(Restrictions.eq("status", "1"));  //已领取
		dc = dc.add(Restrictions.eq("platform", platform));
		List<LosePromo> promoList = proposalService.findByCriteria(dc);
		for (LosePromo item : promoList) {
			item.setStatus("2");
			transferService.update(item);
		}
	}
	
	/**
	 * @description：活动礼品寄送查询
	 * @param： 
	 * @author：jasonleung
	 */
	public String queryGiftList() {
		
		log.info("进入OfficeAction类的queryGiftList方法，参数type的值为：" + type);
		log.info("进入OfficeAction类的queryGiftList方法，参数title的值为：" + title);
		log.info("进入OfficeAction类的queryGiftList方法，参数activityBeginTime的值为：" + activityBeginTime);
		log.info("进入OfficeAction类的queryGiftList方法，参数activityEndTime的值为：" + activityEndTime);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Gift.class);
		
		if (StringUtils.isNotEmpty(type)) {
			
			dc = dc.add(Restrictions.eq("type", type));
		}
		
		if (StringUtils.isNotEmpty(title)) {
			
			dc = dc.add(Restrictions.like("title", title, MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(activityBeginTime)) {
			
			dc = dc.add(Restrictions.ge("startTime", DateUtil.parseDateForStandard(activityBeginTime)));
		}
		
		if (StringUtils.isNotEmpty(activityEndTime)) {
			
			dc = dc.add(Restrictions.le("endTime", DateUtil.parseDateForStandard(activityEndTime)));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
		
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	/**
	 * @description：新增礼品活动
	 * @param： 
	 * @author：jasonleung
	 */
	public void addGift() {
		
		log.info("进入OfficeAction类的addGift方法。");
		
		Date $startTimeDeposit = null;
		Date $endTimeDeposit = null;
		Date $startTimeBet = null;
		Date $endTimeBet = null;
		
		if (StringUtils.isNotEmpty(startTimeDeposit) && StringUtils.isNotEmpty(endTimeDeposit)) {
		
			$startTimeDeposit = DateUtil.parseDateForStandard(startTimeDeposit.replace("+", " "));
			$endTimeDeposit = DateUtil.parseDateForStandard(endTimeDeposit.replace("+", " "));
			// 如果页面选择的是存款，则默认给投注额赋值为0，如果不赋值，则在属性赋值转换的时候会报错，因为betAmount的属性类型为对象，而从页面传递过来的是空字符串，所以需要特殊处理，把值再设置为空
			betAmount = null;
		}
		
		if (StringUtils.isNotEmpty(startTimeBet) && StringUtils.isNotEmpty(endTimeBet)) {
			
			$startTimeBet = DateUtil.parseDateForStandard(startTimeBet.replace("+", " "));
			$endTimeBet = DateUtil.parseDateForStandard(endTimeBet.replace("+", " "));
			// 如果页面选择的是投注，则默认给存款额赋值为0，如果不赋值，则在属性赋值转换的时候会报错，因为depositAmount的属性类型为对象，而从页面传递过来的是空字符串，所以需要特殊处理，把值再设置为空
			depositAmount = null;
		}
		
		try {
			
			Gift gift = new Gift(type, title, depositAmount, $startTimeDeposit, $endTimeDeposit, betAmount, $startTimeBet, $endTimeBet, startTime, endTime, vip, new Date(), remark, giftQuantity);
			
			proposalService.save(gift);
			
			GsonUtil.GsonObject("添加成功");
		} catch (Exception e) {
			
			GsonUtil.GsonObject("添加失败，失败原因：" + e.getMessage());
		}
	}
	
	/**
	 * @description：查询礼品活动订单信息
	 * @param： 
	 * @author：jasonleung
	 */
	public String giftOrderList() {
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		//boss权限或者用户名为组长才能查询数据
		if (!"yan751028".equalsIgnoreCase(operator.getUsername()) &&!operator.getAuthority().equals("boss"))
			return INPUT;
		
		DetachedCriteria dc = DetachedCriteria.forClass(GiftOrder.class);
		
		dc = dc.add(Restrictions.eq("giftID", id));
		
		if (StringUtils.isNotEmpty(loginname)) {
			
			dc = dc.add(Restrictions.like("loginname", loginname, MatchMode.ANYWHERE));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		} else {
			
			o = Order.desc("applyDate");
//			dc = dc.addOrder(o);
		}
		
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	/**
	 * @description：修改礼品活动订单信息
	 * @param： 
	 * @author：jasonleung
	 */
	public void modifyGiftApplyInfo() {
		
		Operator op = (Operator) this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		
		if (null == op) {
			
			GsonUtil.GsonObject("会话已失效，请重新登录！");
			return;
		}
		
		GiftOrder order = (GiftOrder) operatorService.get(GiftOrder.class, id);
		
		order.setRealname(addressee);
		order.setAddress(address);
		order.setCellphoneNo(cellphoneNo);
		
		operatorService.update(order);
		
		log.info(op.getUsername() + "修改礼品收货信息：" + order.getLoginname());
		
		GsonUtil.GsonObject("修改成功");
	}
	
	/**
	 * @description：禁用/启用礼品活动
	 * @param： 
	 * @author：jasonleung
	 */
	public void optGift() {
		
		Gift gift = (Gift) operatorService.get(Gift.class, id);
		
		gift.setStatus(status);
		
		operatorService.update(gift);
		
		GsonUtil.GsonObject("操作成功");
	}
	
	/**
	 * 自动升级
	 */
	public void autoUpgrade(){
		try {
			AutoUpdateLevelUtil.autoUpgrade(autoUpgradeService);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		this.println("升降级执行完毕");
	}
	
	/**
	 * 查询升级日志
	 * @return
	 */
	public String queryUpgradeLog(){
		DetachedCriteria dc = DetachedCriteria.forClass(UpgradeLog.class);
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("username", loginname));
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", status));  //状态
		if (StringUtils.isNotEmpty(upgradeYyyyMm))
			dc = dc.add(Restrictions.eq("optmonth", upgradeYyyyMm));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	} 
	
	/**
	 * 查询pt日结佣金
	 * @return
	 */
	public String getPtCommissions(){
		Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			StringBuffer sbf = new StringBuffer();
			sbf.append("select p.agent , p.createdate , p.platform , p.amount , p.percent , p.subCount ,p.activeuser ,p.flag ,p.createTime ,p.excuteTime , p.remark , p.betall , p.profitall , p.platformfee , p.couponfee , p.ximafee , p.historyfee ,u.slotaccount,IF(p.progressive_bets IS NULL, 0.00,p.progressive_bets) progressive_bets, p.depositfee from ptcommissions p LEFT JOIN userstatus u on p.agent=u.loginname ");
			if(StringUtils.isNotBlank(vip)){
				String sql = "select v1.agent agent from agentvip v1 where v1.level = " + vip + "  and v1.createdate = (select v2.createdate from agentvip v2 order by v2.createtime desc limit 1 )" ;
				sbf.append(" inner join ("+sql+") v on p.agent = v.agent");
			}
			if(StringUtils.isNotBlank(intro)){
				String sql = "select u1.loginname from users u1 where u1.partner = '" + intro + "'  and u1.role='AGENT'";
				sbf.append(" inner join ("+sql+") uu on p.agent = uu.loginname");
			}
			if(null != startDate && null != endDate){
				String sql = "select u2.loginname from users u2 where u2.createtime >= '" + DateUtil.formatDateForStandard(startDate) + "' and u2.createtime<= '"+DateUtil.formatDateForStandard(endDate)+"'  and u2.role='AGENT' " ;
				sbf.append(" inner join ("+sql+") u3 on p.agent = u3.loginname");
			}
			if(null != warnflag){
				String sql = "";
				if(warnflag == 2){
					sql = "select u3.loginname from users u3 where u3.warnflag=2  and u3.role='AGENT'";
				}else if(warnflag == 0){
					sql = "select u3.loginname from users u3 where u3.warnflag!=2  and u3.role='AGENT'";
				}
				sbf.append(" inner join ("+sql+") u5 on p.agent = u5.loginname");
			}
			if(null != agentType && -1!=agentType){
				String sql = "select loginname from internal_agency where type = " + agentType +"  ";
				sbf.append(" inner join ("+sql+") i1 on p.agent = i1.loginname");
			}
			sbf.append(" where 1=1 ");
			if(null != agentType && -1==agentType){
				sbf.append(" and p.agent not in(select loginname from internal_agency ) ");
			}
			if (StringUtils.isNotEmpty(loginname)) {
				sbf.append(" and p.agent = '"+loginname+"'");
			}
			if (StringUtils.isNotEmpty(flag)) {
				sbf.append(" and p.flag="+Integer.parseInt(flag));
			}
			if (StringUtils.isNotEmpty(platform)) {
				sbf.append(" and p.platform='"+platform+"'");
			}
			if (startTime != null){
				sbf.append(" and p.createTime>='"+DateUtil.formatDateForStandard(startTime)+"'");
			}
			if (endTime != null){
				sbf.append(" and p.createTime<='"+DateUtil.formatDateForStandard(endTime)+"'");
			}
			if (StringUtils.isNotEmpty(by) && !StringUtils.equalsIgnoreCase(by, "slotaccount")) {
				sbf.append(" order by p."+by+" "+order);
			}else if(StringUtils.isNotEmpty(by) && StringUtils.equalsIgnoreCase(by, "slotaccount")){
				sbf.append(" order by u.slotaccount "+order);
			}else{
				sbf.append(" order by p.createTime desc ");
			}
			
			Calendar cal=Calendar.getInstance();
			int day_of_month=cal.get(Calendar.DAY_OF_MONTH);
			if(day_of_month>=5 && day_of_month < 10){
				isShow = false ;
			}else{
				isShow = true ;
			}
			Query query = session.createSQLQuery(sbf.toString());
			List list = query.list();
			Double profitall = 0.0 ;
			Double platformfee = 0.0 ;
			Double amount = 0.0 ;
			Double couponfee = 0.0 ;
			Double ximafee = 0.0 ;
			Double progressive_bets = 0.0; 
			Double depositfee = 0.0;
			for (Object object : list) {
				Object[] objarray = (Object[])object;
				profitall += (Double)objarray[12];
			    platformfee += (Double)objarray[13];
			    amount += (Double)objarray[3];
			    couponfee += (Double)objarray[14];
			    ximafee += (Double)objarray[15];
			    progressive_bets += (Double)objarray[18];
			    depositfee += (Double)objarray[19];
			}
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List<PtCommissionsVo> contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(list.size());
			int pages = PagenationUtil.computeTotalPages(list.size(), size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			page.setStatics1(profitall);
			page.setStatics2(platformfee);
			page.setStatics3(amount);
			page.setStatics4(couponfee);
			page.setStatics5(ximafee);
			page.setStatics6(progressive_bets);
			page.setStatics7(depositfee);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}
	
	public String queryAgentSlotAccount(){
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			Userstatus agentAccount = (Userstatus) operatorService.get(Userstatus.class, StringUtil.trim(loginname));
			if(agentAccount!=null && null != agentAccount.getSlotaccount()){
				GsonUtil.GsonObject(agentAccount.getSlotaccount());
			}else if (agentAccount!=null && null == agentAccount.getSlotaccount()){
				agentAccount.setSlotaccount(0.0);
				operatorService.update(agentAccount);
				GsonUtil.GsonObject("0.0");
			}else{
				Userstatus status = new Userstatus() ;
				status.setLoginname(loginname);
				status.setTouzhuflag(0);
				status.setCashinwrong(0);
				status.setSlotaccount(0.0);
				operatorService.save(status);
				GsonUtil.GsonObject("0.0");
			}
			return null;
		} catch (Exception e) {
			GsonUtil.GsonObject("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
		}
		return null;
	}
	
	public String getCommissionsDetail(){
		if(StringUtils.isNotBlank(platform) && StringUtils.isNotBlank(agent) && null != createdate){
			DetachedCriteria dc = DetachedCriteria.forClass(PtCommissionRecords.class);
			dc.add(Restrictions.eq("id.agent", agent)) ;
			dc.add(Restrictions.eq("id.createdate", createdate)) ;
			if (platform.equals("slotmachine")) {
				dc.add(Restrictions.in("id.platform", new Object[] { "newpt", "ttg", "gpi", "qt","dt","mg","png","mwg" }));
			} else if (platform.equals("liveall")) {
				dc.add(Restrictions.in("id.platform", new Object[] { "ea", "bbin", "keno", "keno2", "sba", "sixlottery", "ebet", "jc" }));
			}
			List<PtCommissionRecords> list = operatorService.findByCriteria(dc) ;
			GsonUtil.GsonObject(list);
		}
		return null ;
	}
	
	public String getCommissionsDetail1(){
		if(StringUtils.isNotBlank(platform) && StringUtils.isNotBlank(agent) && null != createdate){
			DetachedCriteria dc = DetachedCriteria.forClass(PtCommissionRecords.class);
			dc.add(Restrictions.eq("id.agent", agent)) ;
			dc.add(Restrictions.eq("id.createdate", createdate)) ;
			if(platform.equals("slotmachine")){
				dc.add(Restrictions.in("id.platform", new Object[]{"newpt","ttg","gpi","qt","mg","dt","nt","aginfish","aginslot","png","mwg","ptsky","swfish"}));
			} else if(platform.equals("liveall")){
				dc.add(Restrictions.in("id.platform", new Object[]{"ea","ag","agin","bbin","keno","keno2","sb","sixlottery","ebet","jc","n2live","ebetapp"}));
			} else if (platform.equals("sports")) {
				dc.add(Restrictions.in("id.platform", new Object[] { "sba"}));
			} else if (platform.equals("lottery")) {
				dc.add(Restrictions.in("id.platform", new Object[] { "og" }));
			}
			List<PtCommissionRecords> list = operatorService.findByCriteria(dc) ;
			getRequest().setAttribute("list", list);
		}
		return INPUT ;
	}
	
	public String getAgentVip(){
		DetachedCriteria dc = DetachedCriteria.forClass(AgentVip.class);
		
		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("id.agent", loginname));
		}
		if (startTime != null)
			dc = dc.add(Restrictions.ge("createtime", startTime));
		if (endTime != null)
			dc = dc.add(Restrictions.le("createtime", endTime));
		
		if(flag.equals("0")){
			dc = dc.add(Restrictions.ge("registertime", DateUtil.parseDateForStandard("2015-1-1 00:00:00")));
		}else if(flag.equals("1")){
			dc = dc.add(Restrictions.le("registertime", DateUtil.parseDateForStandard("2015-1-1 00:00:00")));
		}
		
		if(null != level){
			dc.add(Restrictions.eq("level", level));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		Calendar cal=Calendar.getInstance();
	    int day_of_month=cal.get(Calendar.DAY_OF_MONTH);
	    if(day_of_month>=1 && day_of_month<=6){
	    	isShow = true ;
	    }else{
	    	isShow = false ;
	    }
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String executeAgentVip(){
		AutoAgentVipUtil util = new AutoAgentVipUtil(userDao, customerService);
		util.updateAgentVipLevel();
		GsonUtil.GsonObject("执行成功");
		return null ;
	}
	
	/**
	 * 处理危险级别玩家的升级
	 */
	public void optUpgrade(){
		UpgradeLog upgradeLog = (UpgradeLog) operatorService.get(UpgradeLog.class, fldid);
		autoUpgradeService.optUpgrade(upgradeLog, status);
	}
	
	
	public List checkKeyWord(String str){
		String sql = "select * from keyword where instr('"+str+"' ,value )>0" ;
		return autoUpgradeService.queryList(sql, null);
		
	}

	public ILosePromoService getLosePromoService() {
		return losePromoService;
	}

	public void setLosePromoService(ILosePromoService losePromoService) {
		this.losePromoService = losePromoService;
	}

	public Boolean getUnfinshedYouHui() {
		return unfinshedYouHui;
	}

	public void setUnfinshedYouHui(Boolean unfinshedYouHui) {
		this.unfinshedYouHui = unfinshedYouHui;
	}
	public IAutoUpgradeService getAutoUpgradeService() {
		return autoUpgradeService;
	}

	public void setAutoUpgradeService(IAutoUpgradeService autoUpgradeService) {
		this.autoUpgradeService = autoUpgradeService;
	}

	public Long getFldid() {
		return fldid;
	}

	public void setFldid(Long fldid) {
		this.fldid = fldid;
	}

	public String getRemarkRemark() {
		return remarkRemark;
	}

	public void setRemarkRemark(String remarkRemark) {
		this.remarkRemark = remarkRemark;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}
	
	/**
	 * 查询TTG的数据
	 * @return
	 */
	public String queryTTGCredit() {
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getLoginname()!=null && !customer.getLoginname().equals("")){
				String afterPtRemoteAmount = PtUtil1.getPlayerAccount(customer.getLoginname());
				if (afterPtRemoteAmount == null) {
					out.println("系统繁忙，请稍后尝试！");
				} else {
					out.println(afterPtRemoteAmount);
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	/**
	 * 查询QT的数据
	 * @return
	 */
	public String queryQTCredit() {
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getLoginname()!=null && !customer.getLoginname().equals("")){
				String afterPtRemoteAmount = QtUtil.getBalance(customer.getLoginname());
				if (QtUtil.RESULT_FAIL.equals(afterPtRemoteAmount)) {
					out.println("系统繁忙，请稍后尝试！");
				} else {
					out.println(afterPtRemoteAmount);
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	/**
	 * 查询NT的余额
	 * @return
	 */
	public String queryNTCredit() {
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			if(customer.getLoginname()!=null && !customer.getLoginname().equals("")){
				JSONObject ntba = JSONObject.fromObject(NTUtils.getNTMoney(customer.getLoginname()));
				if (!ntba.getBoolean("result")) {
					out.println("系统繁忙，请稍后尝试！错误消息:"+ntba.getString("error"));
				} else {
					out.println(ntba.getDouble("balance"));
				}
			}else{
				out.println("系统繁忙，请稍后尝试！");
			}
			return null;
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
    public String queryBetrank2(){
        Page page = new Page();

        //查询总条数
        //StringBuffer sqlCount =new StringBuffer("select count(*) from (select 1 from platform_data where starttime >='"+DateUtil.formatDateForStandard(startTime)+"' and starttime <='"+DateUtil.formatDateForStandard(endTime)+"' and (platform="+platform+" ");
        StringBuffer sqlCount =new StringBuffer("select count(*) from (select 1 from agprofit where createTime >='"+DateUtil.formatDateForStandard(startTime)+"' and createTime <'"+DateUtil.formatDateForStandard(endTime)+"'  ");
        if(!StringUtil.isEmpty(loginname)){
            sqlCount.append("  and loginname='"+loginname+"' ");
        }
        sqlCount.append("  and platform NOT IN ( '761','agin','aginfish','mwg','n2live','sba')  GROUP BY loginname  )a");

        Session session = userDao.getSessionFactory().openSession();
        try {
            SQLQuery query = session.createSQLQuery(sqlCount.toString());
            List counts = query.list();
            String count =counts.get(0).toString();
            //分页查询
            //StringBuffer sqlPage=new StringBuffer("select sum(p.bet) as sumbet,p.loginname,'"+type+"',u.lastarea from platform_data p,users u where p.starttime >='"+DateUtil.formatDateForStandard(startTime)+"' and p.starttime <='"+DateUtil.formatDateForStandard(endTime)+"' and p.loginname=u.loginname and (p.platform="+platform+" ");
            StringBuffer sqlPage=new StringBuffer("select sum(p.bettotal) as sumbet,p.loginname,platform,u.lastarea from agprofit p,users u where p.createTime >='"+DateUtil.formatDateForStandard(startTime)+"' and p.createTime <'"+DateUtil.formatDateForStandard(endTime)+"' and p.loginname=u.loginname");

            if(!StringUtil.isEmpty(loginname)){
                sqlPage.append("  and p.loginname='"+loginname+"' ");
            }
            sqlPage.append("  and  p.platform NOT IN ( '761','agin','aginfish','mwg','n2live','sba') GROUP BY p.loginname order by  sumbet desc");
            SQLQuery query1 = session.createSQLQuery(sqlPage.toString());
            query1.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
            query1.setMaxResults(size.intValue());
            List list = query1.list();
            List<BetRankModel> listbetrank=new ArrayList<BetRankModel>();
            int z = (pageIndex.intValue() - 1) * size.intValue()+1;
            for(Object obj : list){
                BetRankModel br = new BetRankModel();
                Object[]objarray = (Object[])obj;
                br.setAmount(objarray[0]+"");
                br.setLoginname((String)objarray[1]);
                br.setNo(z);
                br.setPlatform((String)objarray[2]);
                br.setAddress((String)objarray[3]);
                listbetrank.add(br);
                z++;
            }

            page.setPageNumber(pageIndex);
            page.setSize(size);
            page.setTotalRecords(Integer.parseInt(count));
            int pages = PagenationUtil.computeTotalPages(Integer.parseInt(count), size).intValue();
            page.setTotalPages(Integer.valueOf(pages));
            if (pageIndex.intValue() > pages) {
                pageIndex = Page.PAGE_BEGIN_INDEX;
            }
            page.setPageNumber(pageIndex);
            page.setPageContents(listbetrank);
            page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
            getRequest().setAttribute("page", page);
        } catch (Exception e) {
            log.error("server error", e);
        } finally {
            session.close();
        }
        return INPUT;
    }
	
	//queryBetrank
	/**
	 *  
	 * @return
	 */
	public String queryBetrank(){
		String platform =" ";
		Page page = new Page();
		if(type.equals("updatedate")){
		DetachedCriteria dc = DetachedCriteria.forClass(BetRankModel.class);
		List<BetRankModel> listBetRankModel = operatorService.findByCriteria(dc) ;
		page.setPageNumber(1);
		page.setSize(1000);
		page.setTotalRecords(1000);
		page.setPageContents(listBetRankModel);
		getRequest().setAttribute("page", page);
		return INPUT;
		}
		
		
		/*if(type.equals("ttg")){
			platform="'ttg')";
		}else if(type.equals("pttiger")){
			platform="'pttiger')";
		}else if(type.equals("gpi")){
			platform="'gpi' or platform = 'rslot' or platform = 'png' or platform = 'bs' or platform = 'ctxm')";
		}else if(type.equals("all")){
			platform="'gpi' or platform = 'rslot' or platform = 'png' or platform = 'bs' or platform = 'ctxm' or platform = 'ttg' or platform = 'pttiger')";
		}*/
		//查询总条数
		//StringBuffer sqlCount =new StringBuffer("select count(*) from (select 1 from platform_data where starttime >='"+DateUtil.formatDateForStandard(startTime)+"' and starttime <='"+DateUtil.formatDateForStandard(endTime)+"' and (platform="+platform+" ");
		StringBuffer sqlCount =new StringBuffer("select count(*) from (select 1 from platform_data where starttime >='"+DateUtil.formatDateForStandard(startTime)+"' and starttime <'"+DateUtil.formatDateForStandard(endTime)+"'  ");
		if(!StringUtil.isEmpty(loginname)){
			sqlCount.append("  and loginname='"+loginname+"'");
		}
		sqlCount.append(" GROUP BY loginname  )a");
		
		Session session = userDao.getSessionFactory().openSession();
		try {
			SQLQuery query = session.createSQLQuery(sqlCount.toString());
			List counts = query.list();
			String count =counts.get(0).toString();
			//分页查询
			//StringBuffer sqlPage=new StringBuffer("select sum(p.bet) as sumbet,p.loginname,'"+type+"',u.lastarea from platform_data p,users u where p.starttime >='"+DateUtil.formatDateForStandard(startTime)+"' and p.starttime <='"+DateUtil.formatDateForStandard(endTime)+"' and p.loginname=u.loginname and (p.platform="+platform+" ");
			StringBuffer sqlPage=new StringBuffer("select sum(p.bet) as sumbet,p.loginname,platform,u.lastarea from platform_data p,users u where p.starttime >='"+DateUtil.formatDateForStandard(startTime)+"' and p.starttime <'"+DateUtil.formatDateForStandard(endTime)+"' and p.loginname=u.loginname");

			if(!StringUtil.isEmpty(loginname)){
				sqlPage.append("  and p.loginname='"+loginname+"'");
			}
			sqlPage.append(" GROUP BY p.loginname order by  sumbet desc");
			SQLQuery query1 = session.createSQLQuery(sqlPage.toString());
			query1.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query1.setMaxResults(size.intValue());
			List list = query1.list();
			List<BetRankModel> listbetrank=new ArrayList<BetRankModel>();
			int z = (pageIndex.intValue() - 1) * size.intValue()+1;
			for(Object obj : list){
				BetRankModel br = new BetRankModel();
				Object[]objarray = (Object[])obj;
				br.setAmount(objarray[0]+"");
				br.setLoginname((String)objarray[1]);
				br.setNo(z);
				br.setPlatform((String)objarray[2]);
				br.setAddress((String)objarray[3]);
				listbetrank.add(br);
				z++;
			}

            page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(Integer.parseInt(count));
			int pages = PagenationUtil.computeTotalPages(Integer.parseInt(count), size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages) {
				pageIndex = Page.PAGE_BEGIN_INDEX;
			}
			page.setPageNumber(pageIndex);
			page.setPageContents(listbetrank);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}

	/**
	 * 查询平博钱包余额
	 *
	 * @return
	 * */
	public String queryPBCredit(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Double balance = PBUtil.getBalance(loginname);
			out.println(balance);
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			log.error(e);
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	/* 8元比赛盈利排名 pm-start */
	public String slotsGiftMatch(){
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("errorMsg", "");
		Integer week_ = Integer.valueOf(request.getParameter("week_").equals("")?"0":request.getParameter("week_").toString());
		//String slots = request.getParameter("slots");
		//SessionCacheUtil.saveCache(request, new Object[]{week_}, "match_cache");
		Page page = null;
		//判断8元比赛是否关闭,关闭时只允许查询本周之前数据
		try{
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.like("id", "%体验金周赛"));
			List<Const> list = slaveService.getHibernateTemplate().findByCriteria(dc);
			boolean match_c = true;
			for (Const cl : list) {
				if (cl.getValue().equals("1")){
					match_c = false;
					break;
				}
			}
			if (week_ == 0 && match_c){
				request.setAttribute("errorMsg", "请注意! 本周3个产品体验金竞赛已经全部关闭.");
				return SUCCESS;
			} else {
				//将当前周,当前周第一天,平台传入proposalService处理,直接返回排名list
				//获取当前周的第一天与最后天,根据week_区分本周上周下周
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
				startTime = startTime+" 00:00:00";
				endTime = endTime+" 23:59:59";
				page = proposalService.queryWeeklySlotsMatch(request, startTime, endTime,
						pageIndex, size);
				request.setAttribute("week_", week_);
				//request.setAttribute("slots", slots);
				request.setAttribute("startTime", startTime);
				request.setAttribute("endTime", endTime);
				request.setAttribute("page", page);
			}
		} catch (Exception e){
			log.error("slotsGiftMatch exception:", e);
			request.setAttribute("errorMsg", "发生未知错误");
		}
		
		return SUCCESS;
	}
	
	/**
	 * 更新所有老虎机周比赛数据
	 * @return
	 */
	private SlotsMatchWeeklyJob smWeeklyJob;
	public void setSmWeeklyJob(SlotsMatchWeeklyJob smWeeklyJob) {
		this.smWeeklyJob = smWeeklyJob;
	}
	public String updateSlotsMatchWeekly(){
		//TODO 将JOB中的数据处理移到service中
		try {
			String u_d = getRequest().getParameter("update_date");
			smWeeklyJob.generateWeeklyData(u_d);
			GsonUtil.GsonObject("更新比赛数据完成!");
		} catch (Exception e) {
			log.error("setSmWeeklyJob error:",e);
			GsonUtil.GsonObject("更新比赛数据失败,系统错误!");
		}
		return null;
	}
	
	/**
	 * 手动修改排名
	 * @return
	 */
	private SlotsMatchWeekly smWeekly;
	public SlotsMatchWeekly getSmWeekly() {
		return smWeekly;
	}
	public void setSmWeekly(SlotsMatchWeekly smWeekly) {
		this.smWeekly = smWeekly;
	}
	public String modifiedSMRanking(){
		try{
			String mtype = getRequest().getParameter("mtype");
			if (mtype.equals("new")){
				return INPUT;
			} else if (mtype.equals("insert")){
				smWeekly.setStartTime(MatchDateUtil.parseDatetime(
						MatchDateUtil.formatDate(smWeekly.getStartTime())+" 00:00:00"));
				smWeekly.setEndTime(MatchDateUtil.parseDatetime(
						MatchDateUtil.formatDate(smWeekly.getEndTime())+" 23:59:59"));
				Long smw = (Long) this.proposalService.save(smWeekly);
				if (smw != null){
					getRequest().setAttribute("errorMsg", "手动新增周比赛数据成功!");
					//GsonUtil.GsonObject("手动新增周比赛数据成功!");
					return SUCCESS;
				}
			} else if (mtype.equals("update")){//暂无更新需求
				DetachedCriteria dc = DetachedCriteria.forClass(SlotsMatchWeekly.class);
				dc.add(Restrictions.eq("id", smWeekly.getId()));
				SlotsMatchWeekly smw = (SlotsMatchWeekly) this.proposalService.findByCriteria(dc).get(0);
				//smw.set
			} else {
				GsonUtil.GsonObject("操作类型错误,请重试一次吧.");
			}
			GsonUtil.GsonObject("操作失败,新增周比赛数据失败! 请联系技术!");
			return null;
		} catch (Exception e){
			log.error("modifiedSMRanking error:", e);
			GsonUtil.GsonObject("操作异常, 请确认一下需要新增的数据! 异常消息:"+e.getMessage());
			return null;
		}
	}
	
	/* pm-end */
	
	private String note;
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	public String updateBetInfo(){
		PrintWriter out = null;
		try {
		this.getResponse().setCharacterEncoding("UTF-8");
		out = this.getResponse().getWriter();
		BetRankModel betrank = (BetRankModel) operatorService.get(BetRankModel.class, id);
		betrank.setAmount(amount);
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		betrank.setRemark(betrank.getRemark()+";"+op.getUsername()+note);
		operatorService.update(betrank);
		out.println("更新成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println("系统异常，请重试");
		}
		return null;
	}

	public String getUpgradeYyyyMm() {
		return upgradeYyyyMm;
	}

	public void setUpgradeYyyyMm(String upgradeYyyyMm) {
		this.upgradeYyyyMm = upgradeYyyyMm;
	}

	public String getDistributeDate() {
		return distributeDate;
	}

	public void setDistributeDate(String distributeDate) {
		this.distributeDate = distributeDate;
	}

	public SlaveService getSlaveService() {
		return slaveService;
	}

	public void setSlaveService(SlaveService slaveService) {
		this.slaveService = slaveService;
	}

	public String getActivityBeginTime() {
		return activityBeginTime;
	}

	public void setActivityBeginTime(String activityBeginTime) {
		this.activityBeginTime = activityBeginTime;
	}

	public String getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getStartTimeDeposit() {
		return startTimeDeposit;
	}

	public void setStartTimeDeposit(String startTimeDeposit) {
		this.startTimeDeposit = startTimeDeposit;
	}

	public String getEndTimeDeposit() {
		return endTimeDeposit;
	}

	public void setEndTimeDeposit(String endTimeDeposit) {
		this.endTimeDeposit = endTimeDeposit;
	}

	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	public String getStartTimeBet() {
		return startTimeBet;
	}

	public void setStartTimeBet(String startTimeBet) {
		this.startTimeBet = startTimeBet;
	}

	public String getEndTimeBet() {
		return endTimeBet;
	}

	public void setEndTimeBet(String endTimeBet) {
		this.endTimeBet = endTimeBet;
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

	public AgentCustomer getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(AgentCustomer agentPhone) {
		this.agentPhone = agentPhone;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	
	/**
	 * 乐富微信支付
	 * @return
	 */
	public String repairPayOrderLfwx(){
		try {
			java.text.SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				sf.parse(payTime);
			} catch (Exception e) {
				// TODO: handle exception
				this.setErrormsg("您输入的日期格式有误，请重新输入");
				return INPUT;
			}
			if (loginname == null || "".equals(loginname)) {
				this.setErrormsg("会员帐号不允许为空！");
				return INPUT;
			}
			if (billno == null || "".equals(billno)) {
				this.setErrormsg("支付单号不允许为空！");
				return INPUT;
			}
			if (payAmount == null || "".equals(payAmount)) {
				this.setErrormsg("金额不允许为空！");
				return INPUT;
			}
			if(Double.parseDouble(payAmount)>5001){
				this.setErrormsg("金额不能超过5001元人民币！");
				return INPUT;
			}
				String msg = netpayService.repairNetpayOrderHf(getOperatorLoginname(), billno.trim(), payAmount, getIp(), payBillno, payTime, payPlatform, loginname.trim(), remark);
				if (msg == null)
					setErrormsg("补单成功");
				else
					setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			log.error("在线支付补单失败", e);
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}
	
	
	private String topusername;
	
	private String downusername;
	

	public String getTopusername() {
		return topusername;
	}

	public void setTopusername(String topusername) {
		this.topusername = topusername;
	}

	public String getDownusername() {
		return downusername;
	}

	public void setDownusername(String downusername) {
		this.downusername = downusername;
	}

	/**
	 * 查询好友推荐
	 * @return
	 */
	public String getFriendintroduce(){
		DetachedCriteria dc=null;
		if(type.equals("0")){//推荐记录
			dc= DetachedCriteria.forClass(Friendintroduce.class);
		}else if(type.equals("1")){//获取奖金记录
			dc= DetachedCriteria.forClass(Friendbonusrecord.class);
			dc.add(Restrictions.eq("type", "1"));
		}else if(type.equals("2")){//消费奖金记录
			dc= DetachedCriteria.forClass(Friendbonusrecord.class);
			dc.add(Restrictions.eq("type", "2"));
		}else if(type.equals("3")){//奖金池
			dc= DetachedCriteria.forClass(Friendbonus.class);
		}else{//查询全部
			dc= DetachedCriteria.forClass(Friendbonusrecord.class);
		}
		if (StringUtils.isNotEmpty(topusername)) {
			dc.add(Restrictions.eq("toplineuser", topusername));
		}
		if (StringUtils.isNotEmpty(downusername)) {
			if(type.equals("3")){
				setErrormsg("查询奖金余额，不用填写上线玩家账号");
			}
			dc.add(Restrictions.eq("downlineuser", downusername));
		}
		if (startTime != null){
			dc = dc.add(Restrictions.ge("createtime", startTime));
		}
		if (endTime != null){
			dc = dc.add(Restrictions.le("createtime", endTime));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		 Page  page = PageQuery.queryForPagenation_Friendintroduce(operatorService.getHibernateTemplate(), dc, pageIndex, size,type, o);
		 
  		getRequest().setAttribute("page", page);
		return INPUT;
	}	
	
	/**
	 *  查询批量导入优惠
	 * @return
	 */
	public String queryPrivileges(){
		DetachedCriteria dc = DetachedCriteria.forClass(Privilege.class);
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginName", loginname));
		
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));
		
		if (start != null){
			dc = dc.add(Restrictions.ge("createTime", start));
			Calendar cld = Calendar.getInstance();
			cld.setTime(start);
			cld.add(Calendar.DATE, 1);
			dc = dc.add(Restrictions.lt("createTime", cld.getTime()));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 执行当天批量导入的优惠
	 */
	public void exePrivilegesOfToday(){
		String result = SynchronizedUtil.getInstance().exePrivilegesOfToday(proposalService);
		println(result);
	}
	
	public void updatePtInfo(){
		operatorService.updatePtInfo("pt_userse68");
	}
	
	
	public String getInternalAgencies(){
		Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			StringBuffer sbf = new StringBuffer();
			sbf.append("select i.loginname  ,i.type , u.createtime , u.flag,i.belongto from internal_agency i LEFT JOIN users u on i.loginname = u.loginname ");
			sbf.append(" where 1=1 ");
			if (StringUtils.isNotEmpty(loginname)) {
				sbf.append(" and i.loginname = '"+loginname+"'");
			}
			if (null != agentType) {
				sbf.append(" and i.type = "+agentType);
			}
			if (startTime != null){
				sbf.append(" and i.createtime>='"+DateUtil.formatDateForStandard(startTime)+"'");
			}
			if (endTime != null){
				sbf.append(" and i.createtime<='"+DateUtil.formatDateForStandard(endTime)+"'");
			}
			if (StringUtils.isNotEmpty(by)) {
				sbf.append(" order by i."+by+" "+order);
			}else{
				sbf.append(" order by i.createtime desc ");
			}
			
			Query query = session.createSQLQuery(sbf.toString());
			List<PtCommissionsVo> list = query.list();
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(list.size());
			int pages = PagenationUtil.computeTotalPages(list.size(), size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			getRequest().setAttribute("page", page);
		}catch (Exception e) {
			log.error("server error", e);
		}finally {
			session.close();
		}
		return INPUT;
	}
	
	// 总报表信息
	public String queryAgentInfoAnalysis() {

		try {
				start = DateUtil.fmtStandard(stringStartTime);
				end = DateUtil.fmtStandard(stringEndTime);
			Map<String,List> map = operatorService.queryAgentAnalysis(agentType, start, end, loginname,level, warnflag, agent, intvalday, nintvalday, partner, startDate, endDate, order, by) ;
			getRequest().setAttribute("page", map.get("calist"));
			getRequest().setAttribute("pageSum", map.get("sumlist"));
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}
	
	/**
	 * 取消当日导入的待执行优惠
	 */
	public void cancelPrivilegesOfToday(){
		String operator = ((Operator)getHttpSession().getAttribute(Constants.SESSION_OPERATORID)).getUsername();
		SynchronizedUtil.getInstance().cancelPrivilegesOfToday(proposalService, operator);
		println("已取消当日导入的待执行优惠");
	}
	
	private String querySystemConfig(String typeNo, String itemNo, String flag) {

		String str = "";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if (!StringUtils.isEmpty(typeNo)) {
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if (!StringUtils.isEmpty(itemNo)) {
			dc = dc.add(Restrictions.eq("itemNo", itemNo));
		}
		if (!StringUtils.isEmpty(flag)) {
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list = operatorService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			str = list.get(0).getValue();
		}
		return str;
	}

	/**
	 * 查询PNG余额
	 *
	 * @return
	 * */
	public String queryPNGCredit(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Double balance = PNGUtil.getBalance(loginname);
			out.println(balance);
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}

	public String updateUserAgent(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			GsonUtil.GsonObject("请登录");
			return null; 
		}
		if(StringUtils.isEmpty(loginname) || StringUtils.isEmpty(agent)){
			GsonUtil.GsonObject("账号不能为空");
			return null;
		}
		operatorService.updateUserAgent(loginname, agent , op.getUsername());
		GsonUtil.GsonObject("修改成功");
		return null ;
	}
	
	/**
	 * 根据银行卡号查询绑定过的用户账号。
	 * @return
	 * */
	public String queryUserbankinfoByBankno(){
		try {
			Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if(null == op){
				setErrormsg("请先登录！");
				return INPUT;
			}
			if(StringUtils.isBlank(this.bankaccount)){
				setErrormsg("请输入银行卡号");
				return INPUT;
			}
			DetachedCriteria dc=DetachedCriteria.forClass(Userbankinfo.class);
			dc.add(Restrictions.eq("bankno", this.bankaccount));
			
			Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			}else{
				o = Order.desc("addtime");
			}
			Page  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
	  		getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("根据银行卡号查询绑定过的账号出错：",e);
			setErrormsg("操作出现异常:" + e.getMessage());
		}
		
		return INPUT;
	}
	
	/**
	 *  查询代理账号绑定的游戏账号
	 * @return
	 */
	public String queryAgentBindGameUsersRecords(){
		try {
			
			DetachedCriteria dc=DetachedCriteria.forClass(UsersAgentGame.class);
			
			if(StringUtils.isNotEmpty(this.agentUserName)) {
				dc.add(Restrictions.eq("loginnameAgent", this.agentUserName));
			}
			if(StringUtils.isNotEmpty(this.gameUserName)) {
				dc.add(Restrictions.eq("loginnameGame", this.gameUserName));
			}
			if(this.deleteFlag != null){
				if("0".equals(this.deleteFlag)){
					dc.add(Restrictions.eq("deleteFlag", Integer.parseInt(this.deleteFlag)));
				} else if("1".equals(this.deleteFlag)){
					dc.add(Restrictions.eq("deleteFlag", Integer.parseInt(this.deleteFlag)));
				}
			}
			
//			if (startTime != null){
//				dc = dc.add(Restrictions.ge("createtime", startTime));
//			}
//			if (endTime != null){
//				dc = dc.add(Restrictions.le("createtime", endTime));
//			}
			Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//				dc = dc.addOrder(o);
			}else{
				o = Order.desc("createtime");
//				dc = dc.addOrder(Order.desc("createtime"));
			}
			Page  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
	  		getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("查询代理账户绑定游戏账户出错：",e);
			setErrormsg("操作出现异常:" + e.getMessage());
		}
		
		return INPUT;
	}
	
	/**
	 *  代理账号与游戏账号解除绑定
	 * @return
	 */
	public void unbindAgentGameUsers(){
		try {
			Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if(null == op){
				GsonUtil.GsonObject("请登录");
				return; 
			}
			
			if(this.id == null) {
				GsonUtil.GsonObject("未接收到有效id,请刷新页面重新操作");
				return;
			}

			UsersAgentGame usersAgentGame = (UsersAgentGame) this.operatorService.get(UsersAgentGame.class, this.id);
			if(usersAgentGame == null){
				GsonUtil.GsonObject("未查找到数据,请刷新页面重新操作");
				return;
			}
			if(usersAgentGame.getDeleteFlag() == 1){
				GsonUtil.GsonObject("该条记录已经解除绑定，无需重复操作");
				return;
			}
			usersAgentGame.setDeleteFlag(1);
			usersAgentGame.setRemark("解除绑定时间：" + DateUtil.getNow() + ",操作人员：" + op.getUsername());
			this.operatorService.update(usersAgentGame);

		} catch (Exception e) {
			log.error("代理账户解除绑定游戏账户出错：",e);
			GsonUtil.GsonObject("代理账户解除绑定游戏账户出错：" + e.toString());
			return;
		}
		
		GsonUtil.GsonObject("操作成功");
	}
	
	/**
	 *  根据日期查询好友推荐奖金派发记录
	 * @return
	 */
	public String queryFriendRecordsByDate(){
		
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonusrecord.class);
		
		if(StringUtils.isNotBlank(this.distributeDate)){
			
			dc.add(Restrictions.eq("distributeDate", distributeDate));
		}
		if(StringUtils.isNotEmpty(this.topLineUser)){
			dc.add(Restrictions.eq("toplineuser", this.topLineUser));
		}
		if(StringUtils.isNotEmpty(this.downLineUser)){
			dc.add(Restrictions.eq("downlineuser", this.downLineUser));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by) && StringUtils.isNotEmpty(order)) {
			if("asc".equals(order.toLowerCase())){
				o = Order.asc(by);
//				dc.addOrder(Order.asc(by));
			} else {
				o = Order.desc(by);
//				dc.addOrder(Order.desc(by));
			}
		}
		dc.add(Restrictions.eq("type", "1"));
		Page page = PageQuery.queryForPagenationWithStatistics(proposalService.getHibernateTemplate(), dc, pageIndex, size,"money",null,null, o);
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	/**
	 * 根据日期派发好友推荐礼金
	 * 
	 * */
	public void calculateFriendMoney(){
		String message = "";
		try {
			if(StringUtils.isBlank(distributeDate)){
				message = "清选择派发好友推荐金日期";
			} else {
				message = this.friendMoneyDistributeService.calculateFriendMoneyByDate(this.distributeDate);
			}
			
		} catch (Exception e) {
			log.error("派发好友推荐金失败：" , e);
			GsonUtil.GsonObject("派发好友推荐金失败：" + e.toString());
		}
		
		GsonUtil.GsonObject(message);
	}
	
	/**
	 *  查询女神守护记录
	 * @return
	 */
	public String queryGoddessRecords(){
		
		DetachedCriteria dc = DetachedCriteria.forClass(Goddessrecord.class);
		
		if(StringUtils.isNotEmpty(this.loginname)){
			dc.add(Restrictions.eq("loginname", this.loginname));
		}
		if(StringUtils.isNotEmpty(this.goddessname)){
			dc.add(Restrictions.eq("goddessname", this.goddessname));
		}
		if(StringUtils.isNotEmpty(this.couponnum)){
			dc.add(Restrictions.eq("couponnum", this.couponnum));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by) && StringUtils.isNotEmpty(order)) {
			if("asc".equals(order.toLowerCase())){
				o = Order.asc(by);
//				dc.addOrder(Order.asc(by));
			} else {
				o = Order.desc(by);
//				dc.addOrder(Order.desc(by));
			}
		}
		Page page = PageQuery.queryForPagenationWithStatistics(proposalService.getHibernateTemplate(), dc, pageIndex, size,"bettotal",null,null, o);
		if(page != null){
			List<Goddessrecord> pagelist = page.getPageContents();
			if(pagelist != null && pagelist.size() > 0){
				for(int i = 0; i < pagelist.size(); i++){
					pagelist.get(i).setGoddessname(Goddesses.getName(pagelist.get(i).getGoddessname()));
				}
			}
		}
		
		getRequest().setAttribute("page", page);
		
		String sql = " SELECT goddessname,SUM(flowernum) as flowernum,SUM(bettotal) as bettotal,COUNT(loginname) as usernum from goddessrecord GROUP BY goddessname ORDER BY flowernum DESC,bettotal DESC,usernum ASC ";
		Session session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			Query query = session.createSQLQuery(sql);
			list = query.list();
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		List<Object[]> listVO = new ArrayList<Object[]>();
		if(list != null && list.size() > 0){
			Iterator<Object[]> it = list.iterator();
			while(it.hasNext()){
				Object[] obj = it.next();
				obj[0] = Goddesses.getName(obj[0].toString());
				listVO.add(obj);
			}
			
			Goddesses[] g = Goddesses.values();
			for(int i = 0; i < g.length; i++){
				
				Object[] vo = new Object[4];
				
				vo[0] = g[i].getName();
				vo[1] = 0;
				vo[2] = 0.0;
				vo[3] = 0;
				boolean flag = true;
				Iterator<Object[]> it_o = listVO.iterator();
				while(it_o.hasNext()){
					Object[] obj = it_o.next();
					if(obj[0].toString().equals(vo[0].toString())){
						flag = false;
					}
				}
				if(flag){
					listVO.add(vo);
				}
			}
			
		}
		
		getRequest().setAttribute("goddess",listVO);
		return INPUT;
	}
	/**
	 * 派发鲜花
	 * */
	public void calculateFlower(){
		String message = "";
		try {
			Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if(null == op){
				GsonUtil.GsonObject("请登录");
				return; 
			}
			message = this.friendMoneyDistributeService.calculateFlower();
			
		} catch (Exception e) {
			log.error("派发鲜花失败：" , e);
			GsonUtil.GsonObject("派发鲜花失败：" + e.toString());
		}
		
		GsonUtil.GsonObject(message);
		
	}
	
	/**
	 * 派发鲜花
	 * */
	public void calculateRankingAndCoupon(){
		String message = "";
		try {
			Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if(null == op){
				GsonUtil.GsonObject("请登录");
				return; 
			}
			message = this.friendMoneyDistributeService.calculateRankingAndCoupon(op.getUsername());
			
		} catch (Exception e) {
			log.error("计算排名派发红包失败：" , e);
			GsonUtil.GsonObject("计算排名派发红包失败：" + e.toString());
		}
//		int b = 1;
//		for(int i = 0; i < 4000; i++){
//			Goddessrecord g = new Goddessrecord();
//			g.setLoginname("test" + i);
//			g.setGoddessname("GOD" + b);
//			if(b == 5){
//				b = 0;
//			}
//			b++;
//			g.setBettotal((500+i)*1000.0);
//			g.setFlowernum(500+i);
//			this.userDao.save(g);
//		}
		GsonUtil.GsonObject(message);
		
	}
	
	
	public String queryUserSummary() {
		Page page = new Page();
		DetachedCriteria dc = DetachedCriteria.forClass(UsersSummary.class);
		
		if (!loginname.isEmpty()) {
			dc = dc.add(Restrictions.eq("loginname",loginname));
		}
		
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}
		page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);

		return INPUT;
	}
	
	public void gatherUserSummary() {
		
		Session  session=null;	
		try {
		
		    if ((start==null||end==null||paytype==null)) {
				return;
			}
			
			long startTime=System.currentTimeMillis();   //获取开始时间 
					
			Date date=new Date();
			log.info("gatherUserSummary程序运行开始"+date);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
		

			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();

			
			 StringBuffer sql = new StringBuffer("SELECT users.loginname,users.credit,users.role,users.flag,userstatus.loginerrornum,userstatus.slotaccount FROM users LEFT JOIN userstatus ON (users.loginname = userstatus.loginname) WHERE ");
			
			sql.append(" users.createtime>:start and users.createtime<=:end");

			if(loginname!=null){
				sql.append(" and  users.loginname='"+loginname+"'");
			}else if (paytype.equals("agent")) {
				sql.append(" and  users.role='AGENT' and flag=0");
			}else if (paytype.equals("all")) {
				
			}else if (paytype.equals("player")) {
				sql.append(" and  users.isCashin=0 and flag=0");
			}else {
				return ;
			}
			paramsMap.put("start", start);
			paramsMap.put("end", end);
			
		    session = slaveService.getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql.toString());
			query.setProperties(paramsMap);
			List list = query.list();
			String[]strings=new String[]{"TRANSFER_SBAIN","TRANSFER_AGININ","TRANSFER_MEWPTIN","TRANSFER_TTGIN",
					"TRANSFER_SLOTIN","TRANSFER_PBIN","TRANSFER_OGIN","TRANSFER_N2LIVEIN","TRANSFER_MWGIN","TRANSFER_MEWPTIN",
					"TRANSFER_FANYAIN","TRANSFER_FISHIN","TRANSFER_EBETAPPIN","TRANSFER_CHESSIN","TRANSFER_BITIN","TRANSFER_BBININ","TRANSFER_VRIN","TRANSFER_KYQPIN"};
			log.info("gatherUserSummary 获取用户数据"+list.size());
			
			for (int i = 0; i < list.size(); i++) {
				Object[]obj=(Object[]) list.get(i);
				UsersSummary usersSummary=new UsersSummary();
				if (obj[5]!=null) {
					usersSummary.setSlotaccount(Double.valueOf(obj[5].toString()));
				}
				if (obj[4]!=null) {
					usersSummary.setLoginerrornum(Integer.valueOf(obj[4].toString()));
				}

				usersSummary.setCredit(Double.valueOf(obj[1].toString()));
				usersSummary.setMoney(Double.valueOf(obj[1].toString()));
				usersSummary.setRole(obj[2].toString());
				usersSummary.setLoginname(obj[0].toString());
				usersSummary.setFlag(Integer.valueOf(obj[3].toString()));
				usersSummary.setCreatetime(date);
						
//				DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
//				dc = dc.add(Restrictions.in("type", strings));
//				dc = dc.add(Restrictions.eq("loginname", obj[0].toString()));
//
//	            ProjectionList pList = Projections.projectionList();
//	            pList.add(Projections.groupProperty("type"));
//	            dc.setProjection(pList);
//				List listObj = slaveService.getHibernateTemplate().findByCriteria(dc);

                 List<String> listObj = Arrays.asList(strings);
		        for (int j = 0; j < listObj.size(); j++) {
		        	String type=listObj.get(j).toString();
		        	try {
					if (StringUtil.equals("TRANSFER_SBAIN", type)) {
						usersSummary.setSbaSport(ShaBaUtils.CheckUserBalance(obj[0].toString()).doubleValue());
						if(usersSummary.getSbaSport()>=1.0){
							transfer(obj[0].toString(), "sba","self",String.valueOf(Math.floor(usersSummary.getSbaSport())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getSbaSport()));
						}
					}else if (StringUtil.equals("TRANSFER_AGININ", type)) {
						usersSummary.setAg(Double.valueOf(RemoteCaller.queryAgInDspCredit(obj[0].toString())));
						if(usersSummary.getAg()>=1.0){
							transfer(obj[0].toString(), "agin","self",String.valueOf(Math.floor(usersSummary.getAg())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getAg()));
						}
					}else if (StringUtil.equals("TRANSFER_MEWPTIN", type)) {
						usersSummary.setPt(PtUtil.getPlayerMoney(obj[0].toString()));
						if(usersSummary.getPt()>=1.0){
							transfer(obj[0].toString(), "newpt","self",String.valueOf(Math.floor(usersSummary.getPt())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getPt()));
						}
					}else if (StringUtil.equals("TRANSFER_BBININ", type)) {
						usersSummary.setBbin(BBinUtils.GetBalance(obj[0].toString()));	
						if(usersSummary.getBbin()>=1.0){
							transfer(obj[0].toString(), "bbin","self",String.valueOf(Math.floor(usersSummary.getBbin())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getBbin()));
						}
					}else if (StringUtil.equals("TRANSFER_SLOTIN", type)) {
						usersSummary.setSlot(SlotUtil.getBalance(obj[0].toString()));
						if(usersSummary.getSlot()>=1.0){
							transfer(obj[0].toString(), "slot","self",String.valueOf(Math.floor(usersSummary.getSlot())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getSlot()));
						}
					}else if (StringUtil.equals("TRANSFER_MWGIN", type)) {
						usersSummary.setMwg(MWGUtils.getUserBalance(obj[0].toString()));
						if(usersSummary.getMwg()>=1.0){
							transfer(obj[0].toString(), "mwg","self",String.valueOf(Math.floor(usersSummary.getMwg())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getMwg()));
							
						}
					}else if (StringUtil.equals("TRANSFER_N2LIVEIN", type)) {
						usersSummary.setN2live(NTwoUtil.checkClient(obj[0].toString()).getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue());
						if(usersSummary.getN2live()>=1.0){
							transfer(obj[0].toString(), "n2live","self",String.valueOf(Math.floor(usersSummary.getN2live())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getN2live()));
						}
					}else if (StringUtil.equals("TRANSFER_EBETAPPIN", type)) {
						usersSummary.setEbet(EBetAppUtil.getBalance(obj[0].toString()));
						if(usersSummary.getEbet()>=1.0){
							transfer(obj[0].toString(), "ebet","self",String.valueOf(Math.floor(usersSummary.getEbet())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getEbet()));
						}
					}else if (StringUtil.equals("TRANSFER_TTGIN", type)) {
						usersSummary.setTtg(Double.valueOf(PtUtil1.getPlayerAccount(obj[0].toString())));
						if(usersSummary.getTtg()>=1.0){
							transfer(obj[0].toString(), "ttg","self",String.valueOf(Math.floor(usersSummary.getTtg())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getTtg()));
						}
					}else if (StringUtil.equals("TRANSFER_PBIN", type)) {
						usersSummary.setPbSport(PBUtil.getBalance(obj[0].toString()));
						if(usersSummary.getPbSport()>=1.0){
					     	transfer(obj[0].toString(), "pb","self",String.valueOf(Math.floor(usersSummary.getPbSport())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getPbSport()));
						}
					}else if (StringUtil.equals("TRANSFER_FISHIN", type)) {
						 usersSummary.setFish(SlotUtil.getFishBalance(obj[0].toString()));
						 if(usersSummary.getFish()>=1.0){
						    transfer(obj[0].toString(), "fish","self",String.valueOf(Math.floor(usersSummary.getFish())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getFish()));
						 }
					}else if (StringUtil.equals("TRANSFER_CHESSIN", type)) {
						usersSummary.setChess(ChessUtil.getBalance(obj[0].toString()));
						if(usersSummary.getChess()>=1.0){
						    transfer(obj[0].toString(), "chess","self",String.valueOf(Math.floor(usersSummary.getChess())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getChess()));
						}
				    }else if (StringUtil.equals("TRANSFER_VRIN", type)) {
						usersSummary.setVr(KYQPUtil.getBalance(obj[0].toString(),"VR",""));
						if(usersSummary.getVr()>=1.0){
						    transfer(obj[0].toString(), "vr","self",String.valueOf(Math.floor(usersSummary.getVr())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getVr()));
						}
				    }else if (StringUtil.equals("TRANSFER_KYQPIN", type)) {
						usersSummary.setKyqp(KYQPUtil.getBalance(obj[0].toString(), "KYQP", ""));
						if(usersSummary.getKyqp()>=1.0){
						    transfer(obj[0].toString(), "kyqp","self",String.valueOf(Math.floor(usersSummary.getKyqp())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getKyqp()));	
						}
				    }else if (StringUtil.equals("TRANSFER_FANYAIN", type)) {
						usersSummary.setFanya(FanYaUtil.balance(obj[0].toString()));
						if(usersSummary.getFanya()>=1.0){
						    transfer(obj[0].toString(), "fanya","self",String.valueOf(Math.floor(usersSummary.getFanya())));
							usersSummary.setCredit(usersSummary.getCredit()+Math.floor(usersSummary.getFanya()));
						}
				    }
					
		            } catch (Exception e) {
		        		e.printStackTrace();
						continue;
					 }
				   }
		        
		        
		        try {
		        	
		        Users_clear users=(Users_clear) proposalService.get(Users_clear.class, usersSummary.getLoginname());
		        Userstatus userstatus=(Userstatus) proposalService.get(Userstatus.class, usersSummary.getLoginname());

		    	String md5Str = StringUtil.getRandomString_bak(8);
		    	Users_bak  customer = new Users_bak();
				customer.setWarnflag(users.getWarnflag());
				customer.setRole(users.getRole());
				if(users.getRole().equals("AGENT"))
				{
					customer.setLoginname("a_ldo"+users.getLoginname().substring(2));
				}else {
					customer.setLoginname("ldo"+usersSummary.getLoginname());
				}
			
				if(users.getAgent()!=null&&users.getAgent().startsWith("a_"))
				{
					customer.setAgent("a_ldo"+users.getAgent().substring(2));
				}else if(users.getAgent()!=null&&users.getAgent().startsWith("00000")){
					customer.setAgent("ldo"+users.getAgent());
				}else {
					customer.setAgent(users.getAgent());
				}
				
				customer.setPassword(users.getPassword());
				customer.setLevel(users.getLevel());
				customer.setAccountName(users.getAccountName());
				customer.setAliasName(users.getAliasName());
				customer.setPhone(users.getPhone());
				customer.setEmail(users.getEmail());
				customer.setPartner(users.getPartner()==null?users.getPartner():"LDO"+users.getPartner());
				customer.setQq(users.getQq());
				customer.setMd5str(md5Str);
				customer.setReferWebsite(users.getReferWebsite());
				customer.setPwdinfo(users.getPwdinfo());
				customer.setCredit(usersSummary.getCredit());
				customer.setCreatetime(DateUtil.now());
				customer.setRegisterIp(users.getRegisterIp());
				customer.setFlag(1);
				customer.setRemark(users.getRemark());
				customer.setClientos(users.getClientos());
				customer.setWarnremark(users.getWarnremark());
				customer.setArea(users.getArea());
				customer.setSms(users.getSms());
				customer.setInvitecode(users.getInvitecode());
				customer.setHowToKnow(users.getHowToKnow());
				customer.setId(users.getId()+10000000);
				
				if (!StringUtil.isEmpty(users.getAgcode())&&users.getAgcode().startsWith("a_")) {
					customer.setAgcode("a_ldo"+users.getAgcode().substring(2));
				}else if(!StringUtil.isEmpty(users.getAgcode())&&users.getAgcode().startsWith("00000")){
					customer.setAgcode("00000000"+(Integer.valueOf(users.getAgcode())+10000000));
				}else {
					customer.setAgcode(users.getAgcode());
				}
				customer.setPostcode(users.getPostcode());
				customer.setMicrochannel(users.getMicrochannel());
				customer.setBirthday(users.getBirthday());
				customer.setUserRemark("迁移用户:"+users.getUserRemark());

				
				Userstatus_bak userstatusbak=new Userstatus_bak();
				userstatusbak.setLoginname(users.getLoginname());
				if(users.getRole().equals("AGENT")){
					
					userstatusbak.setCommission(userstatus.getCommission());
					userstatusbak.setLiverate(userstatus.getLiverate());
					userstatusbak.setSportsrate(userstatus.getSportsrate());
					userstatusbak.setLotteryrate(userstatus.getLotteryrate());
					userstatusbak.setCommission(userstatus.getCommission());
					userstatusbak.setCommission(userstatus.getCommission());
				}
				userstatusbak.setSlotaccount(userstatus.getSlotaccount());
				userstatusbak.setLoginname(customer.getLoginname());
				userstatusbak.setCashinwrong(userstatus.getCashinwrong());
				userstatusbak.setLoginerrornum(userstatus.getLoginerrornum());
				userstatusbak.setMailflag(userstatus.getMailflag());
				userstatusbak.setTouzhuflag(userstatus.getTouzhuflag());
				userstatusbak.setAmount(userstatus.getAmount());
				userstatusbak.setFirstCash(userstatus.getFirstCash());
				
				operatorService.save(customer);
				operatorService.save(userstatusbak);
				operatorService.save(usersSummary);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("插入数据异常："+usersSummary.getLoginname());
			}

			}
			 
			long endTime=System.currentTimeMillis(); //获取结束时间 
			log.info("copyUsersData程序运行时间： "+((endTime-startTime)/(1000 * 60))+"分钟 **********");
			GsonUtil.GsonObject("导入完毕");
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("服务器异常");
		}finally {
			if(null!=session){
				session.close();
			}
		}

	}
	
	
	public String generateUserID() {
		Seq seq = (Seq) proposalService.get(Seq.class, Constants.SEQ_USERID_BAK, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Integer.parseInt(seqValue) + 1) + "");
			proposalService.update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_USERID_BAK);
			seq.setSeqValue("30001");
			proposalService.save(seq);
		}
		return seq.getSeqValue();
	}
		
	   private static ObjectMapper mapper = new ObjectMapper();

		
		public String transfer(String loginName, String source, String target, String amount) {
                
			try {

				Map<String, Object> paramMap = new HashMap<String, Object>();

				paramMap.put("product", "ld");
				paramMap.put("loginName", loginName);
				paramMap.put("source", source);
				paramMap.put("target", target);
				paramMap.put("amount", amount);
				
				String str = mapper.writeValueAsString(paramMap);
				

				String requestJson = app.util.AESUtil.getInstance().encrypt(str);

				String url =config.getString("middleservice.url.pay");

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
	 * 更新投注额
	 * */
	public void updateBettotal(){
		String message = "";
		try {
			Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if(null == op){
				GsonUtil.GsonObject("请登录");
				return; 
			}
			if(StringUtils.isBlank(this.loginname)){
				GsonUtil.GsonObject("未接收到有效用户名");
			}
			if(this.newbet < 0){
				GsonUtil.GsonObject("修改数量非法");
			}
			
			message = this.friendMoneyDistributeService.updateBettotal(this.loginname,this.newbet);
			log.info("更改守护女神投注额,操作者：" + op.getUsername() + "被更改用户：" + loginname + "更改投注额：" + newbet + "结果：" + message);
			Operationlogs olog = new Operationlogs(op.getUsername(),OperationLogType.SYN_BET_RECORDS.getCode(), DateUtil.convertToTimestamp(new Date()),  "更改投注额：" + newbet + ",被更改用户：" + loginname + ",结果：" + message);
			this.operatorService.save(olog);
			GsonUtil.GsonObject(message);
		} catch (Exception e) {
			log.error("更新投注额失败：" , e);
			GsonUtil.GsonObject("更新投注额失败：" + e.toString());
		}
		GsonUtil.GsonObject(message);
		
	}
	
	
	/**
	 * 处理平台错误 返水记录
	 */
	public void modifyPlatformRecord(){
		
		if (StringUtil.isNotEmpty(platform)) {
			String msg= proposalService.modifyPlatformRecord(getOperatorLoginname(),platform);
			GsonUtil.GsonObject(msg+" >>>>操作成功");
		}else 
			GsonUtil.GsonObject("平台不能为空，请选择");
	}
	
	
	/**
	 * 处理平台错误 返水记录
	 */
	public void modifySinglePlatform(){
		String msg="";
		
		if (StringUtil.isNotEmpty(platform)) {
			msg= proposalService.modifySinglePlatform(getOperatorLoginname(),platform);
			GsonUtil.GsonObject(msg+" >>>>操作成功");
		}else 
			GsonUtil.GsonObject("平台不能为空，请选择");
 
	}
	
	//ag slot 补录数据
		public void modifyAgSlotPlatform(){
					String msg="";
					if (startTime!=null) {
						msg= proposalService.modifyAgSlotPlatform(startTime);
						GsonUtil.GsonObject(" >>>>"+msg);
					}else 
						GsonUtil.GsonObject("时间不能为空");
		}
				
		public String getPlatformUpdateTime(){
			try {
				DetachedCriteria dc = DetachedCriteria.forClass(AgConfig.class);
				Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(),dc, pageIndex, size, null);
				getRequest().setAttribute("page", page);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg(e.getMessage());
			 }
			   return INPUT;
		 }

	
	/**
	 *  单身狗活动查询
	 * @return
	 */
	public String queryNineDaysBets(){
		
		Date agStart = DateUtil.parseDateForStandard("2016-08-02 00:00:00");
		Date agEnd = DateUtil.parseDateForStandard("2016-08-10 23:59:59");
		Date ptStart = DateUtil.parseDateForStandard("2016-08-01 00:00:00");
		Date ptEnd = DateUtil.parseDateForStandard("2016-08-09 00:00:00");
		
		Date endDate = DateUtil.parseDateForStandard("2016-08-10 13:00:00");//最后一次排名时间
		
		Date now = new Date();
		String today = DateUtil.fmtYYYY_MM_DD(now);
		
		Date limit = DateUtil.parseDateForStandard(today + " 13:00:00");
		if(limit.compareTo(endDate) > 0){
			limit = endDate;
		}
		String rankdate = null;
		if(now.compareTo(limit) < 0){
			rankdate = DateUtil.fmtYYYY_MM_DD(DateUtil.getDateAfter(now, -1));
		} else {
			rankdate = DateUtil.fmtYYYY_MM_DD(limit);
		}
		//this.friendMoneyDistributeService.updateSingleParty(agStart, agEnd, ptStart, ptEnd,rankdate);
		SynchronizedUtil.getInstance().updateSingleParty(proposalService, agStart, agEnd, ptStart, ptEnd,rankdate);
		DetachedCriteria dc = DetachedCriteria.forClass(SingleParty.class);
		if(StringUtils.isNotEmpty(this.loginname)){
			dc.add(Restrictions.eq("loginname", this.loginname));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by) && StringUtils.isNotEmpty(order)) {
			if("asc".equals(order.toLowerCase())){
				o = Order.asc(by);
//				dc.addOrder(Order.asc(by));
			} else {
				o = Order.desc(by);
//				dc.addOrder(Order.desc(by));
			}
		}
		Page page = PageQuery.queryForPagenationWithStatistics(proposalService.getHibernateTemplate(), dc, pageIndex, size,"bettotal",null,null, o);
		getRequest().setAttribute("page",page);
		return INPUT;
	}
	public String dealWechat() {
		if (loginname == null || loginname.trim().length()<=0) {
			GsonUtil.GsonObject("用户账号不能为空");
			return null;
		}
		Users user = (Users) operatorService.get(Users.class, loginname);
		DepositWechat dWechat = (DepositWechat) operatorService.get(DepositWechat.class, billno);
		if(dWechat != null && user != null){
			if(!UserRole.MONEY_CUSTOMER.getCode().equals(user.getRole())){
				GsonUtil.GsonObject("输入的账号是代理");
				return null;
			}
			if(dWechat.getState().intValue() != 2){
				GsonUtil.GsonObject("不是未匹配的订单");
				return null;
			}
			
			String msg = netpayService.submitWechatProposal(dWechat, user.getLoginname(), getOperatorLoginname(), getIp());
			if (msg == null) {
				GsonUtil.GsonObject("处理成功");
			} else {
				GsonUtil.GsonObject("处理失败:" + msg);
			}
		}else{
			GsonUtil.GsonObject("异常null，请确认用户账号是否输入正确");
		}
		return null;
	}
	public String changeWechat() {
		DepositWechat dWechat = (DepositWechat) operatorService.get(DepositWechat.class, billno);
		if(dWechat != null){
			if(dWechat.getState().intValue() != 2){
				GsonUtil.GsonObject("不是未匹配的订单");
				return null;
			}
			
			dWechat.setState(1);
			dWechat.setRemark(dWechat.getRemark()+";改状态by "+getOperatorLoginname());
			proposalService.update(dWechat);
			GsonUtil.GsonObject("成功");
		}else{
			GsonUtil.GsonObject("异常null，请确认单号是否正确");
		}
		return null;
	}	

	public String queryWechatline() {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositWechat.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("state", Integer.parseInt(status)));// 状态
		if (StringUtil.isNotEmpty(billno))
			dc = dc.add(Restrictions.like("billno", billno, MatchMode.ANYWHERE));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("paytime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("paytime", end));
		if(StringUtils.isNotBlank(acceptName)){
			dc = dc.add(Restrictions.eq("wechat", acceptName));
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	

	//催账信息查询
	public String queryUrgeOrderPage(){
		Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			String countS = "select count(1) ";
			String selectS = "select * ";
			String midS = "FROM urge_order a ";
			String whereS = "where 1=1 ";
			if (StringUtils.isNotEmpty(status)){
				whereS += "and a.status = "+status+" " ;  //催账记录状态
			}
			if (StringUtils.isNotEmpty(loginname)){
				whereS += "and a.loginname = '"+loginname+"' ";
			}
			if (startDate != null) {
				whereS += "and a.createtime >= '" + DateUtil.fmtYYYY_MM_DD(startDate) + "' ";
			}
			if (endDate != null) {
				whereS += "and a.createtime <= '" + DateUtil.fmtYYYY_MM_DD(endDate) + "' ";
			}
			
			if(StringUtils.isNotEmpty(type)){
				whereS += "and a.type = "+type+" ";
			}
			if (StringUtils.isNotEmpty(by)) {
				whereS += "ORDER BY a."+by+("desc".equals(order)?" DESC ":" ASC ");
			}
			Query query = session.createSQLQuery(countS+midS+whereS);
			String number = query.uniqueResult()+"";
			Integer total=Integer.parseInt(number);
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			
			query = session.createSQLQuery(selectS+midS+whereS).addEntity(UrgeOrder.class);
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List<UrgeOrder> contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(total);
			int pages = PagenationUtil.computeTotalPages(total, size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}
	
	/**
	 * 处理催账记录
	 */
	@SuppressWarnings("unchecked")
	public void handleUrgeOrder(){
		DetachedCriteria dc = DetachedCriteria.forClass(UrgeOrder.class);
		dc = dc.add(Restrictions.eq("id", id));
		List<UrgeOrder> list = proposalService.findByCriteria(dc);
		UrgeOrder uo = list.get(0);
		Operator opr = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		String operator = opr.getUsername();
		if("Y".equals(value)){
			uo.setStatus(1);
			uo.setUpdatetime(new Date());
			uo.setOperator(operator);
		}else{
			uo.setStatus(2);
			String oldRemark = uo.getRemark();
			String newRemark = oldRemark+"  审核人员："+(remark==null?"":remark);
			uo.setRemark(newRemark);
			uo.setOperator(operator);
			uo.setUpdatetime(new Date());
		}
		transferService.update(uo);
	}
	
	
	public String queryConcertBet(){
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("errorMsg", "");
		Page page = null;
		
		try{
			
			if ((size == null) || (size.intValue() == 0))
				size = 20;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			if (null == type)
				type ="2"; 
			page = proposalService.queryConcertBet(loginname,id,pageIndex, size,Integer.valueOf(type));
			
			request.setAttribute("page", page);
			
		} catch (Exception e){
			log.error("queryConcertBet exception:", e);
			request.setAttribute("errorMsg", "发生未知错误");
		}
		
		return SUCCESS;
	}
	
	//  流水假数据添加
	public String modifiedConcertBet() {
		try {
			Integer type = 5;   //必须修改新活动
			DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
			dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.eq("type", type));
			dc = dc.add(Restrictions.eq("round", id));
			List<Concert> list = proposalService.findByCriteria(dc);
			if (list != null && list.size() > 0) {
				GsonUtil.GsonObject(loginname + ":用户已经存在,在添加其他用户名");
				return INPUT;
			}

			ConcertDateType cd = ConcertDateType.getCode(id);
			Concert concert = new Concert();
			concert.setLoginname(loginname);
			concert.setBet(fee);
			concert.setCreatetime(createdate == null ? new Date() : createdate);
			concert.setRound(id);
			concert.setType(type);
			concert.setDisplay(0);
			concert.setStartTime(DateUtil.fmtStandard(cd.getStart()));
			concert.setEndTime(DateUtil.fmtStandard(cd.getEnd()));
			concert.setLastTime(DateUtil.fmtStandard(cd.getStart()));
			concert.setActive(0);
			proposalService.save(concert);
			GsonUtil.GsonObject("添加成功！！");
			return SUCCESS;
		} catch (Exception e) {
			log.error("modifiedConcertBet error:", e);
			GsonUtil.GsonObject("操作异常, 请确认一下需要新增的数据! 异常消息:" + e.getMessage());
			return INPUT;
		}
	}
	
	public void updateRankingData(){
		
		try {
			String msg = proposalService.updateRankingData(this.round, 5);
			GsonUtil.GsonObject(msg);
		}catch (Exception e) {
			log.error("流水排名发生异常", e);
			GsonUtil.GsonObject("流水排名发生异常，请联系相关人员");
		}
	}
	
	/* 指定日期更新 演唱会流水 */
		public String updateConcertBetOnDay(){
		try {
			Date date = new Date();
			Integer type = 5;   //必须修改新活动
			// Date date = DateUtil.fmtStandard("2016-09-3 00:00:00");//模拟当前时间
			ConcertDateType concertDateType = ConcertDateType.getCode(id);
			/*DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
			dc = dc.add(Restrictions.eq("active", 0));
			dc = dc.add(Restrictions.eq("type", type));
			dc = dc.add(Restrictions.eq("startTime",
					DateUtil.fmtStandard(concertDateType.getStart())));
			dc = dc.add(Restrictions.eq("round", concertDateType.getText()));
			List<Concert> list = proposalService.findByCriteria(dc);

			if (list == null || list.size() == 0) {
				GsonUtil.GsonObject("没流水数据更新");
			} else {
				proposalService.concertUpdateBet(list, date, concertDateType,type);
				log.info("演唱会活动 流水更新完毕:" + date);
				GsonUtil.GsonObject("更新指定日期流水成功");
			}*/
			String msg = proposalService.concertUpdateBet(date, concertDateType, type);
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			log.error("指定日期更新流水发生异常", e);
			GsonUtil.GsonObject("指定日期更新流水发生异常，请联系相关人员");
		}
		return null;
	}
	
	public String updateConcertDisplay() {

		Integer ids = id;
		if (id == null || id == 0) {
			GsonUtil.GsonObject("更新显示状态失败，参数错误");
			return null;
		}
		Integer res = proposalService.updateConcertDisplay(ids);
			GsonUtil.GsonObject(res);
			
		return null;
	}
	
	//修改演唱会活动流水
	public String editConcertBet() {

		Integer ids = id;
		if (id == null || id == 0) {
			GsonUtil.GsonObject("更新显示状态失败，参数错误:id为空");
			return null;
		}
		    proposalService.editConcertDisplay(ids,amount);
			GsonUtil.GsonObject("修改成功");
			
		return null;
	}
	
	/**
	 * 查询老虎机钱包余额
	 * 
	 * @return
	 * */
	public String querySlotCredit(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8"); 
			out = this.getResponse().getWriter();
			Double balance = SlotUtil.getBalance(loginname);
			out.println(balance);
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}


	/**
	 * 查询捕鱼钱包余额
	 *
	 * @return
	 * */
	public String queryFishCredit(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Double balance = SlotUtil.getFishBalance(loginname);
			out.println(balance);
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	
	public String queryMGCredit(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8"); 
			out = this.getResponse().getWriter();
			Users user = (Users) proposalService.get(Users.class, loginname);
			Double mgBalance = MGSUtil.getMGBalance(loginname,user.getPassword());
			out.println(mgBalance);
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}
	
	public String queryMGTransferlogs() throws SQLException{
		List<MGTransferLog> transferLogs = MGSUtil.getTransfersByLoginName(mgsService, loginname,stringStartTime,stringEndTime);
		getRequest().setAttribute("transferLogs", transferLogs);
		return INPUT;
	}
	
	public String mgPlayCheck() throws SQLException{
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			this.setErrormsg("会话已失效，请重新登录");
			return INPUT;
		}
		if(!("sale_manager".equals(op.getAuthority()) || "finance_manager".equals(op.getAuthority()) || "finance".equals(op.getAuthority()) || "boss".equals(op.getAuthority()))){
			if(StringUtils.isBlank(this.loginname)){
				this.setErrormsg("根据您的权限，必须输入用户账号进行查询！");
				return INPUT;
			}
		}
		List<MGPlaycheckVo> playList = MGSUtil.mgPlayChck(mgsService, loginname,stringStartTime, stringEndTime);
		getRequest().setAttribute("playList", playList);
		return INPUT;
	}

	public String queryGiftOrderList() {
	
		DetachedCriteria dc = DetachedCriteria.forClass(GiftOrder.class);
		
		if (null != id && 0 != id) {
		
			dc.add(Restrictions.eq("giftID", id));
		}
		
		if (StringUtils.isNotEmpty(loginname)) {
			
			dc.add(Restrictions.like("loginname", loginname, MatchMode.ANYWHERE));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc.addOrder(o);
		} else {
			
			o = Order.desc("applyDate");
//			dc.addOrder(o);
		}
		
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	public String addGiftOrder() {
	
		Object obj = operatorService.get(Users.class, StringUtil.trim(loginname));
		
		if (null == obj) {
			
			GsonUtil.GsonObject("添加失败，失败原因：当前玩家帐号 " + loginname + " 不存在！");
			return null;
		} 
		
		DetachedCriteria dc = DetachedCriteria.forClass(GiftOrder.class);
		
		dc.add(Restrictions.eq("giftID", id));
		dc.add(Restrictions.eq("loginname", loginname));
		
		List list = proposalService.findByCriteria(dc);
		
		if (null != list && !list.isEmpty()) {
		
			GsonUtil.GsonObject("添加失败，失败原因：当前玩家帐号 " + loginname +" 已添加到此活动中！");
			return null;
		}
		
		Users customer = (Users) obj;
			
		GiftOrder giftOrder = new GiftOrder(id, loginname, customer.getLevel(), addressee, cellphoneNo, address, 0, new Date());
		
		proposalService.save(giftOrder);
		
		GsonUtil.GsonObject("添加成功！");
		
		return null;
	}
	
	public String deleteGiftOrder() {
	
		if (null == id) {
			
			GsonUtil.GsonObject("删除失败，传入的参数为空！");
		} else {
			
			try {
				
				proposalService.delete(GiftOrder.class, id);
			} catch(Exception e) {
				
				log.error("执行deleteGiftOrder方法发生异常，异常信息：" + e.getMessage());
				
				GsonUtil.GsonObject("删除失败，请联系管理员！");
			}
			
			GsonUtil.GsonObject("删除成功！");
		}
		
		return null;
	}
	
	public Integer getGiftQuantity() {
		return giftQuantity;
	}

	public void setGiftQuantity(Integer giftQuantity) {
		this.giftQuantity = giftQuantity;
	}	
	
	//解除禁用
	public String relieveOperator() {
		String msg = operatorService.relieveOperator(loginname);
		setErrormsg(msg);
		return  INPUT;
	}	
	
	public String bindEmployeeNo(){
		String msg = null;
		if(StringUtils.isEmpty(employeeNo)){
			msg = "员工编号不能为空";
			setErrormsg(msg);
			return  INPUT;
		}
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		String username = operator.getUsername();
		Operator opr = (Operator) operatorService.get(Operator.class, username);

		String empNo = opr.getEmployeeNo();
		String phoneno = opr.getPhoneno();
		if (StringUtils.isNotEmpty(empNo)) {
			msg = "该账号已绑定员工编号，如要修改请联系产品经理";
		}else{
			if(StringUtils.isEmpty(phoneno)){
				Boolean result = operatorService.bindEmployeeNo(username,employeeNo);
				if(result){
					msg = "绑定成功";
				}else{
					msg = "绑定失败，请联系技术解决！";
				}
			}else{
				msg = "短信验证不需要绑定员工编号";
			}
		}
		setErrormsg(msg);
		return  INPUT;
	}
	
	public String queryGameByPlatform(){
		DetachedCriteria dc = DetachedCriteria.forClass(GameCode.class);
		dc.add(Restrictions.eq("type", type));
		if(StringUtils.isNotBlank(code)){
			dc.add(Restrictions.eq("code", code));
		}
		if(StringUtils.isNotBlank(gamename)){
			dc.add(Restrictions.eq("chineseName", gamename));
		}
		List<GameCode> list = operatorService.getHibernateTemplate().findByCriteria(dc);
		getRequest().setAttribute("list", list);
		return INPUT;
	}
	
	public void deleteGame(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			writeText("会话已失效，请重新登录");
			return;
		}
		operatorService.delete(GameCode.class, id);
		writeText("游戏已删除");
	}
	
	/**
	 *  查询短信配置开关
	 * @return
	 */
	public String querySMSConfig(){
		try {
			
			
			DetachedCriteria dc=DetachedCriteria.forClass(SMSSwitch.class);
			
			Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, 50, null);
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("查询短信配置开关出错：",e);
			setErrormsg("查询短信配置开关出错:" + e.getMessage());
		}
		
		return INPUT;
	}
	
	/**
	 *  新增或更新短信配置开关
	 */
	public void saveOrUpdatSMSConfig(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			writeText("会话已失效，请重新登录");
			return;
		}
		SMSSwitch smsswitch = null;
		if(this.id != null){
			smsswitch = (SMSSwitch) operatorService.get(SMSSwitch.class, this.id);
		}
		if(smsswitch == null){
			smsswitch = new SMSSwitch();
			smsswitch.setTitle(this.title);//title不修改
			smsswitch.setType(this.type);//type不修改
		}
		
//		smsswitch.setContent(this.content);
		smsswitch.setDisable(this.disable);
		smsswitch.setMinvalue(this.minvalue);
		smsswitch.setRemark(this.remark);
		String a = "";
		if(smsswitch.getId() == null){
			a = userDao.save(smsswitch) + "";
		} else {
			userDao.update(smsswitch);
		}
		
		writeText("success##" + a);
	}
	
	public void resetOperatorPassword() {
		
		String msg = "";
		try {
			Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
			if (null == operator) {
				msg = "请先登录！";
			} else {
				msg = operatorService.resetOperatorPassword(operator.getUsername(), username);
			}
		} catch (Exception e) {
			msg = e.getMessage();
		}
		GsonUtil.GsonObject(msg);
	}
	
	/**
	 * 查询使用者手机签到得奖记录
	 * */
	public String queryUserLotteryRecord(){
		DetachedCriteria dc = DetachedCriteria.forClass(UserLotteryRecord.class);
		if(!"".equals(loginname)){
			dc = dc.add(Restrictions.eq("loginname", loginname));
		}
		Order o = Order.desc("winningDate");
//		dc = dc.addOrder(Order.desc("winningDate"));
		int totalRecord = PageQuery.queryForCount(slaveService.getHibernateTemplate(), dc);
		Page page = new Page();
		if(totalRecord>0){
			page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		}
		getRequest().setAttribute("page",page);
		return INPUT;
	}
	
	public void checkUserGetLotteryItem() throws IOException{
		DetachedCriteria dc = DetachedCriteria.forClass(UserLotteryRecord.class);
		dc = dc.add(Restrictions.eq("id", Long.parseLong(itemId)));
		List<UserLotteryRecord> list = slaveService.getHibernateTemplate().findByCriteria(dc);
		if(list.size()>0){
			UserLotteryRecord record = list.get(0);
			record.setIsReceive(1);
			record.setReceiveDate(DateUtil.getCurrentTimestamp());
			slaveService.getHibernateTemplate().update(record);
		}
		PrintWriter out = getResponse().getWriter();
		out.write("sample string");
	}
	
	public String queryLotteryPrizes(){
		DetachedCriteria dc = DetachedCriteria.forClass(LotteryItem.class);
		Order o = Order.asc("type");
//		dc = dc.addOrder(Order.asc("type"));
		int totalRecord = PageQuery.queryForCount(slaveService.getHibernateTemplate(), dc);
		Page page = new Page();
		if(totalRecord>0){
			page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		}
		getRequest().setAttribute("page",page);
		return INPUT;
	}
	
	public void updatePrize() throws IOException{
		DetachedCriteria dc = DetachedCriteria.forClass(LotteryItem.class);
		dc = dc.add(Restrictions.eq("id", Long.parseLong(prizeId)));
		//dc = dc.add(Restrictions.eq("percent", Double.parseDouble(prizePercent)));
		List<LotteryItem> list = slaveService.getHibernateTemplate().findByCriteria(dc);
		if(list.size()>0){
			LotteryItem item = list.get(0);
			item.setPercent(Double.parseDouble(prizePercent));
			slaveService.getHibernateTemplate().update(item);
		}
		PrintWriter out = getResponse().getWriter();
		out.write("修改成功");
	}
	
	/*** 新支付 补单审核 */
	public String getSubmitPayAction2() {

		try {
			this.getRequest().setCharacterEncoding("UTF-8");
			this.getResponse().setCharacterEncoding("UTF-8");

			String url = config.getString("middleservice.url.pay")+"/supplement/one";
			Map param = new HashMap();
			param.put("orderid",billno);
			param.put("money",amount);
			param.put("loginname",loginname);
			param.put("remark",remark);
			String msg = HttpUtils.post(url,param);
			if ("success".equals(msg)) {
				writeText("审核成功");
			} else {
				writeText("审核失败");
			}
		} catch (Exception e) {
			log.error("审核异常：", e);
			writeText("审核异常");
		}
		return null;
	}
	
	public String payOrder2(){

		List<List<PayWayVo>> payWayVos = new ArrayList<>();
		try {
			String result = HttpUtils.get(config.getString("middleservice.url.pay")+"pay/pay_way2",null);
			log.error("payOrder2返回结果："+result);

			Gson gson = new Gson();
			payWayVos = gson.fromJson(result, new TypeToken<List<List<PayWayVo>>>(){}.getType());

		} catch (Exception e) {
			e.printStackTrace();
		}

		getRequest().getSession().getServletContext().setAttribute("payWayVos", payWayVos.get(0));
		getRequest().getSession().getServletContext().setAttribute("payWayVos2", payWayVos.get(1));
		if(null == startTime){
			startTime = DateUtil.getToday();
		}
		if(null == endTime){
			endTime = DateUtil.getTomorrow();
		}
		return INPUT;
	}
	public String getMerType(){

		List<List<PayWayVo>> payWayVos = new ArrayList<>();
		try {

			String url = "/pay/pay_way2?payWay="+payWay;

			if(payWay == null){
				url = "/pay/pay_way2";
			}

			String result = HttpUtils.get(config.getString("middleservice.url.pay")+url,null);

			log.error("getMerType返回结果："+result);

			Gson gson = new Gson();
			payWayVos = gson.fromJson(result, new TypeToken<List<List<PayWayVo>>>(){}.getType());

			GsonUtil.GsonObject(payWayVos.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询秒存存款订单
	 * @return
	 */
	public String queryDepositOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("status", Integer.parseInt(status)));  //订单状态
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(amount)) {
			dc.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}
		if (StringUtils.isNotEmpty(type)) {
			if(type.equals("01")){
				dc.add(Restrictions.in("type", new String[]{"0","1"}));
			}
			else{
				dc.add(Restrictions.eq("type", type));   
			}
		}else{
				dc = dc.add(Restrictions.in("type", new String[]{"0","1","2","3","4","22"}));
		}
		
		dc.add(Restrictions.isNull("spare"));
		dc.add(Restrictions.isNotNull("type")); 
		
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("uaccountname", username));
		}
		
//		dc.add(Restrictions.isNotNull("type"));
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);   
		}
		
//		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size,o);   
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null, null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	/**
	 * 作废秒存存款订单
	 * @return
	 */
	public String updateDepositOrder() {
		log.info(this.getOperatorLoginname() +"废弃秒存存款订单" + ids);
		DepositOrder order = (DepositOrder) operatorService.getHibernateTemplate().get(DepositOrder.class, ids);
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			if(order.getStatus().equals(0)){
				order.setStatus(2);
				order.setRemark(this.getOperatorLoginname() +"废弃秒存存款订单。" + DateUtil.getNow());
				operatorService.update(order);
			}
			out.println("订单已废弃");
		} catch (Exception e) {
			log.error("废弃秒存存款订单"+e.getMessage());
			out.println("作废订单失败");
		}
		return null;
	}
	
	public String queryMGGameDetail() throws SQLException {
		DetachedCriteria dc = DetachedCriteria.forClass(MgLog.class);
		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		if (start != null)
			dc = dc.add(Restrictions.ge("actiontime", start));
		if (end != null)
			dc = dc.add(Restrictions.le("actiontime", end));
		if (gameid != null)
			dc = dc.add(Restrictions.eq("gameid", String.valueOf(gameid)));
		
//		dc = dc.addOrder(Order.desc("actiontime"));

		Page page = PageQuery.queryForPagenation(mgsService.getMgsDao().getHibernateTemplate(), dc, pageIndex, size, Order.desc("actiontime"));
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String changeCMBBank() {
		
		CmbTransfers cmbTransfers = (CmbTransfers) operatorService.get(CmbTransfers.class, transfeId);
		
		if (null != cmbTransfers) {
			
			if (cmbTransfers.getStatus() != 2) {
				
				GsonUtil.GsonObject("不是未匹配的订单");
				return null;
			}
			
			cmbTransfers.setStatus(1);
			cmbTransfers.setRemark(cmbTransfers.getRemark()+";改状态by "+getOperatorLoginname());
			
			proposalService.update(cmbTransfers);
			GsonUtil.GsonObject("成功");
		} else {
			
			GsonUtil.GsonObject("异常，请确认单号是否正确");
		}
		
		return null;
	}
	
	public void batchUpdate(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			writeText("会话已失效，请重新登录");
			return;
		}
		ids = ids.substring(0, ids.length()-1);
		Boolean result = customerService.batchUpdate(belongto,ids);
		if(result){
			writeText("绑定成功");
		}else{
			writeText("绑定失败");
		}
	}

	public void sendSmsByPhoneList() {
		String operatorLoginname = getOperatorLoginname();
		if (StringUtils.isBlank(operatorLoginname)) {

			GsonUtil.GsonObject("登录会话已过期，请重新登录后再操作！");
			return ;
		}
		String value = querySystemConfig("type030", "001", "否");

		if (StringUtils.isBlank(value)) {

			GsonUtil.GsonObject("短信平台通道已关闭！");
			return;
		}

		List list = checkKeyWord(content);

		if (null != list && list.size() > 0) {

			StringBuffer sbf = new StringBuffer();

			for (int i = 0; i < list.size(); i++) {

				sbf = sbf.append(list.get(i) + ",");
			}

			if (sbf.length() > 0) {

				GsonUtil.GsonObject("该短信内容包含关键字：" + sbf.toString() + "，请修改后在发送。");
				return;
			}
		}

		String str = SendPhoneMsgUtil.InternationSms(phone, content);
//记录发送日志
		operatorService.insertOperatorSendSMSLog(operatorLoginname,str);
		GsonUtil.GsonObject(str);
	}

	public String queryPlatformAccountStatus(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			writeText("会话已失效，请重新登录");
			return null;
		}

		Session  session = customerService.getHibernateTemplate().getSessionFactory().openSession();
		String countS = "select count(1) ";
		String selectS = "select a.id, a.loginname, a.platform, a.status, a.operator, a.operate_time ";
		String midS = "FROM platform_account_status a ";
		String whereS = "where 1=1 ";
		if (StringUtils.isNotEmpty(loginname)){
			whereS += "and a.loginname = '"+loginname+"' ";
		}
		if (StringUtils.isNotEmpty(status)){
			whereS += "and a.status = '"+status+"' ";
		}
		if (null != startTime) {
			whereS += "and a.operate_time >= '"+DateUtil.formatDateForStandard(startTime)+"' ";
		}
		if (null != endTime) {
			whereS += "and a.operate_time <= '"+DateUtil.formatDateForStandard(endTime)+"' ";
		}

		if (StringUtils.isNotEmpty(by)) {
			whereS += "ORDER BY a."+by+("desc".equals(order)?" DESC ":" ASC ");
		}
		Query query = session.createSQLQuery(countS+midS+whereS);
		BigInteger countAry = (BigInteger) query.uniqueResult();
		Integer total = Integer.parseInt(countAry.toString());
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;

		query = session.createSQLQuery(selectS+midS+whereS);
		query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
		query.setMaxResults(size.intValue());
		List contentList = query.list();
		Page page = new Page();
		page.setPageNumber(pageIndex);
		page.setSize(size);
		page.setTotalRecords(total);
		int pages = PagenationUtil.computeTotalPages(total, size).intValue();
		page.setTotalPages(Integer.valueOf(pages));
		page.setPageContents(contentList);
		page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		getRequest().setAttribute("page", page);
		if(null!=session){
			session.close();
		}
		return INPUT;

	}

	public String queryCouponConfigList() {

		DetachedCriteria dc = DetachedCriteria.forClass(CouponConfig.class);

		dc.add(Restrictions.eq("isDelete", "N"));

		if (null != couponConfig) {

			if (StringUtils.isNotBlank(couponConfig.getCouponType())) {

				dc.add(Restrictions.eq("couponType", couponConfig.getCouponType()));
			}

			if (StringUtils.isNotBlank(couponConfig.getPlatformId())) {

				dc.add(Restrictions.eq("platformId", couponConfig.getPlatformId()));
			}

			if (StringUtils.isNotBlank(couponConfig.getStatus())) {

				dc.add(Restrictions.eq("status", couponConfig.getStatus()));
			}

			if (StringUtils.isNotBlank(couponConfig.getCouponCode())) {

				dc.add(Restrictions.eq("couponCode", couponConfig.getCouponCode()));
			}

			if (StringUtils.isNotBlank(couponConfig.getLoginName())) {

				dc.add(Restrictions.eq("loginName", couponConfig.getLoginName()));
			}

			if (StringUtils.isNotBlank(couponConfig.getCreateUser())) {

				dc.add(Restrictions.eq("createUser", couponConfig.getCreateUser()));
			}
		}

		if (null != start) {

			dc.add(Restrictions.ge("createTime", start));
		}

		if (null != end) {

			dc.add(Restrictions.le("createTime", end));
		}

		Order o = null;

		if (StringUtils.isNotEmpty(by)) {

			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {

			o = Order.desc("createTime");
		}

		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);

		getRequest().setAttribute("page", page);

		return INPUT;
	}
	private String loginAccount;

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public void addCouponConfig() {
		SimpleDateFormat hhmmss = new SimpleDateFormat("HHmmss");
		String str = hhmmss.format(new Date());

		String operateName = getOperatorLoginname();

		if (StringUtils.isBlank(operateName)) {

			GsonUtil.GsonObject("登录会话已过期，请重新登录后再操作！");
			return;
		}

		if (null == couponConfig) {

			GsonUtil.GsonObject("数据接收异常，请联系技术处理！");
			return;
		}
		int listSize = 0;
		List<String> loginers = new ArrayList<>();
		// if (null == times) {
		if (null != loginAccount && "" != loginAccount) {
			String[] strings = loginAccount.split("\n");

			for (String string : strings) {
				String loginer = string.trim();
				Users user = (Users) proposalService.get(Users.class, loginer);
				if (null != user && user.getRole().equalsIgnoreCase("MONEY_CUSTOMER")) {
					loginers.add(loginer);
				}

			}
		}
		if (null == times) {

			listSize = loginers.size();
		} else {
			listSize = times.intValue();
		}
		for (int i = 0; i < listSize; i++) {

			String sqlCouponId = "yhj" + DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);

			String couponCode = str + codeOne + sqlCouponId + codeTwo;

			couponConfig.setCouponCode(couponCode);
			if (null == times) {
				String loginName = loginers.get(i);
				couponConfig.setStatus("1");
				couponConfig.setAuditTime(new Date());
				couponConfig.setAuditUser(operateName);
				couponConfig.setLoginName(loginName);
			} else {
				couponConfig.setStatus("0");
			}
			couponConfig.setIsDelete("N");
			couponConfig.setCreateTime(new Date());
			couponConfig.setCreateUser(operateName);
			long couponConfigId = (Long) proposalService.save(couponConfig);
			log.info("===================" + couponConfigId);
			if (null == times) {
				String string = loginers.get(i);
				sendMassage(couponConfigId, string);
			}
		}

		/*
		 * } else { for (int i = 0; i < times.intValue(); i++) {
		 *
		 * String sqlCouponId = "yhj" + DateUtil.getYYMMDDN() +
		 * RandomStringUtils.randomAlphanumeric(10).toLowerCase(); String
		 * codeOne = dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3);
		 *
		 * String couponCode = str + codeOne + sqlCouponId + codeTwo;
		 *
		 * couponConfig.setCouponCode(couponCode); couponConfig.setStatus("0");
		 * couponConfig.setIsDelete("N"); couponConfig.setCreateTime(new
		 * Date()); couponConfig.setCreateUser(operateName);
		 *
		 * proposalService.save(couponConfig); } }
		 */
		if (listSize == 0 && null == times) {
			GsonUtil.GsonObject("账号不存在或不是会员！");
		} else {
			GsonUtil.GsonObject("操作成功！");
		}
	}
	public void sendMassage(Long couponConfigId, String name) {
		try {
			if (null != couponConfigId) {

				CouponConfig couponConfig = (CouponConfig) proposalService.get(CouponConfig.class, couponConfigId);

				Map<String, String> params = new HashMap<String, String>();

				params.put("receiveUname", name);
				params.put("ip", getIp());

				if (couponConfig.getCouponType().equals("319")) {

					NumberFormat nf = NumberFormat.getPercentInstance();

					params.put("title", nf.format(couponConfig.getPercent()) + "存送优惠劵代码");
					params.put("content",
							"亲爱的龙都会员，您好！您的" + couponConfig.getPlatformName() + nf.format(couponConfig.getPercent())
									+ "存送优惠券代码为：<" + couponConfig.getCouponCode() + ">，使用方式：个人中心>自助优惠>优惠券专区>存送优惠券，输入转账金额和代码后提交，红利与本金将自助转入您申请的平台，感谢您对我们的支持！");
				} else if (couponConfig.getCouponType().equals("419")) {

					params.put("title", "红包优惠券派发");
					params.put("content",
							"亲爱的龙都会员，您好！您的红包优惠券代码为：<" + couponConfig.getCouponCode() + ">，使用方式：个人中心>自助优惠>红包优惠券，输入代码，选择游戏平台提交即可，感谢您对我们的支持！");
				}

				proposalService.saveTopicStatus(params);
			}

			GsonUtil.GsonObject("操作成功！");
		} catch (Exception e) {

			log.error("更新数据失败，失败原因：" + e.getMessage());

			GsonUtil.GsonObject("更新数据发生异常，请稍后重试！");
		}
	}

	public void batchDeleteCouponConfig() {

		String operateName = getOperatorLoginname();

		if (StringUtils.isBlank(operateName)) {

			GsonUtil.GsonObject("登录会话已过期，请重新登录后再操作！");
			return;
		}

		if (StringUtils.isBlank(ids)) {

			GsonUtil.GsonObject("数据接收异常，请联系技术处理！");
			return;
		}

		List<Long> idList = new ArrayList<Long>();

		for (String id : ids.split(",")) {

			idList.add(Long.parseLong(id));
		}

		String sql = "UPDATE coupon_config SET is_delete = :isDelete, delete_time = :deleteTime, delete_user = :deleteUser WHERE id in (:ids)";

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("isDelete", "Y");
		paramsMap.put("deleteTime", new Date());
		paramsMap.put("deleteUser", operateName);
		paramsMap.put("ids", idList);

		Session session = null;

		try {

			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createSQLQuery(sql);
			query.setProperties(paramsMap);

			query.executeUpdate();

			GsonUtil.GsonObject("操作成功！");
		} catch (Exception e) {

			log.error("删除数据失败，失败原因：" + e.getMessage());

			GsonUtil.GsonObject("删除数据发生异常，请稍后重试！");
		} finally {

			if (null != session && session.isOpen()) {

				session.close();
			}
		}
	}

	public void batchAuditCouponConfig() {

		String operateName = getOperatorLoginname();

		if (StringUtils.isBlank(operateName)) {

			GsonUtil.GsonObject("登录会话已过期，请重新登录后再操作！");
			return;
		}

		if (StringUtils.isBlank(ids)) {

			GsonUtil.GsonObject("数据接收异常，请联系技术处理！");
			return;
		}

		if (StringUtils.isNotBlank(loginname)) {

			Users user = (Users) proposalService.get(Users.class, loginname);

			if (null == user) {

				GsonUtil.GsonObject("会员账号不存在，请核实后再操作！");
				return;
			}

			if (!user.getRole().equalsIgnoreCase("MONEY_CUSTOMER")) {

				GsonUtil.GsonObject("填写的账号不是会员账号，请核实后再操作！");
				return;
			}
		}

		List<Long> idList = new ArrayList<Long>();

		for (String id : ids.split(",")) {

			idList.add(Long.parseLong(id));
		}

		StringBuilder sql = new StringBuilder("UPDATE coupon_config SET status = :status, audit_time = :auditTime, audit_user = :auditUser");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("status", "1");
		paramsMap.put("auditTime", new Date());
		paramsMap.put("auditUser", operateName);

		if (StringUtils.isNotBlank(loginname)) {

			sql.append(",login_name = :loginName");

			paramsMap.put("loginName", loginname);
		}

		sql.append(" WHERE id in (:ids)");

		paramsMap.put("ids", idList);

		Session session = null;

		try {

			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createSQLQuery(sql.toString());
			query.setProperties(paramsMap);

			query.executeUpdate();

			if (StringUtils.isNotBlank(loginname)) {

				Long id = idList.get(0);

				CouponConfig couponConfig = (CouponConfig) proposalService.get(CouponConfig.class, id);

				Map<String, String> params = new HashMap<String, String>();

				params.put("receiveUname", loginname);
				params.put("ip", getIp());

				if (couponConfig.getCouponType().equals("319")) {

					NumberFormat nf = NumberFormat.getPercentInstance();

					params.put("title", nf.format(couponConfig.getPercent()) + "存送优惠劵代码");
					params.put("content", "亲爱的龙都会员，您好！您的" + couponConfig.getPlatformName() + nf.format(couponConfig.getPercent()) + "存送优惠券代码为：<" + couponConfig.getCouponCode() + ">，使用方式：个人中心>自助优惠>优惠券专区>存送优惠券，输入转账金额和代码后提交，红利与本金将自助转入您申请的平台，感谢您对我们的支持！");
				} else if (couponConfig.getCouponType().equals("419")) {

					params.put("title", "红包优惠券派发");
					params.put("content", "亲爱的龙都会员，您好！您的红包优惠券代码为：" + couponConfig.getCouponCode() + "，使用方式：个人中心>自助优惠>优惠券专区>红包优惠券，输入代码，选择游戏平台提交即可，感谢您对我们的支持！");
				}

				proposalService.saveTopicStatus(params);
			}

			GsonUtil.GsonObject("操作成功！");
		} catch (Exception e) {

			log.error("更新数据失败，失败原因：" + e.getMessage());

			GsonUtil.GsonObject("更新数据发生异常，请稍后重试！");
		} finally {

			if (null != session && session.isOpen()) {

				session.close();
			}
		}
	}
	public void querySMSPlatformSwitch() {

		String str = querySystemConfig("type030", "001", "否");

		GsonUtil.GsonObject(str);
	}

	public void updateSMSPlatformSwitch() {

		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);

		dc.add(Restrictions.eq("typeNo", "type030"));

		List<SystemConfig> list = operatorService.findByCriteria(dc);

		SystemConfig systemConfig = list.get(0);

		systemConfig.setFlag(flag);

		systemConfigService.updateSystemConfig(systemConfig);

		GsonUtil.GsonObject("SUCCESS");
	}

	public String getStringStartTime() {
		return stringStartTime;
	}

	public void setStringStartTime(String stringStartTime) {
		this.stringStartTime = stringStartTime;
	}

	public String getStringEndTime() {
		return stringEndTime;
	}

	public void setStringEndTime(String stringEndTime) {
		this.stringEndTime = stringEndTime;
	}
	
	
	
//  换线用户信息列表
	public String queryChangeLineUserRecord() {

		Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		StringBuffer sbf = new StringBuffer();
		//  lineuser.deposit deposit,lineuser.winorlose   winorlose ,
		sbf.append("select lineuser.id id,lineuser.changelinetime changeLineTime, lineuser.username userName, lineuser.codeafter codeAfter,lineuser.codebefore codeBefore, lineuser.createTime createTime,  users.phone phone  from change_line_user lineuser  inner join users users on  users.loginname = lineuser.username     ");
		sbf.append(" where 1=1 ");
		Map<String,Object> mm = new HashMap<String, Object>();
		if (start != null) {
			sbf.append(" and lineuser.changelinetime >=:changelineStarttime");
			mm.put("changelineStarttime",DateUtil.formatDateForStandard(start) );
		}
		if (end != null) {
			sbf.append(" and lineuser.changelinetime <=:changelineEndtime");
			mm.put("changelineEndtime",DateUtil.formatDateForStandard(end) );
		}
		if (startcreateTime != null) {
			sbf.append(" and lineuser.createTime >=:startcreateTime");
			mm.put("startcreateTime",DateUtil.formatDateForStandard(startcreateTime) );
		}
		if (endcreateTime != null) {
			sbf.append(" and lineuser.createTime <=:endcreateTime");
			mm.put("endcreateTime",DateUtil.formatDateForStandard(endcreateTime) );
		}
		if (startDeposit != null) {
			sbf.append(" and lineuser.deposit >=:startDeposit");
			mm.put("startDeposit",startDeposit.doubleValue() );
		}
		if (endDeposit != null) {
			sbf.append(" and lineuser.deposit <=:endDeposit");
			mm.put("endDeposit",endDeposit.doubleValue() );
		}
		if (StringUtils.isNotEmpty(codeAfter)) {
			sbf.append(" and lineuser.codeafter =:codeAfter");
			mm.put("codeAfter",codeAfter );
		}
		if (StringUtils.isNotEmpty(codeBefore)) {
			sbf.append(" and lineuser.codebefore =:codeBefore");
			mm.put("codeBefore",codeBefore );
		}
		if (StringUtils.isNotEmpty(userName)) {
			sbf.append(" and lineuser.username =:userName");
			mm.put("userName",userName );
		}
		if (startWinOrLose != null) {
			sbf.append(" and lineuser.winorlose >=:startWinOrLose");
			mm.put("startWinOrLose",startWinOrLose );
		}
		if (endWinOrLose != null) {
			sbf.append(" and lineuser.winorlose <=:endWinOrLose");
			mm.put("endWinOrLose",endWinOrLose );
		}
		if (StringUtils.isNotBlank(sms)) {
			sbf.append(" and users.sms =:sms");
			mm.put("sms",sms );
		}
		List<String> phoneList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (StringUtil.isNotEmpty(callDayNum)) {
			DetachedCriteria dcc = DetachedCriteria.forClass(CallInfo.class);
			// 判断3天未拨打游戏玩家 
			if ("0".equals(callDayNum)) {
				cal.add(Calendar.DATE, -3);
				Date monday = cal.getTime();
				dcc = dcc.add(Restrictions.ge("createtime", monday));
			}
			// 判断7天未拨打游戏玩家
			if ("1".equals(callDayNum)) {
				cal.add(Calendar.DATE, -7);
				Date monday = cal.getTime();
				dcc = dcc.add(Restrictions.ge("createtime",  monday));
			}
			// 判断15天未拨打游戏玩家
			if ("2".equals(callDayNum)) {
				cal.add(Calendar.DATE, -15);
				Date monday = cal.getTime();
				dcc = dcc.add(Restrictions.ge("createtime", monday));
			}
			// 判断30天未拨打游戏玩家
			if ("3".equals(callDayNum)) {
				cal.add(Calendar.DATE, -31);
				Date monday = cal.getTime();
				dcc = dcc.add(Restrictions.ge("createtime", monday));
			}
			List<CallInfo> list = operatorService.getHibernateTemplate().findByCriteria(dcc);
			for (CallInfo call : list) {
				String phone = call.getCalled();
				if(phone.length()>11)
				{
					phone = phone.substring(1, phone.length());
				}
				try {
					String aPhone = AESUtil.aesEncrypt(phone, AESUtil.KEY);
					phoneList.add(aPhone);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// 判断7天未游戏玩家
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if ("0".equals(dayNumflag)) {
			cal.add(Calendar.DATE, -7);
			Date monday = cal.getTime();
			sbf.append(" and users.lastLoginTime <=:lastLoginTime");
			mm.put("lastLoginTime",DateUtil.formatDateForStandard(monday) );
		}
		// 判断15天未游戏玩家
		if ("1".equals(dayNumflag)) {
			cal.add(Calendar.DATE, -15);
			Date monday = cal.getTime();
			sbf.append(" and users.lastLoginTime <=:lastLoginTime");
			mm.put("lastLoginTime",DateUtil.formatDateForStandard(monday) );
		}
		// 判断30天未游戏玩家  
		if ("2".equals(dayNumflag)) {
			cal.add(Calendar.DATE, -31);
			Date monday = cal.getTime();
			sbf.append(" and users.lastLoginTime <=:lastLoginTime");
			mm.put("lastLoginTime",DateUtil.formatDateForStandard(monday) );
		}
		// 判断30天以上未游戏玩家
		if ("3".equals(dayNumflag)) {
			cal.add(Calendar.DATE, -31);
			Date monday = cal.getTime();
			sbf.append(" and users.lastLoginTime <:lastLoginTime");
			mm.put("lastLoginTime",DateUtil.formatDateForStandard(monday) );
		}
		if(StringUtils.isNotEmpty(by)){
			sbf.append(" order by "+by+" "+order);
		}
		if ((size == null) || (size.intValue() == 0))
			size = 20;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		Query query = null;
		query = session.createSQLQuery(sbf.toString());
		if(!mm.isEmpty()){
			query = query.setProperties(mm);//直接将map参数传组query对像
		}
		List list = query.list();
		Double depositAmount = 0.00;
		Double winorloseAmount = 0.00;
		log.info(mm);
		List formalList = new ArrayList();
		for(Object obj : list){
			Object[] objarray = (Object[])obj;
			if(phoneList.size() != 0) {
				for(int i=0;i< phoneList.size();i++) {
					  String phoneTemp = phoneList.get(i);
					  if(phoneTemp.equals(objarray[6]) == true) {
						  // 查询存款和 玩家输赢
						  // lineuser.id id,lineuser.changelinetime changeLineTime, lineuser.username userName, lineuser.codeafter codeAfter,lineuser.codebefore codeBefore, lineuser.createTime createTime,  users.phone phone 
						  double[] depositAndWinorLose = changeLineUserService.getDepositAndWinorlose((String)objarray[2],start,(Date)objarray[1]  );
						  Object[] object= new Object[] {objarray[0],objarray[1],objarray[2],objarray[3],objarray[4],objarray[5],depositAndWinorLose[0], depositAndWinorLose[1]};
						  formalList.add(object);
						  break;
					  }
				}
			}else {
				// 查询存款和 玩家输赢
				  double[] depositAndWinorLose = changeLineUserService.getDepositAndWinorlose((String)objarray[2],start,(Date)objarray[1]  );
				  Object[] object= new Object[] {objarray[0],objarray[1],objarray[2],objarray[3],objarray[4],objarray[5],depositAndWinorLose[0], depositAndWinorLose[1]};
				  formalList.add(object);
				//  formalList = list;
			}
		}
		for(Object obj : formalList){
			Object[] objarray = (Object[])obj;
			depositAmount +=(Double)objarray[6];
			winorloseAmount +=(Double)objarray[7];
		}
		query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
		query.setMaxResults(size.intValue());
		List contentList = query.list();
		List formalContentList = new ArrayList();
		for(Object obj : contentList){
			Object[] objarray = (Object[])obj;
			if(phoneList.size() != 0) {
				for(int i=0;i< phoneList.size();i++) {
					  String phoneTemp = phoneList.get(i);
					  if(phoneTemp.equals(objarray[6]) == true) {
						  // 查询存款和 玩家输赢
						  double[] depositAndWinorLose = changeLineUserService.getDepositAndWinorlose((String)objarray[2],start,(Date)objarray[1]  );
						  Object[] object= new Object[] {objarray[0],objarray[1],objarray[2],objarray[3],objarray[4],objarray[5],depositAndWinorLose[0], depositAndWinorLose[1]};
						  formalContentList.add(object);
						 // formalContentList.add(obj);
						  break;
					  }
				}
			}else {
				  // 查询存款和 玩家输赢
				  double[] depositAndWinorLose = changeLineUserService.getDepositAndWinorlose((String)objarray[2],start,(Date)objarray[1]  );
				  Object[] object= new Object[] {objarray[0],objarray[1],objarray[2],objarray[3],objarray[4],objarray[5],depositAndWinorLose[0], depositAndWinorLose[1]};
				  formalContentList.add(object);
				
				
				//formalContentList = contentList;
			}
		}
		List changeLineUserList = new ArrayList();
		if(formalContentList != null) {
			for(int i=0;i< formalContentList.size(); i++) {
				Object[] array = (Object[])formalContentList.get(i);
				ChangeLineUser changeLineUser = new ChangeLineUser();
				changeLineUser.setId((Integer)array[0]);
				changeLineUser.setChangeLineTime((Date)array[1]);
				changeLineUser.setUserName( (String)array[2] );
				changeLineUser.setCodeAfter( (String)array[3]);
				changeLineUser.setCodeBefore((String)array[4] );
				changeLineUser.setCreateTime((Date)array[5]   );
				changeLineUser.setDeposit((Double)array[6] );
				changeLineUser.setWinorlose((Double)array[7]);
				changeLineUserList.add(changeLineUser);
			}
		}
		Page page = new Page();
		page.setPageNumber(pageIndex);
		page.setSize(size);
		page.setTotalRecords(formalList.size());
		int pages = PagenationUtil.computeTotalPages(formalList.size(), size).intValue();
		page.setTotalPages(Integer.valueOf(pages));
		if (pageIndex.intValue() > pages)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		page.setPageNumber(pageIndex);
		page.setPageContents(changeLineUserList);
		page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		page.setStatics1(depositAmount);
		page.setStatics2(winorloseAmount);
		getRequest().setAttribute("page", page);
		if(null!=session){
			session.close();
		}
		return INPUT;
	}

	public String copyUsersData() {
		Session  session=null;
		try {
			long startTime=System.currentTimeMillis();   //获取开始时间 
	
		    Date date=new Date();
			log.info("copyUsersData程序运行开始"+date);
			StringBuffer sql = new StringBuffer("SELECT users.loginname,users.credit,users.role,users.flag,userstatus.loginerrornum,userstatus.slotaccount FROM users LEFT JOIN userstatus ON (users.loginname = userstatus.loginname) WHERE users.lastLoginTime>'2018-01-01 00:00:00' and users.lastLoginTime<'2018-07-01 00:00:00' and flag=1 and role='MONEY_CUSTOMER'");
		    session = slaveService.getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql.toString());

			List list = query.list();
//			String[]strings=new String[]{"TRANSFER_SBAIN","TRANSFER_AGININ","TRANSFER_MEWPTIN"};
			log.info("copyUsersData2 获取用户数据"+list.size());
			
			for (int i = 0; i < list.size(); i++) {
				Object[]obj=(Object[]) list.get(i);
				UsersSummary usersSummary=new UsersSummary();
				if (obj[5]!=null) {
					usersSummary.setSlotaccount(Double.valueOf(obj[5].toString()));
				}
				if (obj[4]!=null) {
					usersSummary.setLoginerrornum(Integer.valueOf(obj[4].toString()));
				}

				usersSummary.setCredit(Double.valueOf(obj[1].toString()));
				usersSummary.setRole(obj[2].toString());
				usersSummary.setLoginname(obj[0].toString());
				usersSummary.setFlag(Integer.valueOf(obj[3].toString()));
				usersSummary.setCreatetime(date);
				
				
//				DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
//				dc = dc.add(Restrictions.in("type", strings));
//				dc = dc.add(Restrictions.eq("loginname", obj[0].toString()));
//
//	            ProjectionList pList = Projections.projectionList();
//	            pList.add(Projections.groupProperty("type"));
//	            pList.add(Projections.property("type"));
//	            dc.setProjection(pList);
//				List listObj = slaveService.getHibernateTemplate().findByCriteria(dc);
//		        for (int j = 0; j < listObj.size(); j++) {
//		        	String type=listObj.get(j).toString();
//		        	try {
					/*if (StringUtil.equals("TRANSFER_SBAIN", type)) {
						usersSummary.setSbaSport(ShaBaUtils.CheckUserBalance(obj[0].toString()).doubleValue());
					}else if (StringUtil.equals("TRANSFER_AGININ", type)) {
						usersSummary.setAg(Double.valueOf(APInUtils.GetBalance(obj[0].toString(), obj[0].toString())));
					}else if (StringUtil.equals("TRANSFER_MEWPTIN", type)) {
						usersSummary.setPt(PtUtil.getPlayerMoney(obj[0].toString()));
					}else if (StringUtil.equals("TRANSFER_BBININ", type)) {
						usersSummary.setBbin(BBinUtils.GetBalance(obj[0].toString()));		
					}else if (StringUtil.equals("TRANSFER_SLOTTIN", type)) {
						usersSummary.setSlot(SlotUtil.getBalance(obj[0].toString()));
					}else if (StringUtil.equals("TRANSFER_MWGIN", type)) {
						usersSummary.setMwg(MWGUtils.getUserBalance(obj[0].toString()));
					}else if (StringUtil.equals("TRANSFER_N2LIVE_IN", type)) {
						usersSummary.setN2live(NTwoUtil.checkClient(loginname).getBalance().setScale(2, RoundingMode.HALF_UP).doubleValue());
					}else if (StringUtil.equals("TRANSFER_EBETAPPIN", type)) {
						usersSummary.setEbet(EBetAppUtil.getBalance(obj[0].toString()));
					}else if (StringUtil.equals("TRANSFER_TTGIN", type)) {
						usersSummary.setTtg(Double.valueOf(PtUtil1.getPlayerAccount(obj[0].toString())));
					}else if (StringUtil.equals("TRANSFER_MWGIN", type)) {
						usersSummary.setMwg(MWGUtils.getUserBalance(obj[0].toString()));
					}*/
					
//		        	} catch (Exception e) {
//						continue;
//					}
//
//				}
			
				try {
					usersSummary.setSbaSport(ShaBaUtils.CheckUserBalance(obj[0].toString()).doubleValue());

				} catch (Exception e) {
					log.info("SBA不存在:"+obj[0].toString());
				}
				
				try {
					usersSummary.setAg(Double.valueOf(RemoteCaller.queryAgInDspCredit(obj[0].toString())));
				} catch (Exception e) {
					log.info("AG不存在:"+obj[0].toString());
				}
				
				try {
					usersSummary.setPt(PtUtil.getPlayerMoney(obj[0].toString()));
				} catch (Exception e) {
					log.info("PT不存在:"+obj[0].toString());
				}
				operatorService.save(usersSummary);
			}
				
			 
			long endTime=System.currentTimeMillis(); //获取结束时间 
			log.info("copyUsersData程序运行时间： "+((endTime-startTime)/(1000 * 60))+"分钟 **********");
			GsonUtil.GsonObject("导入完毕");
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("服务器异常");
		}finally {
			if(null!=session){
				session.close();
			}
		}

		return null;
	}


	/**
	 * 删除站内信导致数据异常恢复数据
	 */
	public void checkTopicStatusData() {
		try {
			Session session = proposalService.getHibernateTemplate().getSessionFactory().openSession();

			List list = session.createSQLQuery("select id,login_name from coupon_config where is_delete='N' and status=1 and create_time>'2018-06-12 00:00:00' and create_time<'2018-07-12 18:23:14'").list();

			if (null != list || !list.isEmpty()) {
				log.info("size:" + list.size());
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = (Object[]) list.get(i);
					log.info("id:" + obj[0] + "loginname:" + obj[1]);
					sendMassage(Long.parseLong(String.valueOf(obj[0])), String.valueOf(obj[1]));
				}
			}
			GsonUtil.GsonObject("操作成功！");
			if (null != session) {
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 查询比特游戏钱包余额
	 *
	 * @return
	 * */
	public String queryBitCredit(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			Double balance = BitGameUtil.bitUserBalance(loginname);
			out.println(balance);
		} catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			log.error(e);
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}

	public Date getStartcreateTime() {
		return startcreateTime;
	}

	public void setStartcreateTime(Date startcreateTime) {
		this.startcreateTime = startcreateTime;
	}

	public Date getEndcreateTime() {
		return endcreateTime;
	}

	public void setEndcreateTime(Date endcreateTime) {
		this.endcreateTime = endcreateTime;
	}

	public String getCodeAfter() {
		return codeAfter;
	}

	public void setCodeAfter(String codeAfter) {
		this.codeAfter = codeAfter;
	}

	public String getCodeBefore() {
		return codeBefore;
	}

	public void setCodeBefore(String codeBefore) {
		this.codeBefore = codeBefore;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCallDayNum() {
		return callDayNum;
	}

	public void setCallDayNum(String callDayNum) {
		this.callDayNum = callDayNum;
	}

	public Double getStartDeposit() {
		return startDeposit;
	}

	public void setStartDeposit(Double startDeposit) {
		this.startDeposit = startDeposit;
	}

	public Double getEndDeposit() {
		return endDeposit;
	}

	public void setEndDeposit(Double endDeposit) {
		this.endDeposit = endDeposit;
	}

	public Double getStartWinOrLose() {
		return startWinOrLose;
	}

	public void setStartWinOrLose(Double startWinOrLose) {
		this.startWinOrLose = startWinOrLose;
	}

	public Double getEndWinOrLose() {
		return endWinOrLose;
	}

	public void setEndWinOrLose(Double endWinOrLose) {
		this.endWinOrLose = endWinOrLose;
	}
	
	public String getTransferOut() {
		return transferOut;
	}

	public void setTransferOut(String transferOut) {
		this.transferOut = transferOut;
	}

	public String getTransferIn() {
		return transferIn;
	}

	public void setTransferIn(String transferIn) {
		this.transferIn = transferIn;
	}
	
	
	/**
	 * 
	 * 1. 前台只留一个按钮，就是“抓取PT投注数据”                               
	 * 2. 先判断是否存在指定日期的PT数据，存在则一次性删除；                               
	 * 3. 将PT总投注数据、老虎机和累计老虎机数据、奖池数据都获取后，根据玩家账号，每个玩家一条记录，分批插入数据库
	 * 
	 * @author pony
	 */
	public String updatePtDataOnline() {
		if (org.apache.commons.lang3.StringUtils.isBlank(executeTime)) {
			writeText("请选择日期！");
			return null;
		}
		ReentrantLock lock = null;
		String phpHtmlRegular = null;
		try {
			lock = SynchronizedPool.getLockByEvent(SynchronizedPool.LockEvent.PT_DATA_ACQUISITION);
			if(lock.tryLock()){
				final Stopwatch stopwatch = Stopwatch.createStarted();
				String startTime = DateUtil.fmtYYYY_MM_DDToStandard(executeTime).substring(0, 10);
				String endTime = DateUtil.getchangedDateStr(1, executeTime);
				LogUtils.info("抓取昨天PT总流水>>>>>>> startTime:" + startTime + "  endTime:" + endTime);
				phpHtmlRegular = PtUtil.getSepPlayerBet(startTime, endTime, "both");
				if (StringUtils.isNotBlank(phpHtmlRegular)) {
					JSONObject jsonObj = JSONObject.fromObject(phpHtmlRegular);
					JSONArray arr = null;
					try {
						arr = jsonObj.getJSONArray("result");
					} catch (Exception e) {
						LogUtils.warn("phpHtmlRegular: " + phpHtmlRegular);
						arr = jsonObj.getJSONArray("RESULT");//TB是大写
					}
					//获取数据
					LogUtils.info("pt抓取总投注数据size: " + arr.size());
					
					LogUtils.info("开始同步PT老虎机和累计奖池老虎机数据");
					List<PTBetVO> betsList = null;
					List<PTBetVO> progressiveBetsList = null;
					Date startdate = DateUtil.parseDateForYYYYmmDD(executeTime);
					betsList = PTSpider2.spider(startdate, PTSpider.Type.Slot_Machines.getCode());
					progressiveBetsList = PTSpider2.spider(startdate, PTSpider.Type.Progressive_Slot_Machines.getCode());
					List<PTBetVO> progressiveList = PTSpider2.spider(startdate, PTSpider.Type.ProgressiveWins.getCode());
					
					Map<String, PTBetVO> ptslotBetMap = new HashMap<String, PTBetVO>();
					Map<String, PTBetVO> tiggerAllMap = new HashMap<String, PTBetVO>();
					Map<String, PTBetVO> progressiveMap = new HashMap<String, PTBetVO>();
					for (PTBetVO betVO : betsList) {
						ptslotBetMap.put(betVO.getLoginName(), betVO);
						tiggerAllMap.put(betVO.getLoginName(), betVO);
					}
					for (PTBetVO betVO2 : progressiveBetsList) {
						if(ptslotBetMap.containsKey(betVO2.getLoginName())){
							ptslotBetMap.get(betVO2.getLoginName()).setProgressiveBet(betVO2.getBets());
							ptslotBetMap.get(betVO2.getLoginName()).setProgressviceProfit(betVO2.getProfit());
						}else{
							betVO2.setProgressiveBet(betVO2.getBets());
							betVO2.setProgressviceProfit(betVO2.getProfit());
							betVO2.setBets(0.0);
							betVO2.setProfit(0.0);
							betsList.add(betVO2);
							
							tiggerAllMap.put(betVO2.getLoginName(), betVO2);
						}
					}
					
					for (PTBetVO betVO : progressiveList){//tiggerAllMap中包含tigger游戏，progressiveList却包含所有游戏： 老虎机 + 真人
						progressiveMap.put(betVO.getLoginName(), betVO);
					}
					
					getdateService.processAllPtData(arr, tiggerAllMap, progressiveMap, executeTime);
					LogUtils.info("抓取PT投注数据 --- 成功, 时间:" + stopwatch.stop());
					writeText("[" + executeTime + "]日所有PT投注数据更新完成");
					return null;
				} else {
					LogUtils.error("抓取PT投注数据失败 ： " + phpHtmlRegular, stopwatch.stop());
				}
			}else{
				writeText("PT数据采集程序正在执行，请稍后再试");
				return null;
			}
		} catch (Exception e) {
			LogUtils.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (lock != null && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		writeText("更新失败");
		return null;
	}
	
	//采集AG数据
	public void updateAgDataOnline(){
		if (StringUtils.isBlank(executeTime)) {
			writeText("请选择日期！");
			return;
		}
		
		try {
			getdateService.collectionAgData(platform,executeTime);
			writeText("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			writeText("操作失败，请联系技术解决！");
		}
	}
	
	//获取沙巴前一天数据
	public void collectionSbaData(){
		try {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			/*if(hour<13){
				writeText("提示：请您每天的下午1点后操作") ;
				return ;
			}*/
			
			getdateService.collectionSbaData();
			writeText("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			writeText("操作失败，请联系技术解决！");
		}
	}
	//采集761前一天数据
	public void collection761Data(){
		try {
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(hour<1){
				writeText("提示：请您每天的凌晨1点后操作") ;
				return ;
			}
			if (StringUtils.isBlank(executeTime)) {
				writeText("请选择日期！");
				return ;
			}
			
			getdateService.collection761Data(executeTime);
			writeText("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			writeText("操作失败，请联系技术解决！");
		}
	}


}