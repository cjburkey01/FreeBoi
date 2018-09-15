package com.cjburkey.freeboi.event;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.UUID;

class EventManager<T extends Event> {
    
    public final Class<T> eventClass;
    private final ObjectArrayList<UUID> toRemove = new ObjectArrayList<>();
    private final Object2ObjectOpenHashMap<UUID, IEventListener<T>> listeners = new Object2ObjectOpenHashMap<>();
    
    public EventManager(Class<T> eventClass) {
        this.eventClass = eventClass;
    }
    
    public UUID addListener(IEventListener<T> listener) {
        update();
        UUID uuid = UUID.randomUUID();
        listeners.put(uuid, listener);
        return uuid;
    }
    
    public boolean removeListener(UUID uuid) {
        update();
        if (listeners.containsKey(uuid)) {
            toRemove.add(uuid);
            return true;
        }
        return false;
    }
    
    public void trigger(T e) {
        update();
        for (IEventListener<T> listener : listeners.values()) {
            listener.onCall(e);
        }
    }
    
    private void update() {
        if (toRemove.size() > 0) {
            for (UUID uuid : toRemove) {
                listeners.remove(uuid);
            }
            toRemove.clear();
        }
    }
    
}
