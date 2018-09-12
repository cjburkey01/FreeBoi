package com.cjburkey.freeboi.ecs;

import com.cjburkey.freeboi.Debug;
import com.cjburkey.freeboi.components.Transform;
import java.util.Objects;
import java.util.UUID;

public final class ECSEntity extends SafeHandled {
    
    private final UUID uuid = UUID.randomUUID();
    public final Transform transform = new Transform();
    private final SafeHandle<ECSComponent> components = new SafeHandle<>();
    public boolean enabled = true;
    
    ECSEntity() {
        addComponent(transform);
    }
    
    public <T extends ECSComponent> void addComponent(T component) {
        component.setParent(this);
        components.addObject(component);
    }
    
    public <T extends ECSComponent> void removeComponent(Class<T> type) {
        components.removeObject(type);
    }
    
    public <T extends ECSComponent> T getComponent(Class<T> type) {
        ECSComponent comp = components.getObject(type);
        if (comp == null) {
            return null;
        }
        return type.cast(comp);
    }
    
    void onUpdate() {
        components.update();
        
        foreach(ECSComponent::onUpdate);
    }
    
    void onRender() {
        foreach(ECSComponent::onRender);
    }
    
    private void foreach(Foreach<ECSComponent> action) {
        components.foreach((e) -> {
            if (e == null) {
                return;
            }
            try {
                action.call(e);
            } catch (Exception err) {
                Debug.error("An exception was thrown from object \"{}\" on component of type \"{}\"", uuid, e.getClass());
                Debug.exception(err);
            }
        });
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ECSEntity ecsEntity = (ECSEntity) o;
        return Objects.equals(uuid, ecsEntity.uuid);
    }
    
    public int hashCode() {
        return Objects.hash(uuid);
    }
    
    void handleAdd() {
    }
    
    void handleRem() {
    }
    
    boolean getLoopable() {
        return enabled;
    }
    
    @FunctionalInterface
    private interface Foreach<T> {

        void call(T element);

    }
    
}
