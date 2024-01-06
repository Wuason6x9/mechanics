package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.compatibilities.adapter.Adapter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Boolean isNumber(String number){
        try {
            int n = Integer.parseInt(number);
        }
        catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    public static Boolean isBool(String bool){
        try {
            boolean k = Boolean.parseBoolean(bool.toLowerCase());
        }
        catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    public static String serializeObject(Object ob) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(ob);
        objectOutputStream.flush();
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
    public static Object deserializeObject(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    public static String serializeObjectBukkit(Object ob) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1);
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
        bukkitObjectOutputStream.writeObject(ob);
        bukkitObjectOutputStream.flush();
        bukkitObjectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
    public static Object deserializeObjectBukkit(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    public static String replaceVariables(String source, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String regex = Pattern.quote(entry.getKey()); // Escapar caracteres especiales
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(source);
            source = matcher.replaceAll(entry.getValue());
        }
        return source;
    }

    public static List<String> replaceVariables(List<String> source, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String regex = Pattern.quote(entry.getKey()); // Escapar caracteres especiales
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            for (int i = 0; i < source.size(); i++) {
                String line = source.get(i);
                Matcher matcher = pattern.matcher(line);
                line = matcher.replaceAll(entry.getValue());
                source.set(i, line);
            }
        }
        return source;
    }

    public static String replaceVariablesInsensitive(String source, Map<String, String> replacements) {
        String lowerSource = source.toLowerCase();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String lowerKey = entry.getKey().toLowerCase();
            source = replaceAllInsensitive(source, lowerSource, lowerKey, entry.getValue());
        }
        return source;
    }

    public static List<String> replaceVariablesInsensitive(List<String> source, Map<String, String> replacements) {
        List<String> result = new ArrayList<>();
        for(String s : source){
            result.add(replaceVariablesInsensitive(s, replacements));
        }
        return result;
    }

    private static String replaceAllInsensitive(String source, String lowerSource, String lowerKey, String newValue) {
        StringBuilder result = new StringBuilder();
        int start = 0;
        int index = lowerSource.indexOf(lowerKey);

        while (index != -1) {
            // Añadir la parte del texto que no coincide
            result.append(source, start, index);
            // Añadir el nuevo valor
            result.append(newValue);

            start = index + lowerKey.length();
            index = lowerSource.indexOf(lowerKey, start);
        }

        // Añadir cualquier parte restante del texto fuente
        result.append(source.substring(start));

        return result.toString();
    }

    public static String replaceVariablesCaseSensitive(String source, Map<String, String> replacements) {
        // Para cada clave en el mapa de reemplazos
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            // Buscar y reemplazar todas las ocurrencias de la clave
            source = replaceAllCaseSensitive(source, entry.getKey(), entry.getValue());
        }
        return source;
    }
    //list
    public static List<String> replaceVariablesCaseSensitive(List<String> source, Map<String, String> replacements) {
        // Para cada clave en el mapa de reemplazos
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            // Buscar y reemplazar todas las ocurrencias de la clave
            source = replaceAllCaseSensitive(source, entry.getKey(), entry.getValue());
        }
        return source;
    }

    private static String replaceAllCaseSensitive(String source, String oldString, String newString) {
        int index = source.indexOf(oldString);
        while (index != -1) {
            source = source.substring(0, index) + newString + source.substring(index + oldString.length());
            index = source.indexOf(oldString, index + newString.length());
        }
        return source;
    }
    //list
    private static List<String> replaceAllCaseSensitive(List<String> source, String oldString, String newString) {
        for(String s : source){
            int index = s.indexOf(oldString);
            while (index != -1) {
                s = s.substring(0, index) + newString + s.substring(index + oldString.length());
                index = s.indexOf(oldString, index + newString.length());
            }
        }
        return source;
    }



    @Deprecated
    public static ItemStack createBasicItemStack(String customName, int customModelData, Material material){

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(AdventureUtils.deserializeLegacy(customName,null));
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }
    @Deprecated
    public static ItemStack createItemStackByAdapter(String item, String displayName, List<String> lore, int quantity){

        ItemStack itemStack = Adapter.getInstance().getItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setAmount(quantity);
        if(displayName != null){
            itemMeta.setDisplayName(AdventureUtils.deserializeLegacy(displayName,null));
        }
        if(lore != null){
            itemMeta.setLore(AdventureUtils.deserializeLegacyList(lore,null));
        }
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Mechanics.getInstance(),"random"), PersistentDataType.STRING, UUID.randomUUID().toString());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static String encodeBase64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    public static String decodeBase64(String data) {
        return new String(Base64.getDecoder().decode(data));
    }

}
