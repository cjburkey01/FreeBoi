package com.cjburkey.freeboi.ecs;

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
    }
    
    public final ECSEntity getEntity() {
        return entity;
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
    
}
