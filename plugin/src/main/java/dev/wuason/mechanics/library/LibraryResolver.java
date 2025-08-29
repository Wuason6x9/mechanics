package dev.wuason.mechanics.library;

import dev.wuason.mechanics.library.dependencies.Dependency;
import dev.wuason.mechanics.library.dependencies.DependencyManager;
import dev.wuason.mechanics.library.dependencies.DependencyResolved;
import dev.wuason.mechanics.library.repositories.Repository;
import dev.wuason.mechanics.mechanics.MechanicAddon;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LibraryResolver {

    public static final File LIBRARY_FOLDER = new File("libraries");

    public static LibraryResolver builder(MechanicAddon addon, LibraryLoaderType type) {
        return new LibraryResolver(addon, type);
    }

    private MechanicLibraryLoader libraryLoader;
    private final DependencyManager dependencyManager;
    private boolean autoInject = true;
    private boolean build = false;
    private final List<Consumer<DependencyResolved>> onResolveAndInjected;
    private LibraryLoaderType type = LibraryLoaderType.LIBRARY_CLASSPATH;

    protected LibraryResolver(MechanicAddon addon, LibraryLoaderType type) {
        URLClassLoader classLoader = (URLClassLoader) addon.getClass().getClassLoader();
        if (type == LibraryLoaderType.LIBRARY_CLASSPATH) {
            this.libraryLoader = new MechanicLibraryLoader(addon);
            classLoader = this.libraryLoader.getClassLoader();
        }
        this.dependencyManager = DependencyManager.create(addon, classLoader);
        this.onResolveAndInjected = new ArrayList<>();
        this.type = type;
    }

    public LibraryResolver addDependencies(Dependency... dependencies) {
        for (Dependency dependency : dependencies) {
            this.dependencyManager.addDependency(dependency);
        }
        return this;
    }

    public LibraryResolver autoInject(boolean autoInject) {
        this.autoInject = autoInject;
        return this;
    }

    public LibraryResolver libraryFolder(File libraryFolder) {
        this.dependencyManager.setDependenciesFolder(libraryFolder);
        return this;
    }

    public LibraryResolver addRepositories(Repository... repositories) {
        for (Repository repository : repositories) {
            this.dependencyManager.addRepository(repository);
        }
        return this;
    }

    public LibraryResolver onResolveAndInjected(Consumer<DependencyResolved> consumer) {
        this.onResolveAndInjected.add(consumer);
        return this;
    }

    public LibraryResolver addDefaultRepositories() {
        this.dependencyManager.loadDefaultRepositories();
        return this;
    }

    public DependencyManager build() {
        if (build) return dependencyManager;
        build = true;
        if (autoInject) {
            dependencyManager.addOnResolve(dependencyResolved -> {
                dependencyManager.inject(dependencyResolved);
                for (Consumer<DependencyResolved> consumer : onResolveAndInjected) {
                    consumer.accept(dependencyResolved);
                }
            });
        }
        return dependencyManager;
    }

    public enum LibraryLoaderType {
        LIBRARY_CLASSPATH,
        PLUGIN_CLASSPATH
    }

}
