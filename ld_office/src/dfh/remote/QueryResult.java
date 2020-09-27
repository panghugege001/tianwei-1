// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   QueryResult.java

package dfh.remote;

public class QueryResult
{

	public static void main(String args[])
	{
		System.out.println((new Long(0L)).toString());
	}
	private String loginname;
	private String currency;
	private Double credit=0.0;

	private String code;

	public QueryResult()
	{
	}

	public String getCode()
	{
		return code;
	}

	public Double getCredit()
	{
		return credit;
	}

	public String getCurrency()
	{
		return currency;
	}

	public String getLoginname()
	{
		return loginname;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public void setCredit(Double credit)
	{
		this.credit = credit;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public void setLoginname(String loginname)
	{
		this.loginname = loginname;
	}

	@Override
	public String toString()
	{
		return (new StringBuilder("code:")).append(code).append(";loginname:").append(loginname).append(";currency:").append(currency).append(";credit:").append(credit).toString();
	}
}
