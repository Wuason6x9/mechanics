package dev.wuason.mechanics.actions.args;

import dev.wuason.mechanics.actions.Action;

public abstract class Argument {
    private final String line;

    public Argument(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public abstract Object computeArg(Action action);


}
