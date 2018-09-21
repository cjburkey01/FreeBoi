package com.cjburkey.freeboi.world.generation;

import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.util.Rand;
import com.cjburkey.freeboi.util.Util;
import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.Chunk;
import com.cjburkey.freeboi.world.World;
import org.joml.Vector3f;

public class ChunkGeneratorTest implements IChunkGenerator {
    
    private static final BlockType testBlockType = new BlockType("freeboi", "test", 3, 3);
    
    public void generate(Chunk chunk) {
        if (chunk.getIsGenerated()) {
            return;
        }
        for (int y = 0; y < World.chunkHeight; y ++) {
            for (int x = 0; x < World.chunkWidth; x ++) {
                for (int z = 0; z < World.chunkWidth; z ++) {
                    Pos p = new Pos(chunk.chunkBlockPos.x + x, chunk.chunkBlockPos.y + y, chunk.chunkBlockPos.z + z);
                    if (Rand.rIntI(0, Util.floor(Vector3f.lengthSquared(p.x, p.y, p.z) / 10.0f)) == 0) {
                        chunk.setBlock(new Pos(x, y, z), testBlockType);
                    }
                }
            }
        }
    }
    
}
