package com.cjburkey.freeboi.util;

import java.util.Objects;

public class Pair<T, K> {
    
    public T t;
    public K k;
    
    public Pair(T t, K k) {
        this.t = t;
        this.k = k;
    }
    
    public Pair() {
        this(null, null);
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(t, pair.t) && Objects.equals(k, pair.k);
    }
    
    public int hashCode() {
        return Objects.hash(t, k);
    }
    
}
