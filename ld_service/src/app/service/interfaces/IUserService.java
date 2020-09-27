package app.service.interfaces;

import java.io.Serializable;

import dfh.model.Users;
import dfh.utils.Page;
import app.model.vo.AgentSettingVO;
import app.model.vo.AppCustomVersionVO;
import app.model.vo.BasicInfoVO;
import app.model.vo.BasicRequestVO;
import app.model.vo.ChangePasswordVO;
import app.model.vo.GamePlatBalanceVO;
import app.model.vo.HotGameLoginVO;
import app.model.vo.LoginInfoVO;
import app.model.vo.OnlinePayRecordVO;
import app.model.vo.OnlinePayVO;
import app.model.vo.UnReadVO;
import app.model.vo.UsersVO;

public interface IUserService {

	@SuppressWarnings("rawtypes")
	Object get(Class entityClass, Serializable id);
	
	Serializable save(Object entity);
	
	public LoginInfoVO userRegister(UsersVO userVo);
	
	public LoginInfoVO userLogin(UsersVO userVo);
	
	public LoginInfoVO loginByToken(UsersVO userVo);
	
	public BasicInfoVO getBasicInfo(BasicRequestVO vo);
	
	public UnReadVO getUnReadInfo(String loginname);
	
	public UsersVO getUserInfo(String loginname, IUserBankInfoService userBankInfoService);
	
	public ChangePasswordVO changePassword(ChangePasswordVO vo);
	
	public OnlinePayVO getOnlinePayUrl(UsersVO vo);
	
	public OnlinePayVO getWebDomain();
	
	Page getOnlinePayRecords(OnlinePayRecordVO vo);

	Page getCashinRecords(OnlinePayRecordVO vo);

	Page getWithdrawRecords(OnlinePayRecordVO vo);

	Page getTransferRecords(OnlinePayRecordVO vo);

	Page getConcessionRecords(OnlinePayRecordVO vo);

	Page getCouponRecords(OnlinePayRecordVO vo);
	
	Page getXimaRecords(OnlinePayRecordVO vo) throws Exception;
	
	Page getDepositOrderRecords(OnlinePayRecordVO vo);

	Page getPointRecords(OnlinePayRecordVO vo);

	Page getFriendRecords(OnlinePayRecordVO vo);

	HotGameLoginVO hotGameLogin(HotGameLoginVO vo);

	void update(Users users);
	
	AgentSettingVO updateAgentInfo(AgentSettingVO vo);
	
	AppCustomVersionVO[] getAppCustomVersionData(String hostAddress);
}