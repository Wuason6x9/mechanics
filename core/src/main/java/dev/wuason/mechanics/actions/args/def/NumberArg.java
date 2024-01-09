package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.utils.NumberUtils;

public class NumberArg extends Argument {
    public NumberArg(String line) {
        super(line);
    }

    @Override
    public Object computeArg(Action action) {
        return NumberUtils.getRangeFormatClosed(getLine());
    }
}
