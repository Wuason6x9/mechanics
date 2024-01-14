package dev.wuason.mechanics.actions.args.def.internal.functions;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.config.FunctionInternalConfig;
import dev.wuason.mechanics.actions.functions.FunctionArgument;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class FunctionInternal {
    private final FunctionInternalProperties properties;
    private final FunctionInternalConfig config;
    private final HashMap<String, FunctionInternalArgument> argumentsRequired;

    public FunctionInternal(FunctionInternalProperties properties, HashMap<String, FunctionInternalArgument> argumentsRequired, FunctionInternalConfig config) {
        this.properties = properties;
        this.argumentsRequired = argumentsRequired;
        this.config = config;
    }

    public FunctionInternalProperties getProperties() {
        return properties;
    }
    public HashMap<String, FunctionInternalArgument> getArgumentsRequired() {
        return argumentsRequired;
    }

    public FunctionInternalConfig getConfig() {
        return config;
    }

    public Object computeInit(Action action, Object... args){
        return compute(action, args);
    }

    public abstract Object compute(Action action, Object... args);

    public FunctionInternalArgument[] getOrderedArgs(){
        FunctionInternalArgument[] functionArguments = new FunctionInternalArgument[getArgumentsRequired().size()];
        for(Map.Entry<String, FunctionInternalArgument> entry : getArgumentsRequired().entrySet()){
            functionArguments[entry.getValue().getOrder()] = entry.getValue();
        }
        return functionArguments;
    }

    /*public static class Builder {

        private FunctionInternalProperties.Builder properties;
        private FunctionInternalConfig config;
        private FunctionInternalArgument.Builder argumentBuilder;
        private BiFunction<Action, Object[], Object> compute;

        public Builder(){
            properties = new FunctionInternalProperties.Builder();
            argumentBuilder = new FunctionInternalArgument.Builder();
        }

        public Builder setConfig(FunctionInternalConfig config){
            this.config = config;
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



        }
    }*/
}
