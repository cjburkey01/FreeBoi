package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.shader.Shader;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL31.*;


public abstract class Mesh {
    
    private static Mesh currentMesh;
    
    protected final int vao;
    protected final IntOpenHashSet locations = new IntOpenHashSet();
    
    public final int triangleCount;
    private final int vbo;
    private final int ebo;
    private boolean valid;
    
    public Mesh(float[] vertices, short[] indices) {
        vao = glGenVertexArrays();

        triangleCount = indices.length;
        
        vbo = bufferDataVec3(GL_ARRAY_BUFFER, GL_STATIC_DRAW, 0, vertices);
        ebo = bufferDataUShort(GL_ELEMENT_ARRAY_BUFFER, GL_STATIC_DRAW, -1, indices);
        
        valid = true;
    }
    
    protected final int bufferDataShort(int bindLocation, int usage, int location, short[] data) {
        return rawBufferData(bindLocation, usage, location, 1, GL_SHORT, (bL, u) -> glBufferData(bL, data, u));
    }
    
    protected final int bufferDataUShort(int bindLocation, int usage, int location, short[] data) {
        return rawBufferData(bindLocation, usage, location, 1, GL_UNSIGNED_SHORT, (bL, u) -> glBufferData(bL, data, u));
    }
    
    protected final int bufferDataVec2(int bindLocation, int usage, int location, float[] data) {
        return rawBufferData(bindLocation, usage, location, 2, GL_FLOAT, (bL, u) -> glBufferData(bL, data, u));
    }
    
    protected final int bufferDataVec3(int bindLocation, int usage, int location, float[] data) {
        return rawBufferData(bindLocation, usage, location, 3, GL_FLOAT, (bL, u) -> glBufferData(bL, data, u));
    }
    
    protected final int bufferDataVec4(int bindLocation, int usage, int location, float[] data) {
        return rawBufferData(bindLocation, usage, location, 4, GL_FLOAT, (bL, u) -> glBufferData(bL, data, u));
    }
    
    private final int rawBufferData(int bindLocation, int usage, int location, int size, int itype, IBufferTarget type) {
        bind();
        
        int id = glGenBuffers();
        glBindBuffer(bindLocation, id);
        type.call(bindLocation, usage);
        if (location >= 0 && size > 0) {
            locations.add(location);
            glEnableVertexAttribArray(location);
            glVertexAttribPointer(location, size, itype, false, 0, 0);
            glDisableVertexAttribArray(location);
        }
        glBindBuffer(bindLocation, 0);
        
        unbind();
        return id;
    }
    
    public final void bind() {
        if (currentMesh != null) {
            currentMesh.unbind();
        }
        
        glBindVertexArray(vao);
        if (ebo > 0) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        }
        for (int location : locations) {
            glEnableVertexAttribArray(location);
        }
        
        onBind();
        
        currentMesh = this;
    }
    
    public final void unbind() {
        if (currentMesh != this) {
            return;
        }
        
        for (int location : locations) {
            glDisableVertexAttribArray(location);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        
        onUnbind();
        
        currentMesh = null;
    }
    
    protected void onBind() {
    }
    
    protected void onUnbind() {
    }
    
    public final void destroy() {
        valid = false;
        
        bind();
        onCleanup();
        unbind();
        
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }
    
    public final boolean getIsValid() {
        return valid;
    }
    
    protected abstract void onCleanup();
    public abstract Shader getShader();
    
    public static Mesh getCurrentMesh() {
        return currentMesh;
    }
    
    @FunctionalInterface
    private interface IBufferTarget {
        
        void call(int bindLocation, int usage);
        
    }
    
}
