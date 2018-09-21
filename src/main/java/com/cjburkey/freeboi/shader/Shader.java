package com.cjburkey.freeboi.shader;

import com.cjburkey.freeboi.util.Debug;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class Shader {
    
    private final int program;
    private boolean valid = false;
    private int vertex = -1;
    private int geometry = -1;
    private int fragment = -1;
    private final ObjectOpenHashSet<String> queuedUniforms = new ObjectOpenHashSet<>();
    private final Object2ObjectOpenHashMap<String, Integer> uniforms = new Object2ObjectOpenHashMap<>();
    
    private boolean projectionEnabled;
    private boolean viewEnabled;
    private boolean modelEnabled;
    
    Shader() {
        program = glCreateProgram();
    }
    
    private void registerUniform(String name) {
        if (uniforms.containsKey(name)) {
            Debug.warn("Uniform by name \"{}\" is already registered", name);
            return;
        }
        int loc = glGetUniformLocation(program, name);
        if (loc >= 0) {
            switch (name) {
                case "projectionMatrix":
                    projectionEnabled = true;
                    break;
                case "viewMatrix":
                    viewEnabled = true;
                    break;
                case "modelMatrix":
                    modelEnabled = true;
                    break;
            }
            uniforms.put(name, loc);
            return;
        }
        Debug.warn("Uniform by name \"{}\" not used in shader", name);
    }
    
    void queueUniform(String name) {
        queuedUniforms.add(name);
    }
    
    boolean addVertex(String source) {
        if (vertex < 0) {
            vertex = addShader(GL_VERTEX_SHADER, source);
            return vertex >= 0;
        }
        return false;
    }
    
    boolean addGeometry(String source) {
        if (geometry < 0) {
            geometry = addShader(GL_GEOMETRY_SHADER, source);
            return geometry >= 0;
        }
        return false;
    }
    
    boolean addFragment(String source) {
        if (fragment < 0) {
            fragment = addShader(GL_FRAGMENT_SHADER, source);
            return fragment >= 0;
        }
        return false;
    }
    
    boolean build() {
        if (vertex < 0 && geometry < 0 && fragment < 0) {
            Debug.error("Failed to link shader program: no shaders present in shader program");
            return false;
        }
        
        glLinkProgram(program);
        String status = glGetProgramInfoLog(program);
        if (!(status = status.trim()).isEmpty()) {
            Debug.error("Failed to link shader program: {}", status);
            return false;
        }
        glValidateProgram(program);
        status = glGetProgramInfoLog(program);
        if (!(status = status.trim()).isEmpty()) {
            Debug.warn("Failed to validate shader program: {}", status);
        }
        
        if (vertex >= 0) {
            glDetachShader(program, vertex);
            glDeleteShader(vertex);
        }
        if (geometry >= 0) {
            glDetachShader(program, geometry);
            glDeleteShader(geometry);
        }
        if (fragment >= 0) {
            glDetachShader(program, fragment);
            glDeleteShader(fragment);
        }
        
        valid = true;
        
        for (String queuedUniform : queuedUniforms) {
            registerUniform(queuedUniform);
        }
        queuedUniforms.clear();
        
        return true;
    }
    
    private int addShader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);
        String status = glGetShaderInfoLog(shader);
        if (!(status = status.trim()).isEmpty()) {
            Debug.error("Failed to compile shader: {}", status);
            return -2;
        }
        glAttachShader(program, shader);
        return shader;
    }
    
    public void bind() {
        if (!isValid()) {
            Debug.error("Failed to bind shader program: shader program was destroyed");
            return;
        }
        glUseProgram(program);
    }
    
    public void destroy() {
        valid = false;
        glDeleteProgram(program);
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public void setUniform(String name, int value) {
        int loc = checkUniform(name);
        if (loc >= 0) {
            glUniform1i(loc, value);
        }
    }
    
    public void setUniform(String name, float value) {
        int loc = checkUniform(name);
        if (loc >= 0) {
            glUniform1f(loc, value);
        }
    }
    
    public void setUniform(String name, Vector2f value) {
        int loc = checkUniform(name);
        if (loc >= 0) {
            glUniform2f(loc, value.x, value.y);
        }
    }
    
    public void setUniform(String name, Vector3f value) {
        int loc = checkUniform(name);
        if (loc >= 0) {
            glUniform3f(loc, value.x, value.y, value.z);
        }
    }
    
    public void setUniform(String name, Vector4f value) {
        int loc = checkUniform(name);
        if (loc >= 0) {
            glUniform4f(loc, value.x, value.y, value.z, value.w);
        }
    }
    
    public void setUniform(String name, Matrix3f value) {
        int loc = checkUniform(name);
        if (loc >= 0) {
            FloatBuffer matrix = memAllocFloat(9);
            value.get(matrix);
            glUniformMatrix3fv(loc, false, matrix);
            memFree(matrix);
        }
    }
    
    public void setUniform(String name, Matrix4f value) {
        int loc = checkUniform(name);
        if (loc >= 0) {
            FloatBuffer matrix = memAllocFloat(16);
            value.get(matrix);
            glUniformMatrix4fv(loc, false, matrix);
            memFree(matrix);
        }
    }
    
    private int checkUniform(String name) {
        if (uniforms.containsKey(name)) {
            return uniforms.get(name);
        }
        Debug.warn("Uniform \"{}\" not found", name);
        return -1;
    }
    
    public int getProgram() {
        return program;
    }
    
    public int getVertex() {
        return vertex;
    }
    
    public int getGeometry() {
        return geometry;
    }
    
    public int getFragment() {
        return fragment;
    }
    
    public boolean getIsProjectionMatrixEnabled() {
        return projectionEnabled;
    }
    
    public boolean getIsViewMatrixEnabled() {
        return viewEnabled;
    }
    
    public boolean getIsModelMatrixEnabled() {
        return modelEnabled;
    }
    
    public boolean getIsFullTransformEnabled() {
        return projectionEnabled && viewEnabled && modelEnabled;
    }
    
    public static void unbind() {
        glUseProgram(0);
    }
    
}
