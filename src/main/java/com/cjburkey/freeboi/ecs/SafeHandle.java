package com.cjburkey.freeboi.ecs;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.function.Consumer;

public final class SafeHandle<T extends SafeHandled> {
    
    // Whether or not there may be more than one object per single type
    private final boolean limitOne;
    
    private final Object2ObjectLinkedOpenHashMap<Class<?>, ObjectOpenHashSet<T>> objects = new Object2ObjectLinkedOpenHashMap<>();
    private final ObjectLinkedOpenHashSet<T> objectsToAdd = new ObjectLinkedOpenHashSet<>();
    private final ObjectLinkedOpenHashSet<Class<?>> objectsToRem = new ObjectLinkedOpenHashSet<>();
    private final ObjectLinkedOpenHashSet<T> objectsSpecificToRem = new ObjectLinkedOpenHashSet<>();
    
    public SafeHandle(boolean limitOne) {
        this.limitOne = limitOne;
    }
    
    void update() {
        Class<?> t;
        while (!objectsToRem.isEmpty()) {
            t = objectsToRem.removeFirst();
            if (t != null && objects.containsKey(t)) {
                ObjectOpenHashSet<T> objs = objects.remove(t);
                if (objs != null) {
                    for (T tt : objs) {
                        tt.handleRem();
                    }
                }
            }
        }
        T obj;
        while (!objectsSpecificToRem.isEmpty()) {
            obj = objectsSpecificToRem.removeFirst();
            Class<?> type = obj.getClass();
            if (hasObjects(type)) {
                ObjectOpenHashSet<T> list = objects.get(type);
                if (list != null) {
                    if (list.contains(obj)) {
                        list.remove(obj);
                        obj.handleRem();
                    }
                    if (list.size() < 1) {
                        objects.remove(type);
                    }
                }
            }
        }
        while (!objectsToAdd.isEmpty()) {
            obj = objectsToAdd.removeFirst();
            if (obj != null) {
                Class<? extends SafeHandled> type = obj.getClass();
                ObjectOpenHashSet<T> list;
                if (hasObjects(type)) {
                    list = objects.get(type);
                } else {
                    list = new ObjectOpenHashSet<>();
                    objects.put(type, list);
                }
                list.add(obj);
                obj.handleAdd();
            }
        }
    }
    
    void foreach(Consumer<? super T> action) {
        for (ObjectOpenHashSet<T> objs : objects.values()) {
            for (T object : objs) {
                if (object.getLoopable()) {
                    action.accept(object);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public void addObject(T obj) {
        if (obj == null || (limitOne && hasObjects(obj.getClass()))) {
            return;
        }
        objectsToAdd.add(obj);
    }
    
    public boolean hasObjects(Class<?> type) {
        if (type == null) {
            return false;
        }
        return objects.containsKey(type);
    }
    
    public void removeObjects(Class<?> type) {
        if (!hasObjects(type)) {
            return;
        }
        objectsToRem.add(type);
    }
    
    public void removeObject(T obj) {
        objectsSpecificToRem.add(obj);
    }
    
    public T getObject(Class<?> type) {
        if (hasObjects(type)) {
            return objects.get(type).get(0);
        }
        return null;
    }
    
    public T[] getObjects(Class<?> type, T[] arr) {
        if (hasObjects(type)) {
            return objects.get(type).toArray(arr);
        }
        return null;
    }
    
    public int getTypeCount() {
        return objects.size() + objectsToAdd.size() - objectsToRem.size();
    }
    
    public int getObjectCount() {
        int i = 0;
        for (ObjectOpenHashSet<T> objs : objects.values()) {
            if (objs.size() > 0 && objs.get(0) != null) {
                if (!objectsToRem.contains(objs.get(0).getClass())) {
                    i += objs.size();
                }
            }
        }
        i += objectsToAdd.size();
        return i;
    }
    
}
