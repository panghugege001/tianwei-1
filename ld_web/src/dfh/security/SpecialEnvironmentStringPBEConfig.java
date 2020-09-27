package dfh.security;

import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;

public class SpecialEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig {
	
	@Override
	public void setAlgorithm(String algorithm) {
		super.setAlgorithm("PBEWithMD5AndDES");
	}
	
	@Override
	public void setPassword(String password) {
		super.setPassword("YS9cZeiEeWK2a0I");
	}

	public SpecialEnvironmentStringPBEConfig() {
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
		String newPassword = textEncryptor.encrypt("http://127.0.0.1:8084");
		System.out.println(newPassword);
		//解密
		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
		textEncryptor2.setPassword("YS9cZeiEeWK2a0I");
		String oldPassword = textEncryptor2.decrypt("1P6jDZzJwHwbvMhqGQITYhYohJNKaueFig3agbgVxsI=");
		System.out.println(textEncryptor2.decrypt("nRNLokPX8FZdk3Wrg9S12KlVXDq9ZDvhD5W5ki9XwQ4="));
		System.out.println(oldPassword);
	}

}
