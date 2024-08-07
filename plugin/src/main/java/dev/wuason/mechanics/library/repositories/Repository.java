package dev.wuason.mechanics.library.repositories;

import dev.wuason.mechanics.library.dependencies.Dependency;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public record Repository(String name, String url) {

    public InputStream downloadDependency(Dependency dependency, DownloadType type) throws IOException {
        String jarUrl = jarUrl = url() + dependency.getGroupId().replace(".", "/") + "/" + dependency.getArtifactId() + "/" + dependency.getVersion() + "/" + dependency.getArtifactId() + "-" + dependency.getVersion() + "." + type.name().toLowerCase();
        URL url = new URL(jarUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getInputStream();
    }

    public File downloadDependencyFileJar(Dependency dependency, DownloadType type) throws IOException {
        BufferedInputStream in = new BufferedInputStream(downloadDependency(dependency, type));
        File file = File.createTempFile(dependency.getArtifactId() + "-" + dependency.getVersion(), "." + type.name().toLowerCase());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();
        in.close();
        return file;
    }


    @Override
    public String url() {
        if (url.endsWith("/")) {
            return url;
        } else {
            return url + "/";
        }
    }

    public enum DownloadType {
        JAR,
        POM
    }
}
