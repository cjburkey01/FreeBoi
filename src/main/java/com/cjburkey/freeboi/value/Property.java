package com.cjburkey.freeboi.value;

import java.util.ArrayList;
import java.util.List;

public class Property<T> extends ReadOnlyProperty<T> {
    
    private final List<IListener<T>> listeners = new ArrayList<>();

    public Property(T value) {
        super(value);
    }
    
    public Property() {
        this(null);
    }
    
    public boolean hasValue() {
        return value != null;
    }
    
    public void set(T val) {
        if (value == val || (value != null && value.equals(val))) {
            return;
        }
        for (IListener<T> listener : listeners) {
            listener.onCall(value, val);
        }
        value = val;
    }
    
    public void addListener(IListener<T> listener) {
        listeners.add(listener);
    }
    
    public String toString() {
        return "Property{" + "value=" + value + '}';
    }
    
}
