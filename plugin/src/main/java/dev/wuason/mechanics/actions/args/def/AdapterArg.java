package dev.wuason.mechanics.actions.args.def;

import dev.wuason.adapter.Adapter;
import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;

import static javax.sound.sampled.AudioSystem.getLine;

public class AdapterArg extends Argument {
    public AdapterArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).build(),args);
    }
    @Override
    public Object computeArg(Action action, String line) {
        if (!Adapter.isValid(line)) throw new IllegalArgumentException("Invalid adapter id: " + line);
        return line;
    }
}
