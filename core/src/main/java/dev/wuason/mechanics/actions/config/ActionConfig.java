package dev.wuason.mechanics.actions.config;

import dev.wuason.mechanics.actions.events.EventAction;
import dev.wuason.mechanics.actions.executators.Executor;
import dev.wuason.mechanics.actions.executators.Run;

import java.util.Collection;

public class ActionConfig {

    private final Collection<String> imports;
    private final Run runType;
    private final Executor executor;
    private final EventAction eventAction;
    private final String id;





    //*********** GETTERS ***********//

    public Collection<String> getImports() {
        return imports;
    }

    public Run getRunType() {
        return runType;
    }

    public Executor getExecutor() {
        return executor;
    }

    public EventAction getEventAction() {
        return eventAction;
    }

    public String getId() {
        return id;
    }



}
