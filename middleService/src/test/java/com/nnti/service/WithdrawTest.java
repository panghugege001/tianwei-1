//package com.nnti.service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang.RandomStringUtils;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.alibaba.fastjson.JSONObject;
//import com.nnti.base.BaseTest;
//import com.nnti.common.constants.OperationLogType;
//import com.nnti.common.constants.ProposalFlagType;
//import com.nnti.common.constants.ProposalType;
//import com.nnti.common.exception.BusinessException;
//import com.nnti.common.extend.zookeeper.GenerateNodePath;
//import com.nnti.common.model.vo.OperationLog;
//import com.nnti.common.model.vo.Question;
//import com.nnti.common.model.vo.QuestionStatus;
//import com.nnti.common.model.vo.User;
//import com.nnti.common.model.vo.UserBankInfo;
//import com.nnti.common.service.interfaces.IAgprofitService;
//import com.nnti.common.service.interfaces.IOperatorLogService;
//import com.nnti.common.service.interfaces.IProposalService;
//import com.nnti.common.service.interfaces.IQuestionService;
//import com.nnti.common.service.interfaces.IQuestionStatusService;
//import com.nnti.common.service.interfaces.IUserBankInfoService;
//import com.nnti.common.utils.AESUtil;
//import com.nnti.common.utils.DateUtil;
//import com.nnti.withdraw.model.dto.WithdrawDTO;
//import com.nnti.withdraw.model.vo.FPayorder;
//import com.nnti.withdraw.service.interfaces.IFPayorderService;
//import com.nnti.withdraw.service.interfaces.IWithdrawService;
//
//public class WithdrawTest extends BaseTest {
//	@Autowired
//	private IProposalService proposalService;
//	@Autowired
//	private IAgprofitService agprofitService;
//	@Autowired
//	private IUserBankInfoService userBankInfoService;
//	@Autowired
//	private IQuestionService questionService;
//	@Autowired
//	private IQuestionStatusService questionStatusService;
//	@Autowired
//	private IOperatorLogService operatorLogService;
//	@Autowired
//	private IWithdrawService withdrawService;
//	@Autowired
//	private IFPayorderService fPayorderService;
//	
//	@Test
//	public void test1() {
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		params1.put("loginname", "woodytest");
//		params1.put("startTime", null);
//		Double deposit = proposalService.getDepositAmount(params1);
//		System.out.println(deposit);
//		Double withdraw = proposalService.getWithdrawAmount(params1);
//		System.out.println(withdraw);
//	}
//	
//	@Test
//	public void test2() {
//		Map<String, Object> params2 = new HashMap<String, Object>();
//		params2.put("loginname", "woodytest");
//		params2.put("pstatus", ProposalFlagType.EXCUTED.getCode());
//		params2.put("amount", 0);
//		params2.put("depositcode", 502);
//		params2.put("withdrawalcode", 503);
//		params2.put("startTime", null);
//		Double youhui = proposalService.getYouHuiAmount(params2);
//		System.out.println(youhui);
//		Double gameProfit = agprofitService.getPlayerProfit(params2);
//		System.out.println(gameProfit);
//		
//	}
//	
//	@Test
//	public void test3(){
//		Map<String, Object> params3 = new HashMap<String, Object>();
//		params3.put("loginname", "woodytest");
//		params3.put("flag", 0);
//		params3.put("bankname", "中国银行");
//		List<UserBankInfo>  userBanks = userBankInfoService.findUserBankList(params3);
//		System.out.println(userBanks.size());
//	}
//	
//	@Test
//	public void test4(){
//		Map<String, Object> params4 = new HashMap<String, Object>();
//		params4.put("loginname", "woodytest");
//		params4.put("questionid", 1);
//		params4.put("delflag", 0);
//		List<Question> ques = questionService.getPlayerQuestion(params4);
//		System.out.println(ques.get(0).getContent());
//	}
//	
//	@Test
//	public void test5(){//待测
////		QuestionStatus status = questionStatusService.get("woodytest");
////		System.out.println(status);
//		QuestionStatus status = new QuestionStatus();
//		status.setLoginname("devtest999");
//		status.setErrortimes(1);
//		status.setUpdatetime(new Date());
////		status.setCreatetime(new Date());
//		int a = questionStatusService.update(status);
////		int a = questionStatusService.save(status);
//		System.out.println(a);
//	}
//	
//	@Test
//	public void test6(){
//		User user = new User();
//		user.setLoginName("devtest999");
//		Boolean flag = proposalService.existNotAuditedProposal(user, ProposalType.CASHOUT);
//		System.out.println(flag);
//	}
//	
//	@Test
//	public void test7(){
//		OperationLog log = new OperationLog("system", OperationLogType.ENABLE.getCode(), DateUtil.getCurrentTimestamp(), "禁用会员密保错误5次");
//		int aa = operatorLogService.insert(log);
//		System.out.println(aa);
//	}
//	
//	@Test
//	public void test8(){
//		WithdrawDTO dto = new WithdrawDTO();
//		dto.setLoginName("devtest999");
//		dto.setAccountNo("88888888999");
//		dto.setAnswer("刘德华");
//		dto.setBankName("深圳银行");
//		dto.setIp("127.0.0.1");
//		dto.setMoney(100.00);
//		dto.setPassword("admin_admin");
//		dto.setQuestionid(1);
//		dto.setTkType(null);
//		String msg = withdrawService.addCashOut(dto);
//		System.out.println(msg);
//	}
//	
//	@Test
//	public void test9() throws BusinessException{
//		WithdrawDTO dto = new WithdrawDTO();
//		dto.setLoginName("a_devtest");
//		dto.setAccountNo("88888888999");
////		dto.setAnswer("刘德华");
//		dto.setBankName("工商银行");
//		dto.setIp("127.0.0.1");
//		dto.setMoney(200.00);
//		dto.setPassword("a123a123");
//		dto.setQuestionid(1);
//		dto.setTkType("liveall");
//		String msg = withdrawService.addCashOut(dto);
//		System.out.println(msg);
//		AESUtil.decrypt("mZL9kjNbr+U1bhm+vCyuJteJK8ovEnLp8xi1BdmZ/8cgFHS1bCgzyAyZLyGMTucQ/t/kOs9xwkn/7bK1K527QimJqTQY8lNqYidZesKyZVJsnVwArnnBvf16gTJSSDbfxoRmQcJztUIXiRU6KqhrE6wvQDWqRsJ07g3HyJwvFPX8GVK3Ky2CdcPJT/gs3N+VtUFi+9r7iAYOvM6O72yvE4CgUYWt9nVdychE7145apI=");
//	}
//	
//	@Test
//	public void test10(){
//		String billno = GenerateNodePath.brandPrefix + "_" + DateUtil.fmtyyyyMMddHHmmss(new Date()) + RandomStringUtils.randomAlphanumeric(5).toLowerCase();
//		FPayorder flashorder = new FPayorder();
//		flashorder.setPno(billno);
//		flashorder.setAmout(100.0);
//		flashorder.setBankname("北京");
//		flashorder.setCard_number("11245452121");
//		flashorder.setBillno(billno);
//		flashorder.setLoginname("devtest");
//		flashorder.setAccountName("婉儿");
//		flashorder.setBankAddress("上海");
//		flashorder.setCreateTime(new Date());
//		int a = fPayorderService.create(flashorder);
//		
//	}
//	
//}
