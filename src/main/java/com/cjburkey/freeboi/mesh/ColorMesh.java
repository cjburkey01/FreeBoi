package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.Game;
import com.cjburkey.freeboi.shader.Shader;

import static org.lwjgl.opengl.GL15.*;

public class ColorMesh extends Mesh {
    
    private final int cbo;
    
    public ColorMesh(float[] vertices, float[] colors, short[] indices) {
        super(vertices, indices);
        
        cbo = bufferDataVec3(GL_ARRAY_BUFFER, GL_STATIC_DRAW, 1, colors);
    }
    
    protected void onCleanup() {
        glDeleteBuffers(cbo);
    }
    
    public Shader getShader() {
        return null; //Game.getBasicColorShader()
    }
    
}
