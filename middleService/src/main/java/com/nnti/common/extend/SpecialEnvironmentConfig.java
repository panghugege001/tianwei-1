package com.nnti.common.extend;

import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;

public class SpecialEnvironmentConfig extends EnvironmentStringPBEConfig {

	private static final String PASSWORD_KEY = "ru0ydozT2B5xXxX";

     
	public void setAlgorithm(String algorithm) {

		super.setAlgorithm("PBEWithMD5AndDES");
	}

	public void setPassword(String password) {

		super.setPassword("ComoneBaby"); 
	}            

	public SpecialEnvironmentConfig() {
	}
	public static String getPlainText(String encryptUrl){
		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
		textEncryptor2.setPassword("YS9cZeiEeWK2a0I");
		return textEncryptor2.decrypt(encryptUrl); 
	}

	public static void main(String[] args) {
		//加密
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("YS9cZeiEeWK2a0I");
//		String newPassword = textEncryptor.encrypt("http://69.172.80.19:6066/services/");
		String newPassword = textEncryptor.encrypt("jdbc:mysql://127.0.0.1:3306/tianwei?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");
		System.out.println(newPassword);
		
		String newPassword1 = textEncryptor.encrypt("mysql");
		System.out.println(newPassword1);
		
		String newPassword2 = textEncryptor.encrypt("com.mysql.jdbc.Driver");
		System.out.println(newPassword2);
		
		String newPassword3 = textEncryptor.encrypt("root");
		System.out.println(newPassword3);
		//解密
		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
		textEncryptor2.setPassword("YS9cZeiEeWK2a0I");
		String oldPassword = textEncryptor2.decrypt("xSWHcx/u1wWpVYok1cdMLg==");
		System.out.println(textEncryptor2.decrypt("brE2xMoWlSkmwAyp1nE8mpY8vbJqZvrbXXRe6ffdLNQ="));
		System.out.println(oldPassword);
	}
	
}