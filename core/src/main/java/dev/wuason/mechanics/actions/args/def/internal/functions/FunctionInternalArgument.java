package dev.wuason.mechanics.actions.args.def.internal.functions;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.functions.FunctionArgument;
import dev.wuason.mechanics.actions.functions.FunctionArgumentProperties;
import dev.wuason.mechanics.actions.utils.ArgumentUtils;
import org.apache.commons.lang3.function.TriFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.function.Consumer;

public abstract class FunctionInternalArgument {
    private final String name;
    private final int order;
    private final FunctionInternalArgumentProperties properties;

    public FunctionInternalArgument(String name, int order, FunctionInternalArgumentProperties properties) {
        this.name = name.toUpperCase(Locale.ENGLISH);
        this.order = order;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public FunctionInternalArgumentProperties getProperties() {
        return properties;
    }

    public Object computeArgInit(String line, Action action, Object... args){
        if(line == null && properties.isAutoGetNull()) return null;
        if(properties.isAutoGetPlaceholder()){
            Object placeholder = ArgumentUtils.getArgumentPlaceHolder(line, action);
            if(placeholder != null) return placeholder;
        }
        return computeArg(line, action, args);

    }

    public abstract Object computeArg(String line, Action action,  Object... args);

    public static class Builder {
        private HashMap<Integer, TriFunction<String, Action, Object[], Object>> consumers = new HashMap<>();
        private HashMap<Integer, String> names = new HashMap<>();
        private HashMap<Integer, FunctionInternalArgumentProperties.Builder> propertiesList = new HashMap<>();
        private HashSet<Integer> args = new HashSet<>();

        public Builder(){
        }

        public FunctionInternalArgument.Builder addArgument(int order, String name, TriFunction<String, Action, Object[], Object> computeArg, Consumer<FunctionInternalArgumentProperties.Builder> argPropertiesBuilder){
            args.add(order);
            names.put(order, name);
            consumers.put(order, computeArg);
            FunctionInternalArgumentProperties.Builder argBuilder = new FunctionInternalArgumentProperties.Builder();
            argPropertiesBuilder.accept(argBuilder);
            propertiesList.put(order, argBuilder);
            return this;
        }

        public FunctionInternalArgument.Builder addArgument(int order, String name, Consumer<FunctionInternalArgumentProperties.Builder> argPropertiesBuilder) {
            addArgument(order,name, (s, action, objects) -> null, argPropertiesBuilder);
            return this;
        }

        public FunctionInternalArgument.Builder addArgument(int order, String name, TriFunction<String, Action, Object[], Object> computeArg) {
            addArgument(order,name, computeArg, builder -> {});
            return this;
        }

        public FunctionInternalArgument.Builder addArgument(int order, String name) {
            addArgument(order,name,builder -> {});
            return this;
        }

        public HashMap<String, FunctionInternalArgument> build(){
            HashMap<String, FunctionInternalArgument> args = new HashMap<>();
            Integer[] order = this.args.toArray(Integer[]::new);
            for(int i = 0; i<order.length; i++){
                String name = names.get(order[i]);
                if(name == null) continue;
                TriFunction<String, Action, Object[], Object> computeArg = consumers.getOrDefault(order[i], (s, action, objects) -> null);
                FunctionInternalArgumentProperties.Builder argPropertiesBuilder = propertiesList.getOrDefault(order[i],
                        new FunctionInternalArgumentProperties.Builder());
                FunctionInternalArgument fArg = new FunctionInternalArgument(name, order[i], argPropertiesBuilder.build()) {
                    @Override
                    public Object computeArg(String line, Action action, Object... args) {
                        return computeArg.apply(line, action, args);
                    }
                };
                args.put(fArg.getName(), fArg);
            }
            return args;
        }
    }

}
