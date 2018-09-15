package com.cjburkey.freeboi.input;

class Axis {
    
    final String name;
    final float weight;
    
    private float value;
    
    Axis(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }
    
    void doPositive() {
        value += weight;
    }
    
    void doNegative() {
        value -= weight;
    }
    
    float getValue() {
        return value;
    }
    
}
