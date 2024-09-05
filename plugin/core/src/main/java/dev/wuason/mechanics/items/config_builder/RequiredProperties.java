package dev.wuason.mechanics.items.config_builder;

import java.util.ArrayList;
import java.util.List;

public class RequiredProperties {
    private final List<String> requiredPropertiesKey = new ArrayList<>();
    private final List<String> requiredPropertiesComplete = new ArrayList<>();

    public RequiredProperties() {
    }

    public void completeRequiredProperty(String key) {
        requiredPropertiesComplete.add(key);
    }

    public boolean isComplete() {
        return requiredPropertiesComplete.containsAll(requiredPropertiesKey);
    }

    public List<String> getMissingProperties() {
        List<String> missingProperties = new ArrayList<>(requiredPropertiesKey);
        missingProperties.removeAll(requiredPropertiesComplete);
        return missingProperties;
    }

    public List<String> getRequiredPropertiesComplete() {
        return requiredPropertiesComplete;
    }

    public void setRequiredPropertiesComplete(List<String> requiredPropertiesComplete) {
        this.requiredPropertiesComplete.clear();
        this.requiredPropertiesComplete.addAll(requiredPropertiesComplete);
    }

    public void removeCompleteRequiredProperty(String key) {
        requiredPropertiesComplete.remove(key);
    }

    public void addRequiredProperty(String key) {
        requiredPropertiesKey.add(key);
    }

    public void addRequiredProperty(PropertyKey key) {
        requiredPropertiesKey.add(key.getKey());
    }

    public void removeRequiredProperty(PropertyKey key) {
        requiredPropertiesKey.remove(key.getKey());
    }

    public List<String> getRequiredPropertiesKey() {
        return requiredPropertiesKey;
    }

    public void setRequiredPropertiesKey(List<String> requiredPropertiesKey) {
        this.requiredPropertiesKey.clear();
        this.requiredPropertiesKey.addAll(requiredPropertiesKey);
    }



}
