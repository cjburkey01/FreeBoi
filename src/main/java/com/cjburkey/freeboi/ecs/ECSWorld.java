package com.cjburkey.freeboi.ecs;

public final class ECSWorld {
    
    private final SafeHandle<ECSEntity> entities = new SafeHandle<>(false);
    
    public ECSEntity createEntity() {
        ECSEntity entity = new ECSEntity(this);
        entities.addObject(entity);
        return entity;
    }
    
    public void destroy(ECSEntity entity) {
        entity.onDestroy();
        entities.removeObject(entity);
    }
    
    public void onUpdate() {
        entities.update();
        
        entities.foreach(ECSEntity::onUpdate);
    }
    
    public void onRender() {
        entities.foreach(ECSEntity::onRender);
    }
    
    public void onExit() {
        entities.foreach(ECSEntity::onDestroy);
    }
    
    public int getObjects() {
        return entities.getObjectCount();
    }
    
}
