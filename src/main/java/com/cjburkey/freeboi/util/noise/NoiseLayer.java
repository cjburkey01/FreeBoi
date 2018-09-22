package com.cjburkey.freeboi.util.noise;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class NoiseLayer {
    
    public float scale;
    public float frequency;
    
    public NoiseLayer(float scale, float frequency) {
        this.scale = scale;
        this.frequency = frequency;
    }
    
    public NoiseLayer(float scale) {
        this(scale, 1.0f);
    }
    
    public NoiseLayer() {
        this(1.0f);
    }
    
    public float generate(float x, float y) {
        return scale * (float) SimplexNoise.noise(x * frequency, y * frequency);
    }
    
    public float generate(Vector2f pos) {
        return generate(pos.x, pos.y);
    }
    
    public float generate(float x, float y, float z) {
        return scale * (float) SimplexNoise.noise(x * frequency, y * frequency, z * frequency);
    }
    
    public float generate(Vector3f pos) {
        return generate(pos.x, pos.y, pos.z);
    }
    
    public float generate(float x, float y, float z, float w) {
        return scale * (float) SimplexNoise.noise(x * frequency, y * frequency, z * frequency, w * frequency);
    }
    
    public float generate(Vector4f pos) {
        return generate(pos.x, pos.y, pos.z, pos.w);
    }
    
}
