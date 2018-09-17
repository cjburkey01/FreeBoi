package com.cjburkey.freeboi.util;

import com.cjburkey.freeboi.block.BlockState;
import com.cjburkey.freeboi.mesh.ChunkMeshBuilder;
import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.Chunk;
import org.joml.Vector2f;

public final class ChunkMesher {
    
    public static ChunkMeshBuilder meshChunk(Chunk chunk) {
        final ChunkMeshBuilder mesh = new ChunkMeshBuilder();
        
        for (int x = 0; x < Chunk.SIZE; x ++) {
            for (int y = 0; y < Chunk.SIZE; y ++) {
                for (int z = 0; z < Chunk.SIZE; z ++) {
                    BlockState block = chunk.getBlock(new Pos(x, y, z));
                    if (block != null && !block.isAir()) {
                        addBlock(mesh, chunk, block);
                    }
                }
            }
        }
        
        return mesh;
    }
    
    private static void addBlock(ChunkMeshBuilder mesh, Chunk chunk, BlockState block) {
        final Vector2f uvMin = block.blockType.getAtlasPos();
        final Vector2f uvMax = uvMin.add(new Vector2f(1.0f / 32.0f, 1.0f / 32.0f), new Vector2f());
        
        // Front
        if (chunk.isTransparent(block.blockInChunkPos.south())) {
            MeshUtil.addSquare(mesh, block.getPosInChunk(), Util.right(), Util.up(), uvMin, uvMax);
        }
        // Back
        if (chunk.isTransparent(block.blockInChunkPos.north())) {
            MeshUtil.addSquare(mesh, block.getPosInChunk().add(Util.right()).add(Util.forward()), Util.left(), Util.up(), uvMin, uvMax);
        }
        // Right
        if (chunk.isTransparent(block.blockInChunkPos.east())) {
            MeshUtil.addSquare(mesh, block.getPosInChunk().add(Util.right()), Util.forward(), Util.up(), uvMin, uvMax);
        }
        // Left
        if (chunk.isTransparent(block.blockInChunkPos.west())) {
            MeshUtil.addSquare(mesh, block.getPosInChunk().add(Util.forward()), Util.back(), Util.up(), uvMin, uvMax);
        }
        // Top
        if (chunk.isTransparent(block.blockInChunkPos.up())) {
            MeshUtil.addSquare(mesh, block.getPosInChunk().add(Util.up()), Util.right(), Util.forward(), uvMin, uvMax);
        }
        // Bottom
        if (chunk.isTransparent(block.blockInChunkPos.down())) {
            MeshUtil.addSquare(mesh, block.getPosInChunk().add(Util.forward()), Util.right(), Util.back(), uvMin, uvMax);
        }
    }
    
}
