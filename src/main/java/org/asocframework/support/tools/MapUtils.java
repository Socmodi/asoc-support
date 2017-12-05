package org.asocframework.support.tools;

import org.apache.commons.lang3.StringUtils;
import org.asocframework.support.model.TrionesException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/9/21 11:21 $
 */
public class MapUtils {

    protected MapUtils() {

    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String[]> sArray) {
        Map<String, String> result = new HashMap();
        if (sArray == null || sArray.isEmpty()) {
            return result;
        }
        for ( Map.Entry< String, String[] > entry : sArray.entrySet() ) {
            String value = entry.getValue()[0];
            if (value == null || "".equals(value) || "sign".equalsIgnoreCase(entry.getKey())
                    || "sign_type".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }

    /**
     * 将Map<String, String[]> 转化为Map<String, String>
     * @param sArray
     * @return
     */
    public static Map<String, String> paraConvert(Map<String, String[]> sArray) {
        Map<String, String> result = new HashMap();
        if (sArray == null || sArray.isEmpty()) {
            return result;
        }
        for ( Map.Entry< String, String[] > entry : sArray.entrySet() ) {
            String value = entry.getValue()[0];
            result.put(entry.getKey(), value);
        }
        return result;
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paramFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap();
        if (sArray == null || sArray.isEmpty()) {
            return result;
        }
        for ( Map.Entry< String, String> entry : sArray.entrySet() ) {
            String value = entry.getValue();
            if (value == null || "".equals(value) || "sign".equalsIgnoreCase(entry.getKey())
                    || "sign_type".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            result.put(entry.getKey(), value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if(!StringUtils.isEmpty(value)) {
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    prestr.append(key).append("=").append(value);
                } else {
                    prestr.append(key).append("=").append(value).append("&");
                }
            }
        }

        return prestr.toString();
    }

    /**
     * 将形如key=value&key=value的字符串转换为相应的Map对象
     *
     * @param result
     * @return
     */
    public static Map<String, String> convertResultStringToMap(String result) {
        Map<String, String> map =null;
        try {

            if(StringUtils.isNotBlank(result)){
                String localResult = result;
                if(result.startsWith("{") && result.endsWith("}")){
                    localResult = result.substring(1, result.length()-1);
                }
                map = parseQString(localResult);
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return map;
    }


    /**
     * 解析应答字符串，生成应答要素
     *
     * @param str
     *            需要解析的字符串
     * @return 解析的结果map
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> parseQString(String str)
            throws UnsupportedEncodingException {

        Map<String, String> map = new HashMap<String, String>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if(len>0){
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key

                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else  {// 如果当前生成的是value
                    if(isOpen){
                        if(curChar == openName){
                            isOpen = false;
                        }

                    }else{//如果没开启嵌套
                        if(curChar == '{'){//如果碰到，就开启嵌套
                            isOpen = true;
                            openName ='}';
                        }
                        if(curChar == '['){
                            isOpen = true;
                            openName =']';
                        }
                    }
                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    }else{
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
                                         String key, Map<String, String> map)
            throws UnsupportedEncodingException {
        if (isKey) {
            String localKey = temp.toString();
            if (localKey.length() == 0) {
                throw new TrionesException("QString format illegal");
            }
            map.put(localKey, "");
        } else {
            if (key.length() == 0) {
                throw new TrionesException("QString format illegal");
            }
            map.put(key, temp.toString());
        }
    }
}
