package dev.wuason.mechanics.items;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import dev.wuason.mechanics.Mechanics;
import io.th0rgal.oraxen.shaded.triumphteam.gui.components.util.ItemNbt;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilderMechanic {

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilderMechanic(Material material) {
        this(material, 1);
    }

    public ItemBuilderMechanic(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }
    public ItemBuilderMechanic(String adapterId, int amount){
        this.item = Mechanics.getInstance().getManager().getAdapterManager().getItemStack(adapterId);
        this.item.setAmount(amount);
        this.meta = this.item.getItemMeta();
    }
    public ItemBuilderMechanic(String nbtJson){
        this.item = NBT.itemStackFromNBT(NBT.parseNBT(nbtJson));
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilderMechanic(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilderMechanic(ItemStack item, int amount) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.item.setAmount(amount);
    }

    public ItemBuilderMechanic setName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilderMechanic setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilderMechanic addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilderMechanic removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilderMechanic addFlag(ItemFlag flag) {
        this.meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilderMechanic removeFlag(ItemFlag flag) {
        this.meta.removeItemFlags(flag);
        return this;
    }

    public ItemBuilderMechanic setLore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilderMechanic addLoreLine(String line) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        lore.add(line);
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilderMechanic removeLoreLine(int index) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        if (index >= 0 && index < lore.size()) {
            lore.remove(index);
            this.meta.setLore(lore);
        }
        return this;
    }

    public ItemBuilderMechanic setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilderMechanic setColor(Color color) {
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
        }
        return this;
    }

    public ItemBuilderMechanic setPotionEffect(PotionEffectType effectType, int duration, int amplifier) {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
        }
        return this;
    }

    public ItemBuilderMechanic setPotionColor(Color color) {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).setColor(color);
        }
        return this;
    }

    public ItemBuilderMechanic clearPotionEffects() {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).clearCustomEffects();
        }
        return this;
    }

    public ItemBuilderMechanic setMaterial(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilderMechanic setDurability(short durability) {
        ((Damageable) this.meta).setDamage(durability);
        return this;
    }

    public ItemBuilderMechanic setGlowing(boolean glowing) {
        if (glowing) {
            this.meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.meta.removeEnchant(Enchantment.ARROW_INFINITE);
            this.meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilderMechanic setInvisible(boolean invisible) {
        if (invisible) {
            this.meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            this.meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        return this;
    }

    public ItemBuilderMechanic setCustomModelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilderMechanic setLocalizedName(String name) {
        this.meta.setLocalizedName(name);
        return this;
    }

    public ItemBuilderMechanic setLeatherArmorColor(Color color) {
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
        }
        return this;
    }

    public ItemBuilderMechanic clearMeta() {
        this.meta = this.item.getItemMeta();
        return this;
    }

    public ItemBuilderMechanic removeLore() {
        this.meta.setLore(new ArrayList<>());
        return this;
    }

    public ItemBuilderMechanic setDamage(int damage) {
        this.item.setDurability((short) damage);
        return this;
    }

    public ItemBuilderMechanic replaceLoreLine(int index, String newLine) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        if (index >= 0 && index < lore.size()) {
            lore.set(index, newLine);
            this.meta.setLore(lore);
        }
        return this;
    }

    public ItemBuilderMechanic setLoreLine(int index, String line) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        lore.set(index, line);
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilderMechanic setFireworkEffect(FireworkEffect effect) {
        if (meta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) meta).setEffect(effect);
        }
        return this;
    }

    public ItemBuilderMechanic addPage(String page) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.addPage(page);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilderMechanic setAuthor(String author) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setAuthor(author);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilderMechanic setTitle(String title) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setTitle(title);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilderMechanic setGeneration(BookMeta.Generation generation) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setGeneration(generation);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilderMechanic setPower(int power) {
        if (item.getType() == Material.FIREWORK_ROCKET) {
            FireworkMeta fireworkMeta = (FireworkMeta) this.meta;
            fireworkMeta.setPower(power);
            this.meta = fireworkMeta;
        }
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, String value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, int value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, double value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, float value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, long value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, value);
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, byte[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, int[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER_ARRAY, value);
        return this;
    }

    public ItemBuilderMechanic addPersistentData(NamespacedKey key, long[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.LONG_ARRAY, value);
        return this;
    }

    public ItemBuilderMechanic removePersistentData(NamespacedKey key) {
        this.meta.getPersistentDataContainer().remove(key);
        return this;
    }

    public boolean hasPersistentData(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    // Para los próximos 10 métodos, necesitamos especificar el tipo de dato con el que estamos trabajando.

    public String getPersistentDataString(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public int getPersistentDataInteger(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
    }

    public double getPersistentDataDouble(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
    }

    public float getPersistentDataFloat(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.FLOAT);
    }

    public long getPersistentDataLong(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.LONG);
    }

    public byte[] getPersistentDataByteArray(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.BYTE_ARRAY);
    }

    public int[] getPersistentDataIntegerArray(NamespacedKey key) {
        return this.meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER_ARRAY);
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }

}