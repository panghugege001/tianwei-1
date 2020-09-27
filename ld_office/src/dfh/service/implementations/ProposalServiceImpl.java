package dfh.service.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import app.model.po.ConstraintAddressConfig;
import dfh.model.*;
import jxl.*;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.action.vo.XimaVO;
import dfh.dao.BankinfoDao;
import dfh.dao.GameinfoDao;
import dfh.dao.GuestbookDao;
import dfh.dao.LogDao;
import dfh.dao.OperatorDao;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TopicStatusDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.email.template.EmailTemplateHelp;
import dfh.exception.GenericDfhRuntimeException;
import dfh.icbc.getdata.dao.impl.GetdataDao;
import dfh.icbc.quart.fetch.AGSlotThread;
import dfh.model.bean.AgSlotXima;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.GPI4Xima;
import dfh.model.bean.QT4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.model.enums.BusinessProposalType;
import dfh.model.enums.ConcertDateType;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.SystemLogType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.remote.RemoteCaller;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.AgRecordUtils;
import dfh.utils.Arith;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.NumericUtil;
import dfh.utils.Page;
import dfh.utils.PagenationUtil;
import dfh.utils.SSLMailSender;
import dfh.utils.SlotUtil;
import dfh.utils.StringUtil;

public class ProposalServiceImpl extends UniversalServiceImpl implements ProposalService {

	private static Logger log = Logger.getLogger(ProposalServiceImpl.class);
	private LogDao logDao;
	private OperatorDao operatorDao;
	private ProposalDao proposalDao;
	private SeqDao seqDao;
	private TaskDao taskDao;
	private TradeDao tradeDao;
	private UserDao userDao;
	private GameinfoDao gameinfoDao;
	private BankinfoDao bankinfoDao;
	private SSLMailSender mailSender;
	private NotifyService notifyService;
	private GuestbookDao guestbookDao;
	private GetdataDao getDataDao;
	private TopicStatusDao topicStatusDao;

	
	
	public TopicStatusDao getTopicStatusDao() {
		return topicStatusDao;
	}

	public void setTopicStatusDao(TopicStatusDao topicStatusDao) {
		this.topicStatusDao = topicStatusDao;
	}

	public GetdataDao getGetDataDao() {
		return getDataDao;
	}

	public void setGetDataDao(GetdataDao getDataDao) {
		this.getDataDao = getDataDao;
	}

	public SSLMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(SSLMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public GameinfoDao getGameinfoDao() {
		return gameinfoDao;
	}

	public void setGameinfoDao(GameinfoDao gameinfoDao) {
		this.gameinfoDao = gameinfoDao;
	}

	
	public BankinfoDao getBankinfoDao() {
		return bankinfoDao;
	}

	public void setBankinfoDao(BankinfoDao bankinfoDao) {
		this.bankinfoDao = bankinfoDao;
	}

	public ProposalServiceImpl() {
	}
	
	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public String addCashin(String proposer, String loginname, String aliasName, String title, String from, Double money,
			String corpBankName, String remark, String accountNo,String bankaccount,String saveway,Double fee) throws GenericDfhRuntimeException {
		log.info("add Cashin proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		// 提款必须大于100元
		if (money < 1.0) {
			msg = "1元以上才能存款";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.CASHIN)) {
			msg = "该用户已提交过存款提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.CASHIN);
				Cashin cashin = new Cashin(pno, user.getRole(), user.getLoginname(), aliasName, money, accountNo, corpBankName, remark);
				cashin.setFee(fee);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHIN.getCode(), user.getLevel(), loginname,
						money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				proposal.setBankaccount(bankaccount);
				proposal.setSaveway(saveway);
				save(cashin);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
//				taskDao.generateTasksForCashin(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	@Override
	public String addQrcode(String agent,String recommendCode,String address,String operator,String remark,String code) {
		log.info("add agent groupName:" + agent + " recommendCode:" + recommendCode
				+ " address:" + address + " remark:" + remark+ " qrcode:" + code);
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Qrcode.class);
			if (!StringUtils.isEmpty(agent)) {
				dc.add(Restrictions.eq("agent", agent));
			}
			if (!StringUtils.isEmpty(recommendCode)) {
				dc.add(Restrictions.eq("recommendCode", recommendCode));
			}
			List<Qrcode> list = this.proposalDao.findByCriteria(dc);
			if (list.size() > 0) {
				Qrcode qrcode = list.get(0);
				qrcode.setAddress(address);
				qrcode.setUpdateoperator(operator);
				qrcode.setRemark(remark);
				qrcode.setQrcode(code);
				this.proposalDao.saveOrUpdate(qrcode);
			} else {
				Qrcode qrcode = new Qrcode();
				qrcode.setAddress(address);
				qrcode.setRemark(remark);
				qrcode.setQrcode(code);
				if (!StringUtils.isEmpty(agent)) {
					qrcode.setAgent(agent);
				}
				if (!StringUtils.isEmpty(recommendCode)) {
					qrcode.setRecommendCode(recommendCode);
				}
				qrcode.setUpdateoperator(operator);
				this.proposalDao.save(qrcode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "系统异常";
		}
		return null;
	}
	@Override
	public String importData(File file, String operator)  {
		List<String> typeList = Arrays.asList(new String[] { "1", "2" });
		InputStream stream = null;
		Workbook wb = null;
		Sheet sheet = null;
		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
		} catch (Exception e) {
			e.printStackTrace();
			return "[提示]读取文件失败！";
		}
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		Date currentDate = new Date();
		if (StringUtils.isBlank(operator)) {
			return "[提示]请登录后再进行操作！";
		}
		try {
			for (int i = 1; i < rows; i++) {//第一行是标题行
				ConstraintAddressConfig constraintAddressConfig = new ConstraintAddressConfig();
				Cell c = sheet.getCell(0, i);
				String typeId = c.getContents();
				if (c.getType() == CellType.LABEL) {
					LabelCell labelc00 = (LabelCell) c;
					typeId = labelc00.getString();
				}
				if (StringUtils.isBlank(typeId)) {
					throw new RuntimeException("[提示]配置的约束类型不允许为空！");
				}
				if("拒绝IP".equals(typeId) == true) {
					constraintAddressConfig.setTypeName("拒绝IP");
					typeId= "2";
				}else if("通过IP".equals(typeId) == true) {
					constraintAddressConfig.setTypeName("通过IP");
					typeId= "1";
				}else {
					constraintAddressConfig.setTypeName("拒绝地区");
					typeId= "3";
				}
				constraintAddressConfig.setTypeId(typeId);
				c = sheet.getCell(1, i);
				String value = c.getContents();
				constraintAddressConfig.setValue(value);
				if (StringUtils.isBlank(value)) {
					throw new RuntimeException("[提示]配置的约束项值不允许为空！");
				}
				if (StringUtils.isNotBlank(typeId)) {
					if (typeList.contains(constraintAddressConfig.getTypeId())) {
						if (constraintAddressConfig.getValue().length() < 7 || constraintAddressConfig.getValue().length() > 15 || StringUtils.isBlank(constraintAddressConfig.getValue() )) {
							throw new RuntimeException("[提示]配置的约束项值IP地址长度不正确！");
						}
						String reg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
								+ "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
						Pattern pattern = Pattern.compile(reg);
						Matcher matcher = pattern.matcher(constraintAddressConfig.getValue());
						if(!matcher.matches() ) {
							throw new RuntimeException("[提示]配置的约束项值不是一个有效的IP地址！");
						}
					}
				}
				c = sheet.getCell(2, i);
				String remark = c.getContents();
				DetachedCriteria dc = DetachedCriteria.forClass(ConstraintAddressConfig.class);
				dc.add(Restrictions.eq("typeId", constraintAddressConfig.getTypeId()));
				dc.add(Restrictions.eq("value", constraintAddressConfig.getValue()));
				dc.add(Restrictions.eq("deleteFlag", "1"));
				List list = this.findByCriteria(dc);
				if (null != list && !list.isEmpty()) {
					throw new RuntimeException("[提示]已存在该配置，请勿重复配置！");
				}
				constraintAddressConfig.setRemark(remark);
				constraintAddressConfig.setDeleteFlag("1");
				constraintAddressConfig.setCreatedUser(operator);
				constraintAddressConfig.setCreateTime(currentDate);
				constraintAddressConfig.setUpdatedUser(operator);
				constraintAddressConfig.setUpdateTime(currentDate);
				this.save(constraintAddressConfig);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("EXCEL解析错误：", e);
			if(e.getMessage().length() > 100) {
				throw new RuntimeException("数据库sql执行异常");
			}else {
				throw new RuntimeException(e.getMessage());
			}

		} finally {
			try {
				wb.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("文件关闭------------>error");
			}
			file.deleteOnExit();
		}
		return null;
	}

	//平博体育洗码
	public String PbXiMA(List<Bean4Xima> betsList) {
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		for (Bean4Xima item : betsList) {
			if(item.getUserName().startsWith("ufa") || item.getUserName().startsWith("mzc") || item.getUserName().startsWith("loh")){
				item.setUserName(item.getUserName().substring(3,item.getUserName().length()));
			}else{
				item.setUserName(item.getUserName().substring(2,item.getUserName().length()));
			}
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}
			//体育平台同意为0.4%
			double newrate = 0.004;

			Double rebateLimit = getRebateLimit(user);

			Double mgBet = item.getBetAmount();//投注额
			XimaVO ximaObject = new XimaVO(mgBet, user.getLoginname(), newrate,rebateLimit);

			/***********************************/
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
//			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.MGSELFXIMA, newrate, user.getAgrebate()) ;
			/***********************************/
			String remark = "PB系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);

			Double netWin = item.getProfit();//纯赢（纯输）
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), 0-netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户PB平台输赢值");
			agProfit.setPlatform("pb");
			agProfit.setBettotal(mgBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}





	public String addSwFishBatchXimaProposal(List<Bean4Xima> ptData) {

		Date starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1 * 24 - 0));
		Date endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		if (ptData == null || ptData.size() <= 0 || ptData.get(0) == null) {
			return "数据为空！";
		}
		try {
			String dateStr = DateUtil.formatMMddHH(new Date());
			for (Bean4Xima pt : ptData) {
				String loginName = pt.getUserName();

				pt.setProfit(pt.getBetAmount()-pt.getProfit()); //输赢值

				Users user = (Users) this.getUserDao().get(Users.class, loginName);
				if (user == null) {
					log.info("用户：" + loginName + "，不存在");
					continue;
				}
				double newrate = 0.004;

				Double ptrebate = this.getRebateLimit(user);

				// pt的自助洗码是昨天一天
				starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -24);
				endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

				// ************************************************
				XimaVO ximaObjectTiger = new XimaVO(pt.getBetAmount(), user.getLoginname(), newrate);
				// ******************ptsky去掉自助洗码*********************/
				// ximaObjectTiger = cutSelfXima(ximaObjectTiger, starttime, endtime, ProposalType.PTSKYSELFXIMA, newrate, ptrebate);
				// ******************end*********************/
				String remark = "SWFISH系统洗码";

				String pno = ProposalType.XIMA.getCode() + "f" + dateStr + RandomStringUtils.randomAlphanumeric(10);
				log.info("正在处理提案号swfish：" + pno + ",反水金额：" + Math.round(ximaObjectTiger.getXimaAmouont() * 100.00) / 100.00 + "...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObjectTiger.getValidBetAmount(), ximaObjectTiger.getXimaAmouont(), DateUtil.convertToTimestamp(starttime),
						DateUtil.convertToTimestamp(endtime), ximaObjectTiger.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObjectTiger.getXimaAmouont(), user.getAgent(),
						ProposalFlagType.AUDITED.getCode(), Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.userDao.save(xima);
				this.proposalDao.save(proposal);
				AgProfit agProfit = new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), pt.getProfit(), user.getAgent(),
						ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户SWFISH平台输赢值");
				agProfit.setPlatform("swfish");
				agProfit.setBettotal(pt.getBetAmount());
				this.proposalDao.save(agProfit);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public String addConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit,
			String payType, String remark) throws GenericDfhRuntimeException {
		/*log.info("add Cashout proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotCancledProposal(loginname, ProposalType.CONCESSIONS)) {
			msg = "已经提交或曾经申请过开户优惠提案";
			return msg;
		}

		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.CONCESSIONS);
			try {
				Concessions concessions = new Concessions(pno, user.getRole(), loginname, payType, firstCash, tryCredit, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CONCESSIONS.getCode(), user.getLevel(),
						loginname, tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(concessions);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}*/
		return "已屏蔽";
	}

	/**
	 * 转账优惠
	 */
	public String addBankTransferCons(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit,
			String payType, String remark) throws GenericDfhRuntimeException {
		/*log.info("add BankTransferCons proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.BANKTRANSFERCONS)) {
			msg = "该用户已提交过转账优惠提案，尚未审批完";
			return msg;
		}
		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.BANKTRANSFERCONS);
			try {
				Banktransfercons cons = new Banktransfercons(pno, user.getRole(), loginname, payType, firstCash, tryCredit, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.BANKTRANSFERCONS.getCode(), user.getLevel(),
						loginname, tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(cons);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}*/
		return "已屏蔽";
	}

	public String addNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName, String phone,
			String email, String role, String remark) {
		log.info("add NewAccount proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		if (user != null) {
			msg = "该用户已存在";
			return msg;
		}
		if (UserRole.MONEY_CUSTOMER.getCode().equals(title)
				&& !(loginname.startsWith(Constants.PREFIX_MONEY_CUSTOMER) || loginname.startsWith(Constants.PREFIX_PARTNER_SUBMEMBER)))
			msg = UserRole.MONEY_CUSTOMER.getText() + "应以" + Constants.PREFIX_MONEY_CUSTOMER + "或" + Constants.PREFIX_PARTNER_SUBMEMBER
					+ "开头";
		else if (UserRole.AGENT.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_AGENT))
			msg = UserRole.AGENT.getText() + "应以" + Constants.PREFIX_AGENT + "开头";
		// else if (UserRole.PARTNER.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_PARTNER))
		// msg = UserRole.PARTNER.getText() + "应以" + Constants.PREFIX_PARTNER + "开头";
		if (msg != null)
			return msg;
		/*if (proposalDao.existNotFinishedProposal(loginname, ProposalType.NEWACCOUNT)) {
			msg = "已提交过该帐号的开户提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.NEWACCOUNT);
				Newaccount newAccount = new Newaccount(pno, role, loginname, pwd, phone, email, aliasName, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.NEWACCOUNT.getCode(), VipLevel.COMMON
						.getCode(), loginname, null, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(newAccount);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}*/
		return "已屏蔽";
	}

	public String addReBankInfo(String proposer, String loginname, String title, String from, String accountName, String accountNo,
			String accountType, String bank, String accountCity, String bankAddress, String ip, String remark)
			throws GenericDfhRuntimeException {
		log.info("add reBankInfo proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		/*if (proposalDao.existNotFinishedProposal(loginname, ProposalType.REBANKINFO)) {
			msg = "该用户已提交过银改提案，尚未处理完";
			return msg;
		}
		if (get(Accountinfo.class, loginname) == null) {
			msg = "客户尚未完成银行资料";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.REBANKINFO);
				Rebankinfo rebankinfo = new Rebankinfo(pno, user.getRole(), loginname, accountName, accountNo, bank, accountType,
						accountCity, bankAddress, ip, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.REBANKINFO.getCode(), user.getLevel(),
						loginname, null, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(rebankinfo);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}*/
		return "已屏蔽";
	}

	public String addXima(String proposer, String loginname, String title, String from, Date startTime, Date endTime, Double firstCash,
			Double rate, String payType, String remark) throws GenericDfhRuntimeException {
		log.info("add Xima proposal");
		String msg = null;

		if (startTime != null && endTime.before(startTime)) {
			msg = "结算结束时间必须大于结算开始时间";
			return msg;
		}

		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
//		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.XIMA)) {
//			msg = "已提交过洗码优惠提案，尚未处理完";
//			return msg;
//		}
		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.XIMA);
			try {
				Double tryCredit = Double.valueOf(Math.abs(firstCash.doubleValue()) * Math.abs(rate.doubleValue()));
				// 洗码最多5W
				if (tryCredit > 50000)
					tryCredit = 50000.0;
				Xima xima = new Xima(pno, user.getRole(), loginname, payType, firstCash, tryCredit, DateUtil.convertToTimestamp(startTime),
						DateUtil.convertToTimestamp(endTime), rate, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname,
						tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(xima);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	public String addPrize(String proposer, String loginname, String title, String from, Date startTime, Date endTime, Double firstCash,
			Double rate, String payType, String remark) throws GenericDfhRuntimeException {
		log.info("add Xima proposal");
		String msg = null;

		if (startTime != null && endTime.before(startTime)) {
			msg = "结算结束时间必须大于结算开始时间";
			return msg;
		}

		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.PRIZE)) {
			msg = "已提交过幸运抽奖提案，尚未处理完";
			return msg;
		}
		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.PRIZE);
			try {
				Double tryCredit = Double.valueOf(Math.abs(firstCash.doubleValue()) * Math.abs(rate.doubleValue()));
				Xima xima = new Xima(pno, user.getRole(), loginname, payType, firstCash, tryCredit, DateUtil.convertToTimestamp(startTime),
						DateUtil.convertToTimestamp(endTime), rate, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.PRIZE.getCode(), user.getLevel(), loginname,
						tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, remark, null);
				save(xima);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	public String audit(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		log.info("提案号："+proposal.getPno()+"--flag:"+proposal.getFlag());
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue())
			msg = "该提案不是待审核状态";
		else
			try {
				proposal.setFlag(ProposalFlagType.AUDITED.getCode());
				proposal.setRemark("审核:" + StringUtils.trimToEmpty(remark));
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
				/*if(10000>=proposal.getAmount()){
					proposal.setFlag(ProposalFlagType.SUBMITED.getCode());
					proposal.setMsflag(1);
					proposal.setRemark("审核:1万以下秒提!"+StringUtils.trimToEmpty(remark));
				}*/
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	@Override
	public String auditS(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue())
			msg = "该提案不是待审核状态";
		else
			try {
				if(50000 < proposal.getAmount()){
					return "必须低于或者等于5万人民币！才能使用秒提！";
				}
				proposal.setMsflag(1);
				proposal.setMssgflag(1);
				proposal.setPassflag(1);
				proposal.setRemark("审核:" + StringUtils.trimToEmpty(remark)+";且提交秒付");
				update(proposal);
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String auditMsProposal(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		//System.out.println(proposal.getType());
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.EXCUTED.getCode().intValue() && proposal.getPassflag().intValue() !=0)
			msg = "该提案不是待审核状态";
		else
			try {
				//proposal.setFlag(ProposalFlagType.AUDITED.getCode());
				String newremark= remark.split("※")[0];
				String flag=remark.split("※")[1];
				String result = "";
				if(flag.equals("1")){
					result="正常";
				}
				if(flag.equals("-1")){
					result="非正常";
					String loginname = proposal.getLoginname();
					Userstatus userstatus = (Userstatus) get(Userstatus.class,loginname, LockMode.UPGRADE);
					userstatus.setTouzhuflag(1);
				}
				proposal.setRemark("审核:"+result+"         " + StringUtils.trimToEmpty(newremark));
				proposal.setPassflag(Integer.parseInt(remark.split("※")[1]));
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String secondProposal(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		//System.out.println(proposal.getType());
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.EXCUTED.getCode().intValue() && proposal.getPassflag().intValue() !=0)
			msg = "该提案不是待审核状态";
		else
			try {
				proposal.setRemark("自助洗码;秒反水;executed;审核:成功! 备注："+remark);
				proposal.setPassflag(1);
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String submitYhjProposal(String pno, String operator, String ip, String loginName){
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		//System.out.println(proposal.getType());
		if (proposal == null){
			return "找不到该提案";
		}
		if(ProposalFlagType.SUBMITED.getCode().intValue()==proposal.getFlag()){
			try {
				if(loginName!=null && !loginName.equals("")){
//					Users user=(Users) get(Users.class, loginName, LockMode.UPGRADE);
					Users user=(Users) get(Users.class, loginName);
					if(user==null){
						return "该用户不存在";
					}
					taskDao.auditTask(pno, operator, ip);
					logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
					Map<String,String> params = new  HashMap<String,String>();
					if(proposal.getType()!=537){
						String updateId = seqDao.generateZlxID();
						proposal.setLoginname(loginName);
						proposal.setQuickly(user.getLevel());
						if(proposal.getType()==531){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "10%存送优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的10%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==532){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "20%存送优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的20%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==533){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "30%存送优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的30%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==534){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "40%存送优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的40%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==535){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "50%存送优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的50%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==536){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "红包优惠劵代码");
							params.put("content", "尊敬的龙都客户，您的红包优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==571){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "PT红包优惠劵代码");
							params.put("content", "尊敬的龙都客户，您的PT红包优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==572){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "PT68%存送优惠劵代码");
							params.put("content", "尊敬的龙都客户，您的PT68%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==573){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "PT100%存送优惠劵代码");
							params.put("content", "尊敬的龙都客户，您的PT100%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						}else if(proposal.getType()==574){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "PT88%存送优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的PT88%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！"); 
							
						}else if(proposal.getType()==581){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "188体育红包优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的188体育红包优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！"); 
							
						}else if(proposal.getType()==582){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "188体育存2900送580"); 
							params.put("content", "尊敬的龙都客户，您的188体育存2900送580优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！"); 
							
						}else if(proposal.getType()==584){
							params.put("receiveUname", loginName);
							params.put("ip", ip); 
							params.put("title", "188体育20%存送优惠劵代码"); 
							params.put("content", "尊敬的龙都客户，您的188体育20%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！"); 
						}else if (proposal.getType() == 430 || proposal.getType() == 433) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "MG100%存送优惠券", "MG100%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 431) {
							params = this.generateGuestbookForCoupon(proposal, ip, "MG88%存送优惠券", "MG88%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 432) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "MG68%存送优惠券", "MG68%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 434||proposal.getType() == 437) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "DT100%存送优惠券", "DT100%存送优惠券代码为：", "客服管理员");

						}else if (proposal.getType() == 435) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "DT88%存送优惠券", "DT88%存送优惠券代码为：", "客服管理员");

						}else if (proposal.getType() == 436) {
							params = this.generateGuestbookForCoupon(proposal, ip, "DT68%存送优惠券", "DT68%存送优惠券代码为：", "客服管理员");

						}else if (proposal.getType() == 409 || proposal.getType() == 412) {
							params = this.generateGuestbookForCoupon(proposal, ip, "PT100%存送优惠券", "PT100%存送优惠券代码为：", "客服管理员");

						}else if (proposal.getType() == 410) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "PT88%存送优惠券", "PT88%存送优惠券代码为：", "客服管理员");

						}else if (proposal.getType() == 411) {
							params = this.generateGuestbookForCoupon(proposal, ip, "PT68%存送优惠券", "PT68%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 419) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "红包优惠券派发", "红包优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 401 || proposal.getType() == 404) {
							params = this.generateGuestbookForCoupon(proposal, ip, "TTG100%存送优惠券", "TTG100%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 402) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "TTG88%存送优惠券", "TTG88%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 403) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "TTG68%存送优惠券", "TTG68%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 422 || proposal.getType() == 425) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "NT100%存送优惠券", "NT100%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 423) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "NT88%存送优惠券", "NT88%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 424) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "NT68%存送优惠券", "NT68%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 426 || proposal.getType() == 429) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "QT100%存送优惠券", "QT100%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 427) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "QT88%存送优惠券", "QT88%存送优惠券代码为：", "客服管理员");
						}else if (proposal.getType() == 428) {
							params =  this.generateGuestbookForCoupon(proposal, ip, "QT68%存送优惠券", "QT68%存送优惠券代码为：", "客服管理员");
						}
						//保存站内信
						saveTopicStatus(params);
					}
					proposal.setFlag(ProposalFlagType.AUDITED.getCode());
					update(proposal);
				}else{
					taskDao.auditTask(pno, operator, ip);
					logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
					proposal.setFlag(ProposalFlagType.AUDITED.getCode());
					update(proposal);
				}
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return "失败";
			}
		}else{
			return "该提案不是待审核状态";
		}
	}
	
