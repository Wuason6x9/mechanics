package dev.wuason.mechanics;

import dev.wuason.mechanics.compatibilities.AdapterManager;
import dev.wuason.mechanics.config.ConfigManager;
import dev.wuason.mechanics.mechanics.MechanicsManager;

public class Manager {
    private MechanicsManager mechanicsManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private Mechanics core;
    private AdapterManager adapterManager;

    public Manager(Mechanics core){
        this.core = core;
    }

    public void load(){
        configManager = new ConfigManager(core);
        commandManager = new CommandManager(core);
        mechanicsManager = new MechanicsManager(core);
        mechanicsManager.loadMechanics();
        adapterManager = new AdapterManager(core);
    }
    public void stop(){
        mechanicsManager.stop();
    }


    public MechanicsManager getMechanicsManager() {
        return mechanicsManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public AdapterManager getAdapterManager() {
        return adapterManager;
    }
}
