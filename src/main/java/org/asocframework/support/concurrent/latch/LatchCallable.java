package org.asocframework.support.concurrent.latch;


import org.asocframework.support.tools.BizUtils;

import java.util.concurrent.Callable;

/**
 * @author dhj
 * @version $Id: LatchCallable ,v 0.1 2017/3/17 11:21 dhj Exp $
 * @name
 */
public abstract class LatchCallable<T> implements Callable<T>{

    private String bizId = BizUtils.getBizId();

    private String name;

    protected CountDownLatch latch;

    public LatchCallable(String name) {
        this.name = name;
    }

    @Override
    public T call() throws Exception {
        try{
            initBizId();
            return run();
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            latch.countDown();
           
        }
    }

    public abstract T run();

    private void initBizId(){
        BizUtils.setBizId(bizId);
    }


    protected void registerLatch(CountDownLatch latch){
        this.latch = latch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
