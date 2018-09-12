package com.cjburkey.freeboi;

import com.cjburkey.freeboi.ecs.ECSWorld;
import com.cjburkey.freeboi.util.SemVer;
import com.cjburkey.freeboi.value.Property;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class FreeBoi {
    
    public static final SemVer VERSION = SemVer.fromString("0.0.1");
    public static final FreeBoi instance = new FreeBoi();
    
    private long lastUpdate;
    private double deltaTime;
    private float deltaTimeF;
    private boolean running;
    
    public final Property<Vector2i> windowSize = new Property<>(new Vector2i());
    public final ECSWorld world = new ECSWorld();
    
    private FreeBoi() {
    }
    
    private void run() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Debug.exception(e));
        Thread.currentThread().setName("MainThread");
        Debug.log("Starting FreeBoi");
        
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }
        
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        
        long window = glfwCreateWindow(5, 5, "FreeBoi " + VERSION, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glClearColor(0.075f, 0.375f, 0.075f, 1.0f);
        
        glfwSetFramebufferSizeCallback(window, (win, width, height) -> {
            glViewport(0, 0, width, height);
            windowSize.set(new Vector2i(width, height));
        });
        
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode == null) {
            throw new RuntimeException("Failed to retrieve GLFW screen information");
        }
        int w = vidMode.width() / 2;
        int h = vidMode.height() / 2;
        glfwSetWindowSize(window, w, h);
        glfwSetWindowPos(window, (vidMode.width() - w) / 2, (vidMode.height() - h) / 2);
        
        glfwSwapInterval(1);
        glfwShowWindow(window);
        
        Game.start();
        
        lastUpdate = System.nanoTime();
        
        running = true;
        while (running) {
            long now = System.nanoTime();
            
            glfwPollEvents();
            if (glfwWindowShouldClose(window)) {
                stop();
            }
            
            update();
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            glfwSwapBuffers(window);
            
            deltaTime = (System.nanoTime() - lastUpdate) / 1000000000.0d;
            deltaTimeF = (float) deltaTime;
            lastUpdate = System.nanoTime();
        }
        
        Game.exit();
        
        glfwDestroyWindow(window);
        glfwTerminate();
        
        Debug.log("Exiting FreeBoi");
    }
    
    private void update() {
        world.onUpdate();
        Game.update();
    }
    
    private void render() {
        world.onRender();
        Game.render();
    }
    
    public double getDeltaTimeD() {
        return deltaTime;
    }
    
    public float getDeltaTime() {
        return deltaTimeF;
    }
    
    public void stop() {
        running = false;
    }
    
    public static void main(String[] args) {
        instance.run();
    }
    
}
