package com.nnti.common.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES是一种可逆加密算法，对用户的敏感信息加密处理，对原始数据进行AES加密后，在进行Base64编码转化
 */
public class AESUtil {

	private static Logger log = Logger.getLogger(AESUtil.class);

	// 加密使用的Key值，可以用26个字母和数字组成，此处使用AES-128-CBC加密模式，key需要为16位，key值可自行修改
	public static final String KEY = "smkldospdosldaaa";
	// 加密偏移量，可自行修改
	private static final String OFFSET = "0000000000000000";

	private AESUtil() {

	}

	// 加密
	public static String encrypt(String src) {

		return encrypt(src, KEY);
	}

	// 加密
	public static String encrypt(String src, String key) {

		return encrypt(src, key, OFFSET);
	}

	public static String encrypt(String src, String key, String vector) {

		String str = null;

		try {

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			byte[] raw = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

			// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] dataBytes = src.getBytes("utf-8");
			byte[] encrypted = cipher.doFinal(dataBytes);

			str = new String(Base64.encodeBase64(encrypted));
		} catch (Exception e) {

			log.error("AESUtil执行encrypt方法发生异常，异常信息：" + e.getMessage());
		}

		return str;
	}

	// 解密
	public static String decrypt(String src) {

		return decrypt(src, KEY);
	}

	// 解密
	public static String decrypt(String src, String key) {

		return decrypt(src, key, OFFSET);
	}

	public static String decrypt(String src, String key, String vector) {

		String str = null;

		try {

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

			IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(src);
			byte[] original = cipher.doFinal(encrypted1);

			str = new String(original, "utf-8");
		} catch (Exception e) {

			log.error("AESUtil执行decrypt方法发生异常，异常信息：" + e.getMessage());
		}

		return str;
	}

	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {

		if (StringUtils.isBlank(encryptStr) || encryptStr.contains("null")) {

			return encryptStr;
		}

		return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
	}

	public static byte[] base64Decode(String base64Code) throws Exception {

		return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
	}

	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {

		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(decryptKey.getBytes());

		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, secureRandom);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes);
	}

	public static void main(String[] args) throws Exception {

		System.out.println(decrypt("m9z5jxO70v0OD1j9pXXMO4MaPK0Xho2EGiYEMrHnyUCe5/9zD/MNTg1VeTHcHMND"));
	}
}