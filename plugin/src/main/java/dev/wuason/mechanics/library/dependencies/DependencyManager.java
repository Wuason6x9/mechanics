package dev.wuason.mechanics.library.dependencies;

import dev.wuason.mechanics.Mechanics;
import dev.wuason.mechanics.library.classpath.ClassLoaderInjector;
import dev.wuason.mechanics.library.classpath.MechanicClassLoader;
import dev.wuason.mechanics.library.repositories.Repos;
import dev.wuason.mechanics.library.repositories.Repository;
import dev.wuason.mechanics.mechanics.MechanicAddon;

import java.io.*;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Consumer;

public class DependencyManager {

    private final MechanicAddon core;
    private final ArrayList<DependencyResolved> dependenciesResolved = new ArrayList<>();
    private final URLClassLoader classLoader;
    private final File dependenciesFolder = new File("libraries");
    private final ClassLoaderInjector injector;
    private final ArrayList<Repository> repositories = new ArrayList<>();
    private final Map<String, Dependency> dependencies = new HashMap<>();
    private final List<Consumer<DependencyResolved>> onResolve = new ArrayList<>();
    private JarRemapperHelper jarRemapperHelper;

    public static DependencyManager create(MechanicAddon core, URLClassLoader classLoader) {
        return new DependencyManager(core, classLoader, JarRemapperHelper.REMAPPER);
    }

    public static DependencyManager create(MechanicAddon core, URLClassLoader classLoader, boolean loadDefaultRemapper) {
        return new DependencyManager(core, classLoader, loadDefaultRemapper ? JarRemapperHelper.REMAPPER : null);
    }

    public static DependencyManager createWithCustomLoader(MechanicAddon core, ClassLoader classLoader) {
        return new DependencyManager(core, new MechanicClassLoader(core, core.getClass().getClassLoader(), new java.net.URL[0]), null);
    }

    public static DependencyManager createWithCustomLoader(MechanicAddon core) {
        return new DependencyManager(core, new MechanicClassLoader(core, core.getClass().getClassLoader(), new java.net.URL[0]), null);
    }

    public static DependencyManager createWithAisledLoader(MechanicAddon core) {
        return new DependencyManager(core, new MechanicClassLoader(core), null);
    }

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

    public void loadDefaultRemapper() {
        this.jarRemapperHelper = JarRemapperHelper.REMAPPER;
    }

    public void addRepository(Repository repository) {
        if (!repositories.contains(repository)) repositories.add(repository);
    }

    public void addDependency(Dependency dependency) {
        if (!dependencies.containsKey(dependency.toString())) {
            dependencies.put(dependency.toString(), dependency);
        }
    }

    public void addDependencies(List<Dependency> dependencies) {
        for (Dependency dependency : dependencies) {
            addDependency(dependency);
        }
    }

    public void addRepositories(List<Repository> repositories) {
        for (Repository repository : repositories) {
            addRepository(repository);
        }
    }

    private File getJarFileLocally(Dependency dependency) {
        return new File(this.dependenciesFolder, String.join("/", dependency.getGroupPath()) + "/" + dependency.getArtifactId() + "/" + dependency.getVersion() + "/" + dependency.getJarName());
    }

    public Class<?> inject(Class<?> clazz, ClassLoader classLoader) {
        if (classLoader instanceof MechanicClassLoader) return injector.injectClassFromOtherLoader(clazz, classLoader);
        return clazz;
    }

    public Class<?> inject(Class<?> clazz) {
        if (!(this.classLoader instanceof MechanicClassLoader)) return clazz;
        return injector.injectClassFromOtherLoader(clazz, clazz.getClassLoader());
    }

    public void setJarRemapper(JarRemapperHelper jarRemapperHelper) {
        this.jarRemapperHelper = jarRemapperHelper;
    }

    public void loadDefaultRepositories() {
        addRepositories(Arrays.asList(Repos.MAVEN_CENTRAL, Repos.MAVEN_CENTRAL_MIRROR, Repos.MAVEN_CENTRAL_MIRROR2));
    }

