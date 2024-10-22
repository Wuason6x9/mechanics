package dev.wuason.mechanics.library.dependencies;

import dev.wuason.mechanics.library.repositories.Checksum;

import java.io.File;
import java.util.Arrays;

/**
 * Represents a resolved dependency including metadata about its resolution and remapping.
 */
public class DependencyResolved {
    /**
     * Represents a `Dependency` that the `DependencyResolved` class relies on.
     * This reference to the `Dependency` object is used to track the specific dependency
     * that has been resolved.
     */
    private final Dependency dependency;
    /**
     * Represents the file associated with a resolved dependency.
     * This file is usually the artifact that has been resolved and downloaded.
     */
    private final File file;
    /**
     * Manages the dependencies required for the functioning of the current dependency resolution process.
     */
    private final DependencyManager manager;
    /**
     * The time it took to resolve the dependency, measured in milliseconds.
     */
    private final long resolveTime;
    /**
     * Represents the time taken to remap the dependency, in milliseconds.
     * This value is set during the remapping process of the dependency's JAR file.
     */
    private long remapTime;
    /**
     * Represents the checksum associated with a resolved dependency.
     * The checksum is used to ensure the integrity and validity of the downloaded dependency files.
     * It provides methods to download, calculate, and validate checksums for different types
     * such as SHA512, SHA256, SHA1, and MD5.
     */
    private Checksum checksum;

    /**
     * Constructs a new DependencyResolved instance with the specified dependency, file, manager, and resolve time.
     *
     * @param dependency The dependency that was resolved.
     * @param file The file associated with the resolved dependency.
     * @param manager The dependency manager responsible for resolving the dependency.
     * @param resolveTime The time taken to resolve the dependency.
     */
    public DependencyResolved(Dependency dependency, File file, DependencyManager manager, long resolveTime) {
        this(dependency, file, manager, resolveTime, null);
    }

    /**
     * Constructs a DependencyResolved object with the specified parameters.
     *
     * @param dependency the dependency that has been resolved
     * @param file the file associated with the resolved dependency
     * @param manager the manager responsible for resolving the dependency
     * @param resolveTime the time taken to resolve the dependency
     * @param checksum the checksum of the resolved file
     */
    public DependencyResolved(Dependency dependency, File file, DependencyManager manager, long resolveTime, Checksum checksum) {
        this.dependency = dependency;
        this.file = file;
        this.manager = manager;
        this.resolveTime = resolveTime;
        this.checksum = checksum;

        if(checksum != null) {
            checksum.calculateChecksum(file);
        }
    }

    /**
     * Retrieves the dependency object associated with the instance.
     *
     * @return the Dependency object.
     */
    public Dependency getDependency() {
        return dependency;
    }

    /**
     * Retrieves the resolved file associated with this dependency.
     *
     * @return the file resolved as part of this dependency.
     */
    public File getFile() {
        return file;
    }

    /**
     * Retrieves the remapped JAR file. If the dependency has no remaps, returns the original file.
     * Initializes the default JarRemapperHelper if not already loaded and performs the remapping.
     *
     * @return the remapped JAR file, or the original file if remapping is not required or fails.
     */
    public File getRemappedJar() {
        if (dependency.getRemaps().length == 0) {
            return file;
        }
        long start = System.currentTimeMillis();
        if (manager.getJarRemapper() == null) {
            manager.loadDefaultRemapper();
        }
        JarRemapperHelper jarRemapperHelper = manager.getJarRemapper();
        File newFile = jarRemapperHelper.remap(file, dependency.getRemaps());
        remapTime = System.currentTimeMillis() - start;
        return newFile != null ? newFile : file;
    }

    /**
     * Retrieves the JAR file associated with the dependency. If a checksum
     * is present, it validates the checksum before proceeding. If the dependency
     * has remaps, it returns the remapped JAR file.
     *
     * @return the JAR file associated with the dependency, or a remapped JAR file
     *         if remaps are defined.
     * @throws IllegalStateException if the checksum is invalid for the dependency.
     */
    public File getJarFile() {
        if (checksum != null && !checksum.isChecksumValid()) {
            throw new IllegalStateException("Checksum is not valid for " + dependency + "! Try to redownload the dependency.");
        }
        if (dependency.getRemaps().length > 0) {
            return getRemappedJar();
        }
        return file;
    }

    /**
     * Compares this {@code DependencyResolved} object to the specified object for equality.
     *
     * @param obj the object to be compared for equality with this {@code DependencyResolved}
     * @return {@code true} if the specified object is equal to this {@code DependencyResolved};
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DependencyResolved) {
            return ((DependencyResolved) obj).getDependency().equals(dependency) || (((DependencyResolved) obj).getDependency().getArtifactId().equals(dependency.getArtifactId()) && ((DependencyResolved) obj).getDependency().getVersion().equals(dependency.getVersion()) && ((DependencyResolved) obj).getDependency().getGroupId().equals(dependency.getGroupId()));
        }
        if (obj instanceof Dependency) {
            return ((Dependency) obj).equals(dependency) || (((Dependency) obj).getArtifactId().equals(dependency.getArtifactId()) && ((Dependency) obj).getVersion().equals(dependency.getVersion()) && ((Dependency) obj).getGroupId().equals(dependency.getGroupId()));
        }
        return super.equals(obj);
    }

    /**
     * Returns the time taken to resolve the dependency.
     *
     * @return the resolve time in milliseconds.
     */
    public long getResolveTime() {
        return resolveTime;
    }

    /**
     * Retrieves the remap time in milliseconds.
     *
     * @return The time taken to remap the dependency, in milliseconds.
     */
    public long getRemapTime() {
        return remapTime;
    }

    /**
     * Retrieves the checksum associated with the resolved dependency.
     *
     * @return The checksum instance for the resolved dependency.
     */
    public Checksum getChecksum() {
        return checksum;
    }
}


