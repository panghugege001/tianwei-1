package dfh.action.customer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dfh.action.SubActionSupport;
import dfh.model.AgTryGame;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.SSLMailSender;

public class AgTryGameAction extends SubActionSupport {

	private static final long serialVersionUID = -6997957454254152565L;

	private String userPhone;

	private String imageCode;

	private String agPassword;
	
	private String agInfo;

	private SSLMailSender mailSender;
	
	AgTryGame agTryGame = new AgTryGame();

	/**
	 * 获取ag试玩账号
	 * 
	 * @return
	 */
	public String getAgTryAccounts() {
		try {
			// 电话号码
			if (!isNotNullAndEmpty(userPhone)) {
				GsonUtil.GsonObject("手机号码不能为空！");
				return null;
			}
			if (userPhone.length() != 11) {
				GsonUtil.GsonObject("输入的手机号码有误！");
				return null;
			}
			if (!isMobileNO(userPhone)) {
				GsonUtil.GsonObject("输入的手机号码有误！");
				return null;
			}
			// 验证码
			if (!isNotNullAndEmpty(imageCode)) {
				GsonUtil.GsonObject("验证码不能为空！");
				return null;
			}
			String code = (String) getHttpSession().getAttribute(Constants.SESSION_AG_TRY_RANDID);
			if (!code.equals(imageCode)) {
				GsonUtil.GsonObject("输入的验证码有误！");
				return null;
			}
			//ip限制
			String ip = getIp();
			if (ip == null || ip.equals("")) {
				ip = "127.0.0.1";
			}
			agTryGame.setAgIp(ip);
			agTryGame.setAgPhone(userPhone);
			// 验证电话号码是否纯在
			Integer count = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "agPhoneVerification", new Object[] { agTryGame }, Integer.class);
			if (count==2) {
				GsonUtil.GsonObject("该手机号码已经被注册！");
				return null;
			}else if (count==3) {
				GsonUtil.GsonObject("注册超限！");
				return null;
			}
			//申请试玩账号
			agTryGame = new AgTryGame();
			if (ip == null || ip.equals("")) {
				ip = "127.0.0.1";
			}
			agTryGame.setAgIp(ip);
			String password = dfh.utils.StringUtil.getRandomString(8);
			agTryGame.setAgPhone(userPhone.trim());
			agTryGame.setAgPassword(password);
			agTryGame = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "agSave", new Object[] { agTryGame }, AgTryGame.class);
			if (agTryGame == null) {
				GsonUtil.GsonObject("获取试玩账号失败！");
				return null;
			}
			GsonUtil.GsonObject("您申请的试玩账号和密码已发送你手机，请查收！");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("网络繁忙,请稍后再试！");
			return null;
		}
	}

	public String agTryLogin() {
		try {

			String ip = getIp();
			if (ip == null || ip.equals("")) {
				ip = "127.0.0.1";
			}
		
		    agTryGame = (AgTryGame) getHttpSession().getAttribute(Constants.SESSION_AGTRYUSER);
			agTryGame = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "agLogin", new Object[] { agTryGame,ip}, AgTryGame.class);
	
			//获取不是加密的密码
			getHttpSession().setAttribute(Constants.SESSION_AGTRYUSER,agTryGame);
			
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//LOG.info("ag试玩账号", e.toString());
			setAgInfo("网络繁忙，请稍后在试！");
			return ERROR;
		}
	}

	private boolean isNotNullAndEmpty(String str) {
		boolean b = false;
		if (null != str && str.trim().length() > 0) {
			b = true;
		}
		return b;
	}

	/**
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])||(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public SSLMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(SSLMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getAgPassword() {
		return agPassword;
	}

	public void setAgPassword(String agPassword) {
		this.agPassword = agPassword;
	}

	public String getAgInfo() {
		return agInfo;
	}

	public void setAgInfo(String agInfo) {
		this.agInfo = agInfo;
	}

	public AgTryGame getAgTryGame() {
		return agTryGame;
	}

	public void setAgTryGame(AgTryGame agTryGame) {
		this.agTryGame = agTryGame;
	}
	
}
