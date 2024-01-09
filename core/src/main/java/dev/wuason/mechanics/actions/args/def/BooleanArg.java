package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class BooleanArg extends Argument {
    public BooleanArg(String line) {
        super(line);
    }
    @Override
    public Object computeArg(Action action) {
        return "true".equalsIgnoreCase(getLine().replace(" ", ""));
    }
}
