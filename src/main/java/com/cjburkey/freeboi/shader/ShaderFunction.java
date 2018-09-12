package com.cjburkey.freeboi.shader;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;
import java.util.Objects;

class ShaderFunction {
    
    final String type;
    final String name;
    final ShaderVariable[] parameters;
    private final ObjectArrayList<String> lines = new ObjectArrayList<>();
    
    ShaderFunction(String type, String name, ShaderVariable[] parameters) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
    }
    
    void addLine(int index, String line) {
        lines.add(index, (line.endsWith(";") ? line : (line + ';')));
    }
    
    void addLine(String line) {
        lines.add(line);
    }
    
    String getLine(int line) {
        return lines.get(line);
    }
    
    int getLines() {
        return lines.size();
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShaderFunction that = (ShaderFunction) o;
        return Objects.equals(type, that.type) && Objects.equals(name, that.name) && Arrays.equals(parameters, that.parameters);
    }
    
    public int hashCode() {
        int result = Objects.hash(type, name);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
    
}
