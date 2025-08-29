package dev.wuason.mechanics.items.config_builder;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.wuason.mechanics.items.ItemBuilder;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.HashMap;
import java.util.Locale;

public class PropertyRegistry {
    private final HashMap<Object, Property> properties = new HashMap<>();
    private PropertyRegistry parent;

    public PropertyRegistry() {
    }

    public PropertyRegistry(PropertyRegistry parent) {
        this.parent = parent;
    }

    public void registerProperty(PropertyKey propertyKey, TriConsumer<ItemBuilder, Section, PropertyKey> consumer) {
        if (properties.containsKey(propertyKey)) {
            throw new IllegalArgumentException("Property already registered: " + propertyKey);
        }
        Property property = new Property(propertyKey, consumer);
        properties.put(propertyKey, property);
        properties.put(propertyKey.getKey().toLowerCase(Locale.ENGLISH), property);
    }

    public void unregisterProperty(PropertyKey propertyKey) {
        properties.remove(propertyKey);
        properties.remove(propertyKey.getKey().toLowerCase(Locale.ENGLISH));
    }

    public Property getProperty(PropertyKey propertyKey) {
        if (properties.containsKey(propertyKey)) {
            return properties.get(propertyKey);
        }
        else if (parent != null) {
            return parent.getProperty(propertyKey);
        }
        return null;
    }

    public boolean hasProperty(PropertyKey propertyKey) {
        if (properties.containsKey(propertyKey)) {
            return true;
        }
        else if (parent != null) {
            return parent.hasProperty(propertyKey);
        }
        return false;
    }

    public boolean hasProperty(String key) {
        if (properties.containsKey(key.toLowerCase(Locale.ENGLISH))) {
            return true;
        }
        else if (parent != null) {
            return parent.hasProperty(key);
        }
        return false;
    }

    public Property getProperty(String key) {
        if (properties.containsKey(key.toLowerCase(Locale.ENGLISH))) {
            return properties.get(key.toLowerCase(Locale.ENGLISH));
        }
        else if (parent != null) {
            return parent.getProperty(key);
        }
        return null;
    }

    public void unregisterProperty(String key) {
        Property property = properties.get(key.toLowerCase(Locale.ENGLISH));
        properties.remove(key.toLowerCase(Locale.ENGLISH));
        properties.remove(property.getPropertyKey());
    }

    public void removeParent() {
        parent = null;
    }


    static class Property {

        private final PropertyKey propertyKey;
        private TriConsumer<ItemBuilder, Section, PropertyKey> consumer;

        public Property(PropertyKey propertyKey, TriConsumer<ItemBuilder, Section, PropertyKey> consumer) {
            this.consumer = consumer;
            this.propertyKey = propertyKey;
        }

        public void apply(ItemBuilder builder, Section section, PropertyKey key) {
            consumer.accept(builder, section, key);
        }
        
        public PropertyKey getPropertyKey() {
            return propertyKey;
        }

        public TriConsumer<ItemBuilder, Section, PropertyKey> getConsumer() {
            return consumer;
        }

        public void setConsumer(TriConsumer<ItemBuilder, Section, PropertyKey> consumer) {
            this.consumer = consumer;
        }

    }
}
