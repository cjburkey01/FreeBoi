package com.cjburkey.freeboi.world;

import com.cjburkey.freeboi.Game;
import com.cjburkey.freeboi.components.Transform;
import com.cjburkey.freeboi.concurrent.IAction;
import com.cjburkey.freeboi.concurrent.ThreadPool;
import com.cjburkey.freeboi.concurrent.ThreadSafeHandler;
import com.cjburkey.freeboi.ecs.ECSEntity;
import com.cjburkey.freeboi.ecs.ECSWorld;
import com.cjburkey.freeboi.util.Texture;
import com.cjburkey.freeboi.util.TimeDebug;
import com.cjburkey.freeboi.util.Util;
import com.cjburkey.freeboi.value.Pos;
import com.cjburkey.freeboi.world.event.ChunkGenerationBegin;
import com.cjburkey.freeboi.world.event.ChunkGenerationFinish;
import com.cjburkey.freeboi.world.generation.ChunkGeneratorTest;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.UUID;
import org.joml.Vector3f;

public class World {
    
    public static final int chunkWidth = 32;
    public static final int chunkHeight = 128;
    public static final int regionSize = chunkWidth;
    
    private final int chunkLoadingRadiusBlocks;
    private final float updateInterval;
    private final float unloadTime;
    private float updateTimer = 0.0f;
    private boolean loadChunks = true;
    
    private final Object2ObjectOpenHashMap<Pos, Chunk> chunks = new Object2ObjectOpenHashMap<>();
    private final Object2FloatOpenHashMap<Pos> chunksToUnload = new Object2FloatOpenHashMap<>();
    private final ThreadPool generationThreadPool = new ThreadPool("ChunkGeneration", 4);
    private final Object2ObjectOpenHashMap<UUID, Transform> loaders = new Object2ObjectOpenHashMap<>();
    private final ThreadSafeHandler threadSafeHandler = new ThreadSafeHandler(Integer.MAX_VALUE);
    
    public World(int chunkLoadingRadiusBlocks, float updateInterval, float unloadTime) {
        this.chunkLoadingRadiusBlocks = chunkLoadingRadiusBlocks;
        this.updateInterval = updateInterval;
        this.unloadTime = unloadTime;
    }
    
    public void overrideChunkLoading() {
        loadChunks = !loadChunks;
    }
    
    public void addChunkLoader(ECSEntity entity) {
        loaders.put(entity.uuid, entity.transform);
    }
    
    public void removeChunkLoader(ECSEntity entity) {
        loaders.remove(entity.uuid);
    }
    
    public int getLoadedChunks() {
        return chunks.size();
    }
    
    public float getCheckTime() {
        return updateInterval - updateTimer;
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
        TimeDebug.start("world.update");
        TimeDebug.start("world.update.render");
        ChunkRenderHelper.update();
        TimeDebug.pause("world.update.render");
        TimeDebug.start("world.update.generate");
        threadSafeHandler.update();
        TimeDebug.pause("world.update.generate");
        
        updateTimer += deltaTime;
        if (updateTimer < updateInterval) {
            return;
        }
        updateTimer -= updateInterval;
        
        if (loadChunks) {
            TimeDebug.start("world.update.loadChunks");
            TimeDebug.start("world.update.loadChunks.unloadTime");
            ObjectOpenHashSet<Pos> chunksToRemove = new ObjectOpenHashSet<>();
            for (Pos chunkToUnload : chunksToUnload.keySet()) {
                float time = chunksToUnload.getFloat(chunkToUnload) + updateInterval;
                if (time >= unloadTime) {
                    chunksToRemove.add(chunkToUnload);
                } else {
                    chunksToUnload.put(chunkToUnload, time);
                }
            }
            TimeDebug.pause("world.update.loadChunks.unloadTime");
            TimeDebug.start("world.update.loadChunks.unload");
            for (Pos chunkToRemove : chunksToRemove) {
                chunksToUnload.removeFloat(chunkToRemove);
                unload(chunkToRemove);
            }
            TimeDebug.pause("world.update.loadChunks.unload");
            
            TimeDebug.start("world.update.loadChunks.loadSearch");
            ObjectOpenHashSet<Pos> chunksToLoad = new ObjectOpenHashSet<>();
            for (Transform entity : loaders.values()) {
                Pos currentChunk = blockToRawChunk(entity.position);
                int chunkRadius = Util.divCeil(chunkLoadingRadiusBlocks, chunkWidth);
                for (int x = -chunkRadius; x <= chunkRadius; x++) {
                    for (int y = -chunkRadius; y <= chunkRadius; y++) {
                        for (int z = -chunkRadius; z <= chunkRadius; z++) {
                            chunksToLoad.add(rawChunkToChunk(currentChunk.add(x, y, z)));
                        }
                    }
                }
            }
            TimeDebug.pause("world.update.loadChunks.loadSearch");
            TimeDebug.start("world.update.loadChunks.unloadCheck");
            for (Pos loadedChunk : chunks.keySet()) {
                boolean toLoadHas = chunksToLoad.contains(loadedChunk);
                boolean unloadHas = chunksToUnload.containsKey(loadedChunk);
                if (unloadHas && toLoadHas) {
                    chunksToUnload.removeFloat(loadedChunk);
                } else if (!unloadHas && !toLoadHas) {
                    chunksToUnload.put(loadedChunk, 0.0f);
                }
            }
            TimeDebug.pause("world.update.loadChunks.unloadCheck");
            TimeDebug.start("world.update.loadChunks.load");
            for (Pos chunkToLoad : chunksToLoad) {
                if (!chunks.containsKey(chunkToLoad)) {
                    getChunk(chunkToLoad);
                    //Debug.log("Loaded chunk {}", chunkToLoad);
                }
            }
            TimeDebug.pause("world.update.loadChunks.load");
            TimeDebug.pause("world.update.loadChunks");
        }
        TimeDebug.pause("world.update");
    }
    
    private void unload(Pos chunk) {
        if (chunks.containsKey(chunk)) {
            chunks.remove(chunk).remove();
            //Debug.log("Unloaded chunk {}", chunk);
        }
    }
    
    public void exit() {
        ChunkRenderHelper.stop();
        generationThreadPool.stop();
    }
    
    private void queueGeneration(final Chunk chunk) {
        chunk.markGenerating();
        
        generationThreadPool.queueAction(() -> {
            chunk.init();
            Game.EVENT_HANDLER.trigger(new ChunkGenerationBegin(chunk));
            new ChunkGeneratorTest().generate(chunk);
            chunk.markGenerated();
            queue(() -> Game.EVENT_HANDLER.trigger(new ChunkGenerationFinish(chunk)));
        });
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
    
    private static Pos blockToRawChunk(Vector3f pos) {
        return new Pos(pos.x / chunkWidth, pos.y / chunkWidth, pos.z / chunkWidth);
    }
    
    private static Pos rawChunkToChunk(Pos pos) {
        return new Pos(pos.x, Util.divFloor(pos.y, Util.divFloor(chunkHeight, chunkWidth)), pos.z);
    }
    
}
