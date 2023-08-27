package dev.wuason.mechanics.mechanics;

import org.jetbrains.annotations.NotNull;

public class MechanicLoad implements Comparable<MechanicLoad> {

    private Mechanic mechanic;
    private int prioridad;

    public MechanicLoad(Mechanic mechanic, int prioridad) {
        this.mechanic = mechanic;
        this.prioridad = prioridad;
    }

    @Override
    public int compareTo(@NotNull MechanicLoad o) {
        return o.prioridad - this.prioridad;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }
}
