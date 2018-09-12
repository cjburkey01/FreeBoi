package com.cjburkey.freeboi.shader;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class ShaderCodeBuilder {
    
    private static final String START = "// BEGIN %s SHADER";
    private static final String UNIFORMS = "// UNIFORMS";
    private static final String VARIABLES = "// VARIABLES";
    private static final String INPUT_VARS = "// INPUT VARIABLES";
    private static final String OUTPUT_VARS = "// OUTPUT VARIABLES";
    private static final String LAYOUT_VARS = "// LAYOUT VARIABLES";
    private static final String STRUCTS = "// STRUCTS";
    private static final String FUNCTIONS = "// FUNCTIONS";
    private static final String END = "// END SHADER";
    
    final UUID uuid;
    final String name;
    public boolean commentsEnabled = true;
    public String version = "330";
    public boolean core = true;
    private final Object2ObjectOpenHashMap<String, ShaderVariable> uniforms = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, ShaderVariable> variables = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, ShaderVariable> inputVariables = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, ShaderVariable> outputVariables = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, ShaderLayoutVariable> layoutVariables = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, ShaderStruct> structs = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectLinkedOpenHashMap<String, ShaderFunction> functions = new Object2ObjectLinkedOpenHashMap<>();
    
    ShaderCodeBuilder(String name) {
        uuid = UUID.randomUUID();
        this.name = name;
        
        addFunction("void", "main", new String[0]);
    }
    
    public ObjectCollection<ShaderVariable> getUniforms() {
        return uniforms.values();
    }
    
    public void addUniform(String type, String name) {
        uniforms.put(name, new ShaderVariable(type, name));
    }
    
    public void addVariable(String type, String name, String value) {
        variables.put(name, new ShaderVariable(type, name, value));
    }
    
    public void addVariable(String type, String name) {
        addVariable(type, name, null);
    }
    
    public void addInputVariable(String type, String name) {
        inputVariables.put(name, new ShaderVariable(type, name));
    }
    
    public void addOutputVariable(String type, String name) {
        outputVariables.put(name, new ShaderVariable(type, name));
    }
    
    public void addLayoutVariable(String type, int location, String name) {
        layoutVariables.put(name, new ShaderLayoutVariable(type, location, name));
    }
    
    public void addStruct(String name) {
        structs.put(name, new ShaderStruct(name));
    }
    
    public void addVariableToStruct(String structName, String type, String name) {
        if (!structs.containsKey(structName)) {
            throw new RuntimeException("Shader struct \"" + structName + "\" not defined");
        }
        structs.get(structName).addMember(type, name);
    }
    
    public void addFunction(String type, String name, String[] parameterTypes, String... parameterNames) {
        if (parameterNames.length != parameterTypes.length) {
            throw new RuntimeException("Could not add function with " + parameterTypes.length + " types, but " + parameterNames.length + " names");
        }
        ShaderVariable[] parameters = new ShaderVariable[parameterNames.length];
        for (int i = 0; i < parameters.length; i ++) {
            parameters[i] = new ShaderVariable(parameterTypes[i], parameterNames[i]);
        }
        functions.put(name, new ShaderFunction(type, name, parameters));
    }
    
    public void addLineToFunction(String functionName, int index, String line) {
        if (!functions.containsKey(functionName)) {
            throw new RuntimeException("Shader function \"" + functionName + "\" not defined");
        }
        functions.get(functionName).addLine(index, line);
    }
    
    public void addLineToFunction(String functionName, String line) {
        if (!functions.containsKey(functionName)) {
            throw new RuntimeException("Shader function \"" + functionName + "\" not defined");
        }
        ShaderFunction shaderFunction = functions.get(functionName);
        shaderFunction.addLine(shaderFunction.getLines(), line);
    }
    
    public String getCode() {
        StringBuilder output = new StringBuilder();
        
        if (commentsEnabled) {
            output.append(String.format(START, name));
            output.append('\n');
            output.append('\n');
        }
        
        output.append("#version ");
        output.append(version);
        if (core) {
            output.append(" core");
        }
        output.append('\n');
        output.append('\n');
        
        if (uniforms.size() > 0) {
            if (commentsEnabled) {
                output.append(UNIFORMS);
                output.append('\n');
                output.append('\n');
            }
            for (ShaderVariable uniform : uniforms.values()) {
                output.append("uniform ");
                output.append(uniform.type);
                output.append(' ');
                output.append(uniform.name);
                output.append(';');
                output.append('\n');
            }
            output.append('\n');
        }

        if (variables.size() > 0) {
            if (commentsEnabled) {
                output.append(VARIABLES);
                output.append('\n');
                output.append('\n');
            }
            for (ShaderVariable variable : variables.values()) {
                output.append(variable.type);
                output.append(' ');
                output.append(variable.name);
                if (variable.value != null) {
                    output.append(' ');
                    output.append('=');
                    output.append(' ');
                    output.append(variable.value);
                }
                output.append(';');
                output.append('\n');
            }
            output.append('\n');
        }

        if (inputVariables.size() > 0) {
            if (commentsEnabled) {
                output.append(INPUT_VARS);
                output.append('\n');
                output.append('\n');
            }
            for (ShaderVariable inputVariable : inputVariables.values()) {
                output.append("in ");
                output.append(inputVariable.type);
                output.append(' ');
                output.append(inputVariable.name);
                output.append(';');
                output.append('\n');
            }
            output.append('\n');
        }

        if (outputVariables.size() > 0) {
            if (commentsEnabled) {
                output.append(OUTPUT_VARS);
                output.append('\n');
                output.append('\n');
            }
            for (ShaderVariable outputVariable : outputVariables.values()) {
                output.append("out ");
                output.append(outputVariable.type);
                output.append(' ');
                output.append(outputVariable.name);
                output.append(';');
                output.append('\n');
            }
            output.append('\n');
        }

        if (layoutVariables.size() > 0) {
            if (commentsEnabled) {
                output.append(LAYOUT_VARS);
                output.append('\n');
                output.append('\n');
            }
            for (ShaderLayoutVariable layoutVariable : layoutVariables.values()) {
                output.append("layout (location = ");
                output.append(layoutVariable.location);
                output.append(") in ");
                output.append(layoutVariable.type);
                output.append(' ');
                output.append(layoutVariable.name);
                output.append(';');
                output.append('\n');
            }
            output.append('\n');
        }
        
        if (structs.size() > 0) {
            if (commentsEnabled) {
                output.append(STRUCTS);
                output.append('\n');
                output.append('\n');
            }
            for (ShaderStruct struct : structs.values()) {
                output.append("struct ");
                output.append(struct.name);
                output.append(' ');
                output.append('{');
                output.append('\n');
                for (ShaderVariable member : struct.getVariables()) {
                    output.append('\t');
                    output.append(member.type);
                    output.append(' ');
                    output.append(member.name);
                    output.append(';');
                    output.append('\n');
                }
                output.append('}');
                output.append(';');
                output.append('\n');
            }
            output.append('\n');
        }
        
        if (functions.size() > 0) {
            if (commentsEnabled) {
                output.append(FUNCTIONS);
                output.append('\n');
                output.append('\n');
            }
            int f = 0;
            ObjectCollection<ShaderFunction> funcs = functions.values();
            List<ShaderFunction> fu = new LinkedList<>(funcs);
            Collections.reverse(fu);
            for (ShaderFunction function : fu) {
                output.append(function.type);
                output.append(' ');
                output.append(function.name);
                output.append('(');
                for (int i = 0; i < function.parameters.length; i ++) {
                    output.append(function.parameters[i].type);
                    output.append(' ');
                    output.append(function.parameters[i].name);
                    if (i < function.parameters.length - 1) {
                        output.append(',');
                        output.append(' ');
                    }
                }
                output.append(')');
                output.append(' ');
                output.append('{');
                output.append('\n');
                for (int i = 0; i < function.getLines(); i ++) {
                    output.append('\t');
                    output.append(function.getLine(i));
                    output.append('\n');
                }
                output.append('}');
                output.append('\n');
                if (f < functions.size() - 1) {
                    output.append('\n');
                }
                f ++;
            }
            output.append('\n');
        }
        
        if (commentsEnabled) {
            output.append(END);
            output.append('\n');
        }
        
        return output.toString();
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShaderCodeBuilder that = (ShaderCodeBuilder) o;
        return Objects.equals(uuid, that.uuid);
    }
    
    public int hashCode() {
        return Objects.hash(uuid);
    }
    
}
