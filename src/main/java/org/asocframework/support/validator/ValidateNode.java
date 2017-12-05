package org.asocframework.support.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.asocframework.support.model.ClassSources;
import org.asocframework.support.model.TrionesException;
import org.asocframework.support.tools.ClassUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiqing
 * @version $Id: ValidateNode，v 1.0 2017/11/7 17:26 jiqing Exp $
 * @desc
 */
public class ValidateNode {

    private String name;

    private Field field;

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

    public ValidateNode(Valid valid, Field field){
        this(valid,field.getName());
        this.field =field;
    }

    public ValidateNode(Valid valid,String name){
        this.valid = valid;
        this.name = name;
        defaultValue = valid.defaultValue();
        maxValue = valid.maxValue();
        minValue = valid.minValue();
        regexp = valid.regexp();
        if(regexp!=null && !"".equals(regexp)){
            pattern = Pattern.compile(regexp);
        }
        regexpTemplate = valid.refexpTemplate();
        belongs = valid.belongs();
        supportNull = valid.supportNull();
        length = valid.length();
    }

    public void validate(ValidateState state,Object value,Object object){
        if(field==null){
            throw new TrionesException("非法验证机制");

        }
        if(ClassUtils.isBaseDataType(value.getClass())){
            validateBaseData(state,value,true);
            if(!StringUtils.isEmpty(defaultValue)){
                try {
                    Class type = ClassSources.getBaseDataClass(field.getType().getName());
                    FieldUtils.writeDeclaredField(object,field.getName(),ClassUtils.constructValue(type,defaultValue),true);
                } catch (IllegalAccessException e) {
                    throw new TrionesException("非法验证机制",e);
                }
            }
        }else {
            validateCompoundData(state,value,value.getClass(),false);
        }
    }


    public void validate(ValidateState state,Object value){
        if(ClassUtils.isBaseDataType(value.getClass())){
            validateBaseData(state, value,false);
        }else {
            validateCompoundData(state,value,value.getClass(),false);
        }
    }

    private void validateCompoundData(ValidateState state,Object value,Class clazz,Boolean supportDef){
        if(value instanceof Collection){
            validateCollection(state, (Collection) value,supportDef);
            return;
        }
        if(clazz.isArray()){
            validateArray(state, (Object[]) value,supportDef);
            return;
        }
        ValidateTools.volidate(state,value);
    }

    private void validateCollection(ValidateState state,Collection collection,Boolean supportDef){
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            if(!state.isPass()){
                break;
            }
            Object val = iterator.next();
            if(ClassUtils.isBaseDataType(val.getClass())){
                validateBaseData(state,val,supportDef);
            }else {
                validateCompoundData(state,val,val.getClass(),false);
            }
        }
    }

    private void validateArray(ValidateState state, Object[] array, Boolean supportDef){
        for (Object val: array) {
            if(!state.isPass()){
                break;
            }
            if(ClassUtils.isBaseDataType(val.getClass())){
                validateBaseData(state,val,supportDef);
            }else {
                validateCompoundData(state,val,val.getClass(),false);
            }
        }

    }

    /**
     *  后续优化,每个校验组件化独立。根据当前节点订阅情况,动态调用校验组件。
     * @param state
     * @param value
     * @param supportDefault
     */
    private void validateBaseData(ValidateState state,Object value,Boolean supportDefault){
        boolean isNull = value==null|| "".equals(value.toString());
        boolean noneDef = StringUtils.isEmpty(defaultValue);
        if(supportNull&&isNull){
            return ;
        }
        if(supportDefault){
            if(!noneDef){
                value = defaultValue;
            }
            if(isNull && noneDef){
                state.setErrorMsg(name+"为: "+value);
                state.setPass(false);
                return ;
            }
        }else {
            if(isNull){
                state.setErrorMsg(name+"为: "+value);
                state.setPass(false);
                return ;
            }
        }
        if(belongs!=null&&belongs.length>0){
            boolean found = false;
            for(String val : belongs){
                if(val.equals(String.valueOf(value))){
                    found = true;
                    break;
                }
            }
            if(!found){
                state.setErrorMsg(name+"非法值:"+value);
                state.setPass(false);
                return ;
            }
        }
        if(length>0&& (value instanceof String)&&((String) value).length()>length){
            state.setErrorMsg(name+"长度大于"+length);
            state.setPass(false);
            return ;
        }

        if(!StringUtils.isEmpty(maxValue)&&(Double.parseDouble(value.toString()) >Double.valueOf(maxValue))){
            state.setErrorMsg(name+"大于最大值"+maxValue);
            state.setPass(false);
            return ;
        }

        if(!StringUtils.isEmpty(minValue)&&(Double.parseDouble(value.toString()) <Double.valueOf(minValue))){
            state.setErrorMsg(name+"小于最小值"+minValue);
            state.setPass(false);
            return;
        }

        if(!StringUtils.isEmpty(regexp)&& value instanceof String){
            Matcher matcher = pattern.matcher((String)value);
            if(!matcher.matches()){
                state.setErrorMsg(name+"非法输入值:"+value);
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
}
