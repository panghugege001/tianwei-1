// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringUtil.java

package com.gsmc.png.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {
	public static final Pattern P_URL = Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
	
	public static int count = 0;
	
	/**
	 * 根据unicode提取指定长度的字符
	 * @param src
	 * @param count
	 * @return string
	 */
	public static String subString(String src,int count){
		if (src==null||src.trim().equals("")) {
			return "";
		}
		int i=0;
		for (;i < src.length(); i++) {
			int code=src.codePointAt(i);
			if (code>=0&&code<=128) {
				if (count>0) {
					count--;
				}else{
					break;
				}
			}else{
				if (count>1) {
					count-=2;
				}else{
					break;
				}
			}
		}
		return src.substring(0, i);
	}
	
	

	public static String formatNumberToDigits(Integer integer, Integer digits) {
		Integer length = Integer.valueOf(integer.toString().length());
		if (length.intValue() > digits.intValue())
			return integer.toString().substring(length.intValue() - digits.intValue());
		StringBuffer buffer = new StringBuffer(integer.toString());
		buffer.reverse();
		for (int i = 0; i < digits.intValue() - length.intValue(); i++)
			buffer.append("0");

		return buffer.reverse().toString();
	}

	public static String getRandomCharacter(int length) {
		StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));

		return sb.toString();
	}

	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));

		return sb.toString();
	}
	
	public static String get4RandomString() {
		StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < 4; i++)
			sb.append(buffer.charAt(r.nextInt(range)));
		return sb.toString();
	}

	public static Integer makeRandom(Integer maxValue) {
		Random r = new Random();
		return r.nextInt(maxValue);
	}

	public static String transform(String content) {
		content = content.replaceAll(" ", "&nbsp;");
		content = content.replaceAll("\n", "<br/>");
		return content;
	}

	public static byte[] convertHexStrToByteArray(String srcHex) {
		byte[] retByteArray = new byte[srcHex.length() / 2];
		byte tmpByte;
		String sDecode;
		for (int i = 0; i < srcHex.length() / 2; i++) {
			sDecode = "0x" + srcHex.substring(2 * i, 2 * i + 2);
			tmpByte = (byte) Integer.decode(sDecode).byteValue();
			retByteArray[i] = tmpByte;
		}
		return retByteArray;
	}
	
	public static String convertByteArrayToHexStr(byte[] srcByteArray) {
		String sTemp = null;
		StringBuffer sOutLine = new StringBuffer();
		byte[] inByte = srcByteArray;
		for (int iSerie = 0; iSerie < inByte.length; iSerie++) {
			// System.out.println("inbyte"+iSerie+inByte[iSerie]);
			if (inByte[iSerie] < 0) {
				sTemp = Integer.toHexString(256 + inByte[iSerie]);
			} else {
				sTemp = Integer.toHexString(inByte[iSerie]);
			}
			if (sTemp.length() < 2) {
				sTemp = "0" + sTemp;
			}
			sTemp = sTemp.toUpperCase();
			sOutLine = sOutLine.append(sTemp);
		}
		return sOutLine.toString();
	}

		/**
		 * 
		 * @param bank_code
		 * @return
		 */
	public static boolean isDianKa(String bank_code){
		String [] diankas = {"YDSZX","DXGK","QBCZK","LTYKT","JWYKT","SDYKT","WMYKT","ZTYKT","WYYKT","SFYKT","SHYKT","JYYKT","THYKT","GYYKT","TXYKT","ZYYKT","TXYKTZX"};
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(diankas) ;
		if(list.contains(bank_code)){
			return true ;
		}else {
			return false ;
		}
	}
		
	public static String getRandomStrExceptOL0(Integer length){
		return RandomStringUtils.random(length, new char[]{'a','b','c','d','e','f' ,'g' ,'h' ,'i' ,'j' ,'k' ,'m' ,'n' ,'p' ,'q' ,'r' ,'s' , 't' ,'u' ,'v' ,'w' ,'x' ,'y','z', '1', '2', '3' ,'4','5','6','7','8','9'}) ;
	}
	
	/**
	 * 手机号码验证
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public static void main(String args[]) {
		System.out.println(StringUtil.getRandomStrExceptOL0(5));

	}

	public StringUtil() {
	}
}
