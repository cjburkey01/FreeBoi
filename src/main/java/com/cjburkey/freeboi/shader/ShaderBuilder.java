package com.cjburkey.freeboi.shader;

import com.cjburkey.freeboi.util.IO;

public class ShaderBuilder {
    
    public boolean projectionMatrixEnabled = true;
    public boolean viewMatrixEnabled = true;
    public boolean modelMatrixEnabled = true;
    
    public boolean vertexEnabled;
    public boolean geometryEnabled;
    public boolean fragmentEnabled;
    
    public final ShaderCodeBuilder vertex = new ShaderCodeBuilder("VERTEX");
    public final ShaderCodeBuilder geometry = new ShaderCodeBuilder("GEOMETRY");
    public final ShaderCodeBuilder fragment = new ShaderCodeBuilder("FRAGMENT");
    
    public Shader buildFromStrings(String vertexString, String geometryString, String fragmentString) {
        Shader shader = new Shader();
        for (ShaderVariable uniform : vertex.getUniforms()) {
            shader.queueUniform(uniform.name);
        }
        for (ShaderVariable uniform : geometry.getUniforms()) {
            shader.queueUniform(uniform.name);
        }
        for (ShaderVariable uniform : fragment.getUniforms()) {
            shader.queueUniform(uniform.name);
        }
        if (vertexString != null && !shader.addVertex(vertexString)) {
            return null;
        }
        if (geometryString != null && !shader.addGeometry(geometryString)) {
            return null;
        }
        if (fragmentString != null && !shader.addFragment(fragmentString)) {
            return null;
        }
        if (shader.build()) {
            return shader;
        }
        return null;
    }
    
    public Shader buildFromResources(String vertexFile, String geometryFile, String fragmentFile) {
        String vertexString = null;
        String geometryString = null;
        String fragmentString = null;
        if (vertexFile != null) {
            vertexString = IO.readResourceFile(vertexFile);
        }
        if (geometryFile != null) {
            geometryString = IO.readResourceFile(geometryFile);
        }
        if (fragmentFile != null) {
            fragmentString = IO.readResourceFile(fragmentFile);
        }
        return buildFromStrings(vertexString, geometryString, fragmentString);
    }
    
    public Shader build() {
        String vertexString = null;
        String geometryString = null;
        String fragmentString = null;
        if (!vertexEnabled && !geometryEnabled && !fragmentEnabled) {
            return null;
        }
        boolean addedTransformUniforms = false;
        if (vertexEnabled) {
            addTransformUniforms(vertex);
            addedTransformUniforms = true;
            vertexString = vertex.getCode();
        }
        if (geometryEnabled) {
            if (!addedTransformUniforms) {
                addTransformUniforms(geometry);
                addedTransformUniforms = true;
            }
            geometryString = geometry.getCode();
        }
        if (fragmentEnabled) {
            if (!addedTransformUniforms) {
                addTransformUniforms(fragment);
            }
            fragmentString = fragment.getCode();
        }
        return buildFromStrings(vertexString, geometryString, fragmentString);
    }
    
    public void setTransformed(boolean transformed) {
        projectionMatrixEnabled = transformed;
        viewMatrixEnabled = transformed;
        modelMatrixEnabled = transformed;
    }
    
    private void addTransformUniforms(ShaderCodeBuilder shader) {
        if (projectionMatrixEnabled && viewMatrixEnabled && modelMatrixEnabled) {
            shader.addFunction("vec4", "transform3", new String[] { "vec3" }, "pos");
            shader.addLineToFunction("transform3", "return transform(vec4(pos, 1.0))");
            shader.addFunction("vec4", "transform", new String[] { "vec4" }, "pos");
            shader.addLineToFunction("transform", "return projection(view(model(pos)))");
        }
        if (projectionMatrixEnabled) {
            shader.addFunction("vec4", "projection", new String[] { "vec4" }, "pos");
            shader.addLineToFunction("projection", "return projectionMatrix * pos");
            shader.addUniform("mat4", "projectionMatrix");
        }
        if (viewMatrixEnabled) {
            shader.addFunction("vec4", "view", new String[] { "vec4" }, "pos");
            shader.addLineToFunction("view", "return viewMatrix * pos");
            shader.addUniform("mat4", "viewMatrix");
        }
        if (modelMatrixEnabled) {
            shader.addFunction("vec4", "model", new String[] { "vec4" }, "pos");
            shader.addLineToFunction("model", "return modelMatrix * pos");
            shader.addUniform("mat4", "modelMatrix");
        }
    }
    
}
