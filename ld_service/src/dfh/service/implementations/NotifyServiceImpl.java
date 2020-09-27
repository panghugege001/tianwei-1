package dfh.service.implementations;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.todaynic.client.mobile.SMS;

import dfh.dao.UserDao;
import dfh.service.interfaces.NotifyService;
import dfh.utils.EmailSender;

public class NotifyServiceImpl extends UniversalServiceImpl implements NotifyService {

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
		EmailSender emailSend=new EmailSender(email, title, msg, emailUser, emailPassword, this.emailServer, this.emailPort);
		if(email.split(",").length>5)
		emailSend.sendMailByAsynchronousMode();
		else
		emailSend.sendMailBySynchronizationMode();
		return "邮件已经送出";
	}
	
	public String sendSms(String phoneNo, String msg) {
		if (sms == null) {
			Hashtable config = new Hashtable();
			config.put("VCPSERVER", vcpServer);
			config.put("VCPSVPORT", vcpPort);
			config.put("VCPUSERID", vcpUserID);
			config.put("VCPPASSWD", vcpPassword);
			sms = new SMS(config);
			sms.setEncodeType("UTF-8");
		}
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

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
