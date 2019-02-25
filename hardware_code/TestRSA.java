package com.company;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class TestRSA {
    private static final String KEY_ALGORITHM = "RSA";

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair= keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String pubKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println(publicKey.getFormat());
        String priKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("Pub key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("Pri key: " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
//
        String text = "1551014935:WZDcSy8o";
        byte[] buf = text.getBytes();


//        String priKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAuzgs2llPiMFU8eb2YJNkC1nbQmaWLwOgOAgVKmgOlrWm3JH0UT0g76txnMEdfER7ahAiS2PmUK3ukQg7T8NniQIDAQABAkAC6JHg0AGhbiB11BjYgf2sQ/h5KPsVDqU87Zo3kw+D9ZZo3wjaGXpUPe+wRc8EfTpsuw8sR/GRxuBrNRuzNd0hAiEA6xw6rE67jGDJMAd2qI0BMkYr2U6VpRQiRTgZl7TvtaECIQDL2qF2rnkMmSBRLtQkljC7Zlbnf9dABKUCDdaePpAY6QIhAOce/Aqw5yuLZZuCasIswxVR5/8agn8iOMJE8NuyscahAiEAn3hUv0cVoulXAqsjgcuMK82PRmIXvRjlDIpOzlQa3aECIDaaWxnhl1v63gsV3e+Cs1afG56BTQAO5ELtCHhDaxYp";
//        String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALs4LNpZT4jBVPHm9mCTZAtZ20Jmli8DoDgIFSpoDpa1ptyR9FE9IO+rcZzBHXxEe2oQIktj5lCt7pEIO0/DZ4kCAwEAAQ==";

        byte[] encryptBuf = encryptByPrivateKey(buf, Base64.getDecoder().decode(priKey));

        String encryptStr = Base64.getEncoder().encodeToString(encryptBuf);

        System.out.println(encryptStr);

        byte[] dencryptBuf = decryptByPublicKey(encryptBuf, Base64.getDecoder().decode(pubKey));



        String anoText = new String(dencryptBuf);
        System.out.println("anoText: " + anoText);

    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key       密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
}
