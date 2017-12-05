package org.asocframework.support.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author jiqing
 * @version $Id: ClassUtils，v 1.0 2017/12/4 17:11 jiqing Exp $
 * @desc
 */
public class ClassUtils {

    private ClassUtils() {
    }

    public static  boolean isBaseDataType(Class clazz){
        return
                (
                        clazz.equals(String.class) ||
                                clazz.equals(Integer.class)||
                                clazz.equals(Byte.class) ||
                                clazz.equals(Long.class) ||
                                clazz.equals(Double.class) ||
                                clazz.equals(Float.class) ||
                                clazz.equals(Character.class) ||
                                clazz.equals(Short.class) ||
                                clazz.equals(BigDecimal.class) ||
                                clazz.equals(BigInteger.class) ||
                                clazz.equals(Boolean.class) ||
                                clazz.equals(Date.class) ||
                                clazz.isPrimitive()
                );
    }


    public static  <T> T constructValue(Class<T> clazz,String source) {
        // 把val转换成type类型返回 比如说getVal("123",Integer.class) 返回一个123
        T value = null;
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            constructor.setAccessible(true);
            value = constructor.newInstance(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static  <T> T parseValue(String val, Class<T> type) {
        T value = null;
        String className = type.getSimpleName();
        if (type == Integer.class) {
            className = "Int";
        }
        String convertMethodName = "parse" + className;
        try {
            Method m = type.getMethod(convertMethodName, String.class);
            value = (T) m.invoke(null, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
