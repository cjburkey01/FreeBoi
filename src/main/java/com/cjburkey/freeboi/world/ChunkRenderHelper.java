package com.cjburkey.freeboi.world;

import com.cjburkey.freeboi.components.MeshRenderer;
import com.cjburkey.freeboi.concurrent.ThreadPool;
import com.cjburkey.freeboi.concurrent.ThreadSafeHandler;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.ecs.ECSWorld;
import com.cjburkey.freeboi.mesh.ChunkMesh;
import com.cjburkey.freeboi.mesh.ChunkMeshBuilder;
import com.cjburkey.freeboi.util.ChunkMesher;
import com.cjburkey.freeboi.util.Texture;

public class ChunkRenderHelper {
    
    private static final ThreadPool generationThreadPool = new ThreadPool("ChunkMesher", 8);
    private static final ThreadSafeHandler threadSafeHandler = new ThreadSafeHandler(512);
    
    public static void update() {
        threadSafeHandler.update();
    }
    
    public static void stop() {
        generationThreadPool.stop();
    }
    
    public static ECSEntity queueChunkRender(ECSWorld scene, final Texture texture, final Chunk chunk) {
        if (chunk.getIsGenerating() || !chunk.getIsGenerated()) {
            return null;
        }
        final ECSEntity entity = scene.createEntity();
        generationThreadPool.queueAction(() -> {
            final ChunkMeshBuilder builder = ChunkMesher.meshChunk(chunk);
            threadSafeHandler.queue(() -> {
                final ChunkMesh mesh = builder.buildMesh();
                mesh.texture = texture;
                final MeshRenderer renderer = new MeshRenderer();
                renderer.mesh.set(mesh);
                entity.addComponent(renderer);
                entity.transform.position.set(chunk.getChunkWorldPos().x, chunk.getChunkWorldPos().y, chunk.getChunkWorldPos().z);
            });
        });
        chunk.setEntity(entity);
        return entity;
    }
    
}
