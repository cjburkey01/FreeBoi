package com.cjburkey.freeboi.world;

import com.cjburkey.freeboi.block.BlockState;
import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.value.Pos;
import java.util.Objects;

public final class Chunk {
    
    public static final int SIZE = 16;
    
    public final World world;
    public final Pos chunkPos;
    public final Pos chunkBlockPos;
    public final Pos chunkRegion;
    
    private BlockState[] blocks = new BlockState[SIZE * SIZE * SIZE];
    private boolean generated = false;
    private boolean generating = false;
    private ECSEntity entity;
    
    public Chunk(World world, Pos chunkPos) {
        this.world = world;
        this.chunkPos = chunkPos;
        chunkBlockPos = chunkPos.mul(SIZE);
        chunkRegion = chunkPos.divFloor(SIZE);
    }
    
    void setEntity(ECSEntity entity) {
        this.entity = entity;
    }
    
    void remove() {
        if (entity != null) {
            entity.destroy();
        }
    }
    
    void markGenerating() {
        generated = false;
        generating = true;
    }
    
    void markGenerated() {
        generated = true;
        generating = false;
    }
    
    public boolean getIsGenerated() {
        return generated;
    }
    
    public boolean getIsGenerating() {
        return generating;
    }
    
    public BlockState getBlock(Pos pos) {
        if (invalid(pos)) {
            return null;
        }
        return blocks[index(pos)];
    }
    
    public boolean isAir(Pos pos) {
        if (invalid(pos)) {
            return true;
        }
        BlockState at = getBlock(pos);
        return at == null || at.isAir();
    }
    
    public boolean isTransparent(Pos pos) {
        if (invalid(pos) || isAir(pos)) {
            return true;
        }
        return Objects.requireNonNull(getBlock(pos)).getIsTransparent();
    }
    
    public BlockState setBlock(Pos pos, BlockType blockType) {
        if (invalid(pos)) {
            return null;
        }
        BlockState state = new BlockState(blockType, this, pos);
        blocks[index(pos)] = state;
        return state;
    }
    
    private boolean invalid(Pos pos) {
        return pos.x < 0 || pos.y < 0 || pos.z < 0 || pos.x >= SIZE || pos.y >= SIZE || pos.z >= SIZE;
    }
    
    private int index(Pos pos) {
        return pos.x * SIZE * SIZE + pos.y * SIZE + pos.z;
    }
    
}
