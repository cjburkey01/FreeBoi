package com.cjburkey.freeboi.components;

import com.cjburkey.freeboi.FreeBoi;
import com.cjburkey.freeboi.ecs.ECSComponent;
import com.cjburkey.freeboi.value.Property;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Camera extends ECSComponent {
    
    private static Camera main;
    
    public final Property<Float> fov = new Property<>(75.0f);
    public final Property<Float> near = new Property<>(0.01f);
    public final Property<Float> far = new Property<>(1000.0f);
    private float aspect = 0.0f;
    
    private final Matrix4f projectionMatrix = new Matrix4f().identity();
    private final Matrix4f viewMatrix = new Matrix4f().identity();
    
    public Camera() {
        if (main == null) {
            main = this;
        }
        
        fov.addListener((o, n) -> updateProjectionMatrix(n, near.get(), far.get()));
        near.addListener((o, n) -> updateProjectionMatrix(fov.get(), n, far.get()));
        far.addListener((o, n) -> updateProjectionMatrix(fov.get(), near.get(), n));
        
        FreeBoi.instance.windowSize.addListener((o, n) -> {
            setAspect(n);
            updateProjectionMatrix(fov.get(), near.get(), far.get());
        });
        
        setAspect(FreeBoi.instance.windowSize.get());
        updateProjectionMatrix(fov.get(), near.get(), far.get());
    }
    
    private void setAspect(Vector2i size) {
        aspect = (float) size.x / size.y;
    }
    
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    
    private void updateProjectionMatrix(float fov, float near, float far) {
        projectionMatrix.identity().perspective((float) Math.toRadians(fov), aspect, near, far);
    }
    
    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotate(getTransform().rotation).translate(getTransform().position.negate(new Vector3f()));
    }
    
    public void makeMain() {
        main = this;
    }
    
    public boolean cameraExists() {
        return main != null;
    }
    
    public static Camera getMain() {
        return main;
    }
    
}
