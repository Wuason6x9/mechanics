package dev.wuason.mechanics.actions.args.def.internal.functions;

import dev.wuason.mechanics.actions.Action;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class FunctionInternal {
    private final String id;
    private final FunctionInternalProperties properties;
    private final HashMap<String, FunctionInternalArgument> argumentsRequired;

    public FunctionInternal(FunctionInternalProperties properties, HashMap<String, FunctionInternalArgument> argumentsRequired, String id) {
        this.properties = properties;
        this.argumentsRequired = argumentsRequired;
        this.id = id.toUpperCase(Locale.ENGLISH);
    }

    public FunctionInternalProperties getProperties() {
        return properties;
    }
    public HashMap<String, FunctionInternalArgument> getArgumentsRequired() {
        return argumentsRequired;
    }

    public Object computeInit(Action action, Object... args){
        return compute(action, args);
    }

    public String getId() {
        return id;
    }

    public abstract Object compute(Action action, Object... args);

    public FunctionInternalArgument[] getOrderedArgs(){
        FunctionInternalArgument[] functionArguments = new FunctionInternalArgument[getArgumentsRequired().size()];
        for(Map.Entry<String, FunctionInternalArgument> entry : getArgumentsRequired().entrySet()){
            functionArguments[entry.getValue().getOrder()] = entry.getValue();
        }
        return functionArguments;
    }

    public static class Builder {

        private FunctionInternalProperties.Builder properties;

        private String name;

        private FunctionInternalArgument.Builder argumentBuilder;
        private BiFunction<Action, Object[], Object> compute;

        public Builder(){
            properties = new FunctionInternalProperties.Builder();
            argumentBuilder = new FunctionInternalArgument.Builder();
        }


        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setCompute(BiFunction<Action, Object[], Object> compute){
            this.compute = compute;
            return this;
        }

        public Builder addArguments(Consumer<FunctionInternalArgument.Builder> argBuilder){
            argBuilder.accept(argumentBuilder);
            return this;
        }

        public Builder setProperties(Consumer<FunctionInternalProperties.Builder> properties){
            properties.accept(this.properties);
            return this;
        }

        public FunctionInternal build(){

            if(name == null) throw new NullPointerException("Name cannot be null");
            if(compute == null) throw new NullPointerException("Compute cannot be null");

            FunctionInternal functionInternal = new FunctionInternal(properties.build(), argumentBuilder.build(), name) {
                @Override
                public Object compute(Action action, Object... args) {
                    return compute.apply(action, args);
                }
            };

            return functionInternal;

        }
    }
}
