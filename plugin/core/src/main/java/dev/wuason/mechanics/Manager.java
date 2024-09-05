package dev.wuason.mechanics;

import dev.wuason.mechanics.commands.CommandManager;
import dev.wuason.mechanics.inventory.InventoryListener;
import dev.wuason.mechanics.items.save.ItemsSaverManager;
import dev.wuason.mechanics.items.remover.ItemRemoverManager;
import dev.wuason.mechanics.mechanics.MechanicManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Manager {
    private CommandManager commandManager;
    private Mechanics core;
    private InventoryListener invMechanicListeners;
    private ItemRemoverManager itemRemoverManager;
    private MechanicManager mechanicManager;
    private ItemsSaverManager itemsSaverManager;

    public Manager(Mechanics core) {
        this.core = core;
    }

    public void load() {
        PluginManager pm = core.getServer().getPluginManager();
        ItemRemoverManager itemRemoverManager = new ItemRemoverManager(core);
        invMechanicListeners = new InventoryListener(core);
        mechanicManager = new MechanicManager(core);
        itemsSaverManager = new ItemsSaverManager();
        pm.registerEvents(invMechanicListeners, core);
        pm.registerEvents(itemRemoverManager, core);
        pm.registerEvents(mechanicManager, core);
        pm.registerEvents(itemsSaverManager, core);
        commandManager = new CommandManager(core);
    }

    public void stop() {
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public InventoryListener getInvMechanicListeners() {
        return invMechanicListeners;
    }

    public ItemRemoverManager getItemRemoverManager() {
        return itemRemoverManager;
    }

    public MechanicManager getMechanicManager() {
        return mechanicManager;
    }
}
