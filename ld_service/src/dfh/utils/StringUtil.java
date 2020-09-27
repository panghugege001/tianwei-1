// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringUtil.java

package dfh.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import dfh.security.EncryptionUtil;

public class StringUtil extends StringUtils {
	public static final Pattern P_URL = Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
	
	
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

	public static String subStrings(String s) {
		return s.substring(0,3)+"****";
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
	/**
	 * 加密url
	 * 
	 * @param url
	 * @return
	 */
	public static String urlEncode(String url) {
		try {
			return URLEncoder.encode(EncryptionUtil.encryptBASE64(url.getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 解密url
	 * 
	 * @param url
	 * @return
	 */
	public static String urlDecode(String key) {
		String url = URLDecoder.decode(key);
		try {
			return new String(EncryptionUtil.decryptBASE64(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	
		public static byte[] convertHexStrToByteArray(String srcHex)
		{
			byte[] retByteArray = new byte[srcHex.length() / 2];
			byte tmpByte;
			String sDecode;
			for (int i = 0; i < srcHex.length() / 2; i++)
			{
				sDecode = "0x" + srcHex.substring(2 * i, 2 * i + 2);
				tmpByte = (byte)Integer.decode(sDecode).byteValue();
				retByteArray[i] = tmpByte;
			}
			return retByteArray;
		}
	   
	 
	
		public static String convertByteArrayToHexStr(byte[] srcByteArray)
		{
			String sTemp = null;
			StringBuffer sOutLine = new StringBuffer();
			byte[] inByte = srcByteArray;
			for (int iSerie = 0; iSerie < inByte.length; iSerie++)
			{
				// System.out.println("inbyte"+iSerie+inByte[iSerie]);
				if (inByte[iSerie] < 0)
				{
					sTemp = Integer.toHexString(256 + inByte[iSerie]);
				}
				else
				{
					sTemp = Integer.toHexString(inByte[iSerie]);
				}
				if (sTemp.length() < 2)
				{
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
	public static String bankcardsub(String num) {
		if (org.apache.commons.lang3.StringUtils.isBlank(num)) {
			return "";
		}
			return num.substring(0, 3) + "********" + num.substring(num.length()-4, num.length());

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

	//等级转换
	public static String getLevel(String str){
		//因为等级问题这里修改用户等级为字母表示
		String level = "";
		if(str.equals("0")){
			level = "A";
		}else if(str.equals("1")){
			level = "B";
		}else if(str.equals("2")){
			level = "C";
		}else if(str.equals("3")){
			level = "D";
		}else if(str.equals("4")){
			level = "E";
		}else if(str.equals("5")){
			level = "F";
		}else if(str.equals("6")){
			level = "G";
		}else if(str.equals("7")){
			level = "H";
		}else if(str.equals("8")){
			level = "I";
		}else if(str.equals("9")){
			level = "J";
		}else if(str.equals("10")){
			level = "K";
		}else if(str.equals("11")){
			level = "L";
		}else if(str.equals("12")){
			level = "M";
		}
		return level;
	}
}
