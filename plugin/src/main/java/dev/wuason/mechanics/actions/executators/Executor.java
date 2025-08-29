package dev.wuason.mechanics.actions.executators;

import dev.wuason.mechanics.actions.Action;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Executor {


    private final String id;
    private final Class<?> clazz;

    public Executor(String id, Class<?> clazz) {
        this.id = id.toUpperCase(Locale.ENGLISH);
        this.clazz = clazz;
    }

    public String getId() {
        return id;
    }

    public Class<?> getExecutorClass() {
        return clazz;
    }

    public abstract void registerPlaceholders(Action action);

    public static class Builder {

        private String id;
        private Class<?> clazz;
        private Consumer<Action> consumer;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setExecutorClass(Class<?> executorClass) {
            this.clazz = clazz;
            return this;
        }

        public Builder setRegisterPlaceholders(Consumer<Action> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Executor build() {

            Objects.requireNonNull(id, "id cannot be null");

            return new Executor(id, clazz) {
                @Override
                public void registerPlaceholders(Action action) {
                    if(consumer != null) consumer.accept(action);
                }
            };
        }

    }
}
