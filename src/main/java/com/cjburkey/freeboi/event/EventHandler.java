package com.cjburkey.freeboi.event;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;

public class EventHandler {
    
    private final Object2ObjectOpenHashMap<Class<? extends Event>, EventManager<? extends Event>> managers = new Object2ObjectOpenHashMap<>();
    
    public <T extends Event> UUID addListener(Class<T> eventClass, IEventListener<T> listener) {
        return getEventManager(eventClass).addListener(listener);
    }
    
    public <T extends Event> boolean removeListener(Class<T> eventClass, UUID listener) {
        return getEventManager(eventClass).removeListener(listener);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Event> void trigger(T e) {
        if (managers.containsKey(e.getClass())) {
            ((EventManager<T>) getEventManager(e.getClass())).trigger(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Event> EventManager<T> getEventManager(Class<T> eventClass) {
        if (!managers.containsKey(eventClass)) {
            EventManager<T> manager = new EventManager<>(eventClass);
            managers.put(eventClass, manager);
            return manager;
        }
        return (EventManager<T>) managers.get(eventClass);
    }
    
}
