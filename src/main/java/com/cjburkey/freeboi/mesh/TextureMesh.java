package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.Game;
import com.cjburkey.freeboi.shader.Shader;
import com.cjburkey.freeboi.util.Texture;

import static org.lwjgl.opengl.GL15.*;

public class TextureMesh extends Mesh {
    
    private final int uvbo;
    public Texture texture;
    
    public TextureMesh(float[] vertices, float[] uvs, short[] indices) {
        super(vertices, indices);
        
        uvbo = bufferDataVec2(GL_ARRAY_BUFFER, GL_STATIC_DRAW, 1, uvs);
    }
    
    protected void onCleanup() {
        glDeleteBuffers(uvbo);
    }
    
    public Shader getShader() {
        return Game.getBasicTextureShader();
    }
    
    protected void onBind() {
        if (texture != null && texture.getIsValid()) {
            texture.bind();
        }
    }

    protected void onUnbind() {
        Texture.unbind();
    }
    
}
