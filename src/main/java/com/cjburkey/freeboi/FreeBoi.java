package com.cjburkey.freeboi;

import com.cjburkey.freeboi.components.MeshRenderer;
import com.cjburkey.freeboi.ecs.ECSWorld;
import com.cjburkey.freeboi.input.Input;
import com.cjburkey.freeboi.util.Debug;
import com.cjburkey.freeboi.util.SemVer;
import com.cjburkey.freeboi.util.TimeDebug;
import com.cjburkey.freeboi.value.BoolProperty;
import com.cjburkey.freeboi.value.Property;
import org.joml.Vector2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class FreeBoi {
    
    public static final SemVer VERSION = SemVer.fromString("0.0.1");
    public static final FreeBoi instance = new FreeBoi();
    private static final float titleUpdate = 1.0f / 30.0f;
    // D    Delta time
    // VS   Vsync enabled
    // WF   Wireframe enabled
    // LC   Loaded chunks
    // CCT  Chunk check timer
    // MR   Mesh renderers
    // MM   Memory usage
    public static final String title = "FreeBoi %s | FPS: %.2f | D: %.4f | VS: %s | WF: %s | LC: %s | CCT: %.4f | MR: %s | MM: %s";
    public static final boolean debugTiming = true;
    
    private long window;
    private long lastUpdate;
    private double deltaTime;
    private float deltaTimeF;
    private boolean running;
    
    public final Property<Vector2i> windowSize = new Property<>(new Vector2i());
    public final BoolProperty wireframe = new BoolProperty(false);
    public final BoolProperty vsync = new BoolProperty(false);
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
        GLFWErrorCallback.createPrint(System.err).set();
        
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        
        window = glfwCreateWindow(5, 5, "FreeBoi", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glClearColor(0.075f, 0.075f, 0.075f, 1.0f);
        
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
        
        wireframe.addListener((o, n) -> glPolygonMode(GL_FRONT_AND_BACK, (n) ? GL_LINE : GL_FILL));
        vsync.addListener((o, n) -> glfwSwapInterval((n) ? 1 : 0));
        
        glfwShowWindow(window);
        wireframe.set(true);
        wireframe.set(false);
        vsync.set(true);
        vsync.set(false);
        
        Input.init(window);
        Game.start();
        
        lastUpdate = System.nanoTime();
        float tmpTime = 0.0f;
        running = true;
        while (running) {
            long now = System.nanoTime();
            
            glfwPollEvents();
            if (glfwWindowShouldClose(window)) {
                stop();
            }
            
            update();
            Input.onUpdate();
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            TimeDebug.finishAndPrintAll(0.1f);  // Anything that takes 100ms or more should be logged as causing lag
            glfwSwapBuffers(window);
            
            deltaTime = (System.nanoTime() - lastUpdate) / 1000000000.0d;
            deltaTimeF = (float) deltaTime;
            lastUpdate = System.nanoTime();
            tmpTime += deltaTimeF;
            if (tmpTime >= titleUpdate) {
                tmpTime -= titleUpdate;
                setTitle();
            }
        }
        
        Debug.log("Exiting FreeBoi");
        world.onExit();
        Game.exit();
        Callbacks.glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        GLFWErrorCallback.createPrint(null).set();
        Debug.log("Exited FreeBoi");
    }
    
    private void setTitle() {
        long available = Runtime.getRuntime().totalMemory() / 1000000L;
        long used = available - Runtime.getRuntime().freeMemory() / 1000000L;
        glfwSetWindowTitle(window, String.format(title, VERSION.toString(), 1.0f / deltaTimeF, deltaTimeF, vsync.get(),
                wireframe.get(), Game.world.getLoadedChunks(), Game.world.getCheckTime(), MeshRenderer.getRenderers().size(),
                used + "MB / " + available + "MB"));
    }
    
    private void update() {
        TimeDebug.start("freeboi.update");
        world.onUpdate();
        Game.update();
        TimeDebug.pause("freeboi.update");
    }
    
    private void render() {
        TimeDebug.start("freeboi.render");
        world.onRender();
        Game.render();
        TimeDebug.pause("freeboi.render");
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
