package dev.wuason.mechanics.items.config_builder;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.wuason.mechanics.items.ItemBuilder;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ItemBuilderConfig extends ItemBuilder {

    public static final PropertyRegistry PROPERTY_REGISTRY = new PropertyRegistry();

    static {
        PROPERTY_REGISTRY.registerProperty(DefaultPropertyKey.DISPLAY_NAME, (builder, section, key) -> {
            builder.setNameWithMiniMessage(section.getString(key.getKey(), "NOT FOUND"));
        });
        PROPERTY_REGISTRY.registerProperty(DefaultPropertyKey.ADAPTER, (builder, section, key) -> {
            builder.adapter(section.getString(key.getKey(), "NOT FOUND"));
        });
        PROPERTY_REGISTRY.registerProperty(DefaultPropertyKey.MATERIAL, (builder, section, key) -> {
            builder.setMaterial(Material.getMaterial(section.getString(key.getKey(), "NOT FOUND").toUpperCase(Locale.ENGLISH)));
        });
        PROPERTY_REGISTRY.registerProperty(DefaultPropertyKey.AMOUNT, (builder, section, key) -> {
            builder.setAmount(section.getInt(key.getKey(), 1));
        });
        PROPERTY_REGISTRY.registerProperty(DefaultPropertyKey.DURABILITY, (builder, section, key) -> {
            builder.setDurability(section.getInt(key.getKey()));
        });
    }

    private final List<TriFunction> eventHandlers = new ArrayList<>();
    private final List<String> keyIgnoreList = new ArrayList<>();

    private final PropertyRegistry registry = new PropertyRegistry(PROPERTY_REGISTRY);
    private final RequiredProperties requiredProperties = new RequiredProperties();

    public ItemBuilderConfig() {
    }

    public void applySection(Section section) {
        applySection(section, registry);
    }

    private void applySection(Section section, String previousKey) {
        applySection(section, registry, previousKey);
    }

    public void applySection(Section section, PropertyRegistry registry) {
        applySection(section, registry, null);
    }

    private void applySection(Section section, PropertyRegistry registry, String previousKey) {
        previousKey = previousKey == null ? "" : previousKey + ".";
        for (String key : section.getRoutesAsStrings(false)) {
            String fullKey = previousKey + key;
            if (section.isSection(key)) {
                applySection(section.getSection(key), registry, fullKey);
            }
            else {
                if (keyIgnoreList.contains(fullKey)) {
                    continue;
                }
                PropertyRegistry.Property property = registry.getProperty(fullKey);
                if (property != null) {
                    boolean consumed = false;
                    for (TriFunction<ItemBuilderConfig, Section, String, Boolean> eventHandler : eventHandlers) {
                        try {
                            consumed = eventHandler.apply(this, section, fullKey);
                            if (consumed) {
                                requiredProperties.completeRequiredProperty(fullKey);
                                break;
                            }
                        }
                        catch (Exception e) {
                            throw new IllegalStateException("Error applying event handler: " + eventHandler, e);
                        }

                    }
                    if (!consumed) {
                        try {
                            property.apply(this, section, property.getPropertyKey());
                            requiredProperties.completeRequiredProperty(fullKey);
                        }
                        catch (Exception e) {
                            throw new IllegalStateException("Error applying property: " + fullKey, e);
                        }
                    }
                }
            }
        }
    }

    public void addEventHandler(TriFunction<ItemBuilderConfig, Section, String, Boolean> eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void addKeyToIgnore(String key) {
        keyIgnoreList.add(key);
    }

    public void removeKeyToIgnore(String key) {
        keyIgnoreList.remove(key);
    }

    public PropertyRegistry getRegistry() {
        return registry;
    }

    public RequiredProperties getRequiredProperties() {
        return requiredProperties;
    }

    @Override
    public ItemStack build() {
        if (!requiredProperties.isComplete()) {
            throw new IllegalStateException("Missing required properties: " + requiredProperties.getMissingProperties() + " for creating item");
        }
        return super.build();
    }

    public enum DefaultPropertyKey implements PropertyKey {
        DISPLAY_NAME("display_name"),
        ADAPTER("item"),
        MATERIAL("material"),
        AMOUNT("amount"),
        DURABILITY("durability"),
        LORE("lore"),
        ;

        private final String key;

        DefaultPropertyKey(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getId() {
            return this.name().toUpperCase(Locale.ENGLISH);
        }
    }

}
