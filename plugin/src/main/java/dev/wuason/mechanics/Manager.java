package dev.wuason.mechanics;

import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import dev.wuason.mechanics.invmechanic.InvMechanicListeners;
import dev.wuason.mechanics.items.remover.ItemRemoverManager;

public class Manager {
    private CommandManager commandManager;
    private Mechanics core;
    private InvMechanicListeners invMechanicListeners;
    private ItemRemoverManager itemRemoverManager;

    public Manager(Mechanics core){
        this.core = core;
    }

    public void load(){
        ItemRemoverManager itemRemoverManager = new ItemRemoverManager(core);
        invMechanicListeners = new InvMechanicListeners(core);
        core.getServer().getPluginManager().registerEvents(invMechanicListeners, core);
        core.getServer().getPluginManager().registerEvents(itemRemoverManager, core);
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
