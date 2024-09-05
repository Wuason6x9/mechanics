package dev.wuason.mechanics.library.repositories;

import dev.wuason.mechanics.library.dependencies.Dependency;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Repository {

    //private final RemoteRepository repository;
    private final String name;
    private final String url;

    public Repository(String name, String url) {
        this.name = name;
        this.url = url;
        //this.repository = new RemoteRepository.Builder(name, "default", url).setPolicy(new RepositoryPolicy(true, RepositoryPolicy.UPDATE_POLICY_ALWAYS, RepositoryPolicy.CHECKSUM_POLICY_FAIL)).build();
    }

    public InputStream downloadDependency(Dependency dependency, DownloadType type, Checksum.ChecksumType checksumType) throws IOException {
        String jarUrl = jarUrl = getUrl() + dependency.getGroupId().replace(".", "/") + "/" + dependency.getArtifactId() + "/" + dependency.getVersion() + "/" + dependency.getArtifactId() + "-" + dependency.getVersion() + "." + type.name().toLowerCase() + (checksumType != null ? "." + checksumType.name().toLowerCase() : "");
        URL url = new URL(jarUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode() == 200 ? connection.getInputStream() : null;
    }

    public InputStream downloadDependency(Dependency dependency, DownloadType type) throws IOException {
        return downloadDependency(dependency, type, null);
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

    public long ping() {
        long start = System.currentTimeMillis();
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return System.currentTimeMillis() - start;
        } catch (IOException e) {
            return -1;
        }
    }

    public enum DownloadType {
        JAR,
        POM
    }

    /*public RemoteRepository getRemoteRepository() {
        return repository;
    }*/

    public String getName() {
        return name;
    }

    public String getUrl() {
        if (url.endsWith("/")) {
            return url;
        } else {
            return url + "/";
        }
    }
}
