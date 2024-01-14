package dev.wuason.mechanics.actions.args.def;

import bsh.EvalError;
import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;

public class JavaArg extends Argument {
    public JavaArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().build(),args);
    }

    @Override
    public Object computeArg(Action action, String line) {
        String code = line.trim();
        Object objReturn = null;
        try {
            objReturn = action.getInterpreter().eval(code);
        } catch (EvalError e) {
            throw new RuntimeException(e);
        }
        return objReturn;
    }
}
