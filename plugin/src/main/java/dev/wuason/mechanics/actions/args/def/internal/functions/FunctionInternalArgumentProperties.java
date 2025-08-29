package dev.wuason.mechanics.actions.args.def.internal.functions;

public class FunctionInternalArgumentProperties {
    private boolean processArg;
    private boolean processArgSearchArgs;
    private boolean autoGetPlaceholder;
    private boolean autoGetNull;
    private boolean required;

    private FunctionInternalArgumentProperties(boolean processArg, boolean processArgSearchArgs, boolean autoGetPlaceholder, boolean autoGetNull, boolean required) {
        this.processArg = processArg;
        this.processArgSearchArgs = processArgSearchArgs;
        this.autoGetPlaceholder = autoGetPlaceholder;
        this.autoGetNull = autoGetNull;
        this.required = required;
    }

    public boolean isProcessArg() {
        return processArg;
    }

    public void setProcessArg(boolean processArg) {
        this.processArg = processArg;
    }

    public boolean isProcessArgSearchArgs() {
        return processArgSearchArgs;
    }

    public void setProcessArgSearchArgs(boolean processArgSearchArgs) {
        this.processArgSearchArgs = processArgSearchArgs;
    }

    public boolean isAutoGetPlaceholder() {
        return autoGetPlaceholder;
    }

    public void setAutoGetPlaceholder(boolean autoGetPlaceholder) {
        this.autoGetPlaceholder = autoGetPlaceholder;
    }

    public boolean isAutoGetNull() {
        return autoGetNull;
    }

    public void setAutoGetNull(boolean autoGetNull) {
        this.autoGetNull = autoGetNull;
    }

    public boolean isRequired() {
        return required;
    }

    public static class Builder {
        private boolean processArg = true;
        private boolean processArgSearchArgs = true;
        private boolean autoGetPlaceholder = true;
        private boolean autoGetNull = false;
        private boolean required = false;

        public Builder() {
        }

        public FunctionInternalArgumentProperties.Builder setProcessArg(boolean processArg) {
            this.processArg = processArg;
            return this;
        }

        public FunctionInternalArgumentProperties.Builder setProcessArgSearchArgs(boolean processArgSearchArgs) {
            this.processArgSearchArgs = processArgSearchArgs;
            return this;
        }

        public FunctionInternalArgumentProperties.Builder setAutoGetPlaceholder(boolean autoGetPlaceholder) {
            this.autoGetPlaceholder = autoGetPlaceholder;
            return this;
        }

        public FunctionInternalArgumentProperties.Builder setAutoGetNull(boolean autoGetNull) {
            this.autoGetNull = autoGetNull;
            return this;
        }

        public FunctionInternalArgumentProperties.Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public FunctionInternalArgumentProperties build() {
            return new FunctionInternalArgumentProperties(processArg, processArgSearchArgs, autoGetPlaceholder, autoGetNull, required);
        }
    }
}
