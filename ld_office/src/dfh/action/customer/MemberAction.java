package dfh.action.customer;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.action.SubActionSupport;
import dfh.listener.MySessionContext;
import dfh.model.Accountinfo;
import dfh.model.Commissionrecords;
import dfh.model.Commissions;
import dfh.model.CommissionsId;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.remote.RemoteCaller;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.NumericUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;

public class MemberAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(MemberAction.class);
	private CustomerService cs;
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
	private String referWebsite;
	private String accountName;
	private String accountType;
	private String accountCity;
	private String bankAddress;
	private String accountNo;
	private String validateCode;
	private Double amount;
	private String remark;
	private Double firstCash;
	private Double tryCredit;
	private String loginname;
	private String errormsg;
	private Double balance;

	private String notifyNote;// 是否短信通知
	private String notifyPhone;// 是否电话通知

	private Date start;// 起始时间
	private Date end;// 结束时间
	private Integer proposalFlag;// 状态
	private Integer proposalType;// 类型
	private Integer pageIndex;// 当前页
	private Integer size;// 每页显示的行数
	
	private Integer year;
	private Integer month;


	private String jobPno;// 订单号

	private Double remit;

	public Double getRemit() {
		return remit;
	}

	public void setRemit(Double remit) {
		this.remit = remit;
	}

	private static final long serialVersionUID = 1L;

	public MemberAction() {

	}

	public String login() {
		if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null)
			return INPUT;

		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(rand)) {
			setErrormsg("验证码错误");
			return INPUT;
		}

		try {
			String msg = cs.loginWebForBoth(loginname, password, getIp());
			if (StringUtils.equals(msg, UserRole.MONEY_CUSTOMER.getCode()) || StringUtils.equals(msg, UserRole.AGENT.getCode())) {
				Users user = (Users) cs.get(Users.class, loginname);
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
			} else {
				setErrormsg(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}

	public String loginTransfer() {
		if (getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID) != null)
			return SUCCESS;

		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(rand)) {
			setErrormsg("验证码错误");
			return INPUT;
		}

		try {
			String msg = cs.loginWeb(loginname, password, getIp());
			if (msg == null) {
				Users user = (Users) cs.get(Users.class, loginname);
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				return SUCCESS;
			} else {
				setErrormsg(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
		}
		return INPUT;
	}

	// 修改会员个人基本信息
	public String modifyCusinfo() throws Exception {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else
			try {
				String msg = cs.modifyCustomerInfo(customer.getLoginname(), StringUtils.trim(aliasName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils.trim(qq), getIp());
				if (msg == null)
					refreshUserInSession();
				setErrormsg(msg != null ? msg : "修改成功");
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统异常");
			}
		return INPUT;
	}

	// 修改密码
	public String modifyPassword() throws Exception {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else
			try {
				String msg = cs.modifyPassword(customer.getLoginname(), password, new_password, getIp());
				setErrormsg(msg != null ? msg : "修改成功");
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("系统异常");
			}
		return INPUT;
	}

	// 检查用户是否登陆 sun
	public boolean checkLogin() {
		Users customer = getCustomerFromSession();
		if (customer == null) {
			setErrormsg("请您从首页登录");
			return false;
		}
		return true;
	}

	// 开户
	public String register() {
		String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
		if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(rand)) {
			setErrormsg("验证码错误");
			return INPUT;
		}

		String msg = cs.register(StringUtils.trim(loginname), password, StringUtils.trim(accountName), StringUtils.trim(aliasName), StringUtils.trim(phone), StringUtils.trim(email), StringUtils
				.trim(qq), StringUtils.trim(referWebsite), getIp());
		if (msg == null) {
			setErrormsg("注册成功");
			Users user = (Users) cs.get(Users.class, loginname);
			if (user.getFlag()!=1) {
				// 根据需求，在注册的的时候，违反约束依然可以注册成功，但是账户的状态时被禁用的。
				// 如：注册成功，并且在账号被未禁用的前提下，该用户信息，才会被加入session.
				getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
			}
			return SUCCESS;
		} else {
			setErrormsg("注册失败 :" + msg);
			return INPUT;
		}
	}

	// 申请提款
	public String withdraw() {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else {
			try {
				String msg = proposalService.addCashout(customer.getLoginname(), customer.getLoginname(), password, customer.getRole(), Constants.FROM_FRONT, amount, customer.getAccountName(),
						StringUtils.trim(accountNo), Constants.DEFAULT_ACCOUNTTYPE, StringUtils.trim(bank), StringUtils.trim(bankAddress), StringUtils.trim(bankAddress), StringUtils.trim(email),
						customer.getPhone(), getIp(), null);
				if (msg == null) {
					refreshUserInSession();
					return SUCCESS;
				} else {
					setErrormsg("提交失败:" + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg(e.getMessage());
			}
		}
		return INPUT;
	}

	public String transferIn() {
		String msg = null;
		try {
			msg = transferService.transferIn(seqService.generateTransferID(), getCustomerLoginname(), remit);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		refreshUserInSession();
		if (msg == null) {
			setErrormsg("转账成功");
			return SUCCESS;
		} else {
			setErrormsg("提交失败:" + msg);
			return INPUT;
		}
	}

	public String transferInForCaiwu() {
		String msg = null;
		try {
			msg = transferService.transferIn(seqService.generateTransferID(), getCustomerLoginname(), remit);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		refreshUserInSession();
		if (msg == null) {
			setErrormsg("转账成功");
		} else {
			setErrormsg("提交失败:" + msg);
		}
		return INPUT;
	}

	public String transferOut() {
		String msg = null;
		try {
			msg = transferService.transferOut(seqService.generateTransferID(), getCustomerLoginname(), remit);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		refreshUserInSession();
		if (msg == null) {
			setErrormsg("转账成功");
			return SUCCESS;
		} else {
			setErrormsg("提交失败:" + msg);
			return INPUT;
		}
	}

	public String transferOutForCaiwu() {
		String msg = null;
		try {
			msg = transferService.transferOut(seqService.generateTransferID(), getCustomerLoginname(), remit);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		refreshUserInSession();
		if (msg == null) {
			setErrormsg("转账成功");
		} else {
			setErrormsg("提交失败:" + msg);
		}
		return INPUT;
	}

	public String addConcessions() throws Exception {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else
			try {
				String msg = proposalService.addUserConcession(customer.getLoginname(), customer.getLoginname(), customer.getRole(), Constants.FROM_FRONT, firstCash, tryCredit, "在线支付", remark,
						getIp());
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
		try {
			if (cs.get(Users.class, loginname) == null)
				getResponse().getWriter().write("false");
			else
				getResponse().getWriter().write("true");
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg("系统异常");
		}
		return null;
	}

	public String queryDepositReccords() {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else {
			DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
			dc = dc.add(Restrictions.eq("loginname", customer.getLoginname()));
			dc = dc.add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
//			dc = dc.addOrder(Order.desc("createTime"));
			Order o = Order.desc("createTime");
			Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
		}
		return INPUT;
	}

	public String queryCashinRecords() {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.eq("type", ProposalType.CASHIN.getCode()));
			dc = dc.add(Restrictions.eq("loginname", customer.getLoginname()));
//			dc = dc.addOrder(Order.desc("createTime"));
			Order o = Order.desc("createTime");
			Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
		}
		return INPUT;
	}

	public String queryWithdrawReccords() {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode()));
			dc = dc.add(Restrictions.eq("loginname", customer.getLoginname()));
//			dc = dc.addOrder(Order.desc("createTime"));
			Order o = Order.desc("createTime");
			Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
		}
		return INPUT;
	}

	public String queryTransferReccords() {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else {
			DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
			dc = dc.add(Restrictions.eq("loginname", customer.getLoginname()));
			dc = dc.add(Restrictions.eq("flag", Constants.FLAG_TRUE));
//			dc = dc.addOrder(Order.desc("createtime"));
			Order o = Order.desc("createtime");
			Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
		}
		return INPUT;
	}

	public String queryConcessionReccords() {
		Users customer = getCustomerFromSession();
		if (customer == null)
			setErrormsg("请您从首页登录");
		else {
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.eq("loginname", customer.getLoginname()));
			dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			dc = dc.add(Restrictions
					.in("type", new Object[] { ProposalType.XIMA.getCode(), ProposalType.OFFER.getCode() }));//ProposalType.BANKTRANSFERCONS.getCode(), ProposalType.CONCESSIONS.getCode(), 
//			dc = dc.addOrder(Order.desc("createTime"));
			Order o = Order.desc("createTime");
			Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
			getRequest().setAttribute("page", page);
		}
		return INPUT;
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
		Users user = getCustomerFromSession();
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
			String loginname = getCustomerLoginname();
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
//				dc = dc.addOrder(Order.desc("createTime"));
				Order o = Order.desc("createTime");

				Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, new Integer(10), o);
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
		if (StringUtils.isNotEmpty(getCustomerLoginname())) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode())).add(Restrictions.eq("agent", getCustomerLoginname()));
			Page page = PageQuery.queryForPagenation(cs.getHibernateTemplate(), dc, pageIndex, size, null);
			getRequest().setAttribute("page", page);
		}
		return INPUT;
	}

	public String queryCommissionrecords() {
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Commissionrecords.class);
			if (StringUtils.isNotEmpty(getCustomerLoginname()))
				dc = dc.add(Restrictions.eq("parent", getCustomerLoginname()));
