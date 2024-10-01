package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.Action;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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

    public FunctionArgument[] getOrderedArgs(){
        FunctionArgument[] functionArguments = new FunctionArgument[getArgs().size()];
        for(Map.Entry<String, FunctionArgument> entry : getArgs().entrySet()){
            functionArguments[entry.getValue().getOrder()] = entry.getValue();
        }
        return functionArguments;
    }

    public String getName() {
        return name;
    }

    public FunctionProperties getProperties() {
        return properties;
    }

    public abstract boolean execute(Action action, Object... args);


    public static class Builder {

        private FunctionArgument.Builder argumentBuilder;
        private String name;
        private FunctionProperties.Builder properties;

        private BiFunction<Action, Object[], Boolean> execute;

        public Builder(){
            argumentBuilder = new FunctionArgument.Builder();
            properties = new FunctionProperties.Builder();
        }

        public Builder addArguments(Consumer<FunctionArgument.Builder> argBuilder){
            argBuilder.accept(argumentBuilder);
            return this;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setProperties(Consumer<FunctionProperties.Builder> properties){
            properties.accept(this.properties);
            return this;
        }

        public Builder setExecute(BiFunction<Action, Object[], Boolean> execute){
            this.execute = execute;
            return this;
        }


        public Function build(){
            if(name == null) throw new RuntimeException("Name cannot be null!");
            if(execute == null) throw new RuntimeException("Execute cannot be null!");
            return new Function(name, argumentBuilder.build(), properties.build()) {
                @Override
                public boolean execute(Action action, Object... args) {
                    return execute.apply(action, args);
                }
            };
        }

    }
}
