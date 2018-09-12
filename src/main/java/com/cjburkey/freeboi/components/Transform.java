package com.cjburkey.freeboi.components;

import com.cjburkey.freeboi.ecs.ECSComponent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform extends ECSComponent {
    
    public final Vector3f position = new Vector3f().zero();
    public final Quaternionf rotation = new Quaternionf().identity();
    public final Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    private final Vector3f prevPos = new Vector3f(position);
    private final Quaternionf prevRot = new Quaternionf(rotation);
    private final Vector3f prevScale = new Vector3f(scale);
    
    private final Matrix4f modelMatrix = new Matrix4f().identity();
    
    public Transform() {
        updateModelMatrix();
    }
    
    private void checkForUpdate() {
        if (!position.equals(prevPos) || !rotation.equals(prevRot) || !scale.equals(prevScale)) {
            updateModelMatrix();
            prevPos.set(position);
            prevRot.set(rotation);
            prevScale.set(scale);
        }
    }
    
    private void updateModelMatrix() {
        modelMatrix.identity().translate(position).rotate(rotation).scale(scale);
    }
    
    public Matrix4f getModelMatrix() {
        checkForUpdate();
        return new Matrix4f(modelMatrix);
    }
    
}
