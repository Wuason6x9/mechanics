package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;
import dev.wuason.mechanics.utils.NumberUtils;

public class NumberArg extends Argument {
    public NumberArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).build(), args);
    }

    @Override
    public Object computeArg(Action action, String line) {
        return NumberUtils.getRangeFormatClosed(line);
    }
}
