package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.functions.Function;

import java.util.Map;

public class FunctionConfig {

    private final Function function;
    private Map<String, String> args;

    public FunctionConfig(Function function, Map<String, String> args) {
        this.function = function;
        this.args = args;
    }

    public Function getFunction() {
        return function;
    }

    public Map<String, String> getArgs() {
        return args;
    }

}
