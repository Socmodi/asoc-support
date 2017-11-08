package org.asocframework.support.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
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

    private boolean suportNull;

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
        suportNull = valid.suportNull();
    }

    public void validate(ValidateState state,Object value,Object object){
        boolean isNull = value==null|| "".equals(value.toString());
        if(suportNull&&isNull){
            return ;
        }
        if(defaultValue!=null && !"".equals(defaultValue)){
            try {
                FieldUtils.writeDeclaredField(object,field.getName(),defaultValue,true);
                value = defaultValue;
            } catch (IllegalAccessException e) {
            }
        }

        if(isNull){
            state.setErrorMsg(field.getName()+"为:"+value);
            state.setPass(false);
            return ;
        }

        if(belongs!=null&&belongs.length>0) {
            boolean found = false;
            for (String val : belongs) {
                if (val.equals(String.valueOf(value))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                state.setErrorMsg(field.getName() + "非法值:" + value);
                state.setPass(false);
                return;
            }
        }

        if(maxValue!=null&&!"".equals(maxValue)&&(Double.parseDouble(value.toString()) >Double.valueOf(maxValue))){
            state.setErrorMsg(field.getName()+"大于最大值"+maxValue);
            state.setPass(false);
            return ;
        }

        if(minValue!=null&&!"".equals(minValue)&&(Double.parseDouble(value.toString()) <Double.valueOf(minValue))){
            state.setErrorMsg(field.getName()+"小于最小值"+minValue);
            state.setPass(false);
            return;
        }

        if(regexp!=null&&!"".equals(regexp)&& value instanceof String){
            Matcher matcher = pattern.matcher((String)value);
            if(!matcher.matches()){
                state.setErrorMsg(field.getName()+"非法输入值:"+regexp);
                state.setPass(false);
                return;
            }
        }

    }


    public void validate(ValidateState state,Object value){
        boolean isNull = value==null|| "".equals(value.toString());
        if(suportNull&&isNull){
            return ;
        }
        if(!StringUtils.isEmpty(defaultValue)){
            value = defaultValue;
        }
        if(isNull){
            state.setErrorMsg(name+"为: "+value);
            state.setPass(false);
            return ;
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
        if(maxValue!=null&&!"".equals(maxValue)&&(Double.parseDouble(value.toString()) >Double.valueOf(maxValue))){
            state.setErrorMsg(name+"大于最大值"+maxValue);
            state.setPass(false);
            return ;
        }
        if(minValue!=null&&!"".equals(minValue)&&(Double.parseDouble(value.toString()) <Double.valueOf(minValue))){
            state.setErrorMsg(name+"小于最小值"+minValue);
            state.setPass(false);
            return;
        }
        if(regexp!=null&&!"".equals(regexp)&& value instanceof String){
            Matcher matcher = pattern.matcher((String)value);
            if(!matcher.matches()){
                state.setErrorMsg(name+"非法输入值:"+regexp);
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

}
