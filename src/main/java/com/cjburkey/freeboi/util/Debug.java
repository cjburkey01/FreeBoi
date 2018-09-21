package com.cjburkey.freeboi.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Debug {
    
    private static final Logger logger = LogManager.getLogger("freeboi");
    
    public static void log(Object msg, Object... args) {
        logger.info(sanitize(msg), args);
    }
    
    public static void log(Object msg) {
        logger.info(sanitize(msg));
    }
    
    public static void warn(Object msg, Object... args) {
        logger.warn(sanitize(msg), args);
    }
    
    public static void warn(Object msg) {
        logger.warn(sanitize(msg));
    }
    
    public static void error(Object msg, Object... args) {
        logger.error(sanitize(msg), args);
    }
    
    public static void error(Object msg) {
        logger.error(sanitize(msg));
    }
    
    public static void exception(Throwable e) {
        error("An exception occurred:");
        Throwable t = e;
        do {
            printStackTrace(t);
        } while ((t = e.getCause()) != null);
    }
    
    private static void printStackTrace(Throwable t) {
        error("  Exception: {}", t.getMessage());
        for (StackTraceElement e : t.getStackTrace()) {
            error("    {}", e.toString());
        }
    }
    
    private static String sanitize(Object msg) {
        String m = (msg == null) ? "null" : msg.toString();
        return (m == null) ? "null" : m;
    }
    
}
