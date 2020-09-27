package com.nnti.common.security;

import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESUtil {

	private static final String KEY = "m9tRBmhB";

	public static byte[] desEncrypt(byte[] plainText) throws Exception {

		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(KEY.getBytes());

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);

		return cipher.doFinal(plainText);
	}

	public static byte[] desDecrypt(byte[] encryptText) throws Exception {

		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(KEY.getBytes());

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);

		return cipher.doFinal(encryptText);
	}

	public static String encrypt(String input) throws Exception {

		return base64Encode(desEncrypt(input.getBytes())).replaceAll("\\s*", "");
	}

	public static String decrypt(String input) throws Exception {

		byte[] result = base64Decode(input);

		return new String(desDecrypt(result));
	}

	public static String base64Encode(byte[] s) {

		if (s == null) {

			return null;
		}

		BASE64Encoder b = new sun.misc.BASE64Encoder();

		return b.encode(s);
	}

	public static byte[] base64Decode(String s) throws IOException {

		if (s == null) {

			return null;
		}

		BASE64Decoder decoder = new BASE64Decoder();

		return decoder.decodeBuffer(s);
	}

	public static String decryptSportBookText(String cipherText) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		byte[] keyBytes = new byte[16];

		byte[] b = KEY.getBytes("utf-8");
		int len = b.length;

		if (len > keyBytes.length) {

			len = keyBytes.length;
		}

		System.arraycopy(b, 0, keyBytes, 0, len);

		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		BASE64Decoder decoder = new BASE64Decoder();

		byte[] results = cipher.doFinal(decoder.decodeBuffer(cipherText));

		return new String(results, "UTF-8");
	}

	public static String encryptSportBookText(String text) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		byte[] keyBytes = new byte[16];

		byte[] b = KEY.getBytes("UTF-8");
		int len = b.length;

		if (len > keyBytes.length) {

			len = keyBytes.length;
		}

		System.arraycopy(b, 0, keyBytes, 0, len);

		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

		BASE64Encoder encoder = new BASE64Encoder();

		return encoder.encode(results);
	}
}