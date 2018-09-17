package com.cjburkey.freeboi.block;

import com.cjburkey.freeboi.value.Resource;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class BlockType {
    
    public final Resource blockName;
    private final Vector2i atlasPos = new Vector2i();
    
    public BlockType(String domain, String uniqueBlockName, int atlasX, int atlasY) {
        this.blockName = new Resource(domain, "blockType/" + uniqueBlockName);
        this.atlasPos.set(atlasX, atlasY);
    }
    
    public Vector2f getAtlasPos() {
        return new Vector2f(atlasPos.x / 32.0f, atlasPos.y / 32.0f);
    }
    
    public boolean getIsTransparent() {
        return false;
    }
    
}
