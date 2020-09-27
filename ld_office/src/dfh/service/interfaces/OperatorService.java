package dfh.service.interfaces;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.dom4j.DocumentException;

import com.nnti.office.model.auth.Operator;

import dfh.mgs.vo.freegame.Offer;
import dfh.model.Autotask;
import dfh.model.enums.AutoGenerateType;
import dfh.model.enums.OperationLogType;
import dfh.model.notdb.Report;

public interface OperatorService extends UniversalService {
	
	boolean isUseReferWebsite(String url);

	String EnableUser(String userName, boolean isEnable, String operator,String remark);

	String login(String loginname, String password, String ip);

	String resetPassword(String loginname, String password, String operator);

	String queryDeposit(Date startTime,Date endTime,String loginname,ProposalService proposalService);

	List queryBet(String username, Date date, Date date1,String game);

	Autotask startAutoTask(AutoGenerateType type, Integer totalRecords, String operator);

	Autotask getLastAutoTask(AutoGenerateType type);

	Autotask refreshAutoTask(Integer taskID, Boolean isSuccess, String remark);

	Autotask stopAutoTask(Integer taskID, String remark);

	Autotask finishAutoTask(Integer taskID, String remark);

	String generateXimaForEach(String loginname, Date startTime, Date endTime, String operator);

	String setLevel(String loginname, Integer level, String operator);

	String changeCreditManual(String loginname, Double amount, String remark, String operator);
	
	String changeQuotal(String billno, String loginname, Double amount, String remark, String operator, String ip);
	
	public String changeQuotalForSlot(String billno, String loginname, Double amount, String remark, String operator, String ip);
	
	String changeBankCreditManual(String loginname, Double amount, String type, String remark, String operator);

	String modifyOwnPassword(String operator, String oldPassword, String newPassword, boolean changeStr);

	String createSubOperator(String newOperator, String email, String password, String operator,String agent,Integer validType,String cellphoneno,String employeeNo);
	
	String createSubOperatorTwo(String newOperator, String email, String password, String operator,String authority,String agent,Integer validType,String cellphoneno,String employeeNo);
	
	String relievePtLimit(String loginname, String username);
	public void insertOperatorSendSMSLog(String operator,String remark);

	/**
	 * @author sun
	 * @param loginname
	 * @param partner
	 * @return
	 */
	String partnerSetlower(String loginname, String partner);

	/**
	 * @author sun
	 */
	String modifyCustomerInfo(String loginname, String aliasName, String accountName, String phone, String email, String qq, String remark, String operator,Timestamp birthday,int sms);
	
	String modifyAgentInfo(String loginname, String referWebsite, String phone, String email, String qq, String partner, String remark,String oldagcode,String newagcode, String operator, Integer agentType, String agentCommission,Double liverate,Double sportsrate,Double lotteryrate);

	double getValidBetAmount(String loginname, Date start, Date end);

	String limitMethod(String loginname,String limit,String adminName);
	/**
	 * 
	 * @author sun
	 */
	String makePartnerBonus(String loginname, Integer year, Integer month, Double validBetAmount, Double cashinoutAmount, String operator, String ip, String remark);

	/**
	 * 
	 * @author sun
	 */
	String excuteMakePartnerBonus(String subLoginname, Double sumProfitAmount, String operator, String ip, String remark);

	/**
	 * 总报表 sun
	 */
	public Report queryReport(Date startTime, Date endTime, String userRole, String loginname);

	/**
	 * 一段时间内投注人数 sun
	 */
	public Integer queryAttendGameNumber(Date startTime, Date endTime, String userRole);

	/**
	 * 新增代理
	 */
	String addAgent(String acode, String loginname, String name, String phone, String email, String qq, String referWebsite, String ip, String operator , Integer agentType);
	public void insertOperatorLog(Operator operator, String remark);
	/**
	 * 新增代理（代理推荐码）
	 */
	String addAgentForRecommendedCode(String acode, String loginname, String name,
			String phone, String email, String qq, String partner,
			String referWebsite, String ip, String operatorLoginname, Integer agentType);
	/*
	 * 本地额度在某段时间内的变化值
	 */
	Double getLocalCreditChangeByPeriod(String loginname, Date startTime, Date endTime) ;

	public Map<String, List> queryCustomerInfoAnalysis(Date start, Date end, String loginname,Integer level,Integer warnflag, String agent,String intvalday,String nintvalday,String intro,String partner, Date startDate , Date endDate ,String order,String by);
	
	public Map<String, List> queryCustomerInfoAnalysisNew(Integer agentType , Date start, Date end, String loginname,Integer level,Integer warnflag, String agent,String intvalday,String nintvalday,String intro,String partner, Date startDate , Date endDate,String order,String by);

	public List queryCustomerInfoAnalysisNew2(Integer agentType, Date start, Date end, String loginname, Integer level, Integer warnflag, String agent, String intvalday, String nintvalday, String intro, String partner, Date startDate, Date endDate, String order, String by,String flag);
	
	String setWarnLevel(String loginname, Integer level,
			String operatorLoginname, String warnremark);

	public String editRateUser(String loginname, String operatorLoginname, Double rate,String platform);

	public String editUser(String loginname, String operatorLoginname, String intro);
	
	public String editAgentPartner(String loginname, String operatorLoginname, String partner);
	
	public Double getAgentProposal(Date start,Date end);
	
	public String judgeWetherToChangePwd(String loginname) throws ParseException;
	
	
	public void insertSelfYouHuiLog(String operator ,String loginname ,boolean flag, OperationLogType type ,String remark ) ;
	
	public String payYesterdayPoint(String sql);
	
	public void updatePtInfo(String pttype);
	
	public Map<String, List> queryAgentAnalysis(Integer agentType, Date start, Date end, String loginname,
			Integer level, Integer warnflag, String agent, String intvalday, String nintvalday,
			String partner, Date startDate, Date endDate, String order, String by) ;
	
	public void updateUserAgent(String loginname , String agent , String operator);
	

	public int getCountProxyFirst(String loginname,String countProxyFirst_start,String countProxyFirst_end);
	
	public void updateMGFreeGames(List<Offer> offers) throws SQLException ;
	
	public Boolean addPlayersToFreegame(String[] playerArr, Integer gameID, String operator) throws DocumentException, JAXBException ;
	
	public String synMemberInfo(String loginname, String password) throws Exception ;
	//比邻导入数据查询
	public List queryPreImportData(Date start, Date end, String intro,
			String isdeposit, Integer level, String agent, String order,
			String by,Integer state);

	public String importData(String phones);
	
	public String relieveOperator(String loginname);	
	
	public void insertExceptionLog(Operator operator,String remark);
	
	public Boolean bindEmployeeNo(String loginname,String employeeNo);
	
	public String resetOperatorPassword(String operator, String userName);

	
}