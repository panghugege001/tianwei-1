package dfh.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Users;
import dfh.model.notdb.FirstlyCashin;
import dfh.remote.bean.LoginValidationBean;

public interface CustomerService extends UniversalService {

	Double getRemoteCredit(String loginname);

	public Map<Object, Object> getResultMap(String sql);
	
	String getRemoteAgCredit(String loginname);
	
	String getRemoteBbinCredit(String loginname);

	String loginWeb(String loginname, String password, String ip);

	String loginWebForBoth(String loginname, String password, String ip);

	String register(String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress);

	String gameLoginValidation(LoginValidationBean bean);

	String modifyBankInfo(String loginname, String bank, String bankAddress, String accountType, String accountName, String accountNo, String accountCity, String ip) throws GenericDfhRuntimeException;

	String modifyCustomerInfo(String loginname, String aliasName, String phone, String email, String qq, String ip) throws GenericDfhRuntimeException;

	String modifyPassword(String loginname, String oldPwd, String newPwd, String ip) throws GenericDfhRuntimeException;

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

	String getRemoteKenoCredit(String loginname);

	String getRemoteAgInCredit(String loginname);

	String getRemoteSbCredit(String loginname);
	
	/**
	 * 更新客服的推荐码
	 */
	public void updateIntroForCS();

	int executeUpdate(String sql, Map<String, Object> params);
	
	/**
	 * 根据QQ号码查询用户
	 * @param qq
	 * @return users列表(可能有多个用户),其中只包含用户名和代理ID
	 */
	public List<Object[]> queryUserByQQ(String qq);
	
	Boolean batchUpdate(String belongto, String ids);
	
}