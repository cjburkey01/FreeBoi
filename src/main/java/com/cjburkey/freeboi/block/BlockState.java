package com.cjburkey.freeboi.block;

import com.cjburkey.freeboi.chunk.Chunk;
import com.cjburkey.freeboi.value.Pos;
import org.joml.Vector3f;

public final class BlockState {
    
    public final BlockType blockType;
    public final Chunk chunk;
    public final Pos blockPos;
    private final Vector3f worldPos = new Vector3f();
    public final Pos blockInChunkPos;
    
    public BlockState(BlockType blockType, Chunk chunk, Pos blockInChunkPos) {
        this.blockType = blockType;
        this.chunk = chunk;
        this.blockInChunkPos = blockInChunkPos;
        blockPos = chunk.chunkBlockPos.add(blockInChunkPos);
        worldPos.set(blockPos.x, blockPos.y, blockPos.z);
    }
    
    // Air
    public BlockState(Chunk chunk, Pos blockInChunkPos) {
        this(null, chunk, blockInChunkPos);
    }
    
    public boolean getIsTransparent() {
        return isAir() || blockType.getIsTransparent();
    }
    
    public boolean isAir() {
        return blockType == null;
    }
    
    public Vector3f getWorldPos() {
        return new Vector3f(worldPos);
    }
    
}
