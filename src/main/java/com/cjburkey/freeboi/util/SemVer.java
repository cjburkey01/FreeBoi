package com.cjburkey.freeboi.util;

import java.util.Objects;
import java.util.regex.Pattern;

public final class SemVer {
    
    public final int major;
    public final int minor;
    public final int patch;
    
    public SemVer(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SemVer semVer = (SemVer) o;
        return major == semVer.major && minor == semVer.minor && patch == semVer.patch;
    }
    
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }
    
    public String toString() {
        return major + "." + minor + "." + patch;
    }
    
    public static SemVer fromString(String version) {
        String[] spl = version.split(Pattern.quote("."));
        if (spl.length == 3) {
            try {
                return new SemVer(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), Integer.parseInt(spl[2]));
            } catch (Exception ignored) {
            }
        }
        return null;
    }
    
}
