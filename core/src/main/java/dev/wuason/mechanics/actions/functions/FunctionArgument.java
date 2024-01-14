package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.utils.ArgumentUtils;
import org.apache.commons.lang3.IntegerRange;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public abstract class FunctionArgument {

    private final String name;
    private final int order;
    private final FunctionArgumentProperties properties;

    public FunctionArgument(@NotNull String name, @NotNull int order, @NotNull FunctionArgumentProperties properties) {
        this.name = name.toUpperCase(Locale.ENGLISH);
        this.order = order;
        this.properties = properties;
    }

    public Object computeArgInit(String line, Action action, Object... args){
        if(line == null && properties.isAutoGetNull()) return null;
        if(properties.isAutoGetPlaceholder()){
            Object placeholder = ArgumentUtils.getArgumentPlaceHolder(line, action);
            if(placeholder != null) return placeholder;
        }
        return computeArg(line, action, args);

    }

    public abstract Object computeArg(String line, Action action, Object... args); //line is the line of the argument, action is the action that is being executed, args before the arguments

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public FunctionArgumentProperties getProperties() {
        return properties;
    }


    public static class Builder {

        private HashMap<Integer, TriFunction<String, Action, Object[], Object>> consumers = new HashMap<>();
        private HashMap<Integer, String> names = new HashMap<>();
        private HashMap<Integer, FunctionArgumentProperties.Builder> propertiesList = new HashMap<>();
        private HashSet<Integer> args = new HashSet<>();

        public Builder(){
        }

        public FunctionArgument.Builder addArgument(int order, String name, TriFunction<String, Action, Object[], Object> computeArg, Consumer<FunctionArgumentProperties.Builder> argPropertiesBuilder){
            args.add(order);
            names.put(order, name);
            consumers.put(order, computeArg);
            FunctionArgumentProperties.Builder argBuilder = new FunctionArgumentProperties.Builder();
            argPropertiesBuilder.accept(argBuilder);
            propertiesList.put(order, argBuilder);
            return this;
        }

        public FunctionArgument.Builder addArgument(int order, String name, Consumer<FunctionArgumentProperties.Builder> argPropertiesBuilder) {
            addArgument(order,name, (s, action, objects) -> null, argPropertiesBuilder);
            return this;
        }

        public FunctionArgument.Builder addArgument(int order, String name, TriFunction<String, Action, Object[], Object> computeArg) {
            addArgument(order,name, computeArg, builder -> {});
            return this;
        }

        public FunctionArgument.Builder addArgument(int order, String name) {
            addArgument(order,name,builder -> {});
            return this;
        }

        public HashMap<String, FunctionArgument> build(){
            HashMap<String, FunctionArgument> args = new HashMap<>();

            Integer[] order = this.args.toArray(Integer[]::new);
            for(int i = 0; i<order.length; i++){

                String name = names.get(order[i]);

                if(name == null) continue;

                TriFunction<String, Action, Object[], Object> computeArg = consumers.getOrDefault(order[i], (s, action, objects) -> null);

                FunctionArgumentProperties.Builder argPropertiesBuilder = propertiesList.getOrDefault(order[i], new FunctionArgumentProperties.Builder());

                FunctionArgument fArg = new FunctionArgument(name, order[i], argPropertiesBuilder.build()) {
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
