package org.asocframework.support.tools;

import org.apache.commons.codec.digest.DigestUtils;


import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

/**
 * @author dhj
 * @version $Id: MD5Util ,v 0.1 2016/10/31 15:42 dhj Exp $
 * @name
 */
public class Md5Utils {

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String inputCharset) {
        return DigestUtils.md5Hex(getContentBytes(text + key, inputCharset));
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String inputCharset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, inputCharset));
        if(mysign.equalsIgnoreCase(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
