package com.cjburkey.freeboi.world;

import com.cjburkey.freeboi.Debug;
import com.cjburkey.freeboi.Game;
import com.cjburkey.freeboi.block.BlockType;
import com.cjburkey.freeboi.components.Transform;
import com.cjburkey.freeboi.concurrent.ActionSet;
import com.cjburkey.freeboi.concurrent.IAction;
import com.cjburkey.freeboi.concurrent.ThreadPool;
import com.cjburkey.freeboi.concurrent.ThreadSafeHandler;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.ecs.ECSWorld;
import com.cjburkey.freeboi.util.Rand;
import com.cjburkey.freeboi.util.Texture;
import com.cjburkey.freeboi.util.Util;
import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.event.ChunkGenerationBegin;
import com.cjburkey.freeboi.world.event.ChunkGenerationFinish;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.UUID;
import org.joml.Vector3f;

public class World {
    
    private final int chunkLoadingRadius;
    private final float updateInterval;
    private final float unloadTime;
    private float updateTimer = 0.0f;
    
    private final Object2ObjectOpenHashMap<Pos, Chunk> chunks = new Object2ObjectOpenHashMap<>();
    private final Object2FloatOpenHashMap<Pos> chunksToUnload = new Object2FloatOpenHashMap<>();
    private final ThreadPool generationThreadPool = new ThreadPool("ChunkGeneration", 4);
    private final Object2ObjectOpenHashMap<UUID, Transform> loaders = new Object2ObjectOpenHashMap<>();
    private final ThreadSafeHandler threadSafeHandler = new ThreadSafeHandler(Integer.MAX_VALUE);
    private final BlockType testBlockType = new BlockType("freeboi", "test", 3, 3);
    
    public World(int chunkLoadingRadius, float updateInterval, float unloadTime) {
        this.chunkLoadingRadius = chunkLoadingRadius;
        this.updateInterval = updateInterval;
        this.unloadTime = unloadTime;
    }
    
    public void addChunkLoader(ECSEntity entity) {
        loaders.put(entity.uuid, entity.transform);
    }
    
    public void removeChunkLoader(ECSEntity entity) {
        loaders.remove(entity.uuid);
    }
    
    private Chunk getChunkRaw(Pos pos) {
        if (chunks.containsKey(pos)) {
            return chunks.get(pos);
        }
        Chunk chunk = new Chunk(this, pos);
        chunks.put(pos, chunk);
        return chunk;
    }
    
    public void update(float deltaTime) {
        updateTimer += deltaTime;
        if (updateTimer < updateInterval) {
            return;
        }
        updateTimer -= updateInterval;
        
        ChunkRenderHelper.update();
        threadSafeHandler.update();
        
        ObjectOpenHashSet<Pos> chunksToRemove = new ObjectOpenHashSet<>();
        for (Pos chunkToUnload : chunksToUnload.keySet()) {
            float time = chunksToUnload.getFloat(chunkToUnload) + updateInterval;
            if (time >= unloadTime) {
                chunksToRemove.add(chunkToUnload);
            } else {
                chunksToUnload.put(chunkToUnload, time);
            }
        }
        for (Pos chunkToRemove : chunksToRemove) {
            chunksToUnload.removeFloat(chunkToRemove);
            unload(chunkToRemove);
        }
        
        ObjectOpenHashSet<Pos> chunksToLoad = new ObjectOpenHashSet<>();
        for (Transform entity : loaders.values()) {
            Pos currentChunk = new Pos(entity.position.div(Chunk.SIZE, new Vector3f()));
            for (int x = -chunkLoadingRadius; x <= chunkLoadingRadius; x ++) {
                for (int y = -chunkLoadingRadius; y <= chunkLoadingRadius; y ++) {
                    for (int z = -chunkLoadingRadius; z <= chunkLoadingRadius; z ++) {
                        Pos pos = currentChunk.add(x, y, z);
                        chunksToLoad.add(pos);
                    }
                }
            }
        }
        for (Pos loadedChunk : chunks.keySet()) {
            boolean toLoadHas = chunksToLoad.contains(loadedChunk);
            boolean unloadHas = chunksToUnload.containsKey(loadedChunk);
            if (unloadHas && toLoadHas) {
                chunksToUnload.removeFloat(loadedChunk);
            } else if (!unloadHas && !toLoadHas) {
                chunksToUnload.put(loadedChunk, 0.0f);
            }
        }
        for (Pos chunkToLoad : chunksToLoad) {
            if (!chunks.containsKey(chunkToLoad)) {
                getChunk(chunkToLoad);
                Debug.log("Loaded chunk {}", chunkToLoad);
            }
        }
    }
    
    private void unload(Pos chunk) {
        if (chunks.containsKey(chunk)) {
            chunks.remove(chunk).remove();
            Debug.log("Unloaded chunk {}", chunk);
        }
    }
    
    public void exit() {
        ChunkRenderHelper.stop();
        generationThreadPool.stop();
    }
    
    private void queueGeneration(final Chunk chunk) {
        chunk.markGenerating();
        
        generationThreadPool.queueAction(ActionSet.buildComplete(() -> queue(() -> Game.EVENT_HANDLER.trigger(new ChunkGenerationBegin(chunk))), () -> {
            for (int x = 0; x < Chunk.SIZE; x ++) {
                for (int y = 0; y < Chunk.SIZE; y ++) {
                    for (int z = 0; z < Chunk.SIZE; z ++) {
                        Pos p = new Pos(chunk.chunkBlockPos.x + x, chunk.chunkBlockPos.y + y, chunk.chunkBlockPos.z + z);
                        if (Rand.rIntI(0, Util.floor(Vector3f.lengthSquared(p.x, p.y, p.z) / 10.0f)) == 0) {
                            chunk.setBlock(new Pos(x, y, z), testBlockType);
                        }
                    }
                }
            }
        }, () -> {
            chunk.markGenerated();
            queue(() -> Game.EVENT_HANDLER.trigger(new ChunkGenerationFinish(chunk)));
        }));
    }
    
    private void queue(IAction action) {
        threadSafeHandler.queue(action);
    }
    
    // Queues the chunk's generation if necessary
    public Chunk getChunk(Pos pos) {
        Chunk chunk = getChunkRaw(pos);
        if (!chunk.getIsGenerated() && !chunk.getIsGenerating()) {
            queueGeneration(chunk);
        }
        return chunk;
    }
    
    public ECSEntity addChunkToScene(ECSWorld scene, Texture texture, Pos pos) {
        return ChunkRenderHelper.queueChunkRender(scene, texture, getChunk(pos));
    }
    
}
