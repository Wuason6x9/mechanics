package dev.wuason.mechanics.mechanics;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class MechanicsManager {

    private Mechanics core;
    private boolean itemsAdderLoaded = false;

    private HashMap<String, Mechanic> mechanics = new HashMap<>();


    File dir;
    public MechanicsManager(Mechanics core) {
        this.core = core;
    }

    public void loadMechanics(){

        AdventureUtils.sendMessagePluginConsole("Loading mechanics!");

        mechanics = new HashMap<>();

        dir = core.getManager().getConfigManager().createDir("mechanics");
        PriorityQueue<MechanicLoad> mechanicsToLoad = new PriorityQueue<>();
        for(File file : dir.listFiles()){

            if(file.getName().contains(".jar")){

                Plugin plugin = null;

                try {
                    plugin = Bukkit.getPluginManager().loadPlugin(file);
                } catch (InvalidPluginException e) {
                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    continue;
                } catch (InvalidDescriptionException e) {
                    throw new RuntimeException(e);
                }
                if(!plugin.getDescription().getDescription().contains("wuason.mechanic")){

                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    continue;
                }
                if(plugin.getDescription().getDescription().split("\\.").length != 3){

                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    continue;

                }
                if(!hasMechanicFile(plugin)){
                    core.getLogger().severe("Failed to load mechanic check jar: " + file.getName());
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    continue;
                }
                MechanicInfo mechanicInfo = getMechanicInfo(plugin);

                Mechanic mechanic = new Mechanic(plugin.getDescription().getDescription().split("\\.")[2].toLowerCase(Locale.ENGLISH), file,plugin.getDescription().getName(), plugin.getDescription().getAPIVersion(), plugin.getDescription().getVersion(),plugin,mechanicInfo);

                mechanicsToLoad.add(new MechanicLoad(mechanic,mechanicInfo.getPriority()));


            }

        }

        while (!mechanicsToLoad.isEmpty()){
            MechanicLoad mechanicLoad = mechanicsToLoad.poll();
            Mechanic mechanic = mechanicLoad.getMechanic();
            core.getManager().getConfigManager().createConfigMechanic(mechanic);
            mechanics.put(mechanic.getAddonMechanicId(),mechanic);
            Bukkit.getPluginManager().enablePlugin(mechanic.getPlugin());
        }

        for(Mechanic mechanic : mechanics.values()){
            if(!mechanic.getPlugin().isEnabled()){
                AdventureUtils.sendMessagePluginConsole("Mechanic Error: " + mechanic.getAddonMechanicName());
                continue;
            }
            AdventureUtils.sendMessagePluginConsole("Mechanic loaded: " + mechanic.getAddonMechanicName());
        }

    }
    public boolean hasMechanicFile(Plugin plugin){
        return plugin.getResource("info.mechanic") != null;
    }

    public MechanicInfo getMechanicInfo(Plugin plugin){

        InputStream inputStream = plugin.getResource("info.mechanic");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String src = bufferedReader.readLine();
            String[] data = src.replace("{","").replace("}","").split(":");
            ArrayList<String> dependencies = new ArrayList<>();
            int priority = Integer.parseInt(data[2]);
            String dependenciesSrc = data[3].replace("[","").replace("]","").replace(" ","");
            if(dependenciesSrc.contains(",")){
                String[] dependenciesData = dependenciesSrc.split(",");
                for(String d : dependenciesData){
                    dependencies.add(d);
                }
            }
            else {
                if(dependenciesSrc.length()>2){
                    dependencies.add(dependenciesSrc);
                }
            }
            MechanicInfo mechanicInfo = new MechanicInfo(dependencies.toArray(String[]::new),data[0],data[1],priority);
            return mechanicInfo;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        Mechanic mechanic = mechanics.get(id);
        return mechanic == null ? null : mechanic;
    }

    public void reloadMechanics(){
        AdventureUtils.sendMessagePluginConsole(core,"<gold>Disabling the mechanics!");
        for(Mechanic m : mechanics.values()){
            if(m.getAddonMechanicName().equalsIgnoreCase("storagemechanic")) continue;
            Bukkit.getPluginManager().disablePlugin(m.getPlugin());
            Bukkit.getScheduler().cancelTasks(m.getPlugin());
            HandlerList.unregisterAll(m.getPlugin());
            try {
                unload(m.getPlugin());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.gc();
        }
        AdventureUtils.sendMessagePluginConsole(core,"<gold>Disabled mechanics!");
        loadMechanics();

    }

    public boolean waitDisableMechanics() {
        boolean a = true;
        for (Mechanic m : mechanics.values()) {
            if (m.getPlugin().isEnabled()) {
                return false;
            }
        }
        return a;
    }

    public boolean stopMechanic(Mechanic mechanic){
        Bukkit.getPluginManager().disablePlugin(mechanic.getPlugin());
        Bukkit.getScheduler().cancelTasks(mechanic.getPlugin());
        HandlerList.unregisterAll(mechanic.getPlugin());
        System.gc();
        try {
            unload(mechanic.getPlugin());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mechanics.remove(mechanic.getAddonMechanicId());
        return true;
    }
    public Mechanic startMechanic(String mName){
        File file = new File(dir.getPath() + "/" + mName.toLowerCase() + ".jar");
        if(file.exists()){
            try {
                Plugin plugin = null;
                try {
                    plugin = Bukkit.getPluginManager().loadPlugin(file);
                } catch (InvalidDescriptionException e) {
                    throw new RuntimeException(e);
                }
                MechanicInfo mechanicInfo = getMechanicInfo(plugin);
                Mechanic mechanic = new Mechanic(plugin.getDescription().getDescription().split("\\.")[2], file,plugin.getDescription().getName(), plugin.getDescription().getAPIVersion(), plugin.getDescription().getVersion(),plugin,mechanicInfo);
                core.getManager().getConfigManager().createConfigMechanic(mechanic);
                mechanics.put(mechanic.getAddonMechanicId(), mechanic);
                Bukkit.getPluginManager().enablePlugin(mechanic.getPlugin());
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

            for(Mechanic mechanic : mechanics.values()){
                if(mechanic.getAddonMechanicId().equals(id)) return mechanic;
            }

        }

        return null;
    }

    public boolean existMechanic(String id){
        return mechanics.containsKey(id);
    }

    public Mechanic[] getMechanics(String apiMcVersion){

        Mechanic[] m = (Mechanic[]) mechanics.values().stream().filter(mechanic -> mechanic.getApiMcVersion().equals(apiMcVersion)).toArray();

        return m;
    }

    public File getDir() {
        return dir;
    }

    public Collection<Mechanic> getMechanics(){
        return mechanics.values();
    }

    public void setItemsAdderLoaded(boolean itemsAdderLoaded) {
        this.itemsAdderLoaded = itemsAdderLoaded;
    }


    public void stop(){
        AdventureUtils.sendMessagePluginConsole("<red> Stopping Mechanics...");
        while(!mechanics.isEmpty()){
            stopMechanic(mechanics.values().stream().toList().get(0));
        }

    }

    public static void unload(Plugin plugin) throws Exception {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (!(pluginManager instanceof SimplePluginManager)) {
            throw new UnsupportedOperationException("Unsupported plugin manager type");
        }

        SimplePluginManager spm = (SimplePluginManager) pluginManager;
        Field pluginsField = spm.getClass().getDeclaredField("plugins");
        Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");

        pluginsField.setAccessible(true);
        lookupNamesField.setAccessible(true);

        List<Plugin> plugins = (List<Plugin>) pluginsField.get(spm);
        Map<String, Plugin> lookupNames = (Map<String, Plugin>) lookupNamesField.get(spm);

        plugins.remove(plugin);
        lookupNames.remove(plugin.getName());
    }
}