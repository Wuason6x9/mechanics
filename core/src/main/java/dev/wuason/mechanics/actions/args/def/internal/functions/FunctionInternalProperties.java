package dev.wuason.mechanics.actions.args.def.internal.functions;

public class FunctionInternalProperties {


    private boolean processArgs;
    private boolean processArgsSearchArgs;

    private FunctionInternalProperties(boolean processArgs, boolean processArgsSearchArgs) {
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

        public FunctionInternalProperties.Builder setProcessArgs(boolean processArgs) {
            this.processArgs = processArgs;
            return this;
        }

        public FunctionInternalProperties.Builder setProcessArgsSearchArgs(boolean processArgsSearchArgs) {
            this.processArgsSearchArgs = processArgsSearchArgs;
            return this;
        }

        public FunctionInternalProperties build() {
            return new FunctionInternalProperties(processArgs, processArgsSearchArgs);
        }
    }
}
