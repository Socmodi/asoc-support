package org.asocframework.support.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiqing
 * @version $Id: ClassSources，v 1.0 2017/12/5 09:55 jiqing Exp $
 * @desc
 */
public class ClassSources {

    private static final Map<String,Class> baseData = new HashMap();

    static {
        baseData.put("java.lang.Integer",Integer.class);
        baseData.put("int",Integer.class);
        baseData.put("java.lang.Double",Double.class);
        baseData.put("double",Double.class);
        baseData.put("java.lang.Float",Float.class);
        baseData.put("float",Float.class);
        baseData.put("java.lang.Long",Long.class);
        baseData.put("long",Long.class);
        baseData.put("java.lang.Short",Short.class);
        baseData.put("short",Short.class);
        baseData.put("java.lang.Byte",Byte.class);
        baseData.put("byte",Byte.class);
        baseData.put("java.lang.Boolean",Boolean.class);
        baseData.put("boolean",Boolean.class);
        baseData.put("java.lang.Character",Character.class);
        baseData.put("char",Character.class);
        baseData.put("java.lang.String",String.class);
    }

    public static Class getBaseDataClass(String classKey){
        return baseData.get(classKey);
    }

}
