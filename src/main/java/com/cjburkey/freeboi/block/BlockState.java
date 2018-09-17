package com.cjburkey.freeboi.block;

import com.cjburkey.freeboi.world.Chunk;
import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.World;
import org.joml.Vector3f;

public final class BlockState {
    
    public final World world;
    public final BlockType blockType;
    public final Chunk chunk;
    public final Pos blockPos;
    private final Vector3f chunkPos = new Vector3f();
    private final Vector3f worldPos = new Vector3f();
    public final Pos blockInChunkPos;
    
    public BlockState(BlockType blockType, Chunk chunk, Pos blockInChunkPos) {
        world = chunk.world;
        this.blockType = blockType;
        this.chunk = chunk;
        this.blockInChunkPos = blockInChunkPos;
        blockPos = chunk.chunkBlockPos.add(blockInChunkPos);
        worldPos.set(blockPos.x, blockPos.y, blockPos.z);
        chunkPos.set(blockInChunkPos.x, blockInChunkPos.y, blockInChunkPos.z);
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
    
    public Vector3f getWorldPos() {
        return new Vector3f(worldPos);
    }
    
    public Vector3f getPosInChunk() {
        return new Vector3f(chunkPos);
    }
    
}
