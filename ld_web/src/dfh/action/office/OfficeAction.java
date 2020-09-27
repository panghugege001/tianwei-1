package dfh.action.office;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.Action;

import dfh.action.SubActionSupport;
import dfh.dao.UserDao;
import dfh.model.Accountinfo;
import dfh.model.Actionlogs;
import dfh.model.Banktransfercons;
import dfh.model.Betrecords;
import dfh.model.Cashin;
import dfh.model.Cashout;
import dfh.model.Commissionrecords;
import dfh.model.Commissions;
import dfh.model.Concessions;
import dfh.model.Const;
import dfh.model.Creditlogs;
import dfh.model.Newaccount;
import dfh.model.Offer;
import dfh.model.Operationlogs;
import dfh.model.Operator;
import dfh.model.Payorder;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.Rebankinfo;
import dfh.model.Task;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.bean.AuditDetail;
import dfh.model.enums.AnnouncementType;
import dfh.model.enums.IssuingBankNumber;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.model.notdb.Report;
import dfh.remote.RemoteConstant;
import dfh.service.interfaces.AnnouncementService;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.SynRecordsService;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.HttpUtils;
import dfh.utils.NumericUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.StringUtil;

public class OfficeAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(OfficeAction.class);
	private ProposalService proposalService;
	private NetpayService netpayService;
	private OperatorService operatorService;
	private CustomerService customerService;
	private TransferService transferService;
	private SynRecordsService synRecordsService;
	private SeqService seqService;
	private NotifyService notifyService;
	private UserDao userDao;
	
	
	
	private String loginname;
	private String password;
	private String validateCode;
	private Date start;
	private Date end;
	private Integer size;
	private Integer pageIndex;
	private String order;
	private String by;
	private String roleCode;
	private String pno;
	private String qq;
	private Integer proposalFlag;
	private String proposalType;
	private String creditLogType;
	private Double downLmit;
	private String billno;
	private Integer payOrderFlag;
	private Integer isTransferIn;
	private Integer transferFalg;
	private Double amount;
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
	private String type;
	private String content;
	private Integer id;
	private String PhoneNum;
	private String Msg;
	private Boolean isEnable;
	private String jobPno;
	private String errormsg;
	private Integer isAdd;
	private String remoteUrl;

	private String oldPassword;
	private String newPassword;
	private String retypePassword;

	private Integer newaccount;

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
	private String referWebsite;

	private String xml;

	private String registerIp;
	private String status;
	private String isCashin;
	private Integer year;
	private Integer month;

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

	public OfficeAction() {
		start = DateUtil.now();
		end = DateUtil.now();

	}

	public String addCashinProposal() {
		try {
			String msg = proposalService.addCashin(getOperatorLoginname(), StringUtil.trim(loginname), StringUtil.trim(accountName), title,
					Constants.FROM_BACK, amount, StringUtil.trim(corpBankName), StringUtil.trim(remark), IssuingBankNumber.getText(StringUtil
							.trim(corpBankName)),"","",null);
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

	public String addCashOutProposal() {
		try {
			Users customer = (Users) operatorService.get(Users.class, StringUtil.trim(loginname));
			String msg = proposalService.addCashout(getOperatorLoginname(), customer.getLoginname(), customer.getPassword(), customer.getRole(),
					Constants.FROM_BACK, amount, customer.getAccountName(), StringUtils.trim(accountNo), StringUtils.trim(accountType),
					StringUtils.trim(bank), StringUtils.trim(accountCity), StringUtil.trim(bankAddress), StringUtils.trim(email), StringUtils
							.trim(phone), getIp(), StringUtils.trim(remark),"");
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

	public String addConcessionsProposal() {
		try {
			String msg = proposalService.addUserConcession(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK,
					firstCash, tryCredit, StringUtil.trim(payType), StringUtil.trim(remark), getIp());
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
			String msg = proposalService.addBankTransferCons(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK,
					firstCash, tryCredit, StringUtil.trim(payType), StringUtil.trim(remark));
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
			String msg = proposalService.addPrize(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, tryCredit,
					StringUtil.trim(remark));
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
			String msg = proposalService.operatorAddNewAccount(getOperatorLoginname(), StringUtil.trim(loginname), StringUtil.trim(password),
					title, Constants.FROM_BACK, StringUtil.trim(aliasName), StringUtil.trim(phone), email, title, StringUtil.trim(remark),
					getIp());
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
			String msg = proposalService.addReBankInfo(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, StringUtil
					.trim(accountName), StringUtil.trim(accountNo), StringUtil.trim(accountType), StringUtil.trim(bank), StringUtil
					.trim(accountCity), StringUtil.trim(bankAddress), getIp(), StringUtil.trim(remark));
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
			String msg = proposalService.addXima(getOperatorLoginname(), StringUtil.trim(loginname), title, Constants.FROM_BACK, start, end,
					firstCash, (rate.doubleValue() / 100), StringUtil.trim(payType), StringUtil.trim(remark));
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
		log.info("提案审核");
		try {
			String msg = proposalService.audit(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			if (msg == null)
				setErrormsg("审核成功");
			else
				setErrormsg("审核失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("审核失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String cancleProposal() {
		try {
			String msg = proposalService.cancle(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			if (msg == null)
				setErrormsg("取消成功");
			else
				setErrormsg("取消失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("取消失败:" + e.getMessage());
		}
		return INPUT;
	}

	

	public String enableUser() {
		try {
			String msg = operatorService.EnableUser(loginname, isEnable.booleanValue(), getOperatorLoginname());
			if (msg == null)
				msg = "操作成功";
			setErrormsg(msg);
		} catch (Exception e) {
			// TODO: handle exception
			this.setErrormsg(e.getMessage().toString());
		}
		return INPUT;
	}

	public String excuteProposal() {
		try {
			String msg = proposalService.excute(jobPno, getOperatorLoginname(), getIp(), StringUtil.trim(remark));
			if (msg == null)
				setErrormsg("执行成功");
			else
				setErrormsg((new StringBuilder("执行失败:")).append(msg).toString());
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("执行失败:" + e.getMessage());
		}
		return INPUT;
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

	public Double getDownLmit() {
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
		Users user = (Users) operatorService.get(Users.class, loginname);
		if (user != null)
			getRequest().setAttribute("user", user);
		return SUCCESS;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public String officeLogin() {
		if (getHttpSession().getAttribute(Constants.SESSION_OPERATORID) != null)
			return SUCCESS;
		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (!validateCode.equalsIgnoreCase(rand)) {
			addFieldError("wwctrl", "验证码错误");
			return INPUT;
		}
		String msg = operatorService.login(loginname, password, getIp());
		if (msg != null)
			addFieldError("wwctrl", msg);
		else {
			Operator operator = (Operator) operatorService.get(Operator.class, loginname);
			getHttpSession().setAttribute(Constants.SESSION_OPERATORID, operator);
			return SUCCESS;
		}
		return INPUT;
	}

	public String queryActionLog() {
		DetachedCriteria dc = DetachedCriteria.forClass(Actionlogs.class);
		if (StringUtils.isNotEmpty(type))
			dc = dc.add(Restrictions.eq("action", type));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		dc = dc.addOrder(Order.asc("createtime"));
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size);
		getRequest().setAttribute("page", page);
		return INPUT;
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

		dc = dc.addOrder(Order.asc("billTime"));
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "billAmount",
				"result",null);
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
				// page.setPageContents(RemoteCaller.getBetRecord(start, end, loginname, null));
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
				// page.setPageContents(RemoteCaller.getBalanceRecords(loginname, start, end));
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
		// Double totalDepositAmount = 0.0, totalCashoutAmount = 0.0, totalConcessionAmount = 0.0, totalBetAmount = 0.0, totalProfitAmount = 0.0;
		//
		// // 从提案表来查询
		// DetachedCriteria dcProposal = DetachedCriteria.forClass(Proposal.class).add(Restrictions.ge("createTime",
		// start)).add(Restrictions.lt("createTime", end)).add(
		// Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		// dcProposal.setProjection(Projections.projectionList().add(Projections.groupProperty("type")).add(Projections.sum("amount")));
		// List resultProposal = proposalService.getHibernateTemplate().findByCriteria(dcProposal);
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
		// DetachedCriteria dcPayOrder = DetachedCriteria.forClass(PayOrder.class).add(Restrictions.ge("createTime",
		// start)).add(Restrictions.lt("createTime", end)).add(
		// Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
		// dcPayOrder.setProjection(Projections.sum("money"));
		// List resultPayOrder = proposalService.getHibernateTemplate().findByCriteria(dcPayOrder);
		// Double netpayAmount = (Double) resultPayOrder.get(0);
		// if (netpayAmount != null)
		// totalDepositAmount += netpayAmount;
		//
		// // 从投注记录表来查询
		// // 有效投注额，排除没有开出结果和派彩为0的注单
		// DetachedCriteria dcBetRecord = DetachedCriteria.forClass(BetRecords.class).add(Restrictions.ge("wagersDate",
		// start)).add(Restrictions.lt("wagersDate", end)).add(
		// Restrictions.eq("userName", loginname)).add(Restrictions.sqlRestriction("result!=''")).add(Restrictions.ne("payoff", new Double(0)));
		// dcBetRecord.setProjection(Projections.projectionList().add(Projections.sum("betAmount")).add(Projections.sum("payoff")));
		// List resultBetRecord = proposalService.getHibernateTemplate().findByCriteria(dcBetRecord);
		// Object[] arrayBetRecord = (Object[]) resultBetRecord.get(0);
		// if (arrayBetRecord[0] != null)
		// totalBetAmount += (Double) arrayBetRecord[0];
		// if (arrayBetRecord[1] != null)
		// totalProfitAmount += (Double) arrayBetRecord[1];
		//
		// // 龙虎投注。龙虎游戏中，下注龙、虎而结果为和的情况，按照投注额的一半计算为有效投注额
		// dcBetRecord.add(Restrictions.eq("gameType",
		// GameType.LONGHUMEN.getCode())).add(Restrictions.sqlRestriction(" substr(result,1,1)=substr(result,3,1) ")).add(
		// Restrictions.sqlRestriction("payoff=betAmount/-2"));
		// List resultBetRecord2 = proposalService.getHibernateTemplate().findByCriteria(dcBetRecord);
		// Object[] arrayBetRecord2 = (Object[]) resultBetRecord2.get(0);
		// Double skipBetAmount = 0.0, skipProfitAmount = 0.0;
		// skipBetAmount = (Double) (arrayBetRecord2[0] == null ? 0.0 : arrayBetRecord2[0]);
		// skipProfitAmount = (Double) (arrayBetRecord2[1] == null ? 0.0 : arrayBetRecord2[1]);
		// log.info("src totalBetAmount:" + totalBetAmount + ";skipBetAmount:" + skipBetAmount + ";skipProfitAmount:" + skipProfitAmount);
		// totalBetAmount -= skipBetAmount / 2;
		//
		// getRequest().setAttribute("totalDepositAmount", totalDepositAmount);
		// getRequest().setAttribute("totalCashoutAmount", totalCashoutAmount);
		// getRequest().setAttribute("totalBetAmount", totalBetAmount);
		// getRequest().setAttribute("totalProfitAmount", totalProfitAmount);
		// getRequest().setAttribute("totalConcessionAmount", totalConcessionAmount);
		// getRequest().setAttribute("aliasName", user.getAliasName());
		// }
		// } else {
		// setErrormsg("会员帐号,开始时间都不能为空");
		// }
		return INPUT;
	}

	public String queryCreditlogs() {
		DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
		if (StringUtils.isNotEmpty(creditLogType))
			dc = dc.add(Restrictions.eq("type", creditLogType));
		if (downLmit != null)
			dc = dc.add(Restrictions.ge("remit", downLmit));
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
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "remit", null,null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryOperationLog() {
		DetachedCriteria dc = DetachedCriteria.forClass(Operationlogs.class);
		if (StringUtils.isNotEmpty(type))
			dc = dc.add(Restrictions.eq("action", type));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(remark))
			dc = dc.add(Restrictions.ilike("remark", remark, MatchMode.ANYWHERE));
		if (start != null)
			dc = dc.add(Restrictions.ge("createtime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createtime", end));
		dc = dc.addOrder(Order.asc("createtime"));
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryPayOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
		if (StringUtils.isNotEmpty(billno)) {
			dc = dc.add(Restrictions.eq("billno", billno));
			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (payOrderFlag != null)
			dc = dc.add(Restrictions.eq("flag", payOrderFlag));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(by)) {
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createTime", end));
		if (newaccount != null)
			dc = dc.add(Restrictions.eq("newaccount", newaccount));
		if (StringUtils.isNotEmpty(by)) {
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "money", null,null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryProposal() {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		if (StringUtils.isNotEmpty(pno)) {
			dc = dc.add(Restrictions.eq("pno", pno));
			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(proposalType))
			dc = dc.add(Restrictions.eq("type", Integer.valueOf(Integer.parseInt(proposalType))));
		if (proposalFlag != null)
			dc = dc.add(Restrictions.eq("flag", proposalFlag));
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.eq("loginname", loginname));
		if (StringUtils.isNotEmpty(agent))
			dc = dc.add(Restrictions.eq("agent", agent));
		if (StringUtils.isNotEmpty(referWebsite)){
			Users agent = userDao.getAgentByWebsite(referWebsite);
			if(agent!=null)
				dc = dc.add(Restrictions.eq("agent", agent.getLoginname()));
		}
		
		if (StringUtils.isNotEmpty(by)) {
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc = dc.addOrder(o);
		}
		if (start != null)
			dc = dc.add(Restrictions.ge("createTime", start));
		if (end != null)
			dc = dc.add(Restrictions.lt("createTime", end));
		
		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null,null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryProposalDetail() {
		Proposal p = (Proposal) operatorService.get(Proposal.class, pno);
		if (p != null) {
			if (ProposalType.NEWACCOUNT.getCode().toString().equals(proposalType)) {
				Newaccount newAccount = (Newaccount) operatorService.get(Newaccount.class, pno);
				getRequest().setAttribute("newAccount", newAccount);
			} else if (ProposalType.CASHIN.getCode().toString().equals(proposalType)) {
				Cashin cashin = (Cashin) operatorService.get(Cashin.class, pno);
				getRequest().setAttribute("cashin", cashin);
			} else if (ProposalType.CASHOUT.getCode().toString().equals(proposalType)) {
				Cashout cashout = (Cashout) operatorService.get(Cashout.class, pno);
				getRequest().setAttribute("cashout", cashout);

				// 输出审核结果
				Map<String, AuditDetail> map = (Map<String, AuditDetail>) getRequest().getSession().getServletContext().getAttribute(
						Constants.AUDIT_DETAIL);
				if (map == null) {
					map = new HashMap<String, AuditDetail>();
					getRequest().getSession().getServletContext().setAttribute(Constants.AUDIT_DETAIL, map);
				}

				if (map.get(p.getPno()) == null) {
					AuditDetail auditDetail = new AuditDetail(p.getLoginname(), p.getPno(), null, null, null, null);
					log.info("查找上次成功提款的提案");
					Proposal lastCashout = proposalService.getLastSuccCashout(p.getLoginname(), p.getCreateTime());

					if (lastCashout == null) {
						log.info("上次成功提款提案为空，确定为首次提款");
						auditDetail.setOutput("用户首次提款");
					} else if (lastCashout.getAfterLocalAmount() == -1.0 || lastCashout.getAfterRemoteAmount() ==  -1.0) {
						log.info("上次提款[" + lastCashout.getPno() + "]瞬时额度记录为空");
						auditDetail.setOutput("上次提款瞬时额度记录为空");
					} else if (p.getAfterLocalAmount() ==  -1.0 || p.getAfterRemoteAmount() ==  -1.0) {
						log.info("本次提款[" + p.getPno() + "]瞬时额度记录为空");
						auditDetail.setOutput("本次提款瞬时额度记录为空");
					} else {
						log.info("上次提款提案号["+lastCashout.getPno()+"]");
						Double lastTotalAmount = lastCashout.getAfterLocalAmount() + lastCashout.getAfterRemoteAmount();
						//本次提款前的总额度
						Double currentTotalAmount = p.getAfterLocalAmount() + p.getAfterRemoteAmount()+p.getAmount();
						log.info("查询据上次提款之间的本地转入转出额度变化");
						Double localCreditChange = operatorService.getLocalCreditChangeByPeriod(p.getLoginname(), lastCashout
								.getCreateTime(), p.getCreateTime());
						log.info("localCreditChange:"+localCreditChange);
						Double loseAmount =currentTotalAmount -lastTotalAmount - localCreditChange  ;
						log.info("计算出预计的输赢值是" + loseAmount);
						auditDetail.setLastTime(lastCashout.getCreateTime());
						auditDetail.setCurrentTime(p.getCreateTime());
						auditDetail.setLastTotalAmount(lastTotalAmount);
						auditDetail.setCurrentTotalAmount(currentTotalAmount);
						auditDetail.setLocalCreditChange(localCreditChange);
						auditDetail.setLoseAmount(loseAmount);
					}
					map.put(p.getPno(), auditDetail);
				}
				getRequest().getSession().getServletContext().setAttribute(Constants.AUDIT_DETAIL, map);

			} else if (ProposalType.CONCESSIONS.getCode().toString().equals(proposalType)) {
				Concessions concessions = (Concessions) operatorService.get(Concessions.class, pno);
				getRequest().setAttribute("concessions", concessions);
			} else if (ProposalType.REBANKINFO.getCode().toString().equals(proposalType)) {
				Rebankinfo rebankinfo = (Rebankinfo) operatorService.get(Rebankinfo.class, pno);
				getRequest().setAttribute("rebankinfo", rebankinfo);
			} else if (ProposalType.XIMA.getCode().toString().equals(proposalType)) {
				Xima xima = (Xima) operatorService.get(Xima.class, pno);
				getRequest().setAttribute("xima", xima);
			} else if (ProposalType.BANKTRANSFERCONS.getCode().toString().equals(proposalType)) {
				Banktransfercons cons = (Banktransfercons) operatorService.get(Banktransfercons.class, pno);
				getRequest().setAttribute("bankTransferCons", cons);
			} else if (ProposalType.OFFER.getCode().toString().equals(proposalType)) {
				Offer offer = (Offer) operatorService.get(Offer.class, pno);
				getRequest().setAttribute("offer", offer);
			} else if (ProposalType.PRIZE.getCode().toString().equals(proposalType)) {
				Prize prize = (Prize) operatorService.get(Prize.class, pno);
				getRequest().setAttribute("prize", prize);
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
				// page.setPageContents(RemoteCaller.getTransferRecord(loginname, start, end, isIn));
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
			dc = dc.add(Restrictions.eq("source",
					isTransferIn.intValue() != Constants.FLAG_TRUE.intValue() ? ((Object) (RemoteConstant.PAGESITE))
							: ((Object) (RemoteConstant.WEBSITE))));
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
		if (StringUtils.isNotEmpty(by)) {
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc = dc.addOrder(o);
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size);

		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String queryUser() {
		Page page = new Page();
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		loginname = StringUtil.trim(loginname);
		agent = StringUtil.trim(agent);
		if (StringUtils.isNotEmpty(roleCode))
			dc = dc.add(Restrictions.eq("role", roleCode));
		if (level != null)
			dc = dc.add(Restrictions.eq("level", level));// sun
		if (StringUtils.isNotEmpty(status))
			dc = dc.add(Restrictions.eq("flag", Integer.parseInt(status)));
		if (StringUtils.isNotEmpty(isCashin))
			dc = dc.add(Restrictions.eq("isCashin", Integer.parseInt(isCashin)));
		if (StringUtils.isEmpty(loginname) && StringUtils.isEmpty(aliasName) && StringUtils.isEmpty(accountName)
				&& StringUtils.isEmpty(registerIp) && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(accountNo)  && StringUtils.isEmpty(referWebsite) && StringUtils.isEmpty(agent)) {
			if (start != null)
				dc = dc.add(Restrictions.ge("createtime", start));
			if (end != null)
				dc = dc.add(Restrictions.lt("createtime", end));

			page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size);
			getRequest().setAttribute("page", page);
			return INPUT;
		}
		if (StringUtils.isNotEmpty(loginname))
			dc = dc.add(Restrictions.ilike("loginname", loginname, MatchMode.ANYWHERE));
		if (StringUtils.isNotEmpty(aliasName))
			dc = dc.add(Restrictions.ilike("aliasName", aliasName, MatchMode.ANYWHERE));
		if (StringUtils.isNotEmpty(agent))
			dc = dc.add(Restrictions.ilike("agent", agent, MatchMode.ANYWHERE));
		if (StringUtils.isNotEmpty(accountName))
			dc = dc.add(Restrictions.eq("accountName", accountName));
		if (StringUtils.isNotEmpty(email))
			dc = dc.add(Restrictions.eq("email", email));
		if (StringUtils.isNotEmpty(phone))
			dc = dc.add(Restrictions.eq("phone", phone));
		if (StringUtils.isNotEmpty(registerIp))
			dc = dc.add(Restrictions.eq("registerIp", registerIp));
		if (StringUtils.isNotEmpty(referWebsite)){
			dc = dc.add(Restrictions.eq("referWebsite", referWebsite));
		}
		if (StringUtils.isNotEmpty(by)) {
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc = dc.addOrder(o);
		}
		page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String repairPayOrder() {
		try {
			String msg = netpayService.repairNetpayOrder(getOperatorLoginname(), billno, amount, getIp());
			if (msg == null)
				setErrormsg("补单成功");
			else
				setErrormsg("补单失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("补单失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String resetUserPassword() {
		String msg = null;
		try {
			msg = operatorService.resetPassword(loginname, password, getOperatorLoginname());
			if (msg == null)
				setErrormsg("密码重设成功");
			else
				setErrormsg("密码重设失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("密码重设失败:" + e.getMessage());
		}
		return INPUT;
	}

	// 发送手机短信 update:sun
	public String sendSms() {
		try {
			// loadUsersPhone();
			if (PhoneNum.length() <= 0)
				setErrormsg("请输入手机号码:");
			else {
				String msg = notifyService.sendSms(PhoneNum.trim(), StringUtil.trim(Msg));
				setErrormsg("返回消息:" + msg);
			}
		} catch (Exception e) {
			setErrormsg("返回消息:" + e.getMessage());
			e.printStackTrace();
		}
		return INPUT;
	}

	// 发送邮件 author:sun
	public String sendEmail() {
		try {
			loadUsersEmail();
			String urlContext = "", msg = "";
			if (StringUtil.isNotEmpty(urladdress)) {
				urlContext = HttpUtils.get(urladdress, null);
			}
			if (email.length() <= 0)
				setErrormsg("返回消息:未找到任何的收件人");
			else {
				msg = notifyService.sendEmail(email, title, Msg);
				setErrormsg("返回消息:" + msg);
			}
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
			List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, sumCount).getPageContents();
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
			Integer pageCount = PageQuery.queryForCount(operatorService.getHibernateTemplate(), dc);
			List usersList = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, 1, pageCount).getPageContents();
			int forCount = usersList.size();
			for (int i = 0; i < forCount; i++) {
				Users user = (Users) usersList.get(i);
				email = email + "," + user.getEmail();
			}
			if (email.startsWith(",")) {
				email = email.substring(1);
			}
		}
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
		String msg = null;
		try {
			msg = operatorService.modifyCustomerInfo(loginname, aliasName, phone, email, qq, remark, getOperatorLoginname());
			if (msg == null)
				setErrormsg("修改成功");
			else
				setErrormsg("修改失败:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("修改失败:" + e.getMessage());
		}
		return INPUT;
	}

	public String queryUserAnalyze() {
		Integer free_customer_count = 0, money_customer_count = 0, common_money_customer_count = 0, baijin_vip_count = 0, zuanshi_vip_count = 0, partner_count = 0;
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class).setProjection(
				Projections.projectionList().add(Projections.groupProperty("role")).add(Projections.groupProperty("level")).add(
						Projections.rowCount()));
		List list = operatorService.findByCriteria(dc);
		if (list != null) {
			Iterator<Object[]> it = list.listIterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				String role = obj[0].toString();
				Integer level = (Integer) obj[1];
				Integer count = (Integer) obj[2];
				// if (role.equals(UserRole.FREE_CUSTOMER.getCode()))
				// free_customer_count += count;
				if (role.equals(UserRole.MONEY_CUSTOMER.getCode())) {
					money_customer_count += count;
					if (level.intValue() == VipLevel.COMMON.getCode().intValue())
						common_money_customer_count += count;
					if (level.intValue() == VipLevel.BAIJIN.getCode().intValue())
						baijin_vip_count += count;
					if (level.intValue() == VipLevel.ZUANSHI.getCode().intValue())
						zuanshi_vip_count += count;
				}
				// if (role.equals(UserRole.PARTNER.getCode()))
				// partner_count += count;
				System.out.println(role + " " + level + " " + count);
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
		try {
			if (isAdd.intValue() == Constants.FLAG_TRUE.intValue()) {
				msg = operatorService.changeCreditManual(loginname, Math.abs(amount), remark, getOperatorLoginname());
			} else {
				msg = operatorService.changeCreditManual(loginname, Math.abs(amount) * -1, remark, getOperatorLoginname());
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
			msg = operatorService.modifyOwnPassword(getOperatorLoginname(), oldPassword, newPassword);
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

	public String createSubOperator() {
		String msg = null;
		try {
			msg = operatorService.createSubOperator(newOperator, password, getOperatorLoginname());
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

	public void setDownLmit(Double downLmit) {
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
					// transferService.transferIn(seqService.generateTransferID(), loginname);// 转入
					// transferService.transferOut(seqService.generateTransferID(), loginname);// 转出
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
				// String msg = customerService.modifyUserRemoteUrl(loginname, remoteUrl);
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
		// String msg = customerService.addMatch(loginname, getOperatorLoginname());
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

	// 提交促销优惠
	public String addOfferProposal() {
		try {
			String msg = proposalService.addOffer(getOperatorLoginname(), StringUtil.trim(loginname), StringUtil.trim(title),
					Constants.FROM_BACK, firstCash, tryCredit, remark);
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
			getRequest()
					.setAttribute("page", operatorService.queryReport(start, end, StringUtil.isEmpty(loginname) ? roleCode : null, loginname));
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}

	// 总报表信息详细
	public String queryReportInfo() {
		try {
			getRequest()
					.setAttribute("page", operatorService.queryReport(start, end, StringUtil.isEmpty(loginname) ? roleCode : null, loginname));

			// 查询详细
			if (StringUtil.isEmpty(loginname)) {
				DetachedCriteria dcBetRecord = DetachedCriteria.forClass(Betrecords.class);
				if (StringUtil.isNotEmpty(roleCode)) {
					if (roleCode.equals(UserRole.MONEY_CUSTOMER.getCode()))
						dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
					// if (roleCode.equals(UserRole.FREE_CUSTOMER.getCode()))
					// dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_FREE_CUSTOMER + "%"));
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
			String msg = operatorService.addAgent(StringUtils.trim(acode), StringUtils.trim(loginname), StringUtils.trim(accountName),
					StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), StringUtils.trim(referWebsite), getIp(),
					getOperatorLoginname());
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
				// AutoTask task = operatorService.getLastAutoTask(AutoGenerateType.AUTO_XIMA);
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
			DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class);
			if (StringUtils.isNotEmpty(agent))
				dc = dc.add(Restrictions.eq("id.loginname", agent));
			dc = dc.add(Restrictions.eq("id.year", year));
			dc = dc.add(Restrictions.eq("id.month", month));
			if (StringUtils.isNotEmpty(by)) {
				Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
				dc = dc.addOrder(o);
			}
			Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "amount", null,null);
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
}
