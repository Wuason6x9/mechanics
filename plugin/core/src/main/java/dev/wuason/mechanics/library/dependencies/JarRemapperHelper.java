package dev.wuason.mechanics.library.dependencies;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class JarRemapperHelper {
    private static final Class<?>[] CLASSES_DEPENDENCIES = new Class<?>[]{
            Remap.class,
            MechanicRemapper.class,
            JarRemapper.class
    };
    private static final Dependency[] DEPENDENCIES = new Dependency[]{
            Dependencies.ASM_COMMONS,
            Dependencies.ASM
    };

    private Object jarRemapper;
    private DependencyManager dependencyManager;
    private final boolean withAisledLoader;
    private HashMap<Class<?>, Class<?>> classMap;

    private JarRemapperHelper(boolean withAisledLoader) {
        this.withAisledLoader = withAisledLoader;
    }

    public JarRemapperHelper(Object jarRemapper) {
        this(false);
        this.jarRemapper = jarRemapper;
    }

    public JarRemapperHelper() {
        this(true);
    }

    public void load() {
        if (!withAisledLoader || jarRemapper != null) return;
        classMap = new HashMap<>();
        dependencyManager = DependencyManager.createWithAisledLoader();
        dependencyManager.addDependencies(Arrays.stream(DEPENDENCIES).toList());
        dependencyManager.loadDefaultRepositories();
        dependencyManager.resolve().forEach(dependencyResolved -> {
            dependencyManager.getInjector().injectJar(dependencyResolved.getFile());
        });
        HashMap<Class<?>, Class<?>> injected = dependencyManager.inject(CLASSES_DEPENDENCIES);
        classMap.putAll(injected);

        Constructor<?> constructor = convert(JarRemapper.class).getConstructors()[0];
        try {
            jarRemapper = constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getJarRemapper() {
        if (jarRemapper == null) {
            load();
        }
        return jarRemapper;
    }

    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }

    public File remap(File oldFile, Remap... remaps) {
        return (File) invokeRemap(oldFile, remaps);
    }

    public void remap(File oldFile, File newFile, Remap... remaps) {
        invokeRemap(oldFile, newFile, remaps);
    }

    private Class<?> convert(Class<?> clazz) {
        if (clazz.getClassLoader() == null) return clazz;
        if(clazz.isArray() && clazz.getComponentType().getClassLoader() != null && classMap.containsKey(clazz.getComponentType())) {
            if (Remap.class == clazz.getComponentType()) {
                Class<?> componentType = convert(clazz.getComponentType());
                return componentType.arrayType();
            }
        }
        if (classMap == null) return clazz;
        if (classMap.containsKey(clazz)) {
            return classMap.get(clazz);
        }
        return clazz;
    }

    private Object invokeRemap(Object... args) {
        if (jarRemapper == null) {
            load();
        }
        if (args.length == 0) {
            throw new IllegalArgumentException("No arguments provided");
        }
        try {
            Class<?> jarRemapperClass = jarRemapper.getClass();
            Method method = jarRemapperClass.getDeclaredMethod("remap", Arrays.stream(args).map(o -> convert(o.getClass())).toArray(Class[]::new));
            method.setAccessible(true);
            for (int i = 0; i < args.length; ++i) {
                if(args[i].getClass() == Remap[].class) {
                    args[i] = convert(Remap.class).arrayType().cast(convertRemapObjects((Remap[]) args[i]));
                }
            }
            return method.invoke(jarRemapper, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object convertRemapObject(Remap remap) {
        try {
            Class<?> remapClass = convert(Remap.class);
            Method method = remapClass.getDeclaredMethod("of", String.class, String.class);
            Object returnObject = method.invoke(null, remap.getOld(), remap.getNew());
            return returnObject;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object convertRemapObjects(Remap[] remaps) {
        Object objects = Array.newInstance(convert(Remap.class), remaps.length);
        for (int i = 0; i < remaps.length; ++i) {
            Array.set(objects, i, convertRemapObject(remaps[i]));
        }
        return objects;
    }

    public static final JarRemapperHelper REMAPPER = new JarRemapperHelper();
}
