package dfh.utils.mwgsign;

/*
 --------------------------------------------**********--------------------------------------------

 该�?��?��??1977年由美国麻�?��?�工学院MIT(Massachusetts Institute of Technology)??�Ronal Rivest，Adi Shamir??�Len Adleman三�?�年轻�?��?��?�出，并以�?�人??��?��?�Rivest，Shamir??�Adlernan?��??�为RSA算�?��?�是�?个支??��?�长密钥??�公?��密钥算�?��?��?要�?��?��?��?�件快�?�长度�?�是?��??��??!

 ??谓RSA??��?��?��?��?�是世�?��?�第�?个�?�对称�?��?��?��?��?��?�是?��论�?�第�?个�?��?��?�用?��?��?��?��?��?��?��??

 1.?��两个??�常大�?�质?��p??�q（�?�常p??�q?��???155??��?�制位�?�都???512??��?�制位�?�并计�?�n=pq，k=(p-1)(q-1)??

 2.将�?��?��?��?��?�整?��M，�?��?�M不�?��??0但是小�?�n??

 3.任�?��?个整?��e，�?��?�e??�k互质，�?��?�e不�?��??0但是小�?�k?��?��?�钥??��?�称作公?��）是(e, n)??

 4.?��?���?个整?��d，使得ed?��以k??��?�数?��1（只要e??�n满足上面?��件�?�d?��定�?�在）�?�解密钥??��?�称作�?�钥）是(d, n)??

 ??��?��?��?��?? ??��?��?��?��?��?�C等�?�M??�e次方?��以n??得�?��?�数??

 �?密�?��?��?? �?密�?��?��?��?�N等�?�C??�d次方?��以n??得�?��?�数??

 ?��要e?�d??�n满足上面给�?��?�条件�?�M等�?�N??

 --------------------------------------------**********--------------------------------------------
 */
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
	
public class RSA {
	/** ??��?�key??�大�? */
	private static int KEYSIZE = 1024;

	/**
	 * ??��?��?�钥�?
	 */
	public static Map<String, String> generateKeyPair() throws Exception {
		/** RSA算�?��?��?��?��?个可信任??��?�机?���? */
		SecureRandom sr = new SecureRandom();
		/** 为RSA算�?��?�建�?个KeyPairGenerator对象 */
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		/** ?��?��上面??��?�机?��?��源�?��?��?��?�个KeyPairGenerator对象 */
		kpg.initialize(KEYSIZE, sr);
		/** ??��?��?��?�对 */
		KeyPair kp = kpg.generateKeyPair();
		/** 得到?��?�� */
		Key publicKey = kp.getPublic();
		byte[] publicKeyBytes = publicKey.getEncoded();
		String pub = new String(Base64.encodeBase64(publicKeyBytes),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);
		/** 得到私钥 */
		Key privateKey = kp.getPrivate();
		byte[] privateKeyBytes = privateKey.getEncoded();
		String pri = new String(Base64.encodeBase64(privateKeyBytes),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);

		Map<String, String> map = new HashMap<String, String>();
		map.put("publicKey", pub);
		map.put("privateKey", pri);
		RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
		BigInteger bint = rsp.getModulus();
		byte[] b = bint.toByteArray();
		byte[] deBase64Value = Base64.encodeBase64(b);
		String retValue = new String(deBase64Value);
		map.put("modulus", retValue);
		return map;
	}

	/**
	 * ??��?�方�? source�? 源数?��
	 */
	public static String encrypt(String source, String publicKey)
			throws Exception {
		Key key = getPublicKey(publicKey);
		/** 得到Cipher对象?��实现对�?�数?��??�RSA??��?? */
		Cipher cipher = Cipher
				.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] b = source.getBytes();
		/** ?��行�?��?��?��?? */
		byte[] b1 = cipher.doFinal(b);
		return new String(Base64.encodeBase64(b1),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);
	}

	/**
	 * �?密�?��?? cryptograph:密�??
	 */
	public static String decrypt(String cryptograph, String privateKey)
			throws Exception {
		Key key = getPrivateKey(privateKey);
		/** 得到Cipher对象对已?��?��?��??��?��?�数?��进�?�RSA�?�? */
		Cipher cipher = Cipher
				.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
		/** ?��行解密�?��?? */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	public static void main(String[] args) throws Exception {
		String aesKey = "aes key";
		String privateKey = "private key";
		String publicKey = "public key";

		String sign = encrypt(aesKey, publicKey);
		System.out.println("sign : " + sign);

		String result = decrypt(sign, privateKey);
		System.out.println("result : " + result);
	}

	/**
	 * 得到?��?��
	 *
	 * @param key
	 *            密钥字符串�?��?��?�base64编�?��??
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 *
	 * @param key
	 *            密钥字符串�?��?��?�base64编�?��??
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	public static String sign(String content, String privateKey) {
		String charset = ConfigureEncryptAndDecrypt.CHAR_ENCODING;
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decodeBase64(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			Signature signature = Signature.getInstance("SHA1WithRSA");

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {
			System.out.println(" sing exception : "+ e.toString());
		}

		return null;
	}

	public static boolean checkSign(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode2(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));
			boolean bverify = signature.verify(Base64.decode2(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
