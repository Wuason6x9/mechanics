package dev.wuason.mechanics.utils;

import dev.wuason.mechanics.compatibilities.AdapterManager;
import dev.wuason.mechanics.Mechanics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

        ItemStack itemStack = Mechanics.getInstance().getManager().getAdapterManager().getItemStack(item);
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
