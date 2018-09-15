package com.cjburkey.freeboi;

import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.chunk.Chunk;
import com.cjburkey.freeboi.components.Camera;
import com.cjburkey.freeboi.components.FreeMoveCamera;
import com.cjburkey.freeboi.components.MeshRenderer;
import com.cjburkey.freeboi.input.Key;
import com.cjburkey.freeboi.mesh.ChunkMesh;
import com.cjburkey.freeboi.shader.Shader;
import com.cjburkey.freeboi.shader.ShaderBuilder;
import com.cjburkey.freeboi.util.ChunkMesher;
import com.cjburkey.freeboi.util.IO;
import com.cjburkey.freeboi.input.Input;
import com.cjburkey.freeboi.util.Rand;
import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.value.Resource;

public final class Game {
    
    private static Shader basicTextureShader;
    private static boolean wireframe = false;
    private static boolean vsync = false;
    private static float time = 0.0f;
    
    static void start() {
        // Create the main camera
        FreeBoi.instance.world.createEntity().addComponent(new Camera()).addComponent(new FreeMoveCamera());
        
        // Create the basic textured shader for the chunk
        buildBasicTextureShader();
        
        // Create a simple test chunk (for experiment purposes)
        Debug.log("Building test chunk mesh");
        Chunk chunk = new Chunk(new Pos());
        BlockType testBlockType = new BlockType("freeboi", "test");
        for (int x = 0; x < Chunk.chunkSize; x ++) {
            for (int y = 0; y < Chunk.chunkSize; y ++) {
                for (int z = 0; z < Chunk.chunkSize; z ++) {
                    if (Rand.rIntI(0, y * 2) == 0) {
                        chunk.setBlock(new Pos(x, y, z), testBlockType);
                    }
                }
            }
        }
        ChunkMesh mesh = ChunkMesher.meshChunk(chunk);
        mesh.texture = IO.loadResourceTexture(new Resource("freeboi", "texture/atlas.png"));
        MeshRenderer renderer = new MeshRenderer();
        renderer.mesh.set(mesh);
        FreeBoi.instance.world.createEntity().addComponent(renderer);
        Debug.log("Built test chunk mesh");
    }
    
    static void update() {
        boolean esc = Input.getIsKeyFirstDown(Key.ESCAPE);
        if (Input.getIsKeyDown(Key.ENTER) && esc) {
            FreeBoi.instance.stop();
            return;
        }
        
        if (esc) {
            Input.mouseLocked.flip();
        }
        if (Input.getIsKeyFirstDown(Key.P)) {
            FreeBoi.instance.wireframe.flip();
        }
        if (Input.getIsKeyFirstDown(Key.V)) {
            FreeBoi.instance.vsync.flip();
        }
    }
    
    static void render() {
    }
    
    static void exit() {
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
