package dev.wuason.mechanics.mechanics;

import dev.wuason.mechanics.Mechanics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

public class MechanicsManager {

    private Mechanics core;

    private ArrayList<Mechanic> mechanics;
    public MechanicsManager(Mechanics core) {
        this.core = core;

        loadMechanics();
    }

    public void loadMechanics(){

        mechanics = new ArrayList<>();

        File dir = core.getConfigManager().createDir("mechanics");

        for(File file : dir.listFiles()){

            if(file.getName().contains(".jar")){

                Plugin plugin = null;

                try {
                    plugin = core.getPluginLoader().loadPlugin(file);
                } catch (InvalidPluginException e) {
                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    continue;
                }

                if(plugin.getDescription().getDescription().contains("wuason.mechanic")){

                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    continue;
                }
                Mechanic mechanic = new Mechanic(plugin.getDescription().getName(), file, plugin.getDescription().getDescription().split(".")[2], plugin.getDescription().getAPIVersion(), plugin.getDescription().getVersion());
                mechanics.add(mechanic);

            }

        }

        for(Mechanic mechanic : mechanics){
            Bukkit.getConsoleSender().sendMessage("MECHANIC LOADED: " + mechanic.getAddonMechanicName());
        }

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

    public Mechanic[] getMechanics(){
        return (Mechanic[]) mechanics.toArray();
    }


}
