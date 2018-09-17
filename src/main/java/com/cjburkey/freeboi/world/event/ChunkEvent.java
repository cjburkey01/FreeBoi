package com.cjburkey.freeboi.world.event;

import com.cjburkey.freeboi.event.Event;
import com.cjburkey.freeboi.world.Chunk;

public abstract class ChunkEvent extends Event {
    
    public final Chunk chunk;
    
    public ChunkEvent(Chunk chunk) {
        this.chunk = chunk;
    }
    
    public boolean getIsCancelable() {
        return false;
    }
    
}
