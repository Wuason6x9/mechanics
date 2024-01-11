package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.Action;

import java.lang.reflect.Array;
import java.util.*;

public abstract class Function {

    private final Map<String, FunctionArgument> args;
    private final String name;
    private final FunctionProperties properties;

    public Function(String name, Map<String, FunctionArgument> args, FunctionProperties properties) {
        this.name = name.toUpperCase(Locale.ENGLISH);
        this.args = args;
        this.properties = properties;
    }

    public Map<String, FunctionArgument> getArgs() {
        return args;
    }

    public FunctionArgument[] orderArgs(Map<String, String> args){
        FunctionArgument[] functionArguments = new FunctionArgument[getArgs().size()];
        for(Map.Entry<String, String> entry : args.entrySet()){
            FunctionArgument functionArgument = getArgs().get(entry.getKey());
            if(functionArgument == null) continue;
            functionArguments[functionArgument.getOrder()] = functionArgument;
        }
        return functionArguments;
    }

    public String getName() {
        return name;
    }

    public FunctionProperties getProperties() {
        return properties;
    }

    public abstract boolean execute(Action action, Object... Args);



}
