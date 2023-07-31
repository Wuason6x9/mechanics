package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.wuason.mechanics.config.ConfigManager;
import dev.wuason.mechanics.mechanics.MechanicsManager;
import dev.wuason.mechanics.utils.AdventureUtils;
import io.th0rgal.protectionlib.ProtectionLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mechanics extends JavaPlugin {

    private static BukkitAudiences adventure;
    private static Mechanics core = null;
    private Manager manager;


    public Mechanics(){
        if(core==null) core = this;
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        AdventureUtils.sendMessagePluginConsole("Starting mechanics plugin!");
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
        CommandAPI.onEnable();
        ProtectionLib.init(this);
        this.manager = new Manager(this);
        this.manager.load();

    }

    @Override
    public void onDisable() {

        manager.stop();
        this.adventure.close();
        CommandAPI.onDisable();

    }

    public static BukkitAudiences getAdventureAudiences(){
        return adventure;
    }

    public static Mechanics getInstance() {
        return core;
    }

    public Manager getManager() {
        return manager;
    }
}
