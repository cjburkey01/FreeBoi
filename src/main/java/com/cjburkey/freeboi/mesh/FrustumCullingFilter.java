package com.cjburkey.freeboi.mesh;

import com.cjburkey.freeboi.components.MeshRenderer;
import com.cjburkey.freeboi.components.Transform;
import java.util.Collection;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class FrustumCullingFilter {
    
    private final Matrix4f matrix = new Matrix4f();
    private final Vector4f[] frustumPlanes = new Vector4f[6];
    
    public FrustumCullingFilter() {
        for (int i = 0; i < frustumPlanes.length; i++) {
            frustumPlanes[i] = new Vector4f();
        }
    }
    
    public void updateFrustum(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        matrix.set(projectionMatrix).mul(viewMatrix);
        for (int i = 0; i < frustumPlanes.length; i++) {
            matrix.frustumPlane(i, frustumPlanes[i]);
        }
    }
    
    private boolean insideFrustum(Vector3f pos, float boundingRadius) {
        for (Vector4f plane : frustumPlanes) {
            if (((plane.x * pos.x) + (plane.y * pos.y) + (plane.z * pos.z) + plane.w) <= -boundingRadius) {
                return false;
            }
        }
        return true;
    }
    
    public void filterMesh(Transform transform, Mesh mesh) {
        mesh.setInsideFrustum(insideFrustum(transform.position, mesh.getBoundingRadius(transform)));
    }
    
    public void filterMeshes(Collection<MeshRenderer> renderers) {
        renderers.forEach((renderer) -> filterMesh(renderer.getTransform(), renderer.mesh.get()));
    }
    
}
