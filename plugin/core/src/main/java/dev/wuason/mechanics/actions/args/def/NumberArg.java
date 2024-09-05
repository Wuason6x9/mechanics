package dev.wuason.mechanics.actions.args.def;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.args.Argument;
import dev.wuason.mechanics.actions.args.ArgumentProperties;
import dev.wuason.mechanics.utils.NumberUtils;

import java.util.List;

public class NumberArg extends Argument {
    public NumberArg(String line, Object[] args) {
        super(line, new ArgumentProperties.Builder().setAutoTransformPlaceholder(true).build(), args);
    }

    @Override
    public Object computeArg(Action action, String line) {
        List<Integer> numbers = NumberUtils.getRangeFormatClosed(line);
        if(numbers.size() == 1) return numbers.get(0);
        return numbers;
    }
}
