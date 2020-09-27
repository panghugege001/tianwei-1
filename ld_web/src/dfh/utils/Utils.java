package dfh.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import dfh.security.SpecialEnvironmentStringPBEConfig;

public class Utils {
	/**
	 * 从ip的字符串形式得到字节数组形式
	 * author:sun
	 * @param ip
	 *  字符串形式的ip
	 * @return 字节数组形式的ip
	 */
	public static byte[] getIpByteArrayFromString(String ip) {
		byte[] ret = new byte[4];
		java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
		try {
			ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	public static void main(String args[]) {
		byte[] a = getIpByteArrayFromString(args[0]);
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);
		System.out.println(getIpStringFromBytes(a));
	}

	/**
	 * 对原始字符串进行编码转换，如果失败，返回原始的字符串
	 * 
	 * @param s
	 *            原始字符串
	 * @param srcEncoding
	 *            源编码方式
	 * @param destEncoding
	 *            目标编码方式
	 * @return 转换编码后的字符串，失败返回原始字符串
	 */
	public static String getString(String s, String srcEncoding,
			String destEncoding) {
		try {
			return new String(s.getBytes(srcEncoding), destEncoding);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
	/**
	 * 根据某种编码方式将字节数组转换成字符串
	 * 
	 * @param b
	 *            字节数组
	 * @param encoding
	 *            编码方式
	 * @return 如果encoding不支持，返回一个缺省编码的字符串
	 */
	public static String getString(byte[] b, String encoding) {
		try {
			return new String(b, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(b);
		}
	}
	/**
	 * 根据某种编码方式将字节数组转换成字符串
	 * 
	 * @param b
	 *            字节数组
	 * @param offset
	 *            要转换的起始位置
	 * @param len
	 *            要转换的长度
	 * @param encoding
	 *            编码方式
	 * @return 如果encoding不支持，返回一个缺省编码的字符串
	 */
	public static String getString(byte[] b, int offset, int len,
			String encoding) {
		try {
			return new String(b, offset, len, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(b, offset, len);
		}
	}

	/**
	 * @param ip
	 * ip的字节数组形式
	 * @return 字符串形式的ip
	 */
	public static String getIpStringFromBytes(byte[] ip) {
		StringBuffer sb = new StringBuffer();
		sb.append(ip[0] & 0xFF);
		sb.append('.');
		sb.append(ip[1] & 0xFF);
		sb.append('.');
		sb.append(ip[2] & 0xFF);
		sb.append('.');
		sb.append(ip[3] & 0xFF);
		return sb.toString();
	}
	
	public static synchronized String string2crc(String s) throws UnsupportedEncodingException{
		CRC32 crc=new CRC32();
		s+=SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("CRC_KEY"));
		crc.update(s.getBytes("gbk"));
		return Long.toHexString(crc.getValue());
	}
	
	public static void write(String content) {
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("text/html; charset=utf-8");
            response.setHeader("Cache-Control", "no-cache"); // 取消浏览器缓存
            PrintWriter out;
            out = response.getWriter();
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	/**
	 * 获取IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr() {  
		HttpServletRequest request = ServletActionContext.getRequest();
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }
}
