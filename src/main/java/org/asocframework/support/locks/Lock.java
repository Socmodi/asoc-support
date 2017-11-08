package org.asocframework.support.locks;

/**
 * @author dhj
 * @version $Id: Lock ,v 0.1 2017/4/1 dhj Exp $
 * @name
 */
public interface Lock {

    boolean lock();

    boolean lock(long maxWaits);

    boolean lock(long maxWaits,long interval);

    void unLock();

}
