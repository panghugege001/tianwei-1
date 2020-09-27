package dfh.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Actionlogs;
import dfh.model.IESnare;
import dfh.model.UrgeOrder;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.notdb.FirstlyCashin;
import dfh.model.weCustomer.UserInfo;
import dfh.remote.bean.LoginValidationBean;
import dfh.remote.bean.SportBookLoginValidationBean;

public interface CustomerService extends UniversalService {

	Double getRemoteCredit(String loginname);

	String loginWeb(String loginname, String password, String ip);

	String loginWebForBoth(String loginname, String password, String ip);

	String register(String howToKnow,Integer sms,String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress,String city,String birthday,String intro,String agentcode,String ioBB,String microchannel);

	String registerTwo(String howToKnow,Integer sms,String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress,String city,String birthday,String intro,Double gifTamount,String agentStr,String type,String ioBB,String microchannel);

	String gameLoginValidation(LoginValidationBean bean);

	String modifyPayPassword(String loginname, String oldPwd, String newPwd, int isPassWord,ProposalService proposalService) throws GenericDfhRuntimeException;

	String modifyBankInfo(String loginname, String bank, String bankAddress, String accountType, String accountName, String accountNo, String accountCity, String ip) throws GenericDfhRuntimeException;

	String modifyCustomerInfo(Integer sms,String loginname, String aliasName, String phone, String email, String qq, String ip,String mailaddress,String microchannel,String birthday,String accountName) throws GenericDfhRuntimeException;

	String modifyPassword(String loginname, String oldPwd, String newPwd, String ip) throws GenericDfhRuntimeException;
	 Double checkWithdrawRecord(String loginname,Date date);
	Double checkDepositRecord(String loginname,Date date);
	
	void synBbsMemberInfo(String loginname, String password); 
	
	String chooseservice(String loginname, String service,String ip) throws GenericDfhRuntimeException;
	
	String queryUserLastXimaEndTime(String loginname);

	Double queryValidBetAmount(String loginname, Date start, Date end);

	/**
	 * 参加擂台赛
	 * 
	 */
	// String addMatch(String loginname, String operator);

	/**
	 * 第一笔存款
	 * 
	 * @author sun
	 * @return
	 */
	FirstlyCashin getFirstCashin(String loginname);

	/**
	 * 检查用户真实姓名
	 * 
	 * @author sun
	 * @return
	 */
	String checkIsUserAliasName(String aliasName);

	List<Users> getSubUsers(String agent);
	
	
	
	Userstatus getUserstatus(String loginname);
	
	String modifyUserstatus(Userstatus userstatus,Actionlogs actionlog);
	
	String lockUser(String loginname);
	
	public String addAgent(String howToKnow,String loginname, String pwd, String accountName, String phone, String email, String qq, String referWebsite, String ipaddress,String city,String microchannel,String partner);
	
	public Boolean wetherHaveSameInfo(Users user);
	
	public Boolean isUsePt8YouHui(String loginname,String accountName,String registerIp);
	
	public Boolean isUsePt8YouHuiNoTime(String loginname,String accountName,String registerIp);
	
	public List<String> getCardNums(String loginname);
	
	public Boolean repeatCards(String loginname);
	
	public IESnare getIESanre(String device);
	/**
	 * 根据cpuID获取对应的数据
	 * @param device
	 * @return
	 */
	public List<IESnare> getAllIESanre(String device);
	
	/**
	 * gpi 登入验证
	 * @param token
	 * @return
	 */
	public String validateGPIAccess(String token);
	
	/* 代理处理部分开始 */
	public String queryAgentMonthlyReport(String loginname);
	/* 代理处理部分结束 */
	
	public String queryAgentReport(String loginname);
	
	
	public Double getDoubleValueBySql(String sql, Map<String, Object> params);
	
	public Integer getCount(String sql, Map<String, Object> params);
	
	public String nTwoGameLoginValidation(LoginValidationBean bean); 
	
	public String nTwoGameAutoLoginValidation(String elementId, String userid, String uuid, String clinetIp);
	
	public String nTwoGetTicketValidation(String id, String username, String date, String sign);
	
	public String nTwoSingleLoginUrl(String loginname);
	
	public String nTwoAppLoginUrl(String loginname);
	
	public List<UrgeOrder> queryUrgeOrderList(String loginname);
	
	public Users getAgentByWebSiteNew(String address);
	
	public Users getAgentById(Integer id);
	
	/**
	 * 
	 * @param tempDepositTime 存款时间
	 * @param thirdOrder	支付宝订单号
	 * @return 支付宝转账记录处理状态
	 */
	public Byte checkthirdOrder(String tempDepositTime,String thirdOrder);
	
	public boolean isUserIpOrNameDuplicate(String accountName,String registeIp);
	
	public boolean isApplyAppPreferential(String loginName);    
	
	public int excuteSql(String sql, Map<String, Object> params);
	
	public UserInfo getUserInfo(String username);

	String updateWeiXin(String loginname, String microchannel);
	String updateQQ(String loginname, String qq);
}