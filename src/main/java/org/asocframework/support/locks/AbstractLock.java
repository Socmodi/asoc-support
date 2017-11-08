package org.asocframework.support.locks;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dhj
 * @version $Id: AbstractLock ,v 0.1 2017/4/1 dhj Exp $
 * @name
 */
public abstract class AbstractLock implements Lock{

    private static final long DEFAULT_INTERVAL = 50L;

    private static ThreadLocal<Lock> locallock =new ThreadLocal<Lock>();

    private AtomicInteger state = new AtomicInteger(0);

    public String key;

    private int begin = 0;

    //private java.util.concurrent.locks.Lock jLock = new ReentrantLock();

    @Override
    public synchronized boolean lock() {
//        jLock.lock();
//        try {
            if (locallock != null) {
                state.incrementAndGet();
                return true;
            }
            if (doLock()) {
                state.incrementAndGet();
                locallock.set(this);
                return true;
            }
            return false;
//        }finally {
//            jLock.unlock();
//        }
    }

    @Override
    public synchronized boolean lock(long maxWaits) {
        return lock(maxWaits,DEFAULT_INTERVAL);
    }

    @Override
    public synchronized boolean lock(long maxWaits,long interval) {
        do{
            if(lock()){
                return true;
            }
            try {
                this.wait(interval);
            } catch (InterruptedException e) {
                return false;
            }
            begin +=interval;
        }while (begin<maxWaits);
        return false;
    }

    @Override
    public synchronized void unLock() {
//        jLock.lock();
//        try {
            /*锁资源已经被释放,直接返回*/
            if(state.get()<1){
                return;
            }
            if(state.decrementAndGet()==0){
                try{
                    doUnLock();
                }finally {
                    locallock.remove();
                }
            }
//        }finally {
//            jLock.unlock();
//        }
    }
    /*do lock 排他*/
    public abstract boolean doLock();

    public abstract void doUnLock();

    public static Lock getLocalLock(){
        return locallock.get();
    }

}
