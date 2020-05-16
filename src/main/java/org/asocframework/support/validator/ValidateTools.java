package org.asocframework.support.validator;


import org.apache.commons.lang3.reflect.FieldUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jiqing
 * @version $Id: ValidateTools，v 1.0 2018/11/7 19:49 jiqing Exp $
 * @desc
 */
public class ValidateTools {

    private static final ConcurrentMap<String, List<ValidateNode>> CACHES = new ConcurrentHashMap();

    private ValidateTools() {
    }

    /**
     * facade层调用，验证多个常规数据参数，int，string，long
     *
     * @param key
     * @param objs
     * @return
     */
    public static ValidateState volidate(String key, Object... objs) {
        ValidateState state = new ValidateState();
        List<ValidateNode> list = CACHES.get(key);
        executeValidateNodes(list, state, objs);
        return state;
    }

    /**
     * facade层调用，用于验证RO数据对象
     *
     * @param param
     * @return
     */
    public static ValidateState volidate(Object param) {
        ValidateState state = new ValidateState();
        if (param == null) {
            state.setPass(false);
            state.setErrorMsg("入参对象为null");
            return state;
        }
        Validator validated = param.getClass().getAnnotation(Validator.class);
        if (validated == null || validated.validate()) {
            return doValidate(state, param);
        }
        return state;
    }

    /**
     * facade层调用，用于验证RO数据对象等
     */
    public static ValidateState volidate(ValidateState state, Object param) {
        Validator validated = param.getClass().getAnnotation(Validator.class);
        if (validated == null || validated.validate()) {
            return doValidate(state, param);
        }
        return state;
    }

    /**
     * 初始化基础参数类型
     *
     * @param clazz
     */
    public static void resolve(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Validator validated = method.getAnnotation(Validator.class);
            if (validated != null) {
                String key = clazz.getName() + "." + validated.alias();
                List<ValidateNode> list = resolveMethod(method);
                if (list != null && list.size() > 0) {
                    getCACHES().put(key, list);
                }
            }
        }
    }

    private static List<ValidateNode> resolveMethod(Method method) {
        List<ValidateNode> list = new ArrayList();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Type[] types = method.getGenericParameterTypes();
        if (parameterAnnotations.length < 0) {
            return null;
        }
        for (int index = 0; index < parameterAnnotations.length; index++) {
            Annotation[] annotations = parameterAnnotations[index];
            if (annotations == null || annotations.length < 0) {
                continue;
            }
            Valid valid = hasValid(annotations);
            if (valid != null && valid.validate()) {
                ValidateNode param = new ValidateNode(valid, valid.name(), types[index]);
                list.add(param);
            }
        }
        return list;
    }

    private static Valid hasValid(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Valid) {
                return (Valid) annotation;
            }
        }
        return null;
    }


    /**
     * @param state
     * @param object
     * @return
     */
    private static ValidateState doValidate(ValidateState state, Object object) {
        String cacheName = object.getClass().getName();
        List<ValidateNode> list = CACHES.get(cacheName);
        if (list == null) {
            list = fillValidateNodes(object);
            CACHES.put(cacheName, list);
        }
        executeValidateNodes(list, state, object);
        return state;
    }

    private static List<ValidateNode> fillValidateNodes(Object object) {
        List<ValidateNode> list = new ArrayList<ValidateNode>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field field = fields[index];
            Valid valid = fields[index].getAnnotation(Valid.class);
            if (valid != null) {
                ValidateNode node = new ValidateNode(valid, field);
                list.add(node);
            }
        }
        return list;

    }

    private static void executeValidateNodes(List<ValidateNode> list, ValidateState state, Object object) {
        for (int index = 0; index < list.size(); index++) {
            ValidateNode node = list.get(index);
            try {
                if (!state.isPass()) {
                    break;
                }
                node.validate(state, FieldUtils.readField(object, node.getField().getName(), true), object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("数据验证异常", e);
            }
        }
    }

    private static void executeValidateNodes(List<ValidateNode> list, ValidateState state, Object... objs) {
        for (int index = 0; index < list.size(); index++) {
            ValidateNode node = list.get(index);
            try {
                if (!state.isPass()) {
                    break;
                }
                node.validate(state, objs[index]);
                if (state.getDefaultValue() != null) {
                    objs[index] = state.getDefaultValue();
                    state.removerDefaultValue();
                    state.putParam(node.getName(), objs[index]);
                }
            } catch (Exception e) {
                throw new RuntimeException("数据验证异常", e);
            }
        }
        state.setArgs(objs);
    }

    public static ConcurrentMap<String, List<ValidateNode>> getCACHES() {
        return CACHES;
    }
}
