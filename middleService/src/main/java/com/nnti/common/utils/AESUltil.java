package com.nnti.common.utils;


import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class AESUltil {

    private static final String AESTYPE = "AES/ECB/PKCS5Padding";

    public static String Encrypt(String plainText, String keyStr) {
        byte[] encrypt = null;
        try {
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(plainText.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return new String(Base64.encodeBase64(encrypt));
    }

    public static String Decrypt(String encryptData, String keyStr) {
        byte[] decrypt = null;
        try {
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return new String(decrypt).trim();
    }

    private static Key generateKey(String key) throws Exception {
        int key_len = 16;
        if (key.length() != key_len) {
            byte[] b = key.getBytes();
            byte[] bb = new byte[key_len];
            int len = b.length > key_len ? key_len : b.length;
            for (int i = 0; i < len; i++) {
                bb[i] = b[i];
            }
            key = new String(bb);
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            return keySpec;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        String keyStr = "key123456";
        String plainText = "Hello World!";

        String encText = Encrypt(plainText, keyStr);
        String decString = Decrypt(encText, keyStr);

        System.out.println(encText);
        System.out.println(decString);
    }
}