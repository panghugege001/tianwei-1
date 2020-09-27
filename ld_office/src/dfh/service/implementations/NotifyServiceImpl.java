package dfh.service.implementations;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.todaynic.client.mobile.SMS;

import dfh.dao.UserDao;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.SMSSwitch;
import dfh.model.Users;
import dfh.model.enums.DXWErrorCode;
import dfh.model.enums.SMSContent;
import dfh.service.interfaces.NotifyService;
import dfh.utils.AESUtil;
import dfh.utils.EmailSender;
import dfh.utils.HttpUtils;
import dfh.utils.SendPhoneMsgUtil;
import dfh.utils.SmsDXWUtils;

public class NotifyServiceImpl extends UniversalServiceImpl implements
		NotifyService {

	private static Log log = LogFactory.getLog(NotifyServiceImpl.class);
	private String emailPassword;
	private String emailPort;
	private String emailServer;
	private String emailUser;
	private SMS sms;
	private String vcpPassword;
	private String vcpPort;
	private String vcpServer;
	private String vcpUserID;
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public NotifyServiceImpl() {
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public String getEmailPort() {
		return emailPort;
	}

	public String getEmailServer() {
		return emailServer;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public String getVcpPassword() {
		return vcpPassword;
	}

	public String getVcpPort() {
		return vcpPort;
	}

	public String getVcpServer() {
		return vcpServer;
	}

	public String getVcpUserID() {
		return vcpUserID;
	}

	public String sendEmail(String email, String title, String msg) {
		log.info((new StringBuilder("send email to ")).append(email).toString());
		EmailSender emailSend = new EmailSender(email, title, msg, emailUser,
				emailPassword, this.emailServer, this.emailPort);
		emailSend.sendMailByAsynchronousMode();
		return "邮件已经送出";
	}

	public String sendSmsListByLevel(String msg, Integer level, Date start,
			Date end) {
		List phones = userDao.getUserPhoneListByLevel(level, start, end);
		if (phones == null || phones.size() == 0) {
			return "该时间范围内，不存在有效的注册会员";
		}

		StringBuilder sf = new StringBuilder();
		try {
			int z = 0;
			for (int i = 0; i < phones.size(); i++) {
				String phone = (String) phones.get(i);
				if (phone != null && !phone.trim().equals("")) {
					try {
						phone = AESUtil.aesDecrypt(phone, AESUtil.KEY);
					} catch (Exception e) {
						log.error("群发短信时有手机号码格式错误：");
					}
					if (phone.length() == 11) {
						z++;
						if (i != (phones.size() - 1)) {
							sf.append(phone.trim() + ",");
						} else {
							sf.append(phone.trim());
						}
					}
					/*if (i != 0 && i % 498 == 0) {
						Thread thread = new Thread();
						thread.sleep(5);
						String str = SendPhoneMsgUtil.shxtongSend(sf.toString(),msg);
						if (!str.startsWith("发送成功")) {
							return "发送失败，请与管理员联系:"+str;
						}
						sf = new StringBuilder("");
					}*/
				}

			}
//			if (z < 498) {
				String str = SendPhoneMsgUtil.bcSend(sf.toString(),msg);
				if (!str.startsWith("发送成功")) {
					return "发送失败，请与管理员联系:"+str;
				}
//			}
		} catch (Exception e) {
			log.error("群发短信失败", e);
			return "发送失败，请重新发送或与管理员联系";
		}
		return "发送成功,共发送" + phones.size() + "条！";
	}

	public String sendSmsList(String msg, String type, Date start, Date end) {
		List phones = userDao.getUserPhoneList(type, start, end);
		if (phones == null || phones.size() == 0) {
			return "该时间范围内，不存在有效的注册会员";
		}

		StringBuilder sf = new StringBuilder();
		try {
			int z = 0;
			for (int i = 0; i < phones.size(); i++) {
				String phone = (String) phones.get(i);
				if (phone != null && !phone.trim().equals("")) {
					try {
					phone = AESUtil.aesDecrypt(phone, AESUtil.KEY);
					} catch (Exception e) {
						log.error("群发短信时有手机号码格式错误：");
					}
					if (phone.length() == 11) {
						z++;
						if (i != (phones.size() - 1)) {
							sf.append(phone.trim() + ",");
						} else {
							sf.append(phone.trim());
						}
					}
					/*if (i != 0 && i % 498 == 0) {
						String str = SendPhoneMsgUtil.shxtongSend(sf.toString(),msg);
						if (!str.startsWith("发送成功")) {
							return "发送失败，请与管理员联系:"+str;
						}
						sf = new StringBuilder("");
						Thread thread = new Thread();
						thread.sleep(5000);
					}*/
				}
			}
//			if (z < 498) {
				String str = SendPhoneMsgUtil.bcSend(sf.toString(), msg);
				if (!str.startsWith("发送成功")) {
					return "发送失败，请与管理员联系:"+str;
				}
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("群发短信失败", e);
			return "发送失败，请重新发送或与管理员联系";
		}
		return "发送成功,共发送" + phones.size() + "条！";
	}

	public String sendSmsList45(String msg, List phones, Date start, Date end) {
		if (phones == null || phones.size() == 0) {
			return "该时间范围内，不存在有效的注册会员";
		}
		StringBuilder sf = new StringBuilder();
		try {
			int z = 0;
			for (int i = 0; i < phones.size(); i++) {
				Users user = (Users) phones.get(i);
				String phone = user.getPhone();
				if (phone != null && !phone.trim().equals("")
						&& phone.length() == 11) {
					z++;
					if (i != (phones.size() - 1)) {
						sf.append(phone.trim() + ",");
					} else {
						sf.append(phone.trim());
					}
				}
//				if (i != 0 && i % 498 == 0) {
//					Thread thread = new Thread();
//					thread.sleep(5);
//					String str = SendPhoneMsgUtil.bcSend(sf.toString(), msg);
//					if (!str.startsWith("发送成功")) {
//						return "发送失败，请与管理员联系:"+str;
//					}
//					sf = new StringBuilder("");
//				}
			}
//			if (z < 498) {
				String str = SendPhoneMsgUtil.bcSend(sf.toString(), msg);
				if (!str.startsWith("发送成功")) {
					return "发送失败，请与管理员联系:"+str;
				}
//			}

		} catch (Exception e) {
			log.error("群发短信失败", e);
			return "发送失败，请重新发送或与管理员联系";
		}
		return "发送成功,共发送" + phones.size() + "条！";
	}

	public String sendSms(String phoneNo, String msg) {
		initSmsObject();
		log.info((new StringBuilder("send sms to ")).append(phoneNo).toString());
		try {
			sms.sendSMS(phoneNo, msg, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("2000".equals(sms.getCode()))
			return "发送成功";
		else
			return sms.getMsg();
	}

	public String sendSmsNew(String phoneNo, String msg) {
		try {
			return SendPhoneMsgUtil.bcSend(phoneNo, msg);// 自建短信 替换为 最新的接口
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	private void initSmsObject() {
		if (sms == null) {
			Hashtable config = new Hashtable();
			config.put("VCPSERVER", vcpServer);
			config.put("VCPSVPORT", vcpPort);
			config.put("VCPUSERID", vcpUserID);
			config.put("VCPPASSWD", vcpPassword);
			sms = new SMS(config);
			sms.setEncodeType("UTF-8");
		}
	}

	/**
	 * 
	 * @methods initSmsConfig
	 * @description <p>
	 *              方法的详细说明
	 *              </p>
	 * @author erick
	 * @date 2014年11月13日 下午1:54:36 参数说明
	 * @return void 返回结果的说明
	 */
	private void initSmsConfig() {
		if (sms == null) {
			Hashtable config = new Hashtable();
			config.put("VCPSERVER", vcpServer);
			config.put("VCPSVPORT", vcpPort);
			config.put("VCPUSERID", "ms101380");
			config.put("VCPPASSWD", "mzgzmj");
			sms = new SMS(config);
			sms.setEncodeType("UTF-8");
		}
	}

	public static String callMine(String phone, String msg) throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://115.238.248.131:808/goip_send_sms.html?username=root&password=root&port=1&recipients="
				+ phone + "&sms=" + URLEncoder.encode(msg, "UTF-8");
		GetMethod method = new GetMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");

		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info("result-->" + result);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
		if (result.contains("OK")) {
			return "发送成功";
		} else {
			return json.getString("reason");
		}
	}

	public static String callMineTwo(String phone, String msg) throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://115.238.248.131:808/goip_send_sms.html?username=root&password=root&port=1&recipients="
				+ phone + "&sms=" + URLEncoder.encode(msg, "UTF-8");
		GetMethod method = new GetMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");

		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info("result-->" + result);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
		if (result.contains("OK")) {
			return "发送成功";
		} else {
			return json.getString("reason");
		}
	}
	
	/**
	 * 根据订单号发送用户定制短信（9）
	 * @param
	 * @return
	 */
	@Override
	public String sendSMSByBillno(String billno, String type){
		String loginname = null;
		Double money = null;
		if("payorder".equals(type)){
			Payorder payorder = (Payorder) userDao.get(Payorder.class, billno);//有的订单刚生成的，不能用从库
			return this.sendSMSByPayorder(payorder);
		} else if("proposal".equals(type)){
			Proposal proposal = (Proposal) userDao.get(Proposal.class, billno);//有的订单刚生成的，不能用从库
			return this.sendSMSByProposal(proposal);
		} else {
			return "参数错误";
		}
	}
	
	/**
	 * 根据订单号发送用户定制短信（9）Proposal
	 * @param
	 * @return
	 */
	@Override
	public String sendSMSByProposal(Proposal proposal){
		
		if(proposal == null){
			return "Proposal订单为空无法发送";
		}
		Users user = (Users) userDao.get(Users.class, proposal.getLoginname());
		if(user == null){
			return "未查询到用户:" + proposal.getLoginname();
		}
		Double money = proposal.getAmount();
		return this.sendSMSP(user, money);
	}
	
	/**
	 * 根据订单号发送用户定制短信（9）Payorder
	 * @param
	 * @return
	 */
	@Override
	public String sendSMSByPayorder(Payorder payorder){
		
		if(payorder == null){
			return "Payorder订单为空无法发送";
		}
		Users user = (Users) userDao.get(Users.class, payorder.getLoginname());
		if(user == null){
			return "未查询到用户:" + payorder.getLoginname();
		}
		Double money = payorder.getMoney();
		return this.sendSMSP(user, money);
	}
	
	private String sendSMSP(Users user, Double money) {
		if(user == null){
			return "user is null, can't send SMS";
		}
		String content = SMSContent.getText("9").replace("$XXX", user.getLoginname()).replace("$MONEY", money + "");
		String service = user.getAddress();
		boolean sendflag = false;
		DetachedCriteria dc = DetachedCriteria.forClass(SMSSwitch.class);
		dc.add(Restrictions.eq("disable", "否"));
		dc.add(Restrictions.eq("type", "9"));
		List<SMSSwitch> list = userDao.getHibernateTemplate().findByCriteria(dc);
		if(list != null && list.size() > 0 && StringUtils.isNotBlank(service)){
			SMSSwitch smsswitch = list.get(0);
			
			String[] ids = service.split(",");
			if(ids != null && ids.length > 0){
				for(int i = 0; i < ids.length; i++){
					
					if(smsswitch.getType().equals(ids[i])){
						sendflag = true;
						break;
					}
				}
			}
			
			if(sendflag){
				if(smsswitch.getMinvalue() != null){
					
					Double minvalue = Double.parseDouble(smsswitch.getMinvalue().toString());
					if(money == null || money < minvalue){
						sendflag = false;
					}
				}
			}
			
			if(sendflag){
				String phoneno = null;
				try {
//					phoneno = AESUtil.aesDecrypt(user.getPhone(),AESUtil.KEY);
					phoneno = user.getPhone();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(StringUtils.isNotBlank(phoneno)){
					String code = SmsDXWUtils.sendSms(phoneno, content);
					if("1".equals(code)){
						return "发送成功！";
					} else {
						String msg = DXWErrorCode.getText(code);
						return "发送失败：" + msg;
					}
				} else {
					return "用户手机号码为空";
				}
			}
			
		}
		return "不发送";
	}

	public static void main(String[] args) {
		try {
			System.out.println(callMineTwo("15502043560", "您好123456"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public void setEmailPort(String emailPort) {
		this.emailPort = emailPort;
	}

	public void setEmailServer(String emailServer) {
		this.emailServer = emailServer;
	}

	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}

	public void setVcpPassword(String vcpPassword) {
		this.vcpPassword = vcpPassword;
	}

	public void setVcpPort(String vcpPort) {
		this.vcpPort = vcpPort;
	}

	public void setVcpServer(String vcpServer) {
		this.vcpServer = vcpServer;
	}

	public void setVcpUserID(String vcpUserID) {
		this.vcpUserID = vcpUserID;
	}

}
