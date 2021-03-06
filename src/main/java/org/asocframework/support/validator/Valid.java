package org.asocframework.support.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jiqing
 * @version $Id: Valid，v 1.0 2017/11/7 17:26 jiqing Exp $
 * @desc
 */
@Inherited
@Target({ElementType.PARAMETER, ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {

    boolean validate() default true;

    String name() default "";

    /**
     * 正则匹配
     * @return
     */
    String regexp() default "";

    /**
     * 正则模板,ip,phone,tele,email;
     * @return
     */
    String regexpTemplate() default "";

    /**
     * 数值类型，数值区间最小
     * @return
     */
    String minValue() default "";

    /**
     * 数值类型，数值区间最小
     * @return
     */
    String maxValue() default "";

    /**
     * 添加默认值
     * @return
     */
    String defaultValue() default "";

    /**
     * 值区间
     * @return
     */
    String[] belongs() default {};

    /**
     * 支持为空
     * @return
     */
    boolean  supportNull() default false;

    /**
     *
     * @return
     */
    int length() default 0;

    /**
     *  组合校验，暂不支持
     * @return
     */
    String validGroup() default "";

}
