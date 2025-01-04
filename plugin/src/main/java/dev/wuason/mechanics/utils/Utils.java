package dev.wuason.mechanics.utils;

import dev.wuason.adapter.Adapter;
import dev.wuason.mechanics.Mechanics;
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

    /**
     * Checks if a given string represents a valid number.
     *
     * @param number the string to check
     * @return true if the string represents a number, false otherwise
     */
    public static Boolean isNumber(String number){
        try {
            Integer.parseInt(number);
        }
        catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    /**
     * Checks if the given string represents a boolean value.
     *
     * @param bool the string to check
     * @return true if the string represents a boolean value, false otherwise
     */
    public static Boolean isBool(String bool){
        try {
            Boolean.parseBoolean(bool.toLowerCase());
        }
        catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    /**
     * Serializes an object into a Base64-encoded string representation.
     *
     * @param ob The object to be serialized.
     * @return The Base64-encoded string representation of the serialized object.
     * @throws IOException If an I/O error occurs while serializing the object.
     */
    public static String serializeObject(Object ob) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(ob);
        objectOutputStream.flush();
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
    /**
     * Deserialize an object from a Base64 encoded string.
     *
     * @param data Base64 encoded string representation of the object to be deserialized
     * @return The deserialized object
     * @throws IOException            If an I/O error occurs while deserializing the object
     * @throws ClassNotFoundException If the class of the serialized object cannot be found during deserialization
     */
    public static Object deserializeObject(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    /**
     * Serializes an object using BukkitObjectOutputStream and returns the serialized object as a Base64-encoded string.
     *
     * @param ob the object to serialize
     * @return the serialized object as a Base64-encoded string
     * @throws IOException if an I/O error occurs while serializing the object
     */
    public static String serializeObjectBukkit(Object ob) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1);
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
        bukkitObjectOutputStream.writeObject(ob);
        bukkitObjectOutputStream.flush();
        bukkitObjectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
    /**
     * This method deserializes an object that was serialized using the BukkitObjectInputStream class.
     *
     * @param data The serialized data as a Base64 encoded string.
     * @return The deserialized object.
     * @throws IOException              If an I/O error occurs.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public static Object deserializeObjectBukkit(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    /**
     * Replaces variables in the given source string using the provided replacements map.
     *
     * @param source       the source string in which variables will be replaced
     * @param replacements a map of variable names and their corresponding values
     * @return the modified source string with variables replaced by their values
     */
    public static String replaceVariables(String source, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String regex = Pattern.quote(entry.getKey());
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(source);
            source = matcher.replaceAll(entry.getValue());
        }
        return source;
    }

    /**
     * Replace variables in a list of strings.
     *
     * @param source       The source list of strings.
     * @param replacements The map containing the variable replacements. The keys are the variables to be replaced, and the values are the replacements.
     * @return The modified list of strings with variables replaced.
     */
    public static List<String> replaceVariables(List<String> source, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String regex = Pattern.quote(entry.getKey());
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

    /**
     * Replaces variables in the source string with corresponding values in a case-insensitive manner.
     *
     * @param source       The source string to replace variables in.
     * @param replacements A map containing variable replacements. The keys represent the variables to replace,
     *                     and the values represent the corresponding replacement values.
     * @return The modified string with variables replaced.
     */
    public static String replaceVariablesInsensitive(String source, Map<String, String> replacements) {
        String lowerSource = source.toLowerCase();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String lowerKey = entry.getKey().toLowerCase();
            source = replaceAllInsensitive(source, lowerSource, lowerKey, entry.getValue());
        }
        return source;
    }

    /**
     * Replaces variables in a list of strings case-insensitively using a map of replacements.
     *
     * @param source The source list of strings.
     * @param replacements The map of replacements where the keys represent the variables to be replaced and the values represent the replacement values.
     * @return The new list of strings with variables replaced case-insensitively.
     */
    public static List<String> replaceVariablesInsensitive(List<String> source, Map<String, String> replacements) {
        List<String> result = new ArrayList<>();
        for(String s : source){
            result.add(replaceVariablesInsensitive(s, replacements));
        }
        return result;
    }

    /**
     * Replaces all occurrences of a substring in a string, without considering case sensitivity.
     *
     * @param source     The original string
     * @param lowerSource The lowercased version of the original string
     * @param lowerKey    The lowercased substring to be replaced
     * @param newValue    The new value to replace the substring with
     * @return The modified string with all occurrences of the substring replaced
     */
    private static String replaceAllInsensitive(String source, String lowerSource, String lowerKey, String newValue) {
        StringBuilder result = new StringBuilder();
        int start = 0;
        int index = lowerSource.indexOf(lowerKey);

        while (index != -1) {
            result.append(source, start, index);
            result.append(newValue);

            start = index + lowerKey.length();
            index = lowerSource.indexOf(lowerKey, start);
        }
        result.append(source.substring(start));

        return result.toString();
    }

    /**
     * Replaces variables in a given source string with their corresponding values from a replacements map.
     * The replacement process is case-sensitive.
     *
     * @param source The string to replace variables in.
     * @param replacements The map containing key-value pairs of variables and their corresponding values.
     * @return The source string with variables replaced by their values.
     */
    public static String replaceVariablesCaseSensitive(String source, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            source = replaceAllCaseSensitive(source, entry.getKey(), entry.getValue());
        }
        return source;
    }
    /**
     * Replaces variables in a list of strings with their corresponding values in a case-sensitive manner.
     *
     * @param source The list of strings to perform the replacements on.
     * @param replacements A map containing the variable-value pairs for replacement.
     * @return The modified list of strings with variables replaced by their corresponding values.
     */
    //list
    public static List<String> replaceVariablesCaseSensitive(List<String> source, Map<String, String> replacements) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            source = replaceAllCaseSensitive(source, entry.getKey(), entry.getValue());
        }
        return source;
    }

    /**
     * Replaces all occurrences of a case-sensitive string with a new string in a source string.
     *
     * @param source    the source string in which to perform the replacements
     * @param oldString the string to be replaced
     * @param newString the replacement string
     * @return the resulting string after all replacements have been performed
     */
    private static String replaceAllCaseSensitive(String source, String oldString, String newString) {
        int index = source.indexOf(oldString);
        while (index != -1) {
            source = source.substring(0, index) + newString + source.substring(index + oldString.length());
            index = source.indexOf(oldString, index + newString.length());
        }
        return source;
    }

    /**
     * Replaces all occurrences of a string in a list of strings, while being case-sensitive.
     * The method iterates through each string in the list and replaces all occurrences of the old string with the new string.
     *
     * @param source     the source list of strings
     * @param oldString  the string to be replaced
     * @param newString  the string to replace the old string with
     * @return the updated list of strings with the replacements made
     */
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



    /**
     * Creates a basic ItemStack with a custom name, custom model data, and material.
     *
     * @deprecated This method is deprecated and may be removed in a future version.
     *             It is recommended to use alternative methods for creating ItemStacks.
     *
     * @param customName The custom display name for the ItemStack
     * @param customModelData The custom model data for the ItemStack
     * @param material The material of the ItemStack
     *
     * @return The created ItemStack with the custom name, custom model data, and material
     */
    @Deprecated
    public static ItemStack createBasicItemStack(String customName, int customModelData, Material material){

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(AdventureUtils.deserializeLegacy(customName,null));
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }
    /**
     * Creates an ItemStack based on the given item ID, display name, lore, and quantity.
     * The item ID should be in the format "type:item", where type represents the implementation type.
     *
     * @param item      The ID of the item in the format "type:item".
     * @param displayName The display name of the item. Can be null.
     * @param lore        The lore of the item. Can be null.
     * @param quantity    The quantity of the item.
     * @return The created ItemStack.
     */
    @Deprecated
    public static ItemStack createItemStackByAdapter(String item, String displayName, List<String> lore, int quantity){

        ItemStack itemStack = Adapter.getItemStack(item);
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

    /**
     * Encodes a String to Base64 format.
     *
     * @param data the data to be encoded
     * @return the encoded data as a String
     */
    public static String encodeBase64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    /**
     * Decodes a Base64 encoded string and returns the decoded string.
     *
     * @param data the Base64 encoded string to decode
     * @return the decoded string
     */
    public static String decodeBase64(String data) {
        return new String(Base64.getDecoder().decode(data));
    }

    /**
     * Saves a resource from an input stream to a specified destination.
     *
     * @param in           The input stream from which to read the resource.
     * @param destination  The destination path where the resource will be saved.
     */
    public static void saveResource(InputStream in, String destination) {

        try {
            OutputStream out = new FileOutputStream(new File(destination));
            byte[] buffer = new byte[1024];
            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Integer> configFill(List<String> arrayList){

        ArrayList<Integer> arrayListNumbers = new ArrayList<>();

        for(String number : arrayList){

            if(!number.contains("-")) {
                if (Utils.isNumber(number)) {
                    arrayListNumbers.add(Integer.parseInt(number));
                }
                continue;
            }

            String numbers[] = number.split("-");

            if(numbers.length>0){
                for(String n : numbers){
                    if(!Utils.isNumber(n)) continue;
                }
                for(int i=Integer.parseInt(numbers[0]);i<Integer.parseInt(numbers[1])+1;i++){
                    arrayListNumbers.add(i);
                }
            }
        }

        return arrayListNumbers;

    }

}
