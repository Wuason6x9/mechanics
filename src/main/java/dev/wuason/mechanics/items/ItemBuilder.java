package dev.wuason.mechanics.items;

import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack item, int amount) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.item.setAmount(amount);
    }

    public ItemBuilder setName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        this.meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag flag) {
        this.meta.removeItemFlags(flag);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        lore.add(line);
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        if (index >= 0 && index < lore.size()) {
            lore.remove(index);
            this.meta.setLore(lore);
        }
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
        }
        return this;
    }

    public ItemBuilder setPotionEffect(PotionEffectType effectType, int duration, int amplifier) {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
        }
        return this;
    }

    public ItemBuilder setPotionColor(Color color) {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).setColor(color);
        }
        return this;
    }

    public ItemBuilder clearPotionEffects() {
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).clearCustomEffects();
        }
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        if (glowing) {
            this.meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.meta.removeEnchant(Enchantment.ARROW_INFINITE);
            this.meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilder setInvisible(boolean invisible) {
        if (invisible) {
            this.meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            this.meta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder setLocalizedName(String name) {
        this.meta.setLocalizedName(name);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
        }
        return this;
    }

    public ItemBuilder clearMeta() {
        this.meta = this.item.getItemMeta();
        return this;
    }

    public ItemBuilder removeLore() {
        this.meta.setLore(new ArrayList<>());
        return this;
    }

    public ItemBuilder setDamage(int damage) {
        this.item.setDurability((short) damage);
        return this;
    }

    public ItemBuilder replaceLoreLine(int index, String newLine) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        if (index >= 0 && index < lore.size()) {
            lore.set(index, newLine);
            this.meta.setLore(lore);
        }
        return this;
    }

    public ItemBuilder setLoreLine(int index, String line) {
        List<String> lore = new ArrayList<>(this.meta.getLore());
        lore.set(index, line);
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder setFireworkEffect(FireworkEffect effect) {
        if (meta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta) meta).setEffect(effect);
        }
        return this;
    }

    public ItemBuilder addPage(String page) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.addPage(page);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilder setAuthor(String author) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setAuthor(author);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilder setTitle(String title) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setTitle(title);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilder setGeneration(BookMeta.Generation generation) {
        if (meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) this.meta;
            bookMeta.setGeneration(generation);
            this.meta = bookMeta;
        }
        return this;
    }

    public ItemBuilder setPower(int power) {
        if (item.getType() == Material.FIREWORK_ROCKET) {
            FireworkMeta fireworkMeta = (FireworkMeta) this.meta;
            fireworkMeta.setPower(power);
            this.meta = fireworkMeta;
        }
        return this;
    }

    public ItemBuilder setBaseColor(Color color) {
        if (meta instanceof BannerMeta) {
            ((BannerMeta) meta).setBaseColor(DyeColor.getByColor(color));
        }
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, String value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, int value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, double value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, float value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, long value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, value);
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, byte[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, int[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER_ARRAY, value);
        return this;
    }

    public ItemBuilder addPersistentData(NamespacedKey key, long[] value) {
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.LONG_ARRAY, value);
        return this;
    }

    public ItemBuilder removePersistentData(NamespacedKey key) {
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