//			dc = dc.add(Restrictions.eq("id.year",year )).add(Restrictions.eq("id.month", month)).addOrder(Order.desc("ximaAmount"));
			dc = dc.add(Restrictions.eq("id.year",year )).add(Restrictions.eq("id.month", month));
			Order o = Order.desc("ximaAmount");
			Page page=PageQuery.queryForPagenationWithStatistics(proposalService.getHibernateTemplate(), dc, pageIndex, size,"ximaAmount","firstDepositAmount","otherAmount", o);
			Commissions cmm=(Commissions) proposalService.get(Commissions.class, new CommissionsId(getCustomerLoginname(), year, month));
			getRequest().setAttribute("page", page);
			getRequest().setAttribute("cmm", cmm);
		} catch (Exception e) {
			e.printStackTrace();
			setErrormsg(e.getMessage());
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
						session.setAttribute(Constants.SESSION_CUSTOMERID, cs.get(Users.class, loginname));
						msg = Constants.FLAG_TRUE + "";
					}

				}
			}
		}
		try {
			getResponse().getWriter().println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
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

	private void refreshAccountinfoInSession() {
		Users customer = getCustomerFromSession();
		getHttpSession().setAttribute("accountinfo", cs.get(Accountinfo.class, customer.getLoginname()));
	}

	private void refreshUserInSession() {
		Users customer = getCustomerFromSession();
		getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, cs.get(Users.class, customer.getLoginname()));
	}

}
