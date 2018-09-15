package com.cjburkey.freeboi.ecs;

import com.cjburkey.freeboi.components.Transform;

public abstract class ECSComponent extends SafeHandled {
    
    private ECSEntity entity;
    public boolean enabled = true;
    
    final void setParent(ECSEntity entity) {
        if (this.entity != null) {
            throw new RuntimeException("A component cannot have its parent entity redefined.");
        }
        this.entity = entity;
    }
    
    final void handleRem() {
        onRemove();
        onCleanup();
    }
    
    public final ECSEntity getEntity() {
        return entity;
    }
    
    public final Transform getTransform() {
        return entity.transform;
    }
    
    void handleAdd() {
        onAdd();
    }
    
    boolean getLoopable() {
        return enabled;
    }
    
    // Virtual methods
    
    public void onAdd() {
    }
    
    public void onUpdate() {
    }
    
    public void onRender() {
    }
    
    public void onRemove() {
    }
    
    public void onCleanup() {
    }
    
}
