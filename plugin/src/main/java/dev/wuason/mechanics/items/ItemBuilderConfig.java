package dev.wuason.mechanics.items;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ItemBuilderConfig {

    private final HashMap<String, Property> properties = new HashMap<>(){{
        put("displayname", new Property("displayname", "DISPLAY_NAME", "displayName", (itemBuilder, o) -> {
            itemBuilder.setNameWithMiniMessage((String) o);
        }));

        put("lore", new Property("lore", "LORE", "lore", (itemBuilder, o) -> {
            itemBuilder.setLoreWithMiniMessage((List<String>) o);
        }));
    }};

    public ItemBuilderConfig() {
    }

    // displayname -> hola -> DISPLAY_NAME



    public ItemStack build(Section section) {

        for (Map.Entry<String, Property> entry : properties.entrySet()) {

        }

        return null;
    }


    public final class Builder {

        private final ItemBuilderConfig config;

        public Builder() {
            this.config = new ItemBuilderConfig();
        }

    }

    class Property {

        private final String key;
        private final String value;
        private final String id;
        private final BiConsumer<ItemBuilder, Object> consumer;

        public Property(String key, String value, String id, BiConsumer<ItemBuilder, Object> consumer) {
            this.key = key;
            this.value = value;
            this.id = id;
            this.consumer = consumer;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String getId() {
            return id;
        }

        public BiConsumer<ItemBuilder, Object> getConsumer() {
            return consumer;
        }

    }

}
