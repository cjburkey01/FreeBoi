package com.cjburkey.freeboi.concurrent;

import com.cjburkey.freeboi.util.Debug;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ThreadPool {
    
    public static final int threadSleepBreakMS = 15;
    
    private final ConcurrentLinkedQueue<IAction> futureActions = new ConcurrentLinkedQueue<>();
    private final Thread[] threads;
    private boolean running;
    
    public ThreadPool(String name, int threadCount) {
        threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i ++) {
            threads[i] = new Thread(this::threadedActivity);
            threads[i].setName("ThreadPool \"" + name + "\" #" + i);
            threads[i].setDaemon(false);
        }
        running = true;
        for (Thread thread : threads) {
            thread.start();
        }
    }
    
    public void queueAction(IAction action) {
        if (action != null) {
            futureActions.offer(action);
        }
    }
    
    // DO NOT CALL ON THE MAIN THREAD FOR ANY REASON
    private void threadedActivity() {
        while (running) {
            while (!futureActions.isEmpty()) {
                IAction action = futureActions.poll();
                if (action != null) {
                    action.onCall();
                }
            }
            try {
                Thread.sleep(threadSleepBreakMS);
            } catch (Exception e) {
                Debug.warn("Failed to sleep");
                Debug.exception(e);
            }
        }
    }
    
    public void stop() {
        futureActions.clear();
        running = false;
    }

    public int getThreadCount() {
        return threads.length;
    }
    
}
