package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class FloatArg extends Argument {
    public FloatArg(String line) {
        super(line);
    }
    @Override
    public Object computeArg(Action action) {
        return Float.parseFloat(getLine().replace(" ", ""));
    }
}
