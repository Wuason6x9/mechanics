package dev.wuason.mechanics.actions.args.def.internal;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;

public class PlayerArg extends Argument {
    public PlayerArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).build(), args);
    }

    @Override
    public Object computeArg(Action action, String line) {
        return action.runCode("$player$." + line);
    }
}
