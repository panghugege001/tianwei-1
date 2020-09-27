package dfh.utils;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class SSLMailSender {

	private String smtp_host_name;
	private int smtp_host_port;
	private String smtp_auth_user;
	private String smtp_auth_pwd;
	private String smtp_transport_protocol;
	private Logger log = Logger.getLogger(SSLMailSender.class);
	private Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
	private String toUser;
	private String html;
	private String subject;
	private String fromEmail;



	

	// methods:
	public void sendmail(String _html, String _toUser, String _subject)throws Exception {

		synchronized (SSLMailSender.class) {
			this.html = _html;
			this.toUser = _toUser;
			this.subject = _subject;

			if (p_email.matcher(toUser).matches()) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {

							Properties props = new Properties();

							props.put("mail.transport.protocol", smtp_transport_protocol);
							props.put("mail.smtp.host", smtp_host_name);
							props.put("mail.smtp.auth", "true");
							//props.put("mail.smtp.port", getSmtp_host_port());

							//SmtpAuth auth = new SmtpAuth(getSmtp_auth_user(), getSmtp_auth_pwd());
							Session mailSession = Session.getInstance(props);
							mailSession.setDebug(true);
							Transport transport = mailSession.getTransport("smtp");

							MimeMessage message = new MimeMessage(mailSession);
							message.setFrom(new InternetAddress(fromEmail));
							message.setSubject(subject);
							message.setContent(html, "text/html;charset=UTF-8");
							message.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
							message.saveChanges();
							//transport.connect(smtp_host_name, smtp_host_port, smtp_auth_user, smtp_auth_pwd);
							transport.connect(smtp_host_name, smtp_auth_user, smtp_auth_pwd);
							transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
							transport.close();
						} catch (Exception e) {
							// TODO: handle exception
							log.error(e);
						}
					}

				}).start();
			}

		}

	}

	// setter:
	public String getSmtp_host_name() {
		return smtp_host_name;
	}

	public void setSmtp_host_name(String smtpHostName) {
		smtp_host_name = smtpHostName;
	}

	public int getSmtp_host_port() {
		return smtp_host_port;
	}

	public void setSmtp_host_port(int smtpHostPort) {
		smtp_host_port = smtpHostPort;
	}

	public String getSmtp_auth_user() {
		return smtp_auth_user;
	}

	public void setSmtp_auth_user(String smtpAuthUser) {
		smtp_auth_user = smtpAuthUser;
	}

	public String getSmtp_auth_pwd() {
		return smtp_auth_pwd;
	}

	public void setSmtp_auth_pwd(String smtpAuthPwd) {
		smtp_auth_pwd = smtpAuthPwd;
	}

	public String getSmtp_transport_protocol() {
		return smtp_transport_protocol;
	}

	public void setSmtp_transport_protocol(String smtpTransportProtocol) {
		smtp_transport_protocol = smtpTransportProtocol;
	}
	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	
	

}
