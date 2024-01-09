package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class VarArg extends Argument {
    public VarArg(String line) {
        super(line);
    }

    @Override
    public Object computeArg(Action action) {
        return null;
    }
}
