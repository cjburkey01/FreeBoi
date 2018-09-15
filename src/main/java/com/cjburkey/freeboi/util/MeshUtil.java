package com.cjburkey.freeboi.util;

import com.cjburkey.freeboi.mesh.TextureMeshBuilder;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MeshUtil {
    
    public static void addQuad(TextureMeshBuilder<?> mesh, final Vector3f bL, final Vector3f bR, final Vector3f tR, final Vector3f tL, final Vector2f uvMin, final Vector2f uvMax) {
        short i = (short) (mesh.verts.size() / 3);
        
        mesh.verts.add(bL.x);
        mesh.verts.add(bL.y);
        mesh.verts.add(bL.z);
        mesh.verts.add(bR.x);
        mesh.verts.add(bR.y);
        mesh.verts.add(bR.z);
        mesh.verts.add(tR.x);
        mesh.verts.add(tR.y);
        mesh.verts.add(tR.z);
        mesh.verts.add(tL.x);
        mesh.verts.add(tL.y);
        mesh.verts.add(tL.z);
        
        mesh.inds.add(i);
        mesh.inds.add((short) (i + 1));
        mesh.inds.add((short) (i + 2));
        mesh.inds.add(i);
        mesh.inds.add((short) (i + 2));
        mesh.inds.add((short) (i + 3));
        
        mesh.uvs.add(uvMin.x);
        mesh.uvs.add(uvMax.y);
        mesh.uvs.add(uvMax.x);
        mesh.uvs.add(uvMax.y);
        mesh.uvs.add(uvMax.x);
        mesh.uvs.add(uvMin.y);
        mesh.uvs.add(uvMin.x);
        mesh.uvs.add(uvMin.y);
    }
    
    public static void addSizedQuad(TextureMeshBuilder<?> mesh, final Vector3f corner, final Vector3f right, final Vector3f up, final Vector3f size, final Vector2f uvMin, final Vector2f uvMax) {
        final Vector3f bR = new Vector3f();
        final Vector3f tR = new Vector3f();
        final Vector3f tL = new Vector3f();
        
        corner.add(right.mul(size.x, new Vector3f()), bR);
        bR.add(up.mul(size.y, new Vector3f()), tR);
        corner.add(up.mul(size.y, new Vector3f()), tL);
        
        addQuad(mesh, corner, bR, tR, tL, uvMin, uvMax);
    }
    
    public static void addSquare(TextureMeshBuilder<?> mesh, final Vector3f corner, final Vector3f right, final Vector3f up, final Vector2f uvMin, final Vector2f uvMax) {
        addSizedQuad(mesh, corner, right, up, new Vector3f(1.0f, 1.0f, 1.0f), uvMin, uvMax);
    }
    
}
