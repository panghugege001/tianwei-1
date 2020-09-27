package dfh.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Live800Encode {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    
    /**
     * 转为16进制加密计算哈希值的运算
     **/
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * @param origin String
     * @return String
     * @throws Exception
     */
    public static String getMD5Encode(String origin) throws Exception {
        if (!inited) {
            throw new Exception("MD5 算法实例初始化错误！");
        }
        if (origin == null) {
            return null;
        }
        byte[] temp = null;
        /**
         * 代表这个方法加锁,相当于不管哪一个线程A每次运行到这个方法时,都要检查有没有其它正在用这个方法的线程B（或者C D等）,
         * 有的话要等正在使用这个方法的线程B（或者C D）运行完这个方法后再运行此线程A,没有的话,直接运行 它包括两种用法：synchronized 方法和 synchronized 块。
         **/
        synchronized (md) {
            temp = md.digest(origin.getBytes());
        }

        return byteArrayToHexString(temp);

    }

    private static MessageDigest md = null;
    private static boolean inited = false;
    static {
        try {
            md = MessageDigest.getInstance("MD5");
            inited = true;
        }
        catch (NoSuchAlgorithmException ex) {
            inited = false;
        }
    }
    public static void main(String[] args) {
        try {
        	System.err.println(getMD5Encode("ff"));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
