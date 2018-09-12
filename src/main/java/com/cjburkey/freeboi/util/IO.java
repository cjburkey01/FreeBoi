package com.cjburkey.freeboi.util;

import com.cjburkey.freeboi.Debug;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

import static org.lwjgl.opengl.GL13.*;

public final class IO {
    
    public static String readResourceFile(String path) {
        path = cleanPath(path);
        try {
            return readInputStream(IO.class.getResourceAsStream(path));
        } catch (Exception e) {
            Debug.error("Failed to read file: {}", path);
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
    
    public static byte[] readResourceBytes(String path) {
        path = cleanPath(path);
        try {
            InputStream is = IO.class.getResourceAsStream(path);
            if (is == null) {
                throw new FileNotFoundException("Failed to locate resource");
            }
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            Debug.error("Failed to read file: {}", path);
            Debug.exception(e);
        }
        return new byte[0];
    }
    
    public static Texture loadResourceTexture(String path, int texture, boolean pixelPerfect, boolean repeat) {
        byte[] bytes = readResourceBytes(path);
        if (bytes.length <= 0) {
            return null;
        }
        return new Texture(bytes, texture, pixelPerfect, repeat);
    }
    
    public static Texture loadResourceTexture(String path, boolean pixelPerfect, boolean repeat) {
        return loadResourceTexture(path, GL_TEXTURE0, pixelPerfect, repeat);
    }
    
    public static Texture loadResourceTexture(String path) {
        return loadResourceTexture(path, true, true);
    }
    
    private static String cleanPath(String path) {
        path = path.trim().replaceAll(Pattern.quote("\\"), "/");
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return '/' + path;
    }
    
}
