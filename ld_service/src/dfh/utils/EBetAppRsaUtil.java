package dfh.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EBetAppRsaUtil {

    /**
     * 因处理sign的验证需求 , 所以 web , webservice , office project 当中也有一个EBetAppRsaUtil, Key更换需一起更换
     */
    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALnEZFBuCQ8+a77icxHSvjDv8BPSvfM9MkXJY/1GCKc9zD1lDVjA7i/jSxsm8aei2l9XXtCPWFdgAcSXO8mAyTmfDKzBx9m8hIv9piMrkVaIfvYE8e1EwKLhIT46XQy4f8Ihq3MlIxC2Qs0B54F0k/zLBYhX6Z7BQ26bJojWWJ3lAgMBAAECgYBSNz4ibqCp03mCMNy2nau5IugBDfsthP8T7c7IEE/SHMKgxXGDv1drcFAUQ0ceZy/4lEWNTnPNr9r4c+ptAweLK7GvjMc6eeVW/RK7QjYh2afzrQuUsvxPyoIEdYJFTvg52bG2bYTHnhGli45XHIUi5hsRMs0OezMPIveIk0qNHQJBAPSLGeb4TX0B6AP5d+m472y1Uh4tEte6dq7qZ7CXtDVtcoO+NmNj9o7bizrYGDLNpUhlkfWYSycAmaAeR8zFYisCQQDCeF2scMe3jiRUQAIkOLraJCGF2QFhDk2szRWdCCAX+uxr5UZv3YG6p7MO7o3+A8/KK1LEg6Q3tFdMFbxJPsgvAkAc79TZ0uoAb9hPmBaLbpnOSEp2TakcI/FlU1F/wQPPCqtxUXHIIw0K7Im2Jdn/MPxs66/w2tuRuu1y2AJxBa5PAkEAmfXLS5M6OpL5Jg5K47xaCgvi0mHcWqeFTVJMUzc0mWeZt04cNsIqS8kq1J5+oeUIo+kqrNP/te470su+dkqTFwJBALAZ1MC4LAjPkC/I3PJhI3Y6bb7kkA/Go9d4orlT7nz7LRYSXr1eCF0/hK+wK16dOq5nljsbyfP5zL+w1uLbvrY=";
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5xGRQbgkPPmu+4nMR0r4w7/AT0r3zPTJFyWP9RginPcw9ZQ1YwO4v40sbJvGnotpfV17Qj1hXYAHElzvJgMk5nwyswcfZvISL/aYjK5FWiH72BPHtRMCi4SE+Ol0MuH/CIatzJSMQtkLNAeeBdJP8ywWIV+mewUNumyaI1lid5QIDAQAB";

    public static String sign(String data) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return sign(data.getBytes());
    }

    public static String sign(byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(priKey);
        signature.update(data);
        return new String(Base64.encodeBase64(signature.sign()));
    }

    public static boolean verify(byte[] data, byte[] sign) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

}

