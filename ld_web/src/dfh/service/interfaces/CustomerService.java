package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Actionlogs;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.notdb.FirstlyCashin;
import dfh.remote.bean.LoginValidationBean;

public interface CustomerService extends UniversalService {

	Double getRemoteCredit(String loginname);

	String loginWeb(String loginname, String password, String ip);

	String loginWebForBoth(String loginname, String password, String ip);

	String register(String howToKnow,Integer sms,String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress,String intro);

	String gameLoginValidation(LoginValidationBean bean);

	String modifyBankInfo(String loginname, String bank, String bankAddress, String accountType, String accountName, String accountNo, String accountCity, String ip) throws GenericDfhRuntimeException;

	String modifyCustomerInfo(Integer sms,String loginname, String aliasName, String phone, String email, String qq, String ip) throws GenericDfhRuntimeException;

	String modifyPassword(String loginname, String oldPwd, String newPwd, String ip) throws GenericDfhRuntimeException;

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
}