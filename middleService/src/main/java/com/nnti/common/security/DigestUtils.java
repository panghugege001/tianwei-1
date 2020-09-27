package com.nnti.common.security;


import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.utils.Assert;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class DigestUtils {
    private static Logger logger = Logger.getLogger(DigestUtils.class);
    private static final String MD5_ALGORITHM_NAME = "MD5";

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    
	/**
	 * 参数排序后加密
	 * 
	 * @param paramMap 无序的map
	 *            签名参数
	 * @param paySecret
	 *            签名密钥
	 * @return 密文
	 * @throws BusinessException 
	 */
	public static String getSortedSign(Map<String, Object> paramMap, String paySecret) throws Exception {
		SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, Object> m : smap.entrySet()) {
			Object value = m.getValue();
			Assert.notEmpty(String.valueOf(value));
			if(paySecret.startsWith("&Key=")){
				value = URLEncoder.encode(value.toString(), "UTF-8");
			}
			stringBuffer.append(m.getKey()).append("=").append(value).append("&");
		}
		stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

		String argPreSign = stringBuffer.append(paySecret).toString();
		String signStr = signByMD5(argPreSign).toUpperCase();
		return signStr;
	}
    
    public static String signByMD5(String sourceData) throws BusinessException {
        Assert.notEmpty(sourceData);
        return toHex(md5Digest((sourceData).getBytes()));
    }

    public static String signByMD5(String sourceData, String key) throws BusinessException {
        Assert.notEmpty(sourceData, key);
        return toHex(md5Digest((sourceData + key).getBytes()));
    }
    
    //addis
    public static String signByMD5Byaddis(String sourceData, String key) throws BusinessException {
        Assert.notEmpty(sourceData, key);
        return toHex(md5Digest((sourceData + "&key=" + key).getBytes()));
    }
    
    public static String  md5(String str){
    	try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;

            //字符数组转换成字符串
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {   
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString().toUpperCase();
            // 16位的加密
             //return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    

    public static byte[] md5Digest(byte[] bytes) {
        return digest(MD5_ALGORITHM_NAME, bytes);
    }

    public static String md5DigestAsHex(byte[] bytes) {
        return digestAsHexString(MD5_ALGORITHM_NAME, bytes);
    }

    public static String md5DigestAsHex(String sourceData, String key) throws BusinessException {
        return md5DigestAsHex(sourceData, key, "UTF-8");
    }

    public static String md5DigestAsHex(String sourceData, String key, String code) throws BusinessException {
        try {
            return digestAsHexString(MD5_ALGORITHM_NAME, (sourceData + key).getBytes(code));
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(ErrorCode.SC_10001.getCode(), "转码失败");
        }
    }

    public static StringBuilder appendMd5DigestAsHex(byte[] bytes, StringBuilder builder) {
        return appendDigestAsHex(MD5_ALGORITHM_NAME, bytes, builder);
    }

    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }
    }

    private static byte[] digest(String algorithm, byte[] bytes) {
        return getDigest(algorithm).digest(bytes);
    }

    private static String digestAsHexString(String algorithm, byte[] bytes) {
        char[] hexDigest = digestAsHexChars(algorithm, bytes);
        return new String(hexDigest);
    }

    private static StringBuilder appendDigestAsHex(String algorithm, byte[] bytes, StringBuilder builder) {
        char[] hexDigest = digestAsHexChars(algorithm, bytes);
        return builder.append(hexDigest);
    }

    private static char[] digestAsHexChars(String algorithm, byte[] bytes) {
        byte[] digest = digest(algorithm, bytes);
        return encodeHex(digest);
    }

    private static char[] encodeHex(byte[] bytes) {
        char chars[] = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }

    public static String hmacSign(String aValue, String aKey) {
        byte[] k_ipad = new byte[64];
        byte[] k_opad = new byte[64];
        byte[] keyb;
        byte[] value;
        try {
            keyb = aKey.getBytes("UTF-8");
            value = aValue.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5C);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte[] dg = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return toHex(dg);
    }

    public static boolean hmacVerify(String aValue, String aKey, String aHmac) {
        return hmacSign(aValue, aKey).equals(aHmac);
    }

    private static String toHex(byte[] input) {
        if (input == null) return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xFF;
            if (current < 16) output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }

    public static String decodeByBase64(String code) throws BusinessException {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(code));
        } catch (IOException e) {
            logger.error("BASE64解密异常：", e);
            throw new BusinessException(ErrorCode.SC_10001.getCode(), "BASE64解密异常");
        }
    }
    
    public static void main(String[] args) throws Exception {
    	//sign=md5($mer_order_no.$merchant_no.$notify_url.$order_amount.$pay_type.$product_name.$remark.$return_url.$key).toUpperCase();
    	String ss = "1304304R20190119231144http://127.0.0.1:10000/paycenter/MFPayH5Notify5002003购买金币http://127.0.0.1:10000/paycenter/MFPayH5Returne1ebb8ae21db4c93861e264b94a43504";
    	String ss1 = "123456789";
    	String ss5 = "xn1234";
    	System.out.println(md5(ss1));
    	System.out.println(md5(ss5));
//    	String md5 = md5(ss);
//    	System.out.println(md5.toUpperCase());
    }
}
