package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.args.Argument;

public class VarConfig<T extends Argument> {
    private final String var;
    private final String value;
    private final T argument;

    public VarConfig(String var, String value, T argument) {
        this.var = var;
        this.value = value;
        this.argument = argument;
    }

    public String getVar() {
        return var;
    }

    public String getValue() {
        return value;
    }

    public T getArgument() {
        return argument;
    }
}
