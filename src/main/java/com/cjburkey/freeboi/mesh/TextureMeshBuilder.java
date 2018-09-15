package com.cjburkey.freeboi.mesh;

import it.unimi.dsi.fastutil.floats.FloatArrayList;

public class TextureMeshBuilder<T extends TextureMesh> extends MeshBuilder<T> {
    
    public final FloatArrayList uvs;
    
    public TextureMeshBuilder() {
        this.uvs = new FloatArrayList();
    }
    
    public T buildMesh() {
        return (T) (new TextureMesh(toArray(verts), toArray(uvs), toArray(inds)));
    }
    
}
