package dfh.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {
	
	public static final String url = "http://mfb-jiekou02.vip:9081/";
	public static final String apikey = "e8807ff1b1d44713a7beeb8822e0e802";
	public static final String secretKey = "8rpj8eqiwslzryu7qx14bqcdj6j9cp393htd6ytzu17bxy44o290ldtiec02vh57";
	
	

	// 签名
	public static String sign(String str, String type) {

		return encrypt(str, type);
	}

	private static String encrypt(String strSrc, String encName) {

		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes(StandardCharsets.UTF_8);

		try {

			md = MessageDigest.getInstance(encName);
			md.update(bt);

			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
			return null;
		}

		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {

		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {

			tmp = (Integer.toHexString(bts[i] & 0xFF));

			if (tmp.length() == 1) {

				des += "0";
			}

			des += tmp;
		}

		return des;
	}
}