package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.utils.AsciiUtils;
import dev.wuason.nms.utils.VersionNMS;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.nms.wrappers.NMSManager;
import io.th0rgal.protectionlib.ProtectionLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.Arrays;

public final class Mechanics extends JavaPlugin {

    private static BukkitAudiences adventure;
    private static Mechanics core = null;
    private Manager manager;
    private NMSManager NMSManager;


    public Mechanics(){
        if(core==null) core = this;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        if(checkVersion()) return;
        NMSManager = new NMSManager();
        adventure = BukkitAudiences.create(this);
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gold>Starting mechanics plugin!");
        AdventureUtils.sendMessagePluginConsole("<gold>NMS: <aqua>" + VersionNMS.getServerVersion());
        AdventureUtils.sendMessagePluginConsole("<gold>Mechanics loaded: <aqua>" + Arrays.stream(Bukkit.getPluginManager().getPlugins()).filter(plugin -> plugin instanceof MechanicAddon).map(plugin -> plugin.getName()).toList());
        CommandAPI.onEnable();
        ProtectionLib.init(this);
        this.manager = new Manager(this);
        this.manager.load();
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        System.out.println();
    }

    @Override
    public void onDisable() {

        manager.stop();
        adventure.close();
        CommandAPI.onDisable();

    }

    /**
     * Retrieves the AdventureAudiences instance.
     *
     * @return The AdventureAudiences instance.
     */
    public static BukkitAudiences getAdventureAudiences(){
        return adventure;
    }

    /**
     * Retrieves the instance of the Mechanics class.
     *
     * @return The Mechanics instance.
     */
    public static Mechanics getInstance() {
        return core;
    }

    public Manager getManager() {
        return manager;
    }

    public void printMechanics(){
        AdventureUtils.sendMessagePluginConsole("<gold>" + AsciiUtils.convertToAscii(AsciiUtils.createTextImage("Mechanics", new Font("Arial", Font.BOLD, 25), Color.BLACK)));
    }

    public boolean checkVersion(){
        if(VersionNMS.getServerVersion().equals(VersionNMS.ServerVersion.UNSUPPORTED)){
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("                 Unsupported version minecraft ");
            core.getLogger().severe("                     Actual version: " + VersionNMS.getNMSVersion());
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            Bukkit.getPluginManager().disablePlugin(core);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the version of the server's NMS (Net Minecraft Server) version.
     *
     * @return The ServerNmsVersion instance.
     */
    public NMSManager getServerNmsVersion() {
        return NMSManager;
    }
}
