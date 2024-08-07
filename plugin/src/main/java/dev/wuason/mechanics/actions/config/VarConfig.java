package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.args.Argument;

public class VarConfig {
    private final String var;
    private final ArgumentConfig argument;

    public VarConfig(String var, ArgumentConfig argument) {
        this.var = var;
        this.argument = argument;
    }

    public String getVar() {
        return var;
    }

    public ArgumentConfig getArgument() {
        return argument;
    }
}
