package dev.wuason.mechanics.data.local;

import dev.wuason.mechanics.Mechanics;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class LocalDataManager {

    private Mechanics core;

    public LocalDataManager(Mechanics core) {
        this.core = core;
    }

    public void saveLocalData(Plugin plugin, String data, String path){

        File file = new File(core.getDataFolder() + "/data/" + plugin.getDescription().getDescription().split("\\.")[2] + "/" + path);

        if(!file.exists()){

            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Writer writer;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            writer.close();
        } catch (IOException e) {
        }

    }

    public String loadLocalData(Plugin plugin, String path) {
        File file = new File(plugin.getDataFolder() + "/data/" + plugin.getDescription().getDescription().split("\\.")[2] + "/" + path);

        if(!file.exists()) {
            throw new RuntimeException("El archivo no existe");
        }

        Reader reader;
        try {
            reader = new FileReader(file);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        try {
            char[] buffer = new char[(int) file.length()];
            reader.read(buffer);
            return new String(buffer);
        } catch(IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch(IOException e) {
            }
        }
    }


}
