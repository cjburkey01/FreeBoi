package com.cjburkey.freeboi.ecs;

public final class ECSWorld {
    
    private SafeHandle<ECSEntity> entities = new SafeHandle<>();
    
    public ECSEntity createEntity() {
        ECSEntity entity = new ECSEntity();
        entities.addObject(entity);
        return entity;
    }
    
    public void onUpdate() {
        entities.update();
        
        entities.foreach(ECSEntity::onUpdate);
    }
    
    public void onRender() {
        entities.foreach(ECSEntity::onRender);
    }
    
}
