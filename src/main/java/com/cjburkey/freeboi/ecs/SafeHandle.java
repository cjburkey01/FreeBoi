package com.cjburkey.freeboi.ecs;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.function.Consumer;

public final class SafeHandle<T extends SafeHandled> {
    
    // Whether or not there may be more than one object per single type
    private final boolean limitOne;
    
    private final Object2ObjectLinkedOpenHashMap<Class<?>, ObjectArrayList<T>> objects = new Object2ObjectLinkedOpenHashMap<>();
    private final ObjectLinkedOpenHashSet<T> objectsToAdd = new ObjectLinkedOpenHashSet<>();
    private final ObjectLinkedOpenHashSet<Class<?>> objectsToRem = new ObjectLinkedOpenHashSet<>();
    
    public SafeHandle(boolean limitOne) {
        this.limitOne = limitOne;
    }
    
    void update() {
        Class<?> t;
        while (!objectsToRem.isEmpty()) {
            t = objectsToRem.removeFirst();
            if (t != null && objects.containsKey(t)) {
                ObjectArrayList<T> objs = objects.remove(t);
                if (objs != null) {
                    for (T tt : objs) {
                        tt.handleRem();
                    }
                }
            }
        }
        T obj;
        while (!objectsToAdd.isEmpty()) {
            obj = objectsToAdd.removeFirst();
            if (obj != null) {
                Class<? extends SafeHandled> type = obj.getClass();
                ObjectArrayList<T> list;
                if (hasObjects(type)) {
                    list = objects.get(type);
                } else {
                    list = new ObjectArrayList<>();
                    objects.put(type, list);
                }
                list.add(obj);
                obj.handleAdd();
            }
        }
    }
    
    void foreach(Consumer<? super T> action) {
        for (ObjectArrayList<T> objs : objects.values()) {
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
        for (ObjectArrayList<T> objs : objects.values()) {
            if (!objectsToRem.contains(objs.get(0).getClass())) {
                i += objs.size();
            }
        }
        i += objectsToAdd.size();
        return i;
    }
    
}
