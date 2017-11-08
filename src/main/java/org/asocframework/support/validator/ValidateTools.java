package org.asocframework.support.validator;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jiqing
 * @version $Id: ValidateTools，v 1.0 2017/11/7 19:49 jiqing Exp $
 * @desc
 */
public class ValidateTools {

    private static final ConcurrentMap<String,List<ValidateNode>> caches = new ConcurrentHashMap<String,List<ValidateNode>>();

    private static Map<String,List<ValidateNode>> pcaches = new ConcurrentHashMap();


    /**
     * facade层调用，验证多个常规数据参数，int，string，long
     * @param key
     * @param objs
     * @return
     */
    public static ValidateState volidate(String key , Object ...objs){
        ValidateState state = new ValidateState();
        List<ValidateNode> list = pcaches.get(key);
        executeValidateNodes(list,state,objs);
        return state;
    }

    /**
     * facade层调用，用于验证RO数据对象
     * @param param
     * @return
     */
    public  static ValidateState volidate(Object param){
        ValidateState state = new ValidateState();
        if(param==null){
            state.setPass(false);
            state.setErrorMsg("入参对象为null");
            return state;
        }
        Validator validated =  param.getClass().getAnnotation(Validator.class);
        if(validated==null || validated.validate()){
            return doValidate(state, param);
        }
        return state;
    }

    /*facade层调用，用于验证RO数据对象等*/
    private static ValidateState volidate(ValidateState state, Object param) {
        Class clazz  =  param.getClass();
        /*针对非基础类型对象数组*/
        if(isArray(clazz)){
            //暂时不处理
        }
        /*针对非基础类型对象List*/
        if(isList(param)){
            //暂时不处理
        }
        /*针对非基础类型对象Set*/
        if(isSet(param)){
            //暂时不处理
        }
        /*针对非基础类型对象Map*/
        if(isMap(param)){
            //暂时不处理
        }
        Validator validated =  param.getClass().getAnnotation(Validator.class);
        if(validated==null || validated.validate()){
            return doValidate(state, param);
        }
        return state;
    }

    /**
     * 初始化基础参数类型
     * @param clazz
     */
    public static void  resolve(Class clazz){
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method:methods){
            Validator validated = method.getAnnotation(Validator.class);
            if(validated!=null){
                String key = clazz.getName()+"."+validated.alias();
                List<ValidateNode> list = new ArrayList<ValidateNode>();
                Annotation[][] annotations =  method.getParameterAnnotations();
                for(Annotation[] annotationArr: annotations){
                    if(annotationArr.length>0){
                        Annotation annotation = annotationArr[0];
                        if(annotation instanceof Valid){
                            Valid validate = (Valid) annotation;
                            if(validate.validate()){
                                ValidateNode param = new ValidateNode(validate,validate.name());
                                list.add(param);
                            }
                        }
                    }
                }
                getPcaches().put(key,list);
            }
        }
    }

    /**
     *
     * @param state
     * @param object
     * @return
     */
    private static ValidateState doValidate(ValidateState state, Object object){
        String cacheName = object.getClass().getName();
        List<ValidateNode> list = caches.get(cacheName);
        if(list==null){
            list = fillValidateNodes(object);
            caches.put(cacheName,list);
        }
        executeValidateNodes(list,state,object);
        return state;
    }

    private static List<ValidateNode> fillValidateNodes(Object object){
        List<ValidateNode> list = new ArrayList<ValidateNode>();
        Field[] fields =  object.getClass().getDeclaredFields();
        for(int index=0;index<fields.length;index++){
            Field field = fields[index];
            Valid valid = fields[index].getAnnotation(Valid.class);
            if(valid !=null){
                ValidateNode node = new ValidateNode(valid,field);
                list.add(node);
            }
        }
        return list;

    }

    private static void executeValidateNodes(List<ValidateNode> list,ValidateState state,Object object){
        for(int index=0;index<list.size();index++){
            ValidateNode node = list.get(index);
            try {
                if(!state.isPass()){
                    break;
                }
                node.validate(state, FieldUtils.readField(object,node.getField().getName(),true),object);
            } catch (IllegalAccessException e) {
            }
        }
    }

    private static void executeValidateNodes(List<ValidateNode> list,ValidateState state,Object... objs){
        for(int index=0;index<list.size();index++){
            ValidateNode node = list.get(index);
            try {
                if(!state.isPass()){
                    break;
                }
                node.validate(state,objs[index]);
            } catch (Exception e) {
            }
        }
    }


    private static  boolean isBaseDataType(Class clazz){
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

    private static boolean isMap(Object obj){
        return (obj instanceof Map<?,?>);
    }

    private static boolean isList(Object obj){
        return (obj instanceof List<?>);
    }

    private static boolean isSet(Object obj){
        return (obj instanceof java.util.Set<?>);
    }

    private static boolean isArray(Class clazz){
        return clazz.isArray();
    }

    public static ConcurrentMap<String, List<ValidateNode>> getCaches() {
        return caches;
    }

    public static Map<String, List<ValidateNode>> getPcaches() {
        return pcaches;
    }

    public static void setPcaches(Map<String, List<ValidateNode>> pcaches) {
        ValidateTools.pcaches = pcaches;
    }


}
