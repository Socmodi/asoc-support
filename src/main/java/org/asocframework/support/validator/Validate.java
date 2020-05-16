package org.asocframework.support.validator;

/**
 * @author jiqing
 * @version $Id: AbstrctValidate，v 1.0 2018/2/7 11:37 jiqing Exp $
 * @desc
 */
public interface Validate {

    /**
     * 执行校验
     * @param state
     * @param value
     * @param object
     */
    void validate(ValidateState state, Object value, Object object);

    /**
     * 执行校验
     * @param state
     * @param value
     */
    void validate(ValidateState state, Object value);


}
