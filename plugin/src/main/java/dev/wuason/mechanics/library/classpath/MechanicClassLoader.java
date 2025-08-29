package dev.wuason.mechanics.library.classpath;

import dev.wuason.mechanics.mechanics.MechanicAddon;
import dev.wuason.mechanics.mechanics.MechanicManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class MechanicClassLoader extends URLClassLoader {

    public MechanicClassLoader(MechanicAddon core, ClassLoader parent, URL... urls) {
        super(urls, parent);
        MechanicManager.addClassLoader(core, this);
    }

    public MechanicClassLoader(MechanicAddon core, URL... urls) {
        super(urls);
        MechanicManager.addClassLoader(core, this);
    }


    public Class<?> loadClassOtherLoader(String name, ClassLoader loader) throws ClassNotFoundException {
        if (loader == null) {
            throw new ExceptionInInitializerError("ClassLoader is null in loadClassOtherLoader");
        }
        String path = name.replace('.', '/').concat(".class");
        InputStream is = loader.getResourceAsStream(path);
        if (is == null) {
            throw new ClassNotFoundException(name);
        }
        try {
            byte[] b = is.readAllBytes();
            return defineClass(name, b, 0, b.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    public Class<?> loadClassOtherLoader(Class<?> clazz, ClassLoader loader) throws ClassNotFoundException {
        return loadClassOtherLoader(clazz.getName(), loader);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public void addURLs(URL[] urls) {
        for (URL url : urls) {
            addURL(url);
        }
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (IOException e) {
        }
    }

    @Override
    public String toString() {
        return "MechanicClassLoader{" +
                "urls=" + getURLs() +
                '}';
    }
}
