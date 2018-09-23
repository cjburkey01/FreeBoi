package com.cjburkey.freeboi.world;

import com.cjburkey.freeboi.block.BlockState;
import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.data.DataHandler;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.util.Util;
import com.cjburkey.freeboi.value.Pos;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Objects;

import static com.cjburkey.freeboi.world.World.*;

public final class Chunk {
    
    public final World world;
    public final int x;
    public final int y;
    public final int z;
    
    //private BlockType[] blocks = null;
    private final Int2ObjectOpenHashMap<BlockType> blockMap = new Int2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<Pos, DataHandler> blockData = new Object2ObjectOpenHashMap<>();
    private boolean generated = false;
    private boolean generating = false;
    private ECSEntity entity;
    private Pos chunkPos;
    private Pos chunkWorldPos;
    
    public Chunk(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    void initArray() {
        //blocks = new BlockType[chunkWidth * chunkWidth * chunkHeight];
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
        return /*blocks != null && */generated;
    }
    
    public boolean getIsGenerating() {
        return generating;
    }
    
    public BlockState getBlock(int x, int y, int z) {
        if (invalid(x, y, z)) {
            return null;
        }
        //return new BlockState(blocks[index(x, y, z)], this, x, y, z);
        return new BlockState(blockMap.get(index(x, y, z)), this, x, y, z);
    }
    
    public BlockState getBlock(Pos pos) {
        return getBlock(pos.x, pos.y, pos.z);
    }
    
    public boolean isAir(int x, int y, int z) {
        if (invalid(x, y, z)) {
            return true;
        }
        BlockState at = getBlock(x, y, z);
        return at == null || at.air;
    }
    
    public boolean isAir(Pos pos) {
        return isAir(pos.x, pos.y, pos.z);
    }
    
    public boolean isTransparent(int x, int y, int z) {
        if (invalid(x, y, z) || isAir(x, y, z)) {
            return true;
        }
        return Objects.requireNonNull(getBlock(x, y, z)).getIsTransparent();
    }
    
    public boolean isTransparent(Pos pos) {
        return isTransparent(pos.x, pos.y, pos.z);
    }
    
    public void setBlock(int x, int y, int z, BlockType blockType) {
        if (!invalid(x, y, z)) {
            //blocks[index(x, y, z)] = blockType;
            blockMap.put(index(x, y, z), blockType);
        }
    }
    
    private boolean invalid(int x, int y, int z) {
        return x < 0 || y < 0 || z < 0 || x >= chunkWidth || y >= chunkHeight || z >= chunkWidth;
    }
    
    private boolean invalid(Pos pos) {
        return invalid(pos.x, pos.y, pos.z);
    }

    private int index(int x, int y, int z) {
        return y * chunkWidth * chunkWidth + x * chunkWidth + z;
    }
    
    private int index(Pos pos) {
        return index(pos.x, pos.y, pos.z);
    }
    
    public Pos getChunkPos() {
        if (chunkPos == null) {
            chunkPos = new Pos(x, y, z);
        }
        return chunkPos;
    }
    
    public Pos getChunkWorldPos() {
        if (chunkWorldPos == null) {
            chunkWorldPos = new Pos(x * chunkWidth, y * chunkHeight, z * chunkWidth);
        }
        return chunkWorldPos;
    }
    
    public static Pos chunkPosToBlockPos(Pos chunkPos) {
        return new Pos(chunkPos.x * chunkWidth, chunkPos.y * chunkHeight, chunkPos.z * chunkWidth);
    }
    
    public static Pos blockPosToChunkPos(Pos blockPos) {
        return new Pos(Util.divFloor(blockPos.x, chunkWidth), Util.divFloor(blockPos.x, chunkHeight), Util.divFloor(blockPos.x, chunkWidth));
    }
    
}
