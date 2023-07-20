package dev.wuason.mechanics.mechanics;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MechanicsManager {

    private Mechanics core;
    private boolean itemsAdderLoaded = false;

    private ArrayList<Mechanic> mechanics = new ArrayList<>();


    File dir;
    public MechanicsManager(Mechanics core) {
        this.core = core;
    }

    public void loadMechanics(){

        AdventureUtils.sendMessagePluginConsole("Loading mechanics!");

        mechanics = new ArrayList<>();

        dir = core.getConfigManager().createDir("mechanics");

        for(File file : dir.listFiles()){

            if(file.getName().contains(".jar")){

                Plugin plugin = null;

                try {
                    plugin = core.getPluginLoader().loadPlugin(file);
                } catch (InvalidPluginException e) {
                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    continue;
                }

                if(!plugin.getDescription().getDescription().contains("wuason.mechanic")){

                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    core.getPluginLoader().disablePlugin(plugin);
                    continue;
                }

                if(plugin.getDescription().getDescription().split("\\.").length != 3){

                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    core.getPluginLoader().disablePlugin(plugin);
                    continue;

                }

                Mechanic mechanic = new Mechanic(plugin.getDescription().getDescription().split("\\.")[2], file,plugin.getDescription().getName(), plugin.getDescription().getAPIVersion(), plugin.getDescription().getVersion(),plugin);
                core.getConfigManager().createConfigMechanic(mechanic);
                mechanics.add(mechanic);
                core.getPluginLoader().enablePlugin(plugin);

            }

        }

        for(Mechanic mechanic : mechanics){
            if(!mechanic.getPlugin().isEnabled()){
                AdventureUtils.sendMessagePluginConsole("Mechanic Error: " + mechanic.getAddonMechanicName());
                continue;
            }
            AdventureUtils.sendMessagePluginConsole("Mechanic loaded: " + mechanic.getAddonMechanicName());
        }

    }

    public boolean waitDependencies(){
        boolean a = true;
        ArrayList<Plugin> plugins = new ArrayList<>();
        PluginManager pluginManager = Bukkit.getPluginManager();

        for(String p : core.getDescription().getSoftDepend()){
            Plugin pl = pluginManager.getPlugin(p);
            if(pl != null){
                plugins.add(pl);
            }
        }

        for(Plugin plugin : plugins){
            if(!plugin.isEnabled()){
                a = false;
            }
        }

        return a;
    }

    public Mechanic getMechanic(String id){

        for(Mechanic mechanic : mechanics){
            if(mechanic.getAddonMechanicId().equals(id)) return mechanic;
        }

        return null;
    }

    public void reloadMechanics(){
        AdventureUtils.sendMessagePluginConsole(core,"<gold>Disabling the mechanics!");
        for(Mechanic m : mechanics){
            if(m.getAddonMechanicName().equalsIgnoreCase("storagemechanic")) continue;
            core.getPluginLoader().disablePlugin(m.getPlugin());
            Bukkit.getScheduler().cancelTasks(m.getPlugin());
            HandlerList.unregisterAll(m.getPlugin());
            System.gc();
        }
        while (!waitDisableMechanics()){}
        AdventureUtils.sendMessagePluginConsole(core,"<gold>Disabled mechanics!");
        loadMechanics();

    }

    public boolean waitDisableMechanics() {
        boolean a = true;
        for (Mechanic m : mechanics) {
            if (m.getPlugin().isEnabled()) {
                return false;
            }
        }
        return a;
    }

    public boolean stopMechanic(Mechanic mechanic){
        core.getPluginLoader().disablePlugin(mechanic.getPlugin());
        Bukkit.getScheduler().cancelTasks(mechanic.getPlugin());
        HandlerList.unregisterAll(mechanic.getPlugin());
        System.gc();
        mechanics.remove(mechanic);
        return true;
    }
    public Mechanic startMechanic(String mName){
        File file = new File(dir.getPath() + "/" + mName.toLowerCase() + ".jar");
        if(file.exists()){
            try {
                Plugin plugin = core.getPluginLoader().loadPlugin(file);
                Mechanic mechanic = new Mechanic(plugin.getDescription().getDescription().split("\\.")[2], file,plugin.getDescription().getName(), plugin.getDescription().getAPIVersion(), plugin.getDescription().getVersion(),plugin);
                core.getConfigManager().createConfigMechanic(mechanic);
                mechanics.add(mechanic);
                core.getPluginLoader().enablePlugin(mechanic.getPlugin());
                return mechanic;
            } catch (InvalidPluginException e) {
                throw new RuntimeException(e);
            }
        }
        AdventureUtils.sendMessagePluginConsole("<gold>Mechanic " + mName + " doesn't exist!");
        return null;
    }
    public void reloadMechanic(Mechanic mechanic){
        if(mechanic.getAddonMechanicName().equalsIgnoreCase("storagemechanic")) return;
        stopMechanic(mechanic);
        while (mechanic.getPlugin().isEnabled()){}
        startMechanic(mechanic.getAddonMechanicName());
    }

    public Mechanic getMechanic(Plugin plugin){

        if(plugin.getDescription().getDescription().contains("wuason.mechanic")){

            String id = plugin.getDescription().getDescription().split("\\.")[2];

            for(Mechanic mechanic : mechanics){
                if(mechanic.getAddonMechanicId().equals(id)) return mechanic;
            }

        }

        return null;
    }

    public boolean existMechanic(String id){

        for(Mechanic mechanic : mechanics){
            if(mechanic.getAddonMechanicId().equals(id)) return true;
        }

        return false;
    }

    public Mechanic[] getMechanics(String apiMcVersion){

        Mechanic[] m = (Mechanic[]) mechanics.stream().filter(mechanic -> mechanic.getApiMcVersion().equals(apiMcVersion)).toArray();

        return m;
    }

    public File getDir() {
        return dir;
    }

    public ArrayList<Mechanic> getMechanics(){
        return mechanics;
    }

    public void setItemsAdderLoaded(boolean itemsAdderLoaded) {
        this.itemsAdderLoaded = itemsAdderLoaded;
    }


    public void stop(){
        AdventureUtils.sendMessagePluginConsole("<red> Stopping Mechanics...");
        while(!mechanics.isEmpty()){
            stopMechanic(mechanics.get(0));
        }

    }
}
