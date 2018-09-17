package com.cjburkey.freeboi.mesh;

public class ChunkMesh extends TextureMesh {
    
    public ChunkMesh(float[] vertices, float[] uvs, short[] indices) {
        super(vertices, uvs, indices);
        
        cleanupTexture = false;
    }
    
}