	private Map<String,String> generateGuestbookForCoupon(Proposal proposal, String ip, String title, String contentPart, String adminName){
		Map<String,String> params = new HashMap<String,String>();
		params.put("receiveUname", proposal.getLoginname());
		params.put("ip", ip); 
		params.put("title", title);
		if(proposal.getType() == 419){
			params.put("content","尊敬的龙都客户，您的" + contentPart + proposal.getShippingCode() + "，请您手机登录龙都官网或APP——自助优惠——红包优惠券进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
		} else {
			params.put("content","尊敬的龙都客户，您的" + contentPart + proposal.getShippingCode() + "，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
		}
		return params;
	}
	
//	private Guestbook generateGuestbookForCoupon(Proposal proposal, String ip, String title, String contentPart, String adminName){
//		Guestbook guestBook = new Guestbook();
//		guestBook.setUsername(proposal.getLoginname());
//		guestBook.setFlag(0);//
//		guestBook.setIpaddress(ip);
//		guestBook.setCreatedate(new Timestamp(new Date().getTime()));//
//		guestBook.setTitle(title);
//		if(proposal.getType() == 419){
//			guestBook.setContent("尊敬的龙都客户，您的" + contentPart + proposal.getShippingCode() + "，请您手机登录龙都官网或APP——自助优惠——红包优惠券进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
//		} else {
//			guestBook.setContent("尊敬的龙都客户，您的" + contentPart + proposal.getShippingCode() + "，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
//		}
//		guestBook.setAdminstatus(1);//
//		guestBook.setUserstatus(0);//
//		guestBook.setUpdateid("");//
//		guestBook.setIsadmin(0);//
//		guestBook.setAdminname(adminName);
//		return guestBook;
//	}
	
	public String saveTopicStatus(Map<String, String> params) {
		String msg = "";
		try {
			TopicStatus topicStatus = new TopicStatus();
			topicStatus.setCreateTime(new Timestamp(new Date().getTime()));
			topicStatus.setTopicId(-1);
			topicStatus.setIpAddress(params.get("ip"));// 发送者ip地址
			topicStatus.setIsUserRead(0);
			topicStatus.setReceiveUname(params.get("receiveUname"));
			topicStatus.setContent(params.get("content"));
			topicStatus.setTitle(params.get("title"));
			topicStatusDao.save(topicStatus);
		} catch (Exception e) {
			e.printStackTrace();
			return msg;
		}
		return "成功!";
	}
	
	
	public String submitYhjCancel(String pno, String operator, String ip, String loginName){
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null){
		    msg = "找不到该提案";
		}
		try {
			proposal.setFlag(ProposalFlagType.CANCLED.getCode());
			taskDao.auditTask(pno, operator, ip);
			logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
			update(proposal);
			msg = null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String excuteCommission(String loginname, String operator, Integer year,Integer month, String remark) throws GenericDfhRuntimeException{
		String msg = null;
		try{
			DetachedCriteria dc = DetachedCriteria.forClass(Commissions.class);
			dc = dc.add(Restrictions.eq("id.year", year));
			dc = dc.add(Restrictions.eq("id.month", month));
			dc = dc.add(Restrictions.eq("id.loginname", loginname));
			List<Commissions> commissions=this.findByCriteria(dc);
			if(commissions==null || commissions.size()==0){
				msg = "找不到该佣金记录";
			}else{
				Commissions commission=commissions.get(0);
				if(commission.getFlag()==1){
					this.getTradeDao().changeCredit(commission.getId().getLoginname(),commission.getAmount(), CreditChangeType.COMMISSION.getCode(),
							commission.getId().getLoginname()+commission.getId().getYear()+commission.getId().getMonth(), "ok");
					commission.setFlag(0);
					commission.setRemark(remark);
					this.update(commission);
				}else{
					msg = "该佣金记录已经执行";
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String auditBusinessProposal(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		BusinessProposal proposal = (BusinessProposal) get(BusinessProposal.class, pno, LockMode.UPGRADE);
		//System.out.println(proposal.getType());
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue())
			msg = "该提案不是待审核状态";
		else
			try {
				proposal.setFlag(ProposalFlagType.AUDITED.getCode());
				proposal.setRemark(proposal.getRemark()+"          "+"审核:" + StringUtils.trimToEmpty(remark));
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String cancle(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		//取消操作，更改秒付宝订单记录为失败
		FPayorder fpayorder = (FPayorder) get(FPayorder.class, pno);
		Users users = (Users)get(Users.class, proposal.getLoginname());
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue()
				|| proposal.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue())// update sun
			msg = "该提案已取消或已执行";
		else if (proposal.getWhereisfrom().equals(Constants.FROM_FRONT)&&proposal.getType().intValue()==ProposalType.CASHIN.getCode().intValue()) {
			try {
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, operator, ip);
				Userstatus userstatus = (Userstatus)get(Userstatus.class, proposal.getLoginname());
				if(userstatus  == null){
					userstatus = new Userstatus();
					userstatus.setLoginname(proposal.getLoginname());
					userstatus.setCashinwrong(1);
					userstatus.setMailflag(1);
					userstatus.setLoginerrornum(0);
					save(userstatus);
				}else{
					userstatus.setCashinwrong(userstatus.getCashinwrong()+1);
					if(userstatus.getCashinwrong()>=3){
						users.setFlag(1);
						users.setRemark("多次提交存款错误");
						update(users);
					}
				}
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		else
			try {
				if (proposal.getType().intValue() == ProposalType.CASHOUT.getCode().intValue() || proposal.getType().intValue() == ProposalType.PROXYADVANCE.getCode().intValue()){
					if(StringUtils.isNotBlank(proposal.getSaveway()) && proposal.getSaveway().equals("slotmachine")){
						getDataDao.changeAgentSlotCreditBySql(users, Double.valueOf(Math.abs(proposal.getAmount().doubleValue())),
								CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(operator).toString());
					}else{
						tradeDao.changeCredit(proposal.getLoginname(), Double.valueOf(Math.abs(proposal.getAmount().doubleValue())),
								CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(operator).toString());
					}
				}
				
				//如果提案为五分钟提款，将秒付宝订单号改成失败
				if(fpayorder != null && proposal.getMsflag()==0){
					fpayorder.setFlag(-1);
					update(fpayorder);
				}
				
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, operator, ip);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String canclemscashoutproposal(String pno, String operator, String ip, String remark,Integer unknown) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue()
				|| proposal.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue())// update sun
			msg = "该提案已取消或已执行";
		else
			try {
				if (proposal.getType().intValue() == ProposalType.CASHOUT.getCode().intValue() || proposal.getType().intValue() == ProposalType.PROXYADVANCE.getCode().intValue())
					tradeDao.changeCredit(proposal.getLoginname(), Double.valueOf(Math.abs(proposal.getAmount().doubleValue())),
							CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(operator).toString());
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, operator, ip);
				
				proposal.setMstype(1);
				proposal.setUnknowflag(unknown);
				update(proposal);
				
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String cancleBusinessProposal(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		BusinessProposal proposal = (BusinessProposal) get(BusinessProposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue()
				|| proposal.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue())// update sun
			msg = "该提案已取消或已执行";
		else
			try {
				
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, operator, ip);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String cashinCancel(String pno, String operator, String ip,String remark)throws GenericDfhRuntimeException{
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue()
				|| proposal.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue())// update sun
			msg = "该提案已取消或已执行";
		else if (proposal.getWhereisfrom().equals(Constants.FROM_FRONT)&&proposal.getType().intValue()==ProposalType.CASHIN.getCode().intValue()) {
			try {
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, operator, ip);
				
				CashinRecord cashinRecord = (CashinRecord)get(CashinRecord.class, pno,LockMode.UPGRADE);
				cashinRecord.setFlag(-1);
				cashinRecord.setOperator(operator);
				update(cashinRecord);
				
				Userstatus userstatus = (Userstatus)get(Userstatus.class, proposal.getLoginname());
				if(userstatus  == null){
					userstatus = new Userstatus();
					userstatus.setLoginname(proposal.getLoginname());
					userstatus.setCashinwrong(1);
					userstatus.setMailflag(1);
					userstatus.setLoginerrornum(0);
					save(userstatus);
				}else{
					userstatus.setCashinwrong(userstatus.getCashinwrong()+1);
					if(userstatus.getCashinwrong()>=3){
						Users users = (Users)get(Users.class, proposal.getLoginname());
						users.setFlag(1);
						users.setRemark("多次提交存款错误");
						update(users);
					}
				}
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}
	public String clientCancle(String pno, String loginname, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue())
			msg = "该提案已取消或已执行或已审核";
		else
			try {
				if (proposal.getType().intValue() == ProposalType.CASHOUT.getCode().intValue())
					tradeDao.changeCredit(proposal.getLoginname(), Double.valueOf(Math.abs(proposal.getAmount().doubleValue())),
							CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(loginname).toString());
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, loginname, ip);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String excute(String pno, String operator, String ip, String remark,String bankinfoid,Double fee) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.AUDITED.getCode().intValue()) {
			msg = "该提案不是待执行状态";
		} else {
			Integer type = proposal.getType();
			proposal.setRemark(proposal.getRemark() + ";executed:" + StringUtils.trimToEmpty(remark));
			
			if (type.intValue() == ProposalType.CASHIN.getCode().intValue()) {
				if(proposal.getWhereisfrom().equals(Constants.FROM_FRONT)){
					Userstatus status = (Userstatus)get(Userstatus.class, proposal.getLoginname());
					if(status != null){
						if(status.getCashinwrong()!=null&&status.getCashinwrong()!=0){
							status.setCashinwrong(0);
							update(status);
						}
					}
				}
				Cashin cashin = (Cashin) get(Cashin.class, pno, LockMode.UPGRADE);
				if (cashin == null)
					throw new GenericDfhRuntimeException("存款提案记录不存在");
				tradeDao.changeCredit(cashin.getLoginname(), cashin.getMoney(), CreditChangeType.CASHIN.getCode(), pno, remark);
				//Double transferConsMoney = Constants.getTransferConsMoney(cashin.getFee());
				Double transferConsMoney = cashin.getFee();
				if (transferConsMoney > 0)
					tradeDao.changeCredit(cashin.getLoginname(), transferConsMoney, CreditChangeType.BANK_TRANSFER_CONS.getCode(), null,
							"系统自动添加");

				// 设置用户 isCashin字段
				userDao.setUserCashin(proposal.getLoginname());
				try {
					String smsmsg = this.notifyService.sendSMSByProposal(proposal);
					log.info(smsmsg);
				} catch (Exception e) {
					log.error("提案补单补单发送短信失败：", e);
				}
			} /*else if (type.intValue() == ProposalType.CONCESSIONS.getCode().intValue()) {
				Concessions concessions = (Concessions) get(Concessions.class, pno);
				if (concessions == null)
					throw new GenericDfhRuntimeException("首存优惠提案记录不存在");
				tradeDao.changeCredit(concessions.getLoginname(), Math.abs(concessions.getTryCredit()), CreditChangeType.FIRST_DEPOSIT_CONS
						.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.BANKTRANSFERCONS.getCode().intValue()) {
				Banktransfercons cons = (Banktransfercons) get(Banktransfercons.class, pno);
				if (cons == null)
					throw new GenericDfhRuntimeException("转账优惠提案记录不存在");
				tradeDao.changeCredit(cons.getLoginname(), Math.abs(cons.getTryCredit()), CreditChangeType.BANK_TRANSFER_CONS.getCode(),
						pno, remark);
			} else if (type.intValue() == ProposalType.REBANKINFO.getCode().intValue()) {
				Rebankinfo rebankinfo = (Rebankinfo) get(Rebankinfo.class, pno);
				if (rebankinfo == null)
					throw new GenericDfhRuntimeException("银改提案记录不存在");
				Accountinfo accountinfo = (Accountinfo) get(Accountinfo.class, rebankinfo.getLoginname());
				accountinfo.setAccountCity(rebankinfo.getAccountCity());
				accountinfo.setAccountName(rebankinfo.getAccountName());
				accountinfo.setAccountNo(rebankinfo.getAccountNo());
				accountinfo.setAccountType(rebankinfo.getAccountType());
				accountinfo.setBank(rebankinfo.getBank());
				accountinfo.setBankAddress(rebankinfo.getBankAddress());
				accountinfo.setLastModifyTime(DateUtil.now());
				update(accountinfo);
			}*/ else if (type.intValue() == ProposalType.XIMA.getCode().intValue()) {
				Xima xima = (Xima) get(Xima.class, pno);
				if (xima == null)
					throw new GenericDfhRuntimeException("洗码优惠提案记录不存在");
				// userDao.setLevel(xima.getLoginname(), VipLevel.BAIJIN.getCode(), Constants.DEFAULT_OPERATOR);
				tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
						CreditChangeType.XIMA_CONS.getCode(), pno, remark);
				
				
			}else if (type.intValue()==ProposalType.SELFXIMA.getCode()) {
				Xima xima = (Xima) get(Xima.class, pno);
				if (xima == null)
					throw new GenericDfhRuntimeException("洗码优惠提案记录不存在");
				// userDao.setLevel(xima.getLoginname(), VipLevel.BAIJIN.getCode(), Constants.DEFAULT_OPERATOR);
				tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
						CreditChangeType.XIMA_CONS.getCode(), pno, remark);
				
				// 自助洗码正式上线后，这里需要添加如下函数：
				//System.out.println("正在执行...");
				/*try {
					gameinfoDao.updateXimaStatus(xima.getLoginname(), xima.getStartTime(), xima.getEndTime());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new GenericDfhRuntimeException(e.getMessage());
				}*/
				//System.out.println("已经执行");
				
			} else if (type.intValue() == ProposalType.PRIZE.getCode().intValue()) {
				Prize prize = (Prize) get(Prize.class, pno);
				if (prize == null)
					throw new GenericDfhRuntimeException("幸运抽奖提案记录不存在");
				tradeDao.changeCredit(prize.getLoginname(), Double.valueOf(Math.abs(prize.getTryCredit().doubleValue())),
						CreditChangeType.PRIZE.getCode(), pno, remark);
				
			} else if (type.intValue() == ProposalType.BIRTHDAY.getCode().intValue()) {
				Prize prize = (Prize) get(Prize.class, pno);
				if (prize == null)
					throw new GenericDfhRuntimeException("生日礼金提案记录不存在");
				tradeDao.changeCreditAgentSlot(prize.getLoginname(), Double.valueOf(Math.abs(prize.getTryCredit().doubleValue())),
						CreditChangeType.BIRTHDAY.getCode(), pno, remark);
			}else if (type.intValue() == ProposalType.LEVELPRIZE.getCode().intValue()) {
				Prize prize = (Prize) get(Prize.class, pno);
				if (prize == null)
					throw new GenericDfhRuntimeException("晋级礼金提案记录不存在");
				tradeDao.changeCredit(prize.getLoginname(), Double.valueOf(Math.abs(prize.getTryCredit().doubleValue())),
						CreditChangeType.LEVELPRIZE.getCode(), pno, remark);
			}else if (type.intValue() == ProposalType.PROFIT.getCode().intValue()) {
				/*Xima xima = (Xima) get(Xima.class, pno);
				if (xima == null)
					throw new GenericDfhRuntimeException("负盈利反赠提案记录不存在");
				tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
						CreditChangeType.PROFIT.getCode(), pno, remark);*/
				return "负盈利反赠提案由玩家自行处理";		
			} else if (type.intValue() == 516){
				Intransfer intransfer = (Intransfer)get(Intransfer.class, pno);
				if (intransfer == null)
					throw new GenericDfhRuntimeException("户内转账提案记录不存在");
				String[] fromto = intransfer.getFromto().split("※");
				String fromByTo = intransfer.getWherefrom()+"-->"+intransfer.getWhereto();
				
				bankinfoDao.changeBankInAmount(fromto[0], 1, 0, -(intransfer.getAmount()+intransfer.getFee()),fromByTo);
				bankinfoDao.changeBankInAmount(fromto[1], 0, 0, intransfer.getAmount(),fromByTo);

				intransfer.setTransferflag(1);//执行成功
				tradeDao.update(intransfer);
			}else if (type.intValue() == ProposalType.OFFER.getCode().intValue()) {
				Offer offer = (Offer) get(Offer.class, pno);
				if (offer == null)
					throw new GenericDfhRuntimeException("再存优惠提案记录不存在");
				tradeDao.changeCredit(offer.getLoginname(), Math.abs(offer.getMoney().doubleValue()),
						CreditChangeType.OFFER_CONS.getCode(), pno, remark);
				proposal.setRemark("BEGIN"+proposal.getLoginname());
			}
			try {
				String backremark = proposal.getRemark();
				Integer backflag = proposal.getFlag();
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				
				if(type.intValue() == ProposalType.XIMA.getCode()  || 
						type.intValue() == ProposalType.PRIZE.getCode()||
						type.intValue() == ProposalType.OFFER.getCode()||
						type.intValue() == ProposalType.BIRTHDAY.getCode()||
						type.intValue() == ProposalType.LEVELPRIZE.getCode() ){
					
						proposal.setExecuteTime(DateUtil.now());
				}
				
				if (type.intValue() == ProposalType.CASHOUT.getCode().intValue()||type.intValue() == ProposalType.PROXYADVANCE.getCode().intValue()) {
					Bankinfo bankinfo = (Bankinfo) get(Bankinfo.class, Integer.parseInt(bankinfoid), LockMode.UPGRADE);
					proposal.setBankaccount(bankinfo.getUsername());
					proposal.setExecuteTime(DateUtil.now());
					proposal.setTimecha(DateUtil.getTimecha(proposal.getCreateTime(), proposal.getExecuteTime()));
					proposal.setOvertime(DateUtil.getOvertime(proposal.getCreateTime(), proposal.getExecuteTime()));
					if(proposal.getMsflag()==1)
						proposal.setMstype(2);
					
					//如果选择了民生银行,那开始自助支付
					/*if(bankinfo.getRemark()!=null && !"".equals(bankinfo.getRemark().trim()) 
							&& bankinfo.getRemark().indexOf("秒付")!=-1 && 10000>=proposal.getAmount() 
							&& "民生银行".equals(bankinfo.getBankname()) &&
							2==bankinfo.getType() && 0==bankinfo.getUseable()){
						Const cons = (Const) userDao.get(Const.class, "民生银行秒付");
						if(cons!=null && cons.getValue().equals("0")){
							proposal.setFlag(backflag);
							proposal.setRemark(backremark);
							msg = "抱歉，秒付提款功能暂时关闭";
							return msg;
						}else{
							proposal.setMsflag(1);
							proposal.setFlag(ProposalFlagType.SUBMITED.getCode());
							return null;
						}
					}*/
					
					if(type.intValue() == ProposalType.CASHOUT.getCode().intValue()){
						bankinfoDao.changeAmount(bankinfoid, 2, 0, -(proposal.getAmount()+fee),proposal.getPno());
					}else{
						bankinfoDao.changeAmountTwo(bankinfoid, 2, 0, -(proposal.getAmount()+fee),proposal.getPno());
					}
					Userstatus status = (Userstatus)get(Userstatus.class, proposal.getLoginname());
					if(status != null){
						Integer touzhuFlag=status.getTouzhuflag();
						if(touzhuFlag!= null &&touzhuFlag==1){
							status.setTouzhuflag(0);
							update(status);
						}
					}
					
					//发送自动支付数据
					
				}else if (type.intValue() == ProposalType.CASHIN.getCode().intValue()){
					bankinfoDao.changeAmountByName(proposal.getBankaccount(), proposal.getAmount(),pno);
				}
				
				
				taskDao.excuteTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + pno);
				
				//取消操作，更改秒付宝订单记录为失败
				FPayorder fpayorder = (FPayorder) get(FPayorder.class, pno);
				//如果提案为五分钟提款，将秒付宝订单号改成失败
				if(fpayorder !=null && proposal.getMsflag()==0){
					fpayorder.setFlag(-1);
					update(fpayorder);
				}
				
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
			
			try {
				//发送存款和提款发送邮件通知功能，根据user表里锁字段address来判断是否需要发送  
				Users user=(Users)this.userDao.get(Users.class, proposal.getLoginname());
				if(user==null){
					return null;
				}
				String service=user.getAddress();
				if(null==service){
					service="";
				}
				if (type.intValue() == ProposalType.CASHIN.getCode().intValue()) {
						
					if(service.indexOf("10")!= -1){
						String html = EmailTemplateHelp.toHTML(Constants.EMAIL_CASHIN_BODY_HTML, new Object[]{user.getLoginname(),NumericUtil.double2String(proposal.getAmount()),DateUtil.formatDateForStandard(proposal.getCreateTime())});
						this.mailSender.sendmail(html, user.getEmail(), "存款通知 - e路发娱乐城");
					}
					if(service.indexOf("9")!= -1){//发短信
						notifyService.sendSms(user.getPhone(), "e路发客户"+proposal.getLoginname()+",你在"+proposal.getCreateTime()+"时提交的存款申请"+proposal.getAmount()+"元已经处理完毕,请查收,现帐号余额为"+user.getCredit()+"元,祝多多盈利");
					}	
				}else if (type.intValue() == ProposalType.CASHOUT.getCode().intValue()) {
					if(service.indexOf("6")!= -1){
						String html = EmailTemplateHelp.toHTML(Constants.EMAIL_CASHOUT_BODY_HTML, new Object[]{user.getLoginname(),NumericUtil.double2String(proposal.getAmount()),DateUtil.formatDateForStandard(proposal.getCreateTime())});
						this.mailSender.sendmail(html, user.getEmail(), "提款通知 - e路发娱乐城");
					}
					if(service.indexOf("5")!= -1){//发短信
						notifyService.sendSms(user.getPhone(), "e路发客户"+proposal.getLoginname()+",你在"+proposal.getCreateTime()+"时提交的提款申请"+proposal.getAmount()+"元已经付款完毕,请查收,现帐号余额为"+user.getCredit()+"元,祝多多盈利");
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e.getMessage(),e);
			}
			
		}
		return msg;
	}
	
	@Override
	public String excuteOffer(String pno, String operator, String ip) throws GenericDfhRuntimeException {
		log.info(ip+"--"+pno+"--"+operator+"---"+new Date()+"");
		Proposal proposal = (Proposal) get(Proposal.class, pno);
		proposal.setRemark("END"+proposal.getLoginname());
		update(proposal);
		return null;
	}
	
	public String executemscashoutproposal(String pno,String bankinfoid){
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else {
			try {
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				Bankinfo bankinfo = (Bankinfo) get(Bankinfo.class, Integer.parseInt(bankinfoid), LockMode.UPGRADE);
				proposal.setBankaccount(bankinfo.getUsername());
				proposal.setExecuteTime(DateUtil.now());
				proposal.setMstype(2);
				proposal.setUnknowflag(2);
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setTimecha(DateUtil.getTimecha(proposal.getCreateTime(), proposal.getExecuteTime()));
				proposal.setOvertime(DateUtil.getOvertime(proposal.getCreateTime(), proposal.getExecuteTime()));
				bankinfoDao.changeAmount(bankinfoid, 2, 0, -(proposal.getAmount()),pno);
				update(proposal);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return msg;
	}
	
	public String excuteBusinessProposal(String pno, String operator, String ip, String remark,String bankinfoid,Double fee,Double actualmoney,String file) throws GenericDfhRuntimeException {
		String msg = null;
		BusinessProposal proposal = (BusinessProposal) get(BusinessProposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.AUDITED.getCode().intValue()) {
			msg = "该提案不是待执行状态";
		} else {
			try {
				
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setRemark(proposal.getRemark() + ";executed:" + StringUtils.trimToEmpty(remark));
				Bankinfo bankinfo = (Bankinfo) get(Bankinfo.class, Integer.parseInt(bankinfoid), LockMode.UPGRADE);
				proposal.setBankaccount(bankinfo.getUsername());
				proposal.setExcattachment(file);
				//proposal.setBankaccountid(bankinfo.getType());
				proposal.setActualmoney(actualmoney);
				//12代表事务提案
				bankinfoDao.changeAmount(bankinfoid, 12, 0, -(proposal.getAmount()+fee),pno);
				
				update(proposal);
				taskDao.excuteTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
			
			 
			
		}
		return msg;
	}
	
	public String cashinExcute(String pno, String operator, String ip)throws GenericDfhRuntimeException{
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null){
			msg = "找不到该提案";
		}	
		else {
			
			Integer type = proposal.getType();
			
			if (type.intValue() == ProposalType.CASHIN.getCode().intValue()) {
				if(proposal.getWhereisfrom().equals(Constants.FROM_FRONT)){
					Userstatus status = (Userstatus)get(Userstatus.class, proposal.getLoginname());
					if(status != null){
						if(status.getCashinwrong()!=null&&status.getCashinwrong()!=0){
							status.setCashinwrong(0);
							update(status);
						}
					}
				}
				Cashin cashin = (Cashin) get(Cashin.class, pno, LockMode.UPGRADE);
				if (cashin == null)
					throw new GenericDfhRuntimeException("存款提案记录不存在");
				tradeDao.changeCredit(cashin.getLoginname(), cashin.getMoney(), CreditChangeType.CASHIN.getCode(), pno, "");
				Double transferConsMoney = Constants.getTransferConsMoney(cashin.getMoney());
				if (transferConsMoney > 0)
					tradeDao.changeCredit(cashin.getLoginname(), transferConsMoney, CreditChangeType.BANK_TRANSFER_CONS.getCode(), null,
							"系统自动添加");

				// 设置用户 isCashin字段
				userDao.setUserCashin(proposal.getLoginname());
				
				CashinRecord cashinRecord = (CashinRecord)get(CashinRecord.class, pno,LockMode.UPGRADE);
				cashinRecord.setFlag(1);
				cashinRecord.setOperator(operator);
				update(cashinRecord);
			} 
			try {
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setRemark(proposal.getRemark() + ";自动执行:");
				 if (type.intValue() == ProposalType.CASHIN.getCode().intValue()){
					bankinfoDao.changeAmountByName(proposal.getBankaccount(), proposal.getAmount(),pno);
				}
				
				
				taskDao.excuteTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
			
			try {
				//发送存款和提款发送邮件通知功能，根据userstatus表里锁字段mailflag来判断是否需要发送  
				Userstatus userstatus = (Userstatus)get(Userstatus.class, proposal.getLoginname());
				if(null ==userstatus ||null==userstatus.getMailflag() ||userstatus.getMailflag()==1){
					//System.out.println("不要发送邮件");
				}else{
					if (type.intValue() == ProposalType.CASHIN.getCode().intValue()) {
						Users user=(Users) this.userDao.get(Users.class, proposal.getLoginname());
						String html = EmailTemplateHelp.toHTML(Constants.EMAIL_CASHIN_BODY_HTML, new Object[]{user.getLoginname(),NumericUtil.double2String(proposal.getAmount()),DateUtil.formatDateForStandard(proposal.getCreateTime())});
						this.mailSender.sendmail(html, user.getEmail(), "存款通知 - 9win国际娱乐城");
					}else if (type.intValue() == ProposalType.CASHOUT.getCode().intValue()) {
						Users user=(Users) this.userDao.get(Users.class, proposal.getLoginname());
						String html = EmailTemplateHelp.toHTML(Constants.EMAIL_CASHOUT_BODY_HTML, new Object[]{user.getLoginname(),NumericUtil.double2String(proposal.getAmount()),DateUtil.formatDateForStandard(proposal.getCreateTime())});
						this.mailSender.sendmail(html, user.getEmail(), "提款通知 - 9win国际娱乐城");
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e.getMessage(),e);
			}
			
		}
		return msg;
	}
	/**
	 * 得到指定时间内未执行的提案集合
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Proposal> getNotExecProposal(Date starttime,Date endtime)throws Exception{
		return this.findByCriteria(DetachedCriteria.forClass(Proposal.class)
				.add(Restrictions.or(Restrictions.eq("type", ProposalType.XIMA.getCode()), Restrictions.eq("type", ProposalType.SELFXIMA.getCode())))
				.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode()), Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode())))
				.add(Restrictions.ge("createTime", starttime))
				.add(Restrictions.lt("createTime", endtime)));
	}
	
	@SuppressWarnings("unchecked")
	public List<Proposal> getNotExecProfitProposal(Date starttime,Date endtime)throws Exception{
		return this.findByCriteria(DetachedCriteria.forClass(Proposal.class)
				.add(Restrictions.eq("type", ProposalType.PROFIT.getCode()))
				.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode()), Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode())))
				);
	}
	
	@SuppressWarnings("unchecked")
	public List<Proposal> getNotExecPrizeProposal(Date starttime,Date endtime)throws Exception{
		return this.findByCriteria(DetachedCriteria.forClass(Proposal.class)
				.add(Restrictions.eq("type", ProposalType.PRIZE.getCode()))
				.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode()), Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode())))
				);
	}
	
	

	/*
	 * public String addCashout(String proposer, String loginname, String title, String from, Double money, String ip, String remark, String
	 * notifyNote, String notifyPhone) throws GenericDfhRuntimeException { log.info("add Cashout proposal"); String msg = null; Users user =
	 * (Users) get(Users.class, loginname, LockMode.UPGRADE); msg = userDao.checkUserForProposal(user); if (msg != null) return msg; if
	 * (user.getCredit() < money) { msg = "用户额度不足，无法申请提款"; return msg; } if (!user.getRole().equals(title)) { msg = "未找到该类型的帐号,可能用户类型选择错误";
	 * return msg; } if (proposalDao.existNotAuditedProposal(loginname, ProposalType.CASHOUT)) { msg = "该用户已提交过提款提案，尚未审批完"; return msg; } if
	 * (msg == null) try { Accountinfo accountinfo = (Accountinfo) get(Accountinfo.class, loginname); if (accountinfo == null) { msg =
	 * "用户尚未完善银行资料"; } else if (!user.getAliasName().equals(accountinfo.getAccountName())) msg = "用户账户姓名与银行信息姓名不一致"; else { String pno =
	 * seqDao.generateProposalPno(ProposalType.CASHOUT); tradeDao.changeCredit(loginname, Math.abs(money) * -1,
	 * CreditChangeType.CASHOUT.getCode(), pno, null); Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money,
	 * accountinfo.getAccountName(), accountinfo.getAccountType(), accountinfo.getAccountCity(), accountinfo.getBankAddress(),
	 * accountinfo.getAccountNo(), accountinfo.getBank(), user.getPhone(), user.getEmail(), ip, remark, notifyNote, notifyPhone); Proposal
	 * proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHOUT.getCode(), user.getLevel(), loginname, money,
	 * ProposalFlagType.SUBMITED.getCode(), from, null, null); save(cashout); save(proposal); taskDao.generateTasks(pno, proposer); msg =
	 * null; } } catch (Exception e) { e.printStackTrace(); throw new GenericDfhRuntimeException(e.getMessage()); } return msg; }
	 */

	// 审核执行
	public String auditExcute(String pno, String operator, String ip, String remark) {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue()) {
			msg = "该提案不是待审核状态";
		} else {
			try {
				proposal.setFlag(ProposalFlagType.AUDITED.getCode());
				proposal.setRemark("审核:" + StringUtils.trimToEmpty(remark));
				save(proposal);
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new GenericDfhRuntimeException(ex.getMessage());
			}
			proposal = (Proposal) get(Proposal.class, pno);
			Integer type = proposal.getType();
			if (type.intValue() == ProposalType.CASHIN.getCode().intValue()) {
				Cashin cashin = (Cashin) get(Cashin.class, pno, LockMode.UPGRADE);
				if (cashin == null)
					throw new GenericDfhRuntimeException("存款提案记录不存在");
				tradeDao.changeCredit(cashin.getLoginname(), cashin.getMoney(), CreditChangeType.CASHIN.getCode(), pno, remark);
				Double transferConsMoney = Constants.getTransferConsMoney(cashin.getMoney());
				if (transferConsMoney > 0)
					tradeDao.changeCredit(cashin.getLoginname(), transferConsMoney, CreditChangeType.BANK_TRANSFER_CONS.getCode(), null,
							"系统自动添加");
			} /*else if (type.intValue() == ProposalType.CONCESSIONS.getCode().intValue()) {
				Concessions concessions = (Concessions) get(Concessions.class, pno);
				if (concessions == null)
					throw new GenericDfhRuntimeException("首存优惠提案记录不存在");
				tradeDao.changeCredit(concessions.getLoginname(), Math.abs(concessions.getTryCredit()), CreditChangeType.FIRST_DEPOSIT_CONS
						.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.BANKTRANSFERCONS.getCode().intValue()) {
				Banktransfercons cons = (Banktransfercons) get(Banktransfercons.class, pno);
				if (cons == null)
					throw new GenericDfhRuntimeException("转账优惠提案记录不存在");
				tradeDao.changeCredit(cons.getLoginname(), Math.abs(cons.getTryCredit()), CreditChangeType.BANK_TRANSFER_CONS.getCode(),
						pno, remark);
			} else if (type.intValue() == ProposalType.REBANKINFO.getCode().intValue()) {
				Rebankinfo rebankinfo = (Rebankinfo) get(Rebankinfo.class, pno);
				if (rebankinfo == null)
					throw new GenericDfhRuntimeException("银改提案记录不存在");
				Accountinfo accountinfo = (Accountinfo) get(Accountinfo.class, rebankinfo.getLoginname());
				accountinfo.setAccountCity(rebankinfo.getAccountCity());
				accountinfo.setAccountName(rebankinfo.getAccountName());
				accountinfo.setAccountNo(rebankinfo.getAccountNo());
				accountinfo.setAccountType(rebankinfo.getAccountType());
				accountinfo.setBank(rebankinfo.getBank());
				accountinfo.setBankAddress(rebankinfo.getBankAddress());
				accountinfo.setLastModifyTime(DateUtil.now());
				update(accountinfo);
			}*/ else if (type.intValue() == ProposalType.XIMA.getCode().intValue()) {
				Xima xima = (Xima) get(Xima.class, pno);
				if (xima == null)
					throw new GenericDfhRuntimeException("洗码优惠提案记录不存在");
				// if (xima.getFirstCash().doubleValue() >= 300 * 10000)
				// userDao.setLevel(xima.getLoginname(), VipLevel.BAIJIN.getCode(), Constants.DEFAULT_OPERATOR);
				tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
						CreditChangeType.XIMA_CONS.getCode(), pno, remark);
			}
			try {
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setRemark(proposal.getRemark() + ";执行:" + StringUtils.trimToEmpty(remark));
				taskDao.excuteTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	// 后台添加用户
	public String operatorAddNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName,
			String phone, String email, String role, String remark, String ipaddress) {
		log.info("add NewAccount operatorAddNewAccount");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		if (user != null) {
			msg = "该用户已存在";
			return msg;
		}
		if (UserRole.MONEY_CUSTOMER.getCode().equals(title)
				&& !(loginname.startsWith(Constants.PREFIX_MONEY_CUSTOMER) || loginname.startsWith(Constants.PREFIX_PARTNER_SUBMEMBER)))
			msg = UserRole.MONEY_CUSTOMER.getText() + "应以" + Constants.PREFIX_MONEY_CUSTOMER + "或" + Constants.PREFIX_PARTNER_SUBMEMBER
					+ "开头";
		else if (UserRole.AGENT.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_AGENT))
			msg = UserRole.AGENT.getText() + "应以" + Constants.PREFIX_AGENT + "开头";
		// else if (UserRole.PARTNER.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_PARTNER))
		// msg = UserRole.PARTNER.getText() + "应以" + Constants.PREFIX_PARTNER + "开头";
		if (msg != null)
			return msg;
		/*if (proposalDao.existNotFinishedProposal(loginname, ProposalType.NEWACCOUNT)) {
			msg = "已提交过该帐号的开户提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.NEWACCOUNT);
				Newaccount newAccount = new Newaccount(pno, role, loginname, pwd, phone, email, aliasName, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.NEWACCOUNT.getCode(), VipLevel.COMMON
						.getCode(), loginname, null, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(newAccount);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
				msg = auditExcute(pno, proposer, ipaddress, "ok");
				if (msg != null) {
					throw new GenericDfhRuntimeException(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}*/
		return "已屏蔽";
	}

	// 添加首存优惠
	public String addUserConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit,
			String payType, String remark, String ipaddress) throws GenericDfhRuntimeException {
		log.info("add Cashout proposal");
		String msg = null;
		// FirstlyCashin firstlyCashin = userDao.getFirstCashin(loginname);// 用户第一笔存款
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		// if (firstlyCashin == null)
		msg = "您还没有存款";
		// else if (firstlyCashin.getMoney().doubleValue() < firstCash.doubleValue())
		// msg = "你的第一笔存款金额为:" + firstlyCashin.getMoney();
		if (new Double(firstCash.doubleValue() * 0.68).intValue() != tryCredit.intValue())
			msg = "首存优惠金额计算错误，申请金额应为:第一笔存款金额的68%";
		// else if
		// (DateUtil.convertToTimestamp(DateUtil.getToday()).after(DateUtil.parseDateForStandard(DateUtil.getMontHreduce(user.getCreatetime(),
		// 30))))
		// msg = "您开户已经超过30天不能再享受开户优惠";
		else {
			log.info("add Cashout proposal");
			msg = userDao.checkUserForProposal(user);
			if (msg != null)
				return msg;
			if (!user.getRole().equals(title)) {
				msg = "未找到该类型的帐号,可能用户类型选择错误";
				return msg;
			}
			/*if (proposalDao.existNotCancledProposal(loginname, ProposalType.CONCESSIONS)) {
				msg = "已经提交或曾经申请过首存款优惠提案";
				return msg;
			}
			if (msg == null) {
				try {
					String pno = seqDao.generateProposalPno(ProposalType.CONCESSIONS);
					Concessions concessions = new Concessions(pno, user.getRole(), loginname, payType, firstCash, tryCredit, remark);
					Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CONCESSIONS.getCode(), user.getLevel(),
							loginname, tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
					save(concessions);
					save(proposal);
					taskDao.generateTasks(pno, proposer);
				} catch (Exception e) {
					e.printStackTrace();
					throw new GenericDfhRuntimeException(e.getMessage());
				}
			}*/
		}
		return "已屏蔽";
	}
	
	// 添加首存优惠,有投注要求
	public String addUserTimesConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit,
			String payType, String remark,Integer times, String ipaddress) throws GenericDfhRuntimeException {
		log.info("add Cashout proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		if (new Double(firstCash.doubleValue() * 0.68).intValue() != tryCredit.intValue())
			msg = "首存优惠金额计算错误，申请金额应为:第一笔存款金额的68%";
		else {
			log.info("add Cashout proposal");
			msg = userDao.checkUserForProposal(user);
			if (msg != null)
				return msg;
			if (!user.getRole().equals(title)) {
				msg = "未找到该类型的帐号,可能用户类型选择错误";
				return msg;
			}
			/*if (proposalDao.existNotCancledProposal(loginname, ProposalType.CONCESSIONS)) {
				msg = "已经提交或曾经申请过首存款优惠提案";
				return msg;
			}
			if (msg == null) {
				try {
					String pno = seqDao.generateProposalPno(ProposalType.CONCESSIONS);
					Concessions concessions = new Concessions(pno, user.getRole(), loginname, payType, firstCash, tryCredit, remark);
					Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CONCESSIONS.getCode(), user.getLevel(),
							loginname, tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
					save(concessions);
					save(proposal);
					taskDao.generateTasks(pno, proposer);
					
					//将投注倍数和再存优惠金额放到userstatus中
					Userstatus userstatus = (Userstatus) userDao.get(Userstatus.class, loginname, LockMode.UPGRADE);
					if(userstatus==null){
						Userstatus status=new Userstatus();
						status.setCashinwrong(0);
						status.setLoginname(loginname);
						status.setMailflag(1);
						status.setLoginerrornum(0);
						status.setTouzhuflag(1);
						status.setTimes(times);
						status.setStarttime(DateUtil.now());
						status.setFirstCash(firstCash);
						status.setAmount(tryCredit);
						save(status);
					}else{
						userstatus.setTouzhuflag(1);
						userstatus.setTimes(times);
						userstatus.setStarttime(DateUtil.now());
						userstatus.setFirstCash(firstCash);
						userstatus.setAmount(tryCredit);
						update(userstatus);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					throw new GenericDfhRuntimeException(e.getMessage());
				}
			}*/
		}
		return "已屏蔽";
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public OperatorDao getOperatorDao() {
		return operatorDao;
	}

	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public void setOperatorDao(OperatorDao operatorDao) {
		this.operatorDao = operatorDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String addOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money, String remark)
			throws GenericDfhRuntimeException {
		log.info("add addOffer proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.OFFER)) {
			msg = "该用户已提交过促销优惠提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.OFFER);
				Offer offer = new Offer(pno, title, loginname, firstCash, money, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.OFFER.getCode(), user.getLevel(), loginname,
						money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(offer);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String addTimesOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money,Integer times , String remark)
	throws GenericDfhRuntimeException {
		log.info("add addtimesOffer proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if(StringUtils.isNotBlank(user.getShippingcodePt())){
			msg = "玩家目前正在使用别的pt优惠，请去提案查看记录";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.OFFER)) {
			msg = "该用户已提交过促销优惠提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.OFFER);
				Offer offer = new Offer(pno, title, loginname, firstCash, money, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.OFFER.getCode(), user.getLevel(), loginname,
						money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(offer);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
				
				//将投注倍数和再存优惠金额放到userstatus中
				Userstatus userstatus = (Userstatus) userDao.get(Userstatus.class, loginname, LockMode.UPGRADE);
				if(userstatus==null){
					Userstatus status=new Userstatus();
					status.setCashinwrong(0);
					status.setLoginname(loginname);
					status.setMailflag(1);
					status.setLoginerrornum(0);
					status.setTouzhuflag(1);
					status.setTimes(times);
					status.setStarttime(DateUtil.now());
					status.setFirstCash(firstCash);
					status.setAmount(money);
					save(status);
				}else{
					userstatus.setTouzhuflag(1);
					userstatus.setTimes(times);
					userstatus.setStarttime(DateUtil.now());
					userstatus.setFirstCash(firstCash);
					userstatus.setAmount(money);
					update(userstatus);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
			return msg;
	}	

	public String addPrize(String proposer, String loginname, String title, String from, Double amount, String remark)
			throws GenericDfhRuntimeException {
		log.info("add addPrize proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.PRIZE)) {
			msg = "该用户已提交过幸运抽奖提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.PRIZE);
				Prize prize = new Prize(pno, title, loginname, amount, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.PRIZE.getCode(), user.getLevel(), loginname,
						amount, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, remark, null);
				save(prize);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	
	public String addPrizeBirthdayGifts(String proposer, String loginname, String title, String from, Double amount, String remark)
	throws GenericDfhRuntimeException {
		log.info("add addPrizeBirthdayGifts proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.BIRTHDAY)) {
			msg = "该用户已提交过生日礼金提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
				Prize prize = new Prize(pno, title, loginname, amount, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.BIRTHDAY.getCode(), user.getLevel(), loginname,
						amount, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(prize);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
		}
	
	
	public String addPrizeProposalCoupon(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent)
	throws GenericDfhRuntimeException {
		String msg = null;
			try {
				String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null,0.0, agent, ProposalFlagType.SUBMITED.getCode(), from, null, null);
				if(type==537){
					proposal.setLoginname(agent);
				}else{
					proposal.setLoginname("");
				}
				proposal.setBetMultiples(betMultiples);
				proposal.setShippinginfo(remark);
				if(amount==null){
					proposal.setGifTamount(0.00);
				}else{
					proposal.setGifTamount(amount);
				}
				String str="";
				if(type==531){
					str="A";
				}else if(type==532){
					str="B";
				}else if(type==533){
					str="C";
				}else if(type==534){
					str="D";
				}else if(type==535){
					str="E";
				}else if(type==536){
					str="F";
				}else{
					str="G";
				}
				String sqlCouponId=seqDao.generateYhjID();
				String codeOne=dfh.utils.StringUtil.getRandomString(3);
				String codeTwo=dfh.utils.StringUtil.getRandomString(3);
				proposal.setShippingCode(str+codeOne+sqlCouponId+codeTwo);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				msg=""+e.toString();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String addPrizeProposalCouponTwo(String proposer,String from,Integer type,Double amount, String betMultiples, String remark,String agent)
	throws GenericDfhRuntimeException {
		String msg = null;
			try {
				Integer countType = 100;
				if(type==531){
					countType=2;
				}else if(type==532){
					countType=3;
				}else if(type==533){
					countType=4;
				}else if(type==534){
					countType=5;
				}else if(type==535){
					countType=6;
				}
				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc.add(Restrictions.eq("level", countType));
				dc.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
				dc.add(Restrictions.eq("flag", 0));
				List<Users> users =this.findByCriteria(dc);
				if(users!=null && users.size()>0 && users.get(0)!=null){
					for (Users user : users) {
						String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
						Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null,0.0, agent, ProposalFlagType.AUDITED.getCode(), from, null, null);
						if(type==537){
							proposal.setLoginname(user.getLoginname());
						}else{
							proposal.setLoginname(user.getLoginname());
						}
						proposal.setBetMultiples(betMultiples);
						proposal.setShippinginfo(remark);
						if(amount==null){
							proposal.setGifTamount(0.00);
						}else{
							proposal.setGifTamount(amount);
						}
						String str="";
						if(type==531){
							str="A";
						}else if(type==532){
							str="B";
						}else if(type==533){
							str="C";
						}else if(type==534){
							str="D";
						}else if(type==535){
							str="E";
						}else if(type==536){
							str="F";
						}else{
							str="G";
						}
						String sqlCouponId=seqDao.generateYhjID();
						String codeOne=dfh.utils.StringUtil.getRandomString(3);
						String codeTwo=dfh.utils.StringUtil.getRandomString(3);
						proposal.setShippingCode(str+codeOne+sqlCouponId+codeTwo);
						save(proposal);
						taskDao.generateTasks(pno, proposer);
						if(proposal.getType()==531){
							Guestbook guestBook = new Guestbook();
							guestBook.setUsername(user.getLoginname());
							guestBook.setFlag(0);
							guestBook.setIpaddress("");
							guestBook.setCreatedate(new Timestamp(new Date().getTime()));
							guestBook.setTitle("10%存送优惠劵代码");
							guestBook.setContent("尊敬的龙都客户，您的10%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
							guestBook.setAdminstatus(1);
							guestBook.setUserstatus(0);
							guestBook.setUpdateid("");
							guestBook.setIsadmin(0);
							guestBook.setAdminname("客服管理员");
							guestbookDao.save(guestBook);
						}else if(proposal.getType()==532){
							Guestbook guestBook = new Guestbook();
							guestBook.setUsername(user.getLoginname());
							guestBook.setFlag(0);
							guestBook.setIpaddress("");
							guestBook.setCreatedate(new Timestamp(new Date().getTime()));
							guestBook.setTitle("20%存送优惠劵代码");
							guestBook.setContent("尊敬的龙都客户，您的20%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
							guestBook.setAdminstatus(1);
							guestBook.setUserstatus(0);
							guestBook.setUpdateid("");
							guestBook.setIsadmin(0);
							guestBook.setAdminname("客服管理员");
							guestbookDao.save(guestBook);
						}else if(proposal.getType()==533){
							Guestbook guestBook = new Guestbook();
							guestBook.setUsername(user.getLoginname());
							guestBook.setFlag(0);
							guestBook.setIpaddress("");
							guestBook.setCreatedate(new Timestamp(new Date().getTime()));
							guestBook.setTitle("30%存送优惠劵代码");
							guestBook.setContent("尊敬的龙都客户，您的30%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
							guestBook.setAdminstatus(1);
							guestBook.setUserstatus(0);
							guestBook.setUpdateid("");
							guestBook.setIsadmin(0);
							guestBook.setAdminname("客服管理员");
							guestbookDao.save(guestBook);
						}else if(proposal.getType()==534){
							Guestbook guestBook = new Guestbook();
							guestBook.setUsername(user.getLoginname());
							guestBook.setFlag(0);
							guestBook.setIpaddress("");
							guestBook.setCreatedate(new Timestamp(new Date().getTime()));
							guestBook.setTitle("40%存送优惠劵代码");
							guestBook.setContent("尊敬的龙都客户，您的40%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
							guestBook.setAdminstatus(1);
							guestBook.setUserstatus(0);
							guestBook.setUpdateid("");
							guestBook.setIsadmin(0);
							guestBook.setAdminname("客服管理员");
							guestbookDao.save(guestBook);
						}else if(proposal.getType()==535){
							Guestbook guestBook = new Guestbook();
							guestBook.setUsername(user.getLoginname());
							guestBook.setFlag(0);
							guestBook.setIpaddress("");
							guestBook.setCreatedate(new Timestamp(new Date().getTime()));
							guestBook.setTitle("50%存送优惠劵代码");
							guestBook.setContent("尊敬的龙都客户，您的50%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
							guestBook.setAdminstatus(1);
							guestBook.setUserstatus(0);
							guestBook.setUpdateid("");
							guestBook.setIsadmin(0);
							guestBook.setAdminname("客服管理员");
							guestbookDao.save(guestBook);
						}else if(proposal.getType()==536){
							Guestbook guestBook = new Guestbook();
							guestBook.setUsername(user.getLoginname());
							guestBook.setFlag(0);
							guestBook.setIpaddress("");
							guestBook.setCreatedate(new Timestamp(new Date().getTime()));
							guestBook.setTitle("红包优惠劵代码");
							guestBook.setContent("尊敬的龙都客户，您的红包优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
							guestBook.setAdminstatus(1);
							guestBook.setUserstatus(0);
							guestBook.setUpdateid("");
							guestBook.setIsadmin(0);
							guestBook.setAdminname("客服管理员");
							guestbookDao.save(guestBook);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg=""+e.toString();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	
	/********
	 * 红包优惠
	 * auto
	 */
	public String addRedProposalCoupon(String proposer, String from, Integer type, Double amount, String betMultiples, String remark,String agent) throws GenericDfhRuntimeException {

		String msg = null;
		try {
			String pno = seqDao.generateProposalPno(ProposalType.REDCOUPON419);
			Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, agent, ProposalFlagType.SUBMITED.getCode(), from, null, null);
			proposal.setLoginname("");
			proposal.setBetMultiples(betMultiples);
			proposal.setShippinginfo(remark);
			if (amount == null) {
				proposal.setGifTamount(0.00);
			} else {
				proposal.setGifTamount(amount);
			}
			String str = "";
			if (type == 419) {
				str = "HB";
			} 
			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
			save(proposal);
			taskDao.generateTasks(pno, proposer);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	
	public String addPrizeProposalCouponPt(String proposer, String from, Integer type, Double amount, String betMultiples, String remark, String agent) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
			Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, agent, ProposalFlagType.SUBMITED.getCode(), from, null, null);
			proposal.setLoginname("");
			proposal.setBetMultiples(betMultiples);
			proposal.setShippinginfo(remark);
			if (amount == null) {
				proposal.setGifTamount(0.00);
			} else {
				proposal.setGifTamount(amount);
			}
			String str = "";
			if (type == 571) {
				str = "H";
			} else if (type == 572) {
				str = "I";
			} else if (type == 574) {
				str = "K";
			} else if (type == 409) {
				str = "PT9";
			}else if (type == 410) {
				str = "PT0";
			}else if (type == 411) {
				str = "PT1";
			}else if (type == 412) {
				str = "PT2";
			}
			else  {
				str = "J";
			}
			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
			save(proposal);
			taskDao.generateTasks(pno, proposer);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String addPrizeProposalCouponPtTwo(String proposer, String from, Integer type, Double amount, String betMultiples, String remark, String agent, String usernameType) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc.add(Restrictions.eq("level", Integer.parseInt(usernameType)));
			dc.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
			dc.add(Restrictions.eq("flag", 0));
			List<Users> users =this.findByCriteria(dc);
			if(users!=null && users.size()>0 && users.get(0)!=null){
				for (Users user : users) {
					String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
					Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, agent, ProposalFlagType.AUDITED.getCode(), from, null, null);
					proposal.setLoginname(user.getLoginname());
					proposal.setBetMultiples(betMultiples);
					proposal.setShippinginfo(remark);
					if (amount == null) {
						proposal.setGifTamount(0.00);
					} else {
						proposal.setGifTamount(amount);
					}
					String str = "";
					if (type == 571) {
						str = "H";
					} else if (type == 572) {
						str = "I";
					} else  {
						str = "J";
					}
					String sqlCouponId = seqDao.generateYhjID();
					String codeOne = dfh.utils.StringUtil.getRandomString(3);
					String codeTwo = dfh.utils.StringUtil.getRandomString(3);
					proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
					save(proposal);
					taskDao.generateTasks(pno, proposer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String addPrizeProposalCouponSb(String proposer, String from, Integer type, Double amount, String betMultiples, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
			Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, null, ProposalFlagType.SUBMITED.getCode(), from, null, null);
			proposal.setLoginname("");
			proposal.setBetMultiples(betMultiples);
			proposal.setShippinginfo(remark);
			if (amount == null) {
				proposal.setGifTamount(0.00);
			} else {
				proposal.setGifTamount(amount);
			}
			String str = "";
			if (type == 581) {
				str = "SC";
			} else if (type == 582) {
				str = "SB";
			} else if (type == 584) {
				str = "SD";
			} else  {
				str = "SA";
			}
			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
			save(proposal);
			taskDao.generateTasks(pno, proposer);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	
	public String addPrizeProposalCouponTtg(String proposer, String from, Integer type, Double amount, String betMultiples, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
			Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, null, ProposalFlagType.SUBMITED.getCode(), from, null, null);
			proposal.setLoginname("");
			proposal.setBetMultiples(betMultiples);
			proposal.setShippinginfo(remark);
			if (amount == null) {
				proposal.setGifTamount(0.00);
			} else {
				proposal.setGifTamount(amount);
			}
			String str = "";
			if (type == 401) {
				str = "TG1";
			}else if (type == 402) {
				str = "TG2";
			}else if (type == 403) {
				str = "TG3";
			}else if (type == 404) {
				str = "TG4";
			}else if (type == 405) {
				str = "GP1";
			}else if (type == 406) {
				str = "GP2";
			}else if (type == 407) {
				str = "GP3";
			}else if (type == 408) {
				str = "GP4";
			}else if (type == 425) {
				str = "NT1";
			}else if (type == 422) {
				str = "NT2";
			}else if (type == 423) {
				str = "NT3";
			}else if (type == 424) {
				str = "NT4";
			}else if (type == 426) {
				str = "QT1";
			}else if (type == 427) {
				str = "QT2";
			}else if (type == 428) {
				str = "QT3";
			}else if (type == 429) {
				str = "QT4";
			}else if (type == 430) {
				str = "MG1";
			}else if (type == 431) {
				str = "MG2";
			}else if (type == 432) {
				str = "MG3";
			}else if (type == 433) {
				str = "MG4";
			}else if (type == 434) {
				str = "DT1";
			}else if (type == 435) {
				str = "DT2";
			}else if (type == 436) {
				str = "DT3";
			}else if (type == 437) {
				str = "DT4";
			}
			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
			save(proposal);
			taskDao.generateTasks(pno, proposer);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String addPrizeProposalCouponSbTwo(String proposer, String from, Integer type, Double amount, String betMultiples, String remark,String usernameType) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc.add(Restrictions.eq("level", Integer.parseInt(usernameType)));
			dc.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
			dc.add(Restrictions.eq("flag", 0));
			List<Users> users = this.findByCriteria(dc);
			if (users != null && users.size() > 0 && users.get(0) != null) {
				for (Users user : users) {
					//红包优惠劵
					if (type == 581) {
						DetachedCriteria dc1 = DetachedCriteria.forClass(Proposal.class);
						dc1.add(Restrictions.eq("loginname", user.getLoginname()));
						dc1.add(Restrictions.eq("type", 581));
						List<Proposal> proposals= this.findByCriteria(dc1);
						if (proposals != null && proposals.size() > 0 && proposals.get(0) != null) {
							System.out.println("**********"+user.getLoginname()+"*********");
							continue;
						}
					}
					String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
					Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, null, ProposalFlagType.AUDITED.getCode(), from, null, null);
					proposal.setLoginname(user.getLoginname());
					proposal.setBetMultiples(betMultiples);
					proposal.setShippinginfo(remark);
					if (amount == null) {
						proposal.setGifTamount(0.00);
					} else {
						proposal.setGifTamount(amount);
					}
					String str = "";
					if (type == 581) {
						str = "SC";
					} else if (type == 582) {
						str = "SB";
					} else if (type == 584) {
						str = "SD";
					} else  {
						str = "SA";
					}
					String sqlCouponId = seqDao.generateYhjID();
					String codeOne = dfh.utils.StringUtil.getRandomString(3);
					String codeTwo = dfh.utils.StringUtil.getRandomString(3);
					proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
					save(proposal);
					taskDao.generateTasks(pno, proposer);
					if(proposal.getType()==581){
						Guestbook guestBook = new Guestbook();
						guestBook.setUsername(proposal.getLoginname());
						guestBook.setFlag(0);
						guestBook.setIpaddress("127.0.0.1");
						guestBook.setCreatedate(new Timestamp(new Date().getTime()));
						guestBook.setTitle("188体育红包优惠劵代码");
						guestBook.setContent("尊敬的龙都客户，您的188体育红包优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setUpdateid("");
						guestBook.setIsadmin(0);
						guestBook.setAdminname("客服管理员");
						guestbookDao.save(guestBook);
					}else if(proposal.getType()==582){
						Guestbook guestBook = new Guestbook();
						guestBook.setUsername(proposal.getLoginname());
						guestBook.setFlag(0);
						guestBook.setIpaddress("127.0.0.1");
						guestBook.setCreatedate(new Timestamp(new Date().getTime()));
						guestBook.setTitle("188体育存10万送2万优惠劵代码");
						guestBook.setContent("尊敬的龙都客户，您的188体育存10万送2万优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setUpdateid("");
						guestBook.setIsadmin(0);
						guestBook.setAdminname("客服管理员");
						guestbookDao.save(guestBook);
					}else if(proposal.getType()==583){
						notifyService.sendSms(user.getPhone(), "尊敬的客户，您的优-惠-劵代码已经发入你的站内邮箱，请查收！");
						Guestbook guestBook = new Guestbook();
						guestBook.setUsername(proposal.getLoginname());
						guestBook.setFlag(0);
						guestBook.setIpaddress("127.0.0.1");
						guestBook.setCreatedate(new Timestamp(new Date().getTime()));
						guestBook.setTitle("188体育存20万送5万优惠劵代码");
						guestBook.setContent("尊敬的龙都客户，您的188体育存20万送5万优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setUpdateid("");
						guestBook.setIsadmin(0);
						guestBook.setAdminname("客服管理员");
						guestbookDao.save(guestBook);
					}else if(proposal.getType()==584){
						Guestbook guestBook = new Guestbook();
						guestBook.setUsername(proposal.getLoginname());
						guestBook.setFlag(0);
						guestBook.setIpaddress("127.0.0.1");
						guestBook.setCreatedate(new Timestamp(new Date().getTime()));
						guestBook.setTitle("188体育20%存送优惠劵代码");
						guestBook.setContent("尊敬的龙都客户，您的188体育20%存送优惠劵代码为"+proposal.getShippingCode()+"，请您到龙都账户管理的优惠券专区进行使用，优惠券相关内容请您注意查看温馨提示，谢谢！");
						guestBook.setAdminstatus(1);
						guestBook.setUserstatus(0);
						guestBook.setUpdateid("");
						guestBook.setIsadmin(0);
						guestBook.setAdminname("客服管理员");
						guestbookDao.save(guestBook);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	
	
	public String addActivity(Integer id,String activityName,Double activityPercent,Date start,Date end,String remark) throws GenericDfhRuntimeException{
		SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(id==null){
			Activity activity=new Activity();
			activity.setActivityName(activityName);
			activity.setActivityPercent(activityPercent);
			activity.setActivityStart(start);
			activity.setActivityEnd(end);
			try {
				Calendar dateStart = Calendar.getInstance();
				dateStart.setTime(start);
				dateStart.set(Calendar.DATE, dateStart.get(Calendar.DATE) + 1);
				String backstageStart = yyyymmdd.format(dateStart.getTime()) + " 00:00:00";
				activity.setBackstageStart(yyyyMMddHHmmss.parse(backstageStart));
				Calendar dateEnd = Calendar.getInstance();
				dateEnd.setTime(end);
				dateEnd.set(Calendar.DATE, dateEnd.get(Calendar.DATE) + 1);
				String backstageEnd = yyyymmdd.format(dateEnd.getTime()) + " 00:00:00";
				activity.setBackstageEnd(yyyyMMddHHmmss.parse(backstageEnd));
			} catch (Exception e) {
				new GenericDfhRuntimeException(e.toString());
			}
			activity.setRemark(remark);
			activity.setActivityStatus(0);
			activity.setCreateDate(new Date());
			activity.setUserrole("");
			save(activity);
		}else{
			Activity activity = (Activity)get(Activity.class, id);
			activity.setActivityName(activityName);
			activity.setActivityPercent(activityPercent);
			activity.setActivityStart(start);
			activity.setActivityEnd(end);
			try {
				Calendar dateStart = Calendar.getInstance();
				dateStart.setTime(start);
				dateStart.set(Calendar.DATE, dateStart.get(Calendar.DATE) + 1);
				String backstageStart = yyyymmdd.format(dateStart.getTime()) + " 00:00:00";
				activity.setBackstageStart(yyyyMMddHHmmss.parse(backstageStart));
				Calendar dateEnd = Calendar.getInstance();
				dateEnd.setTime(end);
				dateEnd.set(Calendar.DATE, dateEnd.get(Calendar.DATE) + 1);
				String backstageEnd = yyyymmdd.format(dateEnd.getTime()) + " 00:00:00";
				activity.setBackstageEnd(yyyyMMddHHmmss.parse(backstageEnd));
			} catch (Exception e) {
				new GenericDfhRuntimeException(e.toString());
			}
			activity.setRemark(remark);
			update(activity);
		}
		return null;
	}
	
	public Users getUserAgent(String agent) throws GenericDfhRuntimeException{
		if (StringUtils.isNotEmpty(agent)) {
			DetachedCriteria agentCriteria = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.AGENT.getCode()));
			agentCriteria = agentCriteria.add(Restrictions.eq("loginname", agent)).add(Restrictions.eq("flag", Constants.ENABLE));
			List list = getHibernateTemplate().findByCriteria(agentCriteria);
			if (list != null && list.size() > 0)
				return (Users) list.get(0);
		}
		return null;
	}
	
	public Proposal getProposalPno(String pno) throws GenericDfhRuntimeException{
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("pno", pno));
		List<Proposal> list = findByCriteria(dc);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public String addProposalLevelPrize(String proposer, String loginname, String title, String from, Double amount, String remark)
	throws GenericDfhRuntimeException {
		log.info("add addProposalLevelPrize proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.LEVELPRIZE)) {
			msg = "该用户已提交过晋级礼金提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.LEVELPRIZE);
				Prize prize = new Prize(pno, title, loginname, amount, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.LEVELPRIZE.getCode(), user.getLevel(), loginname,
						amount, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(prize);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
		}

	
	
	/**
	 * 申请提款
	 */
	public String addCashout(String proposer, String loginname, String pwd, String title, String from, Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email, String phone,
			String ip, String remark) throws GenericDfhRuntimeException {
		money = Math.abs(money);
		log.info("add Cashout proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		
		if (user.getCredit() < money) {
			msg = "用户额度不足，无法申请提款";
			return msg;
		}
		// 提款必须大于100元
		if (money < 100.0) {
			msg = "100元以上才能申请提款";
			return msg;
		}
		if (!StringUtils.equalsIgnoreCase(user.getAccountName(), accountName)) {
			msg = "用户真实姓名" + user.getAccountName() + "和提款姓名" + accountName + "不一致";
			return msg;
		}
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotAuditedProposal(loginname, ProposalType.CASHOUT)) {
			msg = "该用户已提交过提款提案，尚未审批完";
			return msg;
		}
		
		List<Proposal> list = proposalDao.getCashoutToday(loginname);
		if (list.size()>=Integer.parseInt(Configuration.getInstance().getValue("CashoutTodayCount"))) {
			msg = "抱歉，提款失败！\\n每天只可以提款【"+Configuration.getInstance().getValue("CashoutTodayCount")+"】次";
			return msg;
		}
		
		double oldCashoutAmount=0d;
		for (int i = 0; i < list.size(); i++) {
			Proposal proposal = list.get(i);
			oldCashoutAmount+=proposal.getAmount();
		}
		
		if ((oldCashoutAmount+money)>Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))) {
			msg = "抱歉，提款失败！\\n" +
					"每天最大提款额度为【"+Configuration.getInstance().getValue("CashoutTodayAmount")+"】元\\n" +
					"该用户已经提款【"+oldCashoutAmount+"】元，还可以提款【"+String.valueOf(Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))-oldCashoutAmount)+"】元";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.CASHOUT);
				tradeDao.changeCredit(loginname, money * -1, CreditChangeType.CASHOUT.getCode(), pno, null);
				Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money, accountName, accountType, accountCity,
						bankAddress, accountNo, bank, phone, email, ip, remark, null, null);

				// 记录提款后的本地和远程额度
				Double afterLocalCredit = -1.0, afterRemoteCredit = -1.0;
				try {
					afterLocalCredit = user.getCredit();
					afterRemoteCredit = RemoteCaller.queryCredit(user.getLoginname());
				} catch (Exception e) {
					e.printStackTrace();
					try {
						afterRemoteCredit = RemoteCaller.queryCredit(user.getLoginname());
					} catch (Exception e1) {
						e1.printStackTrace();
						//如果没查到远程额度，标记为-1.0
						afterRemoteCredit=-1.0;
					}
				}

				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHOUT.getCode(), user.getLevel(), loginname,
						money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null, afterLocalCredit, afterRemoteCredit);
				if(bank ==null||bank.equals("")){
				}else{
					proposal.setBankname(bank);
				}
				save(cashout);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	/**
	 * 申请提款
	 */
	public String  addProxyCashOutProposal(String proposer, String loginname, String from, Double money, String accountNo,
			String accountName,String bank,String remark,String ip) throws GenericDfhRuntimeException {
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		//判断用户是否被冻结
		if (msg != null){
			return msg;
		}
		//判断是否是代理账号
		if(user.getRole().equals("MONEY_CUSTOMER")){
			return "不是代理账号！";
		}
		// 提款必须大于100元
		if (money < 100.0) {
			msg = "最低预支100元";
			return msg;
		}
		// 提款必须小于190000元
		if (money > 190000.0) {
			msg = "最低预支190000元";
			return msg;
		}
		if (!StringUtils.equalsIgnoreCase(user.getAccountName(), accountName)) {
			msg = "用户真实姓名" + user.getAccountName() + "和账号姓名" + accountName + "不一致";
			return msg;
		}
		if (proposalDao.existNotAuditedProposal(loginname, ProposalType.PROXYADVANCE)) {
			msg = "该用户已提交过信用预支提案，尚未审批完";
			return msg;
		}
		try {
			String pno = seqDao.generateProposalPno(ProposalType.CASHOUT);
			tradeDao.changeCredit(loginname, money * -1, CreditChangeType.PROXYADVANCE.getCode(), pno, null);
			Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money, accountName, Constants.DEFAULT_ACCOUNTTYPE, "none",
					"none", accountNo, bank, user.getPhone(), user.getEmail(), "", remark, null, null);

			Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.PROXYADVANCE.getCode(), user.getLevel(), loginname,
					money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null,-1.0,-1.0);
			proposal.setAfterAgRemoteAmount(-1.0);
			proposal.setAfterAgInRemoteAmount(-1.0);
			proposal.setAfterBbinRemoteAmount(-1.0);
			proposal.setBankname(bank);
			save(cashout);
			save(proposal);
			taskDao.generateTasks(pno, proposer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	
	public String addBusiness(String proposer,String depositname,String depositaccount,String depositbank,
			String businessProposalType,Double money,String remark,String file,String belong,Integer bankaccountid)throws GenericDfhRuntimeException{
		String msg = null;
		log.info("add addProposalBusiness proposal");
		if (msg == null)
			try {
				String pno="";
				if(businessProposalType.equals("601")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.SERVERFEE);
				}else if(businessProposalType.equals("602")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.SEOFEE);
				}else if(businessProposalType.equals("603")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.GOVFEE);
				}else if(businessProposalType.equals("604")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.SALARYFEE);
				}else if(businessProposalType.equals("605")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.NORMALFEE);
				}else if(businessProposalType.equals("606")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.HARDWAREFEE);
				}else if(businessProposalType.equals("607")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.PUBLICISTFEE);
				}else if(businessProposalType.equals("608")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.LOANFEE);
				}else if(businessProposalType.equals("609")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.OTHER);
				}else if(businessProposalType.equals("610")){
					pno = seqDao.generateBusinessProposalPno(BusinessProposalType.BROKENBILL);
				}
				
				BusinessProposal businessProposal=new BusinessProposal();
				businessProposal.setPno(pno);
				businessProposal.setAmount(money);
				businessProposal.setAttachment(file);
				businessProposal.setCreateTime(DateUtil.now());
				businessProposal.setDepositaccount(depositaccount);
				businessProposal.setDepositbank(depositbank);
				businessProposal.setDepositname(depositname);
				businessProposal.setFlag(0);
//				businessProposal.setBelong(belong);
				businessProposal.setProposer(proposer);
				businessProposal.setRemark(remark);
				businessProposal.setType(Integer.parseInt(businessProposalType));
				businessProposal.setWhereisfrom("后台");
				businessProposal.setBankaccountid(bankaccountid);
				
				save(businessProposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	/*
	 * 查找自本次提款的上次提款的时刻额度和时间
	 */
	public Proposal getLastSuccCashout(String loginname, Date before) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("loginname", loginname)).add(
				Restrictions.eq("type", ProposalType.CASHOUT.getCode())).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()))
				.add(Restrictions.lt("createTime", before)).addOrder(Order.desc("createTime"));
		List<Proposal> list = findByCriteria(dc, 0, 1);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public String excuteicbctransfer(Integer id, String loginname,
			String operator) throws GenericDfhRuntimeException {
		String msg = null;
		IcbcTransfers icbcTransfers = (IcbcTransfers)get(IcbcTransfers.class, id,  LockMode.UPGRADE);
		if (icbcTransfers == null)
			msg = "找不到该转账记录";
		else if(icbcTransfers.getStatus()!=2){
			msg = "该转账记录不是未匹配状态";
		}
		else
			try {
				icbcTransfers.setStatus(0);
				icbcTransfers.setNotes(loginname);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE,  "icbctransferid :" + id);
				update(icbcTransfers);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String addPrizeProposalPhone(String proposer, String from, Integer type, Double amount, String betMultiples, String remark, String agent) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			List<Customer> list = getCustomerPhoneList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Customer customer = list.get(i);
					List<Proposal> list1 = getProposalList(customer.getShippingCode());
					if (list1 == null || list1.size() <= 0) {
						String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
						Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, agent, ProposalFlagType.AUDITED.getCode(), from, null, null);
						proposal.setLoginname(agent);
						proposal.setShippinginfo("外部电话");
						proposal.setShippingCode(customer.getShippingCode());
						proposal.setGifTamount(amount);
						proposal.setBetMultiples(betMultiples);
						save(proposal);
						taskDao.generateTasks(pno, proposer);
						 notifyService.sendSmsNew(customer.getPhone(),
						 "恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（"+customer.getShippingCode()+"）就可以获得，机会难得，赶快行动吧。");
						System.out.println("恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。" + "***************新增********" + i);
						continue;
					}
					Proposal proposal = list1.get(0);
					if (proposal.getFlag() == 1) {
						proposal.setCreateTime(DateUtil.now());
						update(proposal);
						 notifyService.sendSmsNew(customer.getPhone(),
						 "恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（"+customer.getShippingCode()+"）就可以获得，机会难得，赶快行动吧。");
						System.out.println("恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。" + "***************更新********" + i);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}

	public String addPrizePhone(String phone, String proposer, String from, Integer type, Double amount, String betMultiples, String remark, String agent) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			Customer customer = getCustomerPhone(phone);
			if (customer == null) {
				return "电话号码不存在";
			}
			List<Proposal> list1 = getProposalList(customer.getShippingCode());
			if (list1 == null || list1.size() <= 0) {
				String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, agent, ProposalFlagType.AUDITED.getCode(), from, null, null);
				proposal.setLoginname(agent);
				proposal.setShippinginfo("外部电话");
				proposal.setShippingCode(customer.getShippingCode());
				proposal.setGifTamount(amount);
				proposal.setBetMultiples(betMultiples);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
				System.out.println("恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。" + "***************新增********");
				return msg;
			}
			Proposal proposal = list1.get(0);
			if (proposal.getFlag() == 1) {
				proposal.setCreateTime(DateUtil.now());
				update(proposal);
				System.out.println("恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。" + "***************更新********");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String addPrizeEmail(String email, String proposer, String from, Integer type, Double amount, String betMultiples, String remark, String agent) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			Customer customer = getCustomerEmail(email);
			if (customer == null) {
				return "邮箱不存在";
			}
			List<Proposal> list1 = getProposalList(customer.getShippingCode());
			if (list1 == null || list1.size() <= 0) {
				String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, agent, ProposalFlagType.AUDITED.getCode(), from, null, null);
				proposal.setLoginname(agent);
				proposal.setShippinginfo("外部邮箱");
				proposal.setShippingCode(customer.getShippingCode());
				proposal.setGifTamount(amount);
				proposal.setBetMultiples(betMultiples);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
				notifyService.sendEmail(customer.getEmail(), "邀请卷", "恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。www.longdu97.com");
				return msg;
			}
			Proposal proposal = list1.get(0);
			if (proposal.getFlag() == 1) {
				proposal.setCreateTime(DateUtil.now());
				update(proposal);
				notifyService.sendEmail(customer.getEmail(), "邀请卷", "恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。www.longdu97.com");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}

	public String addPrizeProposalEmail(String proposer, String from, Integer type, Double amount, String betMultiples, String remark, String agent) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			List<Customer> list = getCustomerEmailList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Customer customer = list.get(i);
					List<Proposal> list1 = getProposalList(customer.getShippingCode());
					if (list1 == null || list1.size() <= 0) {
						String pno = seqDao.generateProposalPno(ProposalType.BIRTHDAY);
						Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), type, null, null, 0.0, agent, ProposalFlagType.AUDITED.getCode(), from, null, null);
						proposal.setLoginname(agent);
						proposal.setShippinginfo("外部邮箱");
						proposal.setShippingCode(customer.getShippingCode());
						proposal.setGifTamount(amount);
						proposal.setBetMultiples(betMultiples);
						save(proposal);
						taskDao.generateTasks(pno, proposer);
						notifyService.sendEmail(customer.getEmail(), "邀请卷", "恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。www.longdu97.com");
						continue;
					}
					Proposal proposal = list1.get(0);
					if (proposal.getFlag() == 1) {
						proposal.setCreateTime(DateUtil.now());
						update(proposal);
						notifyService.sendEmail(customer.getEmail(), "邀请卷", "恭喜您获得龙都娱乐城为您提供的开户礼金68元，只需在注册的时候填入相应的邀请码（" + customer.getShippingCode() + "）就可以获得，机会难得，赶快行动吧。www.longdu97.com");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String addPt8CouponProposalEmail(String proposer)
			throws GenericDfhRuntimeException {
		String msg = null;
		try {
			DetachedCriteria c = DetachedCriteria.forClass(PtCoupon.class);
			c = c.add(Restrictions.eq("type", "0")); // 查询所有未使用的优惠劵的邮箱
			List list = proposalDao.findByCriteria(c);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					PtCoupon ptCoupon = (PtCoupon) list.get(i);
					notifyService.sendEmail(ptCoupon.getEmail(), "PT8红包优惠劵",
							"恭喜您获得千亿娱乐城为您提供的开户礼金8元，只需在注册的时候填入相应的优惠码（"
									+ ptCoupon.getCode()
									+ "）就可以获得，机会难得，赶快行动吧。www.longdu97.com");
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "" + e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	public String addPrizeProposalEmailAll(String title,String rmrak,Integer batch)
	throws GenericDfhRuntimeException {
		String msg = null;
		try {
			String email="";
			List<Customer> list = getCustomerEmailList(batch);
			for (int i = 0; i < list.size(); i++) {
				Customer customer=list.get(i);
				email=email+","+customer.getEmail();
			}
			if(!email.equals("")){
				notifyService.sendEmail(email,title, rmrak);
				updateEmailSendTime(batch);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg=""+e.toString();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	return msg;
	}

	public GuestbookDao getGuestbookDao() {
		return guestbookDao;
	}

	public void setGuestbookDao(GuestbookDao guestbookDao) {
		this.guestbookDao = guestbookDao;
	}

	@Override
	public String excutecmbtransfer(Integer id, String loginname,
			String operator) throws GenericDfhRuntimeException {
		String msg = null;
		CmbTransfers cmbTransfers = (CmbTransfers)get(CmbTransfers.class, id,  LockMode.UPGRADE);
		if (cmbTransfers == null)
			msg = "找不到该转账记录";
		else if(cmbTransfers.getStatus()!=2){
			msg = "该转账记录不是未匹配状态";
		}
		else
			try {
				String remark="补单--"+operator;
				cmbTransfers.setRemark(remark);
				cmbTransfers.setStatus(0);
				cmbTransfers.setNotes(loginname);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE,  "cmbtransferid :" + id);
				update(cmbTransfers);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}
	
	public String updateActivityStatus(Object o){
		update(o);
		return null;
	}
	
	public String excuteAlipaytransfer(String id, String loginname,
			String operator) {
		String msg = null;
		AlipayTransfers alipayTransfers = (AlipayTransfers)get(AlipayTransfers.class, id,  LockMode.UPGRADE);
		if (alipayTransfers == null)
			msg = "找不到该转账记录";
		else if(alipayTransfers.getStatus()!=2){
			msg = "该转账记录不是未匹配状态";
		}
		else{
			try {
				alipayTransfers.setStatus(0);
				alipayTransfers.setNotes(loginname);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE,  "alipaytransferid :" + id);
				update(alipayTransfers);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}
	


	@Override
	public String excuteabctransfer(Integer id, String loginname,
			String operator) throws GenericDfhRuntimeException {
		String msg = null;
		AbcTransfers abcTransfers = (AbcTransfers)get(AbcTransfers.class, id,  LockMode.UPGRADE);
		if (abcTransfers == null)
			msg = "找不到该转账记录";
		else if(abcTransfers.getStatus()!=2){
			msg = "该转账记录不是未匹配状态";
		}
		else
			try {
				abcTransfers.setStatus(0);
				abcTransfers.setJyzy(loginname);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE,  "abctransferid :" + id);
				update(abcTransfers);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	@Override
	public String autoAddXimaPtProposal(List<PtProfit> ptProfit) {
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
    	if(ptProfit==null || ptProfit.size()<=0 ||ptProfit.get(0)==null){
			return "数据为空！";
		}
		for (PtProfit pt : ptProfit) {
			String loginName=pt.getLoginname();
			Users user = (Users) this.getUserDao().get(Users.class,loginName,LockMode.UPGRADE);
			if (user==null) {
				log.info("用户："+loginName+"，不存在");
				continue;
			}
			double newrate = getSlotRate(user);
			//************************************************
			XimaVO ximaObject = new XimaVO(pt.getBetCredit(),user.getLoginname(),newrate);
			//设置无上限
			ximaObject.setXimaAmouont(pt.getBetCredit()*newrate);
			/***********************************/
			
			String remark = "PT系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", pt.getBetCredit(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
				Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.userDao.save(xima);
			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), pt.getAmount(), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户PT平台输赢值");
			agProfit.setPlatform("pt");
			agProfit.setBettotal(pt.getBetCredit());
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	
	public String autoAddXimaNewPtProposal(List<PtData> ptData){
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
    	if(ptData==null || ptData.size()<=0 ||ptData.get(0)==null){
			return "数据为空！";
		}
		for (PtData pt : ptData) {
			String loginName=pt.getPlayername().substring(1).toLowerCase();
			Users user = (Users) this.getUserDao().get(Users.class,loginName,LockMode.UPGRADE);
			if (user==null) {
				log.info("用户："+loginName+"，不存在");
				continue;
			}
			double newrate = getSlotRate(user);
			
			//************************************************
			XimaVO ximaObject = new XimaVO(pt.getBets(),user.getLoginname(),newrate);
			//设置无上限
			ximaObject.setXimaAmouont(pt.getBets()*newrate);
			
			
			String remark = "NEWPT系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", pt.getBets(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
				Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.userDao.save(xima);
			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), pt.getHouseearnings(), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户NEWPT平台输赢值");
			agProfit.setPlatform("newpt");
			agProfit.setBettotal(pt.getBets());
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	
	public String autoAddXimaNewPtProposalNewXXX(List<PtDataNew> ptData){
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
    	if(ptData==null || ptData.size()<=0 ||ptData.get(0)==null){
			return "数据为空！";
		}
		try {
			String dateStr = DateUtil.formatMMddHH(new Date());
			for (PtDataNew pt : ptData) {
				String loginName = pt.getPlayername().substring(3).toLowerCase();
				Users user = (Users) this.getUserDao().get(Users.class, loginName);
				if (user == null) {
					log.info("用户：" + loginName + "，不存在");
					continue;
				}
				double newrate = getSlotRate(user);
				
				String remarkAdd="";
				List<SystemConfig> list=this.querySystemConfig("type003", "001", "否");//超级洗码活动 如果没有被禁用 则乘以超级洗码的比例
				if(null!=list&&list.size()>0){
					SystemConfig sc=list.get(0);
					newrate=Arith.mul(newrate,Double.parseDouble(sc.getValue()));
					remarkAdd="超级洗码：";
				}
				
				//pt的自助洗码是昨天一天
				starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(),-24);
				endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

				//************************************************
				XimaVO ximaObjectTiger = new XimaVO(pt.getBetsTiger(),user.getLoginname(), newrate);
				//******************pttiger去掉自助洗码*********************/
				
				
				Double maxRebete = getRebateLimit(user); 
				
				ximaObjectTiger = cutSelfXima(ximaObjectTiger, starttime,
						endtime, ProposalType.PTTIGERSELFXIMA, newrate,
						maxRebete);
				//******************end*********************/
				String remark = remarkAdd+"PTTIGER系统洗码";
				
				String pno = ProposalType.XIMA.getCode() + "O" + dateStr + RandomStringUtils.randomAlphanumeric(10);
				log.info("正在处理提案号pttiger：" + pno + ",反水金额："
						+ Math.round(ximaObjectTiger.getXimaAmouont() * 100.00)
						/ 100.00 + "...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(),
						"网银支付", ximaObjectTiger.getValidBetAmount(),
						ximaObjectTiger.getXimaAmouont(),
						DateUtil.convertToTimestamp(starttime),
						DateUtil.convertToTimestamp(endtime),
						ximaObjectTiger.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(),
						ProposalType.XIMA.getCode(), user.getLevel(),
						user.getLoginname(), ximaObjectTiger.getXimaAmouont(),
						user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.userDao.save(xima);
				this.proposalDao.save(proposal);
				AgProfit agProfit = new AgProfit(pno, "system", DateUtil.now(),
						ProposalType.XIMA.getCode(), user.getLevel(),
						user.getLoginname(), pt.getIncome(), user.getAgent(),
						ProposalFlagType.EXCUTED.getCode(),
						Constants.FROM_BACK, "用户NEWPT平台输赢值");
				agProfit.setPlatform("newpt");
				agProfit.setBettotal(pt.getBets());
				this.proposalDao.save(agProfit);

				//*************ptother********************/
				String remark1 = "ptother系统洗码";
				String pno1 = ProposalType.XIMA.getCode() + "O" + dateStr + RandomStringUtils.randomAlphanumeric(10);
				XimaVO ximaObjectOther = new XimaVO(pt.getBets()
						- pt.getBetsTiger(), user.getLoginname(), 0.004);///除了老虎机外，其他的都是0.4%的反水
				if(ximaObjectOther.getXimaAmouont()<=0.0) continue;
				//******************ptother去掉自助洗码*********************/
				Double ptOtherRate = 0.004;
				//******************end*********************/
				if (ximaObjectOther.getValidBetAmount() < 0) {
					ximaObjectOther.setValidBetAmount(0.0);
				}
				if (ximaObjectOther.getXimaAmouont() < 0) {
					ximaObjectOther.setXimaAmouont(0.0);
				}

				log.info("正在处理提案号ptother：" + pno1 + ",反水金额："
						+ Math.round(ximaObjectOther.getXimaAmouont() * 100.00)
						/ 100.00 + "...");
				Xima xima1 = new Xima(pno1, user.getRole(),
						user.getLoginname(), "网银支付",
						ximaObjectOther.getValidBetAmount(),
						ximaObjectOther.getXimaAmouont(),
						DateUtil.convertToTimestamp(starttime),
						DateUtil.convertToTimestamp(endtime), ptOtherRate,
						remark1);
				Proposal proposal1 = new Proposal(pno1, "system",
						DateUtil.now(), ProposalType.XIMA.getCode(),
						user.getLevel(), user.getLoginname(),
						ximaObjectOther.getXimaAmouont(), user.getAgent(),
						ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark1, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno1, "system");
				this.userDao.save(xima1);
				this.proposalDao.save(proposal1);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String addNewPtSkyBatchXimaProposal(List<Bean4Xima> ptData) {

		Date starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1 * 24 - 0));
		Date endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		if (ptData == null || ptData.size() <= 0 || ptData.get(0) == null) {
			return "数据为空！";
		}
		try {
			String dateStr = DateUtil.formatMMddHH(new Date());
			for (Bean4Xima pt : ptData) {
				String loginName = pt.getUserName();
				
				pt.setProfit(pt.getBetAmount()-pt.getProfit()); //输赢值
				
				Users user = (Users) this.getUserDao().get(Users.class, loginName);
				if (user == null) {
					log.info("用户：" + loginName + "，不存在");
					continue;
				}

				double newrate = getSlotRate(user);
				
				Double ptrebate = this.getRebateLimit(user);

				// pt的自助洗码是昨天一天
				starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -24);
				endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

				// ************************************************
				XimaVO ximaObjectTiger = new XimaVO(pt.getBetAmount(), user.getLoginname(), newrate);
				// ******************ptsky去掉自助洗码*********************/
				ximaObjectTiger = cutSelfXima(ximaObjectTiger, starttime, endtime, ProposalType.PTSKYSELFXIMA, newrate, ptrebate);
				// ******************end*********************/
				String remark = "PTSKY系统洗码";
				
				String pno = ProposalType.XIMA.getCode() + "p" + dateStr + RandomStringUtils.randomAlphanumeric(10);
				log.info("正在处理提案号ptsky：" + pno + ",反水金额：" + Math.round(ximaObjectTiger.getXimaAmouont() * 100.00) / 100.00 + "...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObjectTiger.getValidBetAmount(), ximaObjectTiger.getXimaAmouont(), DateUtil.convertToTimestamp(starttime),
						DateUtil.convertToTimestamp(endtime), ximaObjectTiger.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObjectTiger.getXimaAmouont(), user.getAgent(),
						ProposalFlagType.AUDITED.getCode(), Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.userDao.save(xima);
				this.proposalDao.save(proposal);
				AgProfit agProfit = new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), pt.getProfit(), user.getAgent(),
						ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户PTSKY平台输赢值");
				agProfit.setPlatform("ptsky");
				agProfit.setBettotal(pt.getBetAmount());
				this.proposalDao.save(agProfit);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String autoAddXimaSixLotteryProposal(List<PlatformData> sixData){

		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
    	if(sixData==null || sixData.size()<=0 ||sixData.get(0)==null){
			return "数据为空！";
		}
		for (PlatformData sixlottery : sixData) {
			String loginName = sixlottery.getLoginname() ;
			Users user = (Users) this.getUserDao().get(Users.class,loginName,LockMode.UPGRADE);
			if (user==null) {
				log.info("用户："+loginName+"，不存在");
				continue;
			}
			double newrate = 0.004;
			
			//六合彩的自助洗码是昨天一天
			starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -24) ;
			endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0) ;
			
			//************************************************
			XimaVO ximaObjectSix = new XimaVO(sixlottery.getBet(),user.getLoginname(),newrate);
			ximaObjectSix.setValidBetAmount(sixlottery.getBet());
			//去掉自助洗码的反水
			//ximaObjectSix = cutSelfXima(ximaObjectSix, starttime, endtime, ProposalType.SIXLOTTERYSELFXIMA, newrate , user.getKenorebate()) ;
			
			/***********************************/
			String remark = "SIXLOTTERY系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号sixlottery："+pno+",反水金额："+Math.round(ximaObjectSix.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObjectSix.getValidBetAmount(), ximaObjectSix.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObjectSix.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObjectSix.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
				Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.userDao.save(xima);
			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), sixlottery.getProfit(), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户SIXLOTTERY平台输赢值");
			agProfit.setPlatform("sixlottery");
			agProfit.setBettotal(sixlottery.getBet());
			this.proposalDao.save(agProfit);
		}
		return null;
	}


	/**
	 * 添加EBetApp写入输赢记录 , 不洗马
	 *
	 * @param profits EBet输赢数据
	 */
	public String autoAddXimaEBetAppProposal(List<PlatformData> profits){
		String paltformCode = "ebetapp";
		if (null== profits && profits.isEmpty()){
			return "profits is null";
		}
		for (PlatformData profit : profits) {
			String loginname = profit.getLoginname();
			Users user = (Users) this.getUserDao().get(Users.class,loginname);
			if (user==null) {
				log.info("用户："+loginname+"不存在");
				continue;
			}
			Double betCredit=profit.getBet();//投注额
			Double amount=profit.getProfit(); //记录的Amount为平台盈利
			//不做反水 , 只写入到AgProfit
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			this.getTaskDao().generateTasks(pno, "system");
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户EBetApp平台输赢值");
			agProfit.setPlatform(paltformCode);
			agProfit.setBettotal(betCredit);
			this.proposalDao.save(agProfit);
		}
		return null;
	}

	public XimaVO cutSelfXima(XimaVO ximaVo , Date  starttime, Date  endtime, ProposalType type , Double newrate , Double rebate){
		//进行自助反水缩减   昨天12点到今天12点之间的自助反水
		
		DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.gt("createTime", starttime));
		c.add(Restrictions.le("createTime", endtime));
		c.add(Restrictions.eq("loginname", ximaVo.getLoginname()));
		
		c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
		Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		
		c.add(Restrictions.eq("type", type.getCode()));
		c.setProjection(Projections.property("pno"));
		List list = this.findByCriteria(c);
		if(list!=null && !list.isEmpty()){
			DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
			x.add(Restrictions.in("pno", list.toArray()));
			x.setProjection(Projections.sum("firstCash"));
			List sumx = this.findByCriteria(x);
			Double d = 0.0;
			Double dremain =0.0;
			rebate = (rebate==null?28888.00:rebate) ;
			if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
				if((Double)sumx.get(0)*newrate< rebate ){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
					
					dremain = rebate - (Double)sumx.get(0)*newrate;//剩下可反水额度
					d = ximaVo.getValidBetAmount()-(Double)sumx.get(0);
					
					ximaVo.setValidBetAmount(d);
					if(d<0){
						d=0.0;
					}
				}
				ximaVo.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
			}
		}
		return ximaVo ;
	}
	
	@Override
	public String autoAddXimaJCProposal(List<JCProfitData> jcData){
		
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
    	if(jcData==null || jcData.size()<=0 ||jcData.get(0)==null){
			return "数据为空！";
		}
		try {
			for (JCProfitData jc : jcData) {
				String loginName = jc.getPlayerName().substring(1);
				Users user = (Users) this.getUserDao().get(Users.class,
						loginName, LockMode.UPGRADE);
				if (user == null) {
					log.info("用户：" + loginName + "，不存在");
					continue;
				}
				//JC系统洗码暂时无，为0
				double newrate = 0;
				//JC的系统洗码是昨天一天
				starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(),-24);
				endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

				//************************************************
				XimaVO ximaJC= new XimaVO(jc.getActual(),user.getLoginname(), newrate);
				ximaJC = cutSelfXima(ximaJC, starttime, endtime, ProposalType.JCSELFXIMA, newrate, /*user.getPtrebate()*/0.0);
				//******************end*********************/
				String remark = "JC系统洗码";
				String pno = this.getSeqDao().generateProposalPno(
						ProposalType.XIMA);
				log.info("正在处理提案号JC：" + pno + ",反水金额："
						+ Math.round(ximaJC.getXimaAmouont() * 100.00)
						/ 100.00 + "...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(),
						"网银支付", ximaJC.getValidBetAmount(),
						ximaJC.getXimaAmouont(),
						DateUtil.convertToTimestamp(starttime),
						DateUtil.convertToTimestamp(endtime),
						ximaJC.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(),
						ProposalType.XIMA.getCode(), user.getLevel(),
						user.getLoginname(), ximaJC.getXimaAmouont(),
						user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.userDao.save(xima);
				this.proposalDao.save(proposal);
				AgProfit agProfit = new AgProfit(pno, "system", DateUtil.now(),
						ProposalType.XIMA.getCode(), user.getLevel(),
						user.getLoginname(), jc.getWin(), user.getAgent(),
						ProposalFlagType.EXCUTED.getCode(),
						Constants.FROM_BACK, "用户JC平台输赢值");
				agProfit.setPlatform("jc");
				agProfit.setBettotal(jc.getActual());
				this.proposalDao.save(agProfit);
				
				//*************other********************/
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String updateRankingData(Integer round, int type) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Concert.class);
		dc.add(Restrictions.eq("round", round));
		dc.add(Restrictions.eq("type", type));
		dc.add(Restrictions.eq("active", 0));
		dc.addOrder(Order.desc("bet"));
		List<Concert> list = this.findByCriteria(dc);
		if(list != null && !list.isEmpty()){
			
			double bets = 0.0;
			int ranking = 1;
			Date now = new Date();
			for(int i = 0; i < list.size(); i++){
				Concert c = list.get(i);
				c.setLastTime(now);
				if(i == 0){
					bets = c.getBet();
					c.setRanking(ranking);
					continue;
				}
				
				if(bets != c.getBet()){
					ranking = i+1;
					bets = c.getBet();
				}
				c.setRanking(ranking);
			}
			this.saveAll(list);
		}
		return "更新排名成功，操作条数：" + list.size();
	}	
	
	/**
	 * 演唱会升级流水更新
	 * @throws Exception 
	 */
	@Override
	public String concertUpdateBet(Date now, ConcertDateType concertDateType, Integer type) throws Exception{
		
		if(concertDateType == null) return "活动类型异常！";
		
		String sql = "select loginname, sum(bettotal) as bettotal from agprofit where createTime>=:betTimeStart and createTime<=:betTimeEnd and platform in(:platforms) group by loginname";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("betTimeStart", DateUtil.fmtStandard(concertDateType.getXimaStart()));
		params.put("betTimeEnd", DateUtil.fmtStandard(concertDateType.getXimaEnd()));
		params.put("platforms", new String[]{"newpt", "mg", "dt", "qt", "nt", "ttg", "png", "ptsky"});
		List ximadata = this.getListBySql(sql, params);
		if(ximadata == null || ximadata.isEmpty()) return "查询流水为空";
		
		List<Concert> all = new ArrayList<>();
		for(Object array:ximadata){
			
			Object[] data = (Object[]) array;
			String loginname = data[0].toString();
			Double totalbet = Double.parseDouble(data[1].toString() == null? "0":data[1].toString());
			
			DetachedCriteria insertDc = DetachedCriteria.forClass(Concert.class);
			insertDc.add(Restrictions.eq("startTime",DateUtil.fmtStandard(concertDateType.getStart())));
			insertDc.add(Restrictions.eq("loginname", loginname));
			insertDc.add(Restrictions.eq("type", type));			
			List<Concert> insertList = findByCriteria(insertDc);
			if (insertList == null || insertList.size() == 0){
				Concert cct = new Concert();
				cct.setLoginname(loginname);
				cct.setBet(totalbet);
				cct.setActive(0);
				cct.setStartTime(DateUtil.fmtStandard(concertDateType.getStart()));
				cct.setEndTime(DateUtil.fmtStandard(concertDateType.getEnd()));
				cct.setLastTime(now);
				cct.setCreatetime(now);
				cct.setType(type);
				cct.setRound(concertDateType.getText());
				cct.setDisplay(0);
				all.add(cct);
			} else {
				Concert cct = insertList.get(0);
				cct.setBet(totalbet);
				cct.setLastTime(now);
				all.add(cct);
			}
		}
		this.saveAll(all);
		return "操作成功,更新数据条数" + all.size();
	}
	
	/**
	 * 添加NT系统洗码
	 * @param ntDatas NT输赢数据
	 */
	public String autoAddXimaNTProposal(List<NTProfit> ntDatas){
		if (null==ntDatas && ntDatas.isEmpty()){
			return "ntDatas is null";
		}
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		for (NTProfit ntp : ntDatas) {
			String loginname = ntp.getLoginname();
			Users user = (Users) this.getUserDao().get(Users.class,loginname);
			if (user==null) {
				log.info("用户："+loginname+"不存在");
				continue;
			}
			//double newrate = user.getAgrate().doubleValue();
			double newrate = getSlotRate(user);
			
			//定为新会员及忠实会员 0.6% ，星级、金牌、白金 0.8% ，钻石至尊 1%
			
			String nowdate = DateUtil.fmtyyyyMMdd(new Date());
			//TODO NT与TTG返回相同，但不参与活动
			if(nowdate.equals("20160805") || nowdate.equals("20160812") || nowdate.equals("20160819")){

				newrate = 0.015;
			}
			
//			Double newrebate = null ;
//			if(user.getLevel() <= VipLevel.BAIJIN.getCode()){
//				newrebate = 28888.00 ;
//			}else{
//				newrebate = 99999999999.00 ;
//			}
			
			Double betCredit=ntp.getBetCredit();//投注额
			//agbet=agbet*6;
			//String str=getStringValue(i, 10).trim();//如果玩家赢钱，导入的数据为负数  
			//Double agprofit =-1*Double.parseDouble(str);
			Double amount=ntp.getAmount(); //NT记录的amount就为平台盈利
			//agprofit=agprofit*6;
			XimaVO ximaObject = new XimaVO(betCredit,user.getLoginname(),newrate,this.getRebateLimit(user));
			/***********************************/
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.NTSELFXIMA, newrate, this.getRebateLimit(user));
			/***********************************/
			String remark = "nt系统洗码";
			
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户NT平台输赢值");
			agProfit.setPlatform("nt");
			agProfit.setBettotal(betCredit);
			this.proposalDao.save(agProfit); 
		}
		return null;
	}
	
	@Override
	public String autoAddXimaEaProposal(List<EaData> eaDatas) {
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		
    	if(eaDatas==null || eaDatas.size()<=0 ||eaDatas.get(0)==null){
			return "数据为空！";
		}
		for (EaData eaData : eaDatas) {
			String string=eaData.getLoginname();
			Users user = (Users) this.getUserDao().get(Users.class,string.substring(6,string.length()),LockMode.UPGRADE);
			if (user==null) {
				log.info("用户："+string.substring(6,string.length())+"，不存在");
				continue;
			}
			
			//自定义ea反水率
			double newrate = user.getRate().doubleValue();
			
			//获取活动返水
			Date date=new Date();
			DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
			dc.add(Restrictions.eq("activityStatus", 1));
			dc = dc.add(Restrictions.le("backstageStart", date));
			dc = dc.add(Restrictions.gt("backstageEnd", date));
			dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
			List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
			if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
				Activity activity=listActivity.get(0);
				if(activity.getActivityPercent()!=null){
					newrate = activity.getActivityPercent();
				}
			}
			
			//************************************************
			Double rebet = this.getRebateLimit(user);
			XimaVO ximaObject = new XimaVO(eaData.getZtze(),user.getLoginname(),newrate,rebet);
			
			/***********************************/
			//进行自助反水缩减   昨天12点到今天12点之间的自助反水
			
			DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
			c.add(Restrictions.gt("createTime", starttime));
			c.add(Restrictions.le("createTime", endtime));
			c.add(Restrictions.eq("loginname", user.getLoginname()));
			
			c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
			Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
			
			c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
			c.setProjection(Projections.property("pno"));
			List list = this.proposalDao.findByCriteria(c);
			if(list!=null && !list.isEmpty()){
				DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
				x.add(Restrictions.in("pno", list.toArray()));
				x.setProjection(Projections.sum("firstCash"));
				List sumx = this.proposalDao.findByCriteria(x);
				Double d = 0.0;
				Double dremain =0.0;
				if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0) ){
					if((Double)sumx.get(0)*newrate<rebet){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
						dremain = rebet-(Double)sumx.get(0)*newrate;//剩下可反水额度
						d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
						if(d<0){
							d=0.0;
						}
					}
					ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
				}
			}
			/***********************************/
			String remark = "ea系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", eaData.getZtze(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
				Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), (0-eaData.getZsy()), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户ea平台输赢值");
			agProfit.setPlatform("ea");
			agProfit.setBettotal(eaData.getZtze());
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	
	@Override
	public String autoAddXimaSbaProposal(List sbaDatas) {
		try {
			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
			
			if(sbaDatas==null || sbaDatas.size()<=0 ||sbaDatas.get(0)==null){
				return "数据为空！";
			}
			for (int i = 0; i < sbaDatas.size(); i++) {
				JSONObject jobj = (JSONObject) sbaDatas.get(i);
				
				System.out.println("loginname:"+jobj.get("loginname"));
				System.out.println("bet:"+jobj.get("bet"));
				System.out.println("amount:"+jobj.get("amount"));
				
				String string=(String) jobj.get("loginname");
				Double bet = Double.parseDouble(jobj.get("bet")+"");	//投注额
				Double amount = Double.parseDouble(jobj.get("amount")+""); //玩家总输赢
				
				String loginname = null;
				if(string.startsWith("zb_")){
					loginname = string.substring(3,string.length());
				}else{
					loginname = string.substring(2,string.length());
				}
				Users user = (Users) this.getUserDao().get(Users.class,loginname);
				if (user==null) {
					log.info("用户："+loginname+"，不存在");
					continue;
				}
				double newrate = 0.004;
				//获取活动返水
				Date date=new Date();
				DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
				dc.add(Restrictions.eq("activityStatus", 1));
				dc = dc.add(Restrictions.le("backstageStart", date));
				dc = dc.add(Restrictions.gt("backstageEnd", date));
				dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
				List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
				if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
					Activity activity=listActivity.get(0);
					if(activity.getActivityPercent()!=null){
						newrate = activity.getActivityPercent();
					}
				}
				
				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,getRebateLimit(user));
				
				/***********************************/
				//进行自助反水缩减   昨天12点到今天12点之间的自助反水
				
				DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", starttime));
				c.add(Restrictions.le("createTime", endtime));
				c.add(Restrictions.eq("loginname", user.getLoginname()));
				
				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
				
				c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
				c.setProjection(Projections.property("pno"));
				List list = this.proposalDao.findByCriteria(c);
				if(list!=null && !list.isEmpty()){
					DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", list.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = this.proposalDao.findByCriteria(x);
					Double d = 0.0;
					Double dremain =0.0;
					if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
						if((Double)sumx.get(0)*newrate<user.getEarebate()){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
							dremain = user.getEarebate()-(Double)sumx.get(0)*newrate;//剩下可反水额度
							d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
							if(d<0){
								d=0.0;
							}
						}
						ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
					}
				}
				/***********************************/
				String remark = "sba系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.proposalDao.save(xima);
				this.proposalDao.save(proposal);
				
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户sba平台输赢值");
				agProfit.setPlatform("sba");
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}
	@Override
	public String autoAddXimaMwgProposal(List resultList) {
		try {
			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
			
			for (int i = 0; i < resultList.size(); i++) {
				JSONObject jobj = (JSONObject) resultList.get(i);
				
				String string=(String) jobj.get("loginname");
				Double bet = Double.parseDouble(jobj.get("bet")+"");	//投注额
				Double amount = Double.parseDouble(jobj.get("amount")+""); //玩家总输赢
				
				String loginname = null;
				if(string.startsWith("zb_")){
					loginname = string.substring(3,string.length());
				}else{
					loginname = string.substring(2,string.length());
				}
	
				Users user = (Users) this.getUserDao().get(Users.class,loginname);
				if (user==null) {
					log.info("用户："+loginname+"，不存在");
					continue;
				}
				double newrate = 0.004;
				//获取活动返水
				Date date=new Date();
				DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
				dc.add(Restrictions.eq("activityStatus", 1));
				dc = dc.add(Restrictions.le("backstageStart", date));
				dc = dc.add(Restrictions.gt("backstageEnd", date));
				dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
				List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
				if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
					Activity activity=listActivity.get(0);
					if(activity.getActivityPercent()!=null){
						newrate = activity.getActivityPercent();
					}
				}
				
				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,getRebateLimit(user));
				
				/***********************************/
				//进行自助反水缩减   昨天12点到今天12点之间的自助反水
				
				DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", starttime));
				c.add(Restrictions.le("createTime", endtime));
				c.add(Restrictions.eq("loginname", user.getLoginname()));
				
				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
				
				c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
				c.setProjection(Projections.property("pno"));
				List list = this.proposalDao.findByCriteria(c);
				if(list!=null && !list.isEmpty()){
					DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", list.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = this.proposalDao.findByCriteria(x);
					Double d = 0.0;
					Double dremain =0.0;
					if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
						if((Double)sumx.get(0)*newrate<user.getEarebate()){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
							dremain = user.getEarebate()-(Double)sumx.get(0)*newrate;//剩下可反水额度
							d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
							if(d<0){
								d=0.0;
							}
						}
						ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
					}
				}
				/***********************************/
				String remark = "mwg系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				//暂时不提供反水
				/*this.proposalDao.save(xima);
				this.proposalDao.save(proposal);*/ 
				
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户mwg平台输赢值");
				agProfit.setPlatform("mwg");
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}
	
	@Override
	public String autoAddXima761Proposal(List resultList) {
		try {
			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
			
			for (int i = 0; i < resultList.size(); i++) {
				Object[] jobj = (Object[]) resultList.get(i);
				
				String string=(String) jobj[0];
				Double bet = Double.parseDouble(jobj[1]+"");
				bet = bet/10000;//投注额
				Double amount = Double.parseDouble(jobj[2]+""); //玩家总输赢
				amount = amount/10000;
				
				String loginname = null;
				if(string.startsWith("zb")){
					loginname = string.substring(2,string.length());
				}else{
					loginname = string.substring(1,string.length());
				}
				
				Users user = (Users) this.getUserDao().get(Users.class,loginname);
				if (user==null) {
					log.info("用户："+loginname+"，不存在");
					continue;
				}
				double newrate = 0.004;
				//获取活动返水
				Date date=new Date();
				DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
				dc.add(Restrictions.eq("activityStatus", 1));
				dc = dc.add(Restrictions.le("backstageStart", date));
				dc = dc.add(Restrictions.gt("backstageEnd", date));
				dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
				List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
				if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
					Activity activity=listActivity.get(0);
					if(activity.getActivityPercent()!=null){
						newrate = activity.getActivityPercent();
					}
				}
				
				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,getRebateLimit(user));
				
				/***********************************/
				//进行自助反水缩减   昨天12点到今天12点之间的自助反水
				
				DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", starttime));
				c.add(Restrictions.le("createTime", endtime));
				c.add(Restrictions.eq("loginname", user.getLoginname()));
				
				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
				
				c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
				c.setProjection(Projections.property("pno"));
				List list = this.proposalDao.findByCriteria(c);
				if(list!=null && !list.isEmpty()){
					DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", list.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = this.proposalDao.findByCriteria(x);
					Double d = 0.0;
					Double dremain =0.0;
					if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
						if((Double)sumx.get(0)*newrate<user.getEarebate()){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
							dremain = user.getEarebate()-(Double)sumx.get(0)*newrate;//剩下可反水额度
							d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
							if(d<0){
								d=0.0;
							}
						}
						ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
					}
				}
				/***********************************/
				String remark = "761系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				//暂时不提供反水
				/*this.proposalDao.save(xima);
				this.proposalDao.save(proposal);*/ 
				
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户761平台输赢值");
				agProfit.setPlatform("761");
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}
 
	@Override
	public String autoAddXimaAgProposal(List datas,String platform) {
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		for (int i = 0; i < datas.size(); i++) {
			
			Object[] jobj = (Object[]) datas.get(i);
			
			System.out.println("loginname:"+jobj[0]);
			System.out.println("bet:"+jobj[1]);
			System.out.println("amount:"+jobj[2]);
			
			String loginname = (String) jobj[0];
			Double bet = Double.parseDouble(jobj[1]+"");	//投注额
			Double amount = Double.parseDouble(jobj[2]+""); //玩家总输赢
			
			Users user = (Users) this.getUserDao().get(Users.class, loginname);
			if (user==null) {
				log.info("用户：" + loginname + "不存在");
				continue;
			}
			double newrate = 0.004;
			if("AGIN".equals(platform)){
				newrate = getLiveRate(user);
			}
			if("SLOT".equals(platform)){
				platform = "aginslot";
				newrate = getSlotRate(user);
			}
			
			XimaVO ximaObject = new XimaVO(bet, user.getLoginname(), newrate);
			
			Double maxRebete = getRebateLimit(user);
			
			/*if("SLOT".equals(platform)){
				ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.BBINELESELFXIMA, newrate,maxRebete) ;
			}*/
			if("YPMONEY".equals(platform)){
				platform = "yoplay";
			}
			/***********************************/
			String remark = platform+"系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			
			if ("aginslot".equals(platform) || "AGIN".equals(platform)) {
				this.proposalDao.save(xima);
				this.proposalDao.save(proposal);
			}
			
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户"+platform+"平台输赢值");
			agProfit.setPlatform(platform.toLowerCase());
			agProfit.setBettotal(bet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	

	
	@Override
	public String autoAddXimaAgSlotProposal(List<AgSlotXima> listXima) {
		String msg = null;
		try {
			Date starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
			Date endtime  = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

			if(listXima==null || listXima.size()<=0 ||listXima.get(0)==null){
				return "数据为空！";
			}
			for (AgSlotXima agXima : listXima) {
				String loginname=agXima.getUserName();
				Users user = (Users) this.getUserDao().get(Users.class,loginname,LockMode.UPGRADE);
				if (user==null) {
					log.info("用户："+loginname+"不存在");
					continue;
				}
				
                double newrate = getSlotRate(user);
				
				//白金以及白金以上单日返水无限制
				Double rebateLimit = this.getRebateLimit(user);
				XimaVO ximaObject = new XimaVO(agXima.getValidbetAmount(),agXima.getUserName(),newrate,rebateLimit);
				/***********************************/
				//减自助反水的时间与agin一样。12点-12点
				//ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.KGSELFXIMA, newrate, keno2RebateLimit) ;
				/***********************************/
				String remark = "aginslot系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(),user.getLevel(), agXima.getUserName(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				save(xima);
				save(proposal);
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), agXima.getUserName(),agXima.getNetamount(), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户 aginslot 平台输赢值");
				agProfit.setPlatform("aginslot");
				agProfit.setBettotal(agXima.getValidbetAmount());
				save(agProfit);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("aginslot洗码错误：", e);
			msg = e.toString();
		}
		return msg;
	}
	
	
	@Override
	public Page queryConcertBet(String loginname,Integer id,Integer pageIndex, Integer size,Integer type) {
		   Page page = new Page();
		
		   StringBuffer c_s = new StringBuffer("select count(*) from Concert where  type="+type+" and round="+id+" ");
		   Long count = (Long) getHibernateTemplate().find(c_s.toString()).iterator().next(); 
			
			
		   DetachedCriteria dc=DetachedCriteria.forClass(Concert.class);
		   dc.add(Restrictions.eq("round",id ));
		   dc.add(Restrictions.eq("type",type ));
		   if (StringUtil.isNotEmpty(loginname)) 
			   dc.add(Restrictions.eq("loginname",loginname ));
		   
		   dc.addOrder(Order.desc("bet"));
		
		   List <Concert>list = findByCriteria(dc, (pageIndex - 1) * size, size);

		   if (list!=null&&list.size()>0) {

				page.setPageNumber(pageIndex);
				page.setSize(size);
				page.setTotalRecords(Integer.valueOf(count+""));
				int pages = PagenationUtil.computeTotalPages(Integer.valueOf(count+""), size).intValue();
				page.setTotalPages(Integer.valueOf(pages));
				if (pageIndex > pages)
					pageIndex = Page.PAGE_BEGIN_INDEX;
				page.setPageNumber(pageIndex);
				page.setPageContents(list);
				page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));

		   }
			
		return page;
	}
	
	
	public Integer updateConcertDisplay(Integer id){
		Integer result=0;
		Concert concert=(Concert) get(Concert.class, id);
		if(concert!=null){
			result=concert.getActive().intValue()==0?1:0;
			concert.setActive(result);
			update(concert);
		}
		
		return result;
	}
	
	public String editConcertDisplay(Integer id,String amount){
		String result="";
		Concert concert=(Concert) get(Concert.class, id);
		if(concert!=null){
			concert.setBet(Double.parseDouble(amount.trim()));
			update(concert);
		}
		return result;
	}
	
	/**
	 * 演唱会升级流水更新
	 */
	@Override
	public void concertUpdateBet(List<Concert> list, Date now,ConcertDateType concertDateType,Integer type) throws Exception {

		for (int i = 0; i < list.size(); i++) {

			DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class);
			dc = dc.add(Restrictions.ge("createTime",DateUtil.fmtStandard(concertDateType.getXimaStart())));
			dc = dc.add(Restrictions.le("createTime",DateUtil.fmtStandard(concertDateType.getXimaEnd())));
			dc = dc.add(Restrictions.in("platform", new Object[]{"dt","nt","newpt","mg","ttg","agin","qt","png"} ));
			dc = dc.add(Restrictions.eq("loginname", list.get(i).getLoginname()));
			List<AgProfit> bets = proposalDao.findByCriteria(dc);

			Map<String, Double> after_mer = new HashMap<String, Double>();
			for (AgProfit ag : bets) {
				if (after_mer.containsKey(ag.getLoginname())) {
					after_mer.put(ag.getLoginname(),after_mer.get(ag.getLoginname())+ ag.getBettotal());
				} else {
					after_mer.put(ag.getLoginname(), ag.getBettotal());
				}
			}

			// 判断插入还是更新
			for (String key : after_mer.keySet()) {

				DetachedCriteria insertDc = DetachedCriteria.forClass(Concert.class);
				insertDc.add(Restrictions.eq("startTime",DateUtil.fmtStandard(concertDateType.getStart())));
				insertDc.add(Restrictions.eq("loginname", key));
				insertDc.add(Restrictions.eq("type", type));			
				insertDc.add(Restrictions.eq("active", 0));
				List<Concert> insertList = findByCriteria(insertDc);
				if (insertList == null || insertList.size() == 0)
					continue;
				else {
					Concert cct = insertList.get(0);
					cct.setBet(after_mer.get(key));
					cct.setLastTime(now);
					update(cct);
				}
			}
		}
	}
	
	@Override
	public String rPayProposal(String pno, String operator,
			String ip, String remark) {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null){
		    msg = "找不到该提案";
		}
		try {
			Users user = (Users)get(Users.class,proposal.getLoginname(),LockMode.UPGRADE);
			if(user!=null && 1==proposal.getMsflag()){
				if(user.getCredit()>=proposal.getAmount()){
					Double remit = proposal.getAmount();
					Double credit = user.getCredit();
					Double newCredit = credit - remit;
					user.setCredit(newCredit);
					update(user);
					logDao.insertCreditLog(user.getLoginname(), CreditChangeType.CASHOUT.getCode(), credit, remit, newCredit, "referenceNo:" + pno + ";" + StringUtils.trimToEmpty("rpay:"+remark));
					proposal.setFlag(ProposalFlagType.SUBMITED.getCode());
					proposal.setMstype(0);
					proposal.setUnknowflag(1);
					taskDao.auditTask(pno, operator, ip);
					logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
					update(proposal);
					msg = null;
				}else{
					msg="玩家额度不足";
				}
			}else{
				msg="提交异常";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}
	
	
	@Override
	public void dealSelfYouHuiData() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE, -10);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date());
		c2.add(Calendar.MINUTE, -5);
		//提案里面当前时间的前5分钟到前十分钟之间的自助优惠数据
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc.add(Restrictions.gt("executeTime", c.getTime())) ;
		dc.add(Restrictions.lt("executeTime", c2.getTime())) ;
		dc.add(Restrictions.eq("flag", 2));
		dc.add(Restrictions.in("type", new Object[]{590,591,593,594,595,572,705})) ;
		List<Proposal> proposals = this.findByCriteria(dc) ;
		if(null != proposals && proposals.size()>0){
			for (Proposal proposal : proposals) {
				try {
					DetachedCriteria recordsDc = DetachedCriteria.forClass(PreferentialRecord.class);
					recordsDc.add(Restrictions.eq("pno", proposal.getPno()));
					List<PreferentialRecord> records = this.findByCriteria(recordsDc) ;
					PreferentialRecord rec = null ;
					if(records.size() == 1){
						rec = records.get(0) ;
					}else{
						log.info(proposal.getPno()+"提案下没有优惠记录 ， 数据出现问题");
						continue ;
					}
					/*if(null == rec.getValidBet()){
						log.info(proposal.getPno()+"提案下没有投注额，获取投注额失败。");
						continue ;
					}*/
					if(rec.getValidBet() == null){
						DetachedCriteria betDc = DetachedCriteria.forClass(PlatformData.class);
						betDc = betDc.add(Restrictions.eq("platform", rec.getPlatform()));
						betDc = betDc.add(Restrictions.eq("loginname", rec.getLoginname()));
						betDc.setProjection(Projections.sum("bet")) ;
						Double betAmount = (Double) this.findByCriteria(betDc).get(0) ;
						
						rec.setValidBet(betAmount== null ?-1.0:betAmount);
						log.info(rec.getPlatform()+"   "+rec.getLoginname()+":重置投注额"+betAmount);
						this.update(rec);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String supplementPayOrder(Integer id, String loginname, Double amount, String operator) throws GenericDfhRuntimeException {
		String msg = null;
		ValidateAmountDeposit deposit = (ValidateAmountDeposit)get(ValidateAmountDeposit.class, id,  LockMode.UPGRADE);
		if (deposit == null)
			msg = "找不到该转账记录";
		else if(deposit.getStatus()!=2){
			msg = "该转账记录不是未匹配状态";
		}else{
			try {				
				DetachedCriteria c = DetachedCriteria.forClass(Users.class);
				c.add(Restrictions.eq("loginname", loginname));
				List<Users> users = getDataDao.findByCriteria(c);
				Users user = null;
				if(users != null && users.size()==1){
					user = users.get(0);
					
					deposit.setStatus(1);
					deposit.setTimecha(DateUtil.getTimecha(new Timestamp(deposit.getPayTime().getTime()),new Timestamp(deposit.getReadTime().getTime())));
					deposit.setOverTime(DateUtil.getOvertime(new Timestamp(deposit.getPayTime().getTime()),new Timestamp(deposit.getReadTime().getTime())));
					deposit.setRemark("额度验证存款补单：" + operator);
					
					Double fee = deposit.getFee();
					Proposal proposal = new Proposal();
					
					String pno = getDataDao.generateProposalPno("502");
					if(fee == null)
						fee = 0.0;
					
					Cashin cashin = new Cashin(pno, user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()), amount, "XXXXXXXXXXXXXXXX", deposit.getBankname(), "");
					cashin.setCashintime(DateUtil.now()); 
					
					Double maxAmount = 500d;
					/*************存款红利0.005*******************/
					if(user.getLevel() <= 10){
						//手续费限制
						Calendar cday = Calendar.getInstance();
						cday.set(Calendar.HOUR_OF_DAY, 0);
						cday.set(Calendar.MINUTE, 0);
						cday.set(Calendar.SECOND, 0);
						Date startday = cday.getTime();
						cday.set(Calendar.HOUR_OF_DAY, 23);
						cday.set(Calendar.MINUTE, 59);
						cday.set(Calendar.SECOND, 59);
						Date endday = cday.getTime();
						DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
						dc.add(Restrictions.eq("loginname", user.getLoginname()));
						dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
						dc.setProjection(Projections.sum("fee"));
						List list = getDataDao.findByCriteria(dc);
						//多笔
						if(list!=null && !list.isEmpty() && null!=list.get(0)){
							Double d = (Double)list.get(0);
							Double dam = maxAmount-d;//系统剩余可返还的手续费
							if(dam<=0){
								fee = 0.00;
							}else{
								fee = amount*0.005;
								if(fee>dam){
									fee = dam;
								}
							}
						}else{
							//只有一笔
							fee = amount*0.005;
							if(fee>maxAmount){
								fee = maxAmount;
							}
						}
					}else{
						fee = amount*0.005;
					}
					cashin.setFee(fee);
					proposal.setPno(pno);
					proposal.setProposer("system");
					proposal.setCreateTime(DateUtil.now());
					proposal.setType(502);
					proposal.setQuickly(user.getLevel());
					proposal.setLoginname(user.getLoginname());
					proposal.setAmount(amount+fee);
					proposal.setAgent(user.getAgent());
					proposal.setFlag(2);
					proposal.setWhereisfrom("system");
					proposal.setRemark("网银转账");
					proposal.setBankaccount(deposit.getAcceptName());
					proposal.setSaveway("网银");
					
					getDataDao.save(cashin);
					getDataDao.save(proposal);
					getDataDao.generateTasks(pno, "system");
					getDataDao.changeCredit(user, proposal.getAmount(), "CASHIN", pno, "");
					
					PayOrderValidation order = new PayOrderValidation();
					order.setUserName(loginname);
					order.setStatus("1");  // 已到帐
					order.setAmount(amount);
					order.setOriginalAmount(0.0);   //对于补单产生的存款订单，计划存入金额统一记0  
					order.setCreateTime(new Date());
					order.setArriveTime(deposit.getPayTime());
					order.setBankcard(deposit.getAcceptNo());
					order.setTransferID(id);
					order.setRemark(operator + "补单");
					save(order);
					update(deposit);
					
					// 设置用户 isCashin字段
					if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
						user.setIsCashin(Constants.FLAG_TRUE.intValue());
						getDataDao.update(user);
					}
					//改变我方银行的金额
					getDataDao.changeAmountByName(proposal.getBankaccount(), proposal.getAmount()-fee,pno);
					getDataDao.excuteTask(pno, "system", "");
					getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);
				
					logDao.insertOperationLog(operator, OperationLogType.EXCUTE,  "额度验证存款补单, ID :" + id);
					
					msg = null;	
				} else{
					msg = "用户:"+ loginname +"不存在";
				}
			}catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}
	
	@Override
	public String executePtCommissionsService(String operator , String ids) {
		String [] idarray = ids.split(",") ;
		logDao.insertOperationLog(operator, OperationLogType.EXCUTE,  "老虎机日结佣金");
		String[] agentNames = this.getAgentExcept();
		for (int i = 0; i < idarray.length; i++) {
			String [] id = idarray[i].split("@");
			log.info(idarray[i]);
			PtCommissionsId commissionsId = new PtCommissionsId(id[0], DateUtil.parseDateForStandard(id[2]), id[1]);
			PtCommissions commissions = (PtCommissions) proposalDao.get(PtCommissions.class, commissionsId) ;
			if(commissions.getFlag() == 1){
				log.info(idarray[i]+"  已经添加过日结佣金");
				commissions.setRemark(idarray[i]+"  已经添加过日结佣金");
				getDataDao.update(commissions);
				continue ;
			}
			
			
			//防止后台人员错执行
			Users user = getFindUser(id[0]) ;
			boolean flag = false;
			if(user != null && agentNames != null && agentNames.length > 0){
				
				for(int j = 0; j < agentNames.length; j++){
					if(user.getLoginname().equalsIgnoreCase(agentNames[j])){
						flag =true;
						log.info("该代理用户：" + user.getLoginname() + "，不执行日结佣金");
						commissions.setRemark("该代理用户：" + user.getLoginname() + " 不执行日结佣金");
						commissions.setFlag(1);
						commissions.setExcuteTime(DateUtil.convertToTimestamp(new Date()));
						getDataDao.update(commissions);
						break;
					}
				}
			}
			if(flag){
				continue;
			}
			
			if(commissions.getId().getPlatform().equals("slotmachine")){
				getDataDao.changeAgentSlotCreditBySql(user, commissions.getAmount(), CreditChangeType.CHANGE_COMMISSIONSDAY.getCode(), idarray[i], "老虎机日结佣金");
			}else if(commissions.getId().getPlatform().equals("liveall")||commissions.getId().getPlatform().equals("sports")||commissions.getId().getPlatform().equals("lottery")){
				//每个月的1号到5号不给添加到真人主账户
				int date = Calendar.getInstance().get(Calendar.DATE);
				
				DetachedCriteria dc = DetachedCriteria.forClass(AgentVip.class);
				dc.add(Restrictions.eq("id.agent", commissions.getId().getAgent())) ;
				dc.addOrder(Order.desc("createtime")) ;
				List<AgentVip> vips = proposalDao.findByCriteria(dc);
				if(null == vips || vips.size()==0 || vips.get(0).getLevel() == 0){
					if(date>=1 && date<=5){
						log.info("每个月的1号到5号是真人佣金提款时间，不允许添加到真人账号");
						log.error(commissions.getId().getAgent()+"被"+operator+"错误添加日结佣金，今天是"+date+"号");
						commissions.setRemark(commissions.getId().getAgent()+"错误添加日结佣金，今天是"+date+"号");
						getDataDao.update(commissions);
						continue ;
					}
				}
				getDataDao.changeCredit(user, commissions.getAmount(), CreditChangeType.CHANGE_COMMISSIONSDAY.getCode(), idarray[i], "日结佣金");
			}
			
			commissions.setExcuteTime(DateUtil.convertToTimestamp(new Date()));
			commissions.setFlag(1);
			commissions.setRemark("执行成功");
			getDataDao.update(commissions);
		}
		
		return "执行成功";
	}
	
	private String[] getAgentExcept() {
		String[] names = null;
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		dc = dc.add(Restrictions.eq("typeNo", "type200"));
		dc = dc.add(Restrictions.eq("itemNo", "001"));
		dc = dc.add(Restrictions.eq("flag", "否"));
		List<SystemConfig> list=this.findByCriteria(dc);
		if(list != null && list.size() > 0){
			SystemConfig syscon = list.get(0);
			String value = syscon.getValue();
			if(StringUtils.isNotBlank(value)){
				if(value.contains("，")){
					value = value.replace("，", ",");
				}
				if(value.endsWith(",")){
					value = value.substring(0, value.length() - 1);
				}
				names = value.split(",");
				log.info("不执行日结佣金的代理账号：" + Arrays.asList(names));
			}
		}
		
		return names;
	}
	
	@Override
	public String executeAgentVipService(String operator , String ids , Integer level) {
		String [] idarray = ids.split(",") ;
		logDao.insertOperationLog(operator, OperationLogType.EXCUTE,  "代理VIP等级修改");
		
		for (int i = 0; i < idarray.length; i++) {
			String [] id = idarray[i].split("@");
			log.info(idarray[i]);
			AgentVipId agentVipId = new AgentVipId(id[0], id[1]) ;
			AgentVip agentVip = (AgentVip) proposalDao.get(AgentVip.class, agentVipId) ;
			if(null != agentVip){
				agentVip.setLevel(level);
				proposalDao.update(agentVip);
			}
		}
		return "执行成功";
	}

	@Override
	public String xima4GPI() {
		Date bbinkeno_starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date bbinkeno_endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		//获取有游戏记录的玩家、投注额、以及输赢值
		List<GPI4Xima> userList;
		try {
			userList = proposalDao.getGPIXimaList(bbinkeno_starttime);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		if(null == userList || userList.size() == 0){
			return "没有游戏数据";
		}
		for (GPI4Xima item : userList) {
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}
			double newrate =getSlotRate(user);
			
			Double gpiBet = item.getBetAmount();//投注额
			XimaVO ximaObject = new XimaVO(gpiBet, user.getLoginname(), newrate);
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			//ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.GPISELFXIMA, newrate, user.getPtrebate()) ;
			
			String remark = "GPI系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.proposalDao.save(xima);
				this.proposalDao.save(proposal);
				
			Double netWin = item.getProfit();//纯赢（纯输）
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户GPI平台输赢值");
			agProfit.setPlatform("gpi");
			agProfit.setBettotal(gpiBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	

	@Override
	public Page queryWeeklySlotsMatch(HttpServletRequest request, String startTime, String endTime,
			Integer pageIndex, Integer size) throws Exception{
		if ((size == null) || (size.intValue() == 0))
			size = Page.PAGE_DEFAULT_SIZE;
		if (pageIndex == null)
			pageIndex = Page.PAGE_BEGIN_INDEX;
		Page page = new Page();
		
		//platform = platform.equals("pt")?"newpt":platform;
		
		//查询体验金周竞赛数据
		StringBuffer c_sql = new StringBuffer("select count(sm.loginname) from slots_match_weekly sm where");
		c_sql.append(" 1=1 and sm.startTime='"+ startTime +"' and sm.endTime='"+ endTime +"';");
		Integer count = Integer.valueOf(this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(c_sql.toString()).uniqueResult().toString());
		//count = count>200?200:count; //只查询前200名 
		c_sql = null;
		
		if (count <= 0){
			page.setTotalRecords(0);
			page.setSize(size);
			page.setTotalPages(0);
			page.setPageNumber(0);
			page.setPageContents(null);
			page.setNumberOfRecordsShown(0);
			return page;
		}
		
		final StringBuffer sql = new StringBuffer("select sm.loginname,sm.startTime,sm.endTime,sm.getTime,sm.win,sm.platform from SlotsMatchWeekly sm where");
		sql.append(" 1=1 and sm.startTime='"+startTime+"' and sm.endTime='"+endTime+"'");
		
		int pages = PagenationUtil.computeTotalPages(count, size).intValue();
		final int limit = (pageIndex-1)*size;
		final int f_size = size;
		//sql.append(" group by sm.loginname order by sum(sm.win) asc");
		sql.append(" order by sm.win desc,sm.getTime asc");
		List<SlotsMatchWeekly> agList = this.getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createQuery(sql.toString());
				query.setFirstResult(limit);
				query.setMaxResults(f_size);
				return query.list();
			}
		});
		//find(sql.toString(),new Object[]{platform,MatchDateUtil.parseDatetime(startTime),MatchDateUtil.parseDatetime(endTime)});
		
		page.setTotalRecords(count);
		page.setSize(size);
		page.setTotalPages(pages);
		page.setPageNumber(pageIndex);
		page.setPageContents(agList);
		page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		return page;
	}

	@Override
	public String xima4KenoBC() {
		Date bbinkeno_starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date bbinkeno_endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		//获取有游戏记录的玩家、投注额、以及输赢值
		List<GPI4Xima> userList;
		try {
			userList = proposalDao.getKenoBCXimaList(bbinkeno_starttime);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		if(null == userList || userList.size() == 0){
			return "没有游戏数据";
		}
		for (GPI4Xima item : userList) {
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}
			double newrate = getSlotRate(user);
			
			Double kgbcBet = item.getBetAmount();//投注额
			XimaVO ximaObject = new XimaVO(kgbcBet, user.getLoginname(), newrate,this.getRebateLimit(user));
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			//ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.GPISELFXIMA, newrate, user.getPtrebate()) ;
			
			String remark = "Kneo2百家乐系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.proposalDao.save(xima);
				this.proposalDao.save(proposal);
				
			Double netWin = item.getProfit();//纯赢（纯输）
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户KENO2百家乐平台输赢值");
			agProfit.setPlatform("keno2");
			agProfit.setBettotal(kgbcBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	
	}

	@Override
	public void excuteAutoXimaProposal(Proposal proposal) throws Exception {
		Xima xima = (Xima) this.getGameinfoDao().get(Xima.class, proposal.getPno());
		if (xima==null) {
			log.info("提案号："+proposal.getPno()+"存在问题，请核实.");
			return;
		}
		log.info("正在处理提案号："+proposal.getPno()+"...");
		this.getTradeDao().changeCredit(xima.getLoginname(), xima.getTryCredit(), CreditChangeType.XIMA_CONS.getCode(), proposal.getPno(), "ok");
		proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
		proposal.setRemark(proposal.getRemark() + ";执行:");
		proposal.setExecuteTime(DateUtil.now());
		
		update(proposal);
		this.getTaskDao().excuteTask(proposal.getPno(), "system", "127.0.0.1");
		this.getLogDao().insertOperationLog("system", OperationLogType.EXCUTE, "ip:127.0.0.1;pno:" + proposal.getPno());
	}
	
	@Override
	public Double getDoubleValueBySql(String sql, Map<String, Object> params) {
		return proposalDao.getDoubleValueBySql(sql, params);
	}

	@Override
	public List getListBySql(String sql, Map<String, Object> params) throws Exception {
		return proposalDao.getListBySql(sql, params);
	}

	@Override
	public Integer getCount(String sql, Map<String, Object> params) {
		return userDao.getCount(sql, params);
	}
	
	@Override
	public int excuteSql(String sql, Map<String, Object> params) {
		return userDao.excuteSql(sql, params);
	}
	
	@Override
	public void exeSinglePrivilege(Integer id, String loginName, Double amount, String remark) {
		Users user = (Users) get(Users.class, loginName);
		if(null == user){
			return;
		}
		String pno = ProposalType.PRIZE.getCode() + RandomStringUtils.randomAlphanumeric(15);
		Prize prize = new Prize(pno, user.getRole(), loginName, amount, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.PRIZE.getCode(), user.getLevel(), loginName, amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
		proposal.setExecuteTime(DateUtil.now());
		save(prize);
		save(proposal);
		//修改优惠状态
		Map<String, Object> params = new HashMap<String, Object>();
		String pSqlStr = "update privilege set status = :newStatus where id = :Id";
		params.put("newStatus", 1);
		params.put("Id", id);
		userDao.excuteSql(pSqlStr, params);
		//增加玩家额度
		tradeDao.updateUserCreditSql(user, amount);
		//额度变化日志
		logDao.insertCreditLog(user.getLoginname(), CreditChangeType.PRIZE.getCode(), user.getCredit(), amount, 
				Arith.add(user.getCredit(), amount), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));
	}
	
	/**
	 * 	Double betCredit投注额
	 *   NTProfit  DT使用NT的model即可
	 * @param loginname
	 * @return
	 */
	public String autoAddXimaDtProposal(List<NTProfit> dtData){
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		for (NTProfit nTProfit : dtData) {
			Users user = (Users) this.getUserDao().get(Users.class,nTProfit.getLoginname());
			if (user==null) {
				log.info("用户："+nTProfit.getLoginname()+"不存在");
				continue;
			}
			//定为新会员及忠实会员 0.6% ，星级、金牌、白金 0.8% ，钻石至尊 1%
			double newrate = getSlotRate(user);
			XimaVO ximaObject = new XimaVO(nTProfit.getBetCredit(),user.getLoginname(),newrate,this.getRebateLimit(user));
			/***********************************/
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.DTSELFXIMA, newrate, this.getRebateLimit(user)) ;
			/***********************************/
			String remark = "dt系统洗码";
			
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), nTProfit.getLoginname(), nTProfit.getAmount(), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户DT平台输赢值");
			agProfit.setPlatform("dt");
			agProfit.setBettotal(nTProfit.getBetCredit());
			this.proposalDao.save(agProfit); 
		}
		return null;
	}
	
	@Override
	public String autoAddXimaNTwoProposal(List<PlatformData> profits) {
		String paltformCode = "n2live";
		if (null== profits && profits.isEmpty()){
			return "profits is null";
		}
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

		for (PlatformData profit : profits) {
			String loginname = profit.getLoginname();
			Users user = (Users) this.getUserDao().get(Users.class,loginname);
			if (user==null) {
				log.info("用户："+loginname+"不存在");
				continue;
			}
			//洗码比例
			Double betCredit=profit.getBet();//投注额
			Double amount=profit.getProfit(); //记录的Amount为平台盈利
			
//			double newRate = getNTwoRuleRate(user.getLevel());
//			XimaVO ximaObject = new XimaVO(betCredit,user.getLoginname(),newRate);
			//TODO Double rebate 先绑 user.getAgrebate()
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
//			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.NTWOSELFXIMA, newRate, user.getAgrebate()) ;
//			String remark = paltformCode+"系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
//			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
//			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
//			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
//					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
//			this.proposalDao.save(xima);
//			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户N2Live平台输赢值");
			agProfit.setPlatform(paltformCode);
			agProfit.setBettotal(betCredit);
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	
	 public String modifyPlatformRecord(String loginname, String platform){
		   
		   log.info("******************   modifyPlatformRecord start  ************************");
		   
		  String createtime=new DateUtil().fmtYYYY_MM_DD(new Date())+" 00:00:00";
		   
		 
		  String sqlDelete2 ="delete a from agprofit a , (Select pno from proposal where type=507 and createTime>'"+createtime+"' and flag=1 ) p where a.pno = p.pno;";
		  
		   SQLQuery query2=  userDao.getSessionFactory().getCurrentSession().createSQLQuery(sqlDelete2);
		   query2.executeUpdate();
		   
	 
	      String sql3="delete a from xima a , (select pno from proposal where type=507 and flag=1 and createTime>'"+createtime+"' ) p where a.pno = p.pno;";
		  SQLQuery query3 = userDao.getSessionFactory().getCurrentSession().createSQLQuery(sql3);
		  query3.executeUpdate();
		  
		  String sql4="delete  from systemlogs where  createTime>'"+createtime+"' and action='"+SystemLogType.getPlatform(platform.toUpperCase())+"' ;";
		  SQLQuery query4 = userDao.getSessionFactory().getCurrentSession().createSQLQuery(sql4);
		  query4.executeUpdate();
		  
		  
		  String sql="delete  from proposal where type=507 and flag=1 and createTime>'"+createtime+"' ;";
		  SQLQuery query = userDao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		  query.executeUpdate();
		  
		  logDao.insertOperationLog(loginname, OperationLogType.MODIFYPLATFORMRECORD, "多表");

		 if("pb".equals(platform)){
			 String beforeDate = DateUtil.getchangedDate(-1)+" 00:00:00";
			 String pbsql = "delete from pb_data where  settle_time >='"+beforeDate+"'";
			 getDataDao.excuteSql(pbsql, new HashMap());
		 }

		  log.info("******************   modifyPlatformRecord end  ************************");
	      return "操作成功";
	   }
	   
	  public String modifySinglePlatform(String loginname,String platform){
		   
		   log.info("****************** "+platform+": modifySinglePlatform start  ************************");
		   
		  String createtime=new DateUtil().fmtYYYY_MM_DD(new Date())+" 00:00:00";
		   
		  String sql2="delete  from agprofit where  platform='"+platform+"' and createTime>'"+createtime+"' ;";

//		  String sql2="delete a from agprofit a ,  (select pno from proposal where type=507 and flag=1 and createTime>'"+createtime+"' ) p where a.pno = p.pno;";
		  SQLQuery query2 = userDao.getSessionFactory().getCurrentSession().createSQLQuery(sql2);
		  query2.addEntity(AgProfit.class);
		  query2.executeUpdate();
		  
		  String sql4="delete  from systemlogs where  createTime>'"+createtime+"' and action='"+SystemLogType.getPlatform(platform.toUpperCase())+"' ;";
		  SQLQuery query4 = userDao.getSessionFactory().getCurrentSession().createSQLQuery(sql4);
		  query4.executeUpdate();
		 
		  
		   logDao.insertOperationLog(loginname, OperationLogType.MODIFYPLATFORMRECORD, "单表 agprofit");
		  log.info("******************   modifySinglePlatform end  ************************");
		  
		  return "操作成功";
	   }
	  
	  public String insertSystemLogs(String operator,SystemLogType systemLogType){
			DetachedCriteria dc = DetachedCriteria.forClass(Systemlogs.class);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String applyDate=formatter.format(new Date());
			dc = dc.add(Restrictions.eq("applyDate",applyDate ));
			dc = dc.add(Restrictions.eq("action", systemLogType.getText()));
			List list =findByCriteria(dc);
			if (list!=null&&list.size()>0) {
				return "洗码已经操作或者正在操作!!!!";
			}else {
				Systemlogs ximalogs=new Systemlogs(operator,systemLogType.getText(), DateUtil.now(),applyDate,"");
				save(ximalogs);
			}
		
			return null;
		}
	  
	  public String modifyAgSlotPlatform(Date date){
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		  String[] stringArray = { "XIN", "BG", "ENDO", "NYX" };  
		  ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(stringArray));  
		  String cagent="B20";
		  Integer pageSize=1000;
		  
		  try {
			
		  Calendar calendarLastTime=Calendar.getInstance();
		  calendarLastTime.setTime(date);
		  
		  Date startTime=calendarLastTime.getTime();
		  calendarLastTime.set(Calendar.MINUTE, 59);
		  calendarLastTime.set(Calendar.SECOND, 59);
		  Date endTime=calendarLastTime.getTime();
		  
		  for (int i = 0; i < arrayList.size(); i++) {
			  Map<String , AgDataRecord> map=new HashMap<String, AgDataRecord>();
				  String xml=AgRecordUtils.getBetRecord(arrayList.get(i),startTime, endTime, cagent, pageSize,1);
				  map=AGSlotThread.getReRecordData(xml, startTime,endTime,cagent,arrayList.get(i),pageSize,1);
			  
			  for (String key : map.keySet()) {
					 AgDataRecord agDataRecord=map.get(key);
					 
						DetachedCriteria dc = DetachedCriteria.forClass(AgDataRecord.class);
						dc.add(Restrictions.eq("playerName", agDataRecord.getPlayerName()));
						dc = dc.add(Restrictions.eq("startTime",startTime));
						dc = dc.add(Restrictions.eq("platform", agDataRecord.getPlatform()));
						List list=findByCriteria(dc);
						
						if (list.size()==0) {
							  save(agDataRecord);
							log.info("更新用户补录数据:"+agDataRecord.getPlayerName()+" ----投注次数:"+agDataRecord.getBetcount()+" ----开始时间:"+agDataRecord.getStartTime());
						}else {
							 AgDataRecord aRecord=(AgDataRecord) list.get(0);
							   if (aRecord.getBetcount().intValue()<agDataRecord.getBetcount().intValue()) {
								   aRecord.setBetamount(agDataRecord.getBetamount());
								   aRecord.setBetcount(agDataRecord.getBetcount());
								   aRecord.setValidbetamount(agDataRecord.getValidbetamount());
								   aRecord.setNetamount(agDataRecord.getNetamount());
								   aRecord.setCreateTime(agDataRecord.getCreateTime());
							       update(aRecord);
						  	log.info("修改用户补录数据:"+agDataRecord.getPlayerName()+" ----投注次数:"+agDataRecord.getBetcount()+" ----开始时间:"+agDataRecord.getCreateTime());

							   }
						}
						
				}
		      }
		  } catch (Exception e) {
			  e.printStackTrace();
			  return "操作超时、请在使用功能!!!";
		  }
		  
		  return "操作成功";
	   }


	@Override
	public List<AgSlotXima> sqlQueryList(String start, String end, String platform) {
		return proposalDao.sqlQueryList(start, end, platform);
	}

	@Override
	public List<AgSlotXima> sqlAutoQueryList(String start, String end) {
		return proposalDao.sqlQueryAginSlotList(start, end);
	}

	@Override
	public String autoAddXimaBbinProposal(List sbaDatas,String gamekind) {
		try {
			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);

			if(sbaDatas==null || sbaDatas.size()<=0 ||sbaDatas.get(0)==null){
				return "数据为空！";
			}
			for (int i = 0; i < sbaDatas.size(); i++) {
				JSONObject jobj = (JSONObject) sbaDatas.get(i);

				System.out.println("loginname:"+jobj.get("loginname"));
				System.out.println("bet:"+jobj.get("bet"));
				System.out.println("amount:"+jobj.get("amount"));

				String string=(String) jobj.get("loginname");
				Double bet = Double.parseDouble(jobj.get("bet")+"");	//投注额
				Double amount = Double.parseDouble(jobj.get("amount")+""); //玩家总输赢

				String loginname = null;
				if(string.startsWith("zb")){
					loginname = string.substring(2,string.length());
				}else{
					loginname = string.substring(1,string.length());
				}
				Users user = (Users) this.getUserDao().get(Users.class,loginname);
				if (user==null) {
					log.info("用户："+loginname+"，不存在");
					continue;
				}

				double newrate = getSlotRate(user);
				Double rebateLimit = getRebateLimit(user);
				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,rebateLimit);

				/***********************************/
				//进行自助反水缩减   昨天12点到今天12点之间的自助反水

				DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", starttime));
				c.add(Restrictions.le("createTime", endtime));
				c.add(Restrictions.eq("loginname", user.getLoginname()));

				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));

				c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
				c.setProjection(Projections.property("pno"));
				List list = this.proposalDao.findByCriteria(c);
				if(list!=null && !list.isEmpty()){
					DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", list.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = this.proposalDao.findByCriteria(x);
					Double d = 0.0;
					Double dremain =0.0;
					if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
						if((Double)sumx.get(0)*newrate<user.getEarebate()){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
							dremain = user.getEarebate()-(Double)sumx.get(0)*newrate;//剩下可反水额度
							d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
							if(d<0){
								d=0.0;
							}
						}
						ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
					}
				}
				/***********************************/
				String platform = gamekind.equals("3")?"bbinvid":"bbinele";
				String remark = platform+"系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				if(gamekind.equals("3")) {//真人才有反水
					this.proposalDao.save(xima);
					this.proposalDao.save(proposal);
				}
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户"+platform+"平台输赢值");
				agProfit.setPlatform(platform);
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}


	//泛亚电竞上传返水
	@Override
	public String autoAddXimaFanyaProposal(List resultList) {
		try {
			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);

			for (int i = 0; i < resultList.size(); i++) {
				Object[] jobj = (Object[]) resultList.get(i);
				String loginname=(String) jobj[0];
				Double bet = Double.parseDouble(jobj[1]+"");//投注额
				Double amount = Double.parseDouble(jobj[2]+""); //玩家总输赢
				Users user = (Users) this.getUserDao().get(Users.class,loginname);
				if (user==null) {
					log.info("用户："+loginname+"，不存在");
					continue;
				}
				//固定比例
				double newrate = 0.004;

				Double rebateLimit = getRebateLimit(user);

				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,rebateLimit);

				/***********************************/
				String remark = "fanya系统洗码";//
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");

				this.proposalDao.save(xima);
				this.proposalDao.save(proposal);

				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户Fanya平台输赢值");
				agProfit.setPlatform("fanya");
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}


	/**
	 * 添加NT系统洗码
	 * 
	 * @param ntDatas
	 *            NT输赢数据
	 */
	public String xima4NT(List<Bean4Xima> betsList) {
		
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		for (Bean4Xima item : betsList) {
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}
			//double newrate = user.getAgrate().doubleValue();
			double newrate = getSlotRate(user);
			
			//定为新会员及忠实会员 0.6% ，星级、金牌、白金 0.8% ，钻石至尊 1%
			
			String nowdate = DateUtil.fmtyyyyMMdd(new Date());
			//TODO NT与TTG返回相同，但不参与活动
			if(nowdate.equals("20160805") || nowdate.equals("20160812") || nowdate.equals("20160819")){

				newrate = 0.015;
			}
			
//			Double newrebate = null ;
//			if(user.getLevel() <= VipLevel.BAIJIN.getCode()){
//				newrebate = 28888.00 ;
//			}else{
//				newrebate = 99999999999.00 ;
//			}
			
			Double betCredit=item.getBetAmount();//投注额
			//agbet=agbet*6;
			//String str=getStringValue(i, 10).trim();//如果玩家赢钱，导入的数据为负数  
			//Double agprofit =-1*Double.parseDouble(str);
			Double amount=item.getProfit(); //NT记录的amount就为平台盈利
			//agprofit=agprofit*6;
			XimaVO ximaObject = new XimaVO(betCredit,user.getLoginname(),newrate,this.getRebateLimit(user));
			/***********************************/
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.NTSELFXIMA, newrate, this.getRebateLimit(user));
			/***********************************/
			String remark = "nt系统洗码";
			
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户NT平台输赢值");
			agProfit.setPlatform("nt");
			agProfit.setBettotal(betCredit);
			this.proposalDao.save(agProfit); 
		}
		
		return null;
	}
	
	@Override
	public String xima4MG(List<Bean4Xima> betsList) {
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		for (Bean4Xima item : betsList) {
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}
			//拷贝NT洗码的，暂定
			double newrate = getSlotRate(user);
			
			Double mgBet = item.getBetAmount();//投注额
			XimaVO ximaObject = new XimaVO(mgBet, user.getLoginname(), newrate,this.getRebateLimit(user));//必须要加上getRebateByUserLevel
			
			/***********************************/
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.MGSELFXIMA, newrate, this.getRebateLimit(user)) ;
			/***********************************/
			
			String remark = "MG系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.proposalDao.save(xima);
				this.proposalDao.save(proposal);
				
			Double netWin = item.getProfit();//纯赢（纯输）
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户MG平台输赢值");
			agProfit.setPlatform("mg");
			agProfit.setBettotal(mgBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	
	@Override
	public String systemXima4Common(List<XimaDataVo> tempList, Date starttime, Date endtime, String plantform, String remark, ProposalType proposalType) {
		String msg = null;
		
		try {
			for (XimaDataVo ximaDataVo : tempList) {
				
				double newrate = getSlotRate(ximaDataVo.getUser());
				
				//单日返水限制
				Double rebateLimit = this.getRebateLimit(ximaDataVo.getUser());
				
				XimaVO ximaObject = new XimaVO(ximaDataVo.getTotal_bet(),ximaDataVo.getLoginname(),newrate,rebateLimit);
				/***********************************/
				//减自助反水的时间
				if(proposalType != null){
					ximaObject = cutSelfXima(ximaObject, starttime, endtime, proposalType, newrate, rebateLimit) ;
				}
				/***********************************/
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, ximaDataVo.getUser().getRole(), ximaDataVo.getUser().getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), ximaDataVo.getUser().getLevel(), ximaDataVo.getLoginname(), ximaObject.getXimaAmouont(),ximaDataVo.getUser().getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.save(xima);
				this.save(proposal);
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), ximaDataVo.getUser().getLevel(), ximaDataVo.getUser().getLoginname(),ximaDataVo.getTotal_win(), ximaDataVo.getUser().getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户" + plantform.toUpperCase() + "平台输赢值");
				agProfit.setPlatform(plantform);
				agProfit.setBettotal(ximaDataVo.getTotal_bet());
				this.save(agProfit);
			}
		} catch (Exception e) {
			log.error(plantform + "洗码错误：", e);
			msg = e.toString();
		}
		
		return msg ;
	}

	@SuppressWarnings("rawtypes")
	public Page queryForPagenation(String recordSql, String totalSql, Integer pageIndex, Integer pageSize) {

		Page page = new Page();

		if (pageSize == null || pageSize.intValue() == 0) {

			pageSize = Page.PAGE_DEFAULT_SIZE;
		}
		page.setSize(pageSize);

		if (pageIndex == null) {

			pageIndex = Page.PAGE_BEGIN_INDEX;
		}

		try {

			Integer totalRecords = Integer.parseInt(String.valueOf(this.getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(totalSql).list().get(0)));
			log.info("totalRecords:" + totalRecords);

			Integer totalPages = PagenationUtil.computeTotalPages(totalRecords, pageSize);

			page.setTotalRecords(totalRecords);
			page.setTotalPages(totalPages);

			if (pageIndex > totalPages) {

				pageIndex = Page.PAGE_BEGIN_INDEX;
			}

			page.setPageNumber(pageIndex);

			List list = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(recordSql)
					.list();

			page.setPageContents(list);

			if (null != list && !list.isEmpty()) {

				page.setNumberOfRecordsShown(Integer.valueOf(list.size()));
			} else {

				page.setNumberOfRecordsShown(0);
			}
		} catch (Exception e) {

			log.error("执行queryForPagenation方法发生异常，异常信息：" + e.getMessage());
		}

		return page;
	}
	
	 public String xima4PNG(List<Bean4Xima> betsList) {
		  Date bbinkeno_starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		  Date bbinkeno_endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		  for (Bean4Xima item : betsList) {
			  Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			  if (user==null) {
				  log.info("用户：" + item.getUserName() + "不存在");
				  continue;
			  }
			  
				double newrate = getSlotRate(user);
				
				//单日返水限制
				Double rebateLimit = this.getSlotRate(user);
			  
			  Double Bet = item.getBetAmount();//投注额
			  XimaVO ximaObject = new XimaVO(Bet,user.getLoginname(),newrate,rebateLimit);
			  
			  /***********************************/
			  //进行自助反水缩减   昨天0点到今天0点之间的自助反水
			  ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.PNGSELFXIMA, newrate, user.getAgrebate()) ;
			  /***********************************/
			  
			  String remark = "PNG系统洗码";
			  String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			  log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			  Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
			  Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					  Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			  this.getTaskDao().generateTasks(pno, "system");
			  this.proposalDao.save(xima);
			  this.proposalDao.save(proposal);
			  
			  Double netWin = item.getProfit();//纯赢（纯输）
			  AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户PNG平台输赢值");
			  agProfit.setPlatform("png");
			  agProfit.setBettotal(Bet);
			  this.proposalDao.save(agProfit);
		  }
		  return null;
	  }
 /*
  * 中心钱包版本
  */
 public String xima4QT(List<Bean4Xima> betsList) {
	  Date bbinkeno_starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
	  Date bbinkeno_endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
	  for (Bean4Xima item : betsList) {
		Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
	    if (user==null) {
			log.info("用户：" + item.getUserName() + "不存在");
			continue;
		}
		  
		double newrate = getSlotRate(user);
		String nowdate = DateUtil.fmtyyyyMMdd(new Date());
		if(nowdate.equals("20160805") || nowdate.equals("20160812") || nowdate.equals("20160819")){

			newrate = 0.015;
		}
		  
		  Double Bet = item.getBetAmount();//投注额
		  XimaVO ximaObject = new XimaVO(Bet,user.getLoginname(),newrate,this.getRebateLimit(user));
		  
		  /***********************************/
		  //进行自助反水缩减   昨天0点到今天0点之间的自助反水
		  ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.QTSELFXIMA, newrate, user.getAgrebate()) ;
		  /***********************************/
		  
		  String remark = "QT系统洗码";
		  String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
		  log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
		  Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
		  Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
				  Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
		  this.getTaskDao().generateTasks(pno, "system");
		  this.proposalDao.save(xima);
		  this.proposalDao.save(proposal);
		  
		  Double netWin = item.getProfit();//纯赢（纯输）
		  AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户QT平台输赢值");
		  agProfit.setPlatform("qt");
		  agProfit.setBettotal(Bet);
		  this.proposalDao.save(agProfit);
	  }
	  return null;
 }

	public String addDTFishBatchXimaProposal(List<Bean4Xima> dtData) {

		Date starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1 * 24 - 0));
		Date endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		if (dtData == null || dtData.size() <= 0 || dtData.get(0) == null) {
			return "数据为空！";
		}
		try {
			String dateStr = DateUtil.formatMMddHH(new Date());
			for (Bean4Xima dt : dtData) {
				String loginName = dt.getUserName().toLowerCase();
				Users user = (Users) this.getUserDao().get(Users.class, loginName);
				if (user == null) {
					log.info("用户：" + loginName + "，不存在");
					continue;
				}
				dt.setProfit(dt.getBetAmount() - dt.getProfit()); // 输赢值

				double newrate = 0.004;
				
				starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -24);
				endtime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

				XimaVO ximaObjectTiger = new XimaVO(dt.getBetAmount(), user.getLoginname(), newrate);
				String remark = "HYG系统洗码";

				String pno = ProposalType.XIMA.getCode() + "f" + dateStr + RandomStringUtils.randomAlphanumeric(10);
				log.info("正在处理提案号hyg：" + pno + ",反水金额：" + Math.round(ximaObjectTiger.getXimaAmouont() * 100.00) / 100.00
						+ "...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付",
						ximaObjectTiger.getValidBetAmount(), ximaObjectTiger.getXimaAmouont(),
						DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime),
						ximaObjectTiger.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(),
						user.getLevel(), user.getLoginname(), ximaObjectTiger.getXimaAmouont(), user.getAgent(),
						ProposalFlagType.AUDITED.getCode(), Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
//				this.userDao.save(xima);
//				this.proposalDao.save(proposal);
				AgProfit agProfit = new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(),
						user.getLevel(), user.getLoginname(), dt.getProfit(), user.getAgent(),
						ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户HYG平台输赢值");
				agProfit.setPlatform("hyg");
				agProfit.setBettotal(dt.getBetAmount());
				this.proposalDao.save(agProfit);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public String addNTwoBatchBaobiaoXimaProposal(List<Bean4Xima> profits) {
		String paltformCode = "n2live";
		if (null== profits && profits.isEmpty()){
			return "profits is null(沒用戶下注)";
		}
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);

		for (Bean4Xima profit : profits) {
			String loginName = profit.getUserName().toLowerCase();
			Users user = (Users) this.getUserDao().get(Users.class, loginName);
			if (user == null) {
				log.info("用户：" + loginName + "，不存在");
				continue;
			}

			Double betCredit=profit.getBetAmount();//投注额
			Double amount=profit.getProfit(); //记录的Amount为平台盈利
			double newRate = 0.004;

//			Timestamp date = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -11);
			XimaVO ximaObject = new XimaVO(profit.getBetAmount(), user.getLoginname(), newRate);
//			TODO Double rebate 先绑 user.getAgrebate()
//			进行自助反水缩减   昨天0点到今天0点之间的自助反水
//			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.NTWOSELFXIMA, newRate, user.getAgrebate()) ;
			String remark = paltformCode+"系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			/*log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);*/
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginName, amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户N2Live平台输赢值");
			agProfit.setPlatform(paltformCode);
			agProfit.setBettotal(betCredit);
			this.proposalDao.save(agProfit);
		}
		return null;
	}


	/**
	 * 开元棋牌
	 */
	@Override
	public String autoAddXimaKyqpProposal(List resultList) {
		try {
			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);

			if(resultList==null || resultList.size()<=0 ||resultList.get(0)==null){
				return "数据为空！";
			}

			for (int i = 0; i < resultList.size(); i++) {
				JSONObject jobj = (JSONObject) resultList.get(i);

				System.out.println("loginname:"+jobj.get("loginname"));
				System.out.println("bet:"+jobj.get("bet"));
				System.out.println("amount:"+jobj.get("amount"));

				String string=(String) jobj.get("loginname");
				Double bet = Double.parseDouble(jobj.get("bet")+"");	//投注额
				Double amount = Double.parseDouble(jobj.get("amount")+""); //玩家总输赢

				String loginname = null;
				if(string.startsWith("zb")){
					loginname = string.substring(2,string.length());
				}else{
					loginname = string.substring(1,string.length());
				}

				Users user = (Users) this.getUserDao().get(Users.class,loginname);
				if (user==null) {
					log.info("用户："+loginname+"，不存在");
					continue;
				}
				double newrate = 0.004;
				//获取活动返水
				Date date=new Date();
				DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
				dc.add(Restrictions.eq("activityStatus", 1));
				dc = dc.add(Restrictions.le("backstageStart", date));
				dc = dc.add(Restrictions.gt("backstageEnd", date));
				dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
				List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
				if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
					Activity activity=listActivity.get(0);
					if(activity.getActivityPercent()!=null){
						newrate = activity.getActivityPercent();
					}
				}

				//白金以及白金以上单日返水无限制
				Double rebateLimit = getRebateLimit(user);

				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,rebateLimit);

				/***********************************/
				//进行自助反水缩减   昨天12点到今天12点之间的自助反水

				DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", starttime));
				c.add(Restrictions.le("createTime", endtime));
				c.add(Restrictions.eq("loginname", user.getLoginname()));

				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));

				c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
				c.setProjection(Projections.property("pno"));
				List list = this.proposalDao.findByCriteria(c);
				if(list!=null && !list.isEmpty()){
					DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", list.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = this.proposalDao.findByCriteria(x);
					Double d = 0.0;
					Double dremain =0.0;
					if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
						if((Double)sumx.get(0)*newrate<user.getEarebate()){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
							dremain = user.getEarebate()-(Double)sumx.get(0)*newrate;//剩下可反水额度
							d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
							if(d<0){
								d=0.0;
							}
						}
						ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
					}
				}
				/***********************************/
				String remark = "kyqp系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				//暂时不提供反水
				/*this.proposalDao.save(xima);
				this.proposalDao.save(proposal);*/

				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户kyqp平台输赢值");
				agProfit.setPlatform("kyqp");
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}

	/**
	 * VR彩票
	 */
	@Override
	public String autoAddXimaVRProposal(List resultList,String gamekind) {
		try {
			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);

			for (int i = 0; i < resultList.size(); i++) {
				JSONObject jobj = (JSONObject) resultList.get(i);

				System.out.println("loginname:"+jobj.get("loginname"));
				System.out.println("bet:"+jobj.get("bet"));
				System.out.println("amount:"+jobj.get("amount"));

				String string=(String) jobj.get("loginname");
				Double bet = Double.parseDouble(jobj.get("bet")+"");	//投注额
				Double winLoss = Double.parseDouble(jobj.get("amount")+""); //玩家总输赢

				BigDecimal b1 = new BigDecimal(bet);
				BigDecimal b2 = new BigDecimal(winLoss+"");
				Double amount = (b2.subtract(b1)).doubleValue();//玩家总输赢

				String loginname = null;
				if(string.startsWith("zb")){
					loginname = string.substring(2,string.length());
				}else{
					loginname = string.substring(1,string.length());
				}

				Users user = (Users) this.getUserDao().get(Users.class,loginname);
				if (user==null) {
					log.info("用户："+loginname+"，不存在");
					continue;
				}
				double newrate = 0.004;
				//获取活动返水
				Date date=new Date();
				DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
				dc.add(Restrictions.eq("activityStatus", 1));
				dc = dc.add(Restrictions.le("backstageStart", date));
				dc = dc.add(Restrictions.gt("backstageEnd", date));
				dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
				List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
				if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
					Activity activity=listActivity.get(0);
					if(activity.getActivityPercent()!=null){
						newrate = activity.getActivityPercent();
					}
				}

				//白金以及白金以上单日返水无限制
				Double rebateLimit = getRebateLimit(user);

				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,rebateLimit);

				/***********************************/
				//进行自助反水缩减   昨天12点到今天12点之间的自助反水

				DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", starttime));
				c.add(Restrictions.le("createTime", endtime));
				c.add(Restrictions.eq("loginname", user.getLoginname()));

				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));

				c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
				c.setProjection(Projections.property("pno"));
				List list = this.proposalDao.findByCriteria(c);
				if(list!=null && !list.isEmpty()){
					DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", list.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = this.proposalDao.findByCriteria(x);
					Double d = 0.0;
					Double dremain =0.0;
					if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
						if((Double)sumx.get(0)*newrate<user.getEarebate()){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
							dremain = user.getEarebate()-(Double)sumx.get(0)*newrate;//剩下可反水额度
							d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
							if(d<0){
								d=0.0;
							}
						}
						ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
					}
				}
				/***********************************/
				String platform = gamekind.equals("1")?"vr":"vrlive";
				String platfromTrans = gamekind.equals("1")?"vr官方彩":"vr彩";
				String remark = platfromTrans+"系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				//暂时不提供反水
				/*this.proposalDao.save(xima);
				this.proposalDao.save(proposal);*/

				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户"+platfromTrans+"平台输赢值");
				agProfit.setPlatform(platform);
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return null;
	}

	public Boolean selectPBDataByBetId(String betId){
		DetachedCriteria dc = DetachedCriteria.forClass(PBData.class);
		if(!StringUtils.isEmpty(betId)){
			dc = dc.add(Restrictions.eq("betId", betId));
		}
		List<PBData> list=this.findByCriteria(dc);
		if(null!=list&&list.size()>0){
			return true;
		}
		return false;
	}
	public void insertPBData(List<PBData> resultList) {
		String beforeDate = DateUtil.getchangedDate(-1)+" 00:00:00";
		String sql = "delete from pb_data where  settle_time >='"+beforeDate+"'";
		getDataDao.excuteSql(sql, new HashMap());
		getDataDao.saveOrUpdateAll(resultList);
	}

	public List<PBData> selectPBData(){
		DetachedCriteria dc = DetachedCriteria.forClass(PBData.class);
		List list=this.findByCriteria(dc);
		if(null==list || list.size() == 0){
			return null;
		}
		return list;
	}

	//比特游戏洗码
	public String BitXiMA(List<Bean4Xima> betsList) {
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		for (Bean4Xima item : betsList) {
			if(item.getUserName().startsWith("zb")){
				item.setUserName(item.getUserName().substring(2,item.getUserName().length()));
			}else{
				item.setUserName(item.getUserName().substring(1,item.getUserName().length()));
			}
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}

			Double mgBet = item.getBetAmount();//投注额

			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			this.getTaskDao().generateTasks(pno, "system");

			Double netWin = item.getProfit();//纯赢（纯输）
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), 0-netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户BIT平台输赢值");
			agProfit.setPlatform("bit");
			agProfit.setBettotal(mgBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}

	@Override
	public String autoAddXimaSwSProposal(List datas) {
		Date startTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endTime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		
		for (int i = 0; i < datas.size(); i++) {
			
			Object[] jobj = (Object[]) datas.get(i);
			
			System.out.println("loginname:"+jobj[0]);
			System.out.println("bet:"+jobj[1]);
			System.out.println("amount:"+jobj[2]);
			
			String loginname = (String) jobj[0];
			Double bet = Double.parseDouble(jobj[1]+"");	//投注额
			Double amount = Double.parseDouble(jobj[2]+""); //玩家总输赢
			
			Users user = (Users) this.getUserDao().get(Users.class, loginname);
			if (user==null) {
				log.info("用户：" + loginname + "不存在");
				continue;
			}
			
			double newrate = getSlotRate(user);
			
			Double mgBet = bet;//投注额
			XimaVO ximaObject = new XimaVO(mgBet, user.getLoginname(), newrate);
			/***********************************/
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			
			Double maxRebete = getRebateLimit(user);
			
			ximaObject = cutSelfXima(ximaObject, startTime, endTime, ProposalType.PTSKYSELFXIMA, newrate,maxRebete) ;
			/***********************************/
			String remark = "SW系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);
			
			Double netWin = amount;//赢分
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户SW平台输赢值");
			agProfit.setPlatform("sw");
			agProfit.setBettotal(mgBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}

	@Override
	public String autoAddXimaSwFProposal(List datas) {
		for (int i = 0; i < datas.size(); i++) {
			
			Object[] jobj = (Object[]) datas.get(i);
			
			System.out.println("loginname:"+jobj[0]);
			System.out.println("bet:"+jobj[1]);
			System.out.println("amount:"+jobj[2]);
			
			String loginname = (String) jobj[0];
			Double bet = Double.parseDouble(jobj[1]+"");	//投注额
			Double amount = Double.parseDouble(jobj[2]+""); //玩家总输赢
			
			Users user = (Users) this.getUserDao().get(Users.class, loginname);
			if (user==null) {
				log.info("用户：" + loginname + "不存在");
				continue;
			}
			
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			
			Double netWin = amount;//赢分
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户swfish平台输赢值");
			agProfit.setPlatform("swfish");
			agProfit.setBettotal(bet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}

	@Override
	public String xima4TTG() {
		Date bbinkeno_starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date bbinkeno_endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		//获取有游戏记录的玩家、投注额、以及输赢值，借用QT4xima实体类
		List<QT4Xima> userList;
		try {
			userList = proposalDao.getTTGXimaList(bbinkeno_starttime);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		if(null == userList || userList.size() == 0){
			return "没有游戏数据";
		}
		for (QT4Xima item : userList) {
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}
			
			double newrate = getSlotRate(user);
			
			String remarkAdd="";
			List<SystemConfig> list=this.querySystemConfig("type003", "001", "否");//超级洗码活动 如果没有被禁用 则乘以超级洗码的比例
			if(null!=list&&list.size()>0){
				SystemConfig sc=list.get(0);
				newrate=Arith.mul(newrate,Double.parseDouble(sc.getValue()));
				remarkAdd="超级洗码：";
			}
			
			Double qtBet = item.getBetAmount();//投注额
			XimaVO ximaObject = new XimaVO(qtBet, user.getLoginname(), newrate);
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			
			Double maxRebete = getRebateLimit(user);
			
			ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.TTGSELFXIMA, newrate,maxRebete) ;
			
			String remark = remarkAdd+"ttg系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.proposalDao.save(xima);
			this.proposalDao.save(proposal);
			
			Double netWin = item.getProfit();//纯赢（纯输）
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户TTG平台输赢值");
			agProfit.setPlatform("ttg");
			agProfit.setBettotal(qtBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}
	
	public String xima4QT() {
		Date bbinkeno_starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date bbinkeno_endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		//获取有游戏记录的玩家、投注额、以及输赢值
		List<QT4Xima> userList;
		try {
			userList = proposalDao.getQTXimaList(bbinkeno_starttime);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		if(null == userList || userList.size() == 0){
			return "没有游戏数据";
		}
		for (QT4Xima item : userList) {
			Users user = (Users) this.getUserDao().get(Users.class, item.getUserName());
			if (user==null) {
				log.info("用户：" + item.getUserName() + "不存在");
				continue;
			}
			
			double newrate = getSlotRate(user);
			
			String remarkAdd="";
			List<SystemConfig> list=this.querySystemConfig("type003", "001", "否");//超级洗码活动 如果没有被禁用 则乘以超级洗码的比例
			if(null!=list&&list.size()>0){
				SystemConfig sc=list.get(0);
				newrate=Arith.mul(newrate,Double.parseDouble(sc.getValue()));
				remarkAdd="超级洗码：";
			}
			
			Double qtBet = item.getBetAmount();//投注额
			XimaVO ximaObject = new XimaVO(qtBet, user.getLoginname(), newrate);
			//进行自助反水缩减   昨天0点到今天0点之间的自助反水
			
			Double maxRebete = getRebateLimit(user);
			
			ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.QTSELFXIMA, newrate,maxRebete) ;
			
			String remark = remarkAdd+"qt系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.proposalDao.save(xima);
				this.proposalDao.save(proposal);
				
			Double netWin = item.getProfit();//纯赢（纯输）
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), item.getUserName(), netWin, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户QT平台输赢值");
			agProfit.setPlatform("qt");
			agProfit.setBettotal(qtBet);
			this.proposalDao.save(agProfit);
		}
		return null;
	}

	public List<SystemConfig> querySystemConfig(String typeno,String itemNo,String flag){
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if(!StringUtils.isEmpty(typeno)){
			dc.add(Restrictions.eq("typeNo",typeno)) ;
		}
		if(!StringUtils.isEmpty(itemNo)){
			dc.add(Restrictions.eq("itemNo", itemNo)) ;
		}
		if(!StringUtils.isEmpty(flag)){
			dc.add(Restrictions.eq("flag", flag)) ;
		}
		List<SystemConfig> list= this.proposalDao.findByCriteria(dc);
		return list;
	}
	//反水上限
	private double getRebateLimit(Users user) {
		double rebateLimit = 8888.0;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			rebateLimit = 18888;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			rebateLimit = 18888;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			rebateLimit = 28888;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			rebateLimit = 28888;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			rebateLimit = 88888; 
		}
		return rebateLimit;
	}
	//老虎机反水比例
	private double getSlotRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
	
	//真人反水比例
	private double getLiveRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
	//体育反水比例
	private double getSportsRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
}
