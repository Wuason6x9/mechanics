package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.wuason.mechanics.library.LibraryResolver;
import dev.wuason.mechanics.library.classpath.MechanicClassLoader;
import dev.wuason.mechanics.library.dependencies.*;
import dev.wuason.mechanics.library.repositories.Repos;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.utils.AsciiUtils;
import dev.wuason.mechanics.utils.ServerUtils;
import dev.wuason.mechanics.utils.VersionDetector;
import dev.wuason.nms.utils.VersionNMS;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.nms.wrappers.NMSManager;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public final class Mechanics extends MechanicAddon {

    private static Mechanics core = null;
    private Manager manager;
    private NMSManager NMSManager;
    private LibraryResolver libraryResolver;

    public Mechanics(){
        super("Mechanics");
        if (core != null) {
            throw new IllegalStateException("Mechanics already initialized");
        }
        core = this;
    }

    @Override
    public void onLoad() {
        this.libraryResolver = LibraryResolver.builder(this)
                .addDependencies(
                        Dependencies.BOOSTED_YAML,
                        Dependencies.BEAN_SHELL,
                        Dependencies.COMMAND_API_MOJANG_MAPPED,
                        Dependencies.COMMAND_API,
                        Dependencies.NBT_API,
                        Dependencies.PROTECTION_LIB,
                        Dependencies.APACHE_COMMONS
                )
                .addRepositories(
                        Repos.MAVEN_CENTRAL,
                        Repos.INVESDWIN,
                        Repos.JITPACK,
                        Repos.CODEMC
                );

        this.libraryResolver.onResolveAndInjected(dependencyResolved -> {
            getLogger().info("Resolved " + dependencyResolved.getDependency().getArtifactId() + " in " + dependencyResolved.getResolveTime() + "ms" + (dependencyResolved.getRemapTime() > 0 ? " and remapped in " + dependencyResolved.getRemapTime() + "ms" : ""));
        });

        this.libraryResolver.build().resolve(true, "commandapi-bukkit.*");

        onVersion(VersionDetector.getServerVersion());

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onVersion(VersionDetector.ServerVersion version) {
        List<DependencyResolved> dependencyR = version.isLessThan(VersionDetector.ServerVersion.v1_20_5) ? libraryResolver.build().resolve(Dependencies.COMMAND_API) : libraryResolver.build().resolve(Dependencies.COMMAND_API_MOJANG_MAPPED);
    }

    @Override
    public void onEnable() {
        if(checkVersion() || checkPaper()) return;

        NMSManager = new NMSManager();
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gold>Starting mechanics plugin!");
        AdventureUtils.sendMessagePluginConsole("<gold>NMS: <aqua>" + VersionNMS.getServerVersion());
        AdventureUtils.sendMessagePluginConsole("<gold>Server version: <aqua>" + VersionDetector.getServerVersion().getVersionName());
        AdventureUtils.sendMessagePluginConsole("<gold>Mechanics loaded: <aqua>" + Arrays.stream(Bukkit.getPluginManager().getPlugins()).filter(plugin -> plugin instanceof MechanicAddon).map(plugin -> plugin.getName()).toList().toString().replace("[", "").replace("]", ""));
        //load managers
        CommandAPI.onEnable();
        ProtectionLib.init(this);
        this.manager = new Manager(this);
        this.manager.load();
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        getLogger().info("Mechanics plugin ok!");

    }

    @Override
    public void onDisable() {

        if (manager == null) return;
        manager.stop();
        CommandAPI.onDisable();

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
        if(VersionNMS.getServerVersion().equals(VersionNMS.ServerVersionNMS.UNSUPPORTED)){
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

    public boolean checkPaper(){
        if(!ServerUtils.isPaperServer()){
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("                 Unsupported server version minecraft ");
            core.getLogger().severe("               Now we only support paper and forks of paper");
            core.getLogger().severe("     Download paper from https://papermc.io/downloads/paper");
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
