package com.cjburkey.freeboi.mesh;

public class ChunkMeshBuilder extends TextureMeshBuilder<ChunkMesh> {
    
    public ChunkMesh buildMesh() {
        return new ChunkMesh(toArray(verts), toArray(uvs), toArray(inds));
    }
    
}
