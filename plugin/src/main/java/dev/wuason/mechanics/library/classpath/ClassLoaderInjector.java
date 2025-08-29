package dev.wuason.mechanics.library.classpath;

import dev.wuason.mechanics.utils.AdvancedUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

public class ClassLoaderInjector { //java 9+ only

    private final URLClassLoader classLoader;
    private Collection<URL> paths;
    private Collection<URL> unopenedUrls;

    public ClassLoaderInjector(URLClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("ClassLoader cannot be null");
        }
        this.classLoader = classLoader;
        if (classLoader instanceof MechanicClassLoader) {
            return;
        }
        Object ucp = AdvancedUtils.fetchField(URLClassLoader.class, classLoader, "ucp");
        if (ucp == null) {
            throw new UnsupportedOperationException("Error: Could not access the URLClassPath of the plugin class loader. 1");
        }

        this.paths = (Collection<URL>) AdvancedUtils.fetchField(ucp.getClass(), ucp, "path");
        this.unopenedUrls = (Collection<URL>) AdvancedUtils.fetchField(ucp.getClass(), ucp, "unopenedUrls");
    }

    public void inject(URL url) {
        if (classLoader instanceof MechanicClassLoader) {
            MechanicClassLoader mechanicClassLoader = (MechanicClassLoader) classLoader;
            mechanicClassLoader.addURL(url);
            return;
        }
        if(paths == null || unopenedUrls == null) {
            throw new UnsupportedOperationException("Error: Could not access the URLClassPath of the plugin class loader. 2");
        }
        synchronized (unopenedUrls) {
            unopenedUrls.add(url);
            paths.add(url);
        }
    }

    public void inject(URL[] urls) {
        if (classLoader instanceof MechanicClassLoader) {
            MechanicClassLoader mechanicClassLoader = (MechanicClassLoader) classLoader;
            mechanicClassLoader.addURLs(urls);
            return;
        }
        for (URL url : urls) {
            inject(url);
        }
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    public void injectJar(File jar) {
        try {
            inject(jar.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void injectJars(File... jars) {
        for (File jar : jars) {
            injectJar(jar);
        }
    }

    public Class<?> injectClassFromOtherLoader(Class<?> clazz, ClassLoader loader) { //only for MechanicClassLoader on this moment
        if (classLoader instanceof MechanicClassLoader mechanicClassLoader) {
            try {
                return mechanicClassLoader.loadClassOtherLoader(clazz, loader);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return clazz;
    }

}
