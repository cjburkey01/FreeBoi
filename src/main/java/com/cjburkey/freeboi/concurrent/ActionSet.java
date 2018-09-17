package com.cjburkey.freeboi.concurrent;

public class ActionSet {
    
    public final IAction onStart;
    public final IAction action;
    public final IAction onFinish;
    
    private ActionSet(IAction onStart, IAction action, IAction onFinish) {
        this.onStart = onStart;
        this.action = action;
        this.onFinish = onFinish;
    }
    
    public static ActionSet buildComplete(IAction onStart, IAction action, IAction onFinish) {
        return new ActionSet(onStart, action, onFinish);
    }
    
    public static ActionSet buildWithStart(IAction onStart, IAction action) {
        return new ActionSet(onStart, action, null);
    }
    
    public static ActionSet buildWithFinish(IAction action, IAction onFinish) {
        return new ActionSet(null, action, onFinish);
    }
    
    public static ActionSet build(IAction action) {
        return new ActionSet(null, action, null);
    }
    
}
