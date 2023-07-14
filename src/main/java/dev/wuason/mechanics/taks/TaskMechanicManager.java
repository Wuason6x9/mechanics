package dev.wuason.mechanics.taks;

import java.util.UUID;
import java.util.concurrent.*;

public class TaskMechanicManager {

    private final ExecutorService executor;
    private final ConcurrentHashMap<UUID, Future<?>> taskFutures;

    public TaskMechanicManager() {
        this.executor = Executors.newSingleThreadExecutor();
        this.taskFutures = new ConcurrentHashMap<>();
    }

    public UUID scheduleTask(TaskMechanic task) {
        Future<?> future = executor.submit(task);
        taskFutures.put(task.getId(), future);
        return task.getId();
    }

    public void cancelTask(UUID id) {
        Future<?> future = taskFutures.get(id);
        if (future != null) {
            future.cancel(true);
            taskFutures.remove(id);
        }
    }

    public void cancelAllTasks() {
        for (Future<?> future : taskFutures.values()) {
            future.cancel(true);
        }
        taskFutures.clear();
    }

    public void shutdown() {
        executor.shutdown();
    }
}