package com.cjburkey.freeboi.util;

import com.cjburkey.freeboi.Debug;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

public final class Texture {
    
    final int texture;
    final int width;
    final int height;
    boolean valid;
    
    Texture(byte[] rawFileBits, int tex, boolean pixelPerfect, boolean repeat) {
        if (rawFileBits == null || rawFileBits.length <= 0) {
            throw new RuntimeException("Failed to create texture: no bytes received");
        }
        
        ByteBuffer inputBytesBuffer = memAlloc(rawFileBits.length);
        inputBytesBuffer.put(rawFileBits);
        inputBytesBuffer.flip();
        
        int[] width = new int[1];
        int[] height = new int[1];
        ByteBuffer imageBytesBuffer = stbi_load_from_memory(inputBytesBuffer, width, height, new int[1], 0);
        this.width = width[0];
        this.height = height[0];
        if (this.width <= 0 || this.height <= 0) {
            throw new RuntimeException("Failed to create texture: no width and/or no height");
        }
        
        glActiveTexture(tex);
        glEnable(GL_TEXTURE_2D);
        texture = glGenTextures();
        valid = true;
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, (pixelPerfect ? GL_NEAREST_MIPMAP_LINEAR : GL_LINEAR_MIPMAP_LINEAR));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, (pixelPerfect ? GL_NEAREST : GL_LINEAR));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, (repeat ? GL_REPEAT : GL_CLAMP_TO_EDGE));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, (repeat ? GL_REPEAT : GL_CLAMP_TO_EDGE));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, (repeat ? GL_REPEAT : GL_CLAMP_TO_EDGE));
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBytesBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        unbind();
        
        memFree(inputBytesBuffer);
        memFree(imageBytesBuffer);
    }
    
    public void bind() {
        if (valid) {
            glBindTexture(GL_TEXTURE_2D, texture);
        }
    }
    
    public void destroy() {
        if (!valid) {
            Debug.warn("Texture already destroyed");
            return;
        }
        unbind();
        valid = false;
        glDeleteTextures(texture);
    }
    
    public boolean getIsValid() {
        return valid;
    }
    
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
}
