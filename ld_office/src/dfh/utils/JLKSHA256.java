package dfh.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

public class JLKSHA256 {

    // 将加密后的字节数组转换成字符串
    private static String byteArrayToHexString(byte[] b) {

        StringBuilder hs = new StringBuilder();
        String stmp;

        for (int n = 0; b != null && n < b.length; n++) {

            stmp = Integer.toHexString(b[n] & 0XFF);

            if (stmp.length() == 1) {

                hs.append('0');
            }

            hs.append(stmp);
        }

        return hs.toString().toLowerCase();
    }

    // sha256_HMAC加密
    public static String sha256_HMAC(String message, String secret) {

        String hash = "";

        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            sha256_HMAC.init(secret_key);

            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());

            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {

            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }

        return hash;
    }

    // 利用java原生的摘要实现SHA256加密
    public static String getSHA256StrJava(String str) {

        MessageDigest messageDigest;
        String encodeStr = "";

        try {

            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));

            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return encodeStr;
    }

    // 将byte转为16进制
    private static String byte2Hex(byte[] bytes) {

        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;

        for (int i = 0; i < bytes.length; i++) {

            temp = Integer.toHexString(bytes[i] & 0xFF);

            if (temp.length() == 1) {

                stringBuffer.append("0");
            }

            stringBuffer.append(temp);
        }

        return stringBuffer.toString();
    }

    // 方法用途：对所有传入参数按照字段名的ASCII码从小到大排序（字典序），并且生成url参数串末尾
    public static String unionpayASCII(Map<String, Object> map) {

        Object[] keys = map.keySet().toArray();

        Arrays.sort(keys);

        StringBuilder originStr = new StringBuilder();

        for (Object key : keys) {

            if (null != map.get(key) && !map.get(key).toString().equals("") && !"signature".equals(key)) {

                originStr.append(key).append("=").append(map.get(key));
            }
        }

        return originStr.toString();
    }

    public static void main(String[] args) throws Exception {

        String asciiStr = "coin_total=0.99000000coin_type=TCCout_trade_no=777_jlkzf_woodytest_4006510rand=zk2lailr6jkaw3ep1wc21aqer6nu0kakstatus=1trade_no=2018062115295595433254849624&mch_secret=694634b2f65cf8c66a642b0dd0e67e21";
        String mch_secret = "694634b2f65cf8c66a642b0dd0e67e21";

        System.out.println(System.getProperty("file.encoding"));
        System.out.println(JLKSHA256.sha256_HMAC(asciiStr, mch_secret).toUpperCase());
    }
}
