package com.cjburkey.freeboi.world;

import com.cjburkey.freeboi.block.BlockState;
import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.util.Util;
import com.cjburkey.freeboi.value.Pos;
import java.util.Objects;
import org.joml.Vector3f;

import static com.cjburkey.freeboi.world.World.*;

public final class Chunk {
    
    public final World world;
    public final Pos chunkPos;
    public final Pos chunkBlockPos;
    
    private BlockState[] blocks = null;
    private boolean generated = false;
    private boolean generating = false;
    private ECSEntity entity;
    
    public Chunk(World world, Pos chunkPos) {
        this.world = world;
        this.chunkPos = chunkPos;
        chunkBlockPos = chunkPosToBlockPos(chunkPos);
    }
    
    public void init() {
        blocks = new BlockState[chunkWidth * chunkWidth * chunkHeight];
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
    
    public void markGenerated() {
        generated = true;
        generating = false;
    }
    
    public boolean getIsGenerated() {
        return blocks != null && generated;
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
        return pos.x < 0 || pos.y < 0 || pos.z < 0 || pos.x >= chunkWidth || pos.y >= chunkHeight || pos.z >= chunkWidth;
    }
    
    private int index(Pos pos) {
        return pos.y * chunkWidth * chunkWidth + pos.x * chunkWidth + pos.z;
    }
    
    public static Pos chunkPosToBlockPos(Pos chunkPos) {
        return new Pos(chunkPos.x * chunkWidth, chunkPos.y * chunkHeight, chunkPos.z * chunkWidth);
    }
    
    public static Pos blockPosToChunkPos(Pos blockPos) {
        return new Pos(Util.divFloor(blockPos.x, chunkWidth), Util.divFloor(blockPos.x, chunkHeight), Util.divFloor(blockPos.x, chunkWidth));
    }
    
}
