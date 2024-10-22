package dev.wuason.mechanics.library.dependencies;

import java.io.*;
import java.util.HashMap;

/**
 * The name of the Dependency, which is a unique identifier used to distinguish
 * this particular dependency from others. This can often be a descriptive name
 * or an alias that makes it easier to reference within the application.
 */
public class Dependency {
    /**
     * The name of the Dependency, which is a unique identifier used to distinguish
     * this particular dependency from others. This can often be a descriptive name
     * or an alias that makes it easier to reference within the application.
     */
    private final String name;
    /**
     * Represents the group identifier of a dependency.
     * It is a string that uniquely identifies the group to which a set of artifacts belong.
     * Examples of group IDs are organization names or domain names in reverse notation.
     * Colons (':') in the string are replaced with dots ('.').
     */
    private final String groupId;
    /**
     * Represents the artifact ID of the dependency.
     * The artifact ID is a unique identifier for a specific version of a component.
     */
    private final String artifactId;
    /**
     * Represents the version of the dependency.
     */
    private final String version;
    /**
     * An array of Remap objects that are tied to a Dependency, representing various remapping
     * situations where artifacts might need to be renamed or relocated for proper dependency management.
     */
    private final Remap[] remaps;
    /**
     * This variable represents the JAR file associated with the dependency.
     * It is a final file object, indicating that the reference cannot be changed
     * once the dependency object is created. The JAR file contains the compiled
     * code and resources needed by the application or other dependencies.
     */
    private final File jarFile;
    /**
     * Indicates whether the advanced dependency resolver should be used.
     * When set to {@code true}, the system will utilize a more complex and potentially more accurate
     * method for resolving dependencies. When set to {@code false}, a standard, less complex
     * dependency resolution method will be employed.
     */
    private final boolean useAdvancedDependencyResolver;

    /**
     * Constructs a Dependency object with the specified name, groupId, artifactId, version, and remaps.
     *
     * @param name the name of the dependency
     * @param groupId the group ID of the dependency
     * @param artifactId the artifact ID of the dependency
     * @param version the version of the dependency
     * @param remaps an array of Remap instances for remapping operations
     */
    public Dependency(String name, String groupId, String artifactId, String version, Remap... remaps) {
        this(name, groupId, artifactId, version, remaps, null, false);
    }

    /**
     * Constructs a Dependency object with the given parameters.
     *
     * @param name the name of the dependency
     * @param groupId the group ID of the dependency
     * @param artifactId the artifact ID of the dependency
     * @param version the version of the dependency
     * @param useAdvancedDependencyResolver indicates whether to use the advanced dependency resolver
     * @param remaps an array of Remap objects representing remapping rules
     */
    public Dependency(String name, String groupId, String artifactId, String version, boolean useAdvancedDependencyResolver, Remap... remaps) {
        this(name, groupId, artifactId, version, remaps, null, useAdvancedDependencyResolver);
    }

    /**
     * Constructs a Dependency instance with the specified groupId, artifactId, version, and remaps.
     *
     * @param groupId the group ID of the dependency
     * @param artifactId the artifact ID of the dependency
     * @param version the version of the dependency
     * @param remaps an array of remap objects representing remapping configurations
     */
    public Dependency(String groupId, String artifactId, String version, Remap... remaps) {
        this(artifactId, groupId, artifactId, version, remaps);
    }

    /**
     * Constructs a Dependency object with the specified group ID, artifact ID, version,
     * a flag to use an advanced dependency resolver, and an array of remaps.
     *
     * @param groupId the group ID of the dependency
     * @param artifactId the artifact ID of the dependency
     * @param version the version of the dependency
     * @param useAdvancedDependencyResolver whether to use the advanced dependency resolver
     * @param remaps an array of {@code Remap} objects for mapping purposes
     */
    public Dependency(String groupId, String artifactId, String version, boolean useAdvancedDependencyResolver, Remap... remaps) {
        this(artifactId, groupId, artifactId, version, useAdvancedDependencyResolver, remaps);
    }

    /**
     * Constructs a Dependency object based on the provided jar file.
     *
     * @param jar the jar file from which to create the Dependency object.
     */
    public Dependency(File jar) {
        this(jar.getName(), "", jar.getName().split("-")[0], jar.getName().split("-")[1].replace(".jar", ""), new Remap[0], jar, false);
    }

