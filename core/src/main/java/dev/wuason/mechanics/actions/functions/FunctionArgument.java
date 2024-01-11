package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class FunctionArgument implements Comparable<FunctionArgument> {

    private final String name;
    private final int order;

    public FunctionArgument(String name, int order) {
        this.name = name.toUpperCase(Locale.ENGLISH);
        this.order = order;
    }


    public abstract Object computeArg(String line, Action action, Object... args); //line is the line of the argument, action is the action that is being executed, args before the arguments

    @Override
    public int compareTo(@NotNull FunctionArgument functionArgument) {
        if(this.order < functionArgument.order) {
            return 1;
        } else if(this.order > functionArgument.order) {
            return -1;
        } else {
            return 0;
        }
    }
}
