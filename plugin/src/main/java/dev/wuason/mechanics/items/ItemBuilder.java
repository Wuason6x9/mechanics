package dev.wuason.mechanics.items;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteItemNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import dev.wuason.adapter.Adapter;
import dev.wuason.mechanics.utils.AdventureUtils;
import dev.wuason.mechanics.utils.VersionDetector;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public static ItemBuilder copyOf(ItemStack item) {
        return new ItemBuilder(item.clone());
    }

    public static ItemBuilder from(ItemStack item) {
        return new ItemBuilder(item);
    }

    /**
     * A class that represents an ItemBuilderMechanic.
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Represents an item builder mechanic that is used to create ItemStack objects with specified Material and amount.
     */
    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    /**
     * This class represents an ItemBuilderMechanic, which is used to build ItemStack objects with specific properties.
     */
    public ItemBuilder(String adapterId, int amount) {
        this.item = Adapter.getItemStack(adapterId);
        if (this.item == null) {
            throw new IllegalArgumentException("Adapter with id " + adapterId + " is not valid");
        }
        this.item.setAmount(amount);
        this.meta = this.item.getItemMeta();
    }

    /**
     * Constructs an ItemBuilderMechanic object with the given NBT JSON.
     *
     * @param nbtJson the NBT JSON string representing the item
     */
    public ItemBuilder(String nbtJson) {
        this.item = NBT.itemStackFromNBT(NBT.parseNBT(nbtJson));
        this.meta = this.item.getItemMeta();
    }

    /**
     * Constructs a new ItemBuilderMechanic object with the given ItemStack.
     *
     * @param item the ItemStack to be used
     */
    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    /**
     * Constructs a new ItemBuilderMechanic object
     */

    protected ItemBuilder() {
        this(new ItemStack(Material.AIR));
    }

    /**
     * Constructor for creating an ItemBuilderMechanic object.
     *
     * @param item   The item to be built.
     * @param amount The amount of the item.
     */
    public ItemBuilder(ItemStack item, int amount) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.item.setAmount(amount);
    }

    public ItemBuilder adapter(String adapterId) {
        ItemStack temp = Adapter.getItemStack(adapterId);
        if (temp == null) {
            throw new IllegalArgumentException("Adapter with id " + adapterId + " is not valid");
        }
        this.item = temp;
        this.meta = this.item.getItemMeta();
        return this;
    }

    public ItemBuilder replaceItem(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        return this;
    }

    /**
     * Sets the name of the item.
     *
     * @param name the name to set for the item
     * @return the ItemBuilderMechanic object with the updated name
     */
    public ItemBuilder setName(String name) {
        if (name == null) return this;
        this.meta.displayName(Component.text(name));
        return this;
    }

    /**
     * Sets the name of the item.
     *
     * @param name the name to set for the item
     * @return the ItemBuilderMechanic object with the updated name
     */

    public ItemBuilder setName(Component name) {
        if (name == null) return this;
        this.meta.displayName(name);
        return this;
    }

    /**
     * Sets the name of the item with a mini message.
     *
     * @param name The name of the item, represented as a mini message. If null, the method returns the current instance of ItemBuilderMechanic.
     * @return The current instance of ItemBuilderMechanic.
     */
    public ItemBuilder setNameWithMiniMessage(String name) {
        if (name == null) return this;
        setName(AdventureUtils.deserialize(name));
        return this;
    }

    /**
     * Sets the skull owner for the item.
     *
     * @param player The player whose skull should be set as the owner.
     * @return This ItemBuilderMechanic instance.
     */
    public ItemBuilder setSkullOwner(Player player) {
        if (!this.item.getType().equals(Material.PLAYER_HEAD)) return this;
        if (meta instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(player);
        }
        return this;
    }

    public ItemBuilder setSkullOwner(String texture) {
        if (!this.item.getType().equals(Material.PLAYER_HEAD)) return this;

        if(VersionDetector.getServerVersion().isLessThan(VersionDetector.ServerVersion.v1_20_5)) {
            editNBT(nbt -> {
                ReadWriteNBT skullOwnerCompound = nbt.getOrCreateCompound("SkullOwner");
                skullOwnerCompound.setUUID("Id", UUID.randomUUID());
                skullOwnerCompound.getOrCreateCompound("Properties")
                        .getCompoundList("textures")
                        .addCompound()
                        .setString("Value", texture);
            });
        } else {
            editNBTComponents(nbt -> {
                ReadWriteNBT profileNbt = nbt.getOrCreateCompound("minecraft:profile");
                profileNbt.setUUID("id", UUID.randomUUID());
                ReadWriteNBT propertiesNbt = profileNbt.getCompoundList("properties").addCompound();
                propertiesNbt.setString("name", "textures");
                propertiesNbt.setString("value", texture);
            });
        }


        return this;
    }

    /**
     * Builds an ItemStack with a void name.
     * Sets the Name attribute of the ItemStack's display compound to an empty string.
     *
     * @return The ItemStack with a void name
     */
    public ItemStack buildWithVoidName() {
        setVoidName();
        return build();
    }

    /**
     * Sets the name of the item to an empty string.
     *
     * @return the ItemBuilderMechanic object with the updated name
     */

    public ItemBuilder setVoidName() {
        meta.displayName(Component.text(""));
        return this;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount the amount to set for the item
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    /**
     * Adds an enchantment to the item being built.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level of the enchantment.
     * @return The ItemBuilderMechanic instance.
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Removes the specified enchantment from the item's meta.
     *
     * @param enchantment The enchantment to remove from the item.
     * @return The ItemBuilderMechanic object with the updated meta.
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Adds a flag to the item being built.
     *
     * @param flag The flag to add.
     * @return This ItemBuilderMechanic instance.
     */
    public ItemBuilder addFlag(ItemFlag flag) {
        this.meta.addItemFlags(flag);
        return this;
    }

    /**
     * Removes the specified flag from the item.
     *
     * @param flag the flag to be removed from the item
     * @return the ItemBuilderMechanic instance
     */
    public ItemBuilder removeFlag(ItemFlag flag) {
        this.meta.removeItemFlags(flag);
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore The list of lore strings to set. If null, an empty list will be used.
     * @return The ItemBuilderMechanic instance with the updated lore.
     */
    public ItemBuilder setLore(List<String> lore) {
        if (lore == null) {
            lore = new ArrayList<>();
        }
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Sets the lore of the item with mini message.
     *
     * @param lore The list of lore strings to set. If null, an empty list will be used.
     * @return The ItemBuilderMechanic instance with the updated lore.
     */

    public ItemBuilder setLoreWithMiniMessage(List<String> lore) {
        if (lore == null) {
            lore = new ArrayList<>();
        }
        this.meta.lore(AdventureUtils.deserialize(lore));
        return this;
    }

    /**
     * Adds a new line to the lore of the item.
     *
     * @param line The line to be added to the lore
     * @return The ItemBuilderMechanic instance with the new lore line added
     */

    public ItemBuilder addLoreWithMiniMessage(String line) {
        List<Component> lore = new ArrayList<>();
        if (this.meta.lore() != null) {
            lore = new ArrayList<>(this.meta.lore());
        }
        lore.add(AdventureUtils.deserialize(line));
        this.meta.lore(lore);
        return this;
    }

    /**
     * Adds a new line to the lore of the item.
     *
     * @param line The line to be added to the lore
     * @return The ItemBuilderMechanic instance with the new lore line added
     */
    public ItemBuilder addLoreLine(String line) {
        List<String> lore = new ArrayList<>();
        if (this.meta.getLore() != null) {
            lore = new ArrayList<>(this.meta.getLore());
        }
        lore.add(line);
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Adds the given lines to the lore of the item.
     *
     * @param lines the lines to be added to the lore (non-null)
     * @return the ItemBuilderMechanic object with the updated lore
     */
    public ItemBuilder addLoreLines(List<String> lines) {
        List<String> lore = new ArrayList<>();
        if (this.meta.getLore() != null) {
            lore = new ArrayList<>(this.meta.getLore());
        }
        lore.addAll(lines);
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Removes a specific line from the lore of the item.
     *
     * @param line the line to be removed from the lore
     * @return the ItemBuilderMechanic object with the updated lore
     */
    public ItemBuilder removeLoreLine(String line) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        lore.remove(line);
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Removes the last line from the lore of the item.
     *
     * @return The ItemBuilderMechanic object with the updated lore.
     */
    public ItemBuilder removeLastLoreLine() {
        List<String> lore = new ArrayList<>();
        if (this.meta.getLore() != null) {
            lore = new ArrayList<>(this.meta.getLore());
        }
        if (lore.size() == 0) return this;
        lore.remove(lore.size() - 1);
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Removes the first line of lore from the ItemBuilderMechanic.
     *
     * @return The ItemBuilderMechanic instance with the first line of lore removed.
     */
    public ItemBuilder removeFirstLoreLine() {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        lore.remove(0);
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Removes a line from the lore of the item.
     *
     * @param index the index of the line to remove
     * @return the ItemBuilderMechanic instance with the updated lore
     */
    public ItemBuilder removeLoreLine(int index) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        if (index >= 0 && index < lore.size()) {
            lore.remove(index);
            this.meta.setLore(lore);
        }
        return this;
    }

    /**
     * Sets the unbreakable state of the item.
     *
     * @param unbreakable true if the item should be unbreakable, false otherwise.
     * @return the ItemBuilderMechanic instance with the updated unbreakable state.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Sets the color of the item.
     *
     * @param color the color to set for the item
     * @return the ItemBuilderMechanic object with the updated color
     */
    public ItemBuilder setColor(Color color) {
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
        }
        return this;
    }

    /**
     * Sets a potion effect on the item being built.
     *
     * @param effectType the type of potion effect to apply
     * @param duration   the duration of the potion effect in ticks
     * @param amplifier  the amplifier of the potion effect
     * @return the ItemBuilderMechanic instance with the added potion effect
     */
    public ItemBuilder setPotionEffect(PotionEffectType effectType, int duration, int amplifier) {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
        }
        return this;
    }

    /**
     * Sets the color of the potion for the item being built.
     *
     * @param color The color to set for the potion.
     * @return The ItemBuilderMechanic instance.
     */
    public ItemBuilder setPotionColor(Color color) {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).setColor(color);
        }
        return this;
    }

    /**
     * Clears any custom potion effects from the item's meta if it is a PotionMeta.
     *
     * @return The updated ItemBuilderMechanic instance.
     */
    public ItemBuilder clearPotionEffects() {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).clearCustomEffects();
        }
        return this;
    }

    /**
     * Sets the material of the item.
     *
     * @param material the material to set for the item
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setMaterial(Material material) {
        this.item.setType(material);
        return this;
    }

    /**
     * Sets the durability of the item being built.
     *
     * @param durability the durability to set for the item
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setDurability(int durability) {
        ((Damageable) this.meta).setDamage(durability);
        return this;
    }

    /**
     * Sets the glowing attribute of the item being built.
     *
     * @param glowing true if the item should have the glowing effect, false otherwise
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setGlowing(boolean glowing) {
        if (glowing) {
            this.meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
            this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.meta.removeEnchant(Enchantment.AQUA_AFFINITY);
            this.meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    /**
     * Sets the visibility of the item.
     *
     * @param invisible true to make the item invisible, false otherwise
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setInvisible(boolean invisible) {
        if (invisible) {
            this.meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            this.meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        return this;
    }

    /**
     * Sets the custom model data of the item being built.
     *
     * @param data the custom model data value to set
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setCustomModelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    /**
     * Sets the color of a leather armor item.
     *
     * @param color the color to set for the leather armor
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setLeatherArmorColor(Color color) {
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
        }
        return this;
    }

    /**
     * Clears the meta data of the item being built.
     *
     * @return The updated ItemBuilderMechanic object.
     */
    public ItemBuilder clearMeta() {
        this.meta = this.item.getItemMeta();
        return this;
    }

    /**
     * Removes the lore of the item.
     *
     * @return The updated {@link ItemBuilder} instance with the lore removed.
     */
    public ItemBuilder removeLore() {
        this.meta.setLore(new ArrayList<>());
        return this;
    }

    /**
     * Sets the damage of the item.
     *
     * @param damage the damage value to set for the item
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setDamage(int damage) {
        this.item.setDurability((short) damage);
        return this;
    }

    /**
     * Replaces a line of lore in the ItemStack being built at the specified index.
     *
     * @param index   The index of the line to be replaced.
     * @param newLine The new lore line to replace the existing line.
     * @return The updated ItemBuilderMechanic instance.
     */
    public ItemBuilder replaceLoreLine(int index, String newLine) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        if (index >= 0 && index < lore.size()) {
            lore.set(index, newLine);
            this.meta.setLore(lore);
        }
        return this;
    }

    /**
     * Sets the lore line at the given index.
     *
     * @param index the index at which the lore line should be set
     * @param line  the lore line to set
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder setLoreLine(int index, String line) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        lore.set(index, line);
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Sets the firework effect for the item.
     *
     * @param effect the firework effect to set
     * @return the updated ItemBuilderMechanic
     */
    public ItemBuilder setFireworkEffect(FireworkEffect effect) {
        if (meta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) meta).setEffect(effect);
        }
        return this;
    }

    /**
     * Adds a page to the book meta of the {@link ItemBuilder}.
     *
     * @param page the page to be added to the book
     * @return the {@link ItemBuilder} object
     */
    public ItemBuilder addPage(String page) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.addPage(page);
            this.meta = bookMeta;
        }
        return this;
    }

    /**
     * Sets the author of the item.
     *
     * @param author the author of the item
     * @return the ItemBuilderMechanic object with the author set
     */
    public ItemBuilder setAuthor(String author) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setAuthor(author);
            this.meta = bookMeta;
        }
        return this;
    }

    /**
     * Sets the title of the item.
     *
     * @param title the title to be set
     * @return the updated ItemBuilderMechanic object
     */
    public ItemBuilder setTitle(String title) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setTitle(title);
            this.meta = bookMeta;
        }
        return this;
    }

    /**
     * Sets the generation for the book metadata.
     *
     * @param generation the generation to be set for the book metadata
     * @return the ItemBuilderMechanic instance
     */
    public ItemBuilder setGeneration(BookMeta.Generation generation) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setGeneration(generation);
            this.meta = bookMeta;
        }
        return this;
    }

    /**
     * Sets the power of a firework rocket item.
     *
     * @param power the power level to set
     * @return the item builder mechanic instance
     */
    public ItemBuilder setPower(int power) {
        if (item.getType() == Material.FIREWORK_ROCKET) {
            FireworkMeta fireworkMeta = (FireworkMeta) this.meta;
            fireworkMeta.setPower(power);
            this.meta = fireworkMeta;
        }
        return this;
    }

    /**
     * Adds persistent data to the item's metadata.
     *
     * @param key   The key for the persistent data.
     * @param value The value for the persistent data.
     * @return The ItemBuilderMechanic instance with the persistent data added.
     */
    public ItemBuilder addPersistentData(NamespacedKey key, String value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        return this;
    }

    /**
     * Adds persistent data to the item's metadata.
     *
     * @param key   The key used to store the data in the persistent data container.
     * @param value The value of the data to be stored.
     * @return An instance of ItemBuilderMechanic with the persistent data added.
     */
    public ItemBuilder addPersistentData(NamespacedKey key, int value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        return this;
    }

    /**
     * Adds persistent data to the item's metadata.
     *
     * @param key   the namespaced key for the persistent data
     * @param value the double value to be stored
     * @return the ItemBuilderMechanic instance with the persistent data added
     */
    public ItemBuilder addPersistentData(NamespacedKey key, double value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        return this;
    }

    /**
     * Adds persistent data to the item using the specified key and value.
     *
     * @param key   the namespaced key for the persistent data
     * @param value the float value to be set for the persistent data
     * @return the updated ItemBuilderMechanic object for method chaining
     */
    public ItemBuilder addPersistentData(NamespacedKey key, float value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
        return this;
    }

    /**
     * Adds persistent data to the item's metadata container.
     *
     * @param key   the key of the persistent data, represented by a {@code NamespacedKey}
     * @param value the value of the persistent data, represented by a {@code long}
     * @return an instance of {@code ItemBuilderMechanic} to enable method chaining
     */
    public ItemBuilder addPersistentData(NamespacedKey key, long value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, value);
        return this;
    }

    /**
     * Adds persistent data to the item's metadata container.
     *
     * @param key   The namespaced key for the persistent data.
     * @param value The byte array value to be added.
     * @return An instance of `ItemBuilderMechanic` with the persistent data added.
     */
    public ItemBuilder addPersistentData(NamespacedKey key, byte[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
        return this;
    }

    /**
     * Adds persistent data to the item's metadata container.
     *
     * @param key   the namespaced key of the data
     * @param value the integer array value to be stored
     * @return the ItemBuilderMechanic object with the added persistent data
     */
    public ItemBuilder addPersistentData(NamespacedKey key, int[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER_ARRAY, value);
        return this;
    }

    /**
     * Adds persistent data to the item's metadata.
     *
     * @param key   The namespaced key of the persistent data.
     * @param value The long array value to be added.
     * @return The ItemBuilderMechanic object with the persistent data added.
     */
    public ItemBuilder addPersistentData(NamespacedKey key, long[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.LONG_ARRAY, value);
        return this;
    }

    /**
     * Removes the persistent data with the given key from the item's metadata.
     *
     * @param key the namespaced key of the persistent data to remove
     * @return the current ItemBuilderMechanic instance
     */
    public ItemBuilder removePersistentData(NamespacedKey key) {
        this.meta.getPersistentDataContainer().remove(key);
        return this;
    }

    /**
     * Checks if persistent data with the specified key exists in the metadata container.
     *
     * @param key The key of the persistent data.
     * @return {@code true} if the persistent data exists, {@code false} otherwise.
     */
    public boolean hasPersistentData(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    /**
     * Apply changes to the ItemMeta using the provided Consumer.
     *
     * @param consumer the function that accepts an ItemMeta and applies changes to it
     * @return the ItemBuilderMechanic object with the updated ItemMeta
     */
    public ItemBuilder meta(Consumer<ItemMeta> consumer) {
        consumer.accept(this.meta);
        return this;
    }

    /**
     * Edits the ItemStack using the provided Consumer.
     *
     * @param consumer the Consumer used to modify the ItemStack
     * @return the updated ItemBuilderMechanic instance
     */
    public ItemBuilder edit(Consumer<ItemStack> consumer) {
        consumer.accept(this.item);
        this.meta = this.item.getItemMeta();
        return this;
    }

    /**
     * Edits the NBT data of the ItemStack using the provided Consumer.
     *
     * @param consumer the Consumer used to modify the NBT data
     * @return the updated ItemBuilderMechanic instance
     */

    public ItemBuilder editNBT(Consumer<ReadWriteItemNBT> consumer) {
        if (this.meta != null) this.item.setItemMeta(this.meta);
        NBT.modify(this.item, consumer);
        this.meta = this.item.getItemMeta();
        return this;
    }

    /**
     * Edits the NBT data of the ItemStack using the provided Consumer. 1.20.5+ https://github.com/tr7zw/Item-NBT-API/wiki/Using-the-NBT-API#changing-vanilla-item-nbt-on-1205
     *
     * @param consumer the Consumer used to modify the NBT data
     * @return the updated ItemBuilderMechanic instance
     */

    public ItemBuilder editNBTComponents(Consumer<ReadWriteNBT> consumer) {
        if (this.meta != null) this.item.setItemMeta(this.meta);
        NBT.modifyComponents(this.item, consumer);
        this.meta = this.item.getItemMeta();
        return this;
    }

    /**
     * Retrieves the value associated with the given key from the persistent data container as a string.
     *
     * @param key the namespaced key used to retrieve the value from the persistent data container
     * @return the string value associated with the provided key in the persistent data container
     */
    public String getPersistentDataString(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    /**
     * Retrieves the integer value associated with the given NamespacedKey from the persistent data of the object.
     *
     * @param key The NamespacedKey associated with the persistent data value.
     * @return The integer value associated with the given key, or null if no value is found.
     */
    public int getPersistentDataInteger(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
    }

    /**
     * Retrieves a double value from the persistent data container associated with the specified key.
     * The value is retrieved using the DOUBLE persistent data type.
     *
     * @param key the namespaced key used to access the double value in the persistent data container
     * @return the double value associated with the specified key, or null if the key does not exist
     */
    public double getPersistentDataDouble(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
    }

    /**
     * Retrieves a float value from the persistent data container using the given key.
     *
     * @param key The key used to retrieve the float value from the persistent data container.
     * @return The float value associated with the given key, or null if the key is not found or the value is not of type float.
     */
    public float getPersistentDataFloat(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.FLOAT);
    }

    /**
     * Retrieves a long value associated with the specified namespaced key from the persistent data container.
     *
     * @param key the namespaced key used to lookup the value
     * @return the long value associated with the specified key, or null if no value is found
     */
    public long getPersistentDataLong(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.LONG);
    }

    /**
     * Retrieves a byte array stored in the persistent data container of the given key.
     *
     * @param key the namespaced key used to identify the byte array in the persistent data container
     * @return the byte array value associated with the given key, or null if the key does not exist
     */
    public byte[] getPersistentDataByteArray(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.BYTE_ARRAY);
    }

    /**
     * Retrieves an integer array from the persistent data container using the specified key.
     *
     * @param key The key used to retrieve the integer array from the persistent data container.
     * @return An integer array retrieved from the persistent data container with the specified key.
     */
    public int[] getPersistentDataIntegerArray(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER_ARRAY);
    }

    /**
     * Builds the ItemStack using the provided item metadata and returns it.
     *
     * @return The built ItemStack.
     */
    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }

}