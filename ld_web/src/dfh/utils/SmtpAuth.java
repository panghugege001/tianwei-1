package dfh.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/*
 * 邮件验证类
 * author:sun
 * */
public class SmtpAuth extends Authenticator {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SmtpAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}