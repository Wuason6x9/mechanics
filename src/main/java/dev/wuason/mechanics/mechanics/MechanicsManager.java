package dev.wuason.mechanics.mechanics;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MechanicsManager {

    private Mechanics core;

    private ArrayList<Mechanic> mechanics = new ArrayList<>();

    File dir;
    public MechanicsManager(Mechanics core) {
        this.core = core;

        loadMechanics();
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

                Mechanic mechanic = new Mechanic(plugin.getDescription().getName(), file, plugin.getDescription().getDescription().split("\\.")[2], plugin.getDescription().getAPIVersion(), plugin.getDescription().getVersion());
                mechanics.add(mechanic);

            }

        }

        for(Mechanic mechanic : mechanics){
            AdventureUtils.sendMessagePluginConsole("Mechanic loaded: " + mechanic.getAddonMechanicName());
        }

        core.getCommandManager().registerCommand();

    }

    public Mechanic getMechanic(String id){

        for(Mechanic mechanic : mechanics){
            if(mechanic.getAddonMechanicId().equals(id)) return mechanic;
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


}
