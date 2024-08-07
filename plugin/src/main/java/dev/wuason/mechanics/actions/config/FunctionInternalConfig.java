package dev.wuason.mechanics.actions.config;

import java.util.HashMap;

public class FunctionInternalConfig {

    private final String id;
    private final HashMap<String, String> args;

    public FunctionInternalConfig(String id, HashMap<String, String> args) {
        this.id = id;
        this.args = args;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, String> getArgs() {
        return args;
    }
}
