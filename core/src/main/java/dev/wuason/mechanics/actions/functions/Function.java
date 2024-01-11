package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.Action;

import java.lang.reflect.Array;
import java.util.*;

public abstract class Function {

    private final Map<String, FunctionArgument> args;
    private final String name;

    public Function(String name, Map<String, FunctionArgument> args) {
        this.name = name.toUpperCase(Locale.ENGLISH);
        this.args = args;
    }

    public Map<String, FunctionArgument> getArgs() {
        return args;
    }

    public FunctionArgument[] orderArgs(HashMap<String, String> args){
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

    public abstract boolean execute(Action action, Object... Args);



}
