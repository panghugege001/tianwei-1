package dfh.skydragon.webservice;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import dfh.model.*;
import dfh.service.interfaces.AiSupportService;
import dfh.service.interfaces.IUserbankinfoService;
import dfh.utils.*;
import net.sf.json.JSONArray;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.bean.SmsInfoVo;
import dfh.model.enums.GxVoiceEnum;
import dfh.model.enums.UserRole;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ProposalService;
import dfh.utils.compare.SmsInfoVoComparator;

public class Axis2WebServiceWS{
	private static Logger log = Logger.getLogger(Axis2WebServiceWS.class);
	private CustomerService cs;
	private AiSupportService aiSupportService;
	private ProposalService proposalService;
	private IUserbankinfoService userbankinfoService;

	public IUserbankinfoService getUserbankinfoService() {
		return userbankinfoService;
	}

	public void setUserbankinfoService(IUserbankinfoService userbankinfoService) {
		this.userbankinfoService = userbankinfoService;
	}

	//前端打电话
	public String makeCall(String loginname , String phonenum){
		Users user = (Users) cs.get(Users.class, loginname) ;
		if(null == user){
			return "查无此账号"+loginname;
		}
		if (!StringUtil.isMobileNO(phonenum)) {
			return "输入的手机号码有误！";
		}
		//查询回拨电话配置
		String msg = "";
		DetachedCriteria dc=DetachedCriteria.forClass(CallConfig.class);
		dc.add(Restrictions.eq("flag", 1));
		dc.add(Restrictions.sqlRestriction("1=1 order by rand()"));
		@SuppressWarnings("unchecked")
		List<CallConfig> list = this.proposalService.findByCriteria(dc);
		if(list!=null && list.size()>0 && list.get(0)!=null){
			CallConfig callConfig = list.get(0);
			String phone = user.getPhone();
			if(StringUtils.isNotBlank(phone) && !dfh.utils.StringUtil.isNumeric(phone)){
				try {
					phone = AESUtil.aesDecrypt(phone, AESUtil.KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(StringUtils.isNotBlank(phonenum) && callConfig.getType() == 0){
				phone = phonenum ;
			}
			log.info(loginname+"makeCall-->"+phone);
			if(callConfig.getCompany().equals("国信")){
				msg = TelCrmUtil.sendCustomerCallGx(callConfig.getUrl() , callConfig.getServiceid() , phone);
			}else if(callConfig.getCompany().equals("比邻")){
				msg = TelCrmUtil.sendCustomerCallBl(callConfig.getUrl() , callConfig.getServiceid() , phone);
			}else{
				log.error("不存在"+callConfig.getCompany());
			}
			return msg ;
		}else{
			return "免费热线正在维护";
		}
	}
	
	/**
	 * 注册成功发送欢迎语音
	 * @param loginname
	 */
	public void welcomeVoice(String loginname){
		Users user = (Users) proposalService.get(Users.class, loginname);
		if(null == user || user.getRole().equals(UserRole.AGENT.getCode())){
			log.info(loginname+"---welcome--return");
			return  ;
		}
		try {
			String callMsg = TelCrmUtil.call("welcome_"+user.getLoginname(), AESUtil.aesDecrypt(user.getPhone(), AESUtil.KEY), "" , "19");
			Thread.sleep(1000*60*5);
			String result = "";
			for(int i=0 ; i<15 ;i++){
				result = TelCrmUtil.getResult(user.getLoginname());
				if(result.equals(GxVoiceEnum.SUCCESS.getCode())){ //发送成功
					break;
				}else{
					Thread.sleep(5000);
				}
			}
			user.setWarnremark(user.getWarnremark()+";"+callMsg+":"+result+":"+GxVoiceEnum.getText(result));
			proposalService.update(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Question queryQuestion(String loginname){
		try {
			return proposalService.queryQuestion(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String saveQuetion(String loginname, Integer questionid, String content){
		try {
			return proposalService.saveQuestion(loginname, questionid, content);
		} catch (Exception e) {
			e.printStackTrace();
			return "提交问题失败";
		}
	}
	
	//验证密保问题
	public String questionValidate(String loginname,  Integer questionid ,String answer){
		try {
			Boolean flag = proposalService.questionValidate(loginname, answer, questionid);
			if(flag){
				return "SUCCESS";
			}else{
				//加入提款错误日志
				if(questionsNumber(loginname)>0){
					proposalService.saveOrUpdateQuestionStatus(loginname);
				}else{
					return "bindingPlease";
				}
			}
			return "密保验证答案错误";
		} catch (Exception e) {
			e.printStackTrace();
			return "提款验证失败";
		}
	}
	
	public QuestionStatus queryQuesStatus(String loginname){
		QuestionStatus status = proposalService.queryQuesStatus(loginname);
		if(status.getErrortimes() >= 5){
			proposalService.EnableUser(loginname, false, "system", "支付宝提款密保验证错误5次，冻结");
		}
		return status;
	}
	
	/**
	 * 查询坐席号是哪个产品
	 * @param ext_no
	 * @return
	 */
	public Operator whichProductOfExtNo(String ext_no){
		DetachedCriteria dc=DetachedCriteria.forClass(Operator.class);
		dc.add(Restrictions.eq("phonenoBL", ext_no));
		List<Operator> list = proposalService.findByCriteria(dc);
		if(null != list && list.size()>0 && null != list.get(0)){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 更新维护电话拨打状态
	 * @param cs cs1/cs2/cs3
	 * @param caller  主叫
	 * @param called  被叫
	 * @param begin_time
	 * @param end_time
	 * @param duration
	 * @param record_path
	 * @param call_type
	 * @param trunk
	 * @param uuid
	 * @param hangupcause
	 */
	public String updateCRMPhoneInfo(String cs , String caller, String called, String begin_time, String end_time, String duration,
			String record_path, String call_type, String trunk, String uuid, String hangupcause){
		try {
			log.error("updateCRMPhoneInfo entered,cs:"+cs+",caller:"+caller+",called:"+called+",begin_time:"+begin_time+",end_time:"+end_time+",duration:"+duration+",record_path:"+record_path+",call_type:"+call_type+",trunk:"+trunk+",uuid:"+uuid+",hangupcause:"+hangupcause);
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc.add(Restrictions.eq("phone", AESUtil.aesEncrypt(called.length()==12?called.substring(1, called.length()):called, AESUtil.KEY)));
			List<Users> users = proposalService.findByCriteria(dc);
			Date now = new Date();
			for (Users user : users) {
				
				
				//修改用户电话状态  0 未拨打  1 success_已接通 2 no_answer 未接通 3 call_reject 被挂断 4  invalid_number   空号
				if("success".equals(hangupcause))
				{
					user.setSms(1);
				}else if("no_answer".equals(hangupcause))
				{
					user.setSms(2);
				}else if("call_reject".equals(hangupcause))
				{
					user.setSms(3);
				}
				else if("invalid_number".equals(hangupcause))
				{
					user.setSms(4);
				}
				if(null != user.getWarnremark() && user.getWarnremark().length()>550)
				{
					user.setWarnremark(null);
				}
				user.setWarnremark(user.getWarnremark()==null?("hangupcause:"+hangupcause):(user.getWarnremark()+";hangupcause:"+hangupcause));
				proposalService.update(user);
				UserMaintainLog userMaintainLog = new UserMaintainLog(user.getLoginname(), "hangupcause:" + hangupcause, now, Constants.DEFAULT_OPERATOR, now, Constants.DEFAULT_OPERATOR, 0);
				proposalService.save(userMaintainLog);
				log.info("更新维护电话拨打状态updateCRMPhoneInfo方法执行===="+"电话状态="+hangupcause+"备注:"+user.getWarnremark()+"用户名:"+user.getLoginname());
			}
			CallInfo callInfo = new CallInfo( caller, called, begin_time, end_time, duration, record_path, call_type, trunk, uuid, hangupcause, now);
			proposalService.save(callInfo);
			log.info("callInfo的save方法执行了====="+"谁打的called:"+caller+"电话:"+called);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	
	/**
	 * 更新电销电话拨打状态
	 * @param type a1/b1/c1
	 * @param caller  主叫
	 * @param called  被叫
	 * @param begin_time
	 * @param end_time
	 * @param duration
	 * @param record_path
	 * @param call_type
	 * @param trunk
	 * @param uuid
	 * @param hangupcause
	 */
	public String updateTmPhoneInfo(String type , String caller, String called, String begin_time, String end_time, String duration,
			String record_path, String call_type, String trunk, String uuid, String hangupcause){
		try {
			log.error("updateTmPhoneInfo entered,caller:"+caller+",called:"+called+",begin_time:"+begin_time+",end_time:"+end_time+",duration:"+duration+",record_path:"+record_path+",call_type:"+call_type+",trunk:"+trunk+",uuid:"+uuid+",hangupcause:"+hangupcause);
			called = called.length()==12?called.substring(1, called.length()):called ;
			DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
			dc.add(Restrictions.eq("phone", called));
			dc.add(Restrictions.eq("type", type));
			List<Customer> customers = proposalService.findByCriteria(dc);
			for (Customer customer : customers) {
				if(null != customer.getRemark() && customer.getRemark().length()>80)
				{
					customer.setRemark(null);
				}
				customer.setRemark(customer.getRemark()==null?("hangupcause:"+hangupcause):(customer.getRemark()+";hangupcause:"+hangupcause));
				proposalService.update(customer);
			}
			//user表群呼比邻回调此方法
			DetachedCriteria udc = DetachedCriteria.forClass(Users.class);
			udc.add(Restrictions.eq("phone", AESUtil.aesEncrypt(called,AESUtil.KEY)));
			List<Users> users = proposalService.findByCriteria(udc);
			Date now = new Date();
			for (Users user : users) {
				
				
				//修改用户电话状态  0 未拨打  1 success_已接通 2 no_answer 未接通 3 call_reject 被挂断 4  invalid_number   空号
				if("success".equals(hangupcause))
				{
					user.setSms(1);
				}else if("no_answer".equals(hangupcause))
				{
					user.setSms(2);
				}else if("call_reject".equals(hangupcause))
				{
					user.setSms(3);
				}
				else if("invalid_number".equals(hangupcause))
				{
					user.setSms(4);
				}
				if(null != user.getWarnremark() && user.getWarnremark().length()>550)
				{
					user.setWarnremark(null);
				}
				user.setWarnremark(user.getWarnremark()==null?("hangupcause:"+hangupcause):(user.getWarnremark()+";hangupcause:"+hangupcause));
				proposalService.update(user);
				UserMaintainLog userMaintainLog = new UserMaintainLog(user.getLoginname(), "hangupcause:" + hangupcause, now, Constants.DEFAULT_OPERATOR, now, Constants.DEFAULT_OPERATOR, 0);
				proposalService.save(userMaintainLog);
				log.info("更新电销电话拨打状态updateTmPhoneInfo方法执行===="+"电话状态="+hangupcause+"备注:"+user.getWarnremark()+"用户名:"+user.getLoginname());
			}

			CallInfo callInfo = new CallInfo( caller, called, begin_time, end_time, duration, record_path, call_type, trunk, uuid, hangupcause, now);
			proposalService.save(callInfo);
			log.info("callInfo的save方法执行了====="+"谁打的called:"+caller+"电话:"+called);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	public Question queryQuestionForApp(String loginname){
		try {
			return proposalService.queryQuestionForApp(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer questionsNumber(String loginname){
		return proposalService.questionsNumber(loginname);
	}
	
	
	/**
	 * 获取要发的手机号以及验证码
	 * @param loginname
	 * @return
	 */
	public String getPhoneAndValidateCode(String loginname){
		//TODO
		String code = RandomStringUtils.randomNumeric(4);
		Userstatus userstatus=(Userstatus)proposalService.get(Userstatus.class, loginname);
		if(userstatus ==null){
				userstatus=new Userstatus();
				userstatus.setLoginname(loginname);
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
		return "{\"phone\":\""+phone+"\",\"code\":\""+code+"\"}" ;//13242418105
	}

	/**
	 * AI 通过密码进行账号解锁
	 *
	 * @param loginname
	 * @param accoutName
	 * @param email
	 * @param phone
	 * @return
	 */
	public String unlockAccountByPassword(String loginname, String password) {
		try {

			return aiSupportService.unlockAccountByPassword(loginname, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String unbindQuestion(String loginname, String accountName,String phone,String email,String password) {
		try {
			Question question = proposalService.queryQuestion(loginname);
			if(null==question){
				return "对不起,您没有绑定密保.请先绑定密保问题";
			}
			Users users = proposalService.getUsers(loginname);
			if(StringUtils.isBlank(users.getAccountName())){
				return "请先至帐户设置绑定真实姓名！";
			}

			//验证答案
			Boolean flag = proposalService.questionValidateForQuestion(users, accountName, phone,email,password);
			if(!flag){
				return "个人信息有误,解绑失败!";
			}

			question.setDelflag(1);
			proposalService.update(question);
			return "解绑成功";

		} catch (Exception e) {
			e.printStackTrace();
			return "系统异常";
		}
	}

	/**
	 * AI 通过信息进行账号解锁
	 *
	 * @param loginname
	 * @param accoutName
	 * @param email
	 * @param phone
	 * @return
	 */
	public String unlockAccountByInfo(String loginname, String accoutName, String email, String phone) {
		try {

			return aiSupportService.unlockAccountByInfo(loginname, accoutName, email, phone);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String unBindBankinfo(String loginname, String bankno) {
		return this.userbankinfoService.unBindBankinfo(loginname, bankno);
	}

	public String getForgetAccbySms(String phone, String ip) {
		try {
			return aiSupportService.forgetAccbySms(phone, ip);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getForgetAccbyEmail(String phone, String ip) {
		try {
			return aiSupportService.sendForgetAccbyEmail(phone, ip);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	
	/**
	 * 检测玩家是否发了验证码且发送正确
	 * @param loginname
	 * @param code
	 * @return
	 */
	public String checkValidateCode(String loginname){
		//TODO
		try {
			Userstatus userstatus=(Userstatus)proposalService.get(Userstatus.class, loginname);
			Users user = (Users)proposalService.get(Users.class, loginname);
			String code = userstatus.getValidateCode();
			String result = TelCrmUtil.getSmsInfo(AESUtil.aesDecrypt(user.getPhone(), AESUtil.KEY));
			if(StringUtils.isNotBlank(result) && !result.toLowerCase().contains("error")){
//				List<SmsInfoVo> smss = (List<SmsInfoVo>) JSONObject.toBean(JSONObject.fromObject(result));
				JSONArray array = JSONArray.fromObject(result);
				List<SmsInfoVo> smss = array.toList(array,SmsInfoVo.class);
				log.info(smss.size());
				if(null != smss && smss.size()>0){
					SmsInfoVoComparator comparator = new SmsInfoVoComparator();
					Collections.sort(smss , comparator);
					SmsInfoVo smsInfo = smss.get(0) ;
					log.info("smsInfo-->"+smsInfo.getPhone()+"  "+smsInfo.getPort()+"  "+smsInfo.getContext()+"  "+smsInfo.getDate());
					if(smsInfo.getContext().contains(code) && (new Date().getTime() - DateUtil.parseDateForStandard(smsInfo.getDate()).getTime())<30*60*1000){
						userstatus.setValidateCode(null);
						userstatus.setSmsflag("1");//持久化验证通过标记到数据库
						proposalService.update(userstatus);
						return "success";
					}else{
						return "error:短信超时或验证码不正确";
					}
				}else{
					return "error:no data" ;
				}
			}else{
				return "error:未收到短信,请稍后或者重新发送";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "checkValidateCode error:"+e.getMessage();
		}
	}
	
	public List<TaskList> getAllTasks(){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskList.class);
		dc.add(Restrictions.eq("disable", 0));
		return proposalService.findByCriteria(dc);
	}

	//记录玩家领取的任务(每天只能领取一个任务)
	public String saveTask(String loginname , Integer taskId){
		return SynchronizedUtil.getInstance().saveUserTask(proposalService, loginname, taskId);
	}
	
	//查询玩家的任务记录
	public Page queryTaskRecord(String loginname , Integer pageIndex , Integer size){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskUserRecord.class);
		dc.add(Restrictions.eq("username", loginname)) ;
//		dc = dc.addOrder(Order.desc("createtime"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o) ;
		List<TaskUserRecord> list = page.getPageContents() ;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (TaskUserRecord p : list) {
			p.setTmpCreatetime(sf.format(p.getCreatetime()));
		}
		return page ;
	}
	
	//获取玩家的任务累计金额
	public TaskAmount getTaskAmount(String loginname){
		TaskAmount taskAmount = (TaskAmount) proposalService.get(TaskAmount.class, loginname);
		if(null == taskAmount){
			taskAmount = new TaskAmount();
			taskAmount.setLoginname(loginname);
			taskAmount.setAmount(0.0);
			taskAmount.setCreatetime(new Date());
			proposalService.save(taskAmount);
		}
		return taskAmount ; 
	}
	
	//更新玩家任务进度且把额度添加
	public String updateUserTask(String loginname){
		return SynchronizedUtil.getInstance().updateyaoYaoTask(proposalService, loginname);
	}
	
	//领取金额到主账户
	public String receiveTaskAmount(String loginname  , Double remeit){
		if(remeit < 1.0){
			return "友情提示：任务累计金额至少为1元才可以转入主账户";
		}
		Users user = (Users) proposalService.get(Users.class, loginname);
		if(null == user){
			return "玩家不存在";
		}
		if(!user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			return "该活动只对真钱玩家开放";
		}
		return SynchronizedUtil.getInstance().yaoyaoLeTransfer(proposalService, user, remeit);
	}
	
	//1.查询玩家绑定的支付宝存款账号
	public AlipayAccount getAlipayAccount(String loginname  , Integer disable){
		return proposalService.getAlipayAccount(loginname, disable);
	}
	
	//2.保存支付宝账号
	public String saveAlipayAccount(String loginname , String password , String account){
		account = account.trim();
		try {
			Users user = (Users) proposalService.get(Users.class, loginname);
			if(null == user || user.getRole().equals(UserRole.AGENT.getCode())){
				return  "账号不存在";
			}
			if (!user.getPassword().equals(EncryptionUtil.encryptPassword(password))){
				return "您的密码输入有误";
			}
			AlipayAccount alipayAccount = getAlipayAccount(loginname , null);
			try {
				if(null != alipayAccount){
					if(alipayAccount.getDisable() == 1){
						return "您的支付宝存款账号异常，请联系客服";
					}
					alipayAccount.setAlipayAccount(account);
					alipayAccount.setUpdatetime(new Date());
					proposalService.update(alipayAccount);
				}else{
					alipayAccount = new AlipayAccount();
					alipayAccount.setAlipayAccount(account);
					alipayAccount.setLoginname(loginname);
					alipayAccount.setDisable(0);
					alipayAccount.setCreatetime(new Date());
					proposalService.save(alipayAccount);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "您要绑定的支付宝账号可能已被占用，请联系客服" ;
			}
			return "更新成功" ;
		} catch (Exception e) {
			e.printStackTrace();
			return "更新失败："+e.getMessage();
		}
	}

	public String updateUserStatusSmsFlagForApp(String loginname,String code){
		//TODO
		try {
			Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
			userstatus.setValidateCode(code);
			userstatus.setSmsflag("1");// 持久化验证通过标记
			proposalService.update(userstatus);
			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			return "updateUserStatusSmsFlag error:" + e.getMessage();
		}
	}
	
	public CustomerService getCs() {
		return cs;
	}

	public void setCs(CustomerService cs) {
		this.cs = cs;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public AiSupportService getAiSupportService() {
		return aiSupportService;
	}

	public void setAiSupportService(AiSupportService aiSupportService) {
		this.aiSupportService = aiSupportService;
	}
}