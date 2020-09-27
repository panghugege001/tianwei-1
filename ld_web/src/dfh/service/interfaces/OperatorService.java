package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.model.Autotask;
import dfh.model.enums.AutoGenerateType;
import dfh.model.notdb.Report;

public interface OperatorService extends UniversalService {
	

	String EnableUser(String userName, boolean isEnable, String operator);

	String login(String loginname, String password, String ip);

	String resetPassword(String loginname, String password, String operator);

	Autotask startAutoTask(AutoGenerateType type, Integer totalRecords, String operator);

	Autotask getLastAutoTask(AutoGenerateType type);

	Autotask refreshAutoTask(Integer taskID, Boolean isSuccess, String remark);

	Autotask stopAutoTask(Integer taskID, String remark);

	Autotask finishAutoTask(Integer taskID, String remark);

	String generateXimaForEach(String loginname, Date startTime, Date endTime, String operator);

	String setLevel(String loginname, Integer level, String operator);

	String changeCreditManual(String loginname, Double amount, String remark, String operator);

	String modifyOwnPassword(String operator, String oldPassword, String newPassword);

	String createSubOperator(String newOperator, String password, String operator);

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
	String modifyCustomerInfo(String loginname, String aliasName, String phone, String email, String qq, String remark, String operator);

	double getValidBetAmount(String loginname, Date start, Date end);

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

	List excuteSQL(final String sql);

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
	String addAgent(String acode, String loginname, String name, String phone, String email, String qq, String referWebsite, String ip, String operator);

	/*
	 * 本地额度在某段时间内的变化值
	 */
	Double getLocalCreditChangeByPeriod(String loginname, Date startTime, Date endTime) ;
	
	List getAllUsers();
	/**
	 * 查询bankinfo中amount是否超过100万
	 */
	Double getBankInfoAmountByName(String username);
}