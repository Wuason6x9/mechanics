package dev.wuason.mechanics.library.dependencies;

import dev.wuason.mechanics.library.repositories.Repository;

import java.io.*;
import java.util.HashMap;

public class Dependency {
    private final String name;
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Remap[] remaps;
    private final File jarFile;

    public Dependency(String name, String groupId, String artifactId, String version, Remap... remaps) {
        this.name = name;
        this.groupId = groupId.replace(':', '.');
        this.artifactId = artifactId;
        this.version = version;
        if (remaps == null) {
            this.remaps = new Remap[0];
        } else {
            this.remaps = remaps;
        }
        this.jarFile = null;
    }

    public Dependency(String groupId, String artifactId, String version, Remap... remaps) {
        this(artifactId, groupId, artifactId, version, remaps);
    }

    public Dependency(File jar) {
        this.jarFile = jar;
        this.name = jar.getName();
        this.groupId = "";
        this.artifactId = jar.getName().split("-")[0];
        this.version = jar.getName().split("-")[1].replace(".jar", "");
        this.remaps = new Remap[0];
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

    /*public File getJarFile() throws IOException {
        File jar = getJarFileLocally();
        if (jar.exists()) {
            return jar;
        }
        return download();
    }

    public File getJarFileLocally() {
        if (jarFile != null) return jarFile;
        return new File(LIBRARY_FOLDER, String.join("/", getGroupPath()) + "/" + artifactId + "/" + version + "/" + getJarName());
    }

    public File download() throws IOException {
        File jar = getJarFileLocally();
        if (!jar.exists()) {
            jar.getParentFile().mkdirs();
            jar.createNewFile();
        }
        BufferedInputStream in = new BufferedInputStream(repo.downloadDependency(this));
        FileOutputStream fileOutputStream = new FileOutputStream(jar);
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();
        in.close();
        return jar;
    }*/


}
