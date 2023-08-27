package dev.wuason.mechanics.mechanics;

public class MechanicInfo {
    private String[] dependencies;
    private String id;
    private String name;
    private int priority;

    public MechanicInfo(String[] dependencies, String id, String name, int priority) {
        this.dependencies = dependencies;
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
