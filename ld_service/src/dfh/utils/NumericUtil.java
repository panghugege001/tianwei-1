// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NumericUtil.java

package dfh.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumericUtil
{

	private static NumberFormat nf;
	private static DecimalFormat df=new DecimalFormat("##,##0.00",DecimalFormatSymbols.getInstance(Locale.CHINA));

	static 
	{
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);
	}
	public static String double2String(double dl){
		return df.format(dl);
	}

	public static String formatDouble(Double number)
	{
		return nf.format(number);
	}
	
	public static Double toDouble(Double n)
	{
		return n!=null?n.doubleValue():0.0;
	}

	public static void main(String args[])
	{
		System.out.println(toDouble(null));
	}

	public NumericUtil()
	{
	}
}
