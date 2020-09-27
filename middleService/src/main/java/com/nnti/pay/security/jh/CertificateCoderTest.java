package com.nnti.pay.security.jh;

import com.nnti.pay.security.jh.CertificateCoder;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Wander on 2017/6/19.
 */
public class CertificateCoderTest {

    private String certificatePath = "F:/workspace/idea/gsmc/middleService/src/main/resources/1010509.cer";
    private String keyStorePath = "F:/workspace/idea/gsmc/middleService/src/main/resources/1010509.keystore";
    private String keyStorePassword = "QAZqaz";
    private String aliasPassword = "QAZqaz";
    private String alias = "1";

    @Test
    public void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String inputStr = "Ceritifcate";
        byte[] data = inputStr.getBytes();

        byte[] encrypt = CertificateCoder.encryptByPublicKey(data,
                certificatePath);

        byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt,
                keyStorePath, alias, keyStorePassword, aliasPassword);
        String outputStr = new String(decrypt);

        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

        // 验证数据一致
        assertArrayEquals(data, decrypt);

        // 验证证书有效
        assertTrue(CertificateCoder.verifyCertificate(certificatePath));

    }

    @Test
    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");

        String inputStr = "sign";
        byte[] data = inputStr.getBytes();

        byte[] encodedData = CertificateCoder.encryptByPrivateKey(data,
                keyStorePath, keyStorePassword, alias, aliasPassword);

        byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData,
                certificatePath);

        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        assertEquals(inputStr, outputStr);

        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        byte[] sign = CertificateCoder.sign(encodedData, keyStorePath, alias,
                keyStorePassword, aliasPassword);
        System.err.println("签名:\r" + Hex.encodeHexString(sign));

        // 验证签名
        boolean status = CertificateCoder.verify(encodedData, sign,
                certificatePath);
        System.err.println("状态:\r" + status);
        assertTrue(status);
    }

    @Test
    public void testVerify() throws Exception {
        System.err.println("密钥库证书有效期验证");
        boolean status = CertificateCoder.verifyCertificate(new Date(),
                keyStorePath, keyStorePassword, alias);
        System.err.println("证书状态:\r" + status);
        assertTrue(status);
    }
}
