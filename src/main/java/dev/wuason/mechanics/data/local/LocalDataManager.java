package dev.wuason.mechanics.data.local;

import com.sk89q.worldedit.util.YAMLConfiguration;
import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.data.Data;
import dev.wuason.mechanics.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LocalDataManager {

    private Mechanics core;
    private Plugin addon;
    private File dir;

    private HashMap<String, Data> dataMap = new HashMap<>();

    private static ArrayList<LocalDataManager> localDataManagerList = new ArrayList<>();

    public LocalDataManager(Plugin addon) {
        localDataManagerList.add(this);
        this.addon = addon;
        this.core = Mechanics.getInstance();
    }

    public void createDataFolder(){
        dir = new File(core.getDataFolder() + "/data/" + addon.getDescription().getName());
        dir.mkdirs();
    }

    public Data[] getAllData(String dataType){

        File file = new File(dir.getPath() + "/" + dataType + "/");
        if(!file.exists()) return null;
        File[] files = Arrays.stream(file.listFiles()).filter(f -> f.getName().endsWith(".mechanic")).toArray(File[]::new);
        if(files.length == 0) return null;
        ArrayList<Data> datas = new ArrayList<>();

        for(File f : files){
            String id = f.getName().replace(".mechanic","");
            Data localData = getData(id,dataType);
            datas.add(localData);
        }
        return datas.toArray(Data[]::new);
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

    public Data getData(String dataFileName, String dataType){
        if(!existData(dataType,dataFileName)) return null;
        if(dataMap.containsKey(dataFileName)) return dataMap.get(dataFileName);
        return loadData(dataType,dataFileName);
    }
    public void removeData(String dataType, String dataFileName){
        if(dataMap.containsKey(dataFileName)) {
            dataMap.remove(dataFileName);
        }
        File file = new File(dir.getPath() + "/" + dataType + "/" + dataFileName + ".mechanic");
        if(file.exists()) file.delete();
    }
    public void saveData(Data data){

        if(dataMap.containsKey(data.getId())) dataMap.remove(data.getId());

        String dataStr = null;
        try {
            dataStr = Utils.serializeObjectBukkit(data);
        } catch (IOException e) {
        }
        if(dataMap.containsKey(data.getId())) dataMap.remove(data.getId());
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
            }
        }
        Writer writer;
        try {
            writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }
    public boolean existData(String dataType, String dataFileName){
        File file = new File(dir.getPath() + "/" + dataType + "/" + dataFileName + ".mechanic");
        if (this.dataMap.containsKey(dataFileName)) {
            return true;
        } else {
            return file.exists() && this.loadData(dataType, dataFileName) != null;
        }
    }

    public Data loadData(String dataType, String dataFileName){
        String dataStr = loadDataStr(dataType,dataFileName);
        Data data;
        try {
            data = (Data) Utils.deserializeObjectBukkit(dataStr);
            dataMap.put(data.getId(),data);
            return data;
        } catch (IOException | ClassNotFoundException e) {
        }
        return null;
    }

    public String loadDataStr(String dataType, String dataFileName) {
        File file = new File(dir.getPath() + "/" + dataType + "/" + dataFileName + ".mechanic");

        if(!file.exists()) {
            return null;
        }

        Reader reader;
        try {
            reader = new FileReader(file);
            try {
                char[] buffer = new char[(int) file.length()];
                reader.read(buffer);
                return new String(buffer);
            } catch(IOException e) {
            }
            finally {
                try {
                    reader.close();
                } catch(IOException e) {
                }
            }
        } catch(IOException e) {
        }
        return null;
    }

    public HashMap<String, Data> getDataMap() {
        return dataMap;
    }
}
