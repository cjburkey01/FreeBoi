package com.cjburkey.freeboi.ecs;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.function.Consumer;

public final class SafeHandle<T extends SafeHandled> {
    
    private final Object2ObjectLinkedOpenHashMap<Class<?>, T> objects = new Object2ObjectLinkedOpenHashMap<>();
    private final ObjectLinkedOpenHashSet<T> objectsToAdd = new ObjectLinkedOpenHashSet<>();
    private final ObjectLinkedOpenHashSet<Class<?>> objectsToRem = new ObjectLinkedOpenHashSet<>();
    
    void update() {
        Class<?> t;
        while (!objectsToRem.isEmpty()) {
            t = objectsToRem.removeFirst();
            if (t != null && objects.containsKey(t)) {
                objects.remove(t).handleRem();
            }
        }
        T obj;
        while (!objectsToAdd.isEmpty()) {
            obj = objectsToAdd.removeFirst();
            if (obj != null) {
                objects.put(obj.getClass(), obj);
                obj.handleAdd();
            }
        }
    }
    
    void foreach(Consumer<? super T> action) {
        objects.values().forEach((e) -> {
            if (e.getLoopable()) {
                action.accept(e);
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    public void addObject(T obj) {
        if (obj == null || hasObject((Class<T>) obj.getClass())) {
            return;
        }
        objectsToAdd.add(obj);
    }
    
    public boolean hasObject(Class<? extends T> type) {
        if (type == null) {
            return false;
        }
        return objects.containsKey(type);
    }
    
    public void removeObject(Class<? extends T> type) {
        if (!hasObject(type)) {
            return;
        }
        objectsToRem.add(type);
    }
    
    public T getObject(Class<? extends T> type) {
        if (hasObject(type)) {
            return objects.get(type);
        }
        return null;
    }
    
}
