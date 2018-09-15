package com.cjburkey.freeboi.block;

import com.cjburkey.freeboi.value.Resource;

public class BlockType {
    
    public final Resource blockName;
    
    public BlockType(String domain, String uniqueBlockName) {
        this.blockName = new Resource(domain, "blockType/" + uniqueBlockName);
    }
    
    public boolean getIsTransparent() {
        return false;
    }
    
}
