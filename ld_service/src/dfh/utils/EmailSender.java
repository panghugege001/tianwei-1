package dfh.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

////author:sun createdate:2009-12-1
public class EmailSender {
	private static Log log = LogFactory.getLog(EmailSender.class);

	private String toEmail;// 收件人
	private String title;// 标题
	private String msg;// 发送的内容可为HTML
	private String froEmail;// 发件人
	private String froEmailPassword;// 对应的邮箱密码
	private String emailServer;// EMAIL服务器地址
	private String emailPort;// 端口号

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String tpEmail) {
		this.toEmail = tpEmail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFroEmail() {
		return froEmail;
	}

	public void setFroEmail(String froEmail) {
		this.froEmail = froEmail;
	}

	public String getFroEmailPassword() {
		return froEmailPassword;
	}

	public void setFroEmailPassword(String froEmailPassword) {
		this.froEmailPassword = froEmailPassword;
	}

	public String getEmailServer() {
		return emailServer;
	}

	public void setEmailServer(String emailServer) {
		this.emailServer = emailServer;
	}

	public String getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(String emailPort) {
		this.emailPort = emailPort;
	}

	public EmailSender() {
	}

	public EmailSender(String toEmail, String title, String msg, String froEmail, String froEmailPassword, String emailServer, String emailPort) {
		this.toEmail = toEmail;
		this.title = title;
		this.msg = msg;
		this.froEmail = froEmail;
		this.froEmailPassword = froEmailPassword;
		this.emailServer = emailServer;
		this.emailPort = emailPort;
	}
	
	

	/**
	 * 同步发送Email 可接爱多个收件人
	 */
	public void send() {
		try {
			log.info("start send email:" + this.toEmail);
			Properties props = new Properties();
			props.put("mail.smtp.host", this.emailServer);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", this.emailPort);

			SmtpAuth auth = new SmtpAuth(froEmail, froEmailPassword);

			Session ssn = Session.getInstance(props, auth);
			ssn.setDebug(true);
			MimeMessage message = new MimeMessage(ssn);
			InternetAddress fromAddress = new InternetAddress(froEmail);
			message.setFrom(fromAddress);

//			String[] toEmails = toEmail.split(",");
//			int toemaillength = toEmails.length;
//			InternetAddress[] toAddress = new InternetAddress[toemaillength];
//			for (int i = 0; i < toemaillength; i++) {
//				toAddress[i] = new InternetAddress(toEmails[i]);
//			}
			message.setRecipients(Message.RecipientType.TO, toEmail);
			message.setSubject(title);
			message.setText(msg);
			message.setContent(msg, "text/html ;charset=" + Constants.DEFAULT_ENCODING);
			Transport transport = ssn.getTransport("smtp");
			transport.connect(this.emailServer, this.froEmail, this.froEmailPassword);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			log.info("stop send email!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同步发送Email
	 */
	public void sendMailBySynchronizationMode() {
		send();
	}

	/**
	 * 异步发送Email
	 */
	public void sendMailByAsynchronousMode() {
		log.info("start sendMailByAsynchronousMode email:" + this.toEmail);
		TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					send();
				} catch (Exception e) {
					log.info(e.getMessage());
				}
			}
		});
	}

	/**
	 * @author sun
	 * @param loginname
	 *            用户账户
	 * @param loginPwd
	 *            　账户密码
	 * @param userRole
	 *            　用户类型
	 * @param userEmail
	 *            用户email
	 */
	public void sendMailNotify(String loginname, String loginPwd, String userRole, String userEmail) {
		try {
			if (StringUtils.isNotEmpty(userEmail)) {
				msg =Constants.SEND_EMAIL_MSG.replace("@username", loginname).replace("@password", loginPwd).replace("@userrole", userRole);
				toEmail = userEmail;
				title = Constants.SEND_EMAIL_TITLE + userRole;
				
				sendMailByAsynchronousMode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
	}
}
