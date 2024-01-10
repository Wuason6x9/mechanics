package dev.wuason.mechanics.actions.events;

import dev.wuason.mechanics.actions.events.defbukkit.OnJoinEvent;

import java.util.HashMap;

public class Events {
    public static final HashMap<String, Class<? extends EventAction>> EVENTS = new HashMap<>();


    static {

        EVENTS.put("onJoin".toUpperCase(), OnJoinEvent.class);

    }



}
