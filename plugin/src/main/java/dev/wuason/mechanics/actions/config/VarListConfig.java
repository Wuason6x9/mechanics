package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.args.Argument;

import java.util.List;

public class VarListConfig<T extends Class<? extends Argument>> {
    private final String var;
    private final Class<T> type;
    private final List<ArgumentConfig> arguments;

    public VarListConfig(String var, Class<T> type, List<ArgumentConfig> arguments) {
        this.var = var;
        this.arguments = arguments;
        this.type = type;
    }

    public String getVar() {
        return var;
    }

    public List<ArgumentConfig> getArguments() {
        return arguments;
    }

    public Class<T> getType() {
        return type;
    }
}
