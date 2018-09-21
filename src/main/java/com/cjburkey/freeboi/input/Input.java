package com.cjburkey.freeboi.input;

import com.cjburkey.freeboi.util.Debug;
import com.cjburkey.freeboi.value.BoolProperty;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Field;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public final class Input {
    
    private static final boolean search = false;
    private static final String prefix = "GLFW_KEY_";
    
    private static boolean init = false;
    private static long window = 0L;
    
    private static final Object2ObjectOpenHashMap<String, Axis> axes = new Object2ObjectOpenHashMap<>();
    
    // Keyboard
    private static final Object2ObjectOpenHashMap<String, KeyAxis> keyAxes = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<Key, Boolean> keysPressed = new Object2ObjectOpenHashMap<>();
    private static final ObjectOpenHashSet<Key> keysReleased = new ObjectOpenHashSet<>();
    
    // Mouse
    public static final BoolProperty mouseLocked = new BoolProperty();
    private static boolean movedMouse = false;
    private static final Vector2f tmpMousePos = new Vector2f();
    private static final Vector2f mousePos = new Vector2f();
    private static final Vector2f prevMousePos = new Vector2f();
    private static final Vector2f deltaMousePos = new Vector2f();
    
    public static void init(long window) {
        if (init) {
            return;
        }
        init = true;
        
        Input.window = window;
        
        // Debug
        if (search) {
            try {
                for (Field field : GLFW.class.getFields()) {
                    if (field.getName().startsWith(prefix)) {
                        System.out.println(field.getName().trim().toUpperCase().substring(prefix.length()) + "(" + field.get(null) + "),");
                    }
                }
                System.out.println(";");
            } catch (Exception e) {
                Debug.exception(e);
            }
        }
        
        addKeyAxis("Horizontal", 1.0f, Key.D, Key.A);
        addKeyAxis("Horizontal", 1.0f, Key.RIGHT, Key.LEFT);
        addKeyAxis("Vertical", 1.0f, Key.W, Key.S);
        addKeyAxis("Vertical", 1.0f, Key.UP, Key.DOWN);
        
        glfwSetKeyCallback(window, (win, key, scan, action, mods) -> {
            Key k = Key.getKey(key);
            if (k.equals(Key.UNKNOWN)) {
                return;
            }
            if (action == GLFW_PRESS) {
                onKeyPress(k);
            } else if (action == GLFW_RELEASE) {
                onKeyRelease(k);
            }
        });
        
        glfwSetCursorPosCallback(window, (win, x, y) -> {
            tmpMousePos.set((float) x, (float) y);
            if (!movedMouse) {
                prevMousePos.set(tmpMousePos);
                movedMouse = true;
            }
        });
        
        mouseLocked.addListener((o, n) -> glfwSetInputMode(window, GLFW_CURSOR, (n) ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL));
    }
    
    public static void onUpdate() {
        for (Key key : keysPressed.keySet()) {
            keysPressed.put(key, false);
        }
        keysReleased.clear();
        
        mousePos.set(tmpMousePos);
        mousePos.sub(prevMousePos, deltaMousePos);
        prevMousePos.set(mousePos);
    }
    
    private static void onKeyPress(Key key) {
        checkKeyAxes(true, key);
        
        keysPressed.put(key, true);
    }
    
    private static void onKeyRelease(Key key) {
        checkKeyAxes(false, key);
        
        keysPressed.remove(key);
        keysReleased.add(key);
    }
    
    public static boolean getIsKeyDown(Key key) {
        return keysPressed.containsKey(key);
    }
    
    public static boolean getIsKeyFirstDown(Key key) {
        return getIsKeyDown(key) && keysPressed.get(key);
    }
    
    public static boolean getIsKeyFirstUp(Key key) {
        return !getIsKeyDown(key) && keysReleased.contains(key);
    }
    
    public static float getAxis(String name) {
        if (!axes.containsKey(name)) {
            Debug.warn("Axis \"{}\" not registered", name);
            return 0.0f;
        }
        return axes.get(name).getValue();
    }
    
    public static Vector2f getMousePos() {
        return new Vector2f(mousePos);
    }
    
    public static Vector2f getMouse() {
        return new Vector2f(mousePos);
    }
    
    public static Vector2f getDeltaMouse() {
        return new Vector2f(deltaMousePos);
    }
    
    public static void addKeyAxis(String name, float weight, Key positive, Key negative) {
        if (axes.containsKey(name) && !keyAxes.containsKey(name)) {
            Debug.error("Failed to register key axis \"{}\": there is already a non-key axis by that name", name);
            return;
        }
        KeyAxis axe;
        if (keyAxes.containsKey(name)) {
            axe = keyAxes.get(name);
        } else {
            axe = new KeyAxis(name, weight);
            keyAxes.put(name, axe);
            axes.put(name, axe);
        }
        axe.addKey(true, positive);
        axe.addKey(false, negative);
    }
    
    private static void checkKeyAxes(boolean press, Key key) {
        for (KeyAxis axis : keyAxes.values()) {
            if ((press && axis.keyPositive.contains(key)) || (!press && axis.keyNegative.contains(key))) {
                axis.doPositive();
            } else if ((press && axis.keyNegative.contains(key)) || (!press && axis.keyPositive.contains(key))) {
                axis.doNegative();
            }
        }
    }
    
}
