package dev.wuason.mechanics.actions.args;

import dev.wuason.mechanics.actions.Action;

public abstract class Argument {
    private final String line;
    private Object[] args;

    public Argument(String line, Object... args) {
        this.line = line;
        this.args = args;
    }

    public String getLine() {
        return line;
    }

    public Object[] getArgs() {
        return args;
    }

    public abstract Object computeArg(Action action);


}
