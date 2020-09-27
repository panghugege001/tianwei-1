package dfh.service.interfaces;

import dfh.skydragon.webservice.model.LoginInfo;

public interface IUserWebService {
	public LoginInfo userLogin(String loginname,String password,String remoteip,String city,String clientos,String ioBB,Boolean loginPt);
	public Boolean checkUserIsExit(String loginname);
	public LoginInfo userRegister(String howToKnow, Integer sms, String loginname,
			String pwd, String accountName, String aliasName, String phone,
			String email, String qq, String referWebsite, String ipaddress,String city,String birthday,
			String intro ,String agentcode,String ioBB,String microchannel);
	public LoginInfo userRegisterTwo(String howToKnow, Integer sms, String loginname,
			String pwd, String accountName, String aliasName, String phone,
			String email, String qq, String referWebsite, String ipaddress,String city,String birthday,
			String intro,Double gifTamount,String agentStr,String type,String ioBB,String microchannel);
	public void saveBbs(String loginname);
	public void logout(String loginname);
	public String loginJc(String loginname, String password);
	public String loginNT(String loginname);
	public String loginGWNT(String loginname, String gameCode);
	public LoginInfo nTwoTicketlogin(String loginname, String token, String remoteip, String city,String clientos);

	LoginInfo loginByPassword(String loginName, String password, String remoteIp, String city, String clientOs, String ioBb);
	LoginInfo loginByEncryptPassword(String loginName, String password, String remoteIp, String city, String clientOs, String ioBB);
	LoginInfo loginByAccessToken(String platform,String accessToken, String remoteIp, String city, String clientOs, String ioBb);
}
