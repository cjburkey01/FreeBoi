package com.cjburkey.freeboi.util.noise;

import com.cjburkey.freeboi.value.Pos;
import org.joml.Vector3f;

public class NoiseSettings {
    
    public final NoiseLayer[] layers;
    
    public NoiseSettings(NoiseLayer... layers) {
        this.layers = layers;
    }
    
    public float generate(Vector3f pos) {
        return layers[0].generate(pos);
    }
    
    public float generate(float x, float y, float z) {
        return layers[0].generate(x, y, z);
    }
    
    public float generate(Pos pos) {
        return generate(pos.x, pos.y, pos.z);
    }
    
}
