package dfh.security;

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

public class DESEncrypt {
    String key;

    public DESEncrypt() {

    }

    public DESEncrypt(String key) {
        this.key = key;
    }

    public byte[] desEncrypt(byte[] plainText) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }

    public byte[] desDecrypt(byte[] encryptText) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        byte encryptedData[] = encryptText;
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }

    public String encrypt(String input) throws Exception {
        return base64Encode(desEncrypt(input.getBytes())).replaceAll("\\s*", "");
    }

    public String decrypt(String input) throws Exception {
        byte[] result = base64Decode(input);
        return new String(desDecrypt(result));
    }

    public String base64Encode(byte[] s) {
        if (s == null)
            return null;
        BASE64Encoder b = new sun.misc.BASE64Encoder();
        return b.encode(s);
    }

    public byte[] base64Decode(String s) throws IOException {
        if (s == null) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(s);
        return b;
    }
    
    public String decryptSportBookText(String cipherText)throws Exception{
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	byte[] keyBytes= new byte[16];
    	byte[] b= key.getBytes("UTF-8");
    	int len= b.length;
    	if (len > keyBytes.length) len = keyBytes.length;
    	System.arraycopy(b, 0, keyBytes, 0, len);
    	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    	IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    	cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
    	BASE64Decoder decoder = new BASE64Decoder();
    	byte [] results = cipher.doFinal(decoder.decodeBuffer(cipherText));
    	return new String(results,"UTF-8");
    }
    
    public String encryptSportBookText(String text)throws Exception{
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	byte[] keyBytes= new byte[16];
    	byte[] b= key.getBytes("UTF-8");
    	int len= b.length;
    	if (len > keyBytes.length) len = keyBytes.length;
    	System.arraycopy(b, 0, keyBytes, 0, len);
    	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    	IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    	cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
    	byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
    	BASE64Encoder encoder = new BASE64Encoder();
    	return encoder.encode(results);
    }

    public static void main(String args[]) {
        try {
            
            DESEncrypt d = new DESEncrypt("jsn72ksm");
            String p=d.encrypt("cagent=B18_AG/\\\\/loginname=ptest98/\\\\/method=ice");
            System.out.println("密文:"+p);
            System.out.println(d.decrypt(p));

                } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
