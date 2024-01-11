package dev.wuason.mechanics.actions.functions;

public class FunctionArgumentProperties {
    private boolean processArg;
    private boolean processArgSearchArgs;

    private FunctionArgumentProperties(boolean processArg, boolean processArgSearchArgs) {
        this.processArg = processArg;
        this.processArgSearchArgs = processArgSearchArgs;
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

    public static class Builder {
        private boolean processArg = true;
        private boolean processArgSearchArgs = true;

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

        public FunctionArgumentProperties build() {
            return new FunctionArgumentProperties(processArg, processArgSearchArgs);
        }
    }
}
