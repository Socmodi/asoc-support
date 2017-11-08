package org.asocframework.support.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jiqing
 * @version $Id: Validator，v 1.0 2017/11/7 17:26 jiqing Exp $
 * @desc
 */
@Inherited
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {

    boolean validate() default true;

    String alias() default "";


}
