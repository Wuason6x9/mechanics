package dev.wuason.mechanics;

import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import dev.wuason.mechanics.invmechanic.InvMechanicListeners;

public class Manager {
    private CommandManager commandManager;
    private Mechanics core;
    private InvMechanicListeners invMechanicListeners;

    public Manager(Mechanics core){
        this.core = core;
    }

    public void load(){
        invMechanicListeners = new InvMechanicListeners(core);
        core.getServer().getPluginManager().registerEvents(invMechanicListeners, core);
        new Adapter(core);
        commandManager = new CommandManager(core);
    }
    public void stop(){
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public InvMechanicListeners getInvMechanicListeners() {
        return invMechanicListeners;
    }
}
