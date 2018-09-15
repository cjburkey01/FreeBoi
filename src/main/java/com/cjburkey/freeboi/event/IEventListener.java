package com.cjburkey.freeboi.event;

@FunctionalInterface
public interface IEventListener<T extends Event> {
    
    void onCall(T e);
    
}
