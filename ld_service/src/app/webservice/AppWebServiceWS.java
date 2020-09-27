package app.webservice;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import app.model.vo.*;
import app.service.interfaces.*;
import dfh.model.*;
import dfh.service.interfaces.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoXimaReturnVo;
import dfh.email.template.EmailTemplateHelp;
import dfh.model.bean.SmsInfoVo;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.remote.bean.NTwoCheckClientResponseBean;
import dfh.security.EncryptionUtil;
import dfh.skydragon.webservice.Axis2WebServiceWS;
import dfh.skydragon.webservice.UserWebServiceWS;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.IPSeeker;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.SSLMailSender;
import dfh.utils.SpringFactoryHepler;
import dfh.utils.StringUtil;
import dfh.utils.TelCrmUtil;
import dfh.utils.UserSynchronizedPool;
import dfh.utils.compare.SmsInfoVoComparator;
import dfh.utils.sendemail.AESUtil;
import dfh.utils.sendemail.SendEmailWs;
import app.enums.DataDictionary;
import app.model.po.LatestPreferential;
import app.model.po.PreferentialComment;
import app.model.po.PreferentialConfig;
import app.util.DateUtil;
import app.util.SynchronizedUserPool;
import app.model.vo.FindPwdByDxVO;

public class AppWebServiceWS {
	
	private static Logger log = Logger.getLogger(AppWebServiceWS.class);
	
	private IUserService userService;
	private ISystemConfigService systemConfigService;
	private ILatestPreferentialService latestPreferentialService;
	private IPreferentialCommentService preferentialCommentService;
	private IConstService constService;
	private ProposalService proposalService;
	private IGGameinfoService gameinfoService;
	private IUserBankInfoService userBankInfoService;
	private IUserStatusService userStatusService;
	private IPreferentialConfigService preferentialConfigService;
	private ISelfPromotionService selfPromotionService;
	private CustomerService cs;
	private ISlotMachineBigBangService slotMachineBigBangService;
	private ISystemAnnouncementService systemAnnouncementService;
	private IAgentAccountListService agentAccountListService;
	private IAccountService accountService;
	private IQuestionService questionService;
	private BankStatusService bankStatusService;
	private SSLMailSender mailSender;
	private SlaveService slaveService;
	private IActivityConfigService activityConfigService;
	private TransferService transferService;
	private static UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");
	private static Axis2WebServiceWS axis2WebServiceWS = (Axis2WebServiceWS) SpringFactoryHepler.getInstance("axis2WebServiceWS");

	public IActivityConfigService getActivityConfigService() {
		return activityConfigService;
	}

	public void setActivityConfigService(IActivityConfigService activityConfigService) {
		this.activityConfigService = activityConfigService;
	}

