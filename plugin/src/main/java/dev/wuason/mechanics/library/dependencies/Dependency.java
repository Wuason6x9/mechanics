package dev.wuason.mechanics.library.dependencies;

import java.io.File;
import java.util.HashMap;

public class Dependency {
    private final String name;
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Remap[] remaps;
    private final File jarFile;
    private final boolean useAdvancedDependencyResolver;

    public Dependency(String name, String groupId, String artifactId, String version, Remap... remaps) {
        this(name, groupId, artifactId, version, remaps, null, false);
    }

    public Dependency(String name, String groupId, String artifactId, String version, boolean useAdvancedDependencyResolver, Remap... remaps) {
        this(name, groupId, artifactId, version, remaps, null, useAdvancedDependencyResolver);
    }

    public Dependency(String groupId, String artifactId, String version, Remap... remaps) {
        this(artifactId, groupId, artifactId, version, remaps);
    }

    public Dependency(String groupId, String artifactId, String version, boolean useAdvancedDependencyResolver, Remap... remaps) {
        this(artifactId, groupId, artifactId, version, useAdvancedDependencyResolver, remaps);
    }

    public Dependency(File jar) {
        this(jar.getName(), "", jar.getName().split("-")[0], jar.getName().split("-")[1].replace(".jar", ""), new Remap[0], jar, false);
    }

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

    public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;

    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public Remap[] getRemaps() {
        return remaps;
    }

    public HashMap<String, String> getRemapsMap() {
        HashMap<String, String> map = new HashMap<>();
        for (Remap remap : remaps) {
            map.put(remap.getOld(), remap.getNew());
        }
        return map;
    }

    public String getJarName() {
        return artifactId + "-" + version + ".jar";
    }

    public String[] getGroupPath() {
        return groupId.split("\\.");
    }

    public File getJarFile() {
        return jarFile;
    }

    public boolean useAdvancedDependencyResolver() {
        return useAdvancedDependencyResolver;
    }

    @Override
    public String toString() {
        return toMavenString(groupId, artifactId, version);
    }

    public static String toMavenString(String groupId, String artifactId, String version) {
        return groupId + ":" + artifactId + ":" + version;
    }

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