    public HashMap<Class<?>, Class<?>> inject(Class<?>... classes) {
        HashMap<Class<?>, Class<?>> classMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            classMap.put(clazz, inject(clazz));
        }
        return classMap;
    }

    public void injectAll() {
        for (DependencyResolved dependency : dependenciesResolved) {
            inject(dependency);
        }
    }

    public void inject(DependencyResolved dependency) {
        if (dependency != null) {
            injector.injectJar(dependency.getJarFile());
        }
    }

    public void inject(List<DependencyResolved> dependencies) {
        for (DependencyResolved dependency : dependencies) {
            inject(dependency);
        }
    }

    public List<DependencyResolved> resolve() {
        //resolve dependencies
        List<DependencyResolved> resolved = new ArrayList<>();
        for (Dependency dependency : dependencies.values()) {
            List<DependencyResolved> dependencyResolved = resolve(dependency);
            resolved.addAll(dependencyResolved);
        }
        return resolved;
    }

    private void onResolve(DependencyResolved dependency) {
        for (Consumer<DependencyResolved> consumer : onResolve) {
            consumer.accept(dependency);
        }
    }

    public List<DependencyResolved> resolve(Dependency... dependency) {
        return resolve(false, dependency);
    }

    public List<DependencyResolved> resolve(boolean isToIgnore, Dependency... dependency) {
        return resolve(isToIgnore, Arrays.stream(dependency).map(Dependency::getArtifactId).toArray(String[]::new));
    }

    public List<DependencyResolved> resolve(String... regex) {
        return resolve(false, regex);
    }

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

    private List<DependencyResolved> resolve(Dependency dependency) {
        if (dependenciesResolved.contains(dependency)) {
            return dependenciesResolved.stream().filter(dependencyResolved -> dependencyResolved.equals(dependency)).findFirst().map(Collections::singletonList).orElse(Collections.emptyList());
        }
        /*if (dependency.useAdvancedDependencyResolver()) {
            List<DependencyResolved> resolved = advancedDependencyResolver.resolveDependency(dependency);
            dependenciesResolved.addAll(resolved);
            resolved.forEach(this::onResolve);
            System.out.println("Resolved " + resolved.size() + " dependencies in " + dependency.getArtifactId());
            return Collections.unmodifiableList(resolved);
        }*/
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
                try {
                    InputStream inputStream = repository.downloadDependency(dependency, Repository.DownloadType.JAR);
                    if (inputStream == null) {
                        continue;
                    }
                    File file = saveDependency(inputStream, Repository.DownloadType.JAR, dependency);
                    resolved = new DependencyResolved(dependency, file, this, System.currentTimeMillis() - start);
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


    public MechanicAddon getCore() {
        return core;
    }

    public ArrayList<DependencyResolved> getDependenciesResolved() {
        return dependenciesResolved;
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }

    public File getDependenciesFolder() {
        return dependenciesFolder;
    }

    public ClassLoaderInjector getInjector() {
        return injector;
    }

    public ArrayList<Repository> getRepositories() {
        return repositories;
    }

    public Map<String, Dependency> getDependencies() {
        return dependencies;
    }

    public JarRemapperHelper getJarRemapper() {
        return jarRemapperHelper;
    }

    public void setDependenciesFolder(File dependenciesFolder) {
        dependenciesFolder.mkdirs();
    }

    public void addOnResolve(Consumer<DependencyResolved> onResolve) {
        this.onResolve.add(onResolve);
    }

    public void close() {
        try {
            classLoader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isResolved(Dependency dependency) {
        return dependenciesResolved.stream().anyMatch(dependencyResolved -> dependencyResolved.equals(dependency));
    }

    /*public Dependency getDependency(Artifact artifact) {
        return dependencies.get(Dependency.toMavenString(artifact));
    }*/

    public Dependency getDependency(String artifact) {
        return dependencies.get(artifact);
    }

    public Dependency getDependency(String groupId, String artifactId, String version) {
        return dependencies.get(Dependency.toMavenString(groupId, artifactId, version));
    }

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
