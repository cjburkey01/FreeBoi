package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.components.Camera;
import com.cjburkey.freeboi.components.Transform;
import com.cjburkey.freeboi.shader.Shader;

import static org.lwjgl.opengl.GL11.*;

public class MeshRenderHelper {

    public final Mesh mesh;
    
    public MeshRenderHelper(Mesh mesh) {
        if (mesh == null || !mesh.getIsValid() || mesh.getShader() == null || !mesh.getShader().isValid()) {
            throw new RuntimeException("Invalid mesh or shader in MeshRenderHelper");
        }
        
        this.mesh = mesh;
    }
    
    public final void render(Camera camera, Transform transform) {
        if (camera == null || transform == null || cannotRender()) {
            return;
        }
        
        mesh.getShader().bind();
        if (mesh.getShader().getIsProjectionMatrixEnabled()) {
            mesh.getShader().setUniform("projectionMatrix", camera.getProjectionMatrix());
        }
        if (mesh.getShader().getIsViewMatrixEnabled()) {
            mesh.getShader().setUniform("viewMatrix", camera.getViewMatrix());
        }
        if (mesh.getShader().getIsModelMatrixEnabled()) {
            mesh.getShader().setUniform("modelMatrix", transform.getModelMatrix());
        }
        
        mesh.bind();
        glDrawElements(GL_TRIANGLES, mesh.triangleCount, GL_UNSIGNED_SHORT, 0L);
        mesh.unbind();
        
        Shader.unbind();
    }
    
    public boolean cannotRender() {
        return (mesh == null || !mesh.getIsValid() || mesh.getShader() == null || !mesh.getShader().isValid());
    }
    
}
