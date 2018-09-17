package com.cjburkey.freeboi.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadSafeHandler {
    
    private final ConcurrentLinkedQueue<IAction> actions = new ConcurrentLinkedQueue<>();
    
    public void update() {
        while (!actions.isEmpty()) {
            IAction action = actions.poll();
            if (action != null) {
                action.onCall();
            }
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
