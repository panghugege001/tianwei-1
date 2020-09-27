// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringUtil.java

package dfh.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import dfh.security.EncryptionUtil;

public class StringUtil extends StringUtils {
	public static final Pattern P_URL = Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
	private static String[] EXECSTATUS_MSG={"失败","成功","未访问","已访问"};
	
	public static String execStatuc2String(Integer status){
		return EXECSTATUS_MSG[status.intValue()];
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

	public static String subStrBefore(String str ,  int size){
		try {
			if(str.length()<size){
				return str;
			}
			String newstr = str.substring(0, size);
			String substring = str.substring(str.length() - 4);
			return newstr+"****"+substring ;
		} catch (Exception e) {
			return str ;
		}
	}


	
	public static String getRandomNumString(int length) {
		StringBuffer buffer = new StringBuffer("0123456789");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));

		return sb.toString();
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
		System.out.println(getRandomString(8));

	}
	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("0123456789");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));

		return sb.toString();
	}

	//临时方法
	public static String getRandomString_bak(int length) {
		StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
			sb.append(buffer.charAt(r.nextInt(range)));

		return sb.toString();
	}
	
	
	public static String getSplitStirng(String ubanknam,String split,Integer index) {
		return StringUtil.split(ubanknam, split)[index];
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

	public static String listToString(List<String> stringList) {
		
		if (stringList == null || stringList.isEmpty()) {
			
			return "";
		}
		
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		
		for (String string : stringList) {
			
			if (flag) {
				
				result.append(",");
			} else {
				
				flag = true;
			}
			
			result.append(string);
		}
		
		return result.toString();
	}
	
	public static Integer[] strArray2intArray(String...arr){
        Integer[] intArr = new Integer[arr.length];
        for (int i=0; i<arr.length; i++) {
            intArr[i] = Integer.parseInt(arr[i]);
        }
        return intArr;
    }

	public static String getRandomStrExceptOL0(Integer length){
		return RandomStringUtils.random(length, new char[]{'a','b','c','d','e','f' ,'g' ,'h' ,'i' ,'j' ,'k' ,'m' ,'n' ,'p' ,'q' ,'r' ,'s' , 't' ,'u' ,'v' ,'w' ,'x' ,'y','z', '1', '2', '3' ,'4','5','6','7','8','9'}) ;
	}


	public StringUtil() {
	}
	
	/**
	 * 判断str是否为null或者是否为""
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if(str == null || str.equals("")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 判断str是否非空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if(isEmpty(str)) {
			return false;
		}else {
			return true;
		}
	}
}
