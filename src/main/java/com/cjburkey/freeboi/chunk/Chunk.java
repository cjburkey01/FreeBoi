package com.cjburkey.freeboi.chunk;

import com.cjburkey.freeboi.block.BlockState;
import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.value.Pos;
import java.util.Objects;

public final class Chunk {
    
    public static final int chunkSize = 16;
    
    public final Pos chunkPos;
    public final Pos chunkBlockPos;
    
    private BlockState[] blocks = new BlockState[chunkSize * chunkSize * chunkSize];
    
    public Chunk(Pos chunkPos) {
        this.chunkPos = chunkPos;
        chunkBlockPos = chunkPos.mul(chunkSize);
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
        return pos.x < 0 || pos.y < 0 || pos.z < 0 || pos.x >= chunkSize || pos.y >= chunkSize || pos.z >= chunkSize;
    }
    
    private int index(Pos pos) {
        return pos.x * chunkSize * chunkSize + pos.y * chunkSize + pos.z;
    }
    
}
