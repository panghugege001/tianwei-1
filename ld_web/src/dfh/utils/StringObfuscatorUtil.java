// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringObfuscatorUtil.java

package dfh.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class StringObfuscatorUtil
{

	private static Logger logger = Logger.getLogger(StringObfuscatorUtil.class);

	public static String format(String originalString, Character delimiter, int groupLen)
	{
		if (originalString == null || delimiter == null)
		{
			logger.debug("Incomplete parameter, can not format a null string with a null delimiter.");
			return null;
		}
		int counter = 0;
		int len = originalString.length();
		StringBuffer newStr = new StringBuffer("");
		while (counter <= len) 
		{
			int beginIndex = counter;
			counter += groupLen;
			if (counter >= len)
			{
				newStr.append(originalString.substring(beginIndex, len));
			} else
			{
				newStr.append(originalString.subSequence(beginIndex, counter));
				newStr.append(delimiter);
			}
		}
		return newStr.toString();
	}

	public static String obfuscate(String originalString, Character mask, int displayLen)
	{
		if (originalString == null || mask == null)
		{
			logger.debug("Incomplete parameter, can not obfuscate a null string using a null mask.");
			return null;
		} else
		{
			int len = originalString.length();
			int displayedPart = len - displayLen;
			displayedPart = displayedPart < 0 ? 0 : displayedPart;
			String subStr = originalString.substring(displayedPart, len);
			return StringUtils.leftPad(subStr, len, mask.charValue());
		}
	}

	public static String removeWhiteSpace(String originalString)
	{
		return originalString.replaceAll(" ", "");
	}

	public StringObfuscatorUtil()
	{
	}

}
