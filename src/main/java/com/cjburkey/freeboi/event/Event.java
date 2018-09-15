package com.cjburkey.freeboi.event;

public abstract class Event {
    
    private boolean canceled;
    
    public final void cancel() {
        if (getIsCancelable()) {
            canceled = true;
        }
    }
    
    public final boolean getIsCanceled() {
        return getIsCancelable() && canceled;
    }
    
    public abstract boolean getIsCancelable();
    
}
