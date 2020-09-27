package dfh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * 配置文件
 * 
 * @author 
 * 
 */
public class Yom {

	/**
	 * 生成随机数字
	 * 
	 * @param num
	 * @return
	 */
	public static String random(int num) {
		String ret = "";
		for (int i = 0; i < num; i++) {
			int randomInt = (int) ((java.lang.Math.random()) * 10);
			ret = ret.concat(Integer.toString(randomInt));
		}
		return ret;
	}

	/**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5(String source) {
		String mdString = null;
		if (source != null) {
			try {
				// 关键是这句
				mdString = getMD5(source.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return mdString.toLowerCase();
	}

	public static String getMD5(byte[] source) {
		String s = null;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		final int temp = 0xf;
		final int arraySize = 32;
		final int strLen = 16;
		final int offset = 4;
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte[] tmp = md.digest();
			char[] str = new char[arraySize];
			int k = 0;
			for (int i = 0; i < strLen; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> offset & temp];
				str[k++] = hexDigits[byte0 & temp];
			}
			s = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}


}
