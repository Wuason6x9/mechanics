package dev.wuason.mechanics.actions.args.def;

import bsh.EvalError;
import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class EventArg extends Argument {

    public EventArg(String line) {
        super(line);
    }

    @Override
    public Object computeArg(Action action) {
        try {
            return action.getInterpreter().eval("$event$.".toUpperCase() + getLine());
        } catch (EvalError e) {
            throw new RuntimeException(e);
        }
    }
}
