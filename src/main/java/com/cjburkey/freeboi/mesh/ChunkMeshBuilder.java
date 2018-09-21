package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.util.TimeDebug;

public class ChunkMeshBuilder extends TextureMeshBuilder<ChunkMesh> {
    
    public ChunkMesh buildMesh() {
        TimeDebug.start("chunkMeshBuilder.buildMesh");
        ChunkMesh mesh = new ChunkMesh(toArray(verts), toArray(uvs), toArray(inds));
        TimeDebug.pause("chunkMeshBuilder.buildMesh");
        return mesh;
    }
    
}
