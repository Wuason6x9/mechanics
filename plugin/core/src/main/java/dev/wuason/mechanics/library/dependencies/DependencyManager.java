package dev.wuason.mechanics.library.dependencies;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.library.classpath.ClassLoaderInjector;
import dev.wuason.mechanics.library.classpath.MechanicClassLoader;
import dev.wuason.mechanics.library.repositories.Checksum;
import dev.wuason.mechanics.library.repositories.Repos;
import dev.wuason.mechanics.library.repositories.Repository;
import dev.wuason.mechanics.mechanics.MechanicAddon;

import java.io.*;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Consumer;

/**
 * Manages dependencies for a MechanicAddon project, including loading, resolving,
 * injecting, and re-mapping JAR files for required dependencies.
 */
public class DependencyManager {

    /**
     * The core MechanicAddon instance used by the DependencyManager.
     * This is the primary addon that the DependencyManager operates on,
     * allowing it to manage dependencies and other related tasks.
     */
    private final MechanicAddon core;
    /**
     * A list that holds all resolved dependencies for a DependencyManager instance.
     * Each entry in this list corresponds to a DependencyResolved object, which encapsulates
     * information about a dependency that has been successfully resolved, including its file,
     * checksum and associated metadata.
     */
    private final ArrayList<DependencyResolved> dependenciesResolved = new ArrayList<>();
    /**
     * The `classLoader` variable is an instance of `URLClassLoader` used to dynamically load classes and resources
     * from specified URLs during the runtime of the application.
     */
    private final URLClassLoader classLoader;
    /**
     * Represents the directory where dependency libraries are stored.
     * This folder contains all the jar files needed for dependencies in the project.
     * Default location for this folder is "libraries".
     */
    private final File dependenciesFolder = new File("libraries");
    /**
     * An instance of {@link ClassLoaderInjector} used to inject classes and resources into the class loader.
     * This variable assists in dynamically adding URL paths to the class loader, managing unopened URLs,
     * and supports operations like injecting single or multiple JAR files, and loading classes from other loaders.
     */
    private final ClassLoaderInjector injector;
    /**
     * A list of repositories that the DependencyManager uses to resolve dependencies.
     */
    private final ArrayList<Repository> repositories = new ArrayList<>();
    /**
     * A map that holds the dependencies managed by the DependencyManager.
     * The keys are dependency identifiers in string format, and the values are Dependency objects.
     */
    private final Map<String, Dependency> dependencies = new HashMap<>();
    /**
     * A list of consumers that will be called when a dependency is resolved.
     *
     * This list holds {@link Consumer} instances that accept a {@link DependencyResolved} object.
     * Each consumer in this list is called when a dependency resolution event occurs.
     */
    private final List<Consumer<DependencyResolved>> onResolve = new ArrayList<>();
    /**
     * Helper object for handling JAR remapping tasks. It manages the lifecycle
     * of a JarRemapper instance and handles the injection of necessary classes
     * and dependencies for performing remapping operations.
     */
    private JarRemapperHelper jarRemapperHelper;

    /**
     * Creates a new instance of DependencyManager with the provided MechanicAddon and URLClassLoader.
     *
     * @param core the core MechanicAddon instance that serves as the primary plugin
     * @param classLoader the URLClassLoader instance that will be used for loading dependencies
     * @return a new instance of DependencyManager
     */
    public static DependencyManager create(MechanicAddon core, URLClassLoader classLoader) {
        return new DependencyManager(core, classLoader, JarRemapperHelper.REMAPPER);
    }

    /**
     * Create a new instance of DependencyManager.
     *
     * @param core the MechanicAddon core to be used by the DependencyManager.
     * @param classLoader the URLClassLoader to be used for loading dependencies.
     * @param loadDefaultRemapper a boolean to indicate whether to load the default JarRemapperHelper.
     *
     * @return a new instance of DependencyManager.
     */
    public static DependencyManager create(MechanicAddon core, URLClassLoader classLoader, boolean loadDefaultRemapper) {
        return new DependencyManager(core, classLoader, loadDefaultRemapper ? JarRemapperHelper.REMAPPER : null);
    }

    /**
     * Creates a new instance of {@code DependencyManager} with a custom class loader.
     *
     * @param core the core {@code MechanicAddon} instance
     * @param classLoader the class loader to be used
     * @return a new {@code DependencyManager} instance configured with the provided class loader
     */
    public static DependencyManager createWithCustomLoader(MechanicAddon core, ClassLoader classLoader) {
        return new DependencyManager(core, new MechanicClassLoader(core, core.getClass().getClassLoader(), new java.net.URL[0]), null);
    }

