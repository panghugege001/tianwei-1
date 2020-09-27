package com.nnti.withdraw.service.implementations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.common.constants.CreditChangeType;
import com.nnti.common.constants.CreditType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.constants.FlashPayFlagEnum;
import com.nnti.common.constants.OperationLogType;
import com.nnti.common.constants.ProposalFlagType;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.constants.UserRole;
import com.nnti.common.constants.VipLevel;
import com.nnti.common.constants.WarnLevel;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.model.vo.AgentVip;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.BankStatus;
import com.nnti.common.model.vo.Cashout;
import com.nnti.common.model.vo.Const;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.model.vo.OperationLog;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.model.vo.ProposalExtend;
import com.nnti.common.model.vo.Question;
import com.nnti.common.model.vo.QuestionStatus;
import com.nnti.common.model.vo.User;
import com.nnti.common.model.vo.UserBankInfo;
import com.nnti.common.model.vo.UserStatus;
import com.nnti.common.security.EncryptionUtil;
import com.nnti.common.service.interfaces.IAgentVipService;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.service.interfaces.IBankStatusService;
import com.nnti.common.service.interfaces.ICashoutService;
import com.nnti.common.service.interfaces.ICommonService;
import com.nnti.common.service.interfaces.IConstService;
import com.nnti.common.service.interfaces.ICreditLogService;
import com.nnti.common.service.interfaces.IOperatorLogService;
import com.nnti.common.service.interfaces.IProposalExtendService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.service.interfaces.IQuestionService;
import com.nnti.common.service.interfaces.IQuestionStatusService;
import com.nnti.common.service.interfaces.ISequenceService;
import com.nnti.common.service.interfaces.IUserBankInfoService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.service.interfaces.IUserStatusService;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.common.utils.StringUtil;
import com.nnti.pay.model.enums.LevelType;
import com.nnti.pay.model.enums.TaskFlagType;
import com.nnti.pay.model.vo.BankCreditlogs;
import com.nnti.pay.model.vo.Task;
import com.nnti.pay.service.interfaces.IBankCreditlogsService;
import com.nnti.pay.service.interfaces.ITaskService;
import com.nnti.withdraw.model.bean.WithdrawBean;
import com.nnti.withdraw.model.dto.WithdrawDTO;
import com.nnti.withdraw.model.vo.FPayorder;
import com.nnti.withdraw.service.interfaces.IFPayorderService;
import com.nnti.withdraw.util.CHPayUtil;
import com.nnti.withdraw.util.XFTPayUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaseService {
private static Logger log = Logger.getLogger(BaseService.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IProposalService proposalService;
	@Autowired
	private IUserStatusService userStatusService;
	@Autowired
	private IUserBankInfoService userBankInfoService;
	@Autowired
	private  IBankStatusService bankStatusService;
	@Autowired
	private IQuestionService questionService;
	@Autowired
	private IQuestionStatusService questionStatusService;
	@Autowired
	private IOperatorLogService operatorLogService;
	@Autowired
	private IConstService constService;
	@Autowired
	private IFPayorderService fPayorderService;
	@Autowired
	private IBankInfoService bankinfoService;
	@Autowired
	private IBankCreditlogsService bankCreditlogsService;
	@Autowired
	private ICashoutService cashoutService;
	@Autowired
	private ICreditLogService creditLogService;
    @Autowired
    private ITaskService taskService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IProposalExtendService proposalExtendService; 
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IAgentVipService agentVipService;
	
	//static Double splitAmount = 49999.00;   
	static Double splitAmount = 45000.00;    
	
	//基本参数信息验证
	public String paramerValidate(WithdrawDTO dto) throws Exception{
		String loginname = dto.getLoginName();
		Integer questionid=null;
		String answer=null;
		String password = dto.getPassword();
		Double money = dto.getMoney();
		String bankName = dto.getBankName();
		String accountNo = dto.getAccountNo();
		String tkType = dto.getTkType();
		String ip = dto.getIp();
		if(!dto.getProduct().equals("dy")) {
			questionid = dto.getQuestionid();
			answer = dto.getAnswer();
		}
		
		if(StringUtils.isBlank(loginname)){
			return "[提示]玩家账号不能为空！";
		}
		if(StringUtils.isBlank(password)){
			return "[提示]密码不能为空！";
		}
		
		if(money < 100){
			return "[提示]提款金额不能少于100元！";
		}
		
		double eps = 1e-10;  // 精度范围  
		if(!(money-Math.floor(money) < eps)){ 
			return "[提示]存款金额必须为整数！";
		}
		
		if(StringUtils.isBlank(bankName)){
			return "[提示]银行不能为空！";
		}
		if(StringUtils.isBlank(accountNo)){
			return "[提示]卡号不能为空！";
		}
		
		User user = userService.get(loginname);
		if(null == user){
			return "账号不存在";
		}
		
		//验证是否完善基本信息
		if(StringUtils.isEmpty(user.getAccountName())){
			return "请完善个人基本注册信息";
		}
		
		
		//验证银行是否维护
		String bankstatus  = getWithDrawBankStatus(dto);
        if("MAINTENANCE".equals(bankstatus)){
        	return "银行系统维护中,请选择其他银行或稍后再试";
        }	
		
        
		if(!user.getPassword().equalsIgnoreCase(EncryptionUtil.encryptPassword(password))){
			List platList = Arrays.asList(new String[] {"yl"});
			if(!platList.contains(dto.getProduct())){
			    return "密码错误";
			}
		}
		if(user.getFlag().intValue() == 1){
			return "会员已经被禁用";
		}
		if(user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			if(!dto.getProduct().equals("dy")) {
				if (null == questionid || StringUtils.isBlank(answer)) {
					return "密保信息不能为空";
				}
			}
		}else if(user.getRole().equals(UserRole.AGENT.getCode())){
			String [] typeArgs = {"liveall","slotmachine"};
			if(StringUtils.isBlank(tkType) || !StringUtil.isContain(tkType, typeArgs)){
				return "代理提款类型不合法" ;
			}

			if(user.getWarnFlag() == WarnLevel.WEIXIAN.getCode()){
				return "请您联系代理专员，谢谢！" ;
			}
		}
		//代理提款限制
		if(StringUtils.isNotBlank(tkType) && tkType.equals("liveall") && user.getRole().equals("AGENT")){
			int date = Calendar.getInstance().get(Calendar.DATE);
			Map<String, Object> params = new HashMap<String ,Object>();
			params.put("agent", user.getLoginName());
			List<AgentVip> vips = agentVipService.findAgentVipList(params);
			if(!(date>=1 && date<=5)){
				if(null == vips || vips.size()==0){
					return  "因代理等级不够不能提款，请多多努力";
				}
			}
			if(!(date>=1 && date<=5) && vips.get(0).getLevel()==0){
				return "其他类佣金提款时间：每月1---5 号";
			}
		}
		
		
		return null ;
	}
	
	//验证银行维护
	public String getWithDrawBankStatus(WithdrawDTO dto) throws Exception{
		BankStatus bankstatus = new BankStatus();
		bankstatus.setBankName(dto.getBankName());
		String bankStatus = bankStatusService.getWithDrawBankStatus(bankstatus);
		return bankStatus;	
	}
	
	
	
	//验证银行卡信息
	public Object validateBankInfo(WithdrawDTO dto) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", dto.getLoginName());
		params.put("flag", 0);
		params.put("bankname", dto.getBankName());
		List<UserBankInfo> banks = userBankInfoService.findUserBankList(params);
		if(null == banks || banks.size()==0 || null == banks.get(0)){
			return "该银行信息有误";
		}
		return banks.get(0);
	}
	
	//真钱玩家验证密保
	public ErrorCode validateQuestion_bak(User user , Integer questionid,  String answer,String product){
		try {
			if(user.getRole().equals(UserRole.AGENT.getCode())){
				return ErrorCode.SC_10000;
			}
			Map<String , Object> params1 = new HashMap<String , Object>();
			params1.put("loginname", user.getLoginName());
			params1.put("questionid", questionid);
			params1.put("delflag", 0);
			List<Question> questions = questionService.getPlayerQuestion(params1);
			QuestionStatus status = questionStatusService.get(user.getLoginName());
			if(null != questions && questions.size()>0 && null != questions.get(0)){
				Question qu = questions.get(0);
				if(StringUtils.equals(answer, qu.getContent())){
					if(null != status){
						status.setErrortimes(0);
						status.setUpdatetime(new Date());
						questionStatusService.update(status);
					}
					return ErrorCode.SC_10000;
				}
			}
			else{
				log.info("提款用户名:"+user.getLoginName());
				log.info("产品名称:"+product);
				if(product.equals("yl")){
					
					return ErrorCode.SC_40000_103;
				}
			}
			if(null == status){
				status = new QuestionStatus();
				status.setLoginname(user.getLoginName());
				status.setErrortimes(1);
				status.setCreatetime(new Date());
				status.setUpdatetime(new Date());
				questionStatusService.save(status);
			}else if(null != status && null != status.getUpdatetime()){
				status.setErrortimes(status.getErrortimes()+1);
				status.setUpdatetime(new Date());
				questionStatusService.update(status);
			}
			if(status.getErrortimes() >= 5){
				User userUpdate = new User();
				userUpdate.setLoginName(user.getLoginName());
				userUpdate.setFlag(1);//禁用
				userUpdate.setRemark("提款密保验证错误5次，冻结");
				userService.update(userUpdate);
				operatorLogService.insert(new OperationLog("system", OperationLogType.ENABLE.getCode(), DateUtil.getCurrentTimestamp(), "禁用会员"+user.getLoginName()+"密保错误5次"));
				return ErrorCode.SC_40000_101;
			}
			return ErrorCode.SC_40000_102;
		} catch (Exception e) {
			e.printStackTrace();
			return ErrorCode.SC_40000_104;
		}
	}
	
	
	
	//真钱玩家验证密保
	public ErrorCode validateQuestion(User user , Integer questionid,  String answer , String product){
		try {
			if(user.getRole().equals(UserRole.AGENT.getCode())){
				return ErrorCode.SC_10000;
			}
			Map<String , Object> params1 = new HashMap<String , Object>();
			params1.put("loginname", user.getLoginName());
			params1.put("questionid", questionid);
			params1.put("delflag", 0);
			
			Map<String , Object> params2 = new HashMap<String , Object>();
			params2.put("loginname", user.getLoginName());
			params2.put("delflag", 0);
			List<Question> questions2 = questionService.getPlayerQuestion(params2);
			if(questions2.size()==0){
				if(product.equals("yl")){
					return ErrorCode.SC_40000_103;
				}
				else{
					return ErrorCode.SC_40000_116;
				} 
				
			}
			
			
			List<Question> questions = questionService.getPlayerQuestion(params1);
			
			if(null != questions && questions.size()>0 && null != questions.get(0)){
				Question qu = questions.get(0);
				QuestionStatus status = questionStatusService.get(user.getLoginName());
				if(StringUtils.equals(answer, qu.getContent())){
					if(null != status){
						status.setErrortimes(0);
						status.setUpdatetime(new Date());
						questionStatusService.update(status);
					}
					return ErrorCode.SC_10000;
				}else{
					if(null == status){
						status = new QuestionStatus();
						status.setLoginname(user.getLoginName());
						status.setErrortimes(1);
						status.setCreatetime(new Date());
						questionStatusService.save(status);
					}else if(null != status && null != status.getUpdatetime() && (new Date().getTime() - status.getUpdatetime().getTime()) < 1000*60*10){
						status.setErrortimes(status.getErrortimes()+1);
						status.setUpdatetime(new Date());
						questionStatusService.update(status);
					}
					if(status.getErrortimes() >= 5){
						User userUpdate = new User();
						userUpdate.setLoginName(user.getLoginName());
						userUpdate.setFlag(1);//禁用
						userUpdate.setRemark("提款密保验证错误5次，冻结");
						userService.update(userUpdate);
						operatorLogService.insert(new OperationLog("system", OperationLogType.ENABLE.getCode(), DateUtil.getCurrentTimestamp(), "禁用会员"+user.getLoginName()+"密保错误5次"));
						return ErrorCode.SC_40000_101;
					}
					return ErrorCode.SC_40000_102;
				}
			}else{
                	return ErrorCode.SC_40000_112;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ErrorCode.SC_40000_104;
		}
	}
	
	

	
	
	
	//是否秒提以及原因
	public Map<String , Object> isSecondWithdraw(WithdrawDTO dto , User user)  throws Exception{
		Map<String , Object> result = new HashMap<String , Object>();
		//秒提是否开启
		Boolean msflag = true;
		String msg = "";
		Const constS = constService.get("信付通代付");
		Boolean mfb = (constS != null && constS.getValue().equals("1")) ? true : false ;
		if(constS != null && constS.getValue().equals("0")){
			msflag = false ;
			msg += "信付通代付关闭";
		}
		if(constS == null){
			msflag = false ;
			msg += "信付通代付关闭";
		}
		
		if(0==user.getLevel() || (1==user.getLevel() && 2 == user.getWarnFlag())){
			msflag = false ;
			msg = VipLevel.getClassmap().get(dto.getProduct()).get(user.getLevel().toString()) + WarnLevel.getText(user.getWarnFlag());
		}
		if(user.getRole().equals(UserRole.AGENT.getCode())){
			msflag = false ;
			msg = "代理提款!";
		}
		/*List bankList = Arrays.asList(new Object[]{"农业银行","深圳发展银行","北京银行","中国银行","兴业银行","光大银行","建设银行","交通银行","平安银行","中信银行","民生银行","工商银行","招商银行"});
		if(!bankList.contains(dto.getBankName())){
			msflag = false ;
			msg = "非畅汇代付银行";
		}*/
		/*if("民生银行".equals(dto.getBankName()) || "上海浦东银行".equals(dto.getBankName()) || "深圳发展银行".equals(dto.getBankName())){
			msflag = false ;
			msg = "非秒提银行";
		}*/
		int dflimit = 5000;
		if(dto.getMoney()>dflimit){
			msflag = false ;
			msg = dflimit+"及以上只能5分钟；";
		}
		if(user.getLevel() >=Integer.parseInt(VipLevel.getClassmap().get("levelCode").get("code")) && user.getWarnFlag().equals(WarnLevel.WEIXIAN.getCode()) && dto.getMoney() > splitAmount){
			msflag = false ;
			msg = VipLevel.getClassmap().get(dto.getProduct()).get(user.getLevel().toString()) + WarnLevel.getText(user.getWarnFlag()) + "提款" + dto.getMoney();
		}
		if(user.getLevel() < Integer.parseInt(VipLevel.getClassmap().get("levelCode").get("code")) && dto.getMoney() > splitAmount){
			msflag = false ;
			msg = VipLevel.getClassmap().get(dto.getProduct()).get(user.getLevel().toString()) + "提款" + dto.getMoney();    
		}
		log.info("产品名称："+dto.getProduct()+"|玩家等级："+user.getLevel()+"|等级名称："+VipLevel.getClassmap().get(dto.getProduct()).get(user.getLevel().toString())+"|提款等级要求："+ Integer.parseInt(VipLevel.getClassmap().get("levelCode").get("code")) );
		
		String depositType = "";
		//累计存款2000以下 不能使用秒提 只能5分钟提款）
	    depositType = this.sumDeposit(dto, user);
	    if(depositType.equals("A")){
			msflag = false ;
			msg += ";总存款小于2000，不能使用秒提;";
	    }
		
		UserStatus userstatus = userStatusService.get(dto.getLoginName());
		if(userstatus != null && userstatus.getTouZhuFlag() == 1){
			msflag = false ;
			msg += "财务控制特殊玩家";
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flag", ProposalFlagType.EXCUTED.getCode());
		params.put("type", ProposalType.SELF_503.getCode());
		params.put("loginname", dto.getLoginName());
		List<Proposal> oldWithdrawSize = proposalService.findProposalList(params);
		int queryType = -1 ;
		if(null == oldWithdrawSize || oldWithdrawSize.size() == 0){
			msflag = false;
			msg += "首提";
			queryType = this.statisticsProposal(dto.getLoginName(), dto.getMoney());
		}
		if(dto.getMoney() >= 10000.0){
			queryType = 4 ;
		}
		
		Boolean originalFlag = msflag;
		if(user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("loginname", dto.getLoginName());
			params1.put("startTime", null);
			Double depAmount = proposalService.getDepositAmount(params1);//存款额度
			Double withdrawl = proposalService.getWithdrawAmount(params1);//提款额度
			depAmount = depAmount == null ?0.0 : depAmount ;
			withdrawl = withdrawl == null ?0.0 : withdrawl ;
			log.info(dto.getLoginName()+"-->depAmount:"+depAmount+"withdrawl:"+withdrawl);
			Double chae = withdrawl - depAmount  ; //存提差
			if(chae < 0){
				if(dto.getMoney() + chae >= 0){
					msflag = false;
				}/*else{
					if(!(msg.contains("非畅汇代付银行") || (userstatus!=null && userstatus.getTouZhuFlag()==1) || dto.getMoney()>dflimit)){
						if(mfb && !depositType.equals("A")){
							msflag = true;	
						}
					}
				}*/
				msg +="存提差"+chae+";";
			}else{
				//查询累计存提款差额
				Map<String, Object> params2 = new HashMap<String, Object>();
				params2.put("loginname", dto.getLoginName());
				params2.put("pstatus", ProposalFlagType.EXCUTED.getCode());
				params2.put("amount", 0);
				params2.put("depositcode", 502);
				params2.put("withdrawalcode", 503);
				params2.put("startTime", null);
				params2.put("loginName", dto.getLoginName());
				Double youhuiAmount = proposalService.getYouHuiAmount(params2) ; //优惠额度
				Double gameProfit = commonService.sumProfitAmount(params2) ; //游戏输赢
				
				params2.put("startTime", DateUtil.getTodayByZeroHour());
				
				Double platformDataSum = commonService.sumPlatformBet(params2);
				if(null == gameProfit){
					msflag = false ; //改为5分钟
					msg += "没有游戏输赢记录";
					gameProfit = 0.0;
				}
				if(null != platformDataSum){
					gameProfit += platformDataSum;
				}
				
				youhuiAmount = youhuiAmount == null ? 0.0 : youhuiAmount ;
				gameProfit = -1*(gameProfit == null ? 0.0 : gameProfit) ;
				if(dto.getMoney() + chae >=  + youhuiAmount	+ gameProfit){
					msflag = false;
				}
				msg += "存提差"+chae+";优惠"+youhuiAmount+"平台输赢"+gameProfit+"";
			}
			msg += originalFlag ? "‘本秒’" : "‘本5’";
		}
		result.put("msflag", msflag);
		result.put("msg", msg);
		result.put("queryType", queryType);
		result.put("depositType", depositType);
		return result ;
	}

	//查询玩家累计总存款
	public String  sumDeposit(WithdrawDTO dto,User user) throws Exception{
		
		    //查询累计总存款
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loginName", user.getLoginName());
			params.put("startTime", "2008-01-01 00:00:01");
			params.put("endTime",new Date());
			Double deposit = commonService.getDeposit(params);
			
			String  deposit_type = "";
			
			//总存款区间0-2000
			if(deposit >=0 && deposit<=2000){
				deposit_type = "A";
			}
			else if(deposit>2000 && deposit <=5000){
				deposit_type = "B";
			}
           else if(deposit>5000 && deposit <=10000){
        		deposit_type = "C";
			}
           else if(deposit>10000 && deposit <=20000){
        	   deposit_type = "D";
			}
           else if(deposit>20000 && deposit <=50000){
        	   deposit_type = "E";
			}
           else if(deposit>50000 && deposit <=100000){
        	   deposit_type = "F";
			}
           else if(deposit>100000){
	           deposit_type = "G";
			}
			
			log.info("玩家：" + user.getLoginName() + "在存款开始时间：" + DateUtil.parse(DateUtil.YYYY_MM_DD_HH_MM_SS, "2008-01-01 00:00:01") 
			 + "和结束时间：" + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date()) + "段内存款额为：" + deposit+"存款类型为DepositType："+deposit_type);
			
		return deposit_type; 
	}
	
	public String dealWithdrawProposalFluence(User user , Map<String , Object> userMfbInfo , UserBankInfo userBank , WithdrawDTO dto )throws Exception{
		String remark = (String) userMfbInfo.get("msg");
		Boolean msflag = (Boolean) userMfbInfo.get("msflag");
		String depositType = (String) userMfbInfo.get("depositType");
		log.info("userMfbInfo值："+userMfbInfo.toString());
		
		Integer isBigAmount = 0 ;
		Double money = dto.getMoney();
		if(dto.getMoney() > splitAmount){
			isBigAmount = 1 ;
		}
		
		Double surplus = money % splitAmount ;
		int times = new Double((money / splitAmount)).intValue() ;
		List<Double> amounts = sqlitMoney(money,surplus,times);
		
		String markStr = remark + ";总共:"+money+"元分成"+((surplus == 0)?times:(times+1));
		userMfbInfo.put("msg", markStr);
		log.info(user.getLoginName()+"要提款的总数是："+money+"分成"+((surplus == 0)?times:(times+1)));
		
		Object doInfo = null ;
		try {
			doInfo = this.dealWithdrawProposalFluence(user, userMfbInfo, userBank, dto,splitAmount,amounts,isBigAmount);
			
		} catch (Exception e) {
			log.error(user.getLoginName()+"提款，保存提案，cashout，fpayorder返回结果");
			e.printStackTrace();
			doInfo = ErrorCode.SC_40000_107;
		}
		if(doInfo instanceof ErrorCode){
			log.error(user.getLoginName()+"处理提款提案失败  "+((ErrorCode)doInfo).getType());
			return ((ErrorCode) doInfo).getType();
		}else{
			if(doInfo instanceof List){
				//调取接口进行秒提
				if(msflag){
					log.info("畅汇处理秒提......................."+user.getLoginName()+"---doinfo："+doInfo);
					
					List<WithdrawBean> beans = (List<WithdrawBean>) doInfo;
					
					try {
						for (WithdrawBean bean : beans) {
							String orderRemark = "";
							
							log.info(user.getLoginName()+"提款，保存提案，cashout，fpayorder返回结果，提案号"+bean.getOrderNo()+",金额："+bean.getAmount());
							
							Proposal proposal = proposalService.get(bean.getOrderNo());
							FPayorder flashorder = fPayorderService.get(bean.getOrderNo());
							
							Proposal proposalUpdate = new Proposal();
							proposalUpdate.setPno(bean.getOrderNo());
							
							FPayorder fPayorderUpdate = new FPayorder();
							fPayorderUpdate.setPno(bean.getOrderNo());
							
							String addOrderInfo = "" ;
							if(null != proposal && null != flashorder){
								Boolean isException = false ;
								try {
									if(userBank.getBankname().contains("邮政")){
										userBank.setBankname("邮政储蓄银行");
									}
									log.info("--->产品名称："+dto.getProduct()+"  提款人："+user.getLoginName()+"  提款银行："+userBank.getBankname());
									String batchdate = DateUtil.format(DateUtil.YYYYMMDD, flashorder.getCreateTime());
									//信付通代付
									addOrderInfo = XFTPayUtil.api_add_order(flashorder.getBillno(), bean.getAmount().toString() , userBank.getBankname(), "广东", user.getAccountName(), userBank.getBankno(),dto.getProduct(),depositType,batchdate);	
									//畅汇代付	
									//addOrderInfo = CHPayUtil.api_add_order(flashorder.getBillno(), bean.getAmount().toString() , userBank.getBankname(), "广东", user.getAccountName(), userBank.getBankno(),dto.getProduct(),depositType);	
									//json = JSONObject.parseObject(addOrderInfo);
									//log.info(addOrderInfo+"转换成json："+json);
									
								} catch (Exception e) {
									e.printStackTrace();
									orderRemark = e.getMessage();
									isException = true ;
								}
								
								if(isException){  
									log.info("接口调用异常处理："+flashorder.getBillno()+"|"+dto.getLoginName());
									proposalUpdate.setRemark("调用信付通代付异常：" + orderRemark);
									fPayorderUpdate.setFlag(FlashPayFlagEnum.FAILED1.getCode());
								}else{
									if("S0001".equals(addOrderInfo)){
										fPayorderUpdate.setFlag(FlashPayFlagEnum.COMMIT.getCode());
										proposalUpdate.setRemark(proposal.getRemark() + ";已提交到信付通");
									}else{
										fPayorderUpdate.setFlag(FlashPayFlagEnum.FAILED1.getCode());
										proposalUpdate.setRemark(proposal.getRemark() + ";提交信付通失败,"+addOrderInfo);
									}
								}
								
								proposalService.update(proposalUpdate);
							}else{
								orderRemark = "提款提案创建异常，请联系开发查看!";
							}
							fPayorderUpdate.setRemark(orderRemark);
							fPayorderService.update(fPayorderUpdate);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return ErrorCode.SC_10000.getType();
				}else{
					return ErrorCode.SC_10000.getType();
				}
			}
		}
		
		
		return ErrorCode.SC_40000_109.getType();
	}
	
	


	public  int statisticsProposal(String loginname , Double money) throws Exception  {
		//		a. 首次提款有申请过首存优惠/限时优惠/或者次存优惠
		//		b. 首次提款1000以上
		//		c. 首次提款有申请过体验金/或者下载app礼金 ，且提款金额300以上
		//		d.提款大于10000
		
		if(money > 1000){
			return 2 ;
		}
		
		//优惠
		List<Integer> typeListSelfDeposit = new ArrayList<Integer>();
		typeListSelfDeposit.add(ProposalType.SELF_590.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_591.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_705.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_707.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_708.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_709.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_710.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_711.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_712.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_730.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_731.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_732.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_733.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_734.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_735.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_598.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_599.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_706.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_740.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_741.getCode());
		typeListSelfDeposit.add(ProposalType.SELF_742.getCode());
		//体验金
		List<Integer> experienceGift = new ArrayList<Integer>();
		experienceGift.add(ProposalType.SELF_701.getCode());
		experienceGift.add(ProposalType.SELF_512.getCode());
		
		Map<String , Object> maps = new HashMap<String , Object>();
		maps.put("loginname", loginname);
		maps.put("flag", ProposalFlagType.EXCUTED.getCode());
		//List<Proposal> pros = proposalDao.findProposalList(maps);
		List<Proposal> pros = proposalService.findProposalList(maps);
		if(null != pros && pros.size()>0){
			for (Proposal proposal : pros) {
				if(typeListSelfDeposit.contains(proposal.getType())){
					return 1 ;
				}
				if(experienceGift.contains(proposal.getType()) && money > 300){
					return 3 ;
				}
			}
		}
		return -1;
	}
	
	public Object dealWithdrawProposalFluence(User user, Map<String, Object> userMfbInfo, UserBankInfo userBank,
			WithdrawDTO dto , Double splitAmount , List<Double> amounts , Integer isBigAmount) throws Exception{
		Double money = dto.getMoney();
		String remark = (String) userMfbInfo.get("msg");
		Boolean msflag = (Boolean) userMfbInfo.get("msflag");
		int queryType = (int) userMfbInfo.get("queryType");
		String depositType = (String) userMfbInfo.get("depositType");
		
		log.info("dealWithdrawProposalFluence接受参数："+remark+"|"+msflag+"|"+queryType+"|"+depositType+"|"+dto.getTkType()+"|"+user.getAccountName());  
		User currUser =  userService.get(user.getLoginName());
		UserStatus currSlot = null;
		
		//该用户已提交过提款提案，尚未审批完
		if (this.existNotAuditedProposal(user, ProposalType.SELF_503)) {
			return ErrorCode.SC_40000_105;
		}
		
		//你之前的秒付提款尚未审核完，暂时不能使用秒付提款
		if (this.existNotMsbankAuditedProposal(user, ProposalType.SELF_503)) {
			return ErrorCode.SC_40000_111;
		}
		
		//定义pno提案号
		String  pno = sequenceService.generateProposalNo(ProposalType.SELF_503.getCode().toString());
		
		log.info(user.getLoginName()+"生成提案号："+pno);
		
		if(currUser.getRole().equals(UserRole.MONEY_CUSTOMER.getCode()) || (null != dto.getTkType() && dto.getTkType().equals("liveall"))){
			//扣掉主账户的钱(玩家提款或者代理真人提款，扣users表)
			if(currUser.getCredit() < money){
				return ErrorCode.SC_40000_106;
			}
			User updateUser = new User();
			updateUser.setLoginName(user.getLoginName());
			updateUser.setCredit(-1 * money);
			userService.update(updateUser);
			
			CreditLog log = new CreditLog();
			log.setLoginName(currUser.getLoginName());
			log.setType(CreditChangeType.CASHOUT);
			log.setCreateTime(new Date());
			log.setCredit(currUser.getCredit());
			log.setNewCredit(currUser.getCredit() - money);
			log.setRemit(-1 * money);
			log.setRemark("referenceNo:"+pno);
			creditLogService.create(log);
			
		}else if(dto.getTkType().equals("slotmachine")){
			currSlot =  userStatusService.get(currUser.getLoginName());
			if(null == currSlot || null == currSlot.getSlotAccount() || currSlot.getSlotAccount() < money){
				return ErrorCode.SC_40000_108;
	        }
			
			UserStatus slot = new UserStatus();
			slot.setLoginName(currUser.getLoginName());
			slot.setSlotAccount(-1 * money);
			userStatusService.update(slot);
			
			CreditLog log = new CreditLog();
			log.setLoginName(currUser.getLoginName());
			log.setType(CreditChangeType.CASHOUT);
			log.setCreateTime(new Date());
			log.setCredit(currSlot.getSlotAccount());
			log.setNewCredit(currSlot.getSlotAccount() - money);
			log.setRemit(-1 * money);
			log.setRemark("referenceNo:"+pno);
			creditLogService.create(log);
		}
		
		/*出款接口模式 ， 到账后再添加cashout记录*/
		List<WithdrawBean> beans = new ArrayList<WithdrawBean>();
		int i= 0;
		
		for (Double amount : amounts) {
           if(amounts !=null && amounts.size()>0 && i !=0){
        		pno = sequenceService.generateProposalNo(ProposalType.SELF_503.getCode().toString());   
           }
           i++;
            
            Double afterLocalCredit = user.getCredit();
			Proposal proposal = new Proposal();
			proposal.setPno(pno);
			proposal.setProposer(currUser.getLoginName());
			proposal.setCreateTime(new Date());
			proposal.setType(ProposalType.SELF_503.getCode());
			proposal.setQuickly(currUser.getLevel());
			proposal.setLoginName(currUser.getLoginName());
			proposal.setAmount(amount);
			proposal.setAgent(currUser.getAgent());
			proposal.setFlag(ProposalFlagType.SUBMITED.getCode());
			proposal.setWhereIsFrom("前台");
			proposal.setRemark(remark);
			proposal.setBankname(dto.getBankName());
			proposal.setMsflag(msflag ? 1:0);
			proposal.setAfterLocalAmount(afterLocalCredit==null?-1.0:afterLocalCredit);
			proposal.setAfterRemoteAmount(0.00);
			if((null != dto.getTkType() && dto.getTkType().equals("slotmachine"))){
				proposal.setSaveway("slotmachine");//提案标识为代理老虎机佣金提款
			}
			proposalService.create(proposal);
			
			log.info(user.getLoginName()+"保存提案操作完成，提案号"+pno);
			
			List<Task> tasks = new ArrayList<>();
			Task audit = new Task(pno, LevelType.AUDIT.getCode(), TaskFlagType.SUBMITED.getCode(), DateUtil.getCurrentTimestamp(), null, currUser.getLoginName());
	        tasks.add(audit);
	        Task excute = new Task(pno, LevelType.EXCUTE.getCode(), TaskFlagType.SUBMITED.getCode(), DateUtil.getCurrentTimestamp(), null, currUser.getLoginName());
	        tasks.add(excute);
	        taskService.createBatch(tasks);
			
			if(-1 != queryType){
				ProposalExtend pExtend = new ProposalExtend();
				pExtend.setPno(pno);
				pExtend.setPlatform("cashout");
				pExtend.setPreferentialId(-1l);
				pExtend.setWithdrawType(queryType);
				pExtend.setCreateTime(new Date());
				proposalExtendService.create(pExtend);
			}
			
			Cashout cashout = new Cashout();
	        cashout.setPno(pno);
	        cashout.setTitle(user.getRole());
	        cashout.setLoginname(user.getLoginName());
	        cashout.setMoney(amount);
	        cashout.setAccountName(user.getAccountName());
	        cashout.setAccountType("借记卡");
	        cashout.setBankAddress("上海");
	        cashout.setAccountNo(userBank.getBankno());
	        cashout.setBank(userBank.getBankname());
	        cashout.setAccountCity("上海");
	        cashoutService.create(cashout);
	        
	        log.info(user.getLoginName()+"保存Cashout操作完成，提案号"+pno);
	        
	        String billno = GenerateNodePath.brandPrefix + "_" + DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date()) + RandomStringUtils.randomAlphanumeric(5).toLowerCase();
	        FPayorder flashorder = new FPayorder();
			flashorder.setPno(pno);
			flashorder.setAmout(amount);
			flashorder.setBankname(userBank.getBankname());
			flashorder.setCard_number(userBank.getBankno());
			flashorder.setBillno(billno);
			flashorder.setLoginname(user.getLoginName());
			flashorder.setAccountName(user.getAccountName());
			flashorder.setBankAddress("上海");
			flashorder.setCreateTime(new Date());
			flashorder.setIp(dto.getIp());
			flashorder.setFlag(FlashPayFlagEnum.WAIT.getCode());
			flashorder.setDepositType(depositType);
			fPayorderService.create(flashorder);
			
			log.info(user.getLoginName()+"保存秒付宝订单记录操作完成，提案号"+pno+",订单号："+billno);
			
			beans.add(new WithdrawBean(amount, pno));
			
			log.info(user.getLoginName()+"返回beans结果，提案号"+pno+",订单号："+billno+",金额："+amount);
		}
		return beans;
	}
   
	
	public boolean existNotMsbankAuditedProposal(User user, ProposalType type) throws Exception{
		if (user.getLoginName() == null || type == null)
			return false;
		
		
		List<Integer> flagList = new ArrayList<Integer>();
		flagList.add(ProposalFlagType.CANCLED.getCode());
		
		List<String> nameList = new ArrayList<String>();
		nameList.add(user.getLoginName());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("passflag", "0");
		params.put("mstype", "1");
		params.put("msflag", "1");
		params.put("loginname",user.getLoginName());
		params.put("flagList", flagList);
		params.put("unknowflag","4");
		int unknowsize  = proposalService.existNotMsbankAuditedProposal(params).size();
		
		List<Integer> flagList2 = new ArrayList<Integer>();
		flagList2.add(ProposalFlagType.EXCUTED.getCode());
		
		
		
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("passflag", "0");
		params2.put("mstype", "2");
		params2.put("msflag", "1");
		params2.put("loginname",user.getLoginName());
		params2.put("flagList", flagList2);
		
		
		int sucsize  = proposalService.existNotMsbankAuditedProposal(params2).size();
		if(unknowsize>0 || sucsize>0){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	public Boolean existNotAuditedProposal(User user, ProposalType type) throws Exception{
		List<Integer> flagList = new ArrayList<Integer>();
		flagList.add(ProposalFlagType.SUBMITED.getCode());
		flagList.add(ProposalFlagType.AUDITED.getCode());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", user.getLoginName());
		params.put("flagList", flagList);
		params.put("type", type.getCode());
		//List<Proposal> proposals = proposalDao.findProposalList(params);
		List<Proposal> proposals = proposalService.findProposalList(params);
		return proposals.size() > 0 ;
	}
	
	
	
	//拆分額度
	public List<Double> sqlitMoney(Double money  , Double surplus , int times){
		List<Double> amounts = new ArrayList<Double>();
		
		if(surplus == 0){
			for(int i =0;i<times;i++){
				amounts.add(splitAmount);
			}
		}else{
			times = times + 1 ;
			for(int i =0;i<times;i++){
				if(i == times-1){
					amounts.add(surplus);
				}else{
					amounts.add(splitAmount);
				}
			}
		}
		return amounts;
	}
	
	//执行用时
	public String getOverTime(Date createTime,Integer mssflag ){
		  Date date =new Date();
		  Date dateTimeCreate =date;
		  String overTime = "0";
	      if ((date.getTime() - 60) >dateTimeCreate.getTime() && mssflag==0){ 
	    	  overTime = "1";
	      }
	      if ((date.getTime() -300) >dateTimeCreate.getTime() && mssflag==1){  
	    	  overTime = "1";
	      }
	      log.info("执行getOverTime方法："+(date.getTime()-60)+"|"+dateTimeCreate.getTime()+"|"+mssflag);
	      log.info("执行getOverTime方法："+(date.getTime()-300)+"|"+dateTimeCreate.getTime()+"|"+mssflag);   
	      return overTime;  
	}

	
	
	//秒付宝提款回调
	public String doNotify(Boolean resultFlag , String billno , String card_number , String card_balance , String fail_message) throws Exception{
		FPayorder fpayOrder = fPayorderService.getByBillno(billno);
		if(null == fpayOrder){
			log.error(billno + "不存在");
			return billno + "不存在";
		}
		
		Proposal proposal = proposalService.get(fpayOrder.getPno());
		if(null == proposal || proposal.getFlag() == 2 || proposal.getFlag() == -1 || fpayOrder.getFlag() == FlashPayFlagEnum.SUCCESS.getCode() || fpayOrder.getFlag() == FlashPayFlagEnum.FAILED2.getCode() ){
			log.info("提款提案已经处理"+billno);
			return "success" ;
		}
		User user = userService.get(proposal.getLoginName());
		
		Proposal proposalFU = new Proposal();
		proposalFU.setPno(fpayOrder.getPno());
		if(resultFlag){
			//付款卡
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("bankcard", card_number.trim());
			List<BankInfo> banks = bankinfoService.findBankInfoList2(map);
			BankInfo bank = null ;
			if(null != banks && banks.size() > 0){
				bank = banks.get(0);
			}else{
				log.error("未配置付款卡"+card_number);
				return "未配置付款卡"+card_number;
			}
			
			Date executeTime = DateUtil.getCurrentTimestamp();
			//提案处理
			proposalFU.setFlag(2);
			proposalFU.setMstype(2);
			proposalFU.setBankaccount(bank.getUserName());
			proposalFU.setExecuteTime(executeTime);
			proposalFU.setTimecha(DateUtil.getTimecha(proposal.getCreateTime(), proposal.getExecuteTime()));
			proposalFU.setOvertime(DateUtil.getOvertime(proposal.getCreateTime(), executeTime,proposal.getMssgflag()));
			
			BankCreditlogs bankCreditlogs = new BankCreditlogs();
            bankCreditlogs.setCreateTime(DateUtil.getCurrentTimestamp());
            bankCreditlogs.setBankName(bank.getUserName());
            bankCreditlogs.setType(CreditType.CASHOUT.getCode());
            bankCreditlogs.setNewCredit(bank.getAmount() - fpayOrder.getAmout());
            bankCreditlogs.setCredit(bank.getAmount());
            bankCreditlogs.setRemit(-1 * fpayOrder.getAmout());
            bankCreditlogs.setRemark("referenceNo:"+fpayOrder.getPno());  
            bankCreditlogsService.add(bankCreditlogs);
            
            
            //采用新方式 (更新银行额度)
	      	Map<String, Object> params = new HashMap<String, Object>();
	  		params.put("id", bank.getId());
	  		params.put("amount", bank.getAmount() - fpayOrder.getAmout());
	  		params.put("bankAmount",Double.valueOf(card_balance));
	  		params.put("updatetime", new Date());
	  		bankinfoService.update2(params);

            FPayorder fpayOrderFU = new  FPayorder();
            fpayOrderFU.setPno(fpayOrder.getPno());
            fpayOrderFU.setUpdateTime(new Date());
            fpayOrderFU.setFlag(FlashPayFlagEnum.SUCCESS.getCode());
            fPayorderService.update(fpayOrderFU);
            
		}else{
			proposalFU.setFlag(-1);
			proposalFU.setMstype(1);
			
			
			/*CreditLog creditLog = new CreditLog();
			creditLog.setLoginName(user.getLoginName());
			creditLog.setType(CreditType.CASHOUT_RETURN.getCode());
			creditLog.setCredit(user.getCredit());
			creditLog.setRemit(fpayOrder.getAmout());
			creditLog.setNewCredit(NumericUtil.add(user.getCredit(), fpayOrder.getAmout()));
			creditLog.setCreateTime(new Date());
			creditLog.setRemark(fpayOrder.getPno()+";"+fail_message);
			creditLogService.create(creditLog);
			
			FPayorder fpayOrderFU = new  FPayorder();
			fpayOrderFU.setPno(fpayOrder.getPno());
			fpayOrderFU.setFlag(FlashPayFlagEnum.FAILED2.getCode());//秒付宝处理失败
			fpayOrderFU.setUpdateTime(new Date());
			fpayOrderFU.setRemark(fail_message);
			fPayorderService.update(fpayOrderFU);*/
			
			//给玩家返钱
			log.info("玩家账号："+user.getLoginName()+"|"+fpayOrder.getAmout()+"|"+proposal.getSaveway());
			if(StringUtils.isNotEmpty(proposal.getSaveway()) && proposal.getSaveway().equals("slotmachine")){
				
				UserStatus userStatus =  userStatusService.get(user.getLoginName());
				
				CreditLog creditLog = new CreditLog();
				creditLog.setLoginName(user.getLoginName());
				creditLog.setType(CreditType.CASHOUT_RETURN.getCode());
				creditLog.setCredit(userStatus.getSlotAccount());
				creditLog.setRemit(fpayOrder.getAmout());
				creditLog.setNewCredit(NumericUtil.add(userStatus.getSlotAccount(), fpayOrder.getAmout()));
				creditLog.setCreateTime(new Date());
				creditLog.setRemark(fpayOrder.getPno()+";"+fail_message);
				creditLogService.create(creditLog);
				
				FPayorder fpayOrderFU = new  FPayorder();
				fpayOrderFU.setPno(fpayOrder.getPno());
				fpayOrderFU.setFlag(FlashPayFlagEnum.FAILED2.getCode());//秒付宝处理失败
				fpayOrderFU.setUpdateTime(new Date());
				fpayOrderFU.setRemark(fail_message);
				fPayorderService.update(fpayOrderFU);
				
				UserStatus slatAccount = new UserStatus();
				slatAccount.setLoginName(user.getLoginName());
				slatAccount.setSlotAccount(fpayOrder.getAmout());
				userStatusService.update(slatAccount);
			}else{
				
				CreditLog creditLog = new CreditLog();
				creditLog.setLoginName(user.getLoginName());
				creditLog.setType(CreditType.CASHOUT_RETURN.getCode());
				creditLog.setCredit(user.getCredit());
				creditLog.setRemit(fpayOrder.getAmout());
				creditLog.setNewCredit(NumericUtil.add(user.getCredit(), fpayOrder.getAmout()));
				creditLog.setCreateTime(new Date());
				creditLog.setRemark(fpayOrder.getPno()+";"+fail_message);
				creditLogService.create(creditLog);
				
				FPayorder fpayOrderFU = new  FPayorder();
				fpayOrderFU.setPno(fpayOrder.getPno());
				fpayOrderFU.setFlag(FlashPayFlagEnum.FAILED2.getCode());//秒付宝处理失败
				fpayOrderFU.setUpdateTime(new Date());
				fpayOrderFU.setRemark(fail_message);
				fPayorderService.update(fpayOrderFU);
				
				
				User userFU = new User();
				userFU.setLoginName(user.getLoginName());
				userFU.setCredit(fpayOrder.getAmout());
				userService.update(userFU);
			}
			//改变执行明细
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pno", fpayOrder.getPno());
			params.put("level", LevelType.AUDIT.getCode());
			Task audit   = taskService.getTask(params).get(0);
			
			if (audit == null) {
				log.info("找不到对应提案任务"+card_number);
				return "找不到对应提案任务 ";
			} else {
				audit.setManager("system");
				audit.setFlag(TaskFlagType.CANCLED.getCode());
				audit.setAgreeTime(DateUtil.getCurrentTimestamp());
				taskService.update(audit);
			}
		}
		log.info("---->保存提案proposal表："+proposalFU.getMstype());//是否成功
		proposalService.update(proposalFU);
		
		return "success" ;
	}
	
	
	
	//审核且秒提
	public String addFlashPay(String order_no,String operator,String remark,String ip,String product) throws Exception{
		Proposal proposal = proposalService.get(order_no);
		FPayorder fPayOrder = fPayorderService.get(order_no);
		log.info(proposal.getFlag()+"  "+fPayOrder==null ? null : fPayOrder.getFlag());
		if(null == fPayOrder || proposal.getFlag() == 2 || proposal.getFlag() == -1 || fPayOrder.getFlag() != FlashPayFlagEnum.WAIT.getCode()){
			log.info("订单已添加...  "+order_no);
			return "订单已添加...  "+order_no;
		}
		Cashout cashout = cashoutService.get(order_no); 
		
		Proposal proposalUP = new Proposal();
		FPayorder fPayOrderUP = new FPayorder();
		proposalUP.setPno(order_no);
		fPayOrderUP.setPno(order_no);
		
		String addOrderInfo = "";
		//JSONObject json = null ;
		Boolean isException = false ;
		try {
			log.info(product+"--->产品订单号："+fPayOrder.getBillno());
			String batchdate = DateUtil.format(DateUtil.YYYYMMDD, fPayOrder.getCreateTime());
			//信付通代付
			addOrderInfo = XFTPayUtil.api_add_order(fPayOrder.getBillno(), fPayOrder.getAmout().toString(), cashout.getBank(), "广东", cashout.getAccountName(), cashout.getAccountNo(),product,fPayOrder.getDepositType(),batchdate);
			//畅汇代付
			//addOrderInfo = CHPayUtil.api_add_order(fPayOrder.getBillno(), fPayOrder.getAmout().toString(), cashout.getBank(), "广东", cashout.getAccountName(), cashout.getAccountNo(),product,fPayOrder.getDepositType());
			//json = JSONObject.parseObject(addOrderInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			addOrderInfo = e.getMessage();
			isException = true ;
		}
		
		if(isException){
			proposalUP.setRemark("调用信付通代付异常：" + addOrderInfo);
			fPayOrderUP.setFlag(FlashPayFlagEnum.FAILED1.getCode());
		}else{
			if("S0001".equals(addOrderInfo)){
				fPayOrderUP.setFlag(FlashPayFlagEnum.COMMIT.getCode());
				proposalUP.setRemark("审核:" + StringUtils.trimToEmpty(remark)+";已提交到信付通");
				proposalUP.setFlag(1);
				proposalUP.setMsflag(1);
				proposalUP.setPassflag(1);
				proposalUP.setMssgflag(1);
			}else{ 
				fPayOrderUP.setFlag(FlashPayFlagEnum.FAILED1.getCode());
				proposalUP.setRemark("审核:" + StringUtils.trimToEmpty(remark)+";提交信付通失败,"+addOrderInfo);
			}
		}
		
		proposalService.update(proposalUP);
		fPayorderService.update(fPayOrderUP);
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pno", order_no);
		params.put("level", LevelType.AUDIT.getCode());
		Task audit   = taskService.getTask(params).get(0);
		
		if (audit == null) {
			return "找不到对应提案任务 ";
		} else {
			audit.setAgreeIp(ip);
			audit.setManager(operator);
			audit.setFlag(TaskFlagType.FINISHED.getCode());
			audit.setAgreeTime(DateUtil.getCurrentTimestamp());
			taskService.update(audit);
		}
		return "请求处理完成" ;
	}
	
	
	
	//重新提交
	public String resubmitFPayOrder(String order_no,String operator,String ip,String product) throws Exception{
		Proposal proposal = proposalService.get(order_no);
		FPayorder fPayOrder = fPayorderService.get(order_no);
		log.info(proposal.getFlag()+"  "+fPayOrder==null ? null : fPayOrder.getFlag());
		if(null == fPayOrder || proposal.getFlag() == 2 || proposal.getFlag() == -1 || fPayOrder.getFlag() != FlashPayFlagEnum.WAIT.getCode()){
			log.info("订单已处理...  "+order_no);
			return "订单已处理...  "+order_no;
		}
		Cashout cashout = cashoutService.get(order_no); 
		
		
		Proposal proposalUP = new Proposal();
		FPayorder fPayOrderUP = new FPayorder();
		proposalUP.setPno(order_no);
		fPayOrderUP.setPno(order_no);
		
		String addOrderInfo = "";
		//JSONObject json = null ;
		Boolean isException = false ;
		try {
			log.info(product+"--->产品订单号："+fPayOrder.getBillno());
			String batchdate = DateUtil.format(DateUtil.YYYYMMDD, fPayOrder.getCreateTime());
			//信付通代付
			addOrderInfo = XFTPayUtil.api_add_order(fPayOrder.getBillno(), fPayOrder.getAmout().toString(), cashout.getBank(), "广东", cashout.getAccountName(), cashout.getAccountNo(),product,fPayOrder.getDepositType(),batchdate);
			//畅汇代付
			//addOrderInfo = CHPayUtil.api_add_order(fPayOrder.getBillno(), fPayOrder.getAmout().toString(), cashout.getBank(), "广东", cashout.getAccountName(), cashout.getAccountNo(),product,fPayOrder.getDepositType());
			//json = JSONObject.parseObject(addOrderInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			addOrderInfo = e.getMessage();
			isException = true ;
		}
		
		if(isException){
			proposalUP.setRemark("调用信付通代付异常：" + addOrderInfo);
			fPayOrderUP.setFlag(FlashPayFlagEnum.FAILED1.getCode());
		}else{
			if("S0001".equals(addOrderInfo)){
				fPayOrderUP.setFlag(FlashPayFlagEnum.COMMIT.getCode());
				proposalUP.setRemark(proposal.getRemark() + ";已提交到信付通");
				proposalUP.setFlag(1);
				proposalUP.setMsflag(1);
				proposalUP.setPassflag(1);
				proposalUP.setMssgflag(1);
			}else{
				fPayOrderUP.setFlag(FlashPayFlagEnum.FAILED1.getCode());
				proposalUP.setRemark(proposal.getRemark() + ";提交信付通失败,"+addOrderInfo);
			}
		}
		
		proposalService.update(proposalUP);
		fPayorderService.update(fPayOrderUP);
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pno", order_no);
		params.put("level", LevelType.AUDIT.getCode());
		Task audit   = taskService.getTask(params).get(0);
		
		if (audit == null) {
			return "找不到对应提案任务 ";
		} else {
			audit.setAgreeIp(ip);
			audit.setManager(operator);
			audit.setFlag(TaskFlagType.FINISHED.getCode());
			audit.setAgreeTime(DateUtil.getCurrentTimestamp());
			taskService.update(audit);
		}
		return "请求处理完成" ;
	}

}
