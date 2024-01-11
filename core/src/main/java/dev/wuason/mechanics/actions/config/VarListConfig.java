package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.args.Argument;

import java.util.ArrayList;

public class VarListConfig<T extends Argument> {
    private final String id;
    private final String var;

    private final ArrayList<T> arguments;

    public VarListConfig(String id, String var, ArrayList<T> arguments) {
        this.id = id;
        this.var = var;
        this.arguments = arguments;
    }

    public String getId() {
        return id;
    }

    public String getVar() {
        return var;
    }

    public ArrayList<T> getArguments() {
        return arguments;
    }
}
