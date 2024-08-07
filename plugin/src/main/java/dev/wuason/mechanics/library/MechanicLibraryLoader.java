package dev.wuason.mechanics.library;

import dev.wuason.mechanics.library.classpath.MechanicClassLoader;
import dev.wuason.mechanics.mechanics.MechanicAddon;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

public class MechanicLibraryLoader {
    private URLClassLoader classLoader;
    public MechanicLibraryLoader(MechanicAddon plugin) {
        classLoader = null;
        if(plugin.getClass().getClassLoader() instanceof PluginClassLoader pluginClassLoader) {
            Field field = null;
            try {
                field = pluginClassLoader.getClass().getDeclaredField("libraryLoader");
                field.setAccessible(true);
                Object libraryLoader = field.get(pluginClassLoader);
                if (libraryLoader != null) {
                    if (libraryLoader instanceof URLClassLoader urlClassLoader) {
                        classLoader = urlClassLoader;
                    }
                }
                else {
                    classLoader = new MechanicClassLoader(plugin, JavaPluginLoader.class.getClassLoader(), new URL[0]);
                    field.set(pluginClassLoader, classLoader);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (classLoader == null) {
            throw new IllegalArgumentException("Could not find class loader");
        }
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }
}
