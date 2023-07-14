package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.wuason.mechanics.config.ConfigManager;
import dev.wuason.mechanics.mechanics.MechanicsManager;
import dev.wuason.mechanics.taks.TaskMechanicManager;
import dev.wuason.mechanics.utils.AdventureUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mechanics extends JavaPlugin {

    private static BukkitAudiences adventure;
    private static Mechanics core = null;
    private MechanicsManager mechanicsManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private TaskMechanicManager taskMechanicManager;



    public Mechanics(){
        if(core==null) core = this;
    }

    @Override
    public void onEnable() {

        adventure = BukkitAudiences.create(this);

        AdventureUtils.sendMessagePluginConsole("Starting mechanics plugin!");

        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
        CommandAPI.onEnable(this);

        taskMechanicManager = new TaskMechanicManager();
        configManager = new ConfigManager(core);
        commandManager = new CommandManager(core);
        mechanicsManager = new MechanicsManager(core);
        mechanicsManager.loadMechanics();

    }

    @Override
    public void onDisable() {

        this.adventure.close();
        taskMechanicManager.shutdown();


    }

    public static BukkitAudiences getAdventureAudiences(){
        return adventure;
    }

    public static Mechanics getInstance() {
        return core;
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

    public TaskMechanicManager getTaskMechanicManager() {
        return taskMechanicManager;
    }

}
