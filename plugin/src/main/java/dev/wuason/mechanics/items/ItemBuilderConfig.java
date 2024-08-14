package dev.wuason.mechanics.items;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public final class ItemBuilderConfig {

    public ItemBuilderConfig() {
    }

    // displayname -> hola -> DISPLAY_NAME



    public ItemStack build(Section section) {





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

        public Property(String key, String value, String id) {
            this.key = key;
            this.value = value;
            this.id = id;
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

    }

}
