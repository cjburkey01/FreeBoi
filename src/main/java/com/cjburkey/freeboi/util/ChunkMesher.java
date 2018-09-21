package com.cjburkey.freeboi.util;

import com.cjburkey.freeboi.block.BlockState;
import com.cjburkey.freeboi.mesh.ChunkMeshBuilder;
import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.Chunk;
import com.cjburkey.freeboi.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class ChunkMesher {
    
    public static ChunkMeshBuilder meshChunk(Chunk chunk) {
        final ChunkMeshBuilder mesh = new ChunkMeshBuilder();
        
        for (int y = 0; y < World.chunkHeight; y ++) {
            for (int x = 0; x < World.chunkWidth; x ++) {
                for (int z = 0; z < World.chunkWidth; z ++) {
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
        final Vector3f blockPos = new Vector3f(block.posInChunk.x, block.posInChunk.y, block.posInChunk.z);
        // Front
        if (chunk.isTransparent(block.posInChunk.south())) {
            MeshUtil.addSquare(mesh, blockPos, Util.right(), Util.up(), uvMin, uvMax);
        }
        // Back
        if (chunk.isTransparent(block.posInChunk.north())) {
            MeshUtil.addSquare(mesh, blockPos.add(Util.right(), new Vector3f()).add(Util.forward()), Util.left(), Util.up(), uvMin, uvMax);
        }
        // Right
        if (chunk.isTransparent(block.posInChunk.east())) {
            MeshUtil.addSquare(mesh, blockPos.add(Util.right(), new Vector3f()), Util.forward(), Util.up(), uvMin, uvMax);
        }
        // Left
        if (chunk.isTransparent(block.posInChunk.west())) {
            MeshUtil.addSquare(mesh, blockPos.add(Util.forward(), new Vector3f()), Util.back(), Util.up(), uvMin, uvMax);
        }
        // Top
        if (chunk.isTransparent(block.posInChunk.up())) {
            MeshUtil.addSquare(mesh, blockPos.add(Util.up(), new Vector3f()), Util.right(), Util.forward(), uvMin, uvMax);
        }
        // Bottom
        if (chunk.isTransparent(block.posInChunk.down())) {
            MeshUtil.addSquare(mesh, blockPos.add(Util.forward(), new Vector3f()), Util.right(), Util.back(), uvMin, uvMax);
        }
    }
    
}
