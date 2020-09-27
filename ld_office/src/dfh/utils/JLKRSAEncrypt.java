package dfh.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class JLKRSAEncrypt {

    // 公钥
    private static RSAPublicKey publicKey;
    // 字节数据转字符串专用集合
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    // 获取公钥
    public static RSAPublicKey getPublicKey() {

        return publicKey;
    }

    // 从文件中输入流中加载公钥
    public static void loadPublicKey(InputStream in) throws Exception {

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String readLine = null;
            StringBuilder sb = new StringBuilder();

            while ((readLine = br.readLine()) != null) {

                if (readLine.charAt(0) == '-') {

                    continue;
                } else {

                    sb.append(readLine);
                    sb.append('\r');
                }
            }

            loadPublicKey(sb.toString());
        } catch (IOException e) {

            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {

            throw new Exception("公钥输入流为空");
        }
    }

    // 从字符串中加载公钥
    public static void loadPublicKey(String publicKeyStr) throws Exception {

        try {

            BASE64Decoder base64Decoder = new BASE64Decoder();

            byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);

            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {

            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {

            throw new Exception("公钥非法");
        } catch (IOException e) {

            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {

            throw new Exception("公钥数据为空");
        }
    }

    // 加密过程
    public static String encrypt(byte[] plainTextData) throws Exception {

        if (publicKey == null) {

            throw new Exception("加密公钥为空, 请设置");
        }

        Cipher cipher = null;

        try {

            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] output = cipher.doFinal(plainTextData);

            BASE64Encoder encoder = new BASE64Encoder();

            return encoder.encode(output);
        } catch (NoSuchAlgorithmException e) {

            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {

            throw new Exception("无此加密算法");
        } catch (InvalidKeyException e) {

            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {

            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {

            throw new Exception("明文数据已损坏");
        }
    }

    // 字节数据转十六进制字符串
    public static String byteArrayToString(byte[] data) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < data.length; i++) {

            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);

            if (i < data.length - 1) {

                stringBuilder.append(' ');
            }
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {

        // 测试字符串
        String encryptStr = "d959caadac9b13dcb3e609440135cf54";

        try {

            // 加密
            String rsaResult = JLKRSAEncrypt.encrypt(encryptStr.getBytes());

            System.out.println(rsaResult.replaceAll("\r|\n", ""));
        } catch (Exception e) {

            System.err.println(e.getMessage());
        }
    }
}
