package com.cjburkey.freeboi.mesh;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;

public abstract class MeshBuilder<T extends Mesh> {
    
    public final FloatArrayList verts;
    public final ShortArrayList inds;
    
    public MeshBuilder() {
        this.verts = new FloatArrayList();
        this.inds = new ShortArrayList();
    }
    
    public abstract T buildMesh();
    
    protected static float[] toArray(FloatArrayList arrayList) {
        return arrayList.toArray(new float[0]);
    }
    
    protected static short[] toArray(ShortArrayList arrayList) {
        return arrayList.toArray(new short[0]);
    }
    
}
