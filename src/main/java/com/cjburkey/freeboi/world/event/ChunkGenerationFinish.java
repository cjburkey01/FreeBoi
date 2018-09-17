package com.cjburkey.freeboi.world.event;

import com.cjburkey.freeboi.world.Chunk;

public class ChunkGenerationFinish extends ChunkEvent {

    public ChunkGenerationFinish(Chunk chunk) {
        super(chunk);
    }
    
}
