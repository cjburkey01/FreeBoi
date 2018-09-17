package com.cjburkey.freeboi.world.event;

import com.cjburkey.freeboi.world.Chunk;

public class ChunkGenerationBegin extends ChunkEvent {

    public ChunkGenerationBegin(Chunk chunk) {
        super(chunk);
    }
    
}
