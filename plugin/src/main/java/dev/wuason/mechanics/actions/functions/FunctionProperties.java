package dev.wuason.mechanics.actions.functions;

public class FunctionProperties {
    private boolean processArgs;
    private boolean processArgsSearchArgs;

    private FunctionProperties(boolean processArgs, boolean processArgsSearchArgs) {
        this.processArgs = processArgs;
        this.processArgsSearchArgs = processArgsSearchArgs;
    }

    public boolean isProcessArgs() {
        return processArgs;
    }

    public void setProcessArgs(boolean processArgs) {
        this.processArgs = processArgs;
    }

    public boolean isProcessArgsSearchArgs() {
        return processArgsSearchArgs;
    }

    public void setProcessArgsSearchArgs(boolean processArgsSearchArgs) {
        this.processArgsSearchArgs = processArgsSearchArgs;
    }

    public static class Builder {
        private boolean processArgs = true;
        private boolean processArgsSearchArgs = true;

        public Builder() {
        }

        public Builder setProcessArgs(boolean processArgs) {
            this.processArgs = processArgs;
            return this;
        }

        public Builder setProcessArgsSearchArgs(boolean processArgsSearchArgs) {
            this.processArgsSearchArgs = processArgsSearchArgs;
            return this;
        }

        public FunctionProperties build() {
            return new FunctionProperties(processArgs, processArgsSearchArgs);
        }
    }
}
