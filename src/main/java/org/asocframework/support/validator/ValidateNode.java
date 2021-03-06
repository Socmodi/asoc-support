package org.asocframework.support.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.asocframework.support.model.ClassSources;
import org.asocframework.support.tools.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiqing
 * @version $Id: ValidateNode，v 1.0 2018/11/7 17:26 jiqing Exp $
 * @desc
 */
public class ValidateNode implements Validate {

    private String name;

    private Field field;

    private Type type;

    private Valid valid;

    private String defaultValue;

    private String minValue;

    private String maxValue;

    private String regexp;

    private Pattern pattern;

    private String regexpTemplate;

    private String[] belongs;

    private boolean supportNull;

    private int length;

    public ValidateNode(Valid valid, Field field) {
        this(valid, field.getName());
        this.field = field;
        this.type = field.getGenericType();
    }

    public ValidateNode(Valid valid, String name, Type type) {
        this(valid, name);
        this.type = type;
    }

    public ValidateNode(Valid valid, String name) {
        this.valid = valid;
        this.name = name;
        defaultValue = valid.defaultValue();
        maxValue = valid.maxValue();
        minValue = valid.minValue();
        regexp = valid.regexp();
        if (StringUtils.isNotEmpty(regexp)) {
            pattern = Pattern.compile(regexp);
        }
        regexpTemplate = valid.regexpTemplate();
        belongs = valid.belongs();
        supportNull = valid.supportNull();
        length = valid.length();
    }

    @Override
    public void validate(ValidateState state, Object value, Object object) {
        if (field == null) {
            throw new RuntimeException("非法验证机制");
        }
        boolean isNull = value == null || "".equals(value.toString());
        boolean noneDefault = StringUtils.isEmpty(defaultValue);
        if (supportNull && isNull) {
            return;
        }
        boolean isCompount = ClassUtils.isBaseDataType((Class) type);
        if (isNull && noneDefault && !isCompount) {
            state.setErrorMsg(name + "为: " + value);
            state.setPass(false);
            return;
        }
        if (isCompount) {
            if (isNull && !noneDefault) {
                value = ClassUtils.constructValue((Class<Object>) this.getType(), defaultValue);
            }
            doValidate(state, value, true);
            if (state.isPass() && !StringUtils.isEmpty(defaultValue)) {
                try {
                    Class type = ClassSources.getBaseDataClass(field.getType().getName());
                    FieldUtils.writeDeclaredField(object, field.getName(), ClassUtils.constructValue(type, defaultValue), true);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("非法验证机制", e);
                }
            }
            return;
        } else {
            doValidate(state, value, false);
        }
    }


    @Override
    public void validate(ValidateState state, Object value) {
        boolean isNull = value == null || "".equals(value.toString());
        boolean noneDefault = StringUtils.isEmpty(defaultValue);
        if (supportNull && isNull) {
            return;
        }
        boolean isCompount = ClassUtils.isBaseDataType((Class<Object>) this.getType());
        if (isNull && noneDefault && !isCompount) {
            state.setErrorMsg(name + "为: " + value);
            state.setPass(false);
            return;
        }
        if (isNull && !noneDefault) {
            value = ClassUtils.constructValue((Class<Object>) this.getType(), defaultValue);
            state.setDefaultValue(value);
        }
        doValidate(state, value, false);
    }

    private void doValidate(ValidateState state, Object value, Boolean supportDef) {
        boolean isCompount = ClassUtils.isBaseDataType((Class<Object>) this.getType());
        doValidate(state, value, supportDef, isCompount);
    }

    private void doValidate(ValidateState state, Object value, Boolean supportDef, boolean isCompount) {
        if (isCompount) {
            validateBaseData(state, value, supportDef);
        } else {
            validateCompoundData(state, value, supportDef);
        }
    }

    private void validateCompoundData(ValidateState state, Object value, Boolean supportDef) {
        if (value instanceof Collection) {
            validateCollection(state, (Collection) value, supportDef);
            return;
        }
        if (value.getClass().isArray()) {
            validateArray(state, (Object[]) value, supportDef);
            return;
        }
        ValidateTools.volidate(state, value);
    }

    private void validateCollection(ValidateState state, Collection collection, Boolean supportDef) {
        if (collection == null || collection.size() < 1) {
            state.setPass(false);
            state.setErrorMsg(name + "为: " + collection);
            return;
        }
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!state.isPass()) {
                break;
            }
            Object val = iterator.next();
            doValidate(state, val, supportDef);
        }
    }

    private void validateArray(ValidateState state, Object[] array, Boolean supportDef) {
        if (array == null || array.length < 1) {
            state.setPass(false);
            state.setErrorMsg(name + "为: " + array);
            return;
        }
        for (Object val : array) {
            if (!state.isPass()) {
                break;
            }
            doValidate(state, val, supportDef);
        }
    }

    /**
     * 后续优化,每个校验组件化独立。根据当前节点订阅情况,动态调用校验组件。
     *
     * @param state
     * @param value
     * @param supportDefault
     */
    private void validateBaseData(ValidateState state, Object value, Boolean supportDefault) {
        boolean isNull = value == null || "".equals(value.toString());
        boolean noneDef = StringUtils.isEmpty(defaultValue);
        if (supportNull && isNull) {
            return;
        }
        if (supportDefault) {
            if (isNull && noneDef) {
                state.setErrorMsg(name + "为: " + value);
                state.setPass(false);
                return;
            }
        } else {
            if (isNull) {
                state.setErrorMsg(name + "为: " + value);
                state.setPass(false);
                return;
            }
        }
        if (belongs != null && belongs.length > 0) {
            boolean found = false;
            for (String val : belongs) {
                if (val.equals(String.valueOf(value))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                state.setErrorMsg(name + "非法值:" + value);
                state.setPass(false);
                return;
            }
        }
        if (length > 0 && (value instanceof String) && ((String) value).length() > length) {
            state.setErrorMsg(name + "长度大于" + length);
            state.setPass(false);
            return;
        }

        if (!StringUtils.isEmpty(maxValue) && (Double.parseDouble(value.toString()) > Double.valueOf(maxValue))) {
            state.setErrorMsg(name + "大于最大值" + maxValue);
            state.setPass(false);
            return;
        }

        if (!StringUtils.isEmpty(minValue) && (Double.parseDouble(value.toString()) < Double.valueOf(minValue))) {
            state.setErrorMsg(name + "小于最小值" + minValue);
            state.setPass(false);
            return;
        }

        if (!StringUtils.isEmpty(regexp) && value instanceof String) {
            Matcher matcher = pattern.matcher((String) value);
            if (!matcher.matches()) {
                state.setErrorMsg(name + "非法输入值:" + value);
                state.setPass(false);
                return;
            }
        }
    }


    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }


    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getRegexpTemplate() {
        return regexpTemplate;
    }

    public void setRegexpTemplate(String regexpTemplate) {
        this.regexpTemplate = regexpTemplate;
    }

    public Valid getValid() {
        return valid;
    }

    public void setValid(Valid valid) {
        this.valid = valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String[] getBelongs() {
        return belongs;
    }

    public void setBelongs(String[] belongs) {
        this.belongs = belongs;
    }

    public boolean isSupportNull() {
        return supportNull;
    }

    public void setSupportNull(boolean supportNull) {
        this.supportNull = supportNull;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
