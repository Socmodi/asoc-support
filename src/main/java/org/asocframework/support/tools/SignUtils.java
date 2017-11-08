package org.asocframework.support.tools;

/**
 * @author dhj
 * @version $Id: SignUtil ,v 0.1 2016/10/31 14:31 dhj Exp $
 * @name
 */
public class SignUtils {

    public static String rsaSign(String content, String privateKey, String inputCharset){
        return RsaUtils.sign(content, privateKey, inputCharset);
    }

    public static String md5Sign(String content, String md5Key, String inputCharset){
        return Md5Utils.sign(content, md5Key, inputCharset);
    }


    public static boolean rsaVerify(String verifyContent,String sign,String pubKey,String charset){
        return RsaUtils.verify(verifyContent,sign,pubKey,charset);
    }

    public static boolean  md5Verify(String verifyContent,String sign,String md5Key,String charset){
        return Md5Utils.verify(verifyContent,sign,md5Key,charset);
    }


}
