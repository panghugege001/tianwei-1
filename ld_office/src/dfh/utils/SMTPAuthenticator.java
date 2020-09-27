// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SMTPAuthenticator.java

package dfh.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator
{

	private Properties props;

	public SMTPAuthenticator(Properties properties)
	{
		props = properties;
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication()
	{
		String username = props.getProperty("mail.from");
		String password = props.getProperty("mail.password");
		return new PasswordAuthentication(username, password);
	}

	public Properties getProps()
	{
		return props;
	}

	public void setProps(Properties props)
	{
		this.props = props;
	}
}
