package com.cjburkey.freeboi.value;

public class BoolProperty extends Property<Boolean> {
    
    public BoolProperty(boolean value) {
        super(value);
    }

    public BoolProperty() {
        this(false);
    }
    
    public boolean flip() {
        set(!get());
        return get();
    }
    
}
