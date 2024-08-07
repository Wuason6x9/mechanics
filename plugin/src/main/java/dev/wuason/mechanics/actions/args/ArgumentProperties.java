package dev.wuason.mechanics.actions.args;

public class ArgumentProperties {
    private final boolean autoGetPlaceholder;
    private final boolean autoTransformPlaceholder;
    private final boolean reSearchPlaceholders;

    private ArgumentProperties(boolean autoGetPlaceholder, boolean autoTransformPlaceholder, boolean reSearchPlaceholders) {
        this.autoGetPlaceholder = autoGetPlaceholder;
        this.autoTransformPlaceholder = autoTransformPlaceholder;
        this.reSearchPlaceholders = reSearchPlaceholders;
    }

    public boolean isAutoGetPlaceholder() {
        return autoGetPlaceholder;
    }

    public boolean isAutoTransformPlaceholder() {
        return autoTransformPlaceholder;
    }

    public boolean isReSearchPlaceholders() {
        return reSearchPlaceholders;
    }

    public static class Builder {
        private boolean autoGetPlaceholder;
        private boolean autoTransformPlaceholder;
        private boolean reSearchPlaceholders;

        public Builder() {
            this.autoGetPlaceholder = false;
            this.autoTransformPlaceholder = false;
            this.reSearchPlaceholders = true;
        }

        public Builder setAutoGetPlaceholder(boolean autoGetPlaceholder) {
            this.autoGetPlaceholder = autoGetPlaceholder;
            return this;
        }

        public Builder setReSearchPlaceholders(boolean reSearchPlaceholders) {
            this.reSearchPlaceholders = reSearchPlaceholders;
            return this;
        }

        public Builder setAutoTransformPlaceholder(boolean autoTransformPlaceholder) {
            this.autoTransformPlaceholder = autoTransformPlaceholder;
            return this;
        }

        public ArgumentProperties build() {
            return new ArgumentProperties(autoGetPlaceholder, autoTransformPlaceholder, reSearchPlaceholders);
        }
    }
}
