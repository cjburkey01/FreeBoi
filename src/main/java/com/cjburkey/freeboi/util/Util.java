package com.cjburkey.freeboi.util;

import org.joml.Vector3f;

public final class Util {
    
    public static final float E = 2.7182818284590452354f;
    public static final float PI = 3.14159265358979323846f;
    public static final float DEG2RAD = PI / 180.0f;
    public static final float RAD2DEG = 1.0f / DEG2RAD;
    
    public static float deg2Rad(float deg) {
        return deg * DEG2RAD;
    }
    
    public static float rad2Deg(float rad) {
        return rad * RAD2DEG;
    }
    
    public static byte min(byte a, byte b) {
        return (a > b) ? b : a;
    }
    
    public static short min(short a, short b) {
        return (a > b) ? b : a;
    }
    
    public static int min(int a, int b) {
        return (a > b) ? b : a;
    }
    
    public static long min(long a, long b) {
        return (a > b) ? b : a;
    }
    
    public static float min(float a, float b) {
        return (a > b) ? b : a;
    }
    
    public static double min(double a, double b) {
        return (a > b) ? b : a;
    }
    
    public static byte max(byte a, byte b) {
        return (a < b) ? b : a;
    }
    
    public static short max(short a, short b) {
        return (a < b) ? b : a;
    }
    
    public static int max(int a, int b) {
        return (a < b) ? b : a;
    }
    
    public static long max(long a, long b) {
        return (a < b) ? b : a;
    }
    
    public static float max(float a, float b) {
        return (a < b) ? b : a;
    }
    
    public static double max(double a, double b) {
        return (a < b) ? b : a;
    }
    
    public static byte clamp(byte val, byte min, byte max) {
        return max(min(min, max), min(max(min, max), val));
    }
    
    public static short clamp(short val, short min, short max) {
        return max(min(min, max), min(max(min, max), val));
    }
    
    public static int clamp(int val, int min, int max) {
        return max(min(min, max), min(max(min, max), val));
    }
    
    public static long clamp(long val, long min, long max) {
        return max(min(min, max), min(max(min, max), val));
    }
    
    public static float clamp(float val, float min, float max) {
        return max(min(min, max), min(max(min, max), val));
    }
    
    public static double clamp(double val, double min, double max) {
        return max(min(min, max), min(max(min, max), val));
    }
    
    public static Vector3f back() {
        return new Vector3f(0.0f, 0.0f, 1.0f);
    }
    
    public static Vector3f forward() {
        return new Vector3f(0.0f, 0.0f, -1.0f);
    }
    
    public static Vector3f right() {
        return new Vector3f(1.0f, 0.0f, 0.0f);
    }
    
    public static Vector3f left() {
        return new Vector3f(-1.0f, 0.0f, 0.0f);
    }
    
    public static Vector3f up() {
        return new Vector3f(0.0f, 1.0f, 0.0f);
    }
    
    public static Vector3f down() {
        return new Vector3f(0.0f, -1.0f, 0.0f);
    }
    
    public static int divFloor(int n, int d) {
        return Math.floorDiv(n ,d);
    }
    
    public static long divFloor(long n, long d) {
        return Math.floorDiv(n ,d);
    }
    
    public static int floor(float value) {
        return (int) Math.floor(value);
    }
    
    public static long floor(double value) {
        return (long) Math.floor(value);
    }
    
    public static int ceil(float value) {
        return (int) Math.ceil(value);
    }
    
    public static long ceil(double value) {
        return (long) Math.ceil(value);
    }
    
}
