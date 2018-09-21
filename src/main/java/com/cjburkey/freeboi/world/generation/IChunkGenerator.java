package com.cjburkey.freeboi.world.generation;

import com.cjburkey.freeboi.world.Chunk;

public interface IChunkGenerator {
    
    void generate(Chunk chunk);
    
}
