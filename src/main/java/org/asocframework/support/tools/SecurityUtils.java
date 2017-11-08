package org.asocframework.support.tools;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author dhj
 * @version $Id: securityUtils ,v 0.1 2017/3/1 17:17 dhj Exp $
 * @name
 */
public class SecurityUtils {

    public static String parityEncode(String data){
        if(StringUtils.isEmpty(data)){
            return data;
        }
        try {
            String codeData = Base64.encode(data.getBytes("UTF-8"));
            char[] cArray = codeData.toCharArray();
            int len = cArray.length;
            char[] A = new char[len%2>0?len/2+1:len/2];
            char[] B = new char[len/2];
            for(int i=0;i<len;i++){
                if(i%2 >0){
                    B[i/2] = cArray[i];
                }else {
                    A[i/2] = cArray[i];
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(A);
            stringBuilder.append(B);
            return stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parityDecode(String data){
        if(StringUtils.isEmpty(data)){
            return data;
        }
        try {
            char[] cArray = data.toCharArray();
            int len = cArray.length;
            char[] A = new char[len];
            if(len%2 >0){
                for(int i=0,j=0;i<len/2;i++,j=j+2){
                    A[j] = cArray[i];
                    A[j+1] = cArray[i+len/2+1];
                }
                A[len-1] = cArray[len/2];
            }else {
                for(int i=0,j=0;i<len/2;i++,j=j+2){
                    A[j] = cArray[i];
                    A[j+1] = cArray[i+len/2];
                }
            }
            return new String(Base64.decode(new String(A)),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String easyColation(String data){
        if(!StringUtils.isEmpty(data)&&data.length()>6){
            int len = data.length();
            char[] chars = data.toCharArray();
            for(int i = len/3;i<len-len/3;i++){
                chars[i]='*';
            }
            return new String(chars);
        }
        return data;
    }


    public static  void main(String args[]){

    }

}
