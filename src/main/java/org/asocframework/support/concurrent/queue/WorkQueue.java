package org.asocframework.support.concurrent.queue;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jiqing
 * @version $Id: WorkQueue，v 1.0 2018/1/15 18:44 jiqing Exp $
 * @desc
 */
public class WorkQueue {


    private static final int QUEUE_SIZE = 64;

    private LinkedList works = new LinkedList();

    private ReentrantLock consumerLock = new ReentrantLock();

    private ReentrantLock providerLock = new ReentrantLock();

    private Condition consumer = consumerLock.newCondition();

    private Condition provider = providerLock.newCondition();

    public void put(Object work){
        try{
            providerLock.lock();
            while (works.size()==QUEUE_SIZE){
                System.out.println("provider wait");
                provider.await();
            }
            works.add(work);
            if(works.size()<QUEUE_SIZE){
                provider.signal();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            providerLock.unlock();
        }
        if(works.size()>0){
            signalNotEmpty();
        }
    }

    public Object take(){
        try{
            consumerLock.lock();
            while (works.size()==0){
                System.out.println("consumer wait");
                consumer.await();
            }
            if (works.size()>0){
                consumer.signal();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            consumerLock.unlock();
        }
        if(works.size() < QUEUE_SIZE){
            signalNotFull();
        }
        return works.removeFirst();
    }

    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.consumerLock;
        takeLock.lock();
        try {
            consumer.signal();
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * Signals a waiting put. Called only from take/poll.
     */
    private void signalNotFull() {
        final ReentrantLock putLock = this.providerLock;
        putLock.lock();
        try {
            provider.signal();
        } finally {
            putLock.unlock();
        }
    }


    public static void main(String args[]) throws Exception{
        final WorkQueue workQueue = new WorkQueue();
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        queue.put(1);
        final boolean run = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run){
                    workQueue.put("work1");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run){
                    workQueue.put("work2");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run){
                    System.out.println(workQueue.take());
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Thread.sleep(1000000);
    }


}
