package com.cjburkey.freeboi.shader;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ShaderStruct {
    
    final String name;
    private final ObjectArrayList<ShaderVariable> members = new ObjectArrayList<>();
    
    public ShaderStruct(String name) {
        this.name = name;
    }
    
    public void addMember(String type, String name) {
        members.add(new ShaderVariable(type, name));
    }
    
    public ShaderVariable[] getVariables() {
        return members.toArray(new ShaderVariable[0]);
    }
    
}
