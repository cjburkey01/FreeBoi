package com.cjburkey.freeboi.block;

import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.Chunk;
import com.cjburkey.freeboi.world.World;

public final class BlockState {
    
    public final World world;
    public final BlockType blockType;
    public final Chunk chunk;
    public final Pos posInChunk;
    
    public BlockState(BlockType blockType, Chunk chunk, Pos posInChunk) {
        world = chunk.world;
        this.blockType = blockType;
        this.chunk = chunk;
        this.posInChunk = posInChunk;
    }
    
    // Air
    public BlockState(Chunk chunk, Pos blockInChunkPos) {
        this(null, chunk, blockInChunkPos);
    }
    
    public boolean isAir() {
        return blockType == null;
    }
    
    public boolean getIsTransparent() {
        return isAir() || blockType.getIsTransparent();
    }
    
    public Pos getWorldPos() {
        return chunk.chunkBlockPos.add(posInChunk);
    }
    
}
