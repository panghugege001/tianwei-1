package com.nnti.common.security;

import org.apache.commons.codec.binary.Base64;


public class Base64Util {
	
	public static String encode(String source) {
		return new String(Base64.encodeBase64(source.getBytes()));
	}
	
	public static String decode(String encodedString) {
		return new String(Base64.decodeBase64(encodedString));
	}
	
}
