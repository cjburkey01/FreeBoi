package com.cjburkey.freeboi.shader;

import java.util.Objects;

class ShaderVariable {
    
    final String type;
    final String name;
    final String value;
    
    ShaderVariable(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
    
    ShaderVariable(String type, String name) {
        this(type, name, null);
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShaderVariable that = (ShaderVariable) o;
        return Objects.equals(type, that.type) && Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }
    
    public int hashCode() {
        return Objects.hash(type, name, value);
    }
    
}
