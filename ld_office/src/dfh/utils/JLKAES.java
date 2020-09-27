package dfh.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

public class JLKAES {

    // 算法名称
    final static String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    final static String algorithmStr = "AES/CBC/PKCS7Padding";

    private static Key key;
    private static Cipher cipher;
    boolean isInited = false;

    private static void init(byte[] keyBytes) {

        // 如果密钥不足16位，那么就补足. 这个if中的内容很重要
        int base = 32;

        if (keyBytes.length % base != 0) {

            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];

            Arrays.fill(temp, (byte) 0);

            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);

            keyBytes = temp;
        }

        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);

        try {

            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr, "BC");
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        } catch (NoSuchPaddingException e) {

            e.printStackTrace();
        } catch (NoSuchProviderException e) {

            e.printStackTrace();
        }
    }

    public static String encrypt(byte[] content, byte[] keyBytes, String iv) {

        byte[] encryptedText = null;

        init(keyBytes);

        try {

            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));

            encryptedText = cipher.doFinal(content);
        } catch (Exception e) {

            e.printStackTrace();
        }

        BASE64Encoder enco64 = new BASE64Encoder();

        return enco64.encode(encryptedText);
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, String iv) {

        byte[] encryptedText = null;

        init(keyBytes);

        try {

            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));

            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return encryptedText;
    }

    public static void main(String[] args) {

        String iv = "345h54k23lrn93m5";
        // 加解密密钥
        String key = "d959caadac9b13dcb3e609440135cf54";

        String sign = "{\"password\":\"wallet_pass\",\"name\":\"wallet_name\",\"desc\":\"wallet_desc\",\"sign\":\"650E3CD219A92EB097AACAF655429AC1C2F4118E4EBC8B0D6286E0CDE5060E51\"}";

        // 加密方法
        String enc = JLKAES.encrypt(sign.getBytes(), key.getBytes(), iv);

        System.out.println(enc);
    }
}
