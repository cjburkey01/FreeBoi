package com.cjburkey.freeboi;

import com.cjburkey.freeboi.components.Camera;
import com.cjburkey.freeboi.components.FreeMoveCamera;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.event.EventHandler;
import com.cjburkey.freeboi.input.Input;
import com.cjburkey.freeboi.input.Key;
import com.cjburkey.freeboi.shader.Shader;
import com.cjburkey.freeboi.shader.ShaderBuilder;
import com.cjburkey.freeboi.util.Debug;
import com.cjburkey.freeboi.util.IO;
import com.cjburkey.freeboi.util.Texture;
import com.cjburkey.freeboi.value.Resource;
import com.cjburkey.freeboi.world.World;
import com.cjburkey.freeboi.world.event.ChunkGenerationFinish;
import com.cjburkey.freeboi.world.generation.ChunkGeneratorOverworld;

public final class Game {
    
    public static final EventHandler EVENT_HANDLER = new EventHandler();
    
    private static Texture atlasTexture;
    private static Shader basicTextureShader;
    
    public static final World world = new World(new ChunkGeneratorOverworld(), 128, 0.5f, 2.5f);
    
    static void start() {
        // Create the main camera
        ECSEntity cam = FreeBoi.instance.world.createEntity().addComponent(new Camera()).addComponent(new FreeMoveCamera());
        
        buildBasicTextureShader();
        atlasTexture = IO.loadResourceTexture(new Resource("freeboi", "texture/atlas.png"));
        
        world.addChunkLoader(cam);
//        world.overrideChunkLoading();
//        world.getChunk(new Pos(0, 1, 0));
//        world.getChunk(new Pos(0, -1, 0));
//        world.getChunk(new Pos(-1, -1, 0));
//        world.getChunk(new Pos(-1, -1, -1));
         
        EVENT_HANDLER.addListener(ChunkGenerationFinish.class, (e) -> world.addChunkToScene(FreeBoi.instance.world, atlasTexture, e.chunk.getChunkPos()));
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
        
        world.update(FreeBoi.instance.getDeltaTime());
    }
    
    static void render() {
    }
    
    static void exit() {
        world.exit();
        
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
