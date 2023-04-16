package dev.wuason.mechanics.mechanics;

import dev.wuason.mechanics.Mechanics;
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

                Plugin mechanic = null;

                try {
                    mechanic = core.getPluginLoader().loadPlugin(file);
                } catch (InvalidPluginException e) {
                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    continue;
                }

                if(mechanic.getDescription().getDescription().contains("wuason.mechanic")){

                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    continue;
                }

            }

        }

    }


}
