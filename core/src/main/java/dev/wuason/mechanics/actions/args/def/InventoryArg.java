package dev.wuason.mechanics.actions.args.def;

import bsh.EvalError;
import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class InventoryArg extends Argument {
    public InventoryArg(String line) {
        super(line);
    }
    @Override
    public Object computeArg(Action action) {
        return action.runCode("$inventory$.".toUpperCase() + getLine());
    }
}
