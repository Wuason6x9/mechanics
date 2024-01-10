package dev.wuason.mechanics.actions.functions;

import dev.wuason.mechanics.actions.Action;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public abstract class Function {

    private final Collection<FunctionArgument> args;
    private final String name;

    public Function(String name, Collection<FunctionArgument> args) {
        this.name = name;
        this.args = args;
    }

    public Collection<FunctionArgument> getArgs() {
        return args;
    }

    public String getName() {
        return name;
    }

    public abstract boolean execute(Action action, Object... Args);



}
