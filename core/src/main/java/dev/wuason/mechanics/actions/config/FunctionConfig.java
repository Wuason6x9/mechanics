package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.functions.Function;

import java.util.HashMap;

public class FunctionConfig {

    private final Function function;
    private final HashMap<String,String> args;

    public FunctionConfig(Function function, HashMap<String, String> args) {
        this.function = function;
        this.args = args;
    }

    public Function getFunction() {
        return function;
    }

    public HashMap<String, String> getArgs() {
        return args;
    }

}
