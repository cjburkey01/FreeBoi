package com.cjburkey.freeboi.components;

import com.cjburkey.freeboi.ecs.ECSComponent;
import com.cjburkey.freeboi.mesh.Mesh;
import com.cjburkey.freeboi.mesh.MeshRenderHelper;
import com.cjburkey.freeboi.value.Property;

public final class MeshRenderer extends ECSComponent {
    
    public final Property<Mesh> mesh = new Property<>();
    public boolean cleanup = true;
    private MeshRenderHelper meshRenderer;
    
    public MeshRenderer() {
        mesh.addListener((o, n) -> meshRenderer = new MeshRenderHelper(n));
    }
    
    public void onRender() {
        if (meshRenderer == null || !meshRenderer.canRender()) {
            return;
        }
        meshRenderer.render(Camera.getMain(), getTransform());
    }
    
    public void onCleanup() {
        if (cleanup) {
            mesh.get().destroy();
        }
    }
    
}