    /**
     * Creates a DependencyManager with a custom class loader.
     *
     * @param core the MechanicAddon object that serves as the core component for the dependency manager
     * @return an instance of DependencyManager configured with a custom class loader
     */
    public static DependencyManager createWithCustomLoader(MechanicAddon core) {
        return new DependencyManager(core, new MechanicClassLoader(core, core.getClass().getClassLoader(), new java.net.URL[0]), null);
    }

    /**
     * Creates a new instance of {@link DependencyManager} using an aisled loader.
     *
     * @param core the {@link MechanicAddon} instance representing the core addon.
     * @return a new {@link DependencyManager} instance initialized with a {@link MechanicClassLoader}.
     */
    public static DependencyManager createWithAisledLoader(MechanicAddon core) {
        return new DependencyManager(core, new MechanicClassLoader(core), null);
    }

    /**
     * Creates a DependencyManager instance with the default Mechanics singleton
     * and a MechanicClassLoader initialized with the same Mechanics instance.
     *
     * @return a new instance of DependencyManager configured with the default
     *         Mechanics singleton and a MechanicClassLoader.
     */
    public static DependencyManager createWithAisledLoader() {
        return new DependencyManager(Mechanics.getInstance(), new MechanicClassLoader(Mechanics.getInstance()), null);
    }

    DependencyManager(MechanicAddon core, URLClassLoader classLoader, JarRemapperHelper jarRemapperHelper) {
        this.core = core;
        this.classLoader = classLoader;
        this.injector = new ClassLoaderInjector(classLoader);
        this.dependenciesFolder.mkdirs();
        this.jarRemapperHelper = jarRemapperHelper;
    }

    /**
     * Loads the default JarRemapperHelper instance for the current DependencyManager.
     *
     * This method sets the jarRemapperHelper field to the predefined REMAPPER instance
     * from the JarRemapperHelper class.
     */
    public void loadDefaultRemapper() {
        this.jarRemapperHelper = JarRemapperHelper.REMAPPER;
    }

    /**
     * Adds a repository to the list of repositories if it is not already present.
     *
     * @param repository The repository to be added.
     */
    public void addRepository(Repository repository) {
        if (!repositories.contains(repository)) repositories.add(repository);
    }

    /**
     * Adds a {@link Dependency} to the dependency map if it is not already present.
     *
     * @param dependency the {@link Dependency} instance to be added to the dependency map
     */
    public void addDependency(Dependency dependency) {
        if (!dependencies.containsKey(dependency.toString())) {
            dependencies.put(dependency.toString(), dependency);
        }
    }

    /**
     * Adds a list of dependencies to the DependencyManager.
     *
     * @param dependencies the list of Dependency objects to be added
     */
    public void addDependencies(List<Dependency> dependencies) {
        for (Dependency dependency : dependencies) {
            addDependency(dependency);
        }
    }

    /**
     * Adds multiple repositories to the DependencyManager.
     *
     * @param repositories the list of Repository instances to be added.
     */
    public void addRepositories(List<Repository> repositories) {
        for (Repository repository : repositories) {
            addRepository(repository);
        }
    }

    /**
     * Retrieves the local file path of the JAR file corresponding to the specified dependency.
     *
     * @param dependency the dependency for which the local JAR file path is to be retrieved
     * @return a File object representing the local JAR file path of the specified dependency
     */
    private File getJarFileLocally(Dependency dependency) {
        return new File(this.dependenciesFolder, String.join("/", dependency.getGroupPath()) + "/" + dependency.getArtifactId() + "/" + dependency.getVersion() + "/" + dependency.getJarName());
    }

    /**
     * Injects a class using the provided class loader. If the class loader is an instance of {@link MechanicClassLoader},
     * it injects the class from the other loader. Otherwise, it returns the original class.
     *
     * @param clazz the class to be injected
     * @param classLoader the class loader to use for injection
     * @return the injected class if using {@link MechanicClassLoader}; otherwise, the original class
     */
    public Class<?> inject(Class<?> clazz, ClassLoader classLoader) {
        if (classLoader instanceof MechanicClassLoader) return injector.injectClassFromOtherLoader(clazz, classLoader);
        return clazz;
    }

