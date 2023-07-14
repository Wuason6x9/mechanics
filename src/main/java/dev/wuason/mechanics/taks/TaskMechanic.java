package dev.wuason.mechanics.taks;
import java.util.UUID;

public class TaskMechanic implements Runnable {

    private final UUID id;
    private final Runnable runnable;
    private boolean isRunning;
    private boolean isCancelled;

    public TaskMechanic(Runnable runnable) {
        this.id = UUID.randomUUID();
        this.runnable = runnable;
    }

    public UUID getId() {
        return this.id;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public void run() {
        if (!isCancelled) {
            this.isRunning = true;

            // Llamar al Runnable proporcionado
            this.runnable.run();

            this.isRunning = false;
        }
    }
}