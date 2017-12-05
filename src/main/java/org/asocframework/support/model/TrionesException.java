package org.asocframework.support.model;

/**
 * @author jiqing
 * @version $Id: TrionesException，v 1.0 2017/12/5 14:54 jiqing Exp $
 * @desc
 */
public class TrionesException extends RuntimeException{

    public TrionesException(String message, Exception e) {
        super(message,e);
    }

    public TrionesException(String message) {
        super(message);
    }

    public TrionesException(Exception e) {
        super(e);
    }
}
