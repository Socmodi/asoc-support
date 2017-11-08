package org.asocframework.support.concurrent.latch;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dhj
 * @version $Id: CountDownLatch ,v 0.1 2017/3/17 10:56 dhj Exp $
 * @name
 */
public class CountDownLatch<T extends LatchCallable> extends java.util.concurrent.CountDownLatch{

    private Map<String,Future> futures = new HashMap();

    private Map<String,T> nodeMap = new HashMap();

    public CountDownLatch(T ...nodes){
        super(nodes.length);
        init(nodes);
    }

    private void init(T ...nodes){
        for(T node:nodes){
            if(node==null){
                throw  new RuntimeException("CountDownLatch construction error!node had null!");
            }
            if(nodeMap.containsKey(node.getName())){
                throw  new RuntimeException("CountDownLatch construction error!,node name exists");
            }
            node.registerLatch(this);
            nodeMap.put(node.getName(),node);
        }
    }

    public void  invoke(ThreadPoolExecutor executor){
        excute(executor);
        doAwait(0, null);
    }

    public void  invoke(ThreadPoolExecutor executor,long time,TimeUnit unit){
        excute(executor);
        doAwait(time,unit);
    }

    public void doAwait(long time,TimeUnit unit){
        try {
            if(time>0){
                this.await(time,unit);
            }else {
                this.await();
            }
        } catch (InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }

    private void  excute(ThreadPoolExecutor executor){
        Iterator<Map.Entry<String,T>> iterator = nodeMap.entrySet().iterator();
        while (iterator.hasNext()){
            T node = iterator.next().getValue();
            futures.put(node.getName(), executor.submit(node));
        }

    }

    public <V> V  getNodeResult(String nodeName,Class<V> clazz ){
        return getResult(nodeName,0,null,clazz);
    }

    public <V> V  getNodeResult(String nodeName,long time,TimeUnit unit,Class<V> clazz ){
        return getResult(nodeName,time,unit,clazz);
    }

    <V> V  getResult(String nodeName,long time,TimeUnit unit,Class<V> clazz ){
        try {
            if(time>0){
                return (V) futures.get(nodeName).get(time,unit);
            }else {
                return (V) futures.get(nodeName).get();
            }

        }  catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    public static void main(String args[]){
//        ThreadPoolExecutor executor =new ThreadPoolExecutor(6,12,
//                10L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(1000));
//
//        Callable callable =new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                Thread.sleep(300);
//                throw new RuntimeException("");
//            }
//        };
//        for(int i = 0;i<100;i++){
//            executor.submit(callable);
//        }
//        LatchCallable latch = new LatchCallable<Boolean>("t1") {
//            @Override
//            public Boolean run() {
//                try {
//                    Thread.sleep(110);
//                    System.out.println(getName());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        };
//        LatchCallable latch2 = new LatchCallable<Boolean>("t2") {
//            @Override
//            public Boolean run() {
//                try {
//                    Thread.sleep(3022);
//                    System.out.println(getName());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//        };
//        CountDownLatch downLatch = new CountDownLatch(latch,latch2);
//        System.out.println("waiting " + System.currentTimeMillis());
//        downLatch.invoke(executor);
//        System.out.println("work " + System.currentTimeMillis());
//        System.out.println("t1:" + downLatch.getNodeResult("t1", Boolean.class));
//        //System.out.println("t2:" + downLatch.getNodeResult("t2", Boolean.class));
//
//    }
}
