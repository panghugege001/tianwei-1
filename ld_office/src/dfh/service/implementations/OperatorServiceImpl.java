package dfh.service.implementations;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import dfh.model.*;
import dfh.service.interfaces.ProposalService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.nnti.office.model.auth.Operator;
import com.nnti.office.model.log.Operationlogs;

import dfh.action.bbs.Email;
import dfh.action.bbs.Sms;
import dfh.action.vo.AgentReferralsVO;
import dfh.dao.BLDao;
import dfh.dao.BankinfoDao;
import dfh.dao.LogDao;
import dfh.dao.NetPayDao;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.dao.SlaveDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.mgs.OrionUtil;
import dfh.mgs.vo.freegame.AddPlayersToOfferRequest;
import dfh.mgs.vo.freegame.Offer;
import dfh.mgs.vo.freegame.PlayerAction;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.AutoGenerateType;
import dfh.model.enums.AutoTaskFlagType;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.notdb.AgentAnalysis;
import dfh.model.notdb.CustomerAnalysis;
import dfh.model.notdb.DataImportBL;
import dfh.model.notdb.Report;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.utils.AESUtil;
import dfh.utils.Arith;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.NumericUtil;
import dfh.utils.StringUtil;
import dfh.utils.compare.AgentAnalysisComparator;
import dfh.utils.compare.CustomerAnalysisComparator;
import dfh.utils.compare.DataImportBLComparator;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class OperatorServiceImpl extends UniversalServiceImpl implements OperatorService {

	private static Logger log = Logger.getLogger(OperatorServiceImpl.class);

	private LogDao logDao;
	private UserDao userDao;
	private SeqDao seqDao;
	private TradeDao tradeDao;
	private TaskDao taskDao;
	private BankinfoDao bankinfoDao;
	private NotifyService notifyService;
	private SlaveDao slaveDao;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ProposalDao proposalDao;
	private NetPayDao payorderDao;
	private BLDao blDao;
	
	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public NetPayDao getPayorderDao() {
		return payorderDao;
	}

	public void setPayorderDao(NetPayDao payorderDao) {
		this.payorderDao = payorderDao;
	}

	public ProposalDao getProposalDao() {
		return proposalDao;
	}
	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public BankinfoDao getBankinfoDao() {
		return bankinfoDao;
	}

	public void setBankinfoDao(BankinfoDao bankinfoDao) {
		this.bankinfoDao = bankinfoDao;
	}

	public OperatorServiceImpl() {
	}

	public static void main(String[] args) {
		System.out.println(dfh.utils.StringUtil.P_URL.matcher("http://阿扁大.d").matches());
	}

	@Override
	public void insertOperatorLog(Operator operator , String remark) {
		logDao.insertOperationLog(operator.getUsername(), OperationLogType.REMARK_EDIT, remark);
	}
	
	@Override
	public String synMemberInfo(String loginname, String password) throws Exception {

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
			// 判断用户是否已经同步过了
			String sql = "select username from pre_ucenter_members where username='" + loginname + "'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String sqlTwo1 = "update pre_ucenter_members set password='" + password + "' where username='" + loginname + "'";
				stmt.executeUpdate(sqlTwo1);
				return "成功更新BBS密码";
			}
			String sqlTwo = "insert into pre_ucenter_members(username , password , regdate , salt) values('"+loginname+"' , '"+password+"' , "+new Long((new Date().getTime()/1000))+" , '"+RandomStringUtils.random(6, true, true).toLowerCase()+"' )";
			stmt.executeUpdate(sqlTwo);
			return "更新成功" ;
		} catch (Exception e) {
			e.printStackTrace();
			return "更新出现异常："+e.getMessage();
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
				return "更新出现异常："+e.getMessage();
			}
		}
	}

	public String EnableUser(String userName, boolean isEnable, String operator,String remark) {
		String msg = null;
		Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
		if (user == null) {
			msg = "未找到该用户";
		} else {
			user.setFlag(isEnable ? Constants.ENABLE : Constants.DISABLE);
			user.setRemark(remark);
			update(user);
			if(user.getFlag() == 0 ){
				Userstatus userstatus = (Userstatus)get(Userstatus.class, userName,LockMode.UPGRADE);
				if(null != userstatus){
					userstatus.setLoginerrornum(0);
					userstatus.setCashinwrong(0);
					update(userstatus);
				}
			}
			logDao.insertOperationLog(operator, OperationLogType.ENABLE, (isEnable ? "启用" : "禁用") + "会员：" + userName+"，备注："+remark);
			msg = null;
		}
		return msg;
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public String login(String loginname, String password, String ip) {
		String msg = null;
		Operator op = (Operator) get(Operator.class, loginname);
		if (op == null) {
			msg = "帐号不存在";
		} else if (op.getEnabled().equals(Constants.FLAG_FALSE)) {
			msg = "帐号已被禁用";
			op.setSmsPwd(null);//只要登录就清空手机短信验证码。
			op.setSmsUpdateTime(null);
			update(op);
		} else if (!op.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			//密码不正确超过5次 冻结用户
			if(op.getPasswordNumber()==null){
				op.setPasswordNumber(1);
			     update(op);
			}
			else if(op.getPasswordNumber()>=5){
				 op.setEnabled(Constants.FLAG_FALSE);
				  msg = "账户已被禁用";
				  op.setSmsPwd(null);//只要登录就清空手机短信验证码。
				  op.setSmsUpdateTime(null);
				  update(op);
				  return msg;
			}
			else{
				int passwordNumber = op.getPasswordNumber();
				op.setPasswordNumber(passwordNumber+1);
			    update(op);
			}
			msg = "密码不正确";
		} else {
			//如果登录正常，则将登录错误此数置为0次
			op.setPasswordNumber(0);
			op.setSmsPwd(null);//只要登录就清空手机短信验证码。
			op.setSmsUpdateTime(null);
			op.setLoginTimes(Integer.valueOf(op.getLoginTimes().intValue() + 1));
			op.setLastLoginTime(DateUtil.now());
			op.setLastLoginIp(ip);
			op.setRandomStr(StringUtil.getRandomString(10));
			logDao.insertOperationLog(loginname, OperationLogType.LOGIN, ip);
			update(op);
		}
		return msg;
	}
	
	public String judgeWetherToChangePwd(String loginname) throws ParseException{
		Operator op = (Operator) get(Operator.class, loginname);
		//判断7天内是否修改过密码
		if(null == op){
			return null ;
		}
		if(null == op.getFirstDayWeek()){
			op.setFirstDayWeek(DateUtil.getYYYY_MM_DD());
			update(op);
			return "success" ;
		}
		if((null != op.getFirstDayWeek()) && ((DateUtil.getYYYY_MM_DD().getTime() - op.getFirstDayWeek().getTime())/86400000 < 7)){
			return "success" ;
		}
		return "changePwd";
	}
	
	public void insertSelfYouHuiLog(String operator ,String loginname ,boolean flag, OperationLogType type ,String remark ){
		logDao.insertOperationLog(operator, type, loginname+"玩家优惠转账限制置为"+(flag?"开启":"关闭" ));
	}
	

	public String addAgent(String acode, String loginname, String name, String phone, String email, String qq, String referWebsite, String ip,
			String operator, Integer agentType) {
		logDao.insertOperationLog(operator, OperationLogType.ADDAGENT, ip);
		return userDao.addAgent(acode, loginname, name, phone, email, qq, referWebsite, ip , agentType);
	}
	
	public String addAgentForRecommendedCode(String acode, String loginname, String name, String phone, String email, String qq, String partner,String referWebsite, String ip,
			String operator, Integer agentType) {
		logDao.insertOperationLog(operator, OperationLogType.ADDAGENT, ip);
		return userDao.addAgentForRecommendedCode(acode, loginname, name, phone, email, qq, partner, referWebsite, ip , agentType);
	}

	public String resetPassword(String loginname, String password, String operator) {
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			return "ERROR";
		} else {
			String oldPassword = user.getPassword();
			user.setPassword(EncryptionUtil.encryptPassword(password));
			user.setPwdinfo(userDao.encrypt(password));
			update(user);
			//notifyService.sendSms(user.getPhone(), StringUtil.trim("新密码是："+password));
			//notifyService.sendEmail(user.getEmail(),"密码更新：", StringUtil.trim("新密码是："+password));
			Sms email=new Sms(notifyService,user.getPhone(),password);
			email.start();
			Email email123=new Email(notifyService,user.getEmail(),password);
			email123.start();
			logDao.insertOperationLog(operator, OperationLogType.RESET_PWD, loginname + "的原密码:" + oldPassword);
			if(user.getRole().equals("MONEY_CUSTOMER")){
				return "MONEY_CUSTOMER";
			}else{
				return "AGENT";
			}
		} 
	}

	public String setLevel(String loginname, Integer level, String operator) {
		return userDao.setLevel(loginname, level, operator);
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String generateCommissionForEach(String loginname, Date endTime, String operator, List<String> userNameList) {
		return null;
	}

	public String generateXimaForEach(String loginname, Date startTime, Date endTime, String operator) {
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname);
		if (user == null) {
			msg = "用户不存在";
		} else if (user.getFlag().intValue() == Constants.DISABLE.intValue()) {
			msg = "用户被禁用";
		} else {
			// 获取上次洗码截止时间
			Date finalTime = userDao.getLastXimaEndTime(loginname);
			Proposal proposalDate = userDao.getConcessions(loginname);// 用户首存优惠
			if (finalTime != null && (startTime.before(finalTime) || startTime.equals(finalTime))) {
				msg = "该用户在" + DateUtil.formatDateForStandard(finalTime) + "内的洗码已经结算过";
			} else if (proposalDate != null && (startTime.before(proposalDate.getCreateTime()) || startTime.equals(proposalDate.getCreateTime()))
					&& (endTime.after(proposalDate.getCreateTime()) || endTime.equals(proposalDate.getCreateTime()))) {
				return "该用户本周享受过开户优惠";
			} else {
				Double validBetAmount = userDao.getValidBetAmount(loginname, startTime, endTime, null);
				if (validBetAmount.intValue() == 0) {
					msg = "用户在该段时间有效投注额为0";
				}
				Double rate = 0.0;
				// Double rate = userDao.getXimaRate(user, validBetAmount);
				if (rate.doubleValue() <= 0) {
					msg = "洗码率为0";
				} else {
					log.info("会员的有效投注额:" + validBetAmount + ";洗码率:" + rate);
					String pno = seqDao.generateProposalPno(ProposalType.XIMA);
					Double tryCredit = Double.valueOf(Math.abs(validBetAmount) * Math.abs(rate));
					String remark = "自动结算洗码";
					Xima xima = new Xima(pno, user.getRole(), loginname, "在线支付", validBetAmount, tryCredit, DateUtil
							.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), rate, remark);
					Proposal proposal = new Proposal(pno, operator, DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname,
							tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
					proposal.setFlag(ProposalFlagType.AUDITED.getCode());
					taskDao.generateTasks(pno, operator);
					save(xima);
					save(proposal);
					msg = null;
				}

			}
		}
		return msg;
	}
	@Override
	public List queryBet(String username, Date start, Date end,String game) {
		return tradeDao.queryBet(username,start,end,game);
	}

	@Override
	public String queryDeposit(Date startTime, Date endTime,String loginname,ProposalService proposalService ) {
		/**
		 * 检查该玩家某个时间段的存款量
		 * @param loginname
		 * @return
		 */

		Double sumMoney=0.0;
		DetachedCriteria dc1 = DetachedCriteria.forClass(Payorder.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.eq("type", 0));

		dc1.add(Restrictions.ge("createTime",startTime));



		dc1.add(Restrictions.le("createTime",endTime));


		dc1.setProjection(Projections.sum("money"));
		List list1 = proposalService.findByCriteria(dc1);
		Double result1 =(Double)list1.get(0);
		if(null==result1){
			result1=0.0;
		}
		DetachedCriteria dc2 = DetachedCriteria.forClass(Proposal.class);
		dc2.add(Restrictions.eq("loginname", loginname));
		dc2.add(Restrictions.eq("type", 502));
		dc2.add(Restrictions.eq("flag", 2));

		dc2.add(Restrictions.ge("createTime",startTime));



		dc2.add(Restrictions.le("createTime",endTime));



		dc2.setProjection(Projections.sum("amount"));
		List list2 = proposalService.findByCriteria(dc2);
		Double result2 =(Double)list2.get(0);
		if(null==result2){
			result2=0.0;
		}
		sumMoney=result1+result2;
		return sumMoney.toString();

	}


	public Autotask startAutoTask(AutoGenerateType type, Integer totalRecords, String operator) {
		Autotask generateTask = new Autotask();
		generateTask.setFlag(AutoTaskFlagType.INIT.getCode());
		generateTask.setTaskType(type.getCode());
		generateTask.setTotalRecords(totalRecords);
		generateTask.setStartTime(DateUtil.now());
		generateTask.setOperator(operator);
		generateTask.setFinishRecords(0);
		generateTask.setFailRecords(0);
		save(generateTask);
		return generateTask;
	}

	public Autotask getLastAutoTask(AutoGenerateType type) {
		List<Autotask> list = findByCriteria(DetachedCriteria.forClass(Autotask.class).add(Restrictions.eq("taskType", type.getCode())).addOrder(
				Order.desc("startTime")), 0, 1);
		if (list == null || list.size() == 0)
			return null;
		else {
			return list.get(0);
		}
	}

	public Autotask refreshAutoTask(Integer taskID, Boolean isSuccess, String remark) {
		Autotask task = (Autotask) get(Autotask.class, taskID, LockMode.UPGRADE);
		if (task == null) {
			return null;
		}
		if (task.getFlag().intValue() == AutoTaskFlagType.STOPPED.getCode()) {
			return task;
		}
		task.setRefreshTime(DateUtil.now());
		task.setFlag(AutoTaskFlagType.PROCEED.getCode());
		if (StringUtils.isNotEmpty(remark))
			task.setRemark(remark);
		if (isSuccess)
			task.setFinishRecords(task.getFinishRecords() + 1);
		else
			task.setFailRecords(task.getFailRecords() + 1);
		// 如果总记录数已达到，标记为已生成
		if (task.getTotalRecords().intValue() == (task.getFailRecords() + task.getFinishRecords()))
			task.setFlag(AutoTaskFlagType.GENERATED.getCode());
		update(task);
		return task;
	}

	public Autotask stopAutoTask(Integer taskID, String remark) {
		Autotask task = (Autotask) get(Autotask.class, taskID, LockMode.UPGRADE);
		if (task == null) {
			return null;
		}
		task.setFlag(AutoTaskFlagType.STOPPED.getCode());
		task.setEndTime(DateUtil.now());
		task.setRemark(remark);
		update(task);
		return task;
	}

	public Autotask finishAutoTask(Integer taskID, String remark) {
		Autotask task = (Autotask) get(Autotask.class, taskID, LockMode.UPGRADE);
		if (task == null) {
			return null;
		}
		task.setFlag(AutoTaskFlagType.EXCUTED.getCode());
		task.setEndTime(DateUtil.now());
		task.setRemark(remark);
		update(task);
		return task;
	}

	public String changeCreditManual(String loginname, Double amount, String remark, String operator) {
		String msg = null;
		if (StringUtils.isNotEmpty(loginname) && amount != null && StringUtils.isNotEmpty(operator)) {
			Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			tradeDao.changeCredit(user, amount, CreditChangeType.CHANGE_MANUAL.getCode(), null, remark);
			logDao.insertOperationLog(loginname, OperationLogType.CHANGE_CREDIT_MANUAL, operator+"为用户" + loginname + "增加/扣除"
					+ NumericUtil.formatDouble(amount));
		}
		return msg;
	}
	
	public String changeQuotal(String billno, String loginname, Double amount, String remark, String operator, String ip) {
		String msg = null; 
		if (StringUtils.isNotEmpty(loginname) && amount != null && StringUtils.isNotEmpty(operator)) {
			Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			 
			tradeDao.changeQuotal(user,billno, amount, CreditChangeType.CHANGE_QUOTAL.getCode(), null, remark, operator, ip);
			logDao.insertOperationLog(loginname, OperationLogType.CHANGE_CREDIT_QUOTAL, operator+"为用户" + loginname + "增加/扣除"
					+ NumericUtil.formatDouble(amount));
		}
		return msg;
	}
	
	public String changeQuotalForSlot(String billno, String loginname, Double amount, String remark, String operator, String ip) {
		String msg = null; 
		if (StringUtils.isNotEmpty(loginname) && amount != null && StringUtils.isNotEmpty(operator)) {
			Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			
			tradeDao.changeQuotalForSlot(user,billno, amount, CreditChangeType.CHANGE_QUOTALSLOT.getCode(), null, loginname + "老虎机佣金增加/扣除"
					+ NumericUtil.formatDouble(amount), operator, ip);
			logDao.insertOperationLog(loginname, OperationLogType.CHANGE_CREDIT_QUOTAL, operator+"为用户" + loginname + "老虎机佣金增加/扣除"
					+ NumericUtil.formatDouble(amount));
		}
		return msg;
	}
	
	public String changeBankCreditManual(String loginname, Double amount,String type, String remark, String operator) {
		String msg = null;
		if (StringUtils.isNotEmpty(loginname) && amount != null && StringUtils.isNotEmpty(operator)) {
			//Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			//tradeDao.changeCredit(user, amount, CreditChangeType.CHANGE_MANUAL.getCode(), null, remark);
			msg = bankinfoDao.changeBankCreditManual(loginname, amount, remark,type);
			logDao.insertOperationLog(loginname, OperationLogType.CG_BKCREDIT_MANUAL, "为银行帐号" + loginname + "增加/扣除"
					+ NumericUtil.formatDouble(amount));
		}else if(loginname==null||loginname.equals("")){
			msg="The bank account should not be empty";
		}else if(amount == null){
			msg="Please enter the amount again";
		}
		return msg;
	}

	public String createSubOperator(String newOperator,String email, String password, String operator,String agent,Integer validType,String cellphoneno,String employeeNo) {
		String msg = null;
		String power = "manager";
		Operator currentOperator = (Operator) get(Operator.class, operator);
		if (currentOperator == null) {
			msg = "帐号不存在";
		} else if (!currentOperator.getAuthority().toLowerCase().contains(power)) {
			msg = "用户" + operator + "没有开设子帐号的权限";
		} else if (get(Operator.class, newOperator) != null) {
			msg = "帐号已存在";
		} else {
			String authrities = currentOperator.getAuthority().substring(0, currentOperator.getAuthority().indexOf(power) - 1);
			Operator newOp = new Operator(newOperator, EncryptionUtil.encryptPassword(password), Constants.ENABLE, authrities, 0, DateUtil.now(),validType,cellphoneno,employeeNo);
			newOp.setEmail(email);
			newOp.setAgent(agent);
			save(newOp);
			logDao.insertOperationLog(operator, OperationLogType.CREATE_SUB_OP, "创建了下级管理员:" + newOperator);
			msg = null;
		}
		return msg;
	}
	
	public String createSubOperatorTwo(String newOperator, String email, String password,String operator, String authority,String agent,Integer validType,String phoneno,String employeeNO) {
		Operator oldopt = (Operator) get(Operator.class, newOperator);
		if(oldopt != null){
			return "帐号已存在,不允许重复创建！";
		}
		Operator newOp = new Operator(newOperator, EncryptionUtil.encryptPassword(password), Constants.ENABLE, authority, 0, DateUtil.now(),validType,phoneno,employeeNO);
		newOp.setEmail(email);
		newOp.setAgent(agent);
		save(newOp);
		logDao.insertOperationLog(operator, OperationLogType.CREATE_SUB_OP, "创建了下级管理员:" + newOperator);
		return null;
	}

	public String modifyOwnPassword(String operator, String oldPassword, String newPassword, boolean changeStr) {
		String msg = null;
		Operator currentOperator = (Operator) get(Operator.class, operator, LockMode.UPGRADE);
		if (currentOperator == null) {
			msg = "帐号不存在";
		} else if (currentOperator.getEnabled().intValue() == Constants.DISABLE.intValue()) {
			currentOperator.setSmsPwd(null);
			currentOperator.setSmsUpdateTime(null);
			msg = "账户已被禁用";
		} else if (!currentOperator.getPassword().equals(EncryptionUtil.encryptPassword(oldPassword))) {
			//旧密码不正确超过5次 冻结用户
			if(currentOperator.getPasswordNumber()==null){
				currentOperator.setPasswordNumber(1);
			    update(currentOperator);
			}
			
			else if(currentOperator.getPasswordNumber()==5){
				currentOperator.setEnabled(Constants.FLAG_FALSE);
				currentOperator.setSmsPwd(null);
				currentOperator.setSmsUpdateTime(null);
				  update(currentOperator);
				  msg = "账户已被禁用";
				  return msg;
			}
			else{
				int passwordNumber = currentOperator.getPasswordNumber();
				currentOperator.setPasswordNumber(passwordNumber+1);
			      update(currentOperator);
			}
			msg = "旧密码不正确";
		} else if (oldPassword.equals(newPassword)) {
			msg = "新密码与旧密码相同";
		} else {
			currentOperator.setPassword(EncryptionUtil.encryptPassword(newPassword));
			
			currentOperator.setFirstDayWeek(DateUtil.getYYYY_MM_DD());
			//如果修改正确，则将密码错误次数置为0
			currentOperator.setPasswordNumber(0);
			currentOperator.setSmsPwd(null);
			currentOperator.setSmsUpdateTime(null);
			if(changeStr){
				currentOperator.setRandomStr(StringUtil.getRandomString(10));
			}
			update(currentOperator);
			logDao.insertOperationLog(operator, OperationLogType.MODIFY_OWN_PWD, null);
			msg = null;
		}
		return msg;
	}

	public String partnerSetlower(String loginname, String partner) {
		String msg = null;
		// Users user = (Users) get(Users.class, loginname);
		// Users pUser = (Users) get(Users.class, partner);
		// if (user == null) {
		// msg = "返回消息:未找到" + loginname + "会员账号";
		// } else if (StringUtils.isNotEmpty(user.getPartner())) {
		// msg = loginname + "账号已经有对应的合作伙伴";
		//
		// } else if (user.getFlag() == Constants.DISABLE) {
		// msg = loginname + "账号已经被禁用";
		// } else if (pUser == null) {
		// msg = "未找到" + partner + "合作伙伴账号";
		// // } else if (!pUser.getRole().equals(UserRole.PARTNER.getCode())) {
		// // msg = partner + "不是合作伙伴账号";
		// } else if (pUser.getFlag().intValue() == Constants.DISABLE.intValue()) {
		// msg = "合作伙伴" + partner + "已经被禁用";
		// } else {
		// logDao.insertOperationLog(loginname, OperationLogType.SET_PARTNERLOWER, "会员" + loginname + "合作伙伴" + partner);
		// user.setPartner(partner);
		// update(user);
		// }
		return msg;
	}

	public String modifyCustomerInfo(String loginname, String aliasName, String accountName, String phone, String email, String qq, String remark, String operator,Timestamp birthday,int sms) {
		
//		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
//		dc = dc.add(Restrictions.ilike("email", "%@%"));
//		dc = dc.add(Restrictions.ilike("phone", "%1%"));
//		List<Users> list = logDao.getHibernateTemplate().findByCriteria(dc);
//		System.out.println("数据量："+list.size());
//		int phoneNum = 0;
//		int emailNum = 0;
//		int count = 0;
//		for(Users user :list)
//		{
//			count ++;
//			if(count>1000)
//			{
//				break;
//			}
////			if(user.getLoginname().equals("devtest999"))
////			{
//				phone = user.getPhone();
//				email = user.getEmail();
//				if(StringUtils.isNotBlank(phone) && dfh.utils.StringUtil.isNumeric(phone))
//				{
//					phoneNum++;
//					System.out.println("phone:"+phone);
//					try {
//						System.out.println("处理phone:"+AESUtil.aesEncrypt(phone, AESUtil.KEY));
//						user.setPhone(AESUtil.aesEncrypt(phone, AESUtil.KEY));
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				if(StringUtils.isNotBlank(email) && email.contains("@"))
//				{
//					emailNum++;
//					System.out.println("email:"+email);
//					try {
//						user.setEmail(AESUtil.aesEncrypt(email, AESUtil.KEY));
//						System.out.println("处理email:"+AESUtil.aesEncrypt(email, AESUtil.KEY));
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//				System.out.println("getemail:"+user.getEmail());
//				System.out.println("getphone:"+user.getPhone());
//				logDao.update(user);
////			}
//		}
//		System.out.println("phone错误数据:"+phoneNum);
//		System.out.println("email错误数据:"+emailNum);
//		
		
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			String oldAliasName = user.getAliasName();
			String oldAccountName = user.getAccountName();
			String oldPhone = user.getPhone();
			String oldEmail = user.getEmail();
			String oldQq = user.getQq();
			Timestamp oldBirthday=user.getBirthday();

			user.setAliasName(aliasName);
			user.setAccountName(accountName);
			user.setPhone(phone);
			user.setEmail(email);
			user.setQq(qq);
			user.setBirthday(birthday);
			user.setRemark(remark);
			user.setSms(sms);
			update(user);
			
			String logRemark = operator+"将 ";
			if (!StringUtils.equals(oldAliasName, aliasName))
				logRemark += "原昵称[" + oldAliasName + "]改为[" + aliasName + "];";
			if (!StringUtils.equals(oldAccountName, accountName))
				logRemark += "原姓名[" + oldAccountName + "]改为[" + accountName + "];";
			if (!StringUtils.equals(oldPhone, phone))
				logRemark += "原电话[" + oldPhone + "]改为[" + phone + "];";
			if (!StringUtils.equals(oldEmail, email))
				logRemark += "原邮箱[" + oldEmail + "]改为[" + email + "];";
			if (!StringUtils.equals(oldQq, qq))
				logRemark += "原QQ[" + oldQq + "]改为[" + qq + "];";
			if (!birthday.equals(oldBirthday))
				logRemark += "原生日[" + oldBirthday + "]改为[" + birthday + "];";

			logDao.insertOperationLog(loginname, OperationLogType.MODIFY_CUS_INFO, logRemark);
		}
		return msg;
	}
	
	public String limitMethod(String loginname,String limit,String adminName){
		Users user = getFindUser(loginname);
		if (user == null){
			return "帐号不存在";
		}
		logDao.insertActionLog(loginname, ActionLogType.MODIFY_CUS_INFO, loginname+"从原来的"+user.getCreditlimit()+"更新为现的"+limit+"(操作人:"+adminName+")");
		if(updateUser(Double.parseDouble(limit),loginname)){
			return null;
		}
		return "提交失败";
	}

	
	public String modifyAgentInfo(String loginname, String referWebsite, String phone, String email, String qq, String partner, String remark,String oldagcode,String newagcode, String operator,Integer agentType, String agentCommission,Double liverate,Double sportsrate,Double lotteryrate) {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else if(!user.getRole().equals("AGENT")){
			msg = "此帐号不是代理";
		}else {
			try {
				String oldPhone = AESUtil.aesEncrypt(phone, AESUtil.KEY);
			String oldEmail = AESUtil.aesEncrypt(email, AESUtil.KEY);
			String oldQq = user.getQq();
			String oldreferWebsite=user.getReferWebsite();
			String oldacode=user.getAgcode();
				if (StringUtils.isNotBlank(phone) &&!StringUtils.equals(oldPhone, user.getPhone()))
					user.setPhone(oldPhone);
				if (StringUtils.isNotBlank(email) &&!StringUtils.equals(oldEmail, user.getEmail()))
					user.setEmail(oldEmail);

				user.setQq(qq);
			user.setPartner(partner);
			if(!oldreferWebsite.equals(referWebsite)){
				AgentAddress  agentAddress = (AgentAddress) get(AgentAddress.class, referWebsite);
				if(null != agentAddress && agentAddress.getDeleteflag()==0){
					msg = referWebsite+"已经被代理"+agentAddress.getLoginname()+"使用";
					return msg ;
				}
				user.setReferWebsite(referWebsite);
				List<AgentAddress> list = excuteSQL("select * from agent_address where loginname = '"+user.getLoginname()+"' and deleteflag=0" , AgentAddress.class) ;
				if(null != list && list.size()==1){
					delete(AgentAddress.class, list.get(0).getAddress());
					
					AgentAddress addAgent = new AgentAddress();
					addAgent.setLoginname(loginname);
					addAgent.setAddress(referWebsite);
					addAgent.setDeleteflag(0);
					addAgent.setCreatetime(new Date());
					save(addAgent);
				}else{
					msg = user.getLoginname()+"代理同时有多个代理地址";
					return msg ;
				}
				
			}
			if (!StringUtils.equals(oldacode,newagcode)&&oldacode!=null&&!oldacode.equals("")){
				user.setAgcode(newagcode);
				userDao.updateAgcode(oldacode, newagcode);
			}
			
			InternalAgency inAgency = (InternalAgency) get(InternalAgency.class, loginname);
			String logRemark = "";
			user.setRemark(remark);
			if(null != agentType){
				if(null == inAgency){
					inAgency = new InternalAgency(loginname, agentType, new Date(), "修改By:"+operator);
					save(inAgency);
					logRemark = "增加内部代理：" + loginname + ",类型:" + agentType + ",操作By:" + operator;
				}else{
					int type = inAgency.getType();
					inAgency.setType(agentType);
					inAgency.setCreatetime(new Date());
					inAgency.setRemark("修改By:"+operator);
					update(inAgency);
					logRemark = "修改内部代理：" + loginname + "类型:" + type +"->" + agentType + ",操作By:" + operator;
				}
			} else {
				if(inAgency != null){
					delete(InternalAgency.class, loginname);
					logRemark = "删除内部代理：" + loginname + ",原类型:" + agentType + ",操作By:" + operator;
				}
			}
			update(user);
			
			Userstatus userstatus = (Userstatus) get(Userstatus.class, loginname);
			
			if (null == userstatus) {
						
				userstatus = new Userstatus();
				userstatus.setLoginname(loginname);
				userstatus.setTouzhuflag(0);
				userstatus.setCashinwrong(0);
				userstatus.setSlotaccount(0.0);
			}
						
			userstatus.setCommission(agentCommission);
			userstatus.setLiverate(liverate);
			userstatus.setSportsrate(sportsrate);
			userstatus.setLotteryrate(lotteryrate);
						
			saveOrUpdate(userstatus);
			
			if (!StringUtils.equals(oldPhone, phone))
				logRemark += "原电话[" + oldPhone + "]改为[" + phone + "];";
			if (!StringUtils.equals(oldEmail, email))
				logRemark += "原邮箱[" + oldEmail + "]改为[" + email + "];";
			if (!StringUtils.equals(oldQq, qq))
				logRemark += "原QQ[" + oldQq + "]改为[" + qq + "];";
			if (!StringUtils.equals(oldreferWebsite, referWebsite))
				logRemark += "原代理网址[" + oldreferWebsite + "]改为[" + referWebsite + "];";
			if (!StringUtils.equals(oldacode,newagcode))
				logRemark += "原代理agcode[" + oldacode + "]改为[" + newagcode + "];";
			
			

			logDao.insertOperationLog(loginname, OperationLogType.MODIFY_AGENT_INFO, logRemark);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

	// sun
	public double getValidBetAmount(String loginname, Date start, Date end) {
		return userDao.getValidBetAmount(loginname, start, end, null);
	}

	// sun
	public String makePartnerBonus(String loginname, Integer year, Integer month, Double validBetAmount, Double cashinoutAmount, String operator,
			String ip, String remark) {
		String msg = null;
		/*
		 * Users user = (Users) this.get(Users.class, loginname); if (StringUtils.isEmpty(user.getPartner())) msg = "未找到对应的合作伙伴账号"; else if
		 * (userDao.checkCommissionrecords(loginname, year, month) != null) msg = "此用户本月已结算过佣金"; else { Commissionrecords commisionrecords = new
		 * Commissionrecords(); commisionrecords.setId(new CommissionrecordsId(loginname, year, month));
		 * 
		 * commisionrecords.setCashinoutAmount(cashinoutAmount); commisionrecords.setFlag(Constants.FLAG_FALSE);
		 * commisionrecords.setRemark(remark); commisionrecords.setSubLoginname(user.getPartner());
		 * commisionrecords.setValidBetAmount(validBetAmount); commisionrecords.setCreateDate(DateUtil.now());
		 * 
		 * Double sumProfit = (validBetAmount * Constants.PROFITRATE) - (cashinoutAmount * Constants.OUTLAYRATE);// 拥金计算结果 公式(有效投注额*0.002 减去
		 * 存取在线支付总额*0.01) commisionrecords.setProfitAmount(sumProfit);
		 * 
		 * commisionrecords.setOutlayRate(Constants.OUTLAYRATE); commisionrecords.setProfitRate(Constants.PROFITRATE); save(commisionrecords); }
		 */
		return msg;
	}

	// sun
	public String excuteMakePartnerBonus(String subLoginname, Double sumProfitAmount, String operator, String ip, String remark) {
		String msg = null;
		// Users user = (Users) get(Users.class, subLoginname);
		// if (user == null)
		// msg = "不存在此合作伙伴";
		// else if (sumProfitAmount < 500)
		// msg = "结算值小于500累记到下个月";
		// else {
		// final String newSubLoginname = subLoginname;
		// getHibernateTemplate().execute(new HibernateCallback() {
		// public Object doInHibernate(Session session) throws HibernateException, SQLException {
		// session.createSQLQuery("update commissionrecords set flag=0,excuteDate='" + DateUtil.now() + "' where subLoginname='" + newSubLoginname
		// + "'").executeUpdate();
		// return null;
		// }
		// });
		// if (sumProfitAmount > 0) {
		// tradeDao.changeCredit(subLoginname, sumProfitAmount, CreditChangeType.COMMISSION.getCode(), null, remark);
		// }
		// }
		return msg;
	}

	public List excuteSQL(final String sql ,final Class entity) {
		List list = null;
		list = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createSQLQuery(sql).addEntity(entity).list();
			}
		});
		return list;
	}

	public Report queryReport(Date startTime, Date endTime, String userRole, String loginname) {
		Report report = new Report();
		DetachedCriteria dcBetRecord = DetachedCriteria.forClass(Betrecords.class);

		if (StringUtils.isNotEmpty(userRole)) {
			if (userRole.equals(UserRole.MONEY_CUSTOMER.getCode()))
				dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
			// if (userRole.equals(UserRole.FREE_CUSTOMER.getCode()))
			// dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_FREE_CUSTOMER + "%"));
		}
		if (StringUtils.isNotEmpty(loginname)) {
			dcBetRecord = dcBetRecord.add(Restrictions.eq("passport", loginname));
			report.setLoginname(loginname);
		}
		if (startTime != null)
			dcBetRecord = dcBetRecord.add(Restrictions.ge("billTime", startTime));
		if (endTime != null)
			dcBetRecord = dcBetRecord.add(Restrictions.lt("billTime", endTime));

		dcBetRecord.setProjection(Projections.projectionList().add(Projections.sum("billAmount")).add(Projections.sum("result")));
		List resultBetRecord = getHibernateTemplate().findByCriteria(dcBetRecord);
		Object[] arrayBetRecord = (Object[]) resultBetRecord.get(0);

		if (arrayBetRecord[0] != null)
			report.setSumBillAmount((Double) arrayBetRecord[0]);// 总投注额
		if (arrayBetRecord[1] != null)
			report.setSumResult((Double) arrayBetRecord[1]);// 总返利情况
		if (StringUtils.isEmpty(loginname))
			report.setSumAttend(queryAttendGameNumber(startTime, endTime, userRole));// 投注人数

		if (arrayBetRecord[0] != null && arrayBetRecord[1] != null) {
			String winPercent = (NumericUtil.formatDouble((report.getSumResult() / report.getSumBillAmount()) * 100)) + "%";
			if (winPercent.indexOf("-") >= 0)
				winPercent = winPercent.replace("-", "");
			else
				winPercent = "-" + winPercent;
			report.setWinPercent(winPercent);
		}
		report.setSumFactAmount(userDao.getValidBetAmount(loginname, startTime, endTime, userRole));// 有效投注额
		return report;
	}

	public Integer queryAttendGameNumber(Date startTime, Date endTime, String userRole) {
		Integer result = 0;
		DetachedCriteria dc = DetachedCriteria.forClass(Betrecords.class);
		if (StringUtils.isNotEmpty(userRole)) {
			if (userRole.equals(UserRole.MONEY_CUSTOMER.getCode())) {
				dc = dc.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
			}
			// if (userRole.equals(UserRole.FREE_CUSTOMER.getCode())) {
			// dc = dc.add(Restrictions.like("passport", "" + Constants.PREFIX_FREE_CUSTOMER + "%"));
			// }
		}
		if (startTime != null) {
			dc = dc.add(Restrictions.ge("billTime", startTime));
		}
		if (endTime != null) {
			dc = dc.add(Restrictions.lt("billTime", endTime));
		}
		dc.setProjection(Projections.projectionList().add(Projections.countDistinct("passport")));
		List resultCount = getHibernateTemplate().findByCriteria(dc);
		if (resultCount.get(0) != null)
			result = (Integer) resultCount.get(0);// 总投注人数
		return result;
	}

	/*
	 * 获取某段时间内的本地额度变化值,不包含提款
	 */
	public Double getLocalCreditChangeByPeriod(String loginname, Date startTime, Date endTime) {
		DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
		dc = dc.add(Restrictions.ne("type", CreditChangeType.TRANSFER_IN.getCode())).add(
				Restrictions.ne("type", CreditChangeType.TRANSFER_OUT.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.TRANSFER_BBININ.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.TRANSFER_BBINOUT.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.TRANSFER_DSPIN.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.TRANSFER_DSPOUT.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.TRANSFER_KENOIN.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.TRANSFER_KENOOUT.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.CASHOUT.getCode())).add(Restrictions.eq("loginname", loginname)).add(
						Restrictions.gt("createtime", startTime));
		if (endTime != null)
			dc = dc.add(Restrictions.lt("createtime", endTime));
		dc = dc.setProjection(Projections.sum("remit"));
		List<Double> list = findByCriteria(dc);
		return NumericUtil.toDouble(list.size() > 0 ? list.get(0) : null) ;
	}

	/**
	 * 检测指定代理网址是否可用
	 * return boolean true 可用，false 禁用
	 */
	public boolean isUseReferWebsite(String url) {
		// TODO Auto-generated method stub
		return userDao.getAgentByWebsite(url)==null?true:false;
	}

	@Override
	public Map<String,List> queryCustomerInfoAnalysis( Date start,  Date end,
			 String loginname,Integer level,Integer warnflag, String agent,String intvalday,String nintvalday,String intro,String partner, Date startDate , Date endDate,String order,String by) {
		
		Session  session = slaveDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer sbf = new StringBuffer();
		sbf.append("select * from ( select  t_shuying.loginname,warnflag,level,agent,intro, "+
		" t_shuying.tdeposit,case when t_payorder.netpaymoney is null then 0 else t_payorder.netpaymoney end as netpaymoney, "+
		" t_shuying.withdraw, t_shuying.profit as profit,createtime,lastLoginTime,DATEDIFF(CURRENT_TIMESTAMP,lastLoginTime) as intvalday, "+
		" loginTimes,profitall,role ,partner " +
		" from (select t_dw_user.loginname,case when tdeposit is null then 0 else tdeposit end as tdeposit,case when t_withdraw.withdraw is null then 0 else t_withdraw.withdraw  end as withdraw, " +
		" case when t_withdraw.withdraw  is null then -t_deposit.deposit else t_withdraw.withdraw-(case when t_deposit.deposit is null then 0 else t_deposit.deposit end ) end as profit  from " +
		//总存提款用户
		"(select loginname from "+    
		"	(select loginname from proposal where type = 502 and flag= 2 and createTime>=? and createTime<=? ");
		sbf.append(" group by loginname "+ 
				
		"	union all "+
		"	select loginname from payorder where flag=0 and type=0  and createTime>=? and createTime<=? ");
		sbf.append(" group by loginname "+ 
				
		"	union all "+
		"	select loginname from proposal where type = 503 and flag= 2 and createTime>=? and createTime<=? ");
		sbf.append(" group by loginname "+
		"	)t_sub_user group by loginname "+
		")t_dw_user ");
		
		sbf.append(" left join ");
		//总存款
		sbf.append(
		" (select loginname,sum(subdeposit) as deposit from "+
		"   (select loginname,sum(amount) as subdeposit from proposal where type = 502 and flag= 2 and createTime>=? and createTime<=? ");
		sbf.append(
		" 	 group by loginname "+
		"        union all "+
		"	 select loginname,sum(money) as netpaymoney from payorder where flag=0 and type=0 ");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		Date netDate = null;
		try {
			date = sdf.parse("2012-05-01 00:00:00");
			if(null!=start && start.before(date)){
				netDate = date;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sbf.append(" and createTime>=? and createTime<=? " );
		sbf.append(" group by loginname )t_sub_deposit group by loginname)t_deposit on t_dw_user.loginname=	t_deposit.loginname ");
		
		sbf.append(
		" left join (select loginname,sum(amount) as tdeposit from proposal where type = 502 and flag= 2  and createTime>=? and createTime<=? ");
		sbf.append(" group by loginname)tt_deposit on t_dw_user.loginname=tt_deposit.loginname ");
		
		sbf.append(	
		" left join "+ 
		" (select loginname,sum(amount) as withdraw from proposal where type = 503 and flag= 2 and createTime>=? and createTime<=? ");
		sbf.append(" group by loginname)t_withdraw "+
		" on t_dw_user.loginname=t_withdraw.loginname )t_shuying "+
		" left join users on users.loginname = t_shuying.loginname ");
		
		sbf.append(" left join "+
		" (select loginname,sum(money) as netpaymoney from payorder where flag=0 and type=0 and createTime>= ? and createTime<=? ");
		sbf.append(" group by loginname)t_payorder " +
		" on t_payorder.loginname = t_shuying.loginname ");
		
		sbf.append(
		" left join (select tall_deposit.loginname as loginname, "+
		" case when tall_withdraw.withdraw  is null then -tall_deposit.deposit else tall_withdraw.withdraw-tall_deposit.deposit end as profitall  from "+
		
		" (select loginname,sum(subdeposit) as deposit from "+
			"   (select loginname,sum(amount) as subdeposit from proposal where type = 502 and flag= 2 "+
			   "  group by loginname "+
			" union all "+ 
		" select loginname,sum(money) as netpaymoney from payorder where flag=0 and createTime>='2012-05-01 00:00:00' "+
				"group by loginname "+
		" )t_sub_deposit group by loginname)tall_deposit "+
		" left join  (select loginname,sum(amount) as withdraw from proposal where type = 503 and flag= 2   group by loginname)tall_withdraw "+
		
		" on tall_deposit.loginname=tall_withdraw.loginname )tall on tall.loginname = t_shuying.loginname ");
		sbf.append(" )newtable where 1=1 and role='MONEY_CUSTOMER' ");
		
		Map<String,Object> mm = new HashMap<String, Object>();
		if(null!=loginname && !"".equals(loginname)){
			sbf.append(" and loginname =:lg");
			mm.put("lg",loginname );
		}
		if(null!=intvalday && !"".equals(intvalday)){
			sbf.append(" and newtable.intvalday <=:itd");
			mm.put("itd",intvalday );
		}
		if(null!=nintvalday && !"".equals(nintvalday)){
			sbf.append(" and newtable.intvalday >=:itd");
			mm.put("itd",nintvalday );
		}
		if(null!=intro && !"".equals(intro)){
			if("_e685201314".equals(intro)){
				if("88".equals(nintvalday)){
					sbf.append(" and (newtable.intro ='' or newtable.intro is null) ");
				}
			}else{
				sbf.append(" and newtable.intro =:ito");
				mm.put("ito",intro );
			}
		}
		List<String> agents = null;
		if(StringUtils.isNotBlank(partner)){
			String sql = "select loginname from users where partner = '"+partner+"' " ;
			agents = session.createSQLQuery(sql).list() ;
			if(null != agents && agents.size()>0){
				sbf.append(" and newtable.agent in(:ptragents)");
			}
		}
		List<String> agentstime = null ;
		if(null != startDate && null != endDate){
			String sql = "select loginname from users where role='"+UserRole.AGENT.getCode()+"' and createtime>= '"+DateUtil.formatDateForStandard(startDate)+"' and createtime<='"+DateUtil.formatDateForStandard(endDate)+"'" ;
			agentstime = session.createSQLQuery(sql).list() ;
			if(null != agentstime && agentstime.size()>0){
				sbf.append(" and newtable.agent in(:ptragentime)");
			}
		}
		if(null!=agent && !"".equals(agent)){
			sbf.append(" and newtable.agent =:agt");
			mm.put("agt",agent );
		}
		if(null!=level && !"".equals(level)){
			sbf.append(" and newtable.level =:lel");
			mm.put("lel",level );
		}
		if(null!=warnflag && !"".equals(warnflag)){
			sbf.append(" and newtable.warnflag =:wfg");
			mm.put("wfg",warnflag );
		}
		sbf.append(" order by ");
		sbf.append(by+" ");
		sbf.append(order+" ");
		
		List list = null;
		Query query = session.createSQLQuery(sbf.toString()).setTimestamp(0, start).setTimestamp(1, end).
		setTimestamp(2,netDate==null?start:netDate ).setTimestamp(3, end).setTimestamp(4, start).
		setTimestamp(5, end).setTimestamp(6, start).setTimestamp(7, end).setTimestamp(8, netDate==null?start:netDate).
		setTimestamp(9, end).setTimestamp(10, start).setTimestamp(11, end).
		setTimestamp(12, start).setTimestamp(13, end).setTimestamp(14,netDate==null?start:netDate ).setTimestamp(15, end);
		if(!mm.isEmpty()){
			query = query.setProperties(mm);//直接将map参数传组query对像
		}
		if(null != agents && agents.size()>0){
			query.setParameterList("ptragents", agents);  
		}
		if(null != agentstime && agentstime.size()>0){
			query.setParameterList("ptragentime", agentstime);  
		}
		list = query.list();
		List<CustomerAnalysis> calist = new ArrayList<CustomerAnalysis>();
		List<Double> sumlist = new ArrayList<Double>();
		if(null!=list && !list.isEmpty()){
			Double depositSumAmount=0.00;
			Double netpaySumAmount=0.00;
			Double withdrawSumAmount=0.00;
			Double profileSumAmount=0.00;
			Double profitAllSumAmount=0.00;
			for(Object obj : list){
				Object[] o=(Object[])obj;
				String name = (String)o[0];
				Integer warnflag1 = (Integer)o[1];
				Integer level1 = (Integer)o[2];
				String agent1 = (String)o[3];
				String intros = (String)o[4];
				Double deposit =  o[5]==null?0:(Double)o[5];
				depositSumAmount +=deposit;
				Double netpaymoney = o[6]==null?0:(Double)o[6];
				netpaySumAmount += netpaymoney;
				Double withdraw = o[7]==null?0:(Double)o[7];
				withdrawSumAmount += withdraw;
				Double profile =o[8]==null?0:(Double)o[8];
				profileSumAmount += profile;
				Date createTime = (Date)o[9];
				Date lastLoginTime = (Date)o[10];
				BigInteger  day = null;
				if(o[11] instanceof Integer ){
					Integer dayi = (Integer)o[11];
					 day = new BigInteger(dayi.toString());
				}else{
					 day = (BigInteger)o[11];
				}
				Integer logintimes = (Integer)o[12];
				Double profitall = o[13]==null?0:(Double)o[13];
				profitAllSumAmount += profitall;
				CustomerAnalysis ca = new CustomerAnalysis(name,warnflag1,level1,agent1,intros, deposit,netpaymoney, withdraw, profile, createTime, lastLoginTime, day, logintimes ,profitall);
				calist.add(ca);
			}
			sumlist.add(depositSumAmount);
			sumlist.add(netpaySumAmount);
			sumlist.add(withdrawSumAmount);
			sumlist.add(profileSumAmount);
			sumlist.add(profitAllSumAmount);
		}
		Map<String,List> map = new HashMap<String,List>();
		map.put("calist", calist);
		map.put("sumlist", sumlist);
		
		//进行a.b.c.d.e分配
		if("_e685201314".equals(intro)){
			if("88".equals(nintvalday)){
				String [] str = {"a","b","c","d","e"};
				if(null!=calist && !calist.isEmpty()){
					for(int i=0,j=0;i<calist.size();i++,j++){
						if(i%5==0){
							j=0;
						}
						String userName = calist.get(i).getLoginname();
						Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
						if(user==null){
							continue;
						}
						user.setIntro(str[j]);
						update(user);
					}
				}
			}
		}
		return map;
	}
	
	@Override
	public Map<String, List> queryCustomerInfoAnalysisNew(Integer agentType ,Date start, Date end, String loginname, Integer level, Integer warnflag, String agent, String intvalday, String nintvalday, String intro, String partner, Date startDate , Date endDate,
			String order, String by) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Session session = null;
		Map<String,List> map = new HashMap<String,List>();
		try {
			session = slaveDao.getHibernateTemplate().getSessionFactory().getCurrentSession();

			StringBuffer userCondition = new StringBuffer();
			
			
			StringBuffer totalSql = new StringBuffer(
					" select loginname,sum(profitall)profitall "+
					" from( "+
					" 	select loginname,0-sum(amount)as profitall from proposal where type = 502 and flag= 2 group by loginname "+
					" 	union all "+
					" 	select loginname,sum(amount)as profitall from proposal where type = 503 and flag= 2 group by loginname "+
					" 	union all "+
					" 	select loginname,0-sum(money) as profitall from payorder where type=0 and flag= 0 group by loginname "+
					" )t group by loginname ");
			
			StringBuffer querySql = new StringBuffer("select t_shuying.loginname,warnflag,level,agent,intro,tdeposit,netpaymoney,withdraw "+
					",withdraw-netpaymoney-tdeposit profit,createtime,lastLoginTime "+
					",DATEDIFF(CURRENT_TIMESTAMP,lastLoginTime) as intvalday,loginTimes,0 as profitall,role ,partner , fri.friendbonus "+
					"from( "+
					" select loginname,sum(tdeposit)tdeposit,sum(withdraw)withdraw,sum(netpaymoney)netpaymoney "+
					" from( "+
					"  select loginname,sum(amount)as tdeposit,0 withdraw ,0 as netpaymoney from proposal where type = 502 and flag= 2 and createtime >=:startDate and createtime <=:endDate  group by loginname "+
					"  union all "+
					"  select loginname,0 as tdeposit,sum(amount)as withdraw ,0 as netpaymoney from proposal where type = 503 and flag= 2 and createtime >=:startDate and createtime <=:endDate group by loginname "+
					"  union all "+
					"  select loginname,0 as tdeposit,0 as withdraw,sum(money) as netpaymoney from payorder where type = 0 and flag= 0 and createtime >=:startDate and createtime <=:endDate group by loginname "+
					
					" )t group by loginname "+
					")t_shuying "+
					"left join ( SELECT SUM(amount) as friendbonus,loginname from proposal where type = '390' AND flag = '2' AND executeTime >=:startDate AND executeTime <=:endDate GROUP BY loginname ) fri on (fri.loginname = t_shuying.loginname) "+
					"left join users u on (u.loginname = t_shuying.loginname @Condition) where ifnull(u.loginname,'')!='' and role='MONEY_CUSTOMER'  ");
					
			
			Map<String,Object> mm = new HashMap<String, Object>();
			mm.put("startDate",sdf.format(start) );
			mm.put("endDate",sdf.format(end) );
			
			if(null!=loginname && !"".equals(loginname)){
				userCondition.append(" and u.loginname =:lg");
				mm.put("lg",loginname );
			}
			if(null!=intvalday && !"".equals(intvalday)){
				userCondition.append(" and DATEDIFF(CURRENT_TIMESTAMP,u.lastLoginTime) <=:itd");
				mm.put("itd",intvalday );
			}
			if(null!=nintvalday && !"".equals(nintvalday)){
				userCondition.append(" and DATEDIFF(CURRENT_TIMESTAMP,u.lastLoginTime) >=:itd");
				mm.put("itd",nintvalday );
			}
			if(null!=intro && !"".equals(intro)){
				if("_e685201314".equals(intro)){
					if("88".equals(nintvalday)){
						userCondition.append(" and (u.intro ='' or u.intro is null) ");
					}
				}else{
					userCondition.append(" and u.intro =:ito");
					mm.put("ito",intro );
				}
			}
			if(StringUtils.isNotBlank(partner)){
				userCondition.append(" and u.agent in (select u2.loginname from users u2 where u2.partner=:partner) ");
				mm.put("partner",partner );
			}
			if(null != startDate && null != endDate){
				userCondition.append(" and u.agent in (select u3.loginname from users u3 where u3.role=:u3role and u3.createtime>=:u3agentstarttime  and u3.createtime<=:u3agentendtime ) ");
				mm.put("u3role",UserRole.AGENT.getCode() );
				mm.put("u3agentstarttime",DateUtil.formatDateForStandard(startDate) );
				mm.put("u3agentendtime",DateUtil.formatDateForStandard(endDate) );
			}
			if(null != agentType && -1!=agentType){
				userCondition.append(" and u.agent in (select a4.loginname from internal_agency a4 where a4.type=:iaType) ");
				mm.put("iaType",agentType );
			}
			if(null != agentType && -1==agentType){
				userCondition.append(" and u.agent not in(select loginname from internal_agency ) ");
			}
			if(null!=agent && !"".equals(agent)){
				userCondition.append(" and u.agent =:agt");
				mm.put("agt",agent );
			}
			if(null!=level && !"".equals(level)){
				userCondition.append(" and u.level =:lel");
				mm.put("lel",level );
			}
			if(null!=warnflag && !"".equals(warnflag)){
				userCondition.append(" and u.warnflag =:wfg");
				mm.put("wfg",warnflag );
			}
			querySql.append(" order by "+by+" "+order);

			int startIndex = querySql.indexOf("@Condition");
			querySql = querySql.replace(startIndex, startIndex+10, userCondition.toString());
			Query query = session.createSQLQuery(querySql.toString());
			if(!mm.isEmpty()){
				query = query.setProperties(mm);//直接将map参数传组query对像
			}

			Query totalQuery = session.createSQLQuery(totalSql.toString());
			List<Object[]> totalList = totalQuery.list();
			Map<Object,Object> totalMap = new HashMap<Object,Object>();
			int totalLen = totalList.size();
			for(int i =0;i<totalLen;i++){
				totalMap.put(totalList.get(i)[0], totalList.get(i)[1]);
			}
			
			List list = query.list();
			
			List<CustomerAnalysis> calist = new ArrayList<CustomerAnalysis>();
			
			List<Double> sumlist = new ArrayList<Double>();
			Double depositSumAmount=0.00;
			Double netpaySumAmount=0.00;
			Double withdrawSumAmount=0.00;
			Double profileSumAmount=0.00;
			Double profitAllSumAmount=0.00;
			Double friendBonusAll = 0.0;
			for(Object obj : list){
				Object[] o=(Object[])obj;
				String name = (String)o[0];
				Integer warnflag1 = (Integer)o[1];
				Integer level1 = (Integer)o[2];
				String agent1 = (String)o[3];
				String intros = (String)o[4];
				Double deposit =  o[5]==null?0:(Double)o[5];
				depositSumAmount +=deposit;
				Double netpaymoney = o[6]==null?0:(Double)o[6];
				netpaySumAmount += netpaymoney;
				Double withdraw = o[7]==null?0:(Double)o[7];
				withdrawSumAmount += withdraw;
				Double profile =o[8]==null?0:(Double)o[8];
				profileSumAmount += profile;
				Date createTime = (Date)o[9];
				Date lastLoginTime = (Date)o[10];
				BigInteger  day = null;
				if(o[11] instanceof Integer ){
					Integer dayi = (Integer)o[11];
					 day = new BigInteger(dayi.toString());
				}else{
					 day = (BigInteger)o[11];
				}
				Integer logintimes = (Integer)o[12];
				Double friendBonus = (Double)o[16];
				Double profitall = (Double) (totalMap.get(name)==null?0.00:totalMap.get(name));
				profitAllSumAmount += profitall;
				friendBonusAll += (friendBonus == null?0.0:friendBonus);
				CustomerAnalysis ca = new CustomerAnalysis(name,warnflag1,level1,agent1,intros, deposit,netpaymoney, withdraw, profile, createTime, lastLoginTime, day, logintimes ,profitall,friendBonus);
				calist.add(ca);
			}
			//集合排序  总输赢排序
			if(StringUtils.isNotBlank(by) && StringUtils.isNotBlank(order)){
				if(by.equals("profitall")){
					CustomerAnalysisComparator comparator = new CustomerAnalysisComparator();
					if(order.equals("asc")){
						Collections.sort(calist , comparator);
					}else{
						Collections.sort(calist , Collections.reverseOrder(comparator));
					}
				}
			}
			sumlist.add(depositSumAmount);
			sumlist.add(netpaySumAmount);
			sumlist.add(withdrawSumAmount);
			sumlist.add(profileSumAmount);
			sumlist.add(profitAllSumAmount);
			sumlist.add(friendBonusAll);
			
			map.put("calist", calist);
			map.put("sumlist", sumlist);
			
			//进行a.b.c.d.e分配
			if("_e685201314".equals(intro)){
				if("88".equals(nintvalday)){
					String [] str = {"a","b","c","d","e"};
					if(null!=calist && !calist.isEmpty()){
						for(int i=0,j=0;i<calist.size();i++,j++){
							if(i%5==0){
								j=0;
							}
							String userName = calist.get(i).getLoginname();
							Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
							if(user==null){
								continue;
							}
							user.setIntro(str[j]);
							update(user);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public List queryCustomerInfoAnalysisNew2(Integer agentType, Date start, Date end, String loginname, Integer level, Integer warnflag, String agent, String intvalday, String nintvalday, String intro, String partner, Date startDate, Date endDate, String order, String by,String flag) {
		
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		
		sb.append("select * from (");
		sb.append("select d.login_name,u.accountName,u.warnflag,u.level,u.agent,u.intro,u.partner,d.deposit,d.pay,d.withdrawal,d.friend_bonus,u.createtime,u.lastLoginTime,");
		sb.append("datediff(current_timestamp,u.lastLoginTime) as intvalday,u.loginTimes,d.profit as dtprofit,s.profit as zdprofit,u.flag from");
		sb.append("(select d.login_name,d.agent,sum(d.deposit) as deposit,sum(d.pay) as pay,sum(d.withdrawal) as withdrawal,sum(d.friend_bonus) as friend_bonus,sum(d.profit) as profit");
		sb.append(" from data_gather_detail d WHERE 1 = 1 and d.create_time >= :start and d.create_time <= :end group by login_name) d, users u, data_gather_summary s");
		sb.append(" where 1 = 1 and d.login_name = u.loginname and d.login_name = s.login_name) a WHERE 1 = 1");
		
		paramsMap.put("start", start);
		paramsMap.put("end", end);
		
		if (StringUtils.isNotBlank(loginname)) {
			
			sb.append(" and login_name = :loginname");
			paramsMap.put("loginname", loginname);
		}

		if (StringUtils.isNotBlank(flag)) {

			sb.append(" and a.flag = :flag");
			paramsMap.put("flag",Integer.valueOf(flag));
		}
		
		if (StringUtils.isNotBlank(intro)) {
			
			sb.append(" and intro = :intro");
			paramsMap.put("intro", intro);
		}
		
		if (null != level) {
			
			sb.append(" and level = :level");
			paramsMap.put("level", level);
		}
		
		if (StringUtils.isNotBlank(partner)) {
			
			sb.append(" and agent in (select loginname from users where partner = :partner)");
			paramsMap.put("partner", partner);
		}
		
		if (null != startDate && null != endDate) {
			
			sb.append(" and a.agent in (select u3.loginname from users u3 where u3.role = 'AGENT' and u3.createtime >= :startDate and u3.createtime <= :endDate)");
			paramsMap.put("startDate", startDate);
			paramsMap.put("endDate", endDate);
		}
		
		if (StringUtils.isNotBlank(agent)) {
			
			sb.append(" and a.agent = :agent");
			paramsMap.put("agent", agent);
		}
		
		if (null != warnflag) {
			
			sb.append(" and a.warnflag = :warnflag");
			paramsMap.put("warnflag", warnflag);
		}
		
		if (StringUtils.isNotBlank(nintvalday)) {
			
			sb.append(" and a.intvalday >= :nintvalday");
			paramsMap.put("nintvalday", nintvalday);
		}
		
		if (StringUtils.isNotBlank(intvalday)) {
			
			sb.append(" and a.intvalday <= :intvalday");
			paramsMap.put("intvalday", intvalday);
		}
		
		if (null != agentType) {
			
			if (-1 == agentType) {
				
				sb.append(" and a.agent not in(select loginname from internal_agency)");
			} else {
				
				sb.append(" and a.agent in (select a4.loginname from internal_agency a4 where a4.type = :agentType)");
				paramsMap.put("agentType", agentType);
			}
		}
		
		if (StringUtils.isNotBlank(by) && StringUtils.isNotBlank(order)) {
		
			sb.append(" order by " + by + " " + order + "");
		} else {
			
			sb.append(" order by dtprofit desc");
		}
		
		try {
			
			return proposalDao.getListBySql(sb.toString(), paramsMap);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String setWarnLevel(String loginname, Integer level,
			String operatorLoginname,String warnremark) {
		return userDao.setWarnLevel(loginname, level, operatorLoginname,warnremark);
	}

	@Override
	public String editRateUser(String userName, String operator,
			Double rate,String platform) {
		String msg = null;
		Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
		Double oldRate = null;
		if (user == null) {
			msg = "未找到该用户";
		} else {
			if("ea".equals(platform)){
				oldRate = user.getRate();
				user.setRate(rate);
			}else if("bbin".equals(platform)){
				oldRate = user.getBbinrate();
				user.setBbinrate(rate);
			}else if("ag".equals(platform)){
				oldRate = user.getAgrate();
				user.setAgrate(rate);
			}else if("keno".equals(platform)){
				oldRate = user.getKenorate();
				user.setKenorate(rate);
			}else if("agin".equals(platform)){
				oldRate = user.getAginrate();
				user.setAginrate(rate);
			}else if("sb".equals(platform)){
				oldRate = user.getSbrate();
				user.setSbrate(rate);
			}else{
				return "platform参数不正确";
			}
			update(user);
			if(user.getFlag() == 0 ){
				Userstatus userstatus = (Userstatus)get(Userstatus.class, userName,LockMode.UPGRADE);
				if(null != userstatus){
					userstatus.setLoginerrornum(0);
					userstatus.setCashinwrong(0);
					update(userstatus);
				}
			}
			logDao.insertOperationLog(operator, OperationLogType.RATE,platform+" 会员[ "+userName+" ]反水率由："+oldRate+" 修改为 "+rate+"，" +"，反水率："+rate);
			msg = null;
		}
		return msg;
	}

	@Override
	public String editUser(String loginname, String operatorLoginname,
			String intro) {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		String oldintro = user.getIntro();
		if (user == null) {
			msg = "未找到该用户";
		} else {
			user.setIntro(intro);
			update(user);
			logDao.insertOperationLog(operatorLoginname, OperationLogType.INTRO,"会员[ "+loginname+" ]推荐码由："+oldintro+" 修改为 "+intro);
			msg = null;
		}
		return msg;
	}
	
	@Override
	public String editAgentPartner(String loginname, String operatorLoginname,
			String partner) {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		String oldpartner = user.getPartner() ;
		if (user == null) {
			msg = "未找到该代理";
		} else {
			user.setPartner(partner);
			update(user);
			logDao.insertOperationLog(operatorLoginname, OperationLogType.INTRO,"代理[ "+loginname+" ]推荐码由："+oldpartner+" 修改为 "+partner);
			msg = null;
		}
		return msg;
	}
	
	public Double getAgentProposal(Date start,Date end){
		return userDao.getAgentProposal(start, end);
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public SlaveDao getSlaveDao() {
		return slaveDao;
	}

	public void setSlaveDao(SlaveDao slaveDao) {
		this.slaveDao = slaveDao;
	}
	
	
	@Override
	public String payYesterdayPoint(String sql) {
		//从配置表中查询存款对应积分的倍数---根据玩家分级别
		List<SystemConfig> listConfg=querySystemConfig2(slaveDao.getHibernateTemplate(),"type004","","否");//查询兑换积分比例
		List<SystemConfig> listConfgRequest=querySystemConfig2(slaveDao.getHibernateTemplate(),"type005","001","否");//规定的任务要求
		List<SystemConfig> listConfgAddPoint=querySystemConfig2(slaveDao.getHibernateTemplate(),"type006","001","否");//完成任务要求的金额   额外赠送的积分
		Double addPoints=null;//完成任务 额外奖励的积分
		Double requesMoney=null;//任务要求的存款量
		if(null!=listConfgAddPoint&&listConfgAddPoint.size()>0){
			SystemConfig sc = listConfgAddPoint.get(0);
			addPoints=Double.parseDouble(sc.getValue());
		}
		if(null!=listConfgRequest&&listConfgRequest.size()>0){
			SystemConfig sc = listConfgRequest.get(0);
			requesMoney=Double.parseDouble(sc.getValue());
		}
		Session  session = slaveDao.getHibernateTemplate().getSessionFactory().openSession();
		List list =session.createSQLQuery(sql).list() ;//查询昨日存款
		String nowday =DateUtil.fmtYYYY_MM_DD(new Date());
		if(null!=list&&list.size()>0){
			for(Object obj :list){
				int multiple=1;//积分与存款总额的倍数
				double totalpoints=0.0;
				DetailPoint dp = new DetailPoint();
				Object[] strs =(Object[])obj;
				Double money=(Double) strs[0];
				String username=strs[1]+"";
				String level=strs[2]+"";
				if(null!=listConfg&&listConfg.size()>0){//与配置对比  查询比例
					for(SystemConfig sc :listConfg){
						if(sc.getItemNo().equals(level)){
							multiple=Integer.parseInt(sc.getValue());
						}
					}
				}
				if(null!=requesMoney&&null!=addPoints&&money>=requesMoney){
					totalpoints=money*multiple+addPoints;
				}else{
					totalpoints=Arith.mul(money, multiple);
				}
				dp.setPoints(totalpoints);
				dp.setSumamount(money);
				dp.setUsername(username);
				dp.setType("0");//积分收入
				dp.setCreateday(nowday);
				dp.setCreatetime(new Date());
				DetachedCriteria dc = DetachedCriteria.forClass(TotalPoint.class);
				dc.add(Restrictions.eq("username", username));
				List<TotalPoint> listTotal = getHibernateTemplate().findByCriteria(dc);
				TotalPoint tp = null;
				if(null!=listTotal&&listTotal.size()>0){
					tp = listTotal.get(0);
					dp.setRemark("积分从"+tp.getTotalpoints()+"增加到"+(totalpoints+tp.getTotalpoints()+"新增："+totalpoints));
					tp.setTotalpoints(totalpoints+tp.getTotalpoints());
					tp.setUpdatetime(new Date());
				}else{
					tp=new TotalPoint();
					tp.setUsername(username);
					tp.setTotalpoints(totalpoints);
					tp.setUpdatetime(new Date());
					dp.setRemark("积分从0增加到"+tp.getTotalpoints());
				}
				this.saveOrUpdate(tp);
				this.save(dp);
			}
		}
		if(null!=session){
			session.close();
		}
		return null;
	}
	
	
	private String addpoint(String loginname,Double addpoint){
		String remark="";
		DetachedCriteria dc = DetachedCriteria.forClass(TotalPoint.class);
		dc.add(Restrictions.eq("username", loginname));
		List<TotalPoint> listTotal = getHibernateTemplate().findByCriteria(dc);
		TotalPoint tp = null;
		if(null!=listTotal&&listTotal.size()>0){
			tp = listTotal.get(0);
			String oldtotalpoint=tp.getTotalpoints()+"";
			String nowtotalpoint=Arith.add(addpoint, tp.getTotalpoints())+"";
			remark="积分从"+oldtotalpoint+"增加到"+nowtotalpoint;
			tp.setTotalpoints(Arith.round(Arith.add(addpoint, tp.getTotalpoints()), 2));
			tp.setOldtotalpoints(Arith.round(Arith.add(addpoint, tp.getOldtotalpoints()), 2));
			tp.setUpdatetime(new Date());
		}else{
			tp=new TotalPoint();
			tp.setUsername(loginname);
			tp.setTotalpoints(Arith.round(addpoint, 2));
			tp.setOldtotalpoints(Arith.round(addpoint, 2));
			tp.setUpdatetime(new Date());
			remark="积分从0增加到"+addpoint;
		}
		this.save(tp);	
		return remark;
	}
	
	
	private String querySystemConfig(String typeNo, String itemNo, String flag) {

		String str = "";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if (!StringUtils.isEmpty(typeNo)) {
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if (!StringUtils.isEmpty(itemNo)) {
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if (!StringUtils.isEmpty(flag)) {
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list = getHibernateTemplate().findByCriteria(dc);
		if (null != list && list.size() > 0) {
			str = list.get(0).getValue();
		}
		return str;
	}
	private  List<SystemConfig> querySystemConfig2(HibernateTemplate getHibernateTemplate, String typeNo, String itemNo, String flag){
		String str="";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if(!StringUtils.isEmpty(typeNo)){
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if(!StringUtils.isEmpty(itemNo)){
			dc = dc.add(Restrictions.eq("itemNo", itemNo));
		}
		if(!StringUtils.isEmpty(flag)){
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list=getHibernateTemplate.findByCriteria(dc);
		return list;
	}
	
	private static Integer userid = 0 ;
	public static Integer count = 0 ;
	@Override
	public void updatePtInfo(String pttype) {/*
		// TODO Auto-generated method stub
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession() ;
		Transaction tx = null ;
		while (userid < 2001896960 && count<=1){
			SQLQuery query = session.createSQLQuery("select loginname , id , remark from "+pttype+" where remark is null order by id desc limit 1000 ");
			List<Object[]> users = query.list();
			List<String> userStrs = null ;
			if(users.size()<=20){
				userStrs = new ArrayList<String>();
				for (Object[] userObj : users) {
					userStrs.add((String)userObj[0]);
				}
				//去批量处理玩家
				String info = PtUtil.massBatchUpdate("E",userStrs, "sunrise", "sunrise");
				tx = session.beginTransaction();
				if(StringUtils.isBlank(info)){//本批次的10个账号都是未成功的
					savePtUpdateInfo(userStrs, 0, null,session,pttype);
				}else{
					savePtUpdateInfo(null, null, info,session,pttype);
				}
				return ;
			}
			ExecutorService fixedThreadPool= Executors.newFixedThreadPool(10);
			for (Object[] userObj : users) {
				if(null == userStrs){
					userStrs = new ArrayList<String>();
				}else if(userStrs.size() == 20 ){
					fixedThreadPool.execute(new DealPtInfo(userStrs, session, tx , pttype));
					count ++ ;
					//去批量处理玩家
					String info = PtUtil.massBatchUpdate("E",userStrs, "sunrise", "sunrise");
					tx = session.beginTransaction();
					if(StringUtils.isBlank(info)){//本批次的10个账号都是未成功的
						savePtUpdateInfo(userStrs, 0, null,session,pttype);
					}else{
						savePtUpdateInfo(null, null, info,session , pttype);
					}
					tx.commit();
					session.flush();
					System.out.println("满20");
					userStrs = null ;
				}else{
					userStrs.add((String)userObj[0]);
					userid = (Integer)userObj[1] ;
				}
			}
		}
	*/}
	
	class DealPtInfo extends Thread{
		private List<String> userStrs ;
		private Session session ;
		private Transaction tx ;
		private String pttype ;
		public DealPtInfo(List<String> userStrs , Session session , Transaction tx , String pttype){
			this.userStrs = userStrs ;
			this.session = session ;
			this.tx = tx ;
			this.pttype = pttype ;
		}
		@Override
		public void run() {/*
			//去批量处理玩家
			String info = PtUtil.massBatchUpdate("E",userStrs, "sunrise", "sunrise");
			tx = session.beginTransaction();
			if(StringUtils.isBlank(info)){//本批次的10个账号都是未成功的
				savePtUpdateInfo(userStrs, 0, null,session, pttype);
			}else{
				savePtUpdateInfo(null, null, info,session,pttype);
			}
			tx.commit();
			session.flush();
			count -- ;
			super.run();
		*/}
	}
	
	public void savePtUpdateInfo(List<String> users , Integer status , String pthtml , Session session , String pttype){
			if(null != users && null != status){
				for (String loginname : users) {
					try {
						session.createSQLQuery("update "+pttype+" set remark = '"+status+"' where loginname = '"+loginname+"' ").executeUpdate();;
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
			}else if(StringUtils.isNotBlank(pthtml) && !pthtml.contains("error")){
				JSONObject json = JSONObject.fromObject(pthtml);
				JSONArray jsons = json.getJSONArray("result");
				String remark = "";
				for (Object object : jsons) {
					String _loginname = JSONObject.fromObject(object).get("playername").toString() ;
					String _updatestr = JSONObject.fromObject(object).get("update").toString() ;
					String loginname = _loginname.substring(1, _loginname.length()) ;
					status = 0 ;
					if(_updatestr.contains("Player's information has been updated")){
						remark = "1" ;
					}else{
						remark = _updatestr ;
					}
					try {
						session.createSQLQuery("update "+pttype+" set remark = '"+remark+"' where loginname = '"+loginname+"' ").executeUpdate();;
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	
	@Override
	public Map<String, List> queryAgentAnalysis(Integer agentType, Date start, Date end, String loginname,
			Integer level, Integer warnflag, String agent, String intvalday, String nintvalday,
			String partner, Date startDate, Date endDate, String order, String by) {
		
		Session session = null;
		Map<String,List> map = new HashMap<String,List>();
		try {
			session = slaveDao.getHibernateTemplate().getSessionFactory().getCurrentSession();

			//查询代理下线的输赢以及存提款 
			StringBuffer totalSql = new StringBuffer(
					" select u.agent , sum(profitall) , SUM(depositall) , sum(drawall) from ( "+
					" select loginname,sum(profitall) profitall ,sum(depositamount) depositall , sum(drawamount) drawall from(  "+
					" select loginname,0-sum(amount)as profitall , sum(amount)as depositamount , 0 as drawamount from proposal where type = 502 and flag= 2 and agent is not null and createTime>=:start and createTime<=:end  group by loginname "+
					" union all "+
					" select loginname,sum(amount)as profitall , 0 as depositamount , sum(amount) as  drawamount from proposal where type = 503 and flag= 2 and agent is not null and createTime>=:start and createTime<=:end group by loginname "+
					" union all "+
					" select loginname,0-sum(money) as profitall , sum(money)as depositamount , 0 as drawamount  from payorder where type=0 and flag= 0 and createTime>=:start and createTime<=:end  group by loginname "+
					" )t group by loginname " +
					" ) q1  LEFT JOIN users u on q1.loginname = u.loginname where  1=1 " );
			totalSql.append(" GROUP BY u.agent ");
			
			Map<String,Object> mapEntry = new HashMap<String, Object>();
			mapEntry.put("start",sdf.format(start) );
			mapEntry.put("end",sdf.format(end) );
			
			
			//查询代理基本信息以及注册人数，游戏投注总额，游戏输赢总额
			StringBuffer querySql = new StringBuffer("select u0.loginname , u0.createtime,u0.partner,u0.warnflag,pcl.liveaccountall,u0.lastLoginTime,u0.loginTimes "+
					" ,i.type,pcs.slotaccountall , ua.regnum , ag.betall , ag.agentprofitall ,uni11.depnum , DATEDIFF(CURRENT_TIMESTAMP,u0.lastLoginTime) as intvalday , pc.agentdrwalamount , fri.friendbonus "+
					" from users u0 "+
					" LEFT JOIN internal_agency i on u0.loginname=i.loginname  "+
					" LEFT JOIN userstatus us on u0.loginname=us.loginname "+
					" LEFT JOIN (select agent , COUNT(loginname) regnum from users where createtime >=:start and createtime <=:end and agent is not null GROUP BY agent) ua on u0.loginname=ua.agent "+
					" LEFT JOIN (SELECT agent , SUM(bettotal) betall , SUM(amount) agentprofitall from agprofit where createTime>=:start  and createtime <=:end  and agent is not null GROUP BY agent) ag on u0.loginname=ag.agent" +
					/*" LEFT JOIN ("+
						" SELECT count(DISTINCT uni1.loginname) depnum , u2.agent from "+
						" ("+
						" select loginname from proposal where createTime>=:start and createTime<=:end and type=502 and flag=2 and agent is not null  GROUP BY loginname "+
						" UNION ALL "+
						" select loginname from payorder p1 where p1.createTime>=:start  and p1.createTime<=:end and p1.type=0 and p1.flag=0 GROUP BY p1.loginname"+ 
						" ) uni1 LEFT JOIN users u2 on uni1.loginname = u2.loginname GROUP BY u2.agent " +
						" ) uni11 on uni11.agent=u0.loginname" +*/
					" LEFT JOIN (SELECT COUNT(loginname) depnum , agent from users where createtime >=:start and createtime <=:end and agent is not null and isCashin=0 GROUP BY agent ) uni11 on uni11.agent=u0.loginname "+
					" LEFT JOIN (select agent , SUM(amount) agentdrwalamount from ptcommissions where flag=1 and createTime>=:start and createTime<=:end GROUP BY agent  ) pc on u0.loginname=pc.agent "+
					" LEFT JOIN (select agent , SUM(amount) slotaccountall from ptcommissions where flag=1 and createTime>=:start and createTime<=:end and platform='slotmachine' GROUP BY agent  ) pcs on u0.loginname=pcs.agent "+
					" LEFT JOIN (select agent , SUM(amount) liveaccountall from ptcommissions where flag=1 and createTime>=:start and createTime<=:end and platform='liveall' GROUP BY agent  ) pcl on u0.loginname=pcl.agent "+
					" LEFT JOIN (SELECT u.agent agent, sum(p.amount) friendbonus FROM users u LEFT JOIN proposal p ON u.loginname = p.loginname WHERE u.agent IS NOT NULL AND u.agent <> '' AND p.type = '390' AND p.flag = '2' AND p.executeTime >=:start AND p.executeTime <=:end GROUP BY u.agent) fri on u0.loginname = fri.agent "+
					" where u0.role='AGENT' " );
			
			
			Map<String,Object> mm = new HashMap<String, Object>();
			mm.put("start",sdf.format(start) );
			mm.put("end",sdf.format(end) );
			
			/*if(null != startDate && null != endDate){
				querySql.append(" and u0.createtime>=:startDate and u0.createtime<=:endDate ");
				mm.put("startDate",sdf.format(startDate) );
				mm.put("endDate",sdf.format(endDate) );
			}*/
			
			if(null != startDate){
				querySql.append(" and u0.createtime>=:startDate ");
				mm.put("startDate",sdf.format(startDate) );
			}
			
			if(null != endDate){
				querySql.append(" and u0.createtime<=:endDate ");
				mm.put("endDate",sdf.format(endDate) );
			}
			
			if(null!=loginname && !"".equals(loginname)){
				querySql.append(" and u0.loginname =:lg");
				mm.put("lg",loginname );
			}
			if(StringUtils.isNotBlank(intvalday)){
				querySql.append(" and DATEDIFF(CURRENT_TIMESTAMP,u0.lastLoginTime) <=:itd");
				mm.put("itd",intvalday );
			}
			if(StringUtils.isNotBlank(nintvalday)){
				querySql.append(" and DATEDIFF(CURRENT_TIMESTAMP,u0.lastLoginTime) >=:nitd");
				mm.put("nitd",nintvalday );
			}
			
			if(StringUtils.isNotBlank(partner)){
				querySql.append(" and u0.partner=:partner ");
				mm.put("partner",partner );
			}
			if(null!=warnflag && !"".equals(warnflag)){
				querySql.append(" and u0.warnflag =:wfg");
				mm.put("wfg",warnflag );
			}
			if(null != agentType && -1!=agentType){
				querySql.append(" and u0.loginname in (select a4.loginname from internal_agency a4 where a4.type=:iaType) ");
				mm.put("iaType",agentType );
			}
			if(null != agentType && -1==agentType){
				querySql.append(" and u0.loginname not in(select loginname from internal_agency ) ");
			}
			querySql.append(" order by u0.createtime desc ");
			
			Query query = session.createSQLQuery(querySql.toString());
			if(!mm.isEmpty()){
				query = query.setProperties(mm);//直接将map参数传组query对像
			}

			Query totalQuery = session.createSQLQuery(totalSql.toString());
			if(!mapEntry.isEmpty()){
				totalQuery = totalQuery.setProperties(mapEntry);//直接将map参数传组query对像
			}
			List<Object[]> totalList = totalQuery.list();
			Map<Object,Double []> totalMap = new HashMap<Object,Double []>();
			int totalLen = totalList.size();
			for(int i =0;i<totalLen;i++){
				totalMap.put(totalList.get(i)[0], new Double[]{(Double) totalList.get(i)[1] , (Double) totalList.get(i)[2] , (Double) totalList.get(i)[3]});
			}
			
			List list = query.list();
			
			List<AgentAnalysis> calist = new ArrayList<AgentAnalysis>();
			
			List<Object> sumlist = new ArrayList<Object>();
			Double depositSumAmount=0.00; //下线总存款
			Double withdrawSumAmount=0.00; //下线总提款
			Double profileSumAmount=0.00;  //下线总输赢
			Double profitAllSumAmount=0.00;//下线游戏输赢
			Integer _SregNumAll = 0 ; //注册总人数
			Integer _SdepNumAll = 0 ;//下线存款总人数
			Double _SbetAll = 0.0 ;//下线投注总额
			Double _SagentWithdraw = 0.0 ; //代理总提款数
			Double _SagentFriendbonus = 0.0;//下线总好友推荐金
			Integer validNumSum = 0;//有效人数
			List<String> agentName = new ArrayList<String>();
			for(Object obj : list){
				Object[] o=(Object[])obj;
				String name = (String)o[0];
				name = name.toLowerCase();
				agentName.add(name);
				Date createTime = (Date)o[1];
				String partner1 = (String)o[2];
				Integer warnflag1 = (Integer)o[3];
				Double credit = (Double)o[4];
				Date lastLoginTime = (Date)o[5];
				Integer logintimes = (Integer)o[6];
				Integer agentType1 = (Integer)o[7];
				Double slotaccount = (Double)o[8];
				BigInteger regnum = (BigInteger)o[9];
				Double betall = (Double)o[10];
				Double agentprofitall = (Double)o[11];
				BigInteger depnum = (BigInteger)o[12];
				BigInteger  day = null;
				if(null != lastLoginTime){
					if(o[13] instanceof Integer ){
						Integer dayi = (Integer)o[13];
						day = new BigInteger(dayi.toString());
					}else{
						day = new BigInteger((o[13]).toString());
					}
				}else{
					day = new BigInteger("0") ;
				}
				Double agentdrawlall = (Double)o[14];
				Double agentFriendBonus = (Double)o[15];
				
				Double profitall = totalMap.get(name)==null?0.00:totalMap.get(name)[0];
				Double depositall = totalMap.get(name)==null?0.00:totalMap.get(name)[1];
				Double drawall = totalMap.get(name)==null?0.00:totalMap.get(name)[2];
//				List proposalList = slaveDao.getAgentReferralsCountAtProposal(name, start, end);
//				List payorderList = slaveDao.getAgentReferralsCountAtPayorder(name, start, end);
//				Map<String, AgentReferralsVO> usersTotal = this.userListToMap(proposalList, payorderList);
//				Set<String> userSet = usersTotal.keySet();
				depositSumAmount += (null==depositall)?0.0:depositall;
				withdrawSumAmount += (null==drawall)?0.0:drawall;
				profileSumAmount += (null==profitall)?0.0:profitall;
				profitAllSumAmount += (null==agentprofitall)?0.0:agentprofitall ;
				_SregNumAll += (null==regnum)?0:regnum.intValue() ;
				_SdepNumAll += (null==depnum)?0:depnum.intValue() ;
				_SbetAll += (null==betall)?0.0:betall ;
				_SagentWithdraw += (null==agentdrawlall)?0.0:agentdrawlall;
				_SagentFriendbonus += (null==agentFriendBonus)?0.0:agentFriendBonus;
				
				AgentAnalysis al = new AgentAnalysis(name, createTime, partner1, warnflag1, agentType1,
						regnum, depnum, betall, depositall, drawall, slotaccount,
						credit, lastLoginTime, logintimes, day, profitall,agentprofitall,agentdrawlall,0,agentFriendBonus) ;
				if(depnum != null && depnum.intValue() > 0){
					al.setIsValid(1);
					validNumSum++;
				} else {
					al.setIsValid(0);
				}
				
				calist.add(al);
			}
			//提到循环外一次查询
			List proposalList = slaveDao.getAgentReferralsCountAtProposal(agentName, start, end);
			List payorderList = slaveDao.getAgentReferralsCountAtPayorder(agentName, start, end);
			Map<String, String> usersTotal = this.userListToMapNew(proposalList, payorderList);
			this.fillActivePlayerCount(calist,usersTotal);
			sumlist.add(_SregNumAll);
			sumlist.add(_SdepNumAll);
			sumlist.add(_SbetAll);
			sumlist.add(profitAllSumAmount);
			sumlist.add(depositSumAmount);
			sumlist.add(withdrawSumAmount);
			sumlist.add(_SagentWithdraw);
			sumlist.add(profileSumAmount);
			sumlist.add(_SagentFriendbonus);
			sumlist.add(validNumSum);
			
			//集合排序  总输赢排序
			if(StringUtils.isNotBlank(by) && StringUtils.isNotBlank(order)){
				if(by.equals("profitall")){
					AgentAnalysisComparator comparator = new AgentAnalysisComparator();
					if(order.equals("asc")){
						Collections.sort(calist , comparator);
					}else{
						Collections.sort(calist , Collections.reverseOrder(comparator));
					}
				}
			}
			map.put("calist", calist);
			map.put("sumlist", sumlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	private void fillActivePlayerCount(List<AgentAnalysis> calist,
			Map<String, String> usersTotal) {
		if(calist == null || usersTotal == null || calist.size() == 0 || usersTotal.size() == 0)
			return;
		
		for(AgentAnalysis cal :calist){
			Iterator it = usersTotal.entrySet().iterator();
			int count = 0;
			while(it.hasNext()){
				Map.Entry entry = (Entry) it.next();
				String agentname = (String) entry.getValue();
				if(agentname.equals(cal.getLoginname())){
					count++;
					it.remove();
				}
			}
			cal.setAgentActiveCount(count);
		}
	}

	private Map<String, String> userListToMapNew(List<Object[]> proposalList, List<Object[]> payorderList) throws Exception {
		Map<String, String> users = new HashMap<String, String>();
		if (proposalList != null) {
			for (int i = 0; i < proposalList.size(); i++) {
				Object[] o = proposalList.get(i);
				users.put(String.valueOf(o[1]), String.valueOf(o[0]));
			}
		}

		if (payorderList != null) {
			for (int i = 0; i < payorderList.size(); i++) {
				Object[] o = payorderList.get(i);
				users.put(String.valueOf(o[1]), String.valueOf(o[0]));
			}
		}

		return users;
	}
	
	private Map<String, AgentReferralsVO> userListToMap(List<Object[]> proposalList, List<Object[]> payorderList) throws Exception {
		Map<String, AgentReferralsVO> users = new HashMap<String, AgentReferralsVO>();
		if (proposalList != null) {
			for (int i = 0; i < proposalList.size(); i++) {
				Object[] o = proposalList.get(i);
				users.put(String.valueOf(o[0]), new AgentReferralsVO(String.valueOf(o[0]), ((Number) o[1]).doubleValue()));
			}
		}

		if (payorderList != null) {
			for (int i = 0; i < payorderList.size(); i++) {
				Object[] o = payorderList.get(i);
				String key = String.valueOf(o[0]);
				if (users.containsKey(key)) {
					AgentReferralsVO vo = users.remove(key);
					vo.setMoney(((Number) o[1]).doubleValue());
					users.put(key, vo);
				} else {
					users.put(String.valueOf(o[0]), new AgentReferralsVO(String.valueOf(o[0]), ((Number) o[1]).doubleValue()));
				}
			}
		}

		return users;
	}

	@Override
	public void updateUserAgent(String loginname, String agent ,String operator) {
		Users user = (Users) userDao.get(Users.class, loginname);
		if(null == user){
			return ;
		}
		Users uagent = (Users) userDao.get(Users.class, agent);
		if(null == uagent){
			return ;
		}
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession() ;
		session.createSQLQuery("update users set agent= ? where loginname = ? ").setParameter(0, agent).setParameter(1, loginname).executeUpdate();
		session.createSQLQuery("update agprofit set agent= ? where loginname = ? ").setParameter(0, agent).setParameter(1, loginname).executeUpdate();
		session.createSQLQuery("update proposal set agent= ? where loginname = ? ").setParameter(0, agent).setParameter(1, loginname).executeUpdate();
		session.createSQLQuery("update data_gather_detail set agent = ? where login_name = ?").setParameter(0, agent).setParameter(1, loginname).executeUpdate();
		session.createSQLQuery("update data_gather_summary set agent = ? where login_name = ?").setParameter(0, agent).setParameter(1, loginname).executeUpdate();
		Operationlogs log = new Operationlogs();
		log.setAction("修改玩家代理");
		log.setCreatetime(new Timestamp(System.currentTimeMillis()));
		log.setLoginname(operator);
		log.setRemark(loginname+"代理由"+user.getAgent()+" 改为"+agent);
		session.save(log);
		session.flush();
		session.clear();
	}
	
	public int getCountProxyFirst(String loginname,String countProxyFirst_start,String countProxyFirst_end){
		 Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		 Query query = session.createSQLQuery("select countProxyFirst('"+loginname+"','"+countProxyFirst_start+"','"+countProxyFirst_end+"') from dual");
		 List list = query.list();
		return Integer.parseInt(list.get(0).toString());
	}
	

	@Override
	public void updateMGFreeGames(List<Offer> offers) throws SQLException {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Connection conn = session.connection();
		PreparedStatement ps = conn.prepareStatement("delete from mgfreegame");
		ps.execute();
		PreparedStatement stmt = conn.prepareStatement("insert into mgfreegame(id, description, cost, playerCount, startDate, endDate) values(?, ? ,? ,? ,?, ?)");
		for (Offer offer : offers) {
			stmt.setInt(1, offer.getId());
			stmt.setString(2, offer.getDescription());
			stmt.setInt(3, offer.getCost());
			stmt.setInt(4, offer.getPlayerCount());
			stmt.setDate(5, new java.sql.Date(offer.getStartDate().getTime()));
			stmt.setDate(6, new java.sql.Date(offer.getEndDate().getTime()));
			stmt.addBatch();
		}
		stmt.executeBatch();
		conn.commit();
	}

	@Override
	public Boolean addPlayersToFreegame(String[] playerArr, Integer gameID, String operator) throws DocumentException, JAXBException {
		Date now = new Date();
		AddPlayersToOfferRequest req = new AddPlayersToOfferRequest();
		for (String playerName : playerArr) {
			AddMGFreeGameLog addMGFreeGameLog = new AddMGFreeGameLog();
			addMGFreeGameLog.setGameID(gameID);
			addMGFreeGameLog.setLoginname(playerName);
			addMGFreeGameLog.setAddDate(now);
			addMGFreeGameLog.setOperator(operator);
			save(addMGFreeGameLog);
			
			PlayerAction item = new PlayerAction();
			item.setISOCurrencyCode("CNY");
			item.setLoginName(playerName);
			item.setPlayerStartDate(new Date());
			item.setOfferId(gameID);
			//item.setInstanceId(addMGFreeGameLog.getId());
			req.getPlayerActions().add(item);
		}
		req.setServerId(OrionUtil.serverId);
		req.setSequence(DateUtil.fmtyyyyMMdd(now) + RandomStringUtils.randomAlphanumeric(8));
		return OrionUtil.addPlayersToFreegame(req);
	}

	@Override
	public List queryPreImportData(Date start, Date end, String intro,
			String isdeposit, Integer level, String agent, String order,
			String by,Integer state) {
		List<DataImportBL> calist = new ArrayList<DataImportBL>();
		Session session = null;
		try {
			session = slaveDao.getHibernateTemplate().getSessionFactory().getCurrentSession();

			StringBuffer totalSql = new StringBuffer(
					" select loginname,sum(profitall)profitall "+
					" from( "+
					" 	select loginname,0-sum(amount)as profitall from proposal where type = 502 and flag= 2 group by loginname "+
					" 	union all "+
					" 	select loginname,sum(amount)as profitall from proposal where type = 503 and flag= 2 group by loginname "+
					" 	union all "+
					" 	select loginname,0-sum(money) as profitall from payorder where type=0 and flag= 0 group by loginname "+
					" )t group by loginname ");
			
			StringBuffer querySql = new StringBuffer();
			querySql.append("SELECT ");
			querySql.append("	u.loginname, ");
			querySql.append("	accountName, ");
			querySql.append("	LEVEL, ");
			querySql.append("	agent, ");
			querySql.append("	intro, ");
			querySql.append("	createtime, ");
			querySql.append("	lastLoginTime, ");
			querySql.append("	DATEDIFF( ");
			querySql.append("		CURRENT_TIMESTAMP, ");
			querySql.append("		lastLoginTime ");
			querySql.append("	) AS intvalday, ");
			querySql.append("	loginTimes, ");
			querySql.append("	phone ");
			querySql.append("FROM ");
			querySql.append("	users u ");
			querySql.append("WHERE ");
			if("Y".equals(isdeposit)){
				querySql.append("	EXISTS ( ");
				querySql.append("		SELECT ");
				querySql.append("			1 ");
				querySql.append("		FROM ");
				querySql.append("			( ");
				querySql.append("				SELECT DISTINCT ");
				querySql.append("					loginname ");
				querySql.append("				FROM ");
				querySql.append("					proposal ");
				querySql.append("				WHERE ");
				querySql.append("					type = 502 ");
				querySql.append("				AND flag = 2 ");
				querySql.append("				AND createtime >=:startDate ");
				querySql.append("				AND createtime <=:endDate  ");				
				querySql.append("				UNION ");
				querySql.append("					SELECT DISTINCT ");
				querySql.append("						loginname ");
				querySql.append("					FROM ");
				querySql.append("						payorder ");
				querySql.append("					WHERE ");
				querySql.append("						type = 0 ");
				querySql.append("					AND flag = 0 ");
				querySql.append("					AND createtime >=:startDate ");
				querySql.append("					AND createtime <=:endDate  ");				
				querySql.append("			) t ");
				querySql.append("		WHERE ");
				querySql.append("			t.loginname = u.loginname ");
				querySql.append("	) ");
			}else if ("N".equals(isdeposit)){
				querySql.append(" NOT EXISTS ( ");
				querySql.append("		SELECT ");
				querySql.append("			1 ");
				querySql.append("		FROM ");
				querySql.append("			( ");
				querySql.append("				SELECT DISTINCT ");
				querySql.append("					loginname ");
				querySql.append("				FROM ");
				querySql.append("					proposal ");
				querySql.append("				WHERE ");
				querySql.append("					type = 502 ");
				querySql.append("				AND flag = 2 ");
				querySql.append("				AND createtime >=:startDate ");
				querySql.append("				AND createtime <=:endDate  ");
				querySql.append("				UNION ");
				querySql.append("					SELECT DISTINCT ");
				querySql.append("						loginname ");
				querySql.append("					FROM ");
				querySql.append("						payorder ");
				querySql.append("					WHERE ");
				querySql.append("						type = 0 ");
				querySql.append("					AND flag = 0 ");
				querySql.append("					AND createtime >=:startDate ");
				querySql.append("					AND createtime <=:endDate  ");				
				querySql.append("			) t ");
				querySql.append("		WHERE ");
				querySql.append("			t.loginname = u.loginname ");
				querySql.append("	) ");
			}else{
				querySql.append("	1=1 ");
			}
			querySql.append(" AND createtime >=:startDate ");
			querySql.append(" AND createtime <=:endDate  ");	
			Map<String,Object> mm = new HashMap<String, Object>();
			mm.put("startDate",sdf.format(start) );
			mm.put("endDate",sdf.format(end) );
			
			if(null != intro && !"".equals(intro)){
				querySql.append(" AND u.intro =:intro ");
				mm.put("intro",intro );
			}
			
			if(null != level && !"".equals(level)){
				querySql.append(" AND u.level =:level ");
				mm.put("level",level );
			}
			if(null != state && state==1){
				querySql.append("AND u.agent is NULL OR  u.agent not in('a_tm01','a_tm02','a_tm03','a_tm04','a_tm05','a_tm06','a_tm07','a_tm08','a_tm09','a_tm10','a_tm11') ");
			}
			if(null != agent && !"".equals(agent)){
				querySql.append(" AND u.agent =:agent ");
				mm.put("agent",agent );
			}
			querySql.append(" order by "+by+" "+order);

			String sql = querySql.toString();


			Query query = session.createSQLQuery(sql);
			if(!mm.isEmpty()){
				query = query.setProperties(mm);//直接将map参数传组query对像
			}

			Query totalQuery = session.createSQLQuery(totalSql.toString());
			List<Object[]> totalList = totalQuery.list();
			Map<Object,Object> totalMap = new HashMap<Object,Object>();
			int totalLen = totalList.size();
			for(int i =0;i<totalLen;i++){
				totalMap.put(totalList.get(i)[0], totalList.get(i)[1]);
			}
			
			List list = query.list();
			
			
			for(Object obj : list){
				Object[] o=(Object[])obj;
				String name = (String)o[0];
				String accountName = (String)o[1];
				Integer level1 = (Integer)o[2];
				String agent1 = (String)o[3];
				String intros = (String)o[4];
				Date createTime = (Date)o[5];
				Date lastLoginTime = (Date)o[6];
				BigInteger  day = null;
				if (o[7] instanceof Integer) {
					Integer dayi = (Integer) o[7];
					day = new BigInteger(dayi+"");
				} else {
					day = (BigInteger) o[7];
				}
				Integer logintimes = (Integer)o[8];
				String dbphone = (String)o[9];
				String phone = "";
				if(StringUtils.isNotEmpty(dbphone)){
					try {
						phone = AESUtil.aesDecrypt(dbphone, AESUtil.KEY)+"@";
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				name = name.toLowerCase();
				Double profitall = (Double) (totalMap.get(name)==null?0.00:totalMap.get(name));
				DataImportBL ca = new DataImportBL(name,accountName,phone,level1,agent1,intros, createTime, lastLoginTime, day, logintimes ,profitall);
				calist.add(ca);
			}
			//集合排序  总输赢排序
			if(StringUtils.isNotBlank(by) && StringUtils.isNotBlank(order)){
				if(by.equals("profitall")){
					DataImportBLComparator comparator = new DataImportBLComparator();
					if(order.equals("asc")){
						Collections.sort(calist , comparator);
					}else{
						Collections.sort(calist , Collections.reverseOrder(comparator));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return calist;
	}

	//把电话号码导入比邻数据库
	@Override
	public String importData(String phones) {
		ObTask vo = new ObTask();
		String task_name = "109"+DateUtil.fmtyyyyMMddHHmmss(new Date());
		vo.setTask_type("2");
		vo.setTask_name(task_name);
		vo.setCreate_at(new Date());
		vo.setCreate_user(109d);
		vo.setUpdate_at(new Date());
		// 开时时间  
        //Long begin = new Date().getTime();  
		try {
			Integer task_id = (Integer) blDao.save(vo);
			// 去掉最后一个字符
			phones = phones.substring(0, phones.length() - 1);
			String[] arr = phones.split("@");
			String prefix = "INSERT INTO task_contact (task_id, contact_name, phone_number) VALUES ";
			Connection conn = blDao.getConn();
			conn.setAutoCommit(false);
			StringBuffer suffix = new StringBuffer();
			final int batchSize = 5000;
			int count = 0;
			PreparedStatement pst = conn.prepareStatement("");
			for (int i = 0; i < arr.length; i++) {
				suffix.append("(" + task_id + ",'" + i + "','" + arr[i] + "'),");
				if (++count % batchSize == 0) {
					// 构建完整sql
					String sql = prefix + suffix.substring(0, suffix.length() - 1);
					// 添加执行sql
					pst.addBatch(sql);
					// 执行操作
					pst.executeBatch();
					// 提交事务
					conn.commit();
					// 清空上一次添加的数据
					suffix = new StringBuffer();
				}
			}
			//不足batchSize条
			// 构建完整sql
			String sql = prefix + suffix.substring(0, suffix.length() - 1);
			// 添加执行sql
			pst.addBatch(sql);
			// 执行操作
			pst.executeBatch();
			// 提交事务
			conn.commit();
			// 清空上一次添加的数据
			suffix = new StringBuffer();
			
			pst.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "导入失败：" + e.getMessage();
		}
        // 结束时间  
        //Long end = new Date().getTime();  
        // 耗时  
        //System.out.println("cast : " + (end - begin) / 1000 + " ms");  
		return "导入成功！";
	}

	public BLDao getBlDao() {
		return blDao;
	}

	public void setBlDao(BLDao blDao) {
		this.blDao = blDao;
	}
	
	//解除禁用
	public String relieveOperator(String loginname) {
		String msg = "用户不存在";
		Operator op = (Operator) get(Operator.class, loginname);
		if (op != null) {
			
			if(op.getEnabled()==1){
				op.setEnabled(0);
				op.setPasswordNumber(0);
				update(op);
				msg = "解除成功";
				return msg;
			}else{
				msg = "该账号正常，不需要解除";
			}
		}
		return msg;
	}	
	
	public String resetOperatorPassword(String operatorUsername, String userName) {
		Operator operator = (Operator) get(Operator.class, userName);
		
		if (null == operator) {
			return "没有找到账号为“" + userName + "”的后台信息，请核实后再进行操作！";
		}
		
		operator.setPassword(EncryptionUtil.encryptPassword("123456"));
		operator.setPasswordNumber(0);
		operator.setEnabled(0);
		update(operator);
		logDao.insertOperationLog(operatorUsername, OperationLogType.MODIFY_OWN_PWD, null);
		
		return "密码重置成功，重置后的密码为：123456，建议登录后修改密码！";
	}


	@Override
	public void insertOperatorSendSMSLog(String operator, String remark) {

		logDao.insertOperationLog(operator, OperationLogType.SEND_PHONESMS_LOG, remark);
		log.info("后台人员发送短信:"+operator+","+remark);
	}
	
	@Override
	public String relievePtLimit(String loginname, String username) {
		Users user = (Users) get(Users.class, loginname) ;
		if(null != user && org.apache.commons.lang3.StringUtils.isNotBlank(user.getShippingcodePt())){
			user.setShippingcodePt(null);
			update(user);
			logDao.insertOperationLog(username, OperationLogType.RELIEVE_PTLIMIT, "后台账号："+username+"解除玩家账号："+loginname+"pt流水限制");
			return "解除成功！";
		}else{
			return "用户不存在或者pt优惠码为空！";
		}
	}
	@Override
	public Boolean bindEmployeeNo(String loginname,String employeeNo) {
		return userDao.bindEmployeeNO(loginname,employeeNo);
	}

	@Override
	public void insertExceptionLog(com.nnti.office.model.auth.Operator operator, String remark) {
		operator.setEnabled(1);
		logDao.update(operator);
		Operationlogs log = new Operationlogs();
		log.setLoginname(operator.getUsername());
		log.setCreatetime(DateUtil.getCurrentTimestamp());
		log.setAction(OperationLogType.OPERTR_EXCEPTION.getCode());
		log.setRemark("异常:"+remark);
		logDao.save(log);
	}
}
