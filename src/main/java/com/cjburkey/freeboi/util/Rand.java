package com.cjburkey.freeboi.util;

import java.util.Random;

public final class Rand {
    
    public static final Random random = new Random();
    
    public static float rFloat() {
        return random.nextFloat();
    }
    
    public static float rFloat(float min, float max) {
        min = Util.min(max, min);
        return ((Util.max(max, min) - min) * rFloat()) + min;
    }
    
    public static int rInt() {
        return random.nextInt();
    }
    
    public static int rInt(int min, int max, boolean inclusive) {
        min = Util.min(max, min);
        return random.nextInt((Util.max(max, min) - min) + (inclusive ? 1 : 0)) + min;
    }
    
    public static int rInt(int min, int max) {
        return rInt(min, max, false);
    }
    
    public static int rIntI(int min, int max) {
        return rInt(min, max, true);
    }
    
}
