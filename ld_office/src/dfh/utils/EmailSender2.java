package dfh.utils;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;

public class EmailSender2 {

	private static final String SMTP_HOST_NAME = "121.54.168.71";
	private static final String SMTP_PORT = "25";
	private static final String emailMsgTxt = "";;
	private static final String emailSubjectTxt = "大富豪国际娱乐城";
	private static final String emailFromAddress = "support@88fuhao.net";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String emailPassword = "654321a";
	 private static final String[] sendTo = { "win@good.ph", "532181371@qq.com", "119140846@qq.com", "martin119140846@gmail.com" };

	static Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);

	public static void main(String args[]) throws Exception {
//		String address = IOUtils.toString(new FileInputStream("C:\\Documents and Settings\\Administrator\\桌面\\email.txt"));
//		// address=address.replaceAll("\\n", "");
//		String[] addressArray = address.split(",");
//		List<String> list = new ArrayList<String>();
//		for (int i = 0; i < addressArray.length; i++) {
//			String email = StringUtil.trimToEmpty(addressArray[i]);
//			// if(!p_email.matcher(email).matches())
//			// System.err.println("$$"+email+"$$"+" "+p_email.matcher(email).matches());
//			if (list.contains(email))
//				continue;
//			if (p_email.matcher(email).matches()) {
//				list.add(email);
//			}
//		}
//		System.out.println("总共:" + list.size() + " 有效邮箱");
//
//		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		EmailSender2 sender = new EmailSender2();
		String text = IOUtils.toString(new FileInputStream("C:\\Documents and Settings\\Administrator\\桌面\\test.html"));
//		String sendTo[] = new String[1];
//		sender.sendSSLMessage(list.toArray(new String[]{}), emailSubjectTxt, text, emailFromAddress);
		sender.sendSSLMessage(sendTo, emailSubjectTxt, text, emailFromAddress);
//		Integer size = 20;
//		while (list.size() > 0) {
//			try {
//				String sendTo[] = new String[1];
//				Integer sendLength=list.size()>size?size:list.size();
//				sendTo =  list.subList(0, sendLength).toArray(sendTo);
//				System.out.println(Arrays.toString(sendTo));
//				sender.sendSSLMessage(sendTo, emailSubjectTxt, text, emailFromAddress);
//				System.out.println("Sucessfully Send "+sendLength+" mail to All Users");
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (list.size() > size)
//					list = list.subList(size, list.size());
//				else
//					list = new ArrayList<String>();
//			}
//			Thread.sleep(1000 * 20);
//		}

		System.out.println("finish!!!");

	}

	public void sendSSLMessage(String recipients[], String subject, String message, String from) throws MessagingException {
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("support.dafuhao@gmail.com", "macbookair");
			}
		});

		session.setDebug(debug);

		MimeMessage msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSubject(subject);
		// msg.setText(message);
		msg.setContent(message, "text/html ;charset=" + Constants.DEFAULT_ENCODING);
		Transport transport = session.getTransport("smtp");
		transport.connect(SMTP_HOST_NAME, emailFromAddress, emailPassword);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
	}
}