    /**
     * Constructs a new Dependency instance with the specified parameters.
     *
     * @param name the name of the dependency
     * @param groupId the group ID of the dependency, colons ':' will be replaced with periods '.'
     * @param artifactId the artifact ID of the dependency
     * @param version the version of the dependency
     * @param remaps an array of Remap objects representing remapping configurations, can be null
     * @param jarFile a File object referencing the jar file of the dependency, can be null
     * @param useAdvancedDependencyResolver a boolean flag indicating whether to use an advanced dependency resolver
     */
    public Dependency(String name, String groupId, String artifactId, String version, Remap[] remaps, File jarFile, boolean useAdvancedDependencyResolver) {
        this.name = name;
        this.groupId = groupId.replace(':', '.');
        this.artifactId = artifactId;
        this.version = version;
        this.useAdvancedDependencyResolver = useAdvancedDependencyResolver;
        if (remaps == null) {
            this.remaps = new Remap[0];
        } else {
            this.remaps = remaps;
        }
        this.jarFile = jarFile;
    }

    /**
     * Retrieves the name associated with this dependency.
     *
     * @return the name of the dependency
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the group ID associated with this dependency.
     *
     * @return the group ID as a String.
     */
    public String getGroupId() {
        return groupId;

    }

    /**
     * Retrieves the artifact identifier of the dependency.
     *
     * @return the artifact identifier.
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Retrieves the version of the dependency.
     *
     * @return the version of the dependency as a String.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Retrieves the array of remap objects associated with this dependency.
     *
     * @return an array of {@code Remap} objects representing the remaps defined for the dependency.
     */
    public Remap[] getRemaps() {
        return remaps;
    }

    /**
     * Constructs and returns a map that represents remappings of old values to new values.
     *
     * @return a HashMap where each key is an old value and each corresponding value
     *         is the new value after remapping.
     */
    public HashMap<String, String> getRemapsMap() {
        HashMap<String, String> map = new HashMap<>();
        for (Remap remap : remaps) {
            map.put(remap.getOld(), remap.getNew());
        }
        return map;
    }

    /**
     * Constructs the name of the JAR file using the artifactId and version.
     *
     * @return the constructed JAR file name in the format of artifactId-version.jar
     */
    public String getJarName() {
        return artifactId + "-" + version + ".jar";
    }

    /**
     * Splits the groupId field by periods and returns an array of strings where each element is a part of the groupId.
     *
     * @return an array of strings representing the segments of the groupId
     */
    public String[] getGroupPath() {
        return groupId.split("\\.");
    }

    /**
     * Returns the JAR file associated with this dependency.
     *
     * @return the JAR file associated with this dependency, or null if no JAR file is set.
     */
    public File getJarFile() {
        return jarFile;
    }

    /**
     * Indicates whether the advanced dependency resolver is being used.
     *
     * @return true if the advanced dependency resolver is enabled, false otherwise
     */
    public boolean useAdvancedDependencyResolver() {
        return useAdvancedDependencyResolver;
    }

    /**
     * Generates a string representation of the Dependency object in Maven format.
     *
     * @return A string in the format "groupId:artifactId:version" representing the Dependency.
     */
    @Override
    public String toString() {
        return toMavenString(groupId, artifactId, version);
    }

    /**
     * Combines the given group ID, artifact ID, and version into a Maven-style string.
     *
     * @param groupId the group ID of the dependency
     * @param artifactId the artifact ID of the dependency
     * @param version the version of the dependency
     * @return a string in the format "groupId:artifactId:version"
     */
    public static String toMavenString(String groupId, String artifactId, String version) {
        return groupId + ":" + artifactId + ":" + version;
    }

    /**
     * Compares this {@code Dependency} object to the specified object for equality.
     *
     * @param obj the object to be compared for equality with this {@code Dependency}
     * @return {@code true} if the specified object is equal to this {@code Dependency};
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DependencyResolved) {
            return ((DependencyResolved) obj).getDependency().equals(this);
        }
        if (obj instanceof Dependency) {
            return ((Dependency) obj).getArtifactId().equals(artifactId) && ((Dependency) obj).getVersion().equals(version) && ((Dependency) obj).getGroupId().equals(groupId);
        }
        return super.equals(obj);
    }
}
