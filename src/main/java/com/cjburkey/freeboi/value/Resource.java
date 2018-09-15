package com.cjburkey.freeboi.value;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Resource {
    
    public final String domain;
    public final String path;
    public final String resourcePath;
    
    public Resource(String domain, String path) {
        this.domain = domain;
        path = path.replaceAll(Pattern.quote("\\"), "/");
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        this.path = path;
        this.resourcePath = "/assets/" + this.domain + '/' + this.path;
    }
    
    public String toString() {
        return domain + ":" + path;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resource resource = (Resource) o;
        return Objects.equals(domain, resource.domain) && Objects.equals(path, resource.path);
    }
    
    public int hashCode() {
        return Objects.hash(domain, path);
    }
    
}
