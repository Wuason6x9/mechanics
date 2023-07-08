package dev.wuason.mechanics.data.local;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.data.Data;
import dev.wuason.mechanics.utils.Utils;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalDataManager {

    private Mechanics core;
    private Plugin addon;
    private File dir;

    private HashMap<String, Data> dataMap = new HashMap<>();

    private static ArrayList<LocalDataManager> localDataManagerList = new ArrayList<>();

    public LocalDataManager(Mechanics core, Plugin addon) {
        localDataManagerList.add(this);
        this.addon = addon;
        this.core = core;
    }

    public void createDataFolder(){
        File file = new File(core.getDataFolder() + "/data/" + addon.getDescription().getName());
        file.mkdirs();
    }



    public static ArrayList<LocalDataManager> getLocalDataManagerList() {
        return localDataManagerList;
    }

    public Plugin getAddon() {
        return addon;
    }

    public File getDir() {
        return dir;
    }

    public Mechanics getCore() {
        return core;
    }

    public void saveData(Data data){

        if(dataMap.containsKey(data.getId())) dataMap.remove(data.getId());

        String dataStr = null;
        try {
            dataStr = Utils.serializeObjectBukkit(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveDataStr(dataStr, data.getDataType(), data.getId());
    }
    public void saveDataStr(String data, String dataType, String dataFileName){
        if(data == null) return;
        File file = new File(dir.getPath() + "/" + dataType + "/" + dataFileName + ".mechanic");
        file.getParentFile().mkdirs();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Writer writer;
        try {
            writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Data loadData(String dataType, String dataFileName){
        String dataStr = loadDataStr(dataType,dataFileName);
        Data data;
        try {
            data = (Data) Utils.deserializeObjectBukkit(dataStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        dataMap.put(data.getId(),data);
        return data;
    }

    public String loadDataStr(String dataType, String dataFileName) {
        File file = new File(dir.getPath() + "/" + dataType + "/" + dataFileName + ".mechanic");

        if(!file.exists()) {
            return null;
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

    public HashMap<String, Data> getDataMap() {
        return dataMap;
    }
}
