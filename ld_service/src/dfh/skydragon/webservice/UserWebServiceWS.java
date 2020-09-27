package dfh.skydragon.webservice;

import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import dfh.model.*;
import dfh.utils.*;
import dfh.utils.bitGame.BitGameUtil;
import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jasypt.util.text.BasicTextEncryptor;

import dfh.action.vo.ActivityCalendarVO;
import dfh.action.vo.AnnouncementVO;
import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoXimaReturnVo;
import dfh.action.vo.AutoYouHuiVo;
import dfh.action.vo.DtPotVO;
import dfh.action.vo.DtVO;
import dfh.action.vo.EbetH5VO;
import dfh.action.vo.QueryDataEuroVO;
import dfh.extend.zookeeper.GenerateNodePath;
import dfh.extend.zookeeper.ZookeeperFactoryBean;
import dfh.model.bean.Bet;
import dfh.model.bean.GiftVo;
import dfh.model.bean.ServiceStatus;
import dfh.model.bean.SuggestionVo;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.ConcertDateType;
import dfh.model.enums.DXWErrorCode;
import dfh.model.enums.GamePlatform;
import dfh.model.enums.Goddesses;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.SMSContent;
import dfh.model.enums.UserRole;
import dfh.model.notdb.FirstlyCashin;
import dfh.model.weCustomer.UserInfo;
import dfh.remote.DocumentParser;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.EBetResp;
import dfh.remote.bean.KenoResponseBean;
import dfh.remote.bean.LoginValidationBean;
import dfh.remote.bean.NTwoCheckClientResponseBean;
import dfh.remote.bean.SportBookLoginValidationBean;
import dfh.service.interfaces.ActivityCalendarService;
import dfh.service.interfaces.AgTryGameService;
import dfh.service.interfaces.AgentService;
import dfh.service.interfaces.AllMatchService;
import dfh.service.interfaces.AnnouncementService;
import dfh.service.interfaces.BankStatusService;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ICreditlogsService;
import dfh.service.interfaces.IGGameinfoService;
import dfh.service.interfaces.IGetPwdBackService;
import dfh.service.interfaces.IMemberSignrecord;
import dfh.service.interfaces.IQuerybetrankService;
import dfh.service.interfaces.ISelfYouHuiService;
import dfh.service.interfaces.IUpgradeService;
import dfh.service.interfaces.IUserLotteryService;
import dfh.service.interfaces.IUserWebService;
import dfh.service.interfaces.IUserbankinfoService;
import dfh.service.interfaces.IValidatedPayOrderServcie;
import dfh.service.interfaces.LoginTokenService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.SlaveService;
import dfh.service.interfaces.TransferService;
import dfh.skydragon.webservice.model.LoginInfo;
import dfh.skydragon.webservice.model.PtTotal;
import dfh.skydragon.webservice.model.RecordInfo;
import dfh.utils.transfer.SynchrBbinOutUtil;
import dfh.utils.transfer.SynchrKenoOutUtil;
import dfh.utils.transfer.SynchrPt8SelfUtil;
import dfh.utils.transfer.SynchrSixlotteryOutUtil;
import dfh.utils.transfer.SynchrSportInUtil;
import dfh.utils.transfer.SynchrSportOutUtil;
import edu.emory.mathcs.backport.java.util.Arrays;

public class UserWebServiceWS {
	private static Logger log = Logger.getLogger(UserWebServiceWS.class);
	private Object jcToLock = new Object();
	private Object jcFromLock = new Object();

	private IUserWebService userWebSerivce;
	private IGGameinfoService gameinfoService;
	private IUserbankinfoService userbankinfoService;
	private ProposalService proposalService;
	private AnnouncementService announcementService;
	private ICreditlogsService creditlogsService;
	private CustomerService cs;
	private SeqService seqService;
	private TransferService transferService;
	private AgTryGameService agTryGameService;
	private ISelfYouHuiService selfYouHuiService;
	private IValidatedPayOrderServcie validatedPayOrderService;
	private IMemberSignrecord memberService;
	private IUpgradeService upgradeService;
	private SlaveService slaveService;
	private AllMatchService allMatchService;
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private LoginTokenService loginTokenService;
	private IUserLotteryService userLotteryService;
	private ActivityCalendarService activityCalendarService;
	private BankStatusService bankStatusService;

	public BankStatusService getBankStatusService() {
		return bankStatusService;
	}

	public void setBankStatusService(BankStatusService bankStatusService) {
		this.bankStatusService = bankStatusService;
	}

	public IUserLotteryService getUserLotteryService() {
		return userLotteryService;
	}

	public void setUserLotteryService(IUserLotteryService userLotteryService) {
		this.userLotteryService = userLotteryService;
	}

	public void setAllMatchService(AllMatchService allMatchService) {
		this.allMatchService = allMatchService;
	}

	public void setLoginTokenService(LoginTokenService loginTokenService) {
		this.loginTokenService = loginTokenService;
	}

	/**
	 * 置顶公告
	 * 
	 * @return
	 */
	public List<AnnouncementVO> queryTopNews() {
		return announcementService.queryTopNews();
	}

	/**
	 * 新闻区
	 * 
	 * @return
	 */
	public List<AnnouncementVO> query() {
		return announcementService.query();
	}

