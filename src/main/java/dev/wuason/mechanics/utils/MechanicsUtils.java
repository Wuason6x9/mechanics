package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.mechanics.Mechanic;
import org.bukkit.plugin.Plugin;

public class MechanicsUtils {

    public static boolean isMechanicLoaded(Plugin plugin){

        for(Mechanic mechanic : Mechanics.getInstance().getMechanicsManager().getMechanics()){

            if(mechanic.getAddonMechanicId().equals(plugin.getDescription().getDescription().split("\\.")[2])) return true;

        }

        return false;

    }

}
