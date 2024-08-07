package dev.wuason.mechanics.actions.functions;

public class FunctionArgumentProperties {
    private boolean processArg;
    private boolean processArgSearchArgs;
    private boolean autoGetPlaceholder;
    private boolean autoGetNull;
    private boolean required;

    private FunctionArgumentProperties(boolean processArg, boolean processArgSearchArgs, boolean autoGetPlaceholder, boolean autoGetNull, boolean required) {
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

    public boolean isAutoGetNull() {
        return autoGetNull;
    }

    public void setAutoGetNull(boolean autoGetNull) {
        this.autoGetNull = autoGetNull;
    }

    public boolean isAutoGetPlaceholder() {
        return autoGetPlaceholder;
    }

    public void setAutoGetPlaceholder(boolean autoGetPlaceholder) {
        this.autoGetPlaceholder = autoGetPlaceholder;
    }

    public boolean isRequired() {
        return required;
    }

    public static class Builder {
        private boolean processArg = true;
        private boolean processArgSearchArgs = true;
        private boolean autoGetNull = false;
        private boolean autoGetPlaceholder = true;
        private boolean required = false;

        public Builder() {
        }

        public FunctionArgumentProperties.Builder setProcessArg(boolean processArg) {
            this.processArg = processArg;
            return this;
        }

        public FunctionArgumentProperties.Builder setProcessArgSearchArgs(boolean processArgSearchArgs) {
            this.processArgSearchArgs = processArgSearchArgs;
            return this;
        }

        public FunctionArgumentProperties.Builder setAutoGetPlaceholder(boolean autoGetPlaceholder) {
            this.autoGetPlaceholder = autoGetPlaceholder;
            return this;
        }

        public FunctionArgumentProperties.Builder setAutoGetNull(boolean autoGetNull) {
            this.autoGetNull = autoGetNull;
            return this;
        }

        public FunctionArgumentProperties.Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public FunctionArgumentProperties build() {
            return new FunctionArgumentProperties(processArg, processArgSearchArgs, autoGetPlaceholder, autoGetNull, required);
        }
    }
}
