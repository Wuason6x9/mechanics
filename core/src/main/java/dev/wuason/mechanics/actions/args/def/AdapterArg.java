package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.compatibilities.adapter.Adapter;

public class AdapterArg extends Argument {
    public AdapterArg(String line) {
        super(line);
    }
    @Override
    public Object computeArg(Action action) {
        Adapter adapter = Adapter.getInstance();
        return adapter.computeAdapterId(getLine());
    }
}
