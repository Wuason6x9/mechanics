package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.args.Argument;

public class ArgumentConfig {
    private Class<? extends Argument> type;
    private String argument;

    public ArgumentConfig(Class<? extends Argument> type, String argument) {
        this.type = type;
        this.argument = argument;
    }

    public Class<? extends Argument> getType() {
        return type;
    }

    public String getArgument() {
        return argument;
    }
}
