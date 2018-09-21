package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.components.Camera;
import com.cjburkey.freeboi.components.Transform;
import com.cjburkey.freeboi.shader.Shader;
import com.cjburkey.freeboi.util.TimeDebug;

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
        
        TimeDebug.start("meshRenderHelper.render.bindShader");
        mesh.getShader().bind();
        TimeDebug.pause("meshRenderHelper.render.bindShader");
        
        TimeDebug.start("meshRenderHelper.render.matrixUniform");
        TimeDebug.start("meshRenderHelper.render.matrixUniform.projection");
        if (mesh.getShader().getIsProjectionMatrixEnabled()) {
            mesh.getShader().setUniform("projectionMatrix", camera.getProjectionMatrix());
        }
        TimeDebug.pause("meshRenderHelper.render.matrixUniform.projection");
        TimeDebug.start("meshRenderHelper.render.matrixUniform.view");
        if (mesh.getShader().getIsViewMatrixEnabled()) {
            mesh.getShader().setUniform("viewMatrix", camera.getViewMatrix());
        }
        TimeDebug.pause("meshRenderHelper.render.matrixUniform.view");
        TimeDebug.start("meshRenderHelper.render.matrixUniform.model");
        if (mesh.getShader().getIsModelMatrixEnabled()) {
            mesh.getShader().setUniform("modelMatrix", transform.getModelMatrix());
        }
        TimeDebug.pause("meshRenderHelper.render.matrixUniform.model");
        TimeDebug.pause("meshRenderHelper.render.matrixUniform");
        
        TimeDebug.start("meshRenderHelper.render.renderMesh.bind");
        mesh.bind();
        TimeDebug.pause("meshRenderHelper.render.renderMesh.bind");
        TimeDebug.start("meshRenderHelper.render.renderMesh");
        glDrawElements(GL_TRIANGLES, mesh.triangleCount, GL_UNSIGNED_SHORT, 0L);
        TimeDebug.pause("meshRenderHelper.render.renderMesh");
        TimeDebug.start("meshRenderHelper.render.renderMesh.unbind");
        mesh.unbind();
        TimeDebug.pause("meshRenderHelper.render.renderMesh.unbind");
        
        TimeDebug.start("meshRenderHelper.render.unbindShader");
        Shader.unbind();
        TimeDebug.pause("meshRenderHelper.render.unbindShader");
    }
    
    public boolean cannotRender() {
        return (mesh == null || !mesh.getIsValid() || mesh.getShader() == null || !mesh.getShader().isValid());
    }
    
}
