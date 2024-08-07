package dev.wuason.mechanics.actions.args.def;

import bsh.EvalError;
import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;

public class InventoryArg extends Argument {
    public InventoryArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).build(), args);
    }
    @Override
    public Object computeArg(Action action, String line) {
        return action.runCode("$inventory$.".toUpperCase() + line);
    }
}
