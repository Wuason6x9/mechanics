package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class FunctionArgument {

    private final String name;
    private final int order;
    private final FunctionArgumentProperties properties;

    public FunctionArgument(@NotNull String name, @NotNull int order, @NotNull FunctionArgumentProperties properties) {
        this.name = name.toUpperCase(Locale.ENGLISH);
        this.order = order;
        this.properties = properties;
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
}
