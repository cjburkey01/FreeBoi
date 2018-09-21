package com.cjburkey.freeboi.util;

import com.cjburkey.freeboi.value.Resource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.io.IOUtils;

import static org.lwjgl.opengl.GL13.*;

public final class IO {
    
    public static String readResourceFile(Resource resource) {
        try {
            return readInputStream(IO.class.getResourceAsStream(resource.resourcePath));
        } catch (Exception e) {
            Debug.error("Failed to read resource: {}", resource.toString());
            Debug.exception(e);
        }
        return null;
    }
    
    public static String readInputStream(InputStream stream) throws Exception {
        BufferedReader reader = null;
        try {
            if (stream == null) {
                throw new FileNotFoundException("Failed to locate resource");
            }
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append('\n');
            }
            return output.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                Debug.error("Failed to close resource reader");
                Debug.exception(e);
            }
        }
    }
    
    public static byte[] readResourceBytes(Resource resource) {
        try {
            InputStream is = IO.class.getResourceAsStream(resource.resourcePath);
            if (is == null) {
                throw new FileNotFoundException("Failed to locate resource");
            }
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            Debug.error("Failed to read resource: {}", resource.toString());
            Debug.exception(e);
        }
        return new byte[0];
    }
    
    public static Texture loadResourceTexture(Resource resource, int texture, boolean pixelPerfect, boolean repeat) {
        byte[] bytes = readResourceBytes(resource);
        if (bytes.length <= 0) {
            return null;
        }
        return new Texture(bytes, texture, pixelPerfect, repeat);
    }
    
    public static Texture loadResourceTexture(Resource resource, boolean pixelPerfect, boolean repeat) {
        return loadResourceTexture(resource, GL_TEXTURE0, pixelPerfect, repeat);
    }
    
    public static Texture loadResourceTexture(Resource resource) {
        return loadResourceTexture(resource, true, false);
    }
    
}