    /**
     * Injects a given class using a custom class loader if applicable.
     *
     * @param clazz the class to be injected
     * @return the injected class if a custom class loader is used, otherwise returns the original class
     */
    public Class<?> inject(Class<?> clazz) {
        if (!(this.classLoader instanceof MechanicClassLoader)) return clazz;
        return injector.injectClassFromOtherLoader(clazz, clazz.getClassLoader());
    }

    /**
     * Sets the JarRemapperHelper for this DependencyManager.
     *
     * @param jarRemapperHelper the JarRemapperHelper instance to be used for managing JAR remapping tasks
     */
    public void setJarRemapper(JarRemapperHelper jarRemapperHelper) {
        this.jarRemapperHelper = jarRemapperHelper;
    }

    /**
     * Loads the default repositories into the DependencyManager instance, which include
     * standard Maven Central and its mirrors. This method adds the predefined repositories
     * to the list of repositories used for dependency resolution.
     */
    public void loadDefaultRepositories() {
        addRepositories(Arrays.asList(Repos.MAVEN_CENTRAL, Repos.MAVEN_CENTRAL_MIRROR, Repos.MAVEN_CENTRAL_MIRROR2));
    }

    /**
     * Injects a list of classes and returns a map of the original classes to their
     * respective injected classes.
     *
     * @param classes the array of classes to be injected
     * @return a map where the key is the original class and the value is the injected class
     */
    public HashMap<Class<?>, Class<?>> inject(Class<?>... classes) {
        HashMap<Class<?>, Class<?>> classMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            classMap.put(clazz, inject(clazz));
        }
        return classMap;
    }

    /**
     * Injects all resolved dependencies into the current environment.
     *
     * This method iterates over each resolved dependency in the list and
     * calls the {@code inject} method to inject them individually.
     */
    public void injectAll() {
        for (DependencyResolved dependency : dependenciesResolved) {
            inject(dependency);
        }
    }

    /**
     * Injects a resolved dependency into the application.
     *
     * @param dependency the resolved dependency to be injected
     */
    public void inject(DependencyResolved dependency) {
        if (dependency != null) {
            injector.injectJar(dependency.getJarFile());
        }
    }

    /**
     * Injects a list of resolved dependencies into the system.
     *
     * @param dependencies the list of DependencyResolved instances to be injected
     */
    public void inject(List<DependencyResolved> dependencies) {
        for (DependencyResolved dependency : dependencies) {
            inject(dependency);
        }
    }

    /**
     * Resolves all dependencies and returns a list of resolved dependencies.
     *
     * @return a list of resolved dependencies.
     */
    public List<DependencyResolved> resolve() {
        //resolve dependencies
        List<DependencyResolved> resolved = new ArrayList<>();
        for (Dependency dependency : dependencies.values()) {
            List<DependencyResolved> dependencyResolved = resolve(dependency);
            resolved.addAll(dependencyResolved);
        }
        return resolved;
    }

    /**
     * Invokes all registered consumers with the given resolved dependency.
     *
     * @param dependency the resolved dependency to be passed to the consumers
     */
    private void onResolve(DependencyResolved dependency) {
        for (Consumer<DependencyResolved> consumer : onResolve) {
            consumer.accept(dependency);
        }
    }

    /**
     * Resolves the given dependencies and returns a list of resolved dependencies.
     *
     * @param dependency varargs parameter representing the dependencies to be resolved.
     * @return a list of {@code DependencyResolved} objects representing the resolved dependencies.
     */
    public List<DependencyResolved> resolve(Dependency... dependency) {
        return resolve(false, dependency);
    }

    /**
     * Resolves a list of dependencies based on the given parameters.
     *
     * @param isToIgnore a boolean indicating whether to ignore certain conditions during the resolution process.
     * @param dependency an array of {@link Dependency} objects to be resolved.
     * @return a list of {@link DependencyResolved} objects representing the resolved dependencies.
     */
    public List<DependencyResolved> resolve(boolean isToIgnore, Dependency... dependency) {
        return resolve(isToIgnore, Arrays.stream(dependency).map(Dependency::getArtifactId).toArray(String[]::new));
    }

    /**
     * Resolves dependencies that match the provided regex patterns.
     *
     * @param regex the regular expressions to match the dependencies against
     * @return a list of resolved dependencies that match the given regular expressions
     */
    public List<DependencyResolved> resolve(String... regex) {
        return resolve(false, regex);
    }

    /**
     * Resolves dependencies based on provided regex patterns.
     *
     * @param isToIgnore indicates whether to ignore or consider dependencies matching the provided regex patterns.
     * @param regex the array of regex patterns used for matching the dependencies' artifact IDs.
     * @return a list of resolved dependencies that match the criteria defined by the regex patterns and the isToIgnore flag.
     */
    public List<DependencyResolved> resolve(boolean isToIgnore, String... regex) {
        List<DependencyResolved> resolvedList = new ArrayList<>();
        for (Dependency dependency : dependencies.values()) {
            if(isToIgnore){
                if (Arrays.stream(regex).anyMatch(dependency.getArtifactId()::matches)) {
                    continue;
                }
            } else {
                if (!Arrays.stream(regex).anyMatch(dependency.getArtifactId()::matches)) {
                    continue;
                }
            }
            List<DependencyResolved> resolved = resolve(dependency);
            resolvedList.addAll(resolved);
        }
        return resolvedList;
    }

    /**
     * Resolves the given dependency by checking if it is already resolved or by
     * downloading it from the configured repositories if it is not.
     *
     * @param dependency the dependency to be resolved
     * @return a list containing the resolved dependency or an empty list if the dependency could not be resolved
     */
    private List<DependencyResolved> resolve(Dependency dependency) {
        if (dependenciesResolved.contains(dependency)) {
            return dependenciesResolved.stream().filter(dependencyResolved -> dependencyResolved.equals(dependency)).findFirst().map(Collections::singletonList).orElse(Collections.emptyList());
        }
        long start = System.currentTimeMillis();
        DependencyResolved resolved = null;
        File jar = getJarFileLocally(dependency);
        if (dependency.getJarFile() != null) {
            resolved = new DependencyResolved(dependency, dependency.getJarFile(), this, System.currentTimeMillis() - start);
        }
        else if (jar.exists()) {
            resolved = new DependencyResolved(dependency, jar, this, System.currentTimeMillis() - start);
        }
        else {
            for (Repository repository : repositories) {
                try (InputStream inputStream = repository.downloadDependency(dependency, Repository.DownloadType.JAR)) {
                    if (inputStream == null) continue;
                    File file = saveDependency(inputStream, Repository.DownloadType.JAR, dependency);
                    Checksum checksum = new Checksum();
                    checksum.downloadChecksums(repository, dependency);
                    resolved = new DependencyResolved(dependency, file, this, System.currentTimeMillis() - start, checksum);
                    break;
                } catch (Exception e) {
                }
            }
        }
        if (resolved != null) {
            onResolve(resolved);
            dependenciesResolved.add(resolved);
            return Collections.singletonList(resolved);
        }
        return Collections.emptyList();
    }


    /**
     * Saves a dependency to the local file system by reading data from an input stream.
     *
     * @param inputStream the InputStream from which to read the dependency data
     * @param type the download type to be used for the repository
     * @param dependency the dependency object that contains information about the dependency to be saved
     * @return a File object pointing to the saved dependency file
     * @throws IOException if an I/O error occurs during the read/write process
     */
    private File saveDependency(InputStream inputStream, Repository.DownloadType type, Dependency dependency) throws IOException {
        BufferedInputStream in = new BufferedInputStream(inputStream);
        File file = getJarFileLocally(dependency);
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();
        in.close();
        return getJarFileLocally(dependency);
    }


    /**
     * Retrieves the core MechanicAddon.
     *
     * @return the core MechanicAddon instance.
     */
    public MechanicAddon getCore() {
        return core;
    }

    /**
     * Retrieves the list of resolved dependencies.
     *
     * @return An ArrayList of DependencyResolved objects representing the resolved dependencies.
     */
    public ArrayList<DependencyResolved> getDependenciesResolved() {
        return dependenciesResolved;
    }

    /**
     * Retrieves the class loader.
     *
     * @return the URLClassLoader instance used by this class
     */
    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Retrieves the folder where the dependencies are stored.
     *
     * @return the File object representing the dependencies folder
     */
    public File getDependenciesFolder() {
        return dependenciesFolder;
    }

    /**
     * Retrieves the current instance of ClassLoaderInjector.
     *
     * @return the current ClassLoaderInjector instance
     */
    public ClassLoaderInjector getInjector() {
        return injector;
    }

    /**
     * Retrieves a list of repositories.
     *
     * @return an ArrayList of Repository objects representing the repositories.
     */
    public ArrayList<Repository> getRepositories() {
        return repositories;
    }

    /**
     * Retrieves the map of dependencies.
     *
     * @return a map where the keys are dependency names and the values are Dependency objects
     */
    public Map<String, Dependency> getDependencies() {
        return dependencies;
    }

    /**
     * Returns an instance of JarRemapperHelper.
     *
     * @return an instance of JarRemapperHelper.
     */
    public JarRemapperHelper getJarRemapper() {
        return jarRemapperHelper;
    }

    /**
     * Sets the folder where dependencies will be stored for the application.
     * This method will create the directory structure if it does not already exist.
     *
     * @param dependenciesFolder the File object representing the directory in which dependencies will be stored
     */
    public void setDependenciesFolder(File dependenciesFolder) {
        dependenciesFolder.mkdirs();
    }

    /**
     * Registers a callback to be invoked when a dependency is resolved.
     *
     * @param onResolve a Consumer that takes a DependencyResolved object,
     * which represents the resolved dependency on which actions can be performed.
     */
    public void addOnResolve(Consumer<DependencyResolved> onResolve) {
        this.onResolve.add(onResolve);
    }

    /**
     * Closes the class loader.
     *
     * The method attempts to safely close the associated class loader.
     * If an IOException occurs during the process, the stack trace of the exception is printed.
     *
     * Method is void and does not return a value.
     *
     * This method should be called to release resources associated with the classLoader.
     */
    public void close() {
        try {
            classLoader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given dependency has been resolved.
     *
     * @param dependency the dependency to check for resolution
     * @return true if the dependency is resolved, false otherwise
     */
    public boolean isResolved(Dependency dependency) {
        return dependenciesResolved.stream().anyMatch(dependencyResolved -> dependencyResolved.equals(dependency));
    }

    /*public Dependency getDependency(Artifact artifact) {
        return dependencies.get(Dependency.toMavenString(artifact));
    }*/

    /**
     * Retrieves the dependency associated with the specified artifact.
     *
     * @param artifact the name of the artifact for which the dependency is to be retrieved
     * @return the dependency associated with the specified artifact, or null if no dependency is found
     */
    public Dependency getDependency(String artifact) {
        return dependencies.get(artifact);
    }

    /**
     * Retrieves a dependency based on its group ID, artifact ID, and version.
     *
     * @param groupId the group ID of the dependency
     * @param artifactId the artifact ID of the dependency
     * @param version the version of the dependency
     * @return the corresponding Dependency object if found, otherwise null
     */
    public Dependency getDependency(String groupId, String artifactId, String version) {
        return dependencies.get(Dependency.toMavenString(groupId, artifactId, version));
    }

    /**
     * Retrieves an existing dependency based on the provided groupId, artifactId, and version.
     * If the dependency does not already exist, it creates a new one and adds it to the collection.
     *
     * @param groupId The group ID of the dependency.
     * @param artifactId The artifact ID of the dependency.
     * @param version The version of the dependency.
     * @return The existing or newly created dependency.
     */
    public Dependency getOrCreateDependency(String groupId, String artifactId, String version) {
        Dependency dependency = getDependency(groupId, artifactId, version);
        if (dependency == null) {
            dependency = new Dependency(groupId, artifactId, version);
            addDependency(dependency);
        }
        return dependency;
    }

    /*public Dependency getOrCreateDependency(Artifact artifact) {
        Dependency dependency = getDependency(artifact);
        if (dependency == null) {
            dependency = new Dependency(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
            addDependency(dependency);
        }
        return dependency;
    }*/

    /**
     * Checks if all dependencies are resolved.
     *
     * @return true if all dependencies are resolved, false otherwise
     */
    public boolean allResolved() {
        boolean resolved = true;
        for (Dependency dependency : dependencies.values()) {
            if (!isResolved(dependency)) {
                resolved = false;
                break;
            }
        }
        return resolved;
    }

    /**
     * Retrieves a list of dependencies that have not been resolved.
     *
     * @return a list of dependencies that are not resolved.
     */
    public List<Dependency> getDependenciesNotResolved() {
        List<Dependency> notResolved = new ArrayList<>();
        for (Dependency dependency : dependencies.values()) {
            if (!isResolved(dependency)) {
                notResolved.add(dependency);
            }
        }
        return notResolved;
    }

}
