package com.cjburkey.freeboi.components;

import com.cjburkey.freeboi.ecs.ECSComponent;
import com.cjburkey.freeboi.mesh.Mesh;
import com.cjburkey.freeboi.mesh.MeshRenderHelper;
import com.cjburkey.freeboi.value.Property;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public final class MeshRenderer extends ECSComponent {
    
    private UUID uuid = UUID.randomUUID();
    private static final ObjectOpenHashSet<MeshRenderer> renderers = new ObjectOpenHashSet<>();
    public final Property<Mesh> mesh = new Property<>();
    public boolean cleanup = true;
    private MeshRenderHelper meshRenderer;
    
    public MeshRenderer() {
        mesh.addListener((o, n) -> meshRenderer = new MeshRenderHelper(n));
        renderers.add(this);
    }
    
    public void onRender() {
//        Frustum Culling
//        
//        if (mesh.get() != null) {
//            boolean in = mesh.get().getInsideFrustum();
//            if (!in) {
//                return;
//            }
//        }
        if (meshRenderer != null && !meshRenderer.cannotRender()) {
            meshRenderer.render(Camera.getMain(), getTransform());
        }
    }
    
    public void onCleanup() {
        renderers.remove(this);
        if (cleanup) {
            mesh.get().destroy();
        }
    }
    
    public static Collection<MeshRenderer> getRenderers() {
        return renderers;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeshRenderer that = (MeshRenderer) o;
        return Objects.equals(uuid, that.uuid);
    }
    
    public int hashCode() {
        return Objects.hash(uuid);
    }
    
}
