package com.cjburkey.freeboi;

import com.cjburkey.freeboi.components.Camera;
import com.cjburkey.freeboi.components.MeshRenderer;
import com.cjburkey.freeboi.components.Transform;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.mesh.MeshRenderHelper;
import com.cjburkey.freeboi.mesh.TextureMesh;
import com.cjburkey.freeboi.shader.Shader;
import com.cjburkey.freeboi.shader.ShaderBuilder;
import com.cjburkey.freeboi.util.IO;
import com.cjburkey.freeboi.util.Texture;

public final class Game {
    
    private static Shader basicTextureShader;
    
    private static Texture testTexture;
    private static TextureMesh testMesh;
    private static ECSEntity testMeshEntity;
    
    private static float time = 0.0f;
    
    // TODO: WINDOW MAXIMIZE DOESN'T UPDATE SIZE
    
    static void start() {
        // Create the main camera
        FreeBoi.instance.world.createEntity().addComponent(new Camera());

        testMeshEntity = FreeBoi.instance.world.createEntity();
        
        buildBasicTextureShader();
        
        testTexture = IO.loadResourceTexture("test.png");
        
        testMesh = new TextureMesh(new float[] {
                // Vertex positions
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
        }, new float[] {
                // Texture coordinates
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
        }, new short[] {
                // Indices of triangles
                0, 1, 2,
                2, 3, 0,
        });
        testMesh.texture = testTexture;
        
        MeshRenderer renderer = new MeshRenderer();
        renderer.mesh.set(testMesh);
        testMeshEntity.addComponent(renderer);
        testMeshEntity.transform.position.z = -2.0f;
    }
    
    static void update() {
        time += FreeBoi.instance.getDeltaTime();
        Debug.log(1.0f / FreeBoi.instance.getDeltaTime());
        
        testMeshEntity.transform.position.x = (float) Math.sin(2.0f * time);
    }
    
    static void render() {
    }
    
    static void exit() {
        testMesh.destroy();
        
        basicTextureShader.destroy();
    }
    
    private static void buildBasicTextureShader() {
        ShaderBuilder builder = new ShaderBuilder();
        builder.vertexEnabled = true;
        builder.fragmentEnabled = true;
        
        builder.vertex.addLayoutVariable("vec3", 0, "vertPos");
        builder.vertex.addLayoutVariable("vec2", 1, "vertUv");
        builder.vertex.addOutputVariable("vec2", "uv");
        builder.vertex.addLineToFunction("main", "uv = vertUv");
        builder.vertex.addLineToFunction("main", "gl_Position = transform3(vertPos)");
        
        builder.fragment.addUniform("sampler2D", "sampler");
        builder.fragment.addInputVariable("vec2", "uv");
        builder.fragment.addOutputVariable("vec4", "fragColor");
        builder.fragment.addLineToFunction("main", "fragColor = texture(sampler, uv)");
        
        basicTextureShader = builder.build();
        if (basicTextureShader == null) {
            Debug.error("Failed to create basic texture shader");
            FreeBoi.instance.stop();
        }
    }
    
    public static Shader getBasicTextureShader() {
        return basicTextureShader;
    }
    
}
