package dev.wuason.mechanics.library.dependencies;

import org.objectweb.asm.commons.Remapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MechanicRemapper extends Remapper {
    private final Map<String, String> mapping = new HashMap<>();

    public MechanicRemapper(Remap... remaps) {
        for(Remap remap : remaps) {
            mapping.put(remap.getOld(), remap.getNew());
        }
    }
    public MechanicRemapper(Map<String, String> mapping) {
        this.mapping.putAll(mapping);
    }

    public MechanicRemapper(List<Remap> remapList) {
        for(Remap remap : remapList) {
            mapping.put(remap.getOld(), remap.getNew());
        }
    }

    public MechanicRemapper(Remap remap) {
        mapping.put(remap.getOld(), remap.getNew());
    }

    @Override
    public String map(final String key) {
        /*if(key.contains("dejvokep")) {
            System.out.println("Mapping: " + key + " to " + mapping.get(key));
        }*/
        for(Map.Entry<String, String> entry : mapping.entrySet()) {
            if(key.contains(entry.getKey())) {
                String value = key.replace(entry.getKey(), entry.getValue());
                return value;
            }
        }
        return super.map(key);
    }

    @Override
    public Object mapValue(Object object) {
        if (object instanceof String) {
            String relocatedName = map((String) object);
            if (relocatedName != null) {
                return relocatedName;
            }
        }
        return super.mapValue(object);
    }
}
