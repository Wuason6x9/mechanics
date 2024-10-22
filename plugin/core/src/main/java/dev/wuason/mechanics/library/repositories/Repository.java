package dev.wuason.mechanics.library.repositories;

import dev.wuason.mechanics.library.dependencies.Dependency;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Repository {

    /**
     * The name of the repository.
     */
    private final String name;
    /**
     * The URL of the repository from which dependencies are to be downloaded.
     */
    private final String url;

    /**
     * Constructs a new Repository with the specified name and URL.
     *
     * @param name the name of the repository
     * @param url the URL of the repository
     */
    public Repository(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Downloads a dependency from the specified repository.
     *
     * @param dependency The dependency to download, consisting of groupId, artifactId, and version.
     * @param type The type of the file to download, such as JAR or POM.
     * @param checksumType The type of checksum to use for verifying the downloaded file, or null if no checksum is required.
     * @return An InputStream to read the content of the downloaded file, or null if the download was unsuccessful.
     * @throws IOException If an I/O error occurs when opening the connection or reading the response.
     */
    public InputStream downloadDependency(Dependency dependency, DownloadType type, Checksum.ChecksumType checksumType) throws IOException {
        String jarUrl = jarUrl = getUrl() + dependency.getGroupId().replace(".", "/") + "/" + dependency.getArtifactId() + "/" + dependency.getVersion() + "/" + dependency.getArtifactId() + "-" + dependency.getVersion() + "." + type.name().toLowerCase() + (checksumType != null ? "." + checksumType.name().toLowerCase() : "");
        URL url = new URL(jarUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode() == 200 ? connection.getInputStream() : null;
    }

    /**
     * Downloads a dependency from the repository.
     *
     * @param dependency the dependency to be downloaded
     * @param type the type of download (e.g., JAR, POM)
     * @return an InputStream to read the downloaded dependency content
     * @throws IOException if an I/O error occurs during the download
     */
    public InputStream downloadDependency(Dependency dependency, DownloadType type) throws IOException {
        return downloadDependency(dependency, type, null);
    }

    /**
     * Downloads a dependency file of type JAR from the repository and stores it in a temporary file.
     *
     * @param dependency the dependency to download, containing details like groupId, artifactId, and version
     * @param type the type of file to download (e.g., JAR)
     * @return the temporary file where the downloaded dependency is stored
     * @throws IOException if an I/O error occurs during the download or file creation process
     */
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

    /**
     * Measures the time taken to establish an HTTP connection to the URL associated with this repository.
     *
     * @return The time taken (in milliseconds) to establish an HTTP connection, or -1 if an IOException occurs
     */
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

    /**
     * Retrieves the name of the repository.
     *
     * @return the name of the repository.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the URL of the repository. Ensures that the URL
     * ends with a forward slash ("/").
     *
     * @return the URL of the repository, guaranteed to end with a "/"
     */
    public String getUrl() {
        if (url.endsWith("/")) {
            return url;
        } else {
            return url + "/";
        }
    }
}
