package dev.wuason.mechanics.library.dependencies;

import dev.wuason.mechanics.library.repositories.Checksum;

import java.io.File;
import java.util.Arrays;

public class DependencyResolved {
    private final Dependency dependency;
    private final File file;
    private final DependencyManager manager;
    private final long resolveTime;
    private long remapTime;
    private Checksum checksum;

    public DependencyResolved(Dependency dependency, File file, DependencyManager manager, long resolveTime) {
        this(dependency, file, manager, resolveTime, null);
    }

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

    public Dependency getDependency() {
        return dependency;
    }

    public File getFile() {
        return file;
    }

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

    public File getJarFile() {
        if (checksum != null && !checksum.isChecksumValid()) {
            throw new IllegalStateException("Checksum is not valid for " + dependency + "! Try to redownload the dependency.");
        }
        if (dependency.getRemaps().length > 0) {
            return getRemappedJar();
        }
        return file;
    }

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

    public long getResolveTime() {
        return resolveTime;
    }

    public long getRemapTime() {
        return remapTime;
    }

    public Checksum getChecksum() {
        return checksum;
    }
}


