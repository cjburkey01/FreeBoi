package com.cjburkey.freeboi.shader;

import java.util.Objects;

class ShaderLayoutVariable extends ShaderVariable {
    
    final int location;
    
    ShaderLayoutVariable(String type, int location, String name) {
        super(type, name);
        
        this.location = location;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ShaderLayoutVariable that = (ShaderLayoutVariable) o;
        return location == that.location;
    }
    
    public int hashCode() {
        return Objects.hash(super.hashCode(), location);
    }
    
}
