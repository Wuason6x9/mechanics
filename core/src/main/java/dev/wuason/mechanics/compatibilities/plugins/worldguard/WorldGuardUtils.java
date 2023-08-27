package dev.wuason.mechanics.compatibilities.plugins.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuardUtils {
    public static boolean isWorldGuardEnabled(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if(plugin == null) return false;
        return plugin.isEnabled();
    }
    public static RegionContainer getContainerManager() {

        return WorldGuard.getInstance().getPlatform().getRegionContainer();

    }
    public static RegionManager getRegionManager(World world){
        RegionManager regionManager = getContainerManager().get(BukkitAdapter.adapt(world));
        return regionManager;
    }

    public boolean hasPermission(Location loc, Player player){
        ApplicableRegionSet regions = getRegionManager(loc.getWorld()).getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        return regions.isMemberOfAll(WorldGuardPlugin.inst().wrapPlayer(player)) || regions.isOwnerOfAll(WorldGuardPlugin.inst().wrapPlayer(player));
    }
}
