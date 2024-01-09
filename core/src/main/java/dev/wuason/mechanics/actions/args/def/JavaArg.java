package dev.wuason.mechanics.actions.args.def;

import bsh.EvalError;
import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;

public class JavaArg extends Argument {
    public JavaArg(String line) {
        super(line);
    }

    @Override
    public Object computeArg(Action action) {
        String code = getLine().trim();
        Object objReturn = null;
        try {
            objReturn = action.getInterpreter().eval(code);
        } catch (EvalError e) {
            throw new RuntimeException(e);
        }
        return objReturn;
    }
}
