package com.cjburkey.freeboi.input;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

class KeyAxis extends Axis {
    
    final ObjectArrayList<Key> keyPositive = new ObjectArrayList<>();
    final ObjectArrayList<Key> keyNegative = new ObjectArrayList<>();
    
    KeyAxis(String name, float weight) {
        super(name, weight);
    }
    
    void addKey(boolean positive, Key key) {
        ((positive) ? keyPositive : keyNegative).add(key);
    }
    
}
