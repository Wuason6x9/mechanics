package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.functions.Function;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionConfig {

    private final Function function;
    private ArrayList<String[]> args;

    public FunctionConfig(Function function, ArrayList<String[]> args) {
        this.function = function;
        this.args = args;
    }

    public Function getFunction() {
        return function;
    }

    public ArrayList<String[]> getArgs() {
        return args;
    }

}
