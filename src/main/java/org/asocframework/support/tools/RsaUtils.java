package org.asocframework.support.tools;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author dhj
 * @version $Id: RsaUtil ,v 0.1 2016/10/31 15:42 dhj Exp $
 * @name
 */
public class RsaUtils {


    public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * 验证签名
     * @param content
     * @param sign
     * @param publicKey
     * @param inputCharset
     * @return
     */
    public static boolean verify(String content, String sign, String publicKey, String inputCharset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(inputCharset));
            return signature.verify(Base64.decode(sign));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证签名
     * @param signDigest
     * @param sign
     * @param publicKey
     * @param inputCharset
     * @return
     */
    public static boolean verify(byte[] signDigest, String sign, String publicKey, String inputCharset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(signDigest);
            return signature.verify(Base64.decode(sign));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 商户私钥
     * @param inputCharset 编码格式
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String inputCharset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
            KeyFactory keyf 				= KeyFactory.getInstance("RSA");
            PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update( content.getBytes(inputCharset) );

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA签名
     * @param signDigest 待签名数据
     * @param privateKey 商户私钥
     * @return 签名值
     */
    public static String sign(byte[] signDigest, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
            KeyFactory keyf 				= KeyFactory.getInstance("RSA");
            PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update( signDigest );
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 私钥加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
        KeyFactory keyf 				= KeyFactory.getInstance("RSA");
        PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);
        String enc = "UTF-8";
        // 使用私钥加密公钥解密
        byte[] result = handleData(priKey, data.getBytes(enc), 1);
        return Base64.encode(result);
    }

    /**
     * 公钥解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        String enc = "UTF-8";
        // 使用私钥加密公钥解密
        byte[] deresult = handleData(pubKey, Base64.decode(data), 0);
        return new String(deresult, enc);
    }

    /**
     *
     * @param k
     * @param data
     * @param encrypt
     *            1 加密 0解密
     * @return
     * @throws NoSuchPaddingException
     * @throws Exception
     */
    private static byte[] handleData(Key k, byte[] data, int encrypt)
            throws Exception {
        if (k != null) {
            Cipher cipher = Cipher.getInstance("RSA");
            if (encrypt == 1) {
                cipher.init(Cipher.ENCRYPT_MODE, k);
                byte[] resultBytes = cipher.doFinal(data);
                return resultBytes;
            } else if (encrypt == 0) {
                cipher.init(Cipher.DECRYPT_MODE, k);
                byte[] resultBytes = cipher.doFinal(data);
                return resultBytes;
            } else {
                throw new RuntimeException("参数必须为: 1 加密 0解密");
            }
        }
        return null;
    }


}
