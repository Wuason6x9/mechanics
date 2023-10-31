package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.wuason.nms.utils.VersionNmsDetector;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.nms.wrappers.ServerNmsVersion;
import io.th0rgal.protectionlib.ProtectionLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mechanics extends JavaPlugin {

    private static BukkitAudiences adventure;
    private static Mechanics core = null;
    private Manager manager;
    private ServerNmsVersion serverNmsVersion;


    public Mechanics(){
        if(core==null) core = this;
    }

    @Override
    public void onEnable() {
        if(checkVersion()) return;
        serverNmsVersion = new ServerNmsVersion();
        adventure = BukkitAudiences.create(this);
        AdventureUtils.sendMessagePluginConsole("Starting mechanics plugin!");
        AdventureUtils.sendMessagePluginConsole("NMS: <aqua>" + VersionNmsDetector.getServerVersion());
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

    public boolean checkVersion(){
        if(VersionNmsDetector.getServerVersion().equals(VersionNmsDetector.ServerVersion.UNSUPPORTED)){
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("                 Unsupported version minecraft ");
            core.getLogger().severe("                     Actual version: " + VersionNmsDetector.getNMSVersion());
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            Bukkit.getPluginManager().disablePlugin(core);
            return true;
        }
        return false;
    }

    public ServerNmsVersion getServerNmsVersion() {
        return serverNmsVersion;
    }
}
