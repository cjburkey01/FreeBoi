package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.components.Transform;
import com.cjburkey.freeboi.world.World;
import org.joml.Vector3f;

public class ChunkMesh extends TextureMesh {
    
    public ChunkMesh(float[] vertices, float[] uvs, short[] indices) {
        super(vertices, uvs, indices);
        
        cleanupTexture = false;
    }
    
    public Vector3f getCenter() {
        float s = World.chunkWidth / 2.0f;
        return new Vector3f(s, World.chunkHeight / 2.0f, s);
    }
    
    public float getBoundingRadius(Transform transform) {
        return transform.scale.lengthSquared();
    }
    
}
