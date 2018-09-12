package com.cjburkey.freeboi.value;

import java.util.Objects;

public class ReadOnlyProperty<T> {
    
    protected T value;
    
    public ReadOnlyProperty(T value) {
        this.value = value;
    }
    
    public T get() {
        return value;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReadOnlyProperty<?> that = (ReadOnlyProperty<?>) o;
        return Objects.equals(value, that.value);
    }
    
    public int hashCode() {
        return Objects.hash(value);
    }
    
    public String toString() {
        return "ReadOnlyProperty{" + "value=" + value + '}';
    }
    
}
