package dev.wuason.mechanics;

import dev.wuason.mechanics.invmechanic.InvMechanicListeners;
import dev.wuason.mechanics.items.remover.ItemRemoverManager;
import dev.wuason.mechanics.mechanics.MechanicManager;

public class Manager {
    private CommandManager commandManager;
    private Mechanics core;
    private InvMechanicListeners invMechanicListeners;
    private ItemRemoverManager itemRemoverManager;
    private MechanicManager mechanicManager;

    public Manager(Mechanics core) {
        this.core = core;
    }

    public void load() {
        ItemRemoverManager itemRemoverManager = new ItemRemoverManager(core);
        invMechanicListeners = new InvMechanicListeners(core);
        mechanicManager = new MechanicManager(core);
        core.getServer().getPluginManager().registerEvents(invMechanicListeners, core);
        core.getServer().getPluginManager().registerEvents(itemRemoverManager, core);
        core.getServer().getPluginManager().registerEvents(mechanicManager, core);
        commandManager = new CommandManager(core);
    }

    public void stop() {
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public InvMechanicListeners getInvMechanicListeners() {
        return invMechanicListeners;
    }

    public ItemRemoverManager getItemRemoverManager() {
        return itemRemoverManager;
    }

    public MechanicManager getMechanicManager() {
        return mechanicManager;
    }
}
