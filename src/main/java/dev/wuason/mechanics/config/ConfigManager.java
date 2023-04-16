package dev.wuason.mechanics.config;

import dev.wuason.mechanics.Mechanics;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigManager {

    private Mechanics core;
    private File fileMainConfig;
    private FileConfiguration configMain;

    public ConfigManager(Mechanics core) {
        this.core = core;
    }

    public void load(){

        fileMainConfig = new File(core.getDataFolder().getPath() + "/config.yml");

        if(!core.getDataFolder().exists()) core.getDataFolder().mkdirs();
        if(!fileMainConfig.exists()){
            InputStream inputStreamConfigMain = core.getResource("config.yml");
            try {
                core.getConfig().load(new InputStreamReader(inputStreamConfigMain));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }

            core.saveConfig();
        }

    }


    public File createFile(String path){

        File file = new File(core.getDataFolder().getPath() + "/" + path);

        if(!file.exists()){

            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return file;
    }

    public File createDir(String path){

        File file = new File(core.getDataFolder().getPath() + "/" + path);

        file.mkdirs();

        return file;

    }

}
