package com.cjburkey.freeboi.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadSafeHandler {
    
    public final int maxUpdates;
    private final ConcurrentLinkedQueue<IAction> actions = new ConcurrentLinkedQueue<>();
    
    public ThreadSafeHandler(int maxUpdates) {
        this.maxUpdates = maxUpdates;
    }
    
    public void update() {
        int updates = 0;
        while (!actions.isEmpty() && updates < maxUpdates) {
            IAction action = actions.poll();
            if (action != null) {
                action.onCall();
            }
            updates ++;
        }
    }
    
    public void queue(IAction action) {
        if (action != null) {
            actions.offer(action);
        }
    }
    
    public void queue(IAction... actions) {
        for (IAction action : actions) {
            queue(action);
        }
    }
    
}
