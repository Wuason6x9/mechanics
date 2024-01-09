package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class TextArg extends Argument {
    public TextArg(String line) {
        super(line);
    }
    @Override
    public Object computeArg(Action action) {
        return getLine().trim().intern();
    }
}
