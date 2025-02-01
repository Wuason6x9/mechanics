package dev.wuason.mechanics.library.repositories;

public class Repos {

    public static final Repository MAVEN_CENTRAL = new Repository("Maven Central", "https://repo.maven.apache.org/maven2/");
    public static final Repository MAVEN_CENTRAL_MIRROR = new Repository("Maven Central Mirror", "https://maven-central.storage.googleapis.com/maven2/");
    public static final Repository MAVEN_CENTRAL_MIRROR2 = new Repository("Maven Central Mirror 2", "https://repo1.maven.org/maven2/");

    public static final Repository JITPACK = new Repository("Jitpack", "https://jitpack.io/");
    public static final Repository INVESDWIN = new Repository("Invesdwin", "https://invesdwin.de/repo/invesdwin-oss/");
    public static final Repository CODEMC = new Repository("CodeMC", "https://repo.codemc.io/repository/maven-public/");
    public static final Repository NEXO_RELEASES = new Repository("CodeMC", "https://repo.nexomc.com/releases");
    public static final Repository ORAXEN_RELEASES = new Repository("CodeMC", "https://repo.oraxen.com/releases");
}
