package com.gsmc.png.security;

import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;

public class SpecialEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig {
	
	@Override
	public void setAlgorithm(String algorithm) {
		super.setAlgorithm("PBEWithMD5AndDES");
	}
	
	@Override
	public void setPassword(String password) {
		super.setPassword("ComoneBaby");
	}

	public SpecialEnvironmentStringPBEConfig() {
	}


	public static void main(String[] args) {
		//加密
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("7ttsR51lZDQB4lT");
		String newPassword = textEncryptor.encrypt("admin");
		System.out.println(newPassword);
		//解密
//		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
//		textEncryptor2.setPassword("nje3ot");
//		String oldPassword = textEncryptor2.decrypt(newPassword);
//		System.out.println(oldPassword);
	}

}
