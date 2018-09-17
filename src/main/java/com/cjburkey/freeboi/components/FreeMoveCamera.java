package com.cjburkey.freeboi.components;

import com.cjburkey.freeboi.FreeBoi;
import com.cjburkey.freeboi.ecs.ECSComponent;
import com.cjburkey.freeboi.input.Input;
import com.cjburkey.freeboi.input.Key;
import com.cjburkey.freeboi.util.Util;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class FreeMoveCamera extends ECSComponent {
    
    private final Vector2f rotation = new Vector2f().zero();
    private final Vector3f move = new Vector3f().zero();
    
    public float moveSpeed = 8.0f;
    public float fastSpeed = 32.0f;
    public float rotationSpeed = 0.25f;
    
    public FreeMoveCamera() {
        Input.mouseLocked.set(true);
    }
    
    public void onUpdate() {
        if (Input.mouseLocked.get()) {
            rotation.add(Input.getDeltaMouse().mul(rotationSpeed));
            rotation.y = Util.clamp(rotation.y, -90.0f, 90.0f);
            
            move.set(Input.getAxis("Horizontal"), 0.0f, -Input.getAxis("Vertical"));
            if (move.x != 0.0f || move.y != 0.0f || move.z != 0.0f) {
                move.normalize().set(getTransform().transformDir(move));
            }
            if (Input.getIsKeyDown(Key.SPACE)) {
                move.y += 1.0f;
            }
            if (Input.getIsKeyDown(Key.LEFT_SHIFT)) {
                move.y -= 1.0f;
            }
            move.mul(FreeBoi.instance.getDeltaTime() * (Input.getIsKeyDown(Key.LEFT_CONTROL) ? fastSpeed : moveSpeed));
        }
        
        getTransform().position.add(move);
        getTransform().rotation.identity().rotationXYZ(Util.deg2Rad(rotation.y), Util.deg2Rad(rotation.x), 0.0f);
    }
    
}
