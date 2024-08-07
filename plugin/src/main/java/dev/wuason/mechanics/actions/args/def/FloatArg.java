package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;

public class FloatArg extends Argument {
    public FloatArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).build(),args);
    }
    @Override
    public Object computeArg(Action action, String line) {
        return Float.parseFloat(line.replace(" ", ""));
    }
}
