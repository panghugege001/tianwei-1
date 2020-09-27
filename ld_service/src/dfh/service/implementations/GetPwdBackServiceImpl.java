package dfh.service.implementations;


import dfh.model.Actionlogs;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.ActionLogType;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.IGetPwdBackService;
import dfh.utils.AESUtil;
import dfh.utils.DateUtil;
import dfh.utils.DtUtil;
import dfh.utils.PtUtil;
import dfh.utils.sendemail.CommonUtils;
import dfh.utils.sendemail.SendEmailWs;
/**
 * 找回密码
 * @author jad
 *
 */
public class GetPwdBackServiceImpl implements IGetPwdBackService {
	private CustomerService cs;
	private String name;
	private String yxdz;
	private String code;
	
	
	public CustomerService getCs() {
		return cs;
	}
	public void setCs(CustomerService cs) {
		this.cs = cs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getYxdz() {
		return yxdz;
	}
	public void setYxdz(String yxdz) {
		this.yxdz = yxdz;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 邮箱找回密码
	 */
	@Override
	public String isUserByYx(String name, String yxdz) {
		String strReturn = "";
		String yxdzOld = yxdz;
		Object obj = cs.get(Users.class, name);
		if (null != obj) {
			Users olduser = (Users) obj;
			if ("1".equals(olduser.getFlag() + "")) {
				return "该账号已冻结!不能使用找回密码！";
			}
			String email = olduser.getEmail();
			try {
				yxdz = AESUtil.aesEncrypt(yxdz, AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (email.equals(yxdz)) {
				String pwd = CommonUtils.getCharAndNumr(8);
				olduser.setPassword(EncryptionUtil.encryptPassword(pwd));
				olduser.setFlag(0);
				cs.update(olduser);
				
				//更新dt密码
				//DtUtil.updateInfo(name, pwd);
				//更新pt密码
				PtUtil.updatePlayerPassword(name, pwd);
				Userstatus userstatus = (Userstatus) cs.get(Userstatus.class,name);
				if (userstatus != null) {
					userstatus.setLoginerrornum(0);
					cs.modifyUserstatus(userstatus, null);
				} else {
					userstatus = new Userstatus(name, 1, 1);
					userstatus.setCashinwrong(0);
					cs.modifyUserstatus(userstatus, null);
				}
				/*String str = "尊敬的客户您好，您的密码是：" + pwd+ "，登陆后请立即修改密码。如果该信息与您无关，请忽略该信息！";
				SendEmailWs.sendEmails(yxdzOld, str, "密码找回");*/
				SendEmailWs.sendEmailsByApi(yxdzOld, pwd);
				strReturn = "邮件已发送，请查收！";
				//记入会员事件记录
				Actionlogs actionlog = new Actionlogs();
				actionlog.setLoginname(name);
				actionlog.setRemark("邮箱找回");
				actionlog.setCreatetime(DateUtil.now());
				actionlog.setAction(ActionLogType.PWD_RETRIEVE.getCode());
				cs.save(actionlog);
			} else {
				strReturn = "用户信息错误，请核查，或直接与客服联系！";
			}
		} else {
			strReturn = "用户信息错误，请核查，或直接与客服联系！";
		}
		return strReturn;
	}
	/**
	 * 短信找回密码
	 */
	@Override
	public String isUserByDh(String name, String dh, String pwd) {
		String strReturn = "0";
		Object obj = cs.get(Users.class, name);
		if (null != obj) {
			Users olduser = (Users) obj;
			if ("1".equals(olduser.getFlag() + "")) {
				return "2";
			}
			String dhhm = olduser.getPhone();
			try {
				dh = AESUtil.aesEncrypt(dh, AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (dhhm.equals(dh)) {
				olduser.setPassword(EncryptionUtil.encryptPassword(pwd));
				olduser.setFlag(0);
				cs.update(olduser);
				
				//更新dt密码
				//DtUtil.updateInfo(name, pwd);
				//更新pt密码
				PtUtil.updatePlayerPassword(name, pwd);
				
				Userstatus userstatus = (Userstatus) cs.get(Userstatus.class,name);
				if (userstatus != null) {
					userstatus.setLoginerrornum(0);
					cs.modifyUserstatus(userstatus, null);
				} else {
					userstatus = new Userstatus(name, 1, 1);
					userstatus.setCashinwrong(0);
					cs.modifyUserstatus(userstatus, null);
				}
				//记入会员事件记录
				Actionlogs actionlog = new Actionlogs();
				actionlog.setLoginname(name);
				actionlog.setRemark("短信找回");
				actionlog.setCreatetime(DateUtil.now());
				actionlog.setAction(ActionLogType.PWD_RETRIEVE.getCode());
				cs.save(actionlog);
			} else {
				strReturn = "1";
			}
		} else {
			strReturn = "1";
		}
		return strReturn;
	}
	
}
