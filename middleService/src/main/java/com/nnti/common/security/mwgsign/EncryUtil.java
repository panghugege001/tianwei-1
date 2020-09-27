package com.nnti.common.security.mwgsign;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import java.lang.Exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class EncryUtil {
	private static transient Logger logger = LoggerFactory.getLogger(EncryUtil.class);

	/**
	 * 对�?��?��?��?��?��?��?��?�签
	 * @param data 业务?��?��密�??
	 * @param key ??��?��?��?��?��??
	 * @param merchantPublicKey ?��?��平台??�公?��
	 * @param privateKey 私钥
	 * @return 验签?��?��?��??
	 * @throws Exception
	 */
	public static TreeMap<String, String> checkSignature(String data, String key, String merchantPublicKey, String privateKey)
	throws Exception {
		// 使用平台私钥�?密�?�获得AES key
		String AESKey = "";
		try {
			AESKey = RSA.decrypt(key, privateKey);
		} catch (Exception e) {
			// AES密钥�?密失�?
			logger.error("rsa descrypt failed to get aes key...", e);
			throw new Exception("签�?�失�?");
		}

		// AES�?密获得�?��?�data?��?��
		String realData = AES.decryptFromBase64(data, AESKey);
		TreeMap<String, String> map = JSON.parseObject(realData,
				new TypeReference<TreeMap<String, String>>() {});
		// 从�?��?�中?��得�?��?�sign
		String sign = StringUtils.trimToEmpty(map.get("sign"));

		// ?��??��?��?��?�数?��
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		StringBuffer oridinalData = new StringBuffer();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String keyInfo = (String) entry.getKey();
			String valueInfo = entry.getValue() == null ? "" : entry.getValue();
			if (StringUtils.equals(keyInfo, "sign")) {
				continue;
			}

			oridinalData.append(keyInfo);
			oridinalData.append("=");
			oridinalData.append(valueInfo);
		}

		// RSA验签
		boolean result = RSA.checkSign(oridinalData.toString(), sign, merchantPublicKey);
		if (!result) {
			logger.error("check rsa signature failed to pass.");
			throw new Exception("签�?�失�?");
		}
		return map;
	}




	/**
	 * ??��?�RSA签�??
	 * @param map
	 * @param privateKey
	 * @return
	 */
	public static String handleRSA(TreeMap<String, Object> map, String privateKey) {
		StringBuffer sbuffer = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sbuffer.append(entry.getValue());
		}
		String signTemp = sbuffer.toString();

		String sign = "";
		if (StringUtils.isNotEmpty(privateKey)) {
			sign = RSA.sign(signTemp, privateKey);
		}
		return sign;
	}



	/**
	 * ??��?�hmac
	 */
	public static String handleHmac(TreeMap<String, String> map, String hmacKey) {
		StringBuffer sbuffer = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sbuffer.append(entry.getValue());
		}
		String hmacTemp = sbuffer.toString();

		String hmac = "";
		if (StringUtils.isNotEmpty(hmacKey)) {
			hmac = Digest.hmacSHASign(hmacTemp, hmacKey, Digest.ENCODE);
		}
		return hmac;
	}
}
