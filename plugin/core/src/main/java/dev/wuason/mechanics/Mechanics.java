package dev.wuason.mechanics;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.wuason.libs.bstats.Metrics;
import dev.wuason.mechanics.library.LibraryResolver;
import dev.wuason.mechanics.library.dependencies.*;
import dev.wuason.mechanics.library.repositories.ChecksumResult;
import dev.wuason.mechanics.library.repositories.Repos;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.utils.*;
import dev.wuason.mechanics.nms.NMSManager;
import dev.wuason.mechanics.utils.version.MinecraftVersion;
import io.th0rgal.protectionlib.ProtectionLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Mechanics extends MechanicAddon {

    private static Mechanics core = null;
    private Manager manager;
    private NMSManager NMSManager;
    private LibraryResolver libraryResolver;
    public static final int SPIGOT_ID = 111934;
    private boolean disabled = false;

    public Mechanics() {
        super("Mechanics");
        if (core != null) {
            throw new IllegalStateException("Mechanics already initialized");
        }
        core = this;
    }

    @Override
    public void onLoad() {
        if (checkVersion() || checkPaper()) {
            disabled = true;
            return;
        }
        loadLibraries();
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        if (disabled || errorLoadingLibraries()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        checkUpdate();
        NMSManager = new NMSManager(this);
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gray>-----------------------------------------------------------");
        AdventureUtils.sendMessagePluginConsole("<gold>                        Mechanics");
        AdventureUtils.sendMessagePluginConsole("");
        AdventureUtils.sendMessagePluginConsole("<gold>Starting mechanics plugin!");
        AdventureUtils.sendMessagePluginConsole("<gold>NMS: <aqua>" + MinecraftVersion.getServerVersionSelected().getNMSVersion().getVersionName());
        AdventureUtils.sendMessagePluginConsole("<gold>Server version: <aqua>" + MinecraftVersion.getServerVersionSelected().getVersionName());
        AdventureUtils.sendMessagePluginConsole("<gold>Mechanics loaded: <aqua>" + Arrays.stream(Bukkit.getPluginManager().getPlugins()).filter(plugin -> plugin instanceof MechanicAddon && !(plugin instanceof Mechanics)).map(Plugin::getName).toList().toString().replace("[", "").replace("]", ""));
        //load managers
        metrics();
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

    private void loadLibraries() {

        this.libraryResolver = LibraryResolver.builder(this, LibraryResolver.LibraryLoaderType.PLUGIN_CLASSPATH)
                .addDependencies(
                        Dependencies.BOOSTED_YAML,
                        Dependencies.BEAN_SHELL,
                        Dependencies.NBT_API,
                        Dependencies.PROTECTION_LIB,
                        Dependencies.APACHE_COMMONS,
                        Dependencies.GSON,
                        Dependencies.GOOGLE_ERROR_PRONE_ANNOTATIONS,
                        Dependencies.MORE_PERSISTENT_DATA_TYPES,
                        Dependencies.CUSTOM_BLOCK_DATA,
                        Dependencies.RTAG,
                        Dependencies.RTAG_BLOCK,
                        Dependencies.RTAG_ENTITY,
                        Dependencies.RTAG_ITEM
                )
                .addRepositories(
                        Repos.INVESDWIN,
                        Repos.JITPACK,
                        Repos.CODEMC
                ).addDefaultRepositories();
        this.libraryResolver.onResolveAndInjected(dependencyResolved -> {
            getLogger().info("Resolved " + dependencyResolved.getDependency().getArtifactId() + " in " + dependencyResolved.getResolveTime() + "ms" + (dependencyResolved.getRemapTime() > 0 ? " and remapped in " + dependencyResolved.getRemapTime() + "ms" : "") + " checksum: " + (dependencyResolved.getChecksum() == null || dependencyResolved.getChecksum().isChecksumValid() ? "OK" : "ERROR") + " " + (dependencyResolved.getChecksum() != null ? dependencyResolved.getChecksum().getChecksumCorrects().stream().map(ChecksumResult::getType).toList().toString() : ""));
        });


        if (MinecraftVersion.getServerVersionSelected().isLessThan(MinecraftVersion.V1_20_5))
            this.libraryResolver.addDependencies(Dependencies.COMMAND_API);
        else this.libraryResolver.addDependencies(Dependencies.COMMAND_API_MOJANG_MAPPED);

        this.libraryResolver.build().resolve();

    }

    private void printMechanics() {
        AdventureUtils.sendMessagePluginConsole("<gold>" + AsciiUtils.convertToAscii(AsciiUtils.createTextImage("Mechanics", new Font("Arial", Font.BOLD, 25), Color.BLACK)));
    }

    private boolean checkVersion() {
        if (MinecraftVersion.getServerVersionSelected() == MinecraftVersion.UNSUPPORTED) {
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("                 Unsupported version minecraft ");
            core.getLogger().severe("                     Actual version: " + MinecraftVersion.getServerVersionBukkit());
            core.getLogger().severe("                     Last supported: " + MinecraftVersion.getLastSupportedVersion().getVersionName());
            core.getLogger().severe("                    Report this error to the developer in discord :>)");
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            return true;
        }
        return false;
    }

    private void metrics() {
        Metrics metrics = new Metrics(this, 23026);
        metrics.addCustomChart(new Metrics.DrilldownPie("addons", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Arrays.stream(Bukkit.getPluginManager().getPlugins()).filter(plugin -> plugin instanceof MechanicAddon && !(plugin instanceof Mechanics)).forEach(plugin -> {
                String version = plugin.getDescription().getVersion();
                String name = plugin.getDescription().getName();
                Map<String, Integer> entryPlugin = new HashMap<>();
                entryPlugin.put(version, 1);
                map.put(name, entryPlugin);
            });
            return map;
        }));
    }

    private boolean errorLoadingLibraries() {
        if (libraryResolver.build().allResolved()) return false;
        core.getLogger().severe("-----------------------------------------------------------");
        core.getLogger().severe("-----------------------------------------------------------");
        core.getLogger().severe("-                 Error loading libraries                 -");
        core.getLogger().severe("-            Dependencies not loaded correctly            -");
        core.getLogger().severe("-          check the console for more information         -");
        core.getLogger().severe("-                Dependencies not loaded:                 -");
        libraryResolver.build().getDependenciesNotResolved().forEach(dependency -> {
            core.getLogger().severe("- " + dependency.toString() + " -");
        });
        core.getLogger().severe("-----------------------------------------------------------");
        core.getLogger().severe("-----------------------------------------------------------");
        return true;
    }

    private boolean checkPaper() {
        if (!ServerUtils.isPaperServer()) {
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("                 Unsupported server version minecraft ");
            core.getLogger().severe("               Now we only support paper and forks of paper");
            core.getLogger().severe("     Download paper from https://papermc.io/downloads/paper");
            core.getLogger().severe("-----------------------------------------------------------");
            core.getLogger().severe("-----------------------------------------------------------");
            return true;
        }
        return false;
    }

    public boolean checkUpdate() {
        if (!SpigotUtils.isOnline()) return false;
        String version = SpigotUtils.getLastVersionByResourceId(SPIGOT_ID);
        if (getDescription().getVersion().equals(version)) return false;
        core.getLogger().severe("-----------------------------------------------------------");
        core.getLogger().severe("-----------------------------------------------------------");
        core.getLogger().severe("                     New version available                 ");
        core.getLogger().severe("               Actual version: " + getDescription().getVersion());
        core.getLogger().severe("               New version: " + version);
        core.getLogger().severe("  Download from https://www.spigotmc.org/resources/" + SPIGOT_ID);
        core.getLogger().severe("                 that if it is not updated                 ");
        core.getLogger().severe("    The plugin has been stopped because it is possible     ");
        core.getLogger().severe("       a serious error may occur in the mechanics data.    ");
        core.getLogger().severe("-----------------------------------------------------------");
        core.getLogger().severe("-----------------------------------------------------------");
        //Bukkit.getPluginManager().disablePlugin(core);
        return true;
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
