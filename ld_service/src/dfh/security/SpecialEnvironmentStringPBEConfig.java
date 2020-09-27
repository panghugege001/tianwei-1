package dfh.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;

public class SpecialEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig {
	
	@Override
	public void setAlgorithm(String algorithm) {
		super.setAlgorithm("PBEWithMD5AndDES");
	}
	
	@Override
	public void setPassword(String password) {
		super.setPassword("9HkFf7KAHdKJgUN");
	}
	
	
	public static String decryptPBEConfig(String decrypt){
		  StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	       EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
	        config.setAlgorithm("PBEWithMD5AndDES");
	       config.setPassword("9HkFf7KAHdKJgUN");
	       encryptor.setConfig(config);
		return  encryptor.decrypt(decrypt);
	}
	
	public SpecialEnvironmentStringPBEConfig() {
	}


	public static void main(String[] args) {
		//加密
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("9HkFf7KAHdKJgUN");
		String newPassword = textEncryptor.encrypt("");
		System.out.println(newPassword);
		//解密
//		BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
//		textEncryptor2.setPassword("nje3ot");
//		String oldPassword = textEncryptor2.decrypt(newPassword);
//		System.out.println(oldPassword);
	}

}