	public TransferService getTransferService() {
		return transferService;
	}

	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}

	public void setSlaveService(SlaveService slaveService) {
		this.slaveService = slaveService;
	}
	
	public void setBankStatusService(BankStatusService bankStatusService) {
		this.bankStatusService = bankStatusService;
	}

	public void setQuestionService(IQuestionService questionService) {
		this.questionService = questionService;
	}

	public void setMailSender(SSLMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setAgentAccountListService(IAgentAccountListService agentAccountListService) {
		this.agentAccountListService = agentAccountListService;
	}

	public void setSlotMachineBigBangService(ISlotMachineBigBangService slotMachineBigBangService) {
		this.slotMachineBigBangService = slotMachineBigBangService;
	}
	
	public void setCs(CustomerService cs) {
		this.cs = cs;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public void setSystemConfigService(ISystemConfigService systemConfigService) {
		this.systemConfigService = systemConfigService;
	}

	public void setLatestPreferentialService(ILatestPreferentialService latestPreferentialService) {
		this.latestPreferentialService = latestPreferentialService;
	}

	public void setPreferentialCommentService(IPreferentialCommentService preferentialCommentService) {
		this.preferentialCommentService = preferentialCommentService;
	}
	
	public void setConstService(IConstService constService) {
		this.constService = constService;
	}
	
	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}
	
	public void setGameinfoService(IGGameinfoService gameinfoService) {
		this.gameinfoService = gameinfoService;
	}
	
	public void setUserBankInfoService(IUserBankInfoService userBankInfoService) {
		this.userBankInfoService = userBankInfoService;
	}
	
	public void setUserStatusService(IUserStatusService userStatusService) {
		this.userStatusService = userStatusService;
	}

	public void setPreferentialConfigService(IPreferentialConfigService preferentialConfigService) {
		this.preferentialConfigService = preferentialConfigService;
	}
	
	public void setSelfPromotionService(ISelfPromotionService selfPromotionService) {
		this.selfPromotionService = selfPromotionService;
	}
	
	public void setSystemAnnouncementService(ISystemAnnouncementService systemAnnouncementService) {
		this.systemAnnouncementService = systemAnnouncementService;
	}
	

    public IAccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }
	
	/**
	 * 注册
	 */
	Object rsyncObjForRegist = new Object();
	public LoginInfoVO register(UsersVO vo) {

		log.info("----------【register】---------------");
		
		LoginInfoVO appInfo = new LoginInfoVO();
		
		synchronized (rsyncObjForRegist) {
			
			try {
				
				String phone = AESUtil.aesEncrypt(vo.getPhone(), AESUtil.KEY);
				vo.setPhone(phone);
				if(vo.getEmail()!=null){
					String email = AESUtil.aesEncrypt(vo.getEmail(), AESUtil.KEY);
					vo.setEmail(email);
				}

			} catch (Exception e) {

				log.error("registerAgent方法调用AESUtil.aesEncrypt方法对手机号码和邮箱进行加密时出现异常，异常信息：" + e.getCause().getMessage());
			}

			String agcode = null != vo.getAgcode() ? vo.getAgcode():"" ;
			UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

			dfh.skydragon.webservice.model.LoginInfo LoginInfo = userWebServiceWS.register("", 0, vo.getLoginname(), vo.getPassword(), vo.getAccountName(), vo.getAliasName(),
					                 vo.getPhone(), vo.getEmail(), vo.getQq(), "", vo.getRegisterIp(),vo.getArea(), vo.getBirthday(),
					                 "", null, "123456", agcode,vo.getInvitecode(),vo.getMicrochannel());

			if(LoginInfo == null){

				appInfo.setMsg("注册失败,请重新注册");
				return appInfo;

			}
			if(StringUtils.isNotEmpty(LoginInfo.getMsg())){

				if(!"success".equals(LoginInfo.getView())){

					appInfo.setMsg(LoginInfo.getMsg());
					return appInfo;
				}

			}
			appInfo.setUser(LoginInfo.getUser());

			//同步论坛信息

			userWebServiceWS.synBbsMemberInfo(vo.getLoginname().toLowerCase(), vo.getPassword());

		}
		return appInfo;
	}

	/**
	 * 登录
	 * @param vo
	 * @return
	 */
	public LoginInfoVO login(UsersVO vo) {

		 log.info("----------【login】---------------");


		 return userService.userLogin(vo);
	}


	/**
	 *     app重启时，根据token登录，记录登录日志，记录玩家活跃情况
	 * @param vo
	 * @return
	 */
	public LoginInfoVO loginByToken(UsersVO vo) {

		log.info("----------【loginByToken】---------------" + vo.getLoginname());


		return userService.loginByToken(vo);
	}


	/***
	 * 一次性获取版面内容填充信息，包括跑马灯公告、弹窗公告、站内信（未读条数）
	 * @return
	 * @throws Exception
	 */
	public BasicInfoVO getBasicInfo(BasicRequestVO requestVO) {

		log.info("----------【getBasicInfo】---------------");

		BasicInfoVO responseVO = userService.getBasicInfo(requestVO);

		return responseVO;
	}

	/***
	 * 获取所有未读
	 * @return
	 * @throws Exception
	 */
	public UnReadVO getUnReadInfo(String loginnane) {

		log.info("----------【getUnReadInfo】---------------");

		UnReadVO vo = userService.getUnReadInfo(loginnane);

		return vo;
	}

	/***
	 * 获取用户基本信息--原始信息
	 * @return
	 * @throws Exception
	 */
	public Users getUser(String loginName) {

		log.info("----------【getUser】---------------");

		return (Users) userService.get(Users.class, loginName);

	}

	/***
	 *  获取游戏平台余额
	 * @return
	 * @throws Exception
	 */
	public GamePlatBalanceVO getGamePlatBalance(GamePlatBalanceVO vo) {

		log.info("----------【getGamePlatBalance】---------------");

		String result = "";
		String msg = "";
		String loginname = vo.getLoginname();
		String password = vo.getPassword();

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		try {
			switch(vo.getPlat()) {
				case "ld":
					Users user = getUser(vo.getLoginname());
					vo.setBalance("" + user.getCredit());
					return vo;
				case "ag":
					result = userWebServiceWS.getAgGameMoney(loginname);
					break;
				case "agin":
					result = userWebServiceWS.getAginGameMoney(loginname);
					break;
				case "newpt":
					result = userWebServiceWS.getPtPlayerMoney(loginname, password) + "";
					break;
				case "slot":
					result = userWebServiceWS.getSlotBalance(loginname,"") + "";
					break;
				case "ebet":
					result = userWebServiceWS.getEbetBalance(loginname);
					break;
				case "dt":
					result = userWebServiceWS.getDtBalance(loginname, password);
					break;
				case "mg":
					result = userWebServiceWS.getMGBalance(loginname, password) + "";
					break;
				case "ttg":
					result = userWebServiceWS.getTtgBalance(loginname);
					break;
				case "qt":
					result = userWebServiceWS.getQtBalance(loginname);
					break;
				case "nt":
					result = userWebServiceWS.getNTBalance(loginname);
					JSONObject rj = JSONObject.fromObject(result);
					if (rj.getBoolean("result")) {
						result = rj.getDouble("balance") + "";
					} else {
						log.info(rj.getString("error"));
						msg = "服务异常";
					}
					break;
				case "ebetapp":
					result = userWebServiceWS.getEbetAppBalance(loginname);
					break;
				case "ea":
					result = userWebServiceWS.getEaGameMoney(loginname);
					break;
				case "sb":
					result = userWebServiceWS.getSbGameMoney(loginname);
					break;
				case "sba"://沙巴体育
					result = userWebServiceWS.getSbaGameMoney(loginname);
					break;
				case "png":
					result = userWebServiceWS.getPNGBalance(loginname) + "";
					break;
				case "mwg":
					result = userWebServiceWS.getMwgGameMoney(loginname) + "";
					break;
				case "n2live":
					NTwoCheckClientResponseBean bean = userWebServiceWS.getNTwoCheckClientResponseBean(loginname);
					if (bean == null) {
						msg = "系统繁忙，请稍后再试";
					} else {
						result = String.valueOf(bean.getBalance().setScale(2, RoundingMode.HALF_UP));
					}
					break;
				case "qd":
					result = userWebServiceWS.querySignAmount(loginname).toString();
					break;
				case "chess":
					result = userWebServiceWS.getChessBalance(loginname).toString();
					break;
				case "fish":
					result = userWebServiceWS.getFishBalance(loginname).toString();
					break;
				case "bbin":
					result = userWebServiceWS.getBbinBalance(loginname).toString();
					break;
				case "fanya":
					result = userWebServiceWS.getFanyaBalance(loginname).toString();
					break;
				case "vr":
					result = userWebServiceWS.getKyqpBalance(loginname,vo.getPlat(),"127.0.0.1").toString();
					break;
				case "kyqp":
					result = userWebServiceWS.getKyqpBalance(loginname,vo.getPlat(),"127.0.0.1").toString();
					break;

			}
		}catch(Exception e){
			e.printStackTrace();
			msg = "系统繁忙，请稍后再试";
		}
		log.info("result = " + result);

		if(StringUtils.isEmpty(result)){
			result = "0.00";
		}

		if(!isMoney(result)){
			msg = "系统繁忙，请稍后再试";
		}

		vo.setBalance(result);
		vo.setMsg(msg);
		return vo;

	}

	/***
	 * 修改密码
	 * @return
	 * @throws Exception
	 */
	public ChangePasswordVO changePassword(ChangePasswordVO vo) {

		log.info("----------【changePassword】---------------");

		if(vo == null){
			vo = new ChangePasswordVO();
			vo.setMsg("数据传输错误");
			return vo;
		}
		//vo = userService.changePassword(vo);

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");
		String message = userWebServiceWS.modifyPassword(vo.getLoginname(), vo.getOriginalPassword(), vo.getNewPassword(),vo.getIp());

		if(!"success".equals(message)){
			vo.setMsg(message);
		}

		return vo;

	}

	/***
	 * 游戏之间转账
	 * @return
	 * @throws Exception
	 */
	public TransferVO transfer(TransferVO vo) {

		log.info("----------【transfer】---------------");

		if(vo == null){
			vo = new TransferVO();
			vo.setMsg("数据传输错误");
			return vo;
		}
		   String strLimit = null;
		   String loginname = vo.getLoginName();
		   Double remit = Double.parseDouble(vo.getMoney());
		   String seqId = "";

		   Users user = getUser(loginname);

		   if(!StringUtils.equals(getPostCode(vo.getSid()), user.getPostcode())){

				vo.setMsg("你的账号在其他设备登录过，为了你的资金安全，请重新登录！");
				return vo;
		   }

		   UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		   try {
			//龙都转其他
			if ("ld".equals(vo.getFrom())) {

				if ("newpt".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferNewPtIn(loginname,
							remit);
				}
				if ("ttg".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferTtIn(loginname, remit);
				}
				if ("nt".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferToNT(loginname, remit);
				}
				if ("agin".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferInforAginDsp(loginname,
							remit);
				}
				if ("qt".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferQtIn(loginname, remit);
				}
				if ("dt".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferDtIn(loginname, remit);
				}
				if ("ebetapp".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferToEBetApp(loginname,remit);
				}
				if ("ea".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferIn(loginname,remit);
				}
				if ("sb".equals(vo.getTo())) {
					strLimit = userWebServiceWS.transferInforTy(loginname,remit);
				}
				if ("sba".equals(vo.getTo())) {//沙巴体育
					strLimit = userWebServiceWS.transferInforSbaTy(loginname,
							remit);
				}
				if ("qd".equals(vo.getTo())){
					strLimit = "无该平台";
				}
			}else if("qd".equals(vo.getFrom())){
				if("ld".equals(vo.getTo())){
					strLimit= "不能转账，签到余额不能转到主账户";
					vo.setMsg(strLimit);
					return vo;
				}
				String result = userWebServiceWS.chekDepositOrderRecordM(loginname,user.getLevel().toString());
				if(!result.equals("true")){
					vo.setMsg(result);
					return vo;
				}
				strLimit = userWebServiceWS.transferSign(loginname, remit, vo.getTo());
			}else{
			// 其他转龙都
			if ("ld".equals(vo.getTo())) {

				if ("newpt".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferNewPtOut(loginname,
							remit);
				}
				if ("ttg".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferTtOut(loginname, remit);
				}
				if ("nt".equals(vo.getFrom())) {
					strLimit = userWebServiceWS
							.transferFromNT(loginname, remit);
				}
				if ("agin".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferOutforAginDsp(
							loginname, remit);
				}
				if ("qt".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferQtOut(loginname, remit);
				}
				if ("dt".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferDtOut(loginname, remit);
				}
				if ("n2live".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferOutNTwo(loginname,
							remit);
				}
				if ("ebetapp".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferFromEBetApp(loginname,
							remit);
				}
				if ("ea".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferOut(loginname,
							remit);
				}
				if ("sb".equals(vo.getFrom())) {
					strLimit = userWebServiceWS.transferOutforTy(loginname,
							remit);
				}
				if ("sba".equals(vo.getFrom())) {//沙巴体育
					strLimit = userWebServiceWS.transferOutforSbaTy(loginname,
							remit);
				}
				if ("qd".equals(vo.getFrom())){
					strLimit = "签到账户不能转账到主账户";
				}
			}else if("qd".equals(vo.getTo())){
				strLimit = "游戏账户不能转账到签到账户";
			}
			}
		} catch (Exception e) {

			log.info("转账异常：" + e.getMessage());
			e.printStackTrace();
			strLimit = "系统繁忙，请稍后再试";

		}

		   log.info("strLimit = " + strLimit);

		if(StringUtils.isNotEmpty(strLimit)){

			   vo.setMsg(strLimit);//
		   }

	       return vo;
	}



	/**
	 *
	 * 一键额度回归龙都主账户
	 *
	 *
	 */
	private static Object oneKeyObject = new Object();
	public String oneKeyTransferOutAllPlatMoney(String loginName , String ip){

		log.info("----------【oneKeyTransferOutAllPlatMoney】---------------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String result= null;

		return result;
	}

	/***
	 * 获取在线支付页面地址
	 * @return
	 * @throws Exception
	 */
	public OnlinePayVO getOnlinePayUrl(UsersVO vo) {

		log.info("----------【getOnlinePayUrl】---------------");

		OnlinePayVO onlinePayVO = userService.getOnlinePayUrl(vo);
		if(StringUtils.isEmpty(onlinePayVO.getDomain())){
			onlinePayVO.setMsg("系统参数配置错误");
		}
		return onlinePayVO;

	}
	/***
	 * 获取web地址
	 * @return
	 * @throws Exception
	 */
	public OnlinePayVO getWebDomain() {

		log.info("----------【getWebDomain】---------------");

		OnlinePayVO onlinePayVO = userService.getWebDomain();
		if(StringUtils.isBlank(onlinePayVO.getDomain())){
			onlinePayVO.setMsg("系统参数配置错误");
		}
		return onlinePayVO;

	}

	/**************************************** 账务清单开始   ****************************************/

	/***
	 * 获取在线存款记录
	 * @return
	 * @throws Exception
	 */
	public Page getOnlinePayRecords(OnlinePayRecordVO vo) {

		log.info("----------【getOnlinePayRecords】---------------");

		return userService.getOnlinePayRecords(vo);
	}


	/***
	 * 获取手工存款记录
	 * @return
	 * @throws Exception
	 */
	public Page getCashinRecords(OnlinePayRecordVO vo) {

		log.info("----------【getCashinRecords】---------------");

		return userService.getCashinRecords(vo);
	}



	/***
	 * 提款记录
	 * @return
	 * @throws Exception
	 */
	public Page getWithdrawRecords(OnlinePayRecordVO vo) {

		log.info("----------【getWithdrawReccords】---------------");

		return userService.getWithdrawRecords(vo);
	}



	/***
	 * 户内转账记录
	 * @return
	 * @throws Exception
	 */
	public Page getTransferRecords(OnlinePayRecordVO vo) {

		log.info("----------【getTransferReccords】---------------");

		return userService.getTransferRecords(vo);
	}


	/***
	 * 优惠活动记录
	 * @return
	 * @throws Exception
	 */
	public Page getConcessionRecords(OnlinePayRecordVO vo) {

		log.info("----------【getConcessionReccords】---------------");

		return userService.getConcessionRecords(vo);
	}

	/***
	 * 存送优惠记录
	 * @return
	 * @throws Exception
	 */
	public Page getCouponRecords(OnlinePayRecordVO vo) {

		log.info("----------【getCouponRecords】---------------");

		return userService.getCouponRecords(vo);
	}



	/***
	 * 自助反水记录
	 * @return
	 * @throws Exception
	 */
	public Page getXimaRecords(OnlinePayRecordVO vo) {

		log.info("----------【getXimaReccords】---------------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String start ="2013-01-01 00:00:01" ;
		String end =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		try {

		Page page = new Page();

		AutoXima ximaTotalRecord = userWebServiceWS.getTotalCount(vo.getLoginName(), start, end);
		int totalRecords=ximaTotalRecord.getTotalCount();
		if(totalRecords==0){

			page.setPageNumber(vo.getPageIndex());
			page.setSize(vo.getPageSize());
			page.setTotalRecords(0);
			page.setTotalPages(0);

		}else{

			List<AutoXima> ximaList = userWebServiceWS.searchXimaDetail(vo.getLoginName(), start, end, vo.getPageIndex(), vo.getPageSize());
			page.setPageNumber(vo.getPageIndex());
			page.setSize(vo.getPageSize());
			page.setTotalRecords(ximaTotalRecord.getTotalCount());
			if(totalRecords%vo.getPageSize()==0){
				page.setTotalPages(totalRecords/vo.getPageSize());
			}else{
				page.setTotalPages(totalRecords/vo.getPageSize()+1);
			}

			List<AutoXimaVO> listBack = new ArrayList<AutoXimaVO>();

			if(ximaList != null && ximaList.size() > 0){
				for(AutoXima autoXima : ximaList){
					AutoXimaVO autoXimaVO = new AutoXimaVO();
					autoXimaVO.setEndTime(autoXima.getEndTime());
					autoXimaVO.setMessage(autoXima.getMessage());
					autoXimaVO.setPno(autoXima.getPno());
					autoXimaVO.setRate(autoXima.getRate());
					autoXimaVO.setStartTime(autoXima.getStartTime());
					autoXimaVO.setStatisticsTimeRange(autoXima.getStatisticsTimeRange());
					autoXimaVO.setValidAmount(autoXima.getValidAmount());
					autoXimaVO.setXimaAmount(autoXima.getXimaAmount());
					autoXimaVO.setXimaStatus(autoXima.getXimaStatus());
					autoXimaVO.setXimaType(autoXima.getXimaType());
					listBack.add(autoXimaVO);
				}
			}

			page.setPageContents(listBack);

		}

			return page;

		} catch (Exception e) {

			log.error("----------【getXimaReccords error】---------------");
			e.printStackTrace();
		}
		return new Page();
	}


	/***
	 * 存款附言记录
	 * @return
	 * @throws Exception
	 */
	public Page getDepositOrderRecords(OnlinePayRecordVO vo) {

		log.info("----------【getDepositOrderRecords】---------------");

		return userService.getDepositOrderRecords(vo);

	}



	/***
	 * 积分记录
	 * @return
	 * @throws Exception
	 */
	public Page getPointRecords(OnlinePayRecordVO vo) {

		log.info("----------【getPointRecords】---------------");

		return userService.getPointRecords(vo);

	}


	/***
	 * 好友推荐记录
	 * @return
	 * @throws Exception
	 */
	public Page getFriendRecords(OnlinePayRecordVO vo) {

		log.info("----------【getFriendRecords】---------------");

		return userService.getFriendRecords(vo);

	}

	public Userbankinfo queryUserBankInfo(UserBankInfoVO vo) {

		log.info("----------【queryUserBankInfo】---------------");

		Userbankinfo obj = userBankInfoService.queryUserBankInfo(vo);

		if (null != obj && !StringUtils.isBlank(obj.getBankno()) && obj.getBankno().length()>8) {
			obj.setBankno(obj.getBankno().replaceAll("^([0-9]{4})[0-9]+([0-9]{4})$", "$1********$2"));
		}

		return obj;
	}

	public List<Userbankinfo> getUserbankinfoList(UserBankInfoVO vo) {

		log.info("----------【getUserbankinfoList】---------------");

		List<Userbankinfo> obj = userBankInfoService.queryUserBankInfoList(vo);

		return obj;
	}
	public String submitWithdrawal(UserBankInfoVO vo) {

		log.info("----------【submitWithdrawal】---------------");

		String loginName = vo.getLoginName();
		String bankName = vo.getBankName();
		Double amount = vo.getAmount();
		String accountNo = vo.getAccountNo();
		String password = vo.getPassword();
		String bankAddress = vo.getBankAddress();
		Integer questionId = vo.getQuestionId();
		String answer = vo.getAnswer();
		String type = vo.getType();
		String ip = vo.getIp();

		log.info("submitWithdrawal方法的参数为：【loginName=" + loginName + "，bankName=" + bankName + "，amount=" + amount + "，accountNo=" + accountNo + "，password=" + password + "，bankAddress=" + bankAddress + "，questionId=" + questionId + "，answer=" + answer + "，type=" + type + "，ip=" + ip + "】");

		Users users = (Users) userService.get(Users.class, loginName);

		if (null == users) {

			return "没有查到玩家账号为“ " + loginName + "”的数据。";
		}

		if("AGENT".equals(users.getRole())){//代理提款，如果没有设置微信，qq，提醒去完善信息(代理提款已不是调用这里，这段代码可以去掉，不去掉也行，暂时保留着)

			if(StringUtils.isEmpty(users.getQq()) || StringUtils.isEmpty(users.getMicrochannel())){

				return "您好，请先点击账户设置完善您的信息【微信、QQ】";
			}
		}else{//玩家提款，如果没有设置昵称，微信，qq，提醒去完善信息

			if(StringUtils.isEmpty(users.getQq()) || StringUtils.isEmpty(users.getMicrochannel()) || StringUtils.isEmpty(users.getAliasName())){

				return "您好，请先点击账户设置完善您的信息【昵称、微信、QQ】";
			}
		}

		String bankStatus = bankStatusService.getBankStatus(bankName);

		if (StringUtils.isNotBlank(bankStatus) && !"OK".equalsIgnoreCase(bankStatus)) {

			return "银行系统维护中，请选择其他银行或稍后再试！";
		}

		if(!StringUtils.equals(getPostCode(vo.getSid()), users.getPostcode())){

			return "你的账号在其他设备登录过，为了你的资金安全，请重新登录！";
		}

		if (users.getFlag() == 1) {

			return "账号已经被冻结，不能进行提款操作！";
		}

		if (null == amount) {

			return "提款金额不能为空！";
		}

		if (amount < 1) {

			return "[提示]单次提款金额最低1元！";
		}

		if (StringUtils.isBlank(accountNo)) {

			return "卡折号不能为空，请重新输入！";
		}

		if (UserRole.AGENT.getCode().equalsIgnoreCase(users.getRole())) {

			String[] typeArgs = { "liveall", "slotmachine" };
			List<String> typeList = Arrays.asList(typeArgs);

			if (StringUtils.isBlank(type) || !typeList.contains(type)) {

				return "代理提款类型不能为空！";
			}
		}

		Userbankinfo userBankInfo = userBankInfoService.queryUserBankInfo(vo);

		if (null == userBankInfo) {

			return "您输入的银行名称有误，或银行账户未启用，请重新输入！";
		}

		accountNo = userBankInfo.getBankno();

		if (UserRole.MONEY_CUSTOMER.getCode().equalsIgnoreCase(users.getRole())) {

			if (StringUtils.isBlank(answer)) {

				return "请输入您的回答！";
			}

			Boolean flag = proposalService.questionValidate(loginName, answer, questionId);

			if (!flag) {

				int size = proposalService.questionsNumber(loginName);

				if (size > 0) {

					proposalService.saveOrUpdateQuestionStatus(loginName);

					QuestionStatus status = proposalService.queryQuesStatus(loginName);

					if (status.getErrortimes() >= 5) {

						proposalService.EnableUser(loginName, false, "system", "提款密保验证错误5次，冻结！");

						return "密保答案输入5次错误！";
					}

					return "密保验证答案错误！";
				} else {

					return "你还未绑定密保，请先绑定密保";
				}
			}
		}

		// 拆分基数
		Double splitAmount = Double.valueOf(Configuration.getInstance().getValue("CashoutSplitAmount"));

		return UserSynchronizedPool.getSynchUtilByUser(loginName).withdraw(proposalService, loginName, loginName, password, users.getRole(), Constants.FROM_FRONT, amount, users.getAccountName(),
																			   StringUtils.trim(accountNo), Constants.DEFAULT_ACCOUNTTYPE, StringUtils.trim(bankName), StringUtils.trim(bankAddress),
																			   StringUtils.trim(bankAddress), null, users.getPhone(), ip, null, null, users, splitAmount, type);
	}

	public synchronized String saveUserQuestion(UserBankInfoVO vo) {

		String loginName = vo.getLoginName();
		String password = vo.getPassword();
		Integer questionId = vo.getQuestionId();
		String answer = vo.getAnswer();

		log.info("submitWithdrawal方法的参数为：【loginName=" + loginName + "，password=" + password + "，questionId=" + questionId + "，answer=" + answer + "】");

		Users users = (Users) userService.get(Users.class, loginName);

		if (null == users) {

			return "没有查到玩家账号为“ " + loginName + "”的数据。";
		}

		if (StringUtils.isBlank(answer)) {

			return "请输入您的回答！";
		}

		if (!DataDictionary.Question.existKey(String.valueOf(questionId))) {

			return "您所绑定的问题不存在！";
		}

		if (!EncryptionUtil.encryptPassword(password).equalsIgnoreCase(users.getPassword())) {

			return "登录密码错误，请重新输入！";
		}

		return proposalService.saveQuestion(loginName, questionId, answer);
	}

	/**************************************** 账务清单结束   ****************************************/

	/**************************************** 最新优惠提供服务开始处   ****************************************/

	public List<LatestPreferential> queryLatestPreferentialList(LatestPreferentialVO latestPreferentialVO) {

		return latestPreferentialService.queryLatestPreferentialList(latestPreferentialVO);
	}

	public LatestPreferential queryLatestPreferential(LatestPreferentialVO latestPreferentialVO) {

		return latestPreferentialService.queryLatestPreferential(latestPreferentialVO);
	}

	public PreferentialVO queryPreferentialPage(LatestPreferentialVO latestPreferentialVO) {

		List<LatestPreferential> preList = latestPreferentialService.queryLatestPreferentialList(latestPreferentialVO);

		PreferentialVO preferentialVO = new PreferentialVO();

		preferentialVO.setPreData(preList);

		return preferentialVO;
	}

	/**************************************** 最新优惠提供服务结束处   ****************************************/

	/**************************************** 系统配置提供服务开始处   ****************************************/

	public List<SystemConfig> querySystemConfigList(SystemConfigVO vo) {

		return systemConfigService.querySystemConfigList(vo);
	}

	public SystemConfig querySystemConfig(SystemConfigVO vo) {

		return systemConfigService.querySystemConfig(vo);
	}

	/**************************************** 系统配置提供服务结束处   ****************************************/

	/**************************************** 手机游戏提供服务开始处   ****************************************/

	public GameVO queryGameUrl(SystemConfigVO vo) {

		vo.setTypeNo("type300");
		vo.setItemNo("001");
		vo.setFlag("否");

		SystemConfig config =  this.querySystemConfig(vo);

		GameVO gameVO = new GameVO();

		if (null == config) {

			gameVO.setMessage("系统参数配置错误。");
		} else {
			gameVO.setDomain(config.getValue());
		}

		return gameVO;
	}

	/**
	 * 热门游戏登录
	 * @param vo
	 * @return
	 */
	public HotGameLoginVO hotGameLogin(HotGameLoginVO vo) {

		log.info("----------【hotGameLogin】---------------");

		return userService.hotGameLogin(vo);
	}


	/**************************************** 手机游戏提供服务结束处   ****************************************/

	/**************************************** 优惠评论提供服务开始处   ****************************************/

	public String savePreferentialComment(PreferentialComment preferentialComment) {

		String message = "";

		try {

			message = preferentialCommentService.savePreferentialComment(preferentialComment);
		} catch (Exception e) {

			log.error("savePreferentialComment方法保存评论发生异常，异常信息："  + e.getMessage());
			message = "评论失败，请稍后重试！";
		}

		return message;
	}

	public List<PreferentialComment> queryPreferentialCommentList(PreferentialCommentVO vo) {

		return preferentialCommentService.queryPreferentialCommentList(vo);
	}

	public Integer countPreferentialComment(PreferentialCommentVO vo) {

		return preferentialCommentService.countPreferentialComment(vo);
	}

	/**************************************** 优惠评论提供服务结束处   ****************************************/

	/**************************************** 自助优惠提供服务开始处   ****************************************/

	public List<PreferentialConfig> queryPreferentialConfigList(String loginName, String passage) {

		Users users = (Users) userService.get(Users.class, loginName);

		if (null == users) {

			log.error("queryPreferentialConfigList方法没有查到玩家账号为“ " + loginName + "”的数据。");
			return null;
		}

/*		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		List<YouHuiConfig> list = userWebServiceWS.getYouHuiConfig(users.getLevel());*/

		return preferentialConfigService.queryPreferentialConfigList(null, null, String.valueOf(users.getLevel()), passage);
	}

	public String saveSelfDepositFromGW(PlatformDepositVO vo) {

		String loginName = vo.getLoginName();

		Users user = getUser(loginName);

		if (!StringUtils.equals(vo.getSid(), user.getPostcode())) {

			return "你的账号在其他设备登录过，为了你的资金安全，请重新登录！";
		}

		return commonSelfDeposit(vo);
	}

	public String saveSelfDeposit(PlatformDepositVO vo) {

		String loginName = vo.getLoginName();

		Users user = getUser(loginName);

		if (!StringUtils.equals(getPostCode(vo.getSid()), user.getPostcode())) {

			return "你的账号在其他设备登录过，为了你的资金安全，请重新登录！";
		}

		return commonSelfDeposit(vo);
	}

	private String commonSelfDeposit(PlatformDepositVO vo) {

		String loginName = vo.getLoginName();
		Integer youhuiType = vo.getYouhuiType();
		Double remit = vo.getRemit();

		log.info("commonSelfDeposit方法的参数为：【loginName=" + loginName + ",youhuiType=" + youhuiType + ",remit=" + remit + "】");

		Pattern pattern = Pattern.compile("^[1-9][0-9]*([0-9]|.0)$");

		if (!pattern.matcher(String.valueOf(remit)).matches()) {

			return "抱歉，金额只能输入整数。";
		}

		String[] youhuiArr = { "590", "591", "592", "593", "594", "596", "597", "598", "599", "702", "703", "704", "705",
							   "706", "707", "708", "709", "710", "711", "712", "730", "731", "732", "733", "734", "735" };

		List<String> list = Arrays.asList(youhuiArr);

		if (!list.contains(String.valueOf(youhuiType))) {

			return "不存在该种优惠。";
		}

		// 每天凌晨0点到1点不允许自助优惠
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int hour = c.get(Calendar.HOUR_OF_DAY);

		if (hour >= 0 && hour < 1) {

			return "每天的0点至1点是自助优惠系统维护时间。";
		}

		String text = ProposalType.getText(youhuiType);

		if (StringUtils.isBlank(text)) {

			return "该优惠类型不存在对应的优惠名称！";
		}

		text = text.substring(2);

		String youHuiFlag = constService.queryConstValue(text);

		if (!youHuiFlag.equals("1")) {

			return text + "维护中...";
		}

		/* 判断是否老虎机存送,并限制最低10元  */
		if (Arrays.asList(new String[] { "590", "591", "598", "599", "702", "703", "704", "705", "706", "707",
										 "708", "709", "710", "711", "712", "730", "731", "732", "733", "734", "735" }).contains(String.valueOf(youhuiType))) {

			if (remit < 10) {

				return "转账金额不能少于10元。";
			}
		} else {

			if (remit < 100) {

				return "转账金额不能少于100元。";
			}
		}

		return SynchronizedUserPool.getSynchronizedByUser(loginName).selfDeposit(vo);
	}

	public Page queryLosePromoPageList(LosePromoVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.losePromoReccords(vo.getLoginName(), vo.getPageIndex(), vo.getPageSize());

	}

	public Page queryDayCommissionPageList(AccountVO vo) {
		return accountService.queryDayCommissionPageList(vo);
	}

	// 领取/取消自助救援金
	public String receiveOrCancelLosePromo(LosePromoVO vo) {

		String pno = vo.getPno();
		Integer targetFlag = vo.getProposalFlag();
		String ip = vo.getIp();
		String target = vo.getPlatform();

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.optLosePromo(pno, targetFlag, ip, target);


	}

	// 查询对应平台的洗码数据
	public String platformSelfXimaData(SelfXimaVO vo) {

		String loginName = vo.getLoginName();
		String platform = vo.getPlatform();
		String constId = vo.getPlatform();

		log.info("platformSelfXimaData方法的参数为：【loginName=" + loginName + "，platform=" + platform + "】");

		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (StringUtils.isBlank(platform)) {

			resultMap.put("message", "游戏平台编码为空！");

			return JSONObject.fromObject(resultMap).toString();
		}

		if ("mg".equals(platform)) {

			constId = "mgs";
		}

		String youHuiFlag = constService.queryConstValue(constId);

		if (!youHuiFlag.equals("1")) {

			resultMap.put("message", platform + "正在维护中。");

			return JSONObject.fromObject(resultMap).toString();
		}

		// 获得洗码起始时间
		String startTime = gameinfoService.getOtherXimaEndTime(loginName, platform);
		// 获取洗码截止时间
		String endTime = DateUtil.getDateFormat(new Date());

		log.info("platformSelfXimaData方法中startTime=" + startTime + ",endTime=" + endTime);

		Date stime = null;
		Date etime = null;

		try {

			stime = DateUtil.getDateFromDateStr(startTime);
			etime = DateUtil.getDateFromDateStr(endTime);
		} catch (ParseException e) {

			resultMap.put("message", "日期格式错误，请重新填写。");

			return JSONObject.fromObject(resultMap).toString();
		}

		if (etime.getTime() < stime.getTime()) {

			resultMap.put("message", "截止时间必须大于起始时间！");

			return JSONObject.fromObject(resultMap).toString();
		}

		AutoXima autoXima = null;

		try {

			UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

			autoXima = userWebServiceWS.getAutoXimaObject(endTime, startTime, loginName, platform);
		} catch (Exception e) {

			log.error("platformSelfXimaData方法获取洗码数据发生异常，异常信息：" + e.getMessage());

			resultMap.put("message", "本次自助洗码异常，请稍后重试！");

			return JSONObject.fromObject(resultMap).toString();
		}

		if (StringUtils.isBlank(autoXima.getMessage())) {

			resultMap.put("message", "SUCCESS");
			resultMap.put("startTime", startTime);
			resultMap.put("endTime", endTime);
			resultMap.put("validAmount", autoXima.getValidAmount());
			resultMap.put("rate", autoXima.getRate());
			resultMap.put("ximaAmount", autoXima.getXimaAmount());

			return JSONObject.fromObject(resultMap).toString();
		} else {

			resultMap.put("message", autoXima.getMessage());
			resultMap.put("startTime", startTime);
			resultMap.put("endTime", endTime);

			return JSONObject.fromObject(resultMap).toString();
		}
	}

	/**
	 * 老虎机自助反水获取平台信息
	 * @param platform
	 * @param _loginname
	 * @return
	 */
	public String getAutoSlotXimaObject(SelfXimaVO vo) {

	    return JSONObject.fromObject(gameinfoService.getSlotAutoXimaObject(vo.getLoginName())).toString();
	}

	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();

	public String executeXima(SelfXimaVO vo) {

		String loginName = vo.getLoginName();
		String platform = vo.getPlatform();
		String startTime = vo.getStartTime();
		String endTime = vo.getEndTime();

		log.info("executeXima方法的参数为：【loginName=" + loginName + "，platform=" + platform + "，startTime=" + startTime + "，endTime=" + endTime + "】");

		Map<String, Object> resultMap = new HashMap<String, Object>();

		Users user = (Users) userService.get(Users.class, loginName);

		if (null == user) {

			resultMap.put("message", "未找到玩家“" + loginName + "”的账户信息。");
			return JSONObject.fromObject(resultMap).toString();
		}

		if (StringUtils.isBlank(platform)) {

			resultMap.put("message", "请选择游戏平台！");
			return JSONObject.fromObject(resultMap).toString();
		}

		if (StringUtils.isBlank(startTime)) {

			resultMap.put("message", "起始时间有误！");
			return JSONObject.fromObject(resultMap).toString();
		}

		if (StringUtils.isBlank(endTime)) {

			resultMap.put("message", "截止时间有误！");
			return JSONObject.fromObject(resultMap).toString();
		}

		String str = loginName + platform;

		Long nowTime = System.currentTimeMillis();

		Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry<String, Long> entry = it.next();

			String key = entry.getKey();
			Long value = cacheMap.get(key);

			if (nowTime - value > 1000 * 60) {

				it.remove();
			}
		}

		if (cacheMap.get(str) != null) {

			resultMap.put("message", "60秒内不能重复提交同一平台。");
			return JSONObject.fromObject(resultMap).toString();
        }

		cacheMap.put(str, nowTime);

		String youHuiFlag = constService.queryConstValue(platform);

		if (!youHuiFlag.equals("1")) {

			resultMap.put("message", platform + "正在维护中。");
			return JSONObject.fromObject(resultMap).toString();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		int hour = c.get(Calendar.HOUR_OF_DAY);

		List<String> list = Arrays.asList(new String[] { "ttg", "gpi", "pttiger", "nt", "qt", "mg", "dt" });

		if (!list.contains(platform)) {

			if (hour >= 12 && hour < 15) {

				resultMap.put("message", "抱歉，暂不能执行自助洗码操作\\n每天的12点至15点是系统洗码时间。");
				return JSONObject.fromObject(resultMap).toString();
			}
		}

		if (hour >= 0 && hour < 3) {

			resultMap.put("message", "抱歉，暂不能执行自助洗码操作\\n每天的0点至3点是自助洗码系统维护时间。");
			return JSONObject.fromObject(resultMap).toString();
		}

		Date start = null;
		Date end = null;

		try {

			Date tempDate = DateUtil.getDateFromDateStr(startTime);

			// 两种情况：防止页面串改起始时间或者防止提交了自助洗码后，先停留在页面，等自助反水执行后，再重复提交

			String date = gameinfoService.getOtherXimaEndTime(loginName, platform);

			if (StringUtils.isNotBlank(date)) {

				start = DateUtil.getDateFromDateStr(date);

				if (start.compareTo(tempDate) != 0) {

					resultMap.put("message", "不允许的时间间隔。");
					return JSONObject.fromObject(resultMap).toString();
				}

				end = DateUtil.getDateFromDateStr(endTime);
			} else {

				resultMap.put("message", "本次洗码异常，请重试！");
				return JSONObject.fromObject(resultMap).toString();
			}
		} catch (ParseException e) {

			resultMap.put("message", "日期格式错误，请重新填写。");
			return JSONObject.fromObject(resultMap).toString();
		}

		if (end.getTime() > new Date().getTime()) {

			end = new Date();
		}

		long ldate = end.getTime() - start.getTime();

		if ((ldate / 1000) < 300) {

			resultMap.put("message", "洗码相隔时间不得小于5分钟！");
			return JSONObject.fromObject(resultMap).toString();
		}

		AutoXimaReturnVo autoXimaReturnVo = gameinfoService.checkSubmitXima(loginName, platform);

		String msg = "";

		if (null != autoXimaReturnVo && !autoXimaReturnVo.getB() && StringUtils.isBlank(autoXimaReturnVo.getMsg())) {

			try {

				msg = UserSynchronizedPool.getSynchUtilByUser(user.getLoginname()).selfXima(gameinfoService, user, end, start, platform);

				if (StringUtils.isNotBlank(msg) && msg.contains("成功")) {

					resultMap.put("message", "SUCCESS");
				}
			} catch (Exception e) {

				log.error("executeXima方法执行execXima发生异常，异常信息：" + e.getMessage());

				resultMap.put("message", "本次洗码异常，请稍后重试！");
				return JSONObject.fromObject(resultMap).toString();
			}
		} else if (null != autoXimaReturnVo && autoXimaReturnVo.getB() && StringUtils.isNotBlank(autoXimaReturnVo.getMsg())) {

			msg = autoXimaReturnVo.getMsg();
		} else {

			resultMap.put("message", "本次洗码异常，请稍后重试！");
			return JSONObject.fromObject(resultMap).toString();
		}

		resultMap.put("message", msg);

		return JSONObject.fromObject(resultMap).toString();
	}

	public AutoXima execSlotXima(SelfXimaVO vo) {
		String loginName = vo.getLoginName();
		String platform = vo.getPlatform();
		AutoXima autoXima= null;
		log.info("execSlotXima方法的参数为：【loginName=" + loginName + "，platform=" + platform);

		///***************************begin
		String str = vo.getLoginName()+platform ;
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
        if (cacheMap.get(str) != null) {
        	autoXima = new AutoXima("60秒内不能重复提交同一平台。");
            return autoXima;
        }
        cacheMap.put(str, nowTiem);
		///***************************end


		Users user = (Users) userService.get(Users.class, loginName);
		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		try {
			AutoXimaReturnVo autoXimaReturnVo =userWebServiceWS.checkSubmitXima(loginName, platform);
			if (null != autoXimaReturnVo && !autoXimaReturnVo.getB() && null == autoXimaReturnVo.getMsg()) {

				String msg = userWebServiceWS.execXimaSlot(user, platform);
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

	//判断讯息类别
	public String getMessage(int status){

		String message = "";
		switch (status) {
		case 1:
			message = "[提示]重覆的玩家注册ip地址或姓名";
			break;
		case 2:
			message = "[提示]已申请过app下载彩金";
			break;
		case 3:
			message = "[提示]请绑定姓名";
			break;
		default:
			message = null;//[提示]用户信息没问题!
			break;
		}
		return message;

	}
	/**
	 * 自助体验金-app下载彩金 基本信息检查
	 * @param vo
	 * @return
	 */
	public SelfExperienceMoneyVO haveSameInfo(String loginName,String type) {

		log.info("----------【haveSameInfo】---------------");

		SelfExperienceMoneyVO vo = new SelfExperienceMoneyVO();

		Users user = cs.getUsers(loginName);

		UserWebServiceWS userWebserviceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");


		if("1".equals(type)){//下载彩金

			String youHuiFlag = userWebserviceWS.checkGameIsProtect("app下载彩金");
			if (!youHuiFlag.equals("1")) {

				vo.setMessage("下载彩金维护中...");
				return vo;
			}

			Integer statusCode = new Integer(0);

			statusCode = userWebserviceWS.checkForAppPreferential(user.getAccountName(),user.getRegisterIp(),user.getLoginname());

			String message = getMessage(statusCode);

			if(StringUtils.isNotEmpty(message)){

				vo.setMessage(message);
				return vo;
			}

		}else{


			String youHuiFlag = constService.queryConstValue("PT8元自助优惠");

			if (!"1".equals(youHuiFlag)) {

				vo.setMessage("PT8元自助优惠维护中...");
				return vo;
			}

			String result = userWebserviceWS.haveSameInfo(user.getLoginname());

			if(StringUtils.isNotEmpty(result)){

				vo.setMessage(result);
				return vo;

			}

			Integer numbers = userWebserviceWS.getCardNums(user.getLoginname());

			if(0 == numbers){

				vo.setMessage("请绑定银行卡");
				return vo;

			}

		}



		String smsFlag = constService.queryConstValue("短信反转验证");

		if("1".equals(smsFlag)){//短信反转验证

			vo.setVerificationType("smsReverse");

		}else{//语音验证与短信验证

			vo.setVerificationType("sms");
		}
		try {
			vo.setPhone(AESUtil.aesDecrypt(user.getPhone(),AESUtil.KEY));
		} catch (Exception e) {
			e.printStackTrace();
		}
			return vo ;
	}
	/**
	 * 开关检查
	 * @param vo
	 * @return
	 */
	public boolean checkConst(String constId) {

		String constFlag = constService.queryConstValue(constId);
		if (!"1".equals(constFlag)) {
			return false;
		}
		return true;
	}
	/**
	 * 短信反转验证-获取接收手机号和验证码
	 * @param vo
	 * @return
	 */
	public SelfExperienceMoneyVO getPhoneAndCode(String loginName) {

		log.info("----------【getPhoneAndCode】---------------");

		String code = RandomStringUtils.randomNumeric(4);
		Userstatus userstatus = (Userstatus)proposalService.get(Userstatus.class, loginName);
		if(userstatus == null){
				userstatus = new Userstatus();
				userstatus.setLoginname(loginName);
				userstatus.setCashinwrong(0);
				userstatus.setLoginerrornum(0);
				userstatus.setMailflag(0);
				userstatus.setValidateCode(code);
				proposalService.save(userstatus);
		}else if(StringUtils.isNotBlank(userstatus.getValidateCode())){
			code = userstatus.getValidateCode() ;
		}else{
			userstatus.setValidateCode(code);
			proposalService.update(userstatus);
		}
		//获取手机号
		String phone = "手机号未配置";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		dc.add(Restrictions.eq("typeNo", "type100"));
		dc.add(Restrictions.eq("flag", "否"));
		List<SystemConfig> configs = proposalService.findByCriteria(dc);
		if(null != configs && configs.size()>0){
			phone = configs.get(0).getValue() ;
		}

		SelfExperienceMoneyVO vo = new SelfExperienceMoneyVO();
		vo.setCode(code);
		vo.setPhone(phone);
		return vo;

	}
	/**
	 * 短信反转验证-提交验证
	 * @param vo
	 * @return
	 */
	public String checkPhoneCode(String loginName) {

		log.info("----------【checkPhoneCode】---------------");

		Axis2WebServiceWS axis2WebServiceWS = (Axis2WebServiceWS) SpringFactoryHepler.getInstance("axis2WebServiceWS");

		return axis2WebServiceWS.checkValidateCode(loginName);

	}
	/**
	 * 提交8元自助体验金到对应游戏平台
	 * @return
	 */
	public String commitPT8Self(String loginName,String ioBB,String ip,String platform) {

		log.info("----------【commitPT8Self】---------------");

        Users users = (Users) userService.get(Users.class, loginName);

		if(!StringUtils.equals(getPostCode(ioBB), users.getPostcode())){

			return "你的账号在其他设备登录过，请重新登录后再领取自助体验金！";
		}

		UserWebServiceWS userWebserviceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String consString = userWebserviceWS.checkGameIsProtect("PT8元自助优惠");

		if(!StringUtils.equals(consString,"1")){

			return "体验金维护中！";
		}

		String result = userWebserviceWS.commitPT8Self(loginName, ioBB, ip, platform);
		return result;
	}
	/**
	 * app下载彩金
	 * @return
	 */
	public String getAppPreferentialM(String loginName,String ioBB,String ip,String platform) {

		log.info("----------【getAppPreferentialM】---------------");

		Users users = (Users) userService.get(Users.class, loginName);

		if(!StringUtils.equals(getPostCode(ioBB), users.getPostcode())){

			return "你的账号在其他设备登录过，请重新登录后再领取自助体验金！";
		}

		UserWebServiceWS userWebserviceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");
		String result = userWebserviceWS.getAppPreferentialM(loginName, platform);
		return result;
	}

	/**************************************** 自助优惠提供服务结束处   ****************************************/

	// 查询自助晋级记录信息
	public List<SelfPromotionVO> querySelfPromotionList(SelfPromotionVO vo) {

		return selfPromotionService.querySelfPromotionList(vo);
	}

	// 自助晋级检测升级
	public String checkUpgrade(SelfPromotionVO vo) {

		UserWebServiceWS userWebserviceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return  userWebserviceWS.checkUpgrade(vo.getLoginName(),"month");

	}

	// 存送优惠卷提交
	private Object submitDepositYHJObj = new Object();
	public String submitDepositYHJ(DepositPreferentialReelVO vo) {

		String loginName = vo.getLoginName();
		String code = vo.getCode();
		String type = vo.getType();
		Double remit = vo.getRemit();
		String ip = vo.getIp();

		log.info("submitDepositYHJ方法的参数为：【loginName=" + loginName + ",code=" + code + ",type=" + type + ",remit=" + remit + ",ip=" + ip + "】");

		if (StringUtils.isBlank(type)) {

			return "请选择转账平台账户！";
		}

		if (remit < 10) {

			return "转账金额不能小于10，请重新输入转账金额！";
		}

		if (StringUtils.isBlank(code)) {

			return "优惠代码不能为空！";
		}

		if ("pt8".equals(type) && StringUtils.isEmpty(code)) {

			return "pt8元红包优惠劵不正确";
		}

		UserWebServiceWS userWebserviceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		synchronized (submitDepositYHJObj) {

			//return userWebserviceWS.transferInforCoupon(loginName, type, remit, code, ip);
			return null;
		}

	}

	// 存送红包优惠卷提交
	private Object submitDepositHBYHJObj = new Object();
	public String submitDepositHBYHJ(DepositPreferentialReelVO vo) {

		String loginName = vo.getLoginName();
		String code = vo.getCode();
		String type = vo.getType();
		String ip = vo.getIp();

		log.info("submitDepositYHJ方法的参数为：【loginName=" + loginName + ",code=" + code + ",type=" + type + ",ip=" + ip + "】");

		if (StringUtils.isBlank(type)) {

			return "请选择转账平台账户！";
		}

		if (StringUtils.isBlank(code)) {

			return "优惠代码不能为空！";
		}

		if ("pt8".equals(type) && StringUtils.isEmpty(code)) {

			return "pt8元红包优惠劵不正确";
		}

		UserWebServiceWS userWebserviceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		synchronized (submitDepositHBYHJObj) {

			return userWebserviceWS.transferInforRedCoupon(loginName, type, code, ip);
		}

	}

	// 查询用户记录信息
	public Userstatus queryUserStatus(UserStatusVO vo) {

		return userStatusService.queryUserStatus(vo.getLoginName());
	}

	// 在线客服-电话回拨
	public String mobileMakeCall(String loginName,String phoneNumber) {

		Users user = (Users) userService.get(Users.class, loginName);

		if (user == null) {
			return "尚未登录！请重新登录！";
		}


		Axis2WebServiceWS axis2WebServiceWS = (Axis2WebServiceWS) SpringFactoryHepler.getInstance("axis2WebServiceWS");
		String result = null;

		try {

			if(StringUtils.isEmpty(phoneNumber)){

				phoneNumber = AESUtil.aesDecrypt(user.getPhone(),AESUtil.KEY);
			}

			result = axis2WebServiceWS.makeCall(loginName, phoneNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	// 每日任务（签到彩金、存款彩金、流水彩金）- 获取基本信息,奖金余额 ，任务操作type，转账游戏平台列表
	public String getEveryDayTaskInfo(String loginName) {

		log.info("------getEveryDayTaskInfo------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

	    Double prizeMoney =  userWebServiceWS.querySignAmount(loginName);

		if(prizeMoney == null){

			return "0.00";
		}
		return prizeMoney.toString();
	}


	//  我的  -- 签到
	public SignRecordInfoVO checkIn(String loginName) throws Exception{

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String result =  userWebServiceWS.doSignRecord(loginName,null,cs.getUsers(loginName).getLevel().toString());

		SignRecordInfoVO signRecordInfoVO = new SignRecordInfoVO();

		signRecordInfoVO.setMsg(result);

		if(result != null && result.contains("签到成功")){

			List<SignRecord> list =userWebServiceWS.findSignrecord(loginName);

			Double signAmount = userWebServiceWS.querySignAmount(loginName);

			signRecordInfoVO.setTimeStr(DateUtil.getDateFormatYYYY_MM_DD(list.get(list.size()-1).getCreatetime())+"");

			signRecordInfoVO.setSignAmount(signAmount);

		}

		return signRecordInfoVO;

	}

	//  App签到日历表界面 获取 以签到日期 和 签到余额
	public SignRecordInfoVO getSignInInfo(String loginName){

		log.info("---------------------getSignInInfo------------------------"+loginName);
		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler
				.getInstance("userWebServiceWS");

		List<SignRecord> list = userWebServiceWS.findSignrecord(loginName);

		SignRecordInfoVO signRecordInfoVO = new SignRecordInfoVO();

		Double signAmount = userWebServiceWS.querySignAmount(loginName);

		for (SignRecord signRecord : list) {

			signRecordInfoVO.setTimeStr(signRecordInfoVO.getTimeStr()+DateUtil.getDateFormatYYYY_MM_DD(signRecord.getCreatetime())+",");

		}

		signRecordInfoVO.setSignAmount(signAmount);

		return signRecordInfoVO;
	}
	// 天天好礼-签到兑换彩金--获得签到奖金账户余额
	public String getCheckInBalance(String loginName) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler
				.getInstance("userWebServiceWS");

		return  userWebServiceWS.querySignAmount(loginName).toString();

	}
	// 每日任务（签到彩金、存款彩金、流水彩金）- 计算彩金
	public DoDailyVO doDaily(String loginName,String type) {

		log.info("------doDaily------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String result = "服务器繁忙，请稍后再试";

		DoDailyVO returnVo = new DoDailyVO();

		try {

			if("checkIn".equals(type)){

				result =  userWebServiceWS.doSignRecord(loginName,null,cs.getUsers(loginName).getLevel().toString());

			}
			if("deposit".equals(type)){

				result = userWebServiceWS.doRechargeRecord(loginName);

			}
			if("bet".equals(type)){

				result = userWebServiceWS.doStreamRecord(loginName);

			}

		} catch (Exception e) {

			log.info(e.getMessage());
		}


		Double prizeMoney =  userWebServiceWS.querySignAmount(loginName);

		returnVo.setMsg(result);
		returnVo.setPrizeMoney(prizeMoney == null ? "0.00" : prizeMoney.toString());

		return returnVo;
	}




	// 每日任务（签到彩金、存款彩金、流水彩金）- 提交彩金到对应平台
	public String transferForPrizeMoney(String loginName,String remit,String platForm) {

	    Users user = (Users) userService.get(Users.class, loginName);

	    if(user == null){

	    	return "用户不存在";
	    }

	    if("AGENT".equals(user.getRole())){

	    	return "代理不允许转账";
	    }

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");


		return userWebServiceWS.transferSign(loginName, Double.parseDouble(remit), platForm);
	}

	// 每日任-务存款红包- 初始化信息
	public TransferHBBDataVO initHBInfo(String loginName){
		TransferHBBDataVO vo = new TransferHBBDataVO();

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String remit = userWebServiceWS.queryHBbonus(loginName, 0);
		if(StringUtils.isNotBlank(remit)){

			vo.setHbMoney(remit);
		}
		Users user = cs.getUsers(loginName);
		List<HBConfig> hbList = userWebServiceWS.queryHBSelect(user.getLevel(), 0);
		List<TransferHBVO> hbvos = new ArrayList<TransferHBVO>();
		if(hbList!=null&&hbList.size()>0){

			for(HBConfig hbConfig:hbList){
				TransferHBVO hbvo = new TransferHBVO();
				hbvo.setId(hbConfig.getId());
				hbvo.setTitle(hbConfig.getTitle());
				hbvos.add(hbvo);
			}
		}

		vo.setHbSelects(hbvos);
		return vo;
	}
	// 每日任务 领取红包
	public TransferHBBDataVO doHB(String loginName,Integer hbSelect){
		TransferHBBDataVO vo = new TransferHBBDataVO();
		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");
		String result;
		try {
			result = userWebServiceWS.doHB(loginName, 0, hbSelect);
			vo.setMsg(result);
		} catch (Exception e) {

			e.printStackTrace();
			vo.setMsg("服务器繁忙，请稍后再试");
			return vo;
		}
		String remit = userWebServiceWS.queryHBbonus(loginName, 0);
		if(StringUtils.isNotBlank(remit)){

			vo.setHbMoney(remit);
		}
		return vo;
	}
	// 每日任务存款红包- 提交红包余额到对应账户
	public String submitHBRemit(String loginName,String hbType , Double hbRemit){

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String result = null;

		if(hbRemit<10){

			return"转入金额不能小于10元！";
		}

		result = userWebServiceWS.transferInforHB(loginName, hbRemit, hbType, 0);

		return result;
	}


	// 全民自助-积分中心--获取可用积分 - 历史积分总和 - 兑换比例
	public IntegralCenterVO getIntegralAndRatio(String loginName) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		TotalPoint point =  userWebServiceWS.queryPoints(loginName);
		IntegralCenterVO vo = new IntegralCenterVO();
		if(point==null){
			vo.setIntegral("0");
		}else {
			DecimalFormat df = new DecimalFormat("#.00");
			vo.setIntegral(df.format(point.getTotalpoints())+"");
		}

		vo.setRatio("2000");
//		Users user = getUser(loginName);
//
//		String ratio = "500";//默认新会员
//		if(user.getLevel()==1){
//			ratio="400";
//		}else if(user.getLevel()==2){
//			ratio="335";
//		}else if(user.getLevel()==3){
//			ratio="285";
//		}else if(user.getLevel()==4){
//			ratio="250";
//		}else if(user.getLevel()==5){
//			ratio="220";
//		}else if(user.getLevel()==6){
//			ratio="200";
//		}
//
//		IntegralCenterVO vo = new IntegralCenterVO();
//
//		if(totalPoint != null){
//
//			vo.setHistoryIntegral(totalPoint.getTotalpoints() + "");
//			vo.setIntegral(totalPoint.getTotalpoints() + "");
//			vo.setRatio(ratio);
//
//		}else{
//
//			vo.setHistoryIntegral("0");
//			vo.setIntegral("0");
//			vo.setRatio(ratio);
//		}
//
		return vo;
	}

	// 全民自助-积分中心--提交积分兑换
	private Object commitIntegralExchangeObj = new Object();
	public String commitIntegralExchange(String loginName,String remit) {

	    UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

	    synchronized (commitIntegralExchangeObj) {

	    	return userWebServiceWS.transferPoint(loginName, Double.parseDouble(remit));

	    }

	}

	// 个人中心-天天好礼-老虎机大爆炸-列表记录信息
	public List<SlotMachineBigBangVO> querySlotMachineBigBangList(SlotMachineBigBangVO vo) {
		return slotMachineBigBangService.querySlotMachineBigBangList(vo);

		/*UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		List<PTBigBang> list = userWebServiceWS.ptBigBangBonuses(vo.getLoginName());

		List<SlotMachineBigBangVO> returnList = new ArrayList<SlotMachineBigBangVO>();;

		if(list != null && !list.isEmpty()){

			for(PTBigBang obj : list){

				SlotMachineBigBangVO slotMachineBigBangVO = new SlotMachineBigBangVO();

				slotMachineBigBangVO.setBonus(obj.getBonus() == null ? 0.00 : obj.getBonus() );
				slotMachineBigBangVO.setDistributeTime(obj.getTempDistributeTime());
				slotMachineBigBangVO.setGiftMoney(obj.getGiftMoney() == null ? 0.00 : obj.getGiftMoney());
				slotMachineBigBangVO.setId(obj.getId());
				slotMachineBigBangVO.setStatus(obj.getStatus() + "");
				slotMachineBigBangVO.setPlatform(obj.getPlatform());

				returnList.add(slotMachineBigBangVO);
			}

		}

		return returnList;*/
	}

	// 个人中心-天天好礼-老虎机大爆炸-领取
	public String receiveSlotMachineBigBang(SlotMachineBigBangVO vo) {

		String platform = vo.getPlatform();
		Integer id = vo.getId();
		String ip = vo.getIp();

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.getPTBigBangBonus(id, ip);
	}

	// 个人中心-天天好礼-好友礼金-查询好友推荐奖金账户余额
	public FriendBonusVO queryFriendBonus(String loginName) {

		FriendBonusVO vo = new FriendBonusVO();

		Users user = (Users) userService.get(Users.class, loginName);

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String money = userWebServiceWS.queryFriendbonus(loginName);

		vo.setId(user.getId());
		vo.setMoney(Double.parseDouble(money));

		return vo;
	}

	// 个人中心-天天好礼-好友礼金-提交
	public String transferFriendBonus(FriendBonusVO vo) {

		String loginName = vo.getLoginName();
		String platform = vo.getPlatform();
		Double remit = vo.getRemit();

		if (remit < 5) {

			return "转账金额不能小于5元！";
		}

		if (remit % 1 != 0) {

			return "转账金额只能是整数！";
		}

		Users user = (Users) userService.get(Users.class, loginName);

		if (UserRole.AGENT.getCode().equals(user.getRole())) {

			return "[提示]代理不能转账！";
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String msg = "";

		try {

			msg = userWebServiceWS.transferFriend(loginName, remit, platform);
		} catch (Exception e) {

			log.error("transferFriendBonus方法调用userWebServiceWS.transferFriend方法时发生异常，异常信息：" + e.getCause().getMessage());
			msg = "转账发生异常，请稍后再试！";
		}

		return msg;
	}

	// 个人中心-天天好礼-闯龙门-查询闯龙门奖金余额
	public EmigratedBonusVO queryEmigratedBonus(String loginName) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String money = "";//userWebServiceWS.queryEmigratedbonus(loginName);

		EmigratedBonusVO vo = new EmigratedBonusVO();
		vo.setMoney(Double.parseDouble(money));

		return vo;
	}

	// 个人中心-天天好礼-闯龙门-提交
	public String submitEmigratedBonus(EmigratedBonusVO vo) {

		String loginName = vo.getLoginName();
		Double remit = vo.getRemit();
		String platform = vo.getPlatform();

		if (remit < 10) {

			return "存款金额不能小于10元！";
		}

		Users user = (Users) userService.get(Users.class, loginName);

		if (UserRole.AGENT.getCode().equals(user.getRole())) {

			return "[提示]代理不能转账！";
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return null;//userWebServiceWS.transferInforEmigrated(loginName, remit, platform);
	}

	// 个人中心-天天好礼-闯龙门-今日报名
	public String applyEmigratedBonus(EmigratedBonusVO vo) {

		String loginName = vo.getLoginName();
		String grade = vo.getGrade();

		if (StringUtils.isBlank(grade)) {

			return "请选择报名等级！";
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String msg = "";

		try {

			msg = "";//userWebServiceWS.doEmigratedApply(loginName, grade);
		}catch(Exception e) {

			log.error("applyEmigratedBonus方法调用userWebServiceWS.doEmigratedApply方法时发生异常，异常信息：" + e.getCause().getMessage());
			msg = "报名活动出现异常，请稍后再试！";
		}

		return msg;
	}

	// 个人中心-天天好礼-闯龙门-领取昨日奖励
	public String receiveEmigratedBonus(String loginName) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = df.format(new Date()) + " 10:00:00";

		try {

			Date startDate = DateUtil.getDateFromDateStr(startTime);

			if ((new Date()).getTime() < startDate.getTime()) {

				return "请于10点以后再来领取奖励！";
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String msg = "";

		try {

			msg = "";//userWebServiceWS.doEmigrated(loginName);
		} catch (Exception e) {

			log.error("receiveEmigratedBonus方法调用userWebServiceWS.doEmigrated方法时发生异常，异常信息：" + e.getCause().getMessage());
			msg = "领取昨日奖励出现异常，请稍后再试！";
		}

		return msg;
	}

	public Page queryIndexTopList(OnlinePayRecordVO vo) {

		return systemAnnouncementService.queryIndexTopList(vo);
	}

	public String updateUserAliasName(UsersVO vo) {

		String msg = "SUCCESS";

		try {

			Users users = (Users) userService.get(Users.class, vo.getLoginname());
			users.setAliasName(vo.getAliasName());

			userService.update(users);
		} catch (Exception e) {

			log.error("updateUserAliasName方法执行更新操作发生异常，异常信息：" + e.getCause().getMessage());
			msg = "FAILED";
		}

		return msg;
	}

	//--------------------------找回密码开始---------------------------------------------

	public GetCodeByForgetPasswordRequestDataVO getSmsCode(GetCodeByForgetPasswordRequestDataVO vo) {

		log.info("---------------getSmsCode--------------");

		try {

			Users user = getUser(vo.getLoginName());

			if(user == null){

				vo.setMessage("账号不存在");
				return vo;
			}


			if(user.getFlag() == 1){

				vo.setMessage("该账号已冻结!不能使用找回密码！");
				return vo;
			}

			if(StringUtils.isNotEmpty(vo.getPhone())){

				String phoneFormDb = AESUtil.aesDecrypt(user.getPhone(), AESUtil.KEY);

				if(!StringUtils.equals(vo.getPhone(), phoneFormDb)){

					vo.setMessage("电话号码与注册的号码不匹配！");
					return vo;

				}

			}
			if(StringUtils.isNotEmpty(vo.getEmail())){

				String emailFromDb = AESUtil.aesDecrypt(user.getEmail(), AESUtil.KEY);

				if(!StringUtils.equals(vo.getEmail(), emailFromDb)){

					vo.setMessage("邮箱与注册的邮箱不匹配！");
					return vo;

				}

			}

		} catch (Exception e) {

			log.error("find passaword step1 error : " + e.getMessage());

			vo.setMessage("服务器繁忙，请稍后再试");
		}

		return vo;
	}
	public GetCodeByForgetPasswordRequestDataVO getEmailCode(GetCodeByForgetPasswordRequestDataVO vo) {

		log.info("---------------getEmailCode--------------");

		vo = getSmsCode(vo);

		if(StringUtils.isNotEmpty(vo.getMessage())){

			return vo;
		}

		String validateCode = getCharAndNumr(4);

		String message = SendEmailWs.sendEmailsByApi(vo.getEmail(), validateCode);

		vo.setMessage(message);
		vo.setValidateCode(validateCode);

		return vo;
	}
	public GetCodeByForgetPasswordRequestDataVO sendPasswordToPhone(GetCodeByForgetPasswordRequestDataVO vo) {

		log.info("---------------sendPasswordToPhone--------------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String newpwd =getCharAndNumr(8);

		String message = userWebServiceWS.getPwdByDx(vo.getLoginName(), vo.getPhone(), newpwd);

		if("2".equals(message)){
			vo.setMessage("该账号已冻结!不能使用找回密码！");
			return vo;
		}
		if(!"0".equals(message)){
			vo.setMessage("用户信息错误，请核对后再次尝试！");
			return vo;
		}

		vo.setNewPwd(newpwd);

		return vo;
	}
	public GetCodeByForgetPasswordRequestDataVO sendPasswordToEmail(GetCodeByForgetPasswordRequestDataVO vo) {

		log.info("---------------sendPasswordToEmail--------------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String message = userWebServiceWS.getPwdByYx(vo.getLoginName(), vo.getEmail());

		if(StringUtils.isNotEmpty(message)){

			vo.setMessage(message);
			return vo;
		}

		return vo;
	}

	public FindPwdByDxVO findPasswordByEmail(FindPwdByDxVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String message = userWebServiceWS.getPwdByYx(vo.getLoginName(), vo.getEmail());

		vo.setCode("10000");
		vo.setMessage(message);
		return vo;
	}

	public FindPwdByDxVO findPasswordByPhone(FindPwdByDxVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String pwd =getCharAndNumr(8);

		String message = userWebServiceWS.getPwdByDx(vo.getLoginName(), vo.getPhone(), pwd);

		if("2".equals(message)){
			vo.setMessage("该账号已冻结!不能使用找回密码！");
			return vo;
		}
		if(!"0".equals(message)){
			vo.setMessage("用户信息错误，请核对后再次尝试！");
			return vo;
		}

		vo.setCode("10000");
		vo.setNewpwd(pwd);
		return vo;
	}


	/**
	 * 获取随机数字
	 * @param length
	 * @return
	 */
	public static String getCharAndNumr(int length) {
		  String val = "";
		  Random random = new Random();
		  for (int i = 0; i < length; i++) {
			  val += String.valueOf(random.nextInt(10));
		  }
		  return val;
	}


	//--------------------------找回密码结束---------------------------------------------

	//解绑银行卡
	public String unBindBankinfo(String loginname, String bankno) {
		return axis2WebServiceWS.unBindBankinfo(loginname, bankno);
	}
	// 代理中心->申请提款->绑定银行卡
	public String bindBank(UserBankInfoVO vo) {

		String bankName = vo.getBankName();
		String bankNo = vo.getBankNo();
		String bankAddress = vo.getBankAddress();
		String loginName = vo.getLoginName();
		String password = vo.getPassword();

		log.info("bindBank方法的参数为【bankName=" + bankName + ",bankNo=" + bankNo + ",bankAddress=" + bankAddress + ",loginName=" + loginName + ",password=" + password + "】");

		if (StringUtil.isBlank(bankName)) {

			return "[提示]银行名称不能为空！";
		}

		if (StringUtil.isBlank(bankNo)) {

			return "[提示]卡/折号不能为空！";
		}

		if (bankNo.trim().length() > 30 || bankNo.trim().length() < 10) {

			return "[提示]卡/折号长度只能在10-30位之间，请重新输入！";
		}

		if (StringUtil.isBlank(bankAddress)) {

			return "[提示]开户网点不能为空！";
		}

		if (StringUtil.isBlank(password)) {

			return "[提示]登录密码不可为空！";
		}

		Users users = (Users) userService.get(Users.class, loginName);

		if (null == users) {

			return "未找到玩家“" + loginName + "”的账户信息！";
		}

		if (!EncryptionUtil.encryptPassword(password).equalsIgnoreCase(users.getPassword())) {

			return "[提示]登录密码错误，请重新输入！";
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		List<Userbankinfo> userbankinfoList = userWebServiceWS.getUserbankinfoList(loginName);

		if (null != userbankinfoList && !userbankinfoList.isEmpty()) {

			if (userbankinfoList.size() >= 3) {

				return "您已经绑定了三个银行卡/折号，且最多只可以绑定三个\\n如须解绑，请与在线客服联系！";
			}

			for (Userbankinfo userbankinfo : userbankinfoList) {

				if (userbankinfo.getBankname().equals(bankName.trim())) {

					return "该银行已经绑定过卡/折号，请不要重复绑定\\n如须解绑，请与在线客服联系！";
				}
			}
		}

		return userWebServiceWS.banding(loginName, bankNo, bankName, bankAddress);
	}

	// 代理中心->申请提款->绑定密保
	public String bindQuestion(UserBankInfoVO vo) {

		String loginName = vo.getLoginName();
		String password = vo.getPassword();
		Integer questionId = vo.getQuestionId();
		String answer = vo.getAnswer();

		log.info("bindQuestion方法的参数为【loginName=" + loginName + ",password=" + password + ",questionId=" + questionId + ",answer=" + answer + "】");

		Users users = (Users) userService.get(Users.class, loginName);

		if (null == users) {

			return "未找到玩家“" + loginName + "”的账户信息！";
		}

		if (StringUtils.isBlank(password)) {

			return "[提示]密码不可为空！";
		}

		if (StringUtils.isBlank(answer)) {

			return "[提示]请填写你的回答！";
		}

		if (null == questionId) {

			return "[提示]请选择密保问题！";
		}

		if (!DataDictionary.Question.existKey(String.valueOf(questionId))) {

			return "您所绑定的问题不存在！";
		}

		if (!EncryptionUtil.encryptPassword(password).equalsIgnoreCase(users.getPassword())) {

			return "登录密码错误，请重新输入！";
		}

		Axis2WebServiceWS axis2WebServiceWS = (Axis2WebServiceWS) SpringFactoryHepler.getInstance("axis2WebServiceWS");

		return axis2WebServiceWS.saveQuetion(loginName, questionId, answer);
	}

	private Object addCashoutMO = new Object();

	// 代理中心->申请提款->确认提款
	public String submitAgentWithdrawal(WithdrawalVO vo) {

		String loginName = vo.getLoginName();
		String password = vo.getPassword();
		String bankName = vo.getBankName();
		String bankNo = vo.getBankNo();
		String bankAddress = vo.getBankAddress();
		Double money = vo.getMoney();
		Integer questionId = vo.getQuestionId();
		String answer = vo.getAnswer();
		String type = vo.getType();
		String ip = vo.getIp();

		log.info("submitAgentWithdrawal方法的参数为【loginName=" + loginName + ",password=" + password + ",bankName=" + bankName + ",bankNo=" + bankNo + ",bankAddress=" + bankAddress + ",money=" + money + ",questionId=" + questionId + ",answer=" + answer + ",type=" + type + ",ip=" + ip + "】");

		synchronized (addCashoutMO) {

			Users users = (Users) userService.get(Users.class, loginName);

			if (null == users) {

				return "未找到代理“" + loginName + "”的账户信息！";
			}

			if (StringUtil.isBlank(password)) {

				return "[提示]密码不可为空！";
			}

			if (StringUtil.isBlank(bankName)) {

				return "[提示]请选择卡折种类！";
			}

			if (StringUtil.isBlank(bankNo)) {

				return "[提示]卡折号不可为空！";
			}

			if (StringUtil.isBlank(bankAddress)) {

				return "[提示]开户网点不可为空！";
			}

			if (StringUtil.isBlank(String.valueOf(money))) {

				return "[提示]提款金额不可为空！";
			}

			if (!NumberUtils.isNumber(String.valueOf(money))) {

				return "[提示]提款金额必須為數字！";
			}

			if (money < 1) {

				return "[提示]单次提款金额最低1元！";
			}


			if(StringUtils.isEmpty(users.getQq()) || StringUtils.isEmpty(users.getMicrochannel())){

				return "您好，请先点击账户设置完善您的信息【微信、QQ】";
			}

			if(!StringUtils.equals(getPostCode(vo.getSid()), users.getPostcode())){

				return "你的账号在其他设备登录过，为了你的资金安全，请重新登录！";
			}

			UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

			Userbankinfo userbankinfo = userWebServiceWS.getUserbankinfo(loginName, bankName);

			if (null == userbankinfo) {

				return "[提示]您输入的卡/折号有误，请重新输入！";
			}

			if (-1000 == userbankinfo.getFlag()) {

				return "系统繁忙，请重新输入！";
			}

			if ("MONEY_CUSTOMER".equals(users.getRole())) {

				if (StringUtils.isBlank(answer)) {

					return "[提示]您的回答不可为空！";
				}

				Axis2WebServiceWS axis2WebServiceWS = (Axis2WebServiceWS) SpringFactoryHepler.getInstance("axis2WebServiceWS");

				String validatemsg = axis2WebServiceWS.questionValidate(loginName, questionId, answer);

				if (!validatemsg.equals("SUCCESS")) {

					if (validatemsg.equals("bindingPlease")) {

						return "请设定密保问题！";
					}

					QuestionStatus status = axis2WebServiceWS.queryQuesStatus(loginName);

					if (status.getErrortimes() >= 5) {

						return "密保答案输入5次错误！";
					}

					return validatemsg;
				}
			}

			return userWebServiceWS.addCashout(loginName, loginName, password, users.getRole(), Constants.FROM_FRONT, money, users.getAccountName(), userbankinfo.getBankno(),
					                           Constants.DEFAULT_ACCOUNTTYPE, StringUtils.trim(bankName), StringUtils.trim(bankAddress), StringUtils.trim(bankAddress), null,
					                           users.getPhone(), ip, null, "", type);
		}
	}

	// 查询额度记录信息
	public Page queryCreditLogList(AgentAccountListVO vo) {

		return agentAccountListService.queryCreditLogList(vo);
	}


	// 代理-账户设置
	public AgentSettingVO updateAgentInfo(AgentSettingVO vo) {

		return userService.updateAgentInfo(vo);
	}

	// 代理-佣金列表
	public Page queryCommissionrecords(CommissionListRequestVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String[] year_morth = vo.getYear_month().split("-");

		Page page = userWebServiceWS.queryCommissionrecords(vo.getLoginName(),Integer.parseInt(year_morth[0]),
															Integer.parseInt(year_morth[1]), vo.getPageIndex(), vo.getPageSize());

		List<CommissionListVO> list = new ArrayList<CommissionListVO>();

		Commissionrecords c = null;
		CommissionListVO commissionListVO = null;
		if(page.getPageContents() != null){

			for(int i =0,len = page.getPageContents().size();i<len;i++){

				c = (Commissionrecords)page.getPageContents().get(i);
				commissionListVO = new CommissionListVO();

				commissionListVO.setYearmonth(c.getId().getYear()+"-"+c.getId().getMonth());
				commissionListVO.setLoginname(c.getId().getLoginname());
				commissionListVO.setXimaAmount(c.getXimaAmount() + "");
				commissionListVO.setFirstDepositAmount(c.getFirstDepositAmount() + "");
				commissionListVO.setAgAmount(c.getAgAmount() + "");
				commissionListVO.setOtherAmount(c.getOtherAmount() + "");
				commissionListVO.setRemark(c.getRemark());
				list.add(commissionListVO);

			}
		}

		page.setPageContents(list);

		return page;
	}

	// 代理中心-账户清单-下线提案查询
	public Page queryUnderLineProposalList(AgentAccountListVO vo) {

		Page page = agentAccountListService.queryUnderLineProposalList(vo);

		if (null != page) {

			if (null == page.getStatics1()) {

				page.setStatics1(0.00);
			}

			if (null == page.getStatics2()) {

				page.setStatics2(0.00);
			}

			if (!page.getPageContents().isEmpty()) {

				Payorder p1 = null;
				Proposal p2 = null;
				UnderLineProposalVO obj = null;
				List<UnderLineProposalVO> list = new ArrayList<UnderLineProposalVO>();

				for (int i = 0, len = page.getPageContents().size(); i < len; i++) {

					String loginName = "";
					String tempCreateTime = "";
					Double money = 0.00;
					Double giftAmount = 0.00;
					String typeName = "";

					if (null != vo.getProposalType() && vo.getProposalType() == 1000) {

						p1 = (Payorder) page.getPageContents().get(i);

						loginName = p1.getLoginname();
						tempCreateTime = p1.getTempCreateTime();
						money = p1.getMoney();
						typeName = ProposalType.getText(vo.getProposalType());
					} else {

						p2 = (Proposal) page.getPageContents().get(i);

						loginName = p2.getLoginname();
						tempCreateTime = p2.getTempCreateTime();
						money = p2.getAmount();
						giftAmount = p2.getGifTamount() == null ? 0.00 : p2.getGifTamount();
						typeName = ProposalType.getText(p2.getType());
					}

					obj = new UnderLineProposalVO(loginName, tempCreateTime, money, giftAmount, typeName);

					list.add(obj);
				}

				page.setPageContents(list);
			}
		}

		return page;
	}

	// 代理中心-账户清单-平台输赢查询
	public Page queryPlatformLoseWinList(AgentAccountListVO vo) {

		return agentAccountListService.queryPlatformLoseWinList(vo);
	}

	// 代理中心-账户清单-实时输赢查询
	public Page queryRealTimeLoseWinList(AgentAccountListVO vo) {

		return agentAccountListService.queryRealTimeLoseWinList(vo);
	}

	// 代理中心-账户清单-下线会员查询
	public Page queryUnderLineMemberList(AgentAccountListVO vo) {

		return agentAccountListService.queryUnderLineMemberList(vo);
	}

	// 代理注册
	public String registerAgent(UsersVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		// 代理注册时所使用的域名
		String howToKnow = vo.getHowToKnow();
		// 加盟账户
		String loginname = vo.getLoginname();
		// 登陆密码
		String password = vo.getPassword();
		// 真实姓名
		String accountName = vo.getAccountName();
		// 联系电话
		String phone = "";
		// 电子邮箱
		String email = "";

		try {

			phone = AESUtil.aesEncrypt(vo.getPhone(), AESUtil.KEY);
			email = AESUtil.aesEncrypt(vo.getEmail(), AESUtil.KEY);
		} catch (Exception e) {

			log.error("registerAgent方法调用AESUtil.aesEncrypt方法对手机号码和邮箱进行加密时出现异常，异常信息：" + e.getCause().getMessage());
		}

		// QQ
		String qq = vo.getQq();
		// 加盟网址
		String referWebsite = vo.getReferWebsite();
		// 注册IP
		String ipaddress = vo.getRegisterIp();
		// 微信号
		String microchannel = vo.getMicrochannel();
		// 加盟推荐码
		String partner = vo.getPartner();
		// 注册IP对应的城市
		String city = "";

		ServletContext sc = (ServletContext) MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
		IPSeeker seeker = (IPSeeker) sc.getAttribute("ipSeeker");

		if (StringUtils.isNotBlank(ipaddress)) {

			String temp = seeker.getAddress(ipaddress);

			if (StringUtils.isNotBlank(temp) && !"CZ88.NET".equals(temp)) {

				city = temp;
			}
		}

		String msg = userWebServiceWS.addAgent(howToKnow, loginname, password, accountName, phone, email, qq, referWebsite, ipaddress, city, microchannel, partner);

		if (StringUtils.isBlank(msg)) {

			Users user = (Users) userService.get(Users.class, loginname);

			try {

				String html = EmailTemplateHelp.toHTML(Constants.EMAIL_REGISTER_BODY_HTML, new Object[] { loginname, DateUtil.getDateFormat(user.getCreatetime()) });
				mailSender.sendmail(html, user.getEmail(), "恭喜您成为龙都国际娱乐城代理");
			} catch (Exception e) {

				log.error("registerAgent方法调用mailSender.sendmail方法发送邮件出现异常，异常信息：" + e.getCause().getMessage());
				msg = e.getCause().getMessage();
			}
		}

		return msg;
	}

	// 代理每月及每日数据统计
	public AgentCenterVO getAgentAmount(String loginName) {

		AgentCenterVO vo = new AgentCenterVO();

		Users user = (Users) userService.get(Users.class, loginName);

		if (null == user || "MONEY_CUSTOMER".equals(user.getRole())) {

			return null;
		}

		vo.setCredit(setValueFormat(String.valueOf(user.getCredit())));

		Userstatus userstatus = userStatusService.queryUserStatus(loginName);

		if (userstatus != null) {

			vo.setSlotaccount(setValueFormat(String.valueOf(userstatus.getSlotaccount())));
		} else {

			vo.setSlotaccount(setValueFormat("0.00"));
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");
		String result = userWebServiceWS.agentMonthlyReport(loginName);

		JSONObject json = JSONObject.fromObject(result);
		// 本月总输赢
		String profitall = json.getString("profitall");
		// 本月总返水
		String ximafee = json.getString("ximafee");
		// 本月总优惠
		String couponfee = json.getString("couponfee");
		// 会员总人数
		String reg = json.getString("reg");
		// 本月注册量
		String monthReg = json.getString("monthly_reg");
		// 本月投注额
		String betall = json.getString("betall");

		vo.setProfitall(setValueFormat(profitall));
		vo.setXimafee(setValueFormat(ximafee));
		vo.setCouponfee(setValueFormat(couponfee));
		vo.setReg(reg);
		vo.setMonthReg(monthReg);
		vo.setBetall(setValueFormat(betall));

		return vo;
	}

	// 代理中心-账户清单-日结佣金查询
	public Page queryDaySettlementCommissionList(AgentAccountListVO vo) {

		String loginName = vo.getLoginName();
		String startTime = vo.getStartTime();
		String endTime = vo.getEndTime();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();

		log.info("queryDaySettlementCommissionList方法的参数为【loginName=" + loginName + ",startTime=" + startTime + ",endTime=" + endTime + ",pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");

		if (null == pageIndex) {

			pageIndex = 1;
		}

		if (null == pageSize) {

			pageSize = 10;
		}

		if (StringUtils.isBlank(startTime)) {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.MILLISECOND, 0);

			startTime = DateUtil.getDateFormat(c.getTime());
		} else {

			startTime = startTime + " 00:00:00";
		}

		if (StringUtils.isBlank(endTime)) {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			endTime = DateUtil.getDateFormat(c.getTime());
		} else {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());

			String tempTime = DateUtil.getDateFormat(c.getTime());
			endTime = endTime + " " + tempTime.split(" ")[1];
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.searchPtCommissions(loginName, startTime, endTime, pageIndex, pageSize);
	}

	// 代理中心-账户清单-推广网址查询
	public List<String> queryAgentAddress(String loginName) {

		List<String> list = null;

		try {

			AgentService agservice = (AgentService) SpringFactoryHepler.getInstance("agentService");
			list = agservice.queryAgentAddress(loginName);
		} catch (Exception e) {

			log.error("queryAgentAddress方法查询代理推广网址发生异常，异常信息：" + e.getMessage());
		}

		return list;
	}

	private String setValueFormat(String value) {

		Double returnValue = 0.00;

		if (StringUtils.isNotBlank(value) && !"null".equals(value)) {

			returnValue = Double.parseDouble(value);
		}

		return new DecimalFormat("######0.00").format(returnValue);
	}

	public String getPostCode(String sid) {

		if (sid.length() < 10) {

			return sid;
		}

		StringBuffer sb = new StringBuffer(sid);
		sb = sb.reverse();

		return sb.substring(0, 8);
	}
	public static boolean isMoney(String money) {

		Pattern pattern = Pattern.compile("^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$");

		Matcher matcher = pattern.matcher(money);

		if(matcher.matches()){

			return true;
		}

		return false;

	}


	public GiftForAppVO getExistGift(String loginName){

		GiftForAppVO vo = null;

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		Gift gift = userWebServiceWS.getExistGiftForApp(loginName);

		if(gift != null){

			vo = new GiftForAppVO();

			vo.setId(gift.getId() + "");
			vo.setTitle(gift.getTitle());
			vo.setStartTime(DateUtil.getDateFormat(gift.getStartTime()));
			vo.setEndTime(DateUtil.getDateFormat(gift.getEndTime()));
//			vo.setImageUrl(gift.getImageUrl());
//			vo.setSummary(gift.getSummary());
		}

		return vo;

	}
	public String checkGiftReceive(String giftId , String loginName){

		return null;

/*		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String result = userWebServiceWS.checkGiftReceiveForApp(giftId,loginName);

		if(StringUtils.isEmpty(result)){

			result =  "success";
		}

		return result;*/

	}

	public String applyGift(GiftApplyVO vo){


		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String result = userWebServiceWS.applyGift(vo.getLoginname(),Integer.parseInt(vo.getGiftId()),vo.getAddress(),vo.getRecipient(),vo.getCellphone());

		if(result == null){

			result = "success";
		}

		return result;

	}

	/***
	 * 周周回馈
	 * @return
	 * @throws Exception
	 */
	public Page queryWeekSentReccords(PageQueryBaseVO vo) {

		log.info("----------【queryWeekSentReccords】---------------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		Page page = userWebServiceWS.weekSentReccords(vo.getLoginName(), vo.getPageIndex(), vo.getPageSize());

		if(page != null && page != null && page.getSize() > 0){

			List<WeekSent> list = page.getPageContents();

			List<WeekSentVO> listResponse = new ArrayList<WeekSentVO>();

			for(WeekSent obj : list){

				WeekSentVO weekSentVO = new WeekSentVO();
				weekSentVO.setPno(obj.getPno());
				weekSentVO.setPromo(obj.getPromo() + "");
				weekSentVO.setRemark(obj.getRemark());
				weekSentVO.setStatus(obj.getStatus());
				weekSentVO.setTempCreateTime(obj.getTempCreateTime());

				listResponse.add(weekSentVO);
			}

			page.setPageContents(listResponse);

		}

		return page;
	}
	/***
	 * 周周回馈-领取
	 * @return
	 * @throws Exception
	 */
	public String optWeekSent(WeedOptRequestDataVO vo) {

		log.info("----------【optWeekSent】---------------");

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.optWeekSent(vo.getPno(), Integer.parseInt(vo.getFlag()), vo.getIp(), vo.getPlatForm());

	}



	/***
	 * 账户设置  - 短信个性化定制
	 * @return
	 * @throws Exception
	 */
	public String smsCustomization(SmsCustomizationRequestDataVO vo) {

		log.info("----------【smsCustomization】---------------");

		String services = "";

		if("Y".equals(vo.getChangePassword())){

			services = services + "3,";

		}
		if("Y".equals(vo.getDepositExecut())){

			services = services + "9,";

		}
		if("Y".equals(vo.getSelfPromotion())){

			services = services + "2,";

		}
		if("Y".equals(vo.getWithdrawalAppliy())){

			services = services + "5,";

		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.chooseService(vo.getLoginName() , services ,vo.getIp());

	}

	/************************************************************代理中心接口开始处************************************************************/

	// 账户中心->个人中心
	public AgentAccountCenterVO personalCenter(String loginName) {

		Users user = (Users) userService.get(Users.class, loginName);

		Userstatus userStatus = userStatusService.queryUserStatus(loginName);

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		String level = userWebServiceWS.getAgentVip(loginName);

		String str = userWebServiceWS.agentMonthlyReport(loginName);

		AgentAccountCenterVO vo = new AgentAccountCenterVO();

		vo.setLevel(Integer.parseInt(level));
		vo.setSlotAccount(userStatus.getSlotaccount());
		vo.setCredit(user.getCredit());

		JSONObject jsonObject = JSONObject.fromObject(str);

		// 本月总输赢
		Object profitall = jsonObject.get("profitall");
		// 本月总返水
		Object ximafee = jsonObject.get("ximafee");
		// 本月总优惠
		Object couponfee = jsonObject.get("couponfee");
		// 会员总人数
		Object reg = jsonObject.get("reg");
		// 本月注册量
		Object monthly_reg = jsonObject.get("monthly_reg");
		// 本月投注额
		Object betall = jsonObject.get("betall");

		vo.setProfitAll(null == profitall ? 0.00 : "null".equalsIgnoreCase(profitall.toString()) ? 0.00 : Double.parseDouble(profitall.toString()));
		vo.setXimaFee(null == ximafee ? 0.00 : "null".equalsIgnoreCase(ximafee.toString()) ? 0.00 : Double.parseDouble(ximafee.toString()));
		vo.setCouponFee(null == couponfee ? 0.00 : "null".equalsIgnoreCase(couponfee.toString()) ? 0.00 : Double.parseDouble(couponfee.toString()));
		vo.setReg(null == reg ? 0 : "null".equalsIgnoreCase(reg.toString()) ? 0 : Integer.parseInt(reg.toString()));
		vo.setMonthlyReg(null == monthly_reg ? 0 : "null".equalsIgnoreCase(monthly_reg.toString()) ? 0 : Integer.parseInt(monthly_reg.toString()));
		vo.setBetAll(null == betall ? 0.00 : "null".equalsIgnoreCase(betall.toString()) ? 0.00 : Double.parseDouble(betall.toString()));

		return vo;
	}

	// 账户中心->个人中心
	public List<String> agentAddress(String loginName) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.queryAgentAddress(loginName);
	}

	// 账户中心->个人中心->申请提款
	public String agentWithdrawal(AgentAccountCenterVO vo) {

		String loginName = vo.getLoginName();
		String password = vo.getPassword();
		Double amount = vo.getAmount();
		String bankName = vo.getBankName();
		String bankAddress = vo.getBankAddress();
		String ip = vo.getIp();
		String type = vo.getType();

		Users users = (Users) userService.get(Users.class, loginName);

		if (null == users) {

			return "未找到相应的代理账户信息！";
		}

		if (1 == users.getFlag()) {

			return "账号已被冻结，不能提款！";
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		Userbankinfo userbankinfo = userWebServiceWS.getUserbankinfo(loginName, vo.getBankName());

		if (null == userbankinfo || -1000 == userbankinfo.getFlag()) {

			return "未找到银行名称为" + vo.getBankName() + "相应的银行信息！";
		}

		return userWebServiceWS.addCashout(loginName, loginName, password, users.getRole(), "前台", amount, users.getAccountName(),
				StringUtils.trim(userbankinfo.getBankno()), "借记卡", StringUtils.trim(bankName), StringUtils.trim(bankAddress),
				StringUtils.trim(bankAddress), null, users.getPhone(), ip, null, "0", type);
	}

	// 账户中心->个人中心->额度记录
	public Page commissionCreditLog(AgentAccountCenterVO vo) {

		Date startDate = null;
		Date endDate = null;

		try {

			startDate = DateUtil.getDateFromDateStr(vo.getStartTime());
			endDate = DateUtil.getDateFromDateStr(vo.getEndTime());
		} catch (ParseException e) {

			e.printStackTrace();
			return null;
		}

		DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
		dc.add(Restrictions.ge("createtime", startDate));
		dc.add(Restrictions.lt("createtime", endDate));
		dc.add(Restrictions.eq("loginname", vo.getLoginName()));

		Order o = Order.desc("createtime");

		return PageQuery.queryForPagenation(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
	}

	// 账户中心->个人中心->下线提案
	public Page underLineProposal(AgentAccountCenterVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.searchsubuserProposal(vo.getStartTime(), vo.getEndTime(), vo.getLoginName(),
			vo.getProposalType(), vo.getMemberName(),  vo.getPageIndex(), vo.getPageSize());
	}

	// 账户中心->个人中心->平台输赢
	public Page platformWinLose(AgentAccountCenterVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.searchagprofit(vo.getLoginName(), vo.getMemberName(), vo.getPlatformType(), vo.getStartTime(), vo.getEndTime(), vo.getPageIndex(), vo.getPageSize());
	}

	// 账户中心->下线会员中心->下线会员
	public Page offLineMemberList(AgentAccountCenterVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.queryInitSubUsers(vo.getLoginName(), vo.getStartTime(), vo.getEndTime(), vo.getPageIndex(), vo.getPageSize());
	}

	// 账户中心->个人中心->日结佣金
	public Page commissionReport(AgentAccountCenterVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.searchPtCommissions(vo.getLoginName(), vo.getStartTime(), vo.getEndTime(), vo.getPageIndex(), vo.getPageSize());
	}

	// 账户中心->个人中心->个人资料
	public String modifyAgentInfo(AgentAccountCenterVO vo) {

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		return userWebServiceWS.modifyCustomerInfo(0, vo.getLoginName(), vo.getAliasName(), null, null, vo.getQq(), vo.getIp(), null, vo.getMicroChannel(), vo.getBirthday(),vo.getAccountName());
	}

	/************************************************************代理中心接口结束处************************************************************/

	/************************************************************公用部分接口开始处************************************************************/

	public Integer bindQuestionNumber(String loginName) {

		return questionService.countQuestion(loginName);
	}

	public UsersVO getUserInfo(String loginnane) {

		log.info("---getUserInfo----" );

		UsersVO vo = userService.getUserInfo(loginnane, userBankInfoService);
		if(vo!=null){
			List<Qrcode> list = userWebServiceWS.queryQRcode(loginnane);//获取用户专属二维码
			if(list!=null && list.size()>0){
				vo.setQrCodeInfoStr(list.get(0).getAddress());
			}
			log.info("---getUserInfo----" + vo.toString());
		}

		return vo;
	}

	public UserbankVO bankBinding(UserbankVO vo) {

		vo = userBankInfoService.bankBinding(vo);

		return vo;
	}

	public String queryBankInfo(AgentAccountCenterVO vo) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("flag", "fail");

		String loginName = vo.getLoginName();
		String bankName = vo.getBankName();

		String bankStatus = bankStatusService.getBankStatus(bankName);

		if (StringUtils.isBlank(bankStatus) || !"OK".equalsIgnoreCase(bankStatus)) {

			resultMap.put("message", "银行系统维护中，请选择其他银行或稍后再试！");

			return JSONObject.fromObject(resultMap).toString();
		}

		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");

		Userbankinfo userbankinfo = userWebServiceWS.getUserbankinfo(loginName, bankName);

		if (null != userbankinfo && -1000 != userbankinfo.getFlag()) {

			int len = userbankinfo.getBankno().length() - 8;
			String str = "";

			for (int i = 0; i < len; i++) {

				str += "*";
			}

			String bankNo = str + userbankinfo.getBankno().substring(userbankinfo.getBankno().length() - 8);
			String bankAddress = userbankinfo.getBankaddress();

			resultMap.put("flag", "success");
			resultMap.put("bankNo", bankNo);
			resultMap.put("bankAddress", bankAddress);

			return JSONObject.fromObject(resultMap).toString();
		} else {

			resultMap.put("message", "未找到相应的银行卡/折号信息！");

			return JSONObject.fromObject(resultMap).toString();
		}
	}

	/**
	 * 持久化验证结果
	 * @param vo
	 * @return
	 */
	public String updateUserStatusSmsFlagForApp(String loginName,String code) {

		log.info("----------【updateUserStatusSmsFlagForApp】---------------");

		Axis2WebServiceWS axis2WebServiceWS = (Axis2WebServiceWS) SpringFactoryHepler.getInstance("axis2WebServiceWS");

		return axis2WebServiceWS.updateUserStatusSmsFlagForApp(loginName,code);

	}

	/************************************************************公用部分接口结束处************************************************************/
	/**
	 * 获取App代理定制版数据
	 * @param hostAddress
	 * @return
	 */
	public AppCustomVersionVO[] getAppCustomVersionData(String hostAddress){
		return userService.getAppCustomVersionData(hostAddress);
	}

	/**
	 * 生日礼金领取
	 * @param loginname
	 * @return
	 */
	public String getBirthdayMoney(Integer level){
		log.info("----------【getBirthdayMoney】---------------");

		return activityConfigService.queryActivityConfig(level);
	}
	/**
	 * 生日礼金转到主账户
	 * @param loginname
	 * @param amout
	 * @return
	 */
	public String drawBirthdayMoney(String loginname,Double amout){
		log.info("----------【drawBirthdayMoney】---------------");
		Users user = cs.getUsers(loginname);

		return transferService.transferBirthdayToMain(user, amout);
	}


	public ActivityConfigVO  checkActivityInfo(ActivityConfigVO vo){

		UserWebServiceWS userWebserviceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");
		ActivityConfigVO vo1 = new ActivityConfigVO();
		if(userWebserviceWS.selectDeposit(vo.getActiveType())==null){
			vo1.setMsg("该活动不存在");
			return vo;
		}
		if(userWebserviceWS.selectDeposit(vo.getActiveType()).getValue().trim().equals("0")){
			vo1.setMsg("该活动暂未开启");
			return vo1;
		}
		ActivityConfig activityConfig	= userWebserviceWS.checkActivityInfo(vo.getTitle(), vo.getLevel().toString());

		if(activityConfig!= null){
			vo1.setAmount(activityConfig.getAmount());
			vo1.setPlatform(activityConfig.getPlatform());
			vo1.setMultiple(activityConfig.getMultiple());

		}
		return vo1;
	}
}