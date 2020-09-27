package com.nnti.common.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils{

	public static String getRandomString(int length) {

		String buffer = "0123456789abcdefghijklmnopqrstuvwxyz";
		int range = buffer.length();

		StringBuffer sb = new StringBuffer();

		Random r = new Random();

		for (int i = 0; i < length; i++) {

			sb.append(buffer.charAt(r.nextInt(range)));
		}

		return sb.toString();
	}

	public static boolean isContain(String str, String[] args) {

		for (String string : args) {

			if (string.equals(str)) {

				return true;
			}
		}

		return false;
	}

	public static String match(String pattern, String str) {

		String result = "";

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);

		while (m.find()) {

			result = m.group(1);
		}

		return result;
	}
}