package com.cjburkey.freeboi.ecs;

public abstract class SafeHandled {
    
    abstract void handleAdd();
    abstract void handleRem();
    abstract boolean getLoopable();
    
}
