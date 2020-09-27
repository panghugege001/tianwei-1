package dfh.service.interfaces;

public interface AiSupportService {
	
	public String unlockAccountByInfo(String loginname, String accoutName, String email, String phone);

	public String unlockAccountByPassword(String loginname, String password);

	public String forgetAccount(String accoutName, String email, String phone);

	public String forgetAccbySms(String phone, String ip);

	public String sendForgetAccbyEmail(String email, String ip);

	/**
	 * 取款密码
	 * @return
	 */
	public String withdrawPassForget(String loginname, String accoutName, String email, String phone);

	
}