package dev.wuason.mechanics.actions.executators;

import dev.wuason.mechanics.actions.Action;

import java.util.HashMap;

public abstract class Executor {


    private final String id;
    private final Class<?> clazz;

    public Executor(String id, Class<?> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public String getId() {
        return id;
    }

    public Class<?> getExecutorClass() {
        return clazz;
    }

    public abstract void registerPlaceholders(Action action);
}
