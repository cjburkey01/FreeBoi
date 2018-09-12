package com.cjburkey.freeboi.value;

@FunctionalInterface
public interface IListener<T> {
    
    void onCall(T oldVal, T newVal);
    
}
