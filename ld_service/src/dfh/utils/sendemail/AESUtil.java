package dfh.utils.sendemail;


import java.math.BigInteger;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 编码工具类
 * 1.将byte[]转为各种进制的字符串
 * 2.base 64 encode
 * 3.base 64 decode
 * 4.获取byte[]的md5值
 * 5.获取字符串md5值
 * 6.结合base64实现md5加密
 * 7.AES加密
 * 8.AES加密为base 64 code
 * 9.AES解密
 * 10.将base 64 code AES解密
 * @author uikoo9
 * @version 0.0.7.20140601
 */
public class AESUtil {
	
	public static final String KEY = "AUHS&aSs9a89KJ121" ;
	
	private static final String PAY_CENTER_KEY = "smkldospdosldaaa";

    private static final String PAY_CENTER_OFFSET = "0000000000000000";
    
	
	/**
	 * 将byte[]转为各种进制的字符串
	 * @param bytes byte[]
	 * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix){
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}
	
	/**
	 * base 64 encode
	 * @param bytes 待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(byte[] bytes){
		return new BASE64Encoder().encode(bytes);
	}
	
	/**
	 * base 64 decode
	 * @param base64Code 待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception{
		return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
	}
	
	 /*** 解密 **/
    public static String decrypt(String sSrc) throws Exception {

        String originalString = null;

        byte[] raw = PAY_CENTER_KEY.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        IvParameterSpec iv = new IvParameterSpec(PAY_CENTER_OFFSET.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        originalString = new String(original, "utf-8");

        return originalString;
    }
	
	 /*** 加密 */
    public static String encrypt(String src) throws Exception {

        String encrypt = null;

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] raw = PAY_CENTER_KEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(PAY_CENTER_OFFSET.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] dataBytes = src.getBytes("utf-8");
        byte[] encrypted = cipher.doFinal(dataBytes);
        encrypt = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encrypted));

        return encrypt;
    }
    
    
	
	
	/**
	 * AES加密
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
		secureRandom.setSeed(encryptKey.getBytes());
		kgen.init(128, secureRandom);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		
		return cipher.doFinal(content.getBytes("utf-8"));
	}
	
	/**
	 * AES加密为base 64 code
	 * @param content 待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		if(StringUtils.isBlank(content) || content.contains("null")){
			return content ;
		}
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}
	
	/**
	 * AES解密
	 * @param encryptBytes 待解密的byte[]
	 * @param decryptKey 解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
		secureRandom.setSeed(decryptKey.getBytes());
		kgen.init(128, secureRandom);
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		
		return new String(decryptBytes);
	}
	
	/**
	 * 将base 64 code AES解密
	 * @param encryptStr 待解密的base 64 code
	 * @param decryptKey 解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
		if(StringUtils.isBlank(encryptStr) || encryptStr.contains("null")){
			return encryptStr ;
		}
		return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
	}
	
	public static void main(String[] args) throws Exception {
		String content = "15231759203";
		
		String encrypt = aesEncrypt(content, KEY);
		System.out.println("加密后：" + encrypt);
		
		String decrypt = aesDecrypt("7QKps6RytMYmRvxp6FsF+A==", KEY);
		System.out.println("解密后：" + decrypt);
		System.out.println("\n");
	}
}
