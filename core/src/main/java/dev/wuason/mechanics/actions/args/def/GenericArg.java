package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class GenericArg extends Argument {
    private final String name;
    public GenericArg(String name, String line, Object... args) {
        super(line, args);
        this.name = name;
    }

    @Override
    public Object computeArg(Action action) {
        return null;
    }
}
