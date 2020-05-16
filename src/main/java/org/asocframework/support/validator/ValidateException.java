package org.asocframework.support.validator;

/**
 * @author jiqing
 * @version $Id:ValidateException , v1.0 2018/10/12 下午6:34 jiqing Exp $
 * @desc
 */
public class ValidateException extends RuntimeException {

    public ValidateException() {
    }

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

}
