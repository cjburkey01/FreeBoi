package com.cjburkey.freeboi.block;

import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.Chunk;

public final class BlockState {
    
    public final BlockType blockType;
    public final Chunk chunk;
    public final boolean air;
    public final int posInChunkX;
    public final int posInChunkY;
    public final int posInChunkZ;
    
    private Pos blockPosInChunk;
    
    public BlockState(BlockType blockType, Chunk chunk, int posInChunkX, int posInChunkY, int posInChunkZ) {
        this.blockType = blockType;
        air = blockType == null;
        this.chunk = chunk;
        this.posInChunkX = posInChunkX;
        this.posInChunkY = posInChunkY;
        this.posInChunkZ = posInChunkZ;
    }
    
    public boolean getIsTransparent() {
        return air || blockType.getIsTransparent();
    }
    
    public Pos getPosInChunk() {
        if (blockPosInChunk == null) {
            blockPosInChunk = new Pos(posInChunkX, posInChunkY, posInChunkZ);
        }
        return blockPosInChunk;
    }
    
    public Pos getWorldPos() {
        return chunk.getChunkWorldPos().add(posInChunkX, posInChunkY, posInChunkZ);
    }
    
}
