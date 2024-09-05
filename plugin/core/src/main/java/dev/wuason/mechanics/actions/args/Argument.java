package dev.wuason.mechanics.actions.args;

import dev.wuason.mechanics.actions.Action;
import dev.wuason.mechanics.actions.utils.ArgumentUtils;

public abstract class Argument {
    private final String line;
    private Object[] args;

    private final ArgumentProperties properties;

    public Argument(String line, ArgumentProperties properties, Object[] args) {
        this.line = line;
        this.args = args;
        this.properties = properties;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object computeArgInit(Action action) {
        if (!properties.isAutoGetPlaceholder() && !properties.isAutoTransformPlaceholder()) {
            return computeArg(action, line);
        }
        Object placeholder = ArgumentUtils.getArgumentPlaceHolder(line, action);
        if (placeholder != null) {
            if (properties.isAutoGetPlaceholder()) {
                return placeholder;
            }
            if (properties.isAutoTransformPlaceholder()) {
                return computeArg(action, placeholder.toString());
            }
        }
        return computeArg(action, line);
    }

    public abstract Object computeArg(Action action, String line);

    public ArgumentProperties getProperties() {
        return properties;
    }

    public String getOriginalLine() {
        return line;
    }

}
