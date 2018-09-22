package com.cjburkey.freeboi.world.generation;

import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.util.noise.NoiseLayer;
import com.cjburkey.freeboi.util.noise.NoiseSettings;
import com.cjburkey.freeboi.world.Chunk;
import com.cjburkey.freeboi.world.World;

public class ChunkGeneratorOverworld implements IChunkGenerator {
    
    private static final BlockType testBlockType = new BlockType("freeboi", "test", 3, 3);
    private static final NoiseSettings noiseSettings = new NoiseSettings(new NoiseLayer(1.0f, 0.01f));
    
    public void generate(Chunk chunk) {
        if (chunk.getIsGenerated()) {
            return;
        }
        for (int y = 0; y < World.chunkHeight; y ++) {
            for (int x = 0; x < World.chunkWidth; x ++) {
                for (int z = 0; z < World.chunkWidth; z ++) {
                    //Pos p = new Pos(chunk.chunkBlockPos.x + x, chunk.chunkBlockPos.y + y, chunk.chunkBlockPos.z + z);
                    //if (Rand.rIntI(0, Util.floor(Vector3f.lengthSquared(p.x, p.y, p.z)) / 100) == 0) {
                        //chunk.setBlock(x, y, z, testBlockType);
                    //}
                    float noise = noiseSettings.generate(chunk.getChunkWorldPos().x + x, chunk.getChunkWorldPos().y + y, chunk.getChunkWorldPos().z + z);
                    if (noise >= 0.2f) {
                        chunk.setBlock(x, y, z, testBlockType);
                    }
                }
            }
        }
    }
    
}