	/**
	 * 新闻区---分页
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnnouncementVO> querybyPage(int first, int maxresult) {
		return announcementService.querybyPage(first, maxresult);
	}

	/**
	 * 图片区
	 * 
	 * @return
	 */
	public List<HdImage> queryImage() {
		Date date = new Date();
		DetachedCriteria dc = DetachedCriteria.forClass(HdImage.class);
		dc.add(Restrictions.eq("imageType", 0));
		dc.add(Restrictions.eq("imageStatus", 1));
		dc = dc.add(Restrictions.le("imageStart", date));
		dc = dc.add(Restrictions.gt("imageEnd", date));
		dc = dc.addOrder(Order.desc("createdate"));
		List<HdImage> list = this.slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return list;
		}
		return null;
	}

	public Integer queryChrismesCount() {
		DetachedCriteria dc = DetachedCriteria.forClass(ActivityHistory.class);
		dc.add(Restrictions.eq("times", -1));
		dc.add(Restrictions.eq("activityid", -1));
		dc.add(Restrictions.eq("title", "圣诞敲钟活动"));
		List<ActivityHistory> list = this.proposalService.findByCriteria(dc);
		if (list.size() < 1 || list == null) {
			return 0;
		}
		return list.size();
	}

	public String chrismesCount(String loginname) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(ActivityHistory.class);
			dc.add(Restrictions.eq("username", loginname));
			dc.add(Restrictions.eq("times", -1));
			dc.add(Restrictions.eq("activityid", -1));
			dc.add(Restrictions.eq("title", "圣诞敲钟活动"));
			List<ActivityHistory> list = this.proposalService.findByCriteria(dc);
			if (list != null && list.size() > 0) {
				return "您已报名圣诞敲钟活动";
			}
			ActivityHistory activityHistory = new ActivityHistory();
			activityHistory.setUsername(loginname);
			activityHistory.setTitle("圣诞敲钟活动");
			activityHistory.setTimes(-1);
			activityHistory.setActivityid(-1);
			activityHistory.setCreatetime(new Date());
			activityHistory.setRemark("报名圣诞敲钟,流水达到1000");
			activityHistory.setLevel(getUser(loginname).getLevel());
			this.proposalService.save(activityHistory);
			return "恭喜您参加圣诞敲钟活动已成功!!!";
		} catch (Exception e) {
			e.printStackTrace();
			return "敲钟失败";
		}

	}

	/**
	 * flsh
	 * 
	 * @return
	 */
	public HdImage queryImageFlash() {
		Date date = new Date();
		DetachedCriteria dc = DetachedCriteria.forClass(HdImage.class);
		dc.add(Restrictions.eq("imageType", 1));
		dc.add(Restrictions.eq("imageStatus", 1));
		dc = dc.add(Restrictions.le("imageStart", date));
		dc = dc.add(Restrictions.gt("imageEnd", date));
		dc = dc.addOrder(Order.desc("createdate"));
		List<HdImage> list = this.slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 返回单条新闻
	 */
	public AnnouncementVO getAnnouncement(Integer aid) {
		return this.announcementService.getAnnouncement(aid);
	}

	/**
	 * 新闻区 更多 条数
	 * 
	 * @return
	 */
	public Integer totalNewsCount() {
		return announcementService.totalCount();
	}

	/**
	 * 新闻区 更多
	 * 
	 * @param currPageNo
	 * @param rowNo
	 * @return
	 */
	public List<AnnouncementVO> findAll(Integer currPageNo, Integer rowNo) {
		return announcementService.queryAll(currPageNo, rowNo);
	}

	/**
	 * 登录
	 * 
	 * @param loginname
	 * @param password
	 * @param remoteip
	 * @param city
	 * @return
	 */
	public LoginInfo login(String loginname, String password, String remoteip, String city, String clientos,
			String ioBB) {
		return userWebSerivce.userLogin(loginname, password, remoteip, city, clientos, ioBB, false);
	}

	/**
	 * 足球 得奖 数据
	 * 
	 * @author ck
	 * @return
	 */
	public List<QueryDataEuroVO> queryDataEuro() {
		return announcementService.queryDataEuro();
	}

	/**
	 * PT登录
	 * 
	 * @param loginname
	 * @param password
	 * @param remoteip
	 * @param city
	 * @return
	 */
	public LoginInfo loginPt(String loginname, String password, String remoteip, String city, String clientos) {
		return userWebSerivce.userLogin(loginname, password, remoteip, city, "", clientos, true);
	}

	/**
	 * 查询闯关奖金余额
	 * 
	 * @param loginname
	 * @return
	 */
	public String queryEmigratedbonus(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(Emigratedbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		List<Emigratedbonus> list = this.slaveService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			return list.get(0).getMoney() + "";
		} else {
			return "0";
		}
	}

	public List<ActivityCalendarVO> queryTopActivity(Integer top) {
		return activityCalendarService.queryTopActivity(top);
	}

	/**
	 * 闯关活动报名
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String doEmigratedApply(String loginname, String type) throws Exception {
		String str = checkSystemConfigForEmigrated("type110", "否");
		if (StringUtil.isEmpty(str)) {
			return "该活动已停止";
		}
		String str1 = checkSystemConfigForEmigrated("type111", "否");
		if (StringUtil.isEmpty(str1)) {
			return "该活动已停止";
		}
		String str2 = checkSystemConfigForEmigrated("type112", "否");
		if (StringUtil.isEmpty(str2)) {
			return "该活动已停止";
		}
		String str3 = checkSystemConfigForEmigrated("type113", "否");
		if (StringUtil.isEmpty(str3)) {
			return "该活动已停止";
		}
		String str4 = checkSystemConfigForEmigrated("type114", "否");
		if (StringUtil.isEmpty(str4)) {
			return "该活动已停止";
		}
		// 查询当日存款量
		Double result = checkDepositRecord(loginname, DateUtil.getbeforeDay(0), null);
		if (null != result && result >= 100) {
			return SynchronizedUtil.getInstance().doEmigratedApply(transferService, loginname, type);
		} else {
			return "今日存款不足100元，无法报名";
		}
	}

	/**
	 * 全民闯关转入游戏
	 * 
	 * @param loginname
	 * @param remit
	 * @param signType
	 *            转入的游戏类型/ttg/pt
	 * @return
	 */
	public String transferInforEmigrated(String loginname, Double remit, String signType) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (StringUtils.isNotBlank(strLimit)) {
			return strLimit;
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		dc.add(Restrictions.eq("id", "全民闯关奖金转入游戏"));
		dc.add(Restrictions.eq("value", "1"));
		List list = slaveService.getHibernateTemplate().findByCriteria(dc);
		if (null == list || list.size() < 1) {
			return "奖金转入游戏维护中，请稍后再试";
		}
		try {
			return SynchronizedUtil.getInstance().transferInforEmigrated(selfYouHuiService, loginname, remit, signType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查该玩家某个时间段的存款量
	 * 
	 * @param loginname
	 * @return
	 */
	public Double checkDepositRecord(String loginname, String begindateStr, String enddateStr) {
		Double sumMoney = 0.0;
		DetachedCriteria dc1 = DetachedCriteria.forClass(Payorder.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.eq("type", 0));
		Date begindate = null;
		Date enddate = null;
		Date date = DateUtil.parseDateForStandard("2016-01-01 00:00:01");
		begindate = DateUtil.parseDateForStandard(begindateStr + " 00:00:01");
		if (begindate.getTime() < date.getTime()) {
			begindate = new Date();
		}
		if (!StringUtil.isEmpty(enddateStr)) {
			enddate = DateUtil.parseDateForStandard(enddateStr);
		}
		dc1.add(Restrictions.ge("createTime", begindate));
		if (null != enddate) {
			dc1.add(Restrictions.le("createTime", enddate));
		}
		dc1.setProjection(Projections.sum("money"));
		List list1 = slaveService.findByCriteria(dc1);
		Double result1 = 0.0;
		result1 = (Double) list1.get(0);
		if (null == result1) {
			result1 = 0.0;
		}
		DetachedCriteria dc2 = DetachedCriteria.forClass(Proposal.class);
		dc2.add(Restrictions.eq("loginname", loginname));
		dc2.add(Restrictions.eq("type", 502));
		dc2.add(Restrictions.eq("flag", 2));
		dc2.add(Restrictions.ge("createTime", begindate));
		if (null != enddate) {
			dc2.add(Restrictions.le("createTime", enddate));
		}
		dc2.setProjection(Projections.sum("amount"));
		List list2 = slaveService.findByCriteria(dc2);
		Double result2 = 0.0;
		result2 = (Double) list2.get(0);
		if (null == result2) {
			result2 = 0.0;
		}
		sumMoney = result1 + result2;
		return sumMoney;
	}

	/**
	 * 闯关奖金领取
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String doEmigrated(String loginname) throws Exception {
		return SynchronizedUtil.getInstance().doEmigrated(transferService, loginname);
	}

	public String getEmigratedApply(String loginname) {
		DetachedCriteria dcapply = DetachedCriteria.forClass(Emigratedapply.class);
		dcapply.add(Restrictions.eq("username", loginname));
		dcapply.add(Restrictions.le("updatetime", DateUtil.getTomorrow()));
		dcapply.add(Restrictions.ge("updatetime", DateUtil.getToday()));
		List<Emigratedapply> listpply = transferService.findByCriteria(dcapply);
		if (null != listpply && listpply.size() > 0) {
			String typeapply = listpply.get(0).getType();
			String strapply = "";
			if (typeapply.equals("1")) {
				strapply = "龙都-不屈白银!";
			} else if (typeapply.equals("2")) {
				strapply = "龙都-荣耀黄金!";
			} else if (typeapply.equals("3")) {
				strapply = "龙都-华贵铂金!";
			} else if (typeapply.equals("4")) {
				strapply = "龙都-璀璨钻石!";
			} else if (typeapply.equals("5")) {
				strapply = "龙都-最强王者!";
			}
			return "已成功报名,今日闯关等级：" + strapply;
		} else {
			return null;
		}
	}

	// 查询代理推广地址
	public List<Qrcode> queryQRcode(String loginname) {
		try {
			Users customer = (Users) cs.get(Users.class, loginname);
			String agent = customer.getAgent();
			String intro = customer.getIntro();
			String partner = customer.getPartner();
			String role = customer.getRole();
			log.info("agent:  " + agent + ",intro:  " + intro + " ,partner:  " + partner + " ,role:" + role
					+ " ,loginname:" + loginname);
			DetachedCriteria dc = DetachedCriteria.forClass(Qrcode.class);
			List<Qrcode> list = new ArrayList<Qrcode>();
			if (role != null && role.equalsIgnoreCase("MONEY_CUSTOMER")) {
				if (StringUtil.isNotEmpty(intro) && StringUtil.isNotEmpty(agent)) {
					dc = DetachedCriteria.forClass(Qrcode.class);
					dc.add(Restrictions.eq("recommendCode", intro));
					list = this.proposalService.findByCriteria(dc);
					return list;
				}

				else if (StringUtil.isNotEmpty(agent)) {

					dc.add(Restrictions.eq("agent", agent));
					list = this.proposalService.findByCriteria(dc);
					return list;
				}

				else if (StringUtil.isNotEmpty(intro)) {
					dc = DetachedCriteria.forClass(Qrcode.class);
					dc.add(Restrictions.eq("recommendCode", intro));
					list = this.proposalService.findByCriteria(dc);
					return list;
				}

				else {
					return list;
				}
			} else if (role != null && role.equalsIgnoreCase("AGENT")) {
				if (StringUtil.isNotEmpty(partner)) {
					dc = DetachedCriteria.forClass(Qrcode.class);
					dc.add(Restrictions.eq("recommendCode", partner));
					list = this.proposalService.findByCriteria(dc);
					return list;
				} else {
					return list;
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回Users
	 * 
	 * @param loginname
	 * @return
	 */
	public Users getUser(String loginname) {
		return (Users) cs.get(Users.class, loginname);
	}

	/**
	 * 添加代理
	 * 
	 * @param howToKnow
	 * @param loginname
	 * @param password
	 * @param accountName
	 * @param phone
	 * @param email
	 * @param qq
	 * @param referWebsite
	 * @param ipaddress
	 * @param city
	 * @param microchannel
	 * @return
	 */
	public String addAgent(String howToKnow, String loginname, String password, String accountName, String phone,
			String email, String qq, String referWebsite, String ipaddress, String city, String microchannel,
			String partner) {
		return cs.addAgent(howToKnow, loginname, password, accountName, phone, email, qq, referWebsite, ipaddress, city,
				microchannel, partner);
	}

	// 查询代理推广地址
	public List<String> queryAgentAddress(String loginname) {
		try {
			AgentService agservice = (AgentService) SpringFactoryHepler.getInstance("agentService");
			List<String> list = agservice.queryAgentAddress(loginname);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 代理模块统计活跃会员人数
	 * 
	 * @param agent
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Integer> getAgentSubUserInfo(String agent, String start, String end) {
		try {
			AgentService agservice = (AgentService) SpringFactoryHepler.getInstance("agentService");
			List<Integer> list = agservice.getAgentSubUserInfo(agent, DateUtil.fmtStandard(start),
					DateUtil.fmtStandard(end));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始代理模块的下线活跃会员
	 * 
	 * @param agent
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page queryInitSubUsers(String agent, String start, String end, Integer pageIndex, Integer size) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class)
					.add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()))
					.add(Restrictions.eq("agent", agent));
			// dc.add(Restrictions.eq("flag", 0));
			dc.add(Restrictions.ge("createtime", DateUtil.fmtStandard(start)));
			dc.add(Restrictions.le("createtime", DateUtil.fmtStandard(end)));
			Order o = Order.desc("createtime");
			// dc = dc.addOrder(o);
			Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
			List<Users> list = page.getPageContents();
			for (Users user : list) {
				user.setTempCreateTime(sf.format(user.getCreatetime()));
			}
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * pt登录标识
	 * 
	 * @param loginname
	 * @return
	 */
	public String updatePtFlagUser(String loginname) {
		try {
			Users user = (Users) cs.get(Users.class, loginname);
			user.setPtflag(1);
			cs.update(user);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// /**
	// * 注册
	// * @param howToKnow
	// * @param sms
	// * @param loginname
	// * @param pwd
	// * @param accountName
	// * @param aliasName
	// * @param phone
	// * @param email
	// * @param qq
	// * @param referWebsite
	// * @param ipaddress
	// * @param intro
	// * @return
	// */
	// public LoginInfo register(String howToKnow, Integer sms, String
	// loginname,
	// String pwd, String accountName, String aliasName, String phone,
	// String email, String qq, String referWebsite, String ipaddress,String
	// birthday,
	// String intro) {
	// LoginInfo info = userWebSerivce.userRegister(
	// howToKnow, sms,loginname, pwd,
	// accountName,aliasName,phone,email,
	// qq,referWebsite, ipaddress,birthday,intro);
	// if("success".equals(info.getView())){
	// Users user = (Users)cs.get(Users.class, loginname);
	// info.setUser(user);
	// }
	// return info;
	// }

	/**
	 * 注册
	 * 
	 * @param howToKnow
	 * @param sms
	 * @param loginname
	 * @param pwd
	 * @param accountName
	 * @param aliasName
	 * @param phone
	 * @param email
	 * @param qq
	 * @param referWebsite
	 * @param ipaddress
	 * @param intro
	 * @return
	 */
	public LoginInfo register(String howToKnow, Integer sms, String loginname, String pwd, String accountName,
			String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress, String city,
			String birthday, String intro, Proposal proposal, String ioBB, String agentcode, String friendcode,
			String microchannel) {
		LoginInfo info = null;
		if ("周冰".equals(accountName) || "余万城".equals(accountName)) {
			return null;
		}
		if (proposal != null) {
			String type = "";
			proposal = (Proposal) cs.get(Proposal.class, proposal.getPno());
			info = userWebSerivce.userRegisterTwo(howToKnow, sms, loginname, pwd, accountName, aliasName, phone, email,
					qq, referWebsite, ipaddress, city, birthday, intro, proposal.getGifTamount(), proposal.getAgent(),
					type, ioBB, microchannel);
			if ("success".equals(info.getView())) {
				Users user = (Users) cs.get(Users.class, loginname);
				info.setUser(user);
				proposal.setFlag(2);
				proposal.setLoginname(loginname);
				proposal.setExecuteTime(new Date());
				proposal.setQuickly(user.getLevel());
				proposal.setRemark("LONGDU->EA");
				transferService.update(proposal);
				if (user.getRole().equals("MONEY_CUSTOMER")) {
					if (!StringUtil.isEmpty(friendcode)) {
						Friendintroduce fi = new Friendintroduce();
						fi.setDownlineuser(user.getLoginname());
						fi.setCreatetime(new Date());
						fi.setToplineuser(friendcode);
						cs.save(fi);
					}
					GameAccCreateThread thread = new GameAccCreateThread(loginname, pwd);
					thread.start();
				}
			}
		} else {
			info = userWebSerivce.userRegister(howToKnow, sms, loginname, pwd, accountName, aliasName, phone, email, qq,
					referWebsite, ipaddress, city, birthday, intro, agentcode, ioBB, microchannel);
			if ("success".equals(info.getView())) {
				Users user = (Users) cs.get(Users.class, loginname);
				info.setUser(user);

				if (user.getRole().equals("MONEY_CUSTOMER")) {
					if (!StringUtil.isEmpty(friendcode)) {
						Friendintroduce fi = new Friendintroduce();
						fi.setDownlineuser(user.getLoginname());
						fi.setCreatetime(new Date());
						fi.setToplineuser(friendcode);
						cs.save(fi);
					}
					Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
					if (userstatus == null) {
						Userstatus status = new Userstatus();
						status.setLoginname(loginname);
						status.setCashinwrong(0);
						proposalService.save(status);
					}
					GameAccCreateThread thread = new GameAccCreateThread(loginname, pwd);
					thread.start();
				}
			}
		}
		return info;
	}

	/**
	 * 手机端判断CPU信息是否相同
	 * 
	 * @param loginname
	 * @param ioBB
	 * @param ip
	 * @return
	 */
	public String getCpuMobile(String loginname, String ioBB, String ip, String platform) {
		log.info(loginname + "****Mobile****" + ip + "**********" + ioBB);
		if (StringUtils.isBlank(ioBB))
			return "非法请求，请从APP访问";
		try {
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword("GJVNphq8OTJ23QUgzSysynMfswabBOQVjG38tsCWfWfcyYoNNDmGnm7DlVspUQcv");
			String newPassword = textEncryptor.decrypt(ioBB);
			log.info(loginname + "********" + newPassword);

			DetachedCriteria dc = DetachedCriteria.forClass(IESnare.class);
			dc.add(Restrictions.eq("device", newPassword));
			List<IESnare> list = transferService.findByCriteria(dc);
			if (null != list && list.size() > 0) {
				log.info("抱歉注册失败，出现重复信息注册 ！不允许注册，请使用账号找回密码 ！" + loginname);
				Actionlogs log = new Actionlogs("注册账户" + loginname, ActionLogType.MOBILE_REGISTER.getCode(),
						DateUtil.now(), "抱歉注册失败，出现重复信息注册！" + loginname + "注册:deviceID:" + newPassword + ";ip:" + ip);
				cs.save(log);
				return "您的注册信息已重复 ！不允许注册，请使用账号找回密码功能 ！";
			}

			IESnare iesanre = new IESnare(loginname, newPassword, ip, platform);
			cs.save(iesanre);
			Actionlogs log = new Actionlogs(loginname, ActionLogType.MOBILE_REGISTER.getCode(), DateUtil.now(),
					platform + " deviceID:" + newPassword + " ip:" + ip);
			cs.save(log);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return "设备检查失败";
		}
	}

	/**
	 * 判断CPU信息是否相同
	 * 
	 * @param user
	 * @param ioBB
	 * @param ip
	 * @return
	 */
	public String getCpu(String loginname, String ioBB, String ip) {
		/*
		 * if(StringUtils.isBlank(ioBB)){
		 * log.info("非法禁用浏览器的一些设置！不允许注册!危险"+loginname); return
		 * "注册失败：无法获取您当前的设备环境信息"; }
		 */
		if (getCpukg()) {
			log.info(loginname + "********" + ip + "**********" + ioBB);
			if (ioBB.equals("ioBB")) {
				Actionlogs log = new Actionlogs(loginname, "LOGIN", DateUtil.now(), "手机注册:" + ip);
				cs.save(log);
				return null;
			}
			String device = IESnareUtil.getDevice(loginname, ip, ioBB);
			log.info(device);
			if (StringUtils.isNotBlank(device)) {
				DetachedCriteria dc = DetachedCriteria.forClass(IESnare.class);
				dc.add(Restrictions.eq("device", device));
				List<IESnare> list = transferService.findByCriteria(dc);
				if (null != list && list.size() > 0) {
					log.info("抱歉注册失败，出现重复信息注册 ！不允许注册，请使用账号找回密码 ！" + loginname);
					Actionlogs log = new Actionlogs("注册账户" + loginname, "LOGIN", DateUtil.now(),
							"抱歉注册失败，出现重复信息注册！" + loginname + "注册:deviceID:" + device + ";ip:" + ip);
					cs.save(log);
					return "您的注册信息已重复 ！不允许注册，请使用账号找回密码功能 ！";
				}
				IESnare iesanre = new IESnare(loginname, device, ip);
				cs.save(iesanre);
				Actionlogs log = new Actionlogs(loginname, "LOGIN", DateUtil.now(),
						"注册:deviceID:" + device + ";ip:" + ip);
				cs.save(log);
				return null;
			} else {
				log.info("非法禁用浏览器的一些设置！不允许注册!危险" + loginname);
				return "注册失败：无法获取您当前的设备环境信息";
			}
		}
		log.info("cpu已经被关闭" + loginname);
		return null;
	}

	/**
	 * 优惠CPU验证
	 * 
	 * @param loginname
	 * @param ioBB
	 * @param ip
	 * @return
	 */
	public String getCpuForYouHui(String loginname, String ioBB, String ip) {
		if (getCpukg()) {
			log.info(loginname + "********" + ip + "******优惠：" + ioBB);
			String device = IESnareUtil.getDevice(loginname, ip, ioBB);
			log.info(device);
			if (StringUtils.isNotBlank(device)) {
				DetachedCriteria dc = DetachedCriteria.forClass(IESnare.class);
				dc.add(Restrictions.eq("device", device));
				List<IESnare> list = transferService.findByCriteria(dc);
				if (null != list && list.size() > 0) {
					if (list.size() == 1) {
						if (list.get(0).getLoginname().equals(loginname)) {
							return null;
						} else {
							return "出现重复信息，不允许领取"; //
						}
					} else if (list.size() > 1) {
						log.info("自助优惠失败，重复deviceid:" + device);
						return "提交优惠信息重复，自助失败";
					}
				}

				IESnare iesanre = new IESnare(loginname, device, ip);
				cs.save(iesanre);
				Actionlogs log = new Actionlogs(loginname, "YOUHUI", DateUtil.now(),
						"deviceID:" + device + ";ip:" + ip);
				cs.save(log);
				return null;
			} else {
				log.info("非法禁用浏览器的一些设置！不允许自助优惠!" + loginname);
				return "非法禁用浏览器的一些设置！不允许自助优惠!";
			}
		}
		log.info("cpu已经被关闭" + loginname);
		return null;
	}

	/**
	 * 获取CPU开关
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Boolean getCpukg() {
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		dc = dc.add(Restrictions.eq("id", "CPU"));
		dc = dc.add(Restrictions.eq("value", "1"));
		List<Const> list = slaveService.getHibernateTemplate().findByCriteria(dc);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 注册
	 * 
	 * @param intro
	 * @return
	 */
	public Proposal registerYqm(String intro) {
		try {
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", intro));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.eq("type", 537));
			List<Proposal> list = slaveService.findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				return null;
			}
			return list.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 修改用户密码
	 * 
	 * @param loginname
	 * @param password
	 * @param newPassword
	 * @param remoteIp
	 * @return
	 */
	public String modifyPassword(String loginname, String password, String newPassword, String remoteIp) {
		String msg = cs.modifyPassword(loginname, password, newPassword, remoteIp);
		if (null == msg) {
			try {
				String smsmsg = this.sendSMS(loginname, "3", null, SMSContent.getText("3").replace("$XXX", loginname)
						.replace("$MMM", DateUtil.formatDateForStandard(new Date())));
				log.info("玩家修改密码发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("修改密码发送短信失败：", e);
			}
			return "success";
		} else {
			return msg;
		}
	}

	public String synBbsMemberInfo(String loginname, String password) {
		cs.synBbsMemberInfo(loginname, password);
		return "success";
	}

	/**
	 * 修改用户信息
	 * 
	 * @param sms
	 * @param loginname
	 * @param aliasName
	 * @param phone
	 * @param email
	 * @param qq
	 * @param ip
	 * @return
	 */

	public String modifyCustomerInfo(Integer sms, String loginname, String aliasName, String phone, String email,
			String qq, String ip, String mailaddress, String microchannel, String birthday, String accountName) {
		String msg = cs.modifyCustomerInfo(sms, loginname, aliasName, phone, email, qq, ip, mailaddress, microchannel,
				birthday, accountName);
		if (null == msg) {
			return "success";
		} else {
			return msg;
		}
	}

	public String updateQQ(String loginname, String qq) {
		String msg = cs.updateQQ(loginname, qq);
		if (null == msg) {
			return "success";
		} else {
			return msg;
		}
	}

	public String updateWeiXin(String loginname, String microchannel) {
		String msg = cs.updateWeiXin(loginname, microchannel);
		if (null == msg) {
			return "success";
		} else {
			return msg;
		}
	}

	/**
	 * 获取kyqp路径
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String kyqpLogin(String loginname, String gameCode, String ip) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("KYQP游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return ChessUtil.MAINTAIN;
		}
		return KYQPUtil.kyqpLogin(loginname, "KYQP", gameCode, ip);
	}

	/**
	 * 获取vr路径
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String vrLogin(String loginname, String ip) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("VR游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return ChessUtil.MAINTAIN;
		}
		return KYQPUtil.kyqpLogin(loginname, "VR", ip);
	}

	/**
	 * 获取开元棋牌钱包余额
	 * 
	 * @param loginname
	 * @return
	 */
	public Double getKyqpBalance(String loginname, String platform, String ip) {
		try {
			return KYQPUtil.getBalance(loginname, platform, ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	/**
	 * 短信通知
	 * 
	 * @param loginname
	 * @param service
	 * @param ip
	 * @return
	 */
	public String chooseService(String loginname, String service, String ip) {
		String msg = cs.chooseservice(loginname, service, ip);
		if (null == msg) {
			return "success";
		} else {
			return msg;
		}
	}

	/**
	 * 检测用户是否存在
	 * 
	 * @param loginname
	 * @return
	 */
	public Boolean checkUserIsExit(String loginname) {
		return userWebSerivce.checkUserIsExit(loginname);
	}

	/**
	 * 存储登录状态，供Bbs用
	 * 
	 * @param loginname
	 * @return
	 */
	public String saveBbs(String loginname) {
		userWebSerivce.saveBbs(loginname);
		return "";
	}

	/**
	 * 注销logout
	 * 
	 * @param loginname
	 * @return
	 */
	public String logout(String loginname) {
		userWebSerivce.logout(loginname);
		return "";
	}

	@SuppressWarnings("unchecked")
	public String checkGameIsProtect(String platform) {
		if ("mg".equals(platform)) {
			platform = "mg";// mg开关为mgs
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		// Page page =
		// PageQuery.queryForPagenation(seqService.getHibernateTemplate(), dc,
		// 1, 100);
		// List<Const> list = page.getPageContents() ;

		List<Const> list = slaveService.findByCriteria(dc);

		for (Const const1 : list) {
			if (const1.getId().equals(platform)) {
				return const1.getValue();
			}
		}
		return "0";
	}

	/**
	 * 1自助反水start
	 * 
	 * @param loginname
	 * @return
	 */
	public String getXimaEndTime(String loginname, String platform) {
		// if(platform.equals("ea")){
		// return gameinfoService.getXimaEndTime(loginname);
		// }else {
		return gameinfoService.getOtherXimaEndTime(loginname, platform);
		// }
	}

	/**
	 * 1自助反水start
	 * 
	 * @param loginname
	 * @return
	 */
	public String getXimaEndPtTime(String loginname) {
		return gameinfoService.getXimaEndPtTime(loginname);
	}

	/**
	 * 2自助反水end
	 * 
	 * @param _endTime
	 * @param _startTime
	 * @param _loginname
	 * @return
	 */
	public AutoXima getAutoXimaObject(String _endTime, String _startTime, String _loginname, String platform) {
		Date etime = null;
		Date stime = null;
		try {
			etime = sf.parse(_endTime);
			stime = sf.parse(_startTime);
			if (platform.equals("ea")) {
				return gameinfoService.getAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("ag")) {
				return gameinfoService.getAgAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("agin")) {
				return gameinfoService.getAginAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("bbin")) {
				return gameinfoService.getBbinAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("kg")) {
				return gameinfoService.getKgAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("keno")) {
				return gameinfoService.getKenoAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("sb")) {
				return gameinfoService.getSbAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("pttiger")) {
				return gameinfoService.getPtTigerAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("ptother")) {
				return gameinfoService.getPtOtherAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("sixlottery")) {
				return gameinfoService.getSixLotteryAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("ebet")) {
				return gameinfoService.getEbetAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("gpi")) {
				return gameinfoService.getGPIAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("ttg")) {
				return gameinfoService.getTTGAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("qt")) {
				return gameinfoService.getQTAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("nt")) {
				return gameinfoService.getNTAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("mg")) {
				return gameinfoService.getMGAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("dt")) {
				return gameinfoService.getDTAutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("cq9")) {
				return gameinfoService.getCQ9AutoXimaObject(etime, stime, _loginname);
			} else if (platform.equals("pg")) {
				return gameinfoService.getPGAutoXimaObject(etime, stime, _loginname);
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return new AutoXima("日期转换异常，请重新输入！");
		}
	}

	/**
	 * 2自助反水end
	 * 
	 * @param _endTime
	 * @param _startTime
	 * @param _loginname
	 * @return
	 */
	public AutoXima getAutoXimaPtObject(String _loginname) {
		return gameinfoService.getAutoXimaPtObject(_loginname);
	}

	/**
	 * 检测服务状态
	 * 
	 * @return
	 */
	public List<ServiceStatus> checkServiceStatus() {
		List<ServiceStatus> result = new ArrayList<ServiceStatus>();

		Integer zkStatus = 0;
		String exceptionMsg = null;
		String lockName = "checkWebServiceStatus";
		Long zkStartMs = System.currentTimeMillis();
		Boolean lockFlag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		InterProcessMutex lock = null;

		try {

			lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(lockName));
			lockFlag = lock.acquire(Integer.parseInt(Configuration.getInstance().getValue("zk.lock.timeout")),
					TimeUnit.SECONDS);

			if (!lockFlag) {
				zkStatus = 1;
				exceptionMsg = "未获取到锁";
			}
		} catch (Exception e) {
			log.error("测试WebserviceZK获取锁发生异常，异常信息：", e);
			zkStatus = 2;
			exceptionMsg = e.getMessage();
		} finally {
			if (lockFlag) {
				try {
					if (null != lock) {
						lock.release();
					}
				} catch (Exception e) {
					log.error("测试WebserviceZK释放锁发生异常，执行时间：" + time + "，异常信息：", e);
					zkStatus = 3;
					exceptionMsg = e.getMessage();
				}
			}
		}

		Long zkEndMs = System.currentTimeMillis();
		ServiceStatus zk = new ServiceStatus();
		zk.setExceptionMsg(exceptionMsg);
		zk.setName("WebServiceZKStatus");
		zk.setExecMs(zkEndMs - zkStartMs);
		zk.setStatus(zkStatus);
		result.add(zk);

		ServiceStatus db = new ServiceStatus();
		Integer databaseStatus = 0;
		try {
			exceptionMsg = null;
			String sql = "select 1";
			this.cs.list(sql, null);
		} catch (Exception e) {
			log.error("测试Webservice发生异常：", e);
			exceptionMsg = e.getMessage();
			databaseStatus = 1;
		}

		db.setName("WebServiceDBStatus");
		db.setStatus(databaseStatus);
		db.setExceptionMsg(exceptionMsg);
		db.setExecMs(System.currentTimeMillis() - zkEndMs);
		result.add(db);

		return result;
	}

	/**
	 * 3检查是否有未审核洗码
	 * 
	 * @param loginname
	 * @return
	 */
	public AutoXimaReturnVo checkSubmitXima(String loginname, String platform) {
		return gameinfoService.checkSubmitXima(loginname, platform);
	}

	/**
	 * 3检查是否有未审核洗码
	 * 
	 * @param loginname
	 * @return
	 */
	public AutoXimaReturnVo checkSubmitPtXima(String loginname) {
		return gameinfoService.checkSubmitPtXima(loginname);
	}

	/**
	 * 4提交洗码
	 * 
	 * @param user
	 * @param _endTime
	 * @param _startTime
	 * @return
	 */
	public String execXima(Users user, String _endTime, String _startTime, String platform) {
		try {
			Date endTime = sf.parse(_endTime);
			Date startTime = sf.parse(_startTime);
			long ldate = endTime.getTime() - startTime.getTime();
			if ((ldate / 1000) < 300) {
				return "洗码相隔时间不得小于5分钟！";
			}
			// if(platform.equals("mg") || platform.equals("dt")){
			// return platform+"自助洗码维护中...";
			// }
			// return ximaMap.get(user.getLoginname()).execute(gameinfoService,
			// user, endTime, startTime, platform);
			return UserSynchronizedPool.getSynchUtilByUser(user.getLoginname()).selfXima(gameinfoService, user, endTime,
					startTime, platform);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 4提交洗码
	 * 
	 * @param user
	 * @param _endTime
	 * @param _startTime
	 * @return
	 */
	public String execXimaPt(Users user, String _endTime, String _startTime) {
		try {
			Date endTime = sf.parse(_endTime);
			Date startTime = sf.parse(_startTime);
			return gameinfoService.execXimaPt(user, endTime, startTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 老虎机自助反水获取平台信息
	 * 
	 * @param platform
	 * @param _loginname
	 * @return
	 */
	public AutoXima getAutoSlotXimaObject(String _loginname) {
		return gameinfoService.getSlotAutoXimaObject(_loginname);
	}

	/**
	 * 
	 * 
	 * @param user
	 * @param _endTime
	 * @param _startTime
	 * @return
	 */
	public String execXimaSlot(Users user, String platform) {

		String msg = "";
		Boolean lockFlag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		InterProcessMutex lock = null;
		String loginname = user.getLoginname();

		try {

			lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginname));
			lockFlag = lock.acquire(Integer.parseInt(Configuration.getInstance().getValue("zk.lock.timeout")),
					TimeUnit.SECONDS);
		} catch (Exception e) {

			e.printStackTrace();
			log.error("玩家：" + loginname + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}

		try {

			if (lockFlag) {

				log.info("正在处理玩家" + loginname + "的请求，执行时间：" + time);

				msg = UserSynchronizedPool.getSynchUtilByUser(loginname).execXimaSlot(gameinfoService, loginname);

				log.info("处理完成玩家" + loginname + "的请求，执行时间：" + time + "，结束时间：" + sdf.format(new Date()));
			} else {

				log.error("玩家：" + loginname + "未能获取锁，无法执行请求对应的方法....");

				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.error("执行玩家：" + loginname + "请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {

			if (lockFlag) {

				try {

					if (null != lock) {

						lock.release();
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("玩家：" + loginname + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}

		return msg;
	}

	/**
	 * 查询自助洗码明细1
	 * 
	 * @param loginname
	 * @param _startTime
	 * @param _endTime
	 * @return
	 */
	public AutoXima getTotalCount(String loginname, String _startTime, String _endTime) {
		try {
			Date endTime = sf.parse(_endTime);
			Date startTime = sf.parse(_startTime);
			return gameinfoService.getTotalCount(loginname, startTime, endTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询自助洗码明细2
	 * 
	 * @param loginname
	 * @param _startTime
	 * @param _endTime
	 * @param pageno
	 * @param length
	 * @return
	 */
	public List<AutoXima> searchXimaDetail(String loginname, String _startTime, String _endTime, int pageno,
			int length) {
		try {
			Date endTime = sf.parse(_endTime);
			Date startTime = sf.parse(_startTime);
			List<AutoXima> list = gameinfoService.searchXimaDetail(loginname, startTime, endTime, pageno, length);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 绑定银行卡号1
	 * 
	 * @param loginname
	 * @return
	 */
	public List<Userbankinfo> getUserbankinfoList(String loginname) {
		try {
			return userbankinfoService.getUserbankinfoList(loginname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Userbankinfo> getUserbankinfoList2(String loginname) {
		try {
			List<Userbankinfo> userbankinfoList = userbankinfoService.getUserbankinfoList(loginname);
			for (Userbankinfo userbankinfo : userbankinfoList) {
				userbankinfo.setBankno(StringUtil.bankcardsub(userbankinfo.getBankno()));
			}
			return userbankinfoList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String queryLastLoginDate(String loginname) {
		try {
			Users user = getUser(loginname);
			if (user.getLastLoginTime() != null) {
				return DateUtil.fmtDateForBetRecods(user.getLastLoginTime());
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "系统异常";
		}
	}

	/**
	 * 
	 * @param loginname
	 * @param bankname
	 * @param username
	 *            显示给玩家的账户名称
	 * @return
	 */
	public String getRemarkAgain(String loginname, String bankname, String username) {
		String depositId;
		Users user = getUser(loginname);
		if (null == user) {
			log.info("getRemarkAgain:玩家不存在");
			return null;
		}
		if (!bankname.equals("支付宝")) {
			if (!bankname.equals("工商银行") && !bankname.equals("农业银行")) { // 注意，别的产品可能不是这样
				bankname = "招商银行";
			}
		}

		Bankinfo bank = null;
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc = dc.add(Restrictions.eq("type", 1));
		dc = dc.add(Restrictions.eq("isshow", 1));
		dc = dc.add(Restrictions.eq("useable", 0));
		dc = dc.add(Restrictions.eq("bankname", bankname));
		dc = dc.add(Restrictions.eq("username", username));
		dc = dc.add(Restrictions.like("userrole", "%" + user.getLevel() + "%"));
		dc.add(Restrictions.sqlRestriction("1=1 order by rand()"));
		List<Bankinfo> list = proposalService.findByCriteria(dc);
		if (list != null && list.size() > 0 && list.get(0) != null) {
			bank = list.get(0);
		} else {
			log.info(bankname + "后台未开启（条件--》type:1,isshow:1,useable:0,userole:" + user.getLevel() + "）");
			return null;
		}

		try {
			DepositOrder depositOrder = new DepositOrder();
			depositOrder.setLoginname(loginname);
			depositOrder.setBankname(bankname);
			if (bankname.equals("农业银行")) {
				depositId = StringUtil.getRandomStrExceptOL0(4);
			} else {
				depositId = StringUtil.getRandomStrExceptOL0(5);
			}
			depositOrder.setDepositId(depositId);
			depositOrder.setCreatetime(new Date());
			depositOrder.setStatus(0);

			depositOrder.setAccountname(bank.getUsername());
			depositOrder.setBankno(bank.getBankcard());
			depositOrder.setRemark(bank.getAccountno());
			proposalService.save(depositOrder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return depositId;
	}

	/**
	 * 绑定银行卡号2
	 * 
	 * @param loginname
	 * @param bankno
	 * @param bankname
	 * @param bankaddress
	 * @param bankname
	 * @return
	 */
	public String banding(String loginname, String bankno, String bankname, String bankaddress) {
		try {
			if (bankno.equals("支付宝")) {
				Double amount = 0.00;
				DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
				proposalSavedc.add(Restrictions.eq("loginname", loginname));
				proposalSavedc.add(Restrictions.eq("flag", 2));
				proposalSavedc.add(Restrictions.eq("type", 502));
				proposalSavedc.setProjection(Projections.sum("amount"));
				List saveList = proposalService.findByCriteria(proposalSavedc);
				if (saveList != null && !saveList.isEmpty() && null != saveList.get(0)) {
					if (null != saveList.get(0)) {
						amount += (Double) saveList.get(0);
						if (amount <= 0) {
							DetachedCriteria payorderdc = DetachedCriteria.forClass(Payorder.class);
							payorderdc.add(Restrictions.eq("loginname", loginname));
							payorderdc.add(Restrictions.eq("flag", 0));
							payorderdc.add(Restrictions.eq("type", 0));
							payorderdc.setProjection(Projections.sum("money"));
							List payorderList = proposalService.findByCriteria(payorderdc);
							if (payorderList != null && !payorderList.isEmpty() && null != payorderList.get(0)) {
								if (null != payorderList.get(0)) {
									amount += (Double) payorderList.get(0);
								}
							}
						}
					}
				}

				if (amount <= 0) {
					return "建议您本次提款使用其他银行卡进行提款，存款激活之后方可使用绑定支付宝进行提款，谢谢。";
				}
			}
			log.info("banding-=-----------bankname:" + bankname);
			String bankStatus = bankStatusService.getBankStatus(bankname);

			if (StringUtils.isBlank(bankStatus)) {
				return "您输入的银行卡信息错误，请重新输入！";
			}
			this.userbankinfoService.banding(loginname, bankno, bankname, bankaddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}
		return "success";
	}

	/**
	 * 绑定银行卡号3
	 * 
	 * @param loginname
	 * @param bankname
	 * @return
	 */
	public Userbankinfo getUserbankinfo(String loginname, String bankname) {
		try {
			return this.userbankinfoService.getUserbankinfo(loginname, bankname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Userbankinfo userbankinfo = new Userbankinfo();
			userbankinfo.setFlag(-1000);// 标识为异常
			return userbankinfo;
		}
	}

	/**
	 * 申请提款
	 * 
	 * @param proposer
	 * @param loginname
	 * @param pwd
	 * @param title
	 * @param from
	 * @param money
	 * @param accountName
	 * @param accountNo
	 * @param accountType
	 * @param bank
	 * @param accountCity
	 * @param bankAddress
	 * @param email
	 * @param phone
	 * @param ip
	 *            // * @param remark
	 * @param msflag
	 * @return
	 */
	public String addCashout(String proposer, String loginname, String pwd, String title, String from, Double money,
			String accountName, String accountNo, String accountType, String bank, String accountCity,
			String bankAddress, String email, String phone, String ip, String remark, String msflag, String tktype) {

		if (money < 100) {
			return "提款最低金额不能低于100！";
		}
		if (bank.equalsIgnoreCase("支付宝") && !msflag.equals("1")) {
			return "支付宝提款请直接选择秒提";
		}
		if (bank.equalsIgnoreCase("支付宝") && msflag.equals("1")) {
			if (!(money >= 100 && money <= 1000)) {
				return "支付宝秒提金额介于100到1000之间";
			}
			String flag = checkGameIsProtect("支付宝秒提");
			if (!flag.equals("1")) {
				return "支付宝秒提维护中...";
			}
		}
		if (money < 100 && bank.equals("招商银行")) {
			return "提款100元以内的收款银行不能为招商银行。";
		}

		Users user = getUser(loginname);
		Double splitAmount = Double.valueOf(Configuration.getInstance().getValue("CashoutSplitAmount")); // 拆分基数
		String msg = UserSynchronizedPool.getSynchUtilByUser(loginname).withdraw(proposalService, proposer, loginname,
				pwd, title, from, money, accountName, accountNo, accountType, bank, accountCity, bankAddress, email,
				phone, ip, remark, msflag, user, splitAmount, tktype);
		if (msg == null) {
			try {
				String smsmsg = this.sendSMS(user.getLoginname(), "5", money, SMSContent.getText("5")
						.replace("$XXX", loginname).replace("$MMM", DateUtil.formatDateForStandard(new Date())));
				log.info("提款发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("提款发送短信失败：", e);
			}
		}
		return msg;
	}

	/**
	 * 在线支付
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorder(String billno, Double money, String loginname, String msg, String date) {
		return SynchronizedUtil.getInstance().addIPSPayorder(announcementService, billno, money, loginname, msg, date,
				"0");
	}

	/**
	 * 汇付在线支付1 ---动态生成的支付方式
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @param mechantcode
	 *            商户号
	 * @return
	 */
	public String addPayorderHf1(String orderNo, Double OrdAmt, String loginname, String msg, String mechantcode) {
		String aa = SynchronizedUtil.getInstance().execute1(announcementService, orderNo, OrdAmt, loginname, msg, "0",
				mechantcode);
		return aa;
	}

	/**
	 * IPS 补单
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorderBD(String billno, Double money, String loginname, String msg, String date) {
		return SynchronizedUtil.getInstance().addIPSPayorder(announcementService, billno, money, loginname, msg, date,
				"1");
	}

	/**
	 * 智付在线支付
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorderZf(String billno, Double money, String loginname, String msg, String date,
			String merchant_code) {
		System.out.println("ZF loginname:" + loginname);
		String msgr = null;
		if (loginname.contains("_flag_")) {
			msgr = announcementService.addPayorderDianKaZf(billno, money, loginname, msg, date, merchant_code, "0");
		} else {
			msgr = announcementService.addPayorderZf(billno, money, loginname, msg, date, merchant_code, "0");
		}

		if (msgr == null) {
			try {
				if (loginname.contains("_flag_")) {
					String[] args = loginname.split("_flag_");
					loginname = args[0];
				}
				String content = SMSContent.getText("9").replace("$XXX", loginname).replace("$MONEY", money + "");
				String smsmsg = this.sendSMS(loginname, "9", money, content);
				log.info(loginname + "支付成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayorderZf SMS ERROR:", e);
			}

		}
		return msgr;
	}

	/**
	 * 智付补单
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorderZfBD(String billno, Double money, String loginname, String msg, String date,
			String merchant_code) {
		System.out.println("ZF loginname:" + loginname);
		String msgr = null;
		if (loginname.contains("_flag_")) {
			msgr = announcementService.addPayorderDianKaZf(billno, money, loginname, msg, date, merchant_code, "1");
		} else {
			msgr = announcementService.addPayorderZf(billno, money, loginname, msg, date, merchant_code, "1");
		}

		if (msgr == null) {
			try {
				if (loginname.contains("_flag_")) {
					String[] args = loginname.split("_flag_");
					loginname = args[0];
				}
				String content = SMSContent.getText("9").replace("$XXX", loginname).replace("$MONEY", money + "");
				String smsmsg = this.sendSMS(loginname, "9", money, content);
				log.info(loginname + "支付成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayorderZfBD SMS ERROR:", e);
			}
		}
		return msgr;
	}

	/**
	 * 智付同步回调更新玩家额度
	 * 
	 * @param billno
	 *            智付账单号
	 * @param money
	 *            额度
	 * @param order_no
	 *            我们系统的订单号，包含用户名信息， 格式为：玩家账号 + '_flag_' + 产品名称 + 我们系统生成的订单号
	 * @param zforder_no
	 *            发送到智付的订单号
	 * @param merchant_code
	 *            商户号
	 */
	public String dinpaySyncUpdateCredit(String trade_no, String money, String order_no, String card_code,
			String merchant_code) {
		Double remit = Double.parseDouble(money);
		String[] tmpArr = order_no.split("_flag_");
		String loginname = tmpArr[0];
		String result = UserSynchronizedPool.getSynchUtilByUser(loginname).dinpaySyncUpdateCredit(announcementService,
				trade_no, remit, loginname, card_code, merchant_code);
		if (result == null || result.equals("此笔交易已经支付成功")) {
			return null;
		} else {
			return "请检查存款是否到帐，若未到账请联系我们的在线客服";
		}
	}

	/**
	 * 智付同步回调更新玩家额度 补单
	 * 
	 * @param billno
	 *            智付账单号
	 * @param money
	 *            额度
	 * @param order_no
	 *            我们系统的订单号，包含用户名信息， 格式为：玩家账号 + '_flag_' + 产品名称 + 我们系统生成的订单号
	 * @param zforder_no
	 *            发送到智付的订单号
	 * @param merchant_code
	 *            商户号
	 */
	public String dinpaySyncUpdateCreditBD(String trade_no, String money, String order_no, String card_code,
			String merchant_code) {
		Double remit = Double.parseDouble(money);
		String[] tmpArr = order_no.split("_flag_");
		String loginname = tmpArr[0];
		String msg = announcementService.addPayorder4DCard(trade_no, remit, loginname, card_code, merchant_code, "1");

		if (msg == null) {
			try {
				String content = this.createMoneyContent(loginname, remit);
				String smsmsg = this.sendSMS(loginname, "9", remit, content);
				log.info(loginname + "补单成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("dinpaySyncUpdateCreditBD SMS ERROR:", e);
			}
		}
		return msg;
	}

	/**
	 * 海尔在线支付
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorderHaier(String billno, Double money, String loginname, String merchant_code) {
		log.info("function-->addPayorderHaier");
		String[] args;
		if (loginname.contains("_")) {
			args = loginname.split("_");
			loginname = args[1];
		}
		return SynchronizedUtil.getInstance().addPayorderHaier(announcementService, billno, money, loginname,
				merchant_code, "0");
	}

	/**
	 * 海尔补单
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorderHaierBD(String billno, Double money, String loginname, String merchant_code) {
		log.info("function-->addPayorderHaier");
		String[] args;
		if (loginname.contains("_")) {
			args = loginname.split("_");
			loginname = args[1];
		}
		return SynchronizedUtil.getInstance().addPayorderHaier(announcementService, billno, money, loginname,
				merchant_code, "1");
	}

	/**
	 * 支付宝在线支付
	 * 
	 * @param outtradeno
	 * @param money
	 * @param loginname
	 * @param tradeno
	 * @param date
	 * @return
	 */
	public String addPayorderZfb(String outtradeno, Double money, String loginname, String tradeno, String date) {
		return SynchronizedUtil.getInstance().addPayorderZfb(announcementService, outtradeno, money, loginname, tradeno,
				date, "0");
	}

	/**
	 * 支付宝在线支付补单
	 * 
	 * @param outtradeno
	 * @param money
	 * @param loginname
	 * @param tradeno
	 * @param date
	 * @return
	 */
	public String addPayorderZfbBD(String outtradeno, Double money, String loginname, String tradeno, String date) {
		return SynchronizedUtil.getInstance().addPayorderZfb(announcementService, outtradeno, money, loginname, tradeno,
				date, "1");
	}

	/**
	 * 汇付在线支付
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorderHf(String orderNo, Double OrdAmt, String loginname, String msg) {
		String aa = HfAddPayOrderUtil.getInstance().execute(announcementService, orderNo, OrdAmt, loginname, msg, "0");
		// String aa =announcementService.addPayorderHf(orderNo, OrdAmt,
		// loginname, msg);
		return aa;
	}

	/**
	 * 汇付补单
	 * 
	 * @param billno
	 * @param money
	 * @param loginname
	 * @param msg
	 * @param date
	 * @return
	 */
	public String addPayorderHfBD(String orderNo, Double OrdAmt, String loginname, String msg) {
		String aa = HfAddPayOrderUtil.getInstance().execute(announcementService, orderNo, OrdAmt, loginname, msg, "1");
		// String aa =announcementService.addPayorderHf(orderNo, OrdAmt,
		// loginname, msg);
		if (aa == null) {
			try {
				String content = this.createMoneyContent(loginname, OrdAmt);
				String smsmsg = this.sendSMS(loginname, "9", OrdAmt, content);
				log.info(loginname + "补单成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayorderHfBD SMS ERROR:", e);
			}
		}
		return aa;
	}

	/**
	 * 查询赢点排名
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page selectWinPoint(String start, String end, Integer betTotal, String by, String order, Integer pageIndex,
			Integer size) {
		try {
			Date startTime = sf.parse(start);
			Date endTime = sf.parse(end);
			Page page = announcementService.selectWinPoint(startTime, endTime, betTotal, by, order, pageIndex, size);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在线存款记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page selectDepositRecord(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.eq("type", PayOrderFlagType.SUCESS.getCode()));
		// dc = dc.addOrder(Order.desc("createTime"));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Payorder> list = page.getPageContents();
		for (Payorder p : list) {
			p.setTempCreateTime(sf.format(p.getCreateTime()));
		}
		return page;
	}

	public Page unionList(Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Union.class);
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
		return page;
	}

	/**
	 * pt历史投注记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public List<PtProfit> getPtHistoryTurnoverList(String loginname, Integer pageIndex, Integer size) {
		Date[] dataArray = getInWeek();
		DetachedCriteria dc = DetachedCriteria.forClass(PtProfit.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(
				Restrictions.and(Restrictions.ge("starttime", dataArray[0]), Restrictions.le("endtime", dataArray[1])));
		dc = dc.addOrder(Order.desc("starttime"));
		List<PtProfit> list = proposalService.findByCriteria(dc);
		return list;
	}

	public List<PtTotal> getPtWinPointList(String ptby, String ptorder) {
		Date[] dataArray = getInDayAndMonth();
		List<PtTotal> rlist = new ArrayList();
		List<Object[]> list = null;
		Session session = null;
		try {
			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();
			StringBuffer sbf = new StringBuffer();
			sbf.append("select * from( "
					+ "	select loginname,-sum(amount) as pamount,sum(betCredit) as pbetcredit,-sum(amount)/sum(betCredit) as pabsum from ptprofit"
					+ " 	where ");
			if ("1".equals(ptby)) {
				sbf.append(" starttime=? and endtime=? ");
			} else if ("2".equals(ptby)) {
				sbf.append(" starttime>=? and endtime<=? ");
			} else {
				sbf.append(" 1=1 ");
			}
			sbf.append(" group by loginname)t where t.pamount>0 ");
			if ("1".equals(ptorder)) {
				sbf.append(" order by t.pamount desc");
			} else if ("2".equals(ptorder)) {
				sbf.append(" order by t.pbetcredit desc");
			} else {
				sbf.append(" order by t.pabsum desc");
			}
			sbf.append(" limit 10 ");
			SQLQuery sqlq = session.createSQLQuery(sbf.toString());
			if ("1".equals(ptby)) {
				list = sqlq.setTimestamp(0, dataArray[0]).setTimestamp(1, dataArray[1]).list();
			} else if ("2".equals(ptby)) {
				list = sqlq.setTimestamp(0, dataArray[2]).setTimestamp(1, dataArray[3]).list();
			} else {
				list = sqlq.list();
			}
			for (Object[] obj : list) {
				PtTotal pt = new PtTotal();
				pt.setLoginname((String) obj[0]);
				pt.setTotalProfit((Double) obj[1]);
				pt.setTotalBet((Double) obj[2]);
				pt.setWinPointAll(obj[3] == null ? 0 : (Double) obj[3]);
				rlist.add(pt);
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return rlist;
	}

	/**
	 * 存款记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page selectCashinRecords(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("type", ProposalType.CASHIN.getCode()));
		dc = dc.add(Restrictions.eq("loginname", loginname));
		// dc = dc.addOrder(Order.desc("createTime"));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Proposal> list = page.getPageContents();
		for (Proposal p : list) {
			p.setTempCreateTime(sf.format(p.getCreateTime()));
		}
		return page;
	}

	/**
	 * 存款记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page depositOrderRecord(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		// dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<DepositOrder> list = page.getPageContents();
		String str = "";
		for (DepositOrder p : list) {
			p.setTempCreateTime(sf.format(p.getCreatetime()));
			if (StringUtils.isNotBlank(p.getRemark()) && p.getRemark().contains("废弃秒存存款订单")) {
				str = p.getRemark().substring(p.getRemark().indexOf("废"), p.getRemark().length());
				p.setRemark(str);
			}
		}
		return page;
	}

	/**
	 * 提款记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page selectWithdrawReccords(String loginname, Integer pageIndex, Integer size) {
		log.error("debug========= into selectWithdrawReccords loginname: " + loginname);
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode()));
		dc = dc.add(Restrictions.eq("loginname", loginname));
		// dc = dc.addOrder(Order.desc("createTime"));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Proposal> list = page.getPageContents();
		for (Proposal p : list) {
			p.setTempCreateTime(sf.format(p.getCreateTime()));
		}
		return page;
	}

	/**
	 * 户内转账记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page selectTransferReccords(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.eq("flag", Constants.FLAG_TRUE));
		// dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Transfer> list = page.getPageContents();
		for (Transfer t : list) {
			t.setTempCreateTime(sf.format(t.getCreatetime()));
		}
		return page;
	}

	/**
	 * 优惠劵
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page selectCouponRecords(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.in("type",
				new Object[] { 531, 532, 533, 534, 535, 536, 537, 571, 572, 573, 581, 582, 583, 584, 590, 591, 592, 593,
						594, 595, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 730, 731, 732, 733, 734,
						735, 745 }));
		dc = dc.add(Restrictions.eq("flag", 2));
		// dc = dc.addOrder(Order.desc("executeTime"));
		Order o = Order.desc("executeTime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Proposal> list = page.getPageContents();
		for (Proposal t : list) {
			t.setTempCreateTime(sf.format(t.getExecuteTime()));
		}
		return page;
	}

	/**
	 * 获取银行账号
	 * 
	 * @return
	 */
	public List<Bankinfo> getBankinfoEmail(String level) {
		try {
			List<Bankinfo> banks = new ArrayList<Bankinfo>();
			// 工商银行
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc = dc.add(Restrictions.eq("bankname", "工商银行"));
			dc = dc.add(Restrictions.like("userrole", "%" + level + "%"));
			dc.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list = proposalService.findByCriteria(dc);
			if (list != null && list.size() > 0 && list.get(0) != null) {
				banks.add(list.get(0));
			}
			// 招商银行
			DetachedCriteria dc1 = DetachedCriteria.forClass(Bankinfo.class);
			dc1 = dc1.add(Restrictions.eq("type", 1));
			dc1 = dc1.add(Restrictions.eq("isshow", 1));
			dc1 = dc1.add(Restrictions.eq("useable", 0));
			dc1 = dc1.add(Restrictions.eq("bankname", "招商银行"));
			dc1 = dc1.add(Restrictions.eq("paytype", 0)); // paytype 0为网银招行
			dc1 = dc1.add(Restrictions.like("userrole", "%" + level + "%"));
			dc1.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list1 = proposalService.findByCriteria(dc1);
			if (list1 != null && list1.size() > 0 && list1.get(0) != null) {
				banks.add(list1.get(0));
			}
			// 建设银行
			DetachedCriteria dc2 = DetachedCriteria.forClass(Bankinfo.class);
			dc2 = dc2.add(Restrictions.eq("type", 1));
			dc2 = dc2.add(Restrictions.eq("isshow", 1));
			dc2 = dc2.add(Restrictions.eq("useable", 0));
			dc2 = dc2.add(Restrictions.eq("bankname", "建设银行"));
			dc2 = dc2.add(Restrictions.like("userrole", "%" + level + "%"));
			dc2.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list2 = proposalService.findByCriteria(dc2);
			if (list2 != null && list2.size() > 0 && list2.get(0) != null) {
				banks.add(list2.get(0));
			}
			// 农业银行
			DetachedCriteria dc3 = DetachedCriteria.forClass(Bankinfo.class);
			dc3 = dc3.add(Restrictions.eq("type", 1));
			dc3 = dc3.add(Restrictions.eq("isshow", 1));
			dc3 = dc3.add(Restrictions.eq("useable", 0));
			dc3 = dc3.add(Restrictions.eq("bankname", "农业银行"));
			dc3 = dc3.add(Restrictions.like("userrole", "%" + level + "%"));
			dc3.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list3 = proposalService.findByCriteria(dc3);
			if (list3 != null && list3.size() > 0 && list3.get(0) != null) {
				banks.add(list3.get(0));
			}
			// 支付宝
			DetachedCriteria dc4 = DetachedCriteria.forClass(Bankinfo.class);
			dc4 = dc4.add(Restrictions.eq("type", 1));
			dc4 = dc4.add(Restrictions.eq("isshow", 1));
			dc4 = dc4.add(Restrictions.eq("useable", 0));
			dc4 = dc4.add(Restrictions.eq("bankname", "支付宝"));
			dc4 = dc4.add(Restrictions.like("userrole", "%" + level + "%"));
			dc4.add(Restrictions.or(Restrictions.eq("zfbImgCode", ""), Restrictions.isNull("zfbImgCode")));
			dc4 = dc4.add(Restrictions.ne("scanAccount", 1));
			dc4.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list4 = proposalService.findByCriteria(dc4);
			if (list4 != null && list4.size() > 0 && list4.get(0) != null) {
				banks.add(list4.get(0));
			}
			return banks;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 优惠活动记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page selectConcessionReccords(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		dc = dc.add(Restrictions.in("type",
				new Object[] { ProposalType.LEVELPRIZE.getCode(), ProposalType.BANKTRANSFERCONS.getCode(),
						ProposalType.CONCESSIONS.getCode(), ProposalType.XIMA.getCode(), ProposalType.OFFER.getCode(),
						ProposalType.PRIZE.getCode(), ProposalType.PROFIT.getCode(), ProposalType.WEEKSENT.getCode(),
						ProposalType.SELFHELP_APP_PREFERENTIAL.getCode() }));
		// dc = dc.addOrder(Order.desc("createTime"));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Proposal> list = page.getPageContents();
		for (Proposal p : list) {
			p.setTempCreateTime(sf.format(p.getCreateTime()));
		}
		return page;
	}

	public Page selectBetProfit(String agentName, String starttime, String endtime, String platform, String loginname,
			Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		dc.add(Restrictions.eq("agent", agentName));
		dc.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("loginname"));
		dc.setProjection(pList);
		List loginnames = proposalService.findByCriteria(dc);

		DetachedCriteria dcP = DetachedCriteria.forClass(PlatformData.class);
		dcP.add(Restrictions.in("loginname", loginnames.toArray()));
		dcP.add(Restrictions.eq("starttime", DateUtil.parseDateForStandard(starttime)));
		if (StringUtils.isNotBlank(platform)) {
			dcP.add(Restrictions.eq("platform", platform));
		} else {
			dcP.add(Restrictions.ne("platform", "pttiger"));
		}

		if (StringUtils.isNotBlank(loginname)) {
			dcP.add(Restrictions.eq("loginname", loginname));
		}
		Page page = PageQuery.queryForPagenationWithStatistics(proposalService.getHibernateTemplate(), dcP, pageIndex,
				size, "bet", "profit", null, null);
		List<PlatformData> list = page.getPageContents();

		for (PlatformData p : list) {
			p.setTempStarttime(sf.format(p.getStarttime()));
			p.setTempEndtime(sf.format(p.getEndtime()));
		}
		return page;
	}

	public Page searchPtCommissions(String agent, String starttime, String endtime, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(PtCommissions.class);
		dc.add(Restrictions.eq("id.agent", agent));
		dc.add(Restrictions.gt("createTime", DateUtil.parseDateForStandard(starttime)));
		dc.add(Restrictions.lt("createTime", DateUtil.parseDateForStandard(endtime)));
		Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex, size,
				"amount", null, null, null);
		List<PtCommissions> list = page.getPageContents();

		for (PtCommissions p : list) {
			p.setTempExcuteTime(sf.format(p.getCreateTime()));
		}
		return page;
	}

	/**
	 * 自动生成转账订单号
	 * 
	 * @return
	 */
	public String generateTransferID() {
		return seqService.generateTransferID();
	}

	/**
	 * ea 户内转账 E 路发真人账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOut(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOut(seqId, loginname, remit);
		// return SynchrEaOutUtil.getInstance().transferOut(transferService,
		// seqId, loginname, remit);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferOutEA(transferService, seqId, loginname,
				remit);
	}

	/**
	 * ea 户内转账 E 路发账户转账 至 E 路发真人账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferIn(String loginname, Double remit) {
		// return "E68将于 2015/9/1号停止 EA平台 所有服务，请尚有余额的玩家尽速将余额转出，谢谢大家的支持。";
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return transferService.transferIn(seqId, loginname, remit);
			// return SynchrEaInUtil.getInstance().transferIn(transferService,
			// seqId, loginname, remit);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferInEA(transferService, seqId, loginname,
					remit);
		}
		return strLimit;
	}

	/**
	 * ea 户内转账 E 路发账户转账 至 E 路发真人账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInEa(String loginname) {
		String seqId = seqService.generateTransferID();
		return transferService.transferInEa(seqId, loginname);
	}

	/**
	 * ag 户内转账 E 路发ag平台账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutforDsp(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOutforDsp(seqId, loginname, remit);
		// return
		// SynchrAgOutUtil.getInstance().transferOutforDsp(transferService,
		// seqId, loginname, remit);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferOutforDsp(transferService, seqId, loginname,
				remit);
	}

	/**
	 * agin 户内转账 e68 agin平台账户转账 至 e68账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutforAginDsp(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOutforAginDsp(seqId, loginname,
		// remit);
		// return
		// SynchrAginOutUtil.getInstance().transferOutforAginDsp(transferService,
		// seqId, loginname, remit);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferOutforAginDsp(transferService, seqId,
				loginname, remit);
	}

	/**
	 * ag 户内转账 E 路发账户转账 至 E 路发ag平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInforDsp(String loginname, Double remit) {
		return "AG旗舰厅维护升级，暂不能转入";

		/*
		 * String strLimit=transferService.transferLimitMethod(loginname,remit);
		 * if(strLimit==null){ String seqId = seqService.generateTransferID();
		 * // return transferService.transferInforDsp(seqId, loginname, remit);
		 * // return
		 * SynchrAgInUtil.getInstance().transferInforDsp(transferService, seqId,
		 * loginname, remit); return
		 * UserSynchronizedPool.getSynchUtilByUser(loginname
		 * ).transferInforDsp(transferService, seqId, loginname, remit); }
		 * return strLimit;
		 */
	}

	/**
	 * agin 户内转账 e68账户转账 至 e68 agin平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInforAginDsp(String loginname, Double remit) {
		// return "平台转账维护升级，暂不能转入";

		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return transferService.transferInforAginDsp(seqId, loginname,
			// remit);
			// return
			// SynchrAginInUtil.getInstance().transferInforAginDsp(transferService,
			// seqId, loginname, remit);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferInforAginDsp(transferService, seqId,
					loginname, remit);
		}
		return strLimit;
	}

	/**
	 * E 路发keno平台账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @param remoteIp
	 * @return
	 */
	public String transferOutforkeno(String loginname, Double remit, String remoteIp) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOutforkeno(seqId, loginname, remit,
		// remoteIp);
		return SynchrKenoOutUtil.getInstance().transferOutforkeno(transferService, seqId, loginname, remit, remoteIp);
	}

	/**
	 * E 路发账户转账 至 E 路发keno平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @param remoteIp
	 * @return
	 */
	public String transferInforkeno(String loginname, Double remit, String remoteIp) {
		/*
		 * String strLimit=transferService.transferLimitMethod(loginname,remit);
		 * if(strLimit==null){ String seqId = seqService.generateTransferID();
		 * // return transferService.transferInforkeno(seqId, loginname, remit,
		 * remoteIp); return
		 * SynchrKenoInUtil.getInstance().transferInforkeno(transferService,
		 * seqId, loginname, remit, remoteIp); } return strLimit;
		 */
		return "游戏维护，暂不允许转账";
	}

	/**
	 * E 路发keno平台账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @param remoteIp
	 * @return
	 */
	public String transferOutforkeno2(String loginname, Double remit, String remoteIp) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOutforkeno2(seqId, loginname, remit,
		// remoteIp);
		// return
		// SynchrKeno2OutUtil.getInstance().transferOutforkeno2(transferService,
		// seqId, loginname, remit, remoteIp);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferOutforkeno2(transferService, seqId, loginname,
				remit, remoteIp);
	}

	/**
	 * 红包优惠
	 * 
	 * @return
	 */
	public String transferInforRedCoupon(String loginname, String couponType, String couponCode, String ip) {
		String strLimit = transferService.transferLimitMethod(loginname, 0.0);
		if (strLimit == null) {
			AutoYouHuiVo vo = new AutoYouHuiVo();
			if (couponType.equals("pt8")) {
				return "该类型优惠卷维护中";
			}
			String seqId = seqService.generateTransferID();
			if (couponType.equals("ttg")) {
				vo = UserSynchronizedPool.getSynchUtilByUser(loginname).transferTtgRedCoup(transferService, seqId,
						loginname, couponType, couponCode, ip);
				if (null != vo && vo.getMessage().equals("success") && null != vo.getGiftMoney()) {
				} else {
					return "转账失败：" + vo.getMessage();
				}
			} else if (couponType.equals("gpi")) {
			} else if (couponType.equals("pt")) {
				vo = UserSynchronizedPool.getSynchUtilByUser(loginname).transferPtRedCoup(transferService, seqId,
						loginname, couponType, couponCode, ip);
				if (null != vo && vo.getMessage().equals("success") && null != vo.getGiftMoney()) {
				} else {
					return "转账失败：" + vo.getMessage();
				}
			} else if (couponType.equals("qt")) {
				vo = UserSynchronizedPool.getSynchUtilByUser(loginname).transferQtRedCoup(transferService, seqId,
						loginname, couponType, couponCode, ip);
				if (null != vo && vo.getMessage().equals("success") && null != vo.getGiftMoney()) {
				} else {
					return "转账失败：" + vo.getMessage();
				}
			} else if (couponType.equals("nt")) {
				vo = UserSynchronizedPool.getSynchUtilByUser(loginname).transferNtRedCoup(transferService, seqId,
						loginname, couponType, couponCode, ip);
				if (null != vo && vo.getMessage().equals("success") && null != vo.getGiftMoney()) {
				} else {
					return "转账失败：" + vo.getMessage();
				}
			} else if (couponType.equals("mg")) {
				vo = UserSynchronizedPool.getSynchUtilByUser(loginname).transferMgRedCoup(transferService, seqId,
						loginname, couponType, couponCode, ip);
				if (null != vo && vo.getMessage().equals("success") && null != vo.getGiftMoney()) {
				} else {
					return "转账失败：" + vo.getMessage();
				}
			} else if (couponType.equals("dt")) {
				vo = UserSynchronizedPool.getSynchUtilByUser(loginname).transferDtRedCoup(transferService, seqId,
						loginname, couponType, couponCode, ip);
				if (null != vo && vo.getMessage().equals("success") && null != vo.getGiftMoney()) {
				} else {
					return "转账失败：" + vo.getMessage();
				}
			}
		}
		return strLimit;
	}

	/**
	 * 优惠劵
	 * 
	 * @return
	 */
	public String transferInforCouponSb(String loginname, Double remit, String couponCode, String ip) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			return transferService.transferInforCouponSb(seqId, loginname, remit, couponCode, ip);
		}
		return strLimit;
	}

	/**
	 * E 路发波音平台账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutforBbin(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOutforBbin(seqId, loginname, remit);
		return SynchrBbinOutUtil.getInstance().transferOutforBbin(transferService, seqId, loginname, remit);
	}

	/**
	 * E 路发账户转账 至 E 路发波音平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInforBbin(String loginname, Double remit) {
		/*
		 * String strLimit=transferService.transferLimitMethod(loginname,remit);
		 * if(strLimit==null){ String seqId = seqService.generateTransferID();
		 * // return transferService.transferInforBbin(seqId, loginname, remit);
		 * return
		 * SynchrBbinInUtil.getInstance().transferInforBbin(transferService,
		 * seqId, loginname, remit) ; } return strLimit;
		 */
		return "游戏维护中，暂不允许转入";
	}

	/**
	 * E 路发PT平台账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	/*
	 * public String transferOutforSky(String loginname, Double remit) { String
	 * seqId = seqService.generateTransferID(); return
	 * transferService.transferOutforSky(seqId, loginname, remit); }
	 */

	/**
	 * E 路发账户转账 至 E 路发PT平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	/*
	 * public String transferInforSky(String loginname, Double remit) { String
	 * strLimit=transferService.transferLimitMethod(loginname,remit);
	 * if(strLimit==null){ String seqId = seqService.generateTransferID();
	 * return transferService.transferInforSky(seqId, loginname, remit); }
	 * return strLimit; }
	 */

	/**
	 * 新PT 至 qy账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferNewPtOut(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferNewPtOut(seqId, loginname, remit);
		// return transferService.transferPtAndSelfYouHuiOut(seqId, loginname,
		// remit) ;
		// return
		// SynchrNewptOutUtil.getInstance().transferPtAndSelfYouHuiOut(transferService,
		// seqId, loginname, remit);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferPtAndSelfYouHuiOut(transferService, seqId,
				loginname, remit);
	}

	/**
	 * qy账户转账 至 新PT
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferNewPtIn(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return transferService.transferNewPtIn(seqId, loginname, remit);
			// return transferService.transferPtAndSelfYouHuiIn(seqId,
			// loginname, remit,null) ;
			// return
			// SynchrNewptInUtil.getInstance().transferPtAndSelfYouHuiIn(transferService,
			// seqId, loginname, remit ,null);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferPtAndSelfYouHuiIn(transferService, seqId,
					loginname, remit, null);
		}
		return strLimit;
	}

	/**
	 * e68账户转账 至 ttg平台
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferTtIn(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return
			// SynchrTtInUtil.getInstance().transferTtAndSelfYouHuiIn(transferService,
			// seqId, loginname, remit, null);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferTtAndSelfYouHuiIn(transferService, seqId,
					loginname, remit, null);
		}
		return strLimit;
	}

	/**
	 * ttg 至 e68账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferTtOut(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferNewPtOut(seqId, loginname, remit);
		// return transferService.transferPtAndSelfYouHuiOut(seqId, loginname,
		// remit) ;
		// return
		// SynchrTtOutUtil.getInstance().transferTtAndSelfYouHuiOut(transferService,
		// seqId, loginname, remit);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferTtAndSelfYouHuiOut(transferService, seqId,
				loginname, remit);
	}

	/**
	 * E 路发体育平台账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutforTy(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOutforTy(seqId, loginname, remit);
		return SynchrSportOutUtil.getInstance().transferOutforTy(transferService, seqId, loginname, remit);
	}

	/**
	 * E 路发账户转账 至 E 路发体育平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInforTy(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return transferService.transferInforTy(seqId, loginname, remit);
			return SynchrSportInUtil.getInstance().transferInforTy(transferService, seqId, loginname, remit);
		}
		return strLimit;
	}

	/**
	 * 登录ttg
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String loginttg(String loginname) {
		return PtUtil1.createPlayerName(loginname);
	}

	/**
	 * 登录DT
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public DtVO loginDT(String loginname, String password) {
		// 控制游戏开关
		Const ct = transferService.getConsts("DT游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return null;
		}
		return DtUtil.login(loginname, password);
	}

	/**
	 * mwg登录
	 * 
	 * @param loginname
	 * @param jumpType
	 * @return
	 * @throws Exception
	 */
	public String mwgLogin(String loginname, String gameid, Integer jumpType) throws Exception {
		// 手机端
		if (jumpType == 2) {
			return MWGUtils.oauth(loginname, jumpType, "", "");
		}
		String sql = "select remark from gamecode where type =:type and code =:code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "MWG");
		params.put("code", gameid);
		List list = slaveService.getListBysql(sql, params);
		if (list != null && list.size() > 0) {
			String merchantLogoImg = (String) list.get(0);
			return MWGUtils.oauth(loginname, jumpType, gameid, merchantLogoImg);
		}
		return null;
	}

	/**
	 * E 路发体育平台账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutforSB(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return transferService.transferOutforSB(seqId, loginname, remit);
			// return
			// SynchrSBOutUtil.getInstance().transferOutSB(transferService,
			// seqId, loginname, remit);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferOutSB(transferService, seqId, loginname,
					remit);
		}
		return strLimit;
	}

	/**
	 * E 路发账户转账 至 E 路发体育平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInforSB(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return transferService.transferInforSB(seqId, loginname, remit);
			// return SynchrSBInUtil.getInstance().transferInSB(transferService,
			// seqId, loginname, remit);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferInSB(transferService, seqId, loginname,
					remit);
		}
		return strLimit;
	}

	/**
	 * ag 自生成进入游戏 id
	 * 
	 * @param loginname
	 * @return
	 */
	public String generateLoginID(String loginname) {
		return seqService.generateLoginID(loginname);
	}

	/**
	 * keno 自生成进入游戏 id
	 * 
	 * @param loginname
	 * @return
	 */
	public String generateLoginKenoID(String loginname) {
		return seqService.generateLoginKenoID(loginname);
	}

	/**
	 * 代理模块 额度记录
	 * 
	 * @param starttime
	 * @param endtime
	 * @param loginname
	 * @param pageno
	 * @param length
	 * @return
	 */
	public RecordInfo getCreditLogList(String starttime, String endtime, String loginname, int pageno, int length) {

		try {
			RecordInfo info = new RecordInfo();
			Date _startTime = sf.parse(starttime);
			Date _endTime = sf.parse(endtime);
			info.setLength(creditlogsService.totalCreditlogs(_startTime, _endTime, loginname));
			List<Creditlogs> list = creditlogsService.searchCreditlogs(_startTime, _endTime, loginname, pageno, length);
			for (Creditlogs creditlogs : list) {
				creditlogs.setTempCreateTime(sf.format(creditlogs.getCreatetime()));
			}
			info.setDataList(list);
			return info;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 代理模块的下线提案
	 * 
	 * @param agent
	 * @param proposalType
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page searchsubuserProposal(String starttime, String endtime, String agent, Integer proposalType,
			String loginname, Integer pageIndex, Integer size) {
		Date start = null;
		Date end = null;
		if (proposalType != null && proposalType == 1000) {
			DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
			DetachedCriteria userDC = DetachedCriteria.forClass(Users.class);
			userDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("agent", agent));
			if (StringUtils.isNotEmpty(loginname)) {
				userDC.add(Restrictions.eq("loginname", loginname));
			}
			dc.add(Property.forName("loginname").in(userDC));

			try {
				if (starttime != null && !starttime.equals("")) {
					start = sf.parse(starttime);
				}
				if (endtime != null && !endtime.equals("")) {
					end = sf.parse(endtime);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if (start != null)
				dc = dc.add(Restrictions.ge("createTime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createTime", end));

			dc = dc.add(Restrictions.eq("type", PayOrderFlagType.SUCESS.getCode()));
			Order o = Order.desc("createTime");
			// dc = dc.addOrder(o);
			Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex,
					size, "money", null, null, o);
			List<Payorder> list = page.getPageContents();
			for (Payorder p : list) {
				p.setTempCreateTime(sf.format(p.getCreateTime()));
			}
			return page;
		} else if (proposalType != null && proposalType == 550) {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			if (proposalType != null) {
				dc.add(Restrictions.eq("type", proposalType));
			}
			if (agent != null && !agent.equals("")) {
				dc.add(Restrictions.eq("loginname", agent));
			}
			try {
				if (starttime != null && !starttime.equals("")) {
					start = sf.parse(starttime);
				}
				if (endtime != null && !endtime.equals("")) {
					end = sf.parse(endtime);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if (start != null)
				dc = dc.add(Restrictions.ge("createTime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createTime", end));
			dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			Order o = Order.desc("createTime");
			// dc = dc.addOrder(o);
			Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex,
					size, "amount", null, null, o);
			List<Proposal> list = page.getPageContents();
			for (Proposal p : list) {
				p.setTempCreateTime(sf.format(p.getCreateTime()));
			}
			return page;
		} else {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("agent", agent));
			if (proposalType != null) {
				dc.add(Restrictions.eq("type", proposalType));
			}
			if (loginname != null && !loginname.equals("")) {
				dc.add(Restrictions.eq("loginname", loginname));
			}
			try {
				if (starttime != null && !starttime.equals("")) {
					start = sf.parse(starttime);
				}
				if (endtime != null && !endtime.equals("")) {
					end = sf.parse(endtime);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if (start != null)
				dc = dc.add(Restrictions.ge("createTime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createTime", end));
			dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			Order o = Order.desc("createTime");
			// dc = dc.addOrder(o);
			Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex,
					size, "amount", null, null, o);
			List<Proposal> list = page.getPageContents();
			for (Proposal p : list) {
				p.setTempCreateTime(sf.format(p.getCreateTime()));
			}
			return page;
		}
	}

	/**
	 * 代理模块的平台输赢
	 * 
	 * @param agent
	 * @param proposalType
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page searchagprofit(String agent, String loginname, String platform, String startTime, String endTime,
			Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class).add(Restrictions.eq("agent", agent));
		if (loginname != null && !loginname.equals("")) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		if (!platform.equals("")) {
			dc.add(Restrictions.eq("platform", platform));
		}
		Date start = null;
		Date end = null;
		try {
			if (startTime != null && !startTime.equals("")) {
				start = sf.parse(startTime);
			}
			if (endTime != null && !endTime.equals("")) {
				end = sf.parse(endTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createTime", end));
		// System.out.println(dc.toString());
		Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex, size,
				"amount", "bettotal", null, null);
		List<AgProfit> list = page.getPageContents();
		for (AgProfit agProfit : list) {
			agProfit.setTempCreateTime(sf.format(agProfit.getCreateTime()));
		}
		return page;
	}

	/**
	 * 代理模块的下线会员
	 * 
	 * @param agent
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page querySubUsers(String agent, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class)
				.add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode())).add(Restrictions.eq("agent", agent));
		dc.add(Restrictions.eq("flag", 0));
		Page page = PageQuery.queryForPagenation(cs.getHibernateTemplate(), dc, pageIndex, size, null);
		List<Users> list = page.getPageContents();
		for (Users user : list) {
			user.setTempCreateTime(sf.format(user.getCreatetime()));
		}
		return page;
	}

	/**
	 * 获取代理
	 * 
	 * @param agent_website
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Users getUserAgent(String agent_website) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc = dc.add(Restrictions.eq("referWebsite", agent_website));
			dc = dc.add(Restrictions.eq("role", "AGENT"));
			dc = dc.add(Restrictions.eq("flag", 0));
			List<Users> list = proposalService.getHibernateTemplate().findByCriteria(dc);
			if (list != null && list.size() > 0 && null != list.get(0)) {
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 代理模块的佣金明细
	 * 
	 * @param agent
	 * @param year
	 * @param month
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page queryCommissionrecords(String agent, Integer year, Integer month, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Commissionrecords.class);
		if (StringUtils.isNotEmpty(agent)) {
			dc = dc.add(Restrictions.eq("parent", agent));
		} else {
			return null;
		}
		dc = dc.add(Restrictions.eq("id.year", year)).add(Restrictions.eq("id.month", month));
		Order o = Order.desc("ximaAmount");
		Page page = PageQuery.queryForPagenationWithStatistics(slaveService.getHibernateTemplate(), dc, pageIndex, size,
				"ximaAmount", "firstDepositAmount", "otherAmount", o);
		return page;
	}

	public Commissions getCommissions(String agent, Integer year, Integer month) {
		Commissions cmm = (Commissions) proposalService.get(Commissions.class, new CommissionsId(agent, year, month));
		return cmm;
	}

	/**
	 * EA登录验证
	 * 
	 * @param id
	 * @param userid
	 * @param pwd
	 * @return
	 */
	public String gameLoginValidation(String id, String userid, String pwd) {
		return cs.gameLoginValidation(new LoginValidationBean(id, userid, pwd));
	}

	/**
	 * 1表示成 2表示电话已经注册 3表示注册ip超限 ag试玩手机号码验证
	 * 
	 * @param userPhone
	 * @return
	 */
	public Integer agPhoneVerification(AgTryGame agTryGame) {
		Integer agTryGameCount = Integer.parseInt(Configuration.getInstance().getValue("ag_try_game_count"));
		Integer agTryCount = agTryGameService.getIpCount(agTryGame);
		if (agTryCount >= agTryGameCount) {
			return 3;
		}
		agTryGame = agTryGameService.agPhoneVerification(agTryGame);
		if (agTryGame != null) {
			return 2;
		}
		return 1;
	}

	public AgTryGame agLogin(AgTryGame agTryGame, String ip) {
		return agTryGameService.agLogin(agTryGame, ip);
	}

	/**
	 * ag试用账号保存
	 * 
	 * @return
	 */
	public AgTryGame agSave(AgTryGame agTryGame) {
		return agTryGameService.agSave(agTryGame);
	}

	/**
	 * 在线支付开关
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public List<Const> selectDepositSwitch(Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		dc = dc.add(Restrictions.in("id",
				new Object[] { "智付", "智付1", "智付2", "智付3", "智付点卡", "智付点卡1", "环迅", "汇付", "汇潮", "币付宝", "通用智付1", "通用智付2",
						"国付宝1", "智付微信", "智付微信1", "微信支付直连", "乐富微信", "新贝微信", "口袋支付宝", "汇付宝支付", "口袋微信支付", "口袋微信支付2",
						"口袋微信支付3", "海尔支付", "汇付宝微信", "聚宝支付宝", "迅联宝", "微信额度验证", "迅联宝网银", "优付支付宝", "汇潮网银", "新贝支付宝",
						"银宝支付宝", "优付微信", "千网支付宝", "口袋支付宝2", "千网微信", "迅联宝支付宝" }));
		return proposalService.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * 在线支付开关
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Const selectDeposit(String payStyle) {
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		dc = dc.add(Restrictions.eq("id", payStyle));
		List<Const> list = proposalService.getHibernateTemplate().findByCriteria(dc);
		if (null == list || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 
	 * @param constValue
	 *            支付开关是否打开 1：是 0：否 constid 支付开关的id
	 * @return
	 */
	public List<PayMerchant> selectPayMerchant(String constValue, String constid) {
		List<PayMerchant> list = null;
		Map map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer(
				"select p.* from paymerchant p,const c where p.constid=c.id and c.value='" + constValue + "' ");
		list = (List<PayMerchant>) proposalService.getList(sql.toString(), PayMerchant.class);
		return list;
	}

	/**
	 * 
	 */
	public PayMerchant selectPayMerchantById(String constid) {
		/*
		 * List<PayMerchant> list=null; Map map=new HashMap<String,String>();
		 * StringBuffer sql=new StringBuffer(
		 * "select p.* from paymerchant p,const c where p.constid=c.id and c.value='1' "
		 * ); sql.append("  and p.constid='"+constid+"'"); list=
		 * (List<PayMerchant
		 * >)proposalService.getList(sql.toString(),PayMerchant.class); return
		 * list.get(0);
		 */
		// List<PayMerchant> list=null;
		DetachedCriteria dc = DetachedCriteria.forClass(PayMerchant.class);
		dc = dc.add(Restrictions.eq("constid", constid));
		return (PayMerchant) proposalService.getHibernateTemplate().findByCriteria(dc).get(0);
	}

	// 获取智付订单号
	public String getOrderNo(String loginname) {
		System.out.println("==============获取web端传过来的用户名：" + loginname + "===========");
		return seqService.generateZhiFuOrderNoID(loginname);
	}

	// 获取支付宝订单号
	public String getZfbOrderNo() {
		return seqService.generateZfbOrderNoID();
	}

	// 获取汇付订单号
	public String getOrderHfNo(String loginname, Double OrdAmt) {
		// 获取该订单号
		String orderNo = seqService.generateHfOrderNoID();
		if (orderNo == null) {
			return null;
		}
		// 存入改订单
		String str = announcementService.addSaveOrderHf(orderNo, OrdAmt, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	// 获取海尔订单号
	public String getOrderHaierNo(String loginname, Double OrdAmt) {
		// 获取该订单号
		String orderNo = seqService.generateHaierOrderNoID(loginname);
		return orderNo;
	}

	/**
	 * 汇付自动
	 * 
	 * @param loginname
	 * @param OrdAmt
	 * @param constID
	 * @return
	 */
	public String getOrderHfNo1(String loginname, Double OrdAmt, String constID) {
		// 获取该订单号
		String orderNo = seqService.generateHfOrderNoID();
		if (orderNo == null) {
			return null;
		}
		// 存入改订单
		String str = announcementService.addSaveOrderHf(orderNo, OrdAmt, loginname, constID);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	// sb优惠码
	public String getBbCode(String loginname) {
		String pno = seqService.generateProposalPno(ProposalType.BIRTHDAY);
		Proposal proposal = new Proposal(pno, loginname, DateUtil.now(), 581, null, null, 0.0, null,
				ProposalFlagType.AUDITED.getCode(), Constants.FROM_FRONT, null, null);
		proposal.setLoginname(loginname);
		proposal.setBetMultiples("1");
		proposal.setShippinginfo(loginname + "注册系统自动派发188体育优惠劵");
		proposal.setGifTamount(28.00);
		String str = "SC";
		String sqlCouponId = seqService.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
		proposalService.save(proposal);
		return proposal.getShippingCode();
	}

	/**
	 * 获取邮件未读数量
	 * 
	 * @param loginname
	 * @return
	 */
	public Integer getGuestbookCount(String loginname) {
		return proposalService.getGuestbookCount(loginname);
	}

	/**
	 * 获取全部邮件数量
	 * 
	 * @param loginname
	 * @return
	 */
	public Integer getGuestbookCountAll(String loginname) {
		return proposalService.getGuestbookCountAll(loginname);
	}

	/**
	 * 获取邮件
	 * 
	 * @param loginname
	 * @return
	 */
	public Guestbook getGuestbook(Integer id) {
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		dc = dc.add(Restrictions.eq("id", id));
		Guestbook guestbook = (Guestbook) proposalService.getHibernateTemplate().findByCriteria(dc).get(0);
		if (guestbook != null) {
			guestbook.setUserstatus(1);
			proposalService.update(guestbook);
		}
		return guestbook;
	}

	public Guestbook saveGuestbook(String loginname, Integer id, String content, String ip) {
		DetachedCriteria dc = DetachedCriteria.forClass(Guestbook.class);
		dc = dc.add(Restrictions.eq("id", id));
		Guestbook guestbook = (Guestbook) proposalService.getHibernateTemplate().findByCriteria(dc).get(0);
		if (guestbook != null) {
			Guestbook book = new Guestbook();
			book.setUsername(loginname);
			book.setReferenceId(guestbook.getId());
			book.setFlag(0);
			book.setIpaddress(ip);
			book.setCreatedate(sf.format(new Date()));
			book.setAdminname("客服管理员");
			book.setIsadmin(1);
			book.setTitle(guestbook.getTitle());
			book.setContent(content);
			book.setAdminstatus(0);
			book.setUserstatus(1);
			book.setUpdateid(guestbook.getUpdateid());
			proposalService.save(book);
			guestbook.setAdminstatus(0);
			guestbook.setUserstatus(1);
			if (guestbook.getRcount() != null) {
				guestbook.setRcount(guestbook.getRcount() + 1);
			} else {
				guestbook.setRcount(1);
			}
			proposalService.update(guestbook);
		}
		return guestbook;
	}

	public Guestbook saveBookDate(String loginname, Integer id, String title, String content, String ip) {
		String updateId = seqService.generateZlxID();
		Guestbook book = new Guestbook();
		book.setUsername(loginname);
		book.setReferenceId(null);
		book.setFlag(0);
		book.setIpaddress(ip);
		book.setCreatedate(sf.format(new Date()));
		book.setAdminname("客服管理员");
		book.setIsadmin(1);
		book.setTitle(title);
		book.setContent(content);
		book.setAdminstatus(0);
		book.setUserstatus(1);
		book.setUpdateid(updateId);
		proposalService.save(book);
		return book;
	}

	/**
	 * 获取全部邮件
	 * 
	 * @param loginname
	 * @return
	 */
	public List<Guestbook> getGuestbookList(String loginname, Integer page, Integer count, Integer userstatus,
			Integer referenceId) {
		List<Guestbook> list = proposalService.getGuestbookList(loginname, page, count, userstatus, referenceId);
		return list;
	}

	/**
	 * 获取全部邮件
	 * 
	 * @param loginname
	 * @return
	 */
	public Integer getGuestbookListCount(String loginname, Integer userstatus) {
		return proposalService.getGuestbookListCount(loginname, userstatus);
	}

	public void setUserWebSerivce(IUserWebService userWebSerivce) {
		this.userWebSerivce = userWebSerivce;
	}

	public void setCs(CustomerService cs) {
		this.cs = cs;
	}

	public void setGameinfoService(IGGameinfoService gameinfoService) {
		this.gameinfoService = gameinfoService;
	}

	public void setUserbankinfoService(IUserbankinfoService userbankinfoService) {
		this.userbankinfoService = userbankinfoService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}

	public void setSeqService(SeqService seqService) {
		this.seqService = seqService;
	}

	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}

	public void setCreditlogsService(ICreditlogsService creditlogsService) {
		this.creditlogsService = creditlogsService;
	}

	public AgTryGameService getAgTryGameService() {
		return agTryGameService;
	}

	public void setAgTryGameService(AgTryGameService agTryGameService) {
		this.agTryGameService = agTryGameService;
	}

	public ISelfYouHuiService getSelfYouHuiService() {
		return selfYouHuiService;
	}

	public void setSelfYouHuiService(ISelfYouHuiService selfYouHuiService) {
		this.selfYouHuiService = selfYouHuiService;
	}

	public IValidatedPayOrderServcie getValidatedPayOrderService() {
		return validatedPayOrderService;
	}

	public void setValidatedPayOrderService(IValidatedPayOrderServcie validatedPayOrderService) {
		this.validatedPayOrderService = validatedPayOrderService;
	}

	/**
	 * 获取当周前后日期
	 * 
	 * @return
	 */
	private static Date[] getInWeek() {
		Calendar cl = new GregorianCalendar();
		cl.setTime(DateUtil.now());
		cl.setFirstDayOfWeek(Calendar.MONDAY);
		Date[] date = new Date[7];
		for (int j = 0; j < 7; j++) {
			if (j == 0) {
				cl.set(Calendar.HOUR_OF_DAY, 12);
				cl.set(Calendar.MINUTE, 0);
				cl.set(Calendar.SECOND, 0);
			} else if (j == 6) {
				cl.set(Calendar.HOUR_OF_DAY, 12);
				cl.set(Calendar.MINUTE, 0);
				cl.set(Calendar.SECOND, 0);
			}
			cl.set(Calendar.DAY_OF_WEEK, cl.getFirstDayOfWeek() + j);
			date[j] = cl.getTime();
		}
		Date[] res = new Date[2];
		res[0] = date[0];
		res[1] = date[6];
		return res;
	}

	/**
	 * 获取当前月前后日期 0/1 当天 2/3当月
	 */
	private static Date[] getInDayAndMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		Date[] res = new Date[4];
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.now());
		Integer hi = Integer.parseInt(sdf.format(c.getTime()));
		if (hi < 12) {
			c.add(Calendar.DAY_OF_MONTH, -1);
			c.set(Calendar.HOUR_OF_DAY, 12);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			res[0] = c.getTime();

			c.add(Calendar.DAY_OF_MONTH, 1);
			res[1] = c.getTime();

			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			res[2] = c.getTime();

			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			res[3] = c.getTime();
		} else {
			c.set(Calendar.HOUR_OF_DAY, 12);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			res[0] = c.getTime();

			c.add(Calendar.DAY_OF_MONTH, 1);
			res[1] = c.getTime();

			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			res[2] = c.getTime();

			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			res[3] = c.getTime();
		}
		return res;
	}

	// 获取游戏ea金额
	public String getEaGameMoney(String loginname) {
		try {
			Object output = (loginname != null ? RemoteCaller.queryCredit(loginname) : "");
			return output == null ? "系统繁忙" : output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 获取游戏ag金额
	public String getAgGameMoney(String loginname) {
		try {
			Object output = (loginname != null ? RemoteCaller.queryDspCredit(loginname) : "");
			return output == null ? "系统繁忙" : output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 获取游戏agin金额
	public String getAginGameMoney(String loginname) {
		try {
			Object output = (loginname != null ? RemoteCaller.queryDspAginCredit(loginname) : "");
			return output == null ? "系统繁忙" : output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 获取游戏agin金额
	public String getBbinGameMoney(String loginname) {
		try {
			Object output = (loginname != null ? RemoteCaller.queryBbinCredit(loginname) : "");
			return output == null ? "系统繁忙" : output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 获取游戏keno金额
	public String getKenoGameMoney(String loginname) {
		try {
			KenoResponseBean bean = DocumentParser.parseKenocheckcreditResponseRequest(KenoUtil.checkcredit(loginname));
			if (bean != null) {
				if (bean.getName() != null && bean.getName().equals("Credit")) {
					return String.valueOf(bean.getAmount());
				} else if (bean.getName() != null && bean.getName().equals("Error")) {
					return bean.getValue();
				}
			}
			return "系统繁忙";
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 获取游戏keno2金额
	public String getKeno2GameMoney(String loginname) {
		try {
			KenoResponseBean bean = DocumentParser
					.parseKenocheckcreditResponseRequest(Keno2Util.checkcredit(loginname));
			if (bean != null) {
				if (bean.getName() != null && bean.getName().equals("Credit")) {
					return String.valueOf(bean.getAmount());
				} else if (bean.getName() != null && bean.getName().equals("Error")) {
					return bean.getValue();
				}
			}
			return "系统繁忙";
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 获取游戏体育金额
	public String getSbGameMoney(String loginname) {
		try {
			Object output = (loginname != null ? RemoteCaller.querySBCredit(loginname) : "");
			return output == null ? "系统繁忙" : output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 获取mwg（大满贯）金额
	public String getMwgGameMoney(String loginname) {
		log.info("function-->getMwgGameMoney");
		try {
			Object output = (loginname != null ? MWGUtils.getUserBalance(loginname) : null);
			return output == null ? "系统繁忙" : output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// ag登录验证
	public static String agIsCustomerExist(String loginname) {
		return APUtils.isCustomerExist(loginname);
	}

	// 获取沙巴体育金额
	public String getSbaGameMoney(String loginname) {
		log.info("function-->getSbaGameMoney");
		try {
			Object output = (loginname != null ? ShaBaUtils.CheckUserBalance(loginname) : null);
			return output == null ? "系统繁忙" : output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// ag创建用户
	public static String agCheckOrCreateGameAccount(String loginname, String password) {
		return APUtils.CheckOrCreateGameAccount(loginname, password);
	}

	// agin登录验证
	public static String aginIsCustomerExist(String loginname) {
		return APInUtils.isCustomerExist(loginname);
	}

	// agin创建用户
	public static String aginCheckOrCreateGameAccount(String loginname, String password) {
		return APInUtils.CheckOrCreateGameAccount(loginname, password);
	}

	// BBin登录验证
	public static String bbinCheckOrCreateGameAccount(String loginname) {
		// return BBinUtils.CheckOrCreateGameAccount(loginname);
		return null;
	}

	// BBin登录验证
	public static String bbinForwardGame(String loginname) {
		// return BBinUtils.bbinForwardGame(loginname);
		return null;
	}

	// keno登录验证
	public static String kenoCheckOrCreateGameAccount(String PlayerId, String PlayerRealName, String PlayerIP,
			String VendorRef, String Remarks) {
		return KenoUtil.login(PlayerId, PlayerRealName, PlayerIP, VendorRef, Remarks);
	}

	// keno2登录验证
	public static String keno2CheckOrCreateGameAccount(String PlayerId, String PlayerRealName, String PlayerIP,
			String VendorRef, String Remarks) {
		return Keno2Util.login(PlayerId, PlayerRealName, PlayerIP, VendorRef, Remarks);
	}

	public String agTryLogin(String loginname, String password, String gameType, String domain) {
		String loginid = seqService.generateLoginID(loginname);
		String tranferid = seqService.generateTransferID();
		return APTRYUtils.agTryLogin(loginname, password, loginid, tranferid, gameType, domain);
	}

	public String sixLotteryLogin(String loginname, String password) {
		String userflag = SixLotteryUtil.userIsExist(loginname);
		if (userflag.equals("N")) { // 未创建六合彩玩家，进行创建
			String result = SixLotteryUtil.register(loginname, password, loginname);
			String flag = SixLotteryUtil.compileVerifyData("<status>(.*?)</status>", result);
			if (flag.equals("0")) {
				log.info(loginname + "：创建sixlottery成功");
			} else {
				log.info(loginname + "：创建sixlottery失败--"
						+ SixLotteryUtil.compileVerifyData("<error>(.*?)</error>", result));
			}
		}
		String loginResult = SixLotteryUtil.login(loginname, password);
		String loginF = SixLotteryUtil.compileVerifyData("<status>(.*?)</status>", loginResult);
		if (loginF.equals("0")) {
			return SixLotteryUtil.compileVerifyData("<url>(.*?)</url>", loginResult);
		} else {
			return SixLotteryUtil.compileVerifyData("<error>(.*?)</error>", loginResult);
		}
	}

	/**
	 * 修改银行卡的支付密码
	 *
	 * @param loginname
	 * @param password
	 * @param newPassword
	 * @param remoteIp
	 * @return
	 */
	public String modifyPayPassword(String loginname, String password, String newPassword, int isPassWord) {
		String msg = cs.modifyPayPassword(loginname, password, newPassword, isPassWord, proposalService);
		if (null == msg) {
			return "success";
		}
		return msg;
		// if (null == msg) {
		// try {
		// String smsmsg = this.sendSMS(loginname, "3", null,
		// SMSContent.getText("3").replace("$XXX", loginname).replace("$MMM",
		// DateUtil.formatDateForStandard(new Date())));
		// log.info("玩家修改密码发送短信：" + smsmsg);
		// } catch (Exception e) {
		// log.error("修改密码发送短信失败：", e);
		// }
		// return "success";
		// } else {
		// return msg;
		// }
	}

	public String transferInSixLottery(String loginname, Double remit, String remoteIp) {
		/*
		 * String seqId = seqService.generateTransferID(); // return
		 * transferService.transferInforSixlottery(seqId, loginname, remit,
		 * remoteIp) ; return
		 * SynchrSixlotteryInUtil.getInstance().transferInforSixlottery
		 * (transferService, seqId, loginname, remit, remoteIp);
		 */
		return "游戏维护，暂不允许转账";
	}

	public String transferOutSixLottery(String loginname, Double remit, String remoteIp) {
		String seqId = seqService.generateTransferID();
		// return transferService.transferOutforSixLottery(seqId, loginname,
		// remit, remoteIp) ;
		return SynchrSixlotteryOutUtil.getInstance().transferOutforSixLottery(transferService, seqId, loginname, remit,
				remoteIp);
	}

	public String getSixLotteryGameMoney(String loginname) {
		String balanceStr = SixLotteryUtil.balance(loginname);
		String flag = SixLotteryUtil.compileVerifyData("<status>(.*?)</status>", balanceStr);
		if (flag.equals("0")) {
			return SixLotteryUtil.compileVerifyData("<balance>(.*?)</balance>", balanceStr);
		} else if (flag.equals("1")) {
			return SixLotteryUtil.compileVerifyData("<error>(.*?)</error>", balanceStr);
		} else {
			log.info("getSixLotteryGameMoney error-->" + balanceStr);
			return null;
		}
	}

	/**
	 * 获取额度验证存款银行卡信息
	 * 
	 * @param level
	 * @return
	 */
	public Bankinfo getValidateDepositBankInfo(String level) {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc = dc.add(Restrictions.eq("type", 9));
		dc = dc.add(Restrictions.eq("isshow", 1));
		dc = dc.add(Restrictions.eq("useable", 0));
		dc = dc.add(Restrictions.like("userrole", "%" + level + "%"));
		// dc.add(Restrictions.sqlRestriction("1=1 order by rand()"));
		List<Bankinfo> list = proposalService.findByCriteria(dc);
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 生成额度验证存款订单
	 * 
	 * @param payOrder
	 * @return
	 */
	public PayOrderValidation createValidatedPayOrder(PayOrderValidation payOrder) {
		log.info("生成额度验证存款订单. 玩家：" + payOrder.getUserName() + ", 金额：" + payOrder.getOriginalAmount());
		// 判断该用户是否有未支付的订单， 如果存在三笔以上，不允许生成新订单
		Integer unPayedCount = validatedPayOrderService.getUnPayedOrderCount(payOrder.getUserName());
		if (unPayedCount != null && unPayedCount >= 3) {
			log.info("额度验证存款订单创建失败. 玩家：" + payOrder.getUserName() + "24小时内存在三笔或以上的未支付订单");
			payOrder.setCode("0");
			return payOrder;
		}
		PayOrderValidation order = ValidatedPyaOrderUtil.getInstance().createPayOrder(payOrder,
				validatedPayOrderService);
		log.info("额度验证存款订单创建完毕. 玩家：" + order.getUserName() + ", 验证金额：" + order.getAmount());
		return order;
	}

	@SuppressWarnings("unchecked")
	public AutoYouHuiVo getSelfYouHuiObject(String loginname, Integer youhuiType, Double remit, String ioBB,
			String ip) {
		AutoYouHuiVo vo = new AutoYouHuiVo();
		Set<String> youhuiMoenySet = new HashSet<String>(
				Arrays.asList(new String[] { "704", "705", "706", "709", "712", "732", "735" }));
		if (youhuiMoenySet.contains(youhuiType + "")) {
			DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
			dc = dc.add(Restrictions.eq("typeNo", "type201"));
			dc = dc.add(Restrictions.eq("itemNo", "001"));
			dc = dc.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> list = slaveService.findByCriteria(dc);
			if (null != list && list.size() > 0) {
				String str = list.get(0).getValue();
				if (StringUtils.isNotBlank(str)) {
					Double minValue = Double.parseDouble(str);
					if (remit < minValue) {
						vo.setMessage("限时存送优惠必须存款" + minValue + "以上！");
						return vo;
					}
				}
			}
		}
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (StringUtils.isNotBlank(strLimit)) {
			vo.setMessage(strLimit);
			return vo;
		}

		/*
		 * if(!mapYouhui.containsKey(loginname)){ mapYouhui.put(loginname, new
		 * SelfYouhuiUtil()) ; } vo =
		 * mapYouhui.get(loginname).execute(selfYouHuiService, loginname,
		 * youhuiType, remit);
		 */
		/**
		 * 迁移到用户同步池 2016-03-01
		 */
		vo = UserSynchronizedPool.getSynchUtilByUser(loginname).transferInBonus(selfYouHuiService, loginname,
				youhuiType, remit);
		if (vo.getMessage().contains("优惠成功")) {
			// ---------派发上层奖励
			// proposalService.addFriendbonus(loginname, remit,
			// vo.getMessage());
		}
		return vo;
	}

	public String haveSameInfo(String loginname) {
		Users user = getUser(loginname);
		if (null == user) {
			return "玩家不存在";
		}
		boolean flag = cs.wetherHaveSameInfo(user);
		if (flag) {
			return "您的注册资讯重复，无法申请体验金，还请参考我们其他的优惠活动哦～。";
		} else {
			// 判断该玩家是否使用过8元pt优惠
			boolean useFlag = cs.isUsePt8YouHui(loginname, user.getAccountName(), user.getRegisterIp());
			if (useFlag) {
				return "您已经使用过体验金或信息重复";
			} else {
				return null;
			}
		}
	}

	public Integer getCardNums(String loginname) {
		List<String> cards = cs.getCardNums(loginname);
		return cards.size();
	}

	public Boolean haveRepeatCards(String loginname) {
		return cs.repeatCards(loginname);
	}

	public String commitPT8Self(String loginname, String ioBB, String ip, String platform) {
		/*
		 * if(StringUtils.isBlank(ioBB)){
		 * log.info("非法禁用浏览器的一些设置！不允许注册!危险"+loginname); return
		 * "请安装flash或将flash更新至最新版本"; }
		 */
		Users user = getUser(loginname);
		if (null == user) {
			return "玩家不存在";
		}
		YouHuiConfig config = selfYouHuiService.queryYouHuiConfigSingle(ProposalType.SELFPT8.getText(),
				user.getLevel());
		// 日限额
		String strLimit = transferService.transferLimitMethod(loginname, config.getAmount());
		if (StringUtils.isNotBlank(strLimit)) {
			return strLimit;
		}

		boolean flag = cs.wetherHaveSameInfo(user);
		if (flag) {
			return "您的注册资讯重复，无法申请体验金，还请参考我们其他的优惠活动哦～。";
		} else {
			// cpu验证
			// 如果玩家同姓名或者同ip以前领取过体验金，则不在验证CPU
			boolean useFlag = cs.isUsePt8YouHuiNoTime(loginname, user.getAccountName(), user.getRegisterIp());
			if (!useFlag) {
				/*
				 * String cpu = getCpuForYouHui(loginname, ioBB, ip); if (cpu !=
				 * null) { return cpu; }
				 */
			}
			Integer size = getCardNums(loginname);
			if (size == 0) {
				return "请绑定您的银行卡";
			}
			if (haveRepeatCards(loginname)) {
				return "重复银行卡信息";
			}
			// 8元优惠
			String result = SynchrPt8SelfUtil.getInstance().selfYouHui8Yuan(selfYouHuiService, loginname, platform);
			return result;
		}
	}

	public String saveOrUpdateSBToken(String prefix, String loginname) {
		// 登录的时候创建下账号
		ShaBaUtils.CreateMember(loginname);
		ShaBaUtils.SetMemberBetSetting(loginname);
		String token = null;
		try {
			Users user = getUser(loginname);
			if (null == user) {
				return "玩家不存在";
			}
			token = prefix + RandomStringUtils.randomAlphanumeric(47);
			OnlineToken onlineToken = new OnlineToken();
			onlineToken.setLoginname(prefix + loginname);
			onlineToken.setCreatetime(new Date());
			onlineToken.setToken(token);
			cs.saveOrUpdate(onlineToken);
		} catch (Exception e) {
			e.printStackTrace();
			token = null;
		}

		return token;
	}

	public String getIDByToken(String token) {
		if (StringUtils.isNotBlank(token)) {
			DetachedCriteria dc = DetachedCriteria.forClass(OnlineToken.class);
			dc.add(Restrictions.eq("token", token));
			List<OnlineToken> tokens = cs.findByCriteria(dc);
			if (null == tokens || tokens.size() != 1) {
				return null;
			} else {
				return tokens.get(0).getLoginname();
			}
		}
		return null;
	}

	public String saveOrUpdateToken(String loginname) {
		String token = null;
		try {
			Users user = getUser(loginname);
			if (null == user) {
				return "玩家不存在";
			}
			token = RandomStringUtils.randomAlphanumeric(60);
			DetachedCriteria dc = DetachedCriteria.forClass(OnlineToken.class);
			OnlineToken onlineToken = new OnlineToken();
			onlineToken.setLoginname(loginname);
			onlineToken.setCreatetime(new Date());
			onlineToken.setToken(token);
			cs.saveOrUpdate(onlineToken);
		} catch (Exception e) {
			e.printStackTrace();
			token = null;
		}

		return token;
	}

	public OnlineToken getToken(String token) {
		if (StringUtils.isNotBlank(token)) {
			DetachedCriteria dc = DetachedCriteria.forClass(OnlineToken.class);
			dc.add(Restrictions.eq("token", token));
			List<OnlineToken> tokens = cs.findByCriteria(dc);
			if (null == tokens || tokens.size() != 1) {
				return null;
			} else {
				return tokens.get(0);
			}
		}
		return null;
	}

	public List<YouHuiConfig> getYouHuiConfig(Integer level) {
		return selfYouHuiService.queryYouHuiConfig(level);
	}

	public IMemberSignrecord getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberSignrecord memberService) {
		this.memberService = memberService;
	}

	public IUpgradeService getUpgradeService() {
		return upgradeService;
	}

	public void setUpgradeService(IUpgradeService upgradeService) {
		this.upgradeService = upgradeService;
	}

	private IGetPwdBackService getPwdService;

	public IGetPwdBackService getGetPwdService() {
		return getPwdService;
	}

	public void setGetPwdService(IGetPwdBackService getPwdService) {
		this.getPwdService = getPwdService;
	}

	/**
	 * 根据邮箱获取密码
	 * 
	 * @param name
	 * @param yxdz
	 * @return
	 */
	public String getPwdByYx(String name, String yxdz) {
		String msg = getPwdService.isUserByYx(name, yxdz);
		return msg;
	}

	/**
	 * 根据短信获取密码
	 * 
	 * @param name
	 * @param yxdz
	 * @return
	 */
	public String getPwdByDx(String name, String phone, String pwd) {
		String msg = getPwdService.isUserByDh(name, phone, pwd);
		return msg;
	}

	/**
	 * 注册时的数据验证 用户名 手机号 邮箱是否重复
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String sjyz(String key, String value) {
		String str = "";
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		if (key.equals("yhm")) {
			dc.add(Restrictions.eq("loginname", value));
			List<Users> tokens = cs.findByCriteria(dc);
			if (null != tokens && tokens.size() > 0) {
				return "该用户名已存在，请修改！";
			} else {
				return "1";
			}
		} else if (key.equals("sjh")) {
			dc.add(Restrictions.eq("phone", value));
			dc.add(Restrictions.ne("role", "AGENT"));
			List<Users> tokens = cs.findByCriteria(dc);
			if (null != tokens && tokens.size() > 0) {
				return "该手机号已存在，请修改！";
			} else {
				return "1";
			}
		} else if (key.equals("yx")) {
			dc.add(Restrictions.eq("email", value));
			dc.add(Restrictions.ne("role", "AGENT"));
			List<Users> tokens = cs.findByCriteria(dc);
			if (null != tokens && tokens.size() > 0) {
				return "该邮箱已存在，请修改！";
			} else {
				return "1";
			}
		}

		return str;
	}

	/**
	 * 注册唯一性验证
	 * 
	 * @return
	 */
	public String checkUnique4Register(String account, String phone, String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", account);
		params.put("phone", phone);
		params.put("email", email);

		String chkSql = "select count(*) from users where loginname=:loginname";
		if (cs.getCount(chkSql, params) > 0) {
			return "该用户名已存在，请修改";
		}
		params.put("agent", "AGENT");
		chkSql = "select count(*) from users where phone=:phone and role<>:agent";
		if (cs.getCount(chkSql, params) > 0) {
			return "该手机号已存在，请修改";
		}
		chkSql = "select count(*) from users where email=:email and role<>:agent";
		if (cs.getCount(chkSql, params) > 0) {
			return "该邮箱已存在，请修改";
		}
		return null;
	}

	/**
	 * 周周回馈记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page weekSentReccords(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(WeekSent.class);
		dc = dc.add(Restrictions.eq("username", loginname));
		// dc = dc.addOrder(Order.desc("createTime"));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<WeekSent> list = page.getPageContents();
		for (WeekSent ws : list) {
			ws.setTempCreateTime(sf.format(ws.getCreateTime()));
		}
		return page;
	}

	/**
	 * 领取周周回馈
	 * 
	 * @param pno
	 *            提案号
	 * @param targetFlag
	 *            目标状态
	 * @return
	 */
	public String optWeekSent(String pno, Integer targetFlag, String ip, String target) {
		return SynchronizedUtil.getInstance().optWeekSent(proposalService, pno, targetFlag, ip, target);
	}

	/**
	 * PT老虎机负盈利反赠记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page losePromoReccords(String loginname, Integer pageIndex, Integer size) {
		/*
		 * DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class); dc =
		 * dc.add(Restrictions.eq("loginname", loginname)); dc =
		 * dc.add(Restrictions.eq("type", ProposalType.PROFIT.getCode())); dc =
		 * dc.addOrder(Order.desc("createTime")); Page page =
		 * PageQuery.queryForPagenation(proposalService.getHibernateTemplate(),
		 * dc, pageIndex, size); List<Proposal> list = page.getPageContents();
		 * for (Proposal p : list) {
		 * p.setTempCreateTime(sf.format(p.getCreateTime())); }
		 */
		DetachedCriteria dc = DetachedCriteria.forClass(LosePromo.class);
		dc = dc.add(Restrictions.eq("username", loginname));
		dc = dc.add(
				Restrictions.in("platform", new String[] { "pttiger", "ttg", "gpi", "nt", "qt", "mg", "dt", "slot" }));
		/**
		 * 60天未领取的救援金，不显示
		 */
		Calendar cals = Calendar.getInstance();
		cals.add(Calendar.DAY_OF_MONTH, -60);
		dc = dc.add(Restrictions.or(Restrictions.ne("status", "0"),
				Restrictions.and(Restrictions.eq("status", "0"), Restrictions.ge("createTime", cals.getTime()))));

		// dc = dc.addOrder(Order.desc("createTime"));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<LosePromo> list = page.getPageContents();
		for (LosePromo p : list) {
			p.setTempCreateTime(sf.format(p.getCreateTime()));
		}
		return page;
	}

	/**
	 * 领取/取消负盈利反赠订单
	 * 
	 * @param pno
	 *            提案号
	 * @param targetFlag
	 *            目标状态
	 * @return
	 */
	public String optLosePromo(String pno, Integer targetFlag, String ip, String target) {
		String seqId = seqService.generateTransferID();
		return SynchronizedUtil.getInstance().optPTLosePromo(proposalService, pno, targetFlag, ip, target, seqId);
	}

	public List<Bet> queryBets(String username, String type) {
		return UpgradeUtil.getBetByDate(upgradeService, username, type);
	}

	public String checkUpgrade(String username, String type) {
		return UpgradeUtil.checkUpgrade(upgradeService, username, type);
	}

	public EBetResp ebetLogin(String loginname, String hostUrl) {
		return EBetUtil.login(loginname, "EBET", "LIVE", hostUrl, "1");
	}

	public String ebetTransfer(String loginname, Integer credit, String type) {
		String strLimit = transferService.transferLimitMethod(loginname, Double.parseDouble(String.valueOf(credit)));
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			if (type.equals("IN")) {
				// return
				// UserSynchronizedPool.getSynchUtilByUser(loginname).ebetTransferIn(transferService,
				// loginname, credit, type, "EBET", "LIVE", seqId);
				return "游戏转入正在维护...";
			} else if (type.equals("OUT")) {
				// return
				// SynchronizedUtil.getInstance().ebetTransfer(transferService,
				// loginname, credit, type, "EBET", "LIVE", seqId);
				// return
				// SynchronizedUtil.getInstance().transfer4EbetOutVerifyBet(transferService,
				// loginname, credit, type, "EBET", "LIVE", seqId);
				return UserSynchronizedPool.getSynchUtilByUser(loginname).transfer4EbetOutVerifyBet(transferService,
						loginname, credit, type, "EBET", "LIVE", seqId);
			} else {
				return "转账失败!";
			}
		}
		return strLimit;
	}

	public String getEbetBalance(String loginname) {
		Double balance = EBetUtil.getBalance(loginname, "EBET");
		return balance == null ? null : balance.toString();
	}

	/**
	 * 登入gpi slot
	 * 
	 * @param loginname
	 * @param gameid
	 * @return
	 */
	public String gpiLogin(String loginname, String gameid, int isfun) {
		String token = RandomStringUtils.randomAlphanumeric(32);
		GPIToken gpiToken = (GPIToken) cs.get(GPIToken.class, loginname);
		if (gpiToken == null) {
			GPIToken gpiKey = new GPIToken();
			gpiKey.setLoginname(loginname);
			gpiKey.setUpdateTime(new Date());
			gpiKey.setToken(token);
			gpiKey.setCreateTime(new Date());
			gpiKey.setIsTest(0);
			cs.save(gpiKey);
		} else {
			gpiToken.setToken(token);
			gpiToken.setUpdateTime(new Date());
			cs.update(gpiToken);
		}
		return GPIUtil.login(token, gameid, isfun);
	}

	/**
	 * 登入gpi png
	 * 
	 * @param loginname
	 * @param gameid
	 * @return
	 */
	public String gpiOtherLogin(String loginname, String gameid, int isfun, String type) {
		String token = RandomStringUtils.randomAlphanumeric(32);
		GPIToken gpiToken = (GPIToken) cs.get(GPIToken.class, loginname);
		if (gpiToken == null) {
			GPIToken gpiKey = new GPIToken();
			gpiKey.setLoginname(loginname);
			gpiKey.setUpdateTime(new Date());
			gpiKey.setToken(token);
			gpiKey.setCreateTime(new Date());
			gpiKey.setIsTest(0);
			cs.save(gpiKey);
		} else {
			gpiToken.setToken(token);
			gpiToken.setUpdateTime(new Date());
			cs.update(gpiToken);
		}
		if (type.equalsIgnoreCase("png")) {
			return GPIUtil.pngLogin(token, gameid, isfun);
		} else if (type.equalsIgnoreCase("bs")) {
			return GPIUtil.bsLogin(token, gameid, isfun);
		} else if (type.equalsIgnoreCase("ctxm")) {
			return GPIUtil.ctxmLogin(token, gameid, isfun);
		}
		return "";
	}

	/**
	 * 登入gpi mobile
	 * 
	 * @param loginname
	 * @param gameid
	 * @return
	 */
	public String gpiMobileLogin(String loginname, String gameid, int isfun, String type, String domain) {
		String token = RandomStringUtils.randomAlphanumeric(32);
		GPIToken gpiToken = (GPIToken) cs.get(GPIToken.class, loginname);
		if (gpiToken == null) {
			GPIToken gpiKey = new GPIToken();
			gpiKey.setLoginname(loginname);
			gpiKey.setUpdateTime(new Date());
			gpiKey.setToken(token);
			gpiKey.setCreateTime(new Date());
			gpiKey.setIsTest(0);
			cs.save(gpiKey);
		} else {
			gpiToken.setToken(token);
			gpiToken.setUpdateTime(new Date());
			cs.update(gpiToken);
		}
		if (type.equalsIgnoreCase("gpi")) {
			return GPIUtil.loginMobile(token, gameid, isfun, domain);
		} else if (type.equalsIgnoreCase("png")) {
			return GPIUtil.pngMobileLogin(token, gameid, isfun);
		} else if (type.equalsIgnoreCase("bs")) {
			return GPIUtil.bsLogin(token, gameid, isfun);
		} else if (type.equalsIgnoreCase("ctxm")) {
			return GPIUtil.ctxmLogin(token, gameid, isfun);
		}
		return "";
	}

	/**
	 * gpi登录验证
	 * 
	 * @param token
	 * @return
	 */
	public String validateGPIAccess(String token) {
		return cs.validateGPIAccess(token);
	}

	public String getTtgBalance(String loginname) {
		String str = PtUtil1.getPlayerAccount(loginname);
		Double balance = null;
		if (!StringUtil.isEmpty(str)) {
			balance = Double.parseDouble(str);
		}
		return balance == null ? null : balance.toString();
	}

	/**
	 * gpi查询余额
	 * 
	 * @param loginname
	 * @return
	 */
	public String getGPIBalance(String loginname) {
		try {
			Double balance = GPIUtil.getBalance(loginname);
			return balance == null ? "系统繁忙" : balance.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	/**
	 * GPI转入
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInforGPI(String loginname, Double remit) {
		/*
		 * String strLimit =
		 * transferService.transferLimitMethod(loginname,remit); if(strLimit ==
		 * null){ String seqId = seqService.generateTransferID(); return
		 * SynchronizedUtil.getInstance().gpiTransferIn(transferService, seqId,
		 * loginname, remit); } return strLimit;
		 */
		return "游戏维护中...";
	}

	public String transferOut4GPI(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		return SynchronizedUtil.getInstance().gpiTransferOut(transferService, seqId, loginname, remit);
	}

	/**
	 * QT查询余额
	 * 
	 * @param loginname
	 * @return
	 */
	public String getQtBalance(String loginname) {
		try {
			String balance = QtUtil.getBalance(loginname);
			return QtUtil.RESULT_FAIL.equals(balance) ? null : balance.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	/**
	 * QT转入
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferQtIn(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return
			// SynchrQtInUtil.getInstance().transferQtAndSelfYouHuiIn(transferService,
			// seqId, loginname, remit, null);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferQtAndSelfYouHuiIn(transferService, seqId,
					loginname, remit, null);
		}
		return strLimit;
	}

	/**
	 * QT转出
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferQtOut(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return
		// SynchrQtOutUtil.getInstance().transferQtAndSelfYouHuiOut(transferService,
		// seqId, loginname, remit);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferQtAndSelfYouHuiOut(transferService, seqId,
				loginname, remit);
	}

	public Bankinfo getZfbBankInfo(String loginname) {
		Bankinfo zfbBank = new Bankinfo();
		Users user = getUser(loginname);
		if (null == user) {
			return null;
		}
		try {
			DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
			proposalSavedc.add(Restrictions.eq("loginname", loginname));
			proposalSavedc.add(Restrictions.eq("flag", 2));
			proposalSavedc.add(Restrictions.eq("type", 502));
			proposalSavedc.setProjection(Projections.sum("amount"));
			List saveList = proposalService.findByCriteria(proposalSavedc);

			DetachedCriteria payorderdc = DetachedCriteria.forClass(Payorder.class);
			payorderdc.add(Restrictions.eq("loginname", loginname));
			payorderdc.add(Restrictions.eq("flag", 0));
			payorderdc.add(Restrictions.eq("type", 0));
			payorderdc.setProjection(Projections.sum("money"));
			List payorderList = proposalService.findByCriteria(payorderdc);

			Double amount = 0.00;
			if ((saveList != null && !saveList.isEmpty() && null != saveList.get(0)
					|| payorderList != null && !payorderList.isEmpty() && null != payorderList.get(0))) {
				if (null != payorderList.get(0)) {
					amount += (Double) payorderList.get(0);
				}
				if (null != saveList.get(0)) {
					amount += (Double) saveList.get(0);
				}
			}
			// if(amount<5000){
			// zfbBank.setMassage("您的活跃度没有达到,暂时不能使用支付宝附言存款,感谢您的支持!");
			// return zfbBank ;
			// }

			DetachedCriteria dc4 = DetachedCriteria.forClass(Bankinfo.class);
			dc4 = dc4.add(Restrictions.eq("type", 1));
			dc4 = dc4.add(Restrictions.eq("isshow", 1));
			dc4 = dc4.add(Restrictions.eq("useable", 0));
			dc4 = dc4.add(Restrictions.eq("bankname", "支付宝"));
			dc4 = dc4.add(Restrictions.like("userrole", "%" + user.getLevel() + "%"));
			if (amount < 1000) {
				dc4 = dc4.add(Restrictions.eq("vpnname", "F"));
			} else if (amount >= 1000 && amount < 5000) {
				dc4 = dc4.add(Restrictions.eq("vpnname", "E"));
			} else if (amount >= 5000 && amount < 10000) {// A
				dc4 = dc4.add(Restrictions.eq("vpnname", "A"));
			} else if (amount >= 10000 && amount < 50000) {// B
				dc4 = dc4.add(Restrictions.eq("vpnname", "B"));
			} else if (amount >= 50000 && amount < 100000) {// C
				dc4 = dc4.add(Restrictions.eq("vpnname", "C"));
			} else if (amount >= 100000) {// D
				dc4 = dc4.add(Restrictions.eq("vpnname", "D"));
			} else {
				zfbBank.setMassage("支付宝附言未知错误");
				return zfbBank;
			}
			dc4.add(Restrictions.or(Restrictions.eq("zfbImgCode", ""), Restrictions.isNull("zfbImgCode")));
			dc4 = dc4.add(Restrictions.ne("scanAccount", 1));
			dc4.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list4 = proposalService.findByCriteria(dc4);
			if (list4 != null && list4.size() > 0 && list4.get(0) != null) {
				zfbBank = list4.get(0);
			} else {
				zfbBank.setMassage("没有合适的支付宝账号..");
				return zfbBank;
			}
		} catch (Exception e) {
			log.error(loginname + e.getMessage());
		}
		return zfbBank;
	}

	public String getDtBalance(String loginname, String password) {
		String str = DtUtil.getamount(loginname, password);
		Double balance = null;
		if (!StringUtil.isEmpty(str)) {
			balance = Double.parseDouble(str);
		}
		return balance == null ? null : balance.toString();
	}

	/**
	 * QT获得游戏url
	 * 
	 * @param loginname
	 * @param password
	 * @param gameid
	 * @param isfun
	 * @param client
	 * @return
	 */
	public String qtGameUrl(String loginname, String gameid, int isfun, String isflash, String domain) {
		String mode = isfun == 1 ? QtUtil.MODE_DEMO : QtUtil.MODE_REAL;
		String clientType = "1".equals(isflash) ? QtUtil.CLIENTTYPE_FLASH : QtUtil.CLIENTTYPE_HTML5;
		return QtUtil.getGameUrl(gameid, loginname, mode, clientType, domain);
	}

	/**
	 * QT获得游戏url--中心钱包模式
	 * 
	 * @param loginname
	 * @param password
	 * @param gameid
	 * @param isfun
	 * @param client
	 * @return
	 */
	public String qtGameUrlForTp(String loginname, String gameid, int isfun, String isflash, String domain) {
		Const ct = transferService.getConsts("QT游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return null;
		}
		String mode = isfun == 1 ? QtUtilForTp.MODE_DEMO : QtUtilForTp.MODE_REAL;
		String clientType = "1".equals(isflash) ? QtUtilForTp.CLIENTTYPE_FLASH : QtUtilForTp.CLIENTTYPE_HTML5;
		return QtUtilForTp.getGameUrl(gameid, loginname, mode, clientType, domain);
	}

	/*------ JC操作部分 jcbf-----*/

	/**
	 * JC登录
	 * 
	 * @param loginname
	 * @param password
	 * @return
	 */
	public String loginJc(String loginname, String password) {
		return userWebSerivce.loginJc(loginname, password);
	}

	public String getJcBalance(String loginname) {
		String rs = JCUtil.balanceJC(loginname);
		JSONObject json = JSONObject.fromObject(rs);
		if (json.getString("code").equals("j10000")) {
			return json.getString("balance");
		} else {
			return json.getString("message");
		}
	}

	public String transferToJc(String loginname, Double remit) {
		// String rs = JCUtil.transferToJC(loginname, remit);
		// JSONObject json = JSONObject.parseObject(rs);
		// if ("j10000".equals(json.getString("code"))){
		// 获取自增长ID
		String seqId = seqService.generateTransferID();
		String transRtn = "";
		synchronized (jcToLock) {
			// 同步运行交易和日志记录
			transRtn = transferService.transferJc(seqId, loginname, remit, "TO");
		}
		if (StringUtils.isEmpty(transRtn)) {
			return null;
		} else {
			return transRtn;
		}
		// } else {
		// return json.getString("message");
		// }
	}

	public String transferFromJc(String loginname, Double remit) {
		// String rs = JCUtil.transferFromJC(loginname, remit);
		// JSONObject json = JSONObject.parseObject(rs);
		// if ("j10000".equals(json.getString("code"))){
		String seqId = seqService.generateTransferID();
		String transRtn = "";
		synchronized (jcFromLock) {
			// 同步运行交易和日志记录
			transRtn = transferService.transferJc(seqId, loginname, remit, "FROM");
		}
		if (StringUtils.isEmpty(transRtn)) {
			return null;
		} else {
			return transRtn;
		}
		// } else {
		// return json.getString("message");
		// }
	}

	/*------ jcbf-----*/

	/**
	 * NT登录
	 * 
	 * @param id
	 * @return 用于登录的标识
	 *//*
		 * public String loginNT(String loginname) { return
		 * userWebSerivce.loginNT(loginname); }
		 */

	/**
	 * 中心錢包NT登录
	 * 
	 * @param id
	 * @return 用于登录的标识
	 */
	public String loginGWNT(String loginname, String gameCode) {
		return userWebSerivce.loginGWNT(loginname, gameCode);
	}

	/**
	 * 查询NT余额
	 * 
	 * @param id
	 * @return
	 */
	public String getNTBalance(String loginname) {
		return NTUtils.getNTMoney(loginname);
	}

	/**
	 * NT转账至E68
	 * 
	 * @param id
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferFromNT(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		String transRtn = "";
		// transRtn = transferService.transferFromNTJudge(seqId, loginname,
		// remit);
		transRtn = UserSynchronizedPool.getSynchUtilByUser(loginname).transferFromNTJudge(transferService, seqId,
				loginname, remit);
		if (StringUtils.isEmpty(transRtn)) {
			return null;
		} else {
			return transRtn;
		}
		// } else {
		// return json.getString("message");
		// }
	}

	/**
	 * E68转账至NT
	 * 
	 * @param id
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferToNT(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			String transRtn = "";
			// 同步运行交易和日志记录
			// transRtn =
			// SynchronizedNTUtil.getInstance().transferToNTJudge(transferService,
			// seqId, loginname, remit, null);
			transRtn = UserSynchronizedPool.getSynchUtilByUser(loginname).transferToNTJudge(transferService, seqId,
					loginname, remit, null);
			if (StringUtils.isEmpty(transRtn)) {
				return null;
			} else {
				return transRtn;
			}
		} else {
			return strLimit;
		}
	}

	public Bankinfo getZfbBankRedirectInfo(String loginname, Boolean isImgCode) {
		Bankinfo zfbBank = new Bankinfo();
		Users user = getUser(loginname);
		if (null == user) {
			return null;
		}
		try {
			DetachedCriteria dc4 = DetachedCriteria.forClass(Bankinfo.class);
			dc4 = dc4.add(Restrictions.eq("type", 1));
			dc4 = dc4.add(Restrictions.eq("isshow", 1));
			dc4 = dc4.add(Restrictions.eq("useable", 0));
			dc4 = dc4.add(Restrictions.eq("bankname", "支付宝"));
			dc4 = dc4.add(Restrictions.like("userrole", "%" + user.getLevel() + "%"));
			if (isImgCode) {
				dc4.add(Restrictions.and(Restrictions.ne("zfbImgCode", ""), Restrictions.isNotNull("zfbImgCode")));
				dc4 = dc4.add(Restrictions.eq("scanAccount", 1));
			} else {
				dc4.add(Restrictions.or(Restrictions.eq("zfbImgCode", ""), Restrictions.isNull("zfbImgCode")));
				dc4 = dc4.add(Restrictions.ne("scanAccount", 1));
			}
			if (user.getLevel() <= 1) {
				DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
				proposalSavedc.add(Restrictions.eq("loginname", loginname));
				proposalSavedc.add(Restrictions.eq("flag", 2));
				proposalSavedc.add(Restrictions.eq("type", 502));
				proposalSavedc.setProjection(Projections.sum("amount"));
				List saveList = proposalService.findByCriteria(proposalSavedc);

				DetachedCriteria payorderdc = DetachedCriteria.forClass(Payorder.class);
				payorderdc.add(Restrictions.eq("loginname", loginname));
				payorderdc.add(Restrictions.eq("flag", 0));
				payorderdc.add(Restrictions.eq("type", 0));
				payorderdc.setProjection(Projections.sum("money"));
				List payorderList = proposalService.findByCriteria(payorderdc);

				Double amount = 0.00;
				if ((saveList != null && !saveList.isEmpty() && null != saveList.get(0)
						|| payorderList != null && !payorderList.isEmpty() && null != payorderList.get(0))) {
					if (null != payorderList.get(0)) {
						amount += (Double) payorderList.get(0);
					}
					if (null != saveList.get(0)) {
						amount += (Double) saveList.get(0);
					}
				}
				/*
				 * if(amount<5000){
				 * zfbBank.setMassage("您的活跃度没有达到,暂时不能使用支付宝附言存款,感谢您的支持!"); return
				 * zfbBank ; }
				 */

				if (amount < 1000) {
					dc4 = dc4.add(Restrictions.eq("vpnname", "F"));
				} else if (amount >= 1000 && amount < 5000) {
					dc4 = dc4.add(Restrictions.eq("vpnname", "E"));
				} else if (amount >= 5000 && amount < 10000) {// A
					dc4 = dc4.add(Restrictions.eq("vpnname", "A"));
				} else if (amount >= 10000 && amount < 50000) {// B
					dc4 = dc4.add(Restrictions.eq("vpnname", "B"));
				} else if (amount >= 50000 && amount < 100000) {// C
					dc4 = dc4.add(Restrictions.eq("vpnname", "C"));
				} else if (amount >= 100000) {// D
					dc4 = dc4.add(Restrictions.eq("vpnname", "D"));
				} else {
					zfbBank.setMassage("支付宝附言未知错误");
					return zfbBank;
				}
			}
			dc4.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list4 = proposalService.findByCriteria(dc4);
			if (list4 != null && list4.size() > 0 && list4.get(0) != null) {
				zfbBank = list4.get(0);
			} else {
				zfbBank.setMassage("目前支付宝存款方式正在维护中！");
				return zfbBank;
			}
			if (isImgCode) {
				AlipayAccount alipayAccount = proposalService.getAlipayAccount(loginname, 0);
				if (null == alipayAccount) {
					zfbBank.setZfbImgCode(null);
				}
				return zfbBank;
			} else {
				String msg = getRemarkAgain(loginname, "支付宝", zfbBank.getUsername());
				zfbBank.setVpnpassword(msg);
				return zfbBank;
			}
		} catch (Exception e) {
			log.error(loginname + e.getMessage());
		}

		String msg = getRemarkAgain(loginname, "支付宝", zfbBank.getUsername());
		zfbBank.setVpnpassword(msg);
		return zfbBank;
	}

	/**
	 * PT大爆炸礼金
	 */
	@SuppressWarnings("unchecked")
	public List<PTBigBang> ptBigBangBonus(String loginname) {
		LinkedList<PTBigBang> bigBangList = new LinkedList<PTBigBang>();
		DetachedCriteria dc = DetachedCriteria.forClass(PTBigBang.class);
		dc = dc.add(Restrictions.eq("username", loginname));
		dc = dc.add(Restrictions.eq("status", "1"));
		// 24小时内
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		dc = dc.add(Restrictions.ge("distributeTime", calendar.getTime()));
		dc = dc.addOrder(Order.desc("distributeTime"));
		List<PTBigBang> list = cs.findByCriteria(dc);
		if (list != null && list.size() > 0) {
			PTBigBang ptBigBang = list.get(0);
			ptBigBang.setTempDistributeTime(sf.format(ptBigBang.getDistributeTime()));
			// return ptBigBang;
			bigBangList.addFirst(ptBigBang);
		}

		// 查询已领取或已处理的
		dc = DetachedCriteria.forClass(PTBigBang.class);
		dc = dc.add(Restrictions.eq("username", loginname));
		dc = dc.add(Restrictions.gt("giftMoney", 0.00));
		dc = dc.add(Restrictions.or(Restrictions.eq("status", "2"), Restrictions.eq("status", "3")));
		dc = dc.addOrder(Order.desc("distributeTime"));
		list = cs.findByCriteria(dc, 0, 7);
		for (PTBigBang bigBang : list) {
			bigBang.setTempDistributeTime(sf.format(bigBang.getDistributeTime()));
		}
		bigBangList.addAll(list);
		return bigBangList;
	}

	/**
	 * 领取PT大爆炸礼金
	 * 
	 * @param id
	 * @param ip
	 * @return
	 */
	public String getPTBigBangBonus(Integer id, String ip) {
		return SynchronizedUtil.getInstance().getPTBigBangBonus(proposalService, id, ip);
	}

	public String checkSystemConfig(String typeNo, String itemNo, String flag) {
		StringBuffer str = new StringBuffer("");
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
		List<SystemConfig> list = proposalService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			str = str.append(list.get(0).getTypeName());
			str = str.append("#" + list.get(0).getValue());
		}
		return str.toString();
	}

	public String checkSystemConfigForEmigrated(String typeNo, String flag) {
		StringBuffer str = new StringBuffer("");
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if (!StringUtils.isEmpty(typeNo)) {
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if (!StringUtils.isEmpty(flag)) {
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list = proposalService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			str = str.append(list.get(0).getTypeName());
			str = str.append("#" + list.get(0).getValue());
		}
		return str.toString();
	}

	public Double getPtPlayerMoney(String loginname, String password) {
		return PtUtil.getPlayerMoney(loginname, password);
	}

	public Userstatus getAgentSlot(String loginname) {
		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (null == userstatus) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setTouzhuflag(0);
			status.setCashinwrong(0);
			proposalService.save(status);
			userstatus = new Userstatus();
			userstatus.setSlotaccount(0.0);
		}
		return userstatus;
	}

	/**
	 * live800客户端获取用户信息
	 * 
	 * @param userName
	 * @return 所有信息组装成json
	 */
	@SuppressWarnings("unchecked")
	public String getUserInfo4Live800(String userName) {
		Map<String, String> resultMap = new HashMap<String, String>();
		// 用户信息
		Users user = (Users) slaveService.get(Users.class, userName);
		resultMap.put("playerInfo", net.sf.json.JSONObject.fromObject(user).toString());
		// 在线支付
		DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
		dc = dc.add(Restrictions.eq("loginname", userName));
		dc = dc.add(Restrictions.eq("type", PayOrderFlagType.SUCESS.getCode()));
		dc = dc.addOrder(Order.desc("createTime"));
		List<Payorder> pList = slaveService.findByCriteria(dc, 0, 10);
		resultMap.put("payOrder", net.sf.json.JSONArray.fromObject(pList).toString());
		// 存款
		dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("type", ProposalType.CASHIN.getCode()));
		dc = dc.add(Restrictions.eq("loginname", userName));
		dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		dc = dc.addOrder(Order.desc("createTime"));
		List<Proposal> dList = slaveService.findByCriteria(dc, 0, 10);
		resultMap.put("deposit", net.sf.json.JSONArray.fromObject(dList).toString());
		// 提款
		dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode()));
		dc = dc.add(Restrictions.eq("loginname", userName));
		dc = dc.addOrder(Order.desc("createTime"));
		List<Proposal> wList = slaveService.findByCriteria(dc, 0, 10);
		resultMap.put("withdrawal", net.sf.json.JSONArray.fromObject(wList).toString());
		// 优惠活动
		dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("loginname", userName));
		dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		dc = dc.add(Restrictions.in("type",
				new Object[] { ProposalType.BIRTHDAY.getCode(), ProposalType.LEVELPRIZE.getCode(),
						ProposalType.BANKTRANSFERCONS.getCode(), ProposalType.CONCESSIONS.getCode(),
						ProposalType.XIMA.getCode(), ProposalType.OFFER.getCode(), ProposalType.PRIZE.getCode(),
						ProposalType.PROFIT.getCode(), ProposalType.PTBIGBANG.getCode() }));
		dc = dc.addOrder(Order.desc("createTime"));
		List<Proposal> cList = slaveService.findByCriteria(dc, 0, 10);
		resultMap.put("coupon", net.sf.json.JSONArray.fromObject(cList).toString());
		// 额度记录
		dc = DetachedCriteria.forClass(Transfer.class);
		dc = dc.add(Restrictions.eq("loginname", userName));
		dc = dc.add(Restrictions.eq("flag", Constants.FLAG_TRUE));
		dc = dc.addOrder(Order.desc("createtime"));
		List<Transfer> tList = slaveService.findByCriteria(dc, 0, 10);
		resultMap.put("transfer", net.sf.json.JSONArray.fromObject(tList).toString());

		return net.sf.json.JSONObject.fromObject(resultMap).toString();
	}

	public String getAgentVip(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(AgentVip.class);
		dc.add(Restrictions.eq("id.agent", loginname));
		dc.addOrder(Order.desc("createtime"));
		List<AgentVip> list = cs.findByCriteria(dc);
		if (null != list && list.size() > 0 && null != list.get(0) && list.get(0).getLevel() == 1) {
			return "1";
		} else {
			return "0";
		}
	}

	public Boolean getPlayerOnlineInfo(String loginname) {
		return PtUtil.getPlayerOnlineInfo(loginname);
	}

	/**
	 * 获取代理每月及每日数据统计
	 * 
	 * @param loginname
	 * @return
	 */
	public String agentMonthlyReport(String loginname) {
		return cs.queryAgentReport(loginname);
		// return cs.queryAgentMonthlyReport(loginname);
	}

	public SlaveService getSlaveService() {
		return slaveService;
	}

	public void setSlaveService(SlaveService slaveService) {
		this.slaveService = slaveService;
	}

	public Integer getEnableGiftId() {

		DetachedCriteria dc = DetachedCriteria.forClass(Gift.class);
		dc.add(Restrictions.eq("status", "1"));
		List<Gift> list = new ArrayList<Gift>();
		list = cs.findByCriteria(dc);
		if (list != null && list.size() > 0) {
			return list.get(0).getId();
		}
		return 0;
	}

	/**
	 * 判断是否存在活动礼品
	 * 
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String isExistGift(String loginName) {
		GiftVo giftVo = new GiftVo();
		Users user = (Users) cs.get(Users.class, loginName);
		DetachedCriteria dc = DetachedCriteria.forClass(Gift.class);
		Date now = new Date();
		dc.add(Restrictions.le("startTime", now));
		dc.add(Restrictions.ge("endTime", now));
		dc.add(Restrictions.eq("status", "1"));
		dc.add(Restrictions.like("levels", user.getLevel().toString(), MatchMode.ANYWHERE));
		List<Gift> gifts = cs.findByCriteria(dc);
		if (gifts != null && gifts.size() == 1) {
			Gift gift = gifts.get(0);
			giftVo.setId(gift.getId());
			giftVo.setTitle(gift.getTitle());
			// 检查是否已领取
			DetachedCriteria orderDc = DetachedCriteria.forClass(GiftOrder.class);
			orderDc.add(Restrictions.eq("loginname", loginName));
			orderDc.add(Restrictions.eq("giftID", gift.getId()));
			List<GiftOrder> applyOrderLists = cs.findByCriteria(orderDc);
			if (applyOrderLists != null && applyOrderLists.size() > 0) {
				giftVo.setReach(true);
				giftVo.setOrder(applyOrderLists.get(0));
			} else if (checkQualification(gift, loginName)) { // 检查是否达到领取资格
				// return "{\"gift\":\"" + gift.getId() + "\", \"title\":\"" +
				// gift.getTitle() + "\"}";
				giftVo.setReach(true);
			} else {
				giftVo.setReach(false);
			}
			return net.sf.json.JSONObject.fromObject(giftVo).toString();
		}
		return "";
	}

	/**
	 * 申请礼品
	 * 
	 * @param loginname
	 * @param giftID
	 * @param address
	 * @param addressee
	 * @param cellphoneNo
	 * @return
	 */
	public String applyGift(String loginName, Integer giftID, String address, String addressee, String cellphoneNo) {
		Gift gift = (Gift) cs.get(Gift.class, giftID);
		Users user = (Users) cs.get(Users.class, loginName);
		// 检查时间、角色
		Date now = new Date();
		if (now.before(gift.getStartTime()) || now.after(gift.getEndTime()) || gift.getStatus().equals("0")) {
			return "申请失败，不在活动有效时间内";
		}
		if (!gift.getLevels().contains(user.getLevel().toString())) {
			return "您的等级不符合活动要求";
		}

		/*
		 * String checkSqlValid =
		 * "select count(*) from concert where loginname=:loginname";
		 * Map<String, Object> paramsValid = new HashMap<String, Object>();
		 * paramsValid.put("loginname", loginName); if
		 * (cs.getCount(checkSqlValid, paramsValid) > 0) { return
		 * "您已申请过投注抢门票，请不要重复申请"; }
		 */
		// 检查是否已领取
		String checkSql = "select count(*) from gift_order where loginname=:loginname and giftID = :giftID";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginName);
		params.put("giftID", gift.getId());
		if (cs.getCount(checkSql, params) > 0) {
			return "您已申请过该礼品，请不要重复申请";
		}

		// 检查申请人数是否已达到指定人数限制
		String countSql = "select count(1) from gift_order t where t.giftID = :giftID";
		Map<String, Object> countParams = new HashMap<String, Object>();
		countParams.put("giftID", gift.getId());

		// 获取当前活动申请人数数量
		Integer num = cs.getCount(countSql, countParams);
		String limitSql = "select t.gift_quantity from gift t where t.type = :type and t.status = :status order by t.createTime desc";

		Map<String, Object> limitParams = new HashMap<String, Object>();
		limitParams.put("type", "限时礼品");
		limitParams.put("status", "1");

		List list = cs.list(limitSql, limitParams);

		if (null == list || list.isEmpty()) {

			return "不在活动有效时间内";
		}

		Integer giftQuantity = (Integer) list.get(0);
		if (null != giftQuantity && num >= giftQuantity) {

			return "当前活动申请人数已满！";
		}

		// 检查是否达到领取资格
		if (checkQualification(gift, loginName)) {
			GiftOrder gOrder = new GiftOrder(giftID, loginName, user.getLevel(), addressee, cellphoneNo, address, 0,
					new Date());
			cs.save(gOrder);
			return null;
		} else {
			return "申请失败，您未到达领取该礼品的要求";
		}
	}

	/**
	 * 检查是否满足礼品申请条件
	 * 
	 * @param gift
	 * @return
	 */
	private boolean checkQualification(Gift gift, String loginName) {
		if (gift.getBetAmount() == null && gift.getDepositAmount() == null) {
			return false;
		}
		String sqlStr = null;
		Map<String, Object> params = new HashMap<String, Object>();
		// 验证存款额
		if (gift.getDepositAmount() != null && gift.getDepositAmount() > 0) {
			Double total = 0.0;
			sqlStr = "select sum(money) from payorder where loginname=:loginname and type=:orderType and createTime>=:startTimeDeposit and createTime<=:endTimeDeposit";
			params.put("loginname", loginName);
			params.put("orderType", 0); // 支付成功的订单
			params.put("startTimeDeposit", gift.getStartTimeDeposit());
			params.put("endTimeDeposit", gift.getEndTimeDeposit());
			total = cs.getDoubleValueBySql(sqlStr, params);

			sqlStr = "select sum(amount) from proposal where loginname=:loginname and type=:pType and flag=:optFlag and createTime>=:startTimeDeposit and createTime<=:endTimeDeposit";
			params.put("pType", 502); // 存款提案
			params.put("optFlag", 2); // 已执行
			total = Arith.add(total, cs.getDoubleValueBySql(sqlStr, params));
			if (total < gift.getDepositAmount())
				return false;
		}
		// 验证投注额
		if (gift.getBetAmount() != null && gift.getBetAmount() > 0) {
			Calendar cd = Calendar.getInstance();
			cd.setTime(gift.getStartTimeBet());
			cd.add(Calendar.DAY_OF_MONTH, 1);
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			Date betStartTimeTmp = cd.getTime();

			cd.setTime(gift.getEndTimeBet());
			cd.add(Calendar.DAY_OF_MONTH, 1);
			cd.set(Calendar.HOUR_OF_DAY, 23);
			cd.set(Calendar.MINUTE, 59);
			cd.set(Calendar.SECOND, 59);
			Date betEndTimeTmp = cd.getTime();

			sqlStr = "select sum(bettotal) from agprofit where loginname=:loginname and createTime>=:betStartTime and createTime<=:betEndTime";
			params.put("loginname", loginName);
			params.put("betStartTime", betStartTimeTmp);
			params.put("betEndTime", betEndTimeTmp);
			Double betAmount = cs.getDoubleValueBySql(sqlStr, params);
			if (betAmount < gift.getBetAmount())
				return false;
		}
		return true;
	}

	private IQuerybetrankService queryBetrankService;

	public IQuerybetrankService getQueryBetrankService() {
		return queryBetrankService;
	}

	public void setQueryBetrankService(IQuerybetrankService queryBetrankService) {
		this.queryBetrankService = queryBetrankService;
	}

	/**
	 * 查询流水总量 TTG/GPI/PT
	 * 
	 * @param start
	 *            查询开始时间
	 * @param end
	 *            结束时间
	 * @param type
	 *            老虎机类型
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page querybetRank(String start, String end, String type, Integer pageIndex, Integer size) {
		/*
		 * String platform =type; if(type.equals("ttg")){ platform="'ttg')";
		 * }else if(type.equals("pttiger")){
		 */
		// platform="'gpi' or platform = 'rslot' or platform = 'png' or platform
		// = 'bs' or platform = 'ctxm' or platform = 'ttg' or platform =
		// 'pttiger' )";
		// }else if(type.equals("gpi")){
		String platform = "'gpi' or platform = 'rslot' or platform = 'png' or platform = 'bs' or platform = 'ctxm'  or platform = 'ttg' or platform = 'pttiger' )";
		// }
		// 查询总条数
		String sqlCount = "select count(*) from (select 1 from platform_data where starttime >='" + start
				+ "' and starttime <='" + end + "' and (platform=" + platform
				+ " GROUP BY loginname UNION ALL select 1 from betrank)a";
		List list = queryBetrankService.getListBysql(sqlCount);
		String count = list.get(0).toString();
		if (Integer.parseInt(count) > 500) {
			count = "500";
		}
		// 分页查询
		StringBuffer sqlPage = new StringBuffer("select z.* from (select sum(p.bet) as sumbet,p.loginname,'" + type
				+ "',u.lastarea from platform_data p,users u where p.starttime >='" + start + "' and p.starttime <='"
				+ end + "' and p.loginname=u.loginname and (p.platform=" + platform
				+ "   GROUP BY p.loginname   UNION ALL select sumbet,loginname,'" + type
				+ "',lastarea from betrank )z order by  z.sumbet desc");
		return queryBetrankService.getPage(sqlPage.toString(), pageIndex, size, count);
	}

	/**
	 * 查询每周老虎机比赛数据
	 * 
	 * @param platform
	 * @param startTime
	 * @param endTime
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page querySlotMatchWeekly(String startTime, String endTime, Integer pageIndex, Integer size) {
		Page page = new Page();
		try {
			// 判断当前老虎机平台是否开启
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc = dc.add(Restrictions.like("id", "%体验金周赛"));
			dc = dc.add(Restrictions.eq("value", "1"));
			List<Const> list = allMatchService.getHibernateTemplate().findByCriteria(dc);
			if (list == null || list.size() == 0) {
				return null;
			}
			String c_s = "select count(smw.loginname) from SlotsMatchWeekly smw where smw.startTime='" + startTime
					+ "' and smw.endTime='" + endTime + "'";
			Long count = (Long) allMatchService.getHibernateTemplate().find(c_s).iterator().next();
			count = count > 200 ? 200 : count;// 限定前端页面只显示200条数据
			StringBuffer sql = new StringBuffer(
					"select smw.loginname,smw.startTime,smw.endTime,smw.getTime,smw.win,smw.platform");
			sql.append(" from slots_match_weekly smw where 1=1");
			sql.append(" and smw.startTime='" + startTime + "' and smw.endTime='" + endTime
					+ "' order by smw.win desc,smw.getTime asc");
			page = allMatchService.getSlotsMatchPage(sql.toString(), pageIndex, size, count + "");
		} catch (Exception e) {
			log.error("querySlotMatchWeekly error:", e);
		} finally {
			return page;
		}
	}

	/**
	 * 查询玩家的投诉建议
	 * 
	 * @param pageNo
	 * @param size
	 * @param loginName
	 * @return
	 */
	public String getSuggetionsByUser(int pageNo, int size, String loginName) {
		SuggestionVo sugVo = new SuggestionVo();
		sugVo.setPageNo(pageNo);
		sugVo.setPageSize(size);
		// 计算总数
		String sqlStr = "select count(*) from suggest where loginname=:loginname";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginName);
		sugVo.setCount(cs.getCount(sqlStr, params));

		DetachedCriteria dc = DetachedCriteria.forClass(Suggestion.class);
		dc.add(Restrictions.eq("loginName", loginName));
		dc.addOrder(Order.desc("createTime"));
		List<Suggestion> list = cs.findByCriteria(dc, (pageNo - 1) * size, size);
		for (Suggestion suggestion : list) {
			suggestion.setTempCreateTime(DateUtil.formatDateForStandard(suggestion.getCreateTime()));
		}
		sugVo.setSugList(list);
		return net.sf.json.JSONArray.fromObject(sugVo).toString();
	}

	/**
	 * 查询工单处理详情
	 * 
	 * @param id
	 * @param loginName
	 * @return
	 */
	public String getSuggetionByID(Integer id, String loginName) {
		Suggestion suggestion = (Suggestion) cs.get(Suggestion.class, id);
		if (suggestion == null || !loginName.equals(suggestion.getLoginName())) {
			return "";
		}
		return OAUtil.workflowProgress(suggestion.getRun_id(), suggestion.getFlow_id());
	}

	/**
	 * 创建oa投诉建议工作流
	 * 
	 * @param loginName
	 * @param type
	 * @param content
	 * @return
	 */
	public String createOAWorkFlow(String loginName, String type, String content) {
		// 每天最多提交三条投诉建议
		String sqlStr = "select count(*) from suggest where loginname=:loginname and createtime>=:timeStart and createtime<=:timeEnd";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginName);
		try {
			Date now = new Date();
			params.put("timeStart", DateUtil.processDayStart(now));
			params.put("timeEnd", DateUtil.processDayEnd(now));
		} catch (Exception e1) {
			e1.printStackTrace();
			return "日期转换异常";
		}

		if (cs.getCount(sqlStr, params) >= 3) {
			return "休息一下，明天再来";
		}
		String run_id = OAUtil.createWorkFlow(loginName, type, content);
		if (run_id == null)
			return "服务异常，请稍后再试";
		try {
			Integer.parseInt(run_id);
		} catch (NumberFormatException e) {
			return "服务异常，请稍后再试";
		}
		cs.save(new Suggestion(loginName, OAUtil.flow_id, run_id, type, new Date()));
		return "提交成功";
	}

	public List<SignRecord> findSignrecord(String loginName) {

		return transferService.findSignrecord(loginName);
	}

	/**
	 * 签到操作
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String doSignRecord(String loginname, String device, String level) throws Exception {
		return SynchronizedUtil.getInstance().doSignRecord(transferService, loginname, device, level);
	}

	public Boolean checkRecord(String loginname, String device, String level) throws Exception {
		return SynchronizedUtil.getInstance().checkRecord(transferService, loginname, device, level);
	}

	/**
	 * 签到赠送金额转账至游戏账号
	 * 
	 * @param loginname
	 * @param remit
	 * @param signType
	 *            转入的游戏类型/ttg/pt
	 * @return
	 */
	public String transferSign(String loginname, Double remit, String signType) {
		if (remit.intValue() < 5) {
			return "签到金额需大于或等于5";
		}
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (StringUtils.isNotBlank(strLimit)) {
			return strLimit;
		}
		try {
			return SynchronizedUtil.getInstance().transferSign(selfYouHuiService, loginname, remit, signType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询红包奖金余额
	 * 
	 * @param loginname
	 * @return
	 */
	public String queryHBbonus(String loginname, Integer phone) {
		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		if (phone != null)
			dc.add(Restrictions.eq("phone", phone));

		dc.add(Restrictions.eq("username", loginname));
		List<HBbonus> list = this.proposalService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			return list.get(0).getMoney() + "";
		} else {
			return "0";
		}
	}

	/**
	 * 柱状态 数据
	 * 
	 * @param loginname
	 * @return
	 */
	public PlatformVo queryagprofit(String loginname) {
		PlatformVo vo = new PlatformVo();
		// 提款
		DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
		proposalSavedc.add(Restrictions.eq("loginname", loginname));
		proposalSavedc.add(Restrictions.eq("flag", 2));
		proposalSavedc.add(Restrictions.eq("type", 503));
		proposalSavedc.add(Restrictions.gt("createTime", DateUtil.getStartTime()));
		proposalSavedc.add(Restrictions.le("createTime", DateUtil.getEndTime()));
		proposalSavedc.setProjection(Projections.sum("amount"));
		List saveList = proposalService.findByCriteria(proposalSavedc);
		double t = 0.0;
		if (null != saveList.get(0)) {
			t = (Double) saveList.get(0);
		}
		vo.setTiKuan(t);

		// 存款
		String mcRecord = checkMCRecord(loginname);
		String onlineRecord = checkOnlineRecord(loginname);
		Double mc = Double.valueOf(mcRecord);
		Double online = Double.valueOf(onlineRecord);
		vo.setMc(mc);
		vo.setOnline(online);

		// 盈利=提款-存款
		vo.setYingLi(t - mc - online);

		// 所有平台流水
		DetachedCriteria agProfit = DetachedCriteria.forClass(AgProfit.class);
		agProfit.add(Restrictions.eq("loginname", loginname));
		agProfit.add(Restrictions.gt("createTime", DateUtil.getStartTime()));
		agProfit.add(Restrictions.le("createTime", DateUtil.getEndTime()));
		agProfit.setProjection(Projections.sum("bettotal"));
		List list = proposalService.findByCriteria(agProfit);
		double d = 0.0;
		if (null != list.get(0)) {
			d = (Double) list.get(0);
		}
		vo.setTouZhu(d);
		return vo;
	}

	public String transferInforHB(String loginname, Double remit, String signType, Integer deposit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (StringUtils.isNotBlank(strLimit)) {
			return strLimit;
		}
		String str = "";
		if (deposit == 0)
			str = checkSystemConfig("type023", "001", "否");
		if (deposit == 1)
			str = checkSystemConfig("type024", "001", "否");
		if (StringUtil.isEmpty(str)) {
			return "奖金转入游戏维护中，请稍后再试";
		}
		String[] arr = str.split("#");
		String betMultiples = arr[1];

		try {
			return SynchronizedUtil.getInstance().transferInforHB(selfYouHuiService, loginname, remit, signType,
					betMultiples, deposit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询玩家可领红包
	 * 
	 * @param loginname
	 * @return
	 */
	public List queryHBSelect(Integer vip, Integer type) {
		DetachedCriteria dc = DetachedCriteria.forClass(HBConfig.class);
		dc.add(Restrictions.le("starttime", new Date()));
		dc.add(Restrictions.ge("endtime", new Date()));
		if (type != null)
			dc.add(Restrictions.eq("type", type));

		dc.add(Restrictions.like("vip", vip.toString(), MatchMode.ANYWHERE));
		dc.add(Restrictions.eq("isused", 1));
		List<HBConfig> list = this.proposalService.findByCriteria(dc);
		return list;
	}

	/**
	 * 领取红包
	 * 
	 * @param loginname
	 * @param hbID
	 * @return
	 * @throws Exception
	 */

	public String doHB(String loginname, Integer type, Integer hbID) throws Exception {
		String str = checkSystemConfig("type023", "001", "否");
		if (StringUtil.isEmpty(str)) {
			return "该活动已停止";
		}

		HBConfig config = (HBConfig) proposalService.get(HBConfig.class, hbID);
		if (config == null)
			return "该红包不存在";
		if (config.getIsused() == 0)
			return "该红包已关闭";

		String msg = "";
		Boolean lockFlag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		InterProcessMutex lock = null;

		try {

			lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginname));
			lockFlag = lock.acquire(Integer.parseInt(Configuration.getInstance().getValue("zk.lock.timeout")),
					TimeUnit.SECONDS);
		} catch (Exception e) {

			e.printStackTrace();
			log.error("玩家：" + loginname + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}

		try {

			if (lockFlag) {

				log.info("正在处理玩家" + loginname + "的请求，执行时间：" + time);

				msg = UserSynchronizedPool.getSynchUtilByUser(loginname).doHB(proposalService, cs, loginname, config,
						hbID);

				log.info("处理完成玩家" + loginname + "的请求，执行时间：" + time + "，结束时间：" + sdf.format(new Date()));
			} else {

				log.error("玩家：" + loginname + "未能获取锁，无法执行请求对应的方法....");

				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.error("执行玩家：" + loginname + "请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {

			if (lockFlag) {

				try {

					if (null != lock) {

						lock.release();
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("玩家：" + loginname + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}
		return msg;

	}

	/**
	 * E68账户转账 至 dt平台
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferDtIn(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			// return
			// SynchrTtInUtil.getInstance().transferTtAndSelfYouHuiIn(transferService,
			// seqId, loginname, remit, null);
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferDtAndSelfYouHuiIn(transferService, seqId,
					loginname, remit, null);
		}
		return strLimit;
	}

	/**
	 * DT 至 E68账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferDtOut(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		// return
		// SynchrTtOutUtil.getInstance().transferTtAndSelfYouHuiOut(transferService,
		// seqId, loginname, remit);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferDtAndSelfYouHuiOut(transferService, seqId,
				loginname, remit);
	}

	/**
	 * 查询奖金余额
	 * 
	 * @param loginname
	 * @return
	 */
	public Double querySignAmount(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list = slaveService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			SignAmount sa = (SignAmount) list.get(0);
			return sa.getAmountbalane();
		} else {
			return 0.0;
		}

	}

	/**
	 * 查询奖金余额
	 * 
	 * @param loginname
	 * @return
	 */
	public SignAmount querySignAmountObj(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list = proposalService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			SignAmount sa = (SignAmount) list.get(0);
			return sa;
		} else {
			SignAmount sa = new SignAmount();
			sa.setAmountbalane(0.0);
			return sa;
		}

	}

	/**
	 * 查看今日签到没
	 * 
	 * @param loginname
	 * @return
	 */
	public Boolean isTodaySign(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));// 未删除
		dc.add(Restrictions.ge("createtime", DateUtil.getToday()));
		List<AgentVip> list = cs.findByCriteria(dc);
		return (null != list && list.size() > 0);
	}

	/**
	 * 查看昨日签到没
	 * 
	 * @param loginname
	 * @return
	 */
	public Boolean isYesterdaySign(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));// 未删除
		dc.add(Restrictions.eq("type", "0"));// 签到记录
		dc.add(Restrictions.lt("createtime", DateUtil.getToday())); // 小余今天
		dc.add(Restrictions.ge("createtime", DateUtil.getYesterday())); // 大于等于昨天
		List<AgentVip> list = cs.findByCriteria(dc);
		return (null != list && list.size() > 0);
	}

	/**
	 * 获取微信支付订单号
	 * 
	 * @param loginname
	 * @param OrdAmt
	 * @param constID
	 * @return
	 */
	public String getOrderWeiXinPayNo(String loginname, Double OrdAmt, String constID) {
		// 获取该订单号
		String orderNo = seqService.generateWeiXinOrderNoID();
		if (orderNo == null) {
			return null;
		}
		orderNo = "ewx_" + loginname + "_" + orderNo;
		// 存入订单
		String str = announcementService.addPayorderWeiXin(orderNo, OrdAmt, loginname);
		if ("ok".equals(str)) {
			return orderNo;
		} else {
			return null;
		}
	}

	/**
	 * 微信支付回调
	 */
	public String WeixinpayReturn(String out_trade_no, String OrdAmt, String flag) {
		String aa = SynchronizedUtil.getInstance().weixinpayreturn(announcementService, out_trade_no, flag, OrdAmt);
		return aa;
	}

	public String checkMCRecord(String loginname) {
		DetachedCriteria dc2 = DetachedCriteria.forClass(Proposal.class);
		dc2.add(Restrictions.eq("loginname", loginname));
		dc2.add(Restrictions.eq("type", 502));
		dc2.add(Restrictions.eq("flag", 2));
		dc2.add(Restrictions.ge("createTime", DateUtil.getDate(0)));
		dc2.add(Restrictions.le("createTime", DateUtil.getDate(1)));
		dc2.setProjection(Projections.sum("amount"));
		List list2 = proposalService.findByCriteria(dc2);
		Double result2 = (Double) list2.get(0);
		if (null == result2) {
			result2 = 0.0;
		}
		return result2.toString();
	}

	public String checkOnlineRecord(String loginname) {
		DetachedCriteria dc1 = DetachedCriteria.forClass(Payorder.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.eq("type", 0));
		dc1.add(Restrictions.eq("flag", 0));
		dc1.add(Restrictions.ge("createTime", DateUtil.getDate(0)));
		dc1.add(Restrictions.le("createTime", DateUtil.getDate(1)));
		dc1.setProjection(Projections.sum("money"));
		List list1 = proposalService.findByCriteria(dc1);
		Double result1 = (Double) list1.get(0);
		if (null == result1) {
			result1 = 0.0;
		}
		return result1.toString();
	}

	/**
	 * 检查该玩家某个时间段的存款量
	 * 
	 * @param loginname
	 * @return
	 */
	public String checkSignRecord(String loginname, String begindate, String enddate) {
		Double sumMoney = 0.0;
		DetachedCriteria dc1 = DetachedCriteria.forClass(Payorder.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.eq("type", 0));
		try {
			dc1.add(Restrictions.ge("createTime", DateUtil.fmtStandard(begindate)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(enddate)) {
			try {
				dc1.add(Restrictions.le("createTime", DateUtil.fmtStandard(enddate)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dc1.setProjection(Projections.sum("money"));
		List list1 = slaveService.findByCriteria(dc1);
		Double result1 = (Double) list1.get(0);
		if (null == result1) {
			result1 = 0.0;
		}
		DetachedCriteria dc2 = DetachedCriteria.forClass(Proposal.class);
		dc2.add(Restrictions.eq("loginname", loginname));
		dc2.add(Restrictions.eq("type", 502));
		dc2.add(Restrictions.eq("flag", 2));
		try {
			dc2.add(Restrictions.ge("createTime", DateUtil.fmtStandard(begindate)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(enddate)) {
			try {
				dc2.add(Restrictions.le("createTime", DateUtil.fmtStandard(enddate)));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dc2.setProjection(Projections.sum("amount"));
		List list2 = slaveService.findByCriteria(dc2);
		Double result2 = (Double) list2.get(0);
		if (null == result2) {
			result2 = 0.0;
		}
		sumMoney = result1 + result2;
		return sumMoney.toString();
	}

	// 获取乐富微信支付订单号
	public String getLfWxOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateLfWxOrderNoID(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderLfwx(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	/**
	 * 乐富微信支付回调
	 * 
	 * @param orderid
	 * @param money
	 * @param mechantcode
	 * @param flag
	 *            0:自动回调
	 * @return
	 */
	public String addPayLfWxzf(String orderid, Double money, String loginname, String flag) {
		String aa = SynchronizedUtil.getInstance().addPayLfWxzf(announcementService, orderid, money, loginname, flag);
		if (aa == null) {
			try {
				String content = this.createMoneyContent(loginname, money);
				String smsmsg = this.sendSMS(loginname, "9", money, content);
				log.info(loginname + "支付成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayLfWxzf SMS ERROR:", e);
			}
		}
		return aa;
	}

	/**
	 * 查询当天微信在线支付的量 payPlatform weixinpay
	 * 
	 * @return
	 */
	public String getsumMoney(String payPlatform) {
		DetachedCriteria dc1 = DetachedCriteria.forClass(Payorder.class);
		dc1.add(Restrictions.eq("payPlatform", payPlatform));
		dc1.add(Restrictions.eq("type", 0));
		dc1.add(Restrictions.ge("createTime", DateUtil.getToday()));
		System.out.println("DateUtil.getToday()==" + DateUtil.getToday());
		dc1.add(Restrictions.le("createTime", DateUtil.getTomorrow()));
		System.out.println("DateUtil.getTomorrow()==" + DateUtil.getTomorrow());
		dc1.setProjection(Projections.sum("money"));
		List list1 = slaveService.findByCriteria(dc1);
		Double sumMoney = (Double) list1.get(0);
		if (null == sumMoney) {
			sumMoney = 0.0;
		}
		return sumMoney.toString();
	}

	/**
	 * 推荐好友奖励转入游戏
	 * 
	 * @param loginname
	 * @param remit
	 * @param signType
	 *            转入的游戏类型/ttg/pt
	 * @return
	 * @throws Exception
	 */
	public String transferFriend(String loginname, Double remit, String signType) throws Exception {
		// 好友推荐金转出正在维护
		Const c = (Const) this.transferService.get(Const.class, "好友推荐金转出");
		if (c != null && "0".equals(c.getValue())) {
			return "好友推荐金转出正在维护...";
		}
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (StringUtils.isNotBlank(strLimit)) {
			return strLimit;
		}
		return SynchronizedUtil.getInstance().transferFriend(selfYouHuiService, loginname, remit, signType);
	}

	/**
	 * 查询好友推荐奖金余额
	 * 
	 * @param loginname
	 * @return
	 */
	public String queryFriendbonus(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		List<Friendbonus> list = this.slaveService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			return list.get(0).getMoney() + "";
		} else {
			return "0";
		}
	}

	/**
	 * 查询推荐奖励收入或支出记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page friendbonusrecord(String loginname, String type, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonusrecord.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		if (!StringUtil.isEmpty(type)) {
			dc.add(Restrictions.eq("type", type));
		}
		// dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Friendbonusrecord> list = page.getPageContents();
		for (Friendbonusrecord p : list) {
			if (!StringUtil.isEmpty(p.getDownlineuser())) {
				p.setDownlineuser(p.getDownlineuser().substring(0, 3) + "****");
			}
			if (p.getType().equals("1")) {
				p.setType("收入");
			} else {
				p.setDownlineuser(p.getToplineuser().substring(0, 3) + "****");
				p.setType("支出");
			}
		}
		return page;
	}

	/**
	 * 查询推荐的注册成功的账号
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page friendintroduce(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Friendintroduce.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		// dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Friendintroduce> list = page.getPageContents();
		for (Friendintroduce p : list) {
			if (!StringUtil.isEmpty(p.getDownlineuser())) {
				p.setDownlineuser(p.getDownlineuser().substring(0, 3) + "****");
			}
		}
		return page;
	}

	/**
	 * 返回Users
	 * 
	 * @param loginname
	 * @return
	 */
	public Users getUserById(String idStr) {
		Integer id = null;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
			return null;
		}
		if (null == id) {
			return null;
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		dc = dc.add(Restrictions.eq("id", id));
		List<Users> list = slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (Users) list.get(0);
		}
		return null;
	}

	/**
	 * 积分兑换成奖金
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferPoint(String loginname, Double remit) {
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		dc = dc.add(Restrictions.eq("id", "积分兑换奖金"));
		dc = dc.add(Restrictions.eq("value", "1"));
		List<Const> constPay = slaveService.findByCriteria(dc);
		// 后台是否关闭此 存款
		if (null == constPay || constPay.size() <= 0 || constPay.get(0) == null) {
			return "积分兑换正在维护！";
		}
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		Users user = getUser(loginname);
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferPoint(proposalService, user, remit);
	}

	/**
	 * 查询积分余额
	 * 
	 * @param loginname
	 * @return
	 */
	public TotalPoint queryPoints(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(TotalPoint.class);
		dc.add(Restrictions.eq("username", loginname));
		List<TotalPoint> list = this.slaveService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 积分记录
	 * 
	 * @param loginname
	 * @param pageIndex
	 * @param size
	 * @return
	 */
	public Page querypointRecord(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(DetailPoint.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.in("type", new String[] { "0", "1" }));
		// dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<DetailPoint> list = page.getPageContents();
		for (DetailPoint p : list) {
			p.setCreateday(DateUtil.fmtDateForBetRecods(p.getCreatetime()));
			if (p.getType().equals("0")) {
				p.setType("积分收入");
			} else {
				p.setType("积分支出");
			}
		}
		return page;
	}

	/**
	 * 新贝微信支付
	 * 
	 * @param loginname
	 * @param money
	 * @return
	 */
	public String getXinBWxOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateXinBOrderNoID(loginname);
		// 存入改订单
		orderNo = "e68_" + orderNo;
		String str = announcementService.addSaveOrderXinBwx(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	public String addPayXbWxzf(String orderid, Double money, String loginname, String flag) {
		String aa = UserSynchronizedPool.getSynchUtilByUser(loginname).addPayXbWx(announcementService, orderid, money,
				loginname, flag);
		if (aa == null) {
			try {
				String content = this.createMoneyContent(loginname, money);
				String smsmsg = this.sendSMS(loginname, "9", money, content);
				log.info(loginname + "支付成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayLfWxzf SMS ERROR:", e);
			}
		}
		return aa;
	}

	public String getKdZfOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateKdZfOrderNoID(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderKdZf(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	public String addPayKdZf(String orderid, Double money, String loginname, String flag) {
		String aa = UserSynchronizedPool.getSynchUtilByUser(loginname).addPayKdZf(announcementService, orderid, money,
				loginname, flag);
		if (aa == null) {
			try {
				String content = this.createMoneyContent(loginname, money);
				String smsmsg = this.sendSMS(loginname, "9", money, content);
				log.info(loginname + "补单成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayKdZf SMS ERROR:", e);
			}
		}
		return aa;
	}

	public String addPayKdWxZf(String orderid, Double money, String loginname, String flag) {
		String res = UserSynchronizedPool.getSynchUtilByUser(loginname).addPayKdWxZf(announcementService, orderid,
				money, loginname, flag);
		return res;
	}

	public String getKdWxZfOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateKdWxZfOrderNoID(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderKdWxZf(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	/****
	 * 口袋微信支付2
	 * 
	 * @param loginname
	 * @param money
	 * @return
	 */
	public String getKdWxZfOrderNo2(String loginname, Double money) {
		String orderNo = seqService.generateKdWxZfOrderNoID2(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderKdWxZf2(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	public String addPayKdWxZf2(String orderid, Double money, String loginname, String flag) {
		String res = UserSynchronizedPool.getSynchUtilByUser(loginname).addPayKdWxZf2(announcementService, orderid,
				money, loginname, flag);
		return res;
	}

	public Integer judgeOnlineDepositAmountWay(String payStyle) {
		// 0没有限制 1为1000以及以下 2为1000以上
		String payP = payStyle + "1000+";
		String payS = payStyle + "1000-";
		Const cpayP = (Const) slaveService.get(Const.class, payP);
		Const cpayS = (Const) slaveService.get(Const.class, payS);
		if (null == cpayP || null == cpayS) {
			return 0;
		}
		String payP_val = cpayP.getValue();
		String payS_val = cpayS.getValue();
		if (payP_val.equals("1") && payS_val.equals("1")) {
			return 0;
		}
		if (payS_val.equals("1")) {
			return 1;
		}
		if (payP_val.equals("1")) {
			return 2;
		}
		return 0;
	}

	public String getHhbZfOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateHhbZfOrderNoID(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderHhbZf(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	public String addPayHhbZf(String orderid, Double money, String loginname, String flag) {
		String aa = UserSynchronizedPool.getSynchUtilByUser(loginname).addPayHhbZf(announcementService, orderid, money,
				loginname, flag);
		return aa;
	}

	// 汇付宝微信支付
	public String getHhbWxZfOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateHhbWxZfOrderNoID(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderHhbWxZf(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	public String addPayHhbWxZf(String orderid, Double money, String loginname, String flag) {
		String aa = UserSynchronizedPool.getSynchUtilByUser(loginname).addPayHhbWxZf(announcementService, orderid,
				money, loginname, flag);
		if (aa == null) {
			try {
				String content = this.createMoneyContent(loginname, money);
				String smsmsg = this.sendSMS(loginname, "9", money, content);
				log.info(loginname + "支付成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayHhbWxZf SMS ERROR:", e);
			}
		}
		return aa;
	}

	/*****
	 * 聚宝支付宝
	 * 
	 * @param loginname
	 * @param money
	 * @return
	 */
	public String getJubZfbOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateJubZfbOrderNoID(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderJubZfb(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	// 聚宝支付宝回调
	public String addPayJubZfB(String orderid, Double money, String loginname, String flag) {
		String aa = UserSynchronizedPool.getSynchUtilByUser(loginname).addPayJubZfb(announcementService, orderid, money,
				loginname, flag);
		if (aa == null) {
			try {
				String content = this.createMoneyContent(loginname, money);
				String smsmsg = this.sendSMS(loginname, "9", money, content);
				log.info(loginname + "支付成功发送短信：" + smsmsg);
			} catch (Exception e) {
				log.error("addPayJubZfB SMS ERROR:", e);
			}
		}
		return aa;
	}

	/**
	 * 守护女神报名
	 * 
	 * @param loginname
	 * @param goddess
	 * @return
	 * @throws Exception
	 */
	public String doGoddessApply(String loginname, String goddess) throws Exception {

		return SynchronizedUtil.getInstance().doGoddessApply(transferService, loginname, goddess);
	}

	/**
	 * 查看守护女神报名
	 * 
	 * @param loginname
	 * @param goddess
	 * @return
	 * @throws Exception
	 */
	public String getGoddessApply(String loginname) throws Exception {

		Goddessrecord record = (Goddessrecord) this.slaveService.get(Goddessrecord.class, loginname);
		if (record != null) {
			return "您的守护女神：" + Goddesses.getName(record.getGoddessname());
		} else {
			return "";
		}
	}

	/**
	 * 根据用户名查看守护女神数据
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public List<Goddessrecord> getRecordByLoginname(String loginname) throws Exception {
		if (StringUtils.isEmpty(loginname))
			return null;
		DetachedCriteria dc = DetachedCriteria.forClass(Goddessrecord.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List<Goddessrecord> list = slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0) {
			Goddessrecord vo = list.get(0);
			vo.setGoddessname(Goddesses.getName(vo.getGoddessname()));
			return list;
		}
		return null;
	}

	/**
	 * 查看女神排行榜
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Goddessrecord> getGoddesses() throws Exception {
		String sql = " SELECT goddessname,SUM(flowernum) as flowernum,SUM(bettotal) as bettotal,COUNT(loginname) as usernum from goddessrecord GROUP BY goddessname ORDER BY flowernum DESC,bettotal DESC,usernum ASC ";
		Session session = slaveService.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sql);
		List<Object[]> list = query.list();
		if (session != null) {
			session.close();
		}

		List<Goddessrecord> listVO = new ArrayList<Goddessrecord>();
		Goddesses[] g = Goddesses.values();
		for (int i = 0; i < g.length; i++) {
			Goddessrecord vo = new Goddessrecord();
			vo.setFlowernum(0);
			vo.setGoddessname(g[i].getName());
			vo.setPeonum(0);
			listVO.add(vo);
		}

		if (list != null && list.size() > 0) {

			for (int i = 0; i < list.size(); i++) {
				Object[] obj = list.get(i);
				String goddessname = Goddesses.getName(obj[0].toString());

				Iterator<Goddessrecord> it = listVO.iterator();
				while (it.hasNext()) {
					Goddessrecord goddess = it.next();
					if (goddess.getGoddessname().equals(goddessname)) {
						it.remove();
					}
				}

				Goddessrecord vo = new Goddessrecord();
				vo.setGoddessname(goddessname);
				vo.setFlowernum(Integer.parseInt(obj[1] == null ? "0" : obj[1].toString()));
				vo.setPeonum(Integer.parseInt(obj[3] == null ? "0" : obj[3].toString()));
				listVO.add(vo);
			}
		}
		return listVO;
	}

	/**
	 * 守护女神所有排名
	 */
	public Page getFlowerAllRecord(Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(Goddessrecord.class);

		// dc.addOrder(Order.desc("flowernum"));
		Order o = Order.desc("flowernum");

		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Goddessrecord> list = page.getPageContents();
		if (list != null && list.size() > 0) {
			for (Goddessrecord p : list) {
				if (!StringUtil.isEmpty(p.getLoginname())) {
					p.setLoginname(p.getLoginname().substring(0, 3) + "****");
					p.setGoddessname(Goddesses.getName(p.getGoddessname()));
					p.setCouponnum(null);
				}
			}
		}

		return page;
	}

	/**
	 * 单身狗活动个人排名
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public List<SingleParty> getSelfSingleParty(String loginname) throws Exception {
		if (StringUtils.isEmpty(loginname))
			return null;
		DetachedCriteria dc = DetachedCriteria.forClass(SingleParty.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List<SingleParty> list = slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	/**
	 * 单身狗活动排名
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public Page getNineDaysBets(Integer pageIndex, Integer size) throws Exception {

		Date agStart = DateUtil.parseDateForStandard("2016-08-02 00:00:00");
		Date agEnd = DateUtil.parseDateForStandard("2016-08-10 23:59:59");
		Date ptStart = DateUtil.parseDateForStandard("2016-08-01 00:00:00");
		Date ptEnd = DateUtil.parseDateForStandard("2016-08-09 00:00:00");

		Date endDate = DateUtil.parseDateForStandard("2016-08-10 13:00:00");// 最后一次排名时间

		Date now = new Date();
		String today = DateUtil.fmtYYYY_MM_DD(now);

		Date limit = DateUtil.parseDateForStandard(today + " 13:00:00");
		if (limit.compareTo(endDate) > 0) {
			limit = endDate;
		}
		String rankdate = null;
		if (now.compareTo(limit) < 0) {
			rankdate = DateUtil.fmtYYYY_MM_DD(DateUtil.getDateAfter(now, -1));
		} else {
			rankdate = DateUtil.fmtYYYY_MM_DD(limit);
		}
		SynchronizedUtil.getInstance().updateSingleParty(proposalService, agStart, agEnd, ptStart, ptEnd, rankdate);

		DetachedCriteria dc = DetachedCriteria.forClass(SingleParty.class);
		// dc.addOrder(Order.asc("ranking"));
		Order o = Order.asc("ranking");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<SingleParty> list = page.getPageContents();

		if (list != null && list.size() > 0) {
			for (SingleParty p : list) {
				if (!StringUtil.isEmpty(p.getLoginname())) {
					p.setLoginname(p.getLoginname().substring(0, 3) + "****");
				}
			}
		}

		return page;
	}

	private Date getDateAfter(Date time, int i) {
		if (time == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + i);
		return c.getTime();
	}

	/**
	 * 游戏状态查询
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public GameStatus getGameStatus(String username) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(GameStatus.class);
		dc.add(Restrictions.eq("userName", username));
		List<GameStatus> list = this.slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 游戏状态查询
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public String saveOrUpdateGameStatus(String username, String gamelist) throws Exception {
		GameStatus gs = new GameStatus();
		gs.setUserName(username);
		gs.setGameList(gamelist);
		this.cs.saveOrUpdate(gs);
		return "更新成功";
	}

	/**
	 * N2Live登录验证
	 * 
	 * @param id
	 * @param userid
	 * @param pwd
	 * @return xml
	 */
	public String nTwoGameLoginValidation(String id, String userid, String pwd) {
		return cs.nTwoGameLoginValidation(new LoginValidationBean(id, userid, pwd));
	}

	/**
	 * N2Live 自动登录验证
	 * 
	 * @param map
	 * @return
	 */
	public String nTwoGameAutoLoginValidation(String elementId, String userid, String uuid, String clinetIp) {
		return cs.nTwoGameAutoLoginValidation(elementId, userid, uuid, clinetIp);
	}

	/**
	 * NTwo Live 获取财务后台登入ticket
	 * 
	 * @param elementId
	 * @param username
	 * @param date
	 * @param sign
	 * @return
	 */
	public String nTwoGetTicketValidation(String elementId, String username, String date, String sign) {
		return cs.nTwoGetTicketValidation(elementId, username, date, sign);
	}

	/**
	 * NTwo Live 财务后台登入验证
	 * 
	 * @param loginname
	 * @param token
	 * @param remoteip
	 * @param city
	 * @param clientos
	 * @return
	 */
	public LoginInfo nTwoTicketlogin(String loginname, String token, String remoteip, String city, String clientos) {
		return userWebSerivce.nTwoTicketlogin(loginname, token, remoteip, city, clientos);
	}

	public NTwoCheckClientResponseBean getNTwoCheckClientResponseBean(String userid) {
		try {
			return NTwoUtil.checkClient(userid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 户内转账 NTwo账户转账 至 E 路发账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutNTwo(String loginname, Double remit) {
		String seqId = seqService.generateTransferID();
		return UserSynchronizedPool.getSynchUtilByUser(loginname).transferOutNTwo(transferService, seqId, loginname,
				remit);
	}

	public String nTwoSingleLoginUrl(String loginname) {
		return cs.nTwoSingleLoginUrl(loginname);
	}

	public String nTwoAppLoginUrl(String loginname) {
		return cs.nTwoAppLoginUrl(loginname);
	}

	/**
	 * EBetApp Token登入
	 *
	 * @param accessToken
	 * @param remoteIp
	 * @param city
	 * @param clientOs
	 * @return
	 */
	public LoginInfo loginEBetAppByToken(String accessToken, String remoteIp, String city, String clientOs)
			throws IllegalAccessException {
		String ioBB = "";
		return userWebSerivce.loginByAccessToken("ebetapp", accessToken, remoteIp, city, clientOs, ioBB);
	}

	/**
	 * getEBetVO
	 * 
	 * @param loginname
	 * @return
	 */
	public EbetH5VO getEBetVO(String loginname) {

		if (StringUtils.isBlank(loginname)) {
			log.error("getEBetVO fail:loginname:" + loginname);
			return null;
		}

		TokenInfo tokenInfo = this.loginTokenService.proccessTokenInfoWithValid("ebetapp", loginname);

		EbetH5VO ebetH5VO = new EbetH5VO();
		ebetH5VO.setAccessToken(tokenInfo.getToken());
		ebetH5VO.setChannelId(EBetAppUtil.DEFAULT_CHANNEL_ID);
		ebetH5VO.setLoginname(loginname);
		log.error("getEBetVO success:loginname:" + loginname + ",token:" + ebetH5VO.getAccessToken());
		return ebetH5VO;
	}

	/**
	 * EBetApp 登入
	 *
	 * @param loginName
	 * @param password
	 * @param remoteIp
	 * @param city
	 * @param clientOs
	 * @return
	 */
	public LoginInfo loginEBetApp(String loginName, String password, String remoteIp, String city, String clientOs,
			int eventType) throws IllegalAccessException {
		String ioBB = "";
		// eventType == 3 || eventType 4 , password is encryptPassword
		if (eventType == 3 || eventType == 4) {
			return userWebSerivce.loginByEncryptPassword(loginName, password, remoteIp, city, clientOs, ioBB);
		} else {
			return userWebSerivce.loginByPassword(loginName, password, remoteIp, city, clientOs, ioBB);
		}
	}

	public String getEbetAppBalance(String loginname) {
		Double balance;
		try {
			balance = EBetAppUtil.getBalance(loginname);
		} catch (Exception e) {
			balance = null;
		}
		return balance == null ? null : balance.toString();
	}

	/**
	 * 转账至EBetApp
	 *
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferToEBetApp(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			String transRtn = "";
			// 同步运行交易和日志记录
			transRtn = UserSynchronizedPool.getSynchUtilByUser(loginname).transferToEBetApp(transferService, seqId,
					loginname, remit);
			if (StringUtils.isEmpty(transRtn)) {
				return null;
			} else {
				return transRtn;
			}
		} else {
			return strLimit;
		}
	}

	/**
	 * 转账至EBetApp
	 *
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferFromEBetApp(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			String transRtn = "";
			// 同步运行交易和日志记录
			transRtn = UserSynchronizedPool.getSynchUtilByUser(loginname).transferFromEBetApp(transferService, seqId,
					loginname, remit);
			if (StringUtils.isEmpty(transRtn)) {
				return null;
			} else {
				return transRtn;
			}
		} else {
			return strLimit;
		}
	}

	public Bankinfo getWxBankRedirectInfo(String loginname) {
		Bankinfo wxBank = new Bankinfo();
		Users user = getUser(loginname);
		if (null == user) {
			return null;
		}
		try {
			DetachedCriteria dc4 = DetachedCriteria.forClass(Bankinfo.class);
			dc4 = dc4.add(Restrictions.eq("type", 1));
			dc4 = dc4.add(Restrictions.eq("isshow", 1));
			dc4 = dc4.add(Restrictions.eq("useable", 0));
			dc4 = dc4.add(Restrictions.eq("bankname", "微信"));
			dc4 = dc4.add(Restrictions.like("userrole", "%" + user.getLevel() + "%"));

			if (user.getLevel() <= 1) {
				DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
				proposalSavedc.add(Restrictions.eq("loginname", loginname));
				proposalSavedc.add(Restrictions.eq("flag", 2));
				proposalSavedc.add(Restrictions.eq("type", 502));
				proposalSavedc.setProjection(Projections.sum("amount"));
				List saveList = proposalService.findByCriteria(proposalSavedc);

				DetachedCriteria payorderdc = DetachedCriteria.forClass(Payorder.class);
				payorderdc.add(Restrictions.eq("loginname", loginname));
				payorderdc.add(Restrictions.eq("flag", 0));
				payorderdc.add(Restrictions.eq("type", 0));
				payorderdc.setProjection(Projections.sum("money"));
				List payorderList = proposalService.findByCriteria(payorderdc);

				Double amount = 0.00;
				if ((saveList != null && !saveList.isEmpty() && null != saveList.get(0)
						|| payorderList != null && !payorderList.isEmpty() && null != payorderList.get(0))) {
					if (null != payorderList.get(0)) {
						amount += (Double) payorderList.get(0);
					}
					if (null != saveList.get(0)) {
						amount += (Double) saveList.get(0);
					}
				}
				/*
				 * if(amount<5000){
				 * zfbBank.setMassage("您的活跃度没有达到,暂时不能使用支付宝附言存款,感谢您的支持!"); return
				 * zfbBank ; }
				 */
				if (amount < 1000) {// E
					dc4 = dc4.add(Restrictions.eq("vpnname", "F"));
				} else if (amount >= 1000 && amount < 5000) {
					dc4 = dc4.add(Restrictions.eq("vpnname", "E"));
				} else if (amount >= 5000 && amount < 10000) {// A
					dc4 = dc4.add(Restrictions.eq("vpnname", "A"));
				} else if (amount >= 10000 && amount < 50000) {// B
					dc4 = dc4.add(Restrictions.eq("vpnname", "B"));
				} else if (amount >= 50000 && amount < 100000) {// C
					dc4 = dc4.add(Restrictions.eq("vpnname", "C"));
				} else if (amount >= 100000) {// D
					dc4 = dc4.add(Restrictions.eq("vpnname", "D"));
				} else {
					wxBank.setMassage("微信未找到相应存款账号");
					return wxBank;
				}
			}
			dc4.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list4 = proposalService.findByCriteria(dc4);
			if (list4 != null && list4.size() > 0 && list4.get(0) != null) {
				wxBank = list4.get(0);
			} else {
				wxBank.setMassage("您的活跃度没有达到,暂时不能使用微信存款方式,请您联系在线客服");
				return wxBank;
			}
		} catch (Exception e) {
			log.error(loginname + e.getMessage());
		}

		return wxBank;
	}

	public String checkthirdOrder(String tempDepositTime, String thirdOrder) {
		String result = "";
		Byte status = cs.checkthirdOrder(tempDepositTime, thirdOrder);
		if (status == null) {
			result = "该订单不存在，请核实订单号及存款时间是否正确!";
		} else {
			if (status == 1) {
				result = "该订单已处理，请核实";
			}
		}
		return result;
	}

	public String submitUrgeOrder(UrgeOrder urgeOrder) {
		urgeOrder.setCreatetime(new Date());
		urgeOrder.setUpdatetime(new Date());
		String tempDepositTime = urgeOrder.getTempDepositTime();
		urgeOrder.setDepositTime(DateUtil.parseDateForStandard(tempDepositTime));
		String result = null;
		try {
			cs.save(urgeOrder);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("催账保存异常：" + e.getMessage());
			result = "系统繁忙，请联系客服！";
		}
		result = "操作成功";
		return result;
	}

	public String checkUrgeOrderCount(String loginname) {
		List<UrgeOrder> list = cs.queryUrgeOrderList(loginname);
		String errMsg = "";
		if (list.size() == 5) {
			errMsg = "每天只能提交待处理+处理失败的催账单5条";
		}
		return errMsg;
	}

	@SuppressWarnings("unchecked")
	public Page queryUrgeOrderPage(String loginname, Integer pageIndex, Integer size) {
		log.info("function-->queryUrgeOrderPage");
		DetachedCriteria dc = DetachedCriteria.forClass(UrgeOrder.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		// dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<UrgeOrder> list = page.getPageContents();
		for (UrgeOrder u : list) {
			if (u.getDepositTime() != null) {
				u.setTempDepositTime(sf.format(u.getDepositTime()));
			}
			if (u.getCreatetime() != null) {
				u.setTempCreateTime(sf.format(u.getCreatetime()));
			}
			if (u.getUpdatetime() != null) {
				u.setTempUpdateTime(sf.format(u.getUpdatetime()));
			}
		}
		return page;
	}

	/**
	 * 生成额度验证存款订单 (改进)
	 * 
	 * @param payOrder
	 * @return
	 */
	public PayOrderValidation createValidatedPayOrderGj(PayOrderValidation payOrder) {
		log.info("生成额度验证存款订单. 玩家：" + payOrder.getUserName() + ", 金额：" + payOrder.getOriginalAmount()
				+ (StringUtil.isEmpty(payOrder.getType()) ? "" : ", 类型：" + payOrder.getType()));
		// 判断该用户是否有未支付的订单， 如果存在三笔以上，不允许生成新订单
		Integer unPayedCount = validatedPayOrderService.getUnPayedOrderCountGj(payOrder);
		if (unPayedCount != null && unPayedCount >= 1) {
			log.info("存在该额度，请输入其它额度");
			payOrder.setCode("0");
			return payOrder;
		}
		payOrder.setCode("1");
		return payOrder;
	}

	/***
	 * 微信额度验证 (验证是否存在订单)
	 * 
	 * @param loginname
	 * @return
	 */
	public PayOrderValidation getPayOrderValidation(String loginname) {
		// 判断额度是否可用
		PayOrderValidation payOrder = new PayOrderValidation();
		payOrder.setUserName(loginname);
		PayOrderValidation payOrderValidation = validatedPayOrderService.getUnPayedOrderCountVo(payOrder);
		return payOrderValidation;
	}

	/**
	 * 生成额度验证存款订单
	 * 
	 * @param payOrder
	 * @return
	 */
	public PayOrderValidation createValidatedPayOrderTwo(PayOrderValidation payOrder) {

		log.info("生成微信额度验证存款订单. 玩家：" + payOrder.getUserName() + ", 金额：" + payOrder.getOriginalAmount()
				+ (StringUtil.isEmpty(payOrder.getType()) ? "" : ", 类型：" + payOrder.getType()));
		// 判断该用户是否有未支付的订单
		Integer unPayedCount = validatedPayOrderService.getUnPayedOrderCountGj(payOrder);
		if (unPayedCount != null && unPayedCount >= 1) {
			log.info("存在该微信额度，请输入其它额度");
			payOrder.setCode("0");
			return payOrder;
		}
		Integer numSize = ValidatedPyaOrderUtil.getInstance().createPayOrderGj(payOrder, validatedPayOrderService);
		if (numSize == 1) {
			payOrder.setCode("1");
		} else {
			payOrder.setCode("-1");
		}
		return payOrder;

	}

	/**
	 * 废除订单
	 * 
	 * @param
	 * @return
	 */
	public Boolean discardDepositOrder(Integer id) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(PayOrderValidation.class);
			dc = dc.add(Restrictions.eq("id", id));
			List<PayOrderValidation> list = proposalService.findByCriteria(dc);
			if (list != null && list.size() > 0 && list.get(0) != null) {
				PayOrderValidation payOrderValidation = (PayOrderValidation) list.get(0);
				if (payOrderValidation.getStatus().equals("0")) {
					payOrderValidation.setStatus("2");
					payOrderValidation.setRemark("用户" + payOrderValidation.getUserName() + "废弃额度验证存款订单。");
					proposalService.update(payOrderValidation);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 创建工会
	 *
	 * @param
	 * @return
	 */
	public String createUnion(String userName, Integer level) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Union.class);
			dc = dc.add(Restrictions.eq("president", userName));
			DetachedCriteria dc2 = DetachedCriteria.forClass(UnionStaff.class);
			dc2 = dc2.add(Restrictions.eq("username", userName));
			List<UnionStaff> list2 = proposalService.findByCriteria(dc2);
			if (list2.size() > 0) {
				UnionStaff unionStaff = list2.get(0);
				if (unionStaff.getRemark().equals("申请")) {
					return "您已申请加入过公会";
				} else {
					return "您已经加入公会,不能创建";
				}
			}
			proposalService.createUnion(userName, dc, level);
			return "恭喜!创建成功!";
		} catch (Exception e) {
			e.printStackTrace();
			return "网络异常";
		}
	}

	/*
	 * public ActivityConfig checkActivityInfo(String title,Integer level) { try
	 * { return SynchronizedUtil.getInstance().checkActivityInfo(
	 * selfYouHuiService, title, level); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return null;
	 * 
	 * }
	 */
	public ActivityConfig checkActivityInfo(String title, String level) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(ActivityConfig.class);
			dc = dc.add(Restrictions.eq("status", 1));
			if (StringUtil.isNotBlank(title)) {
				dc = dc.add(Restrictions.eq("englishtitle", title));
			}
			/*
			 * if(StringUtil.isNotBlank(entrance)){ dc =
			 * dc.add(Restrictions.ilike("entrance",entrance)); }
			 */
			if (StringUtil.isNotBlank(level)) {
				dc = dc.add(Restrictions.eq("level", level));
			}
			List<ActivityConfig> list = proposalService.getHibernateTemplate().findByCriteria(dc);
			ActivityConfig s = null;
			if (!list.isEmpty()) {
				s = list.get(0);
			}
			return s;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public String checkStatus(String title, String loginname, String platform, String entrance, String sid)
			throws Exception {
		/*
		 * Boolean lockFlag = false; InterProcessMutex lock = null; try {
		 * 
		 * lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
		 * GenerateNodePath.generateUserLockForUpdate(loginname)); lockFlag =
		 * lock.acquire(Integer.parseInt(Configuration.getInstance().getValue(
		 * "zk.lock.timeout")), TimeUnit.SECONDS); } catch (Exception e) {
		 * 
		 * e.printStackTrace(); log.error("获取锁发生异常，异常信息：" + e.getMessage());
		 * lockFlag = true; }
		 * 
		 * if (lockFlag) { try { return
		 * UserSynchronizedPool.getSynchUtilByUser(loginname).applyActivity(
		 * upgradeService, proposalService, transferService, selfYouHuiService,
		 * title, loginname, platform, entrance, getUser(loginname), sid);
		 * 
		 * }catch (Exception e){ return "系统异常"; } }else {
		 * 
		 * log.error("未能获取锁，无法执行请求对应的方法....");
		 * 
		 * return null; }
		 */
		try {
			return UserSynchronizedPool.getSynchUtilByUser(loginname).applyActivity(upgradeService, proposalService,
					transferService, selfYouHuiService, title, loginname, platform, entrance, getUser(loginname), sid);
		} catch (Exception e) {
			e.printStackTrace();
			return "领取失败";
		}

	}

	public String passOrRefuse(Integer id, String type) {
		DetachedCriteria dc2 = DetachedCriteria.forClass(UnionStaff.class);
		dc2 = dc2.add(Restrictions.eq("id", id));
		dc2 = dc2.add(Restrictions.eq("remark", "申请"));
		List<UnionStaff> list2 = proposalService.findByCriteria(dc2);
		if (type.equals("PASS")) {
			if (!list2.isEmpty() || list2.size() > 0) {
				UnionStaff unionStaff = list2.get(0);
				unionStaff.setRemark("通过");
				proposalService.update(unionStaff);
				return "同意申请成功";
			}
		} else {
			if (!list2.isEmpty() || list2.size() > 0) {
				proposalService.delete(UnionStaff.class, id);
				return "拒绝成功";
			}
		}
		return "";
	}

	public Boolean isApply(String userName) {
		DetachedCriteria dc2 = DetachedCriteria.forClass(UnionStaff.class);
		dc2 = dc2.add(Restrictions.eq("username", userName));
		dc2 = dc2.add(Restrictions.ne("remark", "申请"));
		List<UnionStaff> list2 = proposalService.findByCriteria(dc2);
		if (!list2.isEmpty() || list2.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<UnionStaff> getApplyLists(String userName) {
		DetachedCriteria dc = DetachedCriteria.forClass(Union.class);
		dc = dc.add(Restrictions.eq("president", userName));
		List<Union> list = proposalService.findByCriteria(dc);
		if (!list.isEmpty() || list.size() > 0) {
			Union union = list.get(0);
			DetachedCriteria dc2 = DetachedCriteria.forClass(UnionStaff.class);
			dc2 = dc2.add(Restrictions.eq("union_id", union.getId()));
			dc2 = dc2.add(Restrictions.eq("remark", "申请"));
			List<UnionStaff> list2 = proposalService.findByCriteria(dc2);
			return list2;
		}
		return null;
	}

	public Guild selfGuild(String loginname) {
		DetachedCriteria dc2 = DetachedCriteria.forClass(GuildStaff.class);
		dc2 = dc2.add(Restrictions.eq("username", loginname));
		List<GuildStaff> list2 = proposalService.getHibernateTemplate().findByCriteria(dc2);
		if (list2 != null && list2.size() > 0) {
			GuildStaff guildStaff = list2.get(0);
			DetachedCriteria dc = DetachedCriteria.forClass(Guild.class);
			dc = dc.add(Restrictions.eq("state", 1));
			dc = dc.add(Restrictions.eq("id", guildStaff.getGuildId()));
			List<Guild> list = proposalService.getHibernateTemplate().findByCriteria(dc);
			if (list2 != null && list2.size() > 0) {
				return list.get(0);
			}
			return null;

		}
		return null;
	}

	public List<Guild> queryGuild() {
		DetachedCriteria dc = DetachedCriteria.forClass(Guild.class);
		dc = dc.add(Restrictions.eq("state", 1));
		List<Guild> list = proposalService.findByCriteria(dc);
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	public Page queryGuildDate(Integer id, Integer pageIndex, Integer size) {
		ArrayList<GuildStaff> guildStaffArrayList = new ArrayList<>();
		DetachedCriteria dc = DetachedCriteria.forClass(GuildStaff.class);
		dc.addOrder(Order.desc("gameAmount"));
		if (null != id) {
			dc.add(Restrictions.eq("guildId", id));
		}
		List<GuildStaff> list = proposalService.findByCriteria(dc);
		for (GuildStaff guildStaff : list) {
			if (null != guildStaff.getUpdatetime() && StringUtils.isNotEmpty(guildStaff.getUsername())) {
				guildStaff.setDay(DateUtil.fmtDateForBetRecods(guildStaff.getUpdatetime()));
			}
			guildStaff.setUsername(StringUtil.subStrings(guildStaff.getUsername()));
			guildStaffArrayList.add(guildStaff);
		}
		Page page = new Page();
		page.setPageContents(guildStaffArrayList);
		return page;
	}

	public Page queryGuildCountEach(Integer id, Integer pageIndex, Integer size) {
		String sql = "SELECT  name,count(1) as count,guild_id FROM guild_staff GROUP BY name";
		ArrayList<GuildCountEach> guildCountEaches = new ArrayList<>();
		List<GuildCountEach> list = slaveService.getList(sql, new HashMap<String, Object>());
		for (Object guildCountEach : list) {
			/*
			 * Object o = list.get(0); Object[] ob = (Object[]) o;
			 */
			Object[] countEach = (Object[]) guildCountEach;
			GuildCountEach count = new GuildCountEach();
			count.setName((String) countEach[0]);
			count.setFoo((BigInteger) countEach[1]);
			count.setId((int) countEach[2]);
			int i = count.getFoo().intValue();
			count.setCount(i);
			guildCountEaches.add(count);
		}
		Page page = new Page();
		page.setPageContents(guildCountEaches);
		return page;
	}

	public Page queryGuildRanking(Integer pageIndex, Integer size) {
		ArrayList<GuildRanking> rankings = new ArrayList<>();
		DetachedCriteria dc = DetachedCriteria.forClass(Guild.class);
		dc.add(Restrictions.eq("state", 1));
		List<Guild> list = proposalService.findByCriteria(dc);
		String sql = "SELECT g.name, sum(game_amount),gs.updatetime FROM guild_staff gs LEFT JOIN guild g ON g.id=gs.guild_id  WHERE  guild_id=:id GROUP BY guild_id";
		for (Guild guild : list) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", guild.getId());
			List list1 = slaveService.getList(sql, params);
			if (list1.size() > 0) {
				Object o = list1.get(0);
				Object[] ob = (Object[]) o;
				GuildRanking ranking = new GuildRanking();
				ranking.setName((String) ob[0]);
				ranking.setGame((Double) ob[1]);
				if (null != ob[2]) {
					ranking.setDay(DateUtil.fmtDateForBetRecods((Date) ob[2]));
				}
				rankings.add(ranking);

			}
		}

		Collections.sort(rankings, new Comparator<GuildRanking>() {
			@Override
			public int compare(GuildRanking o1, GuildRanking o2) {
				if (o1.getGame() > o2.getGame()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		Page page = new Page();

		page.setPageContents(rankings);
		return page;
	}

	public String joinGuild(String userName, Integer id) {
		try {

			int i = 0;
			DetachedCriteria dc = DetachedCriteria.forClass(GuildStaff.class);
			dc = dc.add(Restrictions.eq("username", userName));
			List<GuildStaff> list = proposalService.getHibernateTemplate().findByCriteria(dc);
			if (list != null && list.size() > 0) {
				return "已加入其它工会"; // 已经加入其它工会
			}
			DetachedCriteria dc2 = DetachedCriteria.forClass(GuildStaff.class);
			dc2 = dc2.add(Restrictions.eq("guildId", id));
			List<GuildStaff> list2 = proposalService.getHibernateTemplate().findByCriteria(dc2);
			if (list2 == null || list2.size() < 1) {
				i = 0;
			}
			i = list2.size();

			DetachedCriteria dc3 = DetachedCriteria.forClass(Guild.class);
			dc3 = dc3.add(Restrictions.eq("id", id));
			dc3 = dc3.add(Restrictions.eq("state", 1));
			List<Guild> list3 = proposalService.getHibernateTemplate().findByCriteria(dc3);
			if (list3 == null || list3.size() < 1) {
				return "没有此工会"; // 已经加入其它工会
			}
			Guild guild = (Guild) list3.get(0);

			Date date = new Date();
			if (guild.getStartTime().after(date) || guild.getEndTime().before(date)) {
				return "不在报名时间内";
			}

			if (guild.getMax() <= i) {
				return "工会人数已满,请选择其它工会";
			}
			if (guild.getLevel().contains(getUser(userName).getLevel().toString())) {
				GuildStaff guildStaff = new GuildStaff();
				guildStaff.setGuildId(id);
				guildStaff.setUsername(userName);
				guildStaff.setJoinTime(date);
				guildStaff.setDeposet(0.0);
				guildStaff.setGameAmount(0.0);
				guildStaff.setState(0);
				guildStaff.setName(guild.getName());
				proposalService.save(guildStaff);
				return "加入成功!";
			} else {
				return "您不在所接受的等级内";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "系统错误";
		}
	}

	/**
	 * 加入工会
	 *
	 * @param
	 * @return
	 */
	public String joinUnion(String userName, Integer level, Integer id) {
		try {
			Union o = (Union) proposalService.get(Union.class, id);
			if (o.getNums() == o.getMax()) {
				return "公会人数已满";
			}
			DetachedCriteria dc2 = DetachedCriteria.forClass(UnionStaff.class);
			dc2 = dc2.add(Restrictions.eq("username", userName));
			List<UnionStaff> list2 = proposalService.findByCriteria(dc2);
			if (list2.size() > 0) {
				UnionStaff unionStaff = list2.get(0);
				if (unionStaff.getRemark().equals("申请")
						&& new Date().getTime() - unionStaff.getUpdateTime().getTime() < 86400000l) {
					return "您已申请公会，请等候回覆";
				} else {
					return "您已加入公会";
				}
			}
			proposalService.joinUnion(userName, level, id);
			return "恭喜您，申请成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "网络异常";
		}
	}

	public Boolean Kpeople(int[] id) {
		try {
			for (int i : id) {
				proposalService.delete(UnionStaff.class, i);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean exitUnion(String userName) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Union.class);
			dc = dc.add(Restrictions.ne("username", userName));
			DetachedCriteria dc2 = DetachedCriteria.forClass(UnionStaff.class);
			dc2 = dc2.add(Restrictions.ne("username", userName));

			List<UnionStaff> list = proposalService.findByCriteria(dc2);
			if (!list.isEmpty() || list.size() > 0) {
				UnionStaff unionStaff = list.get(0);
				if (unionStaff.getRemark().equals("会长")) {
					DetachedCriteria dc3 = DetachedCriteria.forClass(UnionStaff.class);
					dc3 = dc3.add(Restrictions.ne("union_id", unionStaff.getUnionId()));
					proposalService.changePresident(unionStaff.getId(), unionStaff.getUnionId(), dc, dc3, slaveService);
					return true;
				} else {
					proposalService.delete(UnionStaff.class, unionStaff.getId());
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public List<UnionStaffInfos> getMembers(String userName) {
		DetachedCriteria dc = DetachedCriteria.forClass(Union.class);
		dc = dc.add(Restrictions.eq("president", userName));
		List<Union> list2 = proposalService.findByCriteria(dc);
		if (list2.size() > 0) {
			Union union = list2.get(0);
			DetachedCriteria dc2 = DetachedCriteria.forClass(UnionStaff.class);
			dc2 = dc2.add(Restrictions.ne("username", userName));
			dc2 = dc2.add(Restrictions.eq("union_id", union.getId()));
			dc2 = dc2.add(Restrictions.eq("remark", "通过"));
			List<UnionStaff> list = proposalService.findByCriteria(dc2);
			ArrayList<UnionStaffInfos> staffInfoss = new ArrayList<>();
			for (UnionStaff unionStaff : list) {
				Double aDouble = proposalService.checkPoints(userName);
				long l = aDouble.longValue() / 10 * 2;
				UnionStaffInfos info = new UnionStaffInfos();
				info.setLevel(unionStaff.getLevel());
				info.setId(unionStaff.getId());
				info.setUnionId(unionStaff.getUnionId());
				info.setLastLogin(DateUtil.fmtYYYY_MM_DD(cs.getUsers(userName).getLastLoginTime()));
				info.setPoints(l);
				info.setUsername(userName);
				staffInfoss.add(info);
			}
			return staffInfoss;
		}
		return null;
	}

	public Boolean isPresident(String userName) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Union.class);
			dc = dc.add(Restrictions.eq("president", userName));
			List<Union> list2 = proposalService.findByCriteria(dc);
			if (list2.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 登入mgs
	 * 
	 * @param loginname
	 * @param gameid
	 * @param demoMode
	 *            为true是试玩，为false是真钱
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	public String mgsLogin(String loginname, String password, String itemId, String appId, String demoMode, String ip,
			String session) throws HttpException, IOException {
		Const ct = transferService.getConsts("MG游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return null;
		}
		return MGSUtil.launchGameUrl(loginname, password, itemId, appId, demoMode, ip, session);
	}

	/**
	 * mg H5登录
	 * 
	 * @param loginname
	 * @param password
	 * @param itemId
	 * @param appId
	 * @param demoMode
	 * @param lobbyurl
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	// demoMode为true是试玩，为false是真钱
	public String mgsH5Login(String loginname, String password, String itemId, String appId, String demoMode, String ip,
			String session) throws HttpException, IOException {
		Const ct = transferService.getConsts("MG游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return null;
		}
		return MGSUtil.launchGameUrl(loginname, password, itemId, appId, demoMode, ip, session);
	}

	/**
	 * CQ9 H5登录
	 * 
	 * @param loginname
	 * @param password
	 * @param itemId
	 * @param appId
	 * @param demoMode
	 * @param lobbyurl
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public String cq9H5Login(String loginname, String password, String gameCode, String demoMode)
			throws HttpException, IOException {
		Const ct = transferService.getConsts("CQ9游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return null;
		}
		return CQ9Util.getGameUrl(loginname, password, gameCode, "mobile", true, demoMode);
	}

	public String pgH5Login(String loginname, String password, String gameCode, String demoMode)
			throws HttpException, IOException {
		Boolean mode = true;
		Const ct = transferService.getConsts("PG游戏");
		if (demoMode.equals("0")) {
			mode = false;
		}
		if (null == ct || ct.getValue().equals("0")) {
			return null;
		}
		return PGUtil.login(loginname, password, gameCode, mode);
	}

	public Double getMGBalance(String loginname, String password) throws HttpException, IOException {
		return MGSUtil.getBalance(loginname);
	}

	public Double getCQ9Balance(String loginname, String password) throws HttpException, IOException {
		return CQ9Util.getBalance(loginname);
	}
	
	public Double getPGBalance(String loginname, String password) throws HttpException, IOException {
		return PGUtil.getBalance(loginname);
	}

	public String isExistConcertRecord(String loginName, String now, Integer type) {

		try {

			Users user = (Users) cs.get(Users.class, loginName, LockMode.UPGRADE);
			Date nowDate = DateUtil.fmtStandard(now);

			if (user == null)
				return "用户不存在";

			DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
			dc.add(Restrictions.eq("loginname", user.getLoginname()));
			dc.add(Restrictions.eq("type", type));
			List<Concert> cList = cs.findByCriteria(dc);

			if (cList != null && cList.size() > 0)
				return "已经报名过不需要在提交";
			else {

				Integer countInteger = ConcertDateType.getCount(nowDate);
				for (int i = countInteger.intValue(); i <= 4; i++) {
					Concert concert = new Concert();
					concert.setLoginname(user.getLoginname());
					concert.setCreatetime(nowDate);
					concert.setStartTime(DateUtil.fmtStandard(ConcertDateType.getCode(i).getStart()));
					concert.setEndTime(DateUtil.fmtStandard(ConcertDateType.getCode(i).getEnd()));
					concert.setBet(0.0);
					concert.setLastTime(DateUtil.fmtStandard(ConcertDateType.getCode(i).getStart()));
					concert.setDisplay(1);
					concert.setActive(0);
					concert.setType(type.intValue());
					concert.setRound(i);
					allMatchService.save(concert);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return "服务异常，请稍后再试";
		}
		return "";
	}

	public String getConcertShow() {

		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		dc = dc.add(Restrictions.eq("typeNo", "type901"));

		List<SystemConfig> list = slaveService.findByCriteria(dc);

		if (null != list && list.size() > 0)
			return list.get(0).getItemNo();
		return "1";

	}

	public Integer getConcertPageRank(String loginname, Integer type, Integer show) {
		DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
		dc.add(Restrictions.eq("round", show));
		dc.add(Restrictions.eq("type", type));
		dc = dc.addOrder(Order.desc("bet"));
		dc.setProjection(Projections.property("loginname"));
		List<String> list = slaveService.findByCriteria(dc);
		for (int i = 0; i < list.size(); i++) {
			if (loginname.equals(list.get(i))) {

				return i + 1;
			}
		}
		return null;
	}

	public List<FirstlyCashin> getConcertPageBetByJson(Integer show, Integer type) {
		List<FirstlyCashin> result = new ArrayList<FirstlyCashin>();
		DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
		dc.add(Restrictions.eq("round", show));
		dc.add(Restrictions.eq("type", type));
		ProjectionList projections = Projections.projectionList().add(Projections.property("loginname"))
				.add(Projections.property("bet"));
		dc.setProjection(projections);
		dc = dc.addOrder(Order.desc("bet"));
		List<Object[]> list = slaveService.findByCriteria(dc, 0, 100);
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = list.get(i);
			FirstlyCashin fCashin = new FirstlyCashin();
			fCashin.setLoginname(obj[0].toString());
			fCashin.setMoney(Double.valueOf(obj[1].toString()));
			result.add(fCashin);
		}

		return result;
	}

	public Page getConcertPageBetNew(Integer pageIndex, Integer size, Integer type) {

		DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
		dc.add(Restrictions.eq("round", ConcertDateType.getShowRound(new Date())));
		dc.add(Restrictions.eq("type", type));
		dc.add(Restrictions.eq("active", 0));
		Order o = Order.asc("ranking");
		Page page = PageQuery.queryForPagenation(this.allMatchService.getHibernateTemplate(), dc, pageIndex, size, o);
		if (page != null && page.getPageContents() != null) {
			List<Concert> list = page.getPageContents();
			for (Concert c : list) {
				c.setLoginname(c.getLoginname().substring(0, 2) + "***"
						+ c.getLoginname().substring(c.getLoginname().length() - 3));
			}
		}
		return page;
	}

	public Concert getConcertSelfBet(String loginname, Integer type) {

		DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
		dc.add(Restrictions.eq("round", ConcertDateType.getShowRound(new Date())));
		dc.add(Restrictions.eq("type", type));
		dc.add(Restrictions.eq("active", 0));
		dc.add(Restrictions.eq("loginname", loginname));
		List<Concert> list = this.allMatchService.findByCriteria(dc);
		return list.get(0);
	}

	public Page getConcertPageBet(String loginname, Integer pageIndex, Integer size, Integer show, Integer type) {

		Page page = new Page();
		try {

			StringBuffer c_s = new StringBuffer("select count(*) from Concert where active=0 and type="
					+ type.intValue() + " and round=" + show.intValue() + " ");
			Long count = (Long) allMatchService.getHibernateTemplate().find(c_s.toString()).iterator().next();

			StringBuffer sql = new StringBuffer(
					"select loginname,startTime,endTime,bet from concert where active=0 and type=" + type.intValue()
							+ " and round=" + show.intValue() + " ");

			if (StringUtils.isNotEmpty(loginname))
				sql.append(" and loginname='" + loginname + "'");

			sql.append(" order by bet desc");

			page = allMatchService.getConcertPage(sql.toString(), pageIndex, size, count + "");

		} catch (Exception e) {
			log.error("getConcertPageBet error:", e);
		} finally {
			return page;
		}
	}

	public Page queryGiftData(String loginname, Integer pageIndex, Integer size) {

		DetachedCriteria dc = DetachedCriteria.forClass(GiftOrder.class);

		dc.add(Restrictions.or(Restrictions.eq("giftID", 19), Restrictions.eq("giftID", 19)));
		// dc.addOrder(Order.asc("applyDate"));
		Order o = Order.asc("applyDate");

		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);

		List<GiftOrder> resultList = page.getPageContents();

		List<QueryDataEuroVO> list = new ArrayList<QueryDataEuroVO>();

		if (null != resultList) {

			for (GiftOrder giftOrder : resultList) {

				String loginName = StringUtil.replace(giftOrder.getLoginname(),
						StringUtil.substring(giftOrder.getLoginname(), 2, 6), "****");
				String createTime = DateUtil.formatDateForStandard(giftOrder.getApplyDate());

				list.add(new QueryDataEuroVO(loginName, createTime));
			}
		}

		page.setPageContents(list);

		return page;
	}

	private boolean gamecheck(String type, String gameid) {
		return slaveService.gamecheck(type, gameid);
	}

	public String getAppPreferentialM(String loginname, String platform) {
		Users user = getUser(loginname);
		if (null == user) {
			return "玩家不存在";
		}
		String result = "";
		// 再次检查是否申请过并检查账户资料
		boolean useFlag = cs.isApplyAppPreferential(loginname);
		boolean isUserIpOrNameDuplicate = cs.isUserIpOrNameDuplicate(user.getAccountName(), user.getRegisterIp());
		if (useFlag || isUserIpOrNameDuplicate)
			return "您的注册资讯重复，无法申请，还请参考我们其他的优惠活动哦～。";
		try {
			result = SynchronizedAppPreferentialUtil.getInstance().depositAppDownloadReward(selfYouHuiService,
					loginname, platform); // 真正领取彩金动作
			System.out.println("result=======================" + result);

		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 检查验证资料[app下载彩金]
	 */
	public Integer checkForAppPreferential(String accountName, String registeIp, String loginname) {
		boolean isUserIpOrNameDuplicate = cs.isUserIpOrNameDuplicate(accountName, registeIp);
		if (isUserIpOrNameDuplicate) {
			return 1;
		}
		boolean hadAppliedAppPreferential = cs.isApplyAppPreferential(loginname);
		if (hadAppliedAppPreferential) {
			return 2;
		}
		return 0;
	}

	public Users getAgentByUrl(String referWebsite) {
		return cs.getAgentByWebSiteNew(referWebsite);
	}

	public Users getAgentById(String id) {
		try {
			Integer id_ = Integer.parseInt(id);
			return cs.getAgentById(id_);
		} catch (Exception e) {
			log.error("根据ID查询代理出错：" + id, e);
		}
		return null;
	}

	public Page queryUserLotteryRecordPage(String loginname, int pageIndex, int size) {
		return userLotteryService.queryUserLotteryRecordPage(loginname, pageIndex, size);
	}

	private String createMoneyContent(String loginname, Double money) {
		if (loginname.startsWith("wap_")) {
			loginname = loginname.replace("wap_", "");
		}
		String content = SMSContent.getText("9").replace("$XXX", loginname).replace("$MONEY", money + "");
		return content;
	}

	/**
	 * 发送用户定制短信
	 * 
	 * @param
	 * @return
	 */
	public String sendSMS(String loginname, String type, Double value, String content) {

		if (StringUtils.isBlank(content)) {
			return "短信内容不能为空";
		}
		if (loginname.startsWith("wap_")) {
			loginname = loginname.replace("wap_", "");
		}
		Users user = (Users) cs.get(Users.class, loginname);
		if (user == null) {
			return "未查询到用户:" + loginname;
		}
		String service = user.getAddress();
		boolean sendflag = false;
		DetachedCriteria dc = DetachedCriteria.forClass(SMSSwitch.class);
		dc.add(Restrictions.eq("disable", "否"));
		dc.add(Restrictions.eq("type", type));
		List<SMSSwitch> list = slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0 && StringUtils.isNotBlank(service)) {
			SMSSwitch smsswitch = list.get(0);

			String[] ids = service.split(",");
			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {

					if (smsswitch.getType().equals(ids[i])) {
						sendflag = true;
					}
				}
			}

			if (sendflag) {
				if (smsswitch.getMinvalue() != null) {

					Double minvalue = Double.parseDouble(smsswitch.getMinvalue().toString());
					if (value == null || value < minvalue) {
						sendflag = false;
					}
				}
			}

			if (sendflag) {
				String phoneno = null;
				try {
					phoneno = AESUtil.aesDecrypt(user.getPhone(), AESUtil.KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (StringUtils.isNotBlank(phoneno)) {
					String code = SmsDXWUtils.sendSms(phoneno, content);
					if ("1".equals(code)) {
						return "发送成功！";
					} else {
						String msg = DXWErrorCode.getText(code);
						return "发送失败：" + msg;
					}
				} else {
					return "用户手机号码为空";
				}
			}

		}
		return "不发送";
	}

	/**
	 * 根据订单号发送用户定制短信（9）
	 * 
	 * @param
	 * @return
	 */
	public String sendSMSByBillno(String billno, Double value) {

		Payorder payorder = (Payorder) cs.get(Payorder.class, billno);
		if (payorder == null) {
			return "未查询到订单号：" + billno;
		}
		Users user = (Users) cs.get(Users.class, payorder.getLoginname());
		if (user == null) {
			return "未查询到用户:" + payorder.getLoginname();
		}
		String content = SMSContent.getText("9").replace("$XXX", payorder.getLoginname()).replace("$MONEY", value + "");
		String service = user.getAddress();
		boolean sendflag = false;
		DetachedCriteria dc = DetachedCriteria.forClass(SMSSwitch.class);
		dc.add(Restrictions.eq("disable", "否"));
		dc.add(Restrictions.eq("type", "9"));
		List<SMSSwitch> list = slaveService.findByCriteria(dc);
		if (list != null && list.size() > 0 && StringUtils.isNotBlank(service)) {
			SMSSwitch smsswitch = list.get(0);

			String[] ids = service.split(",");
			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {

					if (smsswitch.getType().equals(ids[i])) {
						sendflag = true;
					}
				}
			}

			if (sendflag) {
				if (smsswitch.getMinvalue() != null) {

					Double minvalue = Double.parseDouble(smsswitch.getMinvalue().toString());
					if (value == null || value < minvalue) {
						sendflag = false;
					}
				}
			}

			if (sendflag) {
				String phoneno = null;
				try {
					phoneno = AESUtil.aesDecrypt(user.getPhone(), AESUtil.KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (StringUtils.isNotBlank(phoneno)) {
					String code = SmsDXWUtils.sendSms(phoneno, content);
					if ("1".equals(code)) {
						return "发送成功！";
					} else {
						String msg = DXWErrorCode.getText(code);
						return "发送失败：" + msg;
					}
				} else {
					return "用户手机号码为空";
				}
			}

		}
		return "不发送";
	}

	/**
	 * 获得手机连续签到抽奖
	 */
	public String mobileSignLottery(String loginname, String itemName) {
		LotteryItem prize = new LotteryItem();
		try {
			prize = userLotteryService.getPrize();
			if (prize == null) {
				return "error,获取奖项失败，请稍后再试!";
			}
			SynchronizedUtil.getInstance().winningLottery(userLotteryService, loginname, prize.getItemName());
		} catch (Exception e) {
			log.error("手机连续签到抽奖出错：使用者" + loginname, e);
			return "error,手机连续签到抽奖失败!" + e.getMessage();
		}
		return "success," + prize.getType();
	}

	// 优付支付分层处理
	public Boolean getUserReDate(String loginname) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -7);
		Date monday = cal.getTime();
		System.out.println(monday);
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.le("createtime", monday));
		List<Users> list = cs.findByCriteria(dc);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 新贝支付宝支付
	 * 
	 * @param loginname
	 * @param money
	 * @return
	 */
	public String getXinBZfbOrderNo(String loginname, Double money) {
		String orderNo = seqService.generateXinBZfbOrderNoID(loginname);
		// 存入改订单
		String str = announcementService.addSaveOrderXinBZfb(orderNo, money, loginname);
		if (str == null) {
			return null;
		}
		return orderNo;
	}

	public ActivityCalendarService getActivityCalendarService() {
		return activityCalendarService;
	}

	public void setActivityCalendarService(ActivityCalendarService activityCalendarService) {
		this.activityCalendarService = activityCalendarService;
	}

	/**
	 * 获取PNG Flash游戏连接
	 * 
	 * @param loginname
	 * @param gid
	 * @return
	 */
	public String getPNGFlashUrl(String loginname, String gid) {
		// 控制游戏开关，其他游戏没有，暂时注掉
		/*
		 * Const ct = transferService.getConsts("png游戏"); if (null == ct ||
		 * ct.getValue().equals("0")) { return null; }
		 */
		return PNGUtil.flashGameLogin(loginname, gid);
	}

	/**
	 * 获取PNG ticket(透明钱包模式)
	 * 
	 * @param loginname
	 * @param gid
	 * @return
	 */
	public String getPNGLoadingTicket(String loginname, String gameCode) {

		Const ct = transferService.getConsts("PNG游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return null;
		}

		return SlotUtil.getPngLoadingTicket(loginname, gameCode);
	}

	/**
	 * 转入PNG
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	/**
	 * 获取bbin路径
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String bbinLogin(String loginname, String gameKind, String gameCode, String mode) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("BBIN游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return ChessUtil.MAINTAIN;
		}
		if (StringUtils.isBlank(gameCode)) {
			return BBinUtils.Login(loginname, gameKind);
		} else {
			if ("h5".equals(mode)) {
				return BBinUtils.ForwardGameH5By5(loginname, gameCode);
			}
			if ("flash".equals(mode)) {
				return BBinUtils.bbinFlashLogin(loginname, gameCode);
			}
		}
		return null;
	}

	/**
	 * 获取泛亚竞技游戏url
	 * 
	 * @param loginname
	 * @throws Exception
	 */
	public String fanyaLogin(String loginname, String password) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("泛亚竞技");
		if (null == ct || ct.getValue().equals("0")) {
			return ChessUtil.MAINTAIN;
		}
		// 在登录之前 获取下 余额确保没有账号就去新建一个
		FanYaUtil.balance(loginname);
		return FanYaUtil.login(loginname);
	}

	/**
	 * 获取泛亚竞技余额
	 * 
	 * @param loginname
	 * @throws Exception
	 */
	public Double getFanyaBalance(String loginname) throws Exception {
		return FanYaUtil.balance(loginname);
	}

	/**
	 * 获取bbin路径(web)
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String bbinMobiLogin(String loginname, String gameCode) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("BBIN游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return ChessUtil.MAINTAIN;
		}
		if (StringUtils.isBlank(gameCode)) {
			return BBinUtils.PlayGameByH5(loginname);
		} else {
			return BBinUtils.ForwardGameH5By5(loginname, gameCode);
		}
	}

	/**
	 * 获取bbin余额
	 * 
	 * @param loginname
	 * @return
	 */
	public Double getBbinBalance(String loginname) {
		return BBinUtils.GetBalance(loginname);
	}

	/**
	 * 从PNG转出
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */

	/**
	 * 获取PNG余额
	 * 
	 * @param loginname
	 * @return
	 */
	public Double getPNGBalance(String loginname) {
		return PNGUtil.getBalance(loginname);
	}

	/**
	 * 获取PNG 手机游戏连接
	 * 
	 * @param loginname
	 * @param gid
	 * @return
	 */
	public String getPNGMobileUrl(String loginname, String gid, String reloadUrl) {
		// 控制游戏开关，其他游戏没有，暂时注掉
		/*
		 * Const ct = transferService.getConsts("png游戏"); if (null == ct ||
		 * ct.getValue().equals("0")) { return null; }
		 */
		return PNGUtil.mobileGameLogin(loginname, gid, reloadUrl);
	}

	/***
	 * 新秒存 查询历史银行记录接口
	 */
	public Page queryDepositBank(String loginname, Integer pageIndex, Integer size) {
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.eq("flag", 1));
		dc.add(Restrictions.sqlRestriction("1=1 GROUP BY ubankno"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<DepositOrder> list = page.getPageContents();
		return page;
	}

	// 获取生日礼金
	public String getBirthdayMoney(String loginname) {
		String str = checkSystemConfig("type513", "001", "否");
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		return selfYouHuiService.getBirthdayMoney(loginname);
	}

	// 生日礼金转入主账户
	public String drawBirthdayMoney(String loginName, Integer id) {

		Users user = getUser(loginName);
		YouHuiConfig s = (YouHuiConfig) proposalService.getHibernateTemplate().get(YouHuiConfig.class, id);
		/*
		 * if(selfYouHuiService.getDeposit(loginName)<100){ return
		 * "当月存款不足，需当月存款达到100。"; }
		 */
		if (user.getBirthday() == null) {
			return "请绑定生日之后在领取";
		}
		try {
			// 生日日期 是否大于或小于3天之内(包含3天好计算 给4天)
			long data = 4 * 24 * 60 * 60 * 1000;
			String a = DateUtil.fmtyyyy_MM_d(user.getBirthday());
			String b = DateUtil.fmtyyyy_MM_d(new Date());
			Date data3 = DateUtil.fmtyyyy_MM_d(b.substring(0, 4) + "-" + a.substring(5, a.length()));
			long data1 = data3.getTime() - new Date().getTime();
			if (Math.abs(data1) > data) {
				return "请在您填写的生日前后三天内领取";
			}
			return transferService.transferBirthdayToMain(user, s.getLimitMoney());
		} catch (ParseException e) {
			e.printStackTrace();
			return "网络异常！请稍后再试";
		}
	}

	public List<Activity> queryBirthdayRecords(String loginname) {
		ArrayList<Activity> activityHistories = new ArrayList<>();
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("type", ProposalType.BIRTHDAY.getCode()));
		List<Proposal> list = proposalService.findByCriteria(dc);
		for (Proposal p : list) {
			Activity history = new Activity();
			history.setActivityPercent(p.getAmount());
			history.setActivityName("生日礼金");
			history.setRemark(DateUtil.fmtyyyy_MM_d(p.getCreateTime()));
			activityHistories.add(history);
		}
		return activityHistories;
	}

	/**
	 * 获取老虎机钱包余额
	 * 
	 * @param loginname
	 * @return
	 */
	public Double getSlotBalance(String loginname, String gameCode) {
		return SlotUtil.getBalanceByCode(loginname, gameCode);
	}

	/**
	 * 获取老虎机钱包余额
	 * 
	 * @param loginname
	 * @return
	 */
	public String getPtSkyUrl(Users users, String gameCode, String urlLobby, String mode, String ip) {

		// 控制游戏开关

		if (!StringUtil.equals(SlotUtil.FUNMODE, mode)) {
			Const ct = transferService.getConsts("PTSKY游戏");
			if (null == ct || ct.getValue().equals("0")) {
				return SlotUtil.MAINTAIN;
			}
		}
		// 领取过优惠无法进入ptsw捕鱼游戏 金龙推币机 捕鱼多福-奖池版 捕鱼多福 福气水果
		if (!StringUtil.equals(SlotUtil.FUNMODE, mode)
				&& ("sw_fufish_intw".equalsIgnoreCase(gameCode) || "sw_fuqsg".equalsIgnoreCase(gameCode)
						|| "sw_fufish-jp".equalsIgnoreCase(gameCode) || "sw_dragon_dozer".equalsIgnoreCase(gameCode))) {
			// 判断玩家是否领取过优惠
			DetachedCriteria dc = DetachedCriteria.forClass(SelfRecord.class);
			dc.add(Restrictions.eq("loginname", users.getLoginname()));
			dc.add(Restrictions.eq("platform", "slot"));
			dc.add(Restrictions.eq("type", 0));
			List<SelfRecord> selfList = proposalService.getHibernateTemplate().findByCriteria(dc);
			if (null != selfList && !selfList.isEmpty()) {
				return SlotUtil.ERROR;
			}
		}
		return SlotUtil.flashGameLogin(users, gameCode, urlLobby, mode, ip);
	}

	/**
	 * 获取平博体育游戏开关
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String PBUserLoginFlag(String loginname) throws Exception {
		// 控制游戏开关
		log.info("获取平博体育游戏开关开始");
		Const ct = transferService.getConsts("平博体育");
		if (null == ct || ct.getValue().equals("0")) {
			log.info("获取平博体育游戏开关失败");
			return ChessUtil.MAINTAIN;
		}
		log.info("获取平博体育游戏开关成功");
		return "PASS";
	}

	public String checkLuActivity(String loginname) throws Exception {
		Date start = DateUtil.ntStart();
		DetachedCriteria dc = DetachedCriteria.forClass(Activity.class);
		dc.add(Restrictions.eq("userrole", loginname));
		dc.add(Restrictions.eq("activityName", "别撸了,战起来"));
		dc.add(Restrictions.ge("createDate", start));
		List<Activity> selfList = proposalService.getHibernateTemplate().findByCriteria(dc);
		if (null != selfList && !selfList.isEmpty()) {
			return "竞猜每日只可以竞猜一次";
		}
		return null;
	}

	public List<Activity> LuActivityHistory(String loginname) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Activity.class);
		dc.add(Restrictions.eq("userrole", loginname));
		dc.add(Restrictions.eq("activityName", "别撸了,战起来"));
		List<Activity> selfList = proposalService.getHibernateTemplate().findByCriteria(dc);
		return selfList;
	}

	public String luActivity(String loginname, Double remit, String platformId, String type) throws Exception {
		String s = "";
		if (platformId.equals("6001")) {
			s = "PT存送优惠";
		}
		if (platformId.equals("6006")) {
			s = "TTG存送优惠";
		}
		if (platformId.equals("6008")) {
			s = "AG真人存送优惠";
		}
		if (platformId.equals("6009")) {
			s = "SLOT存送优惠";
		}
		Activity activity = new Activity();
		activity.setCreateDate(new Date());
		activity.setActivityPercent(remit);
		activity.setActivityName("别撸了,战起来");
		activity.setRemark(s);
		activity.setUserrole(loginname);
		activity.setActivityStatus(Integer.valueOf(type));// 1表示竞猜胜利 0表示失败
		proposalService.save(activity);
		return null;
	}

	/******
	 * 新秒存
	 */
	public Bankinfo getNewdeposit(String loginname, String banktype, String uaccountname, String ubankname,
			String ubankno, Double amout, boolean force, String depositId) {
		Boolean lockFlag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		InterProcessMutex lock = null;

		try {
			lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginname));
			lockFlag = lock.acquire(Integer.parseInt(Configuration.getInstance().getValue("zk.lock.timeout")),
					TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("玩家：" + loginname + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		try {
			if (lockFlag) {
				log.info("正在处理玩家" + loginname + "的请求，执行时间：" + time);
				return UserSynchronizedPool.getSynchUtilByUser(loginname).getNewdeposit(transferService, slaveService,
						cs, loginname, banktype, uaccountname, ubankname, ubankno, amout, force, depositId);
			} else {
				log.error("玩家：" + loginname + "未能获取锁，无法执行请求对应的方法....");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("执行玩家：" + loginname + "请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
		} finally {
			if (lockFlag) {
				try {
					if (null != lock) {
						lock.release();
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("玩家：" + loginname + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
				}
			}
		}
		return null;
	}

	/******
	 * 根据玩家账号和附言删除附言订单
	 * 
	 * @param loginname
	 * @param depositId
	 * @return
	 */
	public String cancelNewdeposit(String loginname, String depositId) {
		return this.proposalService.cancelNewdeposit(loginname, depositId);
	}

	public String getWithDrawBankStatus(String bankname) {
		try {
			return bankStatusService.getBankStatus(bankname);
		} catch (Exception e) {
			log.error("获取提款银行状态异常", e);
			return "ERROR";
		}
	}

	// 获取微信转账额度
	public Double getWxZzQuota(String loginname, Double amout) {
		return UserSynchronizedPool.getSynchUtilByUser(loginname).getWxZzQuota(transferService, loginname, amout);
	}

	/******
	 * 微信 秒存
	 */
	public Bankinfo createWeiXindeposit(String loginname, Double amout) {
		Boolean lockFlag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		InterProcessMutex lock = null;

		try {
			lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginname));
			lockFlag = lock.acquire(Integer.parseInt(Configuration.getInstance().getValue("zk.lock.timeout")),
					TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("玩家：" + loginname + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		try {
			if (lockFlag) {
				log.info("正在处理玩家" + loginname + "的请求，执行时间：" + time);
				return UserSynchronizedPool.getSynchUtilByUser(loginname).createWeiXindeposit(transferService,
						loginname, amout);
			} else {
				log.error("玩家：" + loginname + "未能获取锁，无法执行请求对应的方法....");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("执行玩家：" + loginname + "请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
		} finally {
			if (lockFlag) {
				try {
					if (null != lock) {
						lock.release();
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("玩家：" + loginname + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
				}
			}
		}
		return null;
	}

	/***
	 * 微信秒存 (验证是否存在订单)
	 * 
	 * @param loginname
	 * @return
	 */
	public DepositOrder getDepositPayorder(String loginname) {
		// 判断额度是否可用
		DetachedCriteria dc4 = DetachedCriteria.forClass(DepositOrder.class);
		dc4.add(Restrictions.eq("loginname", loginname));
		dc4.add(Restrictions.eq("status", 0));
		dc4.add(Restrictions.eq("type", "3"));// 微信
		List<DepositOrder> list4 = proposalService.findByCriteria(dc4);
		if (list4 != null && list4.size() > 0) {
			return list4.get(0);
		}
		return null;
	}

	/****
	 * 微信秒存
	 * 
	 * @param loginname
	 * @return
	 */
	public Bankinfo getWxDepositPayorder(String loginname) {
		Bankinfo wxBank = new Bankinfo();
		Users user = getUser(loginname);
		if (null == user) {
			return null;
		}
		try {
			DetachedCriteria dc4 = DetachedCriteria.forClass(Bankinfo.class);
			dc4 = dc4.add(Restrictions.eq("type", 1));
			dc4 = dc4.add(Restrictions.eq("isshow", 1));
			dc4 = dc4.add(Restrictions.eq("useable", 0));
			dc4 = dc4.add(Restrictions.eq("vpnname", "A"));
			dc4 = dc4.add(Restrictions.eq("bankname", "微信"));
			dc4 = dc4.add(Restrictions.like("userrole", "%" + user.getLevel() + "%"));
			dc4.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			List<Bankinfo> list4 = proposalService.findByCriteria(dc4);
			if (list4 != null && list4.size() > 0 && list4.get(0) != null) {
				wxBank = list4.get(0);
			} else {
				wxBank.setMassage("微信通道已关闭");
				return wxBank;
			}
		} catch (Exception e) {
			log.error(loginname + e.getMessage());
		}
		return wxBank;
	}

	public static final String[] levels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M" };

	/****
	 * 通用支付
	 * 
	 * @param loginname
	 * @return
	 */
	public Bankinfo getGatherPayorder(String loginname, String bankname) {
		Bankinfo wxBank = new Bankinfo();
		Users user = getUser(loginname);
		if (null == user) {
			return null;
		}
		try {
			// 判断玩家注册是否满足七天
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cals = Calendar.getInstance();
			cals.setTime(new Date());
			cals.add(Calendar.DAY_OF_MONTH, -7);

			// 判断玩家累计存款量
			Double sumMoney = Double.parseDouble(checkSignRecord(user.getLoginname(), "2008-01-01 00:00:01",
					DateUtil.fmtDateForBetRecods(new Date())));

			DetachedCriteria dc4 = DetachedCriteria.forClass(Bankinfo.class);
			dc4 = dc4.add(Restrictions.eq("type", 1));
			dc4 = dc4.add(Restrictions.eq("isshow", 1));
			dc4 = dc4.add(Restrictions.eq("useable", 0));
			dc4 = dc4.add(Restrictions.eq("bankname", bankname));
			// 注册满足七天
			if (user.getCreatetime().getTime() < cals.getTime().getTime()) {
				dc4.add(Restrictions.in("vpnname", new String[] { "A", "C" }));
			} else {
				dc4.add(Restrictions.in("vpnname", new String[] { "B", "C" }));
			}
			List<Bankinfo> list4 = proposalService.findByCriteria(dc4);
			if (list4 != null && list4.size() > 0 && list4.get(0) != null) {
				dc4 = dc4.add(Restrictions.like("userrole", "%" + user.getLevel() + "%"));
				dc4.add(Restrictions.sqlRestriction("1=1 order by rand()"));
				List<Bankinfo> list5 = proposalService.findByCriteria(dc4);
				if (list5 != null && list5.size() > 0 && list5.get(0) != null) {
					wxBank = list5.get(0);
					if (sumMoney < wxBank.getDepositMin()) {
						wxBank.setMassage("您累计存款小于" + wxBank.getDepositMin() + "元，请使用其他支付方式");
						return wxBank;
					}
				} else {
					wxBank.setMassage("您的会员等级不符合要求，请升级后使用");
					return wxBank;
				}
			} else {
				wxBank.setMassage("支付通道已关闭");
				return wxBank;
			}
		} catch (Exception e) {
			log.error(loginname + e.getMessage());
		}
		return wxBank;
	}

	/**
	 * 废除订单
	 * 
	 * @param
	 * @return
	 */
	public Boolean discardOrder(String id) {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
			dc = dc.add(Restrictions.eq("depositId", id));
			List<DepositOrder> list = proposalService.findByCriteria(dc);
			if (list != null && list.size() > 0 && list.get(0) != null) {
				DepositOrder depositOrder = (DepositOrder) list.get(0);
				if (depositOrder.getStatus() == 0) {
					depositOrder.setStatus(2);
					depositOrder.setRemark("用户" + depositOrder.getLoginname() + "作废微信秒存存款订单。");
					proposalService.update(depositOrder);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 沙巴体育平台账户转账至梦之城账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferOutforSbaTy(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			log.info("function-->transferOutforSbaTy");
			String seqId = seqService.generateTransferID();
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferOutforSbaTy(transferService, seqId,
					loginname, remit);
		}
		return strLimit;
	}

	/**
	 * 梦之城账户转账至沙巴体育平台账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferInforSbaTy(String loginname, Double remit) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (strLimit == null) {
			String seqId = seqService.generateTransferID();
			return UserSynchronizedPool.getSynchUtilByUser(loginname).transferInforSbaTy(transferService, seqId,
					loginname, remit);
		}
		return strLimit;
	}

	// 微客服调用接口
	public UserInfo getWeUserInfo(String username) {
		return cs.getUserInfo(username);
	}

	/**
	 * 每日存款奖金操作
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String doRechargeRecord(String loginname) throws Exception {
		return SynchronizedUtil.getInstance().doRechargeRecord(transferService, loginname);
	}

	/**
	 * 每日流水奖金操作
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String doStreamRecord(String loginname) throws Exception {
		return SynchronizedUtil.getInstance().doStreamRecord(transferService, loginname);
	}

	/**
	 * 判断是否存在活动礼品
	 * 
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Gift getExistGiftForApp(String loginName) {

		Users user = (Users) cs.get(Users.class, loginName);
		String level = String.valueOf(user.getLevel());

		Date now = new Date();

		DetachedCriteria dc = DetachedCriteria.forClass(Gift.class);
		dc.add(Restrictions.le("startTime", now));
		dc.add(Restrictions.ge("endTime", now));
		dc.add(Restrictions.like("levels", level, MatchMode.ANYWHERE));
		dc.add(Restrictions.eq("status", "1"));
		dc.addOrder(Order.desc("createTime"));

		List<Gift> gifts = cs.findByCriteria(dc);

		if (null != gifts && !gifts.isEmpty()) {

			return gifts.get(0);
		}

		return null;
	}

	/**
	 * 检查签到转账的存款记录 手机
	 * 
	 * @param customer
	 * @throws AxisFault
	 */
	public String chekDepositOrderRecordM(String loginName, String userLevel) throws AxisFault {

		Date date = new Date();
		String result = checkSignRecord(loginName, DateUtil.fmtDateForBetRecods(DateUtil.getDateBegin(date)),
				DateUtil.fmtDateForBetRecods(DateUtil.getDateDone(date)));

		double records = Double.valueOf(result);
		log.info("签到转账的存款记录:" + records);

		if ("0".equals(userLevel) && (records < 50.00)) {
			return "您当日存款还差" + (50.00 - records) + "元可进行转账";
		}
		if ("1".equals(userLevel) && (records < 100.00)) {
			return "您当日存款还差" + (100.00 - records) + "元可进行转账";
		}
		if ("2".equals(userLevel) && (records < 300.00)) {
			return "您当日存款还差" + (300.00 - records) + "元可进行转账";
		}
		if ("3".equals(userLevel) && (records < 500.00)) {
			return "您当日存款还差" + (500.00 - records) + "元可进行转账";
		}
		if ("4".equals(userLevel) && (records < 700.00)) {
			return "您当日存款还差" + (700.00 - records) + "元可进行转账";
		}

		return "true";
	}

	public String checkGameMoney(String gameMoney) {
		// if(!gameMoney.matches("^\\d*[1-9]\\d*$")){
		// return "转账金额必须为整数！";
		// }
		// 判断游戏是否为数字
		Double remit = Double.parseDouble(gameMoney);
		if (remit < 1.0) {
			return "转账金额不能小于等于1元！";
		}
		return null;
	}

	/**
	 * 获取DT总奖池
	 * 
	 * @param loginname
	 * @param limitAmount
	 * @return
	 */
	public DtPotVO dtJackpot() {
		return SlotUtil.getDTjackPot();
	}

	/**
	 * 获取DT老虎机路径
	 * 
	 * @param loginname
	 * @return
	 */
	public String getDtGameUrl(Users users, String gameCode, String urlLobby, String mode, String ip) {
		// 控制游戏开关
		Const ct = transferService.getConsts("DT游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return SlotUtil.MAINTAIN;
		}
		return SlotUtil.getDTFlashGameLogin(users, gameCode, urlLobby, mode, ip);
	}

	/**
	 * 获取DT捕鱼路径
	 * 
	 * @param loginname
	 * @return
	 */
	public String getDtFishGameUrl(Users users) {
		// 控制游戏开关
		Const ct = transferService.getConsts("钱来捕鱼游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return SlotUtil.MAINTAIN;
		}
		return SlotUtil.getDTFishGameLogin(users);
	}

	/**
	 * 获取棋牌钱包余额
	 * 
	 * @param loginname
	 * @return
	 */
	public Double getChessBalance(String loginname) {
		return ChessUtil.getBalance(loginname);
	}

	/**
	 * 获取捕鱼钱包余额
	 * 
	 * @param loginname
	 * @return
	 */
	public Double getFishBalance(String loginname) {
		return SlotUtil.getFishBalance(loginname);
	}

	// 生日礼金申请
	public String birthdayActivityInfo(String loginName, Integer id) {
		Users user = getUser(loginName);
		ActivityConfig s = (ActivityConfig) proposalService.getHibernateTemplate().get(ActivityConfig.class, id);
		if (s == null || user == null) {
			return "您还不是vip，请升级到vip再来领取吧";
		}
		if (user.getBirthday() == null) {
			return "请绑定生日之后在领取";
		}
		try {
			// 生日日期 是否大于或小于3天之内(包含3天好计算 给4天)
			long data = 4 * 24 * 60 * 60 * 1000;
			String a = DateUtil.fmtyyyy_MM_d(user.getBirthday());
			String b = DateUtil.fmtyyyy_MM_d(new Date());
			Date data3 = DateUtil.fmtyyyy_MM_d(b.substring(0, 4) + "-" + a.substring(5, a.length()));
			long data1 = data3.getTime() - new Date().getTime();
			if (Math.abs(data1) > data) {
				return "请在您填写的生日前后三天内领取";
			}
			return transferService.transferBirthdayToMain(user, s.getAmount());
		} catch (ParseException e) {
			e.printStackTrace();
			return "网络异常！请稍后再试";
		}
	}

	/**
	 * 获取比特钱包余额
	 * 
	 * @param loginName
	 * @return
	 */
	public Double getBitGameBalance(String loginName) {

		return BitGameUtil.bitUserBalance(loginName);
	}

	/**
	 * 获取比特游戏登陆连接
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public String bitGameUserLogin(String loginName) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("比特游戏开关");
		if (null == ct || ct.getValue().equals("0")) {
			return ChessUtil.MAINTAIN;
		}
		return BitGameUtil.bitUserLogin(loginName);
	}

	/**
	 * 获取棋乐游路径
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String cg761Login(String loginname, String gameCode) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("761游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return "MAINTAIN";
		}
		return ChessUtil.cg761Login(loginname, gameCode);
	}

	/**
	 * 获取sw路径
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String swLogin(String loginname, String gameCode, String mode) throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("sw游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return "MAINTAIN";
		}
		return SWUtil.loginGame(loginname, gameCode, mode);
	}

	/**
	 * 获取ag路径
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception
	 */
	public String aginLogin(String loginname, String referWebsite, String gameCode, String mh5, String mode)
			throws Exception {
		// 控制游戏开关
		Const ct = transferService.getConsts("AGIN游戏");
		if (null == ct || ct.getValue().equals("0")) {
			return "MAINTAIN";
		}
		String loginid = seqService.generateLoginID(loginname);
		String url = null;
		if ("1".equals(mode)) {
			url = AGINUtil.agLogin(loginname, gameCode, referWebsite, mh5, loginid);
		}
		// 试玩地址
		if ("0".equals(mode)) {
			url = AGINUtil.agTryLogin(loginname, gameCode, referWebsite, mh5, loginid);
		}
		return url;
	}

	/**
	 * 获取红包余额
	 * 
	 * @param loginname
	 * @return
	 */
	public String getRedWalletBalance(String loginName) {
		RedRainWallet redRainWallet = (RedRainWallet) proposalService.get(RedRainWallet.class, loginName);
		if (null != redRainWallet) {
			return redRainWallet.getAmout().toString();
		}
		return "0.0";
	}

	/**
	 * 主账户转账到红包雨账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public String transferSelfToRedRain(String loginname, String remit) {
		Double amout = Double.valueOf(remit);
		String msg = "";
		Boolean lockFlag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		InterProcessMutex lock = null;

		try {
			lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginname));
			lockFlag = lock.acquire(Integer.parseInt(Configuration.getInstance().getValue("zk.lock.timeout")),
					TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("玩家：" + loginname + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}

		try {

			if (lockFlag) {

				log.info("正在处理玩家" + loginname + "的请求，执行时间：" + time);

				String strLimit = transferService.transferLimitMethod(loginname, amout);

				if (strLimit == null) {

					String seqId = seqService.generateTransferID();

					msg = UserSynchronizedPool.getSynchUtilByUser(loginname).transferSelfToRedRain(transferService,
							seqId, loginname, amout, "用户主账户转入红包雨账户" + amout + "元");
				} else {

					msg = strLimit;
				}

				log.info("处理完成玩家" + loginname + "的请求，执行时间：" + time + "，结束时间：" + sdf.format(new Date()));
			} else {

				log.error("玩家：" + loginname + "未能获取锁，无法执行请求对应的方法....");

				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.error("执行玩家：" + loginname + "请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {

			if (lockFlag) {

				try {

					if (null != lock) {

						lock.release();
					}
				} catch (Exception e) {

					e.printStackTrace();
					log.error("玩家：" + loginname + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}

		return msg;
	}

	/**
	 * 红包雨金额转账至游戏账号
	 * 
	 * @param loginname
	 * @param remit
	 * @param signType
	 *            转入的游戏类型/mg/dt
	 * @return
	 */
	public String transferInforRedRain(String loginname, Double remit, String signType) {
		String strLimit = transferService.transferLimitMethod(loginname, remit);
		if (StringUtils.isNotBlank(strLimit)) {
			return strLimit;
		}
		try {
			return SynchronizedUtil.getInstance().transferInforRedRain(selfYouHuiService, loginname, remit, signType);
		} catch (Exception e) {
			log.error("红包雨金额转账至游戏账号异常：" + e.getMessage());
		}
		return "转账失败";

	}

}