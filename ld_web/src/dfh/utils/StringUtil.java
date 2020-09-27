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
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import dfh.security.EncryptionUtil;

public class StringUtil extends StringUtils {
	public static final Pattern P_URL = Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
	
	//显示后几位
		public static String subStrLast(String str ,  int size){
			try {
				if(str.length()<size){
					return str;
				}
				String newstr = str.substring(str.length()-size, str.length());
				return newstr ;
			} catch (Exception e) {
				return str ;
			}
		}
		
		public static String subStrBefore(String str ,  int size){
			try {
				if(str.length()<size){
					return str;
				}
				String newstr = str.substring(0, size);
				return newstr ;
			} catch (Exception e) {
				return str ;
			}
		}
	/**
	 * 6位纯数字
	 * @param pwd
	 * @return
	 */
	public static boolean regPayPwd(String pwd){
		Pattern p_pwd = Pattern.compile("^\\d{6}$", Pattern.CASE_INSENSITIVE);
		if(p_pwd.matcher(pwd).matches()){
			return true ;
		}else{
			return false ;
		}
	}
		
	/**
	 * 6-16位数字或英文字母 英文字母开头
	 * @param pwd
	 * @return
	 */
	public static boolean regPwd(String pwd){
		Pattern p_pwd = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{5,15}", Pattern.CASE_INSENSITIVE);
		if(p_pwd.matcher(pwd).matches()){
			return true ;
		}else{
			return false ;
		}
	}
	
	//判断是否为大于0 的数字
	public static String getMoneyfromStr(String result){
		if(result.matches("[\\d]+[.]?[\\d]+")){
			return result+"元";
		}else{
			return result ;
		}
	}
	
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

	public static void main(String args[]) {
//		System.out.println(isAvaliableBankCard(null));
		System.out.println(regPwd("A237211234567896"));

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
		String [] diankas = {"YDSZX","DXGK","LTYKT","QBCZK","JWYKT","SDYKT","WMYKT","ZTYKT","WYYKT","SFYKT","SHYKT","JYYKT","THYKT","TXYKT","ZYYKT","TXYKTZX"};//,"GYYKT" ,"LTYKT"
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(diankas) ;
		if(list.contains(bank_code)){
			return true ;
		}else {
			return false ;
		}
	}
	
	
	public static boolean isAvaliableBankCard(String bank_code){
		String [] diankas = {"YDSZX","DXGK","QBCZK","LTYKT","JWYKT","SDYKT","WMYKT","ZTYKT","WYYKT","SFYKT","SHYKT","JYYKT","THYKT","TXYKT","ZYYKT","TXYKTZX" ,"SPDB","HXB","ECITIC","SPABANK","PSBC","CIB","CEBB","CMBC","CMB","BOC","BCOM","CCB","ICBC","ABC","ZF_WX"};
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(diankas) ;
		if(list.contains(bank_code)){
			return true ;
		}else {
			return false ;
		}
	}
	
	public static String getConfigValue(String key) {
		try {
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			return properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static boolean isContain(String str , String[] args){
		for (String string : args) {
			if(string.equals(str)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 按比例添加*
	 * @param str 字串来源
	 * @param ratio 星号比例
	 * @param type 0:前面 1:后面
	 * @return
	 */
	public static String formatStar(String str,Double ratio,int type){
		if(StringUtils.isBlank(str)||str.length()==0){
			return str;
		}
		int size = (int)(str.length()*ratio);
		
		if(type==0){
			return String.format("%"+str.length()+"s", str.substring(size, str.length())).replaceAll(" ", "*");
		}else{
			return String.format("%-"+str.length()+"s", str.substring(0,str.length()-size)).replaceAll(" ", "*");
		}
	}
	
	public StringUtil() {
	}
	
	/**
	 * 联系电话脱敏,联系电话隐藏中间4位
	 * @param num
	 * @return
	 */
	public static String mobilePhoneFormat(String num) {
		if (StringUtils.isBlank(num)) {
			return "";
		}
		if (num.length() >= 7) {
			return num.substring(0, 3) + "****" + num.substring(7, num.length());
		} else if (num.length() > 3) {
			return StringUtils.rightPad(StringUtils.left(num, 3), StringUtils.length(num), "*");
		} else {
			return num;
		}
	}

	/**
	 * 电子邮箱脱敏,电子邮箱隐藏@前面3位
	 * @param email
	 * @return
	 */
	public static String emailFormat(String email) {
		if (StringUtils.isBlank(email)) {
			return "";
		}
		int index = StringUtils.lastIndexOf(email, "@");
		if (index <= 0) {
			return email;
		} else {
			return StringUtils.rightPad(StringUtils.left(email, index - 3), index, "*").concat(
					StringUtils.mid(email, index, StringUtils.length(email)));
		}
	}

	/**
	 * QQ号码脱敏,隐藏中间三位
	 * @param num
	 * @return
	 */
	public static String qqFormat(String num) {
		if (StringUtils.isBlank(num)) {
			return "";
		}
		if (num.length() > 6) {
			return num.substring(0, 3) + "***" + num.substring(6, num.length());
		} else if (num.length() > 3) {
			return StringUtils.rightPad(StringUtils.left(num, 3), StringUtils.length(num), "*");
		} else {
			return num;
		}
	}
	
	public static  boolean flag(String text){
		if(!text.contains("废弃")){
			return true;
		}
		return false;
	}
}
