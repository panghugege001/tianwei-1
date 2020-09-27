package dfh.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

////author:sun createdate:2009-12-1
public class EmailSender3 {
	private static Log log = LogFactory.getLog(EmailSender3.class);

	private String title;// 标题
	private String msg;// 发送的内容可为HTML
	private String froEmail;// 发件人
	private String froEmailPassword;// 对应的邮箱密码
	private String emailServer;// EMAIL服务器地址
	private String emailPort;// 端口号

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

	public EmailSender3(String title, String msg, String froEmail, String froEmailPassword, String emailServer, String emailPort) {
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
	private void send(String[] toEmail) {
		try {
			log.info("start send email:" + Arrays.toString(toEmail));
			Properties props = new Properties();
			props.put("mail.smtp.host", this.emailServer);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", this.emailPort);

			SmtpAuth auth = new SmtpAuth(froEmail, froEmailPassword);

			Session ssn = Session.getInstance(props, auth);
			MimeMessage message = new MimeMessage(ssn);
			InternetAddress fromAddress = new InternetAddress("support.dafuhao@gmail.com");
			message.setFrom(fromAddress);

			InternetAddress[] toAddress = new InternetAddress[toEmail.length];
			for (int i = 0; i < toEmail.length; i++) {
				toAddress[i] = new InternetAddress(toEmail[i]);
			}
			message.setRecipients(Message.RecipientType.TO, toAddress);
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

	public static void main(String[] args) {
		try {
			Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);

			String address = IOUtils.toString(new FileInputStream("C:\\Documents and Settings\\Administrator\\桌面\\email.txt"));
			// address=address.replaceAll("\\n", "");
			String[] addressArray = address.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < addressArray.length; i++) {
				String email = StringUtils.trimToEmpty(addressArray[i]);
				// if(!p_email.matcher(email).matches())
				// System.err.println("$$"+email+"$$"+" "+p_email.matcher(email).matches());
				if (list.contains(email))
					continue;
				if (p_email.matcher(email).matches()) {
					list.add(email);
				}
			}
			System.out.println("总共:" + list.size() + " 有效邮箱");

			String text = IOUtils.toString(new FileInputStream("C:\\Documents and Settings\\Administrator\\桌面\\test.html"));
			EmailSender3 sender = new EmailSender3("真人百家乐", text, "support@88fuhao.net", "654321a", "121.54.168.71", "25");
			sender.send(list.toArray(new String[] {}));
//			sender.send(new String[]{"532181371@qq.com"});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
