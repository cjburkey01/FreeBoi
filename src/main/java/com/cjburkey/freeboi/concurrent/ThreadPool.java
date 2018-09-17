package com.cjburkey.freeboi.concurrent;

import com.cjburkey.freeboi.Debug;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ThreadPool {
    
    public static final int threadSleepBreakMS = 15;
    
    private final ConcurrentLinkedQueue<ActionSet> futureActions = new ConcurrentLinkedQueue<>();
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
    
    public void queueAction(ActionSet action) {
        if (action != null) {
            futureActions.offer(action);
        }
    }
    
    // DO NOT CALL ON THE MAIN THREAD FOR ANY REASON
    private void threadedActivity() {
        while (running) {
            while (!futureActions.isEmpty()) {
                ActionSet actions = futureActions.poll();
                if (actions == null) {
                    continue;
                }
                if (actions.onStart != null) {
                    actions.onStart.onCall();
                }
                if (actions.action != null) {
                    actions.action.onCall();
                }
                if (actions.onFinish != null) {
                    actions.onFinish.onCall();
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
        running = false;
    }

    public int getThreadCount() {
        return threads.length;
    }
    
